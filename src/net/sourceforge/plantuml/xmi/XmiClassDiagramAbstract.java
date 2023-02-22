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

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.version.Version;
import net.sourceforge.plantuml.xml.XmlFactories;

abstract class XmiClassDiagramAbstract implements XmlDiagramTransformer {

	// https://www.ibm.com/developerworks/library/x-wxxm24/
	// http://pierre.ree7.fr/blog/?p=5

	protected final ClassDiagram classDiagram;
	protected final Document document;
	protected final Element ownedElementRoot;

	protected final Set<Entity> done = new HashSet<>();

	public XmiClassDiagramAbstract(ClassDiagram classDiagram) throws ParserConfigurationException {
		this.classDiagram = classDiagram;

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

		header.appendChild(createXmiDocumentation());
		header.appendChild(createXmiMetamodel());

		final Element content = document.createElement("XMI.content");
		xmi.appendChild(content);

		final Element model = document.createElement("UML:Model");
		model.setAttribute("xmi.id", CucaDiagramXmiMaker.getModel(classDiagram));
		model.setAttribute("name", "PlantUML");
		content.appendChild(model);

		// <UML:Namespace.ownedElement>
		this.ownedElementRoot = document.createElement("UML:Namespace.ownedElement");
		model.appendChild(ownedElementRoot);

	}

	private Element createXmiDocumentation() {
		final Element documentation = document.createElement("XMI.documentation");
		final Element exporter = document.createElement("XMI.exporter");
		exporter.setTextContent("PlantUML");
		final Element exporterVersion = document.createElement("XMI.exporterVersion");
		exporterVersion.setTextContent(Version.versionString());
		documentation.appendChild(exporter);
		documentation.appendChild(exporterVersion);
		return documentation;
	}

	private Element createXmiMetamodel() {
		final Element metamodel = document.createElement("XMI.metamodel");
		metamodel.setAttribute("xmi.name", "UML");
		metamodel.setAttribute("xmi.version", "1.4");
		return metamodel;
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

//		final String parentCode = entity.getQuark().getParent().toStringPoint();
//		if (parentCode.length() == 0)
//			cla.setAttribute("namespace", CucaDiagramXmiMaker.getModel(classDiagram));
//		else
//			cla.setAttribute("namespace", parentCode);

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

		if (entity.isStatic())
			cla.setAttribute("isStatic", "true");

		if (entity.getVisibilityModifier() == VisibilityModifier.PRIVATE_FIELD
				|| entity.getVisibilityModifier() == VisibilityModifier.PRIVATE_METHOD)
			cla.setAttribute("visibility", entity.getVisibilityModifier().getXmiVisibility());

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
