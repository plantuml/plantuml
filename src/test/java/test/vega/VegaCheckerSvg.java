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

		if (Files.exists(expectedFile) == false || data.forceWrite()) {
			Files.write(expectedFile, cleanedSvg.getBytes(UTF_8));
			return expectedFile;
		}

		final String expectedSvg = new String(Files.readAllBytes(expectedFile), UTF_8);
		// The reference file is a former cleanedSvg written as is: when nothing changed, the two
		// strings are identical and the two extra parse + pretty-print passes can be skipped.
		// Normalising both sides is only needed when they differ (line endings of a Windows
		// checkout, for instance), to tell a real change from a whitespace one.
		if (expectedSvg.equals(cleanedSvg))
			return null;

		assertEquals(SvgCleaner.normalise(expectedSvg), SvgCleaner.normalise(cleanedSvg),
				"SVG output mismatch for " + data.getDisplayPath());
		return null;

	}

}
