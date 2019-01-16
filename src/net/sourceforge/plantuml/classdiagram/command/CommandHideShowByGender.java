/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.classdiagram.command;

import java.util.EnumSet;
import java.util.Set;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.EntityGender;
import net.sourceforge.plantuml.cucadiagram.EntityGenderUtils;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandHideShowByGender extends SingleLineCommand2<UmlDiagram> {

	private static final EnumSet<EntityPortion> PORTION_METHOD = EnumSet.<EntityPortion> of(EntityPortion.METHOD);
	private static final EnumSet<EntityPortion> PORTION_MEMBER = EnumSet.<EntityPortion> of(EntityPortion.FIELD,
			EntityPortion.METHOD);
	private static final EnumSet<EntityPortion> PORTION_FIELD = EnumSet.<EntityPortion> of(EntityPortion.FIELD);

	public CommandHideShowByGender() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("COMMAND", "(hide|show)"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("GENDER",
						"(?:(class|object|interface|enum|annotation|abstract|[\\p{L}0-9_.]+|[%g][^%g]+[%g]|\\<\\<.*\\>\\>)[%s]+)*?"), //
				new RegexLeaf("EMPTY", "(?:(empty)[%s]+)?"), //
				new RegexLeaf("PORTION", "(members?|attributes?|fields?|methods?|circle\\w*|stereotypes?)"), //
				new RegexLeaf("$"));
	}

	private final EntityGender emptyByGender(Set<EntityPortion> portion) {
		if (portion == PORTION_METHOD) {
			return EntityGenderUtils.emptyMethods();
		}
		if (portion == PORTION_FIELD) {
			return EntityGenderUtils.emptyFields();
		}
		if (portion == PORTION_MEMBER) {
			throw new IllegalArgumentException();
			// return EntityGenderUtils.emptyMembers();
		}
		return EntityGenderUtils.all();
	}

	@Override
	protected CommandExecutionResult executeArg(UmlDiagram diagram, RegexResult arg) {
		if (diagram instanceof AbstractClassOrObjectDiagram) {
			return executeClassDiagram((AbstractClassOrObjectDiagram) diagram, arg);
		}
		if (diagram instanceof DescriptionDiagram) {
			return executeDescriptionDiagram((DescriptionDiagram) diagram, arg);
		}
		if (diagram instanceof SequenceDiagram) {
			return executeSequenceDiagram((SequenceDiagram) diagram, arg);
		}
		// Just ignored
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeSequenceDiagram(SequenceDiagram diagram, RegexResult arg) {
		final Set<EntityPortion> portion = getEntityPortion(arg.get("PORTION", 0));
		diagram.hideOrShow(portion, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeDescriptionDiagram(DescriptionDiagram diagram, RegexResult arg) {
		final Set<EntityPortion> portion = getEntityPortion(arg.get("PORTION", 0));
		final EntityGender gender;
		final String arg1 = arg.get("GENDER", 0);
		if (arg1 == null) {
			gender = EntityGenderUtils.all();
		} else if (arg1.equalsIgnoreCase("class")) {
			gender = EntityGenderUtils.byEntityType(LeafType.CLASS);
		} else if (arg1.equalsIgnoreCase("object")) {
			gender = EntityGenderUtils.byEntityType(LeafType.OBJECT);
		} else if (arg1.equalsIgnoreCase("interface")) {
			gender = EntityGenderUtils.byEntityType(LeafType.INTERFACE);
		} else if (arg1.equalsIgnoreCase("enum")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ENUM);
		} else if (arg1.equalsIgnoreCase("abstract")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ABSTRACT_CLASS);
		} else if (arg1.equalsIgnoreCase("annotation")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ANNOTATION);
		} else if (arg1.startsWith("<<")) {
			gender = EntityGenderUtils.byStereotype(arg1);
		} else {
			final IEntity entity = diagram.getOrCreateLeaf(Code.of(arg1), null, null);
			gender = EntityGenderUtils.byEntityAlone(entity);
		}

		diagram.hideOrShow(gender, portion, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeClassDiagram(AbstractClassOrObjectDiagram classDiagram, RegexResult arg) {

		final Set<EntityPortion> portion = getEntityPortion(arg.get("PORTION", 0));

		EntityGender gender = null;
		final String arg1 = arg.get("GENDER", 0);
		if (arg1 == null) {
			gender = EntityGenderUtils.all();
		} else if (arg1.equalsIgnoreCase("class")) {
			gender = EntityGenderUtils.byEntityType(LeafType.CLASS);
		} else if (arg1.equalsIgnoreCase("object")) {
			gender = EntityGenderUtils.byEntityType(LeafType.OBJECT);
		} else if (arg1.equalsIgnoreCase("interface")) {
			gender = EntityGenderUtils.byEntityType(LeafType.INTERFACE);
		} else if (arg1.equalsIgnoreCase("enum")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ENUM);
		} else if (arg1.equalsIgnoreCase("abstract")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ABSTRACT_CLASS);
		} else if (arg1.equalsIgnoreCase("annotation")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ANNOTATION);
		} else if (arg1.startsWith("<<")) {
			gender = EntityGenderUtils.byStereotype(arg1);
		} else {
			final IEntity entity = classDiagram.getOrCreateLeaf(Code.of(arg1), null, null);
			gender = EntityGenderUtils.byEntityAlone(entity);
		}
		if (gender != null) {
			final boolean empty = arg.get("EMPTY", 0) != null;
			final boolean emptyMembers = empty && portion == PORTION_MEMBER;
			if (empty == true && emptyMembers == false) {
				gender = EntityGenderUtils.and(gender, emptyByGender(portion));
			}
			if (EntityUtils.groupRoot(classDiagram.getCurrentGroup()) == false) {
				gender = EntityGenderUtils.and(gender, EntityGenderUtils.byPackage(classDiagram.getCurrentGroup()));
			}

			if (emptyMembers) {
				classDiagram.hideOrShow(EntityGenderUtils.and(gender, emptyByGender(PORTION_FIELD)), PORTION_FIELD, arg
						.get("COMMAND", 0).equalsIgnoreCase("show"));
				classDiagram.hideOrShow(EntityGenderUtils.and(gender, emptyByGender(PORTION_METHOD)), PORTION_METHOD,
						arg.get("COMMAND", 0).equalsIgnoreCase("show"));
			} else {
				classDiagram.hideOrShow(gender, portion, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
			}
		}
		return CommandExecutionResult.ok();
	}

	private Set<EntityPortion> getEntityPortion(String s) {
		final String sub = StringUtils.goLowerCase(s.substring(0, 3));
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
