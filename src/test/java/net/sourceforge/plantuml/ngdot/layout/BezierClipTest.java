package net.sourceforge.plantuml.ngdot.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;

class BezierClipTest {

	// A straight line from (0,0) to (0,90) written as one cubic segment
	private static final List<XPoint2D> LINE = Arrays.asList(new XPoint2D(0, 0), new XPoint2D(0, 30),
			new XPoint2D(0, 60), new XPoint2D(0, 90));

	@Test
	void clipEndOfALine() {
		final List<XPoint2D> result = BezierClip.clipEnd(LINE, 10);
		assertEquals(4, result.size());
		assertEquals(0, result.get(0).getY(), 1e-9);
		assertEquals(80, result.get(3).getY(), 1e-6);
	}

	@Test
	void clipStartOfALine() {
		final List<XPoint2D> result = BezierClip.clipStart(LINE, 10);
		assertEquals(4, result.size());
		assertEquals(10, result.get(0).getY(), 1e-6);
		assertEquals(90, result.get(3).getY(), 1e-9);
	}

	@Test
	void clipSpansSeveralSegments() {
		// Two segments: (0,0)->(0,90) then (0,90)->(0,93). Clipping 10 drops the
		// short last segment and cuts the first one.
		final List<XPoint2D> points = Arrays.asList(new XPoint2D(0, 0), new XPoint2D(0, 30), new XPoint2D(0, 60),
				new XPoint2D(0, 90), new XPoint2D(0, 91), new XPoint2D(0, 92), new XPoint2D(0, 93));
		final List<XPoint2D> result = BezierClip.clipEnd(points, 10);
		assertEquals(4, result.size());
		assertEquals(83, result.get(3).getY(), 1e-6);
	}

	@Test
	void curveShorterThanTheArrow() {
		final List<XPoint2D> result = BezierClip.clipEnd(LINE, 200);
		assertEquals(4, result.size());
		assertEquals(0, result.get(3).getY(), 1e-9);
	}

	@Test
	void clipOnAContour() {
		// A diamond of half-diagonals 20 x 10 centered at (0, 90): the line enters
		// it at its top vertex (0, 80)
		final NodeContour diamond = new NodeContour() {
			public boolean contains(double dx, double dy) {
				return Math.abs(dx) / 20 + Math.abs(dy) / 10 <= 1;
			}
		};
		final List<XPoint2D> end = BezierClip.clipEndToContour(LINE, diamond, new XPoint2D(0, 90));
		assertEquals(80, end.get(end.size() - 1).getY(), 1e-6);

		// Leaving a node centered on the start
		final List<XPoint2D> start = BezierClip.clipStartToContour(LINE, diamond, new XPoint2D(0, 0));
		assertEquals(10, start.get(0).getY(), 1e-6);
	}

}
