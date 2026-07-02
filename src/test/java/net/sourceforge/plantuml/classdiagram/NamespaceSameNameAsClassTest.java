package net.sourceforge.plantuml.classdiagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.api.v2.DiagramReturn;
import net.sourceforge.plantuml.api.v2.DiagramUtils;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.plasma.Quark;

/**
 * Non-regression test for https://github.com/plantuml/plantuml/issues/2099
 *
 * <p>
 * A dotted class name such as <code>class a.b</code> implicitly creates a
 * "phantom" group/namespace named <code>a</code>. A later, unrelated
 * top-level <code>class a</code> should still be accepted as a plain class,
 * not rejected because a phantom group already claimed that name.
 */
class NamespaceSameNameAsClassTest {

	@Test
	void testClassSameNameAsImplicitNamespaceIsAccepted() throws IOException {
		final DiagramReturn result = DiagramUtils.exportDiagram("@startuml", "set separator .", "class a.b",
				"class a", "@enduml");

		assertNotNull(result.getDiagram(), "Diagram should have been built");

		final Diagram diagram = result.getDiagram();
		assertEquals("ClassDiagram", diagram.getClass().getSimpleName());
		assertFalse(diagram.getDescription().getDescription().contains("(Error)"),
				"Diagram description should not report an error: " + diagram.getDescription().getDescription());

		final ClassDiagram classDiagram = (ClassDiagram) diagram;

		// "a" must exist as a genuine top-level leaf/class, not just a group.
		final Quark<Entity> quarkA = classDiagram.firstWithName("a");
		assertNotNull(quarkA, "Quark 'a' should exist");
		final Entity leafA = quarkA.getData();
		assertNotNull(leafA, "Top-level class 'a' should exist as a leaf entity");
		assertFalse(leafA.isGroup(), "'a' should be a class (leaf), not a group/namespace");

		// "a.b" should still be reachable as a class nested inside namespace "a".
		final Quark<Entity> quarkB = quarkA.childIfExists("b");
		assertNotNull(quarkB, "Quark 'a.b' should exist");
		final Entity leafB = quarkB.getData();
		assertNotNull(leafB, "Nested class 'a.b' should still exist");
		assertFalse(leafB.isGroup(), "'a.b' should be a class (leaf), not a group/namespace");
	}

}
