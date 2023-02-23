/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 * (C) Copyright 2023 Daniel Santos
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
import java.util.HashSet;
import java.util.Set;

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

import net.sourceforge.plantuml.baraye.Entity;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.xml.XmlFactories;
import net.sourceforge.plantuml.xml.XmlStackBuilderThingy;


abstract class XmiClassDiagramAbstract implements XmlDiagramTransformer {

	// https://www.ibm.com/developerworks/library/x-wxxm24/
	// http://pierre.ree7.fr/blog/?p=5

	protected final ClassDiagram classDiagram;
	protected final XmlStackBuilderThingy s;
	protected final Document document;
	//protected Element ownedElement;

	protected final Set<Entity> done = new HashSet<>();

	public XmiClassDiagramAbstract(ClassDiagram classDiagram) throws ParserConfigurationException {
		this.classDiagram = classDiagram;
		/* <?xml version='1.0' encoding='UTF-8' ?>
		 * <XMI xmi.version='1.2' xmlns:UML='org.omg.xmi.namespace.UML'>
		 *   <XMI.header>
		 *     <XMI.documentation>
		 *       <XMI.exporter>PlantUML</XMI.exporter>
		 *       <XMI.exporterVersion>PlantUML 1.2022</XMI.exporterVersion>
		 *     </XMI.documentation>
		 *     <XMI.metamodel xmi.name="UML" xmi.version="1.4"/>
		 *   </XMI.header>
		 *   <XMI.content>
		 *     <UML:Model xmi.id='model1' name='PlantUML'>
		 *       <UML:Namespace.ownedElement>
		 */
		final DocumentBuilder builder = XmlFactories.newDocumentBuilder();
		this.document = builder.newDocument();
		document.setXmlVersion("1.0");
		document.setXmlStandalone(true);

		final XmlStackBuilderThingy s = new XmlStackBuilderThingy(document);

		s.push(document.createElement("XMI"));
		s.peek().setAttribute("xmi.version", "1.2");
		s.peek().setAttribute("xmlns:UML", "org.omg.xmi.namespace.UML");

		s.push(document.createElement("XMI.header"));

		s.push(document.createElement("XMI.documentation"));
		s.push(document.createElement("XMI.exporter"));
		s.peek().appendChild(document.createTextNode("PlantUML"));
		s.pop(); // XMI.exporter
		s.push(document.createElement("XMI.exporterVersion"));
		s.peek().appendChild(document.createTextNode("PlantUML 1.2022"));	// FIXME
		s.pop(); // XMI.exporterVersion
		s.pop(); // XMI.documentation

		s.push(document.createElement("XMI.metamodel"));
		s.peek().setAttribute("xmi.name", "UML");
		s.peek().setAttribute("xmi.version", "1.4");
		s.pop(); // XMI.metamodel
		s.pop(); // XMI.header

		s.push(document.createElement("XMI.content"));
		s.push(document.createElement("UML:Model"));
		s.peek().setAttribute("xmi.id", CucaDiagramXmiMaker.getModel(classDiagram));
		s.peek().setAttribute("name", "PlantUML");
		s.pushNamespace();

		this.s = s;
	}

	final protected String forXMI(String s) {
		return s.replace(':', ' ');
	}

	final protected String forXMI(Display s) {
		if (Display.isNull(s)) {
			return "";
		}
		return s.get(0).toString().replace(':', ' ');
	}

