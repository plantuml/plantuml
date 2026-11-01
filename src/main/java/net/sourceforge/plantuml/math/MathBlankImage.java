/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
 *
 */
package net.sourceforge.plantuml.math;

import net.sourceforge.plantuml.klimt.MutableImage;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.awt.PortableImageFactory;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

/**
 * An empty image of the size of a formula. TikZ output does not draw the
 * formula: its driver only reads the source that the {@code UImage} carries
 * ({@code withFormula}), but a {@code UImage} needs an image to have a size.
 */
public class MathBlankImage implements MutableImage {

	private final XDimension2D dimension;
	private final double scale;
	private PortableImage cache;

	public MathBlankImage(XDimension2D dimension) {
		this(dimension, 1);
	}

	private MathBlankImage(XDimension2D dimension, double scale) {
		this.dimension = dimension;
		this.scale = scale;
	}

	public PortableImage getImage() {
		if (cache == null)
			cache = PortableImageFactory.build((int) (dimension.getWidth() * scale),
					(int) (dimension.getHeight() * scale), PortableImage.TYPE_INT_ARGB);
		return cache;
	}

	public MutableImage withScale(double newScale) {
		return new MathBlankImage(dimension, scale * newScale);
	}

	public MutableImage muteColor(XColor newColor) {
		return this;
	}

	public MutableImage muteTransparentColor(XColor newColor) {
		return this;
	}

	public MutableImage monochrome() {
		return this;
	}

	public double getScale() {
		return scale;
	}

}
