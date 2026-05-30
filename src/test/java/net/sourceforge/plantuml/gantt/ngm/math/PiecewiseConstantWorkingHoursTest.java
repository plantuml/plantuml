package net.sourceforge.plantuml.gantt.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.gantt.core.TimeRange;

class PiecewiseConstantWorkingHoursTest {

	private static final LocalDate DAY = LocalDate.of(2026, 1, 5); // Monday

	private static TimeRange range(int startHour, int startMinute, int endHour, int endMinute) {
		return new TimeRange(LocalTime.of(startHour, startMinute), LocalTime.of(endHour, endMinute));
	}

	@Test
	void fromWorkingHours_withSingleRange_isClosedBeforeOpenInsideClosedAfter() {
		// Given: a single working range 8:00-12:00
		final PiecewiseConstantHours hours = PiecewiseConstantHours
				.fromWorkingHours(Collections.singletonList(range(8, 0, 12, 0)));

		// When: iterating the day forward from midnight
		final Iterator<Segment> it = hours.iterateSegmentsFrom(DAY.atStartOfDay(), TimeDirection.FORWARD);

		// Then: closed 00:00-08:00, open 08:00-12:00, closed 12:00-24:00
		assertEquals("FORWARD ]2026-01-05T00:00, 2026-01-05T08:00[ value=0", it.next().toString());
		assertEquals("FORWARD ]2026-01-05T08:00, 2026-01-05T12:00[ value=1", it.next().toString());
		assertEquals("FORWARD ]2026-01-05T12:00, 2026-01-06T00:00[ value=0", it.next().toString());
	}

	@Test
	void fromWorkingHours_withTwoRanges_modelsLunchBreak() {
		// Given: 8:00-12:00 and 13:00-18:00 (one hour lunch break)
		final PiecewiseConstantHours hours = PiecewiseConstantHours
				.fromWorkingHours(Arrays.asList(range(8, 0, 12, 0), range(13, 0, 18, 0)));

		final Iterator<Segment> it = hours.iterateSegmentsFrom(DAY.atStartOfDay(), TimeDirection.FORWARD);

		assertEquals("FORWARD ]2026-01-05T00:00, 2026-01-05T08:00[ value=0", it.next().toString());
		assertEquals("FORWARD ]2026-01-05T08:00, 2026-01-05T12:00[ value=1", it.next().toString());
		assertEquals("FORWARD ]2026-01-05T12:00, 2026-01-05T13:00[ value=0", it.next().toString());
		assertEquals("FORWARD ]2026-01-05T13:00, 2026-01-05T18:00[ value=1", it.next().toString());
		assertEquals("FORWARD ]2026-01-05T18:00, 2026-01-06T00:00[ value=0", it.next().toString());
	}

	@Test
	void fromWorkingHours_withEmptyList_isClosedAllDay() {
		// Given: no working hours at all
		final PiecewiseConstantHours hours = PiecewiseConstantHours.fromWorkingHours(Collections.emptyList());

		// When/Then: the whole day is a single closed segment
		final Segment seg = hours.segmentAt(DAY.atTime(9, 0), TimeDirection.FORWARD);
		assertEquals("FORWARD ]2026-01-05T00:00, 2026-01-06T00:00[ value=0", seg.toString());
	}

	@Test
	void fromWorkingHours_withNull_isClosedAllDay() {
		// Given: a null list (defensive case)
		final PiecewiseConstantHours hours = PiecewiseConstantHours.fromWorkingHours(null);

		// When/Then: behaves like an empty list, closed all day long
		final Segment seg = hours.segmentAt(DAY.atTime(15, 30), TimeDirection.FORWARD);
		assertEquals("FORWARD ]2026-01-05T00:00, 2026-01-06T00:00[ value=0", seg.toString());
	}

	@Test
	void fromWorkingHours_withRangeUntilMidnight_staysOpenUntilEndOfDay() {
		// Given: an afternoon/evening shift 14:00-24:00 (end expressed as midnight)
		final PiecewiseConstantHours hours = PiecewiseConstantHours
				.fromWorkingHours(Collections.singletonList(range(14, 0, 0, 0)));

		final Iterator<Segment> it = hours.iterateSegmentsFrom(DAY.atStartOfDay(), TimeDirection.FORWARD);

		// Then: closed until 14:00, then open through the end of the day
		assertEquals("FORWARD ]2026-01-05T00:00, 2026-01-05T14:00[ value=0", it.next().toString());
		assertEquals("FORWARD ]2026-01-05T14:00, 2026-01-06T00:00[ value=1", it.next().toString());
	}

	@Test
	void fromWorkingHours_appliesSameProfileEveryDay() {
		// Given: 9:00-17:00
		final PiecewiseConstantHours hours = PiecewiseConstantHours
				.fromWorkingHours(Collections.singletonList(range(9, 0, 17, 0)));

		// Then: the same intra-day profile repeats on a different (later) day
		final LocalDate otherDay = LocalDate.of(2026, 3, 18);
		final LocalDateTime insideWork = otherDay.atTime(10, 0);
		final LocalDateTime outsideWork = otherDay.atTime(20, 0);

		assertEquals("FORWARD ]2026-03-18T09:00, 2026-03-18T17:00[ value=1",
				hours.segmentAt(insideWork, TimeDirection.FORWARD).toString());
		assertEquals("FORWARD ]2026-03-18T17:00, 2026-03-19T00:00[ value=0",
				hours.segmentAt(outsideWork, TimeDirection.FORWARD).toString());
	}

	@Test
	void fromWorkingHours_backwardIteration_returnsConsistentSegments() {
		// Given: 8:00-12:00
		final PiecewiseConstantHours hours = PiecewiseConstantHours
				.fromWorkingHours(Collections.singletonList(range(8, 0, 12, 0)));

		// When: iterating backward from midnight at the end of the day
		final Iterator<Segment> it = hours.iterateSegmentsFrom(DAY.plusDays(1).atStartOfDay(),
				TimeDirection.BACKWARD);

		// Then: segments are walked from the end of the day back to its start
		assertEquals("BACKWARD ]2026-01-06T00:00, 2026-01-05T12:00[ value=0", it.next().toString());
		assertEquals("BACKWARD ]2026-01-05T12:00, 2026-01-05T08:00[ value=1", it.next().toString());
		assertEquals("BACKWARD ]2026-01-05T08:00, 2026-01-05T00:00[ value=0", it.next().toString());
	}

	@Test
	void fromWorkingHours_withInvertedRange_throws() {
		// Given: an inverted range 12:00-08:00 (end before start, not midnight)
		final List<TimeRange> ranges = Collections.singletonList(range(12, 0, 8, 0));

		// When/Then: with(...) rejects start >= end
		assertThrows(IllegalArgumentException.class, () -> PiecewiseConstantHours.fromWorkingHours(ranges));
	}

}
