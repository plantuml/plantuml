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
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.xml.XmlFactories;

public class XmiDescriptionDiagram implements XmlDiagramTransformer {

	private final DescriptionDiagram diagram;
	private final Document document;
	private final Element ownedElement;

	public XmiDescriptionDiagram(DescriptionDiagram diagram) throws ParserConfigurationException {
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

		for (final Entity gr : diagram.getEntityFactory().groups())
			if (gr.getParentContainer().isRoot())
				addElement(gr, ownedElement);

		for (final Entity ent : diagram.getEntityFactory().leafs())
			if (ent.getParentContainer().isRoot())
				addElement(ent, ownedElement);

		for (final Link link : diagram.getLinks())
			addLink(link);

	}

	private void addElement(final Entity tobeAdded, Element container) {
		final Element element = createEntityNode(tobeAdded);
		container.appendChild(element);
		for (final Entity ent : diagram.getEntityFactory().groups())
			if (ent.getParentContainer() == tobeAdded)
				addElement(ent, element);

		for (final Entity ent : diagram.getEntityFactory().leafs())
			if (ent.getParentContainer() == tobeAdded)
				addElement(ent, element);

	}

	public static String forXMI(String s) {
		return s.replace(':', ' ');
	}

	public static String forXMI(Display s) {
		return s.get(0).toString().replace(':', ' ');
	}

	private void addLink(Link link) {
		final String assId = "ass" + diagram.getUniqueSequence();

		final Element association = document.createElement("UML:Association");
		association.setAttribute("xmi.id", assId);
		association.setAttribute("namespace", CucaDiagramXmiMaker.getModel(diagram));
		if (Display.isNull(link.getLabel()) == false)
			association.setAttribute("name", forXMI(link.getLabel()));

		final Element connection = document.createElement("UML:Association.connection");
		final Element end1 = document.createElement("UML:AssociationEnd");
		end1.setAttribute("xmi.id", "end" + diagram.getUniqueSequence());
		end1.setAttribute("association", assId);
		end1.setAttribute("type", link.getEntity1().getUid());
		if (link.getQuantifier1() != null)
			end1.setAttribute("name", forXMI(link.getQuantifier1()));

		final Element endparticipant1 = document.createElement("UML:AssociationEnd.participant");

		if (link.getType().getDecor2() == LinkDecor.COMPOSITION)
			end1.setAttribute("aggregation", "composite");

		if (link.getType().getDecor2() == LinkDecor.AGREGATION)
			end1.setAttribute("aggregation", "aggregate");

		end1.appendChild(endparticipant1);
		connection.appendChild(end1);

		final Element end2 = document.createElement("UML:AssociationEnd");
		end2.setAttribute("xmi.id", "end" + diagram.getUniqueSequence());
		end2.setAttribute("association", assId);
		end2.setAttribute("type", link.getEntity2().getUid());
		if (link.getQuantifier2() != null)
			end2.setAttribute("name", forXMI(link.getQuantifier2()));

		final Element endparticipant2 = document.createElement("UML:AssociationEnd.participant");
		if (link.getType().getDecor1() == LinkDecor.COMPOSITION)
			end2.setAttribute("aggregation", "composite");

		if (link.getType().getDecor1() == LinkDecor.AGREGATION)
			end2.setAttribute("aggregation", "aggregate");

		end2.appendChild(endparticipant2);
		connection.appendChild(end2);

		association.appendChild(connection);

		ownedElement.appendChild(association);

	}

	private Element createEntityNode(Entity entity) {
		final Element cla = document.createElement("UML:Component");

		cla.setAttribute("xmi.id", entity.getUid());
		cla.setAttribute("name", entity.getDisplay().get(0).toString());
		cla.setAttribute("namespace", CucaDiagramXmiMaker.getModel(diagram));

		final Element feature = document.createElement("UML:Classifier.feature");
		cla.appendChild(feature);

//		for (Member m : entity.getBodier().getFieldsToDisplay()) {
//			final Element attribute = document.createElement("UML:Attribute");
//			attribute.setAttribute("xmi.id", "att" + UniqueSequence.getValue());
//			attribute.setAttribute("name", m.getDisplay(false));
//			feature.appendChild(attribute);
//		}
//
//		for (Member m : entity.getBodier().getMethodsToDisplay()) {
//			final Element operation = document.createElement("UML:Operation");
//			operation.setAttribute("xmi.id", "att" + UniqueSequence.getValue());
//			operation.setAttribute("name", m.getDisplay(false));
//			feature.appendChild(operation);
//		}
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

}
