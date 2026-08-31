package net.sourceforge.plantuml.preproc;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.text.StringLocated;

/**
 * The reader is handed theme files, which may legitimately be empty: the
 * browser build resolves a missing themes.js to an empty theme so published
 * diagrams keep rendering, and a local theme file on the desktop side can be
 * empty too. The first line of an empty source is null, which the YAML header
 * detection used to dereference.
 */
class ReadLineWithYamlHeaderTest {

	private static ReadLineWithYamlHeader reader(String content) {
		return new ReadLineWithYamlHeader(ReadLineReader.create(content.getBytes(UTF_8), "test"));
	}

	@Test
	void empty_source_returns_null_instead_of_throwing() throws IOException {
		assertNull(reader("").readLine());
	}

	@Test
	void yaml_header_is_consumed_and_exposed_as_metadata() throws IOException {
		final ReadLineWithYamlHeader r = reader("---\nname: amiga\n---\nbody line\n");
		final StringLocated first = r.readLine();
		assertEquals("body line", first.getString());
		assertEquals("amiga", r.getMetadata().get("name"));
	}

	@Test
	void source_without_header_is_passed_through() throws IOException {
		final ReadLineWithYamlHeader r = reader("first\nsecond\n");
		assertEquals("first", r.readLine().getString());
		assertEquals("second", r.readLine().getString());
		assertNull(r.readLine());
		assertTrue(r.getMetadata().isEmpty());
	}
}
