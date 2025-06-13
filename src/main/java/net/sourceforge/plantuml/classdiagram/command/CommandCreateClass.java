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
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.NameAndCodeParser;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandCreateClass extends SingleLineCommand2<ClassDiagram> {

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateClass() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateClass.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, //
						"TYPE", "(interface|enum|annotation|abstract[%s]+class|static[%s]+class|abstract|class|entity|circle|diamond|protocol|struct|exception|metaclass|stereotype|dataclass|record)"), //
				RegexLeaf.spaceOneOrMore(), //
				NameAndCodeParser.nameAndCodeForClassWithGeneric(), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceZeroOrMore(),
						new RegexLeaf(1, "GENERIC", "\\<(" + GenericRegexProducer.PATTERN + ")\\>"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(4, "TAGS1", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREO"), //
				new RegexLeaf(4, "TAGS2", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat(new RegexLeaf("##"),
						new RegexLeaf(2, "LINECOLOR", "(?:\\[(dotted|dashed|bold)\\])?(\\w+)?"))), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceOneOrMore(),
						new RegexLeaf(3,
								"EXTENDS", "(extends)[%s]+(" + CommandCreateClassMultilines.CODES + "|[%g]([^%g]+)[%g])"))), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceOneOrMore(),
						new RegexLeaf(3,
								"IMPLEMENTS", "(implements)[%s]+(" + CommandCreateClassMultilines.CODES + "|[%g]([^%g]+)[%g])"))), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceZeroOrMore(), new RegexLeaf("\\{"),
						RegexLeaf.spaceZeroOrMore(), new RegexLeaf("\\}"))), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass)
			throws NoSuchColorException {
		final String typeString = StringUtils.goUpperCase(arg.get("TYPE", 0));
		final LeafType type = LeafType.getLeafType(typeString);
		final String idShort = diagram.cleanId(arg.getLazzy("CODE", 0));
		final String displayString = arg.getLazzy("DISPLAY", 0);
		final String genericOption = arg.getLazzy("DISPLAY", 1);
		final String generic = genericOption != null ? genericOption : arg.get("GENERIC", 0);

		final String stereo = arg.get("STEREO", 0);

		final Failable<Quark<Entity>> quark = diagram.quarkInContextSafe(false, idShort);
		if (quark.isFail())
			return CommandExecutionResult.error(quark.getError(), quark.getScore());

		Entity entity = quark.get().getData();

		if (entity == null) {
			Display display = Display.getWithNewlines(diagram.getPragma(), displayString);
			if (Display.isNull(display))
				display = Display.getWithNewlines(diagram.getPragma(), quark.get().getName()).withCreoleMode(CreoleMode.SIMPLE_LINE);
			entity = diagram.reallyCreateLeaf(location, quark.get(), display, type, null);
		} else {
			if (entity.muteToType(type, null) == false)
				return CommandExecutionResult.error("Bad name");
		}

		final CommandExecutionResult check1 = diagram.checkIfPackageHierarchyIsOk(entity);
		if (check1.isOk() == false)
			return check1;

		diagram.setLastEntity(entity);
		if (stereo != null) {
			final Stereotype stereotype = Stereotype.build(stereo, diagram.getSkinParam().getCircledCharacterRadius(),
					diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
					diagram.getSkinParam().getIHtmlColorSet());
			entity.setStereotype(stereotype);
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
		// entity.setCodeLine(location);

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		final String s = arg.get("LINECOLOR", 1);

		final HColor lineColor = s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s);
		if (lineColor != null)
			colors = colors.add(ColorType.LINE, lineColor);

		if (arg.get("LINECOLOR", 0) != null)
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));

		entity.setColors(colors);

		CommandCreateClassMultilines.manageExtends(location, "EXTENDS", diagram, arg, entity);
		CommandCreateClassMultilines.manageExtends(location, "IMPLEMENTS", diagram, arg, entity);
		CommandCreateClassMultilines.addTags(entity, arg.getLazzy("TAGS", 0));

		if (typeString.contains("STATIC"))
			entity.setStatic(true);

		return CommandExecutionResult.ok();
	}

}
