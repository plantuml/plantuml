package net.sourceforge.plantuml.help;

import static org.assertj.core.api.Assertions.assertThat;
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

		assertThat(output)
				.contains("Help on themes")
				.contains("bluegray", "hacker");
	}
}