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
	protected Element ownedElement;

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
		 */
		final DocumentBuilder builder = XmlFactories.newDocumentBuilder();
		this.document = builder.newDocument();
		document.setXmlVersion("1.0");
		document.setXmlStandalone(true);

		final XmlStackBuilderThingy s = new XmlStackBuilderThingy(document);

		s.push(document.createElement("XMI"));
		s.peek().setAttribute("xmi.version", "1.2");
		// s.peek().setAttribute("xmlns:UML", "href://org.omg/UML/1.3");
		s.peek().setAttribute("xmlns:UML", "org.omg.xmi.namespace.UML");

		s.push(document.createElement("XMI.header"));

		s.push(document.createElement("XMI.documentation"));
		s.push(document.createElement("XMI.exporter"));
		s.peek().appendChild(document.createTextNode("PlantUML"));
		s.pop(); // XMI.exporter
		s.push(document.createElement("XMI.exporterVersion"));
		s.peek().appendChild(document.createTextNode("PlantUML 1.2022"));	// FIXME
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

	final protected Element createEntityNode(Entity entity) {
		final Element cla = document.createElement("UML:Class");
		if (entity.getLeafType() == LeafType.NOTE)
			return null;

		cla.setAttribute("xmi.id", entity.getUid());
		cla.setAttribute("name", entity.getDisplay().get(0).toString());
		final String parentCode = entity.getQuark().getParent().toStringPoint();

		if (parentCode.length() == 0)
			cla.setAttribute("namespace", CucaDiagramXmiMaker.getModel(classDiagram));
		else
			cla.setAttribute("namespace", parentCode);

		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null) {
			final Element stereo = document.createElement("UML:ModelElement.stereotype");
			for (String s : stereotype.getMultipleLabels()) {
				final Element name = document.createElement("UML:Stereotype");
				name.setAttribute("name", s);
				stereo.appendChild(name);
			}
			cla.appendChild(stereo);
		}

		final LeafType type = entity.getLeafType();
		if (type == LeafType.ABSTRACT_CLASS)
			cla.setAttribute("isAbstract", "true");
		else if (type == LeafType.INTERFACE)
			cla.setAttribute("isInterface", "true");

		if (((Entity) entity).isStatic())
			cla.setAttribute("isStatic", "true");

		if (((Entity) entity).getVisibilityModifier() == VisibilityModifier.PRIVATE_FIELD
				|| ((Entity) entity).getVisibilityModifier() == VisibilityModifier.PRIVATE_METHOD)
			cla.setAttribute("visibility", ((Entity) entity).getVisibilityModifier().getXmiVisibility());

		final Element feature = document.createElement("UML:Classifier.feature");
		cla.appendChild(feature);

		for (CharSequence cs : entity.getBodier().getFieldsToDisplay()) {
			final Member m = (Member) cs;

			final Element attribute = document.createElement("UML:Attribute");
			attribute.setAttribute("xmi.id", "att" + classDiagram.getUniqueSequence());
			attribute.setAttribute("name", m.getDisplay(false));
			final VisibilityModifier visibility = m.getVisibilityModifier();
			if (visibility != null)
				attribute.setAttribute("visibility", visibility.getXmiVisibility());
			if (m.isStatic())
				attribute.setAttribute("isStatic", "true");

			feature.appendChild(attribute);
		}

		for (CharSequence cs : entity.getBodier().getMethodsToDisplay()) {
			final Member m = (Member) cs;
			final Element operation = document.createElement("UML:Operation");
			operation.setAttribute("xmi.id", "att" + classDiagram.getUniqueSequence());
			operation.setAttribute("name", m.getDisplay(false));
			final VisibilityModifier visibility = m.getVisibilityModifier();
			if (visibility != null)
				operation.setAttribute("visibility", visibility.getXmiVisibility());
			if (m.isStatic())
				operation.setAttribute("isStatic", "true");

			feature.appendChild(operation);
		}
		return cla;
	}

}
