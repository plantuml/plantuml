package net.sourceforge.plantuml.klimt.drawing.svg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * Interactive SVGs should embed the minified JS/CSS assets (issue #2023)
 * while keeping the human-readable sources available in the repository.
 */
class InteractiveSvgMinifiedAssetsTest {

	@Test
	void sequenceInteractiveSvgEmbedsMinifiedAssets() throws IOException {
		final String definition = ""
				+ "@startuml\n"
				+ "!pragma svginteractive true\n"
				+ "alice -> bob: hello\n"
				+ "@enduml\n";

		final String svg = renderSvg(definition);

		assertTrue(svg.contains("<script>"), "interactive SVG should embed a script");
		assertTrue(svg.contains("Source (unminified):"), "minified payload should keep source link comment");
		assertTrue(svg.contains("sequencediagram.js"), "source link should point at sequencediagram.js");
		assertTrue(svg.contains("sequencediagram.css"), "source link should point at sequencediagram.css");

		// Runtime markers from the minified sequence-diagram script/CSS.
		assertTrue(svg.contains("floating-header"), "minified sequence assets must retain floating-header hooks");
		assertTrue(svg.contains("currentScript"), "minified script must retain currentScript lookup");

		// Unminified sources use multi-line pretty formatting; the embedded
		// payload should not carry that bulk.
		assertFalse(svg.contains("function toggleFloatingHeader()"),
				"pretty-printed sequencediagram.js must not be embedded as-is");

		final int unminifiedPayloadSize = resourceSize("sequencediagram.js") + resourceSize("sequencediagram.css");
		final int embeddedPayloadSize = extractBetween(svg, "<style type=\"text/css\">", "</style>").length()
				+ extractBetween(svg, "<script>", "</script>").length();
		assertTrue(embeddedPayloadSize < unminifiedPayloadSize,
				"embedded interactive assets should be smaller than unminified sources ("
						+ embeddedPayloadSize + " vs " + unminifiedPayloadSize + ")");
	}

	@Test
	void classInteractiveSvgEmbedsMinifiedDefaultAssets() throws IOException {
		final String definition = ""
				+ "@startuml\n"
				+ "!pragma svginteractive true\n"
				+ "class A\n"
				+ "class B\n"
				+ "A --> B\n"
				+ "@enduml\n";

		final String svg = renderSvg(definition);

		assertTrue(svg.contains("Source (unminified):"), "minified payload should keep source link comment");
		assertTrue(svg.contains("default.js"), "source link should point at default.js");
		assertTrue(svg.contains("default.css"), "source link should point at default.css");
		assertTrue(svg.contains("mouseover-active") || svg.contains("click-active"),
				"minified default assets must retain interaction CSS hooks");
		assertFalse(svg.contains("function escapeForCssAttributeSelector"),
				"pretty-printed default.js must not be embedded as-is");
	}

	private static String renderSvg(String definition) throws IOException {
		final SourceStringReader ssr = new SourceStringReader(definition);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ssr.outputImage(baos, 0, new FileFormatOption(FileFormat.SVG));
		return new String(baos.toByteArray(), UTF_8);
	}

	private static int resourceSize(String name) throws IOException {
		final InputStream is = InteractiveSvgMinifiedAssetsTest.class.getResourceAsStream("/svg/" + name);
		if (is == null)
			throw new IOException("Missing resource /svg/" + name);
		return FileUtils.readText(is).getBytes(StandardCharsets.UTF_8).length;
	}

	private static String extractBetween(String text, String start, String end) {
		final int from = text.indexOf(start);
		if (from < 0)
			return "";
		final int contentStart = from + start.length();
		final int to = text.indexOf(end, contentStart);
		if (to < 0)
			return "";
		return text.substring(contentStart, to);
	}
}
