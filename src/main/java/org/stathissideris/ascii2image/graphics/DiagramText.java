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
import java.awt.Font;
import java.awt.geom.Rectangle2D;

/**
 * 
 * @author Efstathios Sideris
 */
public class DiagramText extends DiagramComponent {

	public static final Color DEFAULT_COLOR = Color.black;
	
	private String text;
	private Font font;
	private int xPos, yPos;
	private Color color = Color.black;
	private boolean isTextOnLine = false;
	private boolean hasOutline = false;
	private Color outlineColor = Color.white;

	public DiagramText(int x, int y, String text, Font font){
		if(text == null) throw new IllegalArgumentException("DiagramText cannot be initialised with a null string");
		if(font == null) throw new IllegalArgumentException("DiagramText cannot be initialised with a null font");

		this.xPos = x;
		this.yPos = y;
		this.text = text;
		this.font = font;
	}

	public void centerInBounds(Rectangle2D bounds){
		centerHorizontallyBetween((int) bounds.getMinX(), (int) bounds.getMaxX());
		centerVerticallyBetween((int) bounds.getMinY(), (int) bounds.getMaxY());
	}

	public void centerHorizontallyBetween(int minX, int maxX){
		int width = FontMeasurer.instance().getWidthFor(text, font);
		int center = Math.abs(maxX - minX) / 2;
		xPos += Math.abs(center - width / 2);
		
	}

	public void centerVerticallyBetween(int minY, int maxY){
		int zHeight = FontMeasurer.instance().getZHeight(font);
		int center = Math.abs(maxY - minY) / 2;
		yPos -= Math.abs(center - zHeight / 2);
	}

	public void alignRightEdgeTo(int x){
		int width = FontMeasurer.instance().getWidthFor(text, font);
		xPos = x - width;
	}


	/**
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return
	 */
	public int getXPos() {
		return xPos;
	}

	/**
	 * @return
	 */
	public int getYPos() {
		return yPos;
	}

	/**
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @param font
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * @param string
	 */
	public void setText(String string) {
		text = string;
	}

	/**
	 * @param i
	 */
	public void setXPos(int i) {
		xPos = i;
	}

	/**
	 * @param i
	 */
	public void setYPos(int i) {
		yPos = i;
	}

	public Rectangle2D getBounds(){
		Rectangle2D bounds = FontMeasurer.instance().getBoundsFor(text, font);
		bounds.setRect(
			bounds.getMinX() + xPos,
			bounds.getMinY() + yPos,
			bounds.getWidth(),
			bounds.getHeight());
		return bounds;
	}
	
	public String toString(){
		return "DiagramText, at ("+xPos+", "+yPos+"), within "+getBounds()+" '"+text+"', "+color+" "+font;
	}

	/**
	 * @return
	 */
	public boolean isTextOnLine() {
		return isTextOnLine;
	}

	/**
	 * @param b
	 */
	public void setTextOnLine(boolean b) {
		isTextOnLine = b;
	}

	public boolean hasOutline() {
		return hasOutline;
	}

	public void setHasOutline(boolean hasOutline) {
		this.hasOutline = hasOutline;
	}

	public Color getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}

	
}
