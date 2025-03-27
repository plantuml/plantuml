/**
 * ditaa - Diagrams Through Ascii Art
 * 
 * Copyright (C) 2004-2011 Efstathios Sideris
 *
 * ditaa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * ditaa is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with ditaa.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package org.stathissideris.ascii2image.core;

import java.util.Comparator;

import org.stathissideris.ascii2image.graphics.DiagramShape;

/**
 * 
 * @author Efstathios Sideris
 */
public class ShapeAreaComparator implements Comparator<DiagramShape> {

	/**
	 * Puts diagram shapes in order or area starting from largest to smallest
	 * 
	 */
	public int compare(DiagramShape shape1, DiagramShape shape2) {
		double y1 = shape1.calculateArea();
		double y2 = shape2.calculateArea();
		
		if(y1 > y2) return -1;
		if(y1 < y2) return 1;
		
		return 0;
	}

}
