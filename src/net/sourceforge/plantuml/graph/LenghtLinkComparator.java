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
package net.sourceforge.plantuml.graph;

import java.util.Comparator;
import java.util.Map;

public class LenghtLinkComparator implements Comparator<ALink> {

	private final Map<ANode, Integer> cols;

	public LenghtLinkComparator(Map<ANode, Integer> cols) {
		this.cols = cols;
	}

	public int compare(ALink link1, ALink link2) {
		return (int) Math.signum(getLenght(link1) - getLenght(link2));
	}

	private double getLenght(ALink link) {
		final ANode n1 = link.getNode1();
		final ANode n2 = link.getNode2();
		final int deltaRow = n2.getRow() - n1.getRow();
		final int deltaCol = cols.get(n2) - cols.get(n1);
		return deltaRow * deltaRow + deltaCol * deltaCol;
	}

}
