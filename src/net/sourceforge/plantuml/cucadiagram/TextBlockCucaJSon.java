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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.awt.geom.Dimension2D;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TextBlockCucaJSon extends AbstractTextBlock implements WithPorts {

	private final FontParam fontParam;
	private final ISkinParam skinParam;
	private final FontConfiguration fontConfiguration;
	private final JsonValue json;
	private TextBlockJson jsonTextBlock;
	private double totalWidth;

	public TextBlockCucaJSon(FontConfiguration fontConfiguration, FontParam fontParam, ISkinParam skinParam,
			JsonValue json) {
		this.fontParam = fontParam;
		this.skinParam = skinParam;
		this.json = json;
		this.fontConfiguration = fontConfiguration;
	}

	private TextBlockJson getJsonTextBlock() {
		if (jsonTextBlock == null)
			this.jsonTextBlock = new TextBlockJson(json, 0);

		jsonTextBlock.totalWidth = totalWidth;
		return jsonTextBlock;
	}

	@Override
	public Ports getPorts(StringBounder stringBounder) {
		return new Ports();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return getJsonTextBlock().calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug) {
		getJsonTextBlock().drawU(ug);
	}

	class TextBlockJson extends AbstractTextBlock {

		private final JsonObject obj;
		private double totalWidth;

		public TextBlockJson(JsonValue json, double totalWidth) {
			this.obj = json.asObject();
			this.totalWidth = totalWidth;
		}

		@Override
		public Dimension2D calculateDimension(StringBounder stringBounder) {
			return new Dimension2DDouble(getWidth1(stringBounder) + getWidth2(stringBounder), getHeight(stringBounder));
		}

		private double getWidth1(StringBounder stringBounder) {
			double result = 0;
			for (JsonObject.Member s : obj) {
				final TextBlock tb1 = getTextBlockKey(s.getName());
				result = Math.max(result, tb1.calculateDimension(stringBounder).getWidth());
			}
			return result;
		}

		private double getWidth2(StringBounder stringBounder) {
			double result = 0;
			for (JsonObject.Member s : obj) {
				final TextBlock tb2 = getTextBlockValue(s.getValue(), totalWidth);
				result = Math.max(result, tb2.calculateDimension(stringBounder).getWidth());
			}
			return result;
		}

		private double getHeight(StringBounder stringBounder) {
			double result = 0;
			for (JsonObject.Member s : obj) {
				final TextBlock tb1 = getTextBlockKey(s.getName());
				final TextBlock tb2 = getTextBlockValue(s.getValue(), totalWidth);
				final Dimension2D dim1 = tb1.calculateDimension(stringBounder);
				final Dimension2D dim2 = tb2.calculateDimension(stringBounder);
				result += Math.max(dim1.getHeight(), dim2.getHeight());
			}
			return result;
		}

		@Override
		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final double width1 = getWidth1(stringBounder);
			final double width2 = getWidth2(stringBounder);
			final double height = getHeight(stringBounder);
			ug.apply(UTranslate.dx(width1)).draw(ULine.vline(height));
			final ULine hline = ULine.hline(this.totalWidth);
			for (JsonObject.Member s : obj) {
				final TextBlock tb1 = getTextBlockKey(s.getName());
				final TextBlock tb2 = getTextBlockValue(s.getValue(), width2);
				final Dimension2D dim1 = tb1.calculateDimension(stringBounder);
				final Dimension2D dim2 = tb2.calculateDimension(stringBounder);
				ug.draw(hline);
				tb1.drawU(ug);
				tb2.drawU(ug.apply(UTranslate.dx(width1)));
				ug = ug.apply(UTranslate.dy(Math.max(dim1.getHeight(), dim2.getHeight())));
			}

		}

	}

	private TextBlock getTextBlockValue(JsonValue value, double width2) {
		if (value.isString() || value.isNull() || value.isTrue() || value.isFalse() || value.isNumber()) {
			final String tmp = value.isString() ? value.asString() : value.toString();
			final Display display = Display.getWithNewlines(tmp);
			TextBlock result = display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
			result = TextBlockUtils.withMargin(result, 5, 2);
			return result;
		}
		if (value.isArray())
			return new TextBlockArray(value.asArray(), width2);
		if (value.isObject())
			return new TextBlockJson(value, width2);

		final String tmp = value.getClass().getSimpleName();
		final Display display = Display.getWithNewlines(tmp);
		TextBlock result = display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
		result = TextBlockUtils.withMargin(result, 5, 2);
		return result;
	}

	class TextBlockArray extends AbstractTextBlock {

		private final JsonArray array;
		private final double totalWidth;

		public TextBlockArray(JsonArray array, double totalWidth) {
			this.array = array;
			this.totalWidth = totalWidth;
		}

		@Override
		public Dimension2D calculateDimension(StringBounder stringBounder) {
			Dimension2D result = new Dimension2DDouble(0, 0);
			for (JsonValue element : array) {
				final TextBlock tb = getTextBlockValue(element, totalWidth);
				final Dimension2D dim = tb.calculateDimension(stringBounder);
				result = Dimension2DDouble.mergeTB(result, dim);
			}
			return result;
		}

		@Override
		public void drawU(UGraphic ug) {
			final ULine hline = ULine.hline(this.totalWidth);
			int nb = 0;
			for (JsonValue element : array) {
				final TextBlock tb = getTextBlockValue(element, totalWidth);
				if (nb > 0)
					ug.draw(hline);
				nb++;
				tb.drawU(ug);
				ug = ug.apply(UTranslate.dy(tb.calculateDimension(ug.getStringBounder()).getHeight()));
			}
		}

	}

	private TextBlock getTextBlockKey(String key) {
		final Display display = Display.getWithNewlines(key);
		TextBlock result = display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
		result = TextBlockUtils.withMargin(result, 5, 2);
		return result;
	}

	private FontConfiguration getFontConfiguration() {
		if (fontConfiguration == null)
			return new FontConfiguration(skinParam, fontParam, null);
		return fontConfiguration;
	}

	public void setTotalWidth(double totalWidth) {
		this.totalWidth = totalWidth;
	}

}
