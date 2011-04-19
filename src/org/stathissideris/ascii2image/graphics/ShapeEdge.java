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

import java.awt.geom.GeneralPath;

/**
 * 
 * @author Efstathios Sideris
 */
public class ShapeEdge {
	
	private static final boolean DEBUG = false;
	
	private static final int TYPE_HORIZONTAL = 0;
	private static final int TYPE_VERTICAL = 1;
	private static final int TYPE_SLOPED = 2;
	
	
	private DiagramShape owner;
	private ShapePoint startPoint;
	private ShapePoint endPoint;
	
	public ShapeEdge(ShapePoint start, ShapePoint end, DiagramShape owner){
		this.startPoint = start;
		this.endPoint = end;
		this.owner = owner;
	}
	
	public ShapeEdge(ShapeEdge other){
		this(
			new ShapePoint(other.startPoint),
			new ShapePoint(other.endPoint),
			other.owner
		);
	}
	
	private float getDistanceFromOrigin() {
		int type = this.getType();
		if(type == TYPE_SLOPED)
			throw new RuntimeException("Cannot calculate distance of sloped edge from origin");
		if(type == TYPE_HORIZONTAL)
			return startPoint.y;
		return startPoint.x; //vertical
	}

	//TODO: moveInwardsBy() not implemented
	public void moveInwardsBy(float offset){
		int type = this.getType();
		if(type == TYPE_SLOPED)
			throw new RuntimeException("Cannot move a sloped egde inwards: "+this);
		
		float xOffset = 0;
		float yOffset = 0;
		
		ShapePoint middle = getMiddle();
		GeneralPath path = owner.makeIntoPath();
		if(type == TYPE_HORIZONTAL){
			xOffset = 0;
			ShapePoint up = new ShapePoint(middle.x, middle.y - 0.05f);
			ShapePoint down = new ShapePoint(middle.x, middle.y + 0.05f);
			if(path.contains(up)) yOffset = -offset;
			else if(path.contains(down)) yOffset = offset;
		} else if(type == TYPE_VERTICAL){
			yOffset = 0;
			ShapePoint left = new ShapePoint(middle.x - 0.05f, middle.y);
			ShapePoint right = new ShapePoint(middle.x + 0.05f, middle.y);
			if(path.contains(left)) xOffset = -offset;
			else if(path.contains(right)) xOffset = offset;
		}
		if(DEBUG) System.out.println("Moved edge "+this+" by "+xOffset+", "+yOffset);
		translate(xOffset, yOffset);
	}

	public void translate(float dx, float dy){
		startPoint.x += dx;
		startPoint.y += dy;
		endPoint.x += dx;
		endPoint.y += dy;
	}

	public ShapePoint getMiddle(){
		return new ShapePoint(
			(startPoint.x + endPoint.x) / 2,
			(startPoint.y + endPoint.y) / 2
		);
	}

	/**
	 * Returns the type of the edge
	 * (<code>TYPE_HORIZONTAL, TYPE_VERTICAL, TYPE_SLOPED).
	 * 
	 * @return
	 */
	private int getType(){
		if(isVertical()) return TYPE_VERTICAL;
		if(isHorizontal()) return TYPE_HORIZONTAL;
		return TYPE_SLOPED;
	}

	/**
	 * @return
	 */
	public ShapePoint getEndPoint() {
		return endPoint;
	}

	/**
	 * @return
	 */
	public ShapePoint getStartPoint() {
		return startPoint;
	}

	/**
	 * @param point
	 */
	public void setEndPoint(ShapePoint point) {
		endPoint = point;
	}

	/**
	 * @param point
	 */
	public void setStartPoint(ShapePoint point) {
		startPoint = point;
	}

	/**
	 * @return
	 */
	public DiagramShape getOwner() {
		return owner;
	}

	/**
	 * @param shape
	 */
	public void setOwner(DiagramShape shape) {
		owner = shape;
	}
	
	public boolean equals(Object object){
		if(!(object instanceof ShapeEdge)) return false;
		ShapeEdge edge = (ShapeEdge) object;
		if(startPoint.equals(edge.getStartPoint())
			&& endPoint.equals(edge.getEndPoint())) return true;
		if(startPoint.equals(edge.getEndPoint())
			&& endPoint.equals(edge.getStartPoint())) return true;
		return false;
	}

	public boolean touchesWith(ShapeEdge other){
		if(this.equals(other)) return true;
		
		if(this.isHorizontal() && other.isVertical()) return false;
		if(other.isHorizontal() && this.isVertical()) return false;
		
		if(this.getDistanceFromOrigin() != other.getDistanceFromOrigin()) return false;

		//covering this corner case (should produce false):
		//      ---------
		//              ---------
		
		ShapeEdge first = new ShapeEdge(this);
		ShapeEdge second = new ShapeEdge(other);
		
		if(first.isVertical()) {
			first.changeAxis();
			second.changeAxis();
		}
		
		first.fixDirection();
		second.fixDirection();
		
		if(first.startPoint.x > second.startPoint.x) {
			ShapeEdge temp = first;
			first = second;
			second = temp;
		}
		
		if(first.endPoint.equals(second.startPoint)) return false;
		
		// case 1:
		// ----------
		//      -----------
		
		// case 2:
		//         ------
		// -----------------
		
		if(this.startPoint.isWithinEdge(other) || this.endPoint.isWithinEdge(other)) return true;
		if(other.startPoint.isWithinEdge(this) || other.endPoint.isWithinEdge(this)) return true;	
		
		
		return false;
	}
	
	private void changeAxis(){
		ShapePoint temp = new ShapePoint(startPoint);
		startPoint = new ShapePoint(endPoint.y, endPoint.x);
		endPoint = new ShapePoint(temp.y, temp.x);
	}
	
	/**
	 * if horizontal flips start and end points so that start is left of end
	 * if verical flips start and end points so that start is over of end
	 *
	 */
	private void fixDirection(){
		if(isHorizontal()) {
			if(startPoint.x > endPoint.x) flipDirection();
		} else if(isVertical()) {
			if(startPoint.y > endPoint.y) flipDirection();
		} else {
			throw new RuntimeException("Cannot fix direction of sloped egde");
		}
	}
	
	private void flipDirection(){
		ShapePoint temp = startPoint;
		startPoint = endPoint;
		endPoint = temp;
	}
	
	public boolean isHorizontal(){
		if(startPoint.y == endPoint.y) return true;
		return false;
	}
	
	public boolean isVertical(){
		if(startPoint.x == endPoint.x) return true;
		return false;
	}

	public String toString(){
		return startPoint+" -> "+endPoint;
	}

}
