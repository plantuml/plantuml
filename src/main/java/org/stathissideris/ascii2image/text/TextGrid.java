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

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.stathissideris.ascii2image.core.FileUtils;
import org.stathissideris.ascii2image.core.ProcessingOptions;


/**
 * 
 * @author Efstathios Sideris
 */
public class TextGrid {

	private static final boolean DEBUG = false;

	private ArrayList<StringBuffer> rows;

	private static char[] boundaries = {'/', '\\', '|', '-', '*', '=', ':'};
	private static char[] undisputableBoundaries = {'|', '-', '*', '=', ':'};
	private static char[] horizontalLines = {'-', '='};
	private static char[] verticalLines = {'|', ':'};
	private static char[] arrowHeads = {'<', '>', '^', 'v', 'V'};
	private static char[] cornerChars = {'\\', '/', '+'};
	private static char[] pointMarkers = {'*'};
	private static char[] dashedLines = {':', '~', '='};
	
	private static char[] entryPoints1 = {'\\'};
	private static char[] entryPoints2 = {'|', ':', '+', '\\', '/'};
	private static char[] entryPoints3 = {'/'};
	private static char[] entryPoints4 = {'-', '=', '+', '\\', '/'};
	private static char[] entryPoints5 = {'\\'};
	private static char[] entryPoints6 = {'|', ':', '+', '\\', '/'};
	private static char[] entryPoints7 = {'/'};
	private static char[] entryPoints8 = {'-', '=', '+', '\\', '/'};



	private static HashMap<String, String> humanColorCodes = new HashMap<String, String>();
	static {
		humanColorCodes.put("GRE", "9D9");
		humanColorCodes.put("BLU", "55B");
		humanColorCodes.put("PNK", "FAA");
		humanColorCodes.put("RED", "E32");
		humanColorCodes.put("YEL", "FF3");
		humanColorCodes.put("BLK", "000");
		
	}

	private static HashSet<String> markupTags =
		new HashSet<String>();

	static {
		markupTags.add("d");
		markupTags.add("s");
		markupTags.add("io");
		markupTags.add("c");
		markupTags.add("mo");
		markupTags.add("tr");
		markupTags.add("o");
	}

	public void addToMarkupTags(Collection<String> tags){
		markupTags.addAll(tags);
	}
	
	public static void main(String[] args) throws Exception {
		TextGrid grid = new TextGrid();
		grid.loadFrom("tests/text/art10.txt");

		grid.writeStringTo(grid.new Cell(28, 1), "testing");

		grid.findMarkupTags();
		
		grid.printDebug();
		//System.out.println(grid.fillContinuousArea(0, 0, '-').size()+" cells filled");
		//grid.fillContinuousArea(4, 4, '-');
		//grid.getSubGrid(1,1,3,3).printDebug();
		//grid.printDebug();
	}


	public TextGrid(){
		rows = new ArrayList<StringBuffer>();
	}
	
	public TextGrid(int width, int height){
		String space = StringUtils.repeatString(" ", width);
		rows = new ArrayList<StringBuffer>();
		for(int i = 0; i < height; i++)
			rows.add(new StringBuffer(space));
	}

	public static TextGrid makeSameSizeAs(TextGrid grid){
		return new TextGrid(grid.getWidth(), grid.getHeight());
	}


	public TextGrid(TextGrid otherGrid){
		rows = new ArrayList<StringBuffer>();
		for(StringBuffer row : otherGrid.getRows()) {
			rows.add(new StringBuffer(row));
		}		
	}

	public void clear(){
		String blank = StringUtils.repeatString(" ", getWidth());
		int height = getHeight();
		rows.clear();
		for(int i = 0; i < height; i++)
			rows.add(new StringBuffer(blank)); 
	}

//	duplicated code due to lots of hits to this function
	public char get(int x, int y){
		if(x > getWidth() - 1
			|| y > getHeight() - 1
			|| x < 0
			|| y < 0) return 0;
		return rows.get(y).charAt(x);
	}

	//duplicated code due to lots of hits to this function
	public char get(Cell cell){
		if(cell.x > getWidth() - 1
			|| cell.y > getHeight() - 1
			|| cell.x < 0
			|| cell.y < 0) return 0;
		return rows.get(cell.y).charAt(cell.x);
	}
	
	public StringBuffer getRow(int y){
		return rows.get(y);
	}

	public TextGrid getSubGrid(int x, int y, int width, int height){
		TextGrid grid = new TextGrid(width, height);
		for(int i = 0; i < height; i++){
			grid.setRow(i, new StringBuffer(getRow(y + i).subSequence(x, x + width)));
		}
		return grid;
	}

	public TextGrid getTestingSubGrid(Cell cell){
		return getSubGrid(cell.x - 1, cell.y - 1, 3, 3);
	}


	public String getStringAt(int x, int y, int length){
		return getStringAt(new Cell(x, y), length);
	}

	public String getStringAt(Cell cell, int length){
		int x = cell.x;
		int y = cell.y;
		if(x > getWidth() - 1
			|| y > getHeight() - 1
			|| x < 0
			|| y < 0) return null;
		return rows.get(y).substring(x, x + length);		
	}

	public char getNorthOf(int x, int y){ return get(x, y - 1); }
	public char getSouthOf(int x, int y){ return get(x, y + 1); }
	public char getEastOf(int x, int y){ return get(x + 1, y); }
	public char getWestOf(int x, int y){ return get(x - 1, y); }

	public char getNorthOf(Cell cell){ return getNorthOf(cell.x, cell.y); }
	public char getSouthOf(Cell cell){ return getSouthOf(cell.x, cell.y); }
	public char getEastOf(Cell cell){ return getEastOf(cell.x, cell.y); }
	public char getWestOf(Cell cell){ return getWestOf(cell.x, cell.y); }

	public void writeStringTo(int x, int y, String str){
		writeStringTo(new Cell(x, y), str);
	}

	public void writeStringTo(Cell cell, String str){
		if(isOutOfBounds(cell)) return;
		rows.get(cell.y).replace(cell.x, cell.x + str.length(), str);
	}

	public void set(Cell cell, char c){
		set(cell.x, cell.y, c);
	}

	public void set(int x, int y, char c){
		if(x > getWidth() - 1 || y > getHeight() - 1) return;
		StringBuffer row = rows.get(y);
		row.setCharAt(x, c);
	}
	
	public void setRow(int y, String row){
		if(y > getHeight() || row.length() != getWidth())
			throw new IllegalArgumentException("setRow out of bounds or string wrong size");
		rows.set(y, new StringBuffer(row));
	}

