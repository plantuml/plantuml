
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.sourceforge.plantuml.genericdiagram.GenericEdgeType;
import net.sourceforge.plantuml.genericdiagram.GenericEntityType;
import net.sourceforge.plantuml.genericdiagram.GenericLinkStyle;
import net.sourceforge.plantuml.genericdiagram.IGenericDiagram;
import net.sourceforge.plantuml.genericdiagram.IGenericEdge;
import net.sourceforge.plantuml.genericdiagram.IGenericEntity;
import net.sourceforge.plantuml.genericdiagram.IGenericGroup;
import net.sourceforge.plantuml.genericdiagram.IGenericLeaf;
import net.sourceforge.plantuml.genericdiagram.IGenericLink;
import net.sourceforge.plantuml.genericdiagram.IGenericMember;
import net.sourceforge.plantuml.genericdiagram.IGenericModelAccess;
import net.sourceforge.plantuml.genericdiagram.IGenericModelElement;
import net.sourceforge.plantuml.genericdiagram.IGenericStereotype;

/**
 * Provides convenience methods to access the data model
 */
public class GenericModelAccessor {

	IGenericModelAccess model;

	public GenericModelAccessor(IGenericModelAccess model) {
		this.model = model;
	}

	public List<IGenericDiagram> getDiagrams() {

		return model.getDiagrams();

	}

	public List<IGenericGroup> getGroups() {

		return model.getGroups();

	}

	public List<IGenericLink> getLinks() {

		return model.getLinks();

	}

	public List<IGenericLink> getLinksByStyle(GenericLinkStyle linkStyle) {
		return getLinks().stream()
						.filter(l -> l.getStyle() == linkStyle)
						.collect(Collectors.toList());
	}

	public List<IGenericLeaf> getLeafs() {

		return model.getLeafs();

	}

	public List<IGenericMember> getMembers() {

		return model.getMembers();

	}

	public List<IGenericStereotype> getStereotypes() {

		return model.getStereotypes();

	}

	public List<IGenericEdge> getEdges() {

		return model.getEdges();
	}

	public List<IGenericEdge> getEdgesByType(GenericEdgeType edgeType) {
		return getEdges().stream()
						.filter(e -> e.getEdgeType() == edgeType)
						.collect(Collectors.toList());
	}

	public List<IGenericEntity> getEntities() {

		List<IGenericEntity> entities = new ArrayList<>();
		entities.addAll(getGroups());
		entities.addAll(getLeafs());
		return entities;

	}

	public List<IGenericEntity> getEntitiesByType(GenericEntityType type) {
		return getEntities().stream()
						.filter(e -> e.getType() == type)
						.collect(Collectors.toList());
	}

	public List<IGenericEntity> getEntitiesByTypeAndName(GenericEntityType type, String label) {
		return getEntities().stream()
						.filter(e -> e.getType() == type)
						.filter((e -> e.getLabel().equals(label)))
						.collect(Collectors.toList());
	}

	public List<IGenericEntity> getEntitiesByTypeAndId(GenericEntityType type, Integer id) {
		return getEntities().stream()
						.filter(e -> e.getType() == type)
						.filter(e -> e.getId() == id)
						.collect(Collectors.toList());
	}

	public List<IGenericLink> getLinksById(int id) {
		return model.getLinks().stream()
						.filter(e -> e.getId() == id)
						.collect(Collectors.toList());
	}


	public List<IGenericStereotype> getStereotypesOf(IGenericEntity entity) {

			int[] stereoTypeIds = getEdges().stream()
							.filter(e -> e.getEdgeType() == GenericEdgeType.STEREOTYPE)
							.filter(e -> e.getSource() == entity.getId())
							.map((e -> e.getTarget()))
							.mapToInt(Integer::intValue)
							.toArray();
			return getStereotypes().stream()
							.filter(s -> IntStream.of(stereoTypeIds).anyMatch(i -> i == s.getId()))
							.collect(Collectors.toList());
	}


	public Boolean hasStereotypeNamed(IGenericEntity entity, String name) {
		try {
			return getStereotypesOf(entity)
							.stream()
							.filter(s -> s.getLabel().equals(name))
							.count() > 0;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	public Boolean existsEdge(IGenericEntity src, GenericEdgeType edgeType, IGenericEntity tgt) {

		return 	getEdges(src, edgeType, tgt).size() > 0;

	}

	private List<IGenericEdge> getEdges(IGenericModelElement src, GenericEdgeType edgeType, IGenericModelElement tgt) {
		return getEdges().stream()
						.filter(e -> e.getEdgeType() == edgeType)
						.filter(e -> e.getSource() == src.getId())
						.filter(e -> e.getTarget() == tgt.getId())
						.collect(Collectors.toList());
	}


	public Boolean existsLink(GenericEntity src, GenericLinkStyle linkStyle, GenericEntity tgt) {

		return  getLink(src, linkStyle, tgt) != null;

	}

	//TODO check type of nodes, Member does not fit yet
	public IGenericLink getLink(IGenericEntity src, GenericLinkStyle linkStyle, IGenericEntity tgt) {

		List<IGenericEdge> srcToLink = getEdges().stream()
						.filter(e -> e.getEdgeType() == GenericEdgeType.IS_SOURCE)
						.filter(e -> e.getSource() == src.getId())
						.collect(Collectors.toList());

		List<IGenericEdge> tgtToLink = getEdges().stream()
						.filter(e -> e.getEdgeType() == GenericEdgeType.IS_TARGET)
						.filter(e -> e.getSource() == tgt.getId())
						.collect(Collectors.toList());

		for (IGenericEdge e1 : srcToLink) {
			for (IGenericEdge e2 : tgtToLink) {
				if (e1.getTarget() == (e2.getTarget())) {
					int linkId = e1.getTarget();
					Optional<IGenericLink> link = getLinksById(linkId).stream().findFirst();
					if (link.isPresent()) {
						return link.get();
					}
				}
			}
		}
		return null;
	}

	/**
	 * plantUML Links are represented as entities; the connection to the source
	 * and target entity of the link is done using edges with the type IS_TARGET and IS_SOURCE
	 **/
	public IGenericEntity getLinkTarget(GenericLink genericLink) {

		final IGenericEdge  edge = getEdges().stream()
						.filter(e -> e.getEdgeType() == GenericEdgeType.IS_TARGET)
						.filter(e -> e.getTarget() == genericLink.getId())
						.findAny().get();

		return getEntities().stream()
						.filter(e -> e.getId() == edge.getSource())
						.findAny().get();

	}

	public IGenericEntity getLinkSource(GenericLink genericLink) {

		final IGenericEdge  edge = getEdges().stream()
						.filter(e -> e.getEdgeType() == GenericEdgeType.IS_SOURCE)
						.filter(e -> e.getSource() == genericLink.getId())
						.findAny().get();

		return getEntities().stream()
						.filter(e -> e.getId() == edge.getSource())
						.findAny().get();

	}
}

