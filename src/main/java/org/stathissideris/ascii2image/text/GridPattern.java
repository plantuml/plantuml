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
import java.util.regex.Pattern;

/**
 * This is a TextGrid (usually 3x3) that contains the equivalent of a
 * 2D reqular expression (which uses custom syntax to make things more
 * visual, but standard syntax is also possible).
 * 
 * The custom syntax is:
 * . means anything
 * b means any boundary (any of  - = / \ + | :)
 * ! means not boundary (none of  - = / \ + | :)
 * - means - or =
 * | means | or :
 * [ means not | nor :
 * ~ means not - nor =
 * ^ means a boundary but not - nor =
 * ( means a boundary but not | nor :
 * s means a straight boundary (one of - = + | :)
 * S means not a straight boundary (none of - = + | :)
 * 
 * 1 means a cell that has entry point 1
 * 2 means a cell that has entry point 2
 * 3 means a cell that has entry point 3 etc. up to number 8
 * 
 * %1 means a cell that does not have entry point 1 etc.
 * 
 * See below for an explanation of entry points
 * 
 * +, \, / and the space are literal (as is any other character)
 * 
 * 
 * Entry points
 * 
 * <pre>
 * 1   2   3
 *  *--*--*
 *  |     |
 * 8*     *4
 *  |     |
 *  *--*--*
 * 7   6   5
 * </pre>
 * 
 * We number the entry points for each cell as in the diagram
 * above. If a cell is occupied by a character, we define as
 * entry points the points of the above diagram that the character
 * can touch with the end of its lines. For example - has
 * entry points 8 and 4, | and : have entry points 2 and 6,
 * / has 3 and 7, \ has 1 and 5, + has 2, 6, 8 and 4 etc.   
 * 
 * 
 * @author Efstathios Sideris
 */
public class GridPattern extends TextGrid {
	
	private ArrayList<Pattern> regExps = new ArrayList<Pattern>(); //TODO optimise: store as PatternS
	private boolean regExpsAreValid = false;
	
	private static final boolean DEBUG = false;
	
	private boolean usesStandardSyntax = false;

	public GridPattern(){
		super(3, 3);
	}

	public GridPattern(String row1, String row2, String row3){
		super(Math.max(Math.max(row1.length(), row2.length()), row3.length()), 3);
		setTo(row1, row2, row3);
		regExpsAreValid = false;
	}

	public boolean usesStandardSyntax() {
		return usesStandardSyntax;
	}

	public void setUsesStandardSyntax(boolean b) {
		usesStandardSyntax = b;
		regExpsAreValid = false;
	}

	public boolean isMatchedBy(TextGrid grid){
		/*if(grid.getHeight() != this.getHeight()
			|| grid.getWidth() != this.getWidth()) return false;*/
		if(!regExpsAreValid) prepareRegExps(); 

		for(int i = 0; i < grid.getHeight(); i++) {
			String row = grid.getRow(i).toString();
			Pattern regexp = regExps.get(i);
			if(!regexp.matcher(row).matches()) {
				if(DEBUG)
					System.out.println(row+" does not match "+regexp);
				return false;
			} 
		}
		return true;
	}
	
	private void prepareRegExps(){
		regExpsAreValid = true;
		regExps.clear();
		if (DEBUG)
			System.out.println("Trying to match:");
		if(!usesStandardSyntax){
			Iterator<StringBuffer> it = getRows().iterator();
			while (it.hasNext()) {
				String row = it.next().toString();
				regExps.add(Pattern.compile(makeRegExp(row)));
				if(DEBUG)
					System.out.println(row+" becomes "+makeRegExp(row));
			}			
		} else {
			Iterator<StringBuffer> it = getRows().iterator();
			while (it.hasNext()) {
				String row = it.next().toString();
				regExps.add(Pattern.compile(row));
			}
		}
	}
	
