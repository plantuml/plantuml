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

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.gantt.Failable;
import net.sourceforge.plantuml.gantt.GanttDiagram;
import net.sourceforge.plantuml.gantt.time.DayOfWeekUtils;
import net.sourceforge.plantuml.gantt.ulang.VerbPhraseAction;
import net.sourceforge.plantuml.klimt.color.HColor;

public class SubjectDayOfWeek implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectDayOfWeek();

	private SubjectDayOfWeek() {
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionSubject() {
		return new UBrexNamed("SUBJECT", DayOfWeekUtils.getUbrex());
	}

	@Override
	public Collection<VerbPhraseAction> getVerbPhrases() {
		final List<VerbPhraseAction> result = new ArrayList<>();
		result.add(new VerbPhraseAction(Verbs.are, new ComplementOpen()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
				final DayOfWeek day = (DayOfWeek) subject;
				gantt.openDayOfWeek(day, (String) complement);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.are, new ComplementClose()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
				final DayOfWeek day = (DayOfWeek) subject;
				gantt.closeDayOfWeek(day, (String) complement);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.isOrAre, new ComplementInColors2()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
				final HColor color = ((CenterBorderColor) complement).getCenter();
				final DayOfWeek day = (DayOfWeek) subject;
				gantt.colorDay(day, color);

				return CommandExecutionResult.ok();
			}
		});

		return result;

	}

	@Override
	public Failable<DayOfWeek> getMe(GanttDiagram gantt, UMatcher arg) {
		final String s = arg.findFirstValueByKey("SUBJECT");
		return Failable.ok(DayOfWeekUtils.fromString(s));
	}

}
