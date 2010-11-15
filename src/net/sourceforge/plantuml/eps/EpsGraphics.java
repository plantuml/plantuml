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
 * Revision $Revision: 4207 $
 *
 */
package net.sourceforge.plantuml.eps;

import java.awt.Color;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Locale;

import net.sourceforge.plantuml.ugraphic.UGradient;

public class EpsGraphics {

	// http://www.linuxfocus.org/Francais/May1998/article43.html
	// http://www.tailrecursive.org/postscript/text.html
	private final StringBuilder body = new StringBuilder();
	private final StringBuilder header = new StringBuilder();

	private Color color = Color.BLACK;
	private Color fillcolor = Color.BLACK;

	private String strokeWidth = "1";
	private String strokeDasharray = null;

	private final PostScriptCommandMacro setcolorgradient = new PostScriptCommandMacro("setcolorgradient");
	private final PostScriptCommandMacro simplerect = new PostScriptCommandMacro("simplerect");
	private final PostScriptCommandMacro roundrect = new PostScriptCommandMacro("roundrect");
	private boolean setcolorgradientUsed = false;
	private boolean simplerectUsed = false;
	private boolean roundrectUsed = false;

	public EpsGraphics() {
		header.append("%!PS-Adobe-3.0 EPSF-3.0\n");
		header.append("%%Creator: PlantUML\n");
		header.append("%%Title: noTitle\n");
		header.append("%%CreationDate: " + new Date() + "\n");
		setcolorgradient.add(new PostScriptCommandRaw("3 index 7 index sub 1 index mul 7 index add"));
		setcolorgradient.add(new PostScriptCommandRaw("3 index 7 index sub 2 index mul 7 index add"));
		setcolorgradient.add(new PostScriptCommandRaw("3 index 7 index sub 3 index mul 7 index add"));
		setcolorgradient.add(new PostScriptCommandRaw("setrgbcolor"));
		// setcolorgradient.add(new PostScriptCommandRaw("0 7 1 {pop} for"));
		setcolorgradient.add(new PostScriptCommandRaw("pop pop pop pop pop pop pop "));

		simplerect.add(new PostScriptCommandRaw("newpath moveto 1 index 0 rlineto"));
		simplerect.add(new PostScriptCommandRaw("0 exch rlineto"));
		simplerect.add(new PostScriptCommandRaw("neg 0 rlineto"));

		roundrect.add(new PostScriptCommandRaw("newpath"));
		roundrect.add(new PostScriptCommandRaw("dup 3 index add 2 index 2 index add 2 index 180 270 arc"));
		roundrect
				.add(new PostScriptCommandRaw("2 index 5 index add 1 index sub 2 index 2 index add 2 index 270 0 arc"));
		roundrect.add(new PostScriptCommandRaw(
				"2 index 5 index add 1 index sub 2 index 5 index add 2 index sub 2 index 0 90 arc"));
		roundrect.add(new PostScriptCommandRaw("dup 3 index add 2 index 5 index add 2 index sub 2 index 90 180 arc"));
		roundrect.add(new PostScriptCommandRaw("pop pop pop pop pop "));
	}

	private boolean closeDone = false;

	private int maxX = 10;
	private int maxY = 10;

	final protected void ensureVisible(double x, double y) {
		if (x > maxX) {
			maxX = (int) (x + 1);
		}
		if (y > maxY) {
			maxY = (int) (y + 1);
		}
	}

	protected final Color getColor() {
		return color;
	}

	public void close() {
		checkCloseDone();

		header.append("%%BoundingBox: 0 0 " + maxX + " " + maxY + "\n");
		// header.append("%%DocumentData: Clean7Bit\n");
		// header.append("%%DocumentProcessColors: Black\n");
		header.append("%%ColorUsage: Color\n");
		header.append("%%Origin: 0 0\n");
		header.append("%%EndComments\n\n");
		header.append("gsave\n");
		header.append("0 " + maxY + " translate\n");
		header.append("1 -1 scale\n");
		if (setcolorgradientUsed) {
			header.append(setcolorgradient.getPostStringDefinition());
		}
		if (simplerectUsed) {
			header.append(simplerect.getPostStringDefinition());
		}
		if (roundrectUsed) {
			header.append(roundrect.getPostStringDefinition());
		}

		append("grestore");

		// if(isClipSet())
		// writer.write("grestore\n");

		append("showpage");
		append("%%EOF");
		closeDone = true;
	}

	private void checkCloseDone() {
		if (closeDone) {
			throw new IllegalStateException();
		}
	}

	public String getEPSCode() {
		if (closeDone == false) {
			close();
		}
		return header.toString() + getBodyString();
	}

	protected String getBodyString() {
		return body.toString();
	}

