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
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandCreateClassMultilines extends CommandMultilines2<ClassDiagram> {

	private static final String CODE = CommandLinkClass.getSeparator() + "?[%pLN_$]+" + "(?:"
			+ CommandLinkClass.getSeparator() + "[%pLN_$]+)*";
	public static final String CODES = CODE + "(?:\\s*,\\s*" + CODE + ")*";

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateClassMultilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH);
	}

	@Override
	public String getPatternEnd() {
		return "^[%s]*\\}[%s]*$";
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateClassMultilines.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("VISIBILITY", "(" + VisibilityModifier.regexForVisibilityCharacterInClassName() + ")?"), //
				new RegexLeaf("TYPE",
						"(interface|enum|annotation|abstract[%s]+class|static[%s]+class|abstract|class|entity|protocol|struct|exception|metaclass|stereotype)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexConcat(//
								new RegexLeaf("DISPLAY1", CommandCreateClass.DISPLAY_WITH_GENERIC), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("CODE1", "(" + CommandCreateClass.CODE + ")")), //
						new RegexConcat(//
								new RegexLeaf("CODE2", "(" + CommandCreateClass.CODE + ")"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("DISPLAY2", CommandCreateClass.DISPLAY_WITH_GENERIC)), //
						new RegexLeaf("CODE3", "(" + CommandCreateClass.CODE + ")"), //
						new RegexLeaf("CODE4", "[%g]([^%g]+)[%g]")), //
				new RegexOptional(new RegexConcat(RegexLeaf.spaceZeroOrMore(),
						new RegexLeaf("GENERIC", "\\<(" + GenericRegexProducer.PATTERN + ")\\>"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS1", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREO"), //
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
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end() //
		);
	}

	@Override
	public boolean syntaxWithFinalBracket() {
		return true;
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeNow(ClassDiagram diagram, BlocLines lines) throws NoSuchColorException {
		lines = lines.trimSmart(1);
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final String typeString = StringUtils.goUpperCase(line0.get("TYPE", 0));
		final LeafType type = LeafType.getLeafType(typeString);
		final String visibilityString = line0.get("VISIBILITY", 0);
		VisibilityModifier visibilityModifier = null;
		if (visibilityString != null)
			visibilityModifier = VisibilityModifier.getVisibilityModifier(visibilityString + "FOO", false);

		final String idShort = diagram.cleanId(line0.getLazzy("CODE", 0));

		final String displayString = line0.getLazzy("DISPLAY", 0);
		final String genericOption = line0.getLazzy("DISPLAY", 1);
		final String generic = genericOption != null ? genericOption : line0.get("GENERIC", 0);

		final String stereotype = line0.get("STEREO", 0);

		final Quark<Entity> quark = diagram.quarkInContext(false, idShort);

		Entity entity = quark.getData();

		Display display = Display.getWithNewlines(displayString);
		if (entity == null) {
			if (Display.isNull(display))
				display = Display.getWithNewlines(quark.getName()).withCreoleMode(CreoleMode.SIMPLE_LINE);
			entity = diagram.reallyCreateLeaf(quark, display, type, null);
		} else {
			if (entity.muteToType(type, null) == false)
				return CommandExecutionResult.error("Cannot create " + idShort + " because it already exists");
			if (Display.isNull(display) == false)
				entity.setDisplay(display);
		}
		final CommandExecutionResult check1 = diagram.checkIfPackageHierarchyIfOk(entity);
		if (check1.isOk() == false)
			return check1;

		diagram.setLastEntity(entity);

		entity.setVisibilityModifier(visibilityModifier);
		if (stereotype != null) {
			entity.setStereotype(Stereotype.build(stereotype, diagram.getSkinParam().getCircledCharacterRadius(),
					diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
					diagram.getSkinParam().getIHtmlColorSet()));
			entity.setStereostyle(stereotype);
		}

		final String urlString = line0.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}

		Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());
		final String s1 = line0.get("LINECOLOR", 1);

		final HColor lineColor = s1 == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s1);
		if (lineColor != null)
			colors = colors.add(ColorType.LINE, lineColor);

		if (line0.get("LINECOLOR", 0) != null)
			colors = colors.addLegacyStroke(line0.get("LINECOLOR", 0));

		entity.setColors(colors);

		if (generic != null)
			entity.setGeneric(generic);

		if (typeString.contains("STATIC"))
			entity.setStatic(true);

		if (lines.size() > 1) {
			entity.setCodeLine(lines.getAt(0).getLocation());
			lines = lines.subExtract(1, 1);
			// final Url url = null;
			// if (lines.size() > 0) {
			// final UrlBuilder urlBuilder = new
			// UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			// url = urlBuilder.getUrl(lines.getFirst499().toString());
			// } else {
			// url = null;
			// }
			// if (url != null) {
			// lines = lines.subExtract(1, 0);
			// }
			for (StringLocated s : lines) {
				if (s.getString().length() > 0 && VisibilityModifier.isVisibilityCharacter(s.getString()))
					diagram.setVisibilityModifierPresent(true);

				entity.getBodier().addFieldOrMethod(s.getString());
			}
//			if (url != null) {
//				entity.addUrl(url);
//			}
		}

		manageExtends("EXTENDS", diagram, line0, entity);
		manageExtends("IMPLEMENTS", diagram, line0, entity);
		addTags(entity, line0.getLazzy("TAGS", 0));

		return CommandExecutionResult.ok();
	}

	public static void addTags(Entity entity, String tags) {
		if (tags == null)
			return;

		for (String tag : tags.split("[ ]+")) {
			assert tag.startsWith("$");
			tag = tag.substring(1);
			entity.addStereotag(new Stereotag(tag));
		}
	}

	public static void manageExtends(String keyword, ClassDiagram diagram, RegexResult arg, final Entity entity) {
		if (arg.get(keyword, 0) != null) {
			final Mode mode = arg.get(keyword, 0).equalsIgnoreCase("extends") ? Mode.EXTENDS : Mode.IMPLEMENTS;
			LeafType type2 = LeafType.CLASS;
			if (mode == Mode.IMPLEMENTS)
				type2 = LeafType.INTERFACE;

			if (mode == Mode.EXTENDS && entity.getLeafType() == LeafType.INTERFACE)
				type2 = LeafType.INTERFACE;

			final String codes = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(keyword, 1));
			for (String s : codes.split(",")) {
				final String idShort = StringUtils.trin(s);

				final Quark<Entity> quark = diagram.quarkInContext(false, diagram.cleanId(idShort));
				Entity cl2 = quark.getData();
				if (cl2 == null)
					cl2 = diagram.reallyCreateLeaf(quark, Display.getWithNewlines(quark.getName()), type2, null);

				LinkType typeLink = new LinkType(LinkDecor.NONE, LinkDecor.EXTENDS);
				if (type2 == LeafType.INTERFACE && entity.getLeafType() != LeafType.INTERFACE)
					typeLink = typeLink.goDashed();

				final LinkArg linkArg = LinkArg.noDisplay(2);
				final Link link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(),
						cl2, entity, typeLink, linkArg.withQuantifier(null, null)
								.withDistanceAngle(diagram.getLabeldistance(), diagram.getLabelangle()));
				diagram.addLink(link);
			}
		}
	}

	private Entity executeArg0(ClassDiagram diagram, RegexResult line0) throws NoSuchColorException {

		final String typeString = StringUtils.goUpperCase(line0.get("TYPE", 0));
		final LeafType type = LeafType.getLeafType(typeString);
		final String visibilityString = line0.get("VISIBILITY", 0);
		VisibilityModifier visibilityModifier = null;
		if (visibilityString != null)
			visibilityModifier = VisibilityModifier.getVisibilityModifier(visibilityString + "FOO", false);

		final String idShort = diagram.cleanId(line0.getLazzy("CODE", 0));

		final String displayString = line0.getLazzy("DISPLAY", 0);
		final String genericOption = line0.getLazzy("DISPLAY", 1);
		final String generic = genericOption != null ? genericOption : line0.get("GENERIC", 0);

		final String stereotype = line0.get("STEREO", 0);

		final Quark<Entity> quark = diagram.quarkInContext(false, idShort);

		Display display = Display.getWithNewlines(displayString);
		if (Display.isNull(display))
			display = Display.getWithNewlines(quark.getName()).withCreoleMode(CreoleMode.SIMPLE_LINE);

		Entity entity = quark.getData();

		if (entity == null) {
			entity = diagram.reallyCreateLeaf(quark, display, type, null);
		} else {
//			if (entity.muteToType(type, null) == false)
//				return null;
			entity.setDisplay(display);

		}

		diagram.setLastEntity(entity);

		entity.setVisibilityModifier(visibilityModifier);
		if (stereotype != null) {
			entity.setStereotype(Stereotype.build(stereotype, diagram.getSkinParam().getCircledCharacterRadius(),
					diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
					diagram.getSkinParam().getIHtmlColorSet()));
			entity.setStereostyle(stereotype);
		}

		final String urlString = line0.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}

		Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());
		final String s = line0.get("LINECOLOR", 1);

		final HColor lineColor = s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s);
		if (lineColor != null)
			colors = colors.add(ColorType.LINE, lineColor);

		if (line0.get("LINECOLOR", 0) != null)
			colors = colors.addLegacyStroke(line0.get("LINECOLOR", 0));

		entity.setColors(colors);

		if (generic != null)
			entity.setGeneric(generic);

		if (typeString.contains("STATIC"))
			entity.setStatic(true);

		return entity;
	}
}
