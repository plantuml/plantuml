package net.sourceforge.plantuml.project.ngm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.project.ngm.math.Fraction;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantWeekday;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantHours;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantSpecificDays;
import net.sourceforge.plantuml.project.ngm.math.Combiner;

/**
 * TDD tests for NGMTask.withFixedTotalEffort().
 * 
 * These tests define the expected behavior for fixed-effort tasks.
 * They will NOT pass until the implementation is complete.
 * 
 * The key concept: a fixed-effort task has an intrinsic amount of work
 * (e.g., "80 hours of coding"). The duration depends on the allocation,
 * which now includes the calendar (working hours, weekends, holidays)
 * directly via PiecewiseConstant functions.
 * 
 * The NGMAllocation wraps a PiecewiseConstant that defines:
 *   - WHEN work can happen (calendar: hours, weekdays, holidays)
 *   - HOW MUCH capacity is applied (FTE: 100%, 50%, 200%, etc.)
 * 
 * Formula: The LoadIntegrator integrates the allocation function over time
 * until the total effort is consumed.
 */
@Disabled("TDD tests - implementation pending")
class NGMTaskFixedTotalEffortTest {

	// ========================================================================
	// ALLOCATION BUILDERS
	// These combine calendar and capacity into a single NGMAllocation
	// ========================================================================

	/**
	 * Creates a full-time allocation with no calendar constraints (24/7).
	 * Equivalent to NGMAllocation.fullTime().
	 */
	private static NGMAllocation fullTime24x7() {
		return NGMAllocation.fullTime();
	}

	/**
	 * Creates a half-time allocation with no calendar constraints (24/7 at 50%).
	 */
	private static NGMAllocation halfTime24x7() {
		PiecewiseConstant halfTimeLoad = PiecewiseConstantWeekday.of(new Fraction(1, 2));
		return NGMAllocation.of(halfTimeLoad);
	}

	/**
	 * Creates an allocation for two full-time persons (24/7 at 200%).
	 */
	private static NGMAllocation twoPersons24x7() {
		PiecewiseConstant twoFTE = PiecewiseConstantWeekday.of(new Fraction(2, 1));
		return NGMAllocation.of(twoFTE);
	}

