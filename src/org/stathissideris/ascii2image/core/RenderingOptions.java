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
package org.stathissideris.ascii2image.core;

import java.util.HashMap;

import org.stathissideris.ascii2image.graphics.CustomShapeDefinition;

/**
 * 
 * @author Efstathios Sideris
 */
public class RenderingOptions {

	private HashMap<String, CustomShapeDefinition> customShapes;
	
	private boolean dropShadows = true;
	private boolean renderDebugLines = false;
	private boolean antialias = true;

	private int cellWidth = 10;
	private int cellHeight = 14;
	
	private float scale = 1;

	/**
	 * @return
	 */
	public int getCellHeight() {
		return cellHeight;
	}

	/**
	 * @return
	 */
	public int getCellWidth() {
		return cellWidth;
	}

	/**
	 * @return
	 */
	public boolean dropShadows() {
		return dropShadows;
	}

	/**
	 * @return
	 */
	public boolean renderDebugLines() {
		return renderDebugLines;
	}

	/**
	 * @return
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * @param b
	 */
	public void setDropShadows(boolean b) {
		dropShadows = b;
	}

	/**
	 * @param b
	 */
	public void setRenderDebugLines(boolean b) {
		renderDebugLines = b;
	}

	/**
	 * @param f
	 */
	public void setScale(float f) {
		scale = f;
		cellWidth *= scale;
		cellHeight *= scale;
	}

	/**
	 * @return
	 */
	public boolean performAntialias() {
		return antialias;
	}

	/**
	 * @param b
	 */
	public void setAntialias(boolean b) {
		antialias = b;
	}

}
