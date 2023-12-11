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
 * Contribution :  Hisashi Miyashita
 *
 */
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

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
//				new RegexOptional(new RegexLeaf("STEREO1", "(\\<\\<.*\\>\\>)")), //
//				RegexLeaf.spaceZeroOrMore(), //

				new RegexLeaf("HEAD2", "(0\\)|<<|<_|[<^*+#0@)]|<\\|[\\|\\:]?|[%s]+o)?"), //
				new RegexLeaf("BODY1", "([-=.~]+)"), //
				new RegexLeaf("ARROW_STYLE1", "(?:\\[(" + LINE_STYLE_MUTILPLES + ")\\])?"), //
				new RegexOptional(new RegexLeaf("DIRECTION", "(left|right|up|down|le?|ri?|up?|do?)(?=[-=.~0()\\[])")), //
				new RegexOptional(new RegexLeaf("INSIDE", "(0|\\(0\\)|\\(0|0\\))(?=[-=.~])")), //
				new RegexLeaf("ARROW_STYLE2", "(?:\\[(" + LINE_STYLE + ")\\])?"), //
				new RegexLeaf("BODY2", "([-=.~]*)"), //
				new RegexLeaf("HEAD1", "(\\(0|>>|_>|[>^*+#0@(]|[\\:\\|]?\\|>|\\\\\\\\|o[%s]+)?"), //

				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("SECOND_LABEL", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //

				getGroup("ENT2"), //
				RegexLeaf.spaceZeroOrMore(), //
//				new RegexOptional(new RegexLeaf("STEREO2", "(\\<\\<.*\\>\\>)")), //
//				RegexLeaf.spaceZeroOrMore(), //

				color().getRegex(), //
				StereotypePattern.optional("STEREOTYPE"), //
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

		if (head1.equals("(0"))
			d1 = LinkDecor.CIRCLE_CONNECT;
		else if (head1.equals("#"))
			d1 = LinkDecor.SQUARE;
		else if (head1.equals("0"))
			d1 = LinkDecor.CIRCLE;
		else if (head1.equals("@"))
			d1 = LinkDecor.CIRCLE_FILL;
		else if (head1.equals("("))
			d1 = LinkDecor.PARENTHESIS;
		else if (head1.equals(">"))
			d1 = LinkDecor.ARROW;
		else if (head1.equals("*"))
			d1 = LinkDecor.COMPOSITION;
		else if (head1.equals("o"))
			d1 = LinkDecor.AGREGATION;
		else if (head1.equals("+"))
			d1 = LinkDecor.PLUS;
		else if (head1.equals("\\\\"))
			d1 = LinkDecor.HALF_ARROW;
		else if (head1.equals(">>"))
			d1 = LinkDecor.ARROW_TRIANGLE;
		else if (head1.equals("^"))
			d1 = LinkDecor.EXTENDS;
		else if (head1.equals(":|>"))
			d1 = LinkDecor.DEFINEDBY;
		else if (head1.equals("||>"))
			d1 = LinkDecor.REDEFINES;
		else if (head1.equals("|>"))
			d1 = LinkDecor.EXTENDS;

		if (head2.equals("0)"))
			d2 = LinkDecor.CIRCLE_CONNECT;
		else if (head2.equals("#"))
			d2 = LinkDecor.SQUARE;
		else if (head2.equals("0"))
			d2 = LinkDecor.CIRCLE;
		else if (head2.equals("@"))
			d2 = LinkDecor.CIRCLE_FILL;
		else if (head2.equals(")"))
			d2 = LinkDecor.PARENTHESIS;
		else if (head2.equals("<"))
			d2 = LinkDecor.ARROW;
		else if (head2.equals("*"))
			d2 = LinkDecor.COMPOSITION;
		else if (head2.equals("o"))
			d2 = LinkDecor.AGREGATION;
		else if (head2.equals("+"))
			d2 = LinkDecor.PLUS;
		else if (head2.equals("<<"))
			d2 = LinkDecor.ARROW_TRIANGLE;
		else if (head2.equals("^"))
			d2 = LinkDecor.EXTENDS;
		else if (head2.equals("<|:"))
			d2 = LinkDecor.DEFINEDBY;
		else if (head2.equals("<||"))
			d2 = LinkDecor.REDEFINES;
		else if (head2.equals("<|"))
			d2 = LinkDecor.EXTENDS;

		LinkType result = new LinkType(d1, d2);
		final String queue = getQueue(arg);
		if (queue.contains("."))
			result = result.goDashed();
		else if (queue.contains("~"))
			result = result.goDotted();
		else if (queue.contains("="))
			result = result.goBold();

		final String middle = arg.get("INSIDE", 0);
		if ("0".equals(middle))
			result = result.withMiddleCircle();
		else if ("0)".equals(middle))
			result = result.withMiddleCircleCircled1();
		else if ("(0".equals(middle))
			result = result.withMiddleCircleCircled2();
		else if ("(0)".equals(middle))
			result = result.withMiddleCircleCircled();

		return result;
	}

	private static String trimAndLowerCase(String s) {
		if (s == null)
			return "";

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
		return new RegexLeaf(name, "(" + //
				"[%pLN_.]+" + //
				"|" + //
				"[%g][^%g]+[%g]" + //
				"|" + //
				"\\(\\)[%s]*[%pLN_.]+" + //
				"|" + //
				"\\(\\)[%s]*[%g][^%g]+[%g]" + //
				"|" + //
				":[^:]+:/?" + //
				"|" + //
				"(?!\\[\\*\\])\\[[^\\[\\]]+\\]" + //
				"|" + //
				"\\((?!\\*\\))[^)]+\\)/?" + //
				")");
	}

	@Override
	protected CommandExecutionResult executeArg(DescriptionDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String ent1 = arg.get("ENT1", 0);
		final String ent2 = arg.get("ENT2", 0);
		final String ent1clean = diagram.cleanId(ent1);
		final String ent2clean = diagram.cleanId(ent2);

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT)
			queue = "-";
		else
			queue = getQueue(arg);

		final Labels labels = new Labels(arg);

		final Entity cl1;
		final Entity cl2;
		if (diagram.isGroup(ent1clean) && diagram.isGroup(ent2clean)) {
			cl1 = diagram.getGroup(ent1clean);
			cl2 = diagram.getGroup(ent2clean);
		} else {
			cl1 = getDummy(diagram, ent1);
			cl2 = getDummy(diagram, ent2);
		}
		final LinkArg linkArg = LinkArg.build(Display.getWithNewlines(labels.getLabelLink()), queue.length(),
				diagram.getSkinParam().classAttributeIconSize() > 0);
		Link link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(), cl1, cl2,
				linkType, linkArg.withQuantifier(labels.getFirstLabel(), labels.getSecondLabel())
						.withDistanceAngle(diagram.getLabeldistance(), diagram.getLabelangle()));
		link.setLinkArrow(labels.getLinkArrow());
		if (dir == Direction.LEFT || dir == Direction.UP)
			link = link.getInv();

		link.setColors(color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet()));
		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		if (arg.get("STEREOTYPE", 0) != null) {
			final Stereotype stereotype = Stereotype.build(arg.get("STEREOTYPE", 0));
			link.setStereotype(stereotype);
		}
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

