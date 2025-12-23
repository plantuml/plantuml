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
		// Given: A weekly pattern with the same 30% workload (3/10) applied to all days
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(new Fraction(3, 10));

		// When/Then: Every day of the week should have the same workload
		// We iterate through all 7 days of the week to verify consistency
		for (DayOfWeek d : DayOfWeek.values()) {
			// Create a timestamp for each day at 9:00 AM
			LocalDateTime t = LocalDate.of(2025, 12, 1).with(d).atTime(9, 0);
			assertEquals(new Fraction(3, 10), wk.apply(t), "Unexpected workload on " + d);
		}
	}

	@Test
	void with_updatesOnlyOneDay_andKeepsImmutability() {
		// Given: A weekly pattern with zero workload for all days
		PiecewiseConstantWeekday base = PiecewiseConstantWeekday.of(Fraction.ZERO);

		// When: Creating a new instance with Monday set to 30% workload
		PiecewiseConstantWeekday monday30 = base.with(DayOfWeek.MONDAY, new Fraction(3, 10));

		// Then: The original instance should remain unchanged (immutability)
		assertEquals(Fraction.ZERO, base.apply(LocalDate.of(2025, 12, 1).atStartOfDay())); // Monday in base
		
		// Then: The new instance should have the updated Monday value
		assertEquals(new Fraction(3, 10), monday30.apply(LocalDate.of(2025, 12, 1).atStartOfDay())); // Monday in new instance

		// Then: Other days in the new instance should still have zero workload
		assertEquals(Fraction.ZERO, monday30.apply(LocalDate.of(2025, 12, 2).atStartOfDay())); // Tuesday
	}

	@Test
	void apply_usesDayOfWeekMapping_acrossAWeekBoundary() {
		// Given: A weekly pattern with specific workloads:
		// - Friday: 100% (full workload)
		// - Saturday: 50% (half workload)
		// - All other days: 0% (no workload)
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.FRIDAY, Fraction.ONE)
				.with(DayOfWeek.SATURDAY, new Fraction(1, 2));

		// When/Then: Verify that each day returns the correct workload
		// Friday should be 100%
		assertEquals(Fraction.ONE, wk.apply(LocalDate.of(2025, 12, 5).atTime(10, 0)));
		
		// Saturday should be 50%
		assertEquals(new Fraction(1, 2), wk.apply(LocalDate.of(2025, 12, 6).atTime(10, 0)));
		
		// Sunday should default to 0% (not explicitly set)
		assertEquals(Fraction.ZERO, wk.apply(LocalDate.of(2025, 12, 7).atTime(10, 0)));
	}

	@Test
	void segmentsStartingAt_whenGivenExactStartOfDay_includesThatDay() {
		// Given: A weekly pattern with Monday set to 100% workload, all other days at 0%
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE);

		// Given: Starting point is exactly at the beginning of Monday (2025-12-01 00:00)
		LocalDateTime from = LocalDate.of(2025, 12, 1).atStartOfDay(); // Monday

		// When: Requesting segments starting from midnight Monday
		Iterator<Segment> it = wk.iterateSegmentsFrom(from);

		// Then: The first segment should be the full Monday day
		// The segment contains the given instant (00:00) and starts exactly at 00:00
		Segment s0 = it.next();
		assertEquals(from, s0.aInclusive(), "First segment should start at midnight Monday");
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), s0.bExclusive(), "Segment should end at midnight Tuesday");
		assertEquals(Fraction.ONE, s0.getValue(), "Monday should have 100% workload");
		assertEquals("FORWARD [2025-12-01T00:00, 2025-12-02T00:00) value=1", s0.toString());
	}

	@Test
	void segmentsStartingAt_whenGivenMidday_returnsSegmentContainingThatInstant() {
		// Given: A weekly pattern with:
		// - Monday: 100% workload
		// - Tuesday: 50% workload
		// - All other days: 0% workload
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, new Fraction(1, 2));

		// Given: Starting point is Monday at noon (12:00), in the middle of the day
		LocalDateTime from = LocalDate.of(2025, 12, 1).atTime(12, 0); // Monday noon

		// When: Requesting segments containing Monday noon
		Iterator<Segment> it = wk.iterateSegmentsFrom(from);

		// Then: The first segment returned should be Monday's full day segment [00:00, 00:00+1day)
		// IMPORTANT: Even though we request from 12:00, the segment starts at 00:00 (beginning of day)
		// This demonstrates that segmentsStartingAt returns the segment CONTAINING the instant,
		// not a segment starting exactly at that instant
		Segment monday = it.next();
		assertEquals(LocalDate.of(2025, 12, 1).atStartOfDay(), monday.aInclusive(), 
				"Monday segment should start at midnight, not at noon");
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), monday.bExclusive(), 
				"Monday segment should end at midnight Tuesday");
		assertEquals(Fraction.ONE, monday.getValue(), "Monday should have 100% workload");
		assertEquals("FORWARD [2025-12-01T00:00, 2025-12-02T00:00) value=1",
				monday.toString());

		// Then: The second segment should be Tuesday's full day
		Segment tuesday = it.next();
		assertEquals("FORWARD [2025-12-02T00:00, 2025-12-03T00:00) value=1/2",
				tuesday.toString());
	}

	@Test
	void segmentsStartingAt_producesDailySegments_withCorrectValuesOverSeveralDays() {
		// Given: A weekly pattern with specific workloads:
		// - Wednesday: 40% workload (2/5)
		// - Thursday: 60% workload (3/5)
		// - All other days: 0% workload
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.WEDNESDAY, new Fraction(2, 5)) // 40%
				.with(DayOfWeek.THURSDAY, new Fraction(3, 5)); // 60%

		// Given: Starting point is Wednesday at midnight
		LocalDateTime from = LocalDate.of(2025, 12, 3).atStartOfDay(); // Wednesday
		
		// When: Requesting segments from Wednesday onwards
		Iterator<Segment> it = wk.iterateSegmentsFrom(from);

		// Then: First segment should be Wednesday with 40% workload
		Segment wed = it.next();
		assertEquals(LocalDate.of(2025, 12, 3).atStartOfDay(), wed.aInclusive());
		assertEquals(LocalDate.of(2025, 12, 4).atStartOfDay(), wed.bExclusive());
		assertEquals(new Fraction(2, 5), wed.getValue());
		assertEquals("FORWARD [2025-12-03T00:00, 2025-12-04T00:00) value=2/5",
				wed.toString());

		// Then: Second segment should be Thursday with 60% workload
		Segment thu = it.next();
		assertEquals(LocalDate.of(2025, 12, 4).atStartOfDay(), thu.aInclusive());
		assertEquals(LocalDate.of(2025, 12, 5).atStartOfDay(), thu.bExclusive());
		assertEquals(new Fraction(3, 5), thu.getValue());
		assertEquals("FORWARD [2025-12-04T00:00, 2025-12-05T00:00) value=3/5",
				thu.toString());

		// Then: Third segment should be Friday with 0% workload (default)
		Segment fri = it.next();
		assertEquals(LocalDate.of(2025, 12, 5).atStartOfDay(), fri.aInclusive());
		assertEquals(LocalDate.of(2025, 12, 6).atStartOfDay(), fri.bExclusive());
		assertEquals(Fraction.ZERO, fri.getValue());
		assertEquals("FORWARD [2025-12-05T00:00, 2025-12-06T00:00) value=0",
				fri.toString());
	}

	@Test
	void applyIsConsistentWithSegments_forAnyInstantWithinTheDaySegment() {
		// Given: A weekly pattern with specific workloads for Monday, Tuesday, and Wednesday
		PiecewiseConstantWeekday wk = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, new Fraction(3, 10))    // 30%
				.with(DayOfWeek.TUESDAY, new Fraction(1, 2))     // 50%
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE);        // 100%

		// When/Then: For any instant within a day, apply() should return the same value
		// as the segment containing that instant
		
		// Test Monday at 10:15 AM
		assertApplyMatchesFirstDailySegment(wk, LocalDate.of(2025, 12, 1).atTime(10, 15));
		
		// Test Tuesday at 11:59 PM (near end of day)
		assertApplyMatchesFirstDailySegment(wk, LocalDate.of(2025, 12, 2).atTime(23, 59));
		
		// Test Wednesday at 00:01 AM (near start of day)
		assertApplyMatchesFirstDailySegment(wk, LocalDate.of(2025, 12, 3).atTime(0, 1));
	}

	/**
	 * Helper method to verify that apply(instant) returns the same value as the segment
	 * containing that instant.
	 * 
	 * This is a critical consistency check: the two ways of querying workload
	 * (point-wise via apply() vs. segment-wise via segmentsStartingAt()) must agree.
	 */
	private static void assertApplyMatchesFirstDailySegment(PiecewiseConstantWeekday wk, LocalDateTime instant) {
		// Get the start of the day containing the instant
		LocalDateTime dayStart = instant.toLocalDate().atStartOfDay();

		// Retrieve the segment containing this instant
		Iterator<Segment> it = wk.iterateSegmentsFrom(dayStart);
		assertTrue(it.hasNext(), "Iterator should provide at least one segment");

		Segment seg = it.next();

		// Verify the segment structure matches expectations
		assertEquals(dayStart, seg.aInclusive(), "Segment should start at day boundary");
		assertEquals(dayStart.plusDays(1), seg.bExclusive(), "Segment should span exactly one day");

		// Verify that the instant is actually within the segment bounds
		assertFalse(instant.isBefore(seg.aInclusive()), "Instant should not be before the segment start");
		assertTrue(instant.isBefore(seg.bExclusive()), "Instant should be before the segment end");

		// Core consistency assertion: apply() and segment value must match
		assertEquals(wk.apply(instant), seg.getValue(),
				"apply(instant) should match the value of the segment containing that instant");
	}

}
