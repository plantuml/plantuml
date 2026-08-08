package net.sourceforge.plantuml.cheneer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

class CompactChenRenderingTest {

	private static final String SOURCE = "@startchen\n" + "notation compact\n" + "entity E {\n"
			+ "  \"Customer number\" as Number : INTEGER <<key>><<multi>><<derived>>\n"
			+ "  Partial <<discriminator>>\n" + "}\n" + "@endchen\n";

	@Test
	void test_svgManualUnderlines() throws Exception {
		final byte[] rendered = render(FileFormat.SVG);
		final String svg = new String(rendered, UTF_8);
		assertTrue(svg.contains("Customer number"));
		assertTrue(svg.contains(": INTEGER"));
		assertTrue(svg.contains("stroke-dasharray:2,2"));
		assertTrue(svg.contains("<line"));
	}

	@Test
	void test_pngRendering() throws Exception {
		final byte[] png = render(FileFormat.PNG);
		assertTrue(png.length > 100);
		assertArrayEquals(new byte[] { (byte) 0x89, 'P', 'N', 'G' }, new byte[] { png[0], png[1], png[2], png[3] });
	}

	@Test
	void test_debugRendering() throws Exception {
		final String debug = new String(render(FileFormat.DEBUG), UTF_8);
		assertTrue(debug.contains("Customer number"));
		assertTrue(debug.contains("LINE:"));
	}

	private static byte[] render(FileFormat format) throws Exception {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		new SourceStringReader(SOURCE).outputImage(output, new FileFormatOption(format));
		return output.toByteArray();
	}

}
