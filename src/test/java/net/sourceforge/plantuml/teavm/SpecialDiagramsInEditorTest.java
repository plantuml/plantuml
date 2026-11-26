package net.sourceforge.plantuml.teavm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.eggs.PSystemColors;
import net.sourceforge.plantuml.klimt.sprite.ListSpriteDiagram;
import net.sourceforge.plantuml.teavm.c.PSystemBuilderC;
import net.sourceforge.plantuml.teavm.js.PSystemBuilderJs;

/**
 * The browser (JavaScript) and native (C) editions have their own list of
 * diagram factories: <code>colors</code> and <code>listsprite(s)</code> must be
 * in both of them, as in the JVM edition.
 */
class SpecialDiagramsInEditorTest {

	private static void check(String body, Class<? extends Diagram> expected) {
		final String source = "@startuml\n" + body + "\n@enduml";
		final Diagram js = PSystemBuilderJs.getInstance().createDiagram(source.split("\n", -1));
		assertTrue(expected.isInstance(js), "JS: " + js.getDescription());
		final Diagram c = PSystemBuilderC.getInstance().createDiagram(source.split("\n", -1));
		assertTrue(expected.isInstance(c), "C: " + c.getDescription());
	}

	@ParameterizedTest
	@ValueSource(strings = { "colors", "color", "colors Red", "colors #00FF00" })
	void colors(String body) {
		check(body, PSystemColors.class);
	}

	@ParameterizedTest
	@ValueSource(strings = { "listsprite", "listsprites" })
	void listSprites(String body) {
		check("sprite $foo [4x4/16] {\n0000\nFFFF\n0000\nFFFF\n}\n" + body, ListSpriteDiagram.class);
	}

}
