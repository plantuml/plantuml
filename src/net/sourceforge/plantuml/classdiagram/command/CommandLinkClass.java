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
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.descdiagram.command.Labels;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;

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
				new RegexOptional(new RegexLeaf("FIRST_LABEL", "[%g]([^%g]+)[%g]")), //

				RegexLeaf.spaceZeroOrMore(), //

				new RegexConcat(
						//
						new RegexLeaf("ARROW_HEAD1",
								"([%s]+[ox]|[)#\\[<*+^}]|\\<_|\\<\\|[\\:\\|]|[<\\[]\\||\\}o|\\}\\||\\|o|\\|\\|)?"), //
						new RegexLeaf("ARROW_BODY1", "([-=.]+)"), //
						new RegexLeaf("ARROW_STYLE1", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
						new RegexLeaf("ARROW_DIRECTION", "(left|right|up|down|le?|ri?|up?|do?)?"), //
						new RegexOptional(new RegexLeaf("INSIDE", "(0|\\(0\\)|\\(0|0\\))(?=[-=.~])")), //
						new RegexLeaf("ARROW_STYLE2", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
						new RegexLeaf("ARROW_BODY2", "([-=.]*)"), //
						new RegexLeaf("ARROW_HEAD2",
								"([ox][%s]+|:\\>\\>?|_\\>|[(#\\]>*+^\\{]|[\\|\\:]\\|\\>|\\|[>\\]]|o\\{|\\|\\{|o\\||\\|\\|)?")), //
				RegexLeaf.spaceZeroOrMore(), new RegexOptional(new RegexLeaf("SECOND_LABEL", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr( //
						new RegexLeaf("ENT2", getClassIdentifier()), //
						new RegexLeaf("COUPLE2", COUPLE)), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("LABEL_LINK", "(.+)") //
						)), RegexLeaf.end());
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

		final String ent1String = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 0), "\"");
		final String ent2String = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 0), "\"");
		if (ent1String == null && ent2String == null) {
			return executeArgSpecial3(diagram, arg);
		}

		if (ent1String == null) {
			return executeArgSpecial1(diagram, arg);
		}
		if (ent2String == null) {
			return executeArgSpecial2(diagram, arg);
		}

		Ident ident1 = diagram.buildLeafIdentSpecial(ent1String);
		Ident ident2 = diagram.buildLeafIdentSpecial(ent2String);
		Ident ident1pure = Ident.empty().add(ent1String, diagram.getNamespaceSeparator());
		Ident ident2pure = Ident.empty().add(ent2String, diagram.getNamespaceSeparator());
		Code code1 = diagram.V1972() ? ident1 : diagram.buildCode(ent1String);
		Code code2 = diagram.V1972() ? ident2 : diagram.buildCode(ent2String);
		if (isGroupButNotTheCurrentGroup(diagram, code1, ident1)
				&& isGroupButNotTheCurrentGroup(diagram, code2, ident2)) {
			return executePackageLink(diagram, arg);
		}

		String port1 = null;
		String port2 = null;

		if (diagram.V1972()) {
			if ("::".equals(diagram.getNamespaceSeparator())) {
				if (removeMemberPartIdentSpecial(diagram, ident1) != null) {
					port1 = ident1.getLast();
					ident1 = removeMemberPartIdentSpecial(diagram, ident1);
					code1 = ident1;
				}
				if (removeMemberPartIdentSpecial(diagram, ident2) != null) {
					port2 = ident2.getLast();
					ident2 = removeMemberPartIdentSpecial(diagram, ident2);
					code2 = ident1;
				}
			} else {
				if (removeMemberPartIdent(diagram, ident1) != null) {
					port1 = ident1.getPortMember();
					ident1 = removeMemberPartIdent(diagram, ident1);
					code1 = ident1;
				}
				if (removeMemberPartIdent(diagram, ident2) != null) {
					port2 = ident2.getPortMember();
					ident2 = removeMemberPartIdent(diagram, ident2);
					code2 = ident2;
				}
			}
		} else {
			if (removeMemberPartLegacy1972(diagram, ident1) != null) {
				port1 = ident1.getPortMember();
				code1 = removeMemberPartLegacy1972(diagram, ident1);
				ident1 = ident1.removeMemberPart();
			}
			if (removeMemberPartLegacy1972(diagram, ident2) != null) {
				port2 = ident2.getPortMember();
				code2 = removeMemberPartLegacy1972(diagram, ident2);
				ident2 = ident2.removeMemberPart();
			}
		}

		final IEntity cl1 = getFoo1(diagram, code1, ident1, ident1pure);
		final IEntity cl2 = getFoo1(diagram, code2, ident2, ident2pure);

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final int queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = 1;
		} else {
			queue = getQueueLength(arg);
		}

		final Labels labels = new Labels(arg);

		Link link = new Link(cl1, cl2, linkType, labels.getDisplay(), queue, labels.getFirstLabel(),
				labels.getSecondLabel(), diagram.getLabeldistance(), diagram.getLabelangle(),
				diagram.getSkinParam().getCurrentStyleBuilder());
		if (arg.get("URL", 0) != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(arg.get("URL", 0));
			link.setUrl(url);
		}
		link.setPortMembers(port1, port2);

		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}
		link.setLinkArrow(labels.getLinkArrow());
		link.setColors(color().getColor(diagram.getSkinParam().getThemeStyle(), arg,
				diagram.getSkinParam().getIHtmlColorSet()));
		link.applyStyle(diagram.getSkinParam().getThemeStyle(), arg.getLazzy("ARROW_STYLE", 0));
		link.setCodeLine(location);

		addLink(diagram, link, arg.get("HEADER", 0));

		return CommandExecutionResult.ok();
	}

	private IEntity getFoo1(AbstractClassOrObjectDiagram diagram, Code code, Ident ident, Ident pure) {
		if (isGroupButNotTheCurrentGroup(diagram, code, ident)) {
			if (diagram.V1972()) {
				return diagram.getGroupVerySmart(ident);
			}
//			final Code tap = ident.toCode(diagram);
			return diagram.getGroup(code);
		}
		if (diagram.V1972()) {
			final IEntity result = pure.size() == 1 ? diagram.getLeafVerySmart(ident) : diagram.getLeafStrict(ident);
			if (result != null) {
				return result;
			}
		}
		return diagram.getOrCreateLeaf(ident, code, null, null);
	}

	private boolean isGroupButNotTheCurrentGroup(AbstractClassOrObjectDiagram diagram, Code code, Ident ident) {
		if (diagram.V1972()) {
			if (diagram.getCurrentGroup().getCodeGetName().equals(code.getName())) {
				return false;
			}
			return diagram.isGroupVerySmart(ident);
		} else {
			if (diagram.getCurrentGroup().getCodeGetName().equals(code.getName())) {
				return false;
			}
			return diagram.isGroup(code);
		}
	}

	private Ident removeMemberPartIdentSpecial(AbstractClassOrObjectDiagram diagram, Ident ident) {
		if (diagram.leafExistSmart(ident)) {
			return null;
		}
		final Ident before = ident.parent();
		if (before == null) {
			return null;
		}
		if (diagram.leafExistSmart(before) == false) {
			return null;
		}
		return before;
	}

	private Ident removeMemberPartIdent(AbstractClassOrObjectDiagram diagram, Ident ident) {
		if (diagram.leafExistSmart(ident)) {
			return null;
		}
		final Ident before = ident.removeMemberPart();
		if (before == null) {
			return null;
		}
		if (diagram.leafExistSmart(before) == false) {
			return null;
		}
		return before;
	}

	private Code removeMemberPartLegacy1972(AbstractClassOrObjectDiagram diagram, Ident ident) {
		if (diagram.leafExist(ident)) {
			return null;
		}
		final Ident before = ident.removeMemberPart();
		if (before == null) {
			return null;
		}
		final Code code = before.toCode(diagram);
		if (diagram.leafExist(code) == false) {
			return null;
		}
		return code;
	}

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
		final IEntity cl1 = diagram.V1972() ? diagram.getGroupVerySmart(diagram.buildLeafIdent(ent1String))
				: diagram.getGroup(diagram.buildCode(ent1String));
		final IEntity cl2 = diagram.V1972() ? diagram.getGroupVerySmart(diagram.buildLeafIdent(ent2String))
				: diagram.getGroup(diagram.buildCode(ent2String));

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final int queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = 1;
		} else {
			queue = getQueueLength(arg);
		}

		final Display labelLink = Display.getWithNewlines(arg.get("LABEL_LINK", 0));
		final String firstLabel = arg.get("FIRST_LABEL", 0);
		final String secondLabel = arg.get("SECOND_LABEL", 0);
		final Link link = new Link(cl1, cl2, linkType, labelLink, queue, firstLabel, secondLabel,
				diagram.getLabeldistance(), diagram.getLabelangle(), diagram.getSkinParam().getCurrentStyleBuilder());
		link.setColors(color().getColor(diagram.getSkinParam().getThemeStyle(), arg,
				diagram.getSkinParam().getIHtmlColorSet()));

		diagram.resetPragmaLabel();

		link.applyStyle(diagram.getSkinParam().getThemeStyle(), arg.getLazzy("ARROW_STYLE", 0));

		addLink(diagram, link, arg.get("HEADER", 0));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial1(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		if (diagram.V1972())
			return executeArgSpecial1972Ident1(diagram, arg);
		final String name1A = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 0));
		final String name1B = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 1));
		final Code clName1A = diagram.buildCode(name1A);
		final Code clName1B = diagram.buildCode(name1B);
		if (diagram.leafExist(clName1A) == false) {
			return CommandExecutionResult.error("No class " + clName1A);
		}
		if (diagram.leafExist(clName1B) == false) {
			return CommandExecutionResult.error("No class " + clName1B);
		}

		final String idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 0), "\"");
		final Code ent2 = diagram.buildCode(idShort);
		final IEntity cl2 = diagram.getOrCreateLeaf(diagram.buildLeafIdent(idShort), ent2, null, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));

		final boolean result = diagram.associationClass(1, name1A, name1B, cl2, linkType, label);
		if (result == false) {
			return CommandExecutionResult.error("Cannot have more than 2 assocications");
		}

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial1972Ident1(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		final String name1A = arg.get("COUPLE1", 0);
		final String name1B = arg.get("COUPLE1", 1);
		final Ident ident1A = diagram.buildLeafIdent(name1A);
		final Ident ident1B = diagram.buildLeafIdent(name1B);
		if (diagram.leafExistSmart(ident1A) == false) {
			return CommandExecutionResult.error("No class " + ident1A.getName());
		}
		if (diagram.leafExistSmart(ident1B) == false) {
			return CommandExecutionResult.error("No class " + ident1B.getName());
		}

		final String idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 0), "\"");
		final Ident ident2 = diagram.buildLeafIdent(idShort);
		final IEntity cl2 = diagram.getOrCreateLeaf(ident2, ident2, null, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));

		final boolean result = diagram.associationClass(1, name1A, name1B, cl2, linkType, label);
		if (result == false) {
			return CommandExecutionResult.error("Cannot have more than 2 assocications");
		}

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial1972Ident2(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		final String name2A = arg.get("COUPLE2", 0);
		final String name2B = arg.get("COUPLE2", 1);
		final Ident ident2A = diagram.buildLeafIdent(name2A);
		final Ident ident2B = diagram.buildLeafIdent(name2B);
		if (diagram.leafExistSmart(ident2A) == false) {
			return CommandExecutionResult.error("No class " + ident2A.getName());
		}
		if (diagram.leafExistSmart(ident2B) == false) {
			return CommandExecutionResult.error("No class " + ident2B.getName());
		}

		final String idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 0), "\"");
		final Ident ident1 = diagram.buildLeafIdent(idShort);
		final IEntity cl1 = diagram.getOrCreateLeaf(ident1, ident1, null, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));

		final boolean result = diagram.associationClass(2, name2A, name2B, cl1, linkType, label);
		if (result == false) {
			return CommandExecutionResult.error("Cannot have more than 2 assocications");
		}

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial1972Ident3(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		final String name1A = arg.get("COUPLE1", 0);
		final String name1B = arg.get("COUPLE1", 1);
		final String name2A = arg.get("COUPLE2", 0);
		final String name2B = arg.get("COUPLE2", 1);
		final Ident ident1A = diagram.buildLeafIdent(name1A);
		final Ident ident1B = diagram.buildLeafIdent(name1B);
		final Ident ident2A = diagram.buildLeafIdent(name2A);
		final Ident ident2B = diagram.buildLeafIdent(name2B);
		if (diagram.leafExistSmart(ident1A) == false) {
			return CommandExecutionResult.error("No class " + ident1A.getName());
		}
		if (diagram.leafExistSmart(ident1B) == false) {
			return CommandExecutionResult.error("No class " + ident1B.getName());
		}
		if (diagram.leafExistSmart(ident2A) == false) {
			return CommandExecutionResult.error("No class " + ident2A.getName());
		}
		if (diagram.leafExistSmart(ident2B) == false) {
			return CommandExecutionResult.error("No class " + ident2B.getName());
		}

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));

		return diagram.associationClass(name1A, name1B, name2A, name2B, linkType, label);
	}

	private CommandExecutionResult executeArgSpecial3(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		if (diagram.V1972())
			return executeArgSpecial1972Ident3(diagram, arg);
		final String name1A = arg.get("COUPLE1", 0);
		final String name1B = arg.get("COUPLE1", 1);
		final String name2A = arg.get("COUPLE2", 0);
		final String name2B = arg.get("COUPLE2", 1);
		final Code clName1A = diagram.buildCode(name1A);
		final Code clName1B = diagram.buildCode(name1B);
		final Code clName2A = diagram.buildCode(name2A);
		final Code clName2B = diagram.buildCode(name2B);
		if (diagram.leafExist(clName1A) == false) {
			return CommandExecutionResult.error("No class " + clName1A);
		}
		if (diagram.leafExist(clName1B) == false) {
			return CommandExecutionResult.error("No class " + clName1B);
		}
		if (diagram.leafExist(clName2A) == false) {
			return CommandExecutionResult.error("No class " + clName2A);
		}
		if (diagram.leafExist(clName2B) == false) {
			return CommandExecutionResult.error("No class " + clName2B);
		}

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));

		return diagram.associationClass(name1A, name1B, name2A, name2B, linkType, label);
	}

	private CommandExecutionResult executeArgSpecial2(AbstractClassOrObjectDiagram diagram, RegexResult arg) {
		if (diagram.V1972())
			return executeArgSpecial1972Ident2(diagram, arg);
		final String name2A = arg.get("COUPLE2", 0);
		final String name2B = arg.get("COUPLE2", 1);
		final Code clName2A = diagram.buildCode(name2A);
		final Code clName2B = diagram.buildCode(name2B);
		if (diagram.leafExist(clName2A) == false) {
			return CommandExecutionResult.error("No class " + clName2A);
		}
		if (diagram.leafExist(clName2B) == false) {
			return CommandExecutionResult.error("No class " + clName2B);
		}

		final String idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 0), "\"");
		final Code ent1 = diagram.buildCode(idShort);
		final IEntity cl1 = diagram.getOrCreateLeaf(diagram.buildLeafIdent(idShort), ent1, null, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(arg.get("LABEL_LINK", 0));

		final boolean result = diagram.associationClass(2, name2A, name2B, cl1, linkType, label);
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
		if ("<|:".equals(s)) {
			return LinkDecor.DEFINEDBY;
		}
		if ("<||".equals(s)) {
			return LinkDecor.REDEFINES;
		}
		if ("}".equals(s)) {
			return LinkDecor.CROWFOOT;
		}
		if ("}o".equals(s)) {
			return LinkDecor.CIRCLE_CROWFOOT;
		}
		if ("}|".equals(s)) {
			return LinkDecor.LINE_CROWFOOT;
		}
		if ("|o".equals(s)) {
			return LinkDecor.CIRCLE_LINE;
		}
		if ("||".equals(s)) {
			return LinkDecor.DOUBLE_LINE;
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
		if ("x".equals(s)) {
			return LinkDecor.NOT_NAVIGABLE;
		}
		if ("*".equals(s)) {
			return LinkDecor.COMPOSITION;
		}
		if ("#".equals(s)) {
			return LinkDecor.SQUARE;
		}
		if (")".equals(s)) {
			return LinkDecor.PARENTHESIS;
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
		if (":|>".equals(s)) {
			return LinkDecor.DEFINEDBY;
		}
		if ("||>".equals(s)) {
			return LinkDecor.REDEFINES;
		}
		if (">".equals(s)) {
			return LinkDecor.ARROW;
		}
		if ("{".equals(s)) {
			return LinkDecor.CROWFOOT;
		}
		if ("o{".equals(s)) {
			return LinkDecor.CIRCLE_CROWFOOT;
		}
		if ("|{".equals(s)) {
			return LinkDecor.LINE_CROWFOOT;
		}
		if ("o|".equals(s)) {
			return LinkDecor.CIRCLE_LINE;
		}
		if ("||".equals(s)) {
			return LinkDecor.DOUBLE_LINE;
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
		if ("x".equals(s)) {
			return LinkDecor.NOT_NAVIGABLE;
		}
		if ("*".equals(s)) {
			return LinkDecor.COMPOSITION;
		}
		if ("#".equals(s)) {
			return LinkDecor.SQUARE;
		}
		if ("(".equals(s)) {
			return LinkDecor.PARENTHESIS;
		}
		return LinkDecor.NONE;
	}

	private LinkType getLinkType(RegexResult arg) {
		final LinkDecor decors1 = getDecors1(getArrowHead1(arg));
		final LinkDecor decors2 = getDecors2(getArrowHead2(arg));

		LinkType result = new LinkType(decors2, decors1);
		if (arg.get("ARROW_BODY1", 0).contains(".") || arg.get("ARROW_BODY2", 0).contains(".")) {
			result = result.goDashed();
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
//		final LinkDecor decors1 = getDecors1(getArrowHead1(arg));
//		final LinkDecor decors2 = getDecors2(getArrowHead2(arg));

		String s = getFullArrow(arg);
		s = s.replaceAll("[^-.=\\w]", "");
		if (s.startsWith("o")) {
			s = s.substring(1);
		}
		if (s.endsWith("o")) {
			s = s.substring(0, s.length() - 1);
		}

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
		return notNull(arg.get(key, 0));
	}

	private String getFullArrow(RegexResult arg) {
		return getArrowHead1(arg) + notNull(arg.get("ARROW_BODY1", 0)) + notNull(arg.get("ARROW_DIRECTION", 0))
				+ notNull(arg.get("ARROW_BODY2", 0)) + getArrowHead2(arg);
	}

	public static String notNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

//	private boolean isInversed(LinkDecor decors1, LinkDecor decors2) {
//		if (decors1 == LinkDecor.ARROW && decors2 != LinkDecor.ARROW) {
//			return true;
//		}
//		if (decors2 == LinkDecor.AGREGATION) {
//			return true;
//		}
//		if (decors2 == LinkDecor.COMPOSITION) {
//			return true;
//		}
//		if (decors2 == LinkDecor.PLUS) {
//			return true;
//		}
//		// if (decors2 == LinkDecor.EXTENDS) {
//		// return true;
//		// }
//		return false;
//	}

}
