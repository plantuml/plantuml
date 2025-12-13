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
	void ofSameWorkload_appliesSameFractionForAllDays() {
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(new Fraction(3, 10));

		for (DayOfWeek d : DayOfWeek.values()) {
			LocalDateTime t = LocalDate.of(2025, 12, 1).with(d).atTime(9, 0);
			assertEquals(new Fraction(3, 10), wk.apply(t), "Unexpected workload on " + d);
		}
	}

	@Test
	void with_updatesOnlyOneDay_andKeepsImmutability() {
		PiecewiseConstantWeekday base = PiecewiseConstantWeekday.of(Fraction.ZERO);

		PiecewiseConstantWeekday monday30 = base.with(DayOfWeek.MONDAY, new Fraction(3, 10));

		// Original remains unchanged
		assertEquals(Fraction.ZERO, base.apply(LocalDate.of(2025, 12, 1).atStartOfDay())); // Monday
		// New instance has updated value
		assertEquals(new Fraction(3, 10), monday30.apply(LocalDate.of(2025, 12, 1).atStartOfDay()));

		// Another day still zero
		assertEquals(Fraction.ZERO, monday30.apply(LocalDate.of(2025, 12, 2).atStartOfDay())); // Tuesday
	}

	@Test
	void apply_usesDayOfWeekMapping_acrossAWeekBoundary() {
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO).with(DayOfWeek.FRIDAY, Fraction.ONE)
				.with(DayOfWeek.SATURDAY, new Fraction(1, 2));

		// Friday
		assertEquals(Fraction.ONE, wk.apply(LocalDate.of(2025, 12, 5).atTime(10, 0)));
		// Saturday
		assertEquals(new Fraction(1, 2), wk.apply(LocalDate.of(2025, 12, 6).atTime(10, 0)));
		// Sunday defaults to zero
		assertEquals(Fraction.ZERO, wk.apply(LocalDate.of(2025, 12, 7).atTime(10, 0)));
	}

	@Test
	void segmentsStartingAt_whenGivenExactStartOfDay_includesThatDay() {
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO).with(DayOfWeek.MONDAY, Fraction.ONE);

		LocalDateTime from = LocalDate.of(2025, 12, 1).atStartOfDay(); // Monday
		Iterator<PiecewiseConstant.Segment> it = wk.iterateSegmentsFrom(from);

		PiecewiseConstant.Segment s0 = it.next();
		assertEquals(from, s0.getStartInclusive());
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), s0.getEndExclusive());
		assertEquals(Fraction.ONE, s0.getValue());
		assertEquals("Segment{startInclusive=2025-12-01T00:00, endExclusive=2025-12-02T00:00, value=1}", s0.toString());
	}

	@Test
	void segmentsStartingAt_whenGivenMidday_skipsToNextDay() {
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO).with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, new Fraction(1, 2));

		LocalDateTime from = LocalDate.of(2025, 12, 1).atTime(12, 0); // Monday noon
		Iterator<PiecewiseConstant.Segment> it = wk.iterateSegmentsFrom(from);

		// Should begin on Monday
		PiecewiseConstant.Segment monday = it.next();
		assertEquals(LocalDate.of(2025, 12, 1).atStartOfDay(), monday.getStartInclusive());
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), monday.getEndExclusive());
		assertEquals(Fraction.ONE, monday.getValue());
		assertEquals("Segment{startInclusive=2025-12-01T00:00, endExclusive=2025-12-02T00:00, value=1}",
				monday.toString());

		PiecewiseConstant.Segment tuesday = it.next();
		assertEquals("Segment{startInclusive=2025-12-02T00:00, endExclusive=2025-12-03T00:00, value=1/2}",
				tuesday.toString());

	}

	@Test
	void segmentsStartingAt_producesDailySegments_withCorrectValuesOverSeveralDays() {
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.WEDNESDAY, new Fraction(2, 5)) // 40%
				.with(DayOfWeek.THURSDAY, new Fraction(3, 5)); // 60%

		LocalDateTime from = LocalDate.of(2025, 12, 3).atStartOfDay(); // Wednesday
		Iterator<PiecewiseConstant.Segment> it = wk.iterateSegmentsFrom(from);

		PiecewiseConstant.Segment wed = it.next();
		assertEquals(LocalDate.of(2025, 12, 3).atStartOfDay(), wed.getStartInclusive());
		assertEquals(LocalDate.of(2025, 12, 4).atStartOfDay(), wed.getEndExclusive());
		assertEquals(new Fraction(2, 5), wed.getValue());
		assertEquals("Segment{startInclusive=2025-12-03T00:00, endExclusive=2025-12-04T00:00, value=2/5}",
				wed.toString());

		PiecewiseConstant.Segment thu = it.next();
		assertEquals(LocalDate.of(2025, 12, 4).atStartOfDay(), thu.getStartInclusive());
		assertEquals(LocalDate.of(2025, 12, 5).atStartOfDay(), thu.getEndExclusive());
		assertEquals(new Fraction(3, 5), thu.getValue());
		assertEquals("Segment{startInclusive=2025-12-04T00:00, endExclusive=2025-12-05T00:00, value=3/5}",
				thu.toString());

		PiecewiseConstant.Segment fri = it.next();
		assertEquals(LocalDate.of(2025, 12, 5).atStartOfDay(), fri.getStartInclusive());
		assertEquals(LocalDate.of(2025, 12, 6).atStartOfDay(), fri.getEndExclusive());
		assertEquals(Fraction.ZERO, fri.getValue());
		assertEquals("Segment{startInclusive=2025-12-05T00:00, endExclusive=2025-12-06T00:00, value=0}",
				fri.toString());
	}

	@Test
	void applyIsConsistentWithSegments_forAnyInstantWithinTheDaySegment() {
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, new Fraction(3, 10)).with(DayOfWeek.TUESDAY, new Fraction(1, 2))
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE);

		// Pick instants that are clearly inside their respective days
		assertApplyMatchesFirstDailySegment(wk, LocalDate.of(2025, 12, 1).atTime(10, 15)); // Monday
		assertApplyMatchesFirstDailySegment(wk, LocalDate.of(2025, 12, 2).atTime(23, 59)); // Tuesday
		assertApplyMatchesFirstDailySegment(wk, LocalDate.of(2025, 12, 3).atTime(0, 1)); // Wednesday
	}

	private static void assertApplyMatchesFirstDailySegment(PiecewiseConstantWeekday wk, LocalDateTime instant) {
		LocalDateTime dayStart = instant.toLocalDate().atStartOfDay();

		Iterator<PiecewiseConstant.Segment> it = wk.iterateSegmentsFrom(dayStart);
		assertTrue(it.hasNext(), "Iterator should provide at least one segment");

		PiecewiseConstant.Segment seg = it.next();

		// The first segment should be exactly the day of 'instant'
		assertEquals(dayStart, seg.getStartInclusive(), "Segment should start at day boundary");
		assertEquals(dayStart.plusDays(1), seg.getEndExclusive(), "Segment should span exactly one day");

		// Sanity check that the instant lies inside the segment
		assertFalse(instant.isBefore(seg.getStartInclusive()), "Instant should not be before the segment start");
		assertTrue(instant.isBefore(seg.getEndExclusive()), "Instant should be before the segment end");

		// Core consistency assertion
		assertEquals(wk.apply(instant), seg.getValue(),
				"apply(instant) should match the value of the segment containing that instant");
	}

}
