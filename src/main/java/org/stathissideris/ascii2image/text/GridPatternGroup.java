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
public class GridPatternGroup extends ArrayList<GridPattern> {
	public boolean areAllMatchedBy(TextGrid grid){
		Iterator<GridPattern> it = iterator();
		while (it.hasNext()) {
			GridPattern pattern = it.next();
			if(!pattern.isMatchedBy(grid)) return false;
		}
		return true;
	}

	public boolean isAnyMatchedBy(TextGrid grid){
		Iterator<GridPattern> it = iterator();
		while (it.hasNext()) {
			GridPattern pattern = it.next();
			if(pattern.isMatchedBy(grid)) return true;
		}
		return false;
	}
	
	
	public void add(GridPattern... patterns) {
		for(GridPattern p : patterns) add(p);
	}
	
	//TODO: define criteria for on-line type?
	
	public static final GridPatternGroup cornerCriteria = new GridPatternGroup();
	public static final GridPatternGroup normalCornerCriteria = new GridPatternGroup();
	public static final GridPatternGroup roundCornerCriteria = new GridPatternGroup();

	public static final GridPatternGroup corner1Criteria = new GridPatternGroup();
	public static final GridPatternGroup corner2Criteria = new GridPatternGroup();
	public static final GridPatternGroup corner3Criteria = new GridPatternGroup();
	public static final GridPatternGroup corner4Criteria = new GridPatternGroup();

	
	public static final GridPatternGroup normalCorner1Criteria = new GridPatternGroup();
	public static final GridPatternGroup normalCorner2Criteria = new GridPatternGroup();
	public static final GridPatternGroup normalCorner3Criteria = new GridPatternGroup();
	public static final GridPatternGroup normalCorner4Criteria = new GridPatternGroup();

	public static final GridPatternGroup roundCorner1Criteria = new GridPatternGroup();
	public static final GridPatternGroup roundCorner2Criteria = new GridPatternGroup();
	public static final GridPatternGroup roundCorner3Criteria = new GridPatternGroup();
	public static final GridPatternGroup roundCorner4Criteria = new GridPatternGroup();
	
	public static final GridPatternGroup intersectionCriteria = new GridPatternGroup();
	public static final GridPatternGroup TCriteria = new GridPatternGroup();
	public static final GridPatternGroup inverseTCriteria = new GridPatternGroup();
	public static final GridPatternGroup KCriteria = new GridPatternGroup();
	public static final GridPatternGroup inverseKCriteria = new GridPatternGroup();

	public static final GridPatternGroup crossCriteria = new GridPatternGroup();
	
	public static final GridPatternGroup stubCriteria = new GridPatternGroup();
	public static final GridPatternGroup verticalLinesEndCriteria = new GridPatternGroup();
	public static final GridPatternGroup horizontalLinesEndCriteria = new GridPatternGroup();
	public static final GridPatternGroup linesEndCriteria = new GridPatternGroup();
	
	public static final GridPatternGroup crossOnLineCriteria = new GridPatternGroup();
	public static final GridPatternGroup horizontalCrossOnLineCriteria = new GridPatternGroup();
	public static final GridPatternGroup verticalCrossOnLineCriteria = new GridPatternGroup();

	public static final GridPatternGroup starOnLineCriteria = new GridPatternGroup();
	public static final GridPatternGroup horizontalStarOnLineCriteria = new GridPatternGroup();
	public static final GridPatternGroup verticalStarOnLineCriteria = new GridPatternGroup();

	public static final GridPatternGroup loneDiagonalCriteria = new GridPatternGroup();

