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
package net.sourceforge.plantuml.command.note;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public final class CommandFactoryNote implements SingleMultiFactoryCommand<AbstractEntityDiagram> {
	// ::remove folder when __HAXE__

	public final static CommandFactoryNote ME = new CommandFactoryNote();

	private CommandFactoryNote() {

	}

	private final static IRegex multiLine = RegexConcat.build(CommandFactoryNote.class.getName() + "multi",
			RegexLeaf.start(), //
			new RegexLeaf("note"), //
			RegexLeaf.spaceOneOrMore(), //
			new RegexLeaf("as"), //
			RegexLeaf.spaceOneOrMore(), //
			new RegexLeaf(1, "CODE", "([%pLN_.]+)"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf(4, "TAGS", Stereotag.pattern() + "?"), //
			StereotypePattern.optional("STEREO"), //
			ColorParser.exp1(), //
			RegexLeaf.end() //
	);

	private final static IRegex singleLine = RegexConcat.build(CommandFactoryNote.class.getName() + "single",
			RegexLeaf.start(), //
			new RegexLeaf("note"), //
			RegexLeaf.spaceOneOrMore(), //
			new RegexLeaf("[%g]"), //
			new RegexLeaf(1, "DISPLAY", "([^%g]+)"), //
			new RegexLeaf("[%g]"), //
			RegexLeaf.spaceOneOrMore(), //
			new RegexLeaf("as"), //
			RegexLeaf.spaceOneOrMore(), //
			new RegexLeaf(1, "CODE", "([%pLN_.]+)"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf(4, "TAGS", Stereotag.pattern() + "?"), //
			StereotypePattern.optional("STEREO"), //
			ColorParser.exp1(), //
			RegexLeaf.end() //
	);

	public Command<AbstractEntityDiagram> createSingleLine() {
		return new SingleLineCommand2<AbstractEntityDiagram>(singleLine) {

			@Override
			protected CommandExecutionResult executeArg(final AbstractEntityDiagram diagram, LineLocation location,
					RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
				final Display display = Display.getWithNewlines(diagram.getPragma(), arg.get("DISPLAY", 0));
				return executeInternal(location, diagram, arg, display);
			}

		};
	}

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^[%s]*end[%s]?note$"));

	public Command<AbstractEntityDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<AbstractEntityDiagram>(multiLine, MultilinesStrategy.KEEP_STARTING_QUOTE,
				Trim.BOTH, END) {

			@Override
			protected CommandExecutionResult executeNow(final AbstractEntityDiagram diagram, BlocLines lines,
					ParserPass currentPass) throws NoSuchColorException {
				// StringUtils.trim(lines, false);
				final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
				lines = lines.subExtract(1, 1).expandsNewline(false);
				lines = lines.removeEmptyColumns();
				final Display display = lines.toDisplay();
				return executeInternal(lines.getLocation(), diagram, line0, display);
			}
		};
	}

	private CommandExecutionResult executeInternal(LineLocation location, AbstractEntityDiagram diagram,
			RegexResult arg, Display display) throws NoSuchColorException {
		final String idShort = arg.get("CODE", 0);
		final Quark<Entity> quark = diagram.quarkInContext(false, diagram.cleanId(idShort));

		if (quark.getData() != null)
			return CommandExecutionResult.error("Note already created: " + quark.getName());

		final Entity entity = diagram.reallyCreateLeaf(location, quark, display, LeafType.NOTE, null);

		assert entity != null;
		final String s = arg.get("COLOR", 0);
		entity.setSpecificColorTOBEREMOVED(ColorType.BACK,
				s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s));
		final String stereotypeString = arg.get("STEREO", 0);
		Stereotype stereotype = null;
		if (stereotypeString != null) {
			stereotype = Stereotype.build(stereotypeString);
			entity.setStereotype(stereotype);
		}

		CommandCreateClassMultilines.addTags(entity, arg.get("TAGS", 0));
		return CommandExecutionResult.ok();
	}

}
