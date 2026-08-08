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
 */
package net.sourceforge.plantuml.cheneer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.LineLocation;

public class ChenEerDiagram extends AbstractEntityDiagram {

	public ChenEerDiagram(UmlSource source, Previous previous, PreprocessingArtifact preprocessingArtifact) {
		super(source, DiagramType.CHEN_EER, previous, preprocessingArtifact);
	}

	private static final String RELATIONSHIP_ATTRIBUTE_BOX = "/__plantuml_compact_relationship_attributes__";

	private final Stack<OwnerFrame> ownerStack = new Stack<>();
	private final Map<Entity, List<CompactChenAttribute>> compactAttributes = new IdentityHashMap<>();
	private final Map<Entity, Entity> relationshipBoxes = new IdentityHashMap<>();
	private final Set<String> compactAttributeIds = new LinkedHashSet<>();
	private boolean compactNotation;

	private static final class OwnerFrame {
		private final Entity owner;
		private final Entity rootOwner;
		private final String attributePath;
		private final int depth;

		private OwnerFrame(Entity owner, Entity rootOwner, String attributePath, int depth) {
			this.owner = owner;
			this.rootOwner = rootOwner;
			this.attributePath = attributePath;
			this.depth = depth;
		}
	}

	/**
	 * Pushes the owner of the following attributes.
	 *
	 * @see #peekOwner()
	 * @param group the entity that owns the following attributes
	 */
	public void pushOwner(Entity group) {
		ownerStack.push(new OwnerFrame(group, group, "", 0));
	}

	void pushCompactAttribute(String identity) {
		final OwnerFrame parent = ownerStack.peek();
		final String path = parent.attributePath.length() == 0 ? identity : parent.attributePath + "/" + identity;
		ownerStack.push(new OwnerFrame(parent.rootOwner, parent.rootOwner, path, parent.depth + 1));
	}

	/**
	 * Pops an attribute owner from the stack. See also {@link #peekOwner()}.
	 *
	 * @see #peekOwner()
	 * @return true if an owner was popped, false if the stack was empty
	 */
	public boolean popOwner() {
		if (ownerStack.isEmpty()) {
			return false;
		}
		ownerStack.pop();
		return true;
	}

	/**
	 * Returns the owner of the current attribute.
	 *
	 * <p>
	 * This is used to link attributes based on their lexical position (how they
	 * appear in sources) without nesting the entities (like how packages are done).
	 * It is for this reason that we can't use CucaDiagram.getCurrentGroup, as that
	 * method nests the entities.
	 *
	 * @return the owner of the current attribute, or null if there is no owner
	 */
	public Entity peekOwner() {
		if (ownerStack.isEmpty()) {
			return null;
		}
		return ownerStack.peek().owner;
	}

	boolean isCompactNotation() {
		return compactNotation;
	}

	boolean useCompactNotation() {
		if (leafs().isEmpty() == false)
			return false;

		compactNotation = true;
		return true;
	}

	boolean addCompactAttribute(LineLocation location, String identity, String displayName, String domain,
			Stereotype stereotype, boolean composite) {
		final OwnerFrame frame = ownerStack.peek();
		final String path = frame.attributePath.length() == 0 ? identity : frame.attributePath + "/" + identity;
		final String qualifiedIdentity = frame.rootOwner.getName() + "/" + path;
		if (compactAttributeIds.add(qualifiedIdentity) == false)
			return false;

		final CompactChenAttribute row = new CompactChenAttribute(qualifiedIdentity, displayName, domain, frame.depth,
				stereotype);
		final Entity rowOwner;
		if (frame.rootOwner.getLeafType() == LeafType.CHEN_RELATIONSHIP)
			rowOwner = getOrCreateRelationshipBox(location, frame.rootOwner);
		else
			rowOwner = frame.rootOwner;

		compactAttributes.computeIfAbsent(rowOwner, key -> new ArrayList<>()).add(row);
		if (composite)
			pushCompactAttribute(identity);

		return true;
	}

	private Entity getOrCreateRelationshipBox(LineLocation location, Entity relationship) {
		Entity box = relationshipBoxes.get(relationship);
		if (box != null)
			return box;

		final String id = relationship.getName() + RELATIONSHIP_ATTRIBUTE_BOX;
		final Quark<Entity> quark = quarkInContext(true, id);
		box = reallyCreateLeaf(location, quark, Display.empty(), LeafType.CHEN_ATTRIBUTE, null);
		box.setColors(relationship.getColors());
		box.setStereotype(relationship.getStereotype());
		relationshipBoxes.put(relationship, box);

		final LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE).goDashed();
		final Link link = new Link(location, this, getCurrentStyleBuilder(), box, relationship, linkType,
				LinkArg.build(Display.NULL, 2));
		link.setColors(relationship.getColors());
		addLink(link);
		return box;
	}

	List<CompactChenAttribute> getCompactAttributes(Entity owner) {
		final List<CompactChenAttribute> rows = compactAttributes.get(owner);
		if (rows == null)
			return Collections.emptyList();

		return Collections.unmodifiableList(rows);
	}

	@Override
	public void makeDiagramReady() {
		super.makeDiagramReady();
		if (compactNotation == false)
			return;

		for (Entity entity : leafs())
			if (entity.getLeafType() == LeafType.CHEN_RELATIONSHIP)
				entity.setSvekImage(new EntityImageChenCompactRelationship(entity));

		for (Entity owner : compactAttributes.keySet()) {
			if (owner.getLeafType() == LeafType.CHEN_ENTITY)
				owner.setSvekImage(new EntityImageChenCompactEntity(owner));
			else
				owner.setSvekImage(new EntityImageChenRelationshipAttribute(owner));
		}
	}

}
