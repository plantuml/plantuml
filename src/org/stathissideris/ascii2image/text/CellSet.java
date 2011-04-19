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
package org.stathissideris.ascii2image.text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Efstathios Sideris
 */
public class CellSet implements Iterable<TextGrid.Cell> {

	private static final boolean DEBUG = false;
	private static final boolean VERBOSE_DEBUG = false;
	
	public static final int TYPE_CLOSED = 0;
	public static final int TYPE_OPEN = 1;
	public static final int TYPE_MIXED = 2;
	public static final int TYPE_HAS_CLOSED_AREA = 3;
	public static final int TYPE_UNDETERMINED = 4;

	Set<TextGrid.Cell> internalSet = new HashSet<TextGrid.Cell>();
	
	private int type = TYPE_UNDETERMINED;
	private boolean typeIsValid = false;

	private static final Object FAKE = new Object();
	
	public CellSet(){
		
	}
	
	public CellSet(CellSet other){
		addAll(other);
	}
	
	public Iterator<TextGrid.Cell> iterator(){
		return internalSet.iterator();
	}

	public Object add(TextGrid.Cell cell){
		return internalSet.add(cell);
	}

	public void addAll(CellSet set){
		internalSet.addAll(set.internalSet);
	}
	
	void clear(){
		internalSet.clear();
	}
	
	public int size() {
		return internalSet.size();
	}
	
	public TextGrid.Cell getFirst(){
		//return internalSet.get(0);
		return (TextGrid.Cell) internalSet.iterator().next();
	}
	
	public void printAsGrid(){
		TextGrid grid = new TextGrid(getMaxX()+2, getMaxY()+2);
		grid.fillCellsWith(this, '*');
		grid.printDebug();
	}

