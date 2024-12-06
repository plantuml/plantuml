package test.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.api.v2.DiagramReturn;
import net.sourceforge.plantuml.api.v2.DiagramUtils;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.klimt.creole.Display;

class ApiV2Test {

	@Test
	public void testEmpty() throws IOException {
		final DiagramReturn result = DiagramUtils.exportDiagram("empty");
		assertNull(result.getDiagram());
		assertEquals("No @start/@end found", result.error());
	}

	@Test
	public void testSimple() throws IOException {
		final DiagramReturn result = DiagramUtils.exportDiagram("@startuml", "alice->bob:hello", "@enduml");
		final Diagram diagram = result.getDiagram();
		assertNull(result.error());
		assertNotNull(diagram);
		assertEquals(DiagramType.UML, diagram.getSource().getDiagramType());
		assertEquals("SequenceDiagram", diagram.getClass().getSimpleName());
		assertEquals("(2 participants)", diagram.getDescription().getDescription());
		assertEquals(1, diagram.getNbImages());
		assertTrue(Display.isNull(diagram.getTitleDisplay()));
		assertEquals(0, diagram.getTitleDisplay().asList().size());
		assertEquals(3, diagram.getSource().getTotalLineCount());
		assertFalse(result.getErrorLine().isPresent());
	}

	@Test
	public void testSimpleTitle() throws IOException {
		final DiagramReturn result = DiagramUtils.exportDiagram("@startuml", "title: this is the title",
				"alice->bob:hello", "@enduml");
		final Diagram diagram = result.getDiagram();
		assertNull(result.error());
		assertNotNull(diagram);
		assertEquals(DiagramType.UML, diagram.getSource().getDiagramType());
		assertEquals("SequenceDiagram", diagram.getClass().getSimpleName());
		assertEquals("(2 participants)", diagram.getDescription().getDescription());
		assertEquals(1, diagram.getNbImages());
		assertEquals(1, diagram.getTitleDisplay().asList().size());
		assertEquals("this is the title", diagram.getTitleDisplay().get(0));
		assertEquals(4, diagram.getSource().getTotalLineCount());
		assertFalse(result.getErrorLine().isPresent());
	}

	@Test
	public void testError() throws IOException {
		final DiagramReturn result = DiagramUtils.exportDiagram("@startuml", "ERROR", "@enduml");
		final Diagram diagram = result.getDiagram();
		assertEquals("Syntax Error?", result.error());
		assertNotNull(diagram);
		assertEquals(DiagramType.UML, diagram.getSource().getDiagramType());
		assertEquals("PSystemErrorV2", diagram.getClass().getSimpleName());
		assertEquals("(Error)", diagram.getDescription().getDescription());
		assertEquals(1, diagram.getNbImages());
		assertTrue(Display.isNull(diagram.getTitleDisplay()));
		assertEquals(0, diagram.getTitleDisplay().asList().size());
		assertEquals(3, diagram.getSource().getTotalLineCount());
		assertEquals(1, result.getErrorLine().get());
	}

	@Test
	public void testError2() throws IOException {
		final DiagramReturn result = DiagramUtils.exportDiagram("@startuml", "start", "#zzblue:toto;", "@enduml");
		final Diagram diagram = result.getDiagram();
		assertEquals("No such color (Assumed diagram type: activity)", result.error());
		assertNotNull(diagram);
		assertEquals(DiagramType.UML, diagram.getSource().getDiagramType());
		assertEquals("PSystemErrorV2", diagram.getClass().getSimpleName());
		assertEquals("(Error)", diagram.getDescription().getDescription());
		assertEquals(1, diagram.getNbImages());
		assertTrue(Display.isNull(diagram.getTitleDisplay()));
		assertEquals(0, diagram.getTitleDisplay().asList().size());
		assertEquals(4, diagram.getSource().getTotalLineCount());
		assertEquals(2, result.getErrorLine().get());
	}

//        System.out.println(d.getSource().getPlainString("\n"));
//        System.out.println("Id = "    + d.getSource().getId());

}
