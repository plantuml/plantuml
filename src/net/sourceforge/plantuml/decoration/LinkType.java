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
 * 
 *
 */
package net.sourceforge.plantuml.decoration;

import java.util.Objects;

import net.sourceforge.plantuml.abel.LinkStrategy;
import net.sourceforge.plantuml.klimt.UStroke;

public class LinkType {

	private final LinkDecor decor1;
	private final LinkStyle linkStyle;
	private final LinkDecor decor2;
	private final LinkMiddleDecor middleDecor;

	public boolean isDoubleDecorated() {
		return decor1 != LinkDecor.NONE && decor2 != LinkDecor.NONE;
	}

	public boolean looksLikeRevertedForSvg() {
		if (this.decor1 == LinkDecor.NONE && this.decor2 != LinkDecor.NONE)
			return true;

		return false;
	}

	public boolean looksLikeNoDecorAtAllSvg() {
		if (this.decor1 == LinkDecor.NONE && this.decor2 == LinkDecor.NONE)
			return true;

		if (this.decor1 != LinkDecor.NONE && this.decor2 != LinkDecor.NONE)
			return true;

		return false;
	}

	public LinkType(LinkDecor decor1, LinkDecor decor2) {
		this(decor1, decor2, LinkMiddleDecor.NONE, LinkStyle.NORMAL());
	}

	private LinkType(LinkDecor decor1, LinkDecor decor2, LinkMiddleDecor middleDecor, LinkStyle style) {
		this.decor1 = decor1;
		this.decor2 = decor2;
		this.middleDecor = middleDecor;
		this.linkStyle = Objects.requireNonNull(style);
	}

	public LinkType withoutDecors1() {
		return new LinkType(LinkDecor.NONE, decor2, middleDecor, linkStyle);
	}

	public LinkType withoutDecors2() {
		return new LinkType(decor1, LinkDecor.NONE, middleDecor, linkStyle);
	}

	// public boolean contains(LinkDecor decors) {
	// return decor1 == decors || decor2 == decors;
	// }

	@Override
	public String toString() {
		return decor1 + "-" + linkStyle + "-" + decor2;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final LinkType other = (LinkType) obj;
		return this.decor1 == other.decor1 && this.decor2 == other.decor2 && this.linkStyle == other.linkStyle;
	}

	public boolean isInvisible() {
		return linkStyle.isInvisible();
	}

	public LinkType goDashed() {
		return new LinkType(decor1, decor2, middleDecor, LinkStyle.DASHED());
	}

	public LinkType goDotted() {
		return new LinkType(decor1, decor2, middleDecor, LinkStyle.DOTTED());
	}

	public LinkType goThickness(double thickness) {
		return new LinkType(decor1, decor2, middleDecor, linkStyle.goThickness(thickness));
	}

	public LinkType goBold() {
		return new LinkType(decor1, decor2, middleDecor, LinkStyle.BOLD());
	}

	public LinkType getInversed() {
		return new LinkType(decor2, decor1, middleDecor.getInversed(), linkStyle);
	}

	public LinkType withMiddleCircle() {
		return new LinkType(decor1, decor2, LinkMiddleDecor.CIRCLE, linkStyle);
	}

	public LinkType withMiddleCircleCircled() {
		return new LinkType(decor1, decor2, LinkMiddleDecor.CIRCLE_CIRCLED, linkStyle);
	}

	public LinkType withMiddleCircleCircled1() {
		return new LinkType(decor1, decor2, LinkMiddleDecor.CIRCLE_CIRCLED1, linkStyle);
	}

	public LinkType withMiddleCircleCircled2() {
		return new LinkType(decor1, decor2, LinkMiddleDecor.CIRCLE_CIRCLED2, linkStyle);
	}

	public LinkType getInvisible() {
		return new LinkType(decor1, decor2, middleDecor, LinkStyle.INVISIBLE());
	}

	public String getSpecificDecorationSvek(LinkStrategy linkStrategy) {

		if (linkStrategy == LinkStrategy.SIMPLIER)
			return "arrowtail=none,arrowhead=none";

		final StringBuilder sb = new StringBuilder();

		final boolean isEmpty1 = decor1 == LinkDecor.NONE;
		final boolean isEmpty2 = decor2 == LinkDecor.NONE;

		if (isEmpty1 && isEmpty2) {
			sb.append("arrowtail=none");
			sb.append(",arrowhead=none");
		} else if (isEmpty1 == false && isEmpty2 == false) {
			sb.append("dir=both,");
			sb.append("arrowtail=empty");
			sb.append(",arrowhead=empty");
		} else if (isEmpty1 && isEmpty2 == false) {
			sb.append("arrowtail=empty");
			sb.append(",arrowhead=none");
			sb.append(",dir=back");
			// } else if (isEmpty1 == false && isEmpty2) {
			// sb.append("arrowtail=none");
			// sb.append(",arrowhead=empty");
		}

		final double arrowsize = Math.max(decor1.getArrowSize(), decor2.getArrowSize());
		if (arrowsize > 0) {
			if (sb.length() > 0)
				sb.append(",");

			sb.append("arrowsize=" + arrowsize);
		}
		return sb.toString();
	}

	public final LinkDecor getDecor1() {
		return decor1;
	}

	public final LinkStyle getStyle() {
		return linkStyle;
	}

	public final LinkDecor getDecor2() {
		return decor2;
	}

	private boolean isExtendsOrAggregationOrCompositionOrPlus() {
		return isExtends() || isAggregationOrComposition() || isPlus() || isOf(LinkDecor.DEFINEDBY)
				|| isOf(LinkDecor.REDEFINES);
	}

	private boolean isExtendsOrPlus() {
		return isExtends() || isPlus() || isOf(LinkDecor.DEFINEDBY) || isOf(LinkDecor.REDEFINES);
	}

	private boolean isOf(LinkDecor ld) {
		return decor1 == ld || decor2 == ld;
	}

	public boolean isExtends() {
		return decor1 == LinkDecor.EXTENDS || decor2 == LinkDecor.EXTENDS;
	}

	private boolean isPlus() {
		return decor1 == LinkDecor.PLUS || decor2 == LinkDecor.PLUS;
	}

	private boolean isAggregationOrComposition() {
		return decor1 == LinkDecor.AGREGATION || decor2 == LinkDecor.AGREGATION || decor1 == LinkDecor.COMPOSITION
				|| decor2 == LinkDecor.COMPOSITION;
	}

	public LinkType getPart1() {
		return new LinkType(decor1, LinkDecor.NONE, middleDecor, linkStyle);
	}

	public LinkType getPart2() {
		return new LinkType(LinkDecor.NONE, decor2, middleDecor, linkStyle);
	}

	public UStroke getStroke3(UStroke defaultThickness) {
		if (linkStyle.isThicknessOverrided())
			return linkStyle.getStroke3();

		if (defaultThickness == null)
			return linkStyle.getStroke3();

		if (defaultThickness.getDashVisible() == 0 && defaultThickness.getDashSpace() == 0)
			return linkStyle.goThickness(defaultThickness.getThickness()).getStroke3();

		return defaultThickness;
	}

	public LinkMiddleDecor getMiddleDecor() {
		return middleDecor;
	}

	public LinkType withLollipopInterfaceEye2() {
		return new LinkType(LinkDecor.NONE, decor2, middleDecor, linkStyle);
	}

	public LinkType withLollipopInterfaceEye1() {
		return new LinkType(decor1, LinkDecor.NONE, middleDecor, linkStyle);
	}

}
