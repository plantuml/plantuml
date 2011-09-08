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
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UPath implements UShape, Iterable<USegment> {

	private final List<USegment> segments = new ArrayList<USegment>();

	public void add(double[] coord, USegmentType pathType) {
		segments.add(new USegment(coord, pathType));
	}

	public void moveTo(double x, double y) {
		add(new double[] { x, y }, USegmentType.SEG_MOVETO);
	}

	public void lineTo(double x, double y) {
		add(new double[] { x, y }, USegmentType.SEG_LINETO);
	}

	public void cubicTo(double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
		add(new double[] { ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2 }, USegmentType.SEG_CUBICTO);
	}

	@Override
	public String toString() {
		return segments.toString();
	}

	public Iterator<USegment> iterator() {
		return segments.iterator();
	}

}
