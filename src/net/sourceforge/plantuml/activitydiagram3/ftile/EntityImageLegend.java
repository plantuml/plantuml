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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class EntityImageLegend extends AbstractTextBlock implements TextBlock {

	private final int cornersize = 10;
	private final HtmlColor legendBackgroundColor;
	private final HtmlColor legendColor;
	private final int marginX = 6;
	private final int marginY = 5;
	private final boolean withShadow;
	private final ISkinParam skinParam;

	private final TextBlock textBlock;

	private EntityImageLegend(Display note, ISkinParam skinParam) {
		this.withShadow = false;
		final Rose rose = new Rose();
		this.skinParam = skinParam;

		legendBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.legendBackground);
		legendColor = rose.getHtmlColor(skinParam, ColorParam.legendBorder);

		this.textBlock = note.create(new FontConfiguration(skinParam, FontParam.LEGEND, null),
				HorizontalAlignment.LEFT, skinParam);
	}

	public static TextBlock create(Display legend, ISkinParam skinParam) {
		return TextBlockUtils.withMargin(new EntityImageLegend(legend, skinParam), 8, 8);
	}

	private double getTextHeight(StringBounder stringBounder) {
		final Dimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getHeight() + 2 * marginY;
	}

	private double getPureTextWidth(StringBounder stringBounder) {
		final Dimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getWidth();
	}

	private double getTextWidth(StringBounder stringBounder) {
		return getPureTextWidth(stringBounder) + 2 * marginX;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final double height = getTextHeight(stringBounder);
		final double width = getTextWidth(stringBounder);
		return new Dimension2DDouble(width + 1, height + 1);
	}

	private UGraphic applyStroke(UGraphic ug) {
		final UStroke stroke = skinParam.getThickness(LineParam.legendBorder, null);
		if (stroke == null) {
			return ug;
		}
		return ug.apply(stroke);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final UPolygon polygon = getPolygonNormal(stringBounder);
		if (withShadow) {
			polygon.setDeltaShadow(4);
		}
		ug = ug.apply(new UChangeBackColor(legendBackgroundColor)).apply(new UChangeColor(legendColor));
		ug = applyStroke(ug);
		ug.draw(polygon);
		textBlock.drawU(ug.apply(new UTranslate(marginX, marginY)));
	}

	private UPolygon getPolygonNormal(final StringBounder stringBounder) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, getTextHeight(stringBounder));
		final double width = getTextWidth(stringBounder);
		polygon.addPoint(width, getTextHeight(stringBounder));
		polygon.addPoint(width, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

}
