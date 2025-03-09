/*
 * Copyright 2003-2004, Franz-Josef Elmer, All rights reserved
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details
 * (http://www.gnu.org/copyleft/lesser.html).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jcckit.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import jcckit.graphic.BasicGraphicalElement;
import jcckit.graphic.ClippingRectangle;
import jcckit.graphic.ClippingShape;
import jcckit.graphic.FillAttributes;
import jcckit.graphic.FontStyle;
import jcckit.graphic.GraphPoint;
import jcckit.graphic.GraphicAttributes;
import jcckit.graphic.GraphicalComposite;
import jcckit.graphic.GraphicalCompositeRenderer;
import jcckit.graphic.LineAttributes;
import jcckit.graphic.Oval;
import jcckit.graphic.OvalRenderer;
import jcckit.graphic.Polygon;
import jcckit.graphic.PolygonRenderer;
import jcckit.graphic.Rectangle;
import jcckit.graphic.RectangleRenderer;
import jcckit.graphic.Text;
import jcckit.graphic.TextAttributes;
import jcckit.graphic.TextRenderer;

/**
 * Renderer who draws the {@link jcckit.graphic.GraphicalElement
 * GraphicalElements} into a <tt>java.awt.Graphics2D</tt> context.
 * <p>
 * The default color for lines and texts is determined by the current color of
 * the <tt>Graphics2D</tt> context when a new instance of
 * <tt>Graphics2DRenderer</tt> is created.
 * <p>
 * The default font is <tt>SansSerif-12</tt>.
 * 
 * @author Franz-Josef Elmer
 */