	public void setRow(int y, StringBuffer row){
		if(y > getHeight() || row.length() != getWidth())
			throw new IllegalArgumentException("setRow out of bounds or string wrong size");
		rows.set(y, row);
	}
	
	public int getWidth(){
		if(rows.size() == 0) return 0; //empty buffer
		return rows.get(0).length();
	}

	public int getHeight(){
		return rows.size();
	}

	public void printDebug(){
		Iterator<StringBuffer> it = rows.iterator();
		int i = 0;
		System.out.println(
			"    "
			+StringUtils.repeatString("0123456789", (int) Math.floor(getWidth()/10)+1));
		while(it.hasNext()){
			String row = it.next().toString();
			String index = new Integer(i).toString();
			if(i < 10) index = " "+index;
			System.out.println(index+" ("+row+")");
			i++; 
		}
	}

	public String getDebugString(){
		StringBuffer buffer = new StringBuffer();
		Iterator<StringBuffer> it = rows.iterator();
		int i = 0;
		buffer.append(
			"    "
			+StringUtils.repeatString("0123456789", (int) Math.floor(getWidth()/10)+1)+"\n");
		while(it.hasNext()){
			String row = it.next().toString();
			String index = new Integer(i).toString();
			if(i < 10) index = " "+index;
			row = row.replaceAll("\n", "\\\\n");
			row = row.replaceAll("\r", "\\\\r");
			buffer.append(index+" ("+row+")\n");
			i++; 
		}
		return buffer.toString();
	}

	public String toString(){
		return getDebugString();
	}

