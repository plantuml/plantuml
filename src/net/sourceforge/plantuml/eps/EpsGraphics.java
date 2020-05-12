/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.eps;

import java.awt.Color;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.ugraphic.ShadowManager;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.USegment;
import net.sourceforge.plantuml.ugraphic.USegmentType;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;
import net.sourceforge.plantuml.utils.MathUtils;
import net.sourceforge.plantuml.version.Version;

public class EpsGraphics {

	public static final String END_OF_FILE = "%plantuml done";
	protected static final long COEF = 100L;

	// http://www.linuxfocus.org/Francais/May1998/article43.html
	// http://www.tailrecursive.org/postscript/text.html
	private final StringBuilder body = new StringBuilder();
	private final StringBuilder header = new StringBuilder();

	private Color color = Color.BLACK;
	private Color fillcolor = Color.BLACK;

	private String strokeWidth = format(1);
	// private String strokeDasharray = null;

	private final PostScriptCommandMacro setcolorgradient = new PostScriptCommandMacro("setcolorgradient");
	private final PostScriptCommandMacro simplerect = new PostScriptCommandMacro("simplerect");
	private final PostScriptCommandMacro roundrect = new PostScriptCommandMacro("roundrect");
	private boolean setcolorgradientUsed = false;
	private boolean simplerectUsed = false;
	private boolean roundrectUsed = false;

	public EpsGraphics() {
		header.append("%!PS-Adobe-3.0 EPSF-3.0\n");
		header.append("%%Creator: PlantUML v" + Version.versionString(15) + BackSlash.NEWLINE);
		header.append("%%Title: noTitle\n");
		// header.append("%%CreationDate: " + new Date() + BackSlash.BS_N);
		setcolorgradient.add(new PostScriptCommandRaw("3 index 7 index sub 1 index mul 7 index add", true));
		setcolorgradient.add(new PostScriptCommandRaw("3 index 7 index sub 2 index mul 7 index add", true));
		setcolorgradient.add(new PostScriptCommandRaw("3 index 7 index sub 3 index mul 7 index add", true));
		setcolorgradient.add(new PostScriptCommandRaw("setrgbcolor", true));
		// setcolorgradient.add(new PostScriptCommandRaw("0 7 1 {pop} for"));
		setcolorgradient.add(new PostScriptCommandRaw("pop pop pop pop pop pop pop ", true));

		simplerect.add(new PostScriptCommandRaw("newpath moveto 1 index 0 rlineto", true));
		simplerect.add(new PostScriptCommandRaw("0 exch rlineto", true));
		simplerect.add(new PostScriptCommandRaw("neg 0 rlineto", true));

		roundrect.add(new PostScriptCommandRaw("newpath", true));
		roundrect.add(new PostScriptCommandRaw("dup 3 index add 2 index 2 index add 2 index 180 270 arc", true));
		roundrect.add(new PostScriptCommandRaw("2 index 5 index add 1 index sub 2 index 2 index add 2 index 270 0 arc",
				true));
		roundrect.add(new PostScriptCommandRaw(
				"2 index 5 index add 1 index sub 2 index 5 index add 2 index sub 2 index 0 90 arc", true));
		roundrect.add(new PostScriptCommandRaw("dup 3 index add 2 index 5 index add 2 index sub 2 index 90 180 arc",
				true));
		roundrect.add(new PostScriptCommandRaw("pop pop pop pop pop ", true));
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
		if (urlArea != null) {
			urlArea.ensureVisible((int) Math.round(x), (int) Math.round(y));
		}
	}

	protected final Color getColor() {
		return color;
	}

