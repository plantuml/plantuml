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

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.stathissideris.ascii2image.core.RenderingOptions;
import org.stathissideris.ascii2image.core.Shape3DOrderingComparator;

/**
 * 
 * @author Efstathios Sideris
 */
public class BitmapRenderer {

	private static final boolean DEBUG = false;

	private static final String IDREGEX = "^.+_vfill$";
	
	Stroke normalStroke;
	Stroke dashStroke; 
	
//	public static void main(String[] args) throws Exception {
//		
//		
//		long startTime = System.currentTimeMillis();
//		
//		ConversionOptions options = new ConversionOptions();
//		
//		TextGrid grid = new TextGrid();
//		
//		String filename = "dak_orgstruktur_vs_be.ditaa.OutOfMemoryError.edit.txt";
//		
//		grid.loadFrom("tests/text/"+filename);
//		
//		Diagram diagram = new Diagram(grid, options);
//		new BitmapRenderer().renderToPNG(diagram, "tests/images/"+filename+".png", options.renderingOptions);
//		long endTime = System.currentTimeMillis();
//		long totalTime  = (endTime - startTime) / 1000;
//		System.out.println("Done in "+totalTime+"sec");
//		
//		File workDir = new File("tests/images");
//		//Process p = Runtime.getRuntime().exec("display "+filename+".png", null, workDir);
//	}

	private boolean renderToPNG(Diagram diagram, String filename, RenderingOptions options){	
		RenderedImage image = renderToImage(diagram, options);
		
		try {
			File file = new File(filename);
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("Error: Cannot write to file "+filename);
			return false;
		}
		return true;
	}
	
	public RenderedImage renderToImage(Diagram diagram,  RenderingOptions options){
		BufferedImage image = new BufferedImage(
					diagram.getWidth(),
					diagram.getHeight(),
					BufferedImage.TYPE_INT_RGB);
		
		return render(diagram, image, options);
	}
	
	public RenderedImage render(Diagram diagram, BufferedImage image,  RenderingOptions options){
		RenderedImage renderedImage = image;
		Graphics2D g2 = image.createGraphics();

		Object antialiasSetting = RenderingHints.VALUE_ANTIALIAS_OFF;
		if(options.performAntialias())
			antialiasSetting = RenderingHints.VALUE_ANTIALIAS_ON;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasSetting);

		g2.setColor(Color.white);
		//TODO: find out why the next line does not work
		g2.fillRect(0, 0, image.getWidth()+10, image.getHeight()+10);
		/*for(int y = 0; y < diagram.getHeight(); y ++)
			g2.drawLine(0, y, diagram.getWidth(), y);*/
		
		g2.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

		ArrayList shapes = diagram.getAllDiagramShapes();

		if(DEBUG) System.out.println("Rendering "+shapes.size()+" shapes (groups flattened)");

