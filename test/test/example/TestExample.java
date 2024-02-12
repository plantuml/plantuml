package test.example;

import static test.utils.PlantUmlTestUtils.exportDiagram;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TestExample {

	@Test
	public void test_help_themes() throws Exception {

		final String output = exportDiagram(
				"@startuml",
				"help themes",
				"@enduml"
		).asString();

		assertThat(output)
				.startsWith("                                \nHelp on themes")
				.contains("bluegray", "hacker");
	}
}