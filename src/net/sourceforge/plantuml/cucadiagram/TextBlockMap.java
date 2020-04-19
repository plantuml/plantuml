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
package net.sourceforge.plantuml.cucadiagram;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class TextBlockMap extends AbstractTextBlock implements WithPorts {

	private final FontParam fontParam;
	private final ISkinParam skinParam;
	private final Map<TextBlock, TextBlock> blocksMap = new LinkedHashMap<TextBlock, TextBlock>();
	private final List<String> keys = new ArrayList<String>();
	private double totalWidth;

	public TextBlockMap(FontParam fontParam, ISkinParam skinParam, Map<String, String> map) {
		this.fontParam = fontParam;
		this.skinParam = skinParam;
		for (Map.Entry<String, String> ent : map.entrySet()) {
			final String key = ent.getKey();
			this.keys.add(key);
			final String value = ent.getValue();
			final TextBlock block1 = getTextBlock(key);
			final TextBlock block2 = getTextBlock(value);
			this.blocksMap.put(block1, block2);
		}
	}

	public Ports getPorts(StringBounder stringBounder) {
		final Ports ports = new Ports();
		int i = 0;
		double y = 0;
		for (Map.Entry<TextBlock, TextBlock> ent : blocksMap.entrySet()) {
			final TextBlock key = ent.getKey();
			final TextBlock value = ent.getValue();
			final double height = getHeightOfRow(stringBounder, key, value);
			ports.add(keys.get(i), y, height);
			y += height;
			i++;
		}
		return ports;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getWidthColA(stringBounder) + getWidthColB(stringBounder),
				getTotalHeight(stringBounder));
	}

	private double getWidthColA(StringBounder stringBounder) {
		return getMaxWidth(stringBounder, blocksMap.keySet());
	}

	private double getWidthColB(StringBounder stringBounder) {
		return getMaxWidth(stringBounder, blocksMap.values());
	}

	private double getMaxWidth(StringBounder stringBounder, Collection<TextBlock> blocks) {
		double width = 0;
		for (TextBlock block : blocks) {
			width = Math.max(width, block.calculateDimension(stringBounder).getWidth());
		}
		return width;
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D fullDim = calculateDimension(stringBounder);
		final double trueWidth = Math.max(fullDim.getWidth(), totalWidth);
		final double widthColA = getWidthColA(stringBounder);
		final double widthColB = getWidthColB(stringBounder);
		double y = 0;
		for (Map.Entry<TextBlock, TextBlock> ent : blocksMap.entrySet()) {
			final TextBlock key = ent.getKey();
			final TextBlock value = ent.getValue();
			final UGraphic ugline = ug.apply(UTranslate.dy(y));
			ugline.draw(ULine.hline(trueWidth));
			final double heightOfRow = getHeightOfRow(stringBounder, key, value);
			if (value instanceof Point) {
//				final Dimension2D dimPoint = value.calculateDimension(stringBounder);
//				final double xp = widthColA + (widthColB - dimPoint.getWidth()) / 2;
//				final double yp = (heightOfRow - dimPoint.getHeight()) / 2;
//				value.drawU(ugline.apply(new UTranslate(xp, yp)));
				final double posColA = (trueWidth - key.calculateDimension(stringBounder).getWidth()) / 2;
				key.drawU(ugline.apply(UTranslate.dx(posColA)));
			} else {
				final double posColA = (widthColA - key.calculateDimension(stringBounder).getWidth()) / 2;
				key.drawU(ugline.apply(UTranslate.dx(posColA)));
				value.drawU(ugline.apply(UTranslate.dx(widthColA)));
				ugline.apply(UTranslate.dx(widthColA)).draw(ULine.vline(heightOfRow));
			}
			y += heightOfRow;
		}
		// ug.apply(UTranslate.dx(widthColA)).draw(ULine.vline(fullDim.getHeight()));
	}

	private double getTotalHeight(StringBounder stringBounder) {
		double height = 0;
		for (Map.Entry<TextBlock, TextBlock> ent : blocksMap.entrySet()) {
			final TextBlock key = ent.getKey();
			final TextBlock value = ent.getValue();
			height += getHeightOfRow(stringBounder, key, value);
		}
		return height;
	}

	private double getHeightOfRow(StringBounder stringBounder, TextBlock key, TextBlock value) {
		return Math.max(key.calculateDimension(stringBounder).getHeight(),
				value.calculateDimension(stringBounder).getHeight());
	}

	private TextBlock getTextBlock(String key) {
		if (key.equals("\0")) {
			return new Point(getFontConfiguration().getColor());
		}
		final Display display = Display.getWithNewlines(key);
		TextBlock result = display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
		result = TextBlockUtils.withMargin(result, 5, 2);
		return result;
	}

	static class Point extends AbstractTextBlock {

		private final HColor color;

		public Point(HColor color) {
			this.color = color;
		}

		public Dimension2D calculateDimension(StringBounder stringBounder) {
			return new Dimension2DDouble(getDiameter(), getDiameter());
		}

		public void drawU(UGraphic ug) {
			final UShape point = new UEllipse(getDiameter(), getDiameter());
			ug = ug.apply(color).apply(color.bg());
			ug.draw(point);
		}

		private double getDiameter() {
			return 7;
		}

	}

	private FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, fontParam, null);
	}

	public void setTotalWidth(double totalWidth) {
		this.totalWidth = totalWidth;
	}

}
