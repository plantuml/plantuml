package net.sourceforge.plantuml.style;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import test.utils.PlantUmlTestUtils;

/**
 * Tests for the {@code FontWeight} property in the PlantUML CSS-like styling
 * system, exercising both the {@link Style#getUFont()} unit path and the full
 * end-to-end SVG rendering pipeline from a {@code <style>} block.
 *
 * <p>Covers:
 * <ul>
 *   <li>Unit: {@code FontWeight} alone sets the CSS numeric weight on the
 *     returned {@link UFont}'s {@link UFontFace}.</li>
 *   <li>Unit: {@code FontWeight} + {@code FontStyle: italic} are independent
 *     axes — both survive {@code getUFont()}.</li>
 *   <li>Unit: {@code FontWeight} overrides the weight that would have come
 *     from {@code FontStyle: bold}.</li>
 *   <li>Unit: When only {@code FontStyle: bold} is set (no explicit
 *     {@code FontWeight}), weight defaults to 700 (legacy behaviour).</li>
 *   <li>Integration: The user-requested sequence diagram with
 *     {@code FontWeight: 900} + {@code FontStyle: italic} in a
 *     {@code participant} style block produces SVG {@code <text>} elements
 *     carrying {@code font-weight="900"} and {@code font-style="italic"}.</li>
 * </ul>
 */
class StyleFontWeightTest {

	private static final AutomaticCounter COUNTER = () -> 0;

	// -----------------------------------------------------------------------
	// Helper: build a Style programmatically from loose PName→value pairs
	// -----------------------------------------------------------------------

	private static Style style(Object... nameValuePairs) {
		final Map<PName, Value> map = new EnumMap<>(PName.class);
		for (int i = 0; i < nameValuePairs.length; i += 2) {
			final PName name = (PName) nameValuePairs[i];
			final String rawValue = (String) nameValuePairs[i + 1];
			map.put(name, ValueImpl.regular(rawValue, COUNTER));
		}
		return new Style(StyleSignatureBasic.empty(), map);
	}

	// -----------------------------------------------------------------------
	// Unit tests — Style.getUFont()
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("FontWeight: 900 alone sets CSS weight 900 on the UFont face")
	void fontWeight900AloneSetsWeight() {
		final Style s = style(PName.FontWeight, "900");
		final UFontFace face = s.getUFont().getFontFace();
		assertThat(face.getCssWeight()).isEqualTo(900);
	}

	@Test
	@DisplayName("FontWeight: 300 sets CSS weight 300 (Light)")
	void fontWeight300SetsWeightLight() {
		final Style s = style(PName.FontWeight, "300");
		assertThat(s.getUFont().getFontFace().getCssWeight()).isEqualTo(300);
	}

	@Test
	@DisplayName("FontWeight: bold keyword produces weight 700")
	void fontWeightBoldKeywordProduces700() {
		final Style s = style(PName.FontWeight, "bold");
		assertThat(s.getUFont().getFontFace().getCssWeight()).isEqualTo(700);
	}

	@Test
	@DisplayName("FontWeight: 900 + FontStyle: italic — both axes preserved independently")
	void fontWeight900AndItalicAreBothPreserved() {
		final Style s = style(PName.FontWeight, "900", PName.FontStyle, "italic");
		final UFontFace face = s.getUFont().getFontFace();
		assertThat(face.getCssWeight()).as("weight").isEqualTo(900);
		assertThat(face.isItalic()).as("italic").isTrue();
	}

	@Test
	@DisplayName("FontWeight: 500 + FontStyle: italic — medium-weight italic")
	void fontWeight500AndItalic() {
		final Style s = style(PName.FontWeight, "500", PName.FontStyle, "italic");
		final UFontFace face = s.getUFont().getFontFace();
		assertThat(face.getCssWeight()).as("weight").isEqualTo(500);
		assertThat(face.isItalic()).as("italic").isTrue();
	}

	@Test
	@DisplayName("FontWeight: 900 overrides the weight that FontStyle: bold would set (700)")
	void fontWeight900OverridesBoldFromFontStyle() {
		// FontStyle=bold would give 700; explicit FontWeight=900 must win
		final Style s = style(PName.FontWeight, "900", PName.FontStyle, "bold");
		assertThat(s.getUFont().getFontFace().getCssWeight())
				.as("FontWeight takes precedence over FontStyle bold")
				.isEqualTo(900);
	}

	@Test
	@DisplayName("No FontWeight: FontStyle: bold still defaults to weight 700 (legacy behaviour)")
	void noFontWeightBoldStyleDefaultsTo700() {
		final Style s = style(PName.FontStyle, "bold");
		assertThat(s.getUFont().getFontFace().getCssWeight()).isEqualTo(700);
	}

