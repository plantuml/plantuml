package test.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.api.v2.DiagramReturn;
import net.sourceforge.plantuml.api.v2.DiagramUtils;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.klimt.creole.Display;

class ApiV2CsvTest {

	@ParameterizedTest(name = "[{index}] ")
	@CsvSource({
		// diagSource,                                                             error, diagramType, className,     description, nbImages, totalLineCount, errorLine, nbTitle, title
		" '@startuml\nERROR\n@enduml',                                             Syntax Error? (Assumed diagram type: sequence), SEQUENCE, PSystemErrorV2,           (Error), 1, 3, 1, 0, ",
		" '@startuml\nstart\n:toto;<<#zzblue>>\n@enduml', No such color (Assumed diagram type: activity), SEQUENCE, PSystemErrorV2,           (Error), 1, 4, 2, 0, ",
		" '@startuml\nalice->bob:hello\n@enduml',                                               , SEQUENCE, SequenceDiagram, (2 participants), 1, 3,  , 0, ",
		" '@startuml\ntitle: this is the title\nalice->bob:hello\n@enduml',                     , SEQUENCE, SequenceDiagram, (2 participants), 1, 4,  , 1, this is the title ",
		" '@startuml\ntitle this is the title\nalice->bob:hello\ntitle another title\n@enduml', , SEQUENCE, SequenceDiagram, (2 participants), 1, 5,  , 1, another title ",
		" '@startmindmap\n* root\n@endmindmap',                                                 , MINDMAP, MindMapDiagram,       MindMap, 1, 3,  , 0, ",
		" '@startwbs\n* root\n@endwbs',                                                         , WBS, WBSDiagram, Work Breakdown Structure, 1, 3,  , 0, ",
		// TBC...
	})
	public void test_exportDiagram(String diagSource, String error, String diagramType, String className, String description,
					 Integer nbImages, Integer totalLineCount, Integer errorLine, Integer nbTitle, String title) throws IOException {
		final DiagramReturn result = DiagramUtils.exportDiagram(diagSource);
		final Diagram diagram = result.getDiagram();

		if (StringUtils.isNotEmpty(error)) {
			assertEquals(error, result.error());
			assertTrue(result.getErrorLine().isPresent());
			assertEquals(errorLine, result.getErrorLine().get());
		}
		else {
			assertNull(result.error());
			assertFalse(result.getErrorLine().isPresent());
 		}

		assertNotNull(diagram);
		assertTrue(diagram.getSource().getDiagramTypes().contains(DiagramType.valueOf(diagramType)));
		assertEquals(className, diagram.getClass().getSimpleName());
		assertEquals(description, diagram.getDescription().getDescription());
		assertEquals(nbImages, diagram.getNbImages());
		assertEquals(totalLineCount, diagram.getSource().getTotalLineCount());

		assertEquals(nbTitle, diagram.getTitleDisplay().asList().size());
		if (nbTitle > 0) {
			assertEquals(title, diagram.getTitleDisplay().get(0));
		}
		else {
			assertTrue(Display.isNull(diagram.getTitleDisplay()));
		}
	}
}