	public void printDebug(){
		Iterator<TextGrid.Cell> it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = it.next();
			System.out.print(cell);
			if(it.hasNext()) System.out.print(" ");
		}
		System.out.println();
	}

	public String getCellsAsString(){
		StringBuffer str = new StringBuffer();
		Iterator<TextGrid.Cell> it = iterator();
		while(it.hasNext()){
			str.append(it.next().toString());
			if(it.hasNext()) str.append("/");
		}
		return str.toString();
	}
	
	public String toString(){
		TextGrid grid = new TextGrid(getMaxX()+2, getMaxY()+2);
		grid.fillCellsWith(this, '*');
		return grid.getDebugString();		
	}
	
	/**
	 * Deep copy
	 * 
	 * @param set
	 * @return
	 */
	public static CellSet copyCellSet(CellSet set) {
		TextGrid grid = new TextGrid();
		CellSet newSet = new CellSet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			TextGrid.Cell newCell = grid.new Cell(cell);
			newSet.add(newCell);
		}
		return newSet;
	}

	/*public BoundarySet(BoundarySet set) {
		Iterator it = set.iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			add(new TextGrid.Cell(cell));
		}
	}*/

	public int getType(TextGrid grid) {
		if(typeIsValid) return type;
		typeIsValid = true;
		if(size() == 1) {
			type = TYPE_OPEN;
			return TYPE_OPEN;
		} 
		int typeTrace = getTypeAccordingToTraceMethod(grid);

		if(DEBUG){
			System.out.println("trace: "+typeTrace);
		}

		if(typeTrace == TYPE_OPEN) {
			type = TYPE_OPEN;
			return TYPE_OPEN;
		} 
		if(typeTrace == TYPE_CLOSED) {
			type = TYPE_CLOSED;
			return TYPE_CLOSED;
		} 

		if(typeTrace == TYPE_UNDETERMINED) {
			int typeFill = getTypeAccordingToFillMethod(grid);
			if(typeFill == TYPE_HAS_CLOSED_AREA){
				type = TYPE_MIXED;
				return TYPE_MIXED;	
			} else if(typeFill == TYPE_OPEN){
				type = TYPE_OPEN;
				return TYPE_OPEN;
			}
		}
		
		//in the case that both return undetermined:
		type = TYPE_UNDETERMINED;
		return TYPE_UNDETERMINED;
	}

	private int getTypeAccordingToTraceMethod(TextGrid grid) {
		if(size() < 2) return TYPE_OPEN;
		
		TextGrid workGrid = TextGrid.makeSameSizeAs(grid);
		grid.copyCellsTo(this, workGrid);

		//start with a line end if it exists or with a "random" cell if not
		TextGrid.Cell start = null;
		for(TextGrid.Cell cell : this)
			if(workGrid.isLinesEnd(cell))
				start = cell;
		if(start == null) start = (TextGrid.Cell) getFirst();
		
		if (DEBUG)
			System.out.println("Tracing:\nStarting at "+start+" ("+grid.getCellTypeAsString(start)+")");
		TextGrid.Cell previous = start;
		TextGrid.Cell cell = null;
		CellSet nextCells = workGrid.followCell(previous);
		if(nextCells.size() == 0) return TYPE_OPEN;
		cell = (TextGrid.Cell) nextCells.getFirst();
		if (DEBUG)
			System.out.println("\tat cell "+cell+" ("+grid.getCellTypeAsString(cell)+")");

		
		while(!cell.equals(start)){
			nextCells = workGrid.followCell(cell, previous);
			if(nextCells.size() == 0) {
				if (DEBUG)
					System.out.println("-> Found dead-end, shape is open");
				return TYPE_OPEN;
			} if(nextCells.size() == 1) {
				previous = cell;
				cell = (TextGrid.Cell) nextCells.getFirst();
				if (DEBUG)
					System.out.println("\tat cell "+cell+" ("+grid.getCellTypeAsString(cell)+")");
			} else if(nextCells.size() > 1) {
				if (DEBUG)
					System.out.println("-> Found intersection at cell "+cell);
				return TYPE_UNDETERMINED;
			}
		}
		if (DEBUG)
			System.out.println("-> Arrived back to start, shape is closed");
		return TYPE_CLOSED;
		
//		boolean hasMoved = false;
//		
//		CellSet workSet;
//		workSet = new CellSet(this);
//
//		TextGrid.Cell start = (TextGrid.Cell) get(0);
//		
//		workSet.remove(start);
//		TextGrid.Cell cell = workSet.findCellNextTo(start);
//		
//		while(true && cell != null){
//			
//			hasMoved = true;
//			workSet.remove(cell);
//			
//			CellSet setOfNeighbours = workSet.findCellsNextTo(cell);
//			
//			if(setOfNeighbours.isEmpty()) break;
//
//			TextGrid.Cell c = null;
//			if(setOfNeighbours.size() == 1) c = (TextGrid.Cell) setOfNeighbours.get(0);
//			if(setOfNeighbours.size() > 1) return TYPE_UNDETERMINED;
//			if(c == null) break;
//			else cell = c;
//		}
//		if(cell != null && start.isNextTo(cell) && hasMoved) return TYPE_CLOSED;
//		else return TYPE_OPEN;
	}

	private int getTypeAccordingToFillMethod(TextGrid grid){
		if(size() == 0) return TYPE_OPEN;
		
		CellSet tempSet = copyCellSet(this);
		tempSet.translate( -this.getMinX() + 1, -this.getMinY() + 1);
		TextGrid subGrid = grid.getSubGrid(getMinX() - 1, getMinY() - 1, getWidth() + 3, getHeight() + 3);
		AbstractionGrid abstraction = new AbstractionGrid(subGrid, tempSet);
		TextGrid temp = abstraction.getCopyOfInternalBuffer();

		TextGrid.Cell cell1 = null;
		TextGrid.Cell cell2 = null;

		int width = temp.getWidth();
		int height = temp.getHeight();
		
		TextGrid.Cell fillCell = null;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				TextGrid.Cell cCell = temp.new Cell(x, y);
				if(temp.isBlank(cCell)){
					fillCell = cCell;
					break;
				}
			}
		}
		
		if(fillCell == null){
			System.err.println("Unexpected error: fill method cannot fill anywhere");
			return TYPE_UNDETERMINED;
		}
		
		temp.fillContinuousArea(fillCell, '*');
		if(VERBOSE_DEBUG) {System.out.println("Buffer after filling:"); temp.printDebug();}
		
		if(temp.hasBlankCells()) return TYPE_HAS_CLOSED_AREA;
		else return TYPE_OPEN;
	}

	public void translate(int dx, int dy){
		typeIsValid = false;
		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cCell = (TextGrid.Cell) it.next();
			cCell.x += dx;
			cCell.y += dy;
		}	
	}

	public TextGrid.Cell find(TextGrid.Cell cell){
		Iterator<TextGrid.Cell> it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cCell = it.next();
			if(cCell.equals(cell)) return cCell;
		}
		return null;		
	}

	public boolean contains(TextGrid.Cell cell){
		if(cell == null) return false;
		return internalSet.contains(cell);
	}
	
