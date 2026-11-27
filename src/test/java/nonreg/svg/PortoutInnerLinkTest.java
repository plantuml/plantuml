package nonreg.svg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * Issue #2457: a portout must sit on the bottom edge of its container, also
 * when it is linked to a node inside that container (<code>po --&gt; c</code>).
 * That link closes a cycle with the invisible "content -&gt; portout" ranking
 * edges, so the layout engine has to break it without leaving the port in the
 * middle of the container.
 * <p>
 * Runs through Svek (skipped when dot is not installed). The Smetana variant
 * only covers what Smetana can do today, see
 * {@link #smetanaPortoutLinkedToInnerNode()}.
 */
class PortoutInnerLinkTest extends SvekSvgTest {

	private static final String SOURCE = String.join("\n", //
			"@startuml", //
			"[i]", //
			"component C {", //
			"  portin p1", //
			"  portout po1", //
			"  portout po2", //
			"  component c1", //
			"  component c2", //
			"}", //
			"[o]", //
			"i --> p1", //
			"p1 --> c1", //
			"c1 --> c2", //
			"po1 --> c2", //
			"c2 --> po2", //
			"po2 --> o", //
			"@enduml");

	private static final double PORT_SIZE = 12;
	private static final double TOLERANCE = 1;

	@Test
	void portoutLinkedToInnerNodeStaysOnBottomEdge() throws IOException {
		assertPortsOnBottomEdge(render(SOURCE), "C.po1", "C.po2");
	}

	/**
	 * Smetana does not implement <code>constraint=false</code> (it is still
	 * UNSUPPORTED in the generated dot_init_edge), so the real link po1 --&gt; c2
	 * keeps po1 above c2. The layout must at least not fail, and the portout
	 * without an inner link must still be on the bottom edge.
	 */
	@Test
	void smetanaPortoutWithoutInnerLinkStaysOnBottomEdge() throws IOException {
		assertPortsOnBottomEdge(render(SOURCE.replace("@startuml", "@startuml\n!pragma layout smetana")), "C.po2");
	}

	@Disabled("Known limitation: Smetana ignores constraint=false, a po --> c link keeps the port above c (#2457)")
	@Test
	void smetanaPortoutLinkedToInnerNode() throws IOException {
		assertPortsOnBottomEdge(render(SOURCE.replace("@startuml", "@startuml\n!pragma layout smetana")), "C.po1",
				"C.po2");
	}

	private static void assertPortsOnBottomEdge(String svg, String... ports) {
		final double bottom = clusterBottom(svg, "C");
		for (String port : ports) {
			final double center = portTop(svg, port) + PORT_SIZE / 2;
			assertEquals(bottom, center, TOLERANCE, port + " must be centered on the bottom edge of C");
		}
	}

	private static String render(String source) throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		new SourceStringReader(source).outputImage(os, new FileFormatOption(FileFormat.SVG));
		return new String(os.toByteArray(), UTF_8);
	}

	private static double clusterBottom(String svg, String name) {
		final Matcher m = Pattern.compile("data-qualified-name=\"" + Pattern.quote(name)
				+ "\"[^>]*><rect x=\"[0-9.]+\" y=\"([0-9.]+)\" width=\"[0-9.]+\" height=\"([0-9.]+)\"").matcher(svg);
		assertTrue(m.find(), "cluster " + name + " not found");
		return Double.parseDouble(m.group(1)) + Double.parseDouble(m.group(2));
	}

	private static double portTop(String svg, String name) {
		final Matcher m = Pattern.compile("data-qualified-name=\"" + Pattern.quote(name)
				+ "\"[^>]*><text[^>]*>[^<]*</text><rect x=\"[0-9.]+\" y=\"([0-9.]+)\"").matcher(svg);
		assertTrue(m.find(), "port " + name + " not found");
		return Double.parseDouble(m.group(1));
	}
}
