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
package net.sourceforge.plantuml.cucadiagram;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Hideable;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.Removeable;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.USymbolInterface;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.ugraphic.UComment;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class Link extends WithLinkType implements Hideable, Removeable {

	public final StyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

	final private IEntity cl1;
	final private IEntity cl2;

	private String port1;
	private String port2;

	final private Display label;

	private int length;
	final private String qualifier1;
	final private String qualifier2;
	final private String uid = "LNK" + UniqueSequence.getValue();

	private Display note;
	private Position notePosition;
	private Colors noteColors;
	private NoteLinkStrategy noteLinkStrategy;

	private boolean invis = false;
	private double weight = 1.0;

	private final String labeldistance;
	private final String labelangle;

	private boolean constraint = true;
	private boolean inverted = false;
	private LinkArrow linkArrow = LinkArrow.NONE;

	private boolean opale;
	private boolean horizontalSolitary;
	private String sametail;
	private VisibilityModifier visibilityModifier;
	private final StyleBuilder styleBuilder;

	private Url url;

	public String idCommentForSvg() {
		if (type.looksLikeRevertedForSvg()) {
			final String comment = getEntity1().getCodeGetName() + "<-" + getEntity2().getCodeGetName();
			return comment;
		}
		if (type.looksLikeNoDecorAtAllSvg()) {
			final String comment = getEntity1().getCodeGetName() + "-" + getEntity2().getCodeGetName();
			return comment;
		}
		final String comment = getEntity1().getCodeGetName() + "->" + getEntity2().getCodeGetName();
		return comment;
	}

	public UComment commentForSvg() {
		if (type.looksLikeRevertedForSvg()) {
			return new UComment(
					"reverse link " + getEntity1().getCodeGetName() + " to " + getEntity2().getCodeGetName());
		}
		return new UComment("link " + getEntity1().getCodeGetName() + " to " + getEntity2().getCodeGetName());
	}

	public Link(IEntity cl1, IEntity cl2, LinkType type, Display label, int length, StyleBuilder styleBuilder) {
		this(cl1, cl2, type, label, length, null, null, null, null, null, styleBuilder);
	}

	public Link(IEntity cl1, IEntity cl2, LinkType type, Display label, int length, String qualifier1,
			String qualifier2, String labeldistance, String labelangle, StyleBuilder styleBuilder) {
		this(cl1, cl2, type, label, length, qualifier1, qualifier2, labeldistance, labelangle, null, styleBuilder);
	}

	public Link(IEntity cl1, IEntity cl2, LinkType type, Display label, int length, String qualifier1,
			String qualifier2, String labeldistance, String labelangle, HColor specificColor,
			StyleBuilder styleBuilder) {
		if (length < 1) {
			throw new IllegalArgumentException();
		}
		if (cl1 == null) {
			throw new IllegalArgumentException();
		}
		if (cl2 == null) {
			throw new IllegalArgumentException();
		}

		this.styleBuilder = styleBuilder;
		this.cl1 = cl1;
		this.cl2 = cl2;
		this.type = type;
		if (Display.isNull(label)) {
			this.label = Display.NULL;
		} else {
			this.label = label.manageGuillemet();
			if (VisibilityModifier.isVisibilityCharacter(label.get(0))) {
				visibilityModifier = VisibilityModifier.getVisibilityModifier(label.get(0), false);
			}

		}
		this.length = length;
		this.qualifier1 = qualifier1;
		this.qualifier2 = qualifier2;
		this.labeldistance = labeldistance;
		this.labelangle = labelangle;
		this.setSpecificColor(specificColor);
		if (qualifier1 != null) {
			((ILeaf) cl1).setNearDecoration(true);
		}
		if (qualifier2 != null) {
			((ILeaf) cl2).setNearDecoration(true);
		}
		// if (type.getDecor2() == LinkDecor.EXTENDS) {
		// setSametail(cl1.getUid());
		// }
	}

	// private static boolean doWeHaveToRemoveUrlAtStart(Display label) {
	// if (label.size() == 0) {
	// return false;
	// }
	// final String s = label.get(0).toString();
	// if (s.matches("^\\[\\[\\S+\\]\\].+$")) {
	// return true;
	// }
	// return false;
	// }

	public Link getInv() {
		// if (getLength() == 1) {
		// final int x = cl1.getXposition();
		// cl2.setXposition(x-1);
		// }
		final Link result = new Link(cl2, cl1, getType().getInversed(), label, length, qualifier2, qualifier1,
				labeldistance, labelangle, getSpecificColor(), styleBuilder);
		result.inverted = !this.inverted;
		result.port1 = this.port2;
		result.port2 = this.port1;
		result.url = this.url;
		result.linkConstraint = this.linkConstraint;
		return result;
	}

	@Override
	public void goNorank() {
		setConstraint(false);
	}

	public String getLabeldistance() {
		// Default in dot 1.0
		return labeldistance;
	}

	public String getLabelangle() {
		// Default in dot -25
		return labelangle;
	}

	public String getUid() {
		return uid;
	}

	public final boolean isInvis() {
		if (type.isInvisible()) {
			return true;
		}
		return invis;
	}

	public final void setInvis(boolean invis) {
		this.invis = invis;
	}

	public boolean isBetween(IEntity cl1, IEntity cl2) {
		if (cl1.equals(this.cl1) && cl2.equals(this.cl2)) {
			return true;
		}
		if (cl1.equals(this.cl2) && cl2.equals(this.cl1)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return super.toString() + " {" + length + "} " + cl1 + "-->" + cl2;
	}

	public IEntity getEntity1() {
		return cl1;
	}

	public IEntity getEntity2() {
		return cl2;
	}

	public EntityPort getEntityPort1(Bibliotekon bibliotekon) {
		return new EntityPort(bibliotekon.getNodeUid((ILeaf) cl1), port1);
	}

	public EntityPort getEntityPort2(Bibliotekon bibliotekon) {
		return new EntityPort(bibliotekon.getNodeUid((ILeaf) cl2), port2);
	}

	@Override
	public LinkType getType() {
		if (opale) {
			return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		}
		if (getSametail() != null) {
			return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		}
		LinkType result = type;
		if (OptionFlags.USE_INTERFACE_EYE1) {
			if (isLollipopInterfaceEye(cl1)) {
				type = type.withLollipopInterfaceEye1();
			}
			if (isLollipopInterfaceEye(cl2)) {
				type = type.withLollipopInterfaceEye2();
			}
		}
		return result;
	}

	private boolean isReallyGroup(IEntity ent) {
		if (ent.isGroup() == false) {
			return false;
		}
		final IGroup group = (IGroup) ent;
		return group.getChildren().size() + group.getLeafsDirect().size() > 0;
	}

	public LinkType getTypePatchCluster() {
		LinkType result = getType();
		if (isReallyGroup(getEntity1())) {
			result = result.withoutDecors2();
		}
		if (isReallyGroup(getEntity2())) {
			result = result.withoutDecors1();
		}
		return result;
	}

	private LinkType getTypeSpecialForPrinting() {
		if (opale) {
			return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		}
		LinkType result = type;
		if (OptionFlags.USE_INTERFACE_EYE1) {
			if (isLollipopInterfaceEye(cl1)) {
				type = type.withLollipopInterfaceEye1();
			}
			if (isLollipopInterfaceEye(cl2)) {
				type = type.withLollipopInterfaceEye2();
			}
		}
		return result;
	}

	private boolean isLollipopInterfaceEye(IEntity ent) {
		return ent.getUSymbol() instanceof USymbolInterface;
	}

	public Display getLabel() {
		return label;
	}

	public int getLength() {
		return length;
	}

	public final void setLength(int length) {
		this.length = length;
	}

	public String getQualifier1() {
		return qualifier1;
	}

	public String getQualifier2() {
		return qualifier2;
	}

	public final double getWeight() {
		return weight;
	}

	public final void setWeight(double weight) {
		this.weight = weight;
	}

	public final Display getNote() {
		return note;
	}

	public final NoteLinkStrategy getNoteLinkStrategy() {
		return noteLinkStrategy;
	}

	public final Colors getNoteColors() {
		return noteColors;
	}

	public final Position getNotePosition() {
		return notePosition;
	}

	public final void addNote(Display note, Position position, Colors colors) {
		this.note = note;
		this.notePosition = position;
		this.noteColors = colors;
		this.noteLinkStrategy = NoteLinkStrategy.NORMAL;
	}

	public final void addNoteFrom(Link other, NoteLinkStrategy strategy) {
		this.note = other.note;
		this.notePosition = other.notePosition;
		this.noteColors = other.noteColors;
		this.noteLinkStrategy = strategy;
	}

	public boolean isAutoLinkOfAGroup() {
		if (getEntity1().isGroup() == false) {
			return false;
		}
		if (getEntity2().isGroup() == false) {
			return false;
		}
		if (getEntity1() == getEntity2()) {
			return true;
		}
		return false;
	}

	public boolean containsType(LeafType type) {
		if (getEntity1().getLeafType() == type || getEntity2().getLeafType() == type) {
			return true;
		}
		return false;
	}

	public boolean contains(IEntity entity) {
		if (getEntity1() == entity || getEntity2() == entity) {
			return true;
		}
		return false;
	}

	public IEntity getOther(IEntity entity) {
		if (getEntity1() == entity) {
			return getEntity2();
		}
		if (getEntity2() == entity) {
			return getEntity1();
		}
		throw new IllegalArgumentException();
	}

	public double getMarginDecors1(StringBounder stringBounder, UFont fontQualif, ISkinSimple spriteContainer) {
		final double q = getQualifierMargin(stringBounder, fontQualif, qualifier1, spriteContainer);
		final LinkDecor decor = getType().getDecor1();
		return decor.getMargin() + q;
	}

	public double getMarginDecors2(StringBounder stringBounder, UFont fontQualif, ISkinSimple spriteContainer) {
		final double q = getQualifierMargin(stringBounder, fontQualif, qualifier2, spriteContainer);
		final LinkDecor decor = getType().getDecor2();
		return decor.getMargin() + q;
	}

	private double getQualifierMargin(StringBounder stringBounder, UFont fontQualif, String qualif,
			ISkinSimple spriteContainer) {
		if (qualif != null) {
			final TextBlock b = Display.create(qualif).create(FontConfiguration.blackBlueTrue(fontQualif),
					HorizontalAlignment.LEFT, spriteContainer);
			final Dimension2D dim = b.calculateDimension(stringBounder);
			return Math.max(dim.getWidth(), dim.getHeight());
		}
		return 0;
	}

	public final boolean isConstraint() {
		return constraint;
	}

	public final void setConstraint(boolean constraint) {
		this.constraint = constraint;
	}

	public void setOpale(boolean opale) {
		this.opale = opale;
	}

	public final void setHorizontalSolitary(boolean horizontalSolitary) {
		this.horizontalSolitary = horizontalSolitary;
	}

	public final boolean isHorizontalSolitary() {
		return horizontalSolitary;
	}

	public final LinkArrow getLinkArrow() {
		if (inverted) {
			return linkArrow.reverse();
		}
		return linkArrow;
	}

	public final void setLinkArrow(LinkArrow linkArrow) {
		this.linkArrow = linkArrow;
	}

	public final boolean isInverted() {
		return inverted;
	}

	public boolean hasEntryPoint() {
		return (getEntity1().isGroup() == false && ((ILeaf) getEntity1()).getEntityPosition() != EntityPosition.NORMAL)
				|| (getEntity2().isGroup() == false
						&& ((ILeaf) getEntity2()).getEntityPosition() != EntityPosition.NORMAL);
	}

	public boolean hasTwoEntryPointsSameContainer() {
		return getEntity1().isGroup() == false && getEntity2().isGroup() == false
				&& ((ILeaf) getEntity1()).getEntityPosition() != EntityPosition.NORMAL
				&& ((ILeaf) getEntity2()).getEntityPosition() != EntityPosition.NORMAL
				&& getEntity1().getParentContainer() == getEntity2().getParentContainer();
	}

	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public boolean isHidden() {
		return hidden || cl1.isHidden() || cl2.isHidden();
	}

	public boolean sameConnections(Link other) {
		if (this.cl1 == other.cl1 && this.cl2 == other.cl2) {
			return true;
		}
		if (this.cl1 == other.cl2 && this.cl2 == other.cl1) {
			return true;
		}
		return false;
	}

	public boolean doesTouch(Link other) {
		if (this.cl1 == other.cl1) {
			return true;
		}
		if (this.cl1 == other.cl2) {
			return true;
		}
		if (this.cl2 == other.cl1) {
			return true;
		}
		if (this.cl2 == other.cl2) {
			return true;
		}
		return false;
	}

	public boolean isAutolink() {
		return cl1 == cl2;
	}

	public boolean isRemoved() {
		return cl1.isRemoved() || cl2.isRemoved();
	}

	public boolean hasUrl() {
		if (Display.isNull(label) == false && label.hasUrl()) {
			return true;
		}
		return getUrl() != null;
	}

	public String getSametail() {
		return sametail;
	}

	public void setSametail(String sametail) {
		this.sametail = sametail;
	}

	public void setPortMembers(String port1, String port2) {
		this.port1 = port1;
		this.port2 = port2;
		if (port1 != null) {
			((ILeaf) cl1).addPortShortName(port1);
		}
		if (port2 != null) {
			((ILeaf) cl2).addPortShortName(port2);
		}
	}

	public final VisibilityModifier getVisibilityModifier() {
		return visibilityModifier;
	}

	private UmlDiagramType umlType;

	public void setUmlDiagramType(UmlDiagramType type) {
		this.umlType = type;
	}

	public UmlDiagramType getUmlDiagramType() {
		return umlType;
	}

	private LinkConstraint linkConstraint;

	public void setLinkConstraint(LinkConstraint linkConstraint) {
		this.linkConstraint = linkConstraint;
	}

	public final LinkConstraint getLinkConstraint() {
		return linkConstraint;
	}

}
