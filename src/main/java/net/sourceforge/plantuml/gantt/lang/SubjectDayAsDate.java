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
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.gantt.Failable;
import net.sourceforge.plantuml.gantt.GanttDiagram;
import net.sourceforge.plantuml.gantt.ulang.VerbPhraseAction;
import net.sourceforge.plantuml.klimt.color.HColor;

public class SubjectDayAsDate implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectDayAsDate();

	private SubjectDayAsDate() {
	}

	@Override
	public Collection<VerbPhraseAction> getVerbPhrases() {
		final List<VerbPhraseAction> result = new ArrayList<>();
		result.add(new VerbPhraseAction(Verbs.isOrAre, new ComplementOpen()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
				gantt.openDayAsDate((LocalDate) subject, (String) complement);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.isOrAre, new ComplementClose()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
				gantt.closeDayAsDate((LocalDate) subject, (String) complement);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.isOrAre, new ComplementInColors2()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
				final HColor color = ((CenterBorderColor) complement).getCenter();
				gantt.colorDay((LocalDate) subject, color);
				return CommandExecutionResult.ok();
			}
		});

		return result;

	}

	@Override
	public Failable<LocalDate> getMe(GanttDiagram gantt, UMatcher arg) {
		if (arg.get("BDAY", 0) != null)
			return Failable.ok(resultB(arg));

		if (arg.get("ECOUNT", 0) != null)
			return Failable.ok(resultE(gantt, arg));

		throw new IllegalStateException();
	}

	private LocalDate resultB(UMatcher arg) {
		final int day = Integer.parseInt(arg.get("BDAY", 0));
		final int month = Integer.parseInt(arg.get("BMONTH", 0));
		final int year = Integer.parseInt(arg.get("BYEAR", 0));
		return LocalDate.of(year, month, day);
	}

	private LocalDate resultE(GanttDiagram gantt, UMatcher arg) {
		final String type = arg.get("ETYPE", 0).toUpperCase();
		final String operation = arg.get("EOPERATION", 0);
		int day = Integer.parseInt(arg.get("ECOUNT", 0));
		if ("-".equals(operation))
			day = -day;
		if ("D".equals(type))
			return gantt.getMinDay().plusDays(day);
		if ("T".equals(type))
			return gantt.getToday().plusDays(day);
		if ("E".equals(type))
			return gantt.getMaxDay().plusDays(day);
		throw new IllegalStateException();
	}

	private UBrexPart toUbrexB() {
		return TimeResolution.toUbrexB_YYYY_MM_DD("BYEAR", "BMONTH", "BDAY");
	}

	private UBrexPart toUbrexE() {
		return UBrexConcat.build( //
				new UBrexNamed("ETYPE", new UBrexLeaf("「dDtTeE」")), //
				new UBrexNamed("EOPERATION", new UBrexLeaf("「-+」")), //
				new UBrexNamed("ECOUNT", new UBrexLeaf("〇+〴d")) //
		);
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionSubject() {
		return new UBrexOr(toUbrexB(), toUbrexE());
	}

}