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
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class ArrowConfiguration {

	private final ArrowBody body;

	private final ArrowDressing dressing1;
	private final ArrowDressing dressing2;

	private final ArrowDecoration decoration1;
	private final ArrowDecoration decoration2;

	private final HColor color;

	private final boolean isSelf;
	private final double thickness;
	private final boolean reverseDefine;

	private ArrowConfiguration(ArrowBody body, ArrowDressing dressing1, ArrowDressing dressing2,
			ArrowDecoration decoration1, ArrowDecoration decoration2, HColor color, boolean isSelf,
			double thickness, boolean reverseDefine) {
		if (body == null || dressing1 == null || dressing2 == null) {
			throw new IllegalArgumentException();
		}
		this.reverseDefine = reverseDefine;
		this.thickness = thickness;
		this.body = body;
		this.dressing1 = dressing1;
		this.dressing2 = dressing2;
		this.decoration1 = decoration1;
		this.decoration2 = decoration2;
		this.color = color;
		this.isSelf = isSelf;
	}

	@Override
	public String toString() {
		return name();
	}

	public String name() {
		return body.name() + "(" + dressing1.name() + " " + decoration1 + ")(" + dressing2.name() + " " + decoration2
				+ ")" + isSelf + " " + color;
	}

	public static ArrowConfiguration withDirectionNormal() {
		return new ArrowConfiguration(ArrowBody.NORMAL, ArrowDressing.create(), ArrowDressing.create().withHead(
				ArrowHead.NORMAL), ArrowDecoration.NONE, ArrowDecoration.NONE, null, false, 1, false);
	}

	public static ArrowConfiguration withDirectionBoth() {
		return new ArrowConfiguration(ArrowBody.NORMAL, ArrowDressing.create().withHead(ArrowHead.NORMAL),
				ArrowDressing.create().withHead(ArrowHead.NORMAL), ArrowDecoration.NONE, ArrowDecoration.NONE, null,
				false, 1, false);
	}

	public static ArrowConfiguration withDirectionSelf() {
		return new ArrowConfiguration(ArrowBody.NORMAL, ArrowDressing.create().withHead(ArrowHead.NORMAL),
				ArrowDressing.create().withHead(ArrowHead.NORMAL), ArrowDecoration.NONE, ArrowDecoration.NONE, null,
				true, 1, false);
	}

	public static ArrowConfiguration withDirectionReverse() {
		return withDirectionNormal().reverse();
	}

	public ArrowConfiguration reverse() {
		return new ArrowConfiguration(body, dressing2, dressing1, decoration2, decoration1, color, isSelf, thickness,
				reverseDefine);
	}

	public ArrowConfiguration self() {
		return new ArrowConfiguration(body, dressing1, dressing2, decoration1, decoration2, color, true, thickness,
				reverseDefine);
	}

	public ArrowConfiguration withBody(ArrowBody type) {
		return new ArrowConfiguration(type, dressing1, dressing2, decoration1, decoration2, color, isSelf, thickness,
				reverseDefine);
	}

	public ArrowConfiguration withHead(ArrowHead head) {
		final ArrowDressing newDressing1 = addHead(dressing1, head);
		final ArrowDressing newDressing2 = addHead(dressing2, head);
		return new ArrowConfiguration(body, newDressing1, newDressing2, decoration1, decoration2, color, isSelf,
				thickness, reverseDefine);
	}

	private static ArrowDressing addHead(ArrowDressing dressing, ArrowHead head) {
		if (dressing.getHead() == ArrowHead.NONE) {
			return dressing;
		}
		return dressing.withHead(head);
	}

	public ArrowConfiguration withHead1(ArrowHead head) {
		return new ArrowConfiguration(body, dressing1.withHead(head), dressing2, decoration1, decoration2, color,
				isSelf, thickness, reverseDefine);
	}

	public ArrowConfiguration withHead2(ArrowHead head) {
		return new ArrowConfiguration(body, dressing1, dressing2.withHead(head), decoration1, decoration2, color,
				isSelf, thickness, reverseDefine);
	}

	public ArrowConfiguration withPart(ArrowPart part) {
		if (dressing2.getHead() != ArrowHead.NONE) {
			return new ArrowConfiguration(body, dressing1, dressing2.withPart(part), decoration1, decoration2, color,
					isSelf, thickness, reverseDefine);
		}
		return new ArrowConfiguration(body, dressing1.withPart(part), dressing2, decoration1, decoration2, color,
				isSelf, thickness, reverseDefine);
	}

	public ArrowConfiguration withDecoration1(ArrowDecoration decoration1) {
		return new ArrowConfiguration(body, dressing1, dressing2, decoration1, decoration2, color, isSelf, thickness,
				reverseDefine);
	}

	public ArrowConfiguration withDecoration2(ArrowDecoration decoration2) {
		return new ArrowConfiguration(body, dressing1, dressing2, decoration1, decoration2, color, isSelf, thickness,
				reverseDefine);
	}

	public ArrowConfiguration withColor(HColor color) {
		return new ArrowConfiguration(body, dressing1, dressing2, decoration1, decoration2, color, isSelf, thickness,
				reverseDefine);
	}

	public final ArrowDecoration getDecoration1() {
		return this.decoration1;
	}

	public final ArrowDecoration getDecoration2() {
		return this.decoration2;
	}

	public final ArrowDirection getArrowDirection() {
		if (isSelf) {
			return ArrowDirection.SELF;
		}
		if (this.dressing1.getHead() == ArrowHead.NONE && this.dressing2.getHead() != ArrowHead.NONE) {
			return ArrowDirection.LEFT_TO_RIGHT_NORMAL;
		}
		if (this.dressing1.getHead() != ArrowHead.NONE && this.dressing2.getHead() == ArrowHead.NONE) {
			return ArrowDirection.RIGHT_TO_LEFT_REVERSE;
		}
		return ArrowDirection.BOTH_DIRECTION;
	}

	public boolean isSelfArrow() {
		return getArrowDirection() == ArrowDirection.SELF;
	}

	public boolean isDotted() {
		return body == ArrowBody.DOTTED;
	}

	public boolean isHidden() {
		return body == ArrowBody.HIDDEN;
	}

	public ArrowHead getHead() {
		if (dressing2 != null && dressing2.getHead() != ArrowHead.NONE) {
			return dressing2.getHead();
		}
		return dressing1.getHead();
	}

	public boolean isAsync() {
		return dressing1.getHead() == ArrowHead.ASYNC || dressing2.getHead() == ArrowHead.ASYNC;
	}

	public final ArrowPart getPart() {
		if (dressing2.getHead() != ArrowHead.NONE) {
			return dressing2.getPart();
		}
		return dressing1.getPart();
	}

	public HColor getColor() {
		return color;
	}

	public ArrowDressing getDressing1() {
		return dressing1;
	}

	public ArrowDressing getDressing2() {
		return dressing2;
	}

	public static UGraphic stroke(UGraphic ug, double dashVisible, double dashSpace, double thickness) {
		return ug.apply(new UStroke(dashVisible, dashSpace, thickness));
	}

	public UGraphic applyStroke(UGraphic ug) {
		if (isDotted()) {
			return ug.apply(new UStroke(2, 2, thickness));
		}
		return ug.apply(new UStroke(thickness));
	}

	public UGraphic applyThicknessOnly(UGraphic ug) {
		return ug.apply(new UStroke(thickness));
	}

	public ArrowConfiguration withThickness(double thickness) {
		return new ArrowConfiguration(body, dressing1, dressing2, decoration1, decoration2, color, isSelf, thickness,
				reverseDefine);
	}

	public ArrowConfiguration reverseDefine() {
		return new ArrowConfiguration(body, dressing1, dressing2, decoration1, decoration2, color, isSelf, thickness,
				!reverseDefine);
	}

	public final boolean isReverseDefine() {
		return reverseDefine;
	}

}
