package net.sourceforge.plantuml.sequencediagram;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

class SequenceStereotypeVisibilityTest {

	@Test
	void hideOneStereotypeLeavesTheOthers() throws IOException {
		final String svg = render("@startuml\n" + //
				"hide <<hidden>> stereotype\n" + //
				"participant Foo <<hidden>>\n" + //
				"participant Bar <<not_hidden>>\n" + //
				"@enduml\n");

		assertFalse(hasStereotype(svg, "hidden"));
		assertTrue(hasStereotype(svg, "not_hidden"));
	}

	@Test
	void showOneStereotypeAfterHidingAll() throws IOException {
		final String svg = render("@startuml\n" + //
				"hide stereotype\n" + //
				"show <<not_hidden>> stereotype\n" + //
				"participant Foo <<hidden>>\n" + //
				"participant Bar <<not_hidden>>\n" + //
				"@enduml\n");

		assertFalse(hasStereotype(svg, "hidden"));
		assertTrue(hasStereotype(svg, "not_hidden"));
	}

	@Test
	void hideStereotypeStillHidesEveryStereotype() throws IOException {
		final String svg = render("@startuml\n" + //
				"hide stereotype\n" + //
				"participant Foo <<hidden>>\n" + //
				"participant Bar <<not_hidden>>\n" + //
				"@enduml\n");

		assertFalse(hasStereotype(svg, "hidden"));
		assertFalse(hasStereotype(svg, "not_hidden"));
		assertTrue(svg.contains(">Foo<") || svg.contains(">Foo</text>"));
	}

	@Test
	void hideStereotypeOfOneParticipant() throws IOException {
		final String svg = render("@startuml\n" + //
				"hide Foo stereotype\n" + //
				"participant Foo <<alpha>>\n" + //
				"participant Bar <<beta>>\n" + //
				"@enduml\n");

		assertFalse(hasStereotype(svg, "alpha"));
		assertTrue(hasStereotype(svg, "beta"));
	}

	@Test
	void classDiagramStereotypeFilterIsUnchanged() throws IOException {
		final String svg = render("@startuml\n" + //
				"hide <<focus>> stereotype\n" + //
				"class Access <<Entity>> <<focus>>\n" + //
				"@enduml\n");

		assertTrue(hasStereotype(svg, "Entity"));
		assertFalse(hasStereotype(svg, "focus"));
	}

	private static boolean hasStereotype(String svg, String name) {
		return Pattern.compile("(?<![A-Za-z0-9_])" + Pattern.quote(name) + "(?![A-Za-z0-9_])").matcher(svg).find();
	}

	private static String render(String source) throws IOException {
		final SourceStringReader reader = new SourceStringReader(source);
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		reader.outputImage(output, new FileFormatOption(FileFormat.SVG));
		return output.toString(StandardCharsets.UTF_8);
	}

}
