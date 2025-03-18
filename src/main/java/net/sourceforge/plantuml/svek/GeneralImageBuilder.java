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
 * Contribution :  Hisashi Miyashita
 * Contribution :  Serge Wenger
 * 
 *
 */
package net.sourceforge.plantuml.svek;

import java.util.Collection;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityPosition;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.decoration.symbol.USymbolInterface;
import net.sourceforge.plantuml.descdiagram.EntityImageDesignedDomain;
import net.sourceforge.plantuml.descdiagram.EntityImageDomain;
import net.sourceforge.plantuml.descdiagram.EntityImageMachine;
import net.sourceforge.plantuml.descdiagram.EntityImageRequirement;
import net.sourceforge.plantuml.dot.GraphvizVersion;
import net.sourceforge.plantuml.dot.Neighborhood;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.svek.image.EntityImageActivity;
import net.sourceforge.plantuml.svek.image.EntityImageArcCircle;
import net.sourceforge.plantuml.svek.image.EntityImageAssociation;
import net.sourceforge.plantuml.svek.image.EntityImageAssociationPoint;
import net.sourceforge.plantuml.svek.image.EntityImageBranch;
import net.sourceforge.plantuml.svek.image.EntityImageChenAttribute;
import net.sourceforge.plantuml.svek.image.EntityImageChenCircle;
import net.sourceforge.plantuml.svek.image.EntityImageChenEntity;
import net.sourceforge.plantuml.svek.image.EntityImageChenRelationship;
import net.sourceforge.plantuml.svek.image.EntityImageCircleEnd;
import net.sourceforge.plantuml.svek.image.EntityImageCircleStart;
import net.sourceforge.plantuml.svek.image.EntityImageClass;
import net.sourceforge.plantuml.svek.image.EntityImageDeepHistory;
import net.sourceforge.plantuml.svek.image.EntityImageDescription;
import net.sourceforge.plantuml.svek.image.EntityImageEmptyPackage;
import net.sourceforge.plantuml.svek.image.EntityImageGroup;
import net.sourceforge.plantuml.svek.image.EntityImageJson;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterface;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterfaceEye1;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterfaceEye2;
import net.sourceforge.plantuml.svek.image.EntityImageMap;
import net.sourceforge.plantuml.svek.image.EntityImageNote;
import net.sourceforge.plantuml.svek.image.EntityImageObject;
import net.sourceforge.plantuml.svek.image.EntityImagePort;
import net.sourceforge.plantuml.svek.image.EntityImagePseudoState;
import net.sourceforge.plantuml.svek.image.EntityImageState;
import net.sourceforge.plantuml.svek.image.EntityImageState2;
import net.sourceforge.plantuml.svek.image.EntityImageStateBorder;
import net.sourceforge.plantuml.svek.image.EntityImageStateEmptyDescription;
import net.sourceforge.plantuml.svek.image.EntityImageSynchroBar;
import net.sourceforge.plantuml.svek.image.EntityImageTips;
import net.sourceforge.plantuml.text.Guillemet;

public final class GeneralImageBuilder {

	public static IEntityImage createEntityImageBlock(Entity leaf, boolean isHideEmptyDescriptionForState,
			PortionShower portionShower, Bibliotekon bibliotekon, GraphvizVersion graphvizVersion,
			Collection<Link> links) {
		final IEntityImage result = createEntityImageBlockInternal(leaf, isHideEmptyDescriptionForState, portionShower,
				bibliotekon, graphvizVersion, links);
		// System.err.println("leaf " + leaf + " " + result.getClass());
		return result;
	}

