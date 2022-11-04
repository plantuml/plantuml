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
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.baraye.b;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.baraye.Plasma;
import net.sourceforge.plantuml.baraye.Quark;
import net.sourceforge.plantuml.baraye.a.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.BodierJSon;
import net.sourceforge.plantuml.cucadiagram.BodierMap;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.HideOrShow2;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.entity.IEntityFactory;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public final class ZEntityFactory implements IEntityFactory {

	private final List<Link> links = new ArrayList<>();

	private int rawLayout;

	private final Plasma plasma;

	private final List<HideOrShow2> hides2;
	private final List<HideOrShow2> removed;
	/* private */ final public ICucaDiagram namespaceSeparator;
	// private final boolean mergeIntricated;
	// private Map<IGroup, ILeaf> emptyGroupsAsNode = new HashMap<IGroup, ILeaf>();

	public ILeaf getLeafForEmptyGroup(IGroup g) {
//		return emptyGroupsAsNode.get(g);
		throw new UnsupportedOperationException();
	}

	public ILeaf createLeafForEmptyGroup(IGroup g, ISkinParam skinParam) {
		throw new UnsupportedOperationException();
//		final ILeaf folder = this.createLeaf(g.getIdent(), g.getCode(), g.getDisplay(), LeafType.EMPTY_PACKAGE,
//				g.getParentContainer(), null, this.namespaceSeparator.getNamespaceSeparator());
//		((ZEntityImpl) folder).setOriginalGroup(g);
//		final USymbol symbol = g.getUSymbol();
//		folder.setUSymbol(symbol);
//		folder.setStereotype(g.getStereotype());
//		folder.setColors(g.getColors());
//		if (g.getUrl99() != null)
//			folder.addUrl(g.getUrl99());
//		for (Stereotag tag : g.stereotags())
//			folder.addStereotag(tag);
//
//		emptyGroupsAsNode.put(g, folder);
//		return folder;
	}
//
//	public Display getIntricatedDisplay(Ident ident) {
//		final Set<Ident> known = new HashSet<>(groups2.keySet());
//		known.removeAll(hiddenBecauseOfIntrication);
//		String sep = namespaceSeparator.getNamespaceSeparator();
//		if (sep == null)
//			sep = ".";
//
//		for (int check = ident.size() - 1; check > 0; check--)
//			if (known.contains(ident.getPrefix(check)))
//				// if (hiddenBecauseOfIntrication.contains(ident.getPrefix(check)) == false)
//				return Display.getWithNewlines(ident.getSuffix(check).toString(sep))
//						.withCreoleMode(CreoleMode.SIMPLE_LINE);
//
//		return Display.getWithNewlines(ident.toString(sep)).withCreoleMode(CreoleMode.SIMPLE_LINE);
//	}
//
//	private final Collection<Ident> hiddenBecauseOfIntrication = new ArrayList<>();
//
	public IGroup isIntricated(IGroup parent) {
//		final int leafs = parent.getLeafsDirect().size();
//		final Collection<IGroup> children = parent.getChildren();
//		if (leafs == 0 && children.size() == 1) {
//			final IGroup g = children.iterator().next();
//			if (g.getLeafsDirect().size() == 0 && g.getChildren().size() == 0 && g.getGroupType() == GroupType.PACKAGE)
//				return null;
//
//			for (Link link : this.getLinks())
//				if (link.contains(parent))
//					return null;
//
//			((ZEntityImpl) g).setIntricated(true);
//			hiddenBecauseOfIntrication.add(parent.getIdent());
//			return g;
//		}
//		return null;
		throw new UnsupportedOperationException();
	}

	public ZEntityFactory(List<HideOrShow2> hides2, List<HideOrShow2> removed, ICucaDiagram namespaceSeparator) {
		this.hides2 = hides2;
		this.removed = removed;
		this.namespaceSeparator = namespaceSeparator;
		// this.plasma = new Plasma(namespaceSeparator.getNamespaceSeparator());
		this.plasma = new Plasma(".");
	}

	public boolean isHidden(EntityImp leaf) {
		return false;
//		final IEntity other = isNoteWithSingleLinkAttachedTo(leaf);
//		if (other instanceof ILeaf)
//			return isHidden((ILeaf) other);
//
//		boolean hidden = false;
//		for (HideOrShow2 hide : hides2)
//			hidden = hide.apply(hidden, leaf);
//
//		return hidden;
	}

	public boolean isRemoved(Stereotype stereotype) {
		boolean result = false;
		for (HideOrShow2 hide : removed)
			result = hide.apply(result, stereotype);

		return result;
	}

	public boolean isRemoved(EntityImp leaf) {
		return false;
//		final IEntity other = isNoteWithSingleLinkAttachedTo(leaf);
//		if (other instanceof ILeaf)
//			return isRemoved((ILeaf) other);
//
//		boolean result = false;
//		for (HideOrShow2 hide : removed)
//			result = hide.apply(result, leaf);
//
//		return result;
	}

//	private IEntity isNoteWithSingleLinkAttachedTo(ILeaf leaf) {
//		if (leaf.getLeafType() != LeafType.NOTE)
//			return null;
//		IEntity result = null;
//		for (Link link : this.getLinks()) {
//			if (link.getType().isInvisible())
//				continue;
//			if (link.contains(leaf)) {
//				if (result != null)
//					return result;
//				result = link.getOther(leaf);
//			}
//		}
//		return result;
//
//	}

//	public boolean isRemovedIgnoreUnlinked(ILeaf leaf) {
//		boolean result = false;
//		for (HideOrShow2 hide : removed)
//			if (hide.isAboutUnlinked() == false)
//				result = hide.apply(result, leaf);
//
//		return result;
//	}

	public EntityImp createLeaf(Quark quark, Display display, LeafType entityType, Set<VisibilityModifier> hides,
			String namespaceSeparator) {
		final Bodier bodier;
		if (Objects.requireNonNull(entityType) == LeafType.MAP)
			bodier = new BodierMap();
		else if (Objects.requireNonNull(entityType) == LeafType.JSON)
			bodier = new BodierJSon();
		else
			bodier = BodyFactory.createLeaf(entityType, hides);

		final EntityImp result = new EntityImp(quark, this, bodier, entityType, rawLayout);
		// bodier.setLeaf(result);
		result.setDisplay(display);
		return result;
	}

	public EntityImp createGroup(Quark quark, Display display, GroupType groupType, Set<VisibilityModifier> hides,
			String namespaceSeparator) {
		Objects.requireNonNull(groupType);
		if (quark.getData() != null)
			return (EntityImp) quark.getData();
//		for (Entry<Ident, IGroup> ent : groups2.entrySet())
//			if (ent.getKey().equals(ident))
//				return ent.getValue();

		final Bodier bodier = BodyFactory.createGroup(hides);
		final EntityImp result = new EntityImp(quark, this, bodier, groupType, rawLayout);
		if (Display.isNull(display) == false)
			result.setDisplay(display);

		return result;
	}

	public void addLeaf(ILeaf entity) {
//		if (namespaceSeparator.V1972() == false)
//			leafsByCode.put(entity.getCodeGetName(), entity);
//		leafs2.put(entity.getIdent(), entity);
//		if (namespaceSeparator.V1972())
//			ensureParentIsCreated(entity.getIdent());
		throw new UnsupportedOperationException();
	}
//
//	public void addGroup(IGroup group) {
//		if (namespaceSeparator.V1972() == false)
//			groupsByCode.put(group.getCodeGetName(), group);
//		groups2.put(group.getIdent(), group);
//		if (namespaceSeparator.V1972())
//			ensureParentIsCreated(group.getIdent());
//	}
//
//	private void ensureParentIsCreated(Ident ident) {
//		if (groups2.get(ident.parent()) != null)
//			return;
//		getParentContainer(ident, null);
//	}

//	void removeGroup(String name) {
//		if (namespaceSeparator.V1972())
//			throw new UnsupportedOperationException();
//		final IEntity removed = Objects.requireNonNull(groupsByCode.remove(name));
//		final IEntity removed2 = groups2.remove(removed.getIdent());
//		if (removed != removed2) {
//			bigError();
//		}
//	}
//
//	void removeGroup(Ident ident) {
//		Objects.requireNonNull(groups2.remove(Objects.requireNonNull(ident)));
//	}
//
	public static void bigError() {
		// Thread.dumpStack();
		// System.exit(0);
		throw new IllegalArgumentException();
	}

//	void removeLeaf(String name) {
//		if (namespaceSeparator.V1972())
//			throw new UnsupportedOperationException();
//		final IEntity removed = Objects.requireNonNull(leafsByCode.remove(Objects.requireNonNull(name)));
//		final IEntity removed2 = leafs2.remove(removed.getIdent());
//		if (removed != removed2) {
//			bigError();
//		}
//	}
//
//	void removeLeaf(Ident ident) {
//		final IEntity removed = leafs2.remove(Objects.requireNonNull(ident));
//		if (removed == null) {
//			System.err.println("leafs2=" + leafs2.keySet());
//			throw new IllegalArgumentException(ident.toString());
//		}
//	}
//
//	private void removeLeaf1972(ILeaf leaf) {
//		final boolean removed = leafs2.values().remove(leaf);
//		if (removed == false) {
//			System.err.println("leafs2=" + leafs2.keySet());
//			throw new IllegalArgumentException(leaf.toString());
//		}
//	}
//
//	public IGroup muteToGroup(String name, Code namespace, GroupType type, IGroup parent) {
//		if (namespaceSeparator.V1972())
//			throw new UnsupportedOperationException();
//		final ILeaf leaf = leafsByCode.get(name);
//		((ZEntityImpl) leaf).muteToGroup(namespace, type, parent);
//		final IGroup result = (IGroup) leaf;
//		removeLeaf(name);
//		return result;
//	}

//	public IGroup muteToGroup1972(Ident ident, Code namespace, GroupType type, IGroup parent) {
//		if (!namespaceSeparator.V1972())
//			throw new UnsupportedOperationException();
//		final ILeaf leaf;
//		if (namespaceSeparator.getNamespaceSeparator() == null)
//			leaf = getLeafVerySmart(ident);
//		else
//			leaf = leafs2.get(ident);
//		((ZEntityImpl) leaf).muteToGroup(namespace, type, parent);
//		final IGroup result = (IGroup) leaf;
//		removeLeaf1972(leaf);
//		return result;
//	}

	public IGroup getRootGroup() {
		throw new UnsupportedOperationException();
//		return rootGroup;
	}

	public final ILeaf getLeafStrict(Ident ident) {
		throw new UnsupportedOperationException();
//		return leafs2.get(ident);
	}
//
//	public final ILeaf getLeafSmart(Ident ident) {
//		if (!namespaceSeparator.V1972())
//			throw new UnsupportedOperationException();
//		final ILeaf result = leafs2.get(ident);
//		if (result == null && ident.size() == 1)
//			for (Entry<Ident, ILeaf> ent : leafs2.entrySet())
//				if (ent.getKey().getLast().equals(ident.getLast()))
//					return ent.getValue();
//
//		return result;
//	}
//
//	public final ILeaf getLeafVerySmart(Ident ident) {
//		if (!namespaceSeparator.V1972())
//			throw new UnsupportedOperationException();
//		final ILeaf result = leafs2.get(ident);
//		if (result == null)
//			for (Entry<Ident, ILeaf> ent : leafs2.entrySet())
//				if (ent.getKey().getLast().equals(ident.getLast()))
//					return ent.getValue();
//
//		return result;
//	}
//
//	public Ident buildFullyQualified(Ident currentPath, Ident id) {
//		if (currentPath.equals(id) == false)
//			if (leafs2.containsKey(id) || groups2.containsKey(id))
//				return id;
//
//		if (id.size() > 1)
//			return id;
//
//		return currentPath.add(id);
//	}
//
//	public final IGroup getGroupStrict(Ident ident) {
//		if (namespaceSeparator.getNamespaceSeparator() == null)
//			return getGroupVerySmart(ident);
//
//		final IGroup result = groups2.get(ident);
//		return result;
//	}
//
//	public final IGroup getGroupVerySmart(Ident ident) {
//		final IGroup result = groups2.get(ident);
//		if (result == null)
//			for (Entry<Ident, IGroup> ent : groups2.entrySet())
//				if (ent.getKey().getLast().equals(ident.getLast()))
//					return ent.getValue();
//
//		return result;
//	}
//
//	public final ILeaf getLeaf(Code code) {
//		if (namespaceSeparator.V1972())
//			throw new UnsupportedOperationException();
//		final ILeaf result = leafsByCode.get(code.getName());
//		if (result != null && result != leafs2.get(result.getIdent()))
//			bigError();
//
//		return result;
//	}
//
//	public final IGroup getGroup(Code code) {
//		if (namespaceSeparator.V1972())
//			throw new UnsupportedOperationException();
//		final IGroup result = groupsByCode.get(code.getName());
//		if (result != null && result != groups2.get(result.getIdent()))
//			bigError();
//
//		return result;
//	}

	public final Collection<ILeaf> leafs() {
//		if (namespaceSeparator.V1972())
//			return leafs2();
//		final Collection<ILeaf> result = Collections.unmodifiableCollection(leafsByCode.values());
//		if (new ArrayList<>(result).equals(new ArrayList<>(leafs2())) == false)
//			bigError();
//
//		return result;
		throw new UnsupportedOperationException();
	}

	public final Collection<IGroup> groups() {
//		if (namespaceSeparator.V1972())
//			return groups2();
//		final Collection<IGroup> result = Collections.unmodifiableCollection(groupsByCode.values());
//		if (new ArrayList<>(result).equals(new ArrayList<>(groups2())) == false)
//			bigError();
//
//		return result;
		throw new UnsupportedOperationException();
	}
//
//	public final Collection<IGroup> groups2() {
//		final Collection<IGroup> result = Collections.unmodifiableCollection(groups2.values());
//		return Collections.unmodifiableCollection(result);
//	}
//
//	public final Collection<ILeaf> leafs2() {
//		final Collection<ILeaf> result = Collections.unmodifiableCollection(leafs2.values());
//		return Collections.unmodifiableCollection(result);
//	}

	public void incRawLayout() {
		rawLayout++;
	}

	public final List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

	public void addLink(Link link) {
		if (link.isSingle() && containsSimilarLink(link))
			return;

		links.add(link);
	}

	private boolean containsSimilarLink(Link other) {
		for (Link link : links)
			if (other.sameConnections(link))
				return true;

		return false;
	}

	public void removeLink(Link link) {
		final boolean ok = links.remove(link);
		if (ok == false)
			throw new IllegalArgumentException();

	}

//	public IGroup getParentContainer(Ident ident, IGroup parentContainer) {
//		if (namespaceSeparator.V1972()) {
//			final Ident parent = ident.parent();
//			if (parent.isRoot())
//				return this.rootGroup;
//
//			IGroup result = getGroupStrict(parent);
//			if (result != null)
//				return result;
//
//			final Display display = Display.getWithNewlines(parent.getName());
//			result = createGroup(parent, parent, display, null, GroupType.PACKAGE, null,
//					Collections.<VisibilityModifier>emptySet(), namespaceSeparator.getNamespaceSeparator());
//			addGroup(result);
//			return result;
//		}
//		return Objects.requireNonNull(parentContainer);
//	}

	public ICucaDiagram getDiagram() {
		return namespaceSeparator;
	}

}
