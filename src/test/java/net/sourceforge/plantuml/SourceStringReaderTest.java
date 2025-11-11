package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class SourceStringReaderTest {

	@Test
	void testSourceStringReaderString() throws IOException {

		final String definition = "@startuml\nalice->bob\n@enduml";
		final SourceStringReader ssr = new SourceStringReader(definition);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ssr.outputImage(baos, 0, new FileFormatOption(FileFormat.SVG));
		final String result = new String(baos.toByteArray());
		assertTrue(result.startsWith("<svg "));
	}

}
