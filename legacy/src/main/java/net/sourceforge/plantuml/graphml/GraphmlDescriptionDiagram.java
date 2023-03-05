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
package net.sourceforge.plantuml.graphml;

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
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.xmi.XmlDiagramTransformer;
import net.sourceforge.plantuml.xml.XmlFactories;

public class GraphmlDescriptionDiagram implements XmlDiagramTransformer {

	private final DescriptionDiagram diagram;
	private final Document document;

	public GraphmlDescriptionDiagram(DescriptionDiagram diagram) throws ParserConfigurationException {
		this.diagram = diagram;

		final DocumentBuilder builder = XmlFactories.newDocumentBuilder();
		this.document = builder.newDocument();
		document.setXmlVersion("1.0");
		document.setXmlStandalone(true);

		final Element graphml = document.createElement("graphml");
		graphml.setAttribute("xmlns", "http://graphml.graphdrawing.org/xmlns");
		graphml.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		graphml.setAttribute("xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd");
		document.appendChild(graphml);

		final Element graph = document.createElement("graph");
		graph.setAttribute("edgedefault", "undirected");
		graphml.appendChild(graph);

		for (final Entity ent : diagram.getEntityFactory().leafs())
			if (ent.getParentContainer().isRoot())
				addElement(ent, graph);

	}

	private void addElement(Entity tobeAdded, Element container) {
		final Element element = createEntityNode(tobeAdded);
		container.appendChild(element);

	}

	private Element createEntityNode(Entity entity) {
		final Element cla = document.createElement("node");
		cla.setAttribute("id", entity.getName());
		return cla;
	}

	public void transformerXml(OutputStream os) throws TransformerException, ParserConfigurationException {
		final Source source = new DOMSource(document);

		final Result resultat = new StreamResult(os);

		final Transformer transformer = XmlFactories.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8.name());
		transformer.transform(source, resultat);
	}

}
