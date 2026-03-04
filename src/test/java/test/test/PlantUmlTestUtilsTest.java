package test.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static test.utils.PlantUmlTestUtils.exportDiagram;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import test.utils.PlantUmlTestUtils.ExportDiagram;

class PlantUmlTestUtilsTest {

	
    @Test
    void testDef() {
        String src = makeSource(
            "@startdef(id=macro_def_id)", //
            "A -> B : hello1", //
            "@enddef" //
        );
        ExportDiagram exportDiag = exportDiagram(src);
        Diagram d = exportDiag.getDiagram();
        assertEquals("net.sourceforge.plantuml.definition.PSystemDefinition", d.getClass().getName());
        assertEquals("(Definition)", d.getDescription().toString());
        assertEquals("NULL", d.getTitleDisplay().toString());
        assertEquals(1, d.getNbImages());
        assertEquals(DiagramType.DEFINITION, d.getSource().getDiagramType());
        assertEquals("[]", d.getSource().getTitle().toString());
        assertEquals(3, d.getSource().getTotalLineCount());
        assertEquals("macro_def_id", d.getSource().getId());
    }

    @Test
    void testEbnf() {
        String src = makeSource(
            "@startebnf", //
            "title ebnf-Title", //
            "a=a*;", //
            "@endebnf" //
        );
        ExportDiagram exportDiag = exportDiagram(src);
        Diagram d = exportDiag.getDiagram();
        assertEquals("net.sourceforge.plantuml.ebnf.PSystemEbnf", d.getClass().getName());
        assertEquals("(EBNF)", d.getDescription().toString());
        assertEquals("[ebnf-Title]", d.getTitleDisplay().toString());
        assertEquals(1, d.getNbImages());
        assertEquals(DiagramType.EBNF, d.getSource().getDiagramType());
        assertEquals("[ebnf-Title]", d.getSource().getTitle().toString());
        assertEquals(4, d.getSource().getTotalLineCount());
        assertEquals(null, d.getSource().getId());
    }




    @Test
    void testUmlWithNewpage() {
        String src = makeSource( //
            "@startuml", //
            "title a seq title", //
            "a -> b", //
            "newpage", //
            "c -> d", //
            "@enduml" //
        );
        ExportDiagram exportDiag = exportDiagram(src);
        Diagram d = exportDiag.getDiagram();
        assertEquals("net.sourceforge.plantuml.sequencediagram.SequenceDiagram", d.getClass().getName());
        assertEquals("(4 participants)", d.getDescription().toString());
        assertEquals("[a seq title]", d.getTitleDisplay().toString());
        assertEquals(2, d.getNbImages());
        assertEquals(DiagramType.UML, d.getSource().getDiagramType());
        assertEquals("[a seq title]", d.getSource().getTitle().toString());
        assertEquals(6, d.getSource().getTotalLineCount());
        assertEquals(null, d.getSource().getId());
    }


    @Test
    void testUmlWithTitle() {
        String src = makeSource( //
            "@startuml", //
            "title that is the title", //
            "[a]", //
            "@enduml" //
        );
        ExportDiagram exportDiag = exportDiagram(src);
        Diagram d = exportDiag.getDiagram();
        assertEquals("net.sourceforge.plantuml.descdiagram.DescriptionDiagram", d.getClass().getName());
        assertEquals("(1 entities)", d.getDescription().toString());
        assertEquals("[that is the title]", d.getTitleDisplay().toString());
        assertEquals(1, d.getNbImages());
        assertEquals(DiagramType.UML, d.getSource().getDiagramType());
        assertEquals("[that is the title]", d.getSource().getTitle().toString());
        assertEquals(4, d.getSource().getTotalLineCount());
        assertEquals(null, d.getSource().getId());
    }


    @Test
    void testMindmap() {
        String src = makeSource(
            "@startmindmap", //
            "* root", //
            "@endmindmap" //
        );
        ExportDiagram exportDiag = exportDiagram(src);
        Diagram d = exportDiag.getDiagram();
        assertEquals("net.sourceforge.plantuml.mindmap.MindMapDiagram", d.getClass().getName());
        assertEquals("MindMap", d.getDescription().toString());
        assertEquals("NULL", d.getTitleDisplay().toString());
        assertEquals(1, d.getNbImages());
        assertEquals(DiagramType.MINDMAP, d.getSource().getDiagramType());
        assertEquals("[]", d.getSource().getTitle().toString());
        assertEquals(3, d.getSource().getTotalLineCount());
        assertEquals(null, d.getSource().getId());
    }


    private String makeSource(String... lines) {
        return String.join("\n", lines);
    }

	
}
