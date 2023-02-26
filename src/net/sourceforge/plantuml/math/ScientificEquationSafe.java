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

import static net.atmp.ImageBuilder.plainImageBuilder;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import net.atmp.ImageBuilder;
import net.atmp.PixelImage;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.MutableImage;
import net.sourceforge.plantuml.klimt.drawing.eps.EpsGraphics;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.utils.Log;

public class ScientificEquationSafe {

	private final ScientificEquation equation;
	private final String formula;

	private ScientificEquationSafe(String formula, ScientificEquation equation) {
		this.formula = formula;
		this.equation = equation;
	}

	public static ScientificEquationSafe fromAsciiMath(String formula) {
		try {
			return new ScientificEquationSafe(formula, new AsciiMath(formula));
		} catch (Exception e) {
			Logme.error(e);
			Log.info("Error parsing " + formula);
			return new ScientificEquationSafe(formula, null);
		}
	}

	public static ScientificEquationSafe fromLatex(String formula) {
		try {
			return new ScientificEquationSafe(formula, new LatexBuilder(formula));
		} catch (Exception e) {
			Logme.error(e);
			Log.info("Error parsing " + formula);
			return new ScientificEquationSafe(formula, null);
		}
	}

	private ImageData dimSvg;

	public UImageSvg getSvg(double scale, Color foregroundColor, Color backgroundColor) {
		if (equation != null)
			try {
				final UImageSvg svg = equation.getSvg(scale, foregroundColor, backgroundColor);
				dimSvg = new ImageDataSimple(equation.getDimension());
				return svg;
			} catch (Exception e) {
				printTrace(e);
			}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			dimSvg = plainImageBuilder(getRollback(), new FileFormatOption(FileFormat.SVG)).write(baos);
		} catch (IOException e1) {
			return null;
		}
		return new UImageSvg(new String(baos.toByteArray()), scale);
	}

	public MutableImage getImage(Color foregroundColor, Color backgroundColor) {
		if (equation != null)
			try {
				return equation.getImage(foregroundColor, backgroundColor);
			} catch (Exception e) {
				printTrace(e);
			}
		try {
			final byte[] bytes = ImageBuilder.plainPngBuilder(getRollback()).writeByteArray();
			return new PixelImage(SImageIO.read(bytes), AffineTransformType.TYPE_BILINEAR);
		} catch (IOException e1) {
			return null;
		}
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

	public ImageData export(OutputStream os, FileFormatOption fileFormat, float scale, Color foregroundColor,
			Color backgroundColor) throws IOException {
		if (fileFormat.getFileFormat() == FileFormat.PNG) {
			final BufferedImage image = getImage(foregroundColor, backgroundColor).withScale(scale).getImage();
			SImageIO.write(image, "png", os);
			return new ImageDataSimple(image.getWidth(), image.getHeight());
		}
		if (fileFormat.getFileFormat() == FileFormat.SVG) {
			os.write(getSvg(1, foregroundColor, backgroundColor).getSvg(true).getBytes());
			return dimSvg;
		}
		if (fileFormat.getFileFormat() == FileFormat.EPS) {
			final BufferedImage image = getImage(foregroundColor, backgroundColor).withScale(scale).getImage();
			final EpsGraphics out = new EpsGraphics();
			out.drawImage(image, 0, 0);
			out.close();
			os.write(out.getEPSCode().getBytes());
			return new ImageDataSimple(image.getWidth(), image.getHeight());
		}
		throw new UnsupportedOperationException();
	}

	public final String getFormula() {
		return formula;
	}

}
