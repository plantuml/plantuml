package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunBasicTest extends AbstractCliTest {

	@Test
	void testWriteFile() throws IOException {
		Path file = tempDir.resolve("test.txt");

		Files.writeString(file, String.join(System.lineSeparator(), "Hello World", "Second line", "Third line"));

		String content = Files.readString(file);
		assertTrue(content.contains("Hello"));
		assertTrue(content.contains("Second line"));
	}

	@Test
	void testSvg() throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-svg", file.toAbsolutePath().toString() });

//		System.out.println("tempDir:");
//		Files.list(tempDir).map(Path::getFileName).forEach(System.out::println);

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("hello"));

		// System.out.println(content.split("\n")[0]);

	}

	@Test
	void testSvg4() throws IOException, InterruptedException {
		Path file1 = aliceBob_hello(tempDir, "test1.txt");
		Path file2 = aliceBob_hello(tempDir, "test2.txt");
		Path file3 = aliceBob_hello(tempDir, "test3.txt");
		Path file4 = aliceBob_hello(tempDir, "test4.txt");

		Run.main(new String[] { "-svg", file1.toAbsolutePath().toString(), file2.toAbsolutePath().toString(),
				file3.toAbsolutePath().toString(), file4.toAbsolutePath().toString() });

		assertLs("[test1.svg, test1.txt, test2.svg, test2.txt, test3.svg, test3.txt, test4.svg, test4.txt]", tempDir);

	}

	@Test
	void testSvg5() throws IOException, InterruptedException {
		Path file1 = aliceBob_hello(tempDir, "test1.txt");
		Path file2 = aliceBob_hello(tempDir, "test2.txt");
		Path file3 = aliceBob_hello(tempDir, "test3.txt");
		Path file4 = aliceBob_hello(tempDir, "test4.txt");

		Run.main(new String[] { "-svg", tempDir.toAbsolutePath().toString() });

		assertLs("[test1.svg, test1.txt, test2.svg, test2.txt, test3.svg, test3.txt, test4.svg, test4.txt]", tempDir);

	}

	@Test
	void testSvg6() throws IOException, InterruptedException {
		Path file1 = aliceBob_hello(tempDir, "test1.txt");
		Path file2 = aliceBob_hello(tempDir, "test2.txt");
		Path file3 = aliceBob_hello(tempDir, "test3.txt");
		Path file4 = aliceBob_hello(tempDir, "test4.txt");

		Run.main(new String[] { "-svg", tempDir.toAbsolutePath().toString() + File.separator + "*2*" });

		assertLs("[test1.txt, test2.svg, test2.txt, test3.txt, test4.txt]", tempDir);

	}

}