	static {
		GridPattern crossPattern1 = new GridPattern(
				".6.",
				"4+8",
				".2."
			);
		crossCriteria.add(crossPattern1);
		
		GridPattern KPattern1 = new GridPattern(
				".6.",
				"%4+8",
				".2."
			);
		KCriteria.add(KPattern1);

		GridPattern inverseKPattern1 = new GridPattern(
				".6.",
				"4+%8",
				".2."
			);
		inverseKCriteria.add(inverseKPattern1);

		GridPattern TPattern1 = new GridPattern(
				".%6.",
				"4+8",
				".2."
			);
		TCriteria.add(TPattern1);

		GridPattern inverseTPattern1 = new GridPattern(
				".6.",
				"4+8",
				".%2."
			);
		inverseTCriteria.add(inverseTPattern1);


		// ****** normal corners *******

		GridPattern normalCorner1Pattern1 = new GridPattern(
				".[.",
				"~+(",
				".^."
			);
		normalCorner1Criteria.add(normalCorner1Pattern1);

		GridPattern normalCorner2Pattern1 = new GridPattern(
				".[.",
				"(+~",
				".^."
			);
		normalCorner2Criteria.add(normalCorner2Pattern1);

		GridPattern normalCorner3Pattern1 = new GridPattern(
				".^.",
				"(+~",
				".[."
			);
		normalCorner3Criteria.add(normalCorner3Pattern1);

		GridPattern normalCorner4Pattern1 = new GridPattern(
				".^.",
				"~+(",
				".[."
			);
		normalCorner4Criteria.add(normalCorner4Pattern1);

		// ******* round corners *******

		GridPattern roundCorner1Pattern1 = new GridPattern(
				".[.",
				"~/4",
				".2."
			);
		roundCorner1Criteria.add(roundCorner1Pattern1);

		GridPattern roundCorner2Pattern1 = new GridPattern(
				".[.",
				"4\\~",
				".2."
			);
		roundCorner2Criteria.add(roundCorner2Pattern1);

		GridPattern roundCorner3Pattern1 = new GridPattern(
				".6.",
				"4/~",
				".[."
			);
		roundCorner3Criteria.add(roundCorner3Pattern1);

		GridPattern roundCorner4Pattern1 = new GridPattern(
				".6.",
				"~\\8",
				".[."
			);
		roundCorner4Criteria.add(roundCorner4Pattern1);

		//stubs

		GridPattern stubPattern1 = new GridPattern(
				"!^!",
				"!+!",
				".!."
			);
		stubCriteria.add(stubPattern1);

		GridPattern stubPattern2 = new GridPattern(
				"!^!",
				"!+!",
				".-."
			);
		stubCriteria.add(stubPattern2);

		GridPattern stubPattern3 = new GridPattern(
				"!!.",
				"(+!",
				"!!."
			);
		stubCriteria.add(stubPattern3);

		GridPattern stubPattern4 = new GridPattern(
				"!!.",
				"(+|",
				"!!."
			);
		stubCriteria.add(stubPattern4);

		GridPattern stubPattern5 = new GridPattern(
				".!.",
				"!+!",
				"!^!"
			);
		stubCriteria.add(stubPattern5);

		GridPattern stubPattern6 = new GridPattern(
				".-.",
				"!+!",
				"!^!"
			);
		stubCriteria.add(stubPattern6);
		
		GridPattern stubPattern7 = new GridPattern(
				".!!",
				"!+(",
				".!!"
			);
		stubCriteria.add(stubPattern7);

		GridPattern stubPattern8 = new GridPattern(
				".!!",
				"|+(",
				".!!"
			);
		stubCriteria.add(stubPattern8);


		// ****** ends of lines ******
		GridPattern verticalLinesEndPattern1 = new GridPattern(
				".^.",
				".|.",
				".!."
			);
		verticalLinesEndCriteria.add(verticalLinesEndPattern1);

		GridPattern verticalLinesEndPattern2 = new GridPattern(
				".^.",
				".|.",
				".-."
			);
		verticalLinesEndCriteria.add(verticalLinesEndPattern2);

		GridPattern horizontalLinesEndPattern3 = new GridPattern(
				"...",
				"(-!",
				"..."
			);
		horizontalLinesEndCriteria.add(horizontalLinesEndPattern3);

		GridPattern horizontalLinesEndPattern4 = new GridPattern(
				"...",
				"(-|",
				"..."
			);
		horizontalLinesEndCriteria.add(horizontalLinesEndPattern4);

		GridPattern verticalLinesEndPattern5 = new GridPattern(
				".!.",
				".|.",
				".^."
			);
		verticalLinesEndCriteria.add(verticalLinesEndPattern5);

		GridPattern verticalLinesEndPattern6 = new GridPattern(
				".-.",
				".|.",
				".^."
			);
		verticalLinesEndCriteria.add(verticalLinesEndPattern6);

		GridPattern horizontalLinesEndPattern7 = new GridPattern(
				"...",
				"!-(",
				"..."
			);
		horizontalLinesEndCriteria.add(horizontalLinesEndPattern7);

		GridPattern horizontalLinesEndPattern8 = new GridPattern(
				"...",
				"|-(",
				"..."
			);
		horizontalLinesEndCriteria.add(horizontalLinesEndPattern8);



		// ****** others *******

		GridPattern horizontalCrossOnLinePattern1 = new GridPattern(
				"...",
				"(+(",
				"..."
			);
		horizontalCrossOnLineCriteria.add(horizontalCrossOnLinePattern1);

		GridPattern verticalCrossOnLinePattern1 = new GridPattern(
				".^.",
				".+.",
				".^."
			);
		verticalCrossOnLineCriteria.add(verticalCrossOnLinePattern1);


		GridPattern horizontalStarOnLinePattern1 = new GridPattern(
				"...",
				"(*(",
				"..."
			);
		horizontalStarOnLineCriteria.add(horizontalStarOnLinePattern1);

		GridPattern horizontalStarOnLinePattern2 = new GridPattern(
				"...",
				"!*(",
				"..."
			);
		horizontalStarOnLineCriteria.add(horizontalStarOnLinePattern2);

		GridPattern horizontalStarOnLinePattern3 = new GridPattern(
				"...",
				"(*!",
				"..."
			);
		horizontalStarOnLineCriteria.add(horizontalStarOnLinePattern3);


		GridPattern verticalStarOnLinePattern1 = new GridPattern(
				".^.",
				".*.",
				".^."
			);
		verticalStarOnLineCriteria.add(verticalStarOnLinePattern1);

		GridPattern verticalStarOnLinePattern2 = new GridPattern(
				".!.",
				".*.",
				".^."
			);
		verticalStarOnLineCriteria.add(verticalStarOnLinePattern2);

		GridPattern verticalStarOnLinePattern3 = new GridPattern(
				".^.",
				".*.",
				".!."
			);
		verticalStarOnLineCriteria.add(verticalStarOnLinePattern3);

		
		GridPattern loneDiagonalPattern1 = new GridPattern(
				".%6%7",
				"%4/%8",
				"%3%2."
			);
		loneDiagonalCriteria.add(loneDiagonalPattern1);

		GridPattern loneDiagonalPattern2 = new GridPattern(
				"%1%6.",
				"%4\\%8",
				".%2%5"
			);
		loneDiagonalCriteria.add(loneDiagonalPattern2);


		//groups
		
		intersectionCriteria.addAll(crossCriteria);
		intersectionCriteria.addAll(KCriteria);
		intersectionCriteria.addAll(TCriteria);
		intersectionCriteria.addAll(inverseKCriteria);
		intersectionCriteria.addAll(inverseTCriteria);

		normalCornerCriteria.addAll(normalCorner1Criteria);
		normalCornerCriteria.addAll(normalCorner2Criteria);
		normalCornerCriteria.addAll(normalCorner3Criteria);
		normalCornerCriteria.addAll(normalCorner4Criteria);

		roundCornerCriteria.addAll(roundCorner1Criteria);
		roundCornerCriteria.addAll(roundCorner2Criteria);
		roundCornerCriteria.addAll(roundCorner3Criteria);
		roundCornerCriteria.addAll(roundCorner4Criteria);
		
		corner1Criteria.addAll(normalCorner1Criteria);
		corner1Criteria.addAll(roundCorner1Criteria);

		corner2Criteria.addAll(normalCorner2Criteria);
		corner2Criteria.addAll(roundCorner2Criteria);

		corner3Criteria.addAll(normalCorner3Criteria);
		corner3Criteria.addAll(roundCorner3Criteria);

		corner4Criteria.addAll(normalCorner4Criteria);
		corner4Criteria.addAll(roundCorner4Criteria);

		cornerCriteria.addAll(normalCornerCriteria);
		cornerCriteria.addAll(roundCornerCriteria);
		
		crossOnLineCriteria.addAll(horizontalCrossOnLineCriteria);
		crossOnLineCriteria.addAll(verticalCrossOnLineCriteria);

		starOnLineCriteria.addAll(horizontalStarOnLineCriteria);
		starOnLineCriteria.addAll(verticalStarOnLineCriteria);


		linesEndCriteria.addAll(horizontalLinesEndCriteria);
		linesEndCriteria.addAll(verticalLinesEndCriteria);		
		linesEndCriteria.addAll(stubCriteria);

	}
}
