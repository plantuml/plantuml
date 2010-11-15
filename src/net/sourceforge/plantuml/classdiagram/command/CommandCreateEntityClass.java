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
 * Revision $Revision: 5075 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

public class CommandCreateEntityClass extends SingleLineCommand<ClassDiagram> {

	public CommandCreateEntityClass(ClassDiagram diagram) {
		super(
				diagram,
				"(?i)^(interface|enum|abstract\\s+class|abstract|class)\\s+(?:\"([^\"]+)\"\\s+as\\s+)?(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)(?:\\s*([\\<\\[]{2}.*[\\>\\]]{2}))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final String arg0 = arg.get(0).toUpperCase();
		final EntityType type = EntityType.getEntityType(arg0);
		final String code = arg.get(2);
		final String display = arg.get(1);
		final String stereotype = arg.get(3);
		final Entity entity;
		if (getSystem().entityExist(code)) {
			// return CommandExecutionResult.error("Class already exists : "
			// + code);
			entity = (Entity) getSystem().getOrCreateEntity(code, type);
			entity.muteToType(type);
		} else {
			entity = getSystem().createEntity(code, display, type);
		}
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, getSystem().getSkinParam().getCircledCharacterRadius(),
					getSystem().getSkinParam().getFont(FontParam.CIRCLED_CHARACTER)));
		}
		return CommandExecutionResult.ok();
	}

}
