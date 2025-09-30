package net.sourceforge.plantuml.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFlagUseSeparatorMinusTest extends AbstractCliTest {

	@Test
	void test1() throws IOException, InterruptedException {
		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "alice->bob:hello1", "newpage",
				"alice->bob:hello2", "@enduml"));

		Run.main(new String[] { file.toAbsolutePath().toString() });

		assertLs("[test.png, test.txt, test_001.png]", tempDir);

	}

	@Test
	void test2() throws IOException, InterruptedException {
		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "alice->bob:hello1", "newpage",
				"alice->bob:hello2", "@enduml"));

		Run.main(new String[] { "-useseparatorminus", file.toAbsolutePath().toString() });

		assertLs("[test-001.png, test.png, test.txt]", tempDir);

	}

}
