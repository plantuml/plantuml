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
 * Contribution :  Hisashi Miyashita
 *
 */
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;

public class CommandLinkElement extends SingleLineCommand2<DescriptionDiagram> {

	private static final String KEY1 = "dotted|dashed|plain|bold|hidden|norank|single|thickness=\\d+";
	private static final String KEY2 = ",dotted|,dashed|,plain|,bold|,hidden|,norank|,single|,thickness=\\d+";
	public static final String LINE_STYLE = "(?:#\\w+|" + CommandLinkElement.KEY1 + ")(?:,#\\w+|"
			+ CommandLinkElement.KEY2 + ")*";
	private static final String LINE_STYLE_MUTILPLES = LINE_STYLE + "(?:(?:;" + LINE_STYLE + ")*)";
	public static final String STYLE_COLORS_MULTIPLES = "-\\[(" + LINE_STYLE_MUTILPLES + "*)\\]->";

	public CommandLinkElement() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandLinkElement.class.getName(), RegexLeaf.start(), //
				getGroup("ENT1"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("FIRST_LABEL", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("HEAD2", "(0\\)|<<|<_|[<^*+#0@)]|<\\|[\\|\\:]?|[%s]+o)?"), //
				new RegexLeaf("BODY1", "([-=.~]+)"), //
				new RegexLeaf("ARROW_STYLE1", "(?:\\[(" + LINE_STYLE_MUTILPLES + ")\\])?"), //
				new RegexOptional(new RegexLeaf("DIRECTION", "(left|right|up|down|le?|ri?|up?|do?)(?=[-=.~0()])")), //
				new RegexOptional(new RegexLeaf("INSIDE", "(0|\\(0\\)|\\(0|0\\))(?=[-=.~])")), //
				new RegexLeaf("ARROW_STYLE2", "(?:\\[(" + LINE_STYLE + ")\\])?"), //
				new RegexLeaf("BODY2", "([-=.~]*)"), //
				new RegexLeaf("HEAD1", "(\\(0|>>|_>|[>^*+#0@(]|[\\:\\|]?\\|>|\\\\\\\\|o[%s]+)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("SECOND_LABEL", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				getGroup("ENT2"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("LABEL_LINK", "(?::[%s]*(.+))?"), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.LINE);
	}

	private String getHead(RegexResult arg, final String key) {
		String result = arg.get(key, 0);
		result = trimAndLowerCase(result);
		return result.replace("_", "");
	}

	private LinkType getLinkType(RegexResult arg) {
		final String head1 = getHead(arg, "HEAD1");
		final String head2 = getHead(arg, "HEAD2");
		LinkDecor d1 = LinkDecor.NONE;
		LinkDecor d2 = LinkDecor.NONE;

		if (head1.equals("(0")) {
			d1 = LinkDecor.CIRCLE_CONNECT;
		} else if (head1.equals("#")) {
			d1 = LinkDecor.SQUARE;
		} else if (head1.equals("0")) {
			d1 = LinkDecor.CIRCLE;
		} else if (head1.equals("@")) {
			d1 = LinkDecor.CIRCLE_FILL;
		} else if (head1.equals("(")) {
			d1 = LinkDecor.PARENTHESIS;
		} else if (head1.equals(">")) {
			d1 = LinkDecor.ARROW;
		} else if (head1.equals("*")) {
			d1 = LinkDecor.COMPOSITION;
		} else if (head1.equals("o")) {
			d1 = LinkDecor.AGREGATION;
		} else if (head1.equals("+")) {
			d1 = LinkDecor.PLUS;
		} else if (head1.equals("\\\\")) {
			d1 = LinkDecor.HALF_ARROW;
		} else if (head1.equals(">>")) {
			d1 = LinkDecor.ARROW_TRIANGLE;
		} else if (head1.equals("^")) {
			d1 = LinkDecor.EXTENDS;
		} else if (head1.equals(":|>")) {
			d1 = LinkDecor.DEFINEDBY;
		} else if (head1.equals("||>")) {
			d1 = LinkDecor.REDEFINES;
		} else if (head1.equals("|>")) {
			d1 = LinkDecor.EXTENDS;
		}

		if (head2.equals("0)")) {
			d2 = LinkDecor.CIRCLE_CONNECT;
		} else if (head2.equals("#")) {
			d2 = LinkDecor.SQUARE;
		} else if (head2.equals("0")) {
			d2 = LinkDecor.CIRCLE;
		} else if (head2.equals("@")) {
			d2 = LinkDecor.CIRCLE_FILL;
		} else if (head2.equals(")")) {
			d2 = LinkDecor.PARENTHESIS;
		} else if (head2.equals("<")) {
			d2 = LinkDecor.ARROW;
		} else if (head2.equals("*")) {
			d2 = LinkDecor.COMPOSITION;
		} else if (head2.equals("o")) {
			d2 = LinkDecor.AGREGATION;
		} else if (head2.equals("+")) {
			d2 = LinkDecor.PLUS;
		} else if (head2.equals("<<")) {
			d2 = LinkDecor.ARROW_TRIANGLE;
		} else if (head2.equals("^")) {
			d2 = LinkDecor.EXTENDS;
		} else if (head2.equals("<|:")) {
			d2 = LinkDecor.DEFINEDBY;
		} else if (head2.equals("<||")) {
			d2 = LinkDecor.REDEFINES;
		} else if (head2.equals("<|")) {
			d2 = LinkDecor.EXTENDS;
		}

		LinkType result = new LinkType(d1, d2);
		final String queue = getQueue(arg);
		if (queue.contains(".")) {
			result = result.goDashed();
		} else if (queue.contains("~")) {
			result = result.goDotted();
		} else if (queue.contains("=")) {
			result = result.goBold();
		}

		final String middle = arg.get("INSIDE", 0);
		if ("0".equals(middle)) {
			result = result.withMiddleCircle();
		} else if ("0)".equals(middle)) {
			result = result.withMiddleCircleCircled1();
		} else if ("(0".equals(middle)) {
			result = result.withMiddleCircleCircled2();
		} else if ("(0)".equals(middle)) {
			result = result.withMiddleCircleCircled();
		}
		return result;
	}

	private static String trimAndLowerCase(String s) {
		if (s == null) {
			return "";
		}
		return StringUtils.goLowerCase(StringUtils.trin(s));
	}

	private Direction getDirection(RegexResult arg) {
		final String dir = arg.get("DIRECTION", 0);
		if (dir == null) {
			return StringUtils.getQueueDirection(getQueue(arg));
			// return Direction.DOWN;
		}
		return StringUtils.getQueueDirection(dir);
	}

	private String getQueue(RegexResult arg) {
		return arg.get("BODY1", 0) + arg.get("BODY2", 0);
	}

	private static RegexLeaf getGroup(String name) {
		return new RegexLeaf(name,
				"([%pLN_.]+|\\(\\)[%s]*[%pLN_.]+|\\(\\)[%s]*[%g][^%g]+[%g]|:[^:]+:|(?!\\[\\*\\])\\[[^\\[\\]]+\\]|\\((?!\\*\\))[^)]+\\))");
	}

	@Override
	protected CommandExecutionResult executeArg(DescriptionDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String ent1String = arg.get("ENT1", 0);
		final String ent2String = arg.get("ENT2", 0);
		final Ident ident1 = diagram.buildFullyQualified(ent1String);
		final Ident ident2 = diagram.buildFullyQualified(ent2String);
		Ident ident1pure = Ident.empty().add(ent1String, diagram.getNamespaceSeparator());
		Ident ident2pure = Ident.empty().add(ent2String, diagram.getNamespaceSeparator());
		final Code code1 = diagram.V1972() ? ident1 : diagram.buildCode(ent1String);
		final Code code2 = diagram.V1972() ? ident2 : diagram.buildCode(ent2String);

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		} else {
			queue = getQueue(arg);
		}
		final Labels labels = new Labels(arg);

		final IEntity cl1;
		final IEntity cl2;
		if (diagram.isGroup(code1) && diagram.isGroup(code2)) {
			cl1 = diagram.V1972() ? diagram.getGroupStrict(diagram.buildLeafIdent(ent1String))
					: diagram.getGroup(diagram.buildCode(ent1String));
			cl2 = diagram.V1972() ? diagram.getGroupStrict(diagram.buildLeafIdent(ent2String))
					: diagram.getGroup(diagram.buildCode(ent2String));
		} else {
			cl1 = getFoo1(diagram, code1, ident1, ident1pure);
			cl2 = getFoo1(diagram, code2, ident2, ident2pure);
		}
		Link link = new Link(cl1, cl2, linkType, Display.getWithNewlines(labels.getLabelLink()), queue.length(),
				labels.getFirstLabel(), labels.getSecondLabel(), diagram.getLabeldistance(), diagram.getLabelangle(),
				diagram.getSkinParam().getCurrentStyleBuilder());
		link.setLinkArrow(labels.getLinkArrow());
		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}
		link.setColors(color().getColor(diagram.getSkinParam().getThemeStyle(), arg,
				diagram.getSkinParam().getIHtmlColorSet()));
		link.applyStyle(diagram.getSkinParam().getThemeStyle(), arg.getLazzy("ARROW_STYLE", 0));
		if (arg.get("STEREOTYPE", 0) != null) {
			final Stereotype stereotype = Stereotype.build(arg.get("STEREOTYPE", 0));
			if (UseStyle.useBetaStyle()) {
				link.setStereotype(stereotype);
			} else {
				link.setColors(link.getColors().applyStereotype(stereotype, diagram.getSkinParam(), ColorParam.arrow));
			}
		}
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

	private IEntity getFoo1(DescriptionDiagram diagram, Code code, Ident ident, Ident pure) {
		if (!diagram.V1972() && diagram.isGroup(code)) {
			return diagram.getGroup(code);
		}
		if (diagram.V1972() && diagram.isGroupStrict(ident)) {
			return diagram.getGroupStrict(ident);
		}
		final String codeString = code.getName();
		if (ident.getLast().startsWith("()")) {
			ident = ident.removeStartingParenthesis();
			return getOrCreateLeaf1972(diagram, ident, ident.toCode(diagram), LeafType.DESCRIPTION, USymbol.INTERFACE,
					pure);
		}
		final char codeChar = codeString.length() > 2 ? codeString.charAt(0) : 0;
		final String tmp3 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeString, "\"([:");
		final Ident ident3 = diagram.buildFullyQualified(tmp3);
		final Code code3 = diagram.V1972() ? ident3 : diagram.buildCode(tmp3);
		if (codeChar == '(') {
			return getOrCreateLeaf1972(diagram, ident3, code3, LeafType.USECASE, USymbol.USECASE, pure);
		} else if (codeChar == ':') {
			return getOrCreateLeaf1972(diagram, ident3, code3, LeafType.DESCRIPTION,
					diagram.getSkinParam().actorStyle().toUSymbol(), pure);
		} else if (codeChar == '[') {
			final USymbol sym = diagram.getSkinParam().componentStyle().toUSymbol();
			return getOrCreateLeaf1972(diagram, ident3, code3, LeafType.DESCRIPTION, sym, pure);
		}

		return getOrCreateLeaf1972(diagram, ident, code, null, null, pure);
	}

	private ILeaf getOrCreateLeaf1972(DescriptionDiagram diagram, Ident ident, Code code, LeafType type, USymbol symbol,
			Ident pure) {
		if (diagram.V1972()) {
			final ILeaf result = pure.size() == 1 ? diagram.getLeafVerySmart(ident) : diagram.getLeafStrict(ident);
			// final ILeaf result = diagram.getLeafSmart(ident);
			if (result != null) {
				return result;
			}
		}
		return diagram.getOrCreateLeaf(ident, code, type, symbol);
	}

}
