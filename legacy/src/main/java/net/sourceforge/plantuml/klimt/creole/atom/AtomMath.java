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
package net.sourceforge.plantuml.klimt.creole.atom;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.math.ScientificEquationSafe;

public class AtomMath extends AbstractAtom implements Atom {
	// ::remove file when __CORE__

	private final ScientificEquationSafe math;
	private final HColor foreground;
	private final HColor background;

	public AtomMath(ScientificEquationSafe math, HColor foreground, HColor background) {
		this.math = math;
		this.foreground = foreground;
		this.background = background;
	}

	private XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final BufferedImage image = math.getImage(Color.BLACK, Color.WHITE).withScale(1).getImage();
		return new XDimension2D(image.getWidth(), image.getHeight());
	}

	private XDimension2D dim;

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		if (dim == null) {
			dim = calculateDimensionSlow(stringBounder);
		}
		return dim;
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public void drawU(UGraphic ug) {
		final ColorMapper colorMapper = ug.getColorMapper();
		final boolean isSvg = ug.matchesProperty("SVG");
		final Color back;
		if (background == null)
			back = null;
		else
			back = getColor(colorMapper, background, Color.WHITE);

		final Color fore = getColor(colorMapper, foreground, Color.BLACK);
		// final double dpiFactor = ug.dpiFactor();
		if (isSvg) {
			final UImageSvg svg = math.getSvg(1, fore, back);
			ug.draw(svg);
		} else {
			final UImage image = new UImage(math.getImage(fore, back)).withFormula(math.getFormula());
			ug.draw(image);
		}
	}

	private Color getColor(ColorMapper colorMapper, HColor color, Color defaultValue) {
		if (color instanceof HColorSimple)
			return color.toColor(colorMapper);

		return defaultValue;

	}
}
