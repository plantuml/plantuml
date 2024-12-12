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

import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandMultilinesTitle extends CommandMultilines<TitledDiagram> {

	public static final CommandMultilinesTitle ME = new CommandMultilinesTitle();

	private CommandMultilinesTitle() {
		super("^title$");
	}

	@Override
	public String getPatternEnd() {
		return "^end[%s]?title$";
	}

	public CommandExecutionResult execute(final TitledDiagram diagram, BlocLines lines, ParserPass currentPass) throws NoSuchColorException {
		lines = lines.subExtract(1, 1).expandsJaws5();
		lines = lines.removeEmptyColumns();
		final Display strings = lines.toDisplay();
		if (strings.size() > 0) {
			diagram.setTitle(DisplayPositioned.single(strings.replaceBackslashT(), HorizontalAlignment.CENTER,
					VerticalAlignment.TOP));
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("No title defined");
	}

}
