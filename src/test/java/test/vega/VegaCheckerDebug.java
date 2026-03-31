package test.vega;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class VegaCheckerDebug extends VegaChecker {

	@Override
	public Path checkOutput(VegaInputFile data, ByteArrayOutputStream baos, String suffix, int nbImages, int imageIndex)
			throws IOException {
		final Path result = checkTextOutput(data, baos, suffix, ".txt", "DEBUG");

		final String actualOutput = normalizeLineEndings(new String(baos.toByteArray(), UTF_8));
		checkDebugContains(data, actualOutput, nbImages, imageIndex);
		return result;

	}

	private void checkDebugContains(VegaInputFile data, String actualOutput, int nbImages, int imageIndex) {
		// "expected-debug" for single image, "expected-debug-N" for multi-image
		final String yamlKey = nbImages == 1 ? "expected-debug" : "expected-debug-" + (imageIndex + 1);
		final String label = data.getPath() + " [image " + (imageIndex + 1) + "]";

		for (final String needle : data.getYamlSubList(yamlKey, "contains"))
			assertTrue(actualOutput.contains(needle), "DEBUG output should contain '" + needle + "' for " + label);

		for (final String needle : data.getYamlSubList(yamlKey, "not-contains"))
			assertFalse(actualOutput.contains(needle), "DEBUG output should not contain '" + needle + "' for " + label);
	}

}
