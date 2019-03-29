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
 */
package net.sourceforge.plantuml.timingdiagram;

import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public interface Player extends TimeProjected {

	public void addNote(TimeTick now, Display note, Position position);

	public void defineState(String stateCode, String label);

	public void setState(TimeTick now, String comment, Colors color, String... states);

	public void createConstraint(TimeTick tick1, TimeTick tick2, String message);

	public void drawFrameTitle(UGraphic ug);

	public void drawContent(UGraphic ug);

	public void drawLeftHeader(UGraphic ug);

	public double getWidthHeader(StringBounder stringBounder);

	public double getHeight(StringBounder stringBounder);
	
	public double getFirstColumnWidth(StringBounder stringBounder);


}