public class Graphics2DRenderer implements GraphicalCompositeRenderer, PolygonRenderer, OvalRenderer, TextRenderer,
		RectangleRenderer {
	private static final int FS = 1;
	private static final String DEFAULT_FONT_NAME = "SansSerif";
	private static final FontStyle DEFAULT_FONT_STYLE = FontStyle.NORMAL;
	private static final int DEFAULT_FONT_SIZE = 12;

	private Color _defaultColor;
	private Graphics2D _graphics;

	/**
	 * Initializes this instance. During renderering the current transformation
	 * will be leaved unchanged. But the current Clip may be cleared.
	 * 
	 * @param graphics
	 *            Graphics2D context into which the
	 *            {@link BasicGraphicalElement BaiscGraphicalElements} are
	 *            painted.
	 * @return this instance.
	 */
	public Graphics2DRenderer init(Graphics2D graphics) {
		_graphics = graphics;
		_defaultColor = graphics.getColor(); // the foreground color
		return this;
	}

	/**
	 * Starts rendering of the specified composite. Does nothing except if
	 * <tt>composite</tt> has a {@link ClippingShape}. In this case the Clip
	 * of the <tt>Graphics2D</tt> context becomes the clipping rectangle
	 * determined by the bounding box of the <tt>ClippingShape</tt>.
	 */
	public void startRendering(GraphicalComposite composite) {
		ClippingShape shape = composite.getClippingShape();
		if (shape != null) {
			ClippingRectangle rect = shape.getBoundingBox();
			_graphics.clip(new Rectangle2D.Double(rect.getMinX(), rect.getMinY(), rect.getMaxX() - rect.getMinX(), rect
					.getMaxY()
					- rect.getMinY()));
		}
	}

	/**
	 * Finishes rendering of the specified composite. Does nothing except if
	 * <tt>composite</tt> has a {@link ClippingShape}. In this case the Clip
	 * of the <tt>Graphics2D</tt> context will be cleared.
	 */
	public void finishRendering(GraphicalComposite composite) {
		_graphics.setClip(null);
	}

	/** Paints the specified polygon into the <tt>Graphics2D</tt> context. */
	public void render(Polygon polygon) {
		int numberOfPoints = polygon.getNumberOfPoints();
		if (numberOfPoints > 0) {
			Color currentColor = _graphics.getColor();
			GeneralPath p = new GeneralPath(GeneralPath.WIND_EVEN_ODD, numberOfPoints);
			p.moveTo((float) polygon.getPoint(0).getX(), (float) polygon.getPoint(0).getY());
			for (int i = 1; i < numberOfPoints; i++) {
				p.lineTo((float) polygon.getPoint(i).getX(), (float) polygon.getPoint(i).getY());
			}
			if (polygon.isClosed()) {
				p.closePath();
			}
			drawShape(p, polygon, currentColor);
		}
	}

	/**
	 * Paints the specified rectangle into the current <tt>Graphics</tt>
	 * context.
	 */
	public void render(Rectangle rectangle) {
		Color currentColor = _graphics.getColor();
		GraphPoint center = rectangle.getCenter();
		double width = rectangle.getWidth();
		double height = rectangle.getHeight();
		Rectangle2D rect = new Rectangle2D.Double(center.getX() - 0.5 * width, center.getY() - 0.5 * height, width,
				height);
		drawShape(rect, rectangle, currentColor);
	}

	/**
	 * Paints the specified oval into the current <tt>Graphics</tt> context.
	 */
	public void render(Oval oval) {
		Color currentColor = _graphics.getColor();
		GraphPoint center = oval.getCenter();
		double width = oval.getWidth();
		double height = oval.getHeight();
		Ellipse2D ellipse = new Ellipse2D.Double(center.getX() - 0.5 * width, center.getY() - 0.5 * height, width,
				height);
		drawShape(ellipse, oval, currentColor);
	}

	private void drawShape(Shape shape, BasicGraphicalElement element, Color backupColor) {
		GraphicAttributes attributes = element.getGraphicAttributes();
		Color fillColor = null;
		if (element.isClosed() && attributes instanceof FillAttributes) {
			fillColor = ((FillAttributes) attributes).getFillColor();
		}
		if (fillColor != null) {
			_graphics.setColor(fillColor);
			_graphics.fill(shape);
		}
		Color lineColor = _defaultColor;
		if (attributes instanceof LineAttributes) {
			LineAttributes la = (LineAttributes) attributes;
			BasicStroke stroke = new BasicStroke((float) la.getLineThickness());
			double[] linePattern = la.getLinePattern();
			if (linePattern != null) {
				float[] dash = new float[linePattern.length];
				for (int i = 0; i < dash.length; i++) {
					dash[i] = (float) la.getLinePattern()[i];
				}
				stroke = new BasicStroke(stroke.getLineWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f,
						dash, 0f);
			}
			_graphics.setStroke(stroke);
			if (la.getLineColor() != null || fillColor != null) {
				lineColor = la.getLineColor();
			}
		}
		if (lineColor != null) {
			_graphics.setColor(lineColor);
			_graphics.draw(shape);
		}
		_graphics.setColor(backupColor);
	}

	/**
	 * Paints the specified text into the current <tt>Graphics</tt> context.
	 * <p>
	 * If the font size is zero the default font size will be used.
	 * <p>
	 * If the orientation angle is unequal zero the text will first be painted
	 * into an off-screen image and rotated. Finally, it will be drawn into the
	 * current <tt>Graphics</tt> context. Note, that only integer multiples of
	 * 90 degree rotation are performed. Other orientation angles will be
	 * adjusted to the nearest integer multiple of 90 degree.
	 */
	public void render(Text text) {
		final GraphicAttributes ga = text.getGraphicAttributes();
		if (ga instanceof TextAttributes) {
			final TextAttributes ta = (TextAttributes) ga;
			final Color currentColor = _graphics.getColor();
			Color fontColor = ta.getTextColor();
			if (fontColor == null) {
				fontColor = _defaultColor;
			}
			_graphics.setColor(fontColor);

			final double scale = _graphics.getTransform().getScaleX();
			final String str = text.getText();

			AffineTransform before = _graphics.getTransform();
			_graphics.setTransform(new AffineTransform());

			double fs = ta.getFontSize();
			fs = fs == 0 ? 1 : fs * scale / DEFAULT_FONT_SIZE;

			Font font = createFont(ta, 0);

			AffineTransform fontTransform = new AffineTransform();
			fontTransform.scale(fs, fs);
			fontTransform.rotate(-ta.getOrientationAngle() * Math.PI / 180);
			font = font.deriveFont(fontTransform);
			_graphics.setFont(font);
			Rectangle2D bounds = _graphics.getFontMetrics().getStringBounds(str, _graphics);

			fontTransform.rotate(-ta.getOrientationAngle() * Math.PI / 180);

			final double yy = bounds.getHeight() + bounds.getY();

			Point2D.Double pos = new Point2D.Double(text.getPosition().getX(), text.getPosition().getY());
			before.transform(pos, pos);

			double x = 0;
			double y = 0;
			if (ta.getOrientationAngle() == 0) {
				x = -0.5 * ta.getHorizontalAnchor().getFactor() * bounds.getWidth();
				y = 0.5 * ta.getVerticalAnchor().getFactor() * bounds.getHeight() - yy;
				x = pos.x + x;
				y = pos.y + y;
			} else {
				x = 0.5 * ta.getVerticalAnchor().getFactor() * bounds.getHeight();
				y = 0.5 * ta.getHorizontalAnchor().getFactor() * bounds.getWidth();
				// System.err.println("yy="+y+" dx="+x+" dy="+y);
				// x = 0;
				// y = 0;
				x = pos.x + x;
				y = pos.y + y;
			}

//			if (ta.getOrientationAngle() == 0) {
////				System.err.println("x0=" + x);
////				System.err.println("y0=" + y);
//			} else {
//				System.err.println("bounds=" + bounds + " y=" + bounds.getY() + " h=" + bounds.getHeight() + " vert="
//						+ ta.getVerticalAnchor().getFactor()+" horz="+ta.getHorizontalAnchor().getFactor());
//				System.err.println("x1=" + x);
//				System.err.println("y1=" + y);
//			}

			
			_graphics.drawString(str, (float) x, (float) y);
			// _graphics.fillRect((int)x, (int)y, 5, 5);
			_graphics.setTransform(before);
			_graphics.setColor(currentColor);
		}
	}

	/**
	 * Creates a font instance based on the specified text attributes and font
	 * size.
	 * 
	 * @param attributes
	 *            Text attributes (font name and style).
	 * @param size
	 *            Font size in pixel. If 0 {@link #DEFAULT_FONT_SIZE} will be
	 *            used.
	 * @return new font instance.
	 */
	static Font createFont(TextAttributes attributes, int size) {
		String fontName = attributes.getFontName();
		if (fontName == null) {
			fontName = DEFAULT_FONT_NAME;
		}

		FontStyle fontStyle = attributes.getFontStyle();
		if (fontStyle == null) {
			fontStyle = DEFAULT_FONT_STYLE;
		}
		int style = Font.PLAIN;
		if (fontStyle == FontStyle.BOLD) {
			style = Font.BOLD;
		} else if (fontStyle == FontStyle.ITALIC) {
			style = Font.ITALIC;
		} else if (fontStyle == FontStyle.BOLD_ITALIC) {
			style = Font.BOLD + Font.ITALIC;
		}

		if (size == 0) {
			size = DEFAULT_FONT_SIZE;
		}

		return new Font(fontName, style, size);
	}

}
