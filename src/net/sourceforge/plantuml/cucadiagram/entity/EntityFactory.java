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
package net.sourceforge.plantuml.cucadiagram.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupRoot;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.HideOrShow2;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public final class EntityFactory {

	private final Map<String, ILeaf> leafsByCode = new LinkedHashMap<String, ILeaf>();
	private final Map<String, IGroup> groupsByCode = new LinkedHashMap<String, IGroup>();

	private final Map<Ident, ILeaf> leafs2 = new LinkedHashMap<Ident, ILeaf>();
	private final Map<Ident, IGroup> groups2 = new LinkedHashMap<Ident, IGroup>();

	private final List<Link> links = new ArrayList<Link>();

	private int rawLayout;

	private final IGroup rootGroup = new GroupRoot(this);
	private final List<HideOrShow2> hides2;
	private final List<HideOrShow2> removed;

	public EntityFactory(List<HideOrShow2> hides2, List<HideOrShow2> removed) {
		this.hides2 = hides2;
		this.removed = removed;
	}

	public boolean isHidden(ILeaf leaf) {
		boolean hidden = false;
		for (HideOrShow2 hide : hides2) {
			hidden = hide.apply(hidden, leaf);
		}
		return hidden;
	}

	public boolean isRemoved(ILeaf leaf) {
		boolean result = false;
		for (HideOrShow2 hide : removed) {
			result = hide.apply(result, leaf);
		}
		return result;
	}

	public void thisIsGoingToBeALeaf(Ident ident) {
		// if (byIdents.containsKey(ident) == false) {
		// System.err.println("thisIsGoingToBeALeaf::byIdents=" + byIdents.keySet());
		// System.err.println("thisIsGoingToBeALeaf::ident=" + ident);
		// // Thread.dumpStack();
		// // System.exit(0);
		// }
		// byIdents.remove(ident);
	}

	public void thisIsNotArealGroup(Ident ident) {
		// byIdents.remove(ident);
	}

	public ILeaf createLeaf(Ident ident, Code code, Display display, LeafType entityType, IGroup parentContainer,
			Set<VisibilityModifier> hides, String namespaceSeparator) {
		if (entityType == null) {
			throw new IllegalArgumentException();
		}
		// if (byIdents.containsKey(ident)) {
		// throw new IllegalArgumentException();
		// }
		final Bodier bodier = new Bodier(entityType, hides);
		final EntityImpl result = new EntityImpl(ident, code, this, bodier, parentContainer, entityType,
				namespaceSeparator, rawLayout);
		bodier.setLeaf(result);
		result.setDisplay(display);
		// System.err.println("adding1 "+ident);
		// byIdents.put(ident, result);
		// System.err.println("EntityFactory::createLeaf=" + result);
		return result;
	}

	public IGroup createGroup(Ident ident, Code code, Display display, Code namespace, GroupType groupType,
			IGroup parentContainer, Set<VisibilityModifier> hides, String namespaceSeparator) {
		if (groupType == null) {
			throw new IllegalArgumentException();
		}
		// if (byIdents.containsKey(ident)) {
		// throw new IllegalArgumentException();
		// }
		final Bodier bodier = new Bodier(null, hides);
		final EntityImpl result = new EntityImpl(ident, code, this, bodier, parentContainer, groupType, namespace,
				namespaceSeparator, rawLayout);
		if (Display.isNull(display) == false) {
			result.setDisplay(display);
		}
		// System.err.println("adding2 "+ident);
		// byIdents.put(ident, result);
		// System.err.println("EntityFactory::createGroup=" + result);
		return result;
	}

	public void addLeaf(ILeaf entity) {
		// System.err.println("EntityFactory::addLeaf=" + entity);
		leafsByCode.put(entity.getCodeGetName(), entity);
		leafs2.put(entity.getIdent(), entity);
	}

	public void addGroup(IGroup group) {
		// System.err.println("EntityFactory::addGroup=" + group);
		groupsByCode.put(group.getCodeGetName(), group);
		groups2.put(group.getIdent(), group);
	}

	void removeGroup(String name) {
		final IEntity removed = groupsByCode.remove(name);
		if (removed == null) {
			throw new IllegalArgumentException();
		}
		final IEntity removed2 = groups2.remove(removed.getIdent());
		if (removed != removed2) {
			bigError();
		}
	}

	public static void bigError() {
		Thread.dumpStack();
		// System.exit(0);
		// throw new IllegalArgumentException();
	}

	void removeLeaf(String name) {
		final IEntity removed = leafsByCode.remove(name);
		if (removed == null) {
			throw new IllegalArgumentException();
		}
		final IEntity removed2 = leafs2.remove(removed.getIdent());
		if (removed != removed2) {
			bigError();
		}
	}

	public IGroup muteToGroup(String name, Code namespace, GroupType type, IGroup parent) {
		final ILeaf leaf = leafsByCode.get(name);
		((EntityImpl) leaf).muteToGroup(namespace, type, parent);
		final IGroup result = (IGroup) leaf;
		removeLeaf(name);
		return result;
	}

	public IGroup getRootGroup() {
		return rootGroup;
	}

	public final ILeaf getLeaf(Ident ident) {
		return leafs2.get(ident);
	}

	public final ILeaf getLeaf(Code code) {
		final ILeaf result = leafsByCode.get(code.getName());
		if (result != null && result != leafs2.get(result.getIdent())) {
			bigError();
		}
		return result;
	}

	public final IGroup getGroup(Code code) {
		final IGroup result = groupsByCode.get(code.getName());
		if (result != null && result != groups2.get(result.getIdent())) {
			bigError();
		}
		return result;
	}

	public final IGroup getGroup(Ident ident) {
		final IGroup result = groups2.get(ident);
		return result;
	}

	public final Collection<ILeaf> leafs() {
		final Collection<ILeaf> result = Collections.unmodifiableCollection(leafsByCode.values());
		if (new ArrayList<ILeaf>(result).equals(new ArrayList<ILeaf>(leafs2())) == false) {
			bigError();
		}
		return result;
	}

	public final Collection<IGroup> groups() {
		final Collection<IGroup> result = Collections.unmodifiableCollection(groupsByCode.values());
		if (new ArrayList<IGroup>(result).equals(new ArrayList<IGroup>(groups2())) == false) {
			bigError();
		}
		return result;
	}

	public final Collection<IGroup> groups2() {
		final Collection<IGroup> result = Collections.unmodifiableCollection(groups2.values());
		return Collections.unmodifiableCollection(result);
	}

	public final Collection<ILeaf> leafs2() {
		final Collection<ILeaf> result = Collections.unmodifiableCollection(leafs2.values());
		return Collections.unmodifiableCollection(result);
	}

	public void incRawLayout() {
		rawLayout++;
	}

	public final List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

	public void addLink(Link link) {
		if (link.isSingle() && containsSimilarLink(link)) {
			return;
		}
		links.add(link);
	}

	private boolean containsSimilarLink(Link other) {
		for (Link link : links) {
			if (other.sameConnections(link)) {
				return true;
			}
		}
		return false;
	}

	public void removeLink(Link link) {
		final boolean ok = links.remove(link);
		if (ok == false) {
			throw new IllegalArgumentException();
		}
	}

	public IGroup getParentContainer(Ident ident, IGroup parentContainer) {
		if (parentContainer == null) {
			throw new IllegalArgumentException();
		}
		// if (parentContainer == null) {
		// } else {
		// final Ident identParent = parentContainer.getIdent();
		// if (ident.parent().equals(identParent) == false) {
		// System.err.println("ident=" + ident);
		// System.err.println("parentContainer=" + identParent);
		// Thread.dumpStack();
		// System.exit(0);
		// }
		// }
		return parentContainer;
	}

}
