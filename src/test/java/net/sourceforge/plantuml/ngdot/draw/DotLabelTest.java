package net.sourceforge.plantuml.ngdot.draw;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.ngdot.model.DotAttributes;
import net.sourceforge.plantuml.ngdot.model.DotValue;

class DotLabelTest {

	@Test
	void substitute() {
		assertEquals("node a of G", DotLabel.substitute("node \\N of \\G", "a", "G", null, null, null));
		assertEquals("a to b (a->b)", DotLabel.substitute("\\T to \\H (\\E)", null, null, "a", "b", "a->b"));
		// The line escapes and the escaped backslash are kept for DotLabel.create
		assertEquals("x\\ny\\\\N", DotLabel.substitute("x\\ny\\\\N", "a", null, null, null, null));
		assertEquals("missing:", DotLabel.substitute("missing:\\N", null, null, null, null, null));
	}

	private static void assertDim(double width, double height, XDimension2D actual) {
		assertEquals(width, actual.getWidth(), 1e-9);
		assertEquals(height, actual.getHeight(), 1e-9);
	}

	private static DotAttributes attributes(String... pairs) {
		final DotAttributes result = new DotAttributes();
		for (int i = 0; i < pairs.length; i += 2)
			result.put(pairs[i], DotValue.of(pairs[i + 1]));
		return result;
	}

	@Test
	void boxSize() {
		// A small label: the minimum size 0.75 x 0.5 inch wins
		assertDim(54, 36, NodeShape.BOX.computeSize(new XDimension2D(10, 10), attributes(), false));
		// A wide label: padded by 16 x 8
		assertDim(116, 36, NodeShape.BOX.computeSize(new XDimension2D(100, 10), attributes(), false));
		assertDim(144, 72, NodeShape.BOX.computeSize(new XDimension2D(10, 10), attributes("width", "2", "height", "1"), false));
		assertDim(72, 7.2, NodeShape.BOX.computeSize(new XDimension2D(500, 500),
				attributes("fixedsize", "true", "width", "1", "height", "0.1"), false));
	}

	@Test
	void ellipseGrowsAroundTheLabel() {
		final XDimension2D size = NodeShape.ELLIPSE.computeSize(new XDimension2D(100, 30), attributes(), false);
		assertEquals(116 * Math.sqrt(2), size.getWidth(), 1e-9);
		assertEquals(38 * Math.sqrt(2), size.getHeight(), 1e-9);
	}

	@Test
	void circleAndSquareAreRegular() {
		final XDimension2D circle = NodeShape.CIRCLE.computeSize(new XDimension2D(10, 10), attributes(), false);
		assertEquals(circle.getWidth(), circle.getHeight(), 1e-9);
		final XDimension2D square = NodeShape.BOX.computeSize(new XDimension2D(100, 10), attributes(), true);
		assertDim(116, 116, square);
	}

}
