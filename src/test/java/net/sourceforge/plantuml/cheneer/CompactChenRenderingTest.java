package net.sourceforge.plantuml.cheneer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
	private static final String IDENTIFYING_SOURCE = "@startchen\n" + "notation compact\n" + "entity A {\n  id\n}\n"
			+ "entity B {\n  id\n}\n" + "relationship owns <<identifying>> {\n  since\n}\n" + "A -1- owns\n"
			+ "owns =N= B\n"
			+ "<style>\n" + "chenEerDiagram {\n" + "  chenRelationship {\n"
			+ "    BackGroundColor #D2EDF9\n" + "  }\n" + "}\n" + "</style>\n" + "@endchen\n";

	@Test
	void test_svgManualUnderlines() throws Exception {
		final byte[] rendered = render(FileFormat.SVG);
		final String svg = new String(rendered, UTF_8);
		assertTrue(svg.contains("Customer number"));
		assertTrue(svg.contains(">(</text>"));
		assertTrue(svg.contains(">)</text>"));
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

	@Test
	void test_identifyingRelationshipUsesWhiteBandAroundColoredTitle() throws Exception {
		final String svg = new String(render(IDENTIFYING_SOURCE, FileFormat.SVG), UTF_8);
		assertTrue(svg.contains("fill=\"#FFF\""));
		assertTrue(svg.contains("fill=\"#D2EDF9\""));
	}

	@Test
	void test_standardIdentifyingRelationshipKeepsOriginalFill() throws Exception {
		final String source = IDENTIFYING_SOURCE.replace("notation compact\n", "");
		final String svg = new String(render(source, FileFormat.SVG), UTF_8);
		assertFalse(svg.contains("fill=\"#FFF\""));
	}

	private static byte[] render(FileFormat format) throws Exception {
		return render(SOURCE, format);
	}

	private static byte[] render(String source, FileFormat format) throws Exception {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		new SourceStringReader(source).outputImage(output, new FileFormatOption(format));
		return output.toByteArray();
	}

}
