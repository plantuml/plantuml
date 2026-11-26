package net.sourceforge.plantuml.sdot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;

class OrthogonalPathTest {

	@Test
	void blockedStraightEdgeKeepsItsPortsAndDetoursAroundTheObstacle() {
		final RectangleArea sender = new RectangleArea(0, 0, 20, 20);
		final RectangleArea receiver = new RectangleArea(100, 0, 120, 20);
		final RectangleArea obstacle = new RectangleArea(40, -10, 80, 30);
		final DotPath original = straightLine(new XPoint2D(20, 10), new XPoint2D(100, 10));

		final DotPath routed = route(original, sender, receiver,
				Arrays.asList(sender, obstacle, receiver), Collections.emptyList(), 0, 0);

		assertNotNull(routed);
		assertEquals(original.getStartPoint(), routed.getStartPoint());
		assertEquals(original.getEndPoint(), routed.getEndPoint());
		final Rectangle2D node = new Rectangle2D.Double(40, -10, 40, 40);
		for (XCubicCurve2D segment : routed.getBeziers()) {
			assertFalse(node.intersectsLine(segment.x1, segment.y1, segment.x2, segment.y2),
					"The routed edge must clear the unrelated node");
			assertTrue(segment.x1 == segment.ctrlx1 && segment.x1 == segment.ctrlx2 && segment.x1 == segment.x2
					|| segment.y1 == segment.ctrly1 && segment.y1 == segment.ctrly2 && segment.y1 == segment.y2,
					"The entire Bézier segment must lie on one axis");
		}
	}

	@Test
	void edgesWithSharedPortsUseSeparateTracksBetweenTheStems() {
		final DotPath original = straightLine(new XPoint2D(0, 0), new XPoint2D(160, 0));
		final List<RectangleArea> firstTrack = Collections.singletonList(new RectangleArea(0, 0, 160, 0));

		final DotPath routed = route(original, null, null, Collections.emptyList(), firstTrack, 0, 0);

		assertNotNull(routed);
		assertEquals(original.getStartPoint(), routed.getStartPoint());
		assertEquals(original.getEndPoint(), routed.getEndPoint());
		assertTrue(routed.getBeziers().stream().anyMatch(segment -> segment.y1 == segment.y2
				&& segment.y1 != 0 && segment.getLength() > 90), "The middle of the route has its own track");
	}

	@Test
	void cornerPortFollowsTheSplineWhenTwoSidesAreEquallyClose() {
		final RectangleArea sender = new RectangleArea(0, 0, 20, 20);
		final XPoint2D start = new XPoint2D(20, 0);
		final XPoint2D end = new XPoint2D(20, -100);
		final DotPath original = straightLine(start, end);

		final DotPath routed = route(original, sender, null, Collections.singletonList(sender),
				Collections.emptyList(), 0, 0);

		assertNotNull(routed);
		final XPoint2D firstBend = routed.getBeziers().get(0).getP2();
		assertEquals(start.x, firstBend.x, "The original edge leaves via the top side of the sender");
		assertTrue(firstBend.y < start.y, "The route leaves the corner upwards");
	}

	@Test
	void cornerPortFollowsTheSplineWhenAnotherSideIsMarginallyCloser() {
		final RectangleArea sender = new RectangleArea(0, 0, 20, 20);
		final XPoint2D start = new XPoint2D(0.3, 20);
		final DotPath original = straightLine(start, new XPoint2D(-100, 20));

		final DotPath routed = route(original, sender, null, Collections.singletonList(sender),
				Collections.emptyList(), 0, 0);

		assertNotNull(routed);
		final XPoint2D firstBend = routed.getBeziers().get(0).getP2();
		assertEquals(start.y, firstBend.y, "The original edge leaves via the left side of the sender");
		assertTrue(firstBend.x < start.x, "The route leaves the corner to the left");
	}

	@Test
	void runBesideAnEarlierEdgeKeepsAFullTrackBetweenThem() {
		final DotPath original = straightLine(new XPoint2D(0, 0), new XPoint2D(160, 0));
		final List<RectangleArea> firstTrack = Collections.singletonList(new RectangleArea(0, 3, 160, 3));

		final DotPath routed = route(original, null, null, Collections.emptyList(), firstTrack, 0, 0);

		assertNotNull(routed);
		assertTrue(routed.getBeziers().stream().anyMatch(segment -> segment.y1 == segment.y2
				&& Math.abs(segment.y1 - 3) >= 6 && segment.getLength() > 90),
				"The middle of the route is six pixels away from the earlier edge");
	}

	@Test
	void routeBetweenCloseFacingPortsDoesNotDoubleBack() {
		final RectangleArea sender = new RectangleArea(-10, -20, 10, 0);
		final RectangleArea receiver = new RectangleArea(-5, 20, 15, 40);
		final DotPath original = straightLine(new XPoint2D(0, 0), new XPoint2D(5, 20));
		final double arrowheadLength = 11;

		final DotPath routed = route(original, sender, receiver, Arrays.asList(sender, receiver),
				Collections.emptyList(), 0, arrowheadLength);

		assertNotNull(routed);
		assertEquals(original.getStartPoint(), routed.getStartPoint());
		assertEquals(original.getEndPoint(), routed.getEndPoint());
		assertTrue(routed.getBeziers().stream().allMatch(segment -> segment.y2 >= segment.y1),
				"The offset between the ports needs a sideways step, with all vertical segments towards the target");
		final List<XCubicCurve2D> segments = routed.getBeziers();
		assertTrue(segments.get(segments.size() - 1).getLength() >= arrowheadLength,
				"The final bend must stay outside the arrowhead");
	}

	@Test
	void nearlyAlignedPortsShareAStraightRunAlongTheirFacingSides() {
		final RectangleArea sender = new RectangleArea(0, 0, 20, 20);
		final RectangleArea receiver = new RectangleArea(60, 0, 80, 20);
		final DotPath original = straightLine(new XPoint2D(20, 10), new XPoint2D(60, 11));

		final DotPath routed = route(original, sender, receiver, Arrays.asList(sender, receiver),
				Collections.emptyList(), 0, 0);

		assertNotNull(routed);
		assertEquals(1, routed.getBeziers().size(), "A one-pixel offset can be absorbed along the attachment sides");
		assertEquals(20, routed.getStartPoint().x);
		assertEquals(60, routed.getEndPoint().x);
		assertEquals(routed.getStartPoint().y, routed.getEndPoint().y);
		assertTrue(routed.getStartPoint().y >= 10 && routed.getEndPoint().y <= 11,
				"Both attachments stay between the original port coordinates");
	}

	@Test
	void shortTerminalRunKeepsThePreferredObstacleClearance() {
		final RectangleArea sender = new RectangleArea(-20, -20, 20, 0);
		final RectangleArea receiver = new RectangleArea(-20, 70, 20, 90);
		final RectangleArea title = new RectangleArea(-20, 9, 20, 40);
		final DotPath original = straightLine(new XPoint2D(0, 0), new XPoint2D(0, 70));

		final DotPath routed = route(original, sender, receiver, Arrays.asList(sender, title, receiver),
				Collections.emptyList(), 0, 0);

		assertNotNull(routed);
		final Rectangle2D paddedTitle = new Rectangle2D.Double(-23, 6, 46, 37);
		for (XCubicCurve2D segment : routed.getBeziers())
			assertFalse(paddedTitle.intersectsLine(segment.x1, segment.y1, segment.x2, segment.y2),
					"A shorter terminal run leaves room to turn outside the padded title");
	}

