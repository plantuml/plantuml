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

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.klimt.creole.Display;

public class XmiDescriptionDiagramScript extends XmiDescriptionDiagramAbstract {

	public XmiDescriptionDiagramScript(DescriptionDiagram diagram) throws ParserConfigurationException {
		super(diagram);
	}

	@Override
	protected void addLink(Link link) {
		if (link.isHidden() || link.isInvis())
			return;
		if (!link.getType().getStyle().isNormal()) {
			// this is some kind of dashed line, which means it is a dependency
			Element dependency = createDependency(link);
			ownedElement.appendChild(dependency);
			return;
		}

		final String assId = diagram.getUniqueSequence("ass");

		UMLAggregationKind aggregation = UMLAggregationKind.None;
		if (link.getType().getDecor1() == LinkDecor.COMPOSITION) {
			aggregation = UMLAggregationKind.Composite;
		}
		if (link.getType().getDecor2() == LinkDecor.COMPOSITION) {
			aggregation = UMLAggregationKind.Composite;
		}
		if (link.getType().getDecor1() == LinkDecor.AGREGATION) {
			aggregation = UMLAggregationKind.Aggregation;
		}
		if (link.getType().getDecor2() == LinkDecor.AGREGATION) {
			aggregation = UMLAggregationKind.Aggregation;
		}

		final Element association = document.createElement("UML:Association");
		association.setAttribute("xmi.id", assId);
		// association.setAttribute("namespace",
		// CucaDiagramXmiMaker.getModel(classDiagram));
		if (Display.isNull(link.getLabel()) == false)
			association.setAttribute("name", forXMI(link.getLabel()));

		final Element connection = document.createElement("UML:Association.connection");
		final Element end1 = createAssociationEnd(assId, link.getType().getDecor2(), link.getQuantifier1(),
				link.getEntity1(), aggregation);

		connection.appendChild(end1);
		final Element end2 = createAssociationEnd(assId, link.getType().getDecor1(), link.getQuantifier2(),
				link.getEntity2(), aggregation);
		connection.appendChild(end2);

		association.appendChild(connection);

		ownedElement.appendChild(association);
	}

	private Element createAssociationEnd(final String assId, final LinkDecor decor, final String quantifier,
			Entity entity, UMLAggregationKind aggregation) {
		final Element end = document.createElement("UML:AssociationEnd");
		end.setAttribute("xmi.id", diagram.getUniqueSequence("end"));
		end.setAttribute("association", assId);

//		end1.setAttribute("type", link.getEntity1().getUid());
		if (quantifier != null)
			end.setAttribute("name", forXMI(quantifier));
		// TODO this is the multiplicity, handle that correctly

		end.setAttribute("participant", entity.getUid());

		if (aggregation != UMLAggregationKind.None)
			end.setAttribute("aggregation", aggregation.name);

		boolean navigable = decor != LinkDecor.NOT_NAVIGABLE && decor != LinkDecor.NONE;
		end.setAttribute("isNavigable", Boolean.toString(navigable));

		return end;
	}


	private Element createDependency(Link link) {
		// determine kind and direction
		if (link.isInverted()) {
			return createDependencyClientSupplier(link.getEntity2(), link.getEntity1());
		} else {
			return createDependencyClientSupplier(link.getEntity1(), link.getEntity2());
		}
	}

	private Element createRef(Entity entity) {
		Element ref = document.createElement("UML:Component");
		ref.setAttribute("xmi.idref", entity.getUid());
		return ref;

	}

	private Element createDependencyClientSupplier(Entity clientEntity, Entity supplierEntity) {
		String depID = diagram.getUniqueSequence("dep");
		Element dependency = document.createElement("UML:Dependency");
		dependency.setAttribute("xmi.id", depID);
		Element client = document.createElement("UML:Dependency.client");
		Element supplier = document.createElement("UML:Dependency.supplier");
		Element clientRef = createRef(clientEntity);
		if (clientRef != null)
			client.appendChild(clientRef);
		Element supplierRef = createRef(supplierEntity);
		if (supplierRef != null)
			supplier.appendChild(supplierRef);
		dependency.appendChild(client);
		dependency.appendChild(supplier);
		return dependency;
	}


}
