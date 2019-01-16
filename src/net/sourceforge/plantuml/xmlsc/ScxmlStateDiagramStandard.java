/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.xmlsc;

import java.io.OutputStream;

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

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.statediagram.StateDiagram;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScxmlStateDiagramStandard {

	private final StateDiagram diagram;
	private final Document document;

	public ScxmlStateDiagramStandard(StateDiagram diagram) throws ParserConfigurationException {
		this.diagram = diagram;
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		final DocumentBuilder builder = factory.newDocumentBuilder();
		this.document = builder.newDocument();
		document.setXmlVersion("1.0");
		document.setXmlStandalone(true);

		final Element scxml = document.createElement("scxml");
		scxml.setAttribute("xmlns", "http://www.w3.org/2005/07/scxml");
		scxml.setAttribute("version", "1.0");
		final String initial = getInitial();
		if (initial != null) {
			scxml.setAttribute("initial", initial);
		}
		document.appendChild(scxml);

		for (final IEntity ent : diagram.getLeafsvalues()) {
			scxml.appendChild(createState(ent));
		}

	}

	private String getInitial() {
		for (final IEntity ent : diagram.getLeafsvalues()) {
			if (ent.getLeafType() == LeafType.CIRCLE_START) {
				return getId(ent);
			}
		}
		return null;
	}

	private Element createState(IEntity entity) {
		final Element state = document.createElement("state");
		state.setAttribute("id", getId(entity));
		for (final Link link : diagram.getLinks()) {
			if (link.getEntity1() == entity) {
				addLink(state, link);
			}
		}
		return state;
	}

	private void addLink(Element state, Link link) {
		final Element transition = document.createElement("transition");
		final Display label = link.getLabel();
		if (Display.isNull(label) == false) {
			final String event = label.get(0).toString();
			transition.setAttribute("event", event);
		}
		transition.setAttribute("target", getId(link.getEntity2()));
		state.appendChild(transition);

	}

	private String getId(IEntity entity) {
		String result = entity.getDisplay().get(0).toString();
		result = result.replaceAll("\\*", "");
		return result;
	}

	public void transformerXml(OutputStream os) throws TransformerException, ParserConfigurationException {
		final Source source = new DOMSource(document);

		final Result resultat = new StreamResult(os);

		final TransformerFactory fabrique = TransformerFactory.newInstance();
		final Transformer transformer = fabrique.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.transform(source, resultat);
	}

}
