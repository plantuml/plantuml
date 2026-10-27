package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultLocale;

import net.sourceforge.plantuml.gantt.GanttTaskTableColumn;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.preproc.spm.SpmChannel;

/**
 * With a Turkish default locale, {@code "I".toLowerCase()} is a dotless
 * {@code "ı"} and {@code "i".toUpperCase()} is a dotted {@code "İ"}.
 * Keywords, file names and SVG attribute names must not depend on it.
 */
@DefaultLocale(language = "tr", country = "TR")
class TurkishLocaleTest {

	@Test
	void svgAttributeNamesAreAscii() throws IOException {
		assertEquals("data-source-line", UGroupType.DATA_SOURCE_LINE.getSvgKeyAttributeName());
		assertEquals("title", UGroupType.TITLE.getSvgKeyAttributeName());

		final String svg = toSvg("@startuml\nalice -> bob\n@enduml");
		assertTrue(svg.contains(" data-source-line=\""), svg);
		assertFalse(svg.contains("ı"), svg);
	}

	@Test
	void stdlibFilesAreFound() throws IOException {
		try (InputStream is = SpmChannel.INFO.getInternalInputStream("c4")) {
			assertNotNull(is);
		}
	}

	@Test
	void lineColorIsParsed() {
		assertEquals(ColorType.LINE, ColorType.getType("line"));
	}

	@Test
	void ganttColumnNameIsParsed() {
		assertEquals(GanttTaskTableColumn.DURATION, GanttTaskTableColumn.of("duration"));
	}

	private static String toSvg(String source) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new SourceStringReader(source).outputImage(baos, 0, new FileFormatOption(FileFormat.SVG));
		return new String(baos.toByteArray(), StandardCharsets.UTF_8);
	}

}
