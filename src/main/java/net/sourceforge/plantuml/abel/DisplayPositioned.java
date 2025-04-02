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
package net.sourceforge.plantuml.abel;

import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.utils.LineLocation;

public class DisplayPositioned extends DisplayPositionned {

	private final Display display;
	private final HorizontalAlignment horizontalAlignment;
	private final VerticalAlignment verticalAlignment;
	private final LineLocation location;

	private DisplayPositioned(LineLocation location, Display display, HorizontalAlignment horizontalAlignment,
			VerticalAlignment verticalAlignment) {
		this.location = location;
		this.display = display;
		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;
	}

	public DisplayPositioned withPage(int page, int lastpage) {
		final Display newDisplay = display.withPage(page, lastpage);
		return new DisplayPositioned(location, newDisplay, horizontalAlignment, verticalAlignment);
	}

	public DisplayPositioned withDisplay(Display display) {
		return new DisplayPositioned(location, display, horizontalAlignment, verticalAlignment);
	}

	public DisplayPositioned withHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		return new DisplayPositioned(location, display, horizontalAlignment, verticalAlignment);
	}

	public DisplayPositioned withLocation(LineLocation location) {
		return new DisplayPositioned(location, display, horizontalAlignment, verticalAlignment);
	}

	public static DisplayPositioned single(Display display, HorizontalAlignment horizontalAlignment,
			VerticalAlignment verticalAlignment) {
		return new DisplayPositioned(null, display, horizontalAlignment, verticalAlignment);
	}

	public static DisplayPositioned single(LineLocation location, Display display,
			HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		return new DisplayPositioned(location, display, horizontalAlignment, verticalAlignment);
	}

	public static DisplayPositioned none(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		return new DisplayPositioned(null, Display.NULL, horizontalAlignment, verticalAlignment);
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

	public boolean isNull() {
		return Display.isNull(display);
	}

	public boolean hasUrl() {
		return display.hasUrl();
	}

	public LineLocation getLineLocation() {
		return location;
	}

	public TextBlock createRibbon(FontConfiguration fontConfiguration, ISkinSimple spriteContainer, Style style) {
		final Display display = getDisplay();
		if (Display.isNull(display) || display.size() == 0)
			return null;

		if (style != null)
			return style.createTextBlockBordered(display, spriteContainer.getIHtmlColorSet(), spriteContainer, null,
					LineBreakStrategy.NONE);

		return display.create(fontConfiguration, getHorizontalAlignment(), spriteContainer);
	}

}
