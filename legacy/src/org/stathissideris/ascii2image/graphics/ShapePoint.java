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
package org.stathissideris.ascii2image.graphics;


/**
 * 
 * @author Efstathios Sideris
 */
public class ShapePoint extends java.awt.geom.Point2D.Float {

	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_ROUND = 1;

	private boolean locked = false;
	
	private int type = 0;

	public ShapePoint() {
		super();
	}

	public ShapePoint(float x, float y) {
		super(x, y);
		this.type = TYPE_NORMAL;
	}

	public ShapePoint(float x, float y, int type) {
		super(x, y);
		this.type = type;
	}

	public ShapePoint(ShapePoint other){
		this(other.x, other.y, other.type);
	}
	
	/**
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param i
	 */
	public void setType(int i) {
		type = i;
	}
	
	public boolean isInLineWith(ShapePoint point){
		if(this.x == point.x) return true;
		if(this.y == point.y) return true;
		return false;
	}
	
	public boolean isWithinEdge(ShapeEdge edge) {
		if(edge.isHorizontal()) {
			if(x >= edge.getStartPoint().x && x <= edge.getEndPoint().x) return true;
			if(x >= edge.getEndPoint().x && x <= edge.getStartPoint().x) return true;
			return false;
		} else if(edge.isVertical()) {
			if(y >= edge.getStartPoint().y && y <= edge.getEndPoint().y) return true;
			if(y >= edge.getEndPoint().y && y <= edge.getStartPoint().y) return true;			
			return false;
		}
		throw new RuntimeException("Cannot calculate is ShapePoint is within sloped edge");
	}
	
	public boolean isNorthOf(ShapePoint point){
		return (this.y < point.y);
	}

	public boolean isSouthOf(ShapePoint point){
		return (this.y > point.y);
	}

	public boolean isWestOf(ShapePoint point){
		return (this.x < point.x);
	}

	public boolean isEastOf(ShapePoint point){
		return (this.x > point.x);
	}

	public String toString(){
		return "("+x+", "+y+")";
	}
	
	public void assign(ShapePoint point){
		this.x = point.x;
		this.y = point.y;
	}

	/**
	 * Does the same as assign, but respects the
	 * locked attribute 
	 * 
	 * @param point
	 */
	public void moveTo(ShapePoint point){
		if(locked) return;
		this.x = point.x;
		this.y = point.y;
	}


	/**
	 * @return
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param b
	 */
	public void setLocked(boolean b) {
		locked = b;
	}

}
