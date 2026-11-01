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

import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

import org.scilab.forge.jlatexmath.TeXTextMeasurer;

/**
 * Measures, with the {@link StringBounder} of the diagram, the text of a formula
 * that no TeX font can draw (CJK, ...). It is drawn by
 * {@link UGraphicTeXGraphics#drawText} with the same font, so that the size
 * used for the layout is the one of the drawing, in every output format.
 */
public class StringBounderTextMeasurer implements TeXTextMeasurer {
	// ::remove file when __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__

	/** The size, in pixels, of the font used to measure: the result is given for a size of 1. */
	private static final int REFERENCE_SIZE = 100;

	private final StringBounder stringBounder;

	public StringBounderTextMeasurer(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	public float[] measure(String text) {
		final UFont font = UFontFactory.sansSerif(REFERENCE_SIZE);
		final XDimension2D dim = stringBounder.calculateDimension(font, text);
		final double descent = stringBounder.getDescent(font, text);
		return new float[] { (float) (dim.getWidth() / REFERENCE_SIZE),
				(float) ((dim.getHeight() - descent) / REFERENCE_SIZE), (float) (descent / REFERENCE_SIZE) };
	}

}
