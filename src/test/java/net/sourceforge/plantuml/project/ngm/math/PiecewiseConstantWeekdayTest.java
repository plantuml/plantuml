package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class PiecewiseConstantWeekdayTest {

	@Test
	void segmentAt_returnsForwardAndBackwardSegments_forGivenInstant() {
		// Given: A weekly pattern with uniform 30% workload (3/10) for all days
		final PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(new Fraction(3, 10));

		LocalDateTime mondayAt9am = LocalDate.of(2026, 1, 5).with(DayOfWeek.MONDAY).atTime(9, 0);

		assertEquals("FORWARD ]2026-01-05T00:00, 2026-01-06T00:00[ value=3/10",
				wk.segmentAt(mondayAt9am, TimeDirection.FORWARD).toString());

		assertEquals("BACKWARD ]2026-01-06T00:00, 2026-01-05T00:00[ value=3/10",
				wk.segmentAt(mondayAt9am, TimeDirection.BACKWARD).toString());

	}

	@Test
	void iterateSegmentsFrom_appliesDayOfWeekMapping_acrossWeekBoundaryForward() {
		// Given: A weekly pattern with Friday at 100%, Saturday at 50%, other days at 0%
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO).with(DayOfWeek.FRIDAY, Fraction.ONE)
				.with(DayOfWeek.SATURDAY, new Fraction(1, 2));

		LocalDateTime from = LocalDate.of(2026, 1, 2).atTime(9, 0); // Friday

		// When: Iterating forward from Friday
		final Iterator<Segment> it = wk.iterateSegmentsFrom(from, TimeDirection.FORWARD);

		// Then: Friday is 100%, Saturday is 50%, Sunday defaults to 0%
		assertEquals("FORWARD ]2026-01-02T00:00, 2026-01-03T00:00[ value=1", it.next().toString());
		assertEquals("FORWARD ]2026-01-03T00:00, 2026-01-04T00:00[ value=1/2", it.next().toString());
		assertEquals("FORWARD ]2026-01-04T00:00, 2026-01-05T00:00[ value=0", it.next().toString());
	}

	@Test
	void iterateSegmentsBackwardFrom_appliesDayOfWeekMapping_acrossWeekBoundaryBackward() {
		// Given: A weekly pattern with Friday at 100%, Saturday at 50%, other days at 0%
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO).with(DayOfWeek.FRIDAY, Fraction.ONE)
				.with(DayOfWeek.SATURDAY, new Fraction(1, 2));

		LocalDateTime from = LocalDate.of(2026, 1, 4).atTime(9, 0); // Sunday 09:00

		// When: Iterating backward from Sunday
		final Iterator<Segment> it = wk.iterateSegmentsFrom(from, TimeDirection.BACKWARD);

		// Then: Sunday is 0%, Saturday is 50%, Friday is 100%
		assertEquals("BACKWARD ]2026-01-05T00:00, 2026-01-04T00:00[ value=0", it.next().toString());
		assertEquals("BACKWARD ]2026-01-04T00:00, 2026-01-03T00:00[ value=1/2", it.next().toString());
		assertEquals("BACKWARD ]2026-01-03T00:00, 2026-01-02T00:00[ value=1", it.next().toString());
	}

	@Test
	void iterateSegmentsFrom_whenAtMidnight_includesThatDay() {
		// Given: A weekly pattern with Monday at 100%, other days at 0%
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO).with(DayOfWeek.MONDAY, Fraction.ONE);
		LocalDateTime from = LocalDate.of(2025, 12, 1).atStartOfDay(); // Monday 00:00

		// When: Iterating from exactly midnight Monday
		Iterator<Segment> it = wk.iterateSegmentsFrom(from, TimeDirection.FORWARD);

		// Then: First segment is the full Monday with 100% workload
		Segment s0 = it.next();
		assertEquals(from, s0.startExclusive(), "First segment should start at midnight Monday");
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), s0.endExclusive(),
				"Segment should end at midnight Tuesday");
		assertEquals(Fraction.ONE, s0.getValue(), "Monday should have 100% workload");
		assertEquals("FORWARD ]2025-12-01T00:00, 2025-12-02T00:00[ value=1", s0.toString());
	}

	@Test
	void iterateSegmentsFrom_whenAtMidday_returnsSegmentContainingThatInstant() {
		// Given: A weekly pattern with Monday at 100%, Tuesday at 50%, other days at 0%
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO).with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, new Fraction(1, 2));
		LocalDateTime from = LocalDate.of(2025, 12, 1).atTime(12, 0); // Monday noon

		// When: Iterating from Monday noon
		Iterator<Segment> it = wk.iterateSegmentsFrom(from, TimeDirection.FORWARD);

		// Then: First segment is Monday (containing the instant) with 100%
		Segment monday = it.next();
		assertEquals(LocalDate.of(2025, 12, 1).atStartOfDay(), monday.startExclusive());
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), monday.endExclusive());
		assertEquals(Fraction.ONE, monday.getValue(), "Monday should have 100% workload");
		assertEquals("FORWARD ]2025-12-01T00:00, 2025-12-02T00:00[ value=1", monday.toString());

		// Then: Second segment is Tuesday with 50%
		Segment tuesday = it.next();
		assertEquals("FORWARD ]2025-12-02T00:00, 2025-12-03T00:00[ value=1/2", tuesday.toString());
	}

	@Test
	void iterateSegmentsFrom_producesDailySegments_withCorrectValuesOverSeveralDays() {
		// Given: A weekly pattern with Wednesday at 40%, Thursday at 60%, other days at 0%
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.WEDNESDAY, new Fraction(2, 5)) // 40%
				.with(DayOfWeek.THURSDAY, new Fraction(3, 5)); // 60%
		LocalDateTime from = LocalDate.of(2025, 12, 3).atStartOfDay(); // Wednesday

		// When: Iterating from Wednesday
		Iterator<Segment> it = wk.iterateSegmentsFrom(from, TimeDirection.FORWARD);

		// Then: Wednesday is 40%
		Segment wed = it.next();
		assertEquals(LocalDate.of(2025, 12, 3).atStartOfDay(), wed.startExclusive());
		assertEquals(LocalDate.of(2025, 12, 4).atStartOfDay(), wed.endExclusive());
		assertEquals(new Fraction(2, 5), wed.getValue());
		assertEquals("FORWARD ]2025-12-03T00:00, 2025-12-04T00:00[ value=2/5", wed.toString());

		// Then: Thursday is 60%
		Segment thu = it.next();
		assertEquals(LocalDate.of(2025, 12, 4).atStartOfDay(), thu.startExclusive());
		assertEquals(LocalDate.of(2025, 12, 5).atStartOfDay(), thu.endExclusive());
		assertEquals(new Fraction(3, 5), thu.getValue());
		assertEquals("FORWARD ]2025-12-04T00:00, 2025-12-05T00:00[ value=3/5", thu.toString());

		// Then: Friday defaults to 0%
		Segment fri = it.next();
		assertEquals(LocalDate.of(2025, 12, 5).atStartOfDay(), fri.startExclusive());
		assertEquals(LocalDate.of(2025, 12, 6).atStartOfDay(), fri.endExclusive());
		assertEquals(Fraction.ZERO, fri.getValue());
		assertEquals("FORWARD ]2025-12-05T00:00, 2025-12-06T00:00[ value=0", fri.toString());
	}

	@Test
	void segmentValue_isConsistentWithApply_forAnyInstantWithinDay() {
		// Given: A weekly pattern with Monday at 30%, Tuesday at 50%, Wednesday at 100%
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, new Fraction(3, 10)) // 30%
				.with(DayOfWeek.TUESDAY, new Fraction(1, 2)) // 50%
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE); // 100%

		// Then: For any instant within a day, the segment value matches the day's workload
		assertApplyMatchesFirstDailySegment(wk, LocalDate.of(2025, 12, 1).atTime(10, 15));  // Monday 10:15
		assertApplyMatchesFirstDailySegment(wk, LocalDate.of(2025, 12, 2).atTime(23, 59));  // Tuesday 23:59
		assertApplyMatchesFirstDailySegment(wk, LocalDate.of(2025, 12, 3).atTime(0, 1));    // Wednesday 00:01
	}

	/**
	 * Verifies that the segment containing the given instant has the expected
	 * structure: starts at day boundary, spans exactly one day, and contains the
	 * instant.
	 */
	private static void assertApplyMatchesFirstDailySegment(PiecewiseConstantWeekday wk, LocalDateTime instant) {
		LocalDateTime dayStart = instant.toLocalDate().atStartOfDay();

		Iterator<Segment> it = wk.iterateSegmentsFrom(dayStart, TimeDirection.FORWARD);
		assertTrue(it.hasNext(), "Iterator should provide at least one segment");

		final Segment seg = it.next();

		assertEquals(dayStart, seg.startExclusive(), "Segment should start at day boundary");
		assertEquals(dayStart.plusDays(1), seg.endExclusive(), "Segment should span exactly one day");
		assertFalse(instant.isBefore(seg.startExclusive()), "Instant should not be before the segment start");
		assertTrue(instant.isBefore(seg.endExclusive()), "Instant should be before the segment end");
	}

}
