/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 *
 */
package net.sourceforge.plantuml;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class EmptyImageBuilder {

	private final BufferedImage im;
	private final Graphics2D g2d;

	public EmptyImageBuilder(String watermark, double width, double height, Color background) {
		this(watermark, (int) width, (int) height, background);
	}

	public EmptyImageBuilder(String watermark, int width, int height, Color background) {
		if (width > GraphvizUtils.getenvImageLimit()) {
			Log.info("Width too large " + width + ". You should set PLANTUML_LIMIT_SIZE");
			width = GraphvizUtils.getenvImageLimit();
		}
		if (height > GraphvizUtils.getenvImageLimit()) {
			Log.info("Height too large " + height + ". You should set PLANTUML_LIMIT_SIZE");
			height = GraphvizUtils.getenvImageLimit();
		}
		Log.info("Creating image " + width + "x" + height);
		im = new BufferedImage(width, height, getType(background));
		g2d = im.createGraphics();
		UAntiAliasing.ANTI_ALIASING_ON.apply(g2d);
		if (background != null) {
			g2d.setColor(background);
			g2d.fillRect(0, 0, width, height);
		}
		if (watermark != null) {
			final int gray = 200;
			g2d.setColor(new Color(gray, gray, gray));
			printWatermark(watermark, width, height);
		}
	}

	private int getType(Color background) {
		if (background == null) {
			return BufferedImage.TYPE_INT_ARGB;
		}
		if (background.getAlpha() != 255) {
			return BufferedImage.TYPE_INT_ARGB;
		}
		return BufferedImage.TYPE_INT_RGB;
	}

	private void printWatermark(String watermark, int maxWidth, int maxHeight) {
		final Font javaFont = g2d.getFont();
		final FontMetrics fm = g2d.getFontMetrics(javaFont);
		final Rectangle2D rect = fm.getStringBounds(watermark, g2d);
		final int height = (int) rect.getHeight();
		final int width = (int) rect.getWidth();
		if (height < 2 || width < 2) {
			return;
		}
		if (width <= maxWidth)
			for (int y = height; y < maxHeight; y += height + 1) {
				for (int x = 0; x < maxWidth; x += width + 10) {
					g2d.drawString(watermark, x, y);
				}
			}
		else {
			final List<String> withBreaks = withBreaks(watermark, javaFont, fm, maxWidth);
			int y = 0;
			while (y < maxHeight) {
				for (String s : withBreaks) {
					g2d.drawString(s, 0, y);
					y += (int) fm.getStringBounds(s, g2d).getHeight();
				}
				y += 10;
			}
		}
	}

	private int getWidth(String line, Font javaFont, FontMetrics fm) {
		final Rectangle2D rect = fm.getStringBounds(line, g2d);
		return (int) rect.getWidth();
	}

	private List<String> withBreaks(String watermark, Font javaFont, FontMetrics fm, int maxWidth) {
		final String[] words = watermark.split("\\s+");
		final List<String> result = new ArrayList<String>();
		String pending = "";
		for (String word : words) {
			final String candidate = pending.length() == 0 ? word : pending + " " + word;
			if (getWidth(candidate, javaFont, fm) < maxWidth) {
				pending = candidate;
			} else {
				result.add(pending);
				pending = word;
			}
		}
		if (pending.length() > 0) {
			result.add(pending);
		}
		return result;
	}

	public EmptyImageBuilder(String watermark, int width, int height, Color background, double dpiFactor) {
		this(watermark, width * dpiFactor, height * dpiFactor, background);
		if (dpiFactor != 1.0) {
			g2d.setTransform(AffineTransform.getScaleInstance(dpiFactor, dpiFactor));
		}
	}

	public BufferedImage getBufferedImage() {
		return im;
	}

	public Graphics2D getGraphics2D() {
		return g2d;
	}

	public UGraphicG2d getUGraphicG2d() {
		final UGraphicG2d result = new UGraphicG2d(new ColorMapperIdentity(), g2d, 1.0);
		result.setBufferedImage(im);
		return result;
	}

}
