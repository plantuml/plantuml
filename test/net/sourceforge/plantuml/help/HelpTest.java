package net.sourceforge.plantuml.help;

import static net.sourceforge.plantuml.test.TestUtils.renderUmlAsUnicode;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HelpTest {

	@Test
	public void test_help_themes() throws Exception {

		final String output = renderUmlAsUnicode("help themes");
		
		assertThat(output)
				.startsWith("Help on themes")
				.contains("bluegray", "hacker");
	}
}
