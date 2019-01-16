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
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class EmptyImageBuilder {

	private final BufferedImage im;
	private final Graphics2D g2d;

	public EmptyImageBuilder(double width, double height, Color background) {
		this((int) width, (int) height, background);
	}

	public EmptyImageBuilder(int width, int height, Color background) {
		if (width > GraphvizUtils.getenvImageLimit()) {
			Log.info("Width too large " + width + ". You should set PLANTUML_LIMIT_SIZE");
			width = GraphvizUtils.getenvImageLimit();
		}
		if (height > GraphvizUtils.getenvImageLimit()) {
			Log.info("Height too large " + height + ". You should set PLANTUML_LIMIT_SIZE");
			height = GraphvizUtils.getenvImageLimit();
		}
		Log.info("Creating image " + width + "x" + height);
		im = new BufferedImage(width, height, background == null ? BufferedImage.TYPE_INT_ARGB
				: BufferedImage.TYPE_INT_RGB);
		g2d = im.createGraphics();
		UAntiAliasing.ANTI_ALIASING_ON.apply(g2d);
		if (background != null) {
			g2d.setColor(background);
			g2d.fillRect(0, 0, width, height);
		}
	}

	public EmptyImageBuilder(int width, int height, Color background, double dpiFactor) {
		this(width * dpiFactor, height * dpiFactor, background);
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
