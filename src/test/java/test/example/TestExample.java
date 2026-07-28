package test.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static test.utils.PlantUmlTestUtils.exportDiagram;

import org.junit.jupiter.api.Test;

class TestExample {

	@Test
	public void test_help_themes() throws Exception {

		final String output = exportDiagram(
				"@startuml",
				"help themes",
				"@enduml"
		).asString();

		assertTrue(output.contains("Help on themes")); assertTrue(output.contains("bluegray")); assertTrue(output.contains("hacker"));
	}
}