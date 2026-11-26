package net.sourceforge.plantuml.sdot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;

/**
 * The rules that a diagram alone cannot pin down, because the layout decides
 * where the nodes go. Vega tests SmetanaOrtho010 and SmetanaOrtho011 cover the
 * whole path in diagrams.
 */
class OrthogonalPortsTest {

	@Test
	void overlappingBoxesGetAStraightLineInsteadOfAJog() {
		final RectangleArea top = new RectangleArea(0, 0, 200, 40);
		final RectangleArea bottom = new RectangleArea(150, 100, 250, 140);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(top, bottom), Collections.emptyList());
		// The layout aims at the centers of both boxes.
		final OrthogonalPorts.Attachment edge = add(ports, top, bottom, new XPoint2D(100, 40), new XPoint2D(200, 100));
		ports.assign(new OrthogonalPath.SearchBudget());

		assertEquals(edge.getStart().x, edge.getEnd().x, 0.001);
		assertTrue(edge.getStart().x > 150 && edge.getStart().x < 200, "The line runs where both boxes are");
		assertEquals(40, edge.getStart().y, 0.001);
		assertEquals(100, edge.getEnd().y, 0.001);
	}

	@Test
	void aNarrowOverlapMayUseTheRoomNearTheCorners() {
		final RectangleArea top = new RectangleArea(0, 0, 100, 40);
		final RectangleArea bottom = new RectangleArea(84, 100, 200, 140);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(top, bottom), Collections.emptyList());
		final OrthogonalPorts.Attachment edge = add(ports, top, bottom, new XPoint2D(50, 40), new XPoint2D(142, 100));
		ports.assign(new OrthogonalPath.SearchBudget());

		assertEquals(edge.getStart().x, edge.getEnd().x, 0.001);
	}

	@Test
	void edgesSharingASideKeepTheOrderOfTheirSourcesAndAGap() {
		final RectangleArea target = new RectangleArea(100, 200, 140, 230);
		final List<RectangleArea> nodes = new ArrayList<>(Collections.singletonList(target));
		final OrthogonalPorts ports = new OrthogonalPorts(nodes, Collections.emptyList());
		final List<OrthogonalPorts.Attachment> edges = new ArrayList<>();
		for (double x : new double[] { -200, 120, 400 }) {
			final RectangleArea source = new RectangleArea(x - 30, 0, x + 30, 40);
			nodes.add(source);
			// All splines end on the same point, the middle of the target's top.
			edges.add(add(ports, source, target, new XPoint2D(x, 40), new XPoint2D(120, 200)));
		}
		ports.assign(new OrthogonalPath.SearchBudget());

		for (int i = 1; i < edges.size(); i++) {
			final double gap = edges.get(i).getEnd().x - edges.get(i - 1).getEnd().x;
			assertTrue(gap >= 12 - 0.001, "Arrowheads must not meet, gap was " + gap);
		}
		for (OrthogonalPorts.Attachment edge : edges)
			assertTrue(edge.getEnd().x > 100 && edge.getEnd().x < 140, "Attachments stay on the side");
	}

	@Test
	void edgesPushedIntoTheSameCornerTurnWithoutCrossing() {
		final RectangleArea target = new RectangleArea(0, 200, 60, 230);
		final RectangleArea far = new RectangleArea(300, 0, 360, 40);
		final RectangleArea near = new RectangleArea(300, 100, 360, 140);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(target, far, near), Collections.emptyList());
		// Both come from beyond the right corner of the target's top side. The
		// near one has to turn into the side closer to that corner.
		final OrthogonalPorts.Attachment fromNear = add(ports, near, target, new XPoint2D(330, 140),
				new XPoint2D(30, 200));
		final OrthogonalPorts.Attachment fromFar = add(ports, far, target, new XPoint2D(330, 40),
				new XPoint2D(30, 200));
		ports.assign(new OrthogonalPath.SearchBudget());

		assertTrue(fromFar.getEnd().x < fromNear.getEnd().x,
				"The edge that turns higher up must attach further from the corner");
	}

	@Test
	void anAttachmentStaysWhereTheLayoutPutItWhenItsLineIsBlocked() {
		final RectangleArea proxy = new RectangleArea(0, 0, 400, 40);
		final RectangleArea resource = new RectangleArea(0, 200, 100, 240);
		final RectangleArea registry = new RectangleArea(-20, 80, 150, 150);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(proxy, resource, registry), Collections.emptyList());
		// The layout led the spline around the registry, on its right.
		final XPoint2D start = new XPoint2D(300, 40);
		final XPoint2D end = new XPoint2D(50, 200);
		final OrthogonalPorts.Attachment edge = add(ports, proxy, resource, start, end);
		ports.assign(new OrthogonalPath.SearchBudget());

		assertEquals(start, edge.getStart());
		assertEquals(end, edge.getEnd());
	}

	@Test
	void aStraightLineKeepsTheClearanceTheRouterNeeds() {
		final RectangleArea top = new RectangleArea(0, 0, 200, 40);
		final RectangleArea bottom = new RectangleArea(100, 200, 200, 240);
		// The halfway line at x = 150 would pass 0.2 px beside this node.
		final RectangleArea beside = new RectangleArea(150.2, 80, 300, 150);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(top, bottom, beside), Collections.emptyList());
		final OrthogonalPorts.Attachment edge = add(ports, top, bottom, new XPoint2D(100, 40), new XPoint2D(200, 200));
		ports.assign(new OrthogonalPath.SearchBudget());

		if (edge.getStart().x == edge.getEnd().x)
			assertTrue(edge.getStart().x < 150.2 - OrthogonalPath.MIN_NODE_GAP, "was at " + edge.getStart().x);
	}

	@Test
	void arrowheadsOnASideRoundedAllAlongMeetTheOutline() {
		// Corners of radius 30 on a box 60 wide and only 40 high: half an ellipse
		// on top, 20 deep, as SVG and Java2D draw it.
		final RectangleArea target = new RectangleArea(100, 200, 160, 240);
		final List<RectangleArea> nodes = new ArrayList<>(Collections.singletonList(target));
		final OrthogonalPorts ports = new OrthogonalPorts(nodes, Collections.emptyList());
		final List<OrthogonalPorts.Attachment> edges = new ArrayList<>();
		for (double x : new double[] { -300, -100, 150, 400, 600 }) {
			final RectangleArea source = new RectangleArea(x - 30, 0, x + 30, 40);
			nodes.add(source);
			edges.add(add(ports, source, 0, target, 30, new XPoint2D(x, 40), new XPoint2D(130, 200)));
		}
		ports.assign(new OrthogonalPath.SearchBudget());

		for (OrthogonalPorts.Attachment edge : edges) {
			final double x = edge.getEnd().x;
			final double offMiddle = (x - 130) / 30;
			final double outline = 220 - 20 * Math.sqrt(1 - offMiddle * offMiddle);
			assertEquals(outline, edge.getEnd().y, 0.001, "The arrowhead at x = " + x + " must meet the outline");
		}
	}

	@Test
	void aBoxRoundedAllAlongIsMetInTheMiddle() {
		final RectangleArea source = new RectangleArea(0, 0, 200, 40);
		// A circle, in fact.
		final RectangleArea target = new RectangleArea(150, 100, 190, 140);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(source, target), Collections.emptyList());
		final OrthogonalPorts.Attachment edge = add(ports, source, 0, target, 20, new XPoint2D(100, 40),
				new XPoint2D(155, 100));
		ports.assign(new OrthogonalPath.SearchBudget());

		assertEquals(new XPoint2D(170, 100), edge.getEnd());
	}

	@Test
	void anEdgeFromBeyondACornerStaysOutsideAStraightLine() {
		final RectangleArea target = new RectangleArea(100, 200, 200, 240);
		// Overlaps only the first few pixels of the target, closer to its corner
		// than an edge from further left may attach.
		final RectangleArea above = new RectangleArea(60, 0, 104, 40);
		final RectangleArea farLeft = new RectangleArea(-230, 0, -170, 40);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(target, above, farLeft), Collections.emptyList());
		final OrthogonalPorts.Attachment straight = add(ports, above, target, new XPoint2D(82, 40),
				new XPoint2D(150, 200));
		final OrthogonalPorts.Attachment fromLeft = add(ports, farLeft, target, new XPoint2D(-200, 40),
				new XPoint2D(150, 200));
		ports.assign(new OrthogonalPath.SearchBudget());

		assertTrue(fromLeft.getEnd().x < straight.getEnd().x,
				"The edge from the left must not cross the line coming straight down");
	}

	@Test
	void theResultDoesNotDependOnWhichNodeComesFirst() {
		final RectangleArea top = new RectangleArea(0, 0, 200, 40);
		final RectangleArea bottom = new RectangleArea(0, 200, 200, 240);
		// Two edges between the same boxes whose layout points cross.
		final XPoint2D[][] edges = { { new XPoint2D(100, 40), new XPoint2D(120, 200) },
				{ new XPoint2D(120, 40), new XPoint2D(100, 200) } };
		final List<XPoint2D> forward = new ArrayList<>();
		final List<XPoint2D> backward = new ArrayList<>();
		for (int order = 0; order < 2; order++) {
			final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(top, bottom), Collections.emptyList());
			final OrthogonalPorts.Attachment[] attachments = new OrthogonalPorts.Attachment[2];
			// The second time, the edges run the other way, so the bottom box
			// comes first.
			for (int i = 0; i < 2; i++)
				attachments[i] = order == 0 ? add(ports, top, bottom, edges[i][0], edges[i][1])
						: add(ports, bottom, top, edges[i][1], edges[i][0]);
			ports.assign(new OrthogonalPath.SearchBudget());
			for (OrthogonalPorts.Attachment attachment : attachments) {
				(order == 0 ? forward : backward).add(order == 0 ? attachment.getStart() : attachment.getEnd());
				assertEquals(attachment.getStart().x, attachment.getEnd().x, 0.5,
						"Both edges can run straight, side by side");
			}
		}
		assertEquals(forward, backward);
	}

	@Test
	void aStraightLineKeepsItsPlaceNextToABlockedEdge() {
		final RectangleArea target = new RectangleArea(100, 200, 300, 240);
		final RectangleArea above = new RectangleArea(100, 60, 160, 100);
		final RectangleArea farLeft = new RectangleArea(-100, 0, -40, 40);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(target, above, farLeft),
				Collections.emptyList());
		final OrthogonalPorts.Attachment straight = add(ports, above, target, new XPoint2D(130, 100),
				new XPoint2D(200, 200));
		// Lining up at the left corner of the target would run into the node above,
		// so this end stays at its layout point, right where the straight line runs.
		final OrthogonalPorts.Attachment blocked = add(ports, farLeft, target, new XPoint2D(-70, 40),
				new XPoint2D(165, 200));
		ports.assign(new OrthogonalPath.SearchBudget());

		assertEquals(straight.getStart().x, straight.getEnd().x, 0.5);
		assertTrue(blocked.getEnd().x < straight.getEnd().x);
	}

	@Test
	void aClusterInTheWayBlocksTheRunToo() {
		final RectangleArea top = new RectangleArea(0, 0, 300, 40);
		final RectangleArea bottom = new RectangleArea(0, 200, 300, 240);
		// A cluster that contains neither end, below its title.
		final RectangleArea cluster = new RectangleArea(100, 80, 300, 160);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(top, bottom),
				Collections.singletonList(cluster));
		final XPoint2D start = new XPoint2D(40, 40);
		final XPoint2D end = new XPoint2D(200, 200);
		final OrthogonalPorts.Attachment edge = add(ports, top, bottom, start, end);
		ports.assign(new OrthogonalPath.SearchBudget());

		// The straight line halfway, at x = 120, would cross the cluster.
		assertEquals(start, edge.getStart());
		assertEquals(end, edge.getEnd());
	}

	@Test
	void aLineDoesNotRunAlongAClusterBorder() {
		// A port sits on the bottom border of its cluster. Lining the edge up with
		// it would run the edge along that border, where it cannot be seen.
		final RectangleArea cluster = new RectangleArea(69, 12, 230, 202);
		final RectangleArea source = new RectangleArea(6, 197, 61, 241);
		final RectangleArea port = new RectangleArea(119.5, 196, 131.5, 208);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(source, port),
				Collections.singletonList(cluster));
		final OrthogonalPorts.Attachment edge = add(ports, source, port, new XPoint2D(61, 214),
				new XPoint2D(119.5, 202.97));
		ports.assign(new OrthogonalPath.SearchBudget());

		assertTrue(Math.abs(edge.getStart().y - 202) >= OrthogonalPath.TRACK_GAP, "was at " + edge.getStart().y);
	}

	@Test
	void aDetourThroughTheLayoutSidesTakesASideNextToThem() {
		final RectangleArea source = new RectangleArea(0, 0, 60, 40);
		final RectangleArea target = new RectangleArea(200, 200, 260, 240);
		// In the way of the turn from below the source into the left of the target.
		final RectangleArea obstacle = new RectangleArea(-20, 180, 100, 300);
		final List<RectangleArea> nodes = Arrays.asList(source, target, obstacle);
		final OrthogonalPorts ports = new OrthogonalPorts(nodes, Collections.emptyList());
		final XPoint2D start = new XPoint2D(30, 40);
		final XPoint2D end = new XPoint2D(200, 220);
		// The layout enters the target from the left.
		final DotPath spline = new DotPath().addCurve(start, new XPoint2D(30, 220), new XPoint2D(120, 220), end);
		final OrthogonalPath.Endpoint tail = OrthogonalPath.Endpoint.side(source, 0, 0, 0);
		final OrthogonalPath.Endpoint head = OrthogonalPath.Endpoint.side(target, 0, 0, 0);
		final OrthogonalPorts.Attachment edge = ports.add(spline, tail, head);
		ports.assign(new OrthogonalPath.SearchBudget());

		final DotPath routed = OrthogonalPath.route(spline, edge, tail, head, nodes, Collections.emptyList(),
				new OrthogonalPath.SearchBudget());
		// Leaving the right of the source or entering the top of the target
		// both make a Z of it.
		assertTrue(routed.getBeziers().size() <= 3,
				"A Z replaces the detour around the obstacle, had " + routed.getBeziers().size() + " runs");
	}

	@Test
	void aBudgetTooSmallForPlacementIsLeftToTheRoutes() {
		final RectangleArea top = new RectangleArea(0, 0, 200, 40);
		final RectangleArea bottom = new RectangleArea(150, 100, 250, 140);
		final OrthogonalPorts ports = new OrthogonalPorts(Arrays.asList(top, bottom), Collections.emptyList());
		final XPoint2D start = new XPoint2D(100, 40);
		final XPoint2D end = new XPoint2D(200, 100);
		final OrthogonalPorts.Attachment edge = add(ports, top, bottom, start, end);
		// A round costs 4 x 2 ends x 2 obstacles = 16, more than a tenth of 50.
		final OrthogonalPath.SearchBudget budget = new OrthogonalPath.SearchBudget(50);
		ports.assign(budget);

		assertEquals(start, edge.getStart());
		assertEquals(end, edge.getEnd());
		assertFalse(budget.isSpent());
	}

	@Test
	void anAttachmentMovedNextToACornerKeepsItsSide() {
		// Two edges share the top of a narrow node, so one ends up a pixel off its
		// left corner. Coming from the left, the spline there would suggest the
		// left side instead.
		final RectangleArea target = new RectangleArea(100, 200, 110, 230);
		final RectangleArea left = new RectangleArea(-100, 0, -40, 40);
		final RectangleArea right = new RectangleArea(250, 0, 310, 40);
		final List<RectangleArea> nodes = Arrays.asList(target, left, right);
		final OrthogonalPorts ports = new OrthogonalPorts(nodes, Collections.emptyList());
		final XPoint2D start = new XPoint2D(-70, 40);
		final XPoint2D end = new XPoint2D(105, 200);
		final DotPath spline = new DotPath().addCurve(start, new XPoint2D(-70, 200), new XPoint2D(60, 200), end);
		final OrthogonalPath.Endpoint tail = OrthogonalPath.Endpoint.side(left, 0, 0, 0);
		final OrthogonalPath.Endpoint head = OrthogonalPath.Endpoint.side(target, 0, 0, 0);
		final OrthogonalPorts.Attachment edge = ports.add(spline, tail, head);
		add(ports, right, target, new XPoint2D(280, 40), new XPoint2D(105, 200));
		ports.assign(new OrthogonalPath.SearchBudget());

		final DotPath routed = OrthogonalPath.route(spline, edge, tail, head, nodes, Collections.emptyList(),
				new OrthogonalPath.SearchBudget());
		final List<XCubicCurve2D> segments = routed.getBeziers();
		final XCubicCurve2D last = segments.get(segments.size() - 1);
		assertEquals(last.x1, last.x2, 0.001, "The edge must come down onto the top side");
		assertTrue(last.y2 > last.y1);
	}

	private static OrthogonalPorts.Attachment add(OrthogonalPorts ports, RectangleArea tail, RectangleArea head,
			XPoint2D start, XPoint2D end) {
		return add(ports, tail, 0, head, 0, start, end);
	}

	private static OrthogonalPorts.Attachment add(OrthogonalPorts ports, RectangleArea tail, double tailInset,
			RectangleArea head, double headInset, XPoint2D start, XPoint2D end) {
		return ports.add(new DotPath().addCurve(start, start, end, end),
				OrthogonalPath.Endpoint.side(tail, 0, 0, tailInset), OrthogonalPath.Endpoint.side(head, 0, 0, headInset));
	}
}