		Iterator shapesIt;
		if(options.dropShadows()){
			//render shadows
			shapesIt = shapes.iterator();
			while(shapesIt.hasNext()){
				DiagramShape shape = (DiagramShape) shapesIt.next();

				if(shape.getPoints().isEmpty()) continue;

				//GeneralPath path = shape.makeIntoPath();
				GeneralPath path;
				path = shape.makeIntoRenderPath(diagram);			
							
				float offset = diagram.getMinimumOfCellDimension() / 3.333f;
			
				if(path != null
						&& shape.dropsShadow()
						&& shape.getType() != DiagramShape.TYPE_CUSTOM){
					GeneralPath shadow = new GeneralPath(path);
					AffineTransform translate = new AffineTransform();
					translate.setToTranslation(offset, offset);
					shadow.transform(translate);
					g2.setColor(new Color(150,150,150));
					g2.fill(shadow);
				
				}
			}

		
			//blur shadows
		
			if(true) {
				int blurRadius = 6;
				int blurRadius2 = blurRadius * blurRadius;
				float blurRadius2F = blurRadius2;
				float weight = 1.0f / blurRadius2F;
				float[] elements = new float[blurRadius2];
				for (int k = 0; k < blurRadius2; k++)
					elements[k] = weight;
				Kernel myKernel = new Kernel(blurRadius, blurRadius, elements);

				//if EDGE_NO_OP is not selected, EDGE_ZERO_FILL is the default which creates a black border 
				ConvolveOp simpleBlur =
					new ConvolveOp(myKernel, ConvolveOp.EDGE_NO_OP, null);
								
				BufferedImage destination =
					new BufferedImage(
						image.getWidth(),
						image.getHeight(),
						image.getType());

				simpleBlur.filter(image, (BufferedImage) destination);

				//destination = destination.getSubimage(blurRadius/2, blurRadius/2, image.getWidth(), image.getHeight()); 
				g2 = (Graphics2D) destination.getGraphics();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasSetting);
				renderedImage = (RenderedImage) destination;
			}
		}

		
		//fill and stroke
		
		float dashInterval = Math.min(diagram.getCellWidth(), diagram.getCellHeight()) / 2;
		//Stroke normalStroke = g2.getStroke();
		
		float strokeWeight = diagram.getMinimumOfCellDimension() / 10;
		
		normalStroke =
		  new BasicStroke(
			strokeWeight,
			//10,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND
		  );

		dashStroke = 
		  new BasicStroke(
			strokeWeight,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_ROUND,
			0,
			new float[] {dashInterval}, 
			0
		  );
		
		//TODO: at this stage we should draw the open shapes first in order to make sure they are at the bottom (this is useful for the {mo} shape) 
		
		
		//find storage shapes
		ArrayList storageShapes = new ArrayList();
		shapesIt = shapes.iterator();
		while(shapesIt.hasNext()){
			DiagramShape shape = (DiagramShape) shapesIt.next();
			if(shape.getType() == DiagramShape.TYPE_STORAGE) {
				storageShapes.add(shape);
				continue;
			} 
		}

		//render storage shapes
		//special case since they are '3d' and should be
		//rendered bottom to top
		//TODO: known bug: if a storage object is within a bigger normal box, it will be overwritten in the main drawing loop
		//(BUT this is not possible since tags are applied to all shapes overlaping shapes)

		
		Collections.sort(storageShapes, new Shape3DOrderingComparator());
		
		g2.setStroke(normalStroke);
		shapesIt = storageShapes.iterator();
		while(shapesIt.hasNext()){
			DiagramShape shape = (DiagramShape) shapesIt.next();

			GeneralPath path;
			path = shape.makeIntoRenderPath(diagram);
			
			if(!shape.isStrokeDashed()) {
				if(shape.getFillColor() != null)
					g2.setColor(shape.getFillColor());
				else
					g2.setColor(Color.white);
				g2.fill(path);
			}

			if(shape.isStrokeDashed())
				g2.setStroke(dashStroke);
			else
				g2.setStroke(normalStroke);
			g2.setColor(shape.getStrokeColor());
			g2.draw(path);
		}


		//render the rest of the shapes
		ArrayList pointMarkers = new ArrayList();
		shapesIt = shapes.iterator();
		while(shapesIt.hasNext()){
			DiagramShape shape = (DiagramShape) shapesIt.next();
			if(shape.getType() == DiagramShape.TYPE_POINT_MARKER) {
				pointMarkers.add(shape);
				continue;
			} 
			if(shape.getType() == DiagramShape.TYPE_STORAGE) {
				continue;
			} 
			if(shape.getType() == DiagramShape.TYPE_CUSTOM){
				renderCustomShape(shape, g2);
				continue;
			}

			if(shape.getPoints().isEmpty()) continue;

			int size = shape.getPoints().size();
			
			GeneralPath path;
			path = shape.makeIntoRenderPath(diagram);
			
			//fill
			if(path != null && shape.isClosed() && !shape.isStrokeDashed()){
				if(shape.getFillColor() != null)
					g2.setColor(shape.getFillColor());
				else
					g2.setColor(Color.white);
				g2.fill(path);
			}
			
			//draw
			if(shape.getType() != DiagramShape.TYPE_ARROWHEAD){
				g2.setColor(shape.getStrokeColor());
				if(shape.isStrokeDashed())
					g2.setStroke(dashStroke);
				else
					g2.setStroke(normalStroke);
				g2.draw(path);
			}
		}
		
		//render point markers
		
		g2.setStroke(normalStroke);
		shapesIt = pointMarkers.iterator();
		while(shapesIt.hasNext()){
			DiagramShape shape = (DiagramShape) shapesIt.next();
			//if(shape.getType() != DiagramShape.TYPE_POINT_MARKER) continue;

			GeneralPath path;
			path = shape.makeIntoRenderPath(diagram);
			
			g2.setColor(Color.white);
			g2.fill(path);
			g2.setColor(shape.getStrokeColor());
			g2.draw(path);
		}		
		
		//handle text
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//renderTextLayer(diagram.getTextObjects().iterator());
		
		Iterator textIt = diagram.getTextObjects().iterator();
		while(textIt.hasNext()){
			DiagramText text = (DiagramText) textIt.next();
			g2.setFont(text.getFont());
			if(text.hasOutline()){
				g2.setColor(text.getOutlineColor());
				g2.drawString(text.getText(), text.getXPos() + 1, text.getYPos());
				g2.drawString(text.getText(), text.getXPos() - 1, text.getYPos());
				g2.drawString(text.getText(), text.getXPos(), text.getYPos() + 1);
				g2.drawString(text.getText(), text.getXPos(), text.getYPos() - 1);
			}
			g2.setColor(text.getColor());
			g2.drawString(text.getText(), text.getXPos(), text.getYPos());
		}
		
		if(options.renderDebugLines() || DEBUG){
			Stroke debugStroke =
			  new BasicStroke(
				1,
				BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND
			  );
			g2.setStroke(debugStroke);
			g2.setColor(new Color(170, 170, 170));
			g2.setXORMode(Color.white);
			for(int x = 0; x < diagram.getWidth(); x += diagram.getCellWidth())
				g2.drawLine(x, 0, x, diagram.getHeight());
			for(int y = 0; y < diagram.getHeight(); y += diagram.getCellHeight())
				g2.drawLine(0, y, diagram.getWidth(), y);
		}
		

		g2.dispose();
		
		return renderedImage;
	}
	
	private RenderedImage renderTextLayer(ArrayList textObjects, int width, int height){
		TextCanvas canvas = new TextCanvas(textObjects);
		Image image = canvas.createImage(width, height);
		Graphics g = image.getGraphics();
		canvas.paint(g);
		return (RenderedImage) image;
	}
	
	private class TextCanvas extends Canvas {
		ArrayList textObjects;
		
		public TextCanvas(ArrayList textObjects){
			this.textObjects = textObjects;
		}
		
		public void paint(Graphics g){
			Graphics g2 = (Graphics2D) g;
			Iterator textIt = textObjects.iterator();
			while(textIt.hasNext()){
				DiagramText text = (DiagramText) textIt.next();
				g2.setFont(text.getFont());
				if(text.hasOutline()){
					g2.setColor(text.getOutlineColor());
					g2.drawString(text.getText(), text.getXPos() + 1, text.getYPos());
					g2.drawString(text.getText(), text.getXPos() - 1, text.getYPos());
					g2.drawString(text.getText(), text.getXPos(), text.getYPos() + 1);
					g2.drawString(text.getText(), text.getXPos(), text.getYPos() - 1);
				}
				g2.setColor(text.getColor());
				g2.drawString(text.getText(), text.getXPos(), text.getYPos());
			}
		}
	}
	
	private void renderCustomShape(DiagramShape shape, Graphics2D g2){
		CustomShapeDefinition definition = shape.getDefinition();
		
		Rectangle bounds = shape.getBounds();
		
		if(definition.hasBorder()){
			g2.setColor(shape.getStrokeColor());
			if(shape.isStrokeDashed())
				g2.setStroke(dashStroke);
			else
				g2.setStroke(normalStroke);
			g2.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y);
			g2.drawLine(bounds.x + bounds.width, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
			g2.drawLine(bounds.x, bounds.y + bounds.height, bounds.x + bounds.width, bounds.y + bounds.height);
			g2.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height);
			
//			g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height); //looks different!			
		}
		
		//TODO: custom shape distintion relies on filename extension. Make this more intelligent
		if(definition.getFilename().endsWith(".png")){
			renderCustomPNGShape(shape, g2);
		} else if(definition.getFilename().endsWith(".svg")){
			// renderCustomSVGShape(shape, g2);
			throw new UnsupportedOperationException();
		}
	}
	
