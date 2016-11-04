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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalAlignment;

public class DisplayPositionned {

	private final Display display;
	private final HorizontalAlignment horizontalAlignment;
	private final VerticalAlignment verticalAlignment;

	public DisplayPositionned(Display display, HorizontalAlignment horizontalAlignment,
			VerticalAlignment verticalAlignment) {
		this.display = display;
		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;
	}

	public static DisplayPositionned none(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		return new DisplayPositionned(Display.NULL, horizontalAlignment, verticalAlignment);
	}

	public final Display getDisplay() {
		return display;
	}

	public final HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public final VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public static boolean isNull(DisplayPositionned data) {
		return data == null || Display.isNull(data.display);
	}

	public boolean hasUrl() {
		return display.hasUrl();
	}

}
