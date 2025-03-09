package net.sourceforge.plantuml.xmi;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.xml.XmlFactories;

public final class SequenceDiagramXmiMaker implements FileMaker {

	private final SequenceDiagram diagram;
	private final FileFormat fileFormat;

	public SequenceDiagramXmiMaker(SequenceDiagram sequenceDiagram, FileFormat fileFormat) {
		this.diagram = sequenceDiagram;
		this.fileFormat = fileFormat;
	}

	@Override
	public ImageData createOne(OutputStream os, int index, boolean isWithMetadata) throws IOException {
		DocumentBuilder builder;
		ImageData imageData = new ImageDataSimple(0, 0);
		try {
			builder = XmlFactories.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			Logme.error(e);
			return imageData;
		}
		Document document = builder.newDocument();
		document.setXmlVersion("1.0");
		document.setXmlStandalone(true);

		XmiSequenceDiagram xmi;
		if (fileFormat == FileFormat.XMI_ARGO)
			xmi = new XmiSequenceDiagramArgo(diagram, document);
		else
			xmi = new XmiSequenceDiagramStandard(diagram, document);

		xmi.build();

		try {
			writeDocument(document, os);
		} catch (TransformerException | ParserConfigurationException e) {
			Logme.error(e);
		}
		return imageData;
	}

	@Override
	public int getNbPages() {
		return 1;
	}

	private void writeDocument(Document document, OutputStream os)
			throws TransformerException, ParserConfigurationException {
		final Source source = new DOMSource(document);

		final Result resultat = new StreamResult(os);

		final Transformer transformer = XmlFactories.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8.name());
		transformer.transform(source, resultat);
	}

	@Override
	public void createOneGraphic(UGraphic ug) {
		throw new UnsupportedOperationException();
	}

}
