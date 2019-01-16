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
package net.sourceforge.plantuml.activitydiagram3;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.Rainbow;

public class LinkRendering {

	private final Rainbow rainbow;
	private final Display display;

	public LinkRendering(Rainbow rainbow) {
		this(rainbow, Display.NULL);
	}

	private LinkRendering(Rainbow rainbow, Display display) {
		if (rainbow == null) {
			throw new IllegalArgumentException();
		}
		this.rainbow = rainbow;
		this.display = display;
	}

	public static LinkRendering none() {
		return new LinkRendering(Rainbow.none());
	}

	public LinkRendering withDisplay(Display display) {
		return new LinkRendering(rainbow, display);
	}

	public Display getDisplay() {
		return display;
	}

	public Rainbow getRainbow() {
		return rainbow;
	}

	public Rainbow getRainbow(Rainbow defaultColor) {
		if (rainbow.size() == 0) {
			return defaultColor;
		}
		return rainbow;
	}

	public boolean isNone() {
		return Display.isNull(display) && rainbow.size() == 0;
	}

	@Override
	public String toString() {
		return super.toString() + " " + display + " " + rainbow;
	}
}
