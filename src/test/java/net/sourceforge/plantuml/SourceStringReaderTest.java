package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SourceStringReaderTest {

	@ParameterizedTest
	@CsvSource({
			"@startchart, @startchart without @endchart",
			"@startuml diagram.png, @startuml without @enduml",
			"plain text, No valid @start/@end found"
	})
	void reportsMissingDiagramMarkers(String firstLine, String expected) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		new SourceStringReader(firstLine + "\n").outputImage(output, new FileFormatOption(FileFormat.SVG));
		assertTrue(new String(output.toByteArray(), StandardCharsets.UTF_8).contains(expected));
	}

	@Test
	void testSourceStringReaderString() throws IOException {

		final String definition = "@startuml\nalice->bob\n@enduml";
		final SourceStringReader ssr = new SourceStringReader(definition);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ssr.outputImage(baos, 0, new FileFormatOption(FileFormat.SVG));
		final String result = new String(baos.toByteArray());
		assertTrue(result.matches("(?s)^<svg [^>]*xmlns=\"http://www.w3.org/2000/svg\"[^>]*>" + //
				"\\s*<\\?plantuml " + //
				"(" + //
				"(\\$version\\$)" + //
				"|" + //
				"(\\d+\\.\\d+(\\.\\d+)?(beta\\d+)?)" + //
				")" + //
				"\\s*\\?>.*"), result.toString());
	}

}
