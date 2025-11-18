package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagIncludeTest extends AbstractCliTest {

	@Test
	@StdIo
	void test1(StdOut out) throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		final Path included = tempDir.resolve("included.tmp");
		Files.writeString(included, String.join(System.lineSeparator(), "alice->bob: I am included"));

		Run.main(
				new String[] { "-I" + included.toAbsolutePath().toString(), "-svg", file.toAbsolutePath().toString() });

		assertLs("[included.tmp, test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("I am included"));

		Run.main(new String[] { "-metadata", svgFile.toAbsolutePath().toString() });

		assertTrue(out.capturedString().contains("alice->bob: I am included"));

		// System.out.println(content.split("\n")[0]);

	}

	@Test
	@StdIo
	void test2(StdOut out) throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		final Path included = tempDir.resolve("included.tmp");
		Files.writeString(included, String.join(System.lineSeparator(), "alice->bob: I am included"));

		Run.main(new String[] { "--include", included.toAbsolutePath().toString(), "-svg",
				file.toAbsolutePath().toString() });

		assertLs("[included.tmp, test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("I am included"));

		Run.main(new String[] { "-metadata", svgFile.toAbsolutePath().toString() });

		assertTrue(out.capturedString().contains("alice->bob: I am included"));

		// System.out.println(content.split("\n")[0]);

	}

	@Test
	@StdIo
	void test3(StdOut out) throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		final Path included = tempDir.resolve("included.tmp");
		Files.writeString(included, String.join(System.lineSeparator(), "alice->bob: I am included"));

		Run.main(new String[] { "-I", included.toAbsolutePath().toString(), "-svg", file.toAbsolutePath().toString() });

		assertLs("[included.tmp, test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("I am included"));

		Run.main(new String[] { "-metadata", svgFile.toAbsolutePath().toString() });

		assertTrue(out.capturedString().contains("alice->bob: I am included"));

		// System.out.println(content.split("\n")[0]);

	}

	@Test
	@StdIo
	void testError1(StdErr err) throws Exception {
		assertExit(42, () -> {
			Run.main(new String[] { "-I" });
		});

		assertLs("[]", tempDir);
		// System.out.println(err.capturedString());

		assertEquals("-I [INCLUDE]: missing value", err.capturedString().replaceAll("\\R", ""));

	}

	@Test
	@StdIo
	void testError2(StdErr err) throws Exception {
		assertExit(42, () -> {
			Run.main(new String[] { "-I", "*" });
		});

		assertLs("[]", tempDir);
		// System.out.println(err.capturedString());
		assertTrue(err.capturedString().startsWith("Cannot have a directory here "));

	}

}
