/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.ebnf;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FloatingNote;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.preproc.ConfigurationStore;
import net.sourceforge.plantuml.preproc.OptionKey;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.I18n;

public class ETileBox extends ETile {

	private final String value;
	private final FontConfiguration fc;
	private final Style style;
	private final UText utext;
	private final HColorSet colorSet;
	private final Symbol symbol;
	private final ISkinParam skinParam;
	private final ConfigurationStore<OptionKey> option;
	private String commentAbove;
	private String commentBelow;

	private static final Map<String, String> VALUE_MAP = new HashMap<>();

	static {
		VALUE_MAP.put("\\b", "wordBoundary");
		VALUE_MAP.put("\\B", "nonWordBoundary");
		VALUE_MAP.put("\\d", "digit");
		VALUE_MAP.put("\\D", "nonDigit");
		VALUE_MAP.put("\\w", "word");
		VALUE_MAP.put("\\W", "nonWord");
		VALUE_MAP.put("\\s", "whitespace");
		VALUE_MAP.put("\\S", "nonWhitespace");
		VALUE_MAP.put(".", "anyChar");
	}

	public ETileBox mergeWith(ETileBox other) {
		return new ETileBox(this.value + other.value, symbol, fc, style, colorSet, skinParam, option);
	}

	public ETileBox(String value, Symbol symbol, FontConfiguration fc, Style style, HColorSet colorSet,
			ISkinParam skinParam, ConfigurationStore<OptionKey> option) {
		this.symbol = symbol;
		this.skinParam = skinParam;
		this.option = option;
		this.value = getDrawValue(value);
		this.fc = fc;
		this.utext = UText.build(this.value, fc);
		this.style = style;
		this.colorSet = colorSet;
	}

	private double getPureH1(StringBounder stringBounder) {
		final double height = getTextDim(stringBounder).getHeight() + 10;
		return height / 2;
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		double h1 = getPureH1(stringBounder);
		final TextBlock note = getNoteAbove(stringBounder);
		if (note != TextBlockUtils.EMPTY_TEXT_BLOCK)
			h1 += note.calculateDimension(stringBounder).getHeight() + 20;
		return h1;
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		double h2 = getPureH1(stringBounder);
		final TextBlock note = getNoteBelow(stringBounder);
		if (note != TextBlockUtils.EMPTY_TEXT_BLOCK)
			h2 += note.calculateDimension(stringBounder).getHeight() + 20;
		return h2;
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		double width = getTextDim(stringBounder).getWidth() + 10;
		final TextBlock noteAbove = getNoteAbove(stringBounder);
		if (noteAbove != TextBlockUtils.EMPTY_TEXT_BLOCK)
			width = Math.max(width, noteAbove.calculateDimension(stringBounder).getWidth());

		final TextBlock noteBelow = getNoteBelow(stringBounder);
		if (noteBelow != TextBlockUtils.EMPTY_TEXT_BLOCK)
			width = Math.max(width, noteBelow.calculateDimension(stringBounder).getWidth());

		return width;
	}

	private XDimension2D getTextDim(StringBounder stringBounder) {
		return stringBounder.calculateDimension(fc.getFont(), value);
	}

	private XDimension2D getBoxDim(StringBounder stringBounder) {
		return getTextDim(stringBounder).delta(10);
	}

	@Override
	protected void addCommentAbove(String comment) {
		this.commentAbove = comment;
	}

