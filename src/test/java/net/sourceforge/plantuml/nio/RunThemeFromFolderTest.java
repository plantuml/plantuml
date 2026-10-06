package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.cli.AbstractCliTest;

/**
 * {@code !theme NAME from DIR} reads {@code DIR/puml-theme-NAME.puml}, and says
 * which theme it cannot load when the file is not there.
 */
class RunThemeFromFolderTest extends AbstractCliTest {

	@Test
	void a_theme_is_read_from_the_folder() throws IOException, InterruptedException {
		Files.createDirectories(tempDir.resolve("themes"));
		write("themes/puml-theme-paper.puml", "---", "name: paper", "---", "skinparam backgroundColor #FEDCBA");
		final Path main = main("!theme paper from themes");

		Run.main(new String[] { "-svg", main.toAbsolutePath().toString() });

		final String svg = Files.readString(tempDir.resolve("main.svg"), StandardCharsets.UTF_8);
		assertTrue(svg.toUpperCase().contains("#FEDCBA"), svg);
	}

	@Test
	void a_missing_theme_file_is_reported_by_name() throws Exception {
		Files.createDirectories(tempDir.resolve("themes"));
		final Path main = main("!theme nosuch from themes");

		assertExit(200, () -> Run.main(new String[] { "-svg", main.toAbsolutePath().toString() }));

		final String svg = Files.readString(tempDir.resolve("main.svg"), StandardCharsets.UTF_8);
		assertTrue(svg.contains("Cannot load theme nosuch in themes"), svg);
	}

	@Test
	void a_theme_keeps_the_calling_files_directory() throws IOException, InterruptedException {
		Files.createDirectories(tempDir.resolve("themes"));
		Files.createDirectories(tempDir.resolve("nested"));
		write("themes/puml-theme-including.puml", "!include leaf.puml");
		write("themes/leaf.puml", "Alice -> Bob : wrong directory");
		write("nested/leaf.puml", "Alice -> Bob : caller directory");
		write("nested/after.puml", "Alice -> Bob : after theme");
		write("nested/entry.puml", "!theme including from ../themes", "!include after.puml");
		final Path main = main("!include nested/entry.puml");

		Run.main(new String[] { "-svg", main.toAbsolutePath().toString() });

		final String svg = Files.readString(tempDir.resolve("main.svg"), StandardCharsets.UTF_8);
		assertTrue(svg.contains("caller directory"), svg);
		assertTrue(svg.contains("after theme"), svg);
		assertFalse(svg.contains("wrong directory"), svg);
	}

	private Path main(String theme) throws IOException {
		return write("main.puml", "@startuml", theme, "Alice -> Bob : themed", "@enduml");
	}

	private Path write(String name, String... lines) throws IOException {
		final Path file = tempDir.resolve(name);
		Files.writeString(file, String.join(System.lineSeparator(), lines), StandardCharsets.UTF_8);
		return file;
	}
}
