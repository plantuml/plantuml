package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for LoadIntegrator with combined (product) load functions.
 * 
 * These tests verify that LoadIntegrator correctly computes end dates when
 * integrating the product of PiecewiseConstantSpecificDays and 
 * PiecewiseConstantWeekday, simulating realistic scenarios where workload
 * allocation must respect both weekly patterns and specific calendar dates.
 */
class LoadIntegratorCombinedTest {
	
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
	
	@Test
	void atLeastTwoFunctionsAreNeededForCombining() throws Exception {

		// Zero functions
		assertThrows(IllegalStateException.class, () -> {
			Combiner.CombinedPiecewiseConstant ps = Combiner.CombinedPiecewiseConstant.of(Fraction.PRODUCT);
			
			ps.apply(LocalDateTime.now());
		});
		
		// One function
		assertThrows(IllegalStateException.class, () -> {
			Combiner.CombinedPiecewiseConstant ps = Combiner.CombinedPiecewiseConstant.of(Fraction.PRODUCT);
			ps = ps.with(PiecewiseConstantSpecificDays.of(Fraction.ONE));
			
			ps.apply(LocalDateTime.now());
		});
		
		
		// Two functions
		Combiner.CombinedPiecewiseConstant ps = Combiner.CombinedPiecewiseConstant.of(Fraction.PRODUCT);
		ps = ps.with(PiecewiseConstantSpecificDays.of(Fraction.ONE), PiecewiseConstantWeekday.of(Fraction.ONE));
			
		ps.apply(LocalDateTime.now());
	}

	@Test
	void testIntegrateProduct_standardWeekWithHolidays() {
		// Scenario: Software sprint during Thanksgiving week
		// Monday-Friday work schedule intersected with Thursday-Friday holiday
		
		// Base schedule: Monday to Friday at 100%
		PiecewiseConstantWeekday workWeek = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.THURSDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);
		