	@Override
	protected void addCommentBelow(String comment) {
		this.commentBelow = comment;
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dim = calculateDimension(stringBounder);
		final XDimension2D dimText = getTextDim(stringBounder);
		final XDimension2D dimBox = getBoxDim(stringBounder);
		final HColor lineColor = style.value(PName.LineColor).asColor(colorSet);
		final HColor backgroundColor = style.value(PName.BackGroundColor).asColor(colorSet);

		final TextBlock noteAbove = getNoteAbove(stringBounder);
		final TextBlock noteBelow = getNoteBelow(stringBounder);
		final double posy = noteAbove == TextBlockUtils.EMPTY_TEXT_BLOCK ? 0
				: noteAbove.calculateDimension(stringBounder).getHeight() + 20;

		final double posxBox = (dim.getWidth() - dimBox.getWidth()) / 2;

		if (symbol == Symbol.TERMINAL_STRING1 || symbol == Symbol.TERMINAL_STRING2) {
			final URectangle rect = URectangle.build(dimBox);
			ug.apply(new UTranslate(posxBox, posy)).apply(lineColor).apply(UStroke.withThickness(0.5)).draw(rect);
		} else if (symbol == Symbol.SPECIAL_SEQUENCE) {
			final URectangle rect = URectangle.build(dimBox);
			ug.apply(new UTranslate(posxBox, posy)).apply(lineColor).apply(new UStroke(5, 5, 1)).draw(rect);
//			final URectangle rect1 = URectangle.build(dimBox.delta(2)).rounded(12);
//			final URectangle rect2 = URectangle.build(dimBox.delta(-2)).rounded(8);
//			ug.apply(new UTranslate(posxBox - 1, posy - 1)).apply(lineColor).apply(new UStroke(5.0, 5.0, 1.0)).draw(rect1);
//			ug.apply(new UTranslate(posxBox + 1, posy + 1)).apply(lineColor).apply(UStroke.withThickness(0.5)).draw(rect2);
		} else {
			final URectangle rect = URectangle.build(dimBox).rounded(10);
			ug.apply(new UTranslate(posxBox, posy)).apply(lineColor).apply(backgroundColor.bg())
					.apply(UStroke.withThickness(1.5)).draw(rect);
		}

		ug.apply(new UTranslate(5 + posxBox, posy + 5 + dimText.getHeight() - utext.getDescent(stringBounder)))
				.draw(utext);

		if (noteAbove != TextBlockUtils.EMPTY_TEXT_BLOCK) {
			final double posxAbove = (dim.getWidth() - noteAbove.calculateDimension(stringBounder).getWidth()) / 2;
			noteAbove.drawU(ug.apply(new UTranslate(posxAbove, 0)));
		}

		if (noteBelow != TextBlockUtils.EMPTY_TEXT_BLOCK) {
			final double posxBelow = (dim.getWidth() - noteBelow.calculateDimension(stringBounder).getWidth()) / 2;
			final double posyBelow = dim.getHeight() - noteBelow.calculateDimension(stringBounder).getHeight();
			noteBelow.drawU(ug.apply(new UTranslate(posxBelow, posyBelow)));
		}

		if (posxBox > 0) {
			drawHlineDirected(ug, getH1(stringBounder), 0, posxBox, .5, 25);
			drawHlineDirected(ug, getH1(stringBounder), posxBox + dimBox.getWidth(), dim.getWidth(), .5, 25);
		}

	}

	private TextBlock getNoteAbove(StringBounder stringBounder) {
		if (commentAbove == null)
			return TextBlockUtils.EMPTY_TEXT_BLOCK;

		final FloatingNote note = FloatingNote.createOpale(Display.getWithNewlines(skinParam.getPragma(), commentAbove),
				skinParam, SName.ebnf);
		final XDimension2D dim = note.calculateDimension(stringBounder);
		final double pos = dim.getWidth() * .5;
		XPoint2D pp1 = new XPoint2D(pos, dim.getHeight());
		XPoint2D pp2 = new XPoint2D(pos, 20 + dim.getHeight());
		note.setOpale(Direction.DOWN, pp1, pp2);
		return note;
	}

	private TextBlock getNoteBelow(StringBounder stringBounder) {
		if (commentBelow == null)
			return TextBlockUtils.EMPTY_TEXT_BLOCK;

		final FloatingNote note = FloatingNote.createOpale(Display.getWithNewlines(skinParam.getPragma(), commentBelow),
				skinParam, SName.ebnf);
		final XDimension2D dim = note.calculateDimension(stringBounder);
		final double pos = dim.getWidth() * .5;
		XPoint2D pp1 = new XPoint2D(pos, 0);
		XPoint2D pp2 = new XPoint2D(pos, -20);
		note.setOpale(Direction.UP, pp1, pp2);
		return note;
	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getRepetitionLabel() {
		return value;
	}

	public final Symbol getSymbol() {
		return symbol;
	}

	private String getDrawValue(String value) {
		if (!Boolean.parseBoolean(option.getValue(OptionKey.USE_DESCRIPTIVE_NAMES)) || !VALUE_MAP.containsKey(value))
			return value;

		final String language = option.getValue(OptionKey.LANGUAGE);
		return I18n.getLocalizedValue(language, "ebnf." + VALUE_MAP.get(value), value);
	}

}
