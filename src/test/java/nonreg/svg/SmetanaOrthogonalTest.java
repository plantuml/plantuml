package nonreg.svg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

class SmetanaOrthogonalTest {

	private static final Pattern NUMBER = Pattern.compile("-?\\d+(?:\\.\\d+)?");

	@Test
	void orthogonalRoutingNeedsThePragma() throws Exception {
		final String[] diagram = { "class A", "class B", "class C", "A --> B", "A --> C" };

		assertTrue(isOrthogonal(linkPoints(link(renderWith("!pragma smetana_ortho", diagram), 0))));
		assertFalse(isOrthogonal(linkPoints(link(renderWith("", diagram), 0))),
				"Without the pragma Smetana keeps drawing its splines");
	}

	private static boolean isOrthogonal(List<Point2D> points) {
		for (int i = 1; i < points.size(); i++) {
			final Point2D a = points.get(i - 1);
			final Point2D b = points.get(i);
			if (a.getX() != b.getX() && a.getY() != b.getY())
				return false;
		}
		return true;
	}

	@Test
	void parallelEdgesLeaveEachOthersLabelsClear() throws Exception {
		final Document svg = render("class A", "class B", "A --> B : bravo", "A --> B : charlie", "A --> B : delta");

		final Rectangle2D charlie = textBounds(label(svg, "charlie"));
		final List<Point2D> delta = edgePoints(label(svg, "delta"));
		for (int i = 1; i < delta.size(); i++)
			assertFalse(charlie.intersectsLine(delta.get(i - 1).getX(), delta.get(i - 1).getY(),
					delta.get(i).getX(), delta.get(i).getY()), "The delta connector crosses the charlie label");
	}

	@Test
	void multiplicityAndRoleStayTogetherAtTheirOwnEndOfAnAssociation() throws Exception {
		final Document svg = render("left to right direction", "skinparam ranksep 37", "skinparam nodesep 10",
				"class N0", "class N1", "class N2",
				"N1 \"Tail0\"/\"RoleTail0........\" --> \"Head0\"/\"RoleHead0...........\" N0",
				"N2 \"Tail1\"/\"RoleTail1...\" --> \"Head1\"/\"RoleHead1..........\" N1",
				"N0 \"Tail2\"/\"RoleTail2....................\" --> \"Head2\"/\"RoleHead2...............\" N1",
				"N0 \"Tail3\"/\"RoleTail3....................\" --> \"Head3\"/\"RoleHead3.......................\" N2",
				"N2 \"Tail4\"/\"RoleTail4..................\" --> \"Head4\"/\"RoleHead4.....................\" N0");

		final List<Point2D> points = edgePoints(label(svg, "Tail2"));
		final Point2D tail = points.get(0);
		final Point2D head = points.get(points.size() - 1);
		assertEndpointPair(svg, "Tail2", "RoleTail2....................", tail, head);
		assertEndpointPair(svg, "Head2", "RoleHead2...............", head, tail);
	}

	@Test
	void wideRoleAndMultiplicityStayOnTheSideOfTheirOwnEndpoint() throws Exception {
		final Document svg = render("left to right direction", "skinparam ranksep 53", "skinparam nodesep 38",
				"class N0", "class N2", "class N4",
				"N0 \"Tail0\"/\"RoleTail0.......\" --> \"Head0\"/\"RoleHead0....\" N4",
				"N4 \"Tail1\"/\"RoleTail1............\" --> \"Head1\"/\"RoleHead1...................\" N2");

		final List<Point2D> points = edgePoints(label(svg, "Tail1"));
		assertEndpointPair(svg, "Tail1", "RoleTail1............", points.get(0), points.get(points.size() - 1));
	}

	@Test
	void middleLabelClearsThePaintedStrokeOfAThickConnector() throws Exception {
		final Document svg = render("left to right direction", "class A", "class B",
				"A -[#black,thickness=24]-> B : wide label");

		final Element text = label(svg, "wide label");
		final List<Point2D> points = edgePoints(text);
		final Point2D start = points.get(0);
		final Point2D end = points.get(points.size() - 1);
		assertEquals(start.getY(), end.getY(), 0.01, "The classes are connected horizontally");
		final double strokeWidth = 24;
		final Element path = (Element) ((Element) text.getParentNode()).getElementsByTagName("path").item(0);
		assertTrue(path.getAttribute("style").contains("stroke-width:24;"));
		final Rectangle2D stroke = new Rectangle2D.Double(start.getX(), start.getY() - strokeWidth / 2,
				end.getX() - start.getX(), strokeWidth);
		assertFalse(textBounds(text).intersects(stroke), "The label crosses the painted stroke");
	}