	public void close() {
		checkCloseDone();

		header.append("%%BoundingBox: 0 0 " + maxX + " " + maxY + BackSlash.NEWLINE);
		// header.append("%%DocumentData: Clean7Bit\n");
		// header.append("%%DocumentProcessColors: Black\n");
		header.append("%%ColorUsage: Color\n");
		header.append("%%Origin: 0 0\n");
		header.append("%%EndComments\n\n");
		header.append("gsave\n");
		header.append("0 " + maxY + " translate\n");
		header.append(".01 -.01 scale\n");

		if (setcolorgradientUsed) {
			header.append(setcolorgradient.getPostStringDefinition());
		}
		if (simplerectUsed) {
			header.append(simplerect.getPostStringDefinition());
		}
		if (roundrectUsed) {
			header.append(roundrect.getPostStringDefinition());
		}

		append("grestore", true);

		// if(isClipSet())
		// writer.write("grestore\n");

		append("showpage", true);
		append(END_OF_FILE, true);
		append("%%EOF", true);
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

	public final void setStrokeWidth(double strokeWidth, double dashVisible, double dashSpace) {
		checkCloseDone();
		this.strokeWidth = format(strokeWidth);
		this.dashVisible = (long) (dashVisible * COEF);
		this.dashSpace = (long) (dashSpace * COEF);
	}

	private long dashVisible = 0;
	private long dashSpace = 0;

	public void newpathDot() {
		final boolean dashed = isDashed();
		checkCloseDone();
		append(strokeWidth + " setlinewidth", true);
		appendColor(color);

		if (dashed) {
			append("[" + dashSpace + " " + dashVisible + "] 0 setdash", true);
		}
		append("newpath", true);
	}

	private boolean isDashed() {
		return dashVisible != 0 || dashSpace != 0;
	}

	private boolean isDashed2() {
		return dashVisible == 0 || dashSpace == 0;
	}

	private boolean isDashed3() {
		return dashSpace != 0 && dashVisible != 0;
	}

	public void closepathDot() {
		final boolean dashed = isDashed();
		append("stroke", true);
		if (dashed) {
			append("[] 0 setdash", true);
		}
	}

	public void epsLine(double x1, double y1, double x2, double y2) {
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
		checkCloseDone();
		append(strokeWidth + " setlinewidth", true);
		appendColor(color);
		append("newpath", true);
		if (isDashed2()) {
			append(format(x1) + " " + format(y1) + " moveto", true);
			append(format(x2 - x1) + " " + format(y2 - y1) + " rlineto", true);
		} else if (x1 == x2) {
			epsHLine(x1, Math.min(y1, y2), Math.max(y1, y2));
		} else if (y1 == y2) {
			epsVLine(y1, Math.min(x1, x2), Math.max(x1, x2));
		}
		append("stroke", true);
		ensureVisible(Math.max(x1, x2), Math.max(y1, y2));
	}

	protected void epsHLine(final double x, final double ymin, final double ymax) {
		append(format(x) + " " + format(ymin) + " moveto", true);
		for (long y2 = (long) (ymin * COEF); y2 < (long) (ymax * COEF); y2 += (dashVisible + dashSpace)) {
			final long v;
			if (y2 + dashVisible > (long) (ymax * COEF)) {
				v = y2 - (long) (ymax * COEF);
			} else {
				v = dashSpace;
			}
			append("0 " + v + " rlineto", true);
			append("0 " + dashSpace + " rmoveto", true);
		}
	}

	protected void epsVLine(final double y, final double xmin, final double xmax) {
		append(format(xmin) + " " + format(y) + " moveto", true);
		for (long x2 = (long) (xmin * COEF); x2 < (long) (xmax * COEF); x2 += (dashVisible + dashSpace)) {
			final long v;
			if (x2 + dashVisible > (long) (xmax * COEF)) {
				v = x2 - (long) (xmax * COEF);
			} else {
				v = dashSpace;
			}
			append("" + v + " 0 rlineto", true);
			append("" + dashSpace + " 0 rmoveto", true);
		}
	}

	public void epsPath(double x, double y, UPath path) {
		checkCloseDone();
		if (fillcolor != null) {
			appendColor(fillcolor);
			append("newpath", true);
			for (USegment seg : path) {
				final USegmentType type = seg.getSegmentType();
				final double coord[] = seg.getCoord();
				if (type == USegmentType.SEG_MOVETO) {
					movetoNoMacro(coord[0] + x, coord[1] + y);
				} else if (type == USegmentType.SEG_LINETO) {
					linetoNoMacro(coord[0] + x, coord[1] + y);
				} else if (type == USegmentType.SEG_QUADTO) {
					throw new UnsupportedOperationException();
				} else if (type == USegmentType.SEG_CUBICTO) {
					curvetoNoMacro(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y, coord[4] + x, coord[5] + y);
				} else if (type == USegmentType.SEG_CLOSE) {
					// Nothing
				} else if (type == USegmentType.SEG_ARCTO) {
					// Nothing
				} else {
					Log.println("unknown1 " + seg);
				}
			}
			append("closepath eofill", true);
		}

		if (color != null) {
			append(strokeWidth + " setlinewidth", true);
			appendColor(color);
			append("newpath", true);
			for (USegment seg : path) {
				final USegmentType type = seg.getSegmentType();
				final double coord[] = seg.getCoord();
				if (type == USegmentType.SEG_MOVETO) {
					movetoNoMacro(coord[0] + x, coord[1] + y);
				} else if (type == USegmentType.SEG_LINETO) {
					linetoNoMacro(coord[0] + x, coord[1] + y);
				} else if (type == USegmentType.SEG_QUADTO) {
					throw new UnsupportedOperationException();
				} else if (type == USegmentType.SEG_CUBICTO) {
					curvetoNoMacro(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y, coord[4] + x, coord[5] + y);
				} else if (type == USegmentType.SEG_CLOSE) {
					// Nothing
				} else if (type == USegmentType.SEG_ARCTO) {
					// Nothing
				} else {
					Log.println("unknown2 " + seg);
				}
			}
			append("stroke", true);
		}

	}

	public void epsPolygon(HColorGradient gr, ColorMapper mapper, double... points) {
		assert points.length % 2 == 0;
		setFillColor(mapper.toColor(gr.getColor1()));
		epsPolygon(points);
	}

	public void epsPolygon(double... points) {
		assert points.length % 2 == 0;
		checkCloseDone();
		double lastX = 0;
		double lastY = 0;
		if (fillcolor != null) {
			appendColor(fillcolor);
			append("newpath", true);
			for (int i = 0; i < points.length; i += 2) {
				ensureVisible(points[i], points[i + 1]);
				if (i == 0) {
					append(format(points[i]) + " " + format(points[i + 1]) + " moveto", true);
				} else {
					append(format(points[i] - lastX) + " " + format(points[i + 1] - lastY) + " rlineto", true);
				}
				lastX = points[i];
				lastY = points[i + 1];
			}
			append(format(points[0]) + " " + format(points[1]) + " lineto", true);
			append("closepath eofill", true);
		}

		if (color != null) {
			append(strokeWidth + " setlinewidth", true);
			appendColor(color);
			append("newpath", true);
			for (int i = 0; i < points.length; i += 2) {
				ensureVisible(points[i], points[i + 1]);
				if (i == 0) {
					append(format(points[i]) + " " + format(points[i + 1]) + " moveto", true);
				} else {
					append(format(points[i] - lastX) + " " + format(points[i + 1] - lastY) + " rlineto", true);
				}
				lastX = points[i];
				lastY = points[i + 1];
			}
			append(format(points[0]) + " " + format(points[1]) + " lineto", true);
			append("closepath stroke", true);
		}

	}

	public void epsRectangle(double x, double y, double width, double height, double rx, double ry) {
		checkCloseDone();
		ensureVisible(x, y);
		ensureVisible(x + width, y + height);
		if (fillcolor != null) {
			appendColor(fillcolor);
			epsRectangleInternal(x, y, width, height, rx, ry, true);
			append("closepath eofill", true);
			if (isDashed3()) {
				append("[] 0 setdash", true);
			}

		}

		if (color != null) {
			append(strokeWidth + " setlinewidth", true);
			appendColor(color);
			epsRectangleInternal(x, y, width, height, rx, ry, false);
			append("closepath stroke", true);
			if (isDashed3()) {
				append("[] 0 setdash", true);
			}
		}
	}

	public void epsRectangle(double x, double y, double width, double height, double rx, double ry,
			HColorGradient gr, ColorMapper mapper) {
		checkCloseDone();
		ensureVisible(x, y);
		ensureVisible(x + width, y + height);
		setcolorgradientUsed = true;

		if (rx == 0 && ry == 0) {
			simplerectUsed = true;
			appendColorShort(mapper.toColor(gr.getColor1()));
			appendColorShort(mapper.toColor(gr.getColor2()));
			append(format(width) + " " + format(height) + " " + format(x) + " " + format(y), true);
			append("100 -1 1 {", true);
			append("100 div", true);
			append("newpath", true);
			append("2 index 2 index moveto", true);
			append("dup 5 index mul 2 mul dup 0 rlineto", true);
			append("neg 4 index 2 index mul 2 mul rlineto", true);
			append("closepath eoclip", true);
			append("10 index 10 index 10 index", true);
			append("10 index 10 index 10 index", true);
			append("6 index setcolorgradient", true);
			append("4 index 4 index 4 index 4 index simplerect", true);
			append("closepath eofill", true);
			append("pop", true);
			append("} for", true);
			append("pop pop pop pop", true);
			append("pop pop pop", true);
			append("pop pop pop", true);
			append("initclip", true);
		} else {
			roundrectUsed = true;
			appendColorShort(mapper.toColor(gr.getColor1()));
			appendColorShort(mapper.toColor(gr.getColor2()));
			append(format(width) + " " + format(height) + " " + format(x) + " " + format(y) + " "
					+ format((rx + ry) / 2), true);
			append("100 -1 1 {", true);
			append("100 div", true);
			append("newpath", true);
			append("3 index 3 index moveto", true);
			append("dup 6 index mul 2 mul dup 0 rlineto", true);
			append("neg 5 index 2 index mul 2 mul rlineto", true);
			append("closepath eoclip", true);
			append("11 index 11 index 11 index", true);
			append("11 index 11 index 11 index", true);
			append("6 index setcolorgradient", true);
			append("5 index 5 index 5 index 5 index 5 index roundrect", true);
			append("closepath eofill", true);
			append("pop", true);
			append("} for", true);
			append("pop pop pop pop pop", true);
			append("pop pop pop", true);
			append("pop pop pop", true);
			append("initclip", true);
		}
	}

	private void epsRectangleInternal(double x, double y, double width, double height, double rx, double ry,
			boolean fill) {
		if (rx == 0 && ry == 0) {
			simpleRectangle(x, y, width, height, fill);
		} else {
			roundRectangle(x, y, width, height, rx, ry);
		}
	}

	private void roundRectangle(double x, double y, double width, double height, double rx, double ry) {
		if (isDashed3()) {
			append("[" + dashSpace + " " + dashVisible + "] 0 setdash", true);
		}
		final double round = MathUtils.min((rx + ry) / 2, width / 2, height / 2);
		append(format(width) + " " + format(height) + " " + format(x) + " " + format(y) + " " + format(round)
				+ " roundrect", true);
		roundrectUsed = true;
	}

	private void simpleRectangle(double x, double y, double width, double height, boolean fill) {
		if (isDashed3()) {
			append("[" + dashSpace + " " + dashVisible + "] 0 setdash", true);
		}
		// if (isDashed3() || fill) {
			append(format(width) + " " + format(height) + " " + format(x) + " " + format(y) + " simplerect", true);
			simplerectUsed = true;
		// }
	}

	/**
	 * Converts a counter clockwise angle to a clockwise
	 * angle. i.e. 0 -> 360, 90 -> 270, 180 -> 180, 270 -> 90
	 * @param counterClockwise counter clockwise angle in degrees
	 * @return clockwise angle in degrees
	 */
	private double convertToClockwiseAngle(double counterClockwise) {
		return 360.0 - counterClockwise;
	}

	public void epsEllipse(double x, double y, double xRadius, double yRadius, double start, double extend) {
		checkCloseDone();
		ensureVisible(x + xRadius, y + yRadius);
		double scale = 1;
		if (xRadius != yRadius) {
			scale = yRadius / xRadius;
			append("gsave", true);
			append("1 " + format(scale) + " scale", true);
		}
		// if (fillcolor != null) {
		// appendColor(fillcolor);
		// append("newpath", true);
		// append(format(x) + " " + format(y / scale) + " " + format(xRadius) + " 0 360 arc", true);
		// append("closepath eofill", true);
		// }

		if (color != null) {
			append(strokeWidth + " setlinewidth", true);
			appendColor(color);
			append("newpath", true);



			final double a1 = convertToClockwiseAngle(start + extend);
			final double a2 = convertToClockwiseAngle(start);
			append(format(x) + " " + format(y / scale) + " " + format(xRadius) + " " + (long)a1 + " " + (long)a2
					+ " arc", true);
			append("stroke", true);
		}

		if (scale != 1) {
			append("grestore", true);
		}
	}

	public void epsEllipse(double x, double y, double xRadius, double yRadius) {
		checkCloseDone();
		ensureVisible(x + xRadius, y + yRadius);
		double scale = 1;
		if (xRadius != yRadius) {
			scale = yRadius / xRadius;
			append("gsave", true);
			append("1 " + formatSimple4(scale) + " scale", true);
		}
		if (fillcolor != null) {
			appendColor(fillcolor);
			append("newpath", true);
			append(format(x) + " " + format(y / scale) + " " + format(xRadius) + " 0 360 arc", true);
			append("closepath eofill", true);
		}

		if (color != null) {
			append(strokeWidth + " setlinewidth", true);
			appendColor(color);
			append("newpath", true);
			append(format(x) + " " + format(y / scale) + " " + format(xRadius) + " 0 360 arc", true);
			append("closepath stroke", true);
		}

		if (scale != 1) {
			append("grestore", true);
		}
	}

	protected void appendColor(Color c) {
		if (c == null) {
			return;
		}
		final double r = c.getRed() / 255.0;
		final double g = c.getGreen() / 255.0;
		final double b = c.getBlue() / 255.0;
		append(formatSimple2(r) + " " + formatSimple2(g) + " " + formatSimple2(b) + " setrgbcolor", true);
	}

	protected void appendColorShort(Color c) {
		if (c == null) {
			return;
		}
		final double r = c.getRed() / 255.0;
		final double g = c.getGreen() / 255.0;
		final double b = c.getBlue() / 255.0;
		append(formatSimple2(r) + " " + formatSimple2(g) + " " + formatSimple2(b), true);
	}

	static String format(double x) {
		if (x == 0) {
			return "0";
		}
		return Long.toString((long) (x * COEF));
	}

	public static String formatSimple4(double x) {
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

	private static String formatSimple2(double x) {
		if (x == 0) {
			return "0";
		}
		String s = String.format(Locale.US, "%1.2f", x);
		s = s.replaceAll("(\\.\\d*?)0+$", "$1");
		if (s.endsWith(".")) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	protected void append(String s, boolean checkConsistence) {
		if (checkConsistence && s.indexOf("  ") != -1) {
			throw new IllegalArgumentException(s);
		}
		body.append(s + BackSlash.NEWLINE);
	}

	final public void linetoNoMacro(double x1, double y1) {
		append(format(x1) + " " + format(y1) + " lineto", true);
		ensureVisible(x1, y1);
	}

	final public void movetoNoMacro(double x1, double y1) {
		append(format(x1) + " " + format(y1) + " moveto", true);
		ensureVisible(x1, y1);
	}

	final public void curvetoNoMacro(double x1, double y1, double x2, double y2, double x3, double y3) {
		append(format(x1) + " " + format(y1) + " " + format(x2) + " " + format(y2) + " " + format(x3) + " "
				+ format(y3) + " curveto", true);
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
		ensureVisible(x3, y3);
	}

	// FONT
	public void moveto(double x1, double y1) {
		append(format(x1) + " " + format(y1) + " moveto", true);
		ensureVisible(x1, y1);
	}

	public void lineto(double x1, double y1) {
		append(format(x1) + " " + format(y1) + " lineto", true);
		ensureVisible(x1, y1);
	}

	public void curveto(double x1, double y1, double x2, double y2, double x3, double y3) {
		append(format(x1) + " " + format(y1) + " " + format(x2) + " " + format(y2) + " " + format(x3) + " "
				+ format(y3) + " curveto", true);
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
		ensureVisible(x3, y3);
	}

	public void quadto(double x1, double y1, double x2, double y2) {
		append(format(x1) + " " + format(y1) + " " + format(x1) + " " + format(y1) + " " + format(x2) + " "
				+ format(y2) + " curveto", true);
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
	}

	public void newpath() {
		append("0 setlinewidth", true);
		appendColor(color);
		append("newpath", true);
	}

	public void closepath() {
		append("closepath", true);
	}

	public void fill(int windingRule) {
		append("%fill", true);
		if (windingRule == PathIterator.WIND_EVEN_ODD) {
			append("eofill", true);
		} else if (windingRule == PathIterator.WIND_NON_ZERO) {
			append("fill", true);
		}
	}

	public void drawImage(BufferedImage image, double x, double y) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		append("gsave", true);
		append(format(x) + " " + format(y) + " translate", true);
		append(format(width) + " " + format(height) + " scale", true);
		append("" + width + " " + height + " 8 [" + width + " 0 0 -" + height + " 0 " + height + "]", true);
		// append("" + width + " " + height + " 8 [0 0 0 0 0 0]");
		append("{<", true);
		final StringBuilder sb = new StringBuilder();
		for (int j = height - 1; j >= 0; j--) {
			for (int i = 0; i < width; i++) {
				final String hexString = getRgb(image.getRGB(i, j));
				assert hexString.length() == 6;
				sb.append(hexString);
			}
		}
		append(sb.toString(), true);
		// append(">} image");
		append(">} false 3 colorimage", true);
		ensureVisible(x + width, y + height);
		append("grestore", true);
	}

	static String getRgb(int x) {
		final String s = "000000" + Integer.toHexString(x);
		return s.substring(s.length() - 6);
	}

	public void drawEps(String eps, double x, double y) {

		final int idx = eps.indexOf("%%BoundingBox:");
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		final StringTokenizer st = new StringTokenizer(eps.substring(idx + "%%BoundingBox:".length()), " \n\t\r");
		final int x1 = Integer.parseInt(st.nextToken());
		final int y1 = Integer.parseInt(st.nextToken());
		final int x2 = Integer.parseInt(st.nextToken());
		final int y2 = Integer.parseInt(st.nextToken());
		assert x2 >= x1;
		assert y2 >= y1;

		append("gsave", true);
		final double dx = x - x1;
		final double dy = y + y2;
		append(format(dx) + " " + format(dy) + " translate", true);
		append("1 -1 scale", true);
		append(eps, false);
		ensureVisible(x + (x2 - x1), y + (y2 - y1));
		append("grestore", true);
	}

	protected final long getDashVisible() {
		return dashVisible;
	}

	protected final long getDashSpace() {
		return dashSpace;
	}

	static class UrlArea {
		private final String url;
		private int xmin = Integer.MAX_VALUE;
		private int xmax = Integer.MIN_VALUE;
		private int ymin = Integer.MAX_VALUE;
		private int ymax = Integer.MIN_VALUE;

		UrlArea(String url) {
			this.url = url;
		}

		void ensureVisible(int x, int y) {
			if (x < xmin) {
				xmin = x;
			}
			if (x > xmax) {
				xmax = x;
			}
			if (y < ymin) {
				ymin = y;
			}
			if (y > ymax) {
				ymax = y;
			}
		}
	}

	private UrlArea urlArea;

	public void closeLink() {
		if (urlArea != null && urlArea.xmin != Integer.MAX_VALUE) {
			final int width = urlArea.xmax - urlArea.xmin;
			final int height = urlArea.ymax - urlArea.ymin;
			assert width >= 0 && height >= 0;
			epsUrlLink(urlArea.xmin, urlArea.ymin, width, height, urlArea.url);
		}
		this.urlArea = null;
	}

	public void epsUrlLink(int x, int y, int width, int height, String url) {
		append("[ /Rect [ " + x + " " + y + " " + (x + width) + " " + (y + height) + " ]", true);
		append("/Border [ 0 0 0 ]", true);
		append("/Action << /Subtype /URI /URI (" + url + ") >>", true);
		append("/Subtype /Link", true);
		append("/ANN pdfmark", true);
	}

	public void openLink(String url) {
		this.urlArea = new UrlArea(url);
	}

	// Shadow
	final private ShadowManager shadowManager = new ShadowManager(50, 200);

	public void epsRectangleShadow(double x, double y, double width, double height, double rx, double ry,
			double deltaShadow) {
		setStrokeColor(null);
		for (double i = 0; i <= deltaShadow; i += 0.5) {
			setFillColor(shadowManager.getColor(i, deltaShadow));
			final double diff = i;
			epsRectangle(x + deltaShadow + diff, y + deltaShadow + diff, width - 2 * diff, height - 2 * diff, rx + 1,
					ry + 1);
		}
	}

	public void epsPolygonShadow(double deltaShadow, double... points) {
		assert points.length % 2 == 0;
		setStrokeColor(null);
		for (double i = 0; i <= deltaShadow; i += 0.5) {
			setFillColor(shadowManager.getColor(i, deltaShadow));
			final double diff = i;
			epsPolygon(shadowManager.getShadowDeltaPoints(deltaShadow, diff, points));
		}
	}

	public void epsEllipseShadow(double x, double y, double xRadius, double yRadius, double deltaShadow) {
		setStrokeColor(null);
		for (double i = 0; i <= deltaShadow; i += 0.5) {
			setFillColor(shadowManager.getColor(i, deltaShadow));
			final double diff = i;
			epsEllipse(x + deltaShadow, y + deltaShadow, xRadius - diff, yRadius - diff);
		}
	}

}
