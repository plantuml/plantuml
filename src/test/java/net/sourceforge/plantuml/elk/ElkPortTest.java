package net.sourceforge.plantuml.elk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
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
 *
 * <p>ELK itself ({@code org.eclipse.elk.*}) is an optional runtime dependency: PlantUML compiles
 * against its own reflective proxy ({@link net.sourceforge.plantuml.elk.proxy}) so the project
 * builds without ELK on the classpath (see {@link CucaDiagramFileMakerElk}'s header comment), and
 * only needs the real ELK jars present when a diagram actually asks for {@code !pragma layout elk}.
 * On a build that does not pull it in (e.g. an Ant/non-Maven build), these tests are skipped rather
 * than failed, since a {@code ClassNotFoundException} there would not be this test's regression.</p>
 */
class ElkPortTest {

	@BeforeEach
	void requireElkOnClasspath() {
		Assumptions.assumeTrue(isElkAvailable(),
				"ELK (org.eclipse.elk.graph.util.ElkGraphUtil) is not on the classpath - skipping ELK-dependent test");
	}

	private static boolean isElkAvailable() {
		try {
			Class.forName("org.eclipse.elk.graph.util.ElkGraphUtil");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

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