	@Test
	void endLabelsLeaveTheDecorationsOfTheirConnectorClear() throws Exception {
		final Document svg = render("class A", "class B", "A \"one\" <|---* \"many\" B");

		for (String value : new String[] { "one", "many" }) {
			final Element label = label(svg, value);
			final NodeList decorations = ((Element) label.getParentNode()).getElementsByTagName("polygon");
			assertEquals(2, decorations.getLength());
			for (int i = 0; i < decorations.getLength(); i++)
				assertFalse(polygonBounds((Element) decorations.item(i)).intersects(textBounds(label)),
						"The " + value + " label covers a decoration");
		}
	}

	@Test
	void endLabelsOnAShortConnectorStayBesideIt() throws Exception {
		final Document svg = render("class A", "class B", "A \"one\" --> \"many\" B");

		for (String value : new String[] { "one", "many" }) {
			final Element label = label(svg, value);
			final Rectangle2D nearby = textBounds(label);
			nearby.setRect(nearby.getX() - 8, nearby.getY() - 8, nearby.getWidth() + 16, nearby.getHeight() + 16);
			final List<Point2D> points = edgePoints(label);
			final Point2D start = points.get(0);
			final Point2D end = points.get(points.size() - 1);
			assertTrue(nearby.intersectsLine(start.getX(), start.getY(), end.getX(), end.getY()),
					"The " + value + " label is more than eight pixels away from its connector");
		}
	}

	@Test
	void blankEndLabelsLeaveTheirRoomToVisibleOnes() throws Exception {
		final Document withBlankNeighbors = render("class A", "class B", "A \" \" --> B", "A \"one\" --> B",
				"A \" \" --> B");
		final Document alone = render("class A", "class B", "A --> B", "A \"one\" --> B", "A --> B");

		final Point2D expected = offsetFromConnector(label(alone, "one"));
		final Point2D actual = offsetFromConnector(label(withBlankNeighbors, "one"));
		assertEquals(expected.getX(), actual.getX(), 0.01, "A blank label must not push a visible one aside");
		assertEquals(expected.getY(), actual.getY(), 0.01, "A blank label must not push a visible one along");
	}

	private static Point2D offsetFromConnector(Element label) {
		final Point2D start = edgePoints(label).get(0);
		final Rectangle2D bounds = textBounds(label);
		return new Point2D.Double(bounds.getX() - start.getX(), bounds.getY() - start.getY());
	}

	@Test
	void labelsLeaveTheConnectorOfANoteClear() throws Exception {
		final Document svg = render("class A", "class B", "note \"floating\" as N", "A .. N",
				"A \"one\" --> \"many\" B : label");

		final Element note = (Element) label(svg, "floating").getParentNode();
		final Path2D outline = outline((Element) note.getElementsByTagName("path").item(0));
		for (String value : new String[] { "one", "many", "label" })
			assertFalse(outline.intersects(textBounds(label(svg, value))),
					"The " + value + " label covers the note or its connector");
	}

	@Test
	void edgeToAnEntryPointStaysStraight() throws Exception {
		final Document svg = render("state Comp {", "state in1 <<entryPoint>>", "state Inner", "in1 --> Inner", "}",
				"[*] --> in1");

		final List<Point2D> points = linkPoints(link(svg, 1));
		for (Point2D point : points)
			assertEquals(points.get(0).getX(), point.getX(), 0.01, "Nothing lies between the two states");
	}

	@Test
	void edgeAtAPortCornerLeavesOnTheSideOfItsTarget() throws Exception {
		final Document svg = render("left to right direction", "component X {", "portin p2", "[Y]", "}", "Y --> p2");

		final List<Point2D> points = linkPoints(link(svg, 0));
		for (Point2D point : points)
			assertEquals(points.get(0).getY(), point.getY(), 0.01, "The port faces the component");
	}

