package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFormatHtmlTest extends AbstractCliTest {

	@Test
	void testHtml() throws IOException, InterruptedException {
		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "class foo ", "@enduml"));

		Run.main(new String[] { "-thtml", file.toAbsolutePath().toString() });

		assertLs("[test, test.txt]", tempDir);
		assertLs("[foo.html, index.html]", tempDir.resolve("test"));

		final Path indexFile = tempDir.resolve("test/index.html");
		assertTrue(Files.exists(indexFile));
		final String indexContent = new String(Files.readAllBytes(indexFile), java.nio.charset.StandardCharsets.UTF_8);
		assertTrue(indexContent.contains("foo.html"));

		final Path fooFile = tempDir.resolve("test/foo.html");
		assertTrue(Files.exists(fooFile));
		final String fooContent = new String(Files.readAllBytes(indexFile), java.nio.charset.StandardCharsets.UTF_8);
		assertTrue(fooContent.contains("<h2>Class</h2>"));

	}

}
