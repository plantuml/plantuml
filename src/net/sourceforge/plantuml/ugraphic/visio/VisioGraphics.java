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
package net.sourceforge.plantuml.ugraphic.visio;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.golem.MinMaxDouble;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.USegment;
import net.sourceforge.plantuml.ugraphic.USegmentType;

public class VisioGraphics {

	private final List<VisioShape> shapes = new ArrayList<VisioShape>();
	private final MinMaxDouble limits = new MinMaxDouble();

	public void createVsd(OutputStream os) throws IOException {
		final double width = toInches(limits.getMaxX());
		final double height = toInches(limits.getMaxY());

		out(os, "<?xml version='1.0' encoding='utf-8' ?>");
		out(os,
				"<VisioDocument xml:space='preserve' xmlns='http://schemas.microsoft.com/visio/2003/core' xmlns:vx='http://schemas.microsoft.com/visio/2006/extension' xmlns:v14='http://schemas.microsoft.com/office/visio/2010/extension'>");
		out(os, "<DocumentProperties>");
		out(os, "<Creator>PlantUML</Creator>");
		out(os, "</DocumentProperties>");
		out(os, "<DocumentSheet NameU='TheDoc' LineStyle='0' FillStyle='0' TextStyle='0'>");
		out(os, "</DocumentSheet>");
		out(os, "<Masters/>");
		out(os, "<Pages>");
		out(os, "<Page ID='0' NameU='Page-1' Name='Page 1' ViewScale='1' ViewCenterX='" + (width / 2)
				+ "' ViewCenterY='" + (height / 2) + "'>");
		out(os, "<PageSheet LineStyle='0' FillStyle='0' TextStyle='0'>");
		out(os, "<PageProps>");
		out(os, "<PageWidth Unit='IN_F'>" + width + "</PageWidth>");
		out(os, "<PageHeight Unit='IN_F'>" + height + "</PageHeight>");
		out(os, "<PageScale Unit='IN_F'>1</PageScale>");
		out(os, "<DrawingScale Unit='IN_F'>2</DrawingScale>"); // change for scale
		out(os, "<DrawingSizeType>3</DrawingSizeType>");
		out(os, "<DrawingScaleType>0</DrawingScaleType>");
		out(os, "<InhibitSnap>0</InhibitSnap>");
		out(os, "</PageProps>");
		out(os, "</PageSheet>");
		out(os, "<Shapes>");
		for (VisioShape sh : shapes) {
			sh.yReverse(height).print(os);
			// sh.print(os);
		}
		out(os, "</Shapes>");
		out(os, "</Page>");
		out(os, "</Pages>");
		out(os, "</VisioDocument>");
	}

	private void out(OutputStream os, String s) throws IOException {
		os.write(s.getBytes());
		os.write("\n".getBytes());
	}

	private double toInches(double val) {
		return val / 72.0;
	}

	private void ensureVisible(double x, double y) {
		limits.manage(x, y);
	}

	public void rectangle(double x, double y, double width, double height) {
		ensureVisible(x, y);
		ensureVisible(x + width, y + height);
		final VisioRectangle rect = VisioRectangle.createInches(shapes.size() + 1, x, y, width, height);
		shapes.add(rect);
	}

	public void text(String text, double x, double y, String family, int fontSize, double width, double height,
			Map<String, String> attributes) {
		// System.err.println("x=" + x);
		// System.err.println("y=" + y);
		// System.err.println("text=" + text);
		// System.err.println("family=" + family);
		// System.err.println("fontSize=" + fontSize);
		// System.err.println("width=" + width);
		// System.err.println("attributes=" + attributes);
		ensureVisible(x, y);
		final VisioText txt = VisioText.createInches(shapes.size() + 1, text, fontSize, x, y, width, height);
		shapes.add(txt);

	}

	public void line(double x1, double y1, double x2, double y2) {
		ensureVisible(x1, y1);
		if (x1 == x2 && y1 == y2) {
			return;
		}
		ensureVisible(x2, y2);
		final VisioLine line = VisioLine.createInches(shapes.size() + 1, x1, y1, x2, y2);
		shapes.add(line);
	}

	private void line(Point2D p1, Point2D p2) {
		line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public void upath(final double x, final double y, UPath path) {
		double lx = x;
		double ly = y;
		for (USegment seg : path) {
			final USegmentType type = seg.getSegmentType();
			final double coord[] = seg.getCoord();
			if (type == USegmentType.SEG_MOVETO) {
				lx = coord[0] + x;
				ly = coord[1] + y;
			} else if (type == USegmentType.SEG_LINETO) {
				line(lx, ly, coord[0] + x, coord[1] + y);
				lx = coord[0] + x;
				ly = coord[1] + y;
			} else if (type == USegmentType.SEG_QUADTO) {
				line(lx, ly, coord[2] + x, coord[3] + y);
				lx = coord[2] + x;
				ly = coord[3] + y;
			} else if (type == USegmentType.SEG_CUBICTO) {
				line(lx, ly, coord[4] + x, coord[5] + y);
				// linePoint(lx, ly, coord[0] + x, coord[1] + y);
				// linePoint(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y);
				// linePoint(coord[2] + x, coord[3] + y, coord[4] + x, coord[5] + y);
				lx = coord[4] + x;
				ly = coord[5] + y;
			} else if (type == USegmentType.SEG_CLOSE) {
				// Nothing
			} else if (type == USegmentType.SEG_ARCTO) {
				// Nothing
			} else {
				Log.println("unknown5 " + seg);
			}

		}

	}

	public void polygon(UPolygon poly) {
		Point2D last = null;
		for (Point2D pt : poly.getPoints()) {
			if (last != null) {
				line(last, pt);
			}
			last = pt;
		}
	}

}
