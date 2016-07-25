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
 * Revision $Revision: 4161 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
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
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class CommandCreateClassMultilines extends CommandMultilines2<ClassDiagram> {

	private static final String CODE = CommandLinkClass.getSeparator() + "?[\\p{L}0-9_]+" + "(?:"
			+ CommandLinkClass.getSeparator() + "[\\p{L}0-9_]+)*";
	public static final String CODES = CODE + "(?:\\s*,\\s*" + CODE + ")*";

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateClassMultilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^[%s]*\\}[%s]*$";
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("VISIBILITY", "(" + VisibilityModifier.regexForVisibilityCharacter() + ")?"), //
				new RegexLeaf("TYPE", "(interface|enum|abstract[%s]+class|abstract|class)[%s]+"), //
				new RegexOr(//
						new RegexConcat(//
								new RegexLeaf("DISPLAY1", "[%g](.+)[%g]"), //
								new RegexLeaf("[%s]+as[%s]+"), //
								new RegexLeaf("CODE1", "(" + CommandCreateClass.CODE + ")")), //
						new RegexConcat(//
								new RegexLeaf("CODE2", "(" + CommandCreateClass.CODE + ")"), //
								new RegexLeaf("[%s]+as[%s]+"), // //
								new RegexLeaf("DISPLAY2", "[%g](.+)[%g]")), //
						new RegexLeaf("CODE3", "(" + CommandCreateClass.CODE + ")"), //
						new RegexLeaf("CODE4", "[%g]([^%g]+)[%g]")), //
				new RegexLeaf("GENERIC", "(?:[%s]*\\<(" + GenericRegexProducer.PATTERN + ")\\>)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREO", "(\\<\\<.+\\>\\>)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("LINECOLOR", "(?:##(?:\\[(dotted|dashed|bold)\\])?(\\w+)?)?"), //
				new RegexLeaf("EXTENDS", "([%s]+(extends)[%s]+(" + CODES + "))?"), //
				new RegexLeaf("IMPLEMENTS", "([%s]+(implements)[%s]+(" + CODES + "))?"), //
				new RegexLeaf("[%s]*\\{[%s]*$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	public CommandExecutionResult executeNow(ClassDiagram diagram, BlocLines lines) {
		lines = lines.trimSmart(1);
		lines = lines.removeComments();
		final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
		final IEntity entity = executeArg0(diagram, line0);
		if (entity == null) {
			return CommandExecutionResult.error("No such entity");
		}
		if (lines.size() > 1) {
			lines = lines.subExtract(1, 1);
			final Url url;
			if (lines.size() > 0) {
				final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
				url = urlBuilder.getUrl(lines.getFirst499().toString());
			} else {
				url = null;
			}
			if (url != null) {
				lines = lines.subExtract(1, 0);
			}
			for (CharSequence s : lines) {
				if (s.length() > 0 && VisibilityModifier.isVisibilityCharacter(s.charAt(0))) {
					diagram.setVisibilityModifierPresent(true);
				}
				entity.getBodier().addFieldOrMethod(s.toString());
			}
			if (url != null) {
				entity.addUrl(url);
			}
		}

		manageExtends("EXTENDS", diagram, line0, entity);
		manageExtends("IMPLEMENTS", diagram, line0, entity);

		return CommandExecutionResult.ok();
	}

	public static void manageExtends(String keyword, ClassDiagram system, RegexResult arg, final IEntity entity) {
		if (arg.get(keyword, 1) != null) {
			final Mode mode = arg.get(keyword, 1).equalsIgnoreCase("extends") ? Mode.EXTENDS : Mode.IMPLEMENTS;
			LeafType type2 = LeafType.CLASS;
			if (mode == Mode.IMPLEMENTS) {
				type2 = LeafType.INTERFACE;
			}
			if (mode == Mode.EXTENDS && entity.getEntityType() == LeafType.INTERFACE) {
				type2 = LeafType.INTERFACE;
			}
			final String codes = arg.get(keyword, 2);
			for (String s : codes.split(",")) {
				final Code other = Code.of(StringUtils.trin(s));
				final IEntity cl2 = system.getOrCreateLeaf(other, type2, null);
				LinkType typeLink = new LinkType(LinkDecor.NONE, LinkDecor.EXTENDS);
				if (type2 == LeafType.INTERFACE && entity.getEntityType() != LeafType.INTERFACE) {
					typeLink = typeLink.getDashed();
				}
				final Link link = new Link(cl2, entity, typeLink, Display.NULL, 2, null, null,
						system.getLabeldistance(), system.getLabelangle());
				system.addLink(link);
			}
		}
	}

	private IEntity executeArg0(ClassDiagram diagram, RegexResult arg) {

		final LeafType type = LeafType.getLeafType(StringUtils.goUpperCase(arg.get("TYPE", 0)));
		final String visibilityString = arg.get("VISIBILITY", 0);
		VisibilityModifier visibilityModifier = null;
		if (visibilityString != null) {
			visibilityModifier = VisibilityModifier.getVisibilityModifier(visibilityString.charAt(0), false);
		}

		final Code code = Code.of(arg.getLazzy("CODE", 0)).eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
		final String display = arg.getLazzy("DISPLAY", 0);

		final String stereotype = arg.get("STEREO", 0);
		final String generic = arg.get("GENERIC", 0);

		final ILeaf result;
		if (diagram.leafExist(code)) {
			result = diagram.getOrCreateLeaf(code, null, null);
			result.muteToType(type, null);
		} else {
			result = diagram.createLeaf(code, Display.getWithNewlines(display), type, null);
		}
		result.setVisibilityModifier(visibilityModifier);
		if (stereotype != null) {
			result.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(), diagram
					.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER), diagram.getSkinParam()
					.getIHtmlColorSet()));
		}

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			result.addUrl(url);
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final HtmlColor lineColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR", 1));
		if (lineColor != null) {
			colors = colors.add(ColorType.LINE, lineColor);
		}
		if (arg.get("LINECOLOR", 0) != null) {
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));
		}
		result.setColors(colors);

		// result.setSpecificColorTOBEREMOVED(ColorType.BACK,
		// diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR", 0)));
		// result.setSpecificColorTOBEREMOVED(ColorType.LINE,
		// diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR", 1)));
		// result.applyStroke(arg.get("LINECOLOR", 0));

		if (generic != null) {
			result.setGeneric(generic);
		}
		return result;
	}
}
