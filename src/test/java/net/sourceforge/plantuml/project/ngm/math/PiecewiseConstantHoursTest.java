package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class PiecewiseConstantHoursTest {

	@Test
	void apply_returnsFractionONE_duringWorkingHours() {
		// Given: Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		// When/Then: During morning working hours (8:00-12:00), should return ONE
		assertEquals(Fraction.ONE, hours.apply(LocalDateTime.of(2025, 12, 15, 8, 0)));
		assertEquals(Fraction.ONE, hours.apply(LocalDateTime.of(2025, 12, 15, 10, 30)));
		assertEquals(Fraction.ONE, hours.apply(LocalDateTime.of(2025, 12, 15, 11, 59)));

		// When/Then: During afternoon working hours (14:00-18:00), should return ONE
		assertEquals(Fraction.ONE, hours.apply(LocalDateTime.of(2025, 12, 15, 14, 0)));
		assertEquals(Fraction.ONE, hours.apply(LocalDateTime.of(2025, 12, 15, 16, 0)));
		assertEquals(Fraction.ONE, hours.apply(LocalDateTime.of(2025, 12, 15, 17, 59)));
	}

	@Test
	void apply_returnsFractionZERO_outsideWorkingHours() {
		// Given: Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		// When/Then: Before working hours, should return ZERO
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 15, 7, 59)));
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 15, 0, 0)));

		// When/Then: During lunch break (12:00-14:00), should return ZERO
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 15, 12, 0)));
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 15, 13, 0)));
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 15, 13, 59)));

		// When/Then: After working hours, should return ZERO
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 15, 18, 0)));
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 15, 20, 0)));
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 15, 23, 59)));
	}

	@Test
	void apply_worksAcrossDifferentDays() {
		// Given: Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		// When/Then: The same hours apply to different days
		// Monday
		assertEquals(Fraction.ONE, hours.apply(LocalDateTime.of(2025, 12, 15, 9, 0)));
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 15, 13, 0)));

		// Tuesday
		assertEquals(Fraction.ONE, hours.apply(LocalDateTime.of(2025, 12, 16, 9, 0)));
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 16, 13, 0)));

		// Sunday
		assertEquals(Fraction.ONE, hours.apply(LocalDateTime.of(2025, 12, 21, 9, 0)));
		assertEquals(Fraction.ZERO, hours.apply(LocalDateTime.of(2025, 12, 21, 13, 0)));
	}

	@Test
	void of_createsUniformWorkload() {
		// Given: A uniform workload of 50% for all hours
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(new Fraction(1, 2));

		// When/Then: Any time of day should return the same workload
		assertEquals(new Fraction(1, 2), hours.apply(LocalDateTime.of(2025, 12, 15, 0, 0)));
		assertEquals(new Fraction(1, 2), hours.apply(LocalDateTime.of(2025, 12, 15, 12, 0)));
		assertEquals(new Fraction(1, 2), hours.apply(LocalDateTime.of(2025, 12, 15, 23, 59)));
	}

	@Test
	void with_updatesTimeRange_andKeepsImmutability() {
		// Given: A base schedule with zero workload
		PiecewiseConstantHours base = PiecewiseConstantHours.of(Fraction.ZERO);

		// When: Creating a new instance with 9:00-17:00 set to full workload
		PiecewiseConstantHours updated = base.with(LocalTime.of(9, 0), LocalTime.of(17, 0), Fraction.ONE);

		// Then: The original instance should remain unchanged (immutability)
		assertEquals(Fraction.ZERO, base.apply(LocalDateTime.of(2025, 12, 15, 10, 0)));

		// Then: The new instance should have the updated time range
		assertEquals(Fraction.ONE, updated.apply(LocalDateTime.of(2025, 12, 15, 10, 0)));
		assertEquals(Fraction.ZERO, updated.apply(LocalDateTime.of(2025, 12, 15, 8, 0)));
	}

	@Test
	void segmentsStartingAt_fromMidnight_producesCorrectSegments() {
		// Given: Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		// Given: Starting from midnight
		LocalDateTime from = LocalDate.of(2025, 12, 15).atStartOfDay();

		// When: Requesting segments from midnight
		Iterator<PiecewiseConstant.Segment> it = hours.iterateSegmentsFrom(from);

		// Then: First segment is non-working hours from 00:00 to 08:00
		PiecewiseConstant.Segment s0 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 0, 0), s0.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 15, 8, 0), s0.getEndExclusive());
		assertEquals(Fraction.ZERO, s0.getValue());

		// Then: Second segment is morning work from 08:00 to 12:00
		PiecewiseConstant.Segment s1 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 8, 0), s1.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 15, 12, 0), s1.getEndExclusive());
		assertEquals(Fraction.ONE, s1.getValue());

		// Then: Third segment is lunch break from 12:00 to 14:00
		PiecewiseConstant.Segment s2 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 12, 0), s2.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 15, 14, 0), s2.getEndExclusive());
		assertEquals(Fraction.ZERO, s2.getValue());

		// Then: Fourth segment is afternoon work from 14:00 to 18:00
		PiecewiseConstant.Segment s3 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 14, 0), s3.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 15, 18, 0), s3.getEndExclusive());
		assertEquals(Fraction.ONE, s3.getValue());

		// Then: Fifth segment is evening non-working hours from 18:00 to 00:00 next day
		PiecewiseConstant.Segment s4 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 18, 0), s4.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 16, 0, 0), s4.getEndExclusive());
		assertEquals(Fraction.ZERO, s4.getValue());
	}

	@Test
	void segmentsStartingAt_fromMorningWorkHours_startsWithinWorkSegment() {
		// Given: Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		// Given: Starting from 10:00 (during morning work)
		LocalDateTime from = LocalDateTime.of(2025, 12, 15, 10, 0);

		// When: Requesting segments from 10:00
		Iterator<PiecewiseConstant.Segment> it = hours.iterateSegmentsFrom(from);

		// Then: First segment should be the morning work segment containing 10:00
		PiecewiseConstant.Segment s0 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 8, 0), s0.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 15, 12, 0), s0.getEndExclusive());
		assertEquals(Fraction.ONE, s0.getValue());
		assertTrue(from.isAfter(s0.getStartInclusive()) || from.equals(s0.getStartInclusive()));
		assertTrue(from.isBefore(s0.getEndExclusive()));

		// Then: Second segment should be lunch break
		PiecewiseConstant.Segment s1 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 12, 0), s1.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 15, 14, 0), s1.getEndExclusive());
		assertEquals(Fraction.ZERO, s1.getValue());
	}

	@Test
	void segmentsStartingAt_fromLunchBreak_startsWithinLunchSegment() {
		// Given: Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		// Given: Starting from 13:00 (during lunch break)
		LocalDateTime from = LocalDateTime.of(2025, 12, 15, 13, 0);

		// When: Requesting segments from 13:00
		Iterator<PiecewiseConstant.Segment> it = hours.iterateSegmentsFrom(from);

		// Then: First segment should be the lunch break containing 13:00
		PiecewiseConstant.Segment s0 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 12, 0), s0.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 15, 14, 0), s0.getEndExclusive());
		assertEquals(Fraction.ZERO, s0.getValue());
		assertTrue(from.isAfter(s0.getStartInclusive()) || from.equals(s0.getStartInclusive()));
		assertTrue(from.isBefore(s0.getEndExclusive()));

		// Then: Second segment should be afternoon work
		PiecewiseConstant.Segment s1 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 14, 0), s1.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 15, 18, 0), s1.getEndExclusive());
		assertEquals(Fraction.ONE, s1.getValue());
	}

	@Test
	void segmentsStartingAt_fromAfternoonWorkHours_startsWithinAfternoonSegment() {
		// Given: Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		// Given: Starting from 16:00 (during afternoon work)
		LocalDateTime from = LocalDateTime.of(2025, 12, 15, 16, 0);

		// When: Requesting segments from 16:00
		Iterator<PiecewiseConstant.Segment> it = hours.iterateSegmentsFrom(from);

		// Then: First segment should be the afternoon work segment containing 16:00
		PiecewiseConstant.Segment s0 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 14, 0), s0.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 15, 18, 0), s0.getEndExclusive());
		assertEquals(Fraction.ONE, s0.getValue());
		assertTrue(from.isAfter(s0.getStartInclusive()) || from.equals(s0.getStartInclusive()));
		assertTrue(from.isBefore(s0.getEndExclusive()));

		// Then: Second segment should be evening non-work
		PiecewiseConstant.Segment s1 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 15, 18, 0), s1.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 16, 0, 0), s1.getEndExclusive());
		assertEquals(Fraction.ZERO, s1.getValue());
	}

	@Test
	void segmentsStartingAt_spansMultipleDays_repeatsPattern() {
		// Given: Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		// Given: Starting from midnight
		LocalDateTime from = LocalDate.of(2025, 12, 15).atStartOfDay();

		// When: Iterating through multiple days
		Iterator<PiecewiseConstant.Segment> it = hours.iterateSegmentsFrom(from);

		// Skip first day's segments (5 segments per day)
		for (int i = 0; i < 5; i++) {
			it.next();
		}

		// Then: Second day should start with the same pattern
		PiecewiseConstant.Segment day2s0 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 16, 0, 0), day2s0.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 16, 8, 0), day2s0.getEndExclusive());
		assertEquals(Fraction.ZERO, day2s0.getValue());

		PiecewiseConstant.Segment day2s1 = it.next();
		assertEquals(LocalDateTime.of(2025, 12, 16, 8, 0), day2s1.getStartInclusive());
		assertEquals(LocalDateTime.of(2025, 12, 16, 12, 0), day2s1.getEndExclusive());
		assertEquals(Fraction.ONE, day2s1.getValue());
	}

	@Test
	void applyIsConsistentWithSegments_forAnyInstantWithinSegment() {
		// Given: Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		PiecewiseConstantHours hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		// When/Then: For any instant, apply() should match the segment value

		// Test during morning work
		assertApplyMatchesSegment(hours, LocalDateTime.of(2025, 12, 15, 10, 30));

		// Test during lunch break
		assertApplyMatchesSegment(hours, LocalDateTime.of(2025, 12, 15, 13, 15));

		// Test during afternoon work
		assertApplyMatchesSegment(hours, LocalDateTime.of(2025, 12, 15, 16, 45));

		// Test before work hours
		assertApplyMatchesSegment(hours, LocalDateTime.of(2025, 12, 15, 7, 0));

		// Test after work hours
		assertApplyMatchesSegment(hours, LocalDateTime.of(2025, 12, 15, 20, 0));
	}

	/**
	 * Helper method to verify that apply(instant) returns the same value as the segment
	 * containing that instant.
	 */
	private static void assertApplyMatchesSegment(PiecewiseConstantHours hours, LocalDateTime instant) {
		// Retrieve the segment containing this instant
		Iterator<PiecewiseConstant.Segment> it = hours.iterateSegmentsFrom(instant);
		assertTrue(it.hasNext(), "Iterator should provide at least one segment");

		PiecewiseConstant.Segment seg = it.next();

		// Verify that the instant is actually within the segment bounds
		assertFalse(instant.isBefore(seg.getStartInclusive()), "Instant should not be before segment start");
		assertTrue(instant.isBefore(seg.getEndExclusive()), "Instant should be before segment end");

		// Core consistency assertion: apply() and segment value must match
		assertEquals(hours.apply(instant), seg.getValue(),
				"apply(instant) should match the value of the segment containing that instant");
	}

}
