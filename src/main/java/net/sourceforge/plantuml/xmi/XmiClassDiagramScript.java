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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.utils.Log;

public class XmiClassDiagramScript extends XmiClassDiagramAbstract implements XmlDiagramTransformer {

	private static class MemberData {
		public final String name;
		public final String id;
		public final String kind;

		public MemberData(String name, String id, String kind) {
			super();
			this.name = name;
			this.id = id;
			this.kind = kind;
		}

	}

	protected final Map<String, List<MemberData>> members = new HashMap<>();

	public XmiClassDiagramScript(ClassDiagram classDiagram) throws ParserConfigurationException {
		super(classDiagram);

		addPackage(ownedElementRoot, classDiagram.getRootGroup());

		for (final Link link : classDiagram.getLinks())
			addLink(link);

	}

	final protected Element createElementPackage(Entity group) {
		final Element umlPackage = document.createElement("UML:Package");

		umlPackage.setAttribute("xmi.id", group.getUid());
		umlPackage.setAttribute("name", group.getDisplay().get(0).toString());

		final Element namespaceOwnedElement = document.createElement("UML:Namespace.ownedElement");
		umlPackage.appendChild(namespaceOwnedElement);

		addPackage(namespaceOwnedElement, group);

		return umlPackage;

	}

	private void addPackage(Element element, Entity group) {
		for (final Entity ent : group.leafs()) {
			final Element cla = createEntityNode(ent);
			if (cla == null)
				continue;

			element.appendChild(cla);
			if (members.containsKey(ent.getUid()) == false)
				members.put(ent.getUid(), new ArrayList<MemberData>());

			NodeList attrs = cla.getElementsByTagName("UML:Attribute");
			for (int i = 0; i < attrs.getLength(); i++) {
				Element child = (Element) attrs.item(i);
				members.get(ent.getUid()).add(
						new MemberData(child.getAttribute("name"), child.getAttribute("xmi.id"), child.getTagName()));
			}
			NodeList ops = cla.getElementsByTagName("UML:Operation");
			for (int i = 0; i < ops.getLength(); i++) {
				Element child = (Element) ops.item(i);
				members.get(ent.getUid()).add(
						new MemberData(child.getAttribute("name"), child.getAttribute("xmi.id"), child.getTagName()));
			}
			done.add(ent);
		}

		for (final Entity childGroup : group.groups()) {
			final Element result = createElementPackage(childGroup);
			element.appendChild(result);
			done.add(childGroup);
		}

	}

	private void addLink(Link link) {
		if (link.isHidden() || link.isInvis())
			return;
		if (!link.getType().getStyle().isNormal()) {
			// this is some kind of dashed line, which means it is a dependency
			Element dependency = createDependency(link);
			ownedElementRoot.appendChild(dependency);
			return;
		}

		final String assId = classDiagram.getUniqueSequence("ass");
		if (link.getType().getDecor1() == LinkDecor.EXTENDS || link.getType().getDecor2() == LinkDecor.EXTENDS) {
			addExtension(link, assId);
			return;
		}

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

		ownedElementRoot.appendChild(association);
	}

	private Element createAssociationEnd(final String assId, final LinkDecor decor, final String quantifier,
			Entity entity, UMLAggregationKind aggregation) {
		final Element end = document.createElement("UML:AssociationEnd");
		end.setAttribute("xmi.id", classDiagram.getUniqueSequence("end"));
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

	private void addExtension(Link link, String assId) {
		final Element association = document.createElement("UML:Generalization");
		association.setAttribute("xmi.id", assId);
		if (link.getLabel() != null)
			association.setAttribute("name", forXMI(link.getLabel()));

		if (link.getType().getDecor1() == LinkDecor.EXTENDS)
			generalizationChildParent(association, link.getEntity1().getUid(), link.getEntity2().getUid());
		else if (link.getType().getDecor2() == LinkDecor.EXTENDS)
			generalizationChildParent(association, link.getEntity2().getUid(), link.getEntity1().getUid());
		else
			throw new IllegalStateException();

		ownedElementRoot.appendChild(association);
	}

	private Element createDependency(Link link) {
		// determine kind and direction
		if (link.isInverted()) {
			return createDependencyClientSupplier(link.getEntity2(), link.getPortName2(), link.getEntity1(),
					link.getPortName1());
		} else {
			return createDependencyClientSupplier(link.getEntity1(), link.getPortName1(), link.getEntity2(),
					link.getPortName2());
		}
	}

	private Element createRef(Entity entity, String member) {
		if (member == null) {
			// directly to entity
			Element ref = document.createElement("UML:Class");
			ref.setAttribute("xmi.idref", entity.getUid());
			return ref;
		}
		List<MemberData> mbers = members.get(entity.getUid());
		if (mbers == null) {
			Log.info(() -> String.format("Could not find entity %s in member list", entity.getName()));
			return null;
		}
		for (MemberData m : mbers) {
			if (m.name.contains(member)) {
				Element ref = document.createElement(m.kind);
				ref.setAttribute("xmi.idref", m.id);
				return ref;
			}
		}
		// All members must have been added to the map
		Log.error(String.format("Could not find the member %s in the object %s", member, entity.getName()));
		return null;
	}

	private Element createDependencyClientSupplier(Entity clientEntity, String clientPort, Entity supplierEntity,
			String supplierPort) {
		String depID = classDiagram.getUniqueSequence("dep");
		Element dependency = document.createElement("UML:Dependency");
		dependency.setAttribute("xmi.id", depID);
		Element client = document.createElement("UML:Dependency.client");
		Element supplier = document.createElement("UML:Dependency.supplier");
		Element clientRef = createRef(clientEntity, clientPort);
		if (clientRef != null)
			client.appendChild(clientRef);
		Element supplierRef = createRef(supplierEntity, supplierPort);
		if (supplierRef != null)
			supplier.appendChild(supplierRef);
		dependency.appendChild(client);
		dependency.appendChild(supplier);
		return dependency;
	}

	private void generalizationChildParent(Element association, String uidChild, String uidParent) {
		final Element child = document.createElement("UML:Generalization.child");
		final Element parent = document.createElement("UML:Generalization.parent");

		final Element classChild = document.createElement("UML:Class");
		classChild.setAttribute("xmi.idref", uidChild);
		final Element classParent = document.createElement("UML:Class");
		classParent.setAttribute("xmi.idref", uidParent);

		parent.appendChild(classParent);
		child.appendChild(classChild);

		association.appendChild(child);
		association.appendChild(parent);
//		association.setAttribute("child", uidChild);
//		association.setAttribute("parent", uidParent);

	}

}
