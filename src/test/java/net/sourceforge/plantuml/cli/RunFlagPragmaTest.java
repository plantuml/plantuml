package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagPragmaTest extends AbstractCliTest {

	@Test
	@StdIo
	void test1(StdOut out) throws IOException, InterruptedException {
		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "alice->bob: VAR", "@enduml"));

		Run.main(new String[] { "-PVAR=foo42", "-svg", file.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("VAR"));

		Run.main(new String[] { "-metadata", svgFile.toAbsolutePath().toString() });

		assertTrue(out.capturedString().contains("!pragma VAR foo42"));

		// System.out.println(content.split("\n")[0]);

	}

	@Test
	@StdIo
	void test2(StdOut out) throws IOException, InterruptedException {
		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "alice->bob: VAR", "@enduml"));

		Run.main(new String[] { "--pragma", "VAR=foo42", "-svg", file.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("VAR"));

		Run.main(new String[] { "-metadata", svgFile.toAbsolutePath().toString() });

		assertTrue(out.capturedString().contains("!pragma VAR foo42"));

		// System.out.println(content.split("\n")[0]);

	}

}
