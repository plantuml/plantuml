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
 * Original Author:  Arnaud Roques, Mario Kušek
 * 
 *
 */
package net.sourceforge.plantuml.project.ngm.math;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class PiecewiseConstantHours extends AbstractPiecewiseConstant {

	private final Fraction defaultValue;

	private final NavigableMap<LocalTime, Fraction> timeBoundaries = new TreeMap<>();

	private PiecewiseConstantHours(Fraction defaultValue) {
		this.defaultValue = defaultValue;
	}

	public static PiecewiseConstantHours of(Fraction defaultValue) {
		return new PiecewiseConstantHours(defaultValue);
	}

	@Override
	public Segment segmentAt(LocalDateTime instant, TimeDirection direction) {
		return direction == TimeDirection.FORWARD ? segmentForwardAt(instant) : segmentBackwardAt(instant);
	}

	private Segment segmentForwardAt(LocalDateTime instant) {
		final LocalDate day = instant.toLocalDate();
		final LocalDateTime dayStart = day.atStartOfDay();
		final LocalTime t = instant.toLocalTime();
		final Entry<LocalTime, Fraction> ent = timeBoundaries.floorEntry(t);

		final LocalTime startKey = ent != null ? ent.getKey() : LocalTime.MIDNIGHT;
		final Fraction v = timeBoundaries.get(startKey);
		final Fraction value = v != null ? v : defaultValue;

		final LocalTime endKey = timeBoundaries.higherKey(startKey); // next boundary after start
		final LocalDateTime start = dayStart.with(startKey);
		final LocalDateTime end = (endKey == null) ? dayStart.plusDays(1) : dayStart.with(endKey);

		return Segment.forward(start, end, value);
	}

	private Segment segmentBackwardAt(LocalDateTime instant) {
		final LocalTime t = instant.toLocalTime();

		// At midnight, backward segment is the last one of the previous day
		if (t.equals(LocalTime.MIDNIGHT)) {
			final LocalDateTime dayStart = instant.toLocalDate().minusDays(1).atStartOfDay();

			final Entry<LocalTime, Fraction> last = timeBoundaries.lastEntry(); // may be null
			final LocalTime startKey = (last == null) ? LocalTime.MIDNIGHT : last.getKey();
			final Fraction value = (last == null) ? defaultValue : last.getValue();

			final LocalDateTime start = dayStart.plusDays(1); // 00:00 of next day
			final LocalDateTime end = dayStart.with(startKey); // start of last segment
			return Segment.backward(start, end, value);
		}

		final LocalDateTime dayStart = instant.toLocalDate().atStartOfDay();

		final Entry<LocalTime, Fraction> ent = timeBoundaries.lowerEntry(t);
		final LocalTime startKey = (ent == null) ? LocalTime.MIDNIGHT : ent.getKey();
		final Fraction value = (ent == null) ? defaultValue : ent.getValue();

		final LocalTime endKey = timeBoundaries.higherKey(startKey); // end of that segment
		final LocalDateTime start = (endKey == null) ? dayStart.plusDays(1) : dayStart.with(endKey);
		final LocalDateTime end = dayStart.with(startKey);

		return Segment.backward(start, end, value);
	}

	public PiecewiseConstantHours with(LocalTime start, LocalTime end, Fraction fraction) {

		if (end.equals(LocalTime.MIDNIGHT) == false && start.compareTo(end) >= 0)
			throw new IllegalArgumentException("start must be strictly before end");

		final PiecewiseConstantHours result = new PiecewiseConstantHours(defaultValue);
		result.timeBoundaries.putAll(this.timeBoundaries);
		result.timeBoundaries.put(start, fraction);
		if (end.equals(LocalTime.MIDNIGHT) == false)
			result.timeBoundaries.put(end, defaultValue);
		return result;
	}
}
