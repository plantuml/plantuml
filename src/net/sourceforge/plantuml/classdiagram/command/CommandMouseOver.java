/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 4161 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class CommandMouseOver extends CommandMultilines2<ClassDiagram> {

	public CommandMouseOver() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^\\s*\\}\\s*$";
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("mouseover\\s+"), //
				new RegexOr(//
						new RegexLeaf("NAME1", "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)"), //
						new RegexLeaf("NAME3", "\"([^\"]+)\"")), //
				new RegexLeaf("\\s*\\{\\s*$"));
	}

	public CommandExecutionResult executeNow(ClassDiagram system, List<String> lines) {
		StringUtils.trim(lines, false);
		final RegexResult line0 = getStartingPattern().matcher(lines.get(0).trim());
		Code code = Code.of(line0.get("NAME1", 0));
		if (code == null) {
			code = Code.of(line0.get("NAME3", 0));
		}
		if (system.leafExist(code) == false) {
			return CommandExecutionResult.error("No such entity");
		}
		final IEntity entity = system.getLeafs().get(code);
		for (String s : lines.subList(1, lines.size() - 1)) {
			entity.mouseOver(s);
		}

		return CommandExecutionResult.ok();
	}

	// private Entity executeArg0(Map<String, RegexPartialMatch> arg) {
	//
	// final EntityType type = EntityType.getEntityType(arg.get("TYPE").get(0).toUpperCase());
	// final String code;
	// final String display;
	// if (arg.get("NAME1").get(1) != null) {
	// code = arg.get("NAME1").get(1);
	// display = arg.get("NAME1").get(0);
	// } else if (arg.get("NAME3").get(0) != null) {
	// code = arg.get("NAME3").get(0);
	// display = arg.get("NAME3").get(0);
	// } else {
	// code = arg.get("NAME2").get(0);
	// display = arg.get("NAME2").get(1);
	// }
	// final String stereotype = arg.get("STEREO").get(0);
	// final String generic = arg.get("GENERIC").get(0);
	//
	// if (system.entityExist(code)) {
	// final Entity result = (Entity) system.getOrCreateClass(code);
	// result.muteToType(type);
	// return result;
	// }
	// final Entity entity = system.createEntity(code, display, type);
	// if (stereotype != null) {
	// entity.setStereotype(new Stereotype(stereotype, system.getSkinParam().getCircledCharacterRadius(),
	// system.getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, null)));
	// }
	// if (generic != null) {
	// entity.setGeneric(generic);
	// }
	// return entity;
	// }

}
