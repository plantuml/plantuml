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
 * Revision $Revision: 6927 $
 *
 */
package net.sourceforge.plantuml.statediagram;

import java.util.List;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;

public class StateDiagram extends AbstractEntityDiagram {

	@Override
	public IEntity getOrCreateClass(String code) {
		if (code.startsWith("[*]")) {
			throw new IllegalArgumentException();
		}
		if (isGroup(code)) {
			return getGroup(code).getEntityCluster();
		}
		final IEntity result = getOrCreateEntity(code, EntityType.STATE);
		return result;
	}

	public IEntity getStart() {
		final Group g = getCurrentGroup();
		if (g == null) {
			return getOrCreateEntity("*start", EntityType.CIRCLE_START);
		}
		return getOrCreateEntity("*start*" + g.getCode(), EntityType.CIRCLE_START);
	}

	public IEntity getEnd() {
		final Group p = getCurrentGroup();
		if (p == null) {
			return getOrCreateEntity("*end", EntityType.CIRCLE_END);
		}
		return getOrCreateEntity("*end*" + p.getCode(), EntityType.CIRCLE_END);
	}

	public IEntity getHistorical() {
		final Group g = getCurrentGroup();
		if (g == null) {
			return getOrCreateEntity("*historical", EntityType.PSEUDO_STATE);
		}
		return getOrCreateEntity("*historical*" + g.getCode(), EntityType.PSEUDO_STATE);
	}

	public IEntity getHistorical(String codeGroup) {
		final Group g = getOrCreateGroup(codeGroup, null, null, GroupType.STATE, null);
		final IEntity result = getOrCreateEntity("*historical*" + g.getCode(), EntityType.PSEUDO_STATE);
		endGroup();
		return result;
	}

	public boolean concurrentState() {
		final Group cur = getCurrentGroup();
		if (cur != null && cur.getType() == GroupType.CONCURRENT_STATE) {
			super.endGroup();
		}
		final Group conc1 = getOrCreateGroup("CONC" + UniqueSequence.getValue(), "", null, GroupType.CONCURRENT_STATE,
				getCurrentGroup());
		conc1.setDashed(true);
		if (cur != null && cur.getType() == GroupType.STATE) {
			cur.moveEntitiesTo(conc1);
			super.endGroup();
			final Group conc2 = getOrCreateGroup("CONC" + UniqueSequence.getValue(), "", null,
					GroupType.CONCURRENT_STATE, getCurrentGroup());
			conc2.setDashed(true);
		}
		return true;
	}

	@Override
	public void endGroup() {
		final Group cur = getCurrentGroup();
		if (cur != null && cur.getType() == GroupType.CONCURRENT_STATE) {
			super.endGroup();
		}
		super.endGroup();
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.STATE;
	}

	private boolean hideEmptyDescription = false;

	public final void setHideEmptyDescription(boolean hideEmptyDescription) {
		this.hideEmptyDescription = hideEmptyDescription;
	}

	public final boolean isHideEmptyDescription() {
		return hideEmptyDescription;
	}
	
	final public Link getLastStateLink() {
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link link = links.get(i);
			if (link.getEntity1().getType() != EntityType.NOTE && link.getEntity2().getType() != EntityType.NOTE) {
				return link;
			}
		}
		return null;
	}



	// @Override
	// final protected List<String> getDotStrings() {
	// return Arrays.asList("nodesep=1.95;", "ranksep=1.8;", "edge
	// [fontsize=11,labelfontsize=11];",
	// "node [fontsize=11,height=.35,width=.55];");
	// }

}
