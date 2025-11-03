package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

class PathSystemTest {

	@Test
	void foo() throws Exception {
		// arrange
		final Path file = Paths.get("rel.txt");
		Files.writeString(file, "hello-rel", StandardCharsets.UTF_8);

		final PathSystem cut = PathSystem.fetch();

		// act
		final InputFile in = cut.getInputFile("rel.txt");

		// assert
		try (InputStream is = in.newInputStream()) {
			final byte[] buf = is.readAllBytes();
			assertEquals("hello-rel", new String(buf, StandardCharsets.UTF_8));
		}
	}

	@Test
	void absolutePath(@TempDir Path tempDir) throws Exception {
		// arrange
		final Path abs = tempDir.resolve("abs.txt").toAbsolutePath();
		Files.writeString(abs, "hello-abs", StandardCharsets.UTF_8);

		final PathSystem cut = PathSystem.fetch();

		// act
		final InputFile in = cut.getInputFile(abs.toString());

		// assert
		try (InputStream is = in.newInputStream()) {
			final byte[] buf = is.readAllBytes();
			assertEquals("hello-abs", new String(buf, StandardCharsets.UTF_8));
		}
	}

	@Test
	@DisplayName("~/ expands to user.home and is readable")
	void tildeHomeExpansion() throws Exception {
		// arrange
		final Path home = Paths.get(System.getProperty("user.home"));
		final Path fileInHome = home.resolve("puml_pathsystem_test_" + System.nanoTime() + ".txt");
		Files.writeString(fileInHome, "hello-home", StandardCharsets.UTF_8);

		final PathSystem cut = PathSystem.fetch();

		try {
			// act
			final InputFile in = cut.getInputFile("~/" + fileInHome.getFileName());
			// assert
			try (InputStream is = in.newInputStream()) {
				final byte[] buf = is.readAllBytes();
				assertEquals("hello-home", new String(buf, StandardCharsets.UTF_8));
			}
		} finally {
			Files.deleteIfExists(fileInHome);
		}
	}

	@Test
	@Timeout(10) // prevent hanging if the network is slow
	@DisplayName("https path returns an html page with a <title>")
	void https_path_contains_title() throws Exception {
		final PathSystem cut = PathSystem.fetch();

		// try to open the URL via the PathSystem branch; skip if offline
		InputFile in;
		try {
			in = cut.getInputFile("https://plantuml.com");
		} catch (IOException e) {
			assumeTrue(false, "network unavailable, skipping test: " + e.getMessage());
			return; // safeguard; assumeTrue already aborts
		}

		try (InputStream raw = in.newInputStream(); InputStream is = new BufferedInputStream(raw)) {

			final String html = readUtf8Limited(is, 256 * 1024); // read at most 256 KB
			assertNotNull(html, "html content should not be null");
			assertTrue(html.toLowerCase(Locale.ROOT).contains("<title"), "expected the html to contain a <title> tag");
		}
	}

	/** reads up to {@code maxBytes} bytes from the stream as a UTF-8 string. */
	private static String readUtf8Limited(InputStream is, int maxBytes) throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream(Math.min(maxBytes, 8192));
		final byte[] buffer = new byte[8192];
		int total = 0;
		int n;
		while ((n = is.read(buffer)) != -1) {
			if (total + n > maxBytes) {
				out.write(buffer, 0, maxBytes - total);
				break;
			}
			out.write(buffer, 0, n);
			total += n;
		}
		return out.toString(StandardCharsets.UTF_8);
	}

}
