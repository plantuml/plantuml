package net.sourceforge.plantuml.elk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.klimt.shape.TextBlock;

/**
 * Regression test for issue #2277: NPE when rendering diagrams with ports using ELK layouter.
 *
 * <p>These tests call {@link CucaDiagramFileMakerElk#getTextBlock} directly so that
 * a {@link NullPointerException} from {@code EntityImagePort.upPosition()} propagates
 * without being swallowed by the higher-level error handling in PlantUML's export pipeline.</p>
 */
class ElkPortTest {

	@Test
	void testPortWithElkDoesNotThrowNPE() {
		final String diagram = "@startuml\n" +
				"!pragma layout elk\n" +
				"component C {\n" +
				"    port A\n" +
				"}\n" +
				"@enduml";

		assertDoesNotThrow(() -> renderWithElk(diagram));
	}

	@Test
	void testPortInPortOutWithElkDoesNotThrowNPE() {
		final String diagram = "@startuml\n" +
				"!pragma layout elk\n" +
				"node \"Main\" {\n" +
				"\tportin Rest.Thing\n" +
				"\tportout Rest.Input\n" +
				"\tcomponent Core {\n" +
				"\t\tport Thing\n" +
				"\t}\n" +
				"}\n" +
				"@enduml";

		assertDoesNotThrow(() -> renderWithElk(diagram));
	}

	@Test
	void testFullDiagramFromIssue2277() {
		final String diagram = "@startuml\n" +
				"!pragma layout elk\n" +
				"\n" +
				"node \"Some Server\"\n" +
				"\n" +
				"node \"Main\"{\n" +
				"\tportin Rest.Thing\n" +
				"\tportout Rest.Input\n" +
				"\t\n" +
				"\tcomponent Supporting {\n" +
				"\t\tportin ReadonlyThing\n" +
				"\t\tportout SpecialThing\n" +
				"\t}\n" +
				"\t\n" +
				"\tinterface IThing\n" +
				"\t\n" +
				"\tcomponent Core {\n" +
				"\t\tport Thing\n" +
				"\t}\n" +
				"}\n" +
				"\n" +
				"[Some Server] -l-> Rest.Thing\n" +
				"Rest.Thing -r-> ReadonlyThing\n" +
				"[Some Server] <-r- Rest.Input\n" +
				"SpecialThing -r-> Rest.Input\n" +
				"SpecialThing -r-> IThing\n" +
				"ReadonlyThing --> IThing\n" +
				"IThing <-right- Thing\n" +
				"Core -- IThing\n" +
				"@enduml";

		assertDoesNotThrow(() -> renderWithElk(diagram));
	}

	/**
	 * Parses the diagram source and invokes the ELK layout directly via
	 * {@link CucaDiagramFileMakerElk#getTextBlock}, bypassing the higher-level
	 * try/catch in the export pipeline. Any exception (e.g. NPE) propagates to the caller.
	 */
	private TextBlock renderWithElk(String source) throws Exception {
		final SourceStringReader ssr = new SourceStringReader(source);
		final List<BlockUml> blocks = ssr.getBlocks();
		final Diagram diagram = blocks.get(0).getDiagram();
		final CucaDiagram cucaDiagram = (CucaDiagram) diagram;

		final CucaDiagramFileMakerElk maker = new CucaDiagramFileMakerElk(cucaDiagram);
		final FileFormatOption fileFormatOption = new FileFormatOption(FileFormat.SVG);
		return maker.getTextBlock(Collections.emptyList(), fileFormatOption);
	}
}
