package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