	/**
	 * Adds grid to this. Space characters in this grid
	 * are replaced with the corresponding contents of 
	 * grid, otherwise the contents are unchanged.
	 * 
	 * @param grid
	 * @return false if the grids are of different size
	 */
	public boolean add(TextGrid grid){
		if(getWidth() != grid.getWidth()
			|| getHeight() != grid.getHeight()) return false;
		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width; xi++){
				if(get(xi, yi) == ' ') set(xi, yi, grid.get(xi, yi));
			}
		}
		return true;
	}

	/**
	 * Replaces letters or numbers that are on horizontal or vertical
	 * lines, with the appropriate character that will make the line
	 * continuous (| for vertical and - for horizontal lines)
	 *
	 */
	public void replaceTypeOnLine(){
		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width; xi++){
				char c = get(xi, yi);
				if(Character.isLetterOrDigit(c)) {
					boolean isOnHorizontalLine = isOnHorizontalLine(xi, yi);
					boolean isOnVerticalLine = isOnVerticalLine(xi, yi); 
					if(isOnHorizontalLine && isOnVerticalLine){
						set(xi, yi, '+');
						if(DEBUG) System.out.println("replaced type on line '"+c+"' with +");
					} else if(isOnHorizontalLine){
						set(xi, yi, '-');
						if(DEBUG) System.out.println("replaced type on line '"+c+"' with -");
					} else if(isOnVerticalLine){
						set(xi, yi, '|');
						if(DEBUG) System.out.println("replaced type on line '"+c+"' with |");
					}
				}
			}
		}		
	}

	public void replacePointMarkersOnLine(){
		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width; xi++){
				char c = get(xi, yi);
				Cell cell = new Cell(xi, yi);
				if(StringUtils.isOneOf(c, pointMarkers)
						&& isStarOnLine(cell)){
					
					boolean isOnHorizontalLine = false;
					if(StringUtils.isOneOf(get(cell.getEast()), horizontalLines))
						isOnHorizontalLine = true;
					if(StringUtils.isOneOf(get(cell.getWest()), horizontalLines))
						isOnHorizontalLine = true;

					boolean isOnVerticalLine = false;
					if(StringUtils.isOneOf(get(cell.getNorth()), verticalLines))
						isOnVerticalLine = true;
					if(StringUtils.isOneOf(get(cell.getSouth()), verticalLines))
						isOnVerticalLine = true;
 
					if(isOnHorizontalLine && isOnVerticalLine){
						set(xi, yi, '+');
						if(DEBUG) System.out.println("replaced marker on line '"+c+"' with +");
					} else if(isOnHorizontalLine){
						set(xi, yi, '-');
						if(DEBUG) System.out.println("replaced marker on line '"+c+"' with -");
					} else if(isOnVerticalLine){
						set(xi, yi, '|');
						if(DEBUG) System.out.println("replaced marker on line '"+c+"' with |");
					}
				}
			}
		}		
	}

	public CellSet getPointMarkersOnLine(){
		CellSet result = new CellSet();
		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width; xi++){
				char c = get(xi, yi);
				if(StringUtils.isOneOf(c, pointMarkers)
						&& isStarOnLine(new Cell(xi, yi))){
					result.add(new Cell(xi, yi));
				}
			}
		}
		return result;
	}


	public void replaceHumanColorCodes(){
		int height = getHeight();
		for(int y = 0; y < height; y++){
			String row = rows.get(y).toString();
			Iterator it = humanColorCodes.keySet().iterator();
			while(it.hasNext()){
				String humanCode = (String) it.next();
				String hexCode = (String) humanColorCodes.get(humanCode);
				if(hexCode != null){
					humanCode = "c" + humanCode;
					hexCode = "c" + hexCode;
					row = row.replaceAll(humanCode, hexCode);
					rows.set(y, new StringBuffer(row)); //TODO: this is not the most efficient way to do this
					row = rows.get(y).toString();
				}
			}
		}		
	}


	/**
	 * Replace all occurences of c1 with c2
	 * 
	 * @param c1
	 * @param c2
	 */
	public void replaceAll(char c1, char c2){
		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width; xi++){
				char c = get(xi, yi);
				if(c == c1) set(xi, yi, c2);
			}
		}		
	}

	public boolean hasBlankCells(){
		CellSet set = new CellSet();
		int width = getWidth();
		int height = getHeight();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				Cell cell = new Cell(x, y);
				if(isBlank(cell)) return true;
			}
		}
		return false;
	}


	public CellSet getAllNonBlank(){
		CellSet set = new CellSet();
		int width = getWidth();
		int height = getHeight();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				Cell cell = new Cell(x, y);
				if(!isBlank(cell)) set.add(cell);
			}
		}
		return set;
	}

	public CellSet getAllBoundaries(){
		CellSet set = new CellSet();
		int width = getWidth();
		int height = getHeight();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				Cell cell = new Cell(x, y);
				if(isBoundary(cell)) set.add(cell);
			}
		}
		return set;
	}


	public CellSet getAllBlanksBetweenCharacters(){
		CellSet set = new CellSet();
		int width = getWidth();
		int height = getHeight();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				Cell cell = new Cell(x, y);
				if(isBlankBetweenCharacters(cell)) set.add(cell);
			}
		}
		return set;
	}


	/**
	 * Returns an ArrayList of CellStringPairs that
	 * represents all the continuous (non-blank) Strings
	 * in the grid. Used on buffers that contain only
	 * type, in order to find the positions and the
	 * contents of the strings.
	 * 
	 * @return
	 */
	public ArrayList<CellStringPair> findStrings(){
		ArrayList<CellStringPair> result = new ArrayList<CellStringPair>();
		int width = getWidth();
		int height = getHeight();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(!isBlank(x, y)){
					Cell start = new Cell(x, y);
					String str = String.valueOf(get(x,y));
					char c = get(++x, y);
					boolean finished = false;
					//while(c != ' '){
					while(!finished){
						str += String.valueOf(c);
						c = get(++x, y);
						char next = get(x + 1, y);
						if((c == ' ' || c == 0) && (next == ' ' || next == 0))
							finished = true;
					}
					result.add(new CellStringPair(start, str));
				}
			}
		}
		return result;
	}

	/**
	 * This is done in a bit of a messy way, should be impossible
	 * to go out of sync with corresponding GridPatternGroup.
	 * 
	 * @param cell
	 * @param entryPointId
	 * @return
	 */
	public boolean hasEntryPoint(Cell cell, int entryPointId){
		String result = "";
		char c = get(cell);
		if(entryPointId == 1) {
			return StringUtils.isOneOf(c, entryPoints1);
		
		} else if(entryPointId == 2) {
			return StringUtils.isOneOf(c, entryPoints2);

		} else if(entryPointId == 3) {
			return StringUtils.isOneOf(c, entryPoints3);

		} else if(entryPointId == 4) {
			return StringUtils.isOneOf(c, entryPoints4);

		} else if(entryPointId == 5) {
			return StringUtils.isOneOf(c, entryPoints5);

		} else if(entryPointId == 6) {
			return StringUtils.isOneOf(c, entryPoints6);

		} else if(entryPointId == 7) {
			return StringUtils.isOneOf(c, entryPoints7);

		} else if(entryPointId == 8) {
			return StringUtils.isOneOf(c, entryPoints8);
		}
		return false;
	}

	/**
	 * true if cell is blank and the east and west cells are not
	 * (used to find gaps between words)
	 * 
	 * @param cell
	 * @return
	 */
	public boolean isBlankBetweenCharacters(Cell cell){
		return (isBlank(cell)
				&& !isBlank(cell.getEast())
				&& !isBlank(cell.getWest()));
	}

	/**
	 * Makes blank all the cells that contain non-text
	 * elements.
	 */
	public void removeNonText(){
		//the following order is significant
		//since the south-pointing arrowheads
		//are determined based on the surrounding boundaries
		removeArrowheads();
		removeColorCodes();
		removeBoundaries();
		removeMarkupTags();
	}

	public void removeArrowheads(){
		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width; xi++){
				Cell cell = new Cell(xi, yi);
				if(isArrowhead(cell)) set(cell, ' ');
			}
		}		
	}

	public void removeColorCodes(){
		Iterator cells = findColorCodes().iterator();
		while(cells.hasNext()){
			Cell cell = ((CellColorPair) cells.next()).cell;
			set(cell, ' ');
			cell = cell.getEast(); set(cell, ' ');
			cell = cell.getEast(); set(cell, ' ');
			cell = cell.getEast(); set(cell, ' ');
		}
	}

	public void removeBoundaries(){
		ArrayList toBeRemoved = new ArrayList();

		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width; xi++){
				Cell cell = new Cell(xi, yi);
				if(isBoundary(cell)) toBeRemoved.add(cell);
			}
		}
		
		//remove in two stages, because decision of
		//isBoundary depends on contants of surrounding
		//cells 
		Iterator it = toBeRemoved.iterator();
		while(it.hasNext()){
			Cell cell = (Cell) it.next();
			set(cell, ' ');
		}
	}

	public ArrayList findArrowheads(){
		ArrayList result = new ArrayList();
		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width; xi++){
				Cell cell = new Cell(xi, yi);
				if(isArrowhead(cell)) result.add(cell);
			}
		}
		if(DEBUG) System.out.println(result.size()+" arrowheads found");
		return result;
	}


	public ArrayList<CellColorPair> findColorCodes(){
		Pattern colorCodePattern = Pattern.compile("c[A-F0-9]{3}");
		ArrayList<CellColorPair> result = new ArrayList<CellColorPair>();
		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width - 3; xi++){
				Cell cell = new Cell(xi, yi);
				String s = getStringAt(cell, 4);
				Matcher matcher = colorCodePattern.matcher(s);
				if(matcher.matches()){
					char cR = s.charAt(1);
					char cG = s.charAt(2);
					char cB = s.charAt(3);
					int r = Integer.valueOf(String.valueOf(cR), 16).intValue() * 17;
					int g = Integer.valueOf(String.valueOf(cG), 16).intValue() * 17;
					int b = Integer.valueOf(String.valueOf(cB), 16).intValue() * 17;
					result.add(new CellColorPair(cell, new Color(r, g, b)));
				}
			}
		}
		if(DEBUG) System.out.println(result.size()+" color codes found");
		return result;
	}

	public ArrayList<CellTagPair> findMarkupTags(){
		Pattern tagPattern = Pattern.compile("\\{(.+?)\\}");
		ArrayList<CellTagPair> result = new ArrayList<CellTagPair>();

		int width = getWidth();
		int height = getHeight();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width - 3; x++){
				Cell cell = new Cell(x, y);
				char c = get(cell);
				if(c == '{'){
					String rowPart = rows.get(y).substring(x);
					Matcher matcher = tagPattern.matcher(rowPart);
					if(matcher.find()){
						String tagName = matcher.group(1);
						if(markupTags.contains(tagName)){
							if(DEBUG) System.out.println("found tag "+tagName+" at "+x+", "+y);
							result.add(new CellTagPair(new Cell(x, y), tagName));
						}
					}
				}
			}
		}
		return result;
	}
		
	public void removeMarkupTags(){
		Iterator it = findMarkupTags().iterator();
		while (it.hasNext()) {
			CellTagPair pair = (CellTagPair) it.next();
			String tagName = pair.tag;
			if(tagName == null) continue;
			int length = 2 + tagName.length();
			writeStringTo(pair.cell, StringUtils.repeatString(" ", length));
		}
	}
		


	public boolean matchesAny(GridPatternGroup criteria){
		return criteria.isAnyMatchedBy(this);
	}

	public boolean matchesAll(GridPatternGroup criteria){
		return criteria.areAllMatchedBy(this);
	}

	public boolean matches(GridPattern criteria){
		return criteria.isMatchedBy(this);
	}


	public boolean isOnHorizontalLine(Cell cell){ return isOnHorizontalLine(cell.x, cell.y); }
	private boolean isOnHorizontalLine(int x, int y){
		char c1 = get(x - 1, y);
		char c2 = get(x + 1, y);
		if(isHorizontalLine(c1) && isHorizontalLine(c2)) return true;
		return false;
	}

	public boolean isOnVerticalLine(Cell cell){ return isOnVerticalLine(cell.x, cell.y); }
	private boolean isOnVerticalLine(int x, int y){
		char c1 = get(x, y - 1);
		char c2 = get(x, y + 1);
		if(isVerticalLine(c1) && isVerticalLine(c2)) return true;
		return false;
	}


	public static boolean isBoundary(char c){
		return StringUtils.isOneOf(c, boundaries);
	}
	public boolean isBoundary(int x, int y){ return isBoundary(new Cell(x, y)); }
	public boolean isBoundary(Cell cell){
		char c = get(cell.x, cell.y);
		if(0 == c) return false;
		if('+' == c || '\\' == c || '/' == c){
			// System.out.print("");
			if(
			       isIntersection(cell) 
				|| isCorner(cell)
				|| isStub(cell) 
				|| isCrossOnLine(cell)){
				return true;
			} else return false;
		}
		//return StringUtils.isOneOf(c, undisputableBoundaries);
		if(StringUtils.isOneOf(c, boundaries) && !isLoneDiagonal(cell)){
			return true;
		}
		return false;
	}

	public boolean isLine(Cell cell){
		return isHorizontalLine(cell) || isVerticalLine(cell);
	}

	public static boolean isHorizontalLine(char c){
		return StringUtils.isOneOf(c, horizontalLines);
	}
	public boolean isHorizontalLine(Cell cell){ return isHorizontalLine(cell.x, cell.y); }
	public boolean isHorizontalLine(int x, int y){
		char c = get(x, y);
		if(0 == c) return false;
		return StringUtils.isOneOf(c, horizontalLines);
	}

	public static boolean isVerticalLine(char c){
		return StringUtils.isOneOf(c, verticalLines);
	}
	public boolean isVerticalLine(Cell cell){ return isVerticalLine(cell.x, cell.y); }
	public boolean isVerticalLine(int x, int y){
		char c = get(x, y);
		if(0 == c) return false;
		return StringUtils.isOneOf(c, verticalLines);
	}

	public boolean isLinesEnd(int x, int y){
		return isLinesEnd(new Cell(x, y));
	}
	
	/**
	 * Stubs are also considered end of lines
	 * 
	 * @param cell
	 * @return
	 */
	public boolean isLinesEnd(Cell cell){
		return matchesAny(cell, GridPatternGroup.linesEndCriteria);
	}

	public boolean isVerticalLinesEnd(Cell cell){
		return matchesAny(cell, GridPatternGroup.verticalLinesEndCriteria);
	}

	public boolean isHorizontalLinesEnd(Cell cell){
		return matchesAny(cell, GridPatternGroup.horizontalLinesEndCriteria);
	}


	public boolean isPointCell(Cell cell){
		return (
			isCorner(cell)
			|| isIntersection(cell)
			|| isStub(cell)
			|| isLinesEnd(cell));
	}


	public boolean containsAtLeastOneDashedLine(CellSet set){
		Iterator it = set.iterator();
		while(it.hasNext()) {
			Cell cell = (Cell) it.next();
			if(StringUtils.isOneOf(get(cell), dashedLines)) return true;
		}
		return false;
	}

	public boolean exactlyOneNeighbourIsBoundary(Cell cell) {
		int howMany = 0;
		if(isBoundary(cell.getNorth())) howMany++;
		if(isBoundary(cell.getSouth())) howMany++;
		if(isBoundary(cell.getEast())) howMany++;
		if(isBoundary(cell.getWest())) howMany++;
		return (howMany == 1);
	}

	/**
	 * 
	 * A stub looks like that:
	 * 
	 * <pre>
	 * 
	 * +- or -+ or + or + or /- or -/ or / (you get the point)
	 *             |    |                |
	 * 
	 * </pre>
	 * 
	 * @param cell
	 * @return
	 */

	public boolean isStub(Cell cell){
		return matchesAny(cell, GridPatternGroup.stubCriteria);
	}

	public boolean isCrossOnLine(Cell cell){
		return matchesAny(cell, GridPatternGroup.crossOnLineCriteria);
	}

	public boolean isHorizontalCrossOnLine(Cell cell){
		return matchesAny(cell, GridPatternGroup.horizontalCrossOnLineCriteria);
	}

	public boolean isVerticalCrossOnLine(Cell cell){
		return matchesAny(cell, GridPatternGroup.verticalCrossOnLineCriteria);
	}

	public boolean isStarOnLine(Cell cell){
		return matchesAny(cell, GridPatternGroup.starOnLineCriteria);
	}

	public boolean isLoneDiagonal(Cell cell){
		return matchesAny(cell, GridPatternGroup.loneDiagonalCriteria);
	}


	public boolean isHorizontalStarOnLine(Cell cell){
		return matchesAny(cell, GridPatternGroup.horizontalStarOnLineCriteria);
	}

	public boolean isVerticalStarOnLine(Cell cell){
		return matchesAny(cell, GridPatternGroup.verticalStarOnLineCriteria);
	}

	public boolean isArrowhead(Cell cell){
		return (isNorthArrowhead(cell)
				|| isSouthArrowhead(cell)
				|| isWestArrowhead(cell)
				|| isEastArrowhead(cell));
	}
	
	public boolean isNorthArrowhead(Cell cell){
		return get(cell) == '^';
	}

	public boolean isEastArrowhead(Cell cell){
		return get(cell) == '>';
	}

	public boolean isWestArrowhead(Cell cell){
		return get(cell) == '<';
	}

	public boolean isSouthArrowhead(Cell cell){
		return (get(cell) == 'v' || get(cell) == 'V')
				&& isVerticalLine(cell.getNorth());
	}
	
	
