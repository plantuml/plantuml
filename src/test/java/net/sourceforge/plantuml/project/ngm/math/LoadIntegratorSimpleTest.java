package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.project.ngm.NGMTotalEffort;

public class LoadIntegratorSimpleTest {

	@Test
	public void integrates_constant_weekday_load_one_day() {
		// Given: A constant 100% load applied every weekday
		final PiecewiseConstant load = PiecewiseConstantWeekday.of(Fraction.ONE);

		// Given: Starting on Monday 2025-01-06 at 09:00
		final LocalDateTime start = LocalDateTime.of(2025, 1, 6, 9, 0);
		
		// Given: Total load to consume is 1 day worth of work (Fraction.ONE = 1 full day)
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(1);

		// When: Computing the end date-time after integrating the load
		final LocalDateTime actualEnd = new LoadIntegrator(load, totalLoad).computeEnd(start);

		// Then: With 100% constant load, 1 day of work should complete 24 hours later
		// Expected: Tuesday 2025-01-07 at 09:00 (exactly 24 hours after start)
		final LocalDateTime expectedEnd = LocalDateTime.of(2025, 1, 7, 9, 0);
		
		assertEquals(expectedEnd, actualEnd);
	}

	@Test
	public void integrates_weekday_load_skipping_weekend() {
		// Given: A 100% load on weekdays (Mon-Fri) but 0% load on weekends (Sat-Sun)
		final PiecewiseConstant load = PiecewiseConstantWeekday.of(Fraction.ONE)
				.with(DayOfWeek.SATURDAY, Fraction.ZERO)
				.with(DayOfWeek.SUNDAY, Fraction.ZERO);

		// Given: Starting on Friday 2025-01-03 at 12:00 (midday)
		final LocalDateTime start = LocalDateTime.of(2025, 1, 3, 12, 0);
		
		// Given: Total load to consume is 1 day worth of work
		NGMTotalEffort totalLoad = NGMTotalEffort.ofDays(1);

		// When: Computing the end date-time after integrating the load
		final LocalDateTime actualEnd = new LoadIntegrator(load, totalLoad).computeEnd(start);

		// Then: The first segment returned is Friday [00:00, 00:00+1day) with 100% load
		//       but integration starts from 12:00, consuming only the remaining 12 hours
		//       This consumes 0.5 days of work (12 hours at 100% load)
		// Then: Weekend segments (Saturday-Sunday) are skipped with 0% load
		// Then: On Monday, we work the remaining 0.5 days (12 hours at 100% load)
		//       which takes us from 00:00 to 12:00
		// Expected: Monday 2025-01-06 at 12:00
		final LocalDateTime expectedEnd = LocalDateTime.of(2025, 1, 6, 12, 0);
		
		assertEquals(expectedEnd, actualEnd);
	}

	@Test
	public void integrates_specific_days_with_mixed_daily_rates() {
		// Given: A load function with specific rates for specific days
		// - 2025-02-10: 100% load (1 full day capacity)
		// - 2025-02-11: 50% load (0.5 day capacity)
		// - All other days: 0% load
		final PiecewiseConstant load = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 2, 10), Fraction.ONE)
				.withDay(LocalDate.of(2025, 2, 11), new Fraction(1, 2));

		// Given: Starting on 2025-02-10 at 00:00 (beginning of the day)
		final LocalDateTime start = LocalDateTime.of(2025, 2, 10, 0, 0);
		
		// Given: Total load to consume is 1.5 days worth of work (3/2)
		NGMTotalEffort totalLoad = NGMTotalEffort.ofHours(36);

		// When: Computing the end date-time after integrating the load
		final LocalDateTime actualEnd = new LoadIntegrator(load, totalLoad).computeEnd(start);

		// Then: On Feb 10 (100% load), we consume 1 full day of work
		// Then: On Feb 11 (50% load), we consume 0.5 days of work (over 24 hours)
		// Total consumed: 1 + 0.5 = 1.5 days
		// Expected: 2025-02-12 at 00:00 (beginning of next day)
		final LocalDateTime expectedEnd = LocalDateTime.of(2025, 2, 12, 0, 0);
		
		assertEquals(expectedEnd, actualEnd);
	}

	@Test
	public void integrates_specific_day_partial_consumption_inside_one_day() {
		// Given: A specific day with 25% load rate (1/4 = 0.25)
		final LocalDate day = LocalDate.of(2025, 3, 15);

		final PiecewiseConstant load = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(day, new Fraction(1, 4));

		// Given: Starting on 2025-03-15 at 06:00 (morning)
		final LocalDateTime start = LocalDateTime.of(2025, 3, 15, 6, 0);
		
		// Given: Total load to consume is 1/8 of a day (0.125 days = 3 hours of work)
		NGMTotalEffort totalLoad = NGMTotalEffort.ofHours(3);

		// When: Computing the end date-time after integrating the load
		final LocalDateTime actualEnd = new LoadIntegrator(load, totalLoad).computeEnd(start);

		// Then: With 25% load rate, to consume 1/8 day of work (3 hours):
		// Time needed = (1/8) / (1/4) = (1/8) * 4 = 1/2 day = 12 hours
		// Starting at 06:00 + 12 hours = 18:00
		// Expected: 2025-03-15 at 18:00
		final LocalDateTime expectedEnd = LocalDateTime.of(2025, 3, 15, 18, 0);
		
		assertEquals(expectedEnd, actualEnd);
	}

}
