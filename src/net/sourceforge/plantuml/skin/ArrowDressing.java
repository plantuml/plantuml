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

public class ArrowDressing {

	private final ArrowHead head;
	private final ArrowPart part;
	private final ArrowDecoration decoration;

	public String name() {
		return toString();
	}

	@Override
	public String toString() {
		return head.name() + "*" + decoration.name();
	}

	private ArrowDressing(ArrowHead head, ArrowPart part, ArrowDecoration decoration) {
		if (head == null || part == null || decoration == null) {
			throw new IllegalArgumentException();
		}
		this.head = head;
		this.part = part;
		this.decoration = decoration;
	}

	public static ArrowDressing create() {
		return new ArrowDressing(ArrowHead.NONE, ArrowPart.FULL, ArrowDecoration.NONE);
	}

	public ArrowDressing withHead(ArrowHead head) {
		return new ArrowDressing(head, part, decoration);
	}

	public ArrowDressing withPart(ArrowPart part) {
		return new ArrowDressing(head, part, decoration);
	}

	public ArrowDressing withDecoration(ArrowDecoration decoration) {
		return new ArrowDressing(head, part, decoration);
	}

	public ArrowHead getHead() {
		return head;
	}

	public ArrowPart getPart() {
		return part;
	}

	public ArrowDecoration getDecoration() {
		return decoration;
	}

}