	@Test
	void opposingDecorationsThatDoNotFitLeaveTheSplineInPlace() {
		final RectangleArea sender = new RectangleArea(0, 0, 20, 20);
		final RectangleArea receiver = new RectangleArea(30, 0, 50, 20);
		final DotPath original = straightLine(new XPoint2D(20, 10), new XPoint2D(30, 10));

		final DotPath routed = route(original, sender, receiver, Arrays.asList(sender, receiver),
				Collections.emptyList(), 7, 7);

		assertNull(routed, "The ten-pixel gap cannot contain both decorations");
	}

	@Test
	void edgesOfADiagramShareOneSearchBudget() {
		final RectangleArea sender = new RectangleArea(0, 0, 20, 20);
		final RectangleArea receiver = new RectangleArea(100, 0, 120, 20);
		final RectangleArea obstacle = new RectangleArea(40, -10, 80, 30);
		final List<RectangleArea> nodes = Arrays.asList(sender, obstacle, receiver);
		final DotPath original = straightLine(new XPoint2D(20, 10), new XPoint2D(100, 10));
		final int limit = 100_000;
		final OrthogonalPath.SearchBudget budget = new OrthogonalPath.SearchBudget(limit);

		int routed = 0;
		while (routed < limit && route(original, sender, receiver, nodes, Collections.emptyList(), 0, 0, budget) != null)
			routed++;

		assertTrue(routed > 0, "The first detour is well within the budget");
		assertTrue(routed < limit, "Every detour takes its share of the budget");
		assertNull(route(original, sender, receiver, nodes, Collections.emptyList(), 0, 0, budget),
				"Once the budget is spent, each remaining edge keeps its spline");
	}

	@Test
	void aJogMovedToTheMiddleStaysOutOfTheArrowhead() {
		// Only 8 px between the nodes, and a 6 px arrowhead: the route fits with
		// the least clearance, and its jog must stay behind the arrowhead.
		final RectangleArea sender = new RectangleArea(0, 0, 40, 20);
		final RectangleArea receiver = new RectangleArea(60, 28, 100, 48);
		final DotPath original = straightLine(new XPoint2D(20, 20), new XPoint2D(80, 28));

		final DotPath routed = route(original, sender, receiver, Arrays.asList(sender, receiver),
				Collections.emptyList(), 0, 6);

		assertNotNull(routed);
		final List<XCubicCurve2D> segments = routed.getBeziers();
		final XCubicCurve2D last = segments.get(segments.size() - 1);
		assertTrue(last.y2 - last.y1 >= 6, "The last run must hold the arrowhead, was " + (last.y2 - last.y1));
	}

	private static DotPath route(DotPath spline, RectangleArea tail, RectangleArea head, List<RectangleArea> nodes,
			List<RectangleArea> tracks, double tailDecoration, double headDecoration) {
		return route(spline, tail, head, nodes, tracks, tailDecoration, headDecoration,
				new OrthogonalPath.SearchBudget());
	}

	private static DotPath route(DotPath spline, RectangleArea tail, RectangleArea head, List<RectangleArea> nodes,
			List<RectangleArea> tracks, double tailDecoration, double headDecoration,
			OrthogonalPath.SearchBudget budget) {
		final OrthogonalPath.Endpoint tailEnd = new OrthogonalPath.Endpoint(tail, OrthogonalPath.Endpoint.Shape.FIXED,
				tailDecoration);
		final OrthogonalPath.Endpoint headEnd = new OrthogonalPath.Endpoint(head, OrthogonalPath.Endpoint.Shape.FIXED,
				headDecoration);
		// Fixed ends stay where the spline puts them.
		final OrthogonalPorts.Attachment attachment = new OrthogonalPorts(nodes, Collections.emptyList()).add(spline,
				tailEnd, headEnd);
		return OrthogonalPath.route(spline, attachment, tailEnd, headEnd, nodes, tracks, budget);
	}

	private static DotPath straightLine(XPoint2D a, XPoint2D b) {
		return new DotPath().addCurve(a, a, b, b);
	}
}
