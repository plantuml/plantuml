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
 */
package net.sourceforge.plantuml.componentdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.componentdiagram.ComponentDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

public class CommandCreateActorInComponent extends SingleLineCommand<ComponentDiagram> {

	public CommandCreateActorInComponent(ComponentDiagram diagram) {
		super(
				diagram,
				"(?i)^(?:actor\\s+)?([\\p{L}0-9_.]+|:[^:]+:|\"[^\"]+\")\\s*(?:as\\s+:?([\\p{L}0-9_.]+):?)?(?:\\s*([\\<\\[]{2}.*[\\>\\]]{2}))?$");
	}

	@Override
	protected boolean isForbidden(String line) {
		if (line.matches("^[\\p{L}0-9_.]+$")) {
			return true;
		}
		return false;
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final EntityType type = EntityType.ACTOR;
		final String code;
		final String display;
		if (arg.get(1) == null) {
			code = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(0));
			display = code;
		} else {
			display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(0));
			code = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(1));
		}
		final String stereotype = arg.get(2);
		final Entity entity = (Entity) getSystem().getOrCreateEntity(code, type);
		entity.setDisplay2(display);

		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, getSystem().getSkinParam().getCircledCharacterRadius(),
					getSystem().getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, null)));
		}
		return CommandExecutionResult.ok();
	}

}
