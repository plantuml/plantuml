/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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

import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
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
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class CommandCreateClassMultilines extends CommandMultilines2<ClassDiagram> {

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateClassMultilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^\\s*\\}\\s*$";
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("TYPE", "(interface|enum|abstract\\s+class|abstract|class)\\s+"), //
				new RegexOr(//
						new RegexConcat(//
								new RegexLeaf("DISPLAY1", "\"([^\"]+)\""), //
								new RegexLeaf("\\s+as\\s+"), //
								new RegexLeaf("CODE1", "(" + CommandCreateClass.CODE + ")")), //
						new RegexConcat(//
								new RegexLeaf("CODE2", "(" + CommandCreateClass.CODE + ")"), //
								new RegexLeaf("\\s+as\\s+"), // //
								new RegexLeaf("DISPLAY2", "\"([^\"]+)\"")), //
						new RegexLeaf("CODE3", "(" + CommandCreateClass.CODE + ")"), //
						new RegexLeaf("CODE4", "\"([^\"]+)\"")), //
				new RegexLeaf("GENERIC", "(?:\\s*\\<(" + GenericRegexProducer.PATTERN + ")\\>)?"), //
				new RegexLeaf("\\s*"), //
				// new RegexLeaf("STEREO", "(?:\\s*(\\<\\<.+\\>\\>))?"), //
				new RegexLeaf("STEREO", "(\\<\\<.+\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("LINECOLOR", "(?:##(?:\\[(dotted|dashed|bold)\\])?(\\w+)?)?"), //
				new RegexLeaf("EXTENDS",
						"(\\s+(extends|implements)\\s+((?:\\.|::)?[\\p{L}0-9_]+(?:(?:\\.|::)[\\p{L}0-9_]+)*))?"), //
				new RegexLeaf("\\s*\\{\\s*$"));
	}

	public CommandExecutionResult executeNow(ClassDiagram diagram, List<String> lines) {
		StringUtils.trim(lines, false);
		final RegexResult line0 = getStartingPattern().matcher(lines.get(0).trim());
		final IEntity entity = executeArg0(diagram, line0);
		if (entity == null) {
			return CommandExecutionResult.error("No such entity");
		}
		lines = lines.subList(1, lines.size() - 1);
		final Url url;
		if (lines.size() > 0) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			url = urlBuilder.getUrl(lines.get(0).toString());
		} else {
			url = null;
		}
		if (url != null) {
			lines = lines.subList(1, lines.size());
		}
		for (String s : lines) {
			if (s.length() > 0 && VisibilityModifier.isVisibilityCharacter(s.charAt(0))) {
				diagram.setVisibilityModifierPresent(true);
			}
			entity.addFieldOrMethod(s);
		}
		if (url != null) {
			entity.addUrl(url);
		}

		manageExtends(diagram, line0, entity);

		return CommandExecutionResult.ok();
	}

	private static void manageExtends(ClassDiagram system, RegexResult arg, final IEntity entity) {
		if (arg.get("EXTENDS", 1) != null) {
			final Mode mode = arg.get("EXTENDS", 1).equalsIgnoreCase("extends") ? Mode.EXTENDS : Mode.IMPLEMENTS;
			final Code other = Code.of(arg.get("EXTENDS", 2));
			LeafType type2 = LeafType.CLASS;
			if (mode == Mode.IMPLEMENTS) {
				type2 = LeafType.INTERFACE;
			}
			if (mode == Mode.EXTENDS && entity.getEntityType() == LeafType.INTERFACE) {
				type2 = LeafType.INTERFACE;
			}
			final IEntity cl2 = system.getOrCreateLeaf(other, type2);
			LinkType typeLink = new LinkType(LinkDecor.NONE, LinkDecor.EXTENDS);
			if (type2 == LeafType.INTERFACE && entity.getEntityType() != LeafType.INTERFACE) {
				typeLink = typeLink.getDashed();
			}
			final Link link = new Link(cl2, entity, typeLink, null, 2, null, null, system.getLabeldistance(),
					system.getLabelangle());
			system.addLink(link);
		}
	}

	private IEntity executeArg0(ClassDiagram diagram, RegexResult arg) {

		final LeafType type = LeafType.getLeafType(arg.get("TYPE", 0).toUpperCase());

		final Code code = Code.of(arg.getLazzy("CODE", 0)).eventuallyRemoveStartingAndEndingDoubleQuote();
		final String display = arg.getLazzy("DISPLAY", 0);

		final String stereotype = arg.get("STEREO", 0);
		final String generic = arg.get("GENERIC", 0);

		final ILeaf result;
		if (diagram.leafExist(code)) {
			result = diagram.getOrCreateLeaf(code, null);
			result.muteToType(type);
		} else {
			result = diagram.createLeaf(code, Display.getWithNewlines(display), type);
		}
		if (stereotype != null) {
			result.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(), diagram
					.getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, null)));
		}

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			result.addUrl(url);
		}

		result.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(arg.get("COLOR", 0)));
		result.setSpecificLineColor(HtmlColorUtils.getColorIfValid(arg.get("LINECOLOR", 1)));
		applyStroke(result, arg.get("LINECOLOR", 0));

		if (generic != null) {
			result.setGeneric(generic);
		}
		return result;
	}

	public UStroke getStroke(LinkStyle style) {
		if (style == LinkStyle.DASHED) {
			return new UStroke(6, 6, 1);
		}
		if (style == LinkStyle.DOTTED) {
			return new UStroke(1, 3, 1);
		}
		if (style == LinkStyle.BOLD) {
			return new UStroke(2.5);
		}
		return new UStroke();
	}

	private void applyStroke(ILeaf entity, String s) {
		if (s == null) {
			return;
		}
		final LinkStyle style = LinkStyle.valueOf(s.toUpperCase());
		entity.setSpecificLineStroke(getStroke(style));

	}

}
