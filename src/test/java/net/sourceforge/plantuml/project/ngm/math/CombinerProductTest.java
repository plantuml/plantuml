package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant.Segment;

/**
 * Tests for the Combiner.product() method, focusing on combining
 * PiecewiseConstantSpecificDays with PiecewiseConstantWeekday.
 * 
 * These tests verify that the product operation correctly combines
 * availability calendars and workload allocations.
 */
class CombinerProductTest {

	@Test
	void testProduct_weekdayScheduleWithHolidays() {
		// Scenario: Standard Monday-Friday schedule with Christmas holidays
		// This simulates a realistic work calendar where:
		// - Base schedule: Mon-Fri work, weekends off
		// - Specific days: Christmas break (Dec 25-26) are holidays
		
		// Weekday pattern: work Mon-Fri (100%), off Sat-Sun (0%)
		PiecewiseConstantWeekday weekdaySchedule = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.THURSDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);
		
		// Specific days: Dec 25-26, 2024 are holidays (0%), all other days normal (100%)
		PiecewiseConstantSpecificDays holidayCalendar = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 12, 25), Fraction.ZERO) // Christmas
				.withDay(LocalDate.of(2024, 12, 26), Fraction.ZERO); // Boxing Day
		
		// Product should give us: work Mon-Fri, except Dec 25-26
		PiecewiseConstant combined = Combiner.product(weekdaySchedule, holidayCalendar);
		
		// Start iterating from Monday Dec 23, 2024
		LocalDateTime startDate = LocalDateTime.of(2024, 12, 23, 9, 0);
		Iterator<Segment> segments = combined.segmentsStartingAt(startDate);
		
		// Collect first 10 segments to verify the pattern
		List<Segment> segmentList = collectSegments(segments, 10);
		
		// Expected pattern:
		// Dec 23 (Mon): 1 (work day)
		// Dec 24 (Tue): 1 (work day)
		// Dec 25 (Wed): 0 (holiday, even though it's Wednesday)
		// Dec 26 (Thu): 0 (holiday, even though it's Thursday)
		// Dec 27 (Fri): 1 (work day)
		// Dec 28 (Sat): 0 (weekend)
		// Dec 29 (Sun): 0 (weekend)
		// Dec 30 (Mon): 1 (work day)
		// Dec 31 (Tue): 1 (work day)
		// Jan 1 (Wed): 1 (work day, no holiday defined)
		
		assertSegment(segmentList.get(0), 
				LocalDateTime.of(2024, 12, 23, 0, 0),
				LocalDateTime.of(2024, 12, 24, 0, 0),
				Fraction.ONE, "Dec 23 - Monday work day");
		
		assertSegment(segmentList.get(1),
				LocalDateTime.of(2024, 12, 24, 0, 0),
				LocalDateTime.of(2024, 12, 25, 0, 0),
				Fraction.ONE, "Dec 24 - Tuesday work day");
		
		assertSegment(segmentList.get(2),
				LocalDateTime.of(2024, 12, 25, 0, 0),
				LocalDateTime.of(2024, 12, 26, 0, 0),
				Fraction.ZERO, "Dec 25 - Christmas, should be 0 even though Wednesday");
		
		assertSegment(segmentList.get(3),
				LocalDateTime.of(2024, 12, 26, 0, 0),
				LocalDateTime.of(2024, 12, 27, 0, 0),
				Fraction.ZERO, "Dec 26 - Boxing Day, should be 0 even though Thursday");
		
		assertSegment(segmentList.get(4),
				LocalDateTime.of(2024, 12, 27, 0, 0),
				LocalDateTime.of(2024, 12, 28, 0, 0),
				Fraction.ONE, "Dec 27 - Friday work day");
		
		assertSegment(segmentList.get(5),
				LocalDateTime.of(2024, 12, 28, 0, 0),
				LocalDateTime.of(2024, 12, 29, 0, 0),
				Fraction.ZERO, "Dec 28 - Saturday weekend");
		
		assertSegment(segmentList.get(6),
				LocalDateTime.of(2024, 12, 29, 0, 0),
				LocalDateTime.of(2024, 12, 30, 0, 0),
				Fraction.ZERO, "Dec 29 - Sunday weekend");
		
		assertSegment(segmentList.get(7),
				LocalDateTime.of(2024, 12, 30, 0, 0),
				LocalDateTime.of(2024, 12, 31, 0, 0),
				Fraction.ONE, "Dec 30 - Monday work day");
		
		assertSegment(segmentList.get(8),
				LocalDateTime.of(2024, 12, 31, 0, 0),
				LocalDateTime.of(2025, 1, 1, 0, 0),
				Fraction.ONE, "Dec 31 - Tuesday work day");
		
		assertSegment(segmentList.get(9),
				LocalDateTime.of(2025, 1, 1, 0, 0),
				LocalDateTime.of(2025, 1, 2, 0, 0),
				Fraction.ONE, "Jan 1 - Wednesday work day (no holiday defined)");
	}

	@Test
	void testProduct_partTimeWithVacation() {
		// Scenario: Part-time worker (60% allocation) with 2 vacation days
		// This tests multiplication of fractional values
		
		// Weekday pattern: 60% every day
		Fraction partTimeLoad = new Fraction(3, 5); // 60%
		PiecewiseConstantWeekday partTimeSchedule = PiecewiseConstantWeekday.of(partTimeLoad);
		
		// Specific days: July 15-16 are vacation days (0%), all others normal (100%)
		PiecewiseConstantSpecificDays vacationCalendar = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 7, 15), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 7, 16), Fraction.ZERO);
		
		// Product: 60% * 100% = 60% on normal days, 60% * 0% = 0% on vacation
		PiecewiseConstant combined = Combiner.product(partTimeSchedule, vacationCalendar);
		
		LocalDateTime startDate = LocalDateTime.of(2024, 7, 14, 8, 0);
		Iterator<Segment> segments = combined.segmentsStartingAt(startDate);
		
		List<Segment> segmentList = collectSegments(segments, 5);
		
		// July 14 (Sun): 60%
		assertSegment(segmentList.get(0),
				LocalDateTime.of(2024, 7, 14, 0, 0),
				LocalDateTime.of(2024, 7, 15, 0, 0),
				partTimeLoad, "July 14 - 60% work");
		
		// July 15 (Mon): 0% (vacation)
		assertSegment(segmentList.get(1),
				LocalDateTime.of(2024, 7, 15, 0, 0),
				LocalDateTime.of(2024, 7, 16, 0, 0),
				Fraction.ZERO, "July 15 - vacation day, 60% * 0% = 0%");
		
		// July 16 (Tue): 0% (vacation)
		assertSegment(segmentList.get(2),
				LocalDateTime.of(2024, 7, 16, 0, 0),
				LocalDateTime.of(2024, 7, 17, 0, 0),
				Fraction.ZERO, "July 16 - vacation day, 60% * 0% = 0%");
		
		// July 17 (Wed): 60%
		assertSegment(segmentList.get(3),
				LocalDateTime.of(2024, 7, 17, 0, 0),
				LocalDateTime.of(2024, 7, 18, 0, 0),
				partTimeLoad, "July 17 - back to 60% work");
		
		// July 18 (Thu): 60%
		assertSegment(segmentList.get(4),
				LocalDateTime.of(2024, 7, 18, 0, 0),
				LocalDateTime.of(2024, 7, 19, 0, 0),
				partTimeLoad, "July 18 - 60% work");
	}

	@Test
	void testProduct_weekendOnlyWithBlackoutDates() {
		// Scenario: Weekend-only work schedule with specific blackout dates
		// Tests combining a sparse weekday pattern with specific exclusions
		
		// Weekday pattern: only Saturdays and Sundays
		PiecewiseConstantWeekday weekendOnly = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.SATURDAY, Fraction.ONE)
				.with(DayOfWeek.SUNDAY, Fraction.ONE);
		
		// Specific days: Memorial Day weekend - make Sunday May 26 a blackout (0%)
		PiecewiseConstantSpecificDays blackoutDates = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 5, 26), Fraction.ZERO); // Sunday blackout
		
		// Product: should work Saturdays and Sundays, except May 26
		PiecewiseConstant combined = Combiner.product(weekendOnly, blackoutDates);
		
		LocalDateTime startDate = LocalDateTime.of(2024, 5, 24, 10, 0); // Friday
		Iterator<Segment> segments = combined.segmentsStartingAt(startDate);
		
		List<Segment> segmentList = collectSegments(segments, 7);
		
		// May 24 (Fri): 0 (weekday)
		assertSegment(segmentList.get(0),
				LocalDateTime.of(2024, 5, 24, 0, 0),
				LocalDateTime.of(2024, 5, 25, 0, 0),
				Fraction.ZERO, "May 24 - Friday, no work");
		
		// May 25 (Sat): 1 (weekend work day)
		assertSegment(segmentList.get(1),
				LocalDateTime.of(2024, 5, 25, 0, 0),
				LocalDateTime.of(2024, 5, 26, 0, 0),
				Fraction.ONE, "May 25 - Saturday work day");
		
		// May 26 (Sun): 0 (blackout even though it's Sunday)
		assertSegment(segmentList.get(2),
				LocalDateTime.of(2024, 5, 26, 0, 0),
				LocalDateTime.of(2024, 5, 27, 0, 0),
				Fraction.ZERO, "May 26 - Sunday blackout, 1 * 0 = 0");
		
		// May 27 (Mon): 0 (weekday)
		assertSegment(segmentList.get(3),
				LocalDateTime.of(2024, 5, 27, 0, 0),
				LocalDateTime.of(2024, 5, 28, 0, 0),
				Fraction.ZERO, "May 27 - Monday, no work");
		
		// June 1 (Sat): 1 (weekend work day)
		assertSegment(segmentList.get(5),
				LocalDateTime.of(2024, 6, 1, 0, 0),
				LocalDateTime.of(2024, 6, 2, 0, 0),
				Fraction.ONE, "June 1 - Saturday work day");
		
		// June 2 (Sun): 1 (weekend work day, no blackout)
		assertSegment(segmentList.get(6),
				LocalDateTime.of(2024, 6, 2, 0, 0),
				LocalDateTime.of(2024, 6, 3, 0, 0),
				Fraction.ONE, "June 2 - Sunday work day");
	}

	@Test
	void testProduct_complexFractionalMultiplication() {
		// Scenario: Complex fractional multiplication
		// Developer at 75% capacity, working only Tue/Thu at 120% intensity
		// Combined with specific reduced days
		
		// Weekday pattern: only Tue/Thu at 120%
		Fraction highIntensity = new Fraction(6, 5); // 120%
		PiecewiseConstantWeekday tuesdayThursday = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.TUESDAY, highIntensity)
				.with(DayOfWeek.THURSDAY, highIntensity);
		
		// Specific days: Sept 10 (Tue) is reduced to 50%, Sept 12 (Thu) is off
		PiecewiseConstantSpecificDays adjustedDays = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 9, 10), new Fraction(1, 2)) // 50%
				.withDay(LocalDate.of(2024, 9, 12), Fraction.ZERO); // 0%
		
		// Product: 
		// - Normal Tue/Thu: 120% * 100% = 120%
		// - Sept 10 (Tue): 120% * 50% = 60%
		// - Sept 12 (Thu): 120% * 0% = 0%
		PiecewiseConstant combined = Combiner.product(tuesdayThursday, adjustedDays);
		
		LocalDateTime startDate = LocalDateTime.of(2024, 9, 9, 9, 0); // Monday
		Iterator<Segment> segments = combined.segmentsStartingAt(startDate);
		
		List<Segment> segmentList = collectSegments(segments, 6);
		
		// Sept 9 (Mon): 0 (not a work day)
		assertSegment(segmentList.get(0),
				LocalDateTime.of(2024, 9, 9, 0, 0),
				LocalDateTime.of(2024, 9, 10, 0, 0),
				Fraction.ZERO, "Sept 9 - Monday, no work");
		
		// Sept 10 (Tue): 120% * 50% = 60%
		Fraction expectedTue = new Fraction(3, 5); // 60% = 6/5 * 1/2 = 6/10 = 3/5
		assertSegment(segmentList.get(1),
				LocalDateTime.of(2024, 9, 10, 0, 0),
				LocalDateTime.of(2024, 9, 11, 0, 0),
				expectedTue, "Sept 10 - Tuesday at reduced 60% (120% * 50%)");
		
		// Sept 11 (Wed): 0 (not a work day)
		assertSegment(segmentList.get(2),
				LocalDateTime.of(2024, 9, 11, 0, 0),
				LocalDateTime.of(2024, 9, 12, 0, 0),
				Fraction.ZERO, "Sept 11 - Wednesday, no work");
		
		// Sept 12 (Thu): 120% * 0% = 0%
		assertSegment(segmentList.get(3),
				LocalDateTime.of(2024, 9, 12, 0, 0),
				LocalDateTime.of(2024, 9, 13, 0, 0),
				Fraction.ZERO, "Sept 12 - Thursday off (120% * 0%)");
		
		// Sept 13 (Fri): 0 (not a work day)
		assertSegment(segmentList.get(4),
				LocalDateTime.of(2024, 9, 13, 0, 0),
				LocalDateTime.of(2024, 9, 14, 0, 0),
				Fraction.ZERO, "Sept 13 - Friday, no work");
		
		// Sept 17 (Tue next week): 120% (back to normal)
		assertSegment(segmentList.get(5),
				LocalDateTime.of(2024, 9, 17, 0, 0),
				LocalDateTime.of(2024, 9, 18, 0, 0),
				highIntensity, "Sept 17 - Tuesday at normal 120%");
	}

	@Test
	void testProduct_leapYearBoundary() {
		// Scenario: Product across leap year boundary
		// Tests date handling around Feb 29, 2024
		
		// Weekday pattern: every day at 100%
		PiecewiseConstantWeekday everyday = PiecewiseConstantWeekday.of(Fraction.ONE);
		
		// Specific days: Feb 29 (leap day) is special event day at 50%
		PiecewiseConstantSpecificDays leapDaySpecial = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 2, 29), new Fraction(1, 2));
		
		// Product should show Feb 29 at 50%, others at 100%
		PiecewiseConstant combined = Combiner.product(everyday, leapDaySpecial);
		
		LocalDateTime startDate = LocalDateTime.of(2024, 2, 28, 8, 0);
		Iterator<Segment> segments = combined.segmentsStartingAt(startDate);
		
		List<Segment> segmentList = collectSegments(segments, 3);
		
		// Feb 28: 100%
		assertSegment(segmentList.get(0),
				LocalDateTime.of(2024, 2, 28, 0, 0),
				LocalDateTime.of(2024, 2, 29, 0, 0),
				Fraction.ONE, "Feb 28 - normal day");
		
		// Feb 29: 50% (leap day special)
		assertSegment(segmentList.get(1),
				LocalDateTime.of(2024, 2, 29, 0, 0),
				LocalDateTime.of(2024, 3, 1, 0, 0),
				new Fraction(1, 2), "Feb 29 - leap day at 50%");
		
		// Mar 1: 100%
		assertSegment(segmentList.get(2),
				LocalDateTime.of(2024, 3, 1, 0, 0),
				LocalDateTime.of(2024, 3, 2, 0, 0),
				Fraction.ONE, "Mar 1 - back to normal");
	}

	// ===========================================================================
	// Helper methods
	// ===========================================================================

	/**
	 * Collects a specified number of segments from the iterator.
	 */
	private List<Segment> collectSegments(Iterator<Segment> iterator, int count) {
		List<Segment> result = new ArrayList<>();
		for (int i = 0; i < count && iterator.hasNext(); i++) {
			result.add(iterator.next());
		}
		return result;
	}

	/**
	 * Asserts that a segment has the expected properties.
	 */
	private void assertSegment(Segment actual, LocalDateTime expectedStart, 
			LocalDateTime expectedEnd, Fraction expectedValue, String message) {
		assertNotNull(actual, message + " - segment should not be null");
		assertEquals(expectedStart, actual.getStartInclusive(), 
				message + " - start time mismatch");
		assertEquals(expectedEnd, actual.getEndExclusive(), 
				message + " - end time mismatch");
		assertEquals(expectedValue, actual.getValue(), 
				message + " - value mismatch");
	}
}
