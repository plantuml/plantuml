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
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotag;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class CommandCreateClass extends SingleLineCommand2<ClassDiagram> {

	public static final String DISPLAY_WITH_GENERIC = "[%g](.+?)(?:\\<(" + GenericRegexProducer.PATTERN + ")\\>)?[%g]";
	public static final String CODE = "[^%s{}%g<>]+";
	public static final String CODE_NO_DOTDOT = "[^%s{}%g<>:]+";

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateClass() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateClass.class.getName(),
				RegexLeaf.start(), //
				new RegexLeaf("TYPE", //
						"(interface|enum|annotation|abstract[%s]+class|abstract|class|entity|circle|diamond)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexConcat(//
								new RegexLeaf("DISPLAY1", DISPLAY_WITH_GENERIC), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("CODE1", "(" + CODE + ")")), //
						new RegexConcat(//
								new RegexLeaf("CODE2", "(" + CODE + ")"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("DISPLAY2", DISPLAY_WITH_GENERIC)), //
						new RegexLeaf("CODE3", "(" + CODE + ")"), //
						new RegexLeaf("CODE4", "[%g]([^%g]+)[%g]")), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceZeroOrMore(), new RegexLeaf("GENERIC", "\\<("
						+ GenericRegexProducer.PATTERN + ")\\>"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREO", "(\\<{2}.*\\>{2})?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat(new RegexLeaf("##"), new RegexLeaf("LINECOLOR",
						"(?:\\[(dotted|dashed|bold)\\])?(\\w+)?"))), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceOneOrMore(), new RegexLeaf("EXTENDS",
						"(extends)[%s]+(" + CommandCreateClassMultilines.CODES + ")"))), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceOneOrMore(), new RegexLeaf("IMPLEMENTS",
						"(implements)[%s]+(" + CommandCreateClassMultilines.CODES + ")"))), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceZeroOrMore(), new RegexLeaf("\\{"), RegexLeaf
						.spaceZeroOrMore(), new RegexLeaf("\\}"))), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, LineLocation location, RegexResult arg) {
		final LeafType type = LeafType.getLeafType(StringUtils.goUpperCase(arg.get("TYPE", 0)));
		final String idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("CODE", 0),
				"\"([:");
		final String display = arg.getLazzy("DISPLAY", 0);
		final String genericOption = arg.getLazzy("DISPLAY", 1);
		final String generic = genericOption != null ? genericOption : arg.get("GENERIC", 0);

		final String stereotype = arg.get("STEREO", 0);
		final ILeaf entity;
		final Ident idNewLong = diagram.buildLeafIdent(idShort);
		if (diagram.V1972()) {
			if (diagram.leafExistSmart(idNewLong)) {
				entity = diagram.getOrCreateLeaf(idNewLong, idNewLong, type, null);
				if (entity.muteToType(type, null) == false) {
					return CommandExecutionResult.error("Bad name");
				}
			} else {
				entity = diagram.createLeaf(idNewLong, idNewLong, Display.getWithNewlines(display), type, null);
			}
		} else {
			final Code code = diagram.buildCode(idShort);
			if (diagram.leafExist(code)) {
				entity = diagram.getOrCreateLeaf(idNewLong, code, type, null);
				if (entity.muteToType(type, null) == false) {
					return CommandExecutionResult.error("Bad name");
				}
			} else {
				entity = diagram.createLeaf(idNewLong, code, Display.getWithNewlines(display), type, null);
			}
		}
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(), diagram
					.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER), diagram.getSkinParam()
					.getIHtmlColorSet()));
		}
		if (generic != null) {
			entity.setGeneric(generic);
		}

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final HColor lineColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR", 1));
		if (lineColor != null) {
			colors = colors.add(ColorType.LINE, lineColor);
		}
		if (arg.get("LINECOLOR", 0) != null) {
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));
		}
		entity.setColors(colors);

		// entity.setSpecificColorTOBEREMOVED(ColorType.LINE, lineColor);
		// entity.setSpecificColorTOBEREMOVED(ColorType.HEADER, colors.getColor(ColorType.HEADER));
		//
		// if (colors.getLineStyle() != null) {
		// entity.setSpecificLineStroke(LinkStyle.getStroke(colors.getLineStyle()));
		// }
		//
		// if (arg.get("LINECOLOR", 0) != null) {
		// entity.applyStroke(arg.get("LINECOLOR", 0));
		// }

		// manageExtends(diagram, arg, entity);
		CommandCreateClassMultilines.manageExtends("EXTENDS", diagram, arg, entity);
		CommandCreateClassMultilines.manageExtends("IMPLEMENTS", diagram, arg, entity);
		CommandCreateClassMultilines.addTags(entity, arg.get("TAGS", 0));

		return CommandExecutionResult.ok();
	}
	// public static void manageExtends(ClassDiagram system, RegexResult arg, final IEntity entity) {
	// if (arg.get("EXTENDS", 1) != null) {
	// final Mode mode = arg.get("EXTENDS", 1).equalsIgnoreCase("extends") ? Mode.EXTENDS : Mode.IMPLEMENTS;
	// final Code other = diagram.buildCode(arg.get("EXTENDS", 2));
	// LeafType type2 = LeafType.CLASS;
	// if (mode == Mode.IMPLEMENTS) {
	// type2 = LeafType.INTERFACE;
	// }
	// if (mode == Mode.EXTENDS && entity.getEntityType() == LeafType.INTERFACE) {
	// type2 = LeafType.INTERFACE;
	// }
	// final IEntity cl2 = system.getOrCreateLeaf(other, type2, null);
	// LinkType typeLink = new LinkType(LinkDecor.NONE, LinkDecor.EXTENDS);
	// if (type2 == LeafType.INTERFACE && entity.getEntityType() != LeafType.INTERFACE) {
	// typeLink = typeLink.getDashed();
	// }
	// final Link link = new Link(cl2, entity, typeLink, null, 2, null, null, system.getLabeldistance(),
	// system.getLabelangle());
	// system.addLink(link);
	// }
	// }

}
