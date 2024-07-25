/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.xmi;

import static java.nio.charset.StandardCharsets.UTF_8;

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
import org.w3c.dom.Element;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.xml.XmlFactories;

public abstract class XmiDescriptionDiagramAbstract implements XmlDiagramTransformer {

	protected final DescriptionDiagram diagram;
	protected final Document document;
	protected final Element ownedElement;

	public XmiDescriptionDiagramAbstract(DescriptionDiagram diagram) throws ParserConfigurationException {
		this.diagram = diagram;

		final DocumentBuilder builder = XmlFactories.newDocumentBuilder();
		this.document = builder.newDocument();
		document.setXmlVersion("1.0");
		document.setXmlStandalone(true);

		final Element xmi = document.createElement("XMI");
		xmi.setAttribute("xmi.version", "1.1");
		xmi.setAttribute("xmlns:UML", "href://org.omg/UML/1.3");
		document.appendChild(xmi);

		final Element header = document.createElement("XMI.header");
		xmi.appendChild(header);

		final Element metamodel = document.createElement("XMI.metamodel");
		metamodel.setAttribute("xmi.name", "UML");
		metamodel.setAttribute("xmi.version", "1.3");
		header.appendChild(metamodel);

		final Element content = document.createElement("XMI.content");
		xmi.appendChild(content);

		final Element model = document.createElement("UML:Model");
		model.setAttribute("xmi.id", CucaDiagramXmiMaker.getModel(diagram));
		model.setAttribute("name", "PlantUML");
		content.appendChild(model);

		this.ownedElement = document.createElement("UML:Namespace.ownedElement");
		model.appendChild(ownedElement);

		for (final Entity gr : diagram.groups())
			if (gr.getParentContainer().isRoot())
				addElement(gr, ownedElement);

		for (final Entity ent : diagram.leafs())
			if (ent.getParentContainer().isRoot())
				addElement(ent, ownedElement);

		for (final Link link : diagram.getLinks())
			addLink(link);

	}

	protected void addElement(final Entity tobeAdded, Element container) {
		final Element element = createEntityNode(tobeAdded);
		container.appendChild(element);
		for (final Entity ent : diagram.groups())
			if (ent.getParentContainer() == tobeAdded)
				addElement(ent, element);

		for (final Entity ent : diagram.leafs())
			if (ent.getParentContainer() == tobeAdded)
				addElement(ent, element);

	}

	protected abstract void addLink(Link link);

	protected Element createEntityNode(Entity entity) {
		final Element cla = document.createElement("UML:Component");

		cla.setAttribute("xmi.id", entity.getUid());
		cla.setAttribute("name", entity.getDisplay().get(0).toString());
		cla.setAttribute("namespace", CucaDiagramXmiMaker.getModel(diagram));

		final Element feature = document.createElement("UML:Classifier.feature");
		cla.appendChild(feature);

		return cla;
	}

	public void transformerXml(OutputStream os) throws TransformerException, ParserConfigurationException {
		final Source source = new DOMSource(document);

		final Result resultat = new StreamResult(os);

		final Transformer transformer = XmlFactories.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8.name());
		// tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(source, resultat);
	}

	public static String forXMI(String s) {
		return s.replace(':', ' ');
	}

	public static String forXMI(Display s) {
		return s.get(0).toString().replace(':', ' ');
	}
}
