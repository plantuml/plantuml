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

/**
 * 
 * @author Efstathios Sideris
 */
public class AbstractCell {

	public int rows[][] = new int[3][3];
	{
		for(int y = 0; y < 3; y++)
			for(int x = 0; x < 3; x++)
				rows[x][y] = 0;
	}

	static AbstractCell makeHorizontalLine(){
		AbstractCell result = new AbstractCell();
		result.rows[0][1] = 1;
		result.rows[1][1] = 1;
		result.rows[2][1] = 1;
		return result;
	}

	static AbstractCell makeVerticalLine(){
		AbstractCell result = new AbstractCell();
		result.rows[1][0] = 1;
		result.rows[1][1] = 1;
		result.rows[1][2] = 1;
		return result;
	}

	static AbstractCell makeCorner1(){
		AbstractCell result = new AbstractCell();
		result.rows[1][1] = 1;
		result.rows[1][2] = 1;
		result.rows[2][1] = 1;
		return result;
	}

	static AbstractCell makeCorner2(){
		AbstractCell result = new AbstractCell();
		result.rows[0][1] = 1;
		result.rows[1][1] = 1;
		result.rows[1][2] = 1;
		return result;
	}

	static AbstractCell makeCorner3(){
		AbstractCell result = new AbstractCell();
		result.rows[0][1] = 1;
		result.rows[1][1] = 1;
		result.rows[1][0] = 1;
		return result;
	}

	static AbstractCell makeCorner4(){
		AbstractCell result = new AbstractCell();
		result.rows[2][1] = 1;
		result.rows[1][1] = 1;
		result.rows[1][0] = 1;
		return result;
	}

	static AbstractCell makeT(){
		AbstractCell result = AbstractCell.makeHorizontalLine();
		result.rows[1][2] = 1;
		return result;
	}

	static AbstractCell makeInverseT(){
		AbstractCell result = AbstractCell.makeHorizontalLine();
		result.rows[1][0] = 1;
		return result;
	}

	static AbstractCell makeK(){
		AbstractCell result = AbstractCell.makeVerticalLine();
		result.rows[2][1] = 1;
		return result;
	}

	static AbstractCell makeInverseK(){
		AbstractCell result = AbstractCell.makeVerticalLine();
		result.rows[0][1] = 1;
		return result;
	}

	static AbstractCell makeCross(){
		AbstractCell result = AbstractCell.makeVerticalLine();
		result.rows[0][1] = 1;
		result.rows[2][1] = 1;
		return result;
	}

	static AbstractCell makeStar(){
		AbstractCell result = AbstractCell.makeVerticalLine();
		for(int y = 0; y < 3; y++)
			for(int x = 0; x < 3; x++)
				result.rows[x][y] = 1;
		return result;
	}


}
