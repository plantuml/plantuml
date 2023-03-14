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
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.descdiagram.command.Labels;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

final public class CommandLinkClass extends SingleLineCommand2<AbstractClassOrObjectDiagram> {

	private static final String SINGLE = "[.\\\\]{0,2}[%pLN_]+(?:[.\\\\]{1,2}[%pLN_]+)*";
	private static final String SINGLE_GUILLEMENT = "[%g][.\\\\]{0,2}[%pLN_]+(?:[.\\\\]{1,2}[%pLN_]+)*[%g]";
	private static final String SINGLE2 = "(?:" + SINGLE + "|" + SINGLE_GUILLEMENT + ")";
	private static final String COUPLE = "\\([%s]*(" + SINGLE2 + ")[%s]*,[%s]*(" + SINGLE2 + ")[%s]*\\)";

	public CommandLinkClass(UmlDiagramType umlDiagramType) {
		super(getRegexConcat(umlDiagramType));
	}

	static private RegexConcat getRegexConcat(UmlDiagramType umlDiagramType) {
		return RegexConcat.build(CommandLinkClass.class.getName() + umlDiagramType, RegexLeaf.start(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf("HEADER", "@([\\d.]+)"), //
								RegexLeaf.spaceOneOrMore() //
						)), new RegexOr( //
								new RegexLeaf("ENT1", getClassIdentifier()), //
								new RegexLeaf("COUPLE1", COUPLE)), //

				RegexLeaf.spaceZeroOrMore(), //

				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("[\\[]"), //
						new RegexLeaf("QUALIFIER1", "([^\\[\\]]+)"), //
						new RegexLeaf("[\\]]"), //
						RegexLeaf.spaceOneOrMore() //
				)), //
				new RegexOptional(new RegexLeaf("FIRST_LABEL", "[%g]([^%g]+)[%g]")), //

				RegexLeaf.spaceZeroOrMore(), //

				new RegexConcat(
						//
						optionalHead("ARROW_HEAD1", "(?<=[%s])+[ox]", "[)#\\[<*+^}]_?", "\\<_?\\|[\\:\\|]", "[<\\[]\\|",
								"\\}o", "\\}\\|", "\\|o", "\\|\\|"),
						new RegexLeaf("ARROW_BODY1", "([-=.]+)"), //
						new RegexLeaf("ARROW_STYLE1", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
						new RegexLeaf("ARROW_DIRECTION", "(left|right|up|down|le?|ri?|up?|do?)?"), //
						new RegexOptional(new RegexLeaf("INSIDE", "(0|\\(0\\)|\\(0|0\\))(?=[-=.~])")), //
						new RegexLeaf("ARROW_STYLE2", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
						new RegexLeaf("ARROW_BODY2", "([-=.]*)"), //
						optionalHead("ARROW_HEAD2", "[ox][%s]+", ":\\>\\>?", "_?\\>", "[(#\\]*+^\\{]", "[\\|:]\\|\\>",
								"\\|[>\\]]", "o\\{", "\\|\\{", "o\\|", "\\|\\|")), //

				RegexLeaf.spaceZeroOrMore(), //

				new RegexOptional(new RegexLeaf("SECOND_LABEL", "[%g]([^%g]+)[%g]")), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("[\\[]"), //
						new RegexLeaf("QUALIFIER2", "([^\\[\\]]+)"), //
						new RegexLeaf("[\\]]"), //
						RegexLeaf.spaceOneOrMore() //
				)), //

				RegexLeaf.spaceZeroOrMore(), //

				new RegexOr( //
						new RegexLeaf("ENT2", getClassIdentifier()), //
						new RegexLeaf("COUPLE2", COUPLE)), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("LABEL_LINK", "(.+)") //
						)), RegexLeaf.end());
	}

	private static IRegex optionalHead(String name, String... options) {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (String s : options) {
			if (sb.length() > 1)
				sb.append("|");
			sb.append(s);
		}
		sb.append(")?");
		return new RegexLeaf(name, sb.toString());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.LINE);
	}

	private static String getClassIdentifier() {
		return "(" + getSeparator() + "?[%pLN_$]+(?:" + getSeparator() + "[%pLN_$]+)*|[%g][^%g]+[%g])";
	}

	public static String getSeparator() {
		return "(?:\\.|::|\\\\|\\\\\\\\)";
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractClassOrObjectDiagram diagram, LineLocation location,
			RegexResult arg) throws NoSuchColorException {
		String ent1String = diagram.cleanId(arg.get("ENT1", 0));
		String ent2String = diagram.cleanId(arg.get("ENT2", 0));
		if (ent1String == null && ent2String == null)
			return executeArgSpecial3(diagram, arg);

		if (ent1String == null)
			return executeArgSpecial1(diagram, arg);

		if (ent2String == null)
			return executeArgSpecial2(diagram, arg);

//		if (isGroupButNotTheCurrentGroup(diagram, ent1String) && isGroupButNotTheCurrentGroup(diagram, ent2String)) {
//			return executePackageLink(diagram, arg);
//		}

		String port1 = null;
		String port2 = null;
		final LinkType linkType = getLinkType(arg);
		if (ent1String.contains("::") && diagram.firstWithName(ent1String) == null) {
			port1 = diagram.getPortId(ent1String);
			ent1String = diagram.removePortId(ent1String);
		}

		if (ent2String.contains("::") && diagram.firstWithName(ent2String) == null) {
			port2 = diagram.getPortId(ent2String);
			ent2String = diagram.removePortId(ent2String);
		}

		final Quark<Entity> quark1 = diagram.quarkInContext(true, ent1String);
		final Quark<Entity> quark2 = diagram.quarkInContext(true, ent2String);

		Entity cl1 = quark1.getData();
		if (cl1 == null)
			cl1 = diagram.reallyCreateLeaf(quark1, Display.getWithNewlines(quark1.getName()), LeafType.CLASS, null);
		Entity cl2 = quark2.getData();
		if (cl2 == null)
			cl2 = diagram.reallyCreateLeaf(quark2, Display.getWithNewlines(quark2.getName()), LeafType.CLASS, null);

		final Direction dir = getDirection(arg);
		final int queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT)
			queue = 1;
		else
			queue = getQueueLength(arg);

		final Labels labels = new Labels(arg);

		final String kal1 = arg.get("QUALIFIER1", 0);
		final String kal2 = arg.get("QUALIFIER2", 0);

		final LinkArg linkArg = LinkArg
				.build(labels.getDisplay(), queue, diagram.getSkinParam().classAttributeIconSize() > 0)
				.withQuantifier(labels.getFirstLabel(), labels.getSecondLabel())
				.withDistanceAngle(diagram.getLabeldistance(), diagram.getLabelangle()).withKal(kal1, kal2);

		Link link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(), cl1, cl2,
				linkType, linkArg);
		if (arg.get("URL", 0) != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(arg.get("URL", 0));
			link.setUrl(url);
		}
		link.setPortMembers(port1, port2);

		if (dir == Direction.LEFT || dir == Direction.UP)
			link = link.getInv();

		link.setLinkArrow(labels.getLinkArrow());
		link.setColors(color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet()));
		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		link.setCodeLine(location);

		addLink(diagram, link, arg.get("HEADER", 0));

		return CommandExecutionResult.ok();
	}

