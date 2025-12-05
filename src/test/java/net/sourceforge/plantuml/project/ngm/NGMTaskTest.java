package net.sourceforge.plantuml.project.ngm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class NGMTaskTest {

    @Test
    void fixedDurationTaskComputesEndFromStartAndDuration() {
        // 100% workload – adjust according to the actual API
    	// Not used
        NGMWorkload workload = NGMWorkload.fullTime(); 

        // Intrinsic duration: 3 days
        Duration duration = Duration.ofDays(3);

        // Create a fixed-duration task
        NGMTask task = NGMTask.withFixedDuration(workload, duration);

        // Start date set to 2025-11-30 at 00:00 (timezone-agnostic)
        LocalDateTime start = LocalDate.of(2025, 11, 30).atStartOfDay();
        task.setStart(start);

        // Expected end: start + intrinsic duration
        LocalDateTime expectedEnd = LocalDate.of(2025, 12, 3).atStartOfDay();
        assertEquals(expectedEnd, task.getEnd(), "End must equal start + intrinsic duration");

        // Duration must remain equal to the intrinsic duration
        assertEquals(duration, task.getDuration(), "Fixed duration must remain constant");

        // Workload must remain unchanged
        assertEquals(workload, task.getWorkload());
    }
}