//	public boolean contains(TextGrid.Cell cell){
//		Iterator<TextGrid.Cell> it = iterator();
//		while(it.hasNext()){
//			TextGrid.Cell cCell = it.next();
//			if(cCell.equals(cell)) return true;
//		}
//		return false;		
//	}

	public void addSet(CellSet set){
		typeIsValid = false;
		this.addAll(set);
	}

	public boolean hasCommonCells(CellSet otherSet){
		Iterator<TextGrid.Cell> it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = it.next();
			if(otherSet.contains(cell)) return true;
		}
		return false;
	}

	public TextGrid.Cell find(int x, int y){
		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cCell = (TextGrid.Cell) it.next();
			if(cCell.x == x && cCell.y == y) return cCell;
		}
		return null;		
	}
	
	public CellSet getFilledEquivalent(TextGrid textGrid){
		if(this.getType(textGrid) == CellSet.TYPE_OPEN) return new CellSet(this);
		TextGrid grid = new TextGrid(getMaxX()+2, getMaxY()+2);
		grid.fillCellsWith(this, '*');
		
		//find a cell that has a blank both on the east and the west
		TextGrid.Cell cell = null;
		boolean finished = false;
		for(int y = 0; y < grid.getHeight() && !finished; y++){
			for(int x = 0; x < grid.getWidth() && !finished; x++){
				cell = grid.new Cell(x, y);
				if(!grid.isBlank(cell)
					 && grid.isBlank(cell.getEast())
					 && grid.isBlank(cell.getWest())){
					finished = true;
				}
			}
		}
		if(cell != null){
			cell = cell.getEast();
			if(grid.isOutOfBounds(cell)) return new CellSet(this);
			grid.fillContinuousArea(cell, '*');
			return grid.getAllNonBlank();
		}
		System.err.println("Unexpected error, cannot find the filled equivalent of CellSet");
		return null;
	}
	
	/**
	 * Returns the first cell that is found to be next to <code>cell</code>.
	 * 
	 * @param cell
	 * @return
	 */
	public TextGrid.Cell findCellNextTo(TextGrid.Cell cell){
		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cCell = (TextGrid.Cell) it.next();
			if(cCell.isNextTo(cell)) return cCell;
		}
		return null;
	}

	/**
	 * Returns all the cells that are found to be next to <code>cell</code>.
	 * 
	 * @param cell
	 * @return
	 */
	public CellSet findCellsNextTo(TextGrid.Cell cell){
		if(cell == null) throw new IllegalArgumentException("cell cannot be null");
		CellSet set = new CellSet();
		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cCell = (TextGrid.Cell) it.next();
			if(cCell.isNextTo(cell)) set.add(cCell);
		}
		return set;
	}
	
	public void appendSet(CellSet set){
		typeIsValid = false;
		Iterator it = set.iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			if(find(cell) == null) add(cell);
		}	
	}
	
	public void subtractSet(CellSet set){
		typeIsValid = false;
		Iterator it = set.iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			TextGrid.Cell thisCell = find(cell);
			if(thisCell != null) remove(thisCell);
		}
	}

	public int getWidth(){
		return getMaxX() - getMinX();
	}

	
	public int getHeight(){
		return getMaxY() - getMinY();
	}
	
	public int getMaxX(){
		int result = 0;
		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			if(cell.x > result) result = cell.x;
		}
		return result;
	}

	public int getMinX(){
		int result = Integer.MAX_VALUE;
		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			if(cell.x < result) result = cell.x;
		}
		return result;
	}


	public int getMaxY(){
		int result = 0;
		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			if(cell.y > result) result = cell.y;
		}
		return result;
	}

	public int getMinY(){
		int result = Integer.MAX_VALUE;
		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			if(cell.y < result) result = cell.y;
		}
		return result;
	}


	public Object remove(TextGrid.Cell cell){
		typeIsValid = false;
		cell = find(cell);
		if(cell != null) return internalSet.remove(cell);
		else return null;
	}

	public boolean equals(Object o){
		CellSet otherSet = (CellSet) o;
		return internalSet.equals(otherSet.internalSet);
	}

	
	public static ArrayList removeDuplicateSets(ArrayList list) {
		ArrayList uniqueSets = new ArrayList();

		Iterator it = list.iterator();
		while(it.hasNext()){
			CellSet set = (CellSet) it.next();
			boolean isOriginal = true;
			Iterator uniquesIt = uniqueSets.iterator();
			while(uniquesIt.hasNext()){
				CellSet uniqueSet = (CellSet) uniquesIt.next();
				if(set.equals(uniqueSet)){
					isOriginal = false;
				}
			}
			if(isOriginal) uniqueSets.add(set);
		}
		return uniqueSets;
	}
	
	
	/**
	 * Takes into account character info from the grid
	 * 
	 * @return ArrayList of distinct BoundarySetS
	 */
	public ArrayList breakIntoDistinctBoundaries(TextGrid grid){
		ArrayList result;
		
		AbstractionGrid temp = new AbstractionGrid(grid, this);
		result = temp.getDistinctShapes();

		return result;		
	}


	/**
	 * 
	 * @return ArrayList of distinct BoundarySetS
	 */
	public ArrayList breakIntoDistinctBoundaries(){
		ArrayList result = new ArrayList();

		//CellSet tempSet = copyCellSet(this);
		//tempSet.translate( - this.getMinX() + 1, - this.getMinY() + 1);

//		TextGrid boundaryGrid = new TextGrid(tempSet.getMaxX()+2, tempSet.getMaxY()+2);
//		boundaryGrid.fillCellsWith(tempSet, '*');

		TextGrid boundaryGrid = new TextGrid(getMaxX()+2, getMaxY()+2);
		boundaryGrid.fillCellsWith(this, '*');

		
		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			if(boundaryGrid.isBlank(cell.x, cell.y)) continue;
			CellSet boundarySet = boundaryGrid.fillContinuousArea(cell.x, cell.y, ' ');
			//boundarySet.translate( this.getMinX() - 1, this.getMinY() - 1);
			result.add(boundarySet);
		}
		return result;
	}


	/**
	 * 
	 * Breaks that: 
	 * <pre>
	 *  +-----+
	 *  |     |
	 *  +  ---+-------------------
	 *  |     |
	 *  +-----+
	 * </pre>
	 * 
	 * into the following 3:
	 * 
	 * <pre>
	 *  +-----+
	 *  |     |
	 *  +     +
	 *  |     |
	 *  +-----+
	 * 
	 *     ---
	 *         -------------------
	 * </pre>
	 * 
	 * @param grid
	 * @return a list of boundaries that are either open or closed but not mixed
	 * and they are equivalent to the <code>this</code>
	 */
	public ArrayList breakTrulyMixedBoundaries(TextGrid grid){
		ArrayList result = new ArrayList();
		CellSet visitedEnds = new CellSet();
		
		TextGrid workGrid = TextGrid.makeSameSizeAs(grid);
		grid.copyCellsTo(this, workGrid);

		if (DEBUG){
			System.out.println("Breaking truly mixed boundaries below:");
			workGrid.printDebug();
		}

		Iterator it = iterator();
		while(it.hasNext()){
			TextGrid.Cell start = (TextGrid.Cell) it.next();
			if(workGrid.isLinesEnd(start) && !visitedEnds.contains(start)){
				
				if (DEBUG)
					System.out.println("Starting new subshape:");
				
				CellSet set = new CellSet();
				set.add(start);
				if(DEBUG) System.out.println("Added boundary "+start);
				
				TextGrid.Cell previous = start;
				TextGrid.Cell cell = null;
				CellSet nextCells = workGrid.followCell(previous);
				if(nextCells.size() == 0)
					throw new IllegalArgumentException("This shape is either open but multipart or has only one cell, and cannot be processed by this method");
				cell = (TextGrid.Cell) nextCells.getFirst();
				set.add(cell);
				if(DEBUG) System.out.println("Added boundary "+cell);
				
				boolean finished = false;
				if(workGrid.isLinesEnd(cell)){
					visitedEnds.add(cell);
					finished = true;					
				}
				
				while(!finished){
					nextCells = workGrid.followCell(cell, previous);
					if(nextCells.size() == 1) {
						set.add(cell);
						if(DEBUG) System.out.println("Added boundary " + cell);
						previous = cell;
						cell = (TextGrid.Cell) nextCells.getFirst();
						//if(!cell.equals(start) && grid.isPointCell(cell))
						//	s.addToPoints(makePointForCell(cell, workGrid, cellWidth, cellHeight, allRound));
						if(workGrid.isLinesEnd(cell)){
							visitedEnds.add(cell);
							finished = true;
						}
					} else if(nextCells.size() > 1) {
						finished = true;
					}
				}
				result.add(set);
			}
		}

		//substract all boundary sets from this CellSet
		CellSet whatsLeft = new CellSet(this);
		it = result.iterator();
		while (it.hasNext()) {
			CellSet set = (CellSet) it.next();
			whatsLeft.subtractSet(set);
			if(DEBUG) set.printAsGrid();
		}
		result.add(whatsLeft);
		if(DEBUG) whatsLeft.printAsGrid();
		
		return result;
	}

	
	public TextGrid makeIntoGrid(){
		TextGrid grid = new TextGrid(getMaxX()+2, getMaxY()+2);
		grid.fillCellsWith(this, '*');
		return grid;
	}
	
	public CellSet makeScaledOneThirdEquivalent(){
		TextGrid gridBig = this.makeIntoGrid();
		gridBig.fillCellsWith(this, '*');
		if (VERBOSE_DEBUG){
			System.out.println("---> making ScaledOneThirdEquivalent of:");
			gridBig.printDebug();
		}

		
		TextGrid gridSmall = new TextGrid((getMaxX() + 2) / 3, (getMaxY() + 2) / 3);
		
		
		for(int y = 0; y < gridBig.getHeight(); y++){
			for(int x = 0; x < gridBig.getWidth(); x++){
				TextGrid.Cell cell = gridBig.new Cell(x, y);
				if(!gridBig.isBlank(cell)) gridSmall.set(x/3, y/3, '*');
			}
		}
		
		if (VERBOSE_DEBUG){
			System.out.println("---> made into grid:");
			gridSmall.printDebug();
		}
		
		return gridSmall.getAllNonBlank();
	}
	
}