	/**
	 * Creates a standard office hours allocation: 08:00-12:00 and 14:00-18:00.
	 * This gives 8 working hours per day, 7 days a week.
	 */
	private static NGMAllocation officeHours() {
		PiecewiseConstant hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);
		return NGMAllocation.of(hours);
	}

	/**
	 * Creates a standard office hours allocation on weekdays only.
	 * 08:00-12:00 and 14:00-18:00, Monday to Friday.
	 * Saturdays and Sundays are fully closed.
	 */
	private static NGMAllocation officeHoursWeekdays() {
		PiecewiseConstant hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		PiecewiseConstant weekdays = PiecewiseConstantWeekday.of(Fraction.ONE)
				.with(DayOfWeek.SATURDAY, Fraction.ZERO)
				.with(DayOfWeek.SUNDAY, Fraction.ZERO);

		return NGMAllocation.of(Combiner.product(hours, weekdays));
	}

	/**
	 * Creates a weekday-only allocation (Mon-Fri, 24h/day).
	 * No hour restrictions, but weekends are closed.
	 */
	private static NGMAllocation weekdaysOnly() {
		PiecewiseConstant weekdays = PiecewiseConstantWeekday.of(Fraction.ONE)
				.with(DayOfWeek.SATURDAY, Fraction.ZERO)
				.with(DayOfWeek.SUNDAY, Fraction.ZERO);
		return NGMAllocation.of(weekdays);
	}

	/**
	 * Creates an office hours allocation with specific holidays marked as closed.
	 */
	private static NGMAllocation officeHoursWithHolidays(LocalDate... holidays) {
		PiecewiseConstant hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE)
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);

		PiecewiseConstant weekdays = PiecewiseConstantWeekday.of(Fraction.ONE)
				.with(DayOfWeek.SATURDAY, Fraction.ZERO)
				.with(DayOfWeek.SUNDAY, Fraction.ZERO);

		PiecewiseConstantSpecificDays specificDays = PiecewiseConstantSpecificDays.of(Fraction.ONE);
		for (LocalDate holiday : holidays) {
			specificDays = specificDays.withDay(holiday, Fraction.ZERO);
		}

		return NGMAllocation.of(Combiner.product(hours, weekdays, specificDays));
	}

	/**
	 * Creates a morning-only allocation (08:00-12:00) on weekdays.
	 */
	private static NGMAllocation morningsOnlyWeekdays() {
		PiecewiseConstant mornings = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), Fraction.ONE);

		PiecewiseConstant weekdays = PiecewiseConstantWeekday.of(Fraction.ONE)
				.with(DayOfWeek.SATURDAY, Fraction.ZERO)
				.with(DayOfWeek.SUNDAY, Fraction.ZERO);

		return NGMAllocation.of(Combiner.product(mornings, weekdays));
	}

	/**
	 * Creates a half-time allocation during office hours on weekdays.
	 * This models someone working 50% during standard office hours.
	 */
	private static NGMAllocation halfTimeOfficeHours() {
		PiecewiseConstant hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), new Fraction(1, 2))
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), new Fraction(1, 2));

		PiecewiseConstant weekdays = PiecewiseConstantWeekday.of(Fraction.ONE)
				.with(DayOfWeek.SATURDAY, Fraction.ZERO)
				.with(DayOfWeek.SUNDAY, Fraction.ZERO);

		return NGMAllocation.of(Combiner.product(hours, weekdays));
	}

	// ========================================================================
	// BASIC TESTS - NO CALENDAR (24/7 working)
	// ========================================================================

	/**
	 * Simplest case: 24 hours of effort with full-time allocation.
	 * No calendar constraints = work happens 24/7.
	 * 
	 * Expected: task takes exactly 24 hours (1 day).
	 * 
	 * Implementation hint:
	 *   - With no calendar, assume 24h/day of work
	 *   - LoadIntegrator integrates: 24h * 1 FTE = 24h elapsed
	 */
	@Test
	void simpleEffort24HoursFullTime() {
		// Create a task requiring 24 hours of work
		NGMTotalEffort effort = NGMTotalEffort.ofHours(24);
		NGMTask task = NGMTask.withFixedTotalEffort(fullTime24x7(), effort);

		// Start on Monday 2025-12-01 at midnight
		LocalDateTime start = LocalDate.of(2025, 12, 1).atStartOfDay();
		task.setStart(start);

		// With full-time allocation and no calendar, 24h effort = 24h duration
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 2).atStartOfDay();
		assertEquals(expectedEnd, task.getEnd(),
				"24 hours of effort at 100% allocation should end exactly 24 hours later");

		// Verify the effort is unchanged (it's intrinsic)
		assertEquals(NGMTotalEffort.ofHours(24), task.getTotalEffort(),
				"Total effort must remain constant at 24 hours");
	}

	/**
	 * Same 24 hours of effort, but with half-time allocation (50%).
	 * 
	 * Expected: task takes 48 hours (2 days) because we only work at 50%.
	 * 
	 * Implementation hint:
	 *   - LoadIntegrator integrates: need 24h of work at 0.5 FTE
	 *   - 24h / 0.5 = 48h elapsed
	 */
	@Test
	void simpleEffort24HoursHalfTime() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(24);
		NGMTask task = NGMTask.withFixedTotalEffort(halfTime24x7(), effort);

		LocalDateTime start = LocalDate.of(2025, 12, 1).atStartOfDay();
		task.setStart(start);

		// Half-time means it takes twice as long
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 3).atStartOfDay();
		assertEquals(expectedEnd, task.getEnd(),
				"24 hours of effort at 50% allocation should take 48 calendar hours");
	}

	/**
	 * 80 hours of effort with two full-time people (allocation = 2 FTE).
	 * 
	 * Expected: task takes 40 hours because two people work in parallel.
	 * 
	 * Implementation hint:
	 *   - LoadIntegrator integrates: need 80h of work at 2 FTE
	 *   - 80h / 2 = 40h elapsed
	 */
	@Test
	void effortWithTwoFullTimeResources() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(80);
		NGMTask task = NGMTask.withFixedTotalEffort(twoPersons24x7(), effort);

		LocalDateTime start = LocalDate.of(2025, 12, 1).atStartOfDay();
		task.setStart(start);

		// Two people at 100% = 2x speed
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 2).atTime(16, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"80 hours of effort with 2 FTE should complete in 40 calendar hours");
	}

	// ========================================================================
	// TESTS WITH WORKING HOURS (8h/day typical office)
	// ========================================================================

	/**
	 * 8 hours of effort with typical office hours (08:00-12:00, 14:00-18:00).
	 * Full-time allocation means 8 working hours per day.
	 * 
	 * Expected: task completes at end of first working day.
	 * 
	 * Implementation hint:
	 *   - LoadIntegrator iterates through working segments
	 *   - 8h of effort fits exactly in one 8h working day
	 *   - Task starts at 08:00, ends at 18:00 (with lunch break skipped)
	 */
	@Test
	void effort8HoursWithOfficeHours() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(8);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHours(), effort);

		// Start Monday 2025-12-01 at 08:00
		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(8, 0);
		task.setStart(start);

		// 8 hours of work fits in one day: 08:00-12:00 (4h) + 14:00-18:00 (4h)
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 1).atTime(18, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"8 hours of effort should complete by end of working day (18:00)");
	}

	/**
	 * 12 hours of effort with typical office hours (8h/day).
	 * 
	 * Expected: task spans 1.5 working days.
	 * - Day 1: 8 hours (08:00-18:00)
	 * - Day 2: 4 hours (08:00-12:00)
	 * 
	 * Implementation hint:
	 *   - LoadIntegrator iterates: Day 1 consumes 8h, Day 2 consumes 4h
	 */
	@Test
	void effort12HoursSpanningTwoDays() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(12);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHours(), effort);

		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(8, 0);
		task.setStart(start);

		// Day 1: 8h consumed, Day 2: 4h consumed (morning only)
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 2).atTime(12, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"12 hours of effort should end at noon on day 2");
	}

	/**
	 * Start in the middle of a working day.
	 * 6 hours of effort starting at 10:00.
	 * 
	 * Expected: 
	 * - 10:00-12:00 = 2h
	 * - 14:00-18:00 = 4h
	 * - Total = 6h, ends at 18:00 same day
	 */
	@Test
	void effortStartingMidDay() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(6);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHours(), effort);

		// Start at 10:00 (2 hours into morning slot)
		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(10, 0);
		task.setStart(start);

		// 10:00-12:00 (2h) + 14:00-18:00 (4h) = 6h total
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 1).atTime(18, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"6 hours starting at 10:00 should complete by 18:00");
	}

	// ========================================================================
	// TESTS WITH WEEKENDS (Monday-Friday only)
	// ========================================================================

	/**
	 * 40 hours of effort (5 working days) starting on Friday.
	 * Weekends are closed (allocation = 0 on Sat/Sun).
	 * 
	 * Expected: task ends on Thursday of next week (skipping Sat/Sun).
	 * 
	 * Implementation hint:
	 *   - LoadIntegrator skips segments where allocation = 0
	 *   - Friday: 24h, skip Sat+Sun, Mon-Wed: remaining hours
	 */
	@Test
	void effortSpanningWeekend() {
		// 40 hours with 24h/day availability on weekdays
		NGMTotalEffort effort = NGMTotalEffort.ofHours(40);
		NGMTask task = NGMTask.withFixedTotalEffort(weekdaysOnly(), effort);

		// Start Friday 2025-12-05 at 00:00
		// Friday = Dec 5, Saturday = Dec 6 (closed), Sunday = Dec 7 (closed)
		// Monday = Dec 8
		LocalDateTime start = LocalDate.of(2025, 12, 5).atStartOfDay();
		task.setStart(start);

		// With weekday calendar (24h/day on weekdays):
		// Fri 00:00 to Sat 00:00 = 24h consumed (16h remaining)
		// Sat+Sun = 0 (skipped)
		// Mon 00:00 to Mon 16:00 = 16h consumed
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 8).atTime(16, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"40 hours starting Friday should end Monday 16:00 (skipping weekend)");
	}

	/**
	 * More realistic: 40 hours of effort with 8h working days and weekends off.
	 * 
	 * Start: Monday 2025-12-01 at 08:00
	 * Expected: Friday 2025-12-05 at 18:00 (exactly 5 working days)
	 */
	@Test
	void fortyHoursOverWorkingWeek() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(40);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHoursWeekdays(), effort);

		// Monday 2025-12-01 at 08:00
		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(8, 0);
		task.setStart(start);

		// 40h / 8h per day = 5 days
		// Mon-Fri, ending Friday at 18:00
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 5).atTime(18, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"40 hours should span exactly Mon-Fri (5 working days)");
	}

	// ========================================================================
	// TESTS WITH HOLIDAYS
	// ========================================================================

	/**
	 * Task spanning a public holiday.
	 * 16 hours of effort, starting Monday, but Tuesday is a holiday.
	 * 
	 * Expected: task ends Wednesday at 18:00 (skipping Tuesday).
	 */
	@Test
	void effortSpanningHoliday() {
		// Tuesday Dec 2nd is a holiday
		NGMAllocation allocation = officeHoursWithHolidays(LocalDate.of(2025, 12, 2));

		NGMTotalEffort effort = NGMTotalEffort.ofHours(16);
		NGMTask task = NGMTask.withFixedTotalEffort(allocation, effort);

		// Monday Dec 1st at 08:00
		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(8, 0);
		task.setStart(start);

		// Mon: 8h, Tue: holiday (0h), Wed: 8h -> total 16h
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 3).atTime(18, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"16 hours should skip holiday and end Wednesday");
	}

	/**
	 * Christmas week scenario: multiple holidays close together.
	 * 24 hours of effort starting Dec 23, with Dec 25-26 as holidays.
	 */
	@Test
	void christmasWeekScheduling() {
		// Dec 25 (Wed) and Dec 26 (Thu) are holidays in 2024
		NGMAllocation allocation = officeHoursWithHolidays(
				LocalDate.of(2024, 12, 25),
				LocalDate.of(2024, 12, 26));

		NGMTotalEffort effort = NGMTotalEffort.ofHours(24); // 3 working days
		NGMTask task = NGMTask.withFixedTotalEffort(allocation, effort);

		// Monday Dec 23, 2024 at 08:00
		LocalDateTime start = LocalDate.of(2024, 12, 23).atTime(8, 0);
		task.setStart(start);

		// Mon Dec 23: 8h
		// Tue Dec 24: 8h
		// Wed Dec 25: HOLIDAY
		// Thu Dec 26: HOLIDAY
		// Fri Dec 27: 8h -> total 24h consumed
		LocalDateTime expectedEnd = LocalDate.of(2024, 12, 27).atTime(18, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"24 hours over Christmas should skip Dec 25-26 holidays");
	}

	// ========================================================================
	// TESTS WITH PARTIAL ALLOCATION
	// ========================================================================

	/**
	 * Half-time worker (50%) during office hours on a 16-hour task.
	 * 
	 * Expected: takes 4 working days instead of 2.
	 * 
	 * With 8h office hours at 50% allocation, effective work = 4h/day.
	 * 16h / 4h per day = 4 days.
	 */
	@Test
	void halfTimeAllocationWithOfficeHours() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(16);
		NGMTask task = NGMTask.withFixedTotalEffort(halfTimeOfficeHours(), effort);

		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(8, 0);
		task.setStart(start);

		// 16h effort at 50% during 8h office hours = 4h effective per day
		// 16h / 4h = 4 working days
		// Mon + Tue + Wed + Thu = 4 days, ending Thu 18:00
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 4).atTime(18, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"16 hours at 50% should take 4 working days");
	}

	/**
	 * Person working only mornings (4h/day) on a 20-hour task.
	 * 
	 * This tests a reduced daily availability (not reduced percentage,
	 * but reduced hours - which is encoded in the allocation calendar).
	 */
	@Test
	void morningOnlyWorker() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(20);
		NGMTask task = NGMTask.withFixedTotalEffort(morningsOnlyWeekdays(), effort);

		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(8, 0); // Monday

		// 20h / 4h per day = 5 days
		// Mon-Fri mornings, ending Friday at 12:00
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 5).atTime(12, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"20 hours with morning-only schedule should take 5 days");
	}

	// ========================================================================
	// TESTS FOR setEnd() - REVERSE CALCULATION
	// ========================================================================

	/**
	 * Set the end date and verify start is correctly computed.
	 * If task must end Friday 18:00 and requires 16h, when must it start?
	 * 
	 * Implementation hint:
	 *   - This requires reverse integration (going backwards in time)
	 *   - LoadIntegrator may need a computeStart() method
	 */
	@Test
	void computeStartFromEnd() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(16);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHoursWeekdays(), effort);

		// Task must end Friday Dec 5th at 18:00
		LocalDateTime end = LocalDate.of(2025, 12, 5).atTime(18, 0);
		task.setEnd(end);

		// Going back 16h of working time from Fri 18:00:
		// - Fri 18:00 to Fri 14:00 = 4h of work
		// - Fri 12:00 to Fri 08:00 = 4h of work (8h total)
		// - Thu 18:00 to Thu 14:00 = 4h of work (12h total)
		// - Thu 12:00 to Thu 08:00 = 4h of work (16h total)
		// So start = Thu 08:00
		LocalDateTime expectedStart = LocalDate.of(2025, 12, 4).atTime(8, 0);
		assertEquals(expectedStart, task.getStart(),
				"16 hours ending Friday 18:00 should start Thursday 08:00");
	}

	/**
	 * Set end date with a holiday in between.
	 * Task must end Friday, requires 24h, Wednesday is a holiday.
	 * 
	 * Expected start: Tuesday 08:00 (skipping Wednesday holiday going backwards).
	 */
	@Test
	void computeStartFromEndWithHoliday() {
		// Wednesday Dec 3rd is a holiday
		NGMAllocation allocation = officeHoursWithHolidays(LocalDate.of(2025, 12, 3));

		NGMTotalEffort effort = NGMTotalEffort.ofHours(24); // 3 working days
		NGMTask task = NGMTask.withFixedTotalEffort(allocation, effort);

		// Must end Friday Dec 5th at 18:00
		LocalDateTime end = LocalDate.of(2025, 12, 5).atTime(18, 0);
		task.setEnd(end);

		// Going back 24h of work from Fri 18:00:
		// - Fri: 8h (24-8=16h remaining)
		// - Thu: 8h (16-8=8h remaining)  
		// - Wed: HOLIDAY (skip)
		// - Tue: 8h (8-8=0h remaining)
		// So start = Tue Dec 2nd at 08:00
		LocalDateTime expectedStart = LocalDate.of(2025, 12, 2).atTime(8, 0);
		assertEquals(expectedStart, task.getStart(),
				"24 hours ending Friday should start Tuesday (skipping Wed holiday)");
	}

	// ========================================================================
	// EDGE CASES
	// ========================================================================

	/**
	 * Zero effort task - should start and end at the same instant.
	 */
	@Test
	void zeroEffortTask() {
		NGMTotalEffort effort = NGMTotalEffort.zero();
		NGMTask task = NGMTask.withFixedTotalEffort(fullTime24x7(), effort);

		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(10, 0);
		task.setStart(start);

		assertEquals(start, task.getEnd(),
				"Zero effort task should end at its start time");
	}

	/**
	 * Very small effort (30 minutes) with office hours.
	 */
	@Test
	void thirtyMinuteTask() {
		NGMTotalEffort effort = NGMTotalEffort.ofMinutes(30);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHours(), effort);

		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(9, 0);
		task.setStart(start);

		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 1).atTime(9, 30);
		assertEquals(expectedEnd, task.getEnd(),
				"30 minute task should end 30 minutes after start");
	}

	/**
	 * Task with hours and minutes: 2h30min.
	 */
	@Test
	void twoHoursAndThirtyMinutesTask() {
		NGMTotalEffort effort = NGMTotalEffort.ofHoursAndMinutes(2, 30);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHours(), effort);

		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(9, 0);
		task.setStart(start);

		// 9:00 + 2h30 = 11:30
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 1).atTime(11, 30);
		assertEquals(expectedEnd, task.getEnd(),
				"2h30min task should end at 11:30");
	}

	/**
	 * Task starting during lunch break - should resume in afternoon.
	 * 
	 * Implementation hint:
	 *   - If start is set to a non-working time (allocation = 0),
	 *     work begins at the next working segment.
	 *   - LoadIntegrator should skip zero-allocation segments.
	 */
	@Test
	void taskStartingDuringLunchBreak() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(2);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHours(), effort);

		// Start during lunch break (12:30)
		LocalDateTime start = LocalDate.of(2025, 12, 1).atTime(12, 30);
		task.setStart(start);

		// Work can only begin at 14:00, then 2h of work ends at 16:00
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 1).atTime(16, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"Task starting at lunch should begin work at 14:00");
	}

	/**
	 * Task starting on Saturday - should resume Monday.
	 */
	@Test
	void taskStartingOnWeekend() {
		NGMTotalEffort effort = NGMTotalEffort.ofHours(8);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHoursWeekdays(), effort);

		// Start Saturday Dec 6th 2025
		LocalDateTime start = LocalDate.of(2025, 12, 6).atTime(10, 0);
		task.setStart(start);

		// No work on Sat/Sun, work starts Monday 08:00, ends Monday 18:00
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 8).atTime(18, 0);
		assertEquals(expectedEnd, task.getEnd(),
				"Task starting Saturday should complete Monday");
	}

	// ========================================================================
	// STRESS TEST - LONG TASK
	// ========================================================================

	/**
	 * 6-month project (approximately 1000 working hours).
	 * Tests that the implementation handles large durations correctly.
	 */
	@Test
	void sixMonthProject() {
		// 1000 hours = 125 working days (at 8h/day)
		// 125 days / 5 days per week = 25 weeks = ~6 months
		NGMTotalEffort effort = NGMTotalEffort.ofHours(1000);
		NGMTask task = NGMTask.withFixedTotalEffort(officeHoursWeekdays(), effort);

		// Start Monday Jan 6, 2025
		LocalDateTime start = LocalDate.of(2025, 1, 6).atTime(8, 0);
		task.setStart(start);

		// 125 working days from Jan 6, 2025
		// This is approximately late June 2025
		// Exact calculation left to implementation, but end should be around:
		// 25 weeks = 175 calendar days -> early July
		LocalDateTime end = task.getEnd();

		// Just verify it's in the expected range (June-July 2025)
		assertEquals(2025, end.getYear(), "Should end in 2025");
		// More precise assertions can be added once implementation exists
	}
}
