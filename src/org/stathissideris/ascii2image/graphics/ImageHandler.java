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

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JLabel;

public class ImageHandler {
	
//	private static OffScreenSVGRenderer svgRenderer = 
//		new OffScreenSVGRenderer();
	
	private static ImageHandler instance = new ImageHandler();
	
	public static ImageHandler instance(){
		return instance;
	}
	
	private static final MediaTracker tracker = new MediaTracker(new JLabel());
	
	public Image loadImage(String filename){
		URL url = ClassLoader.getSystemResource(filename);
		Image result = null;
		if(url != null)
			result = Toolkit.getDefaultToolkit().getImage(url);
		else
			result = Toolkit.getDefaultToolkit().getImage(filename);
//			result = null;

		//wait for the image to load before returning
		tracker.addImage(result, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException e) {
			System.err.println("Failed to load image "+filename);
			e.printStackTrace();
		}
		tracker.removeImage(result, 0);
		
		return result;
	}
	
//	public BufferedImage renderSVG(String filename, int width, int height, boolean stretch) throws IOException {
//		File file = new File(filename);
//		URI uri = file.toURI();
//		return svgRenderer.renderToImage(uri.toString(), width, height, stretch, null, null);
//	}
//
//	public BufferedImage renderSVG(String filename, int width, int height, boolean stretch, String idRegex, Color color) throws IOException {
//		File file = new File(filename);
//		URI uri = file.toURI();
//		return svgRenderer.renderToImage(uri.toString(), width, height, stretch, idRegex, color);
//	}

	
}
