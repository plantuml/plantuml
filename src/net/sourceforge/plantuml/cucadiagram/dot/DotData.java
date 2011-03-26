/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6222 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.skin.VisibilityModifier;

final public class DotData implements PortionShower {

	final private List<Link> links;
	final private Map<String, ? extends IEntity> entities;
	final private UmlDiagramType umlDiagramType;
	final private ISkinParam skinParam;
	final private Rankdir rankdir;
	final private GroupHierarchy groupHierarchy;
	final private Group topParent;
	final private PortionShower portionShower;
	private int dpi = 96;

	private StaticFilesMap staticFilesMap;
	private boolean visibilityModifierPresent;

	public DotData(Group topParent, List<Link> links, Map<String, ? extends IEntity> entities,
			UmlDiagramType umlDiagramType, ISkinParam skinParam, Rankdir rankdir, GroupHierarchy groupHierarchy,
			PortionShower portionShower) {
		this.topParent = topParent;
		this.links = links;
		this.entities = entities;
		this.umlDiagramType = umlDiagramType;
		this.skinParam = skinParam;
		this.rankdir = rankdir;
		this.groupHierarchy = groupHierarchy;
		this.portionShower = portionShower;
	}

	public DotData(Group topParent, List<Link> links, Map<String, ? extends IEntity> entities,
			UmlDiagramType umlDiagramType, ISkinParam skinParam, Rankdir rankdir, GroupHierarchy groupHierarchy) {
		this(topParent, links, entities, umlDiagramType, skinParam, rankdir, groupHierarchy, new PortionShower() {
			public boolean showPortion(EntityPortion portion, IEntity entity) {
				return true;
			}
		});
	}

	public boolean hasUrl() {
		return true;
	}

	public DrawFile getStaticImages(EntityType type, String stereo) throws IOException {
		checkObjectOrClassDiagram();
		assert type == EntityType.ABSTRACT_CLASS || type == EntityType.CLASS || type == EntityType.ENUM
				|| type == EntityType.INTERFACE || type == EntityType.LOLLIPOP;
		return staticFilesMap.getStaticFiles(stereo).getStaticImages(type);
	}

	public DrawFile getVisibilityImages(VisibilityModifier visibilityModifier, String stereo) throws IOException {
		checkObjectOrClassDiagram();
		return staticFilesMap.getStaticFiles(stereo).getVisibilityImages(visibilityModifier);
	}
	
	public boolean isThereVisibilityImages() {
		return visibilityModifierPresent;
	}
	
	public void setVisibilityModifierPresent(boolean b) {
		checkObjectOrClassDiagram();
		this.visibilityModifierPresent = b;
	}



	public void setStaticImagesMap(StaticFilesMap staticFilesMap) {
		checkObjectOrClassDiagram();
		this.staticFilesMap = staticFilesMap;
	}

	private void checkObjectOrClassDiagram() {
		if (umlDiagramType != UmlDiagramType.CLASS && umlDiagramType != UmlDiagramType.OBJECT) {
			throw new IllegalStateException();
		}
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

	public Map<String, ? extends IEntity> getEntities() {
		return entities;
	}

	public final Set<IEntity> getAllLinkedTo(final IEntity ent1) {
		final Set<IEntity> result = new HashSet<IEntity>();
		result.add(ent1);
		int size = 0;
		do {
			size = result.size();
			for (IEntity ent : entities.values()) {
				if (isDirectyLinked(ent, result)) {
					result.add(ent);
				}
			}
		} while (size != result.size());
		result.remove(ent1);
		return Collections.unmodifiableSet(result);
	}

	public final Set<IEntity> getAllLinkedDirectedTo(final IEntity ent1) {
		final Set<IEntity> result = new HashSet<IEntity>();
		for (IEntity ent : entities.values()) {
			if (isDirectlyLinkedSlow(ent, ent1)) {
				result.add(ent);
			}
		}
		return Collections.unmodifiableSet(result);
	}

	private boolean isDirectyLinked(IEntity ent1, Collection<IEntity> others) {
		for (IEntity ent2 : others) {
			if (isDirectlyLinkedSlow(ent1, ent2)) {
				return true;
			}
		}
		return false;
	}

	private boolean isDirectlyLinkedSlow(IEntity ent1, IEntity ent2) {
		for (Link link : links) {
			if (link.isBetween(ent1, ent2)) {
				return true;
			}
		}
		return false;
	}

	public boolean isThereLink(Group g) {
		for (Link l : links) {
			if (l.getEntity1() == g.getEntityCluster() || l.getEntity2() == g.getEntityCluster()) {
				return true;
			}
		}
		return false;
	}

	public List<Link> getAutoLinks(Group g) {
		final List<Link> result = new ArrayList<Link>();
		for (Link l : links) {
			if (l.isAutolink(g)) {
				result.add(l);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public List<Link> getToEdgeLinks(Group g) {
		final List<Link> result = new ArrayList<Link>();
		for (Link l : links) {
			if (l.isToEdgeLink(g)) {
				result.add(l);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public List<Link> getFromEdgeLinks(Group g) {
		final List<Link> result = new ArrayList<Link>();
		for (Link l : links) {
			if (l.isFromEdgeLink(g)) {
				result.add(l);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public final Group getTopParent() {
		return topParent;
	}

	public boolean isEmpty(Group g) {
		return groupHierarchy.isEmpty(g);
	}

	public boolean showPortion(EntityPortion portion, IEntity entity) {
		return portionShower.showPortion(portion, entity);
	}

	public final int getDpi() {
		return dpi;
	}
	
	public double getDpiFactor() {
		if (dpi == 96) {
			return 1.0;
		}
		return dpi / 96.0;
	}

	public final void setDpi(int dpi) {
		this.dpi = dpi;
	}
	
	private boolean hideEmptyDescription = false;

	public final void setHideEmptyDescription(boolean hideEmptyDescription) {
		this.hideEmptyDescription = hideEmptyDescription;
	}

	public final boolean isHideEmptyDescription() {
		return hideEmptyDescription;
	}
	


}
