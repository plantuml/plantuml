package net.sourceforge.plantuml.teavm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.teavm.c.PSystemBuilderC;
import net.sourceforge.plantuml.teavm.js.PSystemBuilderJs;

/**
 * The browser (JavaScript) and native (C) editions build a diagram from the text
 * of the editor, without going through <code>BlockUml</code>: the YAML header
 * must be removed from the text, and given to the preprocessor for
 * <code>puml.metadata</code>, there too.
 */
class YamlHeaderInEditorTest {

	private static final String SOURCE = "@startchart\n" //
			+ "---\n" //
			+ "title: Pets adopted by volunteers\n" //
			+ "pets:\n" //
			+ "  Dogs: 386\n" //
			+ "  Cats: 85\n" //
			+ "---\n" //
			+ "title !{puml.metadata[\"title\"]}\n" //
			+ "pie [!{\", \".join(f\"{k}: {v}\" for k, v in puml.metadata[\"pets\"].items())}]\n" //
			+ "@endchart";

	private static void check(Diagram diagram) {
		assertFalse(diagram instanceof PSystemError, diagram.getDescription().toString());
		assertEquals("Chart Diagram", diagram.getDescription().getDescription());
		assertEquals("Pets adopted by volunteers", ((TitledDiagram) diagram).getTitle().getDisplay().get(0).toString());
	}

	@Test
	void javaScriptEdition() {
		check(PSystemBuilderJs.getInstance().createDiagram(SOURCE.split("\n", -1)));
	}

	@Test
	void nativeEdition() {
		check(PSystemBuilderC.getInstance().createDiagram(SOURCE.split("\n", -1)));
	}

	@Test
	void headerWithoutPythonIsJustRemoved() {
		final String source = "@startuml\n---\nname: foo\n---\nAlice -> Bob : hello\n@enduml";
		assertTrue(PSystemBuilderJs.getInstance().createDiagram(source.split("\n", -1)) instanceof TitledDiagram);
		assertTrue(PSystemBuilderC.getInstance().createDiagram(source.split("\n", -1)) instanceof TitledDiagram);
	}
}
