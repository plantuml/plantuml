package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the LoadIntegrator class, verifying correct integration of
 * piecewise constant load functions over time periods.
 * 
 * These tests cover various realistic scenarios with both
 * PiecewiseConstantSpecificDays and PiecewiseConstantWeekday implementations.
 */
class LoadIntegratorTest {
	
	// Enable debug mode for all tests
	@BeforeAll
	static void setup() {
		LoadIntegrator.DEBUG = true;
	}
	
	// Disable debug mode after tests
	@AfterAll
	static void teardown() {
		LoadIntegrator.DEBUG = false;
	}

	// ===========================================================================
	// Tests with PiecewiseConstantSpecificDays
	// ===========================================================================

	@Test
	void testChristmasHoliday_withVacationDays() {
		// Scenario: Developer takes vacation from Dec 23-26, 2024
		// Working 100% on other days, 0% during vacation
		PiecewiseConstant loadFunction = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 12, 23), Fraction.ZERO) // Monday
				.withDay(LocalDate.of(2024, 12, 24), Fraction.ZERO) // Tuesday
				.withDay(LocalDate.of(2024, 12, 25), Fraction.ZERO) // Wednesday
				.withDay(LocalDate.of(2024, 12, 26), Fraction.ZERO); // Friday

		LocalDateTime start = LocalDateTime.of(2024, 12, 20, 9, 0); // Friday Dec 20
		Fraction totalLoad = Fraction.of(5); // 5 days of work

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Should skip the vacation days: Dec 23, 24, 25, 26
		LocalDateTime expected = LocalDateTime.of(2024, 12, 29, 9, 0);
		assertEquals(expected, end, "Should complete after vacation period");
	}

	@Test
	void testChristmasHoliday_withVacationDaysAndWeekends() {
		// Scenario: Developer takes vacation from Dec 23-26, 2024
		// Working 100% on other days, 0% during vacation
		PiecewiseConstant vacation = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 12, 23), Fraction.ZERO) // Monday
				.withDay(LocalDate.of(2024, 12, 24), Fraction.ZERO) // Tuesday
				.withDay(LocalDate.of(2024, 12, 25), Fraction.ZERO) // Wednesday
				.withDay(LocalDate.of(2024, 12, 26), Fraction.ZERO); // Thursday
		
		PiecewiseConstant fiveDaysWeek = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.THURSDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);
		
		PiecewiseConstant loadFunction = Combiner.product(vacation, fiveDaysWeek);


		LocalDateTime start = LocalDateTime.of(2024, 12, 20, 9, 0); // Friday Dec 20
		Fraction totalLoad = Fraction.of(5); // 5 days of work

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Should skip the vacation days: Dec 23, 24, 25, 26 and Weekend: Dec 21, 22, 28, 29
		LocalDateTime expected = LocalDateTime.of(2025, 1, 2, 9, 0);
		assertEquals(expected, end, "Should complete after vacation period");
	}

	@Test
	void testNewYearTransition_spanningYears() {
		// Scenario: Project spans New Year with holiday on Jan 1
		PiecewiseConstantSpecificDays loadFunction = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2025, 1, 1), Fraction.ZERO); // New Year's Day off

		LocalDateTime start = LocalDateTime.of(2024, 12, 30, 14, 30);
		Fraction totalLoad = Fraction.of(3); // 3 days work

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Dec 30, 31, Jan 2 (skipping Jan 1)
		LocalDateTime expected = LocalDateTime.of(2025, 1, 2, 14, 30);
		assertEquals(expected, end, "Should skip New Year's Day");
	}

	@Test
	void testSummerInternship_reducedLoad() {
		// Scenario: Intern works 50% during summer program
		// But takes July 4th off (US Independence Day)
		Fraction halfTime = new Fraction(1, 2);
		PiecewiseConstantSpecificDays loadFunction = PiecewiseConstantSpecificDays.of(halfTime)
				.withDay(LocalDate.of(2024, 7, 4), Fraction.ZERO);

		LocalDateTime start = LocalDateTime.of(2024, 7, 1, 8, 0);
		Fraction totalLoad = Fraction.of(3); // 3 full days equivalent

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// At 50% load: needs 6 calendar days, skipping July 4
		// July 1, 2, 3, 5, 6, 7 (6 days at 50% = 3 full days)
		LocalDateTime expected = LocalDateTime.of(2024, 7, 7, 8, 0);
		assertEquals(expected, end, "Should account for reduced load and holiday");
	}

	@Test
	void testConferenceWeek_partialDays() {
		// Scenario: Developer at conference, working 30% on specific days
		Fraction conferenceLoad = new Fraction(3, 10); // 30%
		PiecewiseConstantSpecificDays loadFunction = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 9, 16), conferenceLoad)
				.withDay(LocalDate.of(2024, 9, 17), conferenceLoad)
				.withDay(LocalDate.of(2024, 9, 18), conferenceLoad)
				.withDay(LocalDate.of(2024, 9, 19), conferenceLoad)
				.withDay(LocalDate.of(2024, 9, 20), conferenceLoad);

		LocalDateTime start = LocalDateTime.of(2024, 9, 13, 10, 0); // Friday before conference
		Fraction totalLoad = Fraction.of(4); // 4 full days of work

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Sept 13 (1 day) + Sept 16-20 at 30% (1.5 days) = 2.5 days
		// Need 1.5 more days: Sept 23 (1 day) + Sept 24 (0.5 day)
		LocalDateTime expected = LocalDateTime.of(2024, 9, 24, 10, 0);
		assertEquals(expected, end, "Should handle mixed load during conference week");
	}

	// ===========================================================================
	// Tests with PiecewiseConstantWeekday
	// ===========================================================================

	@Test
	void testStandardWorkWeek_mondayToFriday() {
		// Scenario: Classic 5-day work week, no weekends
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.THURSDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);

		LocalDateTime start = LocalDateTime.of(2024, 11, 18, 9, 0); // Monday
		Fraction totalLoad = Fraction.of(5); // 1 work week

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Should complete on Friday Nov 22
		LocalDateTime expected = LocalDateTime.of(2024, 11, 22, 9, 0);
		assertEquals(expected, end, "Should complete standard 5-day work week");
	}

	@Test
	void testPartTimeSchedule_threeDaysPerWeek() {
		// Scenario: Part-time worker on Mon/Wed/Fri only
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);

		LocalDateTime start = LocalDateTime.of(2024, 10, 7, 13, 30); // Monday
		Fraction totalLoad = Fraction.of(6); // 6 working days = 2 weeks

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Week 1: Mon Oct 7, Wed 9, Fri 11
		// Week 2: Mon Oct 14, Wed 16, Fri 18
		LocalDateTime expected = LocalDateTime.of(2024, 10, 18, 13, 30);
		assertEquals(expected, end, "Should span 2 weeks for part-time schedule");
	}

	@Test
	void testCompressedWorkWeek_fourTenHourDays() {
		// Scenario: 4x10 schedule (Mon-Thu at 125%, Fri-Sun off)
		Fraction enhancedLoad = new Fraction(5, 4); // 125%
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, enhancedLoad)
				.with(DayOfWeek.TUESDAY, enhancedLoad)
				.with(DayOfWeek.WEDNESDAY, enhancedLoad)
				.with(DayOfWeek.THURSDAY, enhancedLoad);

		LocalDateTime start = LocalDateTime.of(2024, 8, 5, 7, 0); // Monday
		Fraction totalLoad = Fraction.of(10); // 10 standard days of work

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Week 1: Mon-Thu = 5 days equivalent
		// Week 2: Mon-Thu = 5 days equivalent, completes on Thursday
		LocalDateTime expected = LocalDateTime.of(2024, 8, 15, 7, 0); // Second Thursday
		assertEquals(expected, end, "Should complete in 2 four-day weeks at 125% load");
	}

	@Test
	void testRetailSchedule_weekendHeavy() {
		// Scenario: Retail worker, busier on weekends
		Fraction weekdayLoad = new Fraction(1, 2); // 50%
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(weekdayLoad)
				.with(DayOfWeek.SATURDAY, Fraction.ONE)
				.with(DayOfWeek.SUNDAY, Fraction.ONE);

		LocalDateTime start = LocalDateTime.of(2024, 5, 13, 10, 0); // Monday
		Fraction totalLoad = Fraction.of(7); // 7 full days equivalent

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Week 1: Mon-Fri at 50% (2.5) + Sat-Sun at 100% (2) = 4.5 days
		// Need 2.5 more: Mon-Fri next week = 2.5 days, ends Thursday
		LocalDateTime expected = LocalDateTime.of(2024, 5, 23, 10, 0); // Second Thursday
		assertEquals(expected, end, "Should account for weekend-heavy schedule");
	}

	@Test
	void testShiftWorker_alternatingIntensity() {
		// Scenario: Shift pattern with varying intensity
		// Light early week, heavy mid-week, off weekends
		Fraction light = new Fraction(1, 3); // 33%
		Fraction normal = Fraction.ONE;
		Fraction heavy = new Fraction(3, 2); // 150%
		
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, light)
				.with(DayOfWeek.TUESDAY, normal)
				.with(DayOfWeek.WEDNESDAY, heavy)
				.with(DayOfWeek.THURSDAY, heavy)
				.with(DayOfWeek.FRIDAY, normal);

		LocalDateTime start = LocalDateTime.of(2024, 6, 3, 6, 0); // Monday
		Fraction totalLoad = Fraction.of(6); // 6 full days of work

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Week 1: Mon(0.33) + Tue(1) + Wed(1.5) + Thu(1.5) + Fri(1) = 5.33 days
		// Need 0.67 more: next Monday(0.33) + Tuesday(0.34)
		LocalDateTime expected = LocalDateTime.of(2024, 6, 11, 6, 0); // Second Tuesday
		assertEquals(expected, end, "Should handle alternating intensity pattern");
	}

	// ===========================================================================
	// Edge cases and special scenarios
	// ===========================================================================

	@Test
	void testMidDayStart_preservesTime() {
		// Scenario: Start in middle of day, verify time component is preserved
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ONE);

		LocalDateTime start = LocalDateTime.of(2024, 3, 15, 14, 45); // 2:45 PM
		Fraction totalLoad = Fraction.of(3);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		LocalDateTime expected = LocalDateTime.of(2024, 3, 18, 14, 45);
		assertEquals(expected, end, "Should preserve time component from start");
	}


	@Test
	void testSmallFractionalLoad_highPrecision() {
		// Scenario: Very small daily load requires high precision
		Fraction tinyLoad = new Fraction(1, 100); // 1% per day

		PiecewiseConstantSpecificDays loadFunction = PiecewiseConstantSpecificDays.of(tinyLoad);

		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 8, 0);
		Fraction totalLoad = Fraction.ONE; // 1 full day at 1% = 100 calendar days

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		LocalDateTime expected = LocalDateTime.of(2024, 5, 10, 8, 0); // ~100 days later
		assertEquals(expected, end, "Should handle very small fractional loads");
	}

	@Test
	void testLeapYear_february29() {
		// Scenario: Work through leap year February
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ONE);

		LocalDateTime start = LocalDateTime.of(2024, 2, 27, 9, 0); // 2024 is leap year
		Fraction totalLoad = Fraction.of(5);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Feb 27, 28, 29, Mar 1, 2
		LocalDateTime expected = LocalDateTime.of(2024, 3, 2, 9, 0);
		assertEquals(expected, end, "Should correctly handle leap year day");
	}

	@Test
	void testMultipleVacationPeriods_complexSchedule() {
		// Scenario: Developer with multiple vacation blocks
		PiecewiseConstantSpecificDays loadFunction = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				// Spring break
				.withDay(LocalDate.of(2024, 4, 8), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 4, 9), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 4, 10), Fraction.ZERO)
				// Doctor appointment - half day
				.withDay(LocalDate.of(2024, 4, 15), new Fraction(1, 2))
				// Long weekend
				.withDay(LocalDate.of(2024, 4, 19), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 4, 22), Fraction.ZERO);

		LocalDateTime start = LocalDateTime.of(2024, 4, 1, 9, 0);
		Fraction totalLoad = Fraction.of(15); // 15 days of work

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();

		// Should navigate through all vacation periods
		LocalDateTime expected = LocalDateTime.of(2024, 4, 24, 9, 0);
		assertEquals(expected, end, "Should handle multiple vacation periods correctly");
	}
}
