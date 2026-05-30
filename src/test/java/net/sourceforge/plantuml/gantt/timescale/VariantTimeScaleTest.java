package net.sourceforge.plantuml.gantt.timescale;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.gantt.core.TimeRange;
import net.sourceforge.plantuml.gantt.data.WorkingHours;
import net.sourceforge.plantuml.gantt.time.TimePoint;

class VariantTimeScaleTest {

	private WorkingHours workingHours;
	private VariantTimeScale timeScale;

	@BeforeEach
	void setup() {
		LocalDate min = LocalDate.of(2026, 5, 26);
		LocalDate max = LocalDate.of(2026, 7, 14);
		double dayWidth = 240.0;
		double closeFactor = 0.1;

		workingHours = new WorkingHours(
				Collections.singletonList(new TimeRange(LocalTime.of(9, 0), LocalTime.of(19, 0))), dayWidth,
				closeFactor);
		timeScale = new VariantTimeScale(min, max, workingHours);
		assertEquals(114, workingHours.getWorkingDurationForOneDay(), 0.01);

	}

	@Test
	void testGetPosition() {
		assertEquals(0, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 0, 0))), 0.01);
		assertEquals(1, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 1, 0))), 0.01);
		assertEquals(9, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 9, 0))), 0.01);
		assertEquals(19, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 10, 0))), 0.01);
		assertEquals(29, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 11, 0))), 0.01);
		assertEquals(99, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 18, 0))), 0.01);
		assertEquals(109, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 19, 0))), 0.01);
		assertEquals(110, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 20, 0))), 0.01);
		assertEquals(111, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 21, 0))), 0.01);
		assertEquals(113, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 23, 0))), 0.01);
		assertEquals(113.5, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 23, 30))), 0.01);
		assertEquals(114, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 27, 0, 0))), 0.01);
		assertEquals(115, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 27, 1, 0))), 0.01);
	}

}
