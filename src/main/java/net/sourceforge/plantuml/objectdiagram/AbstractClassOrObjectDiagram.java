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
package net.sourceforge.plantuml.objectdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.abel.NoteLinkStrategy;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.utils.LineLocation;

public abstract class AbstractClassOrObjectDiagram extends AbstractEntityDiagram {
	// ::remove folder when __HAXE__

	public AbstractClassOrObjectDiagram(UmlSource source, UmlDiagramType type, Previous previous, PreprocessingArtifact preprocessingArtifact) {
		super(source, type, previous, preprocessingArtifact);
		setNamespaceSeparator(".");
	}

	final public boolean insertBetween(LineLocation location, Entity entity1, Entity entity2, Entity node) {
		final Link link = foundLink(entity1, entity2);
		if (link == null)
			return false;

		final Link l1 = new Link(location, this, getSkinParam().getCurrentStyleBuilder(), entity1, node, link.getType(),
				LinkArg.build(link.getLabel(), link.getLength(), getSkinParam().classAttributeIconSize() > 0)
						.withQuantifier(link.getQuantifier1(), null)
						.withDistanceAngle(link.getLabeldistance(), link.getLabelangle()));
		final Link l2 = new Link(location, this, getSkinParam().getCurrentStyleBuilder(), node, entity2, link.getType(),
				LinkArg.build(link.getLabel(), link.getLength(), getSkinParam().classAttributeIconSize() > 0)
						.withQuantifier(null, link.getQuantifier2())
						.withDistanceAngle(link.getLabeldistance(), link.getLabelangle()));
		addLink(l1);
		addLink(l2);
		removeLink(link);
		return true;
	}

