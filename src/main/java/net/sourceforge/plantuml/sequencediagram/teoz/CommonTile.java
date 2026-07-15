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

	public CommonTile(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	// Single extension point: each tile that needs to react once its own
	// YGauge is solved overrides this directly (livebox steps, goCreate/
	// goDestroy, forwarding to a wrapped inner tile, ...). Default is a no-op.
	// Replaces the former two-level callbackY/callbackY_internal indirection
	// built around a TimeHook value that every override now reads straight
	// off getYGauge().getMin().getCurrentValue() instead.
	public void onGaugeResolved() {
	}

	protected final StringBounder getStringBounder() {
		return stringBounder;
	}

	public double getMiddleX() {
		final double max = getMaxX().getCurrentValue();
		final double min = getMinX().getCurrentValue();
		return (min + max) / 2;
	}

}
