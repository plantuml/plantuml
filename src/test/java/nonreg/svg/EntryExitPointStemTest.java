package nonreg.svg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
 * all points sit on the top or bottom edge, so vertically.
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

	private static final double EPSILON = 0.5;

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
