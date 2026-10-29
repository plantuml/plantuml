package test.vega;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Checks the deterministic PDF ({@code FileFormat.PDF_DETERMINISTIC}): the
 * expected file is the PDF itself, which is readable since its streams are not
 * compressed. The bytes are compared as ISO-8859-1 text, so that nothing is
 * lost, apart from the carriage returns that a Git checkout may add to a file it
 * takes for text (ignored on both sides).
 */
public class VegaCheckerPdf extends VegaChecker {

	@Override
	public Path checkOutput(VegaInputFile data, ByteArrayOutputStream baos, String suffix, int nbImages, int imageIndex)
			throws IOException {
		final Path expectedFile = getExpectedFile(data.getPath(), suffix, ".pdf");

		if (Files.exists(expectedFile) == false || data.forceWrite()) {
			Files.write(expectedFile, baos.toByteArray());
			return expectedFile;
		}

		final String expected = withoutCarriageReturns(new String(Files.readAllBytes(expectedFile), ISO_8859_1));
		final String actual = withoutCarriageReturns(new String(baos.toByteArray(), ISO_8859_1));
		assertEquals(expected, actual, "PDF output mismatch for " + data.getDisplayPath());
		return null;
	}

	private static String withoutCarriageReturns(String s) {
		return s.replace("\r", "");
	}

}
