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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Locale;

/**
 * 
 * @author Efstathios Sideris
 */
public class FontMeasurer {
	
	private static final String fontFamilyName = "Dialog";
	//private static final String fontFamilyName = "Helvetica";
	
	private static final boolean DEBUG = false;
	
	private static final FontMeasurer instance = new FontMeasurer();
	FontRenderContext fakeRenderContext;
	Graphics2D fakeGraphics;
	
	{   
		BufferedImage image = new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB);
		fakeGraphics = image.createGraphics();
		
		if (DEBUG) System.out.println("Locale: "+Locale.getDefault());
		
		fakeRenderContext = fakeGraphics.getFontRenderContext();
	}		 
	

	public int getWidthFor(String str, int pixelHeight){
		Font font = getFontFor(pixelHeight);
		Rectangle2D rectangle = font.getStringBounds(str, fakeRenderContext);
		return (int) rectangle.getWidth();
	}

	public int getHeightFor(String str, int pixelHeight){
		Font font = getFontFor(pixelHeight);
		Rectangle2D rectangle = font.getStringBounds(str, fakeRenderContext);
		return (int) rectangle.getHeight();
	}

	public int getWidthFor(String str, Font font){
		Rectangle2D rectangle = font.getStringBounds(str, fakeRenderContext);
		return (int) rectangle.getWidth();
	}

	public int getHeightFor(String str, Font font){
		Rectangle2D rectangle = font.getStringBounds(str, fakeRenderContext);
		return (int) rectangle.getHeight();
	}
	
	public Rectangle2D getBoundsFor(String str, Font font){
		return font.getStringBounds(str, fakeRenderContext);
	}
	
	public Font getFontFor(int pixelHeight){
		BufferedImage image = new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		return getFontFor(pixelHeight, fakeRenderContext);
	}

	public int getAscent(Font font){
		fakeGraphics.setFont(font);
		FontMetrics metrics = fakeGraphics.getFontMetrics();
		if(DEBUG) System.out.println("Ascent: "+metrics.getAscent());
		return metrics.getAscent();
	}

	public int getZHeight(Font font){
		int height = (int) font.createGlyphVector(fakeRenderContext, "Z").getOutline().getBounds().getHeight();
		if(DEBUG) System.out.println("Z height: "+height);
		return height;
	}

	public Font getFontFor(int maxWidth, String string){
		float size = 12;
		Font currentFont = new Font(fontFamilyName, Font.BOLD, (int) size);
		//ascent is the distance between the baseline and the tallest character
		int width = getWidthFor(string, currentFont);

		int direction; //direction of size change (towards smaller or bigger)
		if(width > maxWidth){
			currentFont = currentFont.deriveFont(size - 1);
			size--;
			direction = -1; 
		} else {
			currentFont = currentFont.deriveFont(size + 1);
			size++;
			direction = 1;
		}
		while(size > 0){
			currentFont = currentFont.deriveFont(size);
			//rectangle = currentFont.getStringBounds(testString, frc);
			width = getWidthFor(string, currentFont);
			if(direction == 1){
				if(width > maxWidth){
					size = size - 1;
					return currentFont.deriveFont(size);
				}
				else size = size + 1;
			} else {
				if(width < maxWidth)
					return currentFont;
				else size = size - 1;
			}
		}
		return null;
	}



	public Font getFontFor(int pixelHeight, FontRenderContext frc){
		float size = 12;
		Font currentFont = new Font(fontFamilyName, Font.BOLD, (int) size);
//		Font currentFont = new Font("Times", Font.BOLD, (int) size);
		if (DEBUG) System.out.println(currentFont.getFontName());
		//ascent is the distance between the baseline and the tallest character
		int ascent = getAscent(currentFont);

		int direction; //direction of size change (towards smaller or bigger)
		if(ascent > pixelHeight){
			currentFont = currentFont.deriveFont(size - 1);
			size--;
			direction = -1; 
		} else {
			currentFont = currentFont.deriveFont(size + 1);
			size++;
			direction = 1;
		}
		while(size > 0){
			currentFont = currentFont.deriveFont(size);
			//rectangle = currentFont.getStringBounds(testString, frc);
			ascent = getAscent(currentFont);
			if(direction == 1){
				if(ascent > pixelHeight){
					size = size - 0.5f;
					return currentFont.deriveFont(size);
				}
				else size = size + 0.5f;
			} else {
				if(ascent < pixelHeight)
					return currentFont;
				else size = size - 0.5f;
			}
		}
		return null;
	}
	
	public static FontMeasurer instance(){
		return instance;
	}
	
	public FontMeasurer(){
	}
	
	public static void main(String[] args) {
		//FontMeasurer.instance().getFontFor(7);
		float size = 12;
		Font currentFont = new Font("Sans", Font.BOLD, (int) size);
		System.out.println(currentFont.getSize());
		currentFont = currentFont.deriveFont(--size);
		System.out.println(currentFont.getSize());
		currentFont = currentFont.deriveFont(--size);
		System.out.println(currentFont.getSize());
		currentFont = currentFont.deriveFont(--size);
		System.out.println(currentFont.getSize());
		currentFont = currentFont.deriveFont(--size);
		System.out.println(currentFont.getSize());
		currentFont = currentFont.deriveFont(--size);
		System.out.println(currentFont.getSize());
	}
}
