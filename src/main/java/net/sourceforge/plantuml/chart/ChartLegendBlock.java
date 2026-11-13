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
 */
package net.sourceforge.plantuml.chart;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.StyleQueries;

/**
 * The legend of a pie (one entry per slice) or of a radar (one entry per series): a colored
 * square and a name per entry, one under the other on the left or the right of the chart,
 * side by side above or below it.
 */
class ChartLegendBlock {

	private static final double SYMBOL_SIZE = 12;
	private static final double TEXT_SPACING = 5;
	private static final double ITEM_SPACING = 15;

	private final List<String> names = new ArrayList<>();
	private final List<HColor> colors = new ArrayList<>();
	private final boolean vertical;
	private final ISkinParam skinParam;
	private final FontConfiguration fontConfig;

	ChartLegendBlock(ISkinParam skinParam, ChartDiagram.LegendPosition position) {
		this.skinParam = skinParam;
		this.vertical = position == ChartDiagram.LegendPosition.LEFT || position == ChartDiagram.LegendPosition.RIGHT;
		this.fontConfig = skinParam.getCurrentStyleBuilder().getMergedStyle(StyleQueries.CHARTDIAG_LEGEND)
				.getFontConfiguration(skinParam.getIHtmlColorSet());
	}

	void add(String name, HColor color) {
		names.add(name);
		colors.add(color);
	}

	boolean isEmpty() {
		return names.isEmpty();
	}

	private TextBlock text(String name) {
		return Display.getWithNewlines(skinParam.getPragma(), name).create(fontConfig, HorizontalAlignment.LEFT,
				skinParam);
	}

	XDimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (int i = 0; i < names.size(); i++) {
			final XDimension2D dim = text(names.get(i)).calculateDimension(stringBounder);
			final double itemWidth = SYMBOL_SIZE + TEXT_SPACING + dim.getWidth();
			final double itemHeight = Math.max(SYMBOL_SIZE, dim.getHeight());
			if (vertical) {
				width = Math.max(width, itemWidth);
				height += itemHeight + (i > 0 ? ITEM_SPACING / 2 : 0);
			} else {
				width += itemWidth + (i > 0 ? ITEM_SPACING : 0);
				height = Math.max(height, itemHeight);
			}
		}
		return new XDimension2D(width, height);
	}

	void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		double x = 0;
		double y = 0;
		for (int i = 0; i < names.size(); i++) {
			final TextBlock text = text(names.get(i));
			final XDimension2D dim = text.calculateDimension(stringBounder);
			final double itemHeight = Math.max(SYMBOL_SIZE, dim.getHeight());
			final HColor color = colors.get(i);
			ug.apply(color).apply(color.bg())
					.apply(new UTranslate(x, y + (itemHeight - SYMBOL_SIZE) / 2))
					.draw(URectangle.build(SYMBOL_SIZE, SYMBOL_SIZE));
			text.drawU(ug.apply(new UTranslate(x + SYMBOL_SIZE + TEXT_SPACING, y + (itemHeight - dim.getHeight()) / 2)));
			if (vertical)
				y += itemHeight + ITEM_SPACING / 2;
			else
				x += SYMBOL_SIZE + TEXT_SPACING + dim.getWidth() + ITEM_SPACING;
		}
	}

}
