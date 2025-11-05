package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class InputFileZipTest {

	@Test
	@DisplayName("reads an existing entry from a temporary ZIP and returns its content")
	void testNewInputStream_readsEntry(@TempDir Path tempDir) throws Exception {
		// arrange: create a ZIP with one entry
		final Path zipPath = tempDir.resolve("sample.zip");
		final String entryName = "dir/hello.txt";
		final String payload = "hello-zip";

		createZipWithSingleEntry(zipPath, entryName, payload);

		final InputFileZip cut = new InputFileZip(zipPath.toFile(), entryName);

		// act: open and read the entry
		final String read;
		try (InputStream is = cut.newInputStream()) {
			read = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		}

		// assert: content matches
		assertEquals(payload, read);

		// and we should be able to delete the ZIP now (stream properly closed ->
		// ZipFile closed)
		assertDoesNotThrow(() -> Files.delete(zipPath));

		assertEquals("sample.zip!dir", cut.getParentFolder().toString());
	}

	@Test
	@DisplayName("throws IOException when the entry does not exist")
	void testNewInputStream_missingEntry(@TempDir Path tempDir) throws Exception {
		// arrange: create a ZIP with a different entry
		final Path zipPath = tempDir.resolve("sample.zip");
		createZipWithSingleEntry(zipPath, "a/b.txt", "data");

		final InputFileZip cut = new InputFileZip(zipPath.toFile(), "not/found.txt");

		// act + assert
		assertThrows(IOException.class, cut::newInputStream);
	}

	@Test
	@DisplayName("supports opening multiple independent streams")
	void testNewInputStream_multipleOpens(@TempDir Path tempDir) throws Exception {
		// arrange
		final Path zipPath = tempDir.resolve("multi.zip");
		final String entryName = "x/y.txt";
		final String payload = "twice";
		createZipWithSingleEntry(zipPath, entryName, payload);

		final InputFileZip cut = new InputFileZip(zipPath.toFile(), entryName);
		assertEquals("multi.zip!x", cut.getParentFolder().toString());

		// act: open twice and read
		final String read1;
		try (InputStream is1 = cut.newInputStream()) {
			read1 = new String(is1.readAllBytes(), StandardCharsets.UTF_8);
		}
		final String read2;
		try (InputStream is2 = cut.newInputStream()) {
			read2 = new String(is2.readAllBytes(), StandardCharsets.UTF_8);
		}

		// assert
		assertEquals(payload, read1);
		assertEquals(payload, read2);
	}

	@Test
	@DisplayName("getParentFolder() for root-level entry returns ZIP root and can read root file")
	void parentFolder_forRootEntry(@TempDir Path tempDir) throws Exception {
		final Path zipPath = tempDir.resolve("root.zip");
		final String entry = "root.txt";
		final String payload = "ROOT";

		createZipWithSingleEntry(zipPath, entry, payload);

		final InputFileZip cut = new InputFileZip(zipPath.toFile(), entry);

		// parent should be ZIP root
		final NFolder parent = cut.getParentFolder();

		assertEquals("root.zip!", cut.getParentFolder().toString());

		// From root parent, reading "root.txt" should work
		try (InputStream is = parent.getInputFile(Paths.get("root.txt")).newInputStream()) {
			final String read = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			assertEquals(payload, read);
		}
	}

	// ---- helpers ----

	private static void createZipWithSingleEntry(Path zipPath, String entryName, String content) throws IOException {
		Files.createDirectories(zipPath.getParent());
		try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
			final ZipEntry e = new ZipEntry(entryName);
			zos.putNextEntry(e);
			zos.write(content.getBytes(StandardCharsets.UTF_8));
			zos.closeEntry();
		}
		// sanity: zip exists and is non-empty
		assumeTrue(Files.exists(zipPath) && Files.size(zipPath) > 0, "failed to create test ZIP");
	}
}
