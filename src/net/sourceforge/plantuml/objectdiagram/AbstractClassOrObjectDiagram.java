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
package net.sourceforge.plantuml.objectdiagram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.baraye.Quark;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositioned;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkArg;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.NoteLinkStrategy;

public abstract class AbstractClassOrObjectDiagram extends AbstractEntityDiagram {

	public AbstractClassOrObjectDiagram(UmlSource source, UmlDiagramType type, Map<String, String> orig) {
		super(source, type, orig);
	}

	final public boolean insertBetween(EntityImp entity1, EntityImp entity2, EntityImp node) {
		final Link link = foundLink(entity1, entity2);
		if (link == null)
			return false;

		final Link l1 = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), entity1, node,
				link.getType(),
				LinkArg.build(link.getLabel(), link.getLength(), getSkinParam().classAttributeIconSize() > 0)
						.withQuantifier(link.getQuantifier1(), null)
						.withDistanceAngle(link.getLabeldistance(), link.getLabelangle()));
		final Link l2 = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), node, entity2,
				link.getType(),
				LinkArg.build(link.getLabel(), link.getLength(), getSkinParam().classAttributeIconSize() > 0)
						.withQuantifier(null, link.getQuantifier2())
						.withDistanceAngle(link.getLabeldistance(), link.getLabelangle()));
		addLink(l1);
		addLink(l2);
		removeLink(link);
		return true;
	}

	private Link foundLink(EntityImp entity1, EntityImp entity2) {
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link l = links.get(i);
			if (l.isBetween(entity1, entity2))
				return l;

		}
		return null;
	}

	public int getNbOfHozizontalLollipop(EntityImp entity) {
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

	public CommandExecutionResult associationClass(EntityImp entity1A, EntityImp entity1B, EntityImp entity2A,
			EntityImp entity2B, LinkType linkType, Display label) {
//		final Quark ident1A = buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(name1A));
//		final Quark ident1B = buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(name1B));
//		final Quark ident2A = buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(name2A));
//		final Quark ident2B = buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(name2B));
//		final Quark code1A = buildFromFullPath(name1A);
//		final Quark code1B = buildFromFullPath(name1B);
//		final Quark code2A = buildFromFullPath(name2A);
//		final Quark code2B = buildFromFullPath(name2B);
//		final IEntity entity1A = getOrCreateLeaf(ident1A, code1A, null, null);
//		final IEntity entity1B = getOrCreateLeaf(ident1B, code1B, null, null);
//		final IEntity entity2A = getOrCreateLeaf(ident2A, code2A, null, null);
//		final IEntity entity2B = getOrCreateLeaf(ident2B, code2B, null, null);
		final List<Association> same1 = getExistingAssociatedPoints(entity1A, entity1B);
		final List<Association> same2 = getExistingAssociatedPoints(entity2A, entity2B);
		if (same1.size() == 0 && same2.size() == 0) {
			final String tmp1 = this.getUniqueSequence("apoint");
			final String tmp2 = this.getUniqueSequence("apoint");
//			final Quark ident1 = buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(tmp1));
//			final Quark ident2 = buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(tmp2));
//			final Quark code1 = buildFromFullPath(tmp1);
//			final Quark code2 = buildFromFullPath(tmp2);
//			final IEntity point1 = getOrCreateLeaf(ident1, code1, LeafType.POINT_FOR_ASSOCIATION, null);
//			final IEntity point2 = getOrCreateLeaf(ident2, code2, LeafType.POINT_FOR_ASSOCIATION, null);

			final Quark code1 = currentQuark().child(tmp1);
			final EntityImp point1 = reallyCreateLeaf(code1, Display.getWithNewlines(""), LeafType.POINT_FOR_ASSOCIATION,
					null);
			final Quark code2 = currentQuark().child(tmp2);
			final EntityImp point2 = reallyCreateLeaf(code2, Display.getWithNewlines(""), LeafType.POINT_FOR_ASSOCIATION,
					null);

			insertPointBetween(entity1A, entity1B, point1);
			insertPointBetween(entity2A, entity2B, point2);

			final int length = 1;
			final Link point1ToPoint2 = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), point1,
					point2, linkType, LinkArg.build(label, length));
			addLink(point1ToPoint2);

			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot link two associations points");
	}

	private void insertPointBetween(final EntityImp entity1A, final EntityImp entity1B, final EntityImp point1) {
		Link existingLink1 = foundLink(entity1A, entity1B);
		if (existingLink1 == null)
			existingLink1 = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), entity1A, entity1B,
					new LinkType(LinkDecor.NONE, LinkDecor.NONE), LinkArg.noDisplay(2));
		else
			removeLink(existingLink1);

		final EntityImp entity1real = existingLink1.isInverted() ? existingLink1.getEntity2()
				: existingLink1.getEntity1();
		final EntityImp entity2real = existingLink1.isInverted() ? existingLink1.getEntity1()
				: existingLink1.getEntity2();

		final Link entity1ToPoint = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), entity1real,
				point1, existingLink1.getType().getPart2(),
				LinkArg.build(existingLink1.getLabel(), existingLink1.getLength())
						.withQuantifier(existingLink1.getQuantifier1(), null)
						.withDistanceAngle(existingLink1.getLabeldistance(), existingLink1.getLabelangle()));
		entity1ToPoint.setLinkArrow(existingLink1.getLinkArrow());
		final Link pointToEntity2 = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), point1,
				entity2real, existingLink1.getType().getPart1(),
				LinkArg.noDisplay(existingLink1.getLength()).withQuantifier(null, existingLink1.getQuantifier2())
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

	public boolean associationClass(int mode, EntityImp entity1, EntityImp entity2, EntityImp associed, LinkType linkType,
			Display label) {
//		final Quark ident1 = buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(name1));
//		final Quark ident2 = buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(name2));
//		final Quark code1 = buildFromFullPath(name1);
//		final Quark code2 = buildFromFullPath(name2);
//		final IEntity entity1 = getOrCreateLeaf(ident1, code1, null, null);
//		final IEntity entity2 = getOrCreateLeaf(ident2, code2, null, null);
		final List<Association> same = getExistingAssociatedPoints(entity1, entity2);
		if (same.size() > 1) {
			return false;
		} else if (same.size() == 0) {
			final Association association = new Association(mode, entity1, entity2, associed);
			association.createNew(mode, linkType, label);

			this.associations.add(association);
			return true;
		}
		assert same.size() == 1;
		final Association association = same.get(0).createSecondAssociation(mode, associed, label);
		association.createInSecond(linkType, label);

		this.associations.add(association);
		return true;
	}

	private List<Association> getExistingAssociatedPoints(final EntityImp entity1, final EntityImp entity2) {
		final List<Association> same = new ArrayList<>();
		for (Association existing : associations)
			if (existing.sameCouple(entity1, entity2))
				same.add(existing);

		return same;
	}

	class Association {
		private EntityImp entity1;
		private EntityImp entity2;
		private EntityImp associed;
		private EntityImp point;

		private Link existingLink;

		private Link entity1ToPoint;
		private Link pointToEntity2;
		private Link pointToAssocied;

		private Association other;

		public Association(int mode, EntityImp entity1, EntityImp entity2, EntityImp associed) {
			this.entity1 = entity1;
			this.entity2 = entity2;
			this.associed = associed;
			final String idShort = AbstractClassOrObjectDiagram.this.getUniqueSequence("apoint");
			final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
			point = reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.POINT_FOR_ASSOCIATION, null);

//			final Quark ident = buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(idShort));
//			final Quark code = buildFromFullPath(idShort);
//			point = getOrCreateLeaf(ident, code, LeafType.POINT_FOR_ASSOCIATION, null);

		}

		public Association createSecondAssociation(int mode2, EntityImp associed2, Display label) {
			final Association result = new Association(mode2, entity1, entity2, associed2);
			result.existingLink = this.existingLink;
			result.other = this;

			if (this.existingLink.getLength() == 1) {
				this.entity1ToPoint.setLength(2);
				this.pointToEntity2.setLength(2);
				this.pointToAssocied.setLength(1);
			}
			return result;
		}

		void createNew(int mode, LinkType linkType, Display label) {
			existingLink = foundLink(entity1, entity2);
			if (existingLink == null)
				existingLink = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), entity1, entity2,
						new LinkType(LinkDecor.NONE, LinkDecor.NONE), LinkArg.noDisplay(2));
			else
				removeLink(existingLink);

			final EntityImp entity1real = existingLink.isInverted() ? existingLink.getEntity2()
					: existingLink.getEntity1();
			final EntityImp entity2real = existingLink.isInverted() ? existingLink.getEntity1()
					: existingLink.getEntity2();

			entity1ToPoint = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), entity1real, point,
					existingLink.getType().getPart2(),
					LinkArg.build(existingLink.getLabel(), existingLink.getLength())
							.withQuantifier(existingLink.getQuantifier1(), null)
							.withDistanceAngle(existingLink.getLabeldistance(), existingLink.getLabelangle()));
			entity1ToPoint.setLinkArrow(existingLink.getLinkArrow());
			pointToEntity2 = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), point, entity2real,
					existingLink.getType().getPart1(),
					LinkArg.noDisplay(existingLink.getLength()).withQuantifier(null, existingLink.getQuantifier2())
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
				pointToAssocied = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), point, associed,
						linkType, LinkArg.build(label, length));
			else
				pointToAssocied = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), associed, point,
						linkType, LinkArg.build(label, length));

			addLink(pointToAssocied);
		}

		void createInSecond(LinkType linkType, Display label) {
			existingLink = foundLink(entity1, entity2);
			if (existingLink == null)
				existingLink = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), entity1, entity2,
						new LinkType(LinkDecor.NONE, LinkDecor.NONE), LinkArg.noDisplay(2));
			else
				removeLink(existingLink);

			entity1ToPoint = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), entity1, point,
					existingLink.getType().getPart2(),
					LinkArg.build(existingLink.getLabel(), 2).withQuantifier(existingLink.getQuantifier1(), null)
							.withDistanceAngle(existingLink.getLabeldistance(), existingLink.getLabelangle()));
			pointToEntity2 = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), point, entity2,
					existingLink.getType().getPart1(),
					LinkArg.noDisplay(2).withQuantifier(null, existingLink.getQuantifier2())
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
			pointToAssocied = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), point, associed,
					linkType, LinkArg.build(label, 1));
			addLink(pointToAssocied);

			final Link lnode = new Link(getEntityFactory(), getSkinParam().getCurrentStyleBuilder(), other.point,
					this.point, new LinkType(LinkDecor.NONE, LinkDecor.NONE), LinkArg.noDisplay(1));
			lnode.setInvis(true);
			addLink(lnode);

		}

		boolean sameCouple(EntityImp entity1, EntityImp entity2) {
			if (this.entity1 == entity1 && this.entity2 == entity2)
				return true;

			if (this.entity1 == entity2 && this.entity2 == entity1)
				return true;

			return false;
		}
	}

	@Override
	public void setLegend(DisplayPositioned legend) {

		final EntityImp currentGroup = this.getCurrentGroup();

		if (currentGroup.instanceofGroupRoot()) {
			super.setLegend(legend);
			return;
		}

		currentGroup.setLegend(legend);
	}

}
