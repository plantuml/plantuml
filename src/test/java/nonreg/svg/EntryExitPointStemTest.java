package nonreg.svg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

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
 * With "linetype ortho", Graphviz ignores the port that pins an edge to an
 * entry/exit point's circle and reaches the (wider) node anywhere on its box:
 * off-centre, or running along the frame. Every edge end at such a point must
 * meet the circle's centre perpendicular to the frame edge it sits on - here,
 * all points sit on the top or bottom edge, so vertically. An edge from outside
 * the state comes in from outside, turning round the point's label if it has
 * to, and the label moves aside so no edge runs through it. Only an edge going
 * straight across to something beside the point, about level with it, keeps
 * meeting it from the side.
 * <p>
 * Geometry is Graphviz's, so this runs through Svek (not Smetana, which has no
 * orthogonal routing) and is skipped when dot is not installed.
 */
class EntryExitPointStemTest extends SvekSvgTest {

	private static final String DIAGRAM = String.join("\n", //
			"@startuml", //
			"skinparam linetype ortho", //
			"state Machine {", //
			"  state \"full\" as epFull <<entryPoint>>", //
			"  state \"cachedToken\" as epCached <<entryPoint>>", //
			"  state \"authorised\" as xpOk <<exitPoint>>", //
			"  state \"refused\" as xpNo <<exitPoint>>", //
			"  [*] --> Initialising", //
			"  epFull --> Initialising", //
			"  Initialising --> Awaiting : ready", //
			"  Initialising --> xpNo : fault", //
			"  epCached --> Validating", //
			"  Awaiting --> Validating : captured", //
			"  Awaiting --> xpNo : timeout", //
			"  Validating --> xpOk : accepted", //
			"  Validating --> xpNo : rejected", //
			"}", //
			"@enduml");

	/**
	 * {@code Idle} ends up below {@code Session}, so the edges from it to the
	 * entry points on top come from below and have to turn round to reach them
	 * from above.
	 */
	private static final String FROM_OUTSIDE = String.join("\n", //
			"@startuml", //
			"skinparam linetype ortho", //
			"skinparam stateDiagramEdgeLabelStyle node", //
			"hide empty description", //
			"state Session {", //
			"  state \"card\" as epCard <<entryPoint>>", //
			"  state \"app\" as epApp <<entryPoint>>", //
			"  state \"done\" as xpDone <<exitPoint>>", //
			"  state \"aborted\" as xpAbort <<exitPoint>>", //
			"  epCard --> Reading", //
			"  epApp --> Validating", //
			"  Reading --> Validating : cardRead", //
			"  Validating --> Charging : accepted", //
			"  Validating --> xpAbort : refused", //
			"  Charging --> xpDone : full", //
			"  Charging --> xpAbort : unplugged", //
			"}", //
			"Idle --> epCard : cardTapped", //
			"Idle --> epApp : appStart", //
			"xpDone --> Idle", //
			"xpAbort --> Idle : / showError()", //
			"@enduml");

	/** The label of each entry/exit point of {@link #FROM_OUTSIDE}. */
	private static final String[][] POINT_LABELS = { { "epCard", "card" }, { "epApp", "app" },
			{ "xpDone", "done" }, { "xpAbort", "aborted" } };

	private static final double EPSILON = 0.5;

	/** How far above its baseline a 14pt label reaches. */
	private static final double ASCENT = 13;

	private static final Pattern TEXT = Pattern
			.compile("<text x=\"([-\\d.]+)\" y=\"([-\\d.]+)\"[^>]*textLength=\"([-\\d.]+)\"[^>]*>([^<]*)</text>");

	private static final Pattern PATH = Pattern.compile("<path d=\"([^\"]+)\"[^>]*id=\"([^\"]+)\"");
	private static final Pattern POINT = Pattern.compile("<ellipse cx=\"([-\\d.]+)\" cy=\"([-\\d.]+)\" rx=\"6\"");
	private static final Pattern NUMBER = Pattern.compile("-?\\d+(?:\\.\\d+)?");

	@Test
	void edges_meet_entry_and_exit_points_centred_and_perpendicular() throws IOException {
		final String svg = render(DIAGRAM);
		final List<XPoint2D> circles = new ArrayList<>();
		final Matcher mc = POINT.matcher(svg);
		while (mc.find())
			circles.add(new XPoint2D(Double.parseDouble(mc.group(1)), Double.parseDouble(mc.group(2))));
		assertEquals(4, circles.size(), "entry/exit point circles");

		int checked = 0;
		final Matcher mp = PATH.matcher(svg);
		while (mp.find()) {
			final List<XPoint2D> corners = corners(mp.group(1));
			final String[] ends = mp.group(2).split("-to-");
			if (isPoint(ends[0])) {
				assertStem(mp.group(2), corners.get(0), corners.get(1), circles);
				checked++;
			}
			if (isPoint(ends[1])) {
				final int n = corners.size();
				assertStem(mp.group(2), corners.get(n - 1), corners.get(n - 2), circles);
				checked++;
			}
		}
		assertEquals(6, checked, "edge ends at entry/exit points");
	}

