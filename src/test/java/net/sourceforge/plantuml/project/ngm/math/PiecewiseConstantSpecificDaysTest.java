package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class PiecewiseConstantSpecificDaysTest {

	@Test
	void defaultValueIsReturnedForAnyDay() {
		// Given: a piecewise constant function with a default workload of ONE
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays.of(Fraction.ONE);

		// When / Then: any arbitrary day should return the default value
		assertEquals(Fraction.ONE, f.apply(LocalDateTime.of(2025, 12, 31, 10, 0)));
		assertEquals(Fraction.ONE, f.apply(LocalDateTime.of(2026, 1, 2, 10, 0)));

		// Also check a different time within the same day to confirm it's "day-constant"
		assertEquals(Fraction.ONE, f.apply(LocalDateTime.of(2026, 1, 2, 23, 59)));
	}

	@Test
	void specificDayOverrideReplacesDefault_forJanuaryFirst2026() {
		// Given: default ONE with a specific override for 2026-01-01 set to ZERO
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays
				.of(Fraction.ONE)
				.withDay(LocalDate.of(2026, 1, 1), Fraction.ZERO);

		// Then: the overridden day returns ZERO for any time within that day
		assertEquals(Fraction.ZERO, f.apply(LocalDateTime.of(2026, 1, 1, 0, 0)));
		assertEquals(Fraction.ZERO, f.apply(LocalDateTime.of(2026, 1, 1, 12, 0)));
		assertEquals(Fraction.ZERO, f.apply(LocalDateTime.of(2026, 1, 1, 23, 59)));

		// And: surrounding days still return the default
		assertEquals(Fraction.ONE, f.apply(LocalDateTime.of(2025, 12, 31, 12, 0)));
		assertEquals(Fraction.ONE, f.apply(LocalDateTime.of(2026, 1, 2, 12, 0)));
	}
	
	@Test
	void segmentsStartingAt_whenGivenExactStartOfDay_includesThatDay() {
		// Given: A pattern with all days at 100% workload
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays
				.of(Fraction.ONE);

		// Given: Starting point is exactly at the beginning of day (2025-12-01 00:00)
		LocalDateTime from = LocalDate.of(2025, 12, 1).atStartOfDay();

		// When: Requesting segments starting from midnight that day
		Iterator<Segment> it = f.iterateSegmentsFrom(from);

		// Then: The first segment should be the full day
		// The segment contains the given instant (00:00) and starts exactly at 00:00
		Segment s0 = it.next();
		assertEquals(from, s0.aInclusive(), "First segment should start at midnight that day");
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), s0.bExclusive(), "Segment should end at midnight next day");
		assertEquals(Fraction.ONE, s0.getValue(), "The day should have 100% workload");
		assertEquals("FORWARD [2025-12-01T00:00, 2025-12-02T00:00) value=1", s0.toString());
	}
	
	@Test
	void segmentsStartingAt_whenGivenMidday_returnsSegmentContainingThatInstant() {
		// Given: A pattern with:
		// - 2025-12-01: 100% workload
		// - 2025-12-02: 50% workload
		// - Default workload: 0% workload
		PiecewiseConstantSpecificDays pw = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 1), Fraction.ONE)
				.withDay(LocalDate.of(2025, 12, 2), new Fraction(1, 2));

		// Given: Starting point is 2025-12-01 at noon (12:00), in the middle of the day
		LocalDateTime from = LocalDate.of(2025, 12, 1).atTime(12, 0); // 2025-12-01 noon

		// When: Requesting segments containing Monday noon
		Iterator<Segment> it = pw.iterateSegmentsFrom(from);

		// Then: The first segment returned should be 2025-12-01 full day segment [00:00, 00:00+1day)
		// IMPORTANT: Even though we request from 12:00, the segment starts at 00:00 (beginning of day)
		// This demonstrates that segmentsStartingAt returns the segment CONTAINING the instant,
		// not a segment starting exactly at that instant
		Segment segment1 = it.next();
		assertEquals(LocalDate.of(2025, 12, 1).atStartOfDay(), segment1.aInclusive(), 
				"Monday segment should start at midnight, not at noon");
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), segment1.bExclusive(), 
				"Monday segment should end at midnight Tuesday");
		assertEquals(Fraction.ONE, segment1.getValue(), "Monday should have 100% workload");
		assertEquals("FORWARD [2025-12-01T00:00, 2025-12-02T00:00) value=1",
				segment1.toString());

		// Then: The second segment should be 2025-12-02 full day
		Segment tuesday = it.next();
		assertEquals("FORWARD [2025-12-02T00:00, 2025-12-03T00:00) value=1/2",
				tuesday.toString());
	}

	@Test
	void segmentsStartingAt_producesDailySegments_withCorrectValuesOverSeveralDays() {
		// Given: A pattern with specific workloads:
		// - Default workload: 0% workload
		// - 2025-12-02: 40% workload (2/5)
		// - 2025-12-03: 60% workload (3/5)
		PiecewiseConstantSpecificDays pw = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 2), new Fraction(2, 5)) // 40%
				.withDay(LocalDate.of(2025, 12, 3), new Fraction(3, 5)); // 60%

		// Given: Starting point is 2025-12-02 at midnight
		LocalDateTime from = LocalDate.of(2025, 12, 2).atStartOfDay();
		
		// When: Requesting segments from Wednesday onwards
		Iterator<Segment> it = pw.iterateSegmentsFrom(from);

		// Then: First segment should be Wednesday with 40% workload
		Segment segment1 = it.next();
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), segment1.aInclusive());
		assertEquals(LocalDate.of(2025, 12, 3).atStartOfDay(), segment1.bExclusive());
		assertEquals(new Fraction(2, 5), segment1.getValue());
		assertEquals("FORWARD [2025-12-02T00:00, 2025-12-03T00:00) value=2/5",
				segment1.toString());

		// Then: Second segment should be 2025-12-03 with 60% workload
		Segment segment2 = it.next();
		assertEquals(LocalDate.of(2025, 12, 3).atStartOfDay(), segment2.aInclusive());
		assertEquals(LocalDate.of(2025, 12, 4).atStartOfDay(), segment2.bExclusive());
		assertEquals(new Fraction(3, 5), segment2.getValue());
		assertEquals("FORWARD [2025-12-03T00:00, 2025-12-04T00:00) value=3/5",
				segment2.toString());

		// Then: Third segment should be 2025-12-04 with 0% workload (default)
		Segment segment3 = it.next();
		assertEquals(LocalDate.of(2025, 12, 4).atStartOfDay(), segment3.aInclusive());
		assertEquals(LocalDate.of(2025, 12, 5).atStartOfDay(), segment3.bExclusive());
		assertEquals(Fraction.ZERO, segment3.getValue());
		assertEquals("FORWARD [2025-12-04T00:00, 2025-12-05T00:00) value=0",
				segment3.toString());
	}
	
	@Test
	void applyIsConsistentWithSegments_forAnyInstantWithinTheDaySegment() {
		// Given: A pattern with specific workloads for 2025-12-01, 2025-12-02, and 2025-12-03
		PiecewiseConstantSpecificDays pw = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 1), new Fraction(3, 10))    // 30%
				.withDay(LocalDate.of(2025, 12, 2), new Fraction(1, 2))     // 50%
				.withDay(LocalDate.of(2025, 12, 3), Fraction.ONE);          // 100%

		// When/Then: For any instant within a day, apply() should return the same value
		// as the segment containing that instant
		
		// Test 2025-12-01 at 10:15 AM
		assertApplyMatchesFirstDailySegment(pw, LocalDateTime.of(2025, 12, 1, 10, 15));
		
		// Test 2025-12-02 at 11:59 PM (near end of day)
		assertApplyMatchesFirstDailySegment(pw, LocalDateTime.of(2025, 12, 2, 23, 59));
		
		// Test 2025-12-03 at 00:01 AM (near start of day)
		assertApplyMatchesFirstDailySegment(pw, LocalDateTime.of(2025, 12, 3, 0, 1));
	}

	/**
	 * Helper method to verify that apply(instant) returns the same value as the segment
	 * containing that instant.
	 * 
	 * This is a critical consistency check: the two ways of querying workload
	 * (point-wise via apply() vs. segment-wise via segmentsStartingAt()) must agree.
	 */
	private static void assertApplyMatchesFirstDailySegment(PiecewiseConstant pw, LocalDateTime instant) {
		// Get the start of the day containing the instant
		LocalDateTime dayStart = instant.toLocalDate().atStartOfDay();

		// Retrieve the segment containing this instant
		Iterator<Segment> it = pw.iterateSegmentsFrom(dayStart);
		assertTrue(it.hasNext(), "Iterator should provide at least one segment");

		Segment seg = it.next();

		// Verify the segment structure matches expectations
		assertEquals(dayStart, seg.aInclusive(), "Segment should start at day boundary");
		assertEquals(dayStart.plusDays(1), seg.bExclusive(), "Segment should span exactly one day");

		// Verify that the instant is actually within the segment bounds
		assertFalse(instant.isBefore(seg.aInclusive()), "Instant should not be before the segment start");
		assertTrue(instant.isBefore(seg.bExclusive()), "Instant should be before the segment end");

		// Core consistency assertion: apply() and segment value must match
		assertEquals(pw.apply(instant), seg.getValue(),
				"apply(instant) should match the value of the segment containing that instant");
	}

}
