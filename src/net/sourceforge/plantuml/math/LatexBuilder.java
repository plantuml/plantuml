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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.Icon;

import net.sourceforge.plantuml.klimt.MutableImage;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;

public class LatexBuilder implements ScientificEquation {

	private final String tex;

	public LatexBuilder(String tex) {
		this.tex = tex;
	}

	private XDimension2D dimension;

	public XDimension2D getDimension() {
		return dimension;
	}

	private Icon buildIcon(Color foregroundColor)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return new TeXIconBuilder(tex, foregroundColor).getIcon();
	}

	public UImageSvg getSvg(double scale, Color foregroundColor, Color backgroundColor)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, IOException {
		final Icon icon = buildIcon(foregroundColor);
		final ConverterSvg converterSvg = new ConverterSvg(icon);
		final String svg = converterSvg.getSvg(scale, true, backgroundColor);
		dimension = converterSvg.getDimension();
		return new UImageSvg(svg, scale);
	}

	public MutableImage getImage(Color foregroundColor, Color backgroundColor)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Icon icon = buildIcon(foregroundColor);
		return new LatexImage(icon, 1, foregroundColor, backgroundColor);
	}

	class LatexImage implements MutableImage {

		private final double scale;
		private final Icon icon;
		private final Color foregroundColor;
		private final Color backgroundColor;
		private BufferedImage cache = null;

		public LatexImage(Icon icon, double scale, Color foregroundColor, Color backgroundColor) {
			this.scale = scale;
			this.foregroundColor = foregroundColor;
			this.backgroundColor = backgroundColor;
			this.icon = icon;
		}

		@Override
		public BufferedImage getImage() {
			if (cache == null) {
				cache = new BufferedImage((int) (icon.getIconWidth() * scale), (int) (icon.getIconHeight() * scale),
						BufferedImage.TYPE_INT_ARGB);
				final Graphics2D g2 = cache.createGraphics();
				g2.scale(scale, scale);
				if (backgroundColor != null) {
					g2.setColor(backgroundColor);
					g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
				}
				icon.paintIcon(null, g2, 0, 0);
			}
			return cache;
		}

		public MutableImage withScale(double scale) {
			return new LatexImage(icon, this.scale * scale, foregroundColor, backgroundColor);
		}

		@Override
		public MutableImage muteColor(Color newColor) {
			return this;
		}

		@Override
		public MutableImage muteTransparentColor(Color newColor) {
			return this;
		}

		@Override
		public MutableImage monochrome() {
			return this;
		}

		@Override
		public double getScale() {
			return scale;
		}

	}

	public String getSource() {
		return tex;
	}

}