	private Link foundLink(Entity entity1, Entity entity2) {
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link l = links.get(i);
			if (l.isBetween(entity1, entity2))
				return l;

		}
		return null;
	}

	public int getNbOfHozizontalLollipop(Entity entity) {
		if (entity.getLeafType() == LeafType.LOLLIPOP_FULL || entity.getLeafType() == LeafType.LOLLIPOP_HALF)
			throw new IllegalArgumentException();

		int result = 0;
		for (Link link : getLinks()) {
			if (link.getLength() == 1 && link.contains(entity)
					&& (link.containsType(LeafType.LOLLIPOP_FULL) || link.containsType(LeafType.LOLLIPOP_HALF)))
				result++;

		}
		return result;
	}

	private final List<Association> associations = new ArrayList<>();

	public CommandExecutionResult associationClass(LineLocation location, Entity entity1A, Entity entity1B, Entity entity2A, Entity entity2B,
			LinkType linkType, Display label) {

		final List<Association> same1 = getExistingAssociatedPoints(entity1A, entity1B);
		final List<Association> same2 = getExistingAssociatedPoints(entity2A, entity2B);
		if (same1.size() == 0 && same2.size() == 0) {
			final String tmp1 = this.getUniqueSequence("apoint");
			final String tmp2 = this.getUniqueSequence("apoint");

			final Quark<Entity> code1 = getCurrentGroup().getQuark().child(tmp1);
			final Entity point1 = reallyCreateLeaf(location, code1, Display.getWithNewlines(getPragma(), ""), LeafType.POINT_FOR_ASSOCIATION,
					null);
			final Quark<Entity> code2 = getCurrentGroup().getQuark().child(tmp2);
			final Entity point2 = reallyCreateLeaf(location, code2, Display.getWithNewlines(getPragma(), ""), LeafType.POINT_FOR_ASSOCIATION,
					null);

			insertPointBetween(location, entity1A, entity1B, point1);
			insertPointBetween(location, entity2A, entity2B, point2);

			final int length = 1;
			final Link point1ToPoint2 = new Link(location, this, getSkinParam().getCurrentStyleBuilder(), point1,
					point2, linkType, LinkArg.build(label, length));
			addLink(point1ToPoint2);

			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot link two associations points");
	}

	private void insertPointBetween(LineLocation location, final Entity entity1A, final Entity entity1B, final Entity point1) {
		Link existingLink1 = foundLink(entity1A, entity1B);
		if (existingLink1 == null)
			existingLink1 = new Link(location, this, getSkinParam().getCurrentStyleBuilder(), entity1A, entity1B,
					new LinkType(LinkDecor.NONE, LinkDecor.NONE), LinkArg.noDisplay(2));
		else
			removeLink(existingLink1);

		final Entity entity1real = existingLink1.isInverted() ? existingLink1.getEntity2() : existingLink1.getEntity1();
		final Entity entity2real = existingLink1.isInverted() ? existingLink1.getEntity1() : existingLink1.getEntity2();

		final Link entity1ToPoint = new Link(location, this, getSkinParam().getCurrentStyleBuilder(), entity1real,
				point1, existingLink1.getType().getPart2(), LinkArg.build(existingLink1.getLabel(), existingLink1.getLength())
						.withQuantifier(existingLink1.getQuantifier1(), null)
						.withDistanceAngle(existingLink1.getLabeldistance(), existingLink1.getLabelangle()));
		entity1ToPoint.setLinkArrow(existingLink1.getLinkArrow());
		final Link pointToEntity2 = new Link(location, this, getSkinParam().getCurrentStyleBuilder(), point1, entity2real,
				existingLink1.getType().getPart1(), LinkArg.noDisplay(existingLink1.getLength()).withQuantifier(null, existingLink1.getQuantifier2())
						.withDistanceAngle(existingLink1.getLabeldistance(), existingLink1.getLabelangle()));

		// int length = 1;
		// if (existingLink.getLength() == 1 && entity1A != entity1B) {
		// length = 2;
		// }
		// if (existingLink.getLength() == 2 && entity1A == entity1B) {
		// length = 2;
		// }

		addLink(entity1ToPoint);
		addLink(pointToEntity2);
	}

	public boolean associationClass(LineLocation location, int mode, Entity entity1, Entity entity2, Entity associed, LinkType linkType,
			Display label) {
		final List<Association> same = getExistingAssociatedPoints(entity1, entity2);
		if (same.size() > 1) {
			return false;
		} else if (same.size() == 0) {
			final Association association = new Association(location, mode, entity1, entity2, associed);
			association.createNew(location, mode, linkType, label);

			this.associations.add(association);
			return true;
		}
		assert same.size() == 1;
		final Association association = same.get(0).createSecondAssociation(location, mode, associed, label);
		association.createInSecond(location, linkType, label);

		this.associations.add(association);
		return true;
	}

	private List<Association> getExistingAssociatedPoints(final Entity entity1, final Entity entity2) {
		final List<Association> same = new ArrayList<>();
		for (Association existing : associations)
			if (existing.sameCouple(entity1, entity2))
				same.add(existing);

		return same;
	}

	class Association {
		private Entity entity1;
		private Entity entity2;
		private Entity associed;
		private Entity point;

		private Link existingLink;

		private Link entity1ToPoint;
		private Link pointToEntity2;
		private Link pointToAssocied;

		private Association other;

		public Association(LineLocation location, int mode, Entity entity1, Entity entity2, Entity associed) {
			this.entity1 = entity1;
			this.entity2 = entity2;
			this.associed = associed;
			final String idShort = AbstractClassOrObjectDiagram.this.getUniqueSequence("apoint");
			final Quark<Entity> quark;
			if (entity1.getQuark().getParent() == entity2.getQuark().getParent())
				quark = entity1.getQuark().getParent().child(idShort);
			else
				quark = quarkInContext(true, cleanId(idShort));
			point = reallyCreateLeaf(location, quark, Display.getWithNewlines(getPragma(), ""), LeafType.POINT_FOR_ASSOCIATION, null);

		}

		public Association createSecondAssociation(LineLocation location, int mode2, Entity associed2, Display label) {
			final Association result = new Association(location, mode2, entity1, entity2, associed2);
			result.existingLink = this.existingLink;
			result.other = this;

			if (this.existingLink.getLength() == 1) {
				this.entity1ToPoint.setLength(2);
				this.pointToEntity2.setLength(2);
				this.pointToAssocied.setLength(1);
			}
			return result;
		}

		void createNew(LineLocation location, int mode, LinkType linkType, Display label) {
			existingLink = foundLink(entity1, entity2);
			if (existingLink == null)
				existingLink = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
						entity1, entity2, new LinkType(LinkDecor.NONE, LinkDecor.NONE),
						LinkArg.noDisplay(2));
			else
				removeLink(existingLink);

			final Entity entity1real = existingLink.isInverted() ? existingLink.getEntity2()
					: existingLink.getEntity1();
			final Entity entity2real = existingLink.isInverted() ? existingLink.getEntity1()
					: existingLink.getEntity2();

			entity1ToPoint = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
					entity1real, point, existingLink.getType().getPart2(), LinkArg.build(existingLink.getLabel(), existingLink.getLength())
							.withQuantifier(existingLink.getQuantifier1(), null)
							.withDistanceAngle(existingLink.getLabeldistance(), existingLink.getLabelangle()));
			entity1ToPoint.setLinkArrow(existingLink.getLinkArrow());
			pointToEntity2 = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
					point, entity2real, existingLink.getType().getPart1(), LinkArg.noDisplay(existingLink.getLength()).withQuantifier(null, existingLink.getQuantifier2())
							.withDistanceAngle(existingLink.getLabeldistance(), existingLink.getLabelangle()));

			int length = 1;
			if (existingLink.getLength() == 1 && entity1 != entity2)
				length = 2;

			if (existingLink.getLength() == 2 && entity1 == entity2)
				length = 2;

			if (length == 1) {
				entity1ToPoint.addNoteFrom(existingLink, NoteLinkStrategy.NORMAL);
			} else {
				entity1ToPoint.addNoteFrom(existingLink, NoteLinkStrategy.HALF_PRINTED_FULL);
				pointToEntity2.addNoteFrom(existingLink, NoteLinkStrategy.HALF_NOT_PRINTED);
			}
			addLink(entity1ToPoint);
			addLink(pointToEntity2);

			if (mode == 1)
				pointToAssocied = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
						point, associed, linkType, LinkArg.build(label, length));
			else
				pointToAssocied = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
						associed, point, linkType, LinkArg.build(label, length));

			addLink(pointToAssocied);
		}

		void createInSecond(LineLocation location, LinkType linkType, Display label) {
			existingLink = foundLink(entity1, entity2);
			if (existingLink == null)
				existingLink = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
						entity1, entity2, new LinkType(LinkDecor.NONE, LinkDecor.NONE),
						LinkArg.noDisplay(2));
			else
				removeLink(existingLink);

			entity1ToPoint = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
					entity1, point, existingLink.getType().getPart2(), LinkArg.build(existingLink.getLabel(), 2).withQuantifier(existingLink.getQuantifier1(), null)
							.withDistanceAngle(existingLink.getLabeldistance(), existingLink.getLabelangle()));
			pointToEntity2 = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
					point, entity2, existingLink.getType().getPart1(), LinkArg.noDisplay(2).withQuantifier(null, existingLink.getQuantifier2())
							.withDistanceAngle(existingLink.getLabeldistance(), existingLink.getLabelangle()));
			// entity1ToPoint = new Link(entity1, point, existingLink.getType(),
			// null, 2);
			// pointToEntity2 = new Link(point, entity2, existingLink.getType(),
			// null, 2);
			addLink(entity1ToPoint);
			addLink(pointToEntity2);
			if (other.pointToAssocied.getEntity1().getLeafType() == LeafType.POINT_FOR_ASSOCIATION) {
				removeLink(other.pointToAssocied);
				other.pointToAssocied = other.pointToAssocied.getInv();
				addLink(other.pointToAssocied);
			}
			pointToAssocied = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
					point, associed, linkType, LinkArg.build(label, 1));
			addLink(pointToAssocied);

			final Link lnode = new Link(location, AbstractClassOrObjectDiagram.this, getSkinParam().getCurrentStyleBuilder(),
					other.point, this.point, new LinkType(LinkDecor.NONE, LinkDecor.NONE),
					LinkArg.noDisplay(1));
			lnode.setInvis(true);
			addLink(lnode);

		}

		boolean sameCouple(Entity entity1, Entity entity2) {
			if (this.entity1 == entity1 && this.entity2 == entity2)
				return true;

			if (this.entity1 == entity2 && this.entity2 == entity1)
				return true;

			return false;
		}
	}

	@Override
	public void setLegend(DisplayPositioned legend) {

		final Entity currentGroup = this.getCurrentGroup();

		if (currentGroup.isRoot()) {
			super.setLegend(legend);
			return;
		}

		currentGroup.setLegend(legend);
	}

}
