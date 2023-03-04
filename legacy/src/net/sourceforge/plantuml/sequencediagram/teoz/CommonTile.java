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
		if (YGauge.USE_ME) {
		} else {
			this.y = y;
			callbackY_internal(y);
		}
	}

	protected void callbackY_internal(TimeHook y) {
		if (YGauge.USE_ME) {
			System.err.println("callbackY_internal::y=" + y + " gauge=" + getYGauge() + " " + getClass());
		}
	}

	protected final StringBounder getStringBounder() {
		return stringBounder;
	}

	final public double getMiddleX() {
		final double max = getMaxX().getCurrentValue();
		final double min = getMinX().getCurrentValue();
		return (min + max) / 2;
	}

	public final TimeHook getTimeHook() {
		if (YGauge.USE_ME) {
			throw new IllegalStateException();
		}
		return y;
	}

}
