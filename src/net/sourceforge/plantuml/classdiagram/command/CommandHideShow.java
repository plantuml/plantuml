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
 * Revision $Revision: 5019 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.EntityGender;
import net.sourceforge.plantuml.cucadiagram.EntityGenderUtils;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class CommandHideShow extends SingleLineCommand<ClassDiagram> {

	private static final EnumSet<EntityPortion> PORTION_METHOD = EnumSet.<EntityPortion> of(EntityPortion.METHOD);
	private static final EnumSet<EntityPortion> PORTION_MEMBER = EnumSet.<EntityPortion> of(EntityPortion.FIELD,
			EntityPortion.METHOD);
	private static final EnumSet<EntityPortion> PORTION_FIELD = EnumSet.<EntityPortion> of(EntityPortion.FIELD);

	public CommandHideShow(ClassDiagram classDiagram) {
		super(
				classDiagram,
				"(?i)^(hide|show)\\s+(?:(class|interface|enum|abstract|[\\p{L}0-9_.]+|\\<\\<.*\\>\\>)\\s+)*?(?:(empty)\\s+)?(members?|attributes?|fields?|methods?|circle\\w*|stereotypes?)$");
	}

	private final EntityGender emptyByGender(Set<EntityPortion> portion) {
		if (portion == PORTION_METHOD) {
			return EntityGenderUtils.emptyMethods();
		}
		if (portion == PORTION_FIELD) {
			return EntityGenderUtils.emptyFields();
		}
		if (portion == PORTION_MEMBER) {
			return EntityGenderUtils.emptyMembers();
		}
		return EntityGenderUtils.all();
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final Set<EntityPortion> portion = getEntityPortion(arg.get(3));
		EntityGender gender = null;

		if (arg.get(1) == null) {
			gender = EntityGenderUtils.all();
		} else if (arg.get(1).equalsIgnoreCase("class")) {
			gender = EntityGenderUtils.byEntityType(EntityType.CLASS);
		} else if (arg.get(1).equalsIgnoreCase("interface")) {
			gender = EntityGenderUtils.byEntityType(EntityType.INTERFACE);
		} else if (arg.get(1).equalsIgnoreCase("enum")) {
			gender = EntityGenderUtils.byEntityType(EntityType.ENUM);
		} else if (arg.get(1).equalsIgnoreCase("abstract")) {
			gender = EntityGenderUtils.byEntityType(EntityType.ABSTRACT_CLASS);
		} else if (arg.get(1).startsWith("<<")) {
			gender = EntityGenderUtils.byStereotype(arg.get(1));
		} else {
			final IEntity entity = getSystem().getOrCreateClass(arg.get(1));
			gender = EntityGenderUtils.byEntityAlone(entity);
		}
		if (gender != null) {
			final boolean empty = arg.get(2) != null;
			if (empty == true) {
				gender = EntityGenderUtils.and(gender, emptyByGender(portion));
			}
			if (getSystem().getCurrentGroup() != null) {
				gender = EntityGenderUtils.and(gender, EntityGenderUtils.byPackage(getSystem().getCurrentGroup()));
			}
			getSystem().hideOrShow(gender, portion, arg.get(0).equalsIgnoreCase("show"));
		}
		return CommandExecutionResult.ok();
	}

	private Set<EntityPortion> getEntityPortion(String s) {
		final String sub = s.substring(0, 3).toLowerCase();
		if (sub.equals("met")) {
			return PORTION_METHOD;
		}
		if (sub.equals("mem")) {
			return PORTION_MEMBER;
		}
		if (sub.equals("att") || sub.equals("fie")) {
			return PORTION_FIELD;
		}
		if (sub.equals("cir")) {
			return EnumSet.<EntityPortion> of(EntityPortion.CIRCLED_CHARACTER);
		}
		if (sub.equals("ste")) {
			return EnumSet.<EntityPortion> of(EntityPortion.STEREOTYPE);
		}
		throw new IllegalArgumentException();
	}
}
