package nonreg.svg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;

/**
 * With "linetype ortho", Graphviz starts an edge in the middle of a side of a
 * state but may then run it along that side, or turn a few pixels away from
 * it. Every edge end at a state or pseudo-state must leave its side at right
 * angles for at least three arrowhead widths, clear of the rounded corners of
 * a state, and on the centre line of a round or diamond pseudo-state.
 * <p>
 * Geometry is Graphviz's, so this runs through Svek (not Smetana, which has no
 * orthogonal routing) and is skipped when dot is not installed.
 */
class StateEdgeStemTest extends SvekSvgTest {

	private static final String DIAGRAM = String.join("\n", //
			"@startuml", //
			"skinparam linetype ortho", //
			"skinparam stateDiagramEdgeLabelStyle node", //
			"state Machine {", //
			"  state \"full\" as epFull <<entryPoint>>", //
			"  state \"cachedToken\" as epCached <<entryPoint>>", //
			"  state \"authorised\" as xpOk <<exitPoint>>", //
			"  state \"refused\" as xpNo <<exitPoint>>", //
			"  [*] --> InitialisingReader", //
			"  epFull --> InitialisingReader", //
			"  InitialisingReader : entry / powerReader()", //
			"  InitialisingReader : do / handshakeReader()", //
			"  InitialisingReader : exit / clearReaderBuffer()", //
			"  InitialisingReader --> AwaitingCredential : readerReady", //
			"  InitialisingReader --> xpNo : readerFault(3 tries) / postFault(F12)", //
			"  epCached --> ValidatingToken", //
			"  AwaitingCredential : do / awaitTapOrApp()", //
			"  AwaitingCredential --> AwaitingCredential : credentialSeen [!wellFormed] / beep(2)", //
			"  AwaitingCredential --> ValidatingToken : credentialCaptured / hash(credential)", //
			"  AwaitingCredential --> xpNo : idleTimeout(60 s) / showTimeout()", //
			"  ValidatingToken : entry / queryBackOffice()", //
			"  ValidatingToken : do / spinner()", //
			"  ValidatingToken --> xpOk : tokenAccepted / issueSessionId()", //
			"  ValidatingToken --> OfflineWhitelist : backOfficeUnreachable(5 s)", //
			"  state OfflineWhitelist <<junction>>", //
			"  OfflineWhitelist --> xpOk : [tokenOnWhitelist] / flagDeferredBilling()", //
			"  OfflineWhitelist --> xpNo : [else] / showTryLater()", //
			"}", //
			"state PickProfile <<choice>>", //
			"xpOk --> PickProfile", //
			"PickProfile --> Fast : [kW > 150 && degC < 35] / setLimit(500 A)", //
			"PickProfile --> Derated : [kW > 150 && degC >= 35] / setLimit(125 A)", //
			"PickProfile --> Normal : [else] / setLimit(kW)", //
			"xpNo --> Idle", //
			"@enduml");

	private static final double EPSILON = 0.5;

	/** Three arrowhead widths. */
	private static final double STEM = 24;

	/** The least distance kept from the rounded corner of a state. */
	private static final double CORNER = 8;

	/** How far off its node an edge may end: an arrowhead takes 5. */
	private static final double TOUCH = 7;

	private static final Pattern ENTITY = Pattern
			.compile("<g class=\"(?:entity|start_entity)\" data-qualified-name=\"([^\"]+)\" id=\"([^\"]+)\"[^>]*>(.*?)</g>");
	private static final Pattern LINK = Pattern
			.compile("<g class=\"link\" data-entity-1=\"([^\"]+)\" data-entity-2=\"([^\"]+)\"[^>]*>(.*?)</g>");
	private static final Pattern RECT = Pattern.compile(
			"<rect[^>]* x=\"([-\\d.]+)\" y=\"([-\\d.]+)\" width=\"([-\\d.]+)\" height=\"([-\\d.]+)\"[^>]* rx=\"12.5\"");
	private static final Pattern ELLIPSE = Pattern
			.compile("<ellipse cx=\"([-\\d.]+)\" cy=\"([-\\d.]+)\" rx=\"([-\\d.]+)\" ry=\"([-\\d.]+)\"");
	private static final Pattern POLYGON = Pattern.compile("<polygon[^>]* points=\"([^\"]+)\"");
	private static final Pattern PATH = Pattern.compile("<path[^>]* d=\"([^\"]+)\"");
	private static final Pattern NUMBER = Pattern.compile("-?\\d+(?:\\.\\d+)?");

	/** The box of a node, and whether edges must meet it on its centre lines. */
	private static final class Shape {
		private final String name;
		private final double minX, minY, maxX, maxY;
		private final boolean round;

		Shape(String name, double minX, double minY, double maxX, double maxY, boolean round) {
			this.name = name;
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
			this.round = round;
		}
	}

