/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.UDrawable;

public abstract class CommonTile implements Tile, UDrawable {

	private final StringBounder stringBounder;
	private TimeHook y = new TimeHook(-1);

	public CommonTile(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	final public void callbackY(TimeHook y) {
		// Under YGauge.USE_ME, the TimeHook accumulated by the legacy pass
		// (GroupingTile.fillPositionelTiles) is ignored: each tile substitutes
		// the min of its own YGauge, already solved by the Real engine. The
		// traversal itself is kept, so the ordering and all the side effects
		// of callbackY_internal (livebox steps, goCreate/goDestroy, ...) are
		// reused unchanged; only the source of the y value differs. This also
		// makes the note wrappers work for free: their callbackY_internal
		// forwards to the inner tile, which substitutes its own gauge.
		if (YGauge.USE_ME)
			y = new TimeHook(getYGauge().getMin().getCurrentValue());
		this.y = y;
		callbackY_internal(y);
	}

	protected void callbackY_internal(TimeHook y) {
	}

	protected final StringBounder getStringBounder() {
		return stringBounder;
	}

	public double getMiddleX() {
		final double max = getMaxX().getCurrentValue();
		final double min = getMinX().getCurrentValue();
		return (min + max) / 2;
	}

	public final TimeHook getTimeHook() {
		// Under YGauge.USE_ME this returns the gauge min captured by
		// callbackY: legacy consumers (Blotter, yNewPages, ...) keep working
		// with gauge-consistent values until they are migrated, after which
		// TimeHook can be deleted entirely.
		return y;
	}

}
