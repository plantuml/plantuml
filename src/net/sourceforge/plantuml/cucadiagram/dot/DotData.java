/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 12087 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;
import net.sourceforge.plantuml.svek.DotMode;
import net.sourceforge.plantuml.ugraphic.ColorMapper;

final public class DotData implements PortionShower {

	final private List<Link> links;
	final private Collection<ILeaf> leafs;
	final private UmlDiagramType umlDiagramType;
	final private ISkinParam skinParam;
	final private Rankdir rankdir;
	final private GroupHierarchy groupHierarchy;
	final private IGroup topParent;
	final private PortionShower portionShower;
	final private boolean isHideEmptyDescriptionForState;
	final private DotMode dotMode;

	private final ColorMapper colorMapper;
	private final EntityFactory entityFactory;

	public DotData(IGroup topParent, List<Link> links, Collection<ILeaf> leafs, UmlDiagramType umlDiagramType,
			ISkinParam skinParam, Rankdir rankdir, GroupHierarchy groupHierarchy, PortionShower portionShower,
			ColorMapper colorMapper, EntityFactory entityFactory, boolean isHideEmptyDescriptionForState,
			DotMode dotMode) {
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
		this.rankdir = rankdir;
		this.groupHierarchy = groupHierarchy;
		this.portionShower = portionShower;
		this.entityFactory = entityFactory;
	}

	public DotData(IGroup topParent, List<Link> links, Collection<ILeaf> leafs, UmlDiagramType umlDiagramType,
			ISkinParam skinParam, Rankdir rankdir, GroupHierarchy groupHierarchy, ColorMapper colorMapper,
			EntityFactory entityFactory, boolean isHideEmptyDescriptionForState, DotMode dotMode) {
		this(topParent, links, leafs, umlDiagramType, skinParam, rankdir, groupHierarchy, new PortionShower() {
			public boolean showPortion(EntityPortion portion, ILeaf entity) {
				return true;
			}
		}, colorMapper, entityFactory, isHideEmptyDescriptionForState, dotMode);
	}

	public UmlDiagramType getUmlDiagramType() {
		return umlDiagramType;
	}

	public ISkinParam getSkinParam() {
		return skinParam;
	}

	public Rankdir getRankdir() {
		return rankdir;
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

	public boolean showPortion(EntityPortion portion, ILeaf entity) {
		return portionShower.showPortion(portion, entity);
	}

	public final ColorMapper getColorMapper() {
		return colorMapper;
	}

	public IGroup getRootGroup() {
		return entityFactory.getRootGroup();
	}

	public final boolean isHideEmptyDescriptionForState() {
		return isHideEmptyDescriptionForState;
	}

	public final DotMode getDotMode() {
		return dotMode;
	}

}