	@Test
	void edges_leave_states_at_right_angles() throws IOException {
		final String svg = render(DIAGRAM);
		final Map<String, Shape> shapes = shapes(svg);
		final List<String> names = new ArrayList<>();
		for (Shape shape : shapes.values())
			names.add(shape.name);
		assertTrue(names.contains("OfflineWhitelist"), "junction found in " + names);
		assertTrue(names.contains("PickProfile"), "choice found in " + names);

		int checked = 0;
		final Matcher m = LINK.matcher(svg);
		while (m.find()) {
			final Matcher mp = PATH.matcher(m.group(3));
			if (mp.find() == false)
				continue;

			final List<XPoint2D> corners = corners(mp.group(1));
			final int n = corners.size();
			final Shape start = shapes.get(m.group(1));
			final Shape end = shapes.get(m.group(2));
			if (start != null) {
				assertStem(start, corners.get(0), corners.get(1));
				checked++;
			}
			if (end != null) {
				assertStem(end, corners.get(n - 1), corners.get(n - 2));
				checked++;
			}
		}
		assertTrue(checked >= 20, "edge ends at states checked: " + checked);
	}

	private static void assertStem(Shape shape, XPoint2D end, XPoint2D next) {
		final double[] gaps = { shape.minY - end.getY(), end.getY() - shape.maxY, shape.minX - end.getX(),
				end.getX() - shape.maxX };
		int side = -1;
		for (int i = 0; i < 4; i++)
			if (Math.abs(gaps[i]) <= TOUCH && (side == -1 || Math.abs(gaps[i]) < Math.abs(gaps[side])))
				side = i;
		assertTrue(side != -1, "an edge end is off " + shape.name + " at " + end);

		final String where = "edge end at " + shape.name + " " + end;
		final boolean horizontalSide = side < 2;
		final double along = horizontalSide ? end.getX() : end.getY();
		final double min = horizontalSide ? shape.minX : shape.minY;
		final double max = horizontalSide ? shape.maxX : shape.maxY;
		if (horizontalSide)
			assertEquals(end.getX(), next.getX(), EPSILON, where + " does not leave its side at right angles");
		else
			assertEquals(end.getY(), next.getY(), EPSILON, where + " does not leave its side at right angles");

		assertTrue(end.distance(next) + Math.max(0, gaps[side]) >= STEM - EPSILON, where + " has a short stem");
		if (shape.round)
			assertEquals((min + max) / 2, along, EPSILON, where + " is off the centre line");
		else
			assertTrue(along >= min + CORNER - EPSILON && along <= max - CORNER + EPSILON,
					where + " is on a rounded corner");
	}

	/** States, and the round and diamond pseudo-states, by entity id. */
	private static Map<String, Shape> shapes(String svg) {
		final Map<String, Shape> result = new HashMap<>();
		final Matcher m = ENTITY.matcher(svg);
		while (m.find()) {
			final String name = m.group(1).replaceFirst(".*\\.", "");
			final String body = m.group(3);
			final Matcher rect = RECT.matcher(body);
			final Matcher ellipse = ELLIPSE.matcher(body);
			final Matcher polygon = POLYGON.matcher(body);
			if (rect.find()) {
				final double x = Double.parseDouble(rect.group(1));
				final double y = Double.parseDouble(rect.group(2));
				result.put(m.group(2), new Shape(name, x, y, x + Double.parseDouble(rect.group(3)),
						y + Double.parseDouble(rect.group(4)), false));
			} else if (ellipse.find() && Double.parseDouble(ellipse.group(3)) >= 6) {
				final double cx = Double.parseDouble(ellipse.group(1));
				final double cy = Double.parseDouble(ellipse.group(2));
				final double rx = Double.parseDouble(ellipse.group(3));
				final double ry = Double.parseDouble(ellipse.group(4));
				result.put(m.group(2), new Shape(name, cx - rx, cy - ry, cx + rx, cy + ry, true));
			} else if (polygon.find()) {
				final List<Double> values = numbers(polygon.group(1));
				double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
				double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
				for (int i = 0; i + 1 < values.size(); i += 2) {
					minX = Math.min(minX, values.get(i));
					maxX = Math.max(maxX, values.get(i));
					minY = Math.min(minY, values.get(i + 1));
					maxY = Math.max(maxY, values.get(i + 1));
				}
				result.put(m.group(2), new Shape(name, minX, minY, maxX, maxY, true));
			}
		}
		return result;
	}

	/** The end points of each cubic in an SVG path made of "M" then "C"s. */
	private static List<XPoint2D> corners(String d) {
		final List<Double> values = numbers(d);
		final List<XPoint2D> result = new ArrayList<>();
		result.add(new XPoint2D(values.get(0), values.get(1)));
		for (int i = 6; i + 1 < values.size(); i += 6)
			result.add(new XPoint2D(values.get(i), values.get(i + 1)));
		return result;
	}

	private static List<Double> numbers(String s) {
		final List<Double> result = new ArrayList<>();
		final Matcher m = NUMBER.matcher(s);
		while (m.find())
			result.add(Double.parseDouble(m.group()));
		return result;
	}

	private static String render(String source) throws IOException {
		// Other tests can leave these behind; either one would take the layout off
		// Graphviz and make this test vacuous.
		TitledDiagram.FORCE_SMETANA = false;
		GraphvizRuntimeEnvironment.getInstance().setDotExecutable(null);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new SourceStringReader(source, UTF_8).outputImage(baos, 0, new FileFormatOption(FileFormat.SVG, false));
		return new String(baos.toByteArray(), UTF_8);
	}

}
