
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Simple Data Class for Generic Data Model
 * using maps to hold the data,
 * convenient for debugging and developing new functionality
 * you see what you get processed at a glance
 */
public class SimpleGenericModel implements IGenericModelCollector, IGenericModelAccess {


	List<GenericModelElement> elements;
	// nodes

	Map<Integer, IGenericGroup> groups;

	Map<Integer, IGenericDiagram> diagrams;
	Map<Integer, IGenericLink> links;

	Map<Integer, IGenericLeaf> leafs;

	Map<Integer, IGenericStereotype> stereotypes;

	Map<Integer, IGenericMember> members;
	// edges
	Map<Integer, IGenericEdge> edges;


	public SimpleGenericModel() {
		init();
	}

	public SimpleGenericModel(
					Map<Integer, IGenericDiagram> diagrams,
					Map<Integer, IGenericGroup> groups,
					Map<Integer, IGenericLink> links,
					Map<Integer, IGenericLeaf> leafs,
					Map<Integer, IGenericStereotype> stereotypes,
					Map<Integer, IGenericMember> members,
					Map<Integer, IGenericEdge> edges) {

		this.diagrams = diagrams;
		this.groups = groups;
		this.links = links;
		this.leafs = leafs;
		this.stereotypes = stereotypes;
		this.members = members;
		this.edges = edges;

	}

	private void init() {
		this.elements = new ArrayList<>();
		this.diagrams = new HashMap<>();
		this.groups = new HashMap<>();
		this.links = new HashMap<>();
		this.leafs = new HashMap<>();
		this.stereotypes = new HashMap<>();
		this.members = new HashMap<>();
		this.edges = new HashMap<>();
	}

	@Override
	public void addDiagram(IGenericDiagram diagram) {
		diagrams.put(diagram.getId(), diagram);
	}

	@Override
	public void addLeaf(IGenericLeaf leaf) {
		leafs.put(leaf.getId(), leaf);
	}

	@Override
	public void addGroup(IGenericGroup group) {
		groups.put(group.getId(), group);
	}

	@Override
	public void addStereotype(IGenericStereotype stereotype) {
		stereotypes.put(stereotype.getId(), stereotype);
	}

	@Override
	public void addMember(IGenericMember member) {
		members.put(member.getId(), member);
	}

	@Override
	public void addLink(IGenericLink link) {
		links.put(link.getId(), link);
	}

	@Override
	public void addEdge(IGenericEdge edge) {
		edges.put(edge.getId(), edge);
	}

	@Override
	public void reset() {
		init();
	}


	@Override
	public List<IGenericDiagram> getDiagrams() {
		return Collections.unmodifiableList(new ArrayList<>(diagrams.values()));
	}

	@Override
	public List<IGenericEdge> getEdges() {
		return Collections.unmodifiableList(new ArrayList<>(edges.values()));
	}

	@Override
	public List<IGenericGroup> getGroups() {
		return Collections.unmodifiableList(new ArrayList<>(groups.values()));
	}

	@Override
	public List<IGenericLeaf> getLeafs() {
		return Collections.unmodifiableList(new ArrayList<>(leafs.values()));
	}

	@Override
	public List<IGenericLink> getLinks() {
		return Collections.unmodifiableList(new ArrayList<>(links.values()));
	}

	@Override
	public List<IGenericStereotype> getStereotypes() {
		return Collections.unmodifiableList(new ArrayList<>(stereotypes.values()));
	}

	@Override
	public List<IGenericMember> getMembers() {
		return Collections.unmodifiableList(new ArrayList<>(members.values()));
	}

	@Override
	public List<IGenericModelElement> getAllNodes() {
		List<IGenericModelElement> nodes = new ArrayList<>();
		nodes.addAll(diagrams.values().stream().map(d -> (IGenericModelElement) d).collect(Collectors.toList()));
		nodes.addAll(groups.values().stream().map(g -> (IGenericModelElement) g).collect(Collectors.toList()));
		nodes.addAll(links.values().stream().map(l -> (IGenericModelElement) l).collect(Collectors.toList()));
		nodes.addAll(leafs.values().stream().map(l -> (IGenericModelElement) l).collect(Collectors.toList()));
		nodes.addAll(members.values().stream().map(m -> (IGenericModelElement) m).collect(Collectors.toList()));
		nodes.addAll(stereotypes.values().stream().map(s -> (IGenericModelElement) s).collect(Collectors.toList()));
		return nodes;
	}


	@Override
	public List<IGenericModelElement> getAllEdges() {

		return edges.values().stream()
						.map(e -> ((IGenericModelElement) e))
						.collect(Collectors.toList());
	}

	@Override
	public void acceptVisitor(IGenericDiagramVisitor visitor) {
		for (IGenericDiagram diagram : getDiagrams()) {
			diagram.acceptVisitor(visitor);
		}
		for (IGenericGroup group : getGroups()) {
			group.acceptVisitor(visitor);
		}
		for (IGenericLeaf leaf : getLeafs()) {
			leaf.acceptVisitor(visitor);
		}
		for (IGenericLink link : getLinks()) {
			link.acceptVisitor(visitor);
		}
		for (IGenericMember member : getMembers()) {
			member.acceptVisitor(visitor);
		}
		for (IGenericStereotype stereotype : getStereotypes()) {
			stereotype.acceptVisitor(visitor);
		}
		for (IGenericEdge edge : getEdges()) {
			edge.acceptVisitor(visitor);
		}
	}
}

