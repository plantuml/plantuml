/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 5616 $
 *
 */
package net.sourceforge.plantuml.xmi;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.utils.UniqueSequence;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmiClassDiagramStar implements IXmiClassDiagram {

	private final ClassDiagram classDiagram;
	private final Document document;
	private final Element ownedElement;

	private final Set<IEntity> done = new HashSet<IEntity>();

	public XmiClassDiagramStar(ClassDiagram classDiagram) throws ParserConfigurationException {
		this.classDiagram = classDiagram;
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		final DocumentBuilder builder = factory.newDocumentBuilder();
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

		// <UML:Model xmi.id="UMLModel.4" name="Design Model"
		// visibility="public" isSpecification="false" isRoot="false"
		// isLeaf="false" isAbstract="false">
		final Element model = document.createElement("UML:Model");
		model.setAttribute("xmi.id", "model1");
		model.setAttribute("name", "PlantUML");
		content.appendChild(model);

		// <UML:Namespace.ownedElement>
		this.ownedElement = document.createElement("UML:Namespace.ownedElement");
		model.appendChild(ownedElement);

		for (final IEntity ent : classDiagram.getLeafsvalues()) {
			// if (fileFormat == FileFormat.XMI_ARGO && isStandalone(ent) == false) {
			// continue;
			// }
			final Element cla = createEntityNode(ent);
			ownedElement.appendChild(cla);
			done.add(ent);
		}

		// if (fileFormat != FileFormat.XMI_STANDARD) {
		for (final Link link : classDiagram.getLinks()) {
			addLink(link);
		}
		// }
	}

//	private boolean isStandalone(IEntity ent) {
//		for (final Link link : classDiagram.getLinks()) {
//			if (link.getEntity1() == ent || link.getEntity2() == ent) {
//				return false;
//			}
//		}
//		return true;
//	}

	public static String forXMI(String s) {
		return s.replace(':', ' ');
	}

	public static String forXMI(Display s) {
		return s.get(0).toString().replace(':', ' ');
	}

	private void addLink(Link link) {
		final String assId = "ass" + UniqueSequence.getValue();
		if (link.getType().getDecor1() == LinkDecor.EXTENDS || link.getType().getDecor2() == LinkDecor.EXTENDS) {
			addExtension(link, assId);
			return;
		}
		final Element association = document.createElement("UML:Association");
		association.setAttribute("xmi.id", assId);
		association.setAttribute("namespace", "model1");
		if (link.getLabel() != null) {
			association.setAttribute("name", forXMI(link.getLabel()));
		}

		final Element connection = document.createElement("UML:Association.connection");
		final Element end1 = document.createElement("UML:AssociationEnd");
		end1.setAttribute("xmi.id", "end" + UniqueSequence.getValue());
		end1.setAttribute("association", assId);
		end1.setAttribute("type", link.getEntity1().getUid());
		if (link.getQualifier1() != null) {
			end1.setAttribute("name", forXMI(link.getQualifier1()));
		}
		final Element endparticipant1 = document.createElement("UML:AssociationEnd.participant");
		// if (fileFormat == FileFormat.XMI_ARGO) {
		// if (done.contains(link.getEntity1())) {
		// endparticipant1.appendChild(createEntityNodeRef(link.getEntity1()));
		// } else {
		// endparticipant1.appendChild(createEntityNode(link.getEntity1()));
		// done.add(link.getEntity1());
		// }
		// } else if (fileFormat == FileFormat.XMI_STAR) {
		if (link.getType().getDecor2() == LinkDecor.COMPOSITION) {
			end1.setAttribute("aggregation", "composite");
		}
		if (link.getType().getDecor2() == LinkDecor.AGREGATION) {
			end1.setAttribute("aggregation", "aggregate");
		}
		// }
		end1.appendChild(endparticipant1);
		connection.appendChild(end1);

		final Element end2 = document.createElement("UML:AssociationEnd");
		end2.setAttribute("xmi.id", "end" + UniqueSequence.getValue());
		end2.setAttribute("association", assId);
		end2.setAttribute("type", link.getEntity2().getUid());
		if (link.getQualifier2() != null) {
			end2.setAttribute("name", forXMI(link.getQualifier2()));
		}
		final Element endparticipant2 = document.createElement("UML:AssociationEnd.participant");
		// if (fileFormat == FileFormat.XMI_ARGO) {
		// if (done.contains(link.getEntity2())) {
		// endparticipant2.appendChild(createEntityNodeRef(link.getEntity2()));
		// } else {
		// endparticipant2.appendChild(createEntityNode(link.getEntity2()));
		// done.add(link.getEntity2());
		// }
		// } else if (fileFormat == FileFormat.XMI_STAR) {
		if (link.getType().getDecor1() == LinkDecor.COMPOSITION) {
			end2.setAttribute("aggregation", "composite");
		}
		if (link.getType().getDecor1() == LinkDecor.AGREGATION) {
			end2.setAttribute("aggregation", "aggregate");
		}
		// }
		end2.appendChild(endparticipant2);
		connection.appendChild(end2);

		association.appendChild(connection);

		ownedElement.appendChild(association);

	}

	private void addExtension(Link link, String assId) {
		final Element association = document.createElement("UML:Generalization");
		association.setAttribute("xmi.id", assId);
		association.setAttribute("namespace", "model1");
		if (link.getLabel() != null) {
			association.setAttribute("name", forXMI(link.getLabel()));
		}
		if (link.getType().getDecor1() == LinkDecor.EXTENDS) {
			association.setAttribute("child", link.getEntity1().getUid());
			association.setAttribute("parent", link.getEntity2().getUid());
		} else if (link.getType().getDecor2() == LinkDecor.EXTENDS) {
			association.setAttribute("child", link.getEntity2().getUid());
			association.setAttribute("parent", link.getEntity1().getUid());
		} else {
			throw new IllegalStateException();
		}
		ownedElement.appendChild(association);

	}

	private Element createEntityNode(IEntity entity) {
		// <UML:Class xmi.id="UMLClass.5" name="Class1" visibility="public"
		// isSpecification="false"
		// namespace="UMLModel.4" isRoot="false" isLeaf="false"
		// isAbstract="false" participant="UMLAssociationEnd.11"
		// isActive="false">
		final Element cla = document.createElement("UML:Class");

		cla.setAttribute("xmi.id", entity.getUid());
		cla.setAttribute("name", entity.getDisplay().get(0).toString());
		cla.setAttribute("namespace", "model1");

		final Element feature = document.createElement("UML:Classifier.feature");
		cla.appendChild(feature);

		for (Member m : entity.getFieldsToDisplay()) {
			// <UML:Attribute xmi.id="UMLAttribute.6" name="Attribute1"
			// visibility="public" isSpecification="false"
			// ownerScope="instance" changeability="changeable"
			// targetScope="instance" type="" owner="UMLClass.5"/>
			final Element attribute = document.createElement("UML:Attribute");
			attribute.setAttribute("xmi.id", "att" + UniqueSequence.getValue());
			attribute.setAttribute("name", m.getDisplay(false));
			feature.appendChild(attribute);
		}

		for (Member m : entity.getMethodsToDisplay()) {
			// <UML:Operation xmi.id="UMLOperation.7" name="Operation1"
			// visibility="public" isSpecification="false"
			// ownerScope="instance" isQuery="false" concurrency="sequential"
			// isRoot="false" isLeaf="false"
			// isAbstract="false" specification="" owner="UMLClass.5"/>
			final Element operation = document.createElement("UML:Operation");
			operation.setAttribute("xmi.id", "att" + UniqueSequence.getValue());
			operation.setAttribute("name", m.getDisplay(false));
			feature.appendChild(operation);
		}
		return cla;
	}

	private Element createEntityNodeRef(IEntity entity) {
		final Element cla = document.createElement("UML:Class");
		cla.setAttribute("xmi.idref", entity.getUid());
		return cla;
	}

	public void transformerXml(OutputStream os) throws TransformerException, ParserConfigurationException {
		final Source source = new DOMSource(document);

		final Result resultat = new StreamResult(os);

		final TransformerFactory fabrique = TransformerFactory.newInstance();
		final Transformer transformer = fabrique.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		// tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(source, resultat);
	}

}
