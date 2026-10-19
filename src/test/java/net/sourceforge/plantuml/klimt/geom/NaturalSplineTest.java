/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Roshan Banisetti
 *
 */
package net.sourceforge.plantuml.klimt.geom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class NaturalSplineTest {

	private static final double EPS = 1e-9;

	private static List<XPoint2D> points(double... xy) {
		final XPoint2D[] result = new XPoint2D[xy.length / 2];
		for (int i = 0; i < result.length; i++)
			result[i] = new XPoint2D(xy[2 * i], xy[2 * i + 1]);
		return Arrays.asList(result);
	}

	@Test
	void fewerThanTwoPointsGiveNoCurve() {
		assertTrue(NaturalSpline.through(Collections.<XPoint2D>emptyList()).isEmpty());
		assertTrue(NaturalSpline.through(points(1, 2)).isEmpty());
	}

	@Test
	void xThatDoesNotStrictlyIncreaseGivesNoCurve() {
		assertTrue(NaturalSpline.through(points(0, 0, 1, 1, 1, 2)).isEmpty());
		assertTrue(NaturalSpline.through(points(0, 0, 2, 1, 1, 2)).isEmpty());
	}

	@Test
	void twoPointsGiveTheStraightLine() {
		final List<XCubicCurve2D> curves = NaturalSpline.through(points(0, 0, 3, 6));
		assertEquals(1, curves.size());
		final XCubicCurve2D c = curves.get(0);
		assertEquals(1, c.ctrlx1, EPS);
		assertEquals(2, c.ctrly1, EPS);
		assertEquals(2, c.ctrlx2, EPS);
		assertEquals(4, c.ctrly2, EPS);
	}

	@Test
	void threePointsGiveTheKnownSpline() {
		// Through (0,0), (1,1), (2,0) the second derivative at the middle point is -3, so the first
		// interval has slope 1.5 at its start and 0 at its end.
		final List<XCubicCurve2D> curves = NaturalSpline.through(points(0, 0, 1, 1, 2, 0));
		assertEquals(2, curves.size());
		final XCubicCurve2D c = curves.get(0);
		assertEquals(0, c.x1, EPS);
		assertEquals(0, c.y1, EPS);
		assertEquals(1.0 / 3, c.ctrlx1, EPS);
		assertEquals(0.5, c.ctrly1, EPS);
		assertEquals(2.0 / 3, c.ctrlx2, EPS);
		assertEquals(1, c.ctrly2, EPS);
		assertEquals(1, c.x2, EPS);
		assertEquals(1, c.y2, EPS);
	}

	@Test
	void theCurvePassesThroughEveryPointWithContinuousSlopeAndCurvature() {
		final List<XPoint2D> pts = points(10, 45, 25, 62, 40, 58, 70, 70, 80, 20);
		final List<XCubicCurve2D> curves = NaturalSpline.through(pts);
		assertEquals(pts.size() - 1, curves.size());
		for (int i = 0; i < curves.size(); i++) {
			final XCubicCurve2D c = curves.get(i);
			assertEquals(pts.get(i).x, c.x1, EPS);
			assertEquals(pts.get(i).y, c.y1, EPS);
			assertEquals(pts.get(i + 1).x, c.x2, EPS);
			assertEquals(pts.get(i + 1).y, c.y2, EPS);
		}
		// The control points sit at a third of each interval in x, so dy/dx and d2y/dx2 at the ends
		// of a Bezier follow from its y values alone.
		for (int i = 0; i < curves.size() - 1; i++) {
			final XCubicCurve2D left = curves.get(i);
			final XCubicCurve2D right = curves.get(i + 1);
			final double h0 = left.x2 - left.x1;
			final double h1 = right.x2 - right.x1;
			assertEquals(3 * (left.y2 - left.ctrly2) / h0, 3 * (right.ctrly1 - right.y1) / h1, EPS);
			assertEquals(6 * (left.ctrly1 - 2 * left.ctrly2 + left.y2) / (h0 * h0),
					6 * (right.y1 - 2 * right.ctrly1 + right.ctrly2) / (h1 * h1), EPS);
		}
		final XCubicCurve2D first = curves.get(0);
		final XCubicCurve2D last = curves.get(curves.size() - 1);
		assertEquals(0, first.y1 - 2 * first.ctrly1 + first.ctrly2, EPS);
		assertEquals(0, last.ctrly1 - 2 * last.ctrly2 + last.y2, EPS);
	}

}
