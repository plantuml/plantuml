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
 * With "linetype ortho" and node-style edge labels, Graphviz may leave or reach
 * a label from its left or right side, at mid-height, and turn straight away.
 * Once the edge is realigned on the centre of the label, no edge may run
 * through the label text: not one passing through the label, and not a loop
 * going back to the state it came from.
 * <p>
 * Geometry is Graphviz's, so this runs through Svek (not Smetana, which has no
 * orthogonal routing) and is skipped when dot is not installed.
 */
class StateEdgeLabelClearTest extends SvekSvgTest {

	private static final String DIAGRAM = String.join("\n", //
			"@startuml", //
			"skinparam linetype ortho", //
			"skinparam stateDiagramEdgeLabelStyle node", //
			"hide empty description", //
			"state Active {", //
			"  [*] --> Idle", //
			"  Idle : SEQEN may be set", //
			"  Idle --> Running : TRIGSEQ (host or GPIO)\\nwith SEQEN = 1", //
			"  Running : start address and length latched", //
			"  Idle <-- Running : sequence clears SEQEN\\n(ENDSEQ) or runs off its length", //
			"  Idle <-- Running : host writes SEQEN = 0\\n(no ENDSEQ)", //
			"  Running --> Running : trigger ignored,\\nFLAG31 raised", //
			"  Idle --> Idle : trigger with SEQEN = 0\\nignored", //
			"}", //
			"Active --> Reset : RESET low", //
			"@enduml");

	/** A label box: drawn with no stroke and slightly rounded corners. */
	private static final Pattern LABEL = Pattern.compile(
			"<rect x=\"([-\\d.]+)\" y=\"([-\\d.]+)\" width=\"([-\\d.]+)\" height=\"([-\\d.]+)\" fill=\"none\" style=\"stroke:none;\" rx=\"2.5\"");
	private static final Pattern LINK = Pattern.compile("<g class=\"link\"[^>]*>(.*?)</g>");
	private static final Pattern PATH = Pattern.compile("<path[^>]* d=\"([^\"]+)\"");
	private static final Pattern NUMBER = Pattern.compile("-?\\d+(?:\\.\\d+)?");

	/** How far inside a label box an edge may touch it. */
	private static final double TOUCH = 1;

	@Test
	void edges_keep_out_of_multi_line_labels() throws IOException {
		final String svg = render(DIAGRAM);

		final List<double[]> labels = new ArrayList<>();
		final Matcher ml = LABEL.matcher(svg);
		while (ml.find()) {
			final double x = Double.parseDouble(ml.group(1));
			final double y = Double.parseDouble(ml.group(2));
			labels.add(new double[] { x, y, x + Double.parseDouble(ml.group(3)),
					y + Double.parseDouble(ml.group(4)) });
		}
		assertTrue(labels.size() >= 5, "labels found: " + labels.size());

		int segments = 0;
		final Matcher m = LINK.matcher(svg);
		while (m.find()) {
			final Matcher mp = PATH.matcher(m.group(1));
			if (mp.find() == false)
				continue;
			final List<XPoint2D> corners = corners(mp.group(1));
			for (int i = 0; i + 1 < corners.size(); i++) {
				segments++;
				for (double[] label : labels)
					assertTrue(crosses(corners.get(i), corners.get(i + 1), label) == false,
							"edge segment " + corners.get(i) + " - " + corners.get(i + 1)
									+ " runs through the label at " + label[0] + "," + label[1]);
			}
		}
		assertTrue(segments >= 10, "edge segments checked: " + segments);
	}

	/**
	 * Whether the horizontal or vertical segment {@code a}-{@code b} runs inside
	 * {@code box}, more than {@link #TOUCH} away from its border.
	 */
	private static boolean crosses(XPoint2D a, XPoint2D b, double[] box) {
		final double minX = Math.max(Math.min(a.getX(), b.getX()), box[0] + TOUCH);
		final double maxX = Math.min(Math.max(a.getX(), b.getX()), box[2] - TOUCH);
		final double minY = Math.max(Math.min(a.getY(), b.getY()), box[1] + TOUCH);
		final double maxY = Math.min(Math.max(a.getY(), b.getY()), box[3] - TOUCH);
		return minX <= maxX && minY <= maxY;
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
