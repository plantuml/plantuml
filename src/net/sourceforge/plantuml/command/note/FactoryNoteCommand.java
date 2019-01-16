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
package net.sourceforge.plantuml.command.note;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotag;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;

public final class FactoryNoteCommand implements SingleMultiFactoryCommand<AbstractEntityDiagram> {

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat(new RegexLeaf("^[%s]*(note)[%s]+"), //
				new RegexLeaf("CODE", "as[%s]+([\\p{L}0-9_.]+)"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("TAGS", Stereotag.pattern() + "?"), //
				new RegexLeaf("[%s]*"), //
				ColorParser.exp1(), //
				new RegexLeaf("$") //
		);
	}

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat(new RegexLeaf("^[%s]*note[%s]+"), //
				new RegexLeaf("DISPLAY", "[%g]([^%g]+)[%g][%s]+as[%s]+"), //
				new RegexLeaf("CODE", "([\\p{L}0-9_.]+)[%s]*"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("TAGS", Stereotag.pattern() + "?"), //
				new RegexLeaf("[%s]*"), //
				ColorParser.exp1(), //
				new RegexLeaf("$") //
		);

	}

	public Command<AbstractEntityDiagram> createSingleLine() {
		return new SingleLineCommand2<AbstractEntityDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final AbstractEntityDiagram system, RegexResult arg) {
				final String display = arg.get("DISPLAY", 0);
				return executeInternal(system, arg, BlocLines.getWithNewlines(display));
			}

		};
	}

	public Command<AbstractEntityDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<AbstractEntityDiagram>(getRegexConcatMultiLine(),
				MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				return "(?i)^[%s]*end[%s]?note$";
			}

			protected CommandExecutionResult executeNow(final AbstractEntityDiagram system, BlocLines lines) {
				// StringUtils.trim(lines, false);
				final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();
				return executeInternal(system, line0, lines);
			}
		};
	}

	private CommandExecutionResult executeInternal(AbstractEntityDiagram diagram, RegexResult arg, BlocLines display) {
		final Code code = Code.of(arg.get("CODE", 0));
		if (diagram.leafExist(code)) {
			return CommandExecutionResult.error("Note already created: " + code.getFullName());
		}
		final IEntity entity = diagram.createLeaf(code, display.toDisplay(), LeafType.NOTE, null);
		assert entity != null;
		entity.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR", 0)));
		CommandCreateClassMultilines.addTags(entity, arg.get("TAGS", 0));
		return CommandExecutionResult.ok();
	}

}
