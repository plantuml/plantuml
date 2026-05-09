package test.test;

import static test.utils.PlantUmlTestUtils.exportDiagram;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TaskInstantTest {
	@Test
	public void testWorkingDaysMilestone() throws Exception {
		final String output = test.utils.PlantUmlTestUtils.exportDiagram(
						"@startgantt",
						"saturday are closed",
						"sunday are closed",
						"Project starts 2025-10-20",
						"[T2] starts 2025-10-27 and requires 15 days",
						"[T3] happens on 5 working days after [T2]'s start",
						"@endgantt"
		).asString();

		assertThat(output).contains("T3");
	}
}
