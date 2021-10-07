package net.sourceforge.plantuml.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public class XmlFactories {

	private XmlFactories() {
	}

	// This class uses the "initialization-on-demand holder" idiom to provide thread-safe
	// lazy initialization of expensive factories.
	// (see https://stackoverflow.com/a/8297830/1848731)

	private static class DocumentBuilderFactoryHolder {
		static final DocumentBuilderFactory INSTANCE = DocumentBuilderFactory.newInstance();
	}

	private static class TransformerFactoryHolder {
		static final TransformerFactory INSTANCE = TransformerFactory.newInstance();
	}

	public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
		return DocumentBuilderFactoryHolder.INSTANCE.newDocumentBuilder();
	}

	public static Transformer newTransformer() throws TransformerConfigurationException {
		return TransformerFactoryHolder.INSTANCE.newTransformer();
	}
}
