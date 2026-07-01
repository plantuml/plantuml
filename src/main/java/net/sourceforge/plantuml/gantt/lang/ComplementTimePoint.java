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
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.plantuml.ubrex.CaptureLookup;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexOptional;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.gantt.Failable;
import net.sourceforge.plantuml.gantt.GanttDiagram;
import net.sourceforge.plantuml.gantt.time.TimePoint;

public class ComplementTimePoint implements Something<GanttDiagram> {

	private ComplementTimePoint() {
	}

	public static ComplementTimePoint any() {
		return new ComplementTimePoint();
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionComplement() {
		final DayPattern dayPattern = new DayPattern("");
		return UBrexConcat.build( //
				dayPattern.toUbrex(), //
				toUbrexTime() //
		);
	}

	private UBrexPart toUbrexTime() {
		final UBrexPart digits1to2 = new UBrexLeaf("〇{1-2}〴d");
		final UBrexPart seconds = new UBrexOptional(UBrexConcat.build( //
				new UBrexLeaf(":"), //
				new UBrexNamed("SECOND", digits1to2) //
		));
		return UBrexConcat.build( //
				new UBrexLeaf("「Tt∙」"), //
				new UBrexNamed("HOUR", digits1to2), //
				new UBrexLeaf(":"), //
				new UBrexNamed("MINUTE", digits1to2), //
				seconds //
		);
	}

	@Override
	public Failable<TimePoint> getMe(GanttDiagram gantt, CaptureLookup arg) {
		final DayPattern dayPattern = new DayPattern("");
		final LocalDate day = dayPattern.getDay(arg);
		if (day == null)
			throw new IllegalStateException();
		return Failable.ok(TimePoint.of(LocalDateTime.of(day, getTime(arg))));
	}

	private LocalTime getTime(CaptureLookup arg) {
		final int hour = Integer.parseInt(arg.findFirstValueByKey("HOUR"));
		final int minute = Integer.parseInt(arg.findFirstValueByKey("MINUTE"));
		final int second = arg.findFirstValueByKey("SECOND") == null ? 0 : Integer.parseInt(arg.findFirstValueByKey("SECOND"));
		return LocalTime.of(hour, minute, second);
	}

}
