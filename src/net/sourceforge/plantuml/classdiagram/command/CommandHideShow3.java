/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 5019 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.EnumSet;
import java.util.Set;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class CommandHideShow3 extends SingleLineCommand2<UmlDiagram> {

	private static final EnumSet<EntityPortion> PORTION_METHOD = EnumSet.<EntityPortion> of(EntityPortion.METHOD);
	private static final EnumSet<EntityPortion> PORTION_MEMBER = EnumSet.<EntityPortion> of(EntityPortion.FIELD,
			EntityPortion.METHOD);
	private static final EnumSet<EntityPortion> PORTION_FIELD = EnumSet.<EntityPortion> of(EntityPortion.FIELD);

	public CommandHideShow3() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), // 
				new RegexLeaf("COMMAND", "(hide|show)"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("VISIBILITY",
						"((?:public|private|protected|package)?(?:[,%s]+(?:public|private|protected|package))*)"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("PORTION", "(members?|attributes?|fields?|methods?)"), //
				new RegexLeaf("$"));
	}
	
	@Override
	protected CommandExecutionResult executeArg(UmlDiagram classDiagram, RegexResult arg) {
		if (classDiagram instanceof ClassDiagram) {
			return executeArgClass((ClassDiagram) classDiagram, arg);
		}
		// Just ignored
		return CommandExecutionResult.ok();
	}


	private CommandExecutionResult executeArgClass(ClassDiagram classDiagram, RegexResult arg) {

		final Set<EntityPortion> portion = getEntityPortion(arg.get("PORTION", 0));

		final Set<VisibilityModifier> visibilities = EnumSet.<VisibilityModifier> noneOf(VisibilityModifier.class);
		final StringTokenizer st = new StringTokenizer(StringUtils.goLowerCase(arg.get("VISIBILITY", 0)), " ,");
		while (st.hasMoreTokens()) {
			addVisibilities(st.nextToken(), portion, visibilities);
		}

		classDiagram.hideOrShow(visibilities, arg.get("COMMAND", 0).equalsIgnoreCase("show"));

		return CommandExecutionResult.ok();
	}

	private void addVisibilities(String token, Set<EntityPortion> portion, Set<VisibilityModifier> result) {
		if (token.equals("public") && portion.contains(EntityPortion.FIELD)) {
			result.add(VisibilityModifier.PUBLIC_FIELD);
		}
		if (token.equals("public") && portion.contains(EntityPortion.METHOD)) {
			result.add(VisibilityModifier.PUBLIC_METHOD);
		}
		if (token.equals("private") && portion.contains(EntityPortion.FIELD)) {
			result.add(VisibilityModifier.PRIVATE_FIELD);
		}
		if (token.equals("private") && portion.contains(EntityPortion.METHOD)) {
			result.add(VisibilityModifier.PRIVATE_METHOD);
		}
		if (token.equals("protected") && portion.contains(EntityPortion.FIELD)) {
			result.add(VisibilityModifier.PROTECTED_FIELD);
		}
		if (token.equals("protected") && portion.contains(EntityPortion.METHOD)) {
			result.add(VisibilityModifier.PROTECTED_METHOD);
		}
		if (token.equals("package") && portion.contains(EntityPortion.FIELD)) {
			result.add(VisibilityModifier.PACKAGE_PRIVATE_FIELD);
		}
		if (token.equals("package") && portion.contains(EntityPortion.METHOD)) {
			result.add(VisibilityModifier.PACKAGE_PRIVATE_METHOD);
		}
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
		throw new IllegalArgumentException();
	}

}
