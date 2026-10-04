package test.vega;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Strips position-dependent and unpredictable attributes from SVG
 * output so that the structural content can be compared across
 * environments.
 *
 * <p>Logic taken from {@code nonreg.svg.SvgTest}.
 */
class SvgCleaner {

	private static final Transformer XML_TRANSFORMER = createPrettyPrintTransformer();

	// Looking the factory up (a service lookup) for every SVG was a measurable share of the suite.
	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = createDocumentBuilderFactory();

	private static DocumentBuilderFactory createDocumentBuilderFactory() {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		return factory;
	}

	/**
	 * Cleans the raw SVG string: removes processing instructions and
	 * all coordinate / dimension attributes that vary between platforms.
	 */
	static String clean(String svgXml) {
		final Document document = parseXml(svgXml);
		removeProcessingInstructions(document);
//		removeElementsByXPath(document,
//				"/svg:svg/@style",
//				"//svg:script/text()",
//				"//svg:style/text()",
//				"//@x",
//				"//@y",
//				"//@x1",
//				"//@x2",
//				"//@y1",
//				"//@y2",
//				"//@cx",
//				"//@cy",
//				"//@rx",
//				"//@ry",
//				"//@width",
//				"//@height",
//				"//@textLength",
//				"//@d",
//				"//@points",
//				"//@viewBox"
//		);
		return toNormalisedString(document);
	}

	/**
	 * Normalises an SVG string (parse + pretty-print) so that
	 * whitespace differences are eliminated before comparison.
	 */
	static String normalise(String svgXml) {
		return toNormalisedString(parseXml(svgXml));
	}

	// ----------------------------------------------------------

	private static Document parseXml(String xml) {
		try {
			final DocumentBuilder builder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse XML", e);
		}
	}

	private static String toNormalisedString(Document document) {
		try {
			final StringWriter writer = new StringWriter();
			XML_TRANSFORMER.transform(new DOMSource(document), new StreamResult(writer));
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException("Failed to convert XML Document to String", e);
		}
	}

	private static void removeProcessingInstructions(Node parent) {
		final NodeList children = parent.getChildNodes();
		final List<Node> toRemove = new ArrayList<>();
		for (int i = 0; i < children.getLength(); i++) {
			final Node child = children.item(i);
			if (child.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE)
				toRemove.add(child);
			else if (child.getNodeType() == Node.ELEMENT_NODE)
				removeProcessingInstructions(child);
		}
		for (final Node node : toRemove)
			parent.removeChild(node);
	}

	private static void removeElementsByXPath(Document document, String... xPathExpressions) {
		final XPath xPath = createSvgAwareXPath();

		for (final String expression : xPathExpressions) {
			try {
				final NodeList nodes = (NodeList) xPath.evaluate(expression, document, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) {
					final Node node = nodes.item(i);
					if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
						final Attr attr = (Attr) node;
						attr.getOwnerElement().removeAttributeNode(attr);
					} else {
						node.getParentNode().removeChild(node);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Failed to execute xpath: " + expression, e);
			}
		}
	}

	private static XPath createSvgAwareXPath() {
		final XPath xPath = XPathFactory.newInstance().newXPath();
		xPath.setNamespaceContext(new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				if ("svg".equals(prefix))
					return "http://www.w3.org/2000/svg";
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

	private static Transformer createPrettyPrintTransformer() {
		try {
			final Transformer transformer = TransformerFactory.newInstance()
					.newTransformer(new StreamSource(new StringReader(
							"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
									+ "    <xsl:strip-space elements=\"*\"/>"
									+ "    <xsl:output method=\"xml\" encoding=\"UTF-8\"/>"
									+ "    <xsl:template match=\"@*|node()\">"
									+ "        <xsl:copy>"
									+ "            <xsl:apply-templates select=\"@*|node()\"/>"
									+ "        </xsl:copy>"
									+ "    </xsl:template>"
									+ "</xsl:stylesheet>")));
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			return transformer;
		} catch (Exception e) {
			throw new RuntimeException("Cannot initialise transformer", e);
		}
	}

}
