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
package net.sourceforge.plantuml.project.command;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandNoteBottom extends CommandMultilines2<GanttDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^end[%s]*note$"));

	public CommandNoteBottom() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandNoteBottom.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "TYPE", "(note)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "POSITION", "(bottom)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(4, "TAGS", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREO"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeNow(GanttDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		lines = lines.subExtract(1, 1);
		lines = lines.removeEmptyColumns();
		final Display strings = lines.toDisplay();
		if (strings.size() == 0)
			return CommandExecutionResult.error("No note defined");

		final String stereotypeString = line0.get("STEREO", 0);
		Stereotype stereotype = null;
		if (stereotypeString != null)
			stereotype = Stereotype.build(stereotypeString);

		return diagram.addNote(strings, stereotype);

	}

}
