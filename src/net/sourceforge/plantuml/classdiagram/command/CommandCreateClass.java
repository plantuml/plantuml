/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlMode;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.baraye.Quark;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotag;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;
import net.sourceforge.plantuml.utils.LineLocation;

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
		return RegexConcat.build(CommandCreateClass.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("TYPE", //
						"(interface|enum|annotation|abstract[%s]+class|static[%s]+class|abstract|class|entity|circle|diamond|protocol|struct|exception|metaclass|stereotype)"), //
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
				new RegexOptional(new RegexConcat(RegexLeaf.spaceZeroOrMore(),
						new RegexLeaf("GENERIC", "\\<(" + GenericRegexProducer.PATTERN + ")\\>"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS1", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREO", "(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS2", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat(new RegexLeaf("##"),
						new RegexLeaf("LINECOLOR", "(?:\\[(dotted|dashed|bold)\\])?(\\w+)?"))), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceOneOrMore(),
						new RegexLeaf("EXTENDS",
								"(extends)[%s]+(" + CommandCreateClassMultilines.CODES + "|[%g]([^%g]+)[%g])"))), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceOneOrMore(),
						new RegexLeaf("IMPLEMENTS",
								"(implements)[%s]+(" + CommandCreateClassMultilines.CODES + "|[%g]([^%g]+)[%g])"))), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceZeroOrMore(), new RegexLeaf("\\{"),
						RegexLeaf.spaceZeroOrMore(), new RegexLeaf("\\}"))), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String typeString = StringUtils.goUpperCase(arg.get("TYPE", 0));
		final LeafType type = LeafType.getLeafType(typeString);
		final String idShort = diagram.cleanIdForQuark(arg.getLazzy("CODE", 0));
		final String displayString = arg.getLazzy("DISPLAY", 0);
		final String genericOption = arg.getLazzy("DISPLAY", 1);
		final String generic = genericOption != null ? genericOption : arg.get("GENERIC", 0);

		final String stereo = arg.get("STEREO", 0);

		final Quark quark = diagram.quarkInContext(idShort, true);

		Display display = Display.getWithNewlines(displayString);
		if (Display.isNull(display))
			display = Display.getWithNewlines(quark.getName()).withCreoleMode(CreoleMode.SIMPLE_LINE);

		EntityImp entity = (EntityImp) quark.getData();

		if (entity == null) {
			entity = diagram.reallyCreateLeaf(quark, display, type, null);
		} else {
			if (entity.muteToType(type, null) == false)
				return CommandExecutionResult.error("Bad name");
			entity.setDisplay(display);

		}
//		final Quark quark = diagram.getPlasma().getIfExistsFromName(idShort);
//		if (quark != null && quark.getData() != null)
//			entity = diagram.getFromName(idShort);
//		else

		// } else
//			entity = (ILeaf) quark.getData();
//		if (entity == null || entity.isGroup()) {
//			for (Quark tmp : diagram.getPlasma().quarks())
//				if (tmp.getData() instanceof EntityImp) {
//					final EntityImp tmp2 = (EntityImp) tmp.getData();
//					if (tmp2 != null && tmp.getName().equals(idShort) && tmp2.isGroup() == false) {
//						entity = (ILeaf) tmp.getData();
//						break;
//					}
//				}
//		}
//		if (entity == null) {
//			entity = diagram.reallyCreateLeaf(quark, display, type, null);
//		} else {
//			if (entity.muteToType(type, null) == false)
//				return CommandExecutionResult.error("Bad name");
//		}
//		} else {
//			final Quark idNewLong = diagram.buildLeafIdent(idShort);
//			final Quark code = diagram.buildCode(idShort);
//			if (diagram.leafExist(code)) {
//				entity = diagram.getOrCreateLeaf(idNewLong, code, type, null);
//				if (entity.muteToType(type, null) == false)
//					return CommandExecutionResult.error("Bad name");
//
//			} else {
//				entity = diagram.createLeaf(idNewLong, code, Display.getWithNewlines(display), type, null);
//			}
//		}
		diagram.setLastEntity(entity);
		if (stereo != null) {
			entity.setStereotype(Stereotype.build(stereo, diagram.getSkinParam().getCircledCharacterRadius(),
					diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
					diagram.getSkinParam().getIHtmlColorSet()));
			entity.setStereostyle(stereo);
		}
		if (generic != null)
			entity.setGeneric(generic);

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}
		entity.setCodeLine(location);

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		final String s = arg.get("LINECOLOR", 1);

		final HColor lineColor = s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s);
		if (lineColor != null)
			colors = colors.add(ColorType.LINE, lineColor);

		if (arg.get("LINECOLOR", 0) != null)
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));

		entity.setColors(colors);

		CommandCreateClassMultilines.manageExtends("EXTENDS", diagram, arg, entity);
		CommandCreateClassMultilines.manageExtends("IMPLEMENTS", diagram, arg, entity);
		CommandCreateClassMultilines.addTags(entity, arg.getLazzy("TAGS", 0));

		if (typeString.contains("STATIC"))
			entity.setStatic(true);

		return CommandExecutionResult.ok();
	}

}
