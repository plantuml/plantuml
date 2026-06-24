package net.sourceforge.plantuml.xml;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public class XmlFactories {

	private XmlFactories() {
	}

	// This class uses the "initialization-on-demand holder" idiom to provide
	// thread-safe
	// lazy initialization of expensive factories.
	// (see https://stackoverflow.com/a/8297830/1848731)

	private static class DocumentBuilderFactoryHolder {
		static final DocumentBuilderFactory INSTANCE = newHardenedDocumentBuilderFactory();

		// Harden against XXE / billion-laughs / external schema fetch (CWE-611/CWE-776).
		// PlantUML is often deployed as a server-side rendering service where the parsed
		// XML (SVG, diagram fragments) may be attacker-controlled.
		private static DocumentBuilderFactory newHardenedDocumentBuilderFactory() {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
				factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
				factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				factory.setXIncludeAware(false);
				factory.setExpandEntityReferences(false);
			} catch (Exception ignored) {
				// best-effort: not all parsers support every feature
			}
			return factory;
		}
	}

	private static class TransformerFactoryHolder {
		static final TransformerFactory INSTANCE = newHardenedTransformerFactory();

		private static TransformerFactory newHardenedTransformerFactory() {
			final TransformerFactory factory = TransformerFactory.newInstance();
			try {
				factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
				factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
				factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
			} catch (Exception ignored) {
				// best-effort: not all transformers support every attribute
			}
			return factory;
		}
	}

	public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
		return DocumentBuilderFactoryHolder.INSTANCE.newDocumentBuilder();
	}

	public static Transformer newTransformer() throws TransformerConfigurationException {
		return TransformerFactoryHolder.INSTANCE.newTransformer();
	}
}
