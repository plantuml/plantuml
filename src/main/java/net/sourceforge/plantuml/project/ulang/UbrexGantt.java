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
package net.sourceforge.plantuml.project.ulang;

import java.util.ArrayList;
import java.util.List;

import com.plantuml.ubrex.TextNavigator;

import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.lang.SubjectDayAsDate;
import net.sourceforge.plantuml.project.lang.SubjectDayOfWeek;
import net.sourceforge.plantuml.project.lang.SubjectProject;
import net.sourceforge.plantuml.project.lang.SubjectTask;

public class UbrexGantt {

	public static void sentence(String sentenceString, String debug) {
		System.out.println("------------------------------");
		System.out.println("BL=" + sentenceString + " " + debug);

		final TextNavigator tn = TextNavigator.build(sentenceString);

		for (UbrexSentence<GanttDiagram> sentence : getSentences()) {
			final boolean match = sentence.check(tn);
			if (match) {
				System.out.println("[+] OK FOR " + sentence);
				break;
			}

		}

	}

	public static List<UbrexSentence<GanttDiagram>> getSentences() {
		final List<UbrexSentence<GanttDiagram>> subjectsList = new ArrayList<>();
		subjectsList.addAll(SubjectTask.ME.getUSentences());
		subjectsList.addAll(SubjectProject.ME.getUSentences());
		subjectsList.addAll(SubjectDayOfWeek.ME.getUSentences());
		subjectsList.addAll(SubjectDayAsDate.ME.getUSentences());
		return subjectsList;
	}

}
