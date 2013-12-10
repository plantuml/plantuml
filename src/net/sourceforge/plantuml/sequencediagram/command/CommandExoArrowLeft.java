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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;

public class CommandExoArrowLeft extends CommandExoArrowAny {

	public CommandExoArrowLeft() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("SHORT", "([?\\[])?"), //
				new RegexOr( //
						new RegexConcat( //
								new RegexLeaf("ARROW_BOTHDRESSING", "(<<?|//?|\\\\\\\\?)?"), //
								new RegexLeaf("ARROW_BODYA1", "(-+)"), //
								new RegexLeaf("ARROW_STYLE1", CommandArrow.getColorOrStylePattern()), //
								new RegexLeaf("ARROW_BODYB1", "(-*)"), //
								new RegexLeaf("ARROW_DRESSING1", "(>>?|//?|\\\\\\\\?)")), //
						new RegexConcat( //
								new RegexLeaf("ARROW_DRESSING2", "(<<?|//?|\\\\\\\\?)"), //
								new RegexLeaf("ARROW_BODYB1", "(-*)"), //
								new RegexLeaf("ARROW_STYLE2", CommandArrow.getColorOrStylePattern()), //
								new RegexLeaf("ARROW_BODYA2", "(-+)"))), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("PARTICIPANT", "([\\p{L}0-9_.@]+|\"[^\"]+\")"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("LABEL", "(?::\\s*(.*))?"), //
				new RegexLeaf("$"));
	}

	@Override
	MessageExoType getMessageExoType(RegexResult arg2) {
		final String dressing1 = arg2.get("ARROW_DRESSING1", 0);
		final String dressing2 = arg2.get("ARROW_DRESSING2", 0);
		if (dressing1 != null) {
			return MessageExoType.FROM_LEFT;
		}
		if (dressing2 != null) {
			return MessageExoType.TO_LEFT;
		}
		throw new IllegalArgumentException();
	}

}
