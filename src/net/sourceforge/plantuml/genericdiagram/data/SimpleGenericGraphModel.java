
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
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 */
package net.sourceforge.plantuml.genericdiagram.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.sourceforge.plantuml.genericdiagram.IGenericDiagram;
import net.sourceforge.plantuml.genericdiagram.IGenericEdge;
import net.sourceforge.plantuml.genericdiagram.IGenericGroup;
import net.sourceforge.plantuml.genericdiagram.IGenericLeaf;
import net.sourceforge.plantuml.genericdiagram.IGenericLink;
import net.sourceforge.plantuml.genericdiagram.IGenericMember;
import net.sourceforge.plantuml.genericdiagram.IGenericModelAccess;
import net.sourceforge.plantuml.genericdiagram.IGenericModelElement;
import net.sourceforge.plantuml.genericdiagram.IGenericStereotype;
import net.sourceforge.plantuml.genericdiagram.genericprocessing.IGenericDiagramVisitor;
import net.sourceforge.plantuml.genericdiagram.genericprocessing.IGenericModelCollector;

/**
 * node link representation of the generic model elements
 * can be easily exported as simple JSON graph
 */
public class SimpleGenericGraphModel implements IGenericModelCollector, IGenericModelAccess {

	List<IGenericModelElement> nodes;
	List<IGenericEdge> links;

	public SimpleGenericGraphModel(SimpleGenericModel genericModelData) {
		init();

		genericModelData.getGroups().stream().forEach(o -> addGroup(o));
		genericModelData.getDiagrams().stream().forEach(o -> addDiagram(o));
		genericModelData.getMembers().stream().forEach(o -> addMember(o));
		genericModelData.getStereotypes().stream().forEach(o -> addStereotype(o));
		genericModelData.getLeafs().stream().forEach(o -> addLeaf(o));
		genericModelData.getLinks().stream().forEach(o -> addLink(o));
		genericModelData.getEdges().stream().forEach((o -> addEdge(o)));
	}

	public SimpleGenericGraphModel(
					List<IGenericModelElement> modelNodes,
					List<IGenericEdge> modelLinks) {
		this.nodes = new ArrayList<>(modelNodes);
		this.links = new ArrayList<>(modelLinks);
	}

	private static Class<?> getImplementationType(String implType) {

		if ("GenericDiagram".equals(implType)) {
			return GenericDiagram.class;
		}
		if ("GenericLeaf".equals(implType)) {
			return GenericLeaf.class;
		}
		if ("GenericLink".equals(implType)) {
			return GenericLink.class;
		}
		if ("GenericMember".equals(implType)) {
			return GenericMember.class;
		}
		if ("GenericStereotype".equals(implType)) {
			return GenericStereotype.class;
		}
		if ("GenericGroup".equals(implType)) {
			return GenericGroup.class;
		}
		if ("GenericEdge".equals(implType)) {
			return GenericEdge.class;
		}
		return null;
	}

	private void init() {
		nodes = new ArrayList<>();
		links = new ArrayList<>();
	}

	@Override
	public void addDiagram(IGenericDiagram diagram) {
		nodes.add(diagram);
	}

	@Override
	public void addLeaf(IGenericLeaf leaf) {
		nodes.add(leaf);
	}

	@Override
	public void addGroup(IGenericGroup group) {
		nodes.add(group);
	}

	@Override
	public void addStereotype(IGenericStereotype stereotype) {
		nodes.add(stereotype);
	}

	@Override
	public void addMember(IGenericMember member) {
		nodes.add(member);
	}

	@Override
	public void addLink(IGenericLink link) {
		nodes.add(link);
	}

	@Override
	public void addEdge(IGenericEdge edge) {
		links.add(edge);
	}

	@Override
	public void reset() {
		init();
	}

	@Override
	public List<IGenericDiagram> getDiagrams() {
		return nodes.stream()
						.filter(n -> n.isDiagram())
						.map(n -> ((GenericDiagram) n))
						.collect(Collectors.toList());
	}

	@Override
	public List<IGenericEdge> getEdges() {
		return links.stream()
						.map(n -> ((GenericEdge) n))
						.collect(Collectors.toList());
	}

	@Override
	public List<IGenericGroup> getGroups() {
		return nodes.stream()
						.filter(n -> n.isGroup())
						.map(n -> ((GenericGroup) n))
						.collect(Collectors.toList());
	}

	@Override
	public List<IGenericLeaf> getLeafs() {
		return nodes.stream()
						.filter(n -> n.isLeaf())
						.map(n -> ((GenericLeaf) n))
						.collect(Collectors.toList());
	}

	@Override
	public List<IGenericLink> getLinks() {
		return nodes.stream()
						.filter(n -> n.isLink())
						.map(n -> ((GenericLink) n))
						.collect(Collectors.toList());

	}

	@Override
	public List<IGenericStereotype> getStereotypes() {
		return nodes.stream()
						.filter(n -> n.isStereotype())
						.map(n -> ((IGenericStereotype) n))
						.collect(Collectors.toList());

	}

	@Override
	public List<IGenericMember> getMembers() {
		return nodes.stream()
						.filter(n -> n.isMember())
						.map(n -> ((IGenericMember) n))
						.collect(Collectors.toList());
	}

	@Override
	public List<IGenericModelElement> getAllNodes() {
		return nodes.stream()
						.map(o -> ((IGenericModelElement) o))
						.collect(Collectors.toList());
	}

	@Override
	public List<IGenericModelElement> getAllEdges() {
		return links.stream()
						.map(e -> ((IGenericModelElement) e))
						.collect(Collectors.toList());
	}

	@Override
	public void acceptVisitor(IGenericDiagramVisitor visitor) {

		//TODO change visitor to interface
		for (IGenericDiagram diagram : getDiagrams()) {
			visitor.visitDiagram((GenericDiagram) diagram);
		}
		for (IGenericLeaf leaf : getLeafs()) {
			visitor.visitLeaf((GenericLeaf) leaf);
		}
		for (IGenericMember member : getMembers()) {
			visitor.visitMember((GenericMember) member);
		}
		for (IGenericStereotype stereotype : getStereotypes()) {
			visitor.visitStereotype((GenericStereotype) stereotype);
		}
		for (IGenericGroup group : getGroups()) {
			visitor.visitGroup((GenericGroup) group);
		}
		for (IGenericLink link : getLinks()) {
			visitor.visitLink((GenericLink) link);
		}
		for (IGenericEdge edge : getEdges()) {
			visitor.visitEdge((GenericEdge) edge);
		}
	}
}
