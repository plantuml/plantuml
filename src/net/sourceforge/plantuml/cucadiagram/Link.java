/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 5649 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.Arrays;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.cucadiagram.dot.DrawFile;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

public class Link implements Imaged {

	final private IEntity cl1;
	final private IEntity cl2;
	final private LinkType type;
	final private String label;

	final private int length;
	final private String qualifier1;
	final private String qualifier2;
	final private String uid = "LNK" + UniqueSequence.getValue();

	private DrawFile imageFile;
	private String note;
	private boolean invis = false;
	private int weight = 1;

	private final String labeldistance;
	private final String labelangle;

	public Link(IEntity cl1, IEntity cl2, LinkType type, String label, int length) {
		this(cl1, cl2, type, label, length, null, null, null, null);
	}

	public Link(IEntity cl1, IEntity cl2, LinkType type, String label, int length, String qualifier1,
			String qualifier2, String labeldistance, String labelangle) {
		if (length < 1) {
			throw new IllegalArgumentException();
		}
		if (cl1 == null || cl2 == null) {
			throw new IllegalArgumentException();
		}
		this.cl1 = cl1;
		this.cl2 = cl2;
		this.type = type;
		this.label = label;
		this.length = length;
		this.qualifier1 = qualifier1;
		this.qualifier2 = qualifier2;
		this.labeldistance = labeldistance;
		this.labelangle = labelangle;
	}

	public Link getInv() {
		return new Link(cl2, cl1, type.getInv(), label, length, qualifier2, qualifier1, labeldistance, labelangle);
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
		return invis;
	}

	public final void setInvis(boolean invis) {
		this.invis = invis;
	}

	private static IEntity muteProxy(IEntity ent, Group g, IEntity proxy) {
		if (ent.getParent() == g) {
			return proxy;
		}
		return ent;
	}

	public Link mute(Group g, Entity proxy) {
		if (cl1.getParent() == g && cl1.getType() != EntityType.GROUP && cl2.getParent() == g
				&& cl2.getType() != EntityType.GROUP) {
			return null;
		}
		final IEntity ent1 = muteProxy(cl1, g, proxy);
		final IEntity ent2 = muteProxy(cl2, g, proxy);
		if (this.cl1 == ent1 && this.cl2 == ent2) {
			return this;
		}
		return new Link(ent1, ent2, type, label, length, qualifier1, qualifier2, labeldistance, labelangle);
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
		return super.toString() + " " + cl1 + "-->" + cl2;
	}

	public IEntity getEntity1() {
		return cl1;
	}

	public IEntity getEntity2() {
		return cl2;
	}

	public LinkType getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	public int getLength() {
		return length;
	}

	public String getQualifier1() {
		return qualifier1;
	}

	public String getQualifier2() {
		return qualifier2;
	}

	public final int getWeight() {
		return weight;
	}

	public final void setWeight(int weight) {
		this.weight = weight;
	}

	public final String getNote() {
		return note;
	}

	public final void setNote(String note) {
		this.note = note;
	}

	public DrawFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(DrawFile imageFile) {
		this.imageFile = imageFile;
	}

	public boolean isAutolink(Group g) {
		if (getEntity1() == g.getEntityCluster() && getEntity2() == g.getEntityCluster()) {
			return true;
		}
		return false;
	}

	public boolean isToEdgeLink(Group g) {
		if (getEntity1().getParent() != g || getEntity2().getParent() != g) {
			return false;
		}
		assert getEntity1().getParent() == g && getEntity2().getParent() == g;
		if (isAutolink(g)) {
			return false;
		}

		if (getEntity2().getType() == EntityType.GROUP) {
			assert getEntity1().getType() != EntityType.GROUP;
			return true;
		}
		return false;
	}

	public boolean isFromEdgeLink(Group g) {
		if (getEntity1().getParent() != g || getEntity2().getParent() != g) {
			return false;
		}
		assert getEntity1().getParent() == g && getEntity2().getParent() == g;
		if (isAutolink(g)) {
			return false;
		}

		if (getEntity1().getType() == EntityType.GROUP) {
			assert getEntity2().getType() != EntityType.GROUP;
			return true;
		}
		return false;
	}

	public boolean containsType(EntityType type) {
		if (getEntity1().getType() == type || getEntity2().getType() == type) {
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

	public double getMarginDecors1(StringBounder stringBounder, Font fontQualif) {
		final double q = getQualifierMargin(stringBounder, fontQualif, qualifier1);
		final LinkDecor decor = type.getDecor1();
		return decor.getSize() + q;
	}

	public double getMarginDecors2(StringBounder stringBounder, Font fontQualif) {
		final double q = getQualifierMargin(stringBounder, fontQualif, qualifier2);
		final LinkDecor decor = type.getDecor2();
		return decor.getSize() + q;
	}

	private double getQualifierMargin(StringBounder stringBounder, Font fontQualif, String qualif) {
		if (qualif != null) {
			final TextBlock b = TextBlockUtils.create(Arrays.asList(qualif), fontQualif, Color.BLACK,
					HorizontalAlignement.LEFT);
			final Dimension2D dim = b.calculateDimension(stringBounder);
			return Math.max(dim.getWidth(), dim.getHeight());
		}
		return 0;
	}

}
