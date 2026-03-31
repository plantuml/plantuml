package test.vega;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VegaCheckerXmi extends VegaChecker {

	@Override
	public Path checkOutput(VegaInputFile data, ByteArrayOutputStream baos, String suffix, int nbImages, int imageIndex)
			throws IOException {
		final String actualOutput = normalizeLineEndings(new String(baos.toByteArray(), UTF_8));
		final Path expectedFile = getExpectedFile(data.getPath(), suffix, ".xmi");

		if (Files.exists(expectedFile) == false) {
			Files.write(expectedFile, actualOutput.getBytes(UTF_8));
			return expectedFile;
		}

		final String expectedOutput = normalizeLineEndings(new String(Files.readAllBytes(expectedFile), UTF_8));
		assertEquals(cleanXmi(expectedOutput), cleanXmi(actualOutput), "XMI output mismatch for " + data.getPath());
		return null;

	}

	/**
	 * Strips the {@code <XMI.documentation>} block from XMI output so that
	 * version-dependent content (like {@code <XMI.exporterVersion>}) does not cause
	 * spurious comparison failures.
	 */
	private static String cleanXmi(String xmi) {
		return xmi.replaceAll("(?s)<XMI\\.documentation>.*?</XMI\\.documentation>", "");
	}

}