//	private String removeStartingParenthesis(String s) {
//		if (s.startsWith("()"))
//			return s.substring(2);
//		return s;
//	}

	private Entity getDummy(DescriptionDiagram diagram, String ident) {
		if (ident.startsWith("()")) {
			ident = diagram.cleanId(ident);
			final Quark<Entity> quark = diagram.quarkInContext(true, ident);
			if (quark.getData() != null)
				return quark.getData();
			return diagram.reallyCreateLeaf(quark, Display.getWithNewlines(quark.getName()), LeafType.DESCRIPTION,
					USymbols.INTERFACE);
		}

		final char codeChar = ident.length() > 2 ? ident.charAt(0) : 0;
		final boolean endWithSlash = ident.endsWith("/");
		ident = diagram.cleanId(ident);
		final Quark<Entity> quark = diagram.quarkInContext(true, ident);

		if (diagram.isGroup(quark))
			return quark.getData();
		if (quark.getData() != null)
			return quark.getData();
		final Display display = Display.getWithNewlines(quark.getName());

		if (codeChar == '(') {
			if (endWithSlash)
				return diagram.reallyCreateLeaf(quark, display, LeafType.USECASE_BUSINESS, USymbols.USECASE);
			else
				return diagram.reallyCreateLeaf(quark, display, LeafType.USECASE, USymbols.USECASE);
		} else if (codeChar == ':') {
			if (endWithSlash)
				return diagram.reallyCreateLeaf(quark, display, LeafType.DESCRIPTION,
						ActorStyle.STICKMAN_BUSINESS.toUSymbol());
			else
				return diagram.reallyCreateLeaf(quark, display, LeafType.DESCRIPTION,
						diagram.getSkinParam().actorStyle().toUSymbol());

		} else if (codeChar == '[') {
			final USymbol sym = diagram.getSkinParam().componentStyle().toUSymbol();
			return diagram.reallyCreateLeaf(quark, display, LeafType.DESCRIPTION, sym);
		}

		return diagram.reallyCreateLeaf(quark, display, LeafType.STILL_UNKNOWN, null);
	}

}
