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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Pragma;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;
import net.sourceforge.plantuml.svek.DotMode;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

final public class DotData implements PortionShower {

	final private List<Link> links;
	final private Collection<ILeaf> leafs;
	final private UmlDiagramType umlDiagramType;
	final private ISkinParam skinParam;
	// final private Rankdir rankdir;
	final private GroupHierarchy groupHierarchy;
	final private IGroup topParent;
	final private PortionShower portionShower;
	final private boolean isHideEmptyDescriptionForState;
	final private DotMode dotMode;
	final private String namespaceSeparator;
	final private Pragma pragma;

	private final ColorMapper colorMapper;
	private final EntityFactory entityFactory;

	public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	public DotData(IGroup topParent, List<Link> links, Collection<ILeaf> leafs, UmlDiagramType umlDiagramType,
			ISkinParam skinParam, GroupHierarchy groupHierarchy, PortionShower portionShower, ColorMapper colorMapper,
			EntityFactory entityFactory, boolean isHideEmptyDescriptionForState, DotMode dotMode,
			String namespaceSeparator, Pragma pragma) {
		this.namespaceSeparator = namespaceSeparator;
		this.pragma = pragma;
		this.topParent = topParent;
		if (topParent == null) {
			throw new IllegalArgumentException();
		}
		this.dotMode = dotMode;
		this.isHideEmptyDescriptionForState = isHideEmptyDescriptionForState;
		this.colorMapper = colorMapper;
		this.links = links;
		this.leafs = leafs;
		this.umlDiagramType = umlDiagramType;
		this.skinParam = skinParam;
		// this.rankdir = rankdir;
		this.groupHierarchy = groupHierarchy;
		this.portionShower = portionShower;
		this.entityFactory = entityFactory;
	}

	public DotData(IGroup topParent, List<Link> links, Collection<ILeaf> leafs, UmlDiagramType umlDiagramType,
			ISkinParam skinParam, GroupHierarchy groupHierarchy, ColorMapper colorMapper, EntityFactory entityFactory,
			boolean isHideEmptyDescriptionForState, DotMode dotMode, String namespaceSeparator, Pragma pragma) {
		this(topParent, links, leafs, umlDiagramType, skinParam, groupHierarchy, new PortionShower() {
			public boolean showPortion(EntityPortion portion, IEntity entity) {
				return true;
			}
		}, colorMapper, entityFactory, isHideEmptyDescriptionForState, dotMode, namespaceSeparator, pragma);
	}

	public UmlDiagramType getUmlDiagramType() {
		return umlDiagramType;
	}

	public ISkinParam getSkinParam() {
		return skinParam;
	}

	public GroupHierarchy getGroupHierarchy() {
		return groupHierarchy;
	}

	public List<Link> getLinks() {
		return links;
	}

	public Collection<ILeaf> getLeafs() {
		return leafs;
	}

	public final IGroup getTopParent() {
		return topParent;
	}

	public boolean isEmpty(IGroup g) {
		return groupHierarchy.isEmpty(g);
	}

	public boolean showPortion(EntityPortion portion, IEntity entity) {
		return portionShower.showPortion(portion, entity);
	}

	public final ColorMapper getColorMapper() {
		return colorMapper;
	}

	public IGroup getRootGroup() {
		return entityFactory.getRootGroup();
	}

	public boolean isDegeneratedWithFewEntities(int nb) {
		return entityFactory.groups().size() == 0 && getLinks().size() == 0 && getLeafs().size() == nb;
	}

	public final boolean isHideEmptyDescriptionForState() {
		return isHideEmptyDescriptionForState;
	}

	public final DotMode getDotMode() {
		return dotMode;
	}

	public final String getNamespaceSeparator() {
		return namespaceSeparator;
	}

	public Pragma getPragma() {
		return pragma;
	}

	public void removeIrrelevantSametail() {
		final Map<String, Integer> sametails = new HashMap<String, Integer>();
		for (Link link : links) {
			if (link.getType().getDecor2().isExtendsLike()) {
				link.setSametail(link.getEntity1().getUid());
			}
			final String sametail = link.getSametail();
			if (sametail == null) {
				continue;
			}
			final Integer value = sametails.get(sametail);
			sametails.put(sametail, value == null ? 1 : value + 1);
		}
		final Collection<String> toremove = new HashSet<String>();
		final int limit = skinParam.groupInheritance();
		for (Map.Entry<String, Integer> ent : sametails.entrySet()) {
			final String key = ent.getKey();
			if (ent.getValue() < limit) {
				toremove.add(key);
			} else {
				final List<Link> some = new ArrayList<Link>();
				for (Link link : links) {
					if (key.equals(link.getSametail())) {
						some.add(link);
					}
				}
				final ILeaf leaf = getLeaf(key);
				final Neighborhood neighborhood = new Neighborhood(leaf, some, getLinksOfThisLeaf(leaf));
				leaf.setNeighborhood(neighborhood);
			}
		}

		for (Link link : links) {
			final String sametail = link.getSametail();
			if (sametail == null) {
				continue;
			}
			if (toremove.contains(sametail)) {
				link.setSametail(null);
			}
		}
	}

	private List<Link> getLinksOfThisLeaf(ILeaf leaf) {
		final List<Link> result = new ArrayList<Link>();
		for (Link link : links) {
			if (link.contains(leaf)) {
				result.add(link);
			}
		}
		return result;
	}

	private ILeaf getLeaf(String key) {
		for (ILeaf entity : leafs) {
			if (entity.getUid().equals(key)) {
				return entity;
			}
		}
		return null;

	}

}
