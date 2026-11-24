package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SourceStringReaderTest {

	// In the sources below, a literal "\n" stands for a line break
	@ParameterizedTest
	@CsvSource(delimiter = '|', value = {
			"@startchart                                     | @startchart without @endchart",
			"@startuml diagram.png\\nalice -> bob            | @startuml without @enduml",
			"   \\startsalt\\n{\\nfoo\\n}                    | @startsalt without @endsalt",
			// Only the last open block counts: the first one is left behind by the second start
			"@startuml\\nalice -> bob\\n@startjson\\n{}      | @startjson without @endjson",
			"plain text                                      | No valid @start/@end found, please check the version",
			"@enduml                                         | No valid @start/@end found, please check the version"
	})
	void namesTheMissingEndLine(String source, String expected) throws IOException {
		final SourceStringReader reader = new SourceStringReader(source.replace("\\n", "\n"));
		assertTrue(reader.getBlocks().isEmpty());
		assertEquals(expected, reader.getNoValidStartMessage());

		// The same message is drawn in the error image
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		reader.outputImage(output, new FileFormatOption(FileFormat.UTXT));
		assertTrue(new String(output.toByteArray(), StandardCharsets.UTF_8).contains(expected));
	}

	@Test
	void closedDiagramIsRendered() throws IOException {
		final SourceStringReader reader = new SourceStringReader("@startchart\n@endchart\n");
		assertEquals(1, reader.getBlocks().size());
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
