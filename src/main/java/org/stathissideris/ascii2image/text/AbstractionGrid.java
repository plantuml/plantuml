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
import java.util.Iterator;

/**
 * 
 * @author Efstathios Sideris
 */
public class AbstractionGrid {

	private static final boolean DEBUG = false;
	
	private TextGrid grid;
	
	/**
	 * Makes an AbstractionGrid using <code>internalGrid</code> as
	 * its internal buffer
	 * 
	 * @param internalGrid
	 * @return
	 */
	public static AbstractionGrid makeUsingBuffer(TextGrid internalGrid){
		if(internalGrid.getWidth() % 3 != 0
			|| internalGrid.getHeight() % 3 != 0) throw new IllegalArgumentException("Passed TextGrid must have dimensions that are divisible by 3."); 
		AbstractionGrid result = new AbstractionGrid(internalGrid.getWidth() / 3, internalGrid.getHeight() / 3);
		result.setInternalBuffer(internalGrid);
		return result;
	}
	
	/**
	 * Makes an AbstractionGrid using the <code>cellSet</code>
	 * of <code>textGrid</code>.
	 * 
	 * @param textGrid
	 * @param cellSet
	 */
	public AbstractionGrid(TextGrid textGrid, CellSet cellSet){
		this(textGrid.getWidth(), textGrid.getHeight());
		/*this(cellSet.getWidth(), cellSet.getHeight());
		
		cellSet = new CellSet(cellSet);
		cellSet.translate( - cellSet.getMinX(), - cellSet.getMinY());*/
		
		if(DEBUG){
			System.out.println("Making AbstractionGrid using buffer:");
			textGrid.printDebug();
			System.out.println("...and the following CellSet:");
			cellSet.printAsGrid();
		}
		
		Iterator it = cellSet.iterator();
		while(it.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) it.next();
			if(textGrid.isBlank(cell)) continue;
			if(textGrid.isCross(cell)){
				set(cell.x, cell.y, AbstractCell.makeCross());
			} else if(textGrid.isT(cell)){
				set(cell.x, cell.y, AbstractCell.makeT());
			} else if(textGrid.isK(cell)){
				set(cell.x, cell.y, AbstractCell.makeK());
			} else if(textGrid.isInverseT(cell)){
				set(cell.x, cell.y, AbstractCell.makeInverseT());
			} else if(textGrid.isInverseK(cell)){
				set(cell.x, cell.y, AbstractCell.makeInverseK());
			} else if(textGrid.isCorner1(cell)){
				set(cell.x, cell.y, AbstractCell.makeCorner1());
			} else if(textGrid.isCorner2(cell)){
				set(cell.x, cell.y, AbstractCell.makeCorner2());
			} else if(textGrid.isCorner3(cell)){
				set(cell.x, cell.y, AbstractCell.makeCorner3());
			} else if(textGrid.isCorner4(cell)){
				set(cell.x, cell.y, AbstractCell.makeCorner4());
			} else if(textGrid.isHorizontalLine(cell)){
				set(cell.x, cell.y, AbstractCell.makeHorizontalLine());
			} else if(textGrid.isVerticalLine(cell)){
				set(cell.x, cell.y, AbstractCell.makeVerticalLine());
			} else if(textGrid.isCrossOnLine(cell)){
				set(cell.x, cell.y, AbstractCell.makeCross());
			} else if(textGrid.isStarOnLine(cell)){
				set(cell.x, cell.y, AbstractCell.makeStar());
			}
		}

		if(DEBUG){
			System.out.println("...the resulting AbstractionGrid is:");
			grid.printDebug();
		}
	}
	
	private AbstractionGrid(int width, int height){
		grid = new TextGrid(width*3, height*3);
	}
	
	public TextGrid getCopyOfInternalBuffer(){
		return new TextGrid(grid);
	}

	private void setInternalBuffer(TextGrid grid){
		this.grid = grid;
	}

	
	public int getWidth(){
		return grid.getWidth() / 3;
	}

	public int getHeight(){
		return grid.getHeight() / 3;
	}

	public TextGrid getAsTextGrid(){
		TextGrid result = new TextGrid(getWidth(), getHeight());
		for(int y = 0; y < grid.getHeight(); y++){
			for(int x = 0; x < grid.getWidth(); x++){
				TextGrid.Cell cell = grid.new Cell(x, y);
				if(!grid.isBlank(cell)) result.set(x/3, y/3, '*');
			}
		}
		if (DEBUG){
			System.out.println("Getting AbstractionGrid as textGrid.\nAbstractionGrid:");
			grid.printDebug();
			System.out.println("...as text grid:");
			result.printDebug();
		}

		return result;
	}

	public ArrayList getDistinctShapes(){
		ArrayList result = new ArrayList();
		
		CellSet nonBlank = grid.getAllNonBlank();
		ArrayList distinct = nonBlank.breakIntoDistinctBoundaries();
		
		Iterator it = distinct.iterator();
		while (it.hasNext()) {
			CellSet set = (CellSet) it.next();
			AbstractionGrid temp = new AbstractionGrid(this.getWidth(), this.getHeight());
			temp.fillCells(set);
			result.add(temp.getAsTextGrid().getAllNonBlank());
		}
		
		return result; 
	}
	
	protected void fillCells(CellSet cells){
		grid.fillCellsWith(cells, '*');
	}
	
	public void set(int xPos, int yPos, AbstractCell cell){
		xPos *= 3;
		yPos *= 3;
		for(int y = 0; y < 3; y++){
			for(int x = 0; x < 3; x++){
				if(cell.rows[x][y] == 1){
					grid.set(xPos + x, yPos + y, '*');
				}
			}
		}
	}

}
