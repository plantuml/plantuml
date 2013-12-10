/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 5191 $
 *
 */
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.graphic.HtmlColor;

public class ArrowConfiguration {

	private final ArrowBody body;

	private final ArrowDressing dressing1;
	private final ArrowDressing dressing2;

	private final HtmlColor color;

	private ArrowConfiguration(ArrowBody body, ArrowDressing dressing1, ArrowDressing dressing2, HtmlColor color) {
		if (body == null || dressing1 == null) {
			throw new IllegalArgumentException();
		}
		this.body = body;
		this.dressing1 = dressing1;
		this.dressing2 = dressing2;
		this.color = color;
	}

	@Override
	public String toString() {
		return name();
	}

	public String name() {
		return body.name() + "(" + dressing1.name() + ")(" + dressing2.name() + ")" + color;
	}

	public static ArrowConfiguration withDirectionNormal() {
		return new ArrowConfiguration(ArrowBody.NORMAL, ArrowDressing.create(), ArrowDressing.create().withHead(
				ArrowHead.NORMAL), null);
	}

	public static ArrowConfiguration withDirectionBoth() {
		return new ArrowConfiguration(ArrowBody.NORMAL, ArrowDressing.create().withHead(ArrowHead.NORMAL),
				ArrowDressing.create().withHead(ArrowHead.NORMAL), null);
	}

	public static ArrowConfiguration withDirectionSelf() {
		return new ArrowConfiguration(ArrowBody.NORMAL, ArrowDressing.create().withHead(ArrowHead.NORMAL), null, null);
	}

	public static ArrowConfiguration withDirectionReverse() {
		return withDirectionNormal().reverse();
	}

	public ArrowConfiguration reverse() {
		return new ArrowConfiguration(body, dressing2, dressing1, color);
	}

	public ArrowConfiguration self() {
		return new ArrowConfiguration(body, dressing1, null, color);
	}

	public ArrowConfiguration withDotted() {
		return new ArrowConfiguration(ArrowBody.DOTTED, dressing1, dressing2, color);
	}

	public ArrowConfiguration withHead(ArrowHead head) {
		final ArrowDressing newDressing1 = addHead(dressing1, head);
		final ArrowDressing newDressing2 = addHead(dressing2, head);
		return new ArrowConfiguration(body, newDressing1, newDressing2, color);
	}

	private static ArrowDressing addHead(ArrowDressing dressing, ArrowHead head) {
		if (dressing == null || dressing.getHead() == ArrowHead.NONE) {
			return dressing;
		}
		return dressing.withHead(head);
	}

	public ArrowConfiguration withHead1(ArrowHead head) {
		return new ArrowConfiguration(body, dressing1.withHead(head), dressing2, color);
	}

	public ArrowConfiguration withHead2(ArrowHead head) {
		return new ArrowConfiguration(body, dressing1, dressing2.withHead(head), color);
	}

	public ArrowConfiguration withPart(ArrowPart part) {
		if (dressing2 != null && dressing2.getHead() != ArrowHead.NONE) {
			return new ArrowConfiguration(body, dressing1, dressing2.withPart(part), color);
		}
		return new ArrowConfiguration(body, dressing1.withPart(part), dressing2, color);
	}

	public ArrowConfiguration withDecorationStart(ArrowDecoration decorationStart) {
		return new ArrowConfiguration(body, dressing1.withDecoration(decorationStart), dressing2, color);
	}

	public ArrowConfiguration withDecorationEnd(ArrowDecoration decorationEnd) {
		return new ArrowConfiguration(body, dressing1, dressing2.withDecoration(decorationEnd), color);
	}

	public ArrowConfiguration withColor(HtmlColor color) {
		return new ArrowConfiguration(body, dressing1, dressing2, color);
	}

	public final ArrowDecoration getDecorationEnd() {
		return this.dressing2.getDecoration();
	}

	public final ArrowDecoration getDecorationStart() {
		return this.dressing1.getDecoration();
	}

	public final ArrowDirection getArrowDirection() {
		if (this.dressing2 == null) {
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

	public ArrowHead getHead() {
		if (dressing2 != null && dressing2.getHead() != ArrowHead.NONE) {
			return dressing2.getHead();
		}
		return dressing1.getHead();
	}

	public boolean isAsync() {
		return dressing1.getHead() == ArrowHead.ASYNC || (dressing2 != null && dressing2.getHead() == ArrowHead.ASYNC);
	}

	public final ArrowPart getPart() {
		if (dressing2 != null && dressing2.getHead() != ArrowHead.NONE) {
			return dressing2.getPart();
		}
		return dressing1.getPart();
	}

	public HtmlColor getColor() {
		return color;
	}

	public ArrowDressing getDressing1() {
		return dressing1;
	}

	public ArrowDressing getDressing2() {
		return dressing2;
	}

}
