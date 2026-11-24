package net.sourceforge.plantuml.sdot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;

class OrthogonalLabelPlacementTest {

	@Test
	void overlappingObstaclesMoveTheLabelPastTheirCombinedBounds() {
		final UTranslate position = placeMidpointLabel(new RectangleArea(30, -50, 53, 50),
				new RectangleArea(54, -50, 77, 50));

		assertNotNull(position);
		assertEquals(79, position.getDx(), "The five-pixel half-width and two-pixel clearance put the center at x=84");
	}

	@Test
	void aGapExactlyAsWideAsTheLabelAndClearanceRemainsUsable() {
		final UTranslate position = placeMidpointLabel(new RectangleArea(30, -50, 55, 50),
				new RectangleArea(69, -50, 90, 50));

		assertNotNull(position);
		assertEquals(57, position.getDx(), "A ten-pixel label fits at x=57 with two pixels clear on either side");
	}

	@Test
	void aCrowdedConnectorCanUseClearSpaceBeyondThePreferredLabelOffsets() {
		final OrthogonalLabelPlacement placement = new OrthogonalLabelPlacement(
				Collections.singletonList(new RectangleArea(-200, -50, 200, 50)));
		final DotPath path = horizontalPath(120);
		placement.addPath(path, 1);

		final UTranslate position = placement.placeMidpoint(path, new XDimension2D(10, 10));

		assertEquals(55, position.getDx(), "The label remains centered along the connector");
		assertTrue(position.getDy() + 10 <= -52 || position.getDy() >= 52,
				"The label clears the crowded area by at least two pixels");
	}

	@Test
	void anEndLabelPushedAlongItsConnectorStaysEntirelyOnItsSideOfTheBisector() {
		// The nearest clear position beside the connector lies past the crowded
		// area, where the label would reach into the other end's half.
		final OrthogonalLabelPlacement placement = new OrthogonalLabelPlacement(
				Collections.singletonList(new RectangleArea(-200, -100, 60, 100)));
		final DotPath path = horizontalPath(200);
		placement.addPath(path, 1);

		final UTranslate position = placement.placeEndpoint(path.getStartPoint(), path.getEndPoint(),
				new XDimension2D(50, 10), 0);

		assertTrue(position.getDx() + 50 <= 98, "The right edge must stay two pixels inside the tail's half");
	}

	@Test
	void anEndLabelLongerThanHalfItsConnectorStaysOnItsOwnSide() {
		final OrthogonalLabelPlacement placement = new OrthogonalLabelPlacement(
				Collections.singletonList(new RectangleArea(-60, -40, 0, 40)));
		final DotPath path = horizontalPath(40);
		placement.addPath(path, 1);

		final UTranslate position = placement.placeEndpoint(path.getStartPoint(), path.getEndPoint(),
				new XDimension2D(50, 10), 0);

		assertTrue(position.getDx() + 25 <= 20, "The middle of the label stays on the tail's side");
		assertTrue(position.getDx() >= 2 || position.getDy() + 10 <= -42 || position.getDy() >= 42,
				"The label clears the node on its own side");
	}

	@Test
	void bothRowsOfAnEndLabelStayOnTheirSideOfADiagonalBisector() {
		final XPoint2D end = new XPoint2D(0, 0);
		final XPoint2D other = new XPoint2D(30, 20);
		final XDimension2D block = new XDimension2D(40, 30);
		final double roleCenterOffset = 7.5;
		final OrthogonalLabelPlacement placement = new OrthogonalLabelPlacement(
				Collections.singletonList(new RectangleArea(-50, -40, -4, 20)));

		final UTranslate position = placement.placeEndpoint(end, other, block, roleCenterOffset);
		final XPoint2D roleCenter = position.getTranslated(new XPoint2D(20, 15 + roleCenterOffset));

		assertTrue(roleCenter.distance(end) < roleCenter.distance(other),
				"Even the row nearer the bisector must belong to its own endpoint");
	}

	@Test
	void aSplineLabelKeepsItsLayoutPositionBesideItsOwnCurve() {
		final OrthogonalLabelPlacement placement = new OrthogonalLabelPlacement(Collections.emptyList());
		final DotPath curve = curvedPath();
		placement.addPath(curve, 1);

		// The curve passes three pixels below this label, inside the bounds of that part of the curve.
		final UTranslate position = placement.placeSplineLabel(curve, new UTranslate(58, 40), new XDimension2D(10, 11));

		assertEquals(58, position.getDx());
		assertEquals(40, position.getDy());
	}

	@Test
	void aSplineLabelMovesWhenItsOwnPaintedStrokeCoversIt() {
		final OrthogonalLabelPlacement placement = new OrthogonalLabelPlacement(Collections.emptyList());
		final DotPath curve = curvedPath();
		placement.addPath(curve, 24);
		final UTranslate original = new UTranslate(58, 40);

		final UTranslate position = placement.placeSplineLabel(curve, original, new XDimension2D(10, 11));

		assertTrue(Math.hypot(position.getDx() - original.getDx(), position.getDy() - original.getDy()) > 2,
				"The curve passes close enough for a thick stroke to obscure the old position");
	}

	@Test
	void aSplineLabelMovesOffAnotherConnector() {
		final OrthogonalLabelPlacement placement = new OrthogonalLabelPlacement(Collections.emptyList());
		final DotPath curve = curvedPath();
		placement.addPath(curve, 1);
		final XPoint2D top = new XPoint2D(63, 0);
		final XPoint2D bottom = new XPoint2D(63, 45);
		placement.addPath(new DotPath().addCurve(top, top, bottom, bottom), 1);

		final UTranslate position = placement.placeSplineLabel(curve, new UTranslate(58, 40), new XDimension2D(10, 11));

		assertTrue(position.getDx() + 10 <= 61 || position.getDx() >= 65 || position.getDy() >= 47,
				"The label clears the other connector by two pixels");
	}

	@Test
	void anEndLabelCanSitInTheCornerBetweenItsNodeAndConnector() {
		final RectangleArea node = new RectangleArea(-20, -20, 20, 0);
		final XPoint2D port = new XPoint2D(0, 0);
		final XPoint2D otherEnd = new XPoint2D(0, 100);
		final OrthogonalLabelPlacement placement = new OrthogonalLabelPlacement(Collections.singletonList(node));
		placement.addPath(new DotPath().addCurve(port, port, otherEnd, otherEnd), 1);

		final UTranslate position = placement.placeEndpoint(port, otherEnd, new XDimension2D(10, 10), 0);

		assertEquals(2, position.getDy(), "The label fits just below the node");
		assertEquals(7.5, Math.abs(position.getDx() + 5), "The label clears the painted stroke by two pixels");
	}

	private static UTranslate placeMidpointLabel(RectangleArea first, RectangleArea second) {
		final DotPath path = horizontalPath(120);
		final OrthogonalLabelPlacement placement = new OrthogonalLabelPlacement(Arrays.asList(first, second));
		placement.addPath(path, 1);
		return placement.placeMidpoint(path, new XDimension2D(10, 10));
	}

	private static DotPath curvedPath() {
		return new DotPath().addCurve(new XPoint2D(0, 0), new XPoint2D(0, 50), new XPoint2D(100, 50),
				new XPoint2D(100, 100));
	}

	private static DotPath horizontalPath(double length) {
		final XPoint2D start = new XPoint2D(0, 0);
		final XPoint2D end = new XPoint2D(length, 0);
		return new DotPath().addCurve(start, start, end, end);
	}
}