//	private void renderCustomSVGShape(DiagramShape shape, Graphics2D g2){
//		CustomShapeDefinition definition = shape.getDefinition();
//		Rectangle bounds = shape.getBounds();
//		Image graphic;
//		try {
//			if(shape.getFillColor() == null) {
//				graphic = ImageHandler.instance().renderSVG(
//						definition.getFilename(), bounds.width, bounds.height, definition.stretches());
//			} else {
//				graphic = ImageHandler.instance().renderSVG(
//						definition.getFilename(), bounds.width, bounds.height, definition.stretches(), IDREGEX, shape.getFillColor());				
//			}
//			g2.drawImage(graphic, bounds.x, bounds.y, null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	private void renderCustomPNGShape(DiagramShape shape, Graphics2D g2){
		CustomShapeDefinition definition = shape.getDefinition();
		Rectangle bounds = shape.getBounds();
		Image graphic = ImageHandler.instance().loadImage(definition.getFilename());
		
		int xPos, yPos, width, height;
		
		if(definition.stretches()){ //occupy all available space
			xPos = bounds.x; yPos = bounds.y;
			width = bounds.width; height = bounds.height;
		} else { //decide how to fit
			int newHeight = bounds.width * graphic.getHeight(null) / graphic.getWidth(null);
			if(newHeight < bounds.height){ //expand to fit width
				height = newHeight;
				width = bounds.width;
				xPos = bounds.x;
				yPos = bounds.y + bounds.height / 2 - graphic.getHeight(null) / 2;
			} else { //expand to fit height
				width = graphic.getWidth(null) * bounds.height / graphic.getHeight(null);
				height = bounds.height;
				xPos = bounds.x + bounds.width / 2 - graphic.getWidth(null) / 2;
				yPos = bounds.y;
			}
		}
		
		g2.drawImage(graphic, xPos, yPos, width, height, null);		
	}
	
	public static boolean isColorDark(Color color){
		int brightness = Math.max(color.getRed(), color.getGreen());
		brightness = Math.max(color.getBlue(), brightness);
		if(brightness < 200) {
			if(DEBUG) System.out.println("Color "+color+" is dark");
			return true;
		}
		if(DEBUG) System.out.println("Color "+color+" is not dark");
		return false;
	}
}
