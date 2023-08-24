/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.abel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.BodierJSon;
import net.sourceforge.plantuml.cucadiagram.BodierMap;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.cucadiagram.HideOrShow;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.plasma.Plasma;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.stereo.Stereotype;

public final class EntityFactory implements IEntityFactory {

	private final List<Link> links = new ArrayList<>();

	private int rawLayout;

	private final Plasma<Entity> namespace;
	private final Quark<Entity> root;

	private final Entity rootGroup;

	private final List<HideOrShow> hides2;
	private final List<HideOrShow> removed;
	final private ICucaDiagram diagram;

	//
	public EntityFactory(List<HideOrShow> hides2, List<HideOrShow> removed, ICucaDiagram diagram) {
		this.hides2 = hides2;
		this.removed = removed;
		this.diagram = diagram;
		this.namespace = new Plasma<Entity>();
		this.root = namespace.root();
		this.rootGroup = new Entity(this.root, this, null, GroupType.ROOT, 0);
	}

	public boolean isHidden(Entity leaf) {
		final Entity other = isNoteWithSingleLinkAttachedTo(leaf);
		if (other != null && other != leaf)
			return isHidden(other);

		boolean hidden = false;
		for (HideOrShow hide : hides2)
			hidden = hide.apply(hidden, leaf);

		return hidden;
	}

	public boolean isRemoved(Stereotype stereotype) {
		boolean result = false;
		for (HideOrShow hide : removed)
			result = hide.apply(result, stereotype);

		return result;
	}

	public boolean isRemoved(Entity leaf) {
		final Entity other = isNoteWithSingleLinkAttachedTo(leaf);
		if (other instanceof Entity)
			return isRemoved((Entity) other);

		boolean result = false;
		for (HideOrShow hide : removed)
			result = hide.apply(result, leaf);

		return result;
	}

	private Entity isNoteWithSingleLinkAttachedTo(Entity note) {
		if (note.getLeafType() != LeafType.NOTE)
			return null;
		assert note.getLeafType() == LeafType.NOTE;
		Entity other = null;
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

	public boolean isRemovedIgnoreUnlinked(Entity leaf) {
		boolean result = false;
		for (HideOrShow hide : removed)
			if (hide.isAboutUnlinked() == false)
				result = hide.apply(result, leaf);

		return result;
	}

	final public Entity createLeaf(Quark<Entity> quark, LeafType entityType, Set<VisibilityModifier> hides) {
		final Bodier bodier;
		if (Objects.requireNonNull(entityType) == LeafType.MAP)
			bodier = new BodierMap();
		else if (Objects.requireNonNull(entityType) == LeafType.JSON)
			bodier = new BodierJSon();
		else
			bodier = BodyFactory.createLeaf(entityType, hides);

		final Entity result = new Entity(quark, this, bodier, entityType, rawLayout);
		bodier.setLeaf(result);
		return result;
	}

	public Entity createGroup(Quark<Entity> quark, GroupType groupType, Set<VisibilityModifier> hides) {
		Objects.requireNonNull(groupType);
		if (quark.getData() != null)
			return quark.getData();

		final Bodier bodier = BodyFactory.createGroup(hides);
		final Entity result = new Entity(quark, this, bodier, groupType, rawLayout);

		return result;
	}

	public Entity getRootGroup() {
		return rootGroup;
	}

	public final Collection<Entity> leafs() {

		final List<Entity> result = new ArrayList<>();
		for (Quark<Entity> quark : quarks()) {
			if (quark.isRoot())
				continue;
			final Entity data = quark.getData();
			if (data != null && data.isGroup() == false)
				result.add(data);
		}
		return Collections.unmodifiableCollection(result);

	}

	public final Collection<Entity> groups() {
		final List<Entity> result = new ArrayList<>();
		for (Quark<Entity> quark : quarks()) {
			if (quark.isRoot())
				continue;

			final Entity data = quark.getData();
			if (data != null && data.isGroup())
				result.add(data);
		}
		return Collections.unmodifiableCollection(result);
	}

	public final Collection<Entity> groupsAndRoot() {
		final List<Entity> result = new ArrayList<>();
		for (Quark<Entity> quark : quarks()) {
			final Entity data = quark.getData();
			if (data != null && data.isGroup())
				result.add(data);
		}
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
		return diagram;
	}

	// ----------

	public Collection<Quark<Entity>> quarks() {
		final List<Quark<Entity>> result = new ArrayList<>();
		for (Quark<Entity> quark : namespace.quarks())
			result.add(quark);

		return result;
	}

	public Quark<Entity> root() {
		return root;
	}

	public void setSeparator(String namespaceSeparator) {
		namespace.setSeparator(namespaceSeparator);
		// printspace.setSeparator(namespaceSeparator);
	}

	public Quark<Entity> firstWithName(String full) {
		final Quark<Entity> tmp = namespace.firstWithName(full);
		return tmp;
	}

	public int countByName(String full) {
		return namespace.countByName(full);
	}

}
