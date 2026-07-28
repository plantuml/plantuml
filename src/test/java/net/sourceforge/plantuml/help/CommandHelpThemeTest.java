package net.sourceforge.plantuml.help;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static test.utils.PlantUmlTestUtils.exportDiagram;

import org.junit.jupiter.api.Test;

class CommandHelpThemeTest {

	@Test
	public void command_help_theme() throws Exception {

		final String output = exportDiagram(
				"@startuml",
				"help themes",
				"@enduml"
		).asString();

		assertTrue(output.contains("Help on themes")); assertTrue(output.contains("bluegray")); assertTrue(output.contains("hacker"));
	}
}