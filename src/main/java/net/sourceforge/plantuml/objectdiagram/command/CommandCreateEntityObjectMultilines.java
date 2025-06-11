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
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.NameAndCodeParser;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandCreateEntityObjectMultilines extends CommandMultilines2<AbstractClassOrObjectDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^[%s]*\\}[%s]*$"));

	public CommandCreateEntityObjectMultilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateEntityObjectMultilines.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(0, "TYPE", "object"), //
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
	protected CommandExecutionResult executeNow(AbstractClassOrObjectDiagram diagram, BlocLines lines,
			ParserPass currentPass) throws NoSuchColorException {
		lines = lines.trim().removeEmptyLines();
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final Entity entity = executeArg0(lines.getLocation(), diagram, line0);
		if (entity == null)
			return CommandExecutionResult.error("No such entity");

		lines = lines.subExtract(1, 1);
		for (StringLocated s : lines) {
			assert s.getString().length() > 0;
			if (VisibilityModifier.isVisibilityCharacter(s.getString()))
				diagram.setVisibilityModifierPresent(true);

			entity.getBodier().addFieldOrMethod(s.getString());
		}
		return CommandExecutionResult.ok();
	}

	private Entity executeArg0(LineLocation location, AbstractClassOrObjectDiagram diagram, RegexResult line0)
			throws NoSuchColorException {
		final String idShort = diagram.cleanId(line0.getLazzy("CODE", 0));
		final Quark<Entity> quark = diagram.quarkInContext(true, idShort);

		final String displayString = line0.getLazzy("DISPLAY", 0);
		final String stereotype = line0.get("STEREO", 0);

		Display display = Display.getWithNewlines(diagram.getPragma(), displayString);
		if (Display.isNull(display))
			display = Display.getWithNewlines(diagram.getPragma(), quark.getName())
					.withCreoleMode(CreoleMode.SIMPLE_LINE);
		Entity entity = quark.getData();
		if (entity == null)
			entity = diagram.reallyCreateLeaf(location, quark, display, LeafType.OBJECT, null);

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
