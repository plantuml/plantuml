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
package net.sourceforge.plantuml.regexdiagram;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.ebnf.ETile;
import net.sourceforge.plantuml.ebnf.ETileAlternation;
import net.sourceforge.plantuml.ebnf.ETileBox;
import net.sourceforge.plantuml.ebnf.ETileConcatenation;
import net.sourceforge.plantuml.ebnf.ETileLookAheadOrBehind;
import net.sourceforge.plantuml.ebnf.ETileNamedGroup;
import net.sourceforge.plantuml.ebnf.ETileOneOrMore;
import net.sourceforge.plantuml.ebnf.ETileOptional;
import net.sourceforge.plantuml.ebnf.ETileRegexGroup;
import net.sourceforge.plantuml.ebnf.ETileRegexGroupAllBut;
import net.sourceforge.plantuml.ebnf.ETileZeroOrMore;
import net.sourceforge.plantuml.ebnf.Symbol;
import net.sourceforge.plantuml.jsondiagram.StyleExtractor;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.CharInspector;

public class PSystemRegex extends TitledDiagram {

	public PSystemRegex(UmlSource source) {
		super(source, UmlDiagramType.REGEX, null);
		final StyleExtractor styleExtractor = new StyleExtractor(source.iterator2());

		final ISkinParam skinParam = getSkinParam();
		try {
			styleExtractor.applyStyles(skinParam);
		} catch (StyleParsingException e) {
			e.printStackTrace();
		}

		this.style = ETile.getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		this.fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		this.colorSet = skinParam.getIHtmlColorSet();
		this.lineColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Regular Expression)");
	}

	private final Deque<ETile> stack = new ArrayDeque<>();
	private final FontConfiguration fontConfiguration;
	private final Style style;
	private final HColorSet colorSet;
	private final HColor lineColor;

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	public CommandExecutionResult changeLanguage(String lang) {
		setParam("language", lang);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult useDescriptiveNames(String useDescriptive) {
		setParam("descriptive", useDescriptive);
		return CommandExecutionResult.ok();
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
//		while (stack.size() > 1)
//			concatenation();
		final ETile peekFirst = stack.peekFirst();
		final TextBlock tb = new AbstractTextBlock() {

			@Override
			public void drawU(UGraphic ug) {
				peekFirst.drawU(ug.apply(lineColor));
			}

			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return peekFirst.calculateDimension(stringBounder);
			}
		};
		return TextBlockUtils.addBackcolor(tb, null);

	}

	public CommandExecutionResult addBlocLines(BlocLines from) {

		try {
			final CharInspector it = from.inspector();
			final List<ReToken> parsed1 = RegexExpression.parse(it);
			// System.err.println("parsed1=" + parsed1);
			final List<ReToken> parsed2 = addImplicitConcatenation(parsed1);
			// System.err.println("parsed2=" + parsed2);
			final ShuntingYard shuntingYard = new ShuntingYard(parsed2.iterator());
			final List<ReToken> result = shuntingYard.getOuputQueue();
			// System.err.println("result=" + result);
			for (ReToken token : result)
				if (token.getType() == ReTokenType.SIMPLE_CHAR)
					pushEtileBox(token, Symbol.TERMINAL_STRING1);
				else if (token.getType() == ReTokenType.ESCAPED_CHAR)
					pushEtileBox(token, Symbol.TERMINAL_STRING1);
				else if (token.getType() == ReTokenType.GROUP)
					pushRegexGroup(token);
				else if (token.getType() == ReTokenType.LOOK_AHEAD)
					lookAheadOrBehind(token.getData());
				else if (token.getType() == ReTokenType.LOOK_BEHIND)
					lookAheadOrBehind(token.getData());
				else if (token.getType() == ReTokenType.NAMED_GROUP)
					namedGroup(token.getData());
				else if (token.getType() == ReTokenType.CLASS)
					pushEtileBox(token, Symbol.LITTERAL);
				else if (token.getType() == ReTokenType.ANCHOR)
					pushEtileBox(token, Symbol.LITTERAL);
				else if (token.getType() == ReTokenType.CONCATENATION_IMPLICIT)
					concatenation();
				else if (token.getType() == ReTokenType.ALTERNATIVE)
					alternation();
				else if (token.getType() == ReTokenType.QUANTIFIER && token.getData().startsWith("*"))
					repetitionZeroOrMore(false);
				else if (token.getType() == ReTokenType.QUANTIFIER && token.getData().startsWith("+"))
					repetitionOneOrMore();
				else if (token.getType() == ReTokenType.QUANTIFIER && token.getData().startsWith("?"))
					optional();
				else if (token.getType() == ReTokenType.QUANTIFIER && token.getData().startsWith("{"))
					repetitionOneOrMore(token.getData());
				else
					throw new RegexParsingException(token.toString());
		} catch (RegexParsingException ex) {
			return CommandExecutionResult.error("Error parsing: " + ex.getMessage());
		}

		return CommandExecutionResult.ok();
	}

	private List<ReToken> addImplicitConcatenation(List<ReToken> list) {
		final List<ReToken> result = new ArrayList<>();
		for (ReToken token : list) {
			if (result.size() > 0
					&& ReTokenType.needImplicitConcatenation(result.get(result.size() - 1).getType(), token.getType()))
				result.add(new ReToken(ReTokenType.CONCATENATION_IMPLICIT, ""));
			result.add(token);
		}
		return result;
	}

	private void pushEtileBox(ReToken element, Symbol type) {
		stack.addFirst(new ETileBox(element.getData(), type, fontConfiguration, style, colorSet, getSkinParam()));
	}

	private void pushRegexGroup(ReToken element) {
		final List<String> elements = new GroupSplitter().split(element.getData());
		if (elements.get(0).equals("^"))
			stack.addFirst(new ETileRegexGroupAllBut(elements, fontConfiguration, style, colorSet, getSkinParam()));
		else
			stack.addFirst(new ETileRegexGroup(elements, fontConfiguration, style, colorSet, getSkinParam()));
	}

	private void lookAheadOrBehind(String name) {
		final ETile arg1 = stack.removeFirst();
		stack.addFirst(new ETileLookAheadOrBehind(arg1, fontConfiguration, style, colorSet, name));
	}

	private void namedGroup(String name) {
		final ETile arg1 = stack.removeFirst();
		stack.addFirst(new ETileNamedGroup(arg1, fontConfiguration, colorSet, getSkinParam(), name));
	}

	private void repetitionZeroOrMore(boolean isCompact) {
		final ETile arg1 = stack.removeFirst();
		if (isCompact)
			stack.addFirst(new ETileZeroOrMore(arg1));
		else
			stack.addFirst(new ETileOptional(new ETileOneOrMore(arg1), getSkinParam()));
	}

	private void optional() {
		final ETile arg1 = stack.removeFirst();
		stack.addFirst(new ETileOptional(arg1, getSkinParam()));
	}

	private void repetitionOneOrMore() {
		final ETile arg1 = stack.removeFirst();
		stack.addFirst(new ETileOneOrMore(arg1));
	}

	private void repetitionOneOrMore(String repetition) {
		final ETile arg1 = stack.removeFirst();
		stack.addFirst(new ETileOneOrMore(arg1, repetition, fontConfiguration.bigger(-2), getSkinParam()));

	}

	private void alternation() {
		final ETile arg1 = stack.removeFirst();
		final ETile arg2 = stack.removeFirst();
		if (arg1 instanceof ETileAlternation) {
			arg1.push(arg2);
			stack.addFirst(arg1);
		} else if (arg2 instanceof ETileAlternation) {
			arg2.push(arg1);
			stack.addFirst(arg2);
		} else {
			final ETile concat = new ETileAlternation();
			concat.push(arg1);
			concat.push(arg2);
			stack.addFirst(concat);
		}
	}

	private void concatenation() {
		final ETile arg1 = stack.removeFirst();
		final ETile arg2 = stack.removeFirst();
		if (isBoxMergeable(arg1) && isBoxMergeable(arg2)) {
			final ETileBox box1 = (ETileBox) arg1;
			final ETileBox box2 = (ETileBox) arg2;
			stack.addFirst(box2.mergeWith(box1));
			return;
		}
		if (isConcatenation1Mergeable(arg1) && isBoxMergeable(arg2)) {
			final ETileConcatenation concat1 = (ETileConcatenation) arg1;
			final ETileBox box1 = (ETileBox) concat1.getFirst();
			final ETileBox box2 = (ETileBox) arg2;
			concat1.overideFirst(box2.mergeWith(box1));
			stack.addFirst(concat1);
			return;
		}
		if (arg1 instanceof ETileConcatenation) {
			arg1.push(arg2);
			stack.addFirst(arg1);
			// This does not work for (A[B])(C)
//		} else if (arg2 instanceof ETileConcatenation) {
//			arg2.push(arg1);
//			stack.addFirst(arg2);
		} else {
			final ETile concat = new ETileConcatenation();
			concat.push(arg1);
			concat.push(arg2);
			stack.addFirst(concat);
		}
	}

	private boolean isConcatenation1Mergeable(ETile tile) {
		if (tile instanceof ETileConcatenation) {
			final ETileConcatenation concat = (ETileConcatenation) tile;
			return isBoxMergeable(concat.getFirst());
		}
		return false;
	}

	private boolean isBoxMergeable(ETile tile) {
		if (tile instanceof ETileBox) {
			final ETileBox box = (ETileBox) tile;
			return box.getSymbol() == Symbol.TERMINAL_STRING1;
		}
		return false;
	}

}