	public final void setStrokeColor(Color c) {
		checkCloseDone();
		this.color = c;
	}

	public void setFillColor(Color c) {
		checkCloseDone();
		this.fillcolor = c;
	}

	public final void setStrokeWidth(String strokeWidth, String strokeDasharray) {
		checkCloseDone();
		this.strokeWidth = strokeWidth;
		this.strokeDasharray = strokeDasharray;
	}

	public void epsLine(double x1, double y1, double x2, double y2) {
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
		if (strokeDasharray != null) {
			append("[" + strokeDasharray + "] 0 setdash");
		}
		checkCloseDone();
		append(strokeWidth + " setlinewidth");
		appendColor(color);
		append("newpath");
		append(format(x1) + " " + format(y1) + " moveto");
		append(format(x2 - x1) + " " + format(y2 - y1) + " rlineto");
		append("closepath stroke");
		ensureVisible(Math.max(x1, x2), Math.max(y1, y2));
		if (strokeDasharray != null) {
			append("[] 0 setdash");
		}
	}

	public void epsPolygon(double... points) {
		checkCloseDone();
		double lastX = 0;
		double lastY = 0;
		if (fillcolor != null) {
			appendColor(fillcolor);
			append("newpath");
			for (int i = 0; i < points.length; i += 2) {
				ensureVisible(points[i], points[i + 1]);
				if (i == 0) {
					append(format(points[i]) + " " + format(points[i + 1]) + " moveto");
				} else {
					append(format(points[i] - lastX) + " " + format(points[i + 1] - lastY) + " rlineto");
				}
				lastX = points[i];
				lastY = points[i + 1];
			}
			append(format(points[0]) + " " + format(points[1]) + " lineto");
			append("closepath eofill");
		}

		if (color != null) {
			append(strokeWidth + " setlinewidth");
			appendColor(color);
			append("newpath");
			for (int i = 0; i < points.length; i += 2) {
				ensureVisible(points[i], points[i + 1]);
				if (i == 0) {
					append(format(points[i]) + " " + format(points[i + 1]) + " moveto");
				} else {
					append(format(points[i] - lastX) + " " + format(points[i + 1] - lastY) + " rlineto");
				}
				lastX = points[i];
				lastY = points[i + 1];
			}
			append(format(points[0]) + " " + format(points[1]) + " lineto");
			append("closepath stroke");
		}

	}

	public void epsRectangle(double x, double y, double width, double height, double rx, double ry) {
		checkCloseDone();
		ensureVisible(x, y);
		ensureVisible(x + width, y + height);
		if (fillcolor != null) {
			appendColor(fillcolor);
			epsRectangleInternal(x, y, width, height, rx, ry);
			append("closepath eofill");
		}

		if (color != null) {
			append(strokeWidth + " setlinewidth");
			appendColor(color);
			epsRectangleInternal(x, y, width, height, rx, ry);
			append("closepath stroke");
		}
	}

	public void epsRectangle(double x, double y, double width, double height, double rx, double ry, UGradient gr) {
		checkCloseDone();
		ensureVisible(x, y);
		ensureVisible(x + width, y + height);
		setcolorgradientUsed = true;

		if (rx == 0 && ry == 0) {
			simplerectUsed = true;
			appendColorShort(gr.getColor1());
			appendColorShort(gr.getColor2());
			append(format(width) + " " + format(height) + " " + format(x) + " " + format(y));
			append("100 -1 1 {");
			append("100 div");
			append("newpath");
			append("2 index 2 index moveto");
			append("dup 5 index mul 2 mul dup 0 rlineto");
			append("neg 4 index 2 index mul 2 mul rlineto");
			append("closepath eoclip");
			append("10 index 10 index 10 index");
			append("10 index 10 index 10 index");
			append("6 index setcolorgradient");
			append("4 index 4 index 4 index 4 index simplerect");
			append("closepath eofill");
			append("pop");
			append("} for");
			append("pop pop pop pop");
			append("pop pop pop");
			append("pop pop pop");
			append("initclip");
		} else {
			roundrectUsed = true;
			appendColorShort(gr.getColor1());
			appendColorShort(gr.getColor2());
			append(format(width) + " " + format(height) + " " + format(x) + " " + format(y) + " "
					+ format((rx + ry) / 2));
			append("100 -1 1 {");
			append("100 div");
			append("newpath");
			append("3 index 3 index moveto");
			append("dup 6 index mul 2 mul dup 0 rlineto");
			append("neg 5 index 2 index mul 2 mul rlineto");
			append("closepath eoclip");
			append("11 index 11 index 11 index");
			append("11 index 11 index 11 index");
			append("6 index setcolorgradient");
			append("5 index 5 index 5 index 5 index 5 index roundrect");
			append("closepath eofill");
			append("pop");
			append("} for");
			append("pop pop pop pop pop");
			append("pop pop pop");
			append("pop pop pop");
			append("initclip");
		}
	}

