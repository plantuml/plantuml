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
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandMultilinesHeader extends CommandMultilines<TitledDiagram> {

	public static final CommandMultilinesHeader ME = new CommandMultilinesHeader();

	private CommandMultilinesHeader() {
		super(Pattern2.cmpile("^(?:(left|right|center)?[%s]*)header$"),
				Pattern2.cmpile("^end[%s]?header$"));
	}

	public CommandExecutionResult execute(final TitledDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		final LineLocation location = lines.getLocation();
		lines = lines.trim();
		final Matcher2 m = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		final String align = m.group(1);
		lines = lines.subExtract(1, 1);
		final Display strings = lines.toDisplay();
		if (strings.size() > 0) {
			HorizontalAlignment ha = HorizontalAlignment.fromString(align, HorizontalAlignment.RIGHT);
			if (align == null)
				ha = FontParam.HEADER.getStyleDefinition(null)
						.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder()).getHorizontalAlignment();

			diagram.updateHeader(location, strings, ha);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Empty header");
	}

}
