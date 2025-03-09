package nonreg.svg;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.FileFormat.SVG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SvgTest {

	public SvgTest() {
		// We want a fully portable way of non regression test, so we force the usage of
		// Smetana. It probably means that non regression tests on
		// class/component/usecase are not complete.
		TitledDiagram.FORCE_SMETANA = true;
	}

	private static final String TRIPLE_QUOTE = "\"\"\"";

	private static final Transformer xmlTransformer = createPrettyPrintTransformer();

	protected void checkXmlAndDescription(final String expectedDescription)
			throws IOException {
		String actual = stripExtraneousAndUnpredictableNodes(runPlantUML(expectedDescription));

		final String expected = readStringFromSourceFile(getDiagramFile(), "{{{", "}}}");

		assertEquals(normaliseXml(expected), normaliseXml(actual));

		if (! sortString(actual).equals(sortString(expected))) {
			assertEquals(expected, actual, "Generated Svg is not ok");
		}
	}

	private String stripExtraneousAndUnpredictableNodes(String svgXML) {
		Document document = parseXML(svgXML);
		removeElementsByXPath(document,
				"/svg:svg/@style",
				"//svg:script/text()",
				"//svg:style/text()",
				"//@x",
				"//@y",
				"//@x1",
				"//@x2",
				"//@y1",
				"//@y2",
				"//@cx",
				"//@cy",
				"//@rx",
				"//@ry",
				"//@width",
				"//@height",
				"//@textLength",
				"//@d",
				"//@points",
				"//@viewBox"
		);
		return convertDocumentToString(document);
	}

	private Document parseXML(String xml) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse XML [" + xml + "]", e);
		}
	}

	private void removeElementsByXPath(Document document, String ...xPathExpressions) {
		XPath xPath = getSvgAwareXPath();

		for (String xPathExpression : xPathExpressions) {
			try {
				NodeList matchingNodes = (NodeList) xPath.evaluate(xPathExpression, document, XPathConstants.NODESET);

				for (int i = 0; i < matchingNodes.getLength(); i++) {
					Node matchingNode = matchingNodes.item(i);

					if (matchingNode.getNodeType() == Node.ATTRIBUTE_NODE) {
						Attr attr = (Attr) matchingNode;
						attr.getOwnerElement().removeAttributeNode(attr);
					} else {
						matchingNode.getParentNode().removeChild(matchingNode);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Failed to execute xpath: " + xPathExpression, e);
			}
		}
	}

	private static XPath getSvgAwareXPath() {
		XPath xPath = XPathFactory.newInstance().newXPath();

		xPath.setNamespaceContext(new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				if ("svg".equals(prefix)) {
					return "http://www.w3.org/2000/svg";  // SVG namespace URI
				}
				return null;
			}

			public String getPrefix(String uri) {
				return null;
			}

			public Iterator<String> getPrefixes(String uri) {
				return null;
			}
		});

		return xPath;
	}

	private String convertDocumentToString(Document document) {
		try {
			StringWriter writer = new StringWriter();
			xmlTransformer.transform(new DOMSource(document), new StreamResult(writer));
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException("Failed to convert XML Document to String.");
		}
	}

	private String normaliseXml(String originalXml)  {
		return convertDocumentToString(parseXML(originalXml));
	}

	private String sortString(String s) {
		final Map<Character, AtomicInteger> map = new TreeMap<>();
		for (int i = 0; i < s.length(); i++) {
			final char ch = s.charAt(i);
			// We ignore non-writable characters
			if (ch <= ' ')
				continue;

			AtomicInteger count = map.get(ch);
			if (count == null)
				map.put(ch, new AtomicInteger(1));
			else
				count.addAndGet(1);
		}
		return map.toString();
	}

	private String getLocalFolder() {
		return "src/test/java/" + getPackageName().replace(".", "/");
	}

	private String getPackageName() {
		return getClass().getPackage().getName();
	}

	private Path getDiagramFile() {
		return Paths.get(getLocalFolder(), getClass().getSimpleName() + ".java");
	}

	private String runPlantUML(String expectedDescription)
			throws IOException {
		final String diagramText = readStringFromSourceFile(getDiagramFile(), TRIPLE_QUOTE, TRIPLE_QUOTE);
		final SourceStringReader ssr = new SourceStringReader(diagramText, UTF_8);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DiagramDescription diagramDescription = ssr.outputImage(baos, 0, new FileFormatOption(SVG, false));
		assertEquals(expectedDescription, diagramDescription.getDescription(), "Bad description");

		return new String(baos.toByteArray(), UTF_8);
	}

	private String readStringFromSourceFile(Path path, String startMarker, String endMarker) throws IOException {
		assertTrue(Files.exists(path), "Cannot find " + path);
		assertTrue(Files.isReadable(path), "Cannot read " + path);
		final List<String> allLines = Files.readAllLines(path, UTF_8);
		final int first = allLines.indexOf(startMarker);
		final int last = allLines.lastIndexOf(endMarker);
		assertTrue(first != -1);
		assertTrue(last != -1);
		assertTrue(last > first);
		return packString(allLines.subList(first + 1, last));
	}

	private String packString(Collection<String> list) {
		final StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}

	private static Transformer createPrettyPrintTransformer() {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new StringReader(
					"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" +
							"    <xsl:strip-space elements=\"*\"/>" +
							"    <xsl:output method=\"xml\" encoding=\"UTF-8\"/>" +
							"    <xsl:template match=\"@*|node()\">" +
							"        <xsl:copy>" +
							"            <xsl:apply-templates select=\"@*|node()\"/>" +
							"        </xsl:copy>" +
							"    </xsl:template>" +
							"</xsl:stylesheet>")));
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			return transformer;
		} catch (Exception e) {
			throw new RuntimeException("Cannot initialise transformer", e);
		}
	}
}