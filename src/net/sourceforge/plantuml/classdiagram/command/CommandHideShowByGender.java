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
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityGender;
import net.sourceforge.plantuml.abel.EntityGenderUtils;
import net.sourceforge.plantuml.abel.EntityPortion;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandHideShowByGender extends SingleLineCommand2<UmlDiagram> {

	public static final CommandHideShowByGender ME = new CommandHideShowByGender();

	private CommandHideShowByGender() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandHideShowByGender.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("COMMAND", "(hide|show)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("GENDER",
						"(?:(class|object|interface|enum|annotation|abstract|[%pLN_.]+|[%g][^%g]+[%g]|\\<\\<.*\\>\\>)[%s]+)*?"), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf("EMPTY", "(empty)"), //
								RegexLeaf.spaceOneOrMore()) //
				), //
				new RegexLeaf("PORTION", "(members?|attributes?|fields?|methods?|circles?|circled?|stereotypes?)"), //
				RegexLeaf.end());
	}

	private final EntityGender emptyByGender(EntityPortion portion) {
		if (portion == EntityPortion.METHOD) {
			return EntityGenderUtils.emptyMethods();
		}
		if (portion == EntityPortion.FIELD) {
			return EntityGenderUtils.emptyFields();
		}
		if (portion == EntityPortion.MEMBER) {
			throw new IllegalArgumentException();
			// return EntityGenderUtils.emptyMembers();
		}
		return EntityGenderUtils.all();
	}

	@Override
	protected CommandExecutionResult executeArg(UmlDiagram diagram, LineLocation location, RegexResult arg) {
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
		final EntityPortion portion = getEntityPortion(arg.get("PORTION", 0));
		diagram.hideOrShow(portion.asSet(), arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeDescriptionDiagram(DescriptionDiagram diagram, RegexResult arg) {
		final EntityPortion portion = getEntityPortion(arg.get("PORTION", 0));
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
		} else if (arg1.equalsIgnoreCase("protocol")) {
			gender = EntityGenderUtils.byEntityType(LeafType.PROTOCOL);
		} else if (arg1.equalsIgnoreCase("struct")) {
			gender = EntityGenderUtils.byEntityType(LeafType.STRUCT);
		} else if (arg1.equalsIgnoreCase("exception")) {
			gender = EntityGenderUtils.byEntityType(LeafType.EXCEPTION);
		} else if (arg1.equalsIgnoreCase("metaclass")) {
			gender = EntityGenderUtils.byEntityType(LeafType.METACLASS);
		} else if (arg1.equalsIgnoreCase("stereotype")) {
			gender = EntityGenderUtils.byEntityType(LeafType.STEREOTYPE);
		} else if (arg1.startsWith("<<")) {
			gender = EntityGenderUtils.byStereotype(arg1);
		} else {
			final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(arg1));
			if (quark.getData() == null)
				return CommandExecutionResult.error("No such element " + quark.getName());
			final Entity entity = quark.getData();
			gender = EntityGenderUtils.byEntityAlone(entity);
		}

		diagram.hideOrShow(gender, portion, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeClassDiagram(AbstractClassOrObjectDiagram diagram, RegexResult arg) {

		final EntityPortion portion = getEntityPortion(arg.get("PORTION", 0));

		EntityGender gender = null;
		String arg1 = arg.get("GENDER", 0);
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
		} else if (arg1.equalsIgnoreCase("protocol")) {
			gender = EntityGenderUtils.byEntityType(LeafType.PROTOCOL);
		} else if (arg1.equalsIgnoreCase("struct")) {
			gender = EntityGenderUtils.byEntityType(LeafType.STRUCT);
		} else if (arg1.equalsIgnoreCase("exception")) {
			gender = EntityGenderUtils.byEntityType(LeafType.EXCEPTION);
		} else if (arg1.equalsIgnoreCase("metaclass")) {
			gender = EntityGenderUtils.byEntityType(LeafType.METACLASS);
		} else if (arg1.equalsIgnoreCase("stereotype")) {
			gender = EntityGenderUtils.byEntityType(LeafType.STEREOTYPE);
		} else if (arg1.startsWith("<<")) {
			gender = EntityGenderUtils.byStereotype(arg1);
		} else {
			arg1 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg1);
			final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(arg1));
			if (quark == null) {
				// Not sure it could really happens... to be checked
				return CommandExecutionResult.error("No such quark " + arg1);
			}
			if (portion == EntityPortion.METHOD) {
					gender = EntityGenderUtils.byClassName(arg1);
			} else {
				Entity entity = quark.getData();
				if (entity == null)
					return CommandExecutionResult.error("No such element " + quark.getName());
				gender = EntityGenderUtils.byEntityAlone(entity);
			}
		}
			final boolean empty = arg.get("EMPTY", 0) != null;
			final boolean emptyMembers = empty && portion == EntityPortion.MEMBER;
			if (empty && !emptyMembers)
					gender = EntityGenderUtils.and(gender, emptyByGender(portion));

			if (!diagram.getCurrentGroup().isRoot())
					gender = EntityGenderUtils.and(gender, EntityGenderUtils.byPackage(diagram.getCurrentGroup()));

			if (emptyMembers) {
					diagram.hideOrShow(EntityGenderUtils.and(gender, emptyByGender(EntityPortion.FIELD)),
									EntityPortion.FIELD, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
					diagram.hideOrShow(EntityGenderUtils.and(gender, emptyByGender(EntityPortion.METHOD)),
									EntityPortion.METHOD, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
			} else {
					diagram.hideOrShow(gender, portion, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
			}
			return CommandExecutionResult.ok();
	}

	private EntityPortion getEntityPortion(String s) {
		final String sub = StringUtils.goLowerCase(s.substring(0, 3));
		if (sub.equals("met"))
			return EntityPortion.METHOD;

		if (sub.equals("mem"))
			return EntityPortion.MEMBER;

		if (sub.equals("att") || sub.equals("fie"))
			return EntityPortion.FIELD;

		if (sub.equals("cir"))
			return EntityPortion.CIRCLED_CHARACTER;

		if (sub.equals("ste"))
			return EntityPortion.STEREOTYPE;

		throw new IllegalArgumentException();
	}

}
