package net.sourceforge.plantuml.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
 *     {@code FontWeight}), weight defaults to 700 (legacy behavior).</li>
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
		return new Style(StyleSignature.empty(), map);
	}

	// -----------------------------------------------------------------------
	// Unit tests — Style.getUFont()
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("FontWeight: 900 alone sets CSS weight 900 on the UFont face")
	void fontWeight900AloneSetsWeight() {
		final Style s = style(PName.FontWeight, "900");
		final UFontFace face = s.getUFont().getFontFace();
		assertEquals(900, face.getCssWeight());
	}

	@Test
	@DisplayName("FontWeight: 300 sets CSS weight 300 (Light)")
	void fontWeight300SetsWeightLight() {
		final Style s = style(PName.FontWeight, "300");
		assertEquals(300, s.getUFont().getFontFace().getCssWeight());
	}

	@Test
	@DisplayName("FontWeight: bold keyword produces weight 700")
	void fontWeightBoldKeywordProduces700() {
		final Style s = style(PName.FontWeight, "bold");
		assertEquals(700, s.getUFont().getFontFace().getCssWeight());
	}

	@Test
	@DisplayName("FontWeight: 900 + FontStyle: italic — both axes preserved independently")
	void fontWeight900AndItalicAreBothPreserved() {
		final Style s = style(PName.FontWeight, "900", PName.FontStyle, "italic");
		final UFontFace face = s.getUFont().getFontFace();
		assertEquals(900, face.getCssWeight());
		assertTrue(face.isItalic());
	}

	@Test
	@DisplayName("FontWeight: 500 + FontStyle: italic — medium-weight italic")
	void fontWeight500AndItalic() {
		final Style s = style(PName.FontWeight, "500", PName.FontStyle, "italic");
		final UFontFace face = s.getUFont().getFontFace();
		assertEquals(500, face.getCssWeight());
		assertTrue(face.isItalic());
	}

	@Test
	@DisplayName("FontWeight: 900 overrides the weight that FontStyle: bold would set (700)")
	void fontWeight900OverridesBoldFromFontStyle() {
		// FontStyle=bold would give 700; explicit FontWeight=900 must win
		final Style s = style(PName.FontWeight, "900", PName.FontStyle, "bold");
		assertEquals(900, s.getUFont().getFontFace().getCssWeight());
	}

	@Test
	@DisplayName("No FontWeight: FontStyle: bold still defaults to weight 700 (legacy behavior)")
	void noFontWeightBoldStyleDefaultsTo700() {
		final Style s = style(PName.FontStyle, "bold");
		assertEquals(700, s.getUFont().getFontFace().getCssWeight());
	}

	@Test
	@DisplayName("No FontWeight, no FontStyle: weight defaults to 400 (normal)")
	void noFontWeightNoFontStyleDefaultsTo400() {
		final Style s = style(PName.FontSize, "14"); // some unrelated property
		assertEquals(400, s.getUFont().getFontFace().getCssWeight());
	}

	@Test
	@DisplayName("FontWeight: 900 — toCssWeightString() returns '900'")
	void fontWeight900CssWeightString() {
		final Style s = style(PName.FontWeight, "900");
		assertEquals("900", s.getUFont().getFontFace().toCssWeightString());
	}

	@Test
	@DisplayName("FontWeight: 900, FontSize: 26 — size is honoured independently")
	void fontSizeIsIndependentOfFontWeight() {
		final Style s = style(PName.FontWeight, "900", PName.FontSize, "26");
		assertEquals(26, s.getUFont().getSize());
		assertEquals(900, s.getUFont().getFontFace().getCssWeight());
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

		assertFalse(svg.contains("An error has occurred"));

		assertTrue(svg.contains("font-weight=\"900\""));

		assertTrue(svg.contains("font-style=\"italic\""));

		assertFalse(svg.contains("font-weight=\"bold\""));
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
		assertEquals(900, face.getCssWeight());
		assertTrue(face.isItalic());

		// Integration path agrees: SVG carries the same weight
		final String svg = PlantUmlTestUtils.exportDiagram(PARTICIPANT_STYLE_PUML)
				.asString(FileFormat.SVG);
		assertTrue(svg.contains("font-weight=\"900\""));
	}
}
