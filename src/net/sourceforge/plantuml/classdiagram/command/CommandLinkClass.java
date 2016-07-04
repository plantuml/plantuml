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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 5436 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.StringTokenizer;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
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
import net.sourceforge.plantuml.graphic.HtmlColorSet;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;

final public class CommandLinkClass extends SingleLineCommand2<AbstractClassOrObjectDiagram> {

	public CommandLinkClass(UmlDiagramType umlDiagramType) {
		super(getRegexConcat(umlDiagramType));
	}

	static private RegexConcat getRegexConcat(UmlDiagramType umlDiagramType) {
		return new RegexConcat(
				new RegexLeaf("HEADER", "^(?:@([\\d.]+)[%s]+)?"), //
				new RegexOr(
				//
						new RegexLeaf("ENT1", "(?:" + optionalKeywords(umlDiagramType) + "[%s]+)?"
								+ getClassIdentifier()),
						new RegexLeaf("COUPLE1",
								"\\([%s]*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)[%s]*,[%s]*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)[%s]*\\)")),
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("FIRST_LABEL", "(?:[%g]([^%g]+)[%g])?"), //
				new RegexLeaf("[%s]*"), //

				new RegexConcat(
						//
						new RegexLeaf("ARROW_HEAD1", "([%s]+o|[#\\[<*+^]|[<\\[]\\|)?"), //
						new RegexLeaf("ARROW_BODY1", "([-=.]+)"), //
						new RegexLeaf("ARROW_STYLE1",
								"(?:\\[((?:#\\w+|dotted|dashed|plain|bold|hidden|norank)(?:,#\\w+|,dotted|,dashed|,plain|,bold|,hidden|,norank)*)\\])?"),
						new RegexLeaf("ARROW_DIRECTION", "(left|right|up|down|le?|ri?|up?|do?)?"), //
						new RegexLeaf("INSIDE", "(?:(0|\\(0\\)|\\(0|0\\))(?=[-=.~]))?"), //
						new RegexLeaf("ARROW_STYLE2",
								"(?:\\[((?:#\\w+|dotted|dashed|plain|bold|hidden|norank)(?:,#\\w+|,dotted|,dashed|,plain|,bold|,hidden|,norank)*)\\])?"),
						new RegexLeaf("ARROW_BODY2", "([-=.]*)"), //
						new RegexLeaf("ARROW_HEAD2", "(o[%s]+|[#\\]>*+^]|\\|[>\\]])?")), //

				new RegexLeaf("[%s]*"), //
				new RegexLeaf("SECOND_LABEL", "(?:[%g]([^%g]+)[%g])?"),
				new RegexLeaf("[%s]*"), //
				new RegexOr(
						new RegexLeaf("ENT2", "(?:" + optionalKeywords(umlDiagramType) + "[%s]+)?"
								+ getClassIdentifier()),
						new RegexLeaf("COUPLE2",
								"\\([%s]*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)[%s]*,[%s]*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)[%s]*\\)")),
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("LABEL_LINK", "(?::[%s]*(.+))?"), //
				new RegexLeaf("$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.LINE);
	}

	private static String getClassIdentifier() {
		return "(" + getSeparator() + "?[\\p{L}0-9_$]+(?:" + getSeparator() + "[\\p{L}0-9_$]+)*|[%g][^%g]+[%g])";
	}

	public static String getSeparator() {
		return "(?:\\.|::|\\\\|\\\\\\\\)";
	}

	private static String optionalKeywords(UmlDiagramType type) {
		if (type == UmlDiagramType.CLASS) {
			return "(interface|enum|annotation|abstract[%s]+class|abstract|class|object)";
		}
		if (type == UmlDiagramType.OBJECT) {
			return "(object)";
		}
		throw new IllegalArgumentException();
	}

	private LeafType getTypeIfObject(String type) {
		if ("object".equalsIgnoreCase(type)) {
			return LeafType.OBJECT;
		}
		return null;
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractClassOrObjectDiagram diagram, RegexResult arg) {

		Code ent1 = Code.of(arg.get("ENT1", 1));
		Code ent2 = Code.of(arg.get("ENT2", 1));

		if (ent1 == null) {
			return executeArgSpecial1(diagram, arg);
		}
		if (ent2 == null) {
			return executeArgSpecial2(diagram, arg);
		}
		ent1 = ent1.eventuallyRemoveStartingAndEndingDoubleQuote("\"");
		ent2 = ent2.eventuallyRemoveStartingAndEndingDoubleQuote("\"");
		if (diagram.isGroup(ent1) && diagram.isGroup(ent2)) {
			return executePackageLink(diagram, arg);
		}

		String port1 = null;
		String port2 = null;

		if (removeMemberPart(diagram, ent1) != null) {
			port1 = ent1.getPortMember();
			ent1 = removeMemberPart(diagram, ent1);
		}
		if (removeMemberPart(diagram, ent2) != null) {
			port2 = ent2.getPortMember();
			ent2 = removeMemberPart(diagram, ent2);
		}

		final String type1 = arg.get("ENT1", 0);
		final LeafType typeIfObject1 = getTypeIfObject(type1);

		final IEntity cl1 = diagram.isGroup(ent1) ? diagram.getGroup(Code.of(StringUtils
				.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 1), "\""))) : diagram.getOrCreateLeaf(
				ent1, typeIfObject1, null);

		final String type2 = arg.get("ENT2", 0);
		LeafType typeIfObject2 = getTypeIfObject(type2);
		if (diagram.leafExist(ent2) == false && cl1.getEntityType() == LeafType.OBJECT && typeIfObject2 == null) {
			typeIfObject2 = LeafType.OBJECT;
		}

		final IEntity cl2 = diagram.isGroup(ent2) ? diagram.getGroup(Code.of(StringUtils
				.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 1), "\""))) : diagram.getOrCreateLeaf(
				ent2, typeIfObject2, null);

		if (arg.get("ENT1", 0) != null) {
			final LeafType type = LeafType.getLeafType(arg.get("ENT1", 0));
			if (type != LeafType.OBJECT) {
				((ILeaf) cl1).muteToType(type, null);
			}
		}
		if (arg.get("ENT2", 0) != null) {
			final LeafType type = LeafType.getLeafType(arg.get("ENT2", 0));
			if (type != LeafType.OBJECT) {
				((ILeaf) cl2).muteToType(type, null);
			}
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final int queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = 1;
		} else {
			queue = getQueueLength(arg);
		}

		String firstLabel = arg.get("FIRST_LABEL", 0);
		String secondLabel = arg.get("SECOND_LABEL", 0);

		String labelLink = null;

		if (arg.get("LABEL_LINK", 0) != null) {
			labelLink = arg.get("LABEL_LINK", 0);
			if (firstLabel == null && secondLabel == null) {
				final Pattern2 p1 = MyPattern.cmpile("^[%g]([^%g]+)[%g]([^%g]+)[%g]([^%g]+)[%g]$");
				final Matcher2 m1 = p1.matcher(labelLink);
				if (m1.matches()) {
					firstLabel = m1.group(1);
					labelLink = StringUtils.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(
							StringUtils.trin(m1.group(2)), "\""));
					secondLabel = m1.group(3);
				} else {
					final Pattern2 p2 = MyPattern.cmpile("^[%g]([^%g]+)[%g]([^%g]+)$");
					final Matcher2 m2 = p2.matcher(labelLink);
					if (m2.matches()) {
						firstLabel = m2.group(1);
						labelLink = StringUtils.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(
								StringUtils.trin(m2.group(2)), "\""));
						secondLabel = null;
					} else {
						final Pattern2 p3 = MyPattern.cmpile("^([^%g]+)[%g]([^%g]+)[%g]$");
						final Matcher2 m3 = p3.matcher(labelLink);
						if (m3.matches()) {
							firstLabel = null;
							labelLink = StringUtils.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(
									StringUtils.trin(m3.group(1)), "\""));
							secondLabel = m3.group(2);
						}
					}
				}
			}
			labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(labelLink, "\"");
		}

		LinkArrow linkArrow = LinkArrow.NONE;
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

		Link link = new Link(cl1, cl2, linkType, Display.getWithNewlines(labelLink), queue, firstLabel, secondLabel,
				diagram.getLabeldistance(), diagram.getLabelangle());
		link.setPortMembers(port1, port2);

		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}
		link.setLinkArrow(linkArrow);
		colors = applyStyle(arg.getLazzy("ARROW_STYLE", 0), link, colors);
		link.setColors(colors);

		addLink(diagram, link, arg.get("HEADER", 0));

		return CommandExecutionResult.ok();
	}

	private Code removeMemberPart(AbstractClassOrObjectDiagram diagram, Code code) {
		if (diagram.leafExist(code)) {
			return null;
		}
		final Code before = code.removeMemberPart();
		if (before == null) {
			return null;
		}
		if (diagram.leafExist(before) == false) {
			return null;
		}
		return before;
	}

	// private CommandExecutionResult executeLinkFields(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
	// System.err.println("field1=" + arg.get("ENT1", 1));
	// System.err.println("field2=" + arg.get("ENT2", 1));
	// return CommandExecutionResult.error("not working yet");
	// }

	private void addLink(AbstractClassOrObjectDiagram diagram, Link link, String weight) {
		diagram.addLink(link);
		if (weight == null) {
			// final LinkType type = link.getType();
			// --|> highest
			// --*, -->, --o normal
			// ..*, ..>, ..o lowest
			// if (type.isDashed() == false) {
			// if (type.contains(LinkDecor.EXTENDS)) {
			// link.setWeight(3);
			// }
			// if (type.contains(LinkDecor.ARROW) ||
			// type.contains(LinkDecor.COMPOSITION)
			// || type.contains(LinkDecor.AGREGATION)) {
			// link.setWeight(2);
			// }
			// }
		} else {
			link.setWeight(Double.parseDouble(weight));
		}
	}

	private CommandExecutionResult executePackageLink(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		final IEntity cl1 = diagram.getGroup(Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(
				arg.get("ENT1", 1), "\"")));
		final IEntity cl2 = diagram.getGroup(Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(
				arg.get("ENT2", 1), "\"")));

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final int queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = 1;
		} else {
			queue = getQueueLength(arg);
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final Display labelLink = Display.getWithNewlines(arg.get("LABEL_LINK", 0));
		final String firstLabel = arg.get("FIRST_LABEL", 0);
		final String secondLabel = arg.get("SECOND_LABEL", 0);
		final Link link = new Link(cl1, cl2, linkType, labelLink, queue, firstLabel, secondLabel,
				diagram.getLabeldistance(), diagram.getLabelangle());

		diagram.resetPragmaLabel();

		colors = applyStyle(arg.getLazzy("ARROW_STYLE", 0), link, colors);
		link.setColors(colors);

		addLink(diagram, link, arg.get("HEADER", 0));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial1(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		final Code clName1 = Code.of(arg.get("COUPLE1", 0));
		final Code clName2 = Code.of(arg.get("COUPLE1", 1));
		if (diagram.leafExist(clName1) == false) {
			return CommandExecutionResult.error("No class " + clName1);
		}
		if (diagram.leafExist(clName2) == false) {
			return CommandExecutionResult.error("No class " + clName2);
		}

		final Code ent2 = Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 1), "\""));
		final IEntity cl2 = diagram.getOrCreateLeaf(ent2, null, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));
		// final int length = getQueueLength(arg);
		// final String weight = arg.get("HEADER").get(0);

		final boolean result = diagram.associationClass(1, clName1, clName2, cl2, linkType, label);
		if (result == false) {
			return CommandExecutionResult.error("Cannot have more than 2 assocications");
		}

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial2(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		final Code clName1 = Code.of(arg.get("COUPLE2", 0));
		final Code clName2 = Code.of(arg.get("COUPLE2", 1));
		if (diagram.leafExist(clName1) == false) {
			return CommandExecutionResult.error("No class " + clName1);
		}
		if (diagram.leafExist(clName2) == false) {
			return CommandExecutionResult.error("No class " + clName2);
		}

		final Code ent1 = Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 1), "\""));
		final IEntity cl1 = diagram.getOrCreateLeaf(ent1, null, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));
		// final int length = getQueueLength(arg);
		// final String weight = arg.get("HEADER").get(0);

		final boolean result = diagram.associationClass(2, clName1, clName2, cl1, linkType, label);
		if (result == false) {
			return CommandExecutionResult.error("Cannot have more than 2 assocications");
		}

		return CommandExecutionResult.ok();
	}

	private LinkDecor getDecors1(String s) {
		if (s == null) {
			return LinkDecor.NONE;
		}
		s = StringUtils.trin(s);
		if ("<|".equals(s)) {
			return LinkDecor.EXTENDS;
		}
		if ("<".equals(s)) {
			return LinkDecor.ARROW;
		}
		if ("^".equals(s)) {
			return LinkDecor.EXTENDS;
		}
		if ("+".equals(s)) {
			return LinkDecor.PLUS;
		}
		if ("o".equals(s)) {
			return LinkDecor.AGREGATION;
		}
		if ("*".equals(s)) {
			return LinkDecor.COMPOSITION;
		}
		if ("#".equals(s)) {
			return LinkDecor.SQUARRE;
		}
		return LinkDecor.NONE;
	}

	private LinkDecor getDecors2(String s) {
		if (s == null) {
			return LinkDecor.NONE;
		}
		s = StringUtils.trin(s);
		if ("|>".equals(s)) {
			return LinkDecor.EXTENDS;
		}
		if (">".equals(s)) {
			return LinkDecor.ARROW;
		}
		if ("^".equals(s)) {
			return LinkDecor.EXTENDS;
		}
		if ("+".equals(s)) {
			return LinkDecor.PLUS;
		}
		if ("o".equals(s)) {
			return LinkDecor.AGREGATION;
		}
		if ("*".equals(s)) {
			return LinkDecor.COMPOSITION;
		}
		if ("#".equals(s)) {
			return LinkDecor.SQUARRE;
		}
		return LinkDecor.NONE;
	}

	private LinkType getLinkType(RegexResult arg) {
		final LinkDecor decors1 = getDecors1(arg.get("ARROW_HEAD1", 0));
		final LinkDecor decors2 = getDecors2(arg.get("ARROW_HEAD2", 0));

		LinkType result = new LinkType(decors2, decors1);
		if (arg.get("ARROW_BODY1", 0).contains(".") || arg.get("ARROW_BODY2", 0).contains(".")) {
			result = result.getDashed();
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

	private int getQueueLength(RegexResult arg) {
		String s = getFullArrow(arg);
		s = s.replaceAll("[^-.=]", "");
		return s.length();
	}

	private Direction getDirection(RegexResult arg) {
		final LinkDecor decors1 = getDecors1(arg.get("ARROW_HEAD1", 0));
		final LinkDecor decors2 = getDecors2(arg.get("ARROW_HEAD2", 0));

		String s = getFullArrow(arg);
		s = s.replaceAll("[^-.=\\w]", "");
		if (s.startsWith("o")) {
			s = s.substring(1);
		}
		if (s.endsWith("o")) {
			s = s.substring(0, s.length() - 1);
		}

		Direction result = StringUtils.getQueueDirection(s);
		if (isInversed(decors1, decors2) && s.matches(".*\\w.*")) {
			result = result.getInv();
		}

		return result;
	}

	private String getFullArrow(RegexResult arg) {
		return notNull(arg.get("ARROW_HEAD1", 0)) + notNull(arg.get("ARROW_BODY1", 0))
				+ notNull(arg.get("ARROW_DIRECTION", 0)) + notNull(arg.get("ARROW_BODY2", 0))
				+ notNull(arg.get("ARROW_HEAD2", 0));
	}

	public static String notNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	@Deprecated
	public static Colors applyStyle(String arrowStyle, Link link) {
		return applyStyle(arrowStyle, link, null);
	}

	public static Colors applyStyle(String arrowStyle, Link link, Colors colors) {
		if (arrowStyle == null) {
			return colors;
		}
		final StringTokenizer st = new StringTokenizer(arrowStyle, ",");
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			if (s.equalsIgnoreCase("dashed")) {
				link.goDashed();
			} else if (s.equalsIgnoreCase("bold")) {
				link.goBold();
			} else if (s.equalsIgnoreCase("dotted")) {
				link.goDotted();
			} else if (s.equalsIgnoreCase("hidden")) {
				link.goHidden();
			} else if (s.equalsIgnoreCase("plain")) {
				// Do nothing
			} else if (s.equalsIgnoreCase("norank")) {
				link.goNorank();
			} else {
				link.setSpecificColor(s);
				if (colors != null) {
					colors = colors.add(ColorType.LINE, HtmlColorSet.getInstance().getColorIfValid(s));
				}
			}
		}
		return colors;
	}

	private boolean isInversed(LinkDecor decors1, LinkDecor decors2) {
		if (decors1 == LinkDecor.ARROW && decors2 != LinkDecor.ARROW) {
			return true;
		}
		if (decors2 == LinkDecor.AGREGATION) {
			return true;
		}
		if (decors2 == LinkDecor.COMPOSITION) {
			return true;
		}
		if (decors2 == LinkDecor.PLUS) {
			return true;
		}
		// if (decors2 == LinkDecor.EXTENDS) {
		// return true;
		// }
		return false;
	}

}
