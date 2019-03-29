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
 */
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkArrow;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;

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

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				getGroup("ENT1"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("LABEL1", "(?:[%g]([^%g]+)[%g])?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("HEAD2", "(0\\)|<<|[<^*+#0)]|<\\||[%s]+o)?"), //
				new RegexLeaf("BODY1", "([-=.~]+)"), //
				new RegexLeaf("ARROW_STYLE1", "(?:\\[(" + LINE_STYLE_MUTILPLES + ")\\])?"), //
				new RegexLeaf("DIRECTION", "(?:(left|right|up|down|le?|ri?|up?|do?)(?=[-=.~0()]))?"), //
				new RegexLeaf("INSIDE", "(?:(0|\\(0\\)|\\(0|0\\))(?=[-=.~]))?"), //
				new RegexLeaf("ARROW_STYLE2", "(?:\\[(" + LINE_STYLE + ")\\])?"), //
				new RegexLeaf("BODY2", "([-=.~]*)"), //
				new RegexLeaf("HEAD1", "(\\(0|>>|[>^*+#0(]|\\|>|o[%s]+)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("LABEL2", "(?:[%g]([^%g]+)[%g])?"), //
				new RegexLeaf("[%s]*"), //
				getGroup("ENT2"), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("LABEL_LINK", "(?::[%s]*(.+))?$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.LINE);
	}

	private LinkType getLinkType(RegexResult arg) {
		final String head1 = trimAndLowerCase(arg.get("HEAD1", 0));
		final String head2 = trimAndLowerCase(arg.get("HEAD2", 0));
		LinkDecor d1 = LinkDecor.NONE;
		LinkDecor d2 = LinkDecor.NONE;

		if (head1.equals("(0")) {
			d1 = LinkDecor.CIRCLE_CONNECT;
		} else if (head1.equals("#")) {
			d1 = LinkDecor.SQUARE;
		} else if (head1.equals("0")) {
			d1 = LinkDecor.CIRCLE;
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
		} else if (head1.equals(">>")) {
			d1 = LinkDecor.ARROW_TRIANGLE;
		} else if (head1.equals("^")) {
			d1 = LinkDecor.EXTENDS;
		} else if (head1.equals("|>")) {
			d1 = LinkDecor.EXTENDS;
		}

		if (head2.equals("0)")) {
			d2 = LinkDecor.CIRCLE_CONNECT;
		} else if (head2.equals("#")) {
			d2 = LinkDecor.SQUARE;
		} else if (head2.equals("0")) {
			d2 = LinkDecor.CIRCLE;
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
		return new RegexLeaf(
				name,
				"([\\p{L}0-9_.]+|\\(\\)[%s]*[\\p{L}0-9_.]+|\\(\\)[%s]*[%g][^%g]+[%g]|:[^:]+:|(?!\\[\\*\\])\\[[^\\[\\]]+\\]|\\((?!\\*\\))[^)]+\\))");
	}

	static class Labels {
		private String firstLabel;
		private String secondLabel;
		private String labelLink;
		private LinkArrow linkArrow = LinkArrow.NONE;

		Labels(RegexResult arg) {
			firstLabel = arg.get("LABEL1", 0);
			secondLabel = arg.get("LABEL2", 0);
			labelLink = arg.get("LABEL_LINK", 0);

			if (labelLink != null) {
				if (firstLabel == null && secondLabel == null) {
					init();
				}
				labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(labelLink);

				if ("<".equals(labelLink)) {
					linkArrow = LinkArrow.BACKWARD;
					labelLink = null;
				} else if (">".equals(labelLink)) {
					linkArrow = LinkArrow.DIRECT_NORMAL;
					labelLink = null;
				} else if (labelLink != null && labelLink.startsWith("< ")) {
					linkArrow = LinkArrow.BACKWARD;
					labelLink = StringUtils.trin(labelLink.substring(2));
				} else if (labelLink != null && labelLink.startsWith("> ")) {
					linkArrow = LinkArrow.DIRECT_NORMAL;
					labelLink = StringUtils.trin(labelLink.substring(2));
				} else if (labelLink != null && labelLink.endsWith(" >")) {
					linkArrow = LinkArrow.DIRECT_NORMAL;
					labelLink = StringUtils.trin(labelLink.substring(0, labelLink.length() - 2));
				} else if (labelLink != null && labelLink.endsWith(" <")) {
					linkArrow = LinkArrow.BACKWARD;
					labelLink = StringUtils.trin(labelLink.substring(0, labelLink.length() - 2));
				}

			}

		}

		private void init() {
			final Pattern2 p1 = MyPattern.cmpile("^[%g]([^%g]+)[%g]([^%g]+)[%g]([^%g]+)[%g]$");
			final Matcher2 m1 = p1.matcher(labelLink);
			if (m1.matches()) {
				firstLabel = m1.group(1);
				labelLink = StringUtils.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils
						.trin(m1.group(2))));
				secondLabel = m1.group(3);
				return;
			}
			final Pattern2 p2 = MyPattern.cmpile("^[%g]([^%g]+)[%g]([^%g]+)$");
			final Matcher2 m2 = p2.matcher(labelLink);
			if (m2.matches()) {
				firstLabel = m2.group(1);
				labelLink = StringUtils.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils
						.trin(m2.group(2))));
				secondLabel = null;
				return;
			}
			final Pattern2 p3 = MyPattern.cmpile("^([^%g]+)[%g]([^%g]+)[%g]$");
			final Matcher2 m3 = p3.matcher(labelLink);
			if (m3.matches()) {
				firstLabel = null;
				labelLink = StringUtils.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils
						.trin(m3.group(1))));
				secondLabel = m3.group(2);
			}
		}

	}

	@Override
	protected CommandExecutionResult executeArg(DescriptionDiagram diagram, LineLocation location, RegexResult arg) {
		final Code ent1 = Code.of(arg.get("ENT1", 0));
		final Code ent2 = Code.of(arg.get("ENT2", 0));

		if (diagram.isGroup(ent1) && diagram.isGroup(ent2)) {
			return executePackageLink(diagram, arg);
		}

		final IEntity cl1 = diagram.isGroup(ent1) ? diagram.getGroup(Code.of(arg.get("ENT1", 0))) : getOrCreateLeaf(
				diagram, ent1);
		final IEntity cl2 = diagram.isGroup(ent2) ? diagram.getGroup(Code.of(arg.get("ENT2", 0))) : getOrCreateLeaf(
				diagram, ent2);

		// if (arg.get("ENT1", 1) != null) {
		// cl1.setStereotype(new Stereotype(arg.get("ENT1", 1)));
		// }
		// if (arg.get("ENT2", 1) != null) {
		// cl2.setStereotype(new Stereotype(arg.get("ENT2", 1)));
		// }

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		} else {
			queue = getQueue(arg);
		}

		final Labels labels = new Labels(arg);

		Link link = new Link(cl1, cl2, linkType, Display.getWithNewlines(labels.labelLink), queue.length(),
				labels.firstLabel, labels.secondLabel, diagram.getLabeldistance(), diagram.getLabelangle());
		link.setLinkArrow(labels.linkArrow);
		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}
		link.setColors(color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet()));
		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		if (arg.get("STEREOTYPE", 0) != null) {
			final Stereotype stereotype = new Stereotype(arg.get("STEREOTYPE", 0));
			link.setColors(link.getColors().applyStereotype(stereotype, diagram.getSkinParam(), ColorParam.arrow));
		}
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

	private ILeaf getOrCreateLeaf(DescriptionDiagram diagram, final Code code2) {
		final String code = code2.getFullName();
		if (code.startsWith("()")) {
			return diagram.getOrCreateLeaf(Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils
					.trin(code.substring(2)))), LeafType.DESCRIPTION, USymbol.INTERFACE);
		}
		final char codeChar = code.length() > 2 ? code.charAt(0) : 0;
		if (codeChar == '(') {
			return diagram.getOrCreateLeaf(code2.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:"),
					LeafType.USECASE, USymbol.USECASE);
		} else if (codeChar == ':') {
			return diagram.getOrCreateLeaf(code2.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:"),
					LeafType.DESCRIPTION, USymbol.ACTOR);
		} else if (codeChar == '[') {
			final USymbol sym = diagram.getSkinParam().useUml2ForComponent() ? USymbol.COMPONENT2 : USymbol.COMPONENT1;
			return diagram.getOrCreateLeaf(code2.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:"),
					LeafType.DESCRIPTION, sym);
		}

		return diagram.getOrCreateLeaf(code2, null, null);
	}

	private CommandExecutionResult executePackageLink(DescriptionDiagram diagram, RegexResult arg) {
		final IEntity cl1 = diagram.getGroup(Code.of(arg.get("ENT1", 0)));
		final IEntity cl2 = diagram.getGroup(Code.of(arg.get("ENT2", 0)));

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		} else {
			queue = getQueue(arg);
		}

		Link link = new Link(cl1, cl2, linkType, Display.getWithNewlines(arg.get("LABEL_LINK", 0)), queue.length());
		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}
		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

}
