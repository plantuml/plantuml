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
package net.sourceforge.plantuml.gantt.lang;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.gantt.Failable;
import net.sourceforge.plantuml.gantt.GanttDiagram;
import net.sourceforge.plantuml.gantt.Today;
import net.sourceforge.plantuml.gantt.ulang.VerbPhraseAction;

public class SubjectToday implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectToday();

	private SubjectToday() {
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionSubject() {
		return new UBrexLeaf("today");
	}

	@Override
	public Failable<Today> getMe(GanttDiagram project, UMatcher arg) {
		return Failable.ok(new Today());
	}

	@Override
	public Collection<VerbPhraseAction> getVerbPhrases() {
		final List<VerbPhraseAction> result = new ArrayList<>();

		result.add(new VerbPhraseAction(Verbs.isColored, new ComplementInColors()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Today task = (Today) subject;
				final CenterBorderColor colors = (CenterBorderColor) complement;
				project.setTodayColors(colors);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.is, ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final LocalDate date = (LocalDate) complement;
				return project.setToday(date);
			}
		});

		return result;

	}

}