	@Test
	void edgeFromAPortToAnInnerComponentClearsTheTitle() throws Exception {
		final Document svg = render("component Comp {", "portin i", "portout o", "[Inner]", "i --> Inner", "}");
		final List<Point2D> points = linkPoints(link(svg, 0));
		assertOrthogonalAndClear(points, textBounds(label(svg, "Comp")));

		final Element port = (Element) label(svg, "i").getParentNode();
		final Element box = (Element) port.getElementsByTagName("rect").item(0);
		final Rectangle2D portBounds = new Rectangle2D.Double(Double.parseDouble(box.getAttribute("x")),
				Double.parseDouble(box.getAttribute("y")), Double.parseDouble(box.getAttribute("width")),
				Double.parseDouble(box.getAttribute("height")));
		final Point2D start = points.get(0);
		assertTrue(portBounds.intersects(start.getX() - 0.01, start.getY() - 0.01, 0.02, 0.02),
				"The connector must touch the port symbol");
	}

	@Test
	void edgeFromAnEntryPointToAnInnerStateClearsTheTitle() throws Exception {
		final Document svg = render("state Comp {", "state i <<entryPoint>>", "state o <<exitPoint>>", "state Inner",
				"i --> Inner", "}");
		assertOrthogonalAndClear(linkPoints(link(svg, 0)), textBounds(label(svg, "Comp")));
	}

	private static void assertOrthogonalAndClear(List<Point2D> points, Rectangle2D obstacle) {
		for (int i = 1; i < points.size(); i += 3) {
			final Point2D start = points.get(i - 1);
			final Point2D end = points.get(i + 2);
			for (int j = i; j <= i + 2; j++) {
				final Point2D point = points.get(j);
				assertTrue(start.getX() == end.getX() ? start.getX() == point.getX() : start.getY() == point.getY(),
						"Every connector segment must be axis-aligned");
			}
			assertFalse(obstacle.intersectsLine(start.getX(), start.getY(), end.getX(), end.getY()),
					"The connector crosses the title");
		}
	}

	@Test
	void edgeKeepsItsDistanceFromTheBorderOfThePackageItEnters() throws Exception {
		final Document svg = render("class X", "package \"A long folder title\" as P {", "class A", "class B", "}",
				"X --> A");

		final Rectangle2D folder = clusterBounds(svg, "A long folder title");
		final List<Point2D> points = linkPoints(link(svg, 0));
		for (int i = 1; i < points.size(); i++) {
			final Point2D a = points.get(i - 1);
			final Point2D b = points.get(i);
			if (a.getY() == b.getY() && a.getX() != b.getX())
				assertTrue(Math.abs(a.getY() - folder.getMinY()) >= 2 && Math.abs(a.getY() - folder.getMaxY()) >= 2,
						"A horizontal run at y=" + a.getY() + " disappears into the folder border");
			if (a.getX() == b.getX() && a.getY() != b.getY())
				assertTrue(Math.abs(a.getX() - folder.getMinX()) >= 2 && Math.abs(a.getX() - folder.getMaxX()) >= 2,
						"A vertical run at x=" + a.getX() + " disappears into the folder border");
		}
	}

	@Test
	void edgeGoesAroundAPackageThatContainsNeitherOfItsEnds() throws Exception {
		final Document svg = render("class A", "package P {", "class B", "class B2", "}", "class C", "A -[hidden]-> B",
				"B -[hidden]-> C", "A --> C");

		final Rectangle2D folder = clusterBounds(svg, "P");
		final List<Point2D> points = linkPoints(link(svg, 0));
		for (int i = 1; i < points.size(); i++)
			assertFalse(folder.intersectsLine(points.get(i - 1).getX(), points.get(i - 1).getY(), points.get(i).getX(),
					points.get(i).getY()), "The edge crosses the unrelated package");
	}

	private static void assertEndpointPair(Document svg, String multiplicity, String role, Point2D end, Point2D other) {
		final Rectangle2D multiplicityBounds = textBounds(label(svg, multiplicity));
		final Rectangle2D roleBounds = textBounds(label(svg, role));
		for (Rectangle2D text : new Rectangle2D[] { multiplicityBounds, roleBounds })
			assertTrue(end.distance(text.getCenterX(), text.getCenterY()) < other.distance(text.getCenterX(), text.getCenterY()),
					"The " + multiplicity + " pair belongs to its own endpoint");
		assertEquals(multiplicityBounds.getCenterX(), roleBounds.getCenterX(), 0.01, "The two labels share a center column");
		assertFalse(multiplicityBounds.intersects(roleBounds), "Both parts of the endpoint label must remain readable");
	}

