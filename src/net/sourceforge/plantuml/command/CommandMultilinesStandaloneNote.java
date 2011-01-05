/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5751 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityType;

public class CommandMultilinesStandaloneNote extends CommandMultilines<AbstractEntityDiagram> {

	public CommandMultilinesStandaloneNote(final AbstractEntityDiagram system) {
		super(system, "(?i)^(note)\\s+as\\s+([\\p{L}0-9_.]+)\\s*(#\\w+)?$", "(?i)^end ?note$");
	}

	public CommandExecutionResult execute(List<String> lines) {

		final List<String> line0 = StringUtils.getSplit(getStartingPattern(), lines.get(0).trim());

		final List<String> strings = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1));
		final String display = StringUtils.getMergedLines(strings);

		final EntityType type = EntityType.NOTE;
		final String code = line0.get(1);
		getSystem().createEntity(code, display, type).setSpecificBackcolor(line0.get(2));

		return CommandExecutionResult.ok();
	}

}
