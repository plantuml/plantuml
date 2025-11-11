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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link NFolderZip}.
 *
 * ZIP layout used in tests: - root.txt -> "ROOT" - dir1/a.txt -> "A" -
 * dir1/dir2/b.txt -> "B" - dir with spaces/c.txt -> "C"
 */
class NFolderZipTest {

	private static final String ROOT_TXT = "root.txt";
	private static final String DIR1_A = "dir1/a.txt";
	private static final String DIR2_B = "dir1/dir2/b.txt";
	private static final String SP_C = "dir with spaces/c.txt";

	@Test
	@DisplayName("read a root-level file via getInputFile")
	void readRootFile(@TempDir Path tempDir) throws Exception {
		Path zip = tempDir.resolve("sample.zip");
		createZip(zip, mapOf(ROOT_TXT, "ROOT", DIR1_A, "A", DIR2_B, "B", SP_C, "C"));

		NFolderZip root = new NFolderZip(zip.toFile());

		try (InputStream is = root.getInputFile(Paths.get(ROOT_TXT)).newInputStream()) {
			assertEquals("ROOT", new String(is.readAllBytes(), StandardCharsets.UTF_8));
		}

		// Ensure resources are closed properly (ZipFile closed by InputFileZip)
		assertDoesNotThrow(() -> Files.delete(zip));
	}

	@Test
	@DisplayName("read nested files via chained getSubfolder")
	void readNestedViaSubfolders(@TempDir Path tempDir) throws Exception {
		Path zip = tempDir.resolve("nested.zip");
		createZip(zip, mapOf(ROOT_TXT, "ROOT", DIR1_A, "A", DIR2_B, "B"));

		NFolderZip root = new NFolderZip(zip.toFile());
		NFolder dir1 = root.getSubfolder(Paths.get("dir1"));
		NFolder dir2 = dir1.getSubfolder(Paths.get("dir2"));

		// read A from dir1/a.txt
		try (InputStream isA = dir1.getInputFile(Paths.get("a.txt")).newInputStream()) {
			assertEquals("A", new String(isA.readAllBytes(), StandardCharsets.UTF_8));
		}
		try (InputStream isA = root.getInputFile(Paths.get("dir1/a.txt")).newInputStream()) {
			assertEquals("A", new String(isA.readAllBytes(), StandardCharsets.UTF_8));
		}

		// read B from dir1/dir2/b.txt
		try (InputStream isB = dir2.getInputFile(Paths.get("b.txt")).newInputStream()) {
			assertEquals("B", new String(isB.readAllBytes(), StandardCharsets.UTF_8));
		}
		try (InputStream isB = root.getInputFile(Paths.get("dir1/dir2/b.txt")).newInputStream()) {
			assertEquals("B", new String(isB.readAllBytes(), StandardCharsets.UTF_8));
		}
	}

	@Test
	@DisplayName("normalize relative traversal (..)")
	void normalizeTraversal(@TempDir Path tempDir) throws Exception {
		Path zip = tempDir.resolve("norm.zip");
		createZip(zip, mapOf(ROOT_TXT, "ROOT", DIR1_A, "A", DIR2_B, "B"));

		NFolderZip root = new NFolderZip(zip.toFile());
		NFolder dir1 = root.getSubfolder(Paths.get("dir1"));
		// Resolve "../root.txt" from inside dir1 → should hit root.txt
		try (InputStream is = dir1.getInputFile(Paths.get("..").resolve("root.txt")).newInputStream()) {
			assertEquals("ROOT", new String(is.readAllBytes(), StandardCharsets.UTF_8));
		}
	}

	@Test
	@DisplayName("read file with spaces in directory name")
	void readWithSpaces(@TempDir Path tempDir) throws Exception {
		Path zip = tempDir.resolve("spaces.zip");
		createZip(zip, mapOf(SP_C, "C"));

		NFolderZip root = new NFolderZip(zip.toFile());
		try (InputStream is = root.getInputFile(Paths.get("dir with spaces").resolve("c.txt")).newInputStream()) {
			assertEquals("C", new String(is.readAllBytes(), StandardCharsets.UTF_8));
		}
	}

	@Test
	@DisplayName("missing entry: newInputStream throws IOException")
	void missingEntryThrows(@TempDir Path tempDir) throws Exception {
		Path zip = tempDir.resolve("missing.zip");
		createZip(zip, mapOf(ROOT_TXT, "ROOT"));

		NFolderZip root = new NFolderZip(zip.toFile());
		InputFile in = root.getInputFile(Paths.get("nope.txt"));
		assertThrows(IOException.class, in::newInputStream);
	}

	@Test
	@DisplayName("multiple independent opens of the same entry")
	void multipleOpens(@TempDir Path tempDir) throws Exception {
		Path zip = tempDir.resolve("multi.zip");
		createZip(zip, mapOf(DIR1_A, "A"));
		NFolderZip root = new NFolderZip(zip.toFile());

		String r1, r2;
		try (InputStream is1 = root.getInputFile(Paths.get("dir1/a.txt")).newInputStream()) {
			r1 = new String(is1.readAllBytes(), StandardCharsets.UTF_8);
		}
		try (InputStream is2 = root.getInputFile(Paths.get("dir1").resolve("a.txt")).newInputStream()) {
			r2 = new String(is2.readAllBytes(), StandardCharsets.UTF_8);
		}
		assertEquals("A", r1);
		assertEquals("A", r2);
	}

	@Test
	@DisplayName("toString shows zip!path form")
	void toStringShowsZipBangPath(@TempDir Path tempDir) throws Exception {
		Path zip = tempDir.resolve("show.zip");
		createZip(zip, mapOf(DIR2_B, "B"));
		NFolderZip root = new NFolderZip(zip.toFile());
		NFolder sub = root.getSubfolder(Paths.get("dir1/dir2"));

		assertEquals("show.zip!", root.toString());
		assertEquals("show.zip!dir1/dir2", sub.toString());

	}

	// ---------- helpers ----------

	private static Map<String, String> mapOf(Object... kv) {
		if (kv.length % 2 != 0)
			throw new IllegalArgumentException("odd kv length");
		Map<String, String> m = new LinkedHashMap<>();
		for (int i = 0; i < kv.length; i += 2) {
			m.put((String) kv[i], (String) kv[i + 1]);
		}
		return m;
	}

	private static void createZip(Path zipPath, Map<String, String> entries) throws IOException {
		Files.createDirectories(zipPath.getParent());
		try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
			for (Map.Entry<String, String> e : entries.entrySet()) {
				String name = e.getKey();
				// ensure directory entries exist in order (optional—most ZIP readers infer
				// dirs)
				int lastSlash = name.lastIndexOf('/');
				if (lastSlash > 0) {
					String dir = name.substring(0, lastSlash + 1);
					// add a directory entry (no-op if duplicate)
					ZipEntry dirEntry = new ZipEntry(dir);
					zos.putNextEntry(dirEntry);
					zos.closeEntry();
				}
				ZipEntry fileEntry = new ZipEntry(name);
				zos.putNextEntry(fileEntry);
				byte[] data = e.getValue().getBytes(StandardCharsets.UTF_8);
				zos.write(data);
				zos.closeEntry();
			}
		}
		assumeTrue(Files.exists(zipPath) && Files.size(zipPath) > 0, "failed to create test ZIP");
	}
}