//	private boolean isGroupButNotTheCurrentGroup(AbstractClassOrObjectDiagram diagram, String code) {
//		if (diagram.getCurrentGroup().getCodeGetName().equals(code))
//			return false;
//
//		return diagram.isGroup(code);
//	}

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

	private CommandExecutionResult executePackageLink(AbstractClassOrObjectDiagram diagram, RegexResult arg)
			throws NoSuchColorException {
		final String ent1String = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 0), "\"");
		final String ent2String = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 0), "\"");
		final Entity cl1 = diagram.getGroup(ent1String);
		final Entity cl2 = diagram.getGroup(ent2String);

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final int queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT)
			queue = 1;
		else
			queue = getQueueLength(arg);

		final Display labelLink = Display.getWithNewlines(arg.get("LABEL_LINK", 0));
		final String firstLabel = arg.get("FIRST_LABEL", 0);
		final String secondLabel = arg.get("SECOND_LABEL", 0);
		final LinkArg linkArg = LinkArg.build(labelLink, queue, diagram.getSkinParam().classAttributeIconSize() > 0);
		final Link link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(), cl1,
				cl2, linkType, linkArg.withQuantifier(firstLabel, secondLabel)
						.withDistanceAngle(diagram.getLabeldistance(), diagram.getLabelangle()));
		link.setColors(color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet()));

		diagram.resetPragmaLabel();

		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));

		addLink(diagram, link, arg.get("HEADER", 0));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial1(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		final String name1A = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 0));
		final String name1B = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 1));

		final Quark<Entity> quark1A = diagram.quarkInContext(true, name1A);
		final Quark<Entity> quark1B = diagram.quarkInContext(true, name1B);

		if (quark1A.getData() != null == false)
			return CommandExecutionResult.error("No class " + name1A);

		if (quark1B.getData() != null == false)
			return CommandExecutionResult.error("No class " + name1B);

		final Entity cl1A = quark1A.getData();
		final Entity cl1B = quark1B.getData();

		final String id2 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 0), "\"");
		final Quark<Entity> ent2 = diagram.quarkInContext(true, id2);

		Entity cl2 = ent2.getData();
		if (cl2 == null)
			cl2 = diagram.reallyCreateLeaf(ent2, Display.getWithNewlines(ent2.getName()), LeafType.CLASS, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));

		final boolean result = diagram.associationClass(1, cl1A, cl1B, cl2, linkType, label);
		if (result == false)
			return CommandExecutionResult.error("Cannot have more than 2 assocications");

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial2(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		final String name2A = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE2", 0));
		final String name2B = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE2", 1));

		final Quark<Entity> quark2A = diagram.quarkInContext(true, name2A);
		final Quark<Entity> quark2B = diagram.quarkInContext(true, name2B);

		if (quark2A.getData() != null == false)
			return CommandExecutionResult.error("No class " + name2A);

		if (quark2B.getData() != null == false)
			return CommandExecutionResult.error("No class " + name2B);

		final Entity cl2A = quark2A.getData();
		final Entity cl2B = quark2B.getData();

		final String id1 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 0), "\"");
		final Quark<Entity> ent1 = diagram.quarkInContext(true, id1);

		Entity cl1 = (Entity) ent1.getData();
		if (cl1 == null)
			cl1 = diagram.reallyCreateLeaf(ent1, Display.getWithNewlines(ent1.getName()), LeafType.CLASS, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));

		final boolean result = diagram.associationClass(2, cl2A, cl2B, cl1, linkType, label);
		if (result == false)
			return CommandExecutionResult.error("Cannot have more than 2 assocications");

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial3(AbstractClassOrObjectDiagram diagram, RegexResult arg) {

		final String name1A = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 0));
		final String name1B = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 1));
		final String name2A = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE2", 0));
		final String name2B = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE2", 1));

		final Quark<Entity> quark1A = diagram.quarkInContext(true, name1A);
		final Quark<Entity> quark1B = diagram.quarkInContext(true, name1B);
		final Quark<Entity> quark2A = diagram.quarkInContext(true, name2A);
		final Quark<Entity> quark2B = diagram.quarkInContext(true, name2B);

		if (quark1A.getData() != null == false)
			return CommandExecutionResult.error("No class " + name1A);
		if (quark1B.getData() != null == false)
			return CommandExecutionResult.error("No class " + name1B);
		if (quark2A.getData() != null == false)
			return CommandExecutionResult.error("No class " + name2A);
		if (quark2B.getData() != null == false)
			return CommandExecutionResult.error("No class " + name2B);

		final Entity cl1A = (Entity) quark1A.getData();
		final Entity cl1B = (Entity) quark1B.getData();
		final Entity cl2A = (Entity) quark2A.getData();
		final Entity cl2B = (Entity) quark2B.getData();

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));

		return diagram.associationClass(cl1A, cl1B, cl2A, cl2B, linkType, label);
	}

	private LinkDecor getDecors1(String s) {
		if (s == null)
			return LinkDecor.NONE;

		s = StringUtils.trin(s);
		if ("<|".equals(s))
			return LinkDecor.EXTENDS;

		if ("<|:".equals(s))
			return LinkDecor.DEFINEDBY;

		if ("<||".equals(s))
			return LinkDecor.REDEFINES;

		if ("}".equals(s))
			return LinkDecor.CROWFOOT;

		if ("}o".equals(s))
			return LinkDecor.CIRCLE_CROWFOOT;

		if ("}|".equals(s))
			return LinkDecor.LINE_CROWFOOT;

		if ("|o".equals(s))
			return LinkDecor.CIRCLE_LINE;

		if ("||".equals(s))
			return LinkDecor.DOUBLE_LINE;

		if ("<".equals(s))
			return LinkDecor.ARROW;

		if ("^".equals(s))
			return LinkDecor.EXTENDS;

		if ("+".equals(s))
			return LinkDecor.PLUS;

		if ("o".equals(s))
			return LinkDecor.AGREGATION;

		if ("x".equals(s))
			return LinkDecor.NOT_NAVIGABLE;

		if ("*".equals(s))
			return LinkDecor.COMPOSITION;

		if ("#".equals(s))
			return LinkDecor.SQUARE;

		if (")".equals(s))
			return LinkDecor.PARENTHESIS;

		return LinkDecor.NONE;
	}

	private LinkDecor getDecors2(String s) {
		if (s == null)
			return LinkDecor.NONE;

		s = StringUtils.trin(s);
		if ("|>".equals(s))
			return LinkDecor.EXTENDS;

		if (":|>".equals(s))
			return LinkDecor.DEFINEDBY;

		if ("||>".equals(s))
			return LinkDecor.REDEFINES;

		if (">".equals(s))
			return LinkDecor.ARROW;

		if ("{".equals(s))
			return LinkDecor.CROWFOOT;

		if ("o{".equals(s))
			return LinkDecor.CIRCLE_CROWFOOT;

		if ("|{".equals(s))
			return LinkDecor.LINE_CROWFOOT;

		if ("o|".equals(s))
			return LinkDecor.CIRCLE_LINE;

		if ("||".equals(s))
			return LinkDecor.DOUBLE_LINE;

		if ("^".equals(s))
			return LinkDecor.EXTENDS;

		if ("+".equals(s))
			return LinkDecor.PLUS;

		if ("o".equals(s))
			return LinkDecor.AGREGATION;

		if ("x".equals(s))
			return LinkDecor.NOT_NAVIGABLE;

		if ("*".equals(s))
			return LinkDecor.COMPOSITION;

		if ("#".equals(s))
			return LinkDecor.SQUARE;

		if ("(".equals(s))
			return LinkDecor.PARENTHESIS;

		return LinkDecor.NONE;
	}

	private LinkType getLinkType(RegexResult arg) {
		final LinkDecor decors1 = getDecors1(getArrowHead1(arg));
		final LinkDecor decors2 = getDecors2(getArrowHead2(arg));

		LinkType result = new LinkType(decors2, decors1);
		if (arg.get("ARROW_BODY1", 0).contains(".") || arg.get("ARROW_BODY2", 0).contains("."))
			result = result.goDashed();

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

	private int getQueueLength(RegexResult arg) {
		String s = getFullArrow(arg);
		s = s.replaceAll("[^-.=]", "");
		return s.length();
	}

	private Direction getDirection(RegexResult arg) {
//		final LinkDecor decors1 = getDecors1(getArrowHead1(arg));
//		final LinkDecor decors2 = getDecors2(getArrowHead2(arg));

		String s = getFullArrow(arg);
		s = s.replaceAll("[^-.=\\w]", "");
		if (s.startsWith("o"))
			s = s.substring(1);

		if (s.endsWith("o"))
			s = s.substring(0, s.length() - 1);

		Direction result = StringUtils.getQueueDirection(s);
//		if (isInversed(decors1, decors2) && s.matches(".*\\w.*")) {
		// result = result.getInv();
//		}

		return result;
	}

	private String getArrowHead1(RegexResult arg) {
		return getArrowHead(arg, "ARROW_HEAD1");
	}

	private String getArrowHead2(RegexResult arg) {
		return getArrowHead(arg, "ARROW_HEAD2");
	}

	private String getArrowHead(RegexResult arg, final String key) {
		return notNull(arg.get(key, 0)).replaceAll("_", "");
	}

	private String getFullArrow(RegexResult arg) {
		return getArrowHead1(arg) + notNull(arg.get("ARROW_BODY1", 0)) + notNull(arg.get("ARROW_DIRECTION", 0))
				+ notNull(arg.get("ARROW_BODY2", 0)) + getArrowHead2(arg);
	}

	public static String notNull(String s) {
		if (s == null)
			return "";
		return s;
	}

}
