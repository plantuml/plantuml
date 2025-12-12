package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class LoadIntegratorSimpleTest {

	@Test
	public void integrates_constant_weekday_load_one_day() {
		// Monday 2025-01-06 09:00, constant 100% every day
		final PiecewiseConstant load = PiecewiseConstantWeekday.of(Fraction.ONE);

		final LocalDateTime start = LocalDateTime.of(2025, 1, 6, 9, 0);
		final Fraction total = Fraction.ONE;

		final LocalDateTime expectedEnd = LocalDateTime.of(2025, 1, 7, 9, 0);

		assertEquals(expectedEnd, new LoadIntegrator(load, start, total).computeEnd());
	}

	@Test
	public void integrates_weekday_load_skipping_weekend() {
		// Friday noon, 100% on weekdays, 0% on weekend.
		// Remaining half-day on Friday + half-day on Monday => Monday noon.
		final PiecewiseConstant load = PiecewiseConstantWeekday.of(Fraction.ONE)
				.with(DayOfWeek.SATURDAY, Fraction.ZERO)
				.with(DayOfWeek.SUNDAY, Fraction.ZERO);

		final LocalDateTime start = LocalDateTime.of(2025, 1, 3, 12, 0); // Friday
		final Fraction total = Fraction.ONE;

		final LocalDateTime expectedEnd = LocalDateTime.of(2025, 1, 6, 12, 0); // Monday

		assertEquals(expectedEnd, new LoadIntegrator(load, start, total).computeEnd());
	}

	@Test
	public void integrates_specific_days_with_mixed_daily_rates() {
		// Only two days are working:
		// 2025-02-10 => 100%
		// 2025-02-11 => 50%
		// Total = 1.5 "day-equivalent" => consumes day 10 (1.0) + full day 11 (0.5) => end at 2025-02-12 00:00
		final PiecewiseConstant load = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 2, 10), Fraction.ONE)
				.withDay(LocalDate.of(2025, 2, 11), new Fraction((long) 1, (long) 2));

		final LocalDateTime start = LocalDateTime.of(2025, 2, 10, 0, 0);
		final Fraction total = new Fraction((long) 3, (long) 2);

		final LocalDateTime expectedEnd = LocalDateTime.of(2025, 2, 12, 0, 0);

		assertEquals(expectedEnd, new LoadIntegrator(load, start, total).computeEnd());
	}

	@Test
	public void integrates_specific_day_partial_consumption_inside_one_day() {
		// One specific day has a low load rate: 25% for the whole day.
		// Starting at 06:00, totalLoad = 1/8 day-equivalent.
		// With rate 1/4 per day, duration needed = (1/8) / (1/4) = 1/2 day = 12h => end at 18:00 same day.
		final LocalDate day = LocalDate.of(2025, 3, 15);

		final PiecewiseConstant load = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(day, new Fraction((long) 1, (long) 4));

		final LocalDateTime start = LocalDateTime.of(2025, 3, 15, 6, 0);
		final Fraction total = new Fraction((long) 1, (long) 8);

		final LocalDateTime expectedEnd = LocalDateTime.of(2025, 3, 15, 18, 0);

		assertEquals(expectedEnd, new LoadIntegrator(load, start, total).computeEnd());
	}

}