//	unicode for bullets
//
//	2022 bullet
//	25CF black circle
//	25AA black circle (small)
//	25A0 black square
//	25A1 white square
//	25CB white circle
//	25BA black right-pointing pointer


	public boolean isBullet(int x, int y){
		return isBullet(new Cell(x, y));
	}
	
	public boolean isBullet(Cell cell){
		char c = get(cell);
		if((c == 'o' || c == '*')
			&& isBlank(cell.getEast())
			&& isBlank(cell.getWest())
			&& Character.isLetterOrDigit(get(cell.getEast().getEast())) )
			return true;
		return false;
	}
	
	public void replaceBullets(){
		int width = getWidth();
		int height = getHeight();
		for(int yi = 0; yi < height; yi++){
			for(int xi = 0; xi < width; xi++){
				Cell cell = new Cell(xi, yi);
				if(isBullet(cell)){
					set(cell, ' ');
					set(cell.getEast(), '\u2022');
				}
			}
		}
	}
	
	/**
	 * true if the cell is not blank
	 * but the previous (west) is
	 * 
	 * @param cell
	 * @return
	 */
	public boolean isStringsStart(Cell cell){
		return (!isBlank(cell) && isBlank(cell.getWest()));
	}

	/**
	 * true if the cell is not blank
	 * but the next (east) is
	 * 
	 * @param cell
	 * @return
	 */
	public boolean isStringsEnd(Cell cell){
		return (!isBlank(cell)
			//&& (isBlank(cell.getEast()) || get(cell.getEast()) == 0));
			&& isBlank(cell.getEast()));
	}

	public int otherStringsStartInTheSameColumn(Cell cell){
		if(!isStringsStart(cell)) return 0;
		int result = 0;
		int height = getHeight();
		for(int y = 0; y < height; y++){
			Cell cCell = new Cell(cell.x, y);
			if(!cCell.equals(cell) && isStringsStart(cCell)){
				result++;
			}
		}
		return result;
	}

	public int otherStringsEndInTheSameColumn(Cell cell){
		if(!isStringsEnd(cell)) return 0;
		int result = 0;
		int height = getHeight();
		for(int y = 0; y < height; y++){
			Cell cCell = new Cell(cell.x, y);
			if(!cCell.equals(cell) && isStringsEnd(cCell)){
				result++;
			}
		}
		return result;
	}

	public boolean isColumnBlank(int x){
		int height = getHeight();
		for(int y = 0; y < height; y++){
			if(!isBlank(x, y)) return false;
		}
		return true;
	}


	public CellSet followLine(int x, int y){
		return followLine(new Cell(x, y));
	}

	public CellSet followIntersection(Cell cell){
		return followIntersection(cell, null);
	}

	public CellSet followIntersection(Cell cell, Cell blocked){
		if(!isIntersection(cell)) return null;
		CellSet result = new CellSet();
		Cell cN = cell.getNorth();
		Cell cS = cell.getSouth();
		Cell cE = cell.getEast();
		Cell cW = cell.getWest();
		if(hasEntryPoint(cN, 6)) result.add(cN);
		if(hasEntryPoint(cS, 2)) result.add(cS);
		if(hasEntryPoint(cE, 8)) result.add(cE);
		if(hasEntryPoint(cW, 4)) result.add(cW);
		if(result.contains(blocked)) result.remove(blocked);
		return result;
	}

	/**
	 * Returns the neighbours of a line-cell that are boundaries
	 *  (0 to 2 cells are returned)
	 * 
	 * @param cell
	 * @return null if the cell is not a line
	 */
	public CellSet followLine(Cell cell){
		if(isHorizontalLine(cell)){
			CellSet result = new CellSet();
			if(isBoundary(cell.getEast())) result.add(cell.getEast());
			if(isBoundary(cell.getWest())) result.add(cell.getWest());
			return result;
		} else if (isVerticalLine(cell)){
			CellSet result = new CellSet();
			if(isBoundary(cell.getNorth())) result.add(cell.getNorth());
			if(isBoundary(cell.getSouth())) result.add(cell.getSouth());
			return result;			
		}
		return null;
	}

	public CellSet followLine(Cell cell, Cell blocked){
		CellSet nextCells = followLine(cell);
		if(nextCells.contains(blocked)) nextCells.remove(blocked);
		return nextCells;
	}

	public CellSet followCorner(Cell cell){
		return followCorner(cell, null);
	}

	public CellSet followCorner(Cell cell, Cell blocked){
		if(!isCorner(cell)) return null;
		if(isCorner1(cell)) return followCorner1(cell, blocked);
		if(isCorner2(cell)) return followCorner2(cell, blocked);
		if(isCorner3(cell)) return followCorner3(cell, blocked);
		if(isCorner4(cell)) return followCorner4(cell, blocked);
		return null;
	}

	public CellSet followCorner1(Cell cell){
		return followCorner1(cell, null);
	}
	public CellSet followCorner1(Cell cell, Cell blocked){
		if(!isCorner1(cell)) return null;
		CellSet result = new CellSet();
		if(!cell.getSouth().equals(blocked)) result.add(cell.getSouth());
		if(!cell.getEast().equals(blocked)) result.add(cell.getEast());
		return result;
	}

	public CellSet followCorner2(Cell cell){
		return followCorner2(cell, null);
	}
	public CellSet followCorner2(Cell cell, Cell blocked){
		if(!isCorner2(cell)) return null;
		CellSet result = new CellSet();
		if(!cell.getSouth().equals(blocked)) result.add(cell.getSouth());
		if(!cell.getWest().equals(blocked)) result.add(cell.getWest());
		return result;
	}

	public CellSet followCorner3(Cell cell){
		return followCorner3(cell, null);
	}
	public CellSet followCorner3(Cell cell, Cell blocked){
		if(!isCorner3(cell)) return null;
		CellSet result = new CellSet();
		if(!cell.getNorth().equals(blocked)) result.add(cell.getNorth());
		if(!cell.getWest().equals(blocked)) result.add(cell.getWest());
		return result;
	}

	public CellSet followCorner4(Cell cell){
		return followCorner4(cell, null);
	}
	public CellSet followCorner4(Cell cell, Cell blocked){
		if(!isCorner4(cell)) return null;
		CellSet result = new CellSet();
		if(!cell.getNorth().equals(blocked)) result.add(cell.getNorth());
		if(!cell.getEast().equals(blocked)) result.add(cell.getEast());
		return result;
	}


	public CellSet followStub(Cell cell){
		return followStub(cell, null);
	}
	public CellSet followStub(Cell cell, Cell blocked){
		if(!isStub(cell)) return null;
		CellSet result = new CellSet();
		if(isBoundary(cell.getEast())) result.add(cell.getEast());
		else if(isBoundary(cell.getWest())) result.add(cell.getWest());
		else if(isBoundary(cell.getNorth())) result.add(cell.getNorth());
		else if(isBoundary(cell.getSouth())) result.add(cell.getSouth());
		if(result.contains(blocked)) result.remove(blocked);
		return result;
	}
	
	public CellSet followCell(Cell cell){
		return followCell(cell, null);
	}
	
	public CellSet followCell(Cell cell, Cell blocked){
		if(isIntersection(cell)) return followIntersection(cell, blocked);
		if(isCorner(cell)) return followCorner(cell, blocked);
		if(isLine(cell)) return followLine(cell, blocked);
		if(isStub(cell)) return followStub(cell, blocked);
		if(isCrossOnLine(cell)) return followCrossOnLine(cell, blocked);
		System.err.println("Umbiguous input at position "+cell+":");
		TextGrid subGrid = getTestingSubGrid(cell);
		subGrid.printDebug();
		throw new RuntimeException("Cannot follow cell "+cell+": cannot determine cell type");
	}

	public String getCellTypeAsString(Cell cell){
		if(isK(cell)) return "K";
		if(isT(cell)) return "T";
		if(isInverseK(cell)) return "inverse K";
		if(isInverseT(cell)) return "inverse T";
		if(isCorner1(cell)) return "corner 1";
		if(isCorner2(cell)) return "corner 2";
		if(isCorner3(cell)) return "corner 3";
		if(isCorner4(cell)) return "corner 4";
		if(isLine(cell)) return "line";
		if(isStub(cell)) return "stub";
		if(isCrossOnLine(cell)) return "crossOnLine";
		return "unrecognisable type";
	}

	
	public CellSet followCrossOnLine(Cell cell, Cell blocked){
		CellSet result = new CellSet();
		if(isHorizontalCrossOnLine(cell)){
			result.add(cell.getEast());
			result.add(cell.getWest());
		} else if(isVerticalCrossOnLine(cell)){
			result.add(cell.getNorth());
			result.add(cell.getSouth());
		}
		if(result.contains(blocked)) result.remove(blocked);
		return result;
	}

	public boolean isOutOfBounds(Cell cell){
		if(cell.x > getWidth() - 1
			|| cell.y > getHeight() - 1
			|| cell.x < 0
			|| cell.y < 0) return true;
		return false;
	}

	public boolean isOutOfBounds(int x, int y){
		char c = get(x, y);
		if(0 == c) return true;
		return false;
	}

	public boolean isBlank(Cell cell){
		char c = get(cell);
		if(0 == c) return false;
		return c == ' ';
	}

	public boolean isBlank(int x, int y){
		char c = get(x, y);
		if(0 == c) return true;
		return c == ' ';
	}

	public boolean isCorner(Cell cell){
		return isCorner(cell.x, cell.y);
	}
	public boolean isCorner(int x, int y){
		return (isNormalCorner(x,y) || isRoundCorner(x,y));
	}


	public boolean matchesAny(Cell cell, GridPatternGroup criteria){
		TextGrid subGrid = getTestingSubGrid(cell);
		return subGrid.matchesAny(criteria);
	}
	
	public boolean isCorner1(Cell cell){
		return matchesAny(cell, GridPatternGroup.corner1Criteria);
	}

	public boolean isCorner2(Cell cell){
		return matchesAny(cell, GridPatternGroup.corner2Criteria);
	}

	public boolean isCorner3(Cell cell){
		return matchesAny(cell, GridPatternGroup.corner3Criteria);
	}

	public boolean isCorner4(Cell cell){
		return matchesAny(cell, GridPatternGroup.corner4Criteria);
	}

	public boolean isCross(Cell cell){
		return matchesAny(cell, GridPatternGroup.crossCriteria);
	}

	public boolean isK(Cell cell){
		return matchesAny(cell, GridPatternGroup.KCriteria);
	}

	public boolean isInverseK(Cell cell){
		return matchesAny(cell, GridPatternGroup.inverseKCriteria);
	}

	public boolean isT(Cell cell){
		return matchesAny(cell, GridPatternGroup.TCriteria);
	}

	public boolean isInverseT(Cell cell){
		return matchesAny(cell, GridPatternGroup.inverseTCriteria);
	}

	public boolean isNormalCorner(Cell cell){
		return matchesAny(cell, GridPatternGroup.normalCornerCriteria);
	}
	public boolean isNormalCorner(int x, int y){
		return isNormalCorner(new Cell(x, y));
	}

	public boolean isRoundCorner(Cell cell){
		return matchesAny(cell, GridPatternGroup.roundCornerCriteria);
	}

	public boolean isRoundCorner(int x, int y){
		return isRoundCorner(new Cell(x, y));
	}

	public boolean isIntersection(Cell cell){
		return matchesAny(cell, GridPatternGroup.intersectionCriteria);
	}
	public boolean isIntersection(int x, int y){
		return isIntersection(new Cell(x, y));
	}

	public void copyCellsTo(CellSet cells, TextGrid grid){
		Iterator it = cells.iterator();
		while(it.hasNext()){
			Cell cell = (Cell) it.next();
			grid.set(cell, this.get(cell));
		}
	}
	
	public boolean equals(TextGrid grid){
		if(grid.getHeight() != this.getHeight()
			|| grid.getWidth() != this.getWidth()
			){
			return false;
		}
		int height = grid.getHeight();
		for(int i = 0; i < height; i++){
			String row1 = this.getRow(i).toString();
			String row2 = grid.getRow(i).toString();
			if(!row1.equals(row2)) return false;
		}
		return true;
	}
	
