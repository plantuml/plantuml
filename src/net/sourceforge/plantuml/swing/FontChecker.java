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
package net.sourceforge.plantuml.swing;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.transform.TransformerException;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.LimitFinder;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.svg.SvgGraphics;
import net.sourceforge.plantuml.klimt.drawing.svg.SvgOption;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontContext;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SImageIO;

public class FontChecker {
    // ::remove folder when __HAXE__
	// ::remove file when __CORE__

	final private UFont font;
	private static final Set<String> SQUARE = new HashSet<>(Arrays.asList("MI=I=XM=I=IX", "MI=I=XM=I=IXMI=I=XM=I=IX"));

	public FontChecker(UFont font) {
		this.font = font;
	}

	public boolean isCharOk(char c) {
		return SQUARE.contains(getCharDesc(c)) == false;
	}

	static private String getType(int type, double oldX, double oldY, double x, double y) {
		if (type == PathIterator.SEG_CLOSE) {
			return "X";
		}
		if (type == PathIterator.SEG_LINETO) {
			if (oldX == x) {
				return "I";
			}
			if (oldY == y) {
				return "=";
			}
			return "L";
		}
		if (type == PathIterator.SEG_MOVETO) {
			return "M";
		}
		if (type == PathIterator.SEG_QUADTO) {
			return "Q";
		}
		if (type == PathIterator.SEG_CUBICTO) {
			return "C";
		}
		throw new IllegalArgumentException();
	}

	public String getCharDesc(char c) {
		final TextLayout t = UFontContext.G2D.createTextLayout(font, "" + c);
		final Shape sh = t.getOutline(null);
		final double current[] = new double[6];
		final PathIterator it = sh.getPathIterator(null);
		int sum = 0;
		final StringBuilder result = new StringBuilder();
		while (it.isDone() == false) {
			final double oldX = current[0];
			final double oldY = current[1];
			final int nb = it.currentSegment(current);
			sum += nb;
			result.append(getType(nb, oldX, oldY, current[0], current[1]));
			it.next();
		}
		return result.toString();
	}

	public String getCharDescVerbose(char c) {
		final TextLayout t = UFontContext.G2D.createTextLayout(font, "" + c);
		final Shape sh = t.getOutline(null);
		final double current[] = new double[6];
		final PathIterator it = sh.getPathIterator(null);
		int sum = 0;
		final StringBuilder result = new StringBuilder();
		while (it.isDone() == false) {
			final double oldX = current[0];
			final double oldY = current[1];
			final int nb = it.currentSegment(current);
			sum += nb;
			result.append(getType(nb, oldX, oldY, current[0], current[1]));
			appendValue(result, current);
			it.next();
		}
		return result.toString();
	}

	private void appendValue(StringBuilder result, double[] current) {
		for (double v : current) {
			final int i = (int) (v * 100);
			result.append(i);
			result.append(":");
		}

	}

	public void printChar(final PrintWriter pw, char c) throws IOException, TransformerException {
		pw.println("<p>");
		final int ascii = (int) c;
		pw.println(ascii + " - " + Integer.toHexString(ascii) + " - ");
		pw.println("&#" + ascii + ";");
		final String svg = getSvgImage(c);
		pw.println(svg);
	}

	private String getSvgImage(char c) throws IOException, TransformerException {
		final SvgGraphics svg = new SvgGraphics(42, SvgOption.basic());
		svg.setStrokeColor("black");
		svg.svgImage(getBufferedImage(c), 0, 0);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		svg.createXml(os);
		os.close();
		return new String(os.toByteArray());
	}

	public BufferedImage getBufferedImage(final char c) throws IOException {
		assert c != '\t';

		final double dim = 20;
		final UDrawable drawable = new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = ug.apply(HColors.BLACK);
				ug.draw(URectangle.build(dim - 1, dim - 1));
				if (!(ug instanceof LimitFinder)) {
					ug = ug.apply(new UTranslate(dim / 3, 2 * dim / 3));
					final UText text = UText.build("" + c, FontConfiguration.blackBlueTrue(font));
					ug.draw(text);
				}
			}
		};
		final byte[] bytes = ImageBuilder.plainPngBuilder(drawable).writeByteArray();
		return SImageIO.read(bytes);
	}

	// public BufferedImage getBufferedImageOld(char c) throws IOException {
	// final double dim = 20;
	// UGraphic2 ug = new FileFormatOption(FileFormat.PNG).createUGraphic(new
	// Dimension2DDouble(dim, dim));
	// ug = (UGraphic2) ug.apply(UChangeColor.nnn(HtmlColorUtils.BLACK));
	// ug.draw(URectangle.build(dim - 1, dim - 1));
	// ug = (UGraphic2) ug.apply(new UTranslate(dim / 3, 2 * dim / 3));
	// final UText text = new UText("" + c, FontConfiguration.create(font,
	// HtmlColorUtils.BLACK));
	// ug.draw(text);
	// final ByteArrayOutputStream os = new ByteArrayOutputStream();
	// ug.writeImageTOBEMOVED(os, null, 96);
	// os.close();
	// return ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
	// }

	public static void main(String[] args) throws IOException, TransformerException {

		final String name = args[0];
		final int size = Integer.parseInt(args[1]);
		final int v1 = Integer.parseInt(args[2]);
		final int v2 = Integer.parseInt(args[3]);
		final SFile f = new SFile("fontchecker-" + name + "-" + v1 + "-" + v2 + ".html");

		final FontChecker fc = new FontChecker(UFont.build(name, Font.PLAIN, size));
		final PrintWriter pw = f.createPrintWriter();
		pw.println("<html>");
		pw.println("<h1>PROBLEM</h1>");
		for (int i = v1; i <= v2; i++) {
			final char c = (char) i;
			final boolean ok = fc.isCharOk(c);
			if (ok == false) {
				fc.printChar(pw, c);
				pw.println("</p>");
			}
		}
		pw.println("<h1>OK</h1>");
		final String allFontNames[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = v1; i <= v2; i++) {
			final char c = (char) i;
			final boolean ok = fc.isCharOk(c);
			if (ok) {
				fc.printChar(pw, c);
				final String desc = fc.getCharDescVerbose(c);
				for (String n : allFontNames) {
					final FontChecker other = new FontChecker(UFont.build(n, Font.PLAIN, size));
					final String descOther = other.getCharDescVerbose(c);
					if (desc.equals(descOther)) {
						pw.println("&nbsp;");
						pw.println(n);
					}
				}
				pw.println("</p>");

			}
		}
		pw.println("</html>");
		pw.close();

	}
}
