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

import org.stathissideris.ascii2image.text.CellSet;
import org.stathissideris.ascii2image.text.TextGrid;

/**
 * 
 * @author Efstathios Sideris
 */
public abstract class DiagramComponent {
	
	private static final boolean DEBUG = false;
	
	protected static ShapePoint makePointForCell(TextGrid.Cell cell, TextGrid grid, int cellWidth, int cellHeight, boolean allRound){
		if (DEBUG)
			System.out.println("Found point at cell "+cell);
		if(grid.isCorner(cell) && allRound){
			return new ShapePoint(
							cell.x * cellWidth + cellWidth/2,
							cell.y * cellHeight + cellHeight/2,
							ShapePoint.TYPE_ROUND  
						);
		} else if(grid.isNormalCorner(cell)){
			return new ShapePoint(
							cell.x * cellWidth + cellWidth/2,
							cell.y * cellHeight + cellHeight/2,
							ShapePoint.TYPE_NORMAL  
						);
		} else if(grid.isRoundCorner(cell)){
			return new ShapePoint(
							cell.x * cellWidth + cellWidth/2,
							cell.y * cellHeight + cellHeight/2,
							ShapePoint.TYPE_ROUND  
						);
		} else if(grid.isLinesEnd(cell)){
			return new ShapePoint(
						cell.x * cellWidth + cellWidth/2,
						cell.y * cellHeight + cellHeight/2,
						ShapePoint.TYPE_NORMAL  
					);
		} else if(grid.isIntersection(cell)){
			return new ShapePoint(
					cell.x * cellWidth + cellWidth/2,
					cell.y * cellHeight + cellHeight/2,
					ShapePoint.TYPE_NORMAL 
				);
		}
		throw new RuntimeException("Cannot make point for cell "+cell);
	}

	public static DiagramComponent createClosedFromBoundaryCells(TextGrid grid, CellSet cells, int cellWidth, int cellHeight){
		return createClosedFromBoundaryCells(grid, cells, cellWidth, cellHeight, false);
	}

	public static DiagramComponent createClosedFromBoundaryCells(TextGrid grid, CellSet cells, int cellWidth, int cellHeight, boolean allRound){
		if(cells.getType(grid) == CellSet.TYPE_OPEN) throw new IllegalArgumentException("CellSet is closed and cannot be handled by this method");
		if(cells.size() < 2) return null;
		
		DiagramShape shape = new DiagramShape();
		shape.setClosed(true);
		if(grid.containsAtLeastOneDashedLine(cells)) shape.setStrokeDashed(true);
		
		TextGrid workGrid = new TextGrid(grid.getWidth(), grid.getHeight());
		grid.copyCellsTo(cells, workGrid);
		
		if (DEBUG){
			System.out.println("Making closed shape from buffer:");
			workGrid.printDebug();
		}
		
		TextGrid.Cell start = (TextGrid.Cell) cells.getFirst();
		if(workGrid.isCorner(start)) shape.addToPoints(makePointForCell(start, workGrid, cellWidth, cellHeight, allRound));
		TextGrid.Cell previous = start;
		TextGrid.Cell cell = null;
		CellSet nextCells = workGrid.followCell(previous);
		if(nextCells.size() == 0) return null;
		cell = (TextGrid.Cell) nextCells.getFirst();
		if(workGrid.isCorner(cell)) shape.addToPoints(makePointForCell(cell, workGrid, cellWidth, cellHeight, allRound));
		
		while(!cell.equals(start)){
			nextCells = workGrid.followCell(cell, previous);
			if(nextCells.size() == 1) {
				previous = cell;
				cell = (TextGrid.Cell) nextCells.getFirst();
				if(!cell.equals(start) && workGrid.isCorner(cell))
					shape.addToPoints(makePointForCell(cell, workGrid, cellWidth, cellHeight, allRound));
			} else if(nextCells.size() > 1) {
				return null;
			} else {
				throw new RuntimeException("cannot create closed shape from boundary cells, nowhere to go from "
						+ cell + " coming from " + previous + " in grid:\n" + grid
						+"\nmaybe you have an edge pointing nowhere?");
			}
		}
		
		return shape;
		
	}
}
