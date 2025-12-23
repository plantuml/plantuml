package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.project.ngm.NGMTotalEffort;

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
		// LoadIntegrator.DEBUG = true;
	}

	// Disable debug mode after tests
	@AfterAll
	static void teardown() {
		// LoadIntegrator.DEBUG = false;
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
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(5);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

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

		PiecewiseConstant fiveDaysWeek = PiecewiseConstantWeekday.of(Fraction.ZERO).with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, Fraction.ONE).with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.THURSDAY, Fraction.ONE).with(DayOfWeek.FRIDAY, Fraction.ONE);

		PiecewiseConstant loadFunction = Combiner.product(vacation, fiveDaysWeek);

		LocalDateTime start = LocalDateTime.of(2024, 12, 20, 9, 0); // Friday Dec 20
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(5);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Should skip the vacation days: Dec 23, 24, 25, 26 and Weekend: Dec 21, 22,
		// 28, 29
		LocalDateTime expected = LocalDateTime.of(2025, 1, 2, 9, 0);
		assertEquals(expected, end, "Should complete after vacation period");
	}

	@Test
	void testNewYearTransition_spanningYears() {
		// Scenario: Project spans New Year with holiday on Jan 1
		PiecewiseConstantSpecificDays loadFunction = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2025, 1, 1), Fraction.ZERO); // New Year's Day off

		LocalDateTime start = LocalDateTime.of(2024, 12, 30, 14, 30);
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(3);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Dec 30 (19/48) - 14:30-24:00
		// Dec 31 (1)
		// Jan 1 (0) - skipping holiday
		// Jan 2 (1)
		// Jan 3 (29/48) - 00:00-14:30
		LocalDateTime expected = LocalDateTime.of(2025, 1, 3, 14, 30);
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
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(3);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// At 50% load: needs 6 calendar days, skipping July 4
		// July 1 (1/3) - 8:00-24:00
		// July 2, 3, 5, 6, 7 (1/2 each)
		// July 4 (0) - skipping holiday
		// July 8 (1/6) - 00:00-8:00
		LocalDateTime expected = LocalDateTime.of(2024, 7, 8, 8, 0);
		assertEquals(expected, end, "Should account for reduced load and holiday");
	}

	@Test
	void testConferenceWeek_partialDays() {
		// Scenario: Developer at conference, working 30% on specific days
		Fraction conferenceLoad = new Fraction(3, 10); // 30%
		PiecewiseConstantSpecificDays loadFunction = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 9, 16), conferenceLoad).withDay(LocalDate.of(2024, 9, 17), conferenceLoad)
				.withDay(LocalDate.of(2024, 9, 18), conferenceLoad).withDay(LocalDate.of(2024, 9, 19), conferenceLoad)
				.withDay(LocalDate.of(2024, 9, 20), conferenceLoad);

		LocalDateTime start = LocalDateTime.of(2024, 9, 13, 10, 0); // Friday before conference
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(4);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Sept 13 (7/13) - 10:00-24:00 = 0.5384615385 days
		// Sept 14, 15 (1) = 2 days
		// Sept 16-19 (3/10 each) = 1.2 days
		// Sept 20 (13/60) - 00:00-17:20 = 0.2166666667 days
		LocalDateTime expected = LocalDateTime.of(2024, 9, 20, 17, 20);
		assertEquals(expected, end, "Should handle mixed load during conference week");
	}

	// ===========================================================================
	// Tests with PiecewiseConstantWeekday
	// ===========================================================================

	@Test
	void testStandardWorkWeek_mondayToFriday() {
		// Scenario: Classic 5-day work week, no weekends
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE).with(DayOfWeek.TUESDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE).with(DayOfWeek.THURSDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);

		LocalDateTime start = LocalDateTime.of(2024, 11, 18, 9, 0); // Monday
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(5); // 1 work week

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Even though it may seem counterintuitive that the end is Monday, November 25
		// for 5 days of work, it's actually
		// logical. The end should be understood as the first instant when the work is
		// finished.
		// Intuitively, one might think that the end is Friday. But since there are 5
		// days, meaning 120 hours of work,
		// given that we started the work on Monday the 18th at 9am, we indeed need to
		// work
		// on Monday the 25th from midnight to 9am to finish the work.
		// Therefore the test is correct.
		LocalDateTime expected = LocalDateTime.of(2024, 11, 25, 9, 0);
		assertEquals(expected, end, "Should complete standard 5-day work week");
	}

	@Test
	void testPartTimeSchedule_threeDaysPerWeek() {
		// Scenario: Part-time worker on Mon/Wed/Fri only
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE).with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);

		LocalDateTime start = LocalDateTime.of(2024, 10, 7, 13, 30); // Monday
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(6); // 6 working days = 2 weeks

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Week 1: Mon Oct 7 (7/16), Wed 9 (1), Fri 11 (1)
		// Week 2: Mon Oct 14 (1), Wed 16 (1), Fri 18 (1)
		// Week 3: Mon Oct 21 (9/16)
		LocalDateTime expected = LocalDateTime.of(2024, 10, 21, 13, 30);
		assertEquals(expected, end, "Should span 2 weeks for part-time schedule");
	}

	@Test
	void testCompressedWorkWeek_fourTenHourDays() {
		// Scenario: 4x10 schedule (Mon-Thu at 125%, Fri-Sun off)
		Fraction enhancedLoad = new Fraction(5, 4); // 125%
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, enhancedLoad).with(DayOfWeek.TUESDAY, enhancedLoad)
				.with(DayOfWeek.WEDNESDAY, enhancedLoad).with(DayOfWeek.THURSDAY, enhancedLoad);

		LocalDateTime start = LocalDateTime.of(2024, 8, 5, 7, 0); // Monday
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(10);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Week 1:
		// Mon = (24-7)/24*1.25 = 85/96 = 0.8854 days equivalent
		// Tue-Thu = 3*1.25 = 3.75 days equivalent
		// Week 2: Mon-Thu = 5 days equivalent
		// Week 3: Mon = 7/24*1.25 = 35/96 = 0.3646 days equivalent
		LocalDateTime expected = LocalDateTime.of(2024, 8, 19, 7, 0); // Second Thursday
		assertEquals(expected, end, "Should complete in 2 four-day weeks at 125% load");
	}

	@Test
	void testRetailSchedule_weekendHeavy() {
		// Scenario: Retail worker, busier on weekends
		Fraction weekdayLoad = new Fraction(1, 2); // 50%
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(weekdayLoad)
				.with(DayOfWeek.SATURDAY, Fraction.ONE).with(DayOfWeek.SUNDAY, Fraction.ONE);

		LocalDateTime start = LocalDateTime.of(2024, 5, 13, 10, 0); // Monday
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(7);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Week 1:
		// Mon (14/24)*1/2 = 7/24 = 0.2917 days
		// Tue-Fri at 50% (1/2)*4 = 2 days
		// Sat-Sun at 100% (1)*2 = 2 days
		// Week 2:
		// Mon-Fri at 50% (1/2)*5 = 2.5 days
		// Sat at 100% (1) = 5/24 = 0.2083 day
		LocalDateTime expected = LocalDateTime.of(2024, 5, 25, 5, 0); // Second Saturday
		assertEquals(expected, end, "Should account for weekend-heavy schedule");
	}

	@Test
	void testShiftWorker_alternatingIntensity() {
		// Scenario: Shift pattern with varying intensity
		// Light early week, heavy mid-week, off weekends
		Fraction light = new Fraction(1, 3); // 33%
		Fraction normal = Fraction.ONE;
		Fraction heavy = new Fraction(3, 2); // 150%

		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ZERO).with(DayOfWeek.MONDAY, light)
				.with(DayOfWeek.TUESDAY, normal).with(DayOfWeek.WEDNESDAY, heavy).with(DayOfWeek.THURSDAY, heavy)
				.with(DayOfWeek.FRIDAY, normal);

		LocalDateTime start = LocalDateTime.of(2024, 6, 3, 6, 0); // Monday
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(6);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Week 1: 5.25 days equivalent
		// Mon 6:00-24:00 (18/24)*(1/3)=1/4 = 0.25 days
		// Tue (1) = 1 day
		// Wed (1.5) = 1.5 days
		// Thu (1.5) = 1.5 days
		// Fri(1) = 1 day
		// Week 2: 0.75 days equivalent
		// Mon (1/3) = 0.3333 days
		// Tue 0:00-10:00 (10/24)*(1) = 0.4167 days
		LocalDateTime expected = LocalDateTime.of(2024, 6, 11, 10, 0); // Second Tuesday
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
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(3);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		LocalDateTime expected = LocalDateTime.of(2024, 3, 18, 14, 45);
		assertEquals(expected, end, "Should preserve time component from start");
	}

	@Test
	void testSmallFractionalLoad_highPrecision() {
		// Scenario: Very small daily load requires high precision
		Fraction tinyLoad = new Fraction(1, 100); // 1% per day

		PiecewiseConstantSpecificDays loadFunction = PiecewiseConstantSpecificDays.of(tinyLoad);

		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 8, 0);
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(1);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		LocalDateTime expected = LocalDateTime.of(2024, 5, 11, 8, 0); // ~100 days later
		assertEquals(expected, end, "Should handle very small fractional loads");
	}

	@Test
	void testLeapYear_february29() {
		// Scenario: Work through leap year February
		PiecewiseConstantWeekday loadFunction = PiecewiseConstantWeekday.of(Fraction.ONE);

		LocalDateTime start = LocalDateTime.of(2024, 2, 27, 9, 0); // 2024 is leap year
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(5);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Feb 27 (5/8), 28, 29, Mar 1, 2, 3 (3/8)
		LocalDateTime expected = LocalDateTime.of(2024, 3, 3, 9, 0);
		assertEquals(expected, end, "Should correctly handle leap year day");
	}

	@Test
	void testMultipleVacationPeriods_complexSchedule() {
		// Scenario: Developer with multiple vacation blocks
		PiecewiseConstantSpecificDays loadFunction = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				// Spring break
				.withDay(LocalDate.of(2024, 4, 8), Fraction.ZERO).withDay(LocalDate.of(2024, 4, 9), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 4, 10), Fraction.ZERO)
				// Doctor appointment - half day
				.withDay(LocalDate.of(2024, 4, 15), new Fraction(1, 2))
				// Long weekend
				.withDay(LocalDate.of(2024, 4, 19), Fraction.ZERO).withDay(LocalDate.of(2024, 4, 22), Fraction.ZERO);

		LocalDateTime start = LocalDateTime.of(2024, 4, 1, 9, 0);
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(15);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime end = integrator.computeEnd(start);

		// Should navigate through all vacation periods
		// 2024-04-01T09:00(Mon) - 2024-04-02T00:00(Tue): load 5/8
		// 2024-04-02T00:00(Tue) - 2024-04-03T00:00(Wed): load 1
		// 2024-04-03T00:00(Wed) - 2024-04-04T00:00(Thu): load 1
		// 2024-04-04T00:00(Thu) - 2024-04-05T00:00(Fri): load 1
		// 2024-04-05T00:00(Fri) - 2024-04-06T00:00(Sat): load 1
		// 2024-04-06T00:00(Sat) - 2024-04-07T00:00(Sun): load 1
		// 2024-04-07T00:00(Sun) - 2024-04-08T00:00(Mon): load 1
		// 2024-04-08T00:00(Mon) - 2024-04-09T00:00(Tue): load 0 - vacation
		// 2024-04-09T00:00(Tue) - 2024-04-10T00:00(Wed): load 0 - vacation
		// 2024-04-10T00:00(Wed) - 2024-04-11T00:00(Thu): load 0 - vacation
		// 2024-04-11T00:00(Thu) - 2024-04-12T00:00(Fri): load 1
		// 2024-04-12T00:00(Fri) - 2024-04-13T00:00(Sat): load 1
		// 2024-04-13T00:00(Sat) - 2024-04-14T00:00(Sun): load 1
		// 2024-04-14T00:00(Sun) - 2024-04-15T00:00(Mon): load 1
		// 2024-04-15T00:00(Mon) - 2024-04-16T00:00(Tue): load 1/2 - half day
		// 2024-04-16T00:00(Tue) - 2024-04-17T00:00(Wed): load 1
		// 2024-04-17T00:00(Wed) - 2024-04-18T00:00(Thu): load 1
		// 2024-04-18T00:00(Thu) - 2024-04-19T00:00(Fri): load 1
		// 2024-04-19T00:00(Fri) - 2024-04-20T00:00(Sat): load 0 - vacation
		// 2024-04-20T00:00(Sat) - 2024-04-20T21:00(Sat): load 7/8

		LocalDateTime expected = LocalDateTime.of(2024, 4, 20, 21, 0);
		assertEquals(expected, end, "Should handle multiple vacation periods correctly");
	}

	// ===========================================================================
	// Tests with PiecewiseConstantSpecificDays - backwards
	// ===========================================================================

	@Test
	void testChristmasHoliday_withVacationDays_backwards() {
		// Scenario: Developer takes vacation from Dec 23-26, 2024
		// Working 100% on other days, 0% during vacation
		PiecewiseConstant loadFunction = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 12, 23), Fraction.ZERO) // Monday
				.withDay(LocalDate.of(2024, 12, 24), Fraction.ZERO) // Tuesday
				.withDay(LocalDate.of(2024, 12, 25), Fraction.ZERO) // Wednesday
				.withDay(LocalDate.of(2024, 12, 26), Fraction.ZERO); // Friday

		LocalDateTime end = LocalDateTime.of(2024, 12, 29, 9, 0); // work finishes Friday Dec 29
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(5);

		LoadIntegrator integrator = new LoadIntegrator(loadFunction, totalLoad);
		LocalDateTime start = integrator.computeStart(end);

		// Should skip the vacation days: Dec 23, 24, 25, 26
		// 2024-12-29T00:00(Sun) - 2024-12-29T09:00(Sun): load 9/24 = 3/8
		// 2024-12-28T00:00(Sat) - 2024-12-29T00:00(Sun): load 1
		// 2024-12-27T00:00(Fri) - 2024-12-28T00:00(Sat): load 1
		// 2024-12-26T00:00(Thu) - 2024-12-27T00:00(Fri): load 0
		// 2024-12-25T00:00(Wed) - 2024-12-26T00:00(Thu): load 0
		// 2024-12-24T00:00(Tue) - 2024-12-25T00:00(Wed): load 0
		// 2024-12-23T00:00(Mon) - 2024-12-24T00:00(Tue): load 0
		// 2024-12-22T00:00(Sun) - 2024-12-23T00:00(Mon): load 1
		// 2024-12-21T00:00(Sat) - 2024-12-22T00:00(Sun): load 1
		// 2024-12-20T09:00(Fri) - 2024-12-21T00:00(Sat): load (24-9)/24 = 15/24 = 5/8

		LocalDateTime expected = LocalDateTime.of(2024, 12, 20, 9, 0);
		assertEquals(expected, start, "Should start before vacation period");
	}

}
