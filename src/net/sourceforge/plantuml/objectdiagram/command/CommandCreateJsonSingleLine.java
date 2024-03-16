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

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
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
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandCreateJsonSingleLine extends SingleLineCommand2<AbstractClassOrObjectDiagram> {

	public CommandCreateJsonSingleLine() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateJsonSingleLine.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("TYPE", "json"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("NAME", "(?:[%g]([^%g]+)[%g][%s]+as[%s]+)?([%pLN_.]+)"), //
				StereotypePattern.optional("STEREO"), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("DATA", "(\\{.*\\})"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractClassOrObjectDiagram diagram, LineLocation location,
			RegexResult arg) throws NoSuchColorException {
		final String name = arg.get("NAME", 1);
		final String data = arg.get("DATA", 0);
		final Entity entity1 = executeArg0(diagram, arg);
		if (entity1 == null)
			return CommandExecutionResult.error("No such entity");

		final JsonValue json = getJsonValue(data);

		if (json == null)
			return CommandExecutionResult.error("Bad data");
		((BodierJSon) entity1.getBodier()).setJson(json);

		return CommandExecutionResult.ok();
	}

	private JsonValue getJsonValue(String data) {
		try {
			final DefaultHandler handler = new DefaultHandler();
			new JsonParser(handler).parse(data);
			final JsonValue json = handler.getValue();
			return json;
		} catch (Exception e) {
			return null;
		}
	}

	private Entity executeArg0(AbstractClassOrObjectDiagram diagram, RegexResult line0) throws NoSuchColorException {
		final String name = line0.get("NAME", 1);

		final Quark<Entity> quark = diagram.quarkInContext(false, diagram.cleanId(name));
		if (quark.getData() != null)
			return null;

		final String displayString = line0.get("NAME", 0);
		final String stereotype = line0.get("STEREO", 0);

		Display display = Display.getWithNewlines(displayString);
		if (Display.isNull(display))
			display = Display.getWithNewlines(name).withCreoleMode(CreoleMode.SIMPLE_LINE);

		final Entity entity = diagram.reallyCreateLeaf(quark, display, LeafType.JSON, null);
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
