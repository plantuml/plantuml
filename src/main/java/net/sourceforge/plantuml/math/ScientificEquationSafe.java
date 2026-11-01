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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ScaleSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.TextBlockExporter;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.utils.Log;

public class ScientificEquationSafe {

	private final ScientificEquation equation;
	private final String formula;

	private MathDrawing drawing;
	private boolean drawingDone;

	private ScientificEquationSafe(String formula, ScientificEquation equation) {
		this.formula = formula;
		this.equation = equation;
	}

	public static ScientificEquationSafe fromAsciiMath(String formula) {
		try {
			return new ScientificEquationSafe(formula, new AsciiMath(formula));
		} catch (Exception e) {
			Logme.error(e);
			Log.info(() -> "Error parsing " + formula);
			return new ScientificEquationSafe(formula, null);
		}
	}

	public static ScientificEquationSafe fromLatex(String formula) {
		try {
			return new ScientificEquationSafe(formula, new LatexBuilder(formula));
		} catch (Exception e) {
			Logme.error(e);
			Log.info(() -> "Error parsing " + formula);
			return new ScientificEquationSafe(formula, null);
		}
	}

	/** The formula, or null if it cannot be made (the source is then shown as text). */
	private MathDrawing getDrawing() {
		if (drawingDone == false) {
			drawingDone = true;
			if (equation != null)
				try {
					drawing = equation.getDrawing();
				} catch (Exception e) {
					printTrace(e);
				}
		}
		return drawing;
	}

	public XDimension2D getDimension(StringBounder stringBounder) {
		final MathDrawing result = getDrawing();
		if (result != null)
			return result.getDimension();
		return getRollback().calculateDimension(stringBounder);
	}

	/**
	 * Draws the formula, or its source as text if it cannot be made.
	 *
	 * @param foreground the color of the formula, null for black
	 * @param background the color behind the formula, null for none
	 */
	public void drawU(UGraphic ug, HColor foreground, HColor background) {
		final MathDrawing result = getDrawing();
		if (result == null) {
			getRollback().drawU(ug);
			return;
		}
		if (background != null) {
			final XDimension2D dim = result.getDimension();
			ug.apply(HColors.transparent()).apply(background.bg())
					.draw(URectangle.build(dim.getWidth(), dim.getHeight()));
		}
		result.draw(ug, foreground);
	}

	public TextBlock getTextBlock(final HColor foreground, final HColor background) {
		return new TextBlock() {

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return getDimension(stringBounder);
			}

			public void drawU(UGraphic ug) {
				ScientificEquationSafe.this.drawU(ug, foreground, background);
			}
		};
	}

	private void printTrace(Exception e) {
		System.err.println("formula=" + formula);
		if (equation != null) {
			System.err.println("Latex=" + equation.getSource());
		}
		Logme.error(e);
	}

	private TextBlock getRollback() {
		return GraphicStrings.createBlackOnWhiteMonospaced(Arrays.asList(formula));
	}

	public ImageData export(OutputStream os, FileFormatOption fileFormat, float scale, XColor foregroundColor,
			XColor backgroundColor) throws IOException {
		final FileFormat format = fileFormat.getFileFormat();
		if (format == FileFormat.PNG || format == FileFormat.SVG || format == FileFormat.SVG_DETERMINISTIC
				|| format == FileFormat.EPS || format == FileFormat.PDF || format == FileFormat.PDF_DETERMINISTIC) {
			final TextBlock block = getTextBlock(HColors.simple(foregroundColor),
					backgroundColor == null ? null : HColors.simple(backgroundColor));
			return TextBlockExporter.builder(block, fileFormat, false).scale(new ScaleSimple(scale)).build()
					.exportTo(os);
		}
		throw new UnsupportedOperationException();
	}

	public final String getFormula() {
		return formula;
	}

	public final String getSource() {
		return equation.getSource();
	}

}