		// Thanksgiving holidays: Nov 28-29, 2024 (Thu-Fri)
		PiecewiseConstantSpecificDays holidays = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 11, 28), Fraction.ZERO) // Thanksgiving
				.withDay(LocalDate.of(2024, 11, 29), Fraction.ZERO); // Black Friday
		
		// Combined: work Mon-Fri except Nov 28-29
		PiecewiseConstant combined = Combiner.product(workWeek, holidays);
		
		// Start Monday Nov 25, need 5 days of work
		LocalDateTime start = LocalDateTime.of(2024, 11, 25, 9, 0);
		Fraction totalLoad = Fraction.of(5);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// 2024-11-25T09:00(Mon) - 2024-11-26T00:00(Tue): load 5/8
		// 2024-11-26T00:00(Tue) - 2024-11-27T00:00(Wed): load 1
		// 2024-11-27T00:00(Wed) - 2024-11-28T00:00(Thu): load 1
		// 2024-11-28T00:00(Thu) - 2024-11-29T00:00(Fri): load 0 - holiday
		// 2024-11-29T00:00(Fri) - 2024-11-30T00:00(Sat): load 0 - holiday
		// 2024-11-30T00:00(Sat) - 2024-12-01T00:00(Sun): load 0 - weekend
		// 2024-12-01T00:00(Sun) - 2024-12-02T00:00(Mon): load 0 - weekend
		// 2024-12-02T00:00(Mon) - 2024-12-03T00:00(Tue): load 1
		// 2024-12-03T00:00(Tue) - 2024-12-04T00:00(Wed): load 1
		// 2024-12-04T00:00(Wed) - 2024-12-04T09:00(Wed): load 3/8
		LocalDateTime expected = LocalDateTime.of(2024, 12, 4, 9, 0);
		assertEquals(expected, end, "Should skip Thanksgiving holidays and weekend");
	}

	@Test
	void testIntegrateProduct_partTimeWithVacation() {
		// Scenario: Part-time consultant (60% allocation) with scheduled vacation
		// Works Monday-Friday at 60%, takes vacation Aug 12-16
		
		// Part-time schedule: Mon-Fri at 60%
		Fraction partTime = new Fraction(3, 5); // 60%
		PiecewiseConstantWeekday workWeek = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, partTime)
				.with(DayOfWeek.TUESDAY, partTime)
				.with(DayOfWeek.WEDNESDAY, partTime)
				.with(DayOfWeek.THURSDAY, partTime)
				.with(DayOfWeek.FRIDAY, partTime);
		
		// Vacation: Aug 12-16, 2024 (full week)
		PiecewiseConstantSpecificDays vacation = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 8, 12), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 8, 13), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 8, 14), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 8, 15), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 8, 16), Fraction.ZERO);
		
		// Combined: 60% Mon-Fri except vacation week
		PiecewiseConstant combined = Combiner.product(workWeek, vacation);
		
		// Start Monday Aug 5, need 6 full days equivalent
		LocalDateTime start = LocalDateTime.of(2024, 8, 5, 10, 0);
		Fraction totalLoad = Fraction.of(6);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// 2024-08-05T10:00(Mon) - 2024-08-06T00:00(Tue): load 14/24 * 3/5 = (14*3)/120 = 42/120 = 7/20
		// 2024-08-06T00:00(Tue) - 2024-08-07T00:00(Wed): load 3/5
		// 2024-08-07T00:00(Wed) - 2024-08-08T00:00(Thu): load 3/5
		// 2024-08-08T00:00(Thu) - 2024-08-09T00:00(Fri): load 3/5
		// 2024-08-09T00:00(Fri) - 2024-08-10T00:00(Sat): load 3/5
		// 2024-08-10T00:00(Sat) - 2024-08-11T00:00(Sun): load 0 - weekend
		// 2024-08-11T00:00(Sun) - 2024-08-12T00:00(Mon): load 0 - weekend
		// 2024-08-12T00:00(Mon) - 2024-08-13T00:00(Tue): load 0 - vacation
		// 2024-08-13T00:00(Tue) - 2024-08-14T00:00(Wed): load 0 - vacation
		// 2024-08-14T00:00(Wed) - 2024-08-15T00:00(Thu): load 0 - vacation
		// 2024-08-15T00:00(Thu) - 2024-08-16T00:00(Fri): load 0 - vacation
		// 2024-08-16T00:00(Fri) - 2024-08-17T00:00(Sat): load 0 - vacation
		// 2024-08-17T00:00(Sat) - 2024-08-18T00:00(Sun): load 0 - weekend
		// 2024-08-18T00:00(Sun) - 2024-08-19T00:00(Mon): load 0 - weekend
		// 2024-08-19T00:00(Mon) - 2024-08-20T00:00(Tue): load 3/5
		// 2024-08-20T00:00(Tue) - 2024-08-21T00:00(Wed): load 3/5
		// 2024-08-21T00:00(Wed) - 2024-08-22T00:00(Thu): load 3/5
		// 2024-08-22T00:00(Thu) - 2024-08-23T00:00(Fri): load 3/5
		// 2024-08-23T00:00(Fri) - 2024-08-24T00:00(Sat): load 3/5
		// 2024-08-24T00:00(Sat) - 2024-08-25T00:00(Sun): load 0 - weekend
		// 2024-08-25T00:00(Sun) - 2024-08-26T00:00(Mon): load 0 - weekend
		// 2024-08-26T00:00(Mon) - 2024-08-26T10:00(Mon): load 10/24 * 3/5 = (10*3)/120 = 30/120 = 1/4
		LocalDateTime expected = LocalDateTime.of(2024, 8, 26, 10, 0);
		assertEquals(expected, end, "Should skip vacation week with part-time allocation");
	}

	@Test
	void testIntegrateProduct_compressedWorkWeekWithHoliday() {
		// Scenario: 4-day compressed work week (Mon-Thu at 125%) with July 4th holiday
		// Tests both enhanced load and specific holiday interaction
		
		// Compressed schedule: Mon-Thu at 125%
		Fraction compressed = new Fraction(5, 4); // 125%
		PiecewiseConstantWeekday workWeek = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, compressed)
				.with(DayOfWeek.TUESDAY, compressed)
				.with(DayOfWeek.WEDNESDAY, compressed)
				.with(DayOfWeek.THURSDAY, compressed);
		
		// July 4th holiday (falls on Thursday in 2024)
		PiecewiseConstantSpecificDays holiday = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 7, 4), Fraction.ZERO);
		
		// Combined: Mon-Thu at 125% except July 4
		PiecewiseConstant combined = Combiner.product(workWeek, holiday);
		
		// Start Monday July 1, need 5 full days
		LocalDateTime start = LocalDateTime.of(2024, 7, 1, 8, 0);
		Fraction totalLoad = Fraction.of(5);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// 2024-07-01T08:00(Mon) - 2024-07-02T00:00(Tue): load (24-8)/24 * 5/4 = 16/24 * 5/4 = 80/96 = 5/6
		// 2024-07-02T00:00(Tue) - 2024-07-03T00:00(Wed): load 5/4
		// 2024-07-03T00:00(Wed) - 2024-07-04T00:00(Thu): load 5/4
		// 2024-07-04T00:00(Thu) - 2024-07-05T00:00(Fri): load 0 - holiday
		// 2024-07-05T00:00(Fri) - 2024-07-06T00:00(Sat): load 0 - not a workday
		// 2024-07-06T00:00(Sat) - 2024-07-07T00:00(Sun): load 0 - weekend
		// 2024-07-07T00:00(Sun) - 2024-07-08T00:00(Mon): load 0 - weekend
		// 2024-07-08T00:00(Mon) - 2024-07-09T00:00(Tue): load 5/4
		// 2024-07-09T00:00(Tue) - 2024-07-09T08:00(Tue): load 8/24 * 5/4 = 5/12
		LocalDateTime expected = LocalDateTime.of(2024, 7, 9, 8, 0);
		assertEquals(expected, end, "Should account for holiday in compressed schedule");
	}

	@Test
	void testIntegrateProduct_weekendWorkWithBlackoutDates() {
		// Scenario: Event coordinator working weekends, with venue unavailable dates
		// Saturday-Sunday work, but venue closed Nov 30 - Dec 1
		
		// Weekend schedule: Sat-Sun at 100%
		PiecewiseConstantWeekday weekendWork = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.SATURDAY, Fraction.ONE)
				.with(DayOfWeek.SUNDAY, Fraction.ONE);
		
		// Venue closed: Nov 30 (Sat) - Dec 1 (Sun)
		PiecewiseConstantSpecificDays venueClosed = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 11, 30), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 12, 1), Fraction.ZERO);
		
		// Combined: work weekends except Nov 30 - Dec 1
		PiecewiseConstant combined = Combiner.product(weekendWork, venueClosed);
		
		// Start Friday Nov 22 (end of work week), need 4 weekend days
		LocalDateTime start = LocalDateTime.of(2024, 11, 22, 18, 0);
		Fraction totalLoad = Fraction.of(4);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// 2024-11-22T00:00(Fri) - 2024-11-23T00:00(Sat): load 0
		// 2024-11-23T00:00(Sat) - 2024-11-24T00:00(Sun): load 1 - weekend work
		// 2024-11-24T00:00(Sun) - 2024-11-25T00:00(Mon): load 1 - weekend work
		// 2024-11-25T00:00(Mon) - 2024-11-26T00:00(Tue): load 0
		// 2024-11-26T00:00(Tue) - 2024-11-27T00:00(Wed): load 0
		// 2024-11-27T00:00(Wed) - 2024-11-28T00:00(Thu): load 0
		// 2024-11-28T00:00(Thu) - 2024-11-29T00:00(Fri): load 0
		// 2024-11-29T00:00(Fri) - 2024-11-30T00:00(Sat): load 0
		// 2024-11-30T00:00(Sat) - 2024-12-01T00:00(Sun): load 0 - venue closed
		// 2024-12-01T00:00(Sun) - 2024-12-02T00:00(Mon): load 0 - venue closed
		// 2024-12-02T00:00(Mon) - 2024-12-03T00:00(Tue): load 0
		// 2024-12-03T00:00(Tue) - 2024-12-04T00:00(Wed): load 0
		// 2024-12-04T00:00(Wed) - 2024-12-05T00:00(Thu): load 0
		// 2024-12-05T00:00(Thu) - 2024-12-06T00:00(Fri): load 0
		// 2024-12-06T00:00(Fri) - 2024-12-07T00:00(Sat): load 0
		// 2024-12-07T00:00(Sat) - 2024-12-08T00:00(Sun): load 1 - weekend work
		// 2024-12-08T00:00(Sun) - 2024-12-09T00:00(Mon): load 1 - weekend work
		LocalDateTime expected = LocalDateTime.of(2024, 12, 9, 0, 0);
		assertEquals(expected, end, "Should skip blackout weekend and continue next available");
	}

	@Test
	void testIntegrateProduct_alternatingScheduleWithConference() {
		// Scenario: Developer with alternating high/low intensity days during conference week
		// Tue/Thu at 150%, Mon/Wed/Fri at 50%, conference reduces all to 30%
		
		// Alternating schedule
		Fraction high = new Fraction(3, 2); // 150%
		Fraction low = new Fraction(1, 2); // 50%
		PiecewiseConstantWeekday alternating = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, low)
				.with(DayOfWeek.TUESDAY, high)
				.with(DayOfWeek.WEDNESDAY, low)
				.with(DayOfWeek.THURSDAY, high)
				.with(DayOfWeek.FRIDAY, low);
		
		// Conference week Oct 7-11: reduce to 30% of normal
		Fraction conferenceReduction = new Fraction(3, 10); // 30%
		PiecewiseConstantSpecificDays conference = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 10, 7), conferenceReduction)
				.withDay(LocalDate.of(2024, 10, 8), conferenceReduction)
				.withDay(LocalDate.of(2024, 10, 9), conferenceReduction)
				.withDay(LocalDate.of(2024, 10, 10), conferenceReduction)
				.withDay(LocalDate.of(2024, 10, 11), conferenceReduction);
		
		// Combined: alternating schedule scaled by conference factor
		PiecewiseConstant combined = Combiner.product(alternating, conference);
		
		// Start Monday Sept 30, need 5 full days
		LocalDateTime start = LocalDateTime.of(2024, 9, 30, 9, 0);
		Fraction totalLoad = Fraction.of(5);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// 2024-09-30T09:00(Mon) - 2024-10-01T00:00(Tue): load 15/24 * 1/2 = 5/16 -- low
		// 2024-10-01T00:00(Tue) - 2024-10-02T00:00(Wed): load 3/2 -- high
		// 2024-10-02T00:00(Wed) - 2024-10-03T00:00(Thu): load 1/2 -- low
		// 2024-10-03T00:00(Thu) - 2024-10-04T00:00(Fri): load 3/2 -- high
		// 2024-10-04T00:00(Fri) - 2024-10-05T00:00(Sat): load 1/2 -- low
		// 2024-10-05T00:00(Sat) - 2024-10-06T00:00(Sun): load 0 -- weekend
		// 2024-10-06T00:00(Sun) - 2024-10-07T00:00(Mon): load 0 -- weekend
		// 2024-10-07T00:00(Mon) - 2024-10-08T00:00(Tue): load 1/2 * 3/10 = 3/20 -- low * conference
		// 2024-10-08T00:00(Tue) - 2024-10-09T00:00(Wed): load 3/2 * 3/10 = 9/20 -- high * conference
		// 2024-10-09T00:00(Wed) - 2024-10-09T14:00(Wed): load 14/24 * 1/2 * 3/10 =  42/480 = 7/80 -- low * conference
		LocalDateTime expected = LocalDateTime.of(2024, 10, 9, 14, 0);
		assertEquals(expected, end, "Should handle complex interaction of alternating and conference schedules");
	}

	@Test
	void testIntegrateProduct_monthEndTransition() {
		// Scenario: Project spanning month-end with fiscal close blackout
		// Standard Mon-Fri, but last business day of month is unavailable
		
		// Standard work week
		PiecewiseConstantWeekday workWeek = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.THURSDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);
		
		// Fiscal close: Jan 31 (Wed) is blocked
		PiecewiseConstantSpecificDays fiscalClose = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 1, 31), Fraction.ZERO);
		
		// Combined
		PiecewiseConstant combined = Combiner.product(workWeek, fiscalClose);
		
		// Start Monday Jan 29, need 4 days
		LocalDateTime start = LocalDateTime.of(2024, 1, 29, 9, 30);
		Fraction totalLoad = Fraction.of(4);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// Jan 29 (Mon): 9:30-24:00 - 29/48 day
		// Jan 30 (Tue): 1 day
		// Jan 31 (Wed): blocked
		// Feb 1 (Thu): 1 day
		// Feb 2 (Fri): 1 day
		// Feb 5 (Mon): 0:00-9:30 - 19/48 day  
		LocalDateTime expected = LocalDateTime.of(2024, 2, 5, 9, 30);
		assertEquals(expected, end, "Should skip fiscal close day at month end");
	}

	@Test
	void testIntegrateProduct_internationalHolidays() {
		// Scenario: Team spanning US and Canadian holidays
		// Mon-Fri schedule with both July 1 (Canada Day) and July 4 (Independence Day)
		
		// Work week
		PiecewiseConstantWeekday workWeek = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.THURSDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);
		
		// International holidays: July 1 (Mon) and July 4 (Thu) 2024
		PiecewiseConstantSpecificDays holidays = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 7, 1), Fraction.ZERO) // Canada Day
				.withDay(LocalDate.of(2024, 7, 4), Fraction.ZERO); // US Independence Day
		
		// Combined
		PiecewiseConstant combined = Combiner.product(workWeek, holidays);
		
		// Start Friday June 28, need 5 days
		LocalDateTime start = LocalDateTime.of(2024, 6, 28, 10, 0);
		Fraction totalLoad = Fraction.of(5);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// Jun 28 (Fri): 10:00-24:00 - 7/12 day
		// Jul 1 (Mon): holiday
		// Jul 2 (Tue): 1 day
		// Jul 3 (Wed): 1 day
		// Jul 4 (Thu): holiday
		// Jul 5 (Fri): 1 day
		// Jul 8 (Mon): 1 day
		// Jul 9 (Tue): 0:00-10:00 - 5/12 day
		LocalDateTime expected = LocalDateTime.of(2024, 7, 9, 10, 0);
		assertEquals(expected, end, "Should respect both Canadian and US holidays");
	}

	@Test
	void testIntegrateProduct_reducedCapacityTraining() {
		// Scenario: Team at reduced capacity during training period
		// Standard Mon-Fri, but training week at 40% capacity
		
		// Work week
		PiecewiseConstantWeekday workWeek = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.THURSDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);
		
		// Training week: Mar 11-15, 2024 at 40% capacity
		Fraction trainingCapacity = new Fraction(2, 5); // 40%
		PiecewiseConstantSpecificDays training = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 3, 11), trainingCapacity)
				.withDay(LocalDate.of(2024, 3, 12), trainingCapacity)
				.withDay(LocalDate.of(2024, 3, 13), trainingCapacity)
				.withDay(LocalDate.of(2024, 3, 14), trainingCapacity)
				.withDay(LocalDate.of(2024, 3, 15), trainingCapacity);
		
		// Combined
		PiecewiseConstant combined = Combiner.product(workWeek, training);
		
		// Start Monday Mar 4, need 8 days
		LocalDateTime start = LocalDateTime.of(2024, 3, 4, 8, 0);
		Fraction totalLoad = Fraction.of(8);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// Week Mar 4: 8:00-24:00 - 16/24 = 2/3 days 
		//  Mar 5-8: 4 days at 100% = 4 days
		// Week Mar 11-15: 5 days at 40% = 2 days (total: 7 days)
		// Week Mar 18: 1 day
		//  Mar 19: 0:00-8:00 - 8/24 = 1/3 day
		LocalDateTime expected = LocalDateTime.of(2024, 3, 19, 8, 0);
		assertEquals(expected, end, "Should account for reduced capacity during training");
	}

	@Test
	void testIntegrateProduct_flexibleScheduleWithDoctorAppointments() {
		// Scenario: Flexible worker (Mon/Wed/Fri) with doctor appointments
		// reducing specific days to half capacity
		
		// Flexible schedule: Mon/Wed/Fri only
		PiecewiseConstantWeekday flexible = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);
		
		// Doctor appointments: May 13 (Mon) and May 22 (Wed) at 50%
		Fraction halfDay = new Fraction(1, 2);
		PiecewiseConstantSpecificDays appointments = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 5, 13), halfDay)
				.withDay(LocalDate.of(2024, 5, 22), halfDay);
		
		// Combined
		PiecewiseConstant combined = Combiner.product(flexible, appointments);
		
		// Start Monday May 6, need 6 days
		LocalDateTime start = LocalDateTime.of(2024, 5, 6, 9, 0);
		Fraction totalLoad = Fraction.of(6);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// 2024-05-06T09:00(Mon) - 2024-05-07T00:00(Tue): load 9:00-24:00 - 15/24 = 5/8
		// 2024-05-07T00:00(Tue) - 2024-05-08T00:00(Wed): load 0 - not a workday
		// 2024-05-08T00:00(Wed) - 2024-05-09T00:00(Thu): load 1
		// 2024-05-09T00:00(Thu) - 2024-05-10T00:00(Fri): load 0 - not a workday
		// 2024-05-10T00:00(Fri) - 2024-05-11T00:00(Sat): load 1
		// 2024-05-11T00:00(Sat) - 2024-05-12T00:00(Sun): load 0 - weekend
		// 2024-05-12T00:00(Sun) - 2024-05-13T00:00(Mon): load 0 - weekend
		// 2024-05-13T00:00(Mon) - 2024-05-14T00:00(Tue): load 1/2 - appointment
		// 2024-05-14T00:00(Tue) - 2024-05-15T00:00(Wed): load 0 - not a workday
		// 2024-05-15T00:00(Wed) - 2024-05-16T00:00(Thu): load 1
		// 2024-05-16T00:00(Thu) - 2024-05-17T00:00(Fri): load 0 - not a workday
		// 2024-05-17T00:00(Fri) - 2024-05-18T00:00(Sat): load 1
		// 2024-05-18T00:00(Sat) - 2024-05-19T00:00(Sun): load 0 - weekend
		// 2024-05-19T00:00(Sun) - 2024-05-20T00:00(Mon): load 0 - weekend
		// 2024-05-20T00:00(Mon) - 2024-05-20T21:00(Mon): load 21/24 = 7/8
		LocalDateTime expected = LocalDateTime.of(2024, 5, 20, 21, 0);
		assertEquals(expected, end, "Should handle half-day appointments in flexible schedule");
	}

	@Test
	void testIntegrateProduct_yearEndHolidayExtravaganza() {
		// Scenario: Year-end with multiple holidays and company shutdown
		// Standard work week with extensive holiday calendar
		
		// Work week
		PiecewiseConstantWeekday workWeek = PiecewiseConstantWeekday.of(Fraction.ZERO)
				.with(DayOfWeek.MONDAY, Fraction.ONE)
				.with(DayOfWeek.TUESDAY, Fraction.ONE)
				.with(DayOfWeek.WEDNESDAY, Fraction.ONE)
				.with(DayOfWeek.THURSDAY, Fraction.ONE)
				.with(DayOfWeek.FRIDAY, Fraction.ONE);
		
		// Company shutdown: Dec 23-27, 2024 and Jan 1, 2025
		PiecewiseConstantSpecificDays shutdown = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2024, 12, 23), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 12, 24), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 12, 25), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 12, 26), Fraction.ZERO)
				.withDay(LocalDate.of(2024, 12, 27), Fraction.ZERO)
				.withDay(LocalDate.of(2025, 1, 1), Fraction.ZERO);
		
		// Combined
		PiecewiseConstant combined = Combiner.product(workWeek, shutdown);
		
		// Start Monday Dec 16, need 10 days
		LocalDateTime start = LocalDateTime.of(2024, 12, 16, 9, 0);
		Fraction totalLoad = Fraction.of(10);
		
		LoadIntegrator integrator = new LoadIntegrator(combined, start, totalLoad);
		LocalDateTime end = integrator.computeEnd();
		
		// 2024-12-16T09:00(Mon) - 2024-12-17T00:00(Tue): load 9:00-24:00 - 15/24 = 5/8
		// 2024-12-17T00:00(Tue) - 2024-12-18T00:00(Wed): load 1
		// 2024-12-18T00:00(Wed) - 2024-12-19T00:00(Thu): load 1
		// 2024-12-19T00:00(Thu) - 2024-12-20T00:00(Fri): load 1
		// 2024-12-20T00:00(Fri) - 2024-12-21T00:00(Sat): load 1
		// 2024-12-21T00:00(Sat) - 2024-12-22T00:00(Sun): load 0 - weekend
		// 2024-12-22T00:00(Sun) - 2024-12-23T00:00(Mon): load 0 - weekend
		// 2024-12-23T00:00(Mon) - 2024-12-24T00:00(Tue): load 0 - shutdown
		// 2024-12-24T00:00(Tue) - 2024-12-25T00:00(Wed): load 0 - shutdown
		// 2024-12-25T00:00(Wed) - 2024-12-26T00:00(Thu): load 0 - shutdown
		// 2024-12-26T00:00(Thu) - 2024-12-27T00:00(Fri): load 0 - shutdown
		// 2024-12-27T00:00(Fri) - 2024-12-28T00:00(Sat): load 0 - shutdown
		// 2024-12-28T00:00(Sat) - 2024-12-29T00:00(Sun): load 0 - weekend
		// 2024-12-29T00:00(Sun) - 2024-12-30T00:00(Mon): load 0 - weekend
		// 2024-12-30T00:00(Mon) - 2024-12-31T00:00(Tue): load 1
		// 2024-12-31T00:00(Tue) - 2025-01-01T00:00(Wed): load 1
		// 2025-01-01T00:00(Wed) - 2025-01-02T00:00(Thu): load 0 - shutdown
		// 2025-01-02T00:00(Thu) - 2025-01-03T00:00(Fri): load 1
		// 2025-01-03T00:00(Fri) - 2025-01-04T00:00(Sat): load 1
		// 2025-01-04T00:00(Sat) - 2025-01-05T00:00(Sun): load 0 - weekend
		// 2025-01-05T00:00(Sun) - 2025-01-06T00:00(Mon): load 0 - weekend
		// 2025-01-06T00:00(Mon) - 2025-01-07T00:00(Tue): load 1
		// 2025-01-07T00:00(Tue) - 2025-01-07T09:00(Tue): load 0:00-9:00 9/24 = 3/8
		LocalDateTime expected = LocalDateTime.of(2025, 1, 7, 9, 0);
		assertEquals(expected, end, "Should navigate complex year-end shutdown period");
	}
}
