/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.Icon;

public class LatexBuilder implements ScientificEquation {

	private final String tex;

	public LatexBuilder(String tex) {
		this.tex = tex;
	}

	private Dimension2D dimension;

	public Dimension2D getDimension() {
		return dimension;
	}

	private Icon buildIcon(Color foregroundColor) throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		return new TeXIconBuilder(tex, foregroundColor).getIcon();
	}

	public String getSvg(Color foregroundColor, Color backgroundColor) throws ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, IOException {
		final Icon icon = buildIcon(foregroundColor);
		final ConverterSvg converterSvg = new ConverterSvg(icon);
		final String svg = converterSvg.getSvg(true, backgroundColor);
		dimension = converterSvg.getDimension();
		return svg;
	}

	public BufferedImage getImage(double scale, Color foregroundColor, Color backgroundColor)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Icon icon = buildIcon(foregroundColor);
		final BufferedImage image = new BufferedImage((int) (icon.getIconWidth() * scale),
				(int) (icon.getIconHeight() * scale), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2 = image.createGraphics();
		g2.scale(scale, scale);
		if (backgroundColor != null) {
			g2.setColor(backgroundColor);
			g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
		}
		icon.paintIcon(null, g2, 0, 0);
		return image;
	}

	public String getSource() {
		return tex;
	}

}