//	@Override
//	public int hashCode() {
//		int h = 0;
//		for(StringBuffer row : rows) {
//			h += row.hashCode();
//		}
//		return h;
//	}
	
	/**
	 * Fills all the cells in <code>cells</code> with <code>c</code>
	 * 
	 * @param cells
	 * @param c
	 */
	public void fillCellsWith(Iterable cells, char c){
		Iterator<Cell> it = cells.iterator();
		while(it.hasNext()){
			Cell cell = it.next();
			set(cell.x, cell.y, c);
		}
	}

	/**
	 * 
	 * Fills the continuous area with if c1 characters with c2,
	 * flooding from cell x, y
	 * 
	 * @param x
	 * @param y
	 * @param c1 the character to replace
	 * @param c2 the character to replace c1 with
	 * @return the list of cells filled
	 */
//	public CellSet fillContinuousArea(int x, int y, char c1, char c2){
//		CellSet cells = new CellSet();
//		//fillContinuousArea_internal(x, y, c1, c2, cells);
//		seedFill(new Cell(x, y), c1, c2);
//		return cells;
//	}

	public CellSet fillContinuousArea(int x, int y, char c){
		return fillContinuousArea(new Cell(x, y), c);
	}

	public CellSet fillContinuousArea(Cell cell, char c){
		if(isOutOfBounds(cell)) throw new IllegalArgumentException("Attempted to fill area out of bounds: "+cell);
		return seedFillOld(cell, c);
	}

	private CellSet seedFill(Cell seed, char newChar){
		CellSet cellsFilled = new CellSet();
		char oldChar = get(seed);
		
		if(oldChar == newChar) return cellsFilled;
		if(isOutOfBounds(seed)) return cellsFilled;

		Stack<Cell> stack = new Stack<Cell>();

		stack.push(seed);
		
		while(!stack.isEmpty()){
			Cell cell = (Cell) stack.pop();
			
			//set(cell, newChar);
			cellsFilled.add(cell);

			Cell nCell = cell.getNorth();
			Cell sCell = cell.getSouth();
			Cell eCell = cell.getEast();
			Cell wCell = cell.getWest();
			
			if(get(nCell) == oldChar && !cellsFilled.contains(nCell)) stack.push(nCell);
			if(get(sCell) == oldChar && !cellsFilled.contains(sCell)) stack.push(sCell);
			if(get(eCell) == oldChar && !cellsFilled.contains(eCell)) stack.push(eCell);
			if(get(wCell) == oldChar && !cellsFilled.contains(wCell)) stack.push(wCell);
		}
		
		return cellsFilled;
	}

	private CellSet seedFillOld(Cell seed, char newChar){
		CellSet cellsFilled = new CellSet();
		char oldChar = get(seed);
		
		if(oldChar == newChar) return cellsFilled;
		if(isOutOfBounds(seed)) return cellsFilled;

		Stack<Cell> stack = new Stack<Cell>();

		stack.push(seed);
		
		while(!stack.isEmpty()){
			Cell cell = (Cell) stack.pop();
			
			set(cell, newChar);
			cellsFilled.add(cell);

			Cell nCell = cell.getNorth();
			Cell sCell = cell.getSouth();
			Cell eCell = cell.getEast();
			Cell wCell = cell.getWest();
			
			if(get(nCell) == oldChar) stack.push(nCell);
			if(get(sCell) == oldChar) stack.push(sCell);
			if(get(eCell) == oldChar) stack.push(eCell);
			if(get(wCell) == oldChar) stack.push(wCell);
		}
		
		return cellsFilled;
	}


	/**
	 * 
	 * Locates and returns the '*' boundaries that we would
	 * encounter if we did a flood-fill at <code>seed</code>. 
	 * 
	 * @param seed
	 * @return
	 */
	public CellSet findBoundariesExpandingFrom(Cell seed){
		CellSet boundaries = new CellSet();
		char oldChar = get(seed);

		if(isOutOfBounds(seed)) return boundaries;

		char newChar = 1; //TODO: kludge

		Stack<Cell> stack = new Stack<Cell>();

		stack.push(seed);
		
		while(!stack.isEmpty()){
			Cell cell = (Cell) stack.pop();
			
			set(cell, newChar);

			Cell nCell = cell.getNorth();
			Cell sCell = cell.getSouth();
			Cell eCell = cell.getEast();
			Cell wCell = cell.getWest();
			
			if(get(nCell) == oldChar) stack.push(nCell);
			else if(get(nCell) == '*') boundaries.add(nCell);
			
			if(get(sCell) == oldChar) stack.push(sCell);
			else if(get(sCell) == '*') boundaries.add(sCell);
			
			if(get(eCell) == oldChar) stack.push(eCell);
			else if(get(eCell) == '*') boundaries.add(eCell);
			
			if(get(wCell) == oldChar) stack.push(wCell);
			else if(get(wCell) == '*') boundaries.add(wCell);
		}
		
		return boundaries;
	}
	
	
	//TODO: incomplete method seedFillLine()
	private CellSet seedFillLine(Cell cell, char newChar){
		CellSet cellsFilled = new CellSet();
		
		Stack stack = new Stack();
		
		char oldChar = get(cell);
		
		if(oldChar == newChar) return cellsFilled;
		if(isOutOfBounds(cell)) return cellsFilled;
		
		stack.push(new LineSegment(cell.x, cell.x, cell.y, 1));
		stack.push(new LineSegment(cell.x, cell.x, cell.y + 1, -1));
		
		int left;
		while(!stack.isEmpty()){
			LineSegment segment = (LineSegment) stack.pop();
			int x;
			//expand to the left
			for(
					x = segment.x1;
					x >= 0 && get(x, segment.y) == oldChar;
					--x){
				set(x, segment.y, newChar);
				cellsFilled.add(new Cell(x, segment.y));
			}
			
			left = cell.getEast().x;
			boolean skip = (x > segment.x1)? true : false; 

			if(left < segment.x1){ //leak on left?
				//TODO: i think the first param should be x
				stack.push(
				        //new LineSegment(segment.y, left, segment.x1 - 1, -segment.dy));
				        new LineSegment(x, left, segment.y - 1, -segment.dy));
			}

			x = segment.x1 + 1;
			do {
			    if(!skip) {
			       	for( ; x < getWidth() && get(x, segment.y) == oldChar; ++x){
			       	    set(x, segment.y, newChar);
			       	    cellsFilled.add(new Cell(x, segment.y));
			       	}
					
			       	stack.push(new LineSegment(left, x - 1, segment.y, segment.dy));
				   	if(x > segment.x2 + 1) //leak on right?
			        stack.push(new LineSegment(segment.x2 + 1, x - 1, segment.y, -segment.dy));
			    }
				skip = false; //skip only once
				
				for(++x; x <= segment.x2 && get(x, segment.y) != oldChar; ++x){;}
				left = x;
			} while( x < segment.x2);
		}

		return cellsFilled;
	}
	
	public boolean cellContainsDashedLineChar(Cell cell){
		char c = get(cell);
		return StringUtils.isOneOf(c, dashedLines);
	}

	public boolean loadFrom(String filename)
			throws FileNotFoundException, IOException
			{
		return loadFrom(filename, null);
	}

	public boolean loadFrom(String filename, ProcessingOptions options)
		throws IOException
	{
				
		String encoding = (options == null) ? null : options.getCharacterEncoding();
		ArrayList<StringBuffer> lines = new ArrayList<StringBuffer>();
		String[] linesArray = FileUtils.readFile(new File(filename), encoding).split("(\r)?\n");
		for(int i = 0; i  < linesArray.length; i++)
			lines.add(new StringBuffer(linesArray[i]));
		
		return initialiseWithLines(lines, options);
	}

	public boolean initialiseWithText(String text, ProcessingOptions options) throws UnsupportedEncodingException {

		ArrayList<StringBuffer> lines = new ArrayList<StringBuffer>();
		String[] linesArray = text.split("(\r)?\n");
		for(int i = 0; i  < linesArray.length; i++)
			lines.add(new StringBuffer(linesArray[i]));

		return initialiseWithLines(lines, options);
	}

	public boolean initialiseWithLines(ArrayList<StringBuffer> lines, ProcessingOptions options) throws UnsupportedEncodingException {

		//remove blank rows at the bottom
		boolean done = false;
		int i;
		for(i = lines.size() - 1; !done; i--){
			StringBuffer row = lines.get(i);
			if(!StringUtils.isBlank(row.toString())) done = true;
		}
		rows = new ArrayList<StringBuffer>(lines.subList(0, i + 2));

		if(options != null) fixTabs(options.getTabSize());
		else fixTabs(options.DEFAULT_TAB_SIZE);


		// make all lines of equal length
		// add blank outline around the buffer to prevent fill glitch
		// convert tabs to spaces (or remove them if setting is 0)
		
		int blankBorderSize = 2;
		
		int maxLength = 0;
		int index = 0;
		
		String encoding = null;
		//if(options != null) encoding = options.getCharacterEncoding();
		
		Iterator<StringBuffer> it = rows.iterator();
		while(it.hasNext()){
			String row = it.next().toString();
			if(encoding != null){
				byte[] bytes = row.getBytes();
				row = new String(bytes, encoding);
			}
			if(row.length() > maxLength) maxLength = row.length();
			rows.set(index, new StringBuffer(row));
			index++;
		}

		it = rows.iterator();
		ArrayList<StringBuffer> newRows = new ArrayList<StringBuffer>();
		//TODO: make the following depend on blankBorderSize
		
		StringBuffer topBottomRow =
			new StringBuffer(StringUtils.repeatString(" ", maxLength + blankBorderSize * 2));
		
		newRows.add(topBottomRow);
		newRows.add(topBottomRow);
		while(it.hasNext()){
			StringBuffer row = it.next();
			
			if(row.length() < maxLength) {
				String borderString = StringUtils.repeatString(" ", blankBorderSize);
				StringBuffer newRow = new StringBuffer();
				
				newRow.append(borderString);
				newRow.append(row);
				newRow.append(StringUtils.repeatString(" ", maxLength - row.length()));
				newRow.append(borderString);
				
				newRows.add(newRow);
			} else { //TODO: why is the following line like that?
				newRows.add(new StringBuffer("  ").append(row).append("  "));
			}
		}
		//TODO: make the following depend on blankBorderSize
		newRows.add(topBottomRow);
		newRows.add(topBottomRow);
		rows = newRows;
		
		replaceBullets();
		replaceHumanColorCodes();
		
		return true;
	}
	
	private void fixTabs(int tabSize){

		int rowIndex = 0;
		Iterator<StringBuffer> it = rows.iterator();

		while(it.hasNext()){
			String row = it.next().toString();
			StringBuffer newRow = new StringBuffer();
			
			char[] chars = row.toCharArray();
			for(int i = 0; i < chars.length; i++){
				if(chars[i] == '\t'){
					int spacesLeft = tabSize - newRow.length() % tabSize;
					if(DEBUG){
						System.out.println("Found tab. Spaces left: "+spacesLeft);
					}
					String spaces = StringUtils.repeatString(" ", spacesLeft);
					newRow.append(spaces);
				} else {
					String character = Character.toString(chars[i]);
					newRow.append(character);
				}
			}
			rows.set(rowIndex, newRow);
			rowIndex++;
		}
	}
	
	/**
	 * @return
	 */
	protected ArrayList<StringBuffer> getRows() {
		return rows;
	}
	
	public class CellColorPair{
		public CellColorPair(Cell cell, Color color){
			this.cell = cell;
			this.color = color;
		}
		public Color color;
		public Cell cell;
	}

	public class CellStringPair{
		public CellStringPair(Cell cell, String string){
			this.cell = cell;
			this.string = string;
		}
		public Cell cell;
		public String string;
	}

	public class CellTagPair{
		public CellTagPair(Cell cell, String tag){
			this.cell = cell;
			this.tag = tag;
		}
		public Cell cell;
		public String tag;
	}

	
	public class Cell{

		public int x, y;
		
		public Cell(Cell cell){
			this(cell.x, cell.y);
		}
		
		public Cell(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public Cell getNorth(){ return new Cell(x, y - 1); }
		public Cell getSouth(){ return new Cell(x, y + 1); }
		public Cell getEast(){ return new Cell(x + 1, y); }
		public Cell getWest(){ return new Cell(x - 1, y); }

		public Cell getNW(){ return new Cell(x - 1, y - 1); }
		public Cell getNE(){ return new Cell(x + 1, y - 1); }
		public Cell getSW(){ return new Cell(x - 1, y + 1); }
		public Cell getSE(){ return new Cell(x + 1, y + 1); }

		public CellSet getNeighbours4(){
			CellSet result = new CellSet();

			result.add(getNorth());
			result.add(getSouth());
			result.add(getWest());
			result.add(getEast());

			return result;
		}

		public CellSet getNeighbours8(){
			CellSet result = new CellSet();

			result.add(getNorth());
			result.add(getSouth());
			result.add(getWest());
			result.add(getEast());

			result.add(getNW());
			result.add(getNE());
			result.add(getSW());
			result.add(getSE());

			return result;
		}


		public boolean isNorthOf(Cell cell){
			if(this.y < cell.y) return true;
			return false;
		}

		public boolean isSouthOf(Cell cell){
			if(this.y > cell.y) return true;
			return false;
		}

		public boolean isWestOf(Cell cell){
			if(this.x < cell.x) return true;
			return false;
		}

		public boolean isEastOf(Cell cell){
			if(this.x > cell.x) return true;
			return false;
		}


		public boolean equals(Object o){
			Cell cell = (Cell) o;
			if(cell == null) return false;
			if(x == cell.x && y == cell.y) return true;
			else return false;
		}
		
		public int hashCode() {
			return (x << 16) | y;
		}
		
		public boolean isNextTo(int x2, int y2){
			if(Math.abs(x2 - x) == 1 && Math.abs(y2 - y) == 1) return false;
			if(Math.abs(x2 - x) == 1 && y2 == y) return true;
			if(Math.abs(y2 - y) == 1 && x2 == x) return true;
			return false;
		}
		
		public boolean isNextTo(Cell cell){
			if(cell == null) throw new IllegalArgumentException("cell cannot be null");
			return this.isNextTo(cell.x, cell.y);
		}
		
		public String toString(){
			return "("+x+", "+y+")";
		}
		
		public void scale(int s){
			x = x * s;
			y = y * s;
		}
		
	}

	private class LineSegment{
		int x1, x2, y, dy;
		public LineSegment(int x1, int x2, int y, int dy){
			this.x1 = x1;
			this.x2 = x2;
			this.y = y;
			this.dy = dy;
		}
	}
}

