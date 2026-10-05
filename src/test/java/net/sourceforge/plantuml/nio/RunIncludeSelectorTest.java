package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.cli.AbstractCliTest;

/**
 * A selector after the name of a local file, {@code !include file!1} or
 * {@code !include file!ID}, chooses a diagram of that file, and two diagrams of
 * one file are two includes for the include strategies.
 */
class RunIncludeSelectorTest extends AbstractCliTest {

	@Test
	void an_index_chooses_the_diagram_at_that_place() throws IOException, InterruptedException {
		final String svg = render("!include two.puml!1");
		assertTrue(svg.contains("second diagram"), svg);
		assertFalse(svg.contains("first diagram"), svg);
	}

	@Test
	void an_id_chooses_the_diagram_that_declares_it() throws IOException, InterruptedException {
		final String svg = render("!include two.puml!SECOND");
		assertTrue(svg.contains("second diagram"), svg);
		assertFalse(svg.contains("first diagram"), svg);
	}

	@Test
	void no_selector_takes_the_first_diagram() throws IOException, InterruptedException {
		final String svg = render("!include two.puml");
		assertTrue(svg.contains("first diagram"), svg);
		assertFalse(svg.contains("second diagram"), svg);
	}

	@Test
	void two_diagrams_of_one_file_are_two_includes() throws IOException, InterruptedException {
		final String svg = render("!include two.puml!0", "!include two.puml!1");
		assertTrue(svg.contains("first diagram"), svg);
		assertTrue(svg.contains("second diagram"), svg);
	}

	@Test
	void include_once_counts_each_diagram_of_a_file() throws IOException, InterruptedException {
		final String svg = render("!include_once two.puml!0", "!include_once two.puml!1");
		assertTrue(svg.contains("first diagram"), svg);
		assertTrue(svg.contains("second diagram"), svg);
		assertFalse(svg.contains("already been included"), svg);
	}

	@Test
	void one_diagram_included_twice_is_included_once() throws IOException, InterruptedException {
		final String svg = render("!include two.puml!1", "!include two.puml!1");
		assertEquals(1, svg.split("second diagram", -1).length - 1, svg);
	}

	@ParameterizedTest
	@ValueSource(strings = { "2", "THIRD", "", "(", "2147483648" })
	void an_unusable_selector_is_an_include_error(String selector) throws Exception {
		final String include = "two.puml!" + selector;
		final Path main = main("!include " + include);
		assertExit(200, () -> Run.main(new String[] { "-svg", main.toAbsolutePath().toString() }));
		final String svg = Files.readString(tempDir.resolve("main.svg"), StandardCharsets.UTF_8);
		assertTrue(svg.contains("cannot include " + include), svg);
		assertFalse(svg.contains("Fatal parsing error"), svg);
	}

	@ParameterizedTest
	@ValueSource(strings = { "THIRD", "(" })
	void a_selector_error_keeps_the_file_as_a_dependency(String selector) throws IOException {
		final Path main = main("!include two.puml!" + selector);
		final SourceFileReader reader = new SourceFileReader(false, main.toFile());
		assertTrue(reader.getIncludedFiles().contains(tempDir.resolve("two.puml").toFile().getCanonicalFile()));
	}

	@Test
	void a_file_without_a_diagram_is_included_whole_whatever_the_selector() throws IOException, InterruptedException {
		write("plain.puml", "Ivan -> Judy : plain file");
		final String svg = render("!include plain.puml!1");
		assertTrue(svg.contains("plain file"), svg);
	}

	private String render(String... includes) throws IOException, InterruptedException {
		final Path main = main(includes);

		Run.main(new String[] { "-svg", main.toAbsolutePath().toString() });

		return Files.readString(tempDir.resolve("main.svg"), StandardCharsets.UTF_8);
	}

	private Path main(String... includes) throws IOException {
		write("two.puml", "@startuml(id=FIRST)", "Alice -> Bob : first diagram", "@enduml", "@startuml(id=SECOND)",
				"Carol -> Dave : second diagram", "@enduml");
		final String[] lines = new String[includes.length + 2];
		lines[0] = "@startuml";
		System.arraycopy(includes, 0, lines, 1, includes.length);
		lines[lines.length - 1] = "@enduml";
		return write("main.puml", lines);
	}

	private Path write(String name, String... lines) throws IOException {
		final Path file = tempDir.resolve(name);
		Files.writeString(file, String.join(System.lineSeparator(), lines), StandardCharsets.UTF_8);
		return file;
	}
}
