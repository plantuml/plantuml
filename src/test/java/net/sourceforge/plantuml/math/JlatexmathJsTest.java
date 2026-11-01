package net.sourceforge.plantuml.math;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * Guards src/main/teavm/generated/jlatexmath.js (committed, like themes.js)
 * against drift: it must be what {@link JlatexmathJsGenerator} makes from the
 * resources of the library. Gradle runs the tests from the project directory.
 */
class JlatexmathJsTest {

	@Test
	void jlatexmath_js_matches_the_resources_of_the_library() throws IOException {
		final String expected = JlatexmathJsGenerator.generate(new File(JlatexmathJsGenerator.RESOURCES_DIR));
		final String actual = new String(Files.readAllBytes(Paths.get(JlatexmathJsGenerator.OUTPUT)), UTF_8);
		assertEquals(expected, actual, "jlatexmath.js is stale: re-run JlatexmathJsGenerator");
	}

	@Test
	void jlatexmath_js_has_the_glyphs_and_the_metrics() throws IOException {
		final String actual = new String(Files.readAllBytes(Paths.get(JlatexmathJsGenerator.OUTPUT)), UTF_8);
		assertTrue(actual.contains("PLANTUML_JLATEXMATH[\"fonts/glyphs.bin\"]"));
		assertTrue(actual.contains("PLANTUML_JLATEXMATH[\"TeXSymbols.xml\"]"));
		for (int i = 0; i < actual.length(); i++)
			assertTrue(actual.charAt(i) < 0x80, "jlatexmath.js must stay pure ASCII");
	}

}
