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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.stathissideris.ascii2image.text.TextGrid;

/**
 * 
 * @author Efstathios Sideris
 */
public class DiagramShape extends DiagramComponent {
	
	private static final boolean DEBUG = false;
	
	public static final int TYPE_SIMPLE = 0;
	public static final int TYPE_ARROWHEAD = 1;
	public static final int TYPE_POINT_MARKER = 2;
	public static final int TYPE_DOCUMENT = 3;
	public static final int TYPE_STORAGE = 4;
	public static final int TYPE_IO = 5;
	public static final int TYPE_DECISION = 6;
	public static final int TYPE_MANUAL_OPERATION = 7; // upside-down trapezoid
	public static final int TYPE_TRAPEZOID = 8; // rightside-up trapezoid
	public static final int TYPE_ELLIPSE = 9;
	public static final int TYPE_CUSTOM = 9999;

	protected int type = TYPE_SIMPLE;

	private Color fillColor = null;
	private Color strokeColor = Color.black;
	
	private boolean isClosed = false;
	private boolean isStrokeDashed = false;

	protected ArrayList points = new ArrayList();

	CustomShapeDefinition definition = null;

	public static void main(String[] args) {
	}

	public static DiagramShape createArrowhead(TextGrid grid, TextGrid.Cell cell, int cellXSize, int cellYSize) {
		if(!grid.isArrowhead(cell)) return null;
		if(grid.isNorthArrowhead(cell)) return createNorthArrowhead(grid, cell, cellXSize, cellYSize);
		if(grid.isSouthArrowhead(cell)) return createSouthArrowhead(grid, cell, cellXSize, cellYSize);
		if(grid.isWestArrowhead(cell)) return createWestArrowhead(grid, cell, cellXSize, cellYSize);
		if(grid.isEastArrowhead(cell)) return createEastArrowhead(grid, cell, cellXSize, cellYSize);
		return null;
	}

