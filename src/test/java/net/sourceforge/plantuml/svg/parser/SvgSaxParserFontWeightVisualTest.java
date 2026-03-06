package net.sourceforge.plantuml.svg.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import test.utils.PlantUmlTestUtils;

/**
 * Visual integration tests verifying that SVG sprite text elements carrying
 * CSS font-weight variants (100–900) survive the full pipeline from
 * {@code !pragma svgparser sax} → {@link SvgSaxParser} → {@link UFontFace} →
 * {@code DriverTextSvg} → SVG {@code <text font-weight="…">} output.
 *
 * <p><b>Two test scenarios:</b>
 * <ol>
 *   <li>{@code svgFontWeightMultiWeight.puml} — fonts that physically support
 *     multiple weight variants (Open Sans, Roboto, Noto Sans, Source Sans Pro,
 *     Helvetica Neue). Weights 300, 400, 500, 600, 700, 800 are used.</li>
 *   <li>{@code svgFontWeightStandardFonts.puml} — standard system fonts that
 *     offer only binary bold/normal at the Java2D level (Arial, Times New Roman,
 *     Verdana, Georgia, Tahoma, Courier New). The pipeline must not break and
 *     should still emit intermediate numeric weights in SVG even if the
 *     underlying typeface ignores them.</li>
 * </ol>
 *
 * <p>Both files use {@code !pragma svgparser sax} to explicitly select the SAX
 * parser. The generated SVG output is inspected to confirm:
 * <ul>
 *   <li>No render error messages.</li>
 *   <li>Numeric {@code font-weight} values (e.g. {@code font-weight="700"})
 *     appear instead of the legacy keyword {@code "bold"}.</li>
 *   <li>Non-standard weights (300, 500, 600, 800) are preserved end-to-end.</li>
 *   <li>{@code font-style="italic"} is emitted where the sprite declares it.</li>
 * </ul>
 */
public class SvgSaxParserFontWeightVisualTest {

	private static final Path RESOURCES = Paths.get("src/test/resources/svg-sprites");
	private static final Path OUTPUT_DIR = Paths.get("target/test-output/svg-sprites");

	@BeforeAll
	static void ensureOutputDir() throws IOException {
		if (!Files.exists(OUTPUT_DIR))
			Files.createDirectories(OUTPUT_DIR);
	}

	// -----------------------------------------------------------------------
	// Helpers
	// -----------------------------------------------------------------------

	private static String renderSvg(String pumlFileName) throws IOException {
		final Path pumlFile = RESOURCES.resolve(pumlFileName);
		assertThat(pumlFile).exists();

		final String source = Files.readString(pumlFile, StandardCharsets.UTF_8);
		// Verify the SAX pragma is present in the source
		assertThat(source).as("PUML must contain !pragma svgparser sax").contains("!pragma svgparser sax");

		final PlantUmlTestUtils.ExportDiagram exporter = PlantUmlTestUtils.exportDiagram(source);
		exporter.assertNoError();

		final String svg = exporter.asString(FileFormat.SVG);

		// Persist SVG for manual visual inspection
		Files.writeString(OUTPUT_DIR.resolve(pumlFileName.replace(".puml", "_visual.svg")), svg,
				StandardCharsets.UTF_8);

		return svg;
	}

	// -----------------------------------------------------------------------
	// Multi-weight fonts (Open Sans, Roboto, Noto Sans, Source Sans, Helvetica Neue)
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("Multi-weight fonts: diagram renders without error")
	void multiWeightFonts_noRenderError() throws IOException {
		final String svg = renderSvg("svgFontWeightMultiWeight.puml");
		assertThat(svg).doesNotContain("An error has occurred");
		assertThat(svg).doesNotContain("An error has occured");
	}

	@Test
	@DisplayName("Multi-weight fonts: lightweight (300) preserved in SVG output")
	void multiWeightFonts_weight300Present() throws IOException {
		final String svg = renderSvg("svgFontWeightMultiWeight.puml");
		assertThat(svg).as("font-weight=\"300\" (Light) should appear in SVG output")
				.contains("font-weight=\"300\"");
	}