	private void epsRectangleInternal(double x, double y, double width, double height, double rx, double ry) {
		if (rx == 0 && ry == 0) {
			simpleRectangle(x, y, width, height);
		} else {
			roundRectangle(x, y, width, height, rx, ry);
		}
	}

	private void roundRectangle(double x, double y, double width, double height, double rx, double ry) {
		append(format(width) + " " + format(height) + " " + format(x) + " " + format(y) + " " + format((rx + ry) / 2)
				+ " roundrect");
		roundrectUsed = true;
	}

	private void simpleRectangle(double x, double y, double width, double height) {
		append(format(width) + " " + format(height) + " " + format(x) + " " + format(y) + " simplerect");
		simplerectUsed = true;
	}

	public void epsEllipse(double x, double y, double xRadius, double yRadius) {
		checkCloseDone();
		ensureVisible(x + xRadius, y + yRadius);
		if (xRadius != yRadius) {
			throw new UnsupportedOperationException();
		}
		if (fillcolor != null) {
			appendColor(fillcolor);
			append("newpath");
			append(format(x) + " " + format(y) + " " + format(xRadius) + " 0 360 arc");
			append("closepath eofill");
		}

		if (color != null) {
			append(strokeWidth + " setlinewidth");
			appendColor(color);
			append("newpath");
			append(format(x) + " " + format(y) + " " + format(xRadius) + " 0 360 arc");
			append("closepath stroke");
		}
	}

	protected void appendColor(Color c) {
		final double r = c.getRed() / 255.0;
		final double g = c.getGreen() / 255.0;
		final double b = c.getBlue() / 255.0;
		append(format(r) + " " + format(g) + " " + format(b) + " setrgbcolor");
	}

	protected void appendColorShort(Color c) {
		final double r = c.getRed() / 255.0;
		final double g = c.getGreen() / 255.0;
		final double b = c.getBlue() / 255.0;
		append(format(r) + " " + format(g) + " " + format(b));
	}

	public static String format(double x) {
		if (x == 0) {
			return "0";
		}
		String s = String.format(Locale.US, "%1.4f", x);
		s = s.replaceAll("(\\.\\d*?)0+$", "$1");
		if (s.endsWith(".")) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	protected void append(String s) {
		if (s.indexOf("  ") != -1) {
			throw new IllegalArgumentException(s);
		}
		body.append(s + "\n");
	}

	// FONT
	public void moveto(double x1, double y1) {
		append(format(x1) + " " + format(y1) + " moveto");
		ensureVisible(x1, y1);
	}

	public void lineto(double x1, double y1) {
		append(format(x1) + " " + format(y1) + " lineto");
		ensureVisible(x1, y1);
	}

	public void curveto(double x1, double y1, double x2, double y2, double x3, double y3) {
		append(format(x1) + " " + format(y1) + " " + format(x2) + " " + format(y2) + " " + format(x3) + " "
				+ format(y3) + " curveto");
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
		ensureVisible(x3, y3);
	}

	public void quadto(double x1, double y1, double x2, double y2) {
		append(format(x1) + " " + format(y1) + " " + format(x1) + " " + format(y1) + " " + format(x2) + " "
				+ format(y2) + " curveto");
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
	}

	public void newpath() {
		append("0 setlinewidth");
		append("[] 0 setdash");
		appendColor(color);
		append("newpath");
	}

	public void closepath() {
		append("closepath");
	}

	public void fill(int windingRule) {
		append("%fill");
		if (windingRule == PathIterator.WIND_EVEN_ODD) {
			append("eofill");
		} else if (windingRule == PathIterator.WIND_NON_ZERO) {
			append("fill");
		}
	}

	public void drawImage(BufferedImage image, double x, double y) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		append("gsave");
		append(format(x) + " " + format(y) + " translate");
		append(format(width) + " " + format(height) + " scale");
		append("" + width + " " + height + " 8 [" + width + " 0 0 -" + height + " 0 " + height + "]");
		// append("" + width + " " + height + " 8 [0 0 0 0 0 0]");
		append("{<");
		final StringBuilder sb = new StringBuilder();
		for (int j = height - 1; j >= 0; j--) {
			for (int i = 0; i < width; i++) {
				final String hexString = getRgb(image.getRGB(i, j));
				assert hexString.length() == 6;
				sb.append(hexString);
			}
		}
		append(sb.toString());
		// append(">} image");
		append(">} false 3 colorimage");
		ensureVisible(x + width, y + height);
		append("grestore");
	}

	static String getRgb(int x) {
		final String s = "000000" + Integer.toHexString(x);
		return s.substring(s.length() - 6);
	}

}
