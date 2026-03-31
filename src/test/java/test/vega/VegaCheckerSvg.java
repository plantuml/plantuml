package test.vega;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VegaCheckerSvg extends VegaChecker {

	@Override
	public Path checkOutput(VegaInputFile data, ByteArrayOutputStream baos, String suffix, int nbImages, int imageIndex)
			throws IOException {
		final String rawSvg = new String(baos.toByteArray(), UTF_8);
		final String cleanedSvg = SvgCleaner.clean(rawSvg);
		final Path expectedFile = getExpectedFile(data.getPath(), suffix, ".svg");

		if (Files.exists(expectedFile) == false) {
			Files.write(expectedFile, cleanedSvg.getBytes(UTF_8));
			return expectedFile;
		}

		final String expectedSvg = new String(Files.readAllBytes(expectedFile), UTF_8);
		assertEquals(SvgCleaner.normalise(expectedSvg), SvgCleaner.normalise(cleanedSvg),
				"SVG output mismatch for " + data.getPath());
		return null;

	}

}
