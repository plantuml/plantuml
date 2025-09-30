package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFlagConfigTest extends AbstractCliTest {

	@Test
	void test1() throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");

		final Path mytitleFile = tempDir.resolve("title.tmp");
		Files.writeString(mytitleFile, String.join(System.lineSeparator(), "title myconfig"));

		assertLs("[test.txt, title.tmp]", tempDir);

		Run.main(new String[] { "-config", mytitleFile.toAbsolutePath().toString(), "-svg",
				tempDir.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt, title.tmp]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains(">myconfig</text"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("hello"));

	}

	@Test
	void test2() throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");

		final Path mytitleFile = tempDir.resolve("title.tmp");
		Files.writeString(mytitleFile, String.join(System.lineSeparator(), "title myconfig"));

		final Path myFooterfile = tempDir.resolve("footer.tmp");
		Files.writeString(myFooterfile, String.join(System.lineSeparator(), "footer myfooter"));

		assertLs("[footer.tmp, test.txt, title.tmp]", tempDir);

		Run.main(new String[] { "-config", mytitleFile.toAbsolutePath().toString(), "-config",
				myFooterfile.toAbsolutePath().toString(), "-svg", tempDir.toAbsolutePath().toString() });

		assertLs("[footer.tmp, test.svg, test.txt, title.tmp]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains(">myconfig</text"));
		assertTrue(content.contains(">myfooter</text"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("hello"));

	}

}
