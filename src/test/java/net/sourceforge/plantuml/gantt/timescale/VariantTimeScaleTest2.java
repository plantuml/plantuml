package net.sourceforge.plantuml.gantt.timescale;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.gantt.core.TimeRange;
import net.sourceforge.plantuml.gantt.data.WorkingHours;
import net.sourceforge.plantuml.gantt.time.TimePoint;

class VariantTimeScaleTest2 {

	private WorkingHours workingHours;
	private VariantTimeScale timeScale;

	@BeforeEach
	void setup() {
		LocalDate min = LocalDate.of(2026, 5, 26);
		LocalDate max = LocalDate.of(2026, 7, 14);
		double dayWidth = 240.0;
		double closeFactor = 0.1;

		final List<TimeRange> ranges = new ArrayList<>();
		ranges.add(new TimeRange(LocalTime.of(8, 0), LocalTime.of(12, 0)));
		ranges.add(new TimeRange(LocalTime.of(14, 0), LocalTime.of(18, 0)));
		workingHours = new WorkingHours(ranges, dayWidth, closeFactor);
		timeScale = new VariantTimeScale(min, max, workingHours);
		assertEquals(96, workingHours.getWorkingDurationForOneDay(), 0.01);

	}

	@Test
	void testGetPosition() {
		assertEquals(0, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 0, 0))), 0.01);
		assertEquals(1, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 1, 0))), 0.01);
		assertEquals(7, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 7, 0))), 0.01);
		assertEquals(8, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 8, 0))), 0.01);
		assertEquals(18, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 9, 0))), 0.01);
		assertEquals(28, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 10, 0))), 0.01);
		assertEquals(48, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 12, 0))), 0.01);
		assertEquals(49, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 13, 0))), 0.01);
		assertEquals(50, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 14, 0))), 0.01);
		assertEquals(70, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 16, 0))), 0.01);
		assertEquals(90, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 18, 0))), 0.01);
		assertEquals(91, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 19, 0))), 0.01);
		assertEquals(92, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 20, 0))), 0.01);
		assertEquals(93, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 21, 0))), 0.01);
		assertEquals(95, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 26, 23, 0))), 0.01);
		assertEquals(96, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 27, 0, 0))), 0.01);
		assertEquals(97, timeScale.getPosition(TimePoint.of(LocalDateTime.of(2026, 5, 27, 1, 0))), 0.01);
	}

}
