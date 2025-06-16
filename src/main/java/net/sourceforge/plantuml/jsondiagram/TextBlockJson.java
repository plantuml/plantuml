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
package net.sourceforge.plantuml.jsondiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonObject.Member;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.yaml.Highlighted;

//See TextBlockMap
public class TextBlockJson extends AbstractTextBlock {
	// ::remove folder when __HAXE__

	private static final double MIN_WIDTH = 30;
	private static final double MIN_HEIGHT = 15;

	private final List<Line> lines = new ArrayList<>();

	private final ISkinParam skinParam;
	private double totalWidth;
	private final JsonValue root;

	private final StyleBuilder styleBuilder;
	private final SName diagramType;

	static class Line {
		final TextBlock b1;
		final TextBlock b2;
		final Highlighted highlighted;

		Line(TextBlock b1, TextBlock b2, Highlighted highlighted) {
			this.b1 = b1;
			this.b2 = b2;
			this.highlighted = highlighted;
		}

		Line(TextBlock b1, Highlighted highlighted) {
			this(b1, null, highlighted);
		}

		double getHeightOfRow(StringBounder stringBounder) {
			final double height = b1.calculateDimension(stringBounder).getHeight();
			if (b2 == null)
				return height;

			return Math.max(height, b2.calculateDimension(stringBounder).getHeight());
		}

	}

	TextBlockJson(ISkinParam skinParam, JsonValue root, List<Highlighted> allHighlighteds) {
		this.styleBuilder = skinParam.getCurrentStyleBuilder();
		this.diagramType = skinParam.getUmlDiagramType() == UmlDiagramType.JSON ? SName.jsonDiagram : SName.yamlDiagram;
		this.skinParam = skinParam;

		this.root = root;
		if (root instanceof JsonObject)
			for (Member member : (JsonObject) root) {
				final String key = member.getName();
				final String value = getShortString(member.getValue());

				final Highlighted highlighted = isHighlighted(key, allHighlighteds);
				final TextBlock block1 = getTextBlock(getStyleToUse(true, highlighted), key);
				final TextBlock block2 = getTextBlock(getStyleToUse(false, highlighted), value);
				this.lines.add(new Line(block1, block2, highlighted));
			}
		if (root instanceof JsonArray) {
			int i = 0;
			for (JsonValue value : (JsonArray) root) {
				final Highlighted highlighted = isHighlighted("" + i, allHighlighteds);
				final TextBlock block2 = getTextBlock(getStyleToUse(false, highlighted), getShortString(value));
				this.lines.add(new Line(block2, highlighted));
				i++;
			}
		}
	}

	private Style getStyleToUse(boolean header, Highlighted highlighted) {
		final StyleSignature signature;
		if (header && highlighted != null)
			signature = StyleSignatureBasic
					.of(SName.root, SName.element, diagramType, SName.header, SName.node, SName.highlight)
					.withTOBECHANGED(highlighted.getStereotype());
		else if (highlighted != null)
			signature = StyleSignatureBasic.of(SName.root, SName.element, diagramType, SName.node, SName.highlight)
					.withTOBECHANGED(highlighted.getStereotype());
		else if (header)
			signature = StyleSignatureBasic.of(SName.root, SName.element, diagramType, SName.header, SName.node);
		else
			signature = StyleSignatureBasic.of(SName.root, SName.element, diagramType, SName.node);

		return signature.getMergedStyle(styleBuilder);
	}

	private Highlighted isHighlighted(String key, List<Highlighted> highlighted) {
		for (Highlighted tmp : highlighted)
			if (tmp.isKeyHighlight(key))
				return tmp;

		return null;
	}

	public int size() {
		int size = 0;
		if (root instanceof JsonObject)
			for (Member member : (JsonObject) root)
				size++;

		if (root instanceof JsonArray)
			for (JsonValue value : (JsonArray) root)
				size++;

		return size;

	}

	private String getShortString(JsonValue value) {
		if (value.isString())
			return value.asString();

		if (value.isNull())
			return "<U+2400>";
		// return "<U+2205> null";

		if (value.isNumber())
			return value.toString();

		if (value.isBoolean())
			if (value.isTrue())
				return "<U+2611> true";
			else
				return "<U+2610> false";

		return "   ";
	}

	public List<JsonValue> children() {
		final List<JsonValue> result = new ArrayList<>();
		if (root instanceof JsonObject)
			for (Member member : (JsonObject) root) {
				final JsonValue value = member.getValue();
				if (value instanceof JsonObject || value instanceof JsonArray)
					result.add(value);
				else
					result.add(null);
			}

		if (root instanceof JsonArray)
			for (JsonValue value : (JsonArray) root)
				if (value instanceof JsonObject || value instanceof JsonArray)
					result.add(value);
				else
					result.add(null);

		return Collections.unmodifiableList(result);
	}

