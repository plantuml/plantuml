package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFormatTxtTest extends AbstractCliTest {

	@Test
	void testUtxt() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-tutxt", file.toAbsolutePath().toString() });

		assertLs("[test.txt, test.utxt]", tempDir);

		final Path textFile = tempDir.resolve("test.utxt");
		assertTrue(Files.exists(textFile));

		final String content = new String(Files.readAllBytes(textFile), java.nio.charset.StandardCharsets.UTF_8);

		final String expected = String.join("\n", //
				"     ┌─────┐          ┌───┐", //
				"     │alice│          │bob│", //
				"     └──┬──┘          └─┬─┘", //
				"        │    hello      │  ", //
				"        │──────────────>│  ", //
				"     ┌──┴──┐          ┌─┴─┐", //
				"     │alice│          │bob│", //
				"     └─────┘          └───┘", //
				"");

		assertEquals(expected, content);

	}

	@Test
	void testAtxt() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-ttxt", file.toAbsolutePath().toString() });

		assertLs("[test.atxt, test.txt]", tempDir);

		final Path textFile = tempDir.resolve("test.atxt");
		assertTrue(Files.exists(textFile));

		final String content = new String(Files.readAllBytes(textFile), java.nio.charset.StandardCharsets.UTF_8);

		final String expected = String.join("\n", //
				"     ,-----.          ,---.", //
				"     |alice|          |bob|", //
				"     `--+--'          `-+-'", //
				"        |    hello      |  ", //
				"        |-------------->|  ", //
				"     ,--+--.          ,-+-.", //
				"     |alice|          |bob|", //
				"     `-----'          `---'", //
				"");

		assertEquals(expected, content);
	}

}
