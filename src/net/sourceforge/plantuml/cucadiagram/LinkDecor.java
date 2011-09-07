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
 * Revision $Revision: 4604 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

public enum LinkDecor {

	NONE(2, false), EXTENDS(30, false), COMPOSITION(15, true), AGREGATION(15, false), ARROW(10, true), PLUS(0, false), SQUARRE(30, false);
	
	private final int size;
	private final boolean fill;
	
	private LinkDecor(int size, boolean fill) {
		this.size = size;
		this.fill = fill;
	}

	public String getArrowDot() {
		if (this == LinkDecor.NONE) {
			return "none";
		} else if (this == LinkDecor.EXTENDS) {
			return "empty";
		} else if (this == LinkDecor.COMPOSITION) {
			return "diamond";
		} else if (this == LinkDecor.AGREGATION) {
			return "ediamond";
		} else if (this == LinkDecor.ARROW) {
			return "open";
		} else if (this == LinkDecor.PLUS) {
			return "odot";
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public String getArrowDotSvek() {
		if (this == LinkDecor.NONE) {
			return "none";
		} else if (this == LinkDecor.EXTENDS) {
			return "empty";
		} else if (this == LinkDecor.COMPOSITION) {
			return "diamond";
		} else if (this == LinkDecor.AGREGATION) {
			return "ediamond";
		} else if (this == LinkDecor.ARROW) {
			return "open";
		} else if (this == LinkDecor.PLUS) {
			return "empty";
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public int getSize() {
		return size;
	}
	
	public boolean isFill() {
		return fill;
	}

}
