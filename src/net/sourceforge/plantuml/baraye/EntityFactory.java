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
package net.sourceforge.plantuml.baraye;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.BodierJSon;
import net.sourceforge.plantuml.cucadiagram.BodierMap;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.HideOrShow2;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.entity.IEntityFactory;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public final class EntityFactory implements IEntityFactory {

	private final List<Link> links = new ArrayList<>();

	private int rawLayout;

	private final Plasma plasma;

	private final EntityImp rootGroup;

	private final List<HideOrShow2> hides2;
	private final List<HideOrShow2> removed;
	/* private */ final public ICucaDiagram namespaceSeparator;

	public EntityImp getLeafForEmptyGroup(EntityImp g) {
		throw new UnsupportedOperationException();
	}

	public EntityImp createLeafForEmptyGroup(EntityImp g, ISkinParam skinPdaram) {
		final EntityImp ent = (EntityImp) g;
		ent.muteToType2(LeafType.EMPTY_PACKAGE);
		return ent;
	}

//
	public EntityImp isIntricated(EntityImp parent) {
		throw new UnsupportedOperationException();
	}

	public EntityFactory(List<HideOrShow2> hides2, List<HideOrShow2> removed, ICucaDiagram namespaceSeparator) {
		this.hides2 = hides2;
		this.removed = removed;
		this.namespaceSeparator = namespaceSeparator;
		this.plasma = new Plasma(".");
		this.rootGroup = new EntityImp(this.plasma.root(), this, null, GroupType.ROOT, 0);
		this.plasma.root().setData(rootGroup);
	}

	public boolean isHidden(EntityImp leaf) {
		final EntityImp other = isNoteWithSingleLinkAttachedTo(leaf);
		if (other != null && other != leaf)
			return isHidden(other);

		boolean hidden = false;
		for (HideOrShow2 hide : hides2)
			hidden = hide.apply(hidden, leaf);

		return hidden;
	}

	public boolean isRemoved(Stereotype stereotype) {
		boolean result = false;
		for (HideOrShow2 hide : removed)
			result = hide.apply(result, stereotype);

		return result;
	}

	public boolean isRemoved(EntityImp leaf) {
		final EntityImp other = isNoteWithSingleLinkAttachedTo(leaf);
		if (other instanceof EntityImp)
			return isRemoved((EntityImp) other);

		boolean result = false;
		for (HideOrShow2 hide : removed)
			result = hide.apply(result, leaf);

		return result;
	}

	private EntityImp isNoteWithSingleLinkAttachedTo(EntityImp note) {
		if (note.getLeafType() != LeafType.NOTE)
			return null;
		assert note.getLeafType() == LeafType.NOTE;
		EntityImp other = null;
		for (Link link : this.getLinks()) {
			if (link.getType().isInvisible())
				continue;
			if (link.contains(note) == false)
				continue;
			if (other != null)
				return null;
			other = link.getOther(note);
			if (other.getLeafType() == LeafType.NOTE)
				return null;

		}
		return other;

	}

	public boolean isRemovedIgnoreUnlinked(EntityImp leaf) {
		boolean result = false;
		for (HideOrShow2 hide : removed)
			if (hide.isAboutUnlinked() == false)
				result = hide.apply(result, leaf);

		return result;
	}

	final public EntityImp createLeaf(Quark quark, Display display, LeafType entityType,
			Set<VisibilityModifier> hides) {
		final Bodier bodier;
		if (Objects.requireNonNull(entityType) == LeafType.MAP)
			bodier = new BodierMap();
		else if (Objects.requireNonNull(entityType) == LeafType.JSON)
			bodier = new BodierJSon();
		else
			bodier = BodyFactory.createLeaf(entityType, hides);

		final EntityImp result = new EntityImp(quark, this, bodier, entityType, rawLayout);
		bodier.setLeaf(result);
		result.setDisplay(display);
		return result;
	}

	public EntityImp createGroup(Quark quark, Display display, GroupType groupType, Set<VisibilityModifier> hides) {
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

	public EntityImp getRootGroup() {
		return rootGroup;
	}

	public final EntityImp getLeafStrict(Quark ident) {
		if (ident instanceof Quark == false)
			throw new UnsupportedOperationException();
		final Quark quark = (Quark) ident;
		final EntityImp result = (EntityImp) quark.getData();
		if (result == null)
			throw new UnsupportedOperationException();
		return result;
	}

	public final Collection<EntityImp> leafs() {

		final List<EntityImp> result = new ArrayList<>();
		for (Quark quark : getPlasma().quarks()) {
			if (quark.isRoot())
				continue;
			final EntityImp data = (EntityImp) quark.getData();
			if (data != null && data.isGroup() == false)
				result.add(data);
		}
		return Collections.unmodifiableCollection(result);

	}

	public final Collection<EntityImp> groups() {
		final List<EntityImp> result = new ArrayList<>();
		for (Quark quark : getPlasma().quarks()) {
			if (quark.isRoot())
				continue;
			final EntityImp data = (EntityImp) quark.getData();
			if (data != null && data.isGroup())
				result.add(data);
		}
		// System.err.println("GROUPS=" + result.size());
		return Collections.unmodifiableCollection(result);

	}

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

	public ICucaDiagram getDiagram() {
		return namespaceSeparator;
	}

	public Plasma getPlasma() {
		return plasma;
	}
}