	@Test
	@DisplayName("No FontWeight, no FontStyle: weight defaults to 400 (normal)")
	void noFontWeightNoFontStyleDefaultsTo400() {
		final Style s = style(PName.FontSize, "14"); // some unrelated property
		assertThat(s.getUFont().getFontFace().getCssWeight()).isEqualTo(400);
	}

	@Test
	@DisplayName("FontWeight: 900 — toCssWeightString() returns '900'")
	void fontWeight900CssWeightString() {
		final Style s = style(PName.FontWeight, "900");
		assertThat(s.getUFont().getFontFace().toCssWeightString()).isEqualTo("900");
	}

	@Test
	@DisplayName("FontWeight: 900, FontSize: 26 — size is honoured independently")
	void fontSizeIsIndependentOfFontWeight() {
		final Style s = style(PName.FontWeight, "900", PName.FontSize, "26");
		assertThat(s.getUFont().getSize()).isEqualTo(26);
		assertThat(s.getUFont().getFontFace().getCssWeight()).isEqualTo(900);
	}

	// -----------------------------------------------------------------------
	// Integration test — full SVG rendering pipeline
	// -----------------------------------------------------------------------

	/**
	 * The exact PUML from the user request: a sequence diagram whose
	 * {@code participant} style block sets {@code FontWeight: 900},
	 * {@code FontStyle: italic}, {@code FontColor: green}, {@code FontSize: 26}.
	 *
	 * <p>Assertions on the rendered SVG:
	 * <ul>
	 *   <li>No render error.</li>
	 *   <li>{@code font-weight="900"} appears in the SVG — the numeric weight
	 *     was emitted by {@code DriverTextSvg} via {@code UFontFace.toCssWeightString()}.</li>
	 *   <li>{@code font-style="italic"} appears in the SVG.</li>
	 *   <li>No legacy {@code font-weight="bold"} keyword anywhere.</li>
	 * </ul>
	 */
	private static final String PARTICIPANT_STYLE_PUML =
		"@startuml\n" +
		"<style>\n" +
		"  sequenceDiagram {\n" +
		"    participant {\n" +
		"      FontName: Roboto;\n" +
		"      FontColor: green;\n" +
		"      FontSize: 26;\n" +
		"      FontStyle: italic;\n" +
		"      LineColor: #E00;\n" +
		"      FontWeight: 900\n" +
		"    }\n" +
		"  }\n" +
		"</style>\n" +
		"\n" +
		"participant Alice\n" +
		"participant Bob\n" +
		"\n" +
		"Alice -> Bob : hello\n" +
		"@enduml\n";

	@Test
	@DisplayName("Integration: participant FontWeight:900 + FontStyle:italic renders font-weight='900' in SVG")
	void participantStyleFontWeight900EmitsSvgFontWeight() throws IOException {
		final PlantUmlTestUtils.ExportDiagram exporter =
				PlantUmlTestUtils.exportDiagram(PARTICIPANT_STYLE_PUML);
		exporter.assertNoError();

		final String svg = exporter.asString(FileFormat.SVG);

		assertThat(svg)
				.as("Diagram should render without error")
				.doesNotContain("An error has occurred")
				.doesNotContain("An error has occured");

		assertThat(svg)
				.as("font-weight='900' must appear in SVG output")
				.contains("font-weight=\"900\"");

		assertThat(svg)
				.as("font-style='italic' must appear in SVG output")
				.contains("font-style=\"italic\"");

		assertThat(svg)
				.as("Legacy 'bold' keyword must never be emitted (Phase 5 guarantee)")
				.doesNotContain("font-weight=\"bold\"");
	}

	@Test
	@DisplayName("Integration: FontWeight:900 participant — weight matches getFontFace().getCssWeight() in unit path")
	void participantStyleFontWeight900UnitAndIntegrationAgree() throws IOException {
		// Unit path: build the same style as the PUML would produce
		final Style participantStyle = style(
				PName.FontWeight, "900",
				PName.FontStyle, "italic",
				PName.FontSize, "26");

		final UFontFace face = participantStyle.getUFont().getFontFace();
		assertThat(face.getCssWeight()).isEqualTo(900);
		assertThat(face.isItalic()).isTrue();

		// Integration path agrees: SVG carries the same weight
		final String svg = PlantUmlTestUtils.exportDiagram(PARTICIPANT_STYLE_PUML)
				.asString(FileFormat.SVG);
		assertThat(svg).contains("font-weight=\"900\"");
	}
}
