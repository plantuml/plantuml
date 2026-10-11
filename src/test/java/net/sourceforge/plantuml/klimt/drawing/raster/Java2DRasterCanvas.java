/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.klimt.drawing.raster;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.UAntiAliasing;
import net.sourceforge.plantuml.klimt.awt.PortableImageFactory;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.g2d.ExtendedGeneralPath;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.png.PngIO;

/**
 * A {@link RasterCanvas} drawing with Java2D exactly as the drivers of
 * UGraphicG2d do (same shapes, strokes, paints and hints), so that a PNG drawn
 * by UGraphicRaster on it can be compared pixel for pixel with -tpng.
 */
class Java2DRasterCanvas implements RasterCanvas {

	private final BufferedImage image;
	private final Graphics2D g2d;

	// An RGB image when opaque, as EmptyImageBuilder
	Java2DRasterCanvas(int width, int height, boolean opaque) {
		this.image = new BufferedImage(width, height,
				opaque ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
		this.g2d = image.createGraphics();
		UAntiAliasing.ANTI_ALIASING_ON.apply(g2d);
	}

	BufferedImage getImage() {
		return image;
	}

	@Override
	public void scale(double factor) {
		g2d.scale(factor, factor);
	}

	@Override
	public void setColor(int argb) {
		g2d.setColor(new Color(argb, true));
	}

	@Override
	public void setLinearGradient(double x1, double y1, double x2, double y2, double[] offsets, int[] colors,
			int spread) {
		if (offsets.length == 2 && offsets[0] == 0 && offsets[1] == 1 && spread == SPREAD_PAD) {
			g2d.setPaint(new GradientPaint((float) x1, (float) y1, new Color(colors[0], true), (float) x2,
					(float) y2, new Color(colors[1], true)));
			return;
		}
		final float[] fractions = new float[offsets.length];
		final Color[] awtColors = new Color[colors.length];
		for (int i = 0; i < offsets.length; i++) {
			fractions[i] = (float) offsets[i];
			awtColors[i] = new Color(colors[i], true);
		}
		final CycleMethod cycle = spread == SPREAD_REFLECT ? CycleMethod.REFLECT
				: spread == SPREAD_REPEAT ? CycleMethod.REPEAT : CycleMethod.NO_CYCLE;
		g2d.setPaint(new LinearGradientPaint(new Point2D.Float((float) x1, (float) y1),
				new Point2D.Float((float) x2, (float) y2), fractions, awtColors, cycle));
	}

	@Override
	public void setStroke(double thickness, double dashVisible, double dashSpace) {
		if (dashVisible == 0)
			g2d.setStroke(new BasicStroke((float) thickness));
		else
			g2d.setStroke(new BasicStroke((float) thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
					new float[] { (float) dashVisible, (float) dashSpace }, 0));
	}

	@Override
	public void setClip(double x, double y, double width, double height) {
		g2d.setClip(new Rectangle2D.Double(x, y, width, height));
	}

	@Override
	public void resetClip() {
		g2d.setClip(null);
	}

	@Override
	public void fill(RasterPath path) {
		g2d.fill(toShape(path));
	}

	@Override
	public void stroke(RasterPath path) {
		g2d.draw(toShape(path));
	}

	@Override
	public void drawString(String text, UFont font, double x, double y, double angle) {
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setFont(font.getUnderlayingFont(text));
		if (angle == 0) {
			g2d.drawString(text, (float) x, (float) y);
			return;
		}
		final AffineTransform orig = g2d.getTransform();
		g2d.translate(x, y);
		g2d.rotate(angle);
		g2d.drawString(text, 0, 0);
		g2d.setTransform(orig);
	}

	// Encoded as -tpng encodes (PngIO, which reduces the colors when it can)
	@Override
	public byte[] toPng() {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PngIO.write(PortableImageFactory.build(image), ColorMapper.IDENTITY, baos, null, 96);
			return baos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void dispose() {
		g2d.dispose();
	}

	// The shapes the Java2D drivers use: a single rectangle, ellipse, arc or line
	// as such, anything else as a path
	private static Shape toShape(RasterPath path) {
		final List<Shape> shapes = new ArrayList<>();
		final List<double[]> lines = new ArrayList<>();
		final ExtendedGeneralPath general = new ExtendedGeneralPath();
		final int[] segments = new int[1];
		path.visit(new RasterPath.Visitor() {
			@Override
			public void moveTo(double x, double y) {
				general.moveTo(x, y);
				lines.add(new double[] { x, y });
				segments[0]++;
			}

			@Override
			public void lineTo(double x, double y) {
				general.lineTo(x, y);
				lines.add(new double[] { x, y });
				segments[0]++;
			}

			@Override
			public void cubicTo(double x1, double y1, double x2, double y2, double x3, double y3) {
				general.curveTo(x1, y1, x2, y2, x3, y3);
				segments[0] += 10;
			}

			@Override
			public void closePath() {
				general.closePath();
				segments[0] += 10;
			}

			@Override
			public void rectangle(double x, double y, double width, double height, double arcWidth,
					double arcHeight) {
				if (arcWidth == 0 && arcHeight == 0)
					shapes.add(new Rectangle2D.Double(x, y, width, height));
				else
					shapes.add(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight));
			}

			@Override
			public void ellipse(double x, double y, double width, double height) {
				shapes.add(new Ellipse2D.Double(x, y, width, height));
			}

			@Override
			public void arc(double x, double y, double width, double height, double start, double extent) {
				shapes.add(new Arc2D.Double(x, y, width, height, start, extent, Arc2D.OPEN));
			}

			@Override
			public void arcTo(double rx, double ry, double rotation, boolean largeArc, boolean sweep, double x,
					double y) {
				general.arcTo(rx, ry, rotation, largeArc, sweep, x, y);
				segments[0] += 10;
			}
		});
		if (shapes.size() == 1 && segments[0] == 0)
			return shapes.get(0);
		if (shapes.isEmpty() && segments[0] == 2)
			return new Line2D.Double(lines.get(0)[0], lines.get(0)[1], lines.get(1)[0], lines.get(1)[1]);
		if (shapes.isEmpty() == false)
			throw new IllegalArgumentException("Mixed path");
		return general;
	}

}