	@Test
	void edges_from_outside_meet_entry_and_exit_points_from_outside_and_clear_of_their_labels()
			throws IOException {
		final String svg = render(FROM_OUTSIDE);
		// Older Graphviz can crash in its orthogonal router, whatever PlantUML does
		// with the edges afterwards.
		assumeFalse(svg.contains("UnparsableGraphvizException"), "Graphviz crashed on this diagram");

		final List<XPoint2D> circles = new ArrayList<>();
		final Matcher mc = POINT.matcher(svg);
		while (mc.find())
			circles.add(new XPoint2D(Double.parseDouble(mc.group(1)), Double.parseDouble(mc.group(2))));
		assertEquals(4, circles.size(), "entry/exit point circles");

		final Map<String, double[]> labels = new HashMap<>();
		final Matcher mt = TEXT.matcher(svg);
		while (mt.find())
			for (String[] label : POINT_LABELS)
				if (label[1].equals(mt.group(4))) {
					final double x = Double.parseDouble(mt.group(1));
					final double y = Double.parseDouble(mt.group(2));
					labels.put(label[0], new double[] { x, y - ASCENT, x + Double.parseDouble(mt.group(3)), y });
				}
		assertEquals(POINT_LABELS.length, labels.size(), "entry/exit point labels");

		int checked = 0;
		final Matcher mp = PATH.matcher(svg);
		while (mp.find()) {
			final List<XPoint2D> corners = corners(mp.group(1));
			// The half of an edge going into its label node has no arrow, and is
			// named "source-label" rather than "source-to-label".
			final String[] ends = mp.group(2).contains("-to-") ? mp.group(2).split("-to-")
					: mp.group(2).split("-", 2);
			final boolean sideways = corners.size() == 2
					&& Math.abs(corners.get(0).getY() - corners.get(1).getY()) < EPSILON;
			if (sideways == false && isPoint(ends[0])) {
				assertStem(mp.group(2), corners.get(0), corners.get(1), circles);
				checked++;
			}
			if (sideways == false && isPoint(ends[1])) {
				final int n = corners.size();
				assertStem(mp.group(2), corners.get(n - 1), corners.get(n - 2), circles);
				checked++;
			}
			// An edge meeting a point on its centre line, from the label's side,
			// must find the label moved out of its way.
			for (String end : ends) {
				final double[] label = labels.get(end);
				if (label != null)
					for (int i = 0; i + 1 < corners.size(); i++)
						assertTrue(crosses(corners.get(i), corners.get(i + 1), label) == false,
								mp.group(2) + " runs through the label of " + end);
			}
		}
		assertTrue(checked >= 8, checked + " edge ends at entry/exit points checked");
	}

	/** Whether the segment from {@code a} to {@code b} runs through {@code box}. */
	private static boolean crosses(XPoint2D a, XPoint2D b, double[] box) {
		return Math.max(a.getX(), b.getX()) > box[0] && Math.min(a.getX(), b.getX()) < box[2]
				&& Math.max(a.getY(), b.getY()) > box[1] && Math.min(a.getY(), b.getY()) < box[3];
	}

	private static boolean isPoint(String name) {
		return name.startsWith("ep") || name.startsWith("xp");
	}

	private static void assertStem(String id, XPoint2D end, XPoint2D next, List<XPoint2D> circles) {
		XPoint2D circle = circles.get(0);
		for (XPoint2D c : circles)
			if (c.distance(end) < circle.distance(end))
				circle = c;

		assertEquals(circle.getX(), end.getX(), EPSILON, id + " is not centred on its point");
		assertEquals(end.getX(), next.getX(), EPSILON, id + " does not meet its point vertically");
		assertTrue(Math.abs(end.getY() - next.getY()) > EPSILON, id + " has an empty stem");
	}

	/** The end points of each cubic in an SVG path made of "M" then "C"s. */
	private static List<XPoint2D> corners(String d) {
		final List<Double> values = new ArrayList<>();
		final Matcher m = NUMBER.matcher(d);
		while (m.find())
			values.add(Double.parseDouble(m.group()));

		final List<XPoint2D> result = new ArrayList<>();
		result.add(new XPoint2D(values.get(0), values.get(1)));
		for (int i = 6; i + 1 < values.size(); i += 6)
			result.add(new XPoint2D(values.get(i), values.get(i + 1)));
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
