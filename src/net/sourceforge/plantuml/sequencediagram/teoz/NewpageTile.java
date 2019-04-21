/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class NewpageTile extends AbstractTile implements TileWithCallbackY {

	private final Newpage newpage;
	private final TileArguments tileArguments;

	@Override
	public double getYPoint(StringBounder stringBounder) {
		return 0;
	}

	public NewpageTile(Newpage newpage, TileArguments tileArguments) {
		this.newpage = newpage;
		this.tileArguments = tileArguments;
	}

	public void drawU(UGraphic ug) {
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return 0;
	}

	public void addConstraints(StringBounder stringBounder) {
	}

	public Real getMinX(StringBounder stringBounder) {
		return tileArguments.getOrigin();
	}

	public Real getMaxX(StringBounder stringBounder) {
		return tileArguments.getOrigin();
	}

	private double y;

	public void callbackY(double y) {
		this.y = y;
	}

	public double getCallbackY() {
		return y;
	}

	public Event getEvent() {
		return newpage;
	}

}
