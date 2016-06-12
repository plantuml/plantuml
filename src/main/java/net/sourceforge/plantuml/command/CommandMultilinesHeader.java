/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 19880 $
 *
 */
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.VerticalAlignment;

public class CommandMultilinesHeader extends CommandMultilines<UmlDiagram> {

	public CommandMultilinesHeader() {
		super("(?i)^(?:(left|right|center)?[%s]*)header$");
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^end[%s]?header$";
	}

	public CommandExecutionResult execute(final UmlDiagram diagram, BlocLines lines) {
		lines = lines.trim(false);
		final Matcher2 m = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		final String align = m.group(1);
		lines = lines.subExtract(1, 1);
		final Display strings = lines.toDisplay();
		if (strings.size() > 0) {
			diagram.setHeader(new DisplayPositionned(strings, HorizontalAlignment.fromString(align,
					HorizontalAlignment.RIGHT), VerticalAlignment.TOP));
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Empty header");
	}

}
