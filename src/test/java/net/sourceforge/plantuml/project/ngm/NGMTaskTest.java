package net.sourceforge.plantuml.project.ngm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class NGMTaskTest {

	@Test
	void fixedDurationTaskComputesEndFromStartAndDuration() {
		// Intrinsic duration: 3 days
		Duration duration = Duration.ofDays(3);

		// Create a fixed-duration task
		NGMTask task = NGMTask.withFixedDuration(NGMAllocation.fullTime(), duration);

		// Start date set to 2025-11-30 at 00:00 (timezone-agnostic)
		LocalDateTime start = LocalDate.of(2025, 11, 30).atStartOfDay();
		task.setStart(start);

		// Expected end: start + intrinsic duration
		LocalDateTime expectedEnd = LocalDate.of(2025, 12, 3).atStartOfDay();
		assertEquals(expectedEnd, task.getEnd(), "End must equal start + intrinsic duration");

		// Duration must remain equal to the intrinsic duration
		assertEquals(duration, task.getDuration(), "Fixed duration must remain constant");
	}

	@Test
	void fixedDurationTaskSixHoursComputesEndFromStart() {
		Duration duration = Duration.ofHours(6);

		NGMTask task = NGMTask.withFixedDuration(NGMAllocation.fullTime(), duration);

		LocalDateTime start = LocalDate.of(2025, 11, 30).atTime(8, 0);
		task.setStart(start);

		LocalDateTime expectedEnd = LocalDate.of(2025, 11, 30).atTime(14, 0);
		assertEquals(expectedEnd, task.getEnd(), "End must equal start + 6h");
		assertEquals(duration, task.getDuration(), "Fixed duration must remain 6h");
	}

	@Test
	void fixedDurationTaskSixHoursRecomputesEndWhenStartChanges() {
		Duration duration = Duration.ofHours(6);

		NGMTask task = NGMTask.withFixedDuration(NGMAllocation.fullTime(), duration);

		// Initial start
		LocalDateTime start1 = LocalDate.of(2025, 11, 30).atTime(8, 0);
		task.setStart(start1);

		LocalDateTime expectedEnd1 = LocalDate.of(2025, 11, 30).atTime(14, 0);
		assertEquals(expectedEnd1, task.getEnd(), "End must follow the initial start + 6h");
		assertEquals(duration, task.getDuration(), "Fixed duration must remain 6h");

		// Change start -> end must be recomputed
		LocalDateTime start2 = LocalDate.of(2025, 11, 30).atTime(10, 30);
		task.setStart(start2);

		assertEquals(LocalDate.of(2025, 11, 30).atTime(16, 30), task.getEnd(), "End must be recomputed when start changes");
		assertEquals(duration, task.getDuration(), "Fixed duration must remain 6h after start change");
	}

	@Test
	void fixedDurationTaskSixHoursRecomputesStartWhenEndChanges() {
		Duration duration = Duration.ofHours(6);

		NGMTask task = NGMTask.withFixedDuration(NGMAllocation.fullTime(), duration);

		// Set an initial start to anchor the task
		LocalDateTime start = LocalDate.of(2025, 11, 30).atTime(9, 0);
		task.setStart(start);

		LocalDateTime expectedEnd = LocalDate.of(2025, 11, 30).atTime(15, 0);
		assertEquals(expectedEnd, task.getEnd(), "End must equal start + 6h");
		assertEquals(duration, task.getDuration(), "Fixed duration must remain 6h");

		// Now change end -> start should shift to preserve 6h duration
		LocalDateTime end2 = LocalDate.of(2025, 11, 30).atTime(18, 0);
		task.setEnd(end2);

		assertEquals(end2, task.getEnd(), "End must reflect the updated value");
		assertEquals(LocalDate.of(2025, 11, 30).atTime(12, 0), task.getStart(), "Start must be recomputed when end changes");
		assertEquals(duration, task.getDuration(), "Fixed duration must remain 6h after end change");
	}

	@Test
	void fixedDurationTaskSixHoursSurvivesMultipleBoundaryEdits() {
		Duration duration = Duration.ofHours(6);

		NGMTask task = NGMTask.withFixedDuration(NGMAllocation.fullTime(), duration);

		// 1) Set start
		LocalDateTime s1 = LocalDateTime.of(2025, 12, 1, 7, 15);
		task.setStart(s1);
		assertEquals(LocalDateTime.of(2025, 12, 1, 13, 15), task.getEnd(), "End must track start + 6h");
		assertEquals(duration, task.getDuration(), "Duration must remain 6h");

		// 2) Set end
		LocalDateTime e2 = LocalDateTime.of(2025, 12, 1, 20, 0);
		task.setEnd(e2);
		assertEquals(LocalDateTime.of(2025, 12, 1, 14, 0), task.getStart(), "Start must track end - 6h");
		assertEquals(e2, task.getEnd(), "End must track the new end");
		assertEquals(duration, task.getDuration(), "Duration must remain 6h");

		// 3) Set start again
		LocalDateTime s3 = LocalDateTime.of(2025, 12, 2, 6, 0);
		task.setStart(s3);
		assertEquals(s3, task.getStart(), "Start must track the new start");
		assertEquals(LocalDateTime.of(2025, 12, 2, 12, 0), task.getEnd(), "End must be recomputed again from start");
		assertEquals(duration, task.getDuration(), "Duration must remain 6h");
	}
}
