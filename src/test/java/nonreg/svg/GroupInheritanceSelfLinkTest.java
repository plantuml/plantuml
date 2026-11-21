package nonreg.svg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;

/**
 * Both ends of a self association must reach the visible class inside the padded
 * node used for grouped inheritance. Vega uses Smetana, so this regression needs
 * the native Graphviz path.
 */
class GroupInheritanceSelfLinkTest extends SvekSvgTest {

	private static final String DIAGRAM = String.join("\n", //
			"@startuml", //
			"skinparam groupInheritance 2", //
			"hide empty members", //
			"hide circle", //
			"Usuario <|-- Cliente", //
			"Usuario <|-- Administrador : {completa,disjunta}", //
			"Usuario \"0..*\" -- \"0..*\" Usuario : recomienda >", //
			"@enduml");
	private static final Pattern NUMBER = Pattern.compile("-?\\d+(?:\\.\\d+)?");
	private static final double EPSILON = 0.75;

	@Test
	void grouped_self_link_connects_both_ends() throws Exception {
		assertSelfLinkConnected(DIAGRAM);
	}

	@Test
	void horizontal_grouped_self_link_connects_both_ends() throws Exception {
		assertSelfLinkConnected(DIAGRAM.replace("@startuml\n", "@startuml\nleft to right direction\n"));
	}

	@Test
	void ungrouped_self_link_still_connects_both_ends() throws Exception {
		assertSelfLinkConnected(DIAGRAM.replace("skinparam groupInheritance 2\n", ""));
	}

	@Test
	void self_link_below_grouping_threshold_still_connects_both_ends() throws Exception {
		assertSelfLinkConnected(DIAGRAM.replace("groupInheritance 2", "groupInheritance 3"));
	}

	private static void assertSelfLinkConnected(String source) throws Exception {
		// Other tests can leave either setting on a non-Graphviz layout path.
		TitledDiagram.FORCE_SMETANA = false;
		GraphvizRuntimeEnvironment.getInstance().setDotExecutable(null);
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		new SourceStringReader(source, UTF_8).outputImage(output, 0, new FileFormatOption(FileFormat.SVG, false));
		final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new ByteArrayInputStream(output.toByteArray()));
		final Element entity = find(document, "g", "data-qualified-name", "Usuario");
		final Element rectangle = (Element) entity.getElementsByTagName("rect").item(0);
		assertNotNull(rectangle, "visible Usuario rectangle");
		final XRectangle2D bounds = new XRectangle2D(number(rectangle, "x"), number(rectangle, "y"),
				number(rectangle, "width"), number(rectangle, "height"));
		final Element path = find(document, "path", "id", "Usuario-Usuario");
		final List<Double> coordinates = new ArrayList<>();
		final Matcher matcher = NUMBER.matcher(path.getAttribute("d"));
		while (matcher.find())
			coordinates.add(Double.parseDouble(matcher.group()));
		assertTrue(coordinates.size() >= 8, "self association has a complete curve");
		final XPoint2D start = new XPoint2D(coordinates.get(0), coordinates.get(1));
		final int last = coordinates.size() - 2;
		final XPoint2D end = new XPoint2D(coordinates.get(last), coordinates.get(last + 1));
		assertEndpointConnected(document, bounds, start, "start");
		assertEndpointConnected(document, bounds, end, "end");
	}

	private static void assertEndpointConnected(Document document, XRectangle2D bounds, XPoint2D point,
			String endpoint) {
		int connectors = 0;
		final NodeList lines = document.getElementsByTagName("line");
		for (int i = 0; i < lines.getLength(); i++) {
			final Element line = (Element) lines.item(i);
			final XPoint2D a = new XPoint2D(number(line, "x1"), number(line, "y1"));
			final XPoint2D b = new XPoint2D(number(line, "x2"), number(line, "y2"));
			if ((point.distance(a) < 0.05 && onBorder(bounds, b))
					|| (point.distance(b) < 0.05 && onBorder(bounds, a)))
				connectors++;
		}
		assertEquals(onBorder(bounds, point) ? 0 : 1, connectors,
				endpoint + " must reach the visible class directly or through exactly one connector");
	}

	private static boolean onBorder(XRectangle2D rect, XPoint2D point) {
		final double x = point.getX();
		final double y = point.getY();
		return x >= rect.getMinX() - EPSILON && x <= rect.getMaxX() + EPSILON
				&& y >= rect.getMinY() - EPSILON && y <= rect.getMaxY() + EPSILON
				&& (Math.abs(x - rect.getMinX()) <= EPSILON || Math.abs(x - rect.getMaxX()) <= EPSILON
						|| Math.abs(y - rect.getMinY()) <= EPSILON || Math.abs(y - rect.getMaxY()) <= EPSILON);
	}

	private static double number(Element element, String attribute) {
		return Double.parseDouble(element.getAttribute(attribute));
	}

	private static Element find(Document document, String tag, String attribute, String value) {
		final NodeList elements = document.getElementsByTagName(tag);
		for (int i = 0; i < elements.getLength(); i++) {
			final Element element = (Element) elements.item(i);
			if (value.equals(element.getAttribute(attribute)))
				return element;
		}
		throw new AssertionError("Missing " + tag + " with " + attribute + "=" + value);
	}
}
