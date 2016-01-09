/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class LinkRendering {

	private final HtmlColor color;
	private Display display = Display.NULL;

	public LinkRendering(HtmlColor color) {
		this.color = color;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public Display getDisplay() {
		return display;
	}

	public HtmlColor getColor() {
		return color;
	}

	@Override
	public String toString() {
		return super.toString() + " " + display + " " + color;
	}

	public static HtmlColor getColor(LinkRendering inLinkRendering, HtmlColor defaultColor) {
		if (inLinkRendering == null || inLinkRendering.getColor() == null) {
			return defaultColor;
		}
		return inLinkRendering.getColor();
	}

	public static HtmlColor getColor(HtmlColor col, HtmlColor defaultColor) {
		if (col == null) {
			return defaultColor;
		}
		return col;
	}

	public static Display getDisplay(LinkRendering linkRendering) {
		if (linkRendering == null) {
			return Display.NULL;
		}
		return linkRendering.getDisplay();
	}

}