	public List<String> keys() {
		final List<String> result = new ArrayList<>();
		if (root instanceof JsonObject)
			for (Member member : (JsonObject) root) {
				final String key = member.getName();
				result.add(key);
			}

		if (root instanceof JsonArray) {
			int i = 0;
			for (JsonValue value : (JsonArray) root) {
				result.add("" + i);
				i++;
			}
		}
		return Collections.unmodifiableList(result);
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final double width = getWidthColA(stringBounder) + getWidthColB(stringBounder);
		final double height = getTotalHeight(stringBounder);
		return new XDimension2D(width, height);
	}

	public double getWidthColA(StringBounder stringBounder) {
		double width = 0;
		for (Line line : lines)
			width = Math.max(width, line.b1.calculateDimension(stringBounder).getWidth());

		return width;
	}

	public double getWidthColB(StringBounder stringBounder) {
		double width = 0;
		for (Line line : lines)
			if (line.b2 != null)
				width = Math.max(width, line.b2.calculateDimension(stringBounder).getWidth());

		return width;
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final XDimension2D fullDim = calculateDimension(stringBounder);
		double trueWidth = Math.max(fullDim.getWidth(), totalWidth);
		final double widthColA = getWidthColA(stringBounder);
		final double widthColB = getWidthColB(stringBounder);

		double y = 0;
		final Style styleNode = StyleSignatureBasic.of(SName.root, SName.element, diagramType, SName.node)
				.getMergedStyle(styleBuilder);
		final UGraphic ugNode = styleNode.applyStrokeAndLineColor(ug, skinParam.getIHtmlColorSet());
		for (Line line : lines) {
			final double heightOfRow = line.getHeightOfRow(stringBounder);
			y += heightOfRow;
		}
		if (y == 0)
			y = MIN_HEIGHT;
		if (trueWidth == 0)
			trueWidth = MIN_WIDTH;

		final double round = styleNode.value(PName.RoundCorner).asDouble();
		final URectangle fullNodeRectangle = URectangle.build(trueWidth, y).rounded(round);
		final HColor backColor = styleNode.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
		ugNode.apply(backColor.bg()).apply(backColor).draw(fullNodeRectangle);

		final Style styleSeparator = styleNode.getSignature().addSName(SName.separator)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final UGraphic ugSeparator = styleSeparator.applyStrokeAndLineColor(ug, skinParam.getIHtmlColorSet());

		y = 0;
		for (Line line : lines) {
			final UGraphic ugline = ugSeparator.apply(UTranslate.dy(y));
			final double heightOfRow = line.getHeightOfRow(stringBounder);
			if (line.highlighted != null) {
				final URectangle back = URectangle.build(trueWidth - 2, heightOfRow).rounded(4);
				final Style styleNodeHighlight = StyleSignatureBasic
						.of(SName.root, SName.element, diagramType, SName.node, SName.highlight)
						.withTOBECHANGED(line.highlighted.getStereotype()).getMergedStyle(styleBuilder);
				final HColor cellBackColor = styleNodeHighlight.value(PName.BackGroundColor)
						.asColor(skinParam.getIHtmlColorSet());
				ugline.apply(cellBackColor).apply(cellBackColor.bg()).apply(new UTranslate(1.5, 0)).draw(back);
			}

			if (y > 0)
				ugline.draw(ULine.hline(trueWidth));

			final HorizontalAlignment horizontalAlignment = styleNode.getHorizontalAlignment();
			horizontalAlignment.draw(ugline, line.b1, 0, 0, widthColA);

			if (line.b2 != null) {
				final UGraphic uglineColB = ugline.apply(UTranslate.dx(widthColA));
				horizontalAlignment.draw(uglineColB, line.b2, 0, 0, widthColB);
				uglineColB.draw(ULine.vline(heightOfRow));
			}

			y += heightOfRow;
		}
		ugNode.draw(fullNodeRectangle);

	}

	private double getTotalHeight(StringBounder stringBounder) {
		double height = 0;
		for (Line line : lines)
			height += line.getHeightOfRow(stringBounder);

		if (height == 0)
			return MIN_HEIGHT;

		return height;
	}

	public double[] getAllHeights(StringBounder stringBounder) {
		final double result[] = new double[lines.size()];
		for (int i = 0; i < lines.size(); i++)
			result[i] = lines.get(i).getHeightOfRow(stringBounder);

		return result;
	}

	private TextBlock getTextBlock(Style style, String key) {
		final Display display = Display.getWithNewlines(skinParam.getPragma(), key);
		final FontConfiguration fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		final LineBreakStrategy wrap = style.wrapWidth();
		final HorizontalAlignment horizontalAlignment = style.getHorizontalAlignment();
		TextBlock result = display.create0(fontConfiguration, horizontalAlignment, skinParam, wrap,
				CreoleMode.NO_CREOLE, null, null);
		result = TextBlockUtils.withMargin(result, 5, 2);
		return result;
	}

	public void setTotalWidth(double totalWidth) {
		this.totalWidth = totalWidth;
	}

	public HColor getBackcolor() {
		return null;
	}

}