	private static IEntityImage createEntityImageBlockInternal(Entity leaf, boolean isHideEmptyDescriptionForState,
			PortionShower portionShower, Bibliotekon bibliotekon, GraphvizVersion graphvizVersion,
			Collection<Link> links) {
		if (leaf.isRemoved())
			throw new IllegalStateException();

		if (leaf.getLeafType().isLikeClass()) {
			final EntityImageClass entityImageClass = new EntityImageClass(leaf, portionShower);
			final Neighborhood neighborhood = leaf.getNeighborhood();
			if (neighborhood != null)
				return new EntityImageProtected(entityImageClass, 20, neighborhood, bibliotekon);

			return entityImageClass;
		}
		if (leaf.getLeafType() == LeafType.NOTE)
			return new EntityImageNote(leaf);

		if (leaf.getLeafType() == LeafType.ACTIVITY)
			return new EntityImageActivity(leaf, bibliotekon);

		if (/* (leaf.getLeafType() == LeafType.PORT) || */leaf.getLeafType() == LeafType.PORTIN
				|| leaf.getLeafType() == LeafType.PORTOUT) {
			final Cluster parent = bibliotekon.getCluster(leaf.getParentContainer());
			return new EntityImagePort(leaf, parent, bibliotekon);
		}

		if (leaf.getLeafType() == LeafType.STATE) {
			if (leaf.getEntityPosition() != EntityPosition.NORMAL) {
				final Cluster stateParent = bibliotekon.getCluster(leaf.getParentContainer());
				return new EntityImageStateBorder(leaf, stateParent, bibliotekon);
			}
			if (isHideEmptyDescriptionForState && leaf.getBodier().getRawBody().size() == 0)
				return new EntityImageStateEmptyDescription(leaf);

			if (leaf.getStereotype() != null
					&& "<<sdlreceive>>".equals(leaf.getStereotype().getLabel(Guillemet.DOUBLE_COMPARATOR)))
				return new EntityImageState2(leaf);

			return new EntityImageState(leaf);

		}
		if (leaf.getLeafType() == LeafType.CIRCLE_START)
			return new EntityImageCircleStart(leaf);

		if (leaf.getLeafType() == LeafType.CIRCLE_END)
			return new EntityImageCircleEnd(leaf);

		if (leaf.getLeafType() == LeafType.BRANCH || leaf.getLeafType() == LeafType.STATE_CHOICE)
			return new EntityImageBranch(leaf);

		if (leaf.getLeafType() == LeafType.LOLLIPOP_FULL || leaf.getLeafType() == LeafType.LOLLIPOP_HALF)
			return new EntityImageLollipopInterface(leaf);

		if (leaf.getLeafType() == LeafType.CIRCLE)
			return new EntityImageDescription(leaf, portionShower, links, bibliotekon);

		if (leaf.getLeafType() == LeafType.DESCRIPTION) {
			if (OptionFlags.USE_INTERFACE_EYE1 && leaf.getUSymbol() instanceof USymbolInterface) {
				return new EntityImageLollipopInterfaceEye1(leaf, bibliotekon);
			} else if (OptionFlags.USE_INTERFACE_EYE2 && leaf.getUSymbol() instanceof USymbolInterface) {
				return new EntityImageLollipopInterfaceEye2(leaf, portionShower);
			} else {
				return new EntityImageDescription(leaf, portionShower, links, bibliotekon);
			}
		}
		if (leaf.getLeafType() == LeafType.USECASE)
			return new EntityImageDescription(leaf, portionShower, links, bibliotekon);
			// return new EntityImageUseCase(leaf, portionShower);

		if (leaf.getLeafType() == LeafType.USECASE_BUSINESS)
			return new EntityImageDescription(leaf, portionShower, links, bibliotekon);
			// return new EntityImageUseCase(leaf, portionShower);

		// if (leaf.getEntityType() == LeafType.CIRCLE_INTERFACE) {
		// return new EntityImageCircleInterface(leaf, skinParam);
		// }
		if (leaf.getLeafType() == LeafType.OBJECT)
			return new EntityImageObject(leaf, portionShower);

		if (leaf.getLeafType() == LeafType.MAP)
			return new EntityImageMap(leaf, portionShower);

		if (leaf.getLeafType() == LeafType.JSON)
			return new EntityImageJson(leaf, portionShower);

		if (leaf.getLeafType() == LeafType.SYNCHRO_BAR || leaf.getLeafType() == LeafType.STATE_FORK_JOIN)
			return new EntityImageSynchroBar(leaf);

		if (leaf.getLeafType() == LeafType.ARC_CIRCLE)
			return new EntityImageArcCircle(leaf);

		if (leaf.getLeafType() == LeafType.POINT_FOR_ASSOCIATION)
			return new EntityImageAssociationPoint(leaf);

		if (leaf.isGroup())
			return new EntityImageGroup(leaf);

		if (leaf.getLeafType() == LeafType.EMPTY_PACKAGE) {
			if (leaf.getUSymbol() != null)
				return new EntityImageDescription(leaf, portionShower, links, bibliotekon);

			return new EntityImageEmptyPackage(leaf, portionShower);
		}
		if (leaf.getLeafType() == LeafType.ASSOCIATION)
			return new EntityImageAssociation(leaf);

		if (leaf.getLeafType() == LeafType.PSEUDO_STATE)
			return new EntityImagePseudoState(leaf);

		if (leaf.getLeafType() == LeafType.DEEP_HISTORY)
			return new EntityImageDeepHistory(leaf);

		if (leaf.getLeafType() == LeafType.TIPS)
			return new EntityImageTips(leaf, bibliotekon);

		if (leaf.getLeafType() == LeafType.CHEN_ENTITY)
			return new EntityImageChenEntity(leaf);

		if (leaf.getLeafType() == LeafType.CHEN_RELATIONSHIP)
			return new EntityImageChenRelationship(leaf);

		if (leaf.getLeafType() == LeafType.CHEN_ATTRIBUTE)
			return new EntityImageChenAttribute(leaf);

		if (leaf.getLeafType() == LeafType.CHEN_CIRCLE)
			return new EntityImageChenCircle(leaf);

		// TODO Clean
		if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isMachineOrSpecification())
			return new EntityImageMachine(leaf);
		else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isDesignedOrSolved())
			return new EntityImageDesignedDomain(leaf);
		else if (leaf.getLeafType() == LeafType.REQUIREMENT)
			return new EntityImageRequirement(leaf);
		else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isLexicalOrGiven())
			return new EntityImageDomain(leaf, 'X');
		else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isCausal())
			return new EntityImageDomain(leaf, 'C');
		else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isBiddableOrUncertain())
			return new EntityImageDomain(leaf, 'B');
		else if (leaf.getLeafType() == LeafType.DOMAIN)
			return new EntityImageDomain(leaf, 'P');
		else
			throw new UnsupportedOperationException(leaf.getLeafType().toString());
	}

	public static UStroke getForcedStroke(Stereotype stereotype, ISkinParam skinParam) {
		UStroke stroke = skinParam.getThickness(LineParam.packageBorder, stereotype);
		if (stroke == null)
			stroke = UStroke.withThickness(1.5);

		return stroke;
	}

}
