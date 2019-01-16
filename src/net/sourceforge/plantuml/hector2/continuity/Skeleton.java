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
package net.sourceforge.plantuml.hector2.continuity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;

public class Skeleton {

	private final Set<IEntity> entities = new HashSet<IEntity>();
	private final List<Link> links = new ArrayList<Link>();

	private Set<IEntity> getDirectChildren(IEntity parent) {
		final Set<IEntity> result = new HashSet<IEntity>();
		for (Link link : links) {
			if (link.isAutolink()) {
				continue;
			}
			if (link.getEntity1() == parent) {
				result.add(link.getEntity2());
			}
		}
		return Collections.unmodifiableSet(result);
	}

	@Override
	public String toString() {
		return "skeleton " + links;
	}

	private Set<IEntity> getIndirectChildren(IEntity parent) {
		final Set<IEntity> result = new HashSet<IEntity>(getDirectChildren(parent));
		int currentSize = result.size();
		while (true) {
			for (IEntity ent : new HashSet<IEntity>(result)) {
				result.addAll(getDirectChildren(ent));
			}
			if (result.contains(parent) || result.size() == currentSize) {
				return Collections.unmodifiableSet(result);
			}
			currentSize = result.size();
		}
	}

	private boolean hasCycle() {
		for (IEntity ent : entities) {
			if (getIndirectChildren(ent).contains(ent)) {
				return true;
			}
		}
		return false;
	}

	public Skeleton removeCycle() {
		final Skeleton result = new Skeleton();
		for (Link link : links) {
			result.add(link);
			if (result.hasCycle()) {
				result.links.remove(link);
			}
		}
		return result;
	}

	public void add(Link link) {
		if (links.contains(link)) {
			throw new IllegalArgumentException();
		}
		if (link.getEntity1().isGroup()) {
			throw new IllegalArgumentException();
		}
		if (link.getEntity2().isGroup()) {
			throw new IllegalArgumentException();
		}
		links.add(link);
		entities.add(link.getEntity1());
		entities.add(link.getEntity2());
	}

	public void addAll(Skeleton other) {
		for (Link otherLink : other.links) {
			this.add(otherLink);
		}

	}

	public boolean doesTouch(Link other) {
		for (Link link : links) {
			if (link.doesTouch(other)) {
				return true;
			}
		}
		return false;
	}

	public boolean doesTouch(Skeleton other) {
		for (Link link : links) {
			if (other.doesTouch(link)) {
				return true;
			}
		}
		return false;
	}

	public void computeLayers() {
		if (hasCycle()) {
			throw new UnsupportedOperationException();
		}
		for (IEntity ent : entities) {
			ent.setHectorLayer(0);
		}
		boolean changed;
		do {
			changed = false;
			for (Link link : links) {
				if (ensureLayer(link)) {
					changed = true;
				}
			}
		} while (changed);
	}

	private boolean ensureLayer(Link link) {
		final int lenght = link.getLength();
		final int l1 = link.getEntity1().getHectorLayer();
		final int l2 = link.getEntity2().getHectorLayer();
		if (lenght == 1) {
			if (l1 < l2) {
				link.getEntity1().setHectorLayer(l2);
				return true;
			} else if (l2 < l1) {
				link.getEntity2().setHectorLayer(l1);
				return true;
			}
		} else {
			final int l2theoric = l1 + lenght - 1;
			if (l2 < l2theoric) {
				link.getEntity2().setHectorLayer(l2theoric);
				return true;
			}
		}
		return false;
	}

	public Collection<IEntity> entities() {
		return Collections.unmodifiableCollection(entities);
	}

}
