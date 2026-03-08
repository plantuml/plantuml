package net.sourceforge.plantuml.klimt.drawing.eps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.geom.MinMax;

/**
 * Minimal unit tests for {@link DriverTextEps} — focuses on the static
 * helper methods that compute bounding boxes from path data.
 */
class DriverTextEpsTest {

	// -----------------------------------------------------------------------
	// getMinMax — bounding box from PathIterator
	// -----------------------------------------------------------------------

	@Test
	void getMinMaxWithSingleMoveTo() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(10, 20);

		final MinMax mm = DriverTextEps.getMinMax(0, 0, path.getPathIterator(null));
		assertEquals(10.0, mm.getMinX(), 0.001);
		assertEquals(20.0, mm.getMinY(), 0.001);
		assertEquals(10.0, mm.getMaxX(), 0.001);
		assertEquals(20.0, mm.getMaxY(), 0.001);
	}

	@Test
	void getMinMaxWithLineTo() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(5, 10);
		path.lineTo(50, 80);
		path.closePath();

		final MinMax mm = DriverTextEps.getMinMax(0, 0, path.getPathIterator(null));
		assertEquals(5.0, mm.getMinX(), 0.001);
		assertEquals(10.0, mm.getMinY(), 0.001);
		assertEquals(50.0, mm.getMaxX(), 0.001);
		assertEquals(80.0, mm.getMaxY(), 0.001);
	}

	@Test
	void getMinMaxAppliesOffset() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(10, 20);
		path.lineTo(30, 40);

		final MinMax mm = DriverTextEps.getMinMax(100, 200, path.getPathIterator(null));
		assertEquals(110.0, mm.getMinX(), 0.001);
		assertEquals(220.0, mm.getMinY(), 0.001);
		assertEquals(130.0, mm.getMaxX(), 0.001);
		assertEquals(240.0, mm.getMaxY(), 0.001);
	}

	@Test
	void getMinMaxWithQuadTo() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, 0);
		path.quadTo(50, 100, 200, 10);

		final MinMax mm = DriverTextEps.getMinMax(0, 0, path.getPathIterator(null));
		assertEquals(0.0, mm.getMinX(), 0.001);
		assertEquals(0.0, mm.getMinY(), 0.001);
		assertEquals(200.0, mm.getMaxX(), 0.001);
		assertEquals(100.0, mm.getMaxY(), 0.001);
	}

	@Test
	void getMinMaxWithCubicTo() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, 0);
		path.curveTo(10, 20, 30, 40, 50, 60);

		final MinMax mm = DriverTextEps.getMinMax(0, 0, path.getPathIterator(null));
		assertEquals(0.0, mm.getMinX(), 0.001);
		assertEquals(0.0, mm.getMinY(), 0.001);
		assertEquals(50.0, mm.getMaxX(), 0.001);
		assertEquals(60.0, mm.getMaxY(), 0.001);
	}

	@Test
	void getMinMaxWidthAndHeight() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(10, 20);
		path.lineTo(60, 20);
		path.lineTo(60, 50);
		path.lineTo(10, 50);
		path.closePath();

		final MinMax mm = DriverTextEps.getMinMax(0, 0, path.getPathIterator(null));
		assertEquals(50.0, mm.getWidth(), 0.001);
		assertEquals(30.0, mm.getHeight(), 0.001);
	}

	// -----------------------------------------------------------------------
	// drawPathIterator — smoke test (no exception)
	// -----------------------------------------------------------------------

	@Test
	void drawPathIteratorDoesNotThrowOnSimplePath() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, 0);
		path.lineTo(10, 0);
		path.lineTo(10, 10);
		path.closePath();

		final EpsGraphics eps = new EpsGraphics();
		// Should not throw any exception
		DriverTextEps.drawPathIterator(eps, 5, 5, path);
	}

	@Test
	void drawPathIteratorHandlesQuadAndCubicSegments() {
		final GeneralPath path = new GeneralPath();
		path.moveTo(0, 0);
		path.quadTo(5, 10, 10, 0);
		path.curveTo(15, 5, 20, 10, 25, 0);
		path.closePath();

		final EpsGraphics eps = new EpsGraphics();
		// Should not throw any exception
		DriverTextEps.drawPathIterator(eps, 0, 0, path);
	}

}
