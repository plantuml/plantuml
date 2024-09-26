package test.test;

import static test.utils.PlantUmlTestUtils.exportDiagram;
import static org.assertj.core.api.Assertions.assertThat;

import net.sourceforge.plantuml.core.Diagram;
import test.utils.PlantUmlTestUtils.ExportDiagram;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PlantUmlTestUtilsTest {

	@ParameterizedTest(name = "[{index}] ")
	@CsvSource({
		" '@startuml\ntitle that is the title\n[a]\n@enduml' ",
        " '@startuml\ntitle a seq title\na -> b\nnewpage\nc -> d\n@enduml' ",
		" '@startebnf\ntitle ebnf-Title\na=a*;\n@endebnf' ",
		" '@startmindmap\n* root\n@endmindmap' ",
        " '@startdef(id=macro_def_id)\nA -> B : hello1\n@enddef' ",
	})
	public void test_exportDiagram(String diagSource) {
		final ExportDiagram exportDiag = exportDiagram(diagSource);
		Diagram d = exportDiag.getDiagram();

        System.out.println("<<<\n");
        System.out.println(d.getSource().getPlainString("\n"));
        System.out.println("===>");
        System.out.println("class = " + d.getClass().getName());
        System.out.println("desc. = " + d.getDescription());
        System.out.println("title = " + d.getTitleDisplay());
        System.out.println("NbImg = " + d.getNbImages());
        System.out.println("Type  = " + d.getSource().getDiagramType());
        System.out.println("Title = " + d.getSource().getTitle());
        System.out.println("NbLine = "+ d.getSource().getTotalLineCount());
        System.out.println("Id = "    + d.getSource().getId());
        System.out.println("...");
        System.out.println(">>>");

    }
}
