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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandDecoratorMultine<D extends Diagram> implements Command<D> {

	private final SingleLineCommand2<D> cmd;
	private final boolean removeEmptyColumn;
	private final int nbMaxLines;

	public static <D extends Diagram> CommandDecoratorMultine<D> create(SingleLineCommand2<D> cmd, int nbMaxLines) {
		return new CommandDecoratorMultine<D>(cmd, false, nbMaxLines);
	}

	private CommandDecoratorMultine(SingleLineCommand2<D> cmd, boolean removeEmptyColumn, int nbMaxLines) {
		this.cmd = cmd;
		this.removeEmptyColumn = removeEmptyColumn;
		this.nbMaxLines = nbMaxLines;
	}

	public CommandExecutionResult execute(D diagram, BlocLines lines, ParserPass currentPass) {
		if (removeEmptyColumn)
			lines = lines.removeEmptyColumns();

		lines = lines.toSingleLineWithHiddenNewLine();
		return cmd.execute(diagram, lines, currentPass);
	}

	public CommandControl isValid(BlocLines lines) {
		if (cmd.isCommandForbidden())
			return CommandControl.NOT_OK;

		lines = lines.toSingleLineWithHiddenNewLine();
		if (cmd.isForbidden(lines.getFirst().getString()))
			return CommandControl.NOT_OK;

		final CommandControl tmp = cmd.isValid(lines);
		if (tmp == CommandControl.OK_PARTIAL)
			throw new IllegalStateException();

		if (tmp == CommandControl.OK)
			return tmp;

		return CommandControl.OK_PARTIAL;
	}

	public int getNbMaxLines() {
		return nbMaxLines;
	}
	
	@Override
	public boolean isEligibleFor(ParserPass pass) {
		return cmd.isEligibleFor(pass);
	}

}