	final public void transformerXml(OutputStream os) throws TransformerException, ParserConfigurationException {
		final Source source = new DOMSource(document);

		final Result resultat = new StreamResult(os);

		final Transformer transformer = XmlFactories.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8.name());
		// tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(source, resultat);
	}

	// UGLY: temporary hack until I can replace all calls to createEntityNode.
	final protected Element createEntityNode(Entity entity) {
		int levels = addEntityNode(entity);
		if (levels > 1)
			throw new java.lang.RuntimeException("oops, I forgot how to Java");
		else if (levels == 1)
			return s.pop();
		else
			return null;
	}

	final protected int addEntityNode(Entity entity) {
		if (entity == null)
			return 0;

		s.push(document.createElement("UML:Class"));
		if (entity.getLeafType() == LeafType.NOTE) {
			// FIXME: uhh... what? Why did I do this?
			return 1; // UML:Class
		}

		s.peek().setAttribute("xmi.id", entity.getUid());
		s.peek().setAttribute("name", entity.getDisplay().get(0).toString());

		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null) {
			s.push(document.createElement("UML:ModelElement.stereotype"));
			for (String str : stereotype.getMultipleLabels()) {
				s.push(document.createElement("UML:Stereotype"));
				s.peek().setAttribute("name", str);
				s.pop(); // UML:Stereotype
			}
			s.pop(); // UML:ModelElement.stereotype
		}

		final LeafType type = entity.getLeafType();
		if (type == LeafType.ABSTRACT_CLASS)
			s.peek().setAttribute("isAbstract", "true");
		else if (type == LeafType.INTERFACE)
			s.peek().setAttribute("isInterface", "true");

		if (((Entity) entity).isStatic())
			s.peek().setAttribute("isStatic", "true");

		if (((Entity) entity).getVisibilityModifier() == VisibilityModifier.PRIVATE_FIELD
				|| ((Entity) entity).getVisibilityModifier() == VisibilityModifier.PRIVATE_METHOD)
			s.peek().setAttribute("visibility", ((Entity) entity).getVisibilityModifier().getXmiVisibility());

		for (final Link link : classDiagram.getLinks()) {
			if (link.getEntity2() != entity)
				continue;
			addLink(link);
		}

		s.push(document.createElement("UML:Classifier.feature"));

		for (CharSequence cs : entity.getBodier().getFieldsToDisplay()) {
			final Member m = (Member) cs;

			s.push(document.createElement("UML:Attribute"));
			s.peek().setAttribute("xmi.id", "att" + classDiagram.getUniqueSequence());
			s.peek().setAttribute("name", m.getDisplay(false));
			final VisibilityModifier visibility = m.getVisibilityModifier();
			if (visibility != null)
				s.peek().setAttribute("visibility", visibility.getXmiVisibility());
			if (m.isStatic())
				s.peek().setAttribute("isStatic", "true");

			s.pop(); // UML:Attribute
		}

		for (CharSequence cs : entity.getBodier().getMethodsToDisplay()) {
			final Member m = (Member) cs;

			s.push(document.createElement("UML:Operation"));
			s.peek().setAttribute("xmi.id", "att" + classDiagram.getUniqueSequence());
			s.peek().setAttribute("name", m.getDisplay(false));
			final VisibilityModifier visibility = m.getVisibilityModifier();
			if (visibility != null)
				s.peek().setAttribute("visibility", visibility.getXmiVisibility());
			if (m.isStatic())
				s.peek().setAttribute("isStatic", "true");

			s.pop(); // UML:Operation
		}
		s.pop(); // UML:Classifier.feature

		// For this to be useful for inner classes, etc., would probably need a namespace?
		return 1; // UML:Class
	}

	private void addLink(Link link) {
		if (link.isHidden() || link.isInvis())
			return;

		final String assId = "ass" + classDiagram.getUniqueSequence();
//		final LinkStyle.Type linkStyleType = link.getType().getStyle().getType();
	//	final boolean isExtends = link.getType().getDecor2() == LinkDecor.EXTENDS;
		//final boolean isRealization = isExtends && linkStyleType == LinkStyle.Type.DASHED;
		//final String st = link.getStereotype().toString();

		switch (link.getTypish()) {
		case GENERALIZATION:
			addGeneralization(link, assId);
			break;
		case REALIZATION:
			addAbstraction(link, assId);
			break;
		case COMPOSITION:
		case AGGREGATION:
		case ASSOCIATION:
		case UNIASSOCIATION:
		case ALIAS:
		default:
			break;
		}
	}

	/*
	 * Defined within type:
	 * 
	 * <UML:GeneralizableElement.generalization>
	 *   <UML:Generalization xmi.idref='generalization id'/>
	 * </UML:GeneralizableElement.generalization>
	 * 
	 * Defined at namespace level of type:
	 *
	 * <UML:Generalization xmi.id='generalization id'>
	 *   <UML:Generalization.child>
	 *     <UML:Class xmi.idref='class id1'/>
	 *   </UML:Generalization.child>
	 *   <UML:Generalization.parent>
	 *     <UML:Class xmi.idref='class id2'/>
	 *   </UML:Generalization.parent>
	 * </UML:Generalization>
	 */
	public void addGeneralization(Link link, String assId) {
		s.push(document.createElement("UML:ModelElement.clientDependency"));
		s.push(document.createElement("UML:Abstraction"));
		s.peek().setAttribute("xmi.idref", assId);
		s.pop(); // UML:Abstraction
		s.pop(); // UML:ModelElement.clientDependency

		XmlStackBuilderThingy ns = s.getNamespace();
		final String stereoId = "ste" + classDiagram.getUniqueSequence();

		ns.push(document.createElement("UML:Generalization"));

		ns.push(document.createElement("UML:Generalization.child"));
		ns.push(document.createElement("UML:Class"));
		ns.peek().setAttribute("xmi.idref", link.getEntity2().getUid());
		ns.pop(); // UML:Class
		ns.pop(); // UML:Generalization.child

		ns.push(document.createElement("UML:Generalization.parent"));
		ns.push(document.createElement("UML:Class"));
		ns.peek().setAttribute("xmi.idref", link.getEntity1().getUid());
		ns.pop(); // UML:Class
		ns.pop(); // UML:Generalization.parent
		ns.pop(); // UML:Generalization


		ns.push(document.createElement("UML:Stereotype"));
		ns.peek().setAttribute("xmi.id", stereoId);
		ns.push(document.createElement("UML:Stereotype.baseClass"));
		ns.peek().setTextContent("Abstraction");
		ns.pop(); // UML:Stereotype.baseClass
		ns.pop(); // UML:Stereotype
	}

	/*
	 * Defined within type:
	 * 
	 * <UML:ModelElement.clientDependency>
	 *   <UML:Abstraction xmi.idref='abstraction id'/>
	 * </UML:ModelElement.clientDependency>
	 * 
	 * Defined at namespace level of type:
	 * 
	 * <UML:Abstraction xmi.id='abstraction id'>
	 *   <UML:ModelElement.stereotype>
	 *     <UML:Stereotype xmi.idref='stereotype id'/>
	 *   </UML:ModelElement.stereotype>
	 *   <UML:Dependency.client>
	 *     <UML:Class xmi.idref='class id1'/>
	 *   </UML:Dependency.client>
	 *   <UML:Dependency.supplier>
	 *     <UML:Class xmi.idref='class id2'/>
	 *   </UML:Dependency.supplier>
	 * </UML:Abstraction>
	 * <UML:Stereotype xmi.id='stereotype id' name='realize'>
	 *   <UML:Stereotype.baseClass>Abstraction</UML:Stereotype.baseClass>
	 * </UML:Stereotype>
	 */
	public void addAbstraction(Link link, String assId) {
		s.push(document.createElement("UML:ModelElement.clientDependency"));
		s.push(document.createElement("UML:Abstraction"));
		s.peek().setAttribute("xmi.idref", assId);
		s.pop(); // UML:Abstraction
		s.pop(); // UML:ModelElement.clientDependency

		XmlStackBuilderThingy ns = s.getNamespace();
		final String stereoId = "ste" + classDiagram.getUniqueSequence();

		ns.push(document.createElement("UML:Abstraction"));
		ns.peek().setAttribute("xmi.id", assId);
		ns.push(document.createElement("UML:ModelElement.stereotype"));
		ns.push(document.createElement("UML:Stereotype"));
		ns.peek().setAttribute("xmi.idref", stereoId);
		ns.pop(); // UML:Stereotype
		ns.pop(); // UML:ModelElement.stereotype

		ns.push(document.createElement("UML:Dependency.client"));
		ns.push(document.createElement("UML:Class"));
		ns.peek().setAttribute("xmi.idref", link.getEntity2().getUid());
		ns.pop(); // UML:Dependency.client
		ns.pop(); // UML:Class

		ns.push(document.createElement("UML:Dependency.supplier"));
		ns.push(document.createElement("UML:Class"));
		ns.peek().setAttribute("xmi.idref", link.getEntity1().getUid());
		ns.pop(); // UML:Class
		ns.pop(); // UML:Dependency.supplier
		ns.pop(); // UML:Abstraction

		ns.push(document.createElement("UML:Stereotype"));
		ns.peek().setAttribute("xmi.id", stereoId);
		ns.push(document.createElement("UML:Stereotype.baseClass"));
		ns.peek().setTextContent("Abstraction");
		ns.pop(); // UML:Stereotype.baseClass
		ns.pop(); // UML:Stereotype
	}
}
