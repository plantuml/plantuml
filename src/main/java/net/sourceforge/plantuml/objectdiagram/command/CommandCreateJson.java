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
package net.sourceforge.plantuml.objectdiagram.command;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.NameAndCodeParser;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.cucadiagram.BodierJSon;
import net.sourceforge.plantuml.json.Json.DefaultHandler;
import net.sourceforge.plantuml.json.JsonParser;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandCreateJson extends CommandMultilines2<AbstractEntityDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^[%s]*\\}[%s]*$"));

	public CommandCreateJson() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateJson.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(0, "TYPE", "json"), //
				RegexLeaf.spaceOneOrMore(), //
				NameAndCodeParser.nameAndCode(), //
				StereotypePattern.optional("STEREO"), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeNow(AbstractEntityDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		lines = lines.trim().removeEmptyLines();
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final Entity entity1 = executeArg0(lines.getLocation(), diagram, line0);
		if (entity1 == null)
			return CommandExecutionResult.error("No such entity");

		final JsonValue json = getJsonValue(lines);

		if (json == null)
			return CommandExecutionResult.error("Bad data");
		((BodierJSon) entity1.getBodier()).setJson(json);

		return CommandExecutionResult.ok();
	}

	@Override
	protected CommandControl finalVerification(BlocLines lines) {
		final JsonValue json = getJsonValue(lines);
		if (json == null)
			return CommandControl.OK_PARTIAL;

		return super.finalVerification(lines);
	}

	private JsonValue getJsonValue(BlocLines lines) {
		final DefaultHandler handler = new DefaultHandler();
		final JsonParser jsonParser = new JsonParser(handler);
		try {
			final String sb = getJsonString(lines);
			jsonParser.parse(sb);
			return handler.getValue();
		} catch (Exception e1) {
			// Sorry, this is VERY ugly
			// Let's see if wa could ignore external brackets...
			try {
				lines = lines.subExtract(1, 1);
				final StringBuilder sb = new StringBuilder();
				for (StringLocated sl : lines)
					sb.append(sl.getString());

				jsonParser.parse(sb.toString());
				return handler.getValue();
			} catch (Exception e2) {
				return null;
			}
		}
	}

	private String getJsonString(BlocLines lines) {
		lines = lines.subExtract(1, 1);
		final StringBuilder sb = new StringBuilder("{");
		for (StringLocated sl : lines)
			sb.append(sl.getString());

		sb.append("}");
		return sb.toString();
	}

	private Entity executeArg0(LineLocation location, AbstractEntityDiagram diagram, RegexResult line0)
			throws NoSuchColorException {
		final String idShort = diagram.cleanId(line0.getLazzy("CODE", 0));

		final Quark<Entity> quark = diagram.quarkInContext(true, idShort);
		if (quark.getData() != null)
			return null;
		final String displayString = line0.getLazzy("DISPLAY", 0);
		final String stereotype = line0.get("STEREO", 0);

		Display display = Display.getWithNewlines(diagram.getPragma(), displayString);
		if (Display.isNull(display))
			display = Display.getWithNewlines(diagram.getPragma(), quark.getName())
					.withCreoleMode(CreoleMode.SIMPLE_LINE);

		final Entity entity = diagram.reallyCreateLeaf(location, quark, display, LeafType.JSON, null);
		if (stereotype != null)
			entity.setStereotype(Stereotype.build(stereotype, diagram.getSkinParam().getCircledCharacterRadius(),
					diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
					diagram.getSkinParam().getIHtmlColorSet()));

		final String s = line0.get("COLOR", 0);
		entity.setSpecificColorTOBEREMOVED(ColorType.BACK,
				s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s));
		return entity;
	}

}
