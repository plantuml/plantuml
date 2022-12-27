/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import net.sourceforge.plantuml.baraye.IEntity;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;

public class XmiClassDiagramArgo extends XmiClassDiagramAbstract implements XmlDiagramTransformer {

	public XmiClassDiagramArgo(ClassDiagram classDiagram) throws ParserConfigurationException {
		super(classDiagram);

		for (final IEntity ent : classDiagram.getLeafsvalues()) {
			if (classDiagram.isStandaloneForArgo(ent) == false)
				continue;

			final Element cla = createEntityNode(ent);
			if (cla == null)
				continue;

			ownedElement.appendChild(cla);
			done.add(ent);
		}

		for (final Link link : classDiagram.getLinks())
			addLink(link);
	}

	private void addLink(Link link) {
		if (link.isHidden() || link.isInvis())
			return;

		final String assId = "ass" + classDiagram.getUniqueSequence();

		final Element association = document.createElement("UML:Association");
		association.setAttribute("xmi.id", assId);
		association.setAttribute("namespace", CucaDiagramXmiMaker.getModel(classDiagram));
		if (link.getLabel() != null)
			association.setAttribute("name", forXMI(link.getLabel()));

		final Element connection = document.createElement("UML:Association.connection");
		final Element end1 = document.createElement("UML:AssociationEnd");
		end1.setAttribute("xmi.id", "end" + classDiagram.getUniqueSequence());
		end1.setAttribute("association", assId);
		end1.setAttribute("type", link.getEntity1().getUid());
		if (link.getQuantifier1() != null)
			end1.setAttribute("name", forXMI(link.getQuantifier1()));

		final Element endparticipant1 = document.createElement("UML:AssociationEnd.participant");

		if (done.contains(link.getEntity1())) {
			endparticipant1.appendChild(createEntityNodeRef(link.getEntity1()));
		} else {
			final Element element = createEntityNode(link.getEntity1());
			if (element == null)
				return;

			endparticipant1.appendChild(element);
			done.add(link.getEntity1());
		}

		end1.appendChild(endparticipant1);
		connection.appendChild(end1);

		final Element end2 = document.createElement("UML:AssociationEnd");
		end2.setAttribute("xmi.id", "end" + classDiagram.getUniqueSequence());
		end2.setAttribute("association", assId);
		end2.setAttribute("type", link.getEntity2().getUid());
		if (link.getQuantifier2() != null)
			end2.setAttribute("name", forXMI(link.getQuantifier2()));

		final Element endparticipant2 = document.createElement("UML:AssociationEnd.participant");

		if (done.contains(link.getEntity2())) {
			endparticipant2.appendChild(createEntityNodeRef(link.getEntity2()));
		} else {
			final Element element = createEntityNode(link.getEntity2());
			if (element == null)
				return;

			endparticipant2.appendChild(element);
			done.add(link.getEntity2());
		}

		end2.appendChild(endparticipant2);
		connection.appendChild(end2);

		association.appendChild(connection);

		ownedElement.appendChild(association);

	}

	private Element createEntityNodeRef(IEntity entity) {
		final Element cla = document.createElement("UML:Class");
		cla.setAttribute("xmi.idref", entity.getUid());
		return cla;
	}

}
