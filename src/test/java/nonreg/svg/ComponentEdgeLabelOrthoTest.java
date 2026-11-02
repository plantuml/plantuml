package nonreg.svg;

import static java.nio.charset.StandardCharsets.UTF_8;
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
 * Node-style edge labels in a component diagram must work with "linetype
 * ortho": each labelled link is routed through its label node, and every
 * segment of every link stays horizontal or vertical.
 * <p>
 * Geometry is Graphviz's, so this runs through Svek (not Smetana, which has no
 * orthogonal routing and is what Vega uses) and is skipped when dot is not
 * installed.
 */
class ComponentEdgeLabelOrthoTest extends SvekSvgTest {

	private static final String PER_LINK = String.join("\n", //
			"@startuml", //
			"skinparam linetype ortho", //
			"component Client", //
			"component Server", //
			"database DB", //
			"Client -[node]-> Server : request / auth", //
			"Server --> DB : query", //
			"Client -[node]-> DB : bulk\\nload", //
			"@enduml");

	private static final String SKINPARAM = String.join("\n", //
			"@startuml", //
			"skinparam linetype ortho", //
			"skinparam componentDiagramEdgeLabelStyle node", //
			"left to right direction", //
			"component Client", //
			"component Server", //
			"database DB", //
			"Client --> Server : request / auth", //
			"Server -[nonode]-> DB : query", //
			"Client --> DB : bulk load", //
			"@enduml");

	/** A label box: drawn with no stroke and slightly rounded corners. */
	private static final Pattern LABEL = Pattern.compile("<rect [^>]*style=\"stroke:none;\" rx=\"2.5\"");
	private static final Pattern LINK = Pattern.compile("<g class=\"link\"[^>]*>(.*?)</g>");
	private static final Pattern PATH = Pattern.compile("<path[^>]* d=\"([^\"]+)\"");
	private static final Pattern NUMBER = Pattern.compile("-?\\d+(?:\\.\\d+)?");

	private static final double EPSILON = 0.5;
	private static final double RADIUS = 5;

	@Test
	void per_link_node_labels_with_ortho_keep_edges_axis_aligned() throws IOException {
		final String svg = render(PER_LINK);
		assertTrue(count(LABEL, svg) == 2, "node-style labels: " + count(LABEL, svg));
		assertAxisAligned(svg, 5);
	}

	@Test
	void skinparam_node_labels_with_ortho_and_nonode_keep_edges_axis_aligned() throws IOException {
		final String svg = render(SKINPARAM);
		// The -[nonode]-> link keeps a plain label: two of the three links get a node.
		assertTrue(count(LABEL, svg) == 2, "node-style labels: " + count(LABEL, svg));
		assertAxisAligned(svg, 3);
	}

	/**
	 * With "!pragma edgeCornerRadius 5", every bend of an ortho link becomes a small
	 * arc, no bigger than the radius, and everything between the arcs stays
	 * horizontal or vertical.
	 */
	@Test
	void per_link_node_labels_with_ortho_and_rounded_corners_keep_straight_runs_and_small_arcs()
			throws IOException {
		final String svg = render(rounded(PER_LINK));
		assertTrue(count(LABEL, svg) == 2, "node-style labels: " + count(LABEL, svg));
		assertStraightRunsAndSmallArcs(svg, 5);
	}

	@Test
	void skinparam_node_labels_with_ortho_nonode_and_rounded_corners_keep_straight_runs_and_small_arcs()
			throws IOException {
		final String svg = render(rounded(SKINPARAM));
		assertTrue(count(LABEL, svg) == 2, "node-style labels: " + count(LABEL, svg));
		assertStraightRunsAndSmallArcs(svg, 3);
	}

	private static String rounded(String source) {
		return source.replace("@startuml\n", "@startuml\n!pragma edgeCornerRadius " + RADIUS + "\n");
	}

	private static void assertStraightRunsAndSmallArcs(String svg, int minStraights) {
		int arcs = 0;
		int straights = 0;
		final Matcher m = LINK.matcher(svg);
		while (m.find()) {
			final Matcher mp = PATH.matcher(m.group(1));
			if (mp.find() == false)
				continue;
			for (double[] c : cubics(mp.group(1))) {
				final double width = spread(c, 0);
				final double height = spread(c, 1);
				if (width <= EPSILON || height <= EPSILON) {
					straights++;
				} else {
					arcs++;
					assertTrue(width <= RADIUS + EPSILON && height <= RADIUS + EPSILON,
							"corner arc " + width + " x " + height + " is bigger than the radius " + RADIUS);
				}
			}
		}
		assertTrue(arcs >= 1, "rounded corners: " + arcs);
		assertTrue(straights >= minStraights, "straight runs: " + straights);
	}

	/** Control: without ortho the same links are skewed, so the check above can fail. */
	@Test
	void without_ortho_edges_are_not_axis_aligned() throws IOException {
		final String svg = render(PER_LINK.replace("skinparam linetype ortho\n", "") //
				.replace("component Client", "skinparam componentDiagramEdgeLabelStyle node\ncomponent Client"));
		assertTrue(allSegments(svg).stream().anyMatch(s -> isAxisAligned(s[0], s[1]) == false),
				"expected a skewed segment without ortho");
	}

	private static void assertAxisAligned(String svg, int minSegments) {
		final List<XPoint2D[]> segments = allSegments(svg);
		for (XPoint2D[] s : segments)
			assertTrue(isAxisAligned(s[0], s[1]), "edge segment " + s[0] + " - " + s[1] + " is not horizontal or vertical");
		assertTrue(segments.size() >= minSegments, "edge segments checked: " + segments.size());
	}

	private static boolean isAxisAligned(XPoint2D a, XPoint2D b) {
		return Math.abs(a.getX() - b.getX()) <= EPSILON || Math.abs(a.getY() - b.getY()) <= EPSILON;
	}

	private static List<XPoint2D[]> allSegments(String svg) {
		final List<XPoint2D[]> result = new ArrayList<>();
		final Matcher m = LINK.matcher(svg);
		while (m.find()) {
			final Matcher mp = PATH.matcher(m.group(1));
			if (mp.find() == false)
				continue;
			final List<XPoint2D> corners = corners(mp.group(1));
			for (int i = 0; i + 1 < corners.size(); i++)
				result.add(new XPoint2D[] { corners.get(i), corners.get(i + 1) });
		}
		return result;
	}

	private static int count(Pattern pattern, String text) {
		int n = 0;
		final Matcher m = pattern.matcher(text);
		while (m.find())
			n++;
		return n;
	}

	/** Each cubic of an SVG path made of "M" then "C"s, as x,y of its start and then its three points. */
	private static List<double[]> cubics(String d) {
		final List<Double> values = new ArrayList<>();
		final Matcher m = NUMBER.matcher(d);
		while (m.find())
			values.add(Double.parseDouble(m.group()));
		final List<double[]> result = new ArrayList<>();
		for (int i = 0; i + 7 < values.size(); i += 6) {
			final double[] c = new double[8];
			for (int k = 0; k < 8; k++)
				c[k] = values.get(i + k);
			result.add(c);
		}
		return result;
	}

	/** Extent of the four points of a cubic along x (axis 0) or y (axis 1). */
	private static double spread(double[] cubic, int axis) {
		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;
		for (int k = axis; k < 8; k += 2) {
			min = Math.min(min, cubic[k]);
			max = Math.max(max, cubic[k]);
		}
		return max - min;
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
