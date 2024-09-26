package test.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.api.v2.DiagramReturn;
import net.sourceforge.plantuml.api.v2.DiagramUtils;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.klimt.creole.Display;

class ApiV2CrashTest {

	// pdiff jpgte
	// Won't run on Github
	// @Test
	public void testSimple() throws IOException {
		final DiagramReturn result = DiagramUtils.exportDiagram("@startuml", "state \"Some State\" as A_State {",
				"state \"Task\" as Task1", "||", "state \"Task\" as Task2", "state ExitSuccessful <<exitPoint>>",
				"state ExitFailure <<exitPoint>>", "}", "@enduml");

		final Diagram diagram = result.getDiagram();
		assertNull(result.error());
		assertNotNull(diagram);
		assertEquals(DiagramType.UML, diagram.getSource().getDiagramType());
		assertEquals("StateDiagram", diagram.getClass().getSimpleName());
		assertEquals("(4 entities)", diagram.getDescription().getDescription());
		assertEquals(1, diagram.getNbImages());
		assertTrue(Display.isNull(diagram.getTitleDisplay()));
		assertEquals(0, diagram.getTitleDisplay().asList().size());
		assertEquals(9, diagram.getSource().getTotalLineCount());
		assertFalse(result.getErrorLine().isPresent());

		// result.asImage();
		final Throwable rootCause = result.getRootCause();
		assertNotNull(rootCause);
		assertTrue(rootCause.getMessage().startsWith("java.lang.StringIndexOutOfBoundsException"));
		rootCause.printStackTrace();
	}

}
