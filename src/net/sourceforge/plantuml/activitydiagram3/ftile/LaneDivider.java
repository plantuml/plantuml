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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class LaneDivider extends AbstractTextBlock {

	private final ISkinParam skinParam;;

	private final double x1;
	private final double x2;
	private final double height;
	private Style style;

	public LaneDivider(ISkinParam skinParam, double x1, double x2, double height) {
		this.skinParam = skinParam;
		this.x1 = x1;
		this.x2 = x2;
		this.height = height;
	}

	public StyleSignature getDefaultStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.classDiagram, SName.swimlane);
	}

	private Style getStyle() {
		if (style == null) {
			this.style = getDefaultStyleDefinition().getMergedStyle(skinParam.getCurrentStyleBuilder());
		}
		return style;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(x1 + x2, height);
	}

	public void drawU(UGraphic ug) {
//		final UShape back = new URectangle(x1 + x2, height).ignoreForCompressionOnY();
//		ug.apply(UChangeColor.nnn(HColorUtils.BLUE)).draw(back);
		final UShape back = new UEmpty(x1 + x2, 1);
		ug.draw(back);

		HColor color = skinParam.getHtmlColor(ColorParam.swimlaneBorder, null, false);
		if (color == null) {
			color = ColorParam.swimlaneBorder.getDefaultValue();
		}
		UStroke thickness = Rose.getStroke(skinParam, LineParam.swimlaneBorder, 2);
		if (SkinParam.USE_STYLES()) {
			color = getStyle().value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
			thickness = getStyle().getStroke();
		}
		ug.apply(UTranslate.dx(x1)).apply(thickness).apply(color).draw(ULine.vline(height));

	}

	public double getWidth() {
		return x1 + x2;
	}

	public final double getX1() {
		return x1;
	}

	public final double getX2() {
		return x2;
	}

}
