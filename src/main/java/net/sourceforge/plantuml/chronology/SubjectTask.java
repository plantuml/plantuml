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
package net.sourceforge.plantuml.chronology;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskCode;
import net.sourceforge.plantuml.project.lang.SentenceSimple;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.project.lang.Words;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;

public class SubjectTask implements Subject<ChronologyDiagram> {

	public static final Subject<ChronologyDiagram> ME = new SubjectTask();

	private SubjectTask() {
	}

	public Failable<Task> getMe(ChronologyDiagram chronology, RegexResult arg) {
		final Task result;

		final String subject = arg.get("SUBJECT", 0);
		final String shortName = arg.get("SHORTNAME", 0);
		final String stereotype = arg.get("STEREOTYPE", 0);

		final TaskCode code = TaskCode.fromIdAndDisplay(shortName, subject);
		result = chronology.getOrCreateTask(code, false);

		if (stereotype != null)
			result.setStereotype(Stereotype.build(arg.get("STEREOTYPE", 0)));

		return Failable.ok(result);
	}

	public Collection<? extends SentenceSimple<ChronologyDiagram>> getSentences() {
		return Arrays.asList(new SentenceHappensChronology());
	}

	public IRegex toRegex() {
		return new RegexOr( //
				new RegexLeaf(1, "SUBJECT", "\\[([^\\[\\]]+?)\\]"), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexOptional(new RegexConcat(//
						Words.exactly(Words.AS), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "SHORTNAME", "\\[([^\\[\\]]+?)\\]"))) //
		);
	}

}