	@Test
	@DisplayName("Multi-weight fonts: semibold (600) preserved in SVG output")
	void multiWeightFonts_weight600Present() throws IOException {
		final String svg = renderSvg("svgFontWeightMultiWeight.puml");
		assertThat(svg).as("font-weight=\"600\" (SemiBold) should appear in SVG output")
				.contains("font-weight=\"600\"");
	}

	@Test
	@DisplayName("Multi-weight fonts: bold (700) preserved as numeric in SVG output")
	void multiWeightFonts_weight700Present() throws IOException {
		final String svg = renderSvg("svgFontWeightMultiWeight.puml");
		assertThat(svg).as("font-weight=\"700\" (Bold) should appear in SVG output")
				.contains("font-weight=\"700\"");
	}

	@Test
	@DisplayName("Multi-weight fonts: extra-bold (800) preserved in SVG output")
	void multiWeightFonts_weight800Present() throws IOException {
		final String svg = renderSvg("svgFontWeightMultiWeight.puml");
		assertThat(svg).as("font-weight=\"800\" (ExtraBold) should appear in SVG output")
				.contains("font-weight=\"800\"");
	}

	@Test
	@DisplayName("Multi-weight fonts: italic axis preserved in SVG output")
	void multiWeightFonts_italicPresent() throws IOException {
		final String svg = renderSvg("svgFontWeightMultiWeight.puml");
		assertThat(svg).as("font-style=\"italic\" should appear in SVG output")
				.contains("font-style=\"italic\"");
	}

	@Test
	@DisplayName("Multi-weight fonts: no legacy 'bold' keyword in font-weight attribute")
	void multiWeightFonts_noLegacyBoldKeyword() throws IOException {
		final String svg = renderSvg("svgFontWeightMultiWeight.puml");
		assertThat(svg).as("Phase 5: font-weight should never be the literal string 'bold'")
				.doesNotContain("font-weight=\"bold\"");
	}

	// -----------------------------------------------------------------------
	// Standard system fonts (Arial, Times New Roman, Verdana, Georgia, etc.)
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("Standard fonts: diagram renders without error")
	void standardFonts_noRenderError() throws IOException {
		final String svg = renderSvg("svgFontWeightStandardFonts.puml");
		assertThat(svg).doesNotContain("An error has occurred");
		assertThat(svg).doesNotContain("An error has occured");
	}

	@Test
	@DisplayName("Standard fonts: bold (700) preserved as numeric in SVG output")
	void standardFonts_weight700Present() throws IOException {
		final String svg = renderSvg("svgFontWeightStandardFonts.puml");
		assertThat(svg).as("font-weight=\"700\" should appear in SVG (from Arial Bold etc.)")
				.contains("font-weight=\"700\"");
	}

	@Test
	@DisplayName("Standard fonts: intermediate weight (500) preserved even though font ignores it at Java2D level")
	void standardFonts_weight500Present() throws IOException {
		final String svg = renderSvg("svgFontWeightStandardFonts.puml");
		assertThat(svg).as("font-weight=\"500\" should appear in SVG (Tahoma 500) even though rendered as normal")
				.contains("font-weight=\"500\"");
	}

	@Test
	@DisplayName("Standard fonts: italic axis preserved in SVG output")
	void standardFonts_italicPresent() throws IOException {
		final String svg = renderSvg("svgFontWeightStandardFonts.puml");
		assertThat(svg).as("font-style=\"italic\" should appear from Times NR and Georgia italic entries")
				.contains("font-style=\"italic\"");
	}

	@Test
	@DisplayName("Standard fonts: no legacy 'bold' keyword in font-weight attribute")
	void standardFonts_noLegacyBoldKeyword() throws IOException {
		final String svg = renderSvg("svgFontWeightStandardFonts.puml");
		assertThat(svg).as("Phase 5: font-weight should never be the literal string 'bold'")
				.doesNotContain("font-weight=\"bold\"");
	}
}
