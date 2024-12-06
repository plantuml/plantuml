package test.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.api.v2.DiagramReturn;
import net.sourceforge.plantuml.api.v2.DiagramUtils;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.StringUtils;

class ApiV2CsvTest {

	@ParameterizedTest(name = "[{index}] ")
	@CsvSource({
		// diagSource,                                                             error, diagramType, className,     description, nbImages, totalLineCount, errorLine, nbTitle, title
		" '@startuml\nERROR\n@enduml',                                             Syntax Error?, UML, PSystemErrorV2,           (Error), 1, 3, 1, 0, ",
		" '@startuml\nstart\n#zzblue:toto;\n@enduml', No such color (Assumed diagram type: activity), UML, PSystemErrorV2,           (Error), 1, 4, 2, 0, ",
		" '@startuml\nalice->bob:hello\n@enduml',                                               , UML, SequenceDiagram, (2 participants), 1, 3,  , 0, ",
		" '@startuml\ntitle: this is the title\nalice->bob:hello\n@enduml',                     , UML, SequenceDiagram, (2 participants), 1, 4,  , 1, this is the title ",
		" '@startuml\ntitle this is the title\nalice->bob:hello\ntitle another title\n@enduml', , UML, SequenceDiagram, (2 participants), 1, 5,  , 1, another title ",
		" '@startmindmap\n* root\n@endmindmap',                                                 , MINDMAP, MindMapDiagram,       MindMap, 1, 3,  , 0, ",
		" '@startwbs\n* root\n@endwbs',                                                         , WBS, WBSDiagram, Work Breakdown Structure, 1, 3,  , 0, ",
		// TBC...
	})
	public void test_exportDiagram(String diagSource, String error, String diagramType, String className, String description,
						 Integer nbImages, Integer totalLineCount, Integer errorLine, Integer nbTitle, String title) throws IOException {
		final DiagramReturn result = DiagramUtils.exportDiagram(diagSource);
		final Diagram diagram = result.getDiagram();

		if (StringUtils.isNotEmpty(error)) {
			assertThat(result.error()).isEqualTo(error);
			assertThat(result.getErrorLine().isPresent()).isTrue();
			assertThat(result.getErrorLine().get()).isEqualTo(errorLine);
		}
		else {
			assertThat(result.error()).isNull();
			assertThat(result.getErrorLine().isPresent()).isFalse();
 		}

		assertThat(diagram).isNotNull();
		assertThat(diagram.getSource().getDiagramType()).isEqualTo(DiagramType.valueOf(diagramType));
		assertThat(diagram.getClass().getSimpleName()).isEqualTo(className);
		assertThat(diagram.getDescription().getDescription()).isEqualTo(description);
		assertThat(diagram.getNbImages()).isEqualTo(nbImages);
		assertThat(diagram.getSource().getTotalLineCount()).isEqualTo(totalLineCount);

		assertThat(diagram.getTitleDisplay().asList().size()).isEqualTo(nbTitle);
		if (nbTitle > 0) {
			assertThat(diagram.getTitleDisplay().get(0)).isEqualTo(title);
		}
		else {
			assertThat(Display.isNull(diagram.getTitleDisplay())).isTrue();
		}
	}
}
