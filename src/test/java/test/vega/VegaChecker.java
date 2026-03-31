package test.vega;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class VegaChecker {

	abstract public Path checkOutput(VegaInputFile data, ByteArrayOutputStream baos, String suffix, int nbImages,
			int imageIndex) throws IOException;

	final protected Path checkTextOutput(VegaInputFile data, ByteArrayOutputStream baos, String suffix,
			String extension, String label) throws IOException {
		final String actualOutput = normalizeLineEndings(new String(baos.toByteArray(), UTF_8));
		final Path expectedFile = getExpectedFile(data.getPath(), suffix, extension);

		if (Files.exists(expectedFile) == false) {
			Files.write(expectedFile, actualOutput.getBytes(UTF_8));
			return expectedFile;
		}

		final String expectedOutput = normalizeLineEndings(new String(Files.readAllBytes(expectedFile), UTF_8));
		assertEquals(expectedOutput, actualOutput, label + " output mismatch for " + data.getPath());
		return null;
	}

	final protected String normalizeLineEndings(String s) {
		return s.replace("\r\n", "\n").replace("\r", "\n");
	}

	/**
	 * Returns the expected-output file path by replacing the {@code .puml}
	 * extension with the given suffix and extension.
	 *
	 * <p>
	 * For a single image, suffix is {@code ""} so {@code hello.puml} becomes
	 * {@code hello.txt}. For multiple images, suffix is {@code "-1"}, {@code "-2"},
	 * etc. so {@code newpage.puml} becomes {@code newpage-1.txt},
	 * {@code newpage-2.txt}.
	 */
	final protected Path getExpectedFile(Path pumlPath, String suffix, String extension) {
		final String name = pumlPath.getFileName().toString();
		final String baseName = name.substring(0, name.lastIndexOf('.'));
		return pumlPath.resolveSibling(baseName + suffix + extension);
	}

}