	private static Element label(Document svg, String value) {
		final NodeList texts = svg.getElementsByTagName("text");
		for (int i = 0; i < texts.getLength(); i++) {
			final Element text = (Element) texts.item(i);
			if (value.equals(text.getTextContent()))
				return text;
		}
		throw new AssertionError("Missing SVG label: " + value);
	}

	private static Rectangle2D textBounds(Element text) {
		final double height = Double.parseDouble(text.getAttribute("font-size"));
		return new Rectangle2D.Double(Double.parseDouble(text.getAttribute("x")),
				Double.parseDouble(text.getAttribute("y")) - height, Double.parseDouble(text.getAttribute("textLength")),
				height);
	}

	private static Rectangle2D polygonBounds(Element polygon) {
		final Matcher coordinate = NUMBER.matcher(polygon.getAttribute("points"));
		final Path2D path = new Path2D.Double();
		while (coordinate.find()) {
			final double x = Double.parseDouble(coordinate.group());
			assertTrue(coordinate.find(), "Every SVG x coordinate has a matching y coordinate");
			if (path.getCurrentPoint() == null)
				path.moveTo(x, Double.parseDouble(coordinate.group()));
			else
				path.lineTo(x, Double.parseDouble(coordinate.group()));
		}
		return path.getBounds2D();
	}

	// The outline of a note or a package consists of lines and corner arcs, which all end in a vertex.
	private static Path2D outline(Element path) {
		final Matcher command = Pattern.compile("[MLA][^MLA]*").matcher(path.getAttribute("d"));
		final Path2D outline = new Path2D.Double();
		while (command.find()) {
			final List<Double> numbers = new ArrayList<>();
			final Matcher number = NUMBER.matcher(command.group());
			while (number.find())
				numbers.add(Double.parseDouble(number.group()));
			final double x = numbers.get(numbers.size() - 2);
			final double y = numbers.get(numbers.size() - 1);
			if (outline.getCurrentPoint() == null)
				outline.moveTo(x, y);
			else
				outline.lineTo(x, y);
		}
		outline.closePath();
		return outline;
	}

	private static Rectangle2D clusterBounds(Document svg, String title) {
		final Element cluster = (Element) label(svg, title).getParentNode();
		return outline((Element) cluster.getElementsByTagName("path").item(0)).getBounds2D();
	}

	private static Element link(Document svg, int index) {
		final NodeList groups = svg.getElementsByTagName("g");
		int remaining = index;
		for (int i = 0; i < groups.getLength(); i++) {
			final Element group = (Element) groups.item(i);
			if ("link".equals(group.getAttribute("class")) && remaining-- == 0)
				return group;
		}
		throw new AssertionError("Missing SVG link: " + index);
	}

	private static List<Point2D> edgePoints(Element label) {
		return linkPoints((Element) label.getParentNode());
	}

	private static List<Point2D> linkPoints(Element group) {
		assertEquals("link", group.getAttribute("class"));
		final NodeList paths = group.getElementsByTagName("path");
		assertEquals(1, paths.getLength());
		final Matcher coordinate = NUMBER.matcher(((Element) paths.item(0)).getAttribute("d"));
		final List<Point2D> points = new ArrayList<>();
		while (coordinate.find()) {
			final double x = Double.parseDouble(coordinate.group());
			assertTrue(coordinate.find(), "Every SVG x coordinate has a matching y coordinate");
			points.add(new Point2D.Double(x, Double.parseDouble(coordinate.group())));
		}
		assertTrue(points.size() >= 4, "Expected a connector with at least one cubic segment");
		return points;
	}

	private static Document render(String... lines) throws Exception {
		return renderWith("!pragma smetana_ortho", lines);
	}

	private static Document renderWith(String optIn, String... lines) throws Exception {
		final String source = "@startuml\n!pragma layout smetana\n" + optIn + "\nskinparam linetype ortho\n"
				+ String.join("\n", lines) + "\n@enduml";
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		new SourceStringReader(source).outputImage(output, new FileFormatOption(FileFormat.SVG));
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new ByteArrayInputStream(output.toByteArray()));
	}
}
