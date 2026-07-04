package net.sourceforge.plantuml.classdiagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
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

	/**
	 * Checking only {@link Diagram#getDescription()} is not enough: a promoted
	 * phantom-group entity used to keep the {@code Bodier} built by
	 * {@code CucaDiagram.createGroup} (never bound to a leaf via
	 * {@code Bodier.setLeaf}, and unable to render fields/methods), which made
	 * the description succeed while actually rendering the image crashed with a
	 * NullPointerException in BodyEnhanced1. These tests render real images to
	 * guard against that class of bug.
	 */
	@Test
	void testRenderingImageDoesNotCrash() throws IOException {
		assertRendersWithoutCrash("@startuml", "set separator .", "class a.b", "class a", "@enduml");
	}

	@Test
	void testRenderingImageDoesNotCrashWhenRelationPointsAtPhantomGroupFirst() throws IOException {
		assertRendersWithoutCrash("@startuml", "set separator .", "class a.b", "X --> a", "class a", "@enduml");
	}

	@Test
	void testRenderingImageDoesNotCrashWithMultiplePhantomChildren() throws IOException {
		assertRendersWithoutCrash("@startuml", "set separator .", "class a.b", "class a.c", "class a", "@enduml");
	}

	@Test
	void testRenderingImageDoesNotCrashWhenPromotedClassHasBodyContent() throws IOException {
		assertRendersWithoutCrash("@startuml", "set separator .", "class a.b", "class a {", "+field1",
				"+method1()", "}", "@enduml");
	}

	/**
	 * Reported by @arnaudroques on the #2099 PR: rendering succeeds and does not
	 * crash, but the nested class "b" is completely missing from the image -
	 * only "a" is drawn. "b" still exists as a genuine leaf Entity (see
	 * {@link #testClassSameNameAsImplicitNamespaceIsAccepted}), so this is a
	 * rendering/layout gap, not a data-model gap: GraphvizImageBuilder only
	 * finds entities to draw via two paths - inside a rendered GROUP, or as a
	 * top-level entity whose parent container is the diagram root - and "b"
	 * (parent container "a", which is no longer a GROUP) falls into neither.
	 */
	@Test
	void testRenderingImageStillShowsNestedClassAfterPromotion() throws IOException {
		final String svg = renderToSvg("@startuml", "set separator .", "class a.b", "class a", "@enduml");
		assertTrue(containsLabel(svg, "a"), "Rendered image should show class 'a':\n" + svg);
		assertTrue(containsLabel(svg, "b"), "Rendered image should still show nested class 'b':\n" + svg);
	}

	@Test
	void testRenderingImageStillShowsAllPhantomChildrenAfterPromotion() throws IOException {
		final String svg = renderToSvg("@startuml", "set separator .", "class a.b", "class a.c", "class a",
				"@enduml");
		assertTrue(containsLabel(svg, "a"), "Rendered image should show class 'a':\n" + svg);
		assertTrue(containsLabel(svg, "b"), "Rendered image should still show nested class 'b':\n" + svg);
		assertTrue(containsLabel(svg, "c"), "Rendered image should still show nested class 'c':\n" + svg);
	}

	private void assertRendersWithoutCrash(String... lines) throws IOException {
		final String svg = renderToSvg(lines);
		assertFalse(svg.contains("has crashed"), "Rendering should not crash:\n" + svg);
		assertFalse(svg.contains("An error has occurred"), "Rendering should not report an error:\n" + svg);
	}

	private String renderToSvg(String... lines) throws IOException {
		final StringBuilder source = new StringBuilder();
		for (String line : lines)
			source.append(line).append('\n');

		final SourceStringReader reader = new SourceStringReader(source.toString());
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		reader.outputImage(baos, new FileFormatOption(FileFormat.SVG));

		return new String(baos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
	}

	private boolean containsLabel(String svg, String label) {
		return svg.contains(">" + label + "<");
	}

}
