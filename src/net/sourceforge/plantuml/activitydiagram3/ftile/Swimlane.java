/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Swimlane implements SpecificBackcolorable {

	private final String name;
	private Display display;

	private UTranslate translate = new UTranslate();
	private double totalWidth;

	public Swimlane(String name) {
		this.name = name;
		this.display = Display.getWithNewlines(name);

	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display label) {
		this.display = label;
	}

	public final UTranslate getTranslate() {
		return translate;
	}

	public final void setTranslateAndWidth(UTranslate translate, double totalWidth) {
		this.translate = translate;
		this.totalWidth = totalWidth;
	}

	public Colors getColors(ISkinParam skinParam) {
		return colors;
	}

	public void setSpecificColorTOBEREMOVED(ColorType type, HtmlColor color) {
		if (color != null) {
			this.colors = colors.add(type, color);
		}
	}

	private Colors colors = Colors.empty();

	public final double getTotalWidth() {
		return totalWidth;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

}
