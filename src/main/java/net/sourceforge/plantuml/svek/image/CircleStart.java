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
package net.sourceforge.plantuml.svek.image;

import net.sourceforge.plantuml.annotation.Fast;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class CircleStart implements TextBlock {

	private static final int SIZE = 20;

	// UML 2.5.1 draws the junction pseudostate as a filled circle, smaller than the one
	// used for the initial pseudostate. The spec does not give an exact ratio, so this
	// keeps it clearly smaller while still an easy target to read and to click on.
	private static final int JUNCTION_SIZE = 12;

	private final ISkinParam skinParam;
	private final Style style;
	private final Colors colors;
	private final int size;

	public CircleStart(ISkinParam skinParam, Style style, Colors colors) {
		this(skinParam, style, colors, false);
	}

	public CircleStart(ISkinParam skinParam, Style style, Colors colors, boolean junction) {
		this.style = style;
		this.colors = colors;
		this.skinParam = skinParam;
		this.size = junction ? JUNCTION_SIZE : SIZE;
	}

	@Fast
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(size, size);
	}

	final public void drawU(UGraphic ug) {
		final UEllipse circle = UEllipse.build(size, size);

		final HColor backColor = colors.getColor(style, PName.BackGroundColor, skinParam.getIHtmlColorSet());
		final HColor lineColor = colors.getColor(style, PName.LineColor, skinParam.getIHtmlColorSet());
//		if (colors.getColor(ColorType.BACK) != null) {
//			lineColor = colors.getColor(ColorType.BACK);
//			backColor = colors.getColor(ColorType.BACK);
//		}

		final double shadowing = style.getShadowing();

		circle.setDeltaShadow(shadowing);
		ug.apply(lineColor).apply(backColor.bg()).apply(style.getStroke()).draw(circle);
	}

}
