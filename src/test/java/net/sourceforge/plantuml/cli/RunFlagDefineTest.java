package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFlagDefineTest extends AbstractCliTest {

	@Test
	void test1() throws IOException, InterruptedException {
		final Path file1 = tempDir.resolve("test.txt");
		Files.writeString(file1, String.join(System.lineSeparator(), "@startuml", "alice->bob: VAR", "@enduml"));
		Path file = file1;

		Run.main(new String[] { "-DVAR=foo42", "-svg", file.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String svgContent = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(svgContent.contains("<svg"));
		assertTrue(svgContent.contains("alice"));
		assertTrue(svgContent.contains("bob"));
		assertTrue(svgContent.contains("foo42"));

		// System.out.println(svgContent.split("\n")[0]);

	}

	@Test
	void test2() throws IOException, InterruptedException {
		final Path file1 = tempDir.resolve("test.txt");
		Files.writeString(file1, String.join(System.lineSeparator(), "@startuml", "alice->bob: VAR BAR", "@enduml"));
		Path file = file1;

		Run.main(new String[] { "-DVAR=foo42", "-DBAR=foo43", "-svg", file.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String svgContent = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(svgContent.contains("<svg"));
		assertTrue(svgContent.contains("alice"));
		assertTrue(svgContent.contains("bob"));
		assertTrue(svgContent.contains("foo42"));
		assertTrue(svgContent.contains("foo43"));

		// System.out.println(svgContent.split("\n")[0]);

	}

}