	private String makeRegExp(String pattern){
		StringBuilder result = new StringBuilder();
		int tokensHandled = 0;
		for(int i = 0; i < pattern.length() && tokensHandled < 3; i++){
			char c = pattern.charAt(i);
			if(c == '[') {
				result.append("[^|:]");
			} else if(c == '|') {
				result.append("[|:]");
			} else if(c == '-') {
				result.append("-");
			} else if(c == '!') {
				result.append("[^-=\\/\\\\+|:]");
			} else if(c == 'b') {
				result.append("[-=\\/\\\\+|:]");
			} else if(c == '^') {
				result.append("[\\/\\\\+|:]");
			} else if(c == '(') {
				result.append("[-=\\/\\\\+]");
			} else if(c == '~') {
				result.append(".");
			} else if(c == '+') {
				result.append("\\+");
			} else if(c == '\\') {
				result.append("\\\\");
			} else if(c == 's') {
				result.append("[-=+|:]");
			} else if(c == 'S') {
				result.append("[\\/\\\\]");
			} else if(c == '*') {
				result.append("\\*");
			
			//entry points
			} else if(c == '1') {
				result.append("[\\\\]");

			} else if(c == '2') {
				result.append("[|:+\\/\\\\]");

			} else if(c == '3') {
				result.append("[\\/]");

			} else if(c == '4') {
				result.append("[-=+\\/\\\\]");

			} else if(c == '5') {
				result.append("[\\\\]");

			} else if(c == '6') {
				result.append("[|:+\\/\\\\]");

			} else if(c == '7') {
				result.append("[\\/]");

			} else if(c == '8') {
				result.append("[-=+\\/\\\\]");
				
			//entry point negations
			} else if(c == '%') {
				if(i+1 > pattern.length()){
					throw new RuntimeException("Invalid pattern, found % at the end");
				}
				c = pattern.charAt(++i);
				
				if(c == '1') {
					result.append("[^\\\\]");

				} else if(c == '2') {
					result.append("[^|:+\\/\\\\]");

				} else if(c == '3') {
					result.append("[^\\/]");

				} else if(c == '4') {
					result.append("[^-=+\\/\\\\]");

				} else if(c == '5') {
					result.append("[^\\\\]");

				} else if(c == '6') {
					result.append("[^|:+\\/\\\\]");

				} else if(c == '7') {
					result.append("[^\\/]");

				} else if(c == '8') {
					result.append("[^-=+\\/\\\\]");
				}
			} else result.append(String.valueOf(c));
			tokensHandled++;
		}
		return result.toString();
	}


	public void setTo(String row1, String row2, String row3){
		if(getHeight() != 3) throw new RuntimeException("This method can only be called for GridPatternS with height 3");
		regExpsAreValid = false;
		writeStringTo(0, 0, row1);
		writeStringTo(0, 1, row2);
		writeStringTo(0, 2, row3);
		//don't use setRow() here!
	}

	public static void main(String[] args) {		
		TextGrid grid = new TextGrid(3, 3);
//		grid.setRow(0, "   ");
//		grid.setRow(1, "-\\ ");
//		grid.setRow(2, " | ");
//		
//		if(GridPatternGroup.corner2Criteria.isAnyMatchedBy(grid)){
//			System.out.println("Grid is corner 2");
//		} else {
//			System.out.println("Grid is not corner 2");
//		}
//
//		if(grid.isCorner2(grid.new Cell(1,1))){
//			System.out.println("Grid is corner 2");
//		} else {
//			System.out.println("Grid is not corner 2");
//		}
//
//		
//		grid.setRow(0, "-+ ");
//		grid.setRow(1, " | ");
//		grid.setRow(2, "-+ ");
//
//		if(GridPatternGroup.cornerCriteria.isAnyMatchedBy(grid)){			
//			System.out.println("Grid is corner");
//		} else {
//			System.out.println("Grid is not corner");
//		}
//
//		if(grid.isCorner(grid.new Cell(1,1))){			
//			System.out.println("Grid is corner");
//		} else {
//			System.out.println("Grid is not corner");
//		}

		grid.setRow(0, "---");
		grid.setRow(1, " / ");
		grid.setRow(2, "---");
		grid.printDebug();
		if(GridPatternGroup.loneDiagonalCriteria.isAnyMatchedBy(grid)){			
			System.out.println("Grid is lone diagonal");
		} else {
			System.out.println("Grid is not lone diagonal");
		}

		grid.setRow(0, "--/");
		grid.setRow(1, " / ");
		grid.setRow(2, "---");
		grid.printDebug();
		if(GridPatternGroup.loneDiagonalCriteria.isAnyMatchedBy(grid)){			
			System.out.println("Grid is lone diagonal");
		} else {
			System.out.println("Grid is not lone diagonal");
		}

		grid.setRow(0, "-- ");
		grid.setRow(1, " \\ ");
		grid.setRow(2, "---");
		grid.printDebug();
		if(GridPatternGroup.loneDiagonalCriteria.isAnyMatchedBy(grid)){			
			System.out.println("Grid is lone diagonal");
		} else {
			System.out.println("Grid is not lone diagonal");
		}

		grid.setRow(0, "-- ");
		grid.setRow(1, " \\ ");
		grid.setRow(2, "--\\");
		grid.printDebug();
		if(GridPatternGroup.loneDiagonalCriteria.isAnyMatchedBy(grid)){			
			System.out.println("Grid is lone diagonal");
		} else {
			System.out.println("Grid is not lone diagonal");
		}

		grid.setRow(0, "   "); 
		grid.setRow(1, "-\\/"); 
		grid.setRow(2, " ||");
		grid.printDebug();
		if(GridPatternGroup.loneDiagonalCriteria.isAnyMatchedBy(grid)){			
			System.out.println("Grid is lone diagonal");
		} else {
			System.out.println("Grid is not lone diagonal");
		}
	}
}
