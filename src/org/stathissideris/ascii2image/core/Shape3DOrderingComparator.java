/*
 * DiTAA - Diagrams Through Ascii Art
 * 
 * Copyright (C) 2004 Efstathios Sideris
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *   
 */
package org.stathissideris.ascii2image.core;

import java.util.Comparator;

import org.stathissideris.ascii2image.graphics.DiagramShape;

/**
 * 
 * @author Efstathios Sideris
 */
public class Shape3DOrderingComparator implements Comparator {

	/**
	 * Puts diagram shapes in pseudo-3d order starting from back to front
	 * 
	 */
	public int compare(Object object1, Object object2) {
		if(!(object1 instanceof DiagramShape)
				|| !(object2 instanceof DiagramShape))
			throw new RuntimeException("This comparator can only compare DiagramShapeS");
		
		DiagramShape shape1 = (DiagramShape) object1;
		DiagramShape shape2 = (DiagramShape) object2;
		
		double y1 = shape1.makeIntoPath().getBounds().getCenterY();
		double y2 = shape2.makeIntoPath().getBounds().getCenterY();
		
		if(y1 > y2) return -1;
		if(y1 < y2) return 1;
		
		return 0;
	}

}
