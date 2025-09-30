package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFlagFilenameTest extends AbstractCliTest {

	@Test
	void testFilename() throws IOException, InterruptedException {
		Files.writeString(tempDir.resolve("test.txt"),
				String.join(System.lineSeparator(), "@startuml", "alice->bob : hello %filename()", "@enduml"));

		Run.main(new String[] { "-svg", "-filename", "foo", tempDir.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("hello test.txt"));

		// System.out.println(content);

	}

}
