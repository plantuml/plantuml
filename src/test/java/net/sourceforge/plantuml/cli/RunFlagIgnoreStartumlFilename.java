package net.sourceforge.plantuml.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFlagIgnoreStartumlFilename extends AbstractCliTest {

	@Test
	void test1() throws IOException, InterruptedException {
		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file,
				String.join(System.lineSeparator(), "@startuml foo.txt", "alice->bob : hello", "@enduml"));

		Run.main(new String[] { file.toAbsolutePath().toString() });

		assertLs("[foo.png, test.txt]", tempDir);

	}

	@Test
	void test2() throws IOException, InterruptedException {
		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file,
				String.join(System.lineSeparator(), "@startuml foo.txt", "alice->bob : hello", "@enduml"));

		Run.main(new String[] { "--ignore-startuml-filename", file.toAbsolutePath().toString() });

		assertLs("[test.png, test.txt]", tempDir);

	}

}
