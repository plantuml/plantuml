package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunCli1 extends AbstractCliTest {

	@Test
	@StdIo
	void test1(StdOut out) throws IOException, InterruptedException {
		final Path file1 = tempDir.resolve("test.txt");
		Files.writeString(file1,
				String.join(System.lineSeparator(), "@startuml", "!include included.tmp", "alice->bob : test1", "@enduml"));
		final Path included = tempDir.resolve("included.tmp");
		Files.writeString(included,
				String.join(System.lineSeparator(), "@startuml", "Charlie -> Delta: test2", "@enduml"));

		Run.main(new String[] { "-svg", file1.toAbsolutePath().toString() });

		assertLs("[included.tmp, test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		// System.err.println(content.split("\n")[0]);
		
		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("Charlie"));
		assertTrue(content.contains("Delta"));
		assertTrue(content.contains("test1"));
		assertTrue(content.contains("test2"));

		Run.main(new String[] { "-metadata", svgFile.toAbsolutePath().toString() });
		
		// System.err.println(out.capturedString());

		assertTrue(out.capturedString().contains("!include included.tmp"));
		assertTrue(out.capturedString().contains("alice->bob : test1"));


	}



	@Test
	@StdIo
	void test2(StdOut out) throws IOException, InterruptedException {
		final Path file1 = tempDir.resolve("test.txt");
		Files.writeString(file1,
				String.join(System.lineSeparator(), "@startuml", "!include included.tmp", "alice->bob : test1", "@enduml"));
		final Path included = tempDir.resolve("included.tmp");
		Files.writeString(included,
				String.join(System.lineSeparator(), "Charlie -> Delta: test2"));

		Run.main(new String[] { "-svg", file1.toAbsolutePath().toString() });

		assertLs("[included.tmp, test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		// System.err.println(content.split("\n")[0]);
		
		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("Charlie"));
		assertTrue(content.contains("Delta"));
		assertTrue(content.contains("test1"));
		assertTrue(content.contains("test2"));

		Run.main(new String[] { "-metadata", svgFile.toAbsolutePath().toString() });
		
		// System.err.println(out.capturedString());

		assertTrue(out.capturedString().contains("!include included.tmp"));
		assertTrue(out.capturedString().contains("alice->bob : test1"));


	}

}
