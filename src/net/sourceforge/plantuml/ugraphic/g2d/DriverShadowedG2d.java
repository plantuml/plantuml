/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6859 $
 *
 */
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class DriverShadowedG2d {

	private final static ConvolveOp convolveOp;

	static {
		final int blurRadius = 6;
		final int blurRadius2 = blurRadius * blurRadius;
		final float blurRadius2F = blurRadius2;
		final float weight = 1.0f / blurRadius2F;
		final float[] elements = new float[blurRadius2];
		for (int k = 0; k < blurRadius2; k++) {
			elements[k] = weight;
		}
		final Kernel myKernel = new Kernel(blurRadius, blurRadius, elements);

		// if EDGE_NO_OP is not selected, EDGE_ZERO_FILL is the default which
		// creates a black border
		convolveOp = new ConvolveOp(myKernel, ConvolveOp.EDGE_NO_OP, null);
	}

	private final Color color = new Color(170, 170, 170);

	protected void drawShadow(Graphics2D g2d, Shape shape, double x, double y, double deltaShadow) {
		// Shadow
		final Rectangle2D bounds = shape.getBounds2D();
		BufferedImage destination = new BufferedImage((int) (bounds.getWidth() + deltaShadow * 2 + 6), (int) (bounds
				.getHeight()
				+ deltaShadow * 2 + 6), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D gg = destination.createGraphics();
		gg.setColor(color);
		gg.translate(deltaShadow - x, deltaShadow - y);
		gg.fill(shape);
		gg.dispose();
		final ConvolveOp simpleBlur = convolveOp;
		destination = simpleBlur.filter(destination, null);
		g2d.drawImage(destination, (int) x, (int) y, null);
		// Shadow

	}

}