	private static DiagramShape createNorthArrowhead(TextGrid grid, TextGrid.Cell cell, int cellXSize, int cellYSize) {
		if(!grid.isNorthArrowhead(cell)) return null;
		DiagramShape shape = new DiagramShape();
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMidX(cell,cellXSize),
			Diagram.getCellMinY(cell,cellYSize)));
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMinX(cell,cellXSize),
			Diagram.getCellMaxY(cell,cellYSize)));
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMaxX(cell,cellXSize),
			Diagram.getCellMaxY(cell,cellYSize)));
		shape.setClosed(true);
		shape.setFillColor(Color.black);
		shape.setStrokeColor(Color.black);
		shape.setType(TYPE_ARROWHEAD);
		return shape;
	}

	private static DiagramShape createSouthArrowhead(TextGrid grid, TextGrid.Cell cell, int cellXSize, int cellYSize) {
		if(!grid.isSouthArrowhead(cell)) return null;
		DiagramShape shape = new DiagramShape();
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMinX(cell,cellXSize),
			Diagram.getCellMinY(cell,cellYSize)));
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMidX(cell,cellXSize),
			Diagram.getCellMaxY(cell,cellYSize)));
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMaxX(cell,cellXSize),
			Diagram.getCellMinY(cell,cellYSize)));
		shape.setClosed(true);
		shape.setFillColor(Color.black);
		shape.setStrokeColor(Color.black);
		shape.setType(TYPE_ARROWHEAD);
		return shape;
	}

	private static DiagramShape createWestArrowhead(TextGrid grid, TextGrid.Cell cell, int cellXSize, int cellYSize) {
		if(!grid.isWestArrowhead(cell)) return null;
		DiagramShape shape = new DiagramShape();
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMaxX(cell,cellXSize),
			Diagram.getCellMinY(cell,cellYSize)));
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMinX(cell,cellXSize),
			Diagram.getCellMidY(cell,cellYSize)));
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMaxX(cell,cellXSize),
			Diagram.getCellMaxY(cell,cellYSize)));
		shape.setClosed(true);
		shape.setFillColor(Color.black);
		shape.setStrokeColor(Color.black);
		shape.setType(TYPE_ARROWHEAD);
		return shape;
	}
	
	private static DiagramShape createEastArrowhead(TextGrid grid, TextGrid.Cell cell, int cellXSize, int cellYSize) {
		if(!grid.isEastArrowhead(cell)) return null;
		DiagramShape shape = new DiagramShape();
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMinX(cell,cellXSize),
			Diagram.getCellMinY(cell,cellYSize)));
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMaxX(cell,cellXSize),
			Diagram.getCellMidY(cell,cellYSize)));
		shape.addToPoints(new ShapePoint(
			Diagram.getCellMinX(cell,cellXSize),
			Diagram.getCellMaxY(cell,cellYSize)));
		shape.setClosed(true);
		shape.setFillColor(Color.black);
		shape.setStrokeColor(Color.black);
		shape.setType(TYPE_ARROWHEAD);
		return shape;
	}

	public static DiagramShape createSmallLine(TextGrid grid, TextGrid.Cell cell, int cellXSize, int cellYSize) {
		if (grid.isLine(cell)) {
			DiagramShape shape = new DiagramShape();
			if (grid.isHorizontalLine(cell)) {
				shape.addToPoints(
					new ShapePoint(
						cell.x * cellXSize,
						cell.y * cellYSize + cellYSize / 2));
				shape.addToPoints(
					new ShapePoint(
						cell.x * cellXSize + cellXSize - 1,
						cell.y * cellYSize + cellYSize / 2));
			} else if (grid.isVerticalLine(cell)) {
				shape.addToPoints(
					new ShapePoint(
						cell.x * cellXSize + cellXSize / 2,
						cell.y * cellYSize));
				shape.addToPoints(
					new ShapePoint(
						cell.x * cellXSize + cellXSize / 2,
						cell.y * cellYSize + cellYSize - 1));
			}
			
			//the -1 above, make a difference: the second point
			//should not fall into the next cell, because this
			//results in a failure of a proper end-of-line
			//plotting correction
			return shape;
		}
		return null;
	}

	public void addToPoints(ShapePoint point){
		points.add(point);
	}
	
	public Iterator getPointsIterator(){
		return points.iterator();
	}
	
	public void scale(float factor){
		Iterator it = getPointsIterator();
		while(it.hasNext()){
			ShapePoint point = (ShapePoint) it.next();
			point.x *= factor;
			point.y *= factor;
		}
	}
	
	public boolean isEmpty(){
		return points.isEmpty();
	}
	
	public boolean isFilled(){
		return (fillColor != null);
	}
	
	public void setIsNotFilled(){
		fillColor = null;
	}
	
	public boolean isPointLinesEnd(ShapePoint point){
		if(isClosed()) return false; //no line-ends in closed shapes!
		if(point == points.get(0)) return true;
		if(point == points.get(points.size() - 1)) return true;
		return false;
	}
	
	//TODO: method in development: isRectangle()
	public boolean isRectangle(){
		if(points.size() != 4) return false;
		ShapePoint p1 = (ShapePoint) points.get(0);
		ShapePoint p2 = (ShapePoint) points.get(1);
		ShapePoint p3 = (ShapePoint) points.get(2);
		ShapePoint p4 = (ShapePoint) points.get(3);
		if(p1.isInLineWith(p2) 
			&& p2.isInLineWith(p3)
			&& p3.isInLineWith(p4)
			&& p4.isInLineWith(p1)) return true;
		return false;
	}
	
	/**
	 * Crude way to determine which of the two shapes is smaller,
	 * based just on their bounding boxes. Used in markup
	 * assignment precendence.
	 * 
	 * @param other
	 * @return
	 */
	public boolean isSmallerThan(DiagramShape other){
		Rectangle bounds = getBounds();
		Rectangle otherBounds = other.getBounds();
		
		int area = bounds.height * bounds.width;
		int otherArea = otherBounds.height * otherBounds.width;
		
		if(area < otherArea) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * @return
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * @param color
	 */
	public void setFillColor(Color color) {
		fillColor = color;
	}

	/**
	 * @param color
	 */
	public void setStrokeColor(Color color) {
		strokeColor = color;
	}

	/**
	 * @return
	 */
	public boolean isClosed() {
		return isClosed;
	}

	/**
	 * @param b
	 */
	public void setClosed(boolean b) {
		isClosed = b;
	}

	public void printDebug(){
		System.out.print("DiagramShape: ");
		System.out.println(points.size()+" points");
	}

	/**
	 * @return
	 */
	public ArrayList getPoints() {
		return points;
	}

	public ShapePoint getPoint(int i) {
		return (ShapePoint) points.get(i);
	}

	public void setPoint(int i, ShapePoint point) {
		points.set(i, point);
	}


	public boolean equals(Object object){
		DiagramShape shape = null;
		if(!(object instanceof DiagramShape)) { return false; }
		else shape = (DiagramShape) object;
		if(getPoints().size() != shape.getPoints().size()) return false;
		
		if(DEBUG) System.out.println("comparing shapes:");
		
		if(DEBUG) System.out.println("points1: ");
		HashMap points1 = new HashMap();
		Iterator it = getPointsIterator(); 
		while(it.hasNext()){
			ShapePoint point = (ShapePoint) it.next(); 
			points1.put( ""+((int) point.x)+","+((int) point.y), null);
			if(DEBUG) System.out.println(((int) point.x)+", "+((int) point.y));
		}
		
		if(DEBUG) System.out.println("points2: ");
		HashMap points2 = new HashMap();
		it = shape.getPointsIterator(); 
		while(it.hasNext()){
			ShapePoint point = (ShapePoint) it.next(); 
			points2.put( ""+((int) point.x)+","+((int) point.y), null);
			if(DEBUG) System.out.println(((int) point.x)+", "+((int) point.y));
		}
		
		it = points1.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			if(!points2.containsKey(key)) {
				if (DEBUG)
					System.out.println("\tare not equal");
				return false;
			} 
		}
		if (DEBUG)
			System.out.println("\tare equal");
		return true;
	}

	public GeneralPath makeIntoPath() {
		int size = getPoints().size();
		
		if(size < 2) return null;
		
		GeneralPath path = new GeneralPath();
		ShapePoint point = (ShapePoint) getPoints().get(0);
		path.moveTo((int) point.x, (int) point.y);
		for(int i = 1; i < size; i++){
			point = (ShapePoint) getPoints().get(i);
			path.lineTo((int) point.x, (int) point.y);
		}
		if(isClosed() && size > 2){
			path.closePath();
		}
		return path;
	}

	public GeneralPath makeMarkerPath(Diagram diagram){
		if(points.size() != 1) return null;
		ShapePoint center = (ShapePoint) this.getPoint(0);
		float diameter =
			(float) 0.7 * Math.min(diagram.getCellWidth(), diagram.getCellHeight());
		return new GeneralPath(new Ellipse2D.Float(
			center.x - diameter/2,
			center.y - diameter/2,
			diameter,
			diameter));
	}

	public Rectangle getBounds(){
		Rectangle bounds = makeIntoPath().getBounds();
		return bounds;
	}
	
	public GeneralPath makeIntoRenderPath(Diagram diagram) {
		int size = getPoints().size();
		
		if(getType() == TYPE_POINT_MARKER){
			return makeMarkerPath(diagram);
		}
		
		if(getType() == TYPE_DOCUMENT && points.size() == 4){
			return makeDocumentPath(diagram);
		}

		if(getType() == TYPE_STORAGE && points.size() == 4){
			return makeStoragePath(diagram);
		}

		if(getType() == TYPE_IO && points.size() == 4){
			return makeIOPath(diagram);
		}

		if(getType() == TYPE_DECISION && points.size() == 4){
			return makeDecisionPath(diagram);
		}

		if(getType() == TYPE_MANUAL_OPERATION && points.size() == 4){
			return makeTrapezoidPath(diagram, true);
		}

		if(getType() == TYPE_TRAPEZOID && points.size() == 4){
			return makeTrapezoidPath(diagram, false);
		}

		if(getType() == TYPE_ELLIPSE && points.size() == 4){
			return makeEllipsePath(diagram);
		}

		if(size < 2) return null;

		GeneralPath path = new GeneralPath();
		ShapePoint point = (ShapePoint) getPoints().get(0);
		TextGrid.Cell cell = diagram.getCellFor(point);
		//path.moveTo((int) point.x, (int) point.y);
		ShapePoint previous = (ShapePoint) getPoints().get(size - 1);
		ShapePoint next = (ShapePoint) getPoints().get(1);
		ShapePoint entryPoint;
		ShapePoint exitPoint;
		
		if(point.getType() == ShapePoint.TYPE_NORMAL){
			//if(isClosed()){
				path.moveTo((int) point.x, (int) point.y);
			/*} else {
				ShapePoint projectionPoint = getCellEdgeProjectionPointBetween(point, next, diagram);
				path.moveTo((int) projectionPoint.x, (int) projectionPoint.y);
			}*/
		} else if(point.getType() == ShapePoint.TYPE_ROUND){
			entryPoint = getCellEdgePointBetween(point, previous, diagram);
			exitPoint = getCellEdgePointBetween(point, next, diagram);
			path.moveTo(entryPoint.x, entryPoint.y);
			path.quadTo(point.x, point.y, exitPoint.x, exitPoint.y);			
		}

		for(int i = 1; i < size; i++){
			previous = point;
			point = (ShapePoint) getPoints().get(i);
			if(i < size - 1)
				next = (ShapePoint) getPoints().get(i + 1);
			else next = (ShapePoint) getPoints().get(0);

			cell = diagram.getCellFor(point);

			if(point.getType() == ShapePoint.TYPE_NORMAL)
				//if(!isPointLinesEnd(point))
					path.lineTo((int) point.x, (int) point.y);
				/*else { //it is line's end, so we plot it at the projected intersection of the line with the cell's edge
					ShapePoint projectionPoint = getCellEdgeProjectionPointBetween(point, previous, diagram);
					path.lineTo((int) projectionPoint.x, (int) projectionPoint.y);
				}*/
			else if(point.getType() == ShapePoint.TYPE_ROUND){
				entryPoint = getCellEdgePointBetween(point, previous, diagram);
				exitPoint = getCellEdgePointBetween(point, next, diagram);

				path.lineTo(entryPoint.x, entryPoint.y);
				path.quadTo(point.x, point.y, exitPoint.x, exitPoint.y);
				//if(!isPointLinesEnd(next)){
					if(next.getType() == ShapePoint.TYPE_NORMAL)
						path.lineTo(next.x, next.y);
					else if(next.getType() == ShapePoint.TYPE_ROUND){
						entryPoint = getCellEdgePointBetween(next, point, diagram);
						path.lineTo(entryPoint.x, entryPoint.y);					
					}
				/*} else {
					entryPoint = getCellEdgeProjectionPointBetween(next, point, diagram);
					path.lineTo(entryPoint.x, entryPoint.y);										
				}*/
			} 
		}
		//TODO: this shouldn't be needed, but it is!
		if(isClosed() && size > 2){
			path.closePath();
		}
		return path;
	}
	
	public ArrayList getEdges(){
		ArrayList edges = new ArrayList();
		if(this.points.size() == 1) return edges;
		int noOfPoints = points.size();
		for(int i = 0; i < noOfPoints - 1; i++){
			ShapePoint startPoint = (ShapePoint) points.get(i);
			ShapePoint endPoint = (ShapePoint) points.get(i + 1);
			ShapeEdge edge = new ShapeEdge(startPoint, endPoint, this);
			edges.add(edge);
		}
		//if it is closed return edge that connects the
		//last point to the first
		if(this.isClosed()){
			ShapePoint firstPoint = (ShapePoint) points.get(0);
			ShapePoint lastPoint = (ShapePoint) points.get(points.size() - 1);
			ShapeEdge edge = new ShapeEdge(lastPoint, firstPoint, this);
			edges.add(edge);
		}
		return edges;
	}

	/**
	 * Finds the point that represents the intersection between the cell edge
	 * that contains pointInCell and the line connecting pointInCell and
	 * otherPoint.
	 * 
  	 * Returns C, if A is point in cell and B is otherPoint:
	 * <pre>
	 *     Cell
	 *    +-----+
	 *    |  A  |C                 B
	 *    |  *--*------------------*
	 *    |     |
	 *    +-----+
	 *</pre>
	 *
	 * @param pointInCell
	 * @param otherPoint
	 * @return
	 */
	public ShapePoint getCellEdgePointBetween(ShapePoint pointInCell, ShapePoint otherPoint, Diagram diagram){
		if(pointInCell == null || otherPoint == null || diagram == null)
			throw new IllegalArgumentException("None of the parameters can be null");
		if(pointInCell.equals(otherPoint))
			throw new IllegalArgumentException("The two points cannot be the same");

		ShapePoint result = null;
		TextGrid.Cell cell = diagram.getCellFor(pointInCell);
		
		if(cell == null)
			throw new RuntimeException("Upexpected error, cannot find cell corresponding to point "+pointInCell+" for diagram "+diagram);
		
		if(otherPoint.isNorthOf(pointInCell))
			result = new ShapePoint(pointInCell.x,
										diagram.getCellMinY(cell));
		else if(otherPoint.isSouthOf(pointInCell))
			result = new ShapePoint(pointInCell.x,
										diagram.getCellMaxY(cell));
		else if(otherPoint.isWestOf(pointInCell))
			result = new ShapePoint(diagram.getCellMinX(cell),
										pointInCell.y);
		else if(otherPoint.isEastOf(pointInCell))
			result = new ShapePoint(diagram.getCellMaxX(cell),
										pointInCell.y);
		
		if(result == null)
			throw new RuntimeException("Upexpected error, cannot find cell edge point for points "+pointInCell+" and "+otherPoint+" for diagram "+diagram);

		
		return result;
	}


	/**
	 * 
	 * Returns C, if A is point in cell and B is otherPoint:
	 * 
	 * <pre>
	 *     Cell
	 *    +-----+
	 *    |  A  |                  B
	 *  C *--*--+------------------*
	 *    |     |
	 *    +-----+
	 * </pre>
	 * 
	 * @param pointInCell
	 * @param otherPoint
	 * @param diagram
	 * @return
	 */

	public ShapePoint getCellEdgeProjectionPointBetween(ShapePoint pointInCell, ShapePoint otherPoint, Diagram diagram){
		if(pointInCell == null || otherPoint == null || diagram == null)
			throw new IllegalArgumentException("None of the parameters can be null");
		if(pointInCell.equals(otherPoint))
			throw new IllegalArgumentException("The two points cannot be the same: "+pointInCell+" and "+otherPoint+" passed");

		ShapePoint result = null;
		TextGrid.Cell cell = diagram.getCellFor(pointInCell);
		
		if(cell == null)
			throw new RuntimeException("Upexpected error, cannot find cell corresponding to point "+pointInCell+" for diagram "+diagram);
		
		if(otherPoint.isNorthOf(pointInCell))
			result = new ShapePoint(pointInCell.x,
										diagram.getCellMaxY(cell));
		else if(otherPoint.isSouthOf(pointInCell))
			result = new ShapePoint(pointInCell.x,
										diagram.getCellMinY(cell));
		else if(otherPoint.isWestOf(pointInCell))
			result = new ShapePoint(diagram.getCellMaxX(cell),
										pointInCell.y);
		else if(otherPoint.isEastOf(pointInCell))
			result = new ShapePoint(diagram.getCellMinX(cell),
										pointInCell.y);
		
		if(result == null)
			throw new RuntimeException("Upexpected error, cannot find cell edge point for points "+pointInCell+" and "+otherPoint+" for diagram "+diagram);

		
		return result;
	}

	public boolean contains(ShapePoint point){
		GeneralPath path = makeIntoPath();
		if(path != null) return path.contains(point);
		return false;
	}

	public boolean contains(Rectangle2D rect){
		GeneralPath path = makeIntoPath();
		if(path != null) return path.contains(rect);
		return false;
	}

	public boolean intersects(Rectangle2D rect){
		GeneralPath path = makeIntoPath();
		if(path != null) return path.intersects(rect);
		return false;
	}
	
	public boolean dropsShadow(){
		return (isClosed()
				&& getType() != DiagramShape.TYPE_ARROWHEAD
				&& getType() != DiagramShape.TYPE_POINT_MARKER
				&& !isStrokeDashed());
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
	
	public void moveEndsToCellEdges(TextGrid grid, Diagram diagram){
		if(isClosed()) return;
		
		ShapePoint linesEnd = (ShapePoint) points.get(0);
		ShapePoint nextPoint = (ShapePoint) points.get(1);

		ShapePoint projectionPoint = getCellEdgeProjectionPointBetween(linesEnd, nextPoint, diagram);
		
		linesEnd.moveTo(projectionPoint);

		linesEnd = (ShapePoint) points.get(points.size() - 1);
		nextPoint = (ShapePoint) points.get(points.size() - 2);

		projectionPoint = getCellEdgeProjectionPointBetween(linesEnd, nextPoint, diagram);
		
		linesEnd.moveTo(projectionPoint);
	}
	
	public void connectEndsToAnchors(TextGrid grid, Diagram diagram){
		if(isClosed()) return;

		ShapePoint linesEnd;
		ShapePoint nextPoint;

		linesEnd = (ShapePoint) points.get(0);
		nextPoint = (ShapePoint) points.get(1);
		
		connectEndToAnchors(grid, diagram, nextPoint, linesEnd);
		
		linesEnd = (ShapePoint) points.get(points.size() - 1);
		nextPoint = (ShapePoint) points.get(points.size() - 2);
		
		connectEndToAnchors(grid, diagram, nextPoint, linesEnd);
		
	}
	
	
	//TODO: improve connect Ends To Arrowheads to take direction into account
	private void connectEndToAnchors(
			TextGrid grid,
			Diagram diagram,
			ShapePoint nextPoint,
			ShapePoint linesEnd){

		if(isClosed()) return;

		TextGrid.Cell anchorCell;
		anchorCell = getPossibleAnchorCell(linesEnd, nextPoint, diagram);
		
		if(grid.isArrowhead(anchorCell)){
			linesEnd.x = diagram.getCellMidX(anchorCell);
			linesEnd.y = diagram.getCellMidY(anchorCell);
			linesEnd.setLocked(true);
		} else if (grid.isCorner(anchorCell) || grid.isIntersection(anchorCell)){
			linesEnd.x = diagram.getCellMidX(anchorCell);
			linesEnd.y = diagram.getCellMidY(anchorCell);
			linesEnd.setLocked(true);
		}
	}

	/**
	 * Given the end of a line, the next point and a Diagram, it
	 * returns the cell that may contain intersections or arrowheads
	 * to which the line's end should be connected
	 * 
	 * @param linesEnd
	 * @param nextPoint
	 * @param diagram
	 * @return
	 */
	private static TextGrid.Cell getPossibleAnchorCell(
			ShapePoint linesEnd,
			ShapePoint nextPoint,
			Diagram diagram
			){
		ShapePoint cellPoint = null;

		if(nextPoint.isNorthOf(linesEnd))
			cellPoint = new ShapePoint(linesEnd.x, linesEnd.y + diagram.getCellHeight());
		if(nextPoint.isSouthOf(linesEnd))
			cellPoint = new ShapePoint(linesEnd.x, linesEnd.y - diagram.getCellHeight());
		if(nextPoint.isWestOf(linesEnd))
			cellPoint = new ShapePoint(linesEnd.x + diagram.getCellWidth(), linesEnd.y);
		if(nextPoint.isEastOf(linesEnd))
			cellPoint = new ShapePoint(linesEnd.x - diagram.getCellWidth(), linesEnd.y);

		return diagram.getCellFor(cellPoint);
	}

	
	public String toString(){
		String s = "DiagramShape, "+points.size()+" points: ";
		Iterator it = getPointsIterator();
		while(it.hasNext()){
			ShapePoint point = (ShapePoint) it.next(); 
			s += point;
			if(it.hasNext()) s += " "; 
		}
		return s;
	}

	/**
	 * @return
	 */
	public boolean isStrokeDashed() {
		return isStrokeDashed;
	}

	/**
	 * @param b
	 */
	public void setStrokeDashed(boolean b) {
		isStrokeDashed = b;
	}

	private GeneralPath makeStoragePath(Diagram diagram) {
		if(points.size() != 4) return null;
		Rectangle bounds = makeIntoPath().getBounds();
		ShapePoint point1 = new ShapePoint((float)bounds.getMinX(), (float)bounds.getMinY());
		ShapePoint point2 = new ShapePoint((float)bounds.getMaxX(), (float)bounds.getMinY());
		ShapePoint point3 = new ShapePoint((float)bounds.getMaxX(), (float)bounds.getMaxY());
		ShapePoint point4 = new ShapePoint((float)bounds.getMinX(), (float)bounds.getMaxY());
		
		ShapePoint pointMidTop = new ShapePoint((float)bounds.getCenterX(), (float)bounds.getMinY());
		ShapePoint pointMidBottom = new ShapePoint((float)bounds.getCenterX(), (float)bounds.getMaxY());
	
		float diameterX = bounds.width;
		float diameterY = 0.75f * diagram.getCellHeight();
	
		//control point offset X, and Y
		float cpOffsetX = bounds.width / 6;
		float cpOffsetYTop = diagram.getCellHeight() / 2;
		float cpOffsetYBottom = 10 * diagram.getCellHeight() / 14;
		//float cpOffsetYBottom = cpOffsetYTop; 
	
		GeneralPath path = new GeneralPath();
	
		//top of cylinder
		path.moveTo(point1.x, point1.y);
		path.curveTo(
			point1.x + cpOffsetX, point1.y + cpOffsetYTop,
			point2.x - cpOffsetX, point2.y + cpOffsetYTop,
			point2.x, point2.y
			);
		path.curveTo(
			point2.x - cpOffsetX, point2.y - cpOffsetYTop,
			point1.x + cpOffsetX, point1.y - cpOffsetYTop,
			point1.x, point1.y
			);
	
		//side of cylinder
		path.moveTo(point1.x, point1.y);
		path.lineTo(point4.x, point4.y);
		
		path.curveTo(
			point4.x + cpOffsetX, point4.y + cpOffsetYBottom,
			point3.x - cpOffsetX, point3.y + cpOffsetYBottom,
			point3.x, point3.y
			);
	
		path.lineTo(point2.x, point2.y);		
		
		return path;
	}

	private GeneralPath makeDocumentPath(Diagram diagram) {
		if(points.size() != 4) return null;
		Rectangle bounds = makeIntoPath().getBounds();
		ShapePoint point1 = new ShapePoint((float)bounds.getMinX(), (float)bounds.getMinY());
		ShapePoint point2 = new ShapePoint((float)bounds.getMaxX(), (float)bounds.getMinY());
		ShapePoint point3 = new ShapePoint((float)bounds.getMaxX(), (float)bounds.getMaxY());
		ShapePoint point4 = new ShapePoint((float)bounds.getMinX(), (float)bounds.getMaxY());
		
		ShapePoint pointMid = new ShapePoint((float)bounds.getCenterX(), (float)bounds.getMaxY());
		
		GeneralPath path = new GeneralPath();
		path.moveTo(point1.x, point1.y);
		path.lineTo(point2.x, point2.y);
		path.lineTo(point3.x, point3.y);
	
		//int controlDX = diagram.getCellWidth();
		//int controlDY = diagram.getCellHeight() / 2;
		
		int controlDX = bounds.width / 6;
		int controlDY = bounds.height / 8;
		
		path.quadTo(pointMid.x + controlDX, pointMid.y - controlDY, pointMid.x, pointMid.y);
		path.quadTo(pointMid.x - controlDX, pointMid.y + controlDY, point4.x, point4.y);
		path.closePath();
		
		return path;
	}

	// to draw a circle with 4 Bezier curves, set the control points at this ratio of
	// the radius above & below the side points
	// thanks to G. Adam Stanislav, http://whizkidtech.redprince.net/bezier/circle/
	private static final float KAPPA = 4f * ((float) Math.sqrt(2) - 1) / 3f;

	private GeneralPath makeEllipsePath(Diagram diagram) {
		if(points.size() != 4) return null;
		Rectangle bounds = makeIntoPath().getBounds();
		float xOff = (float) bounds.getWidth() * 0.5f * KAPPA;
		float yOff = (float) bounds.getHeight() * 0.5f * KAPPA;
		ShapePoint pointMid = new ShapePoint((float)bounds.getCenterX(), (float)bounds.getCenterY());

		ShapePoint left = new ShapePoint((float)bounds.getMinX(), (float)pointMid.getY());
		ShapePoint right = new ShapePoint((float)bounds.getMaxX(), (float)pointMid.getY());
		ShapePoint top = new ShapePoint((float)pointMid.getX(), (float)bounds.getMinY());
		ShapePoint bottom = new ShapePoint((float)pointMid.getX(), (float)bounds.getMaxY());

		GeneralPath path = new GeneralPath();
		path.moveTo(top.x, top.y);
		path.curveTo(top.x + xOff, top.y, right.x, right.y - yOff, right.x, right.y);
		path.curveTo(right.x, right.y + yOff, bottom.x + xOff, bottom.y, bottom.x, bottom.y);
		path.curveTo(bottom.x - xOff, bottom.y, left.x, left.y + yOff, left.x, left.y);
		path.curveTo(left.x, left.y - yOff, top.x - xOff, top.y, top.x, top.y);
		path.closePath();

		return path;
	}

	private GeneralPath makeTrapezoidPath(Diagram diagram, boolean inverted) {
		if(points.size() != 4) return null;
		Rectangle bounds = makeIntoPath().getBounds();
        float offset = 0.7f * diagram.getCellWidth(); // fixed slope
        if (inverted) offset = -offset;
		ShapePoint ul = new ShapePoint((float)bounds.getMinX() + offset, (float)bounds.getMinY());
		ShapePoint ur = new ShapePoint((float)bounds.getMaxX() - offset, (float)bounds.getMinY());
		ShapePoint br = new ShapePoint((float)bounds.getMaxX() + offset, (float)bounds.getMaxY());
		ShapePoint bl = new ShapePoint((float)bounds.getMinX() - offset, (float)bounds.getMaxY());

		ShapePoint pointMid = new ShapePoint((float)bounds.getCenterX(), (float)bounds.getMaxY());

		GeneralPath path = new GeneralPath();
		path.moveTo(ul.x, ul.y);
		path.lineTo(ur.x, ur.y);
		path.lineTo(br.x, br.y);
		path.lineTo(bl.x, bl.y);
		path.closePath();

		return path;
	}

	private GeneralPath makeDecisionPath(Diagram diagram) {
		if(points.size() != 4) return null;
		Rectangle bounds = makeIntoPath().getBounds();
        ShapePoint pointMid = new ShapePoint((float)bounds.getCenterX(), (float)bounds.getCenterY());
		ShapePoint left = new ShapePoint((float)bounds.getMinX(), (float)pointMid.getY());
		ShapePoint right = new ShapePoint((float)bounds.getMaxX(), (float)pointMid.getY());
		ShapePoint top = new ShapePoint((float)pointMid.getX(), (float)bounds.getMinY());
		ShapePoint bottom = new ShapePoint((float)pointMid.getX(), (float)bounds.getMaxY());

		GeneralPath path = new GeneralPath();
		path.moveTo(left.x, left.y);
		path.lineTo(top.x, top.y);
		path.lineTo(right.x, right.y);
		path.lineTo(bottom.x, bottom.y);

		path.closePath();

		return path;
	}

	private GeneralPath makeIOPath(Diagram diagram) {
		if(points.size() != 4) return null;
		Rectangle bounds = makeIntoPath().getBounds();
		ShapePoint point1 = new ShapePoint((float)bounds.getMinX(), (float)bounds.getMinY());
		ShapePoint point2 = new ShapePoint((float)bounds.getMaxX(), (float)bounds.getMinY());
		ShapePoint point3 = new ShapePoint((float)bounds.getMaxX(), (float)bounds.getMaxY());
		ShapePoint point4 = new ShapePoint((float)bounds.getMinX(), (float)bounds.getMaxY());
	
		float offset = diagram.getCellWidth() / 2;
		
		GeneralPath path = new GeneralPath();
		path.moveTo(point1.x + offset, point1.y);
		path.lineTo(point2.x + offset, point2.y);
		path.lineTo(point3.x - offset, point3.y);
		path.lineTo(point4.x - offset, point4.y);
		path.closePath();
		
		return path;
	}

	public CustomShapeDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(CustomShapeDefinition definition) {
		this.definition = definition;
	}

}
