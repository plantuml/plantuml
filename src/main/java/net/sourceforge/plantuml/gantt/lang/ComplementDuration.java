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

import java.time.Duration;
import java.util.List;

import com.plantuml.ubrex.CaptureLookup;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexPart;
import com.plantuml.ubrex.builder.UBrexZeroOrMore;

import net.sourceforge.plantuml.gantt.Failable;
import net.sourceforge.plantuml.gantt.GanttDiagram;
import net.sourceforge.plantuml.gantt.ngm.Load;

public class ComplementDuration implements Something<GanttDiagram> {

	@Override
	public UBrexPart toUnicodeBracketedExpressionComplement() {
		return UBrexConcat.build( //
				oneElement("0"), //
				new UBrexZeroOrMore(UBrexConcat.build( //
						separator(), //
						oneElement("1"))));
	}

	// A single "<number> <unit>[s]" element.
	private static UBrexPart oneElement(String prefix) {
		return UBrexConcat.build( //
				new UBrexNamed("CNUM" + prefix, new UBrexLeaf("〇+〴d")), //
				UBrexLeaf.spaceOneOrMore(), //
				new UBrexNamed("CUNIT" + prefix, //
						new UBrexLeaf("【hour┇minute┇second┇day┇week┇month】")), //
				new UBrexLeaf("〇?s"));
	}

	private static UBrexPart separator() {
		return new UBrexLeaf("【〇+「∙,」and〇+「∙,」┇〇+「∙,」】");
	}

	@Override
	public Failable<Load> getMe(GanttDiagram gantt, CaptureLookup arg) {

		final int[] totals = new int[4]; // days, hours, minutes, seconds

		// The first element is captured under CNUM0/CUNIT0, the repeated ones
		// under CNUM1/CUNIT1.
		accumulate(gantt, totals, arg.findFirstValueByKey("CNUM0"), arg.findFirstValueByKey("CUNIT0"));

		final List<String> cnum = arg.findValuesByKey("CNUM1");
		final List<String> cunit = arg.findValuesByKey("CUNIT1");
		for (int i = 0; i < cnum.size(); i++)
			accumulate(gantt, totals, cnum.get(i), cunit.get(i));

		final Duration duration = gantt.durationOfDaysHoursMinutesSeconds(totals[0], totals[1], totals[2], totals[3]);

		return Failable.ok(Load.of(duration));
	}

	private static void accumulate(GanttDiagram gantt, int[] totals, String num, String unit) {
		final int value = Integer.parseInt(num);
		switch (unit.charAt(0)) {
		case 'H':
		case 'h':
			totals[1] += value;
			break;
		case 'D':
		case 'd':
			totals[0] += value;
			break;
		case 'W':
		case 'w':
			totals[0] += value * gantt.daysInWeek();
			break;
		case 'S':
		case 's':
			totals[3] += value;
			break;
		case 'M':
		case 'm':
			// 'm' is ambiguous between "minute" and "month": disambiguate on the
			// second letter ("mi" -> minute, otherwise month).
			if (unit.length() >= 2 && (unit.charAt(1) == 'i' || unit.charAt(1) == 'I'))
				totals[2] += value;
			else
				totals[0] += value * gantt.daysInMonth();
			break;
		default:
			throw new IllegalArgumentException("unknown time unit: " + unit);
		}
	}
}
