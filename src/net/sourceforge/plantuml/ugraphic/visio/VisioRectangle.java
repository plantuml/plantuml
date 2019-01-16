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

import java.io.IOException;
import java.io.OutputStream;

public class VisioRectangle implements VisioShape {

	private final int id;
	private final double x;
	private final double y;
	private final double width;
	private final double height;

	public static VisioRectangle createInches(int id, double x, double y, double width, double height) {
		return new VisioRectangle(id, toInches(x), toInches(y), toInches(width), toInches(height));
	}

	private VisioRectangle(int id, double x, double y, double width, double height) {
		if (x < 0 || y < 0 || width < 0 || height < 0) {
			// throw new IllegalArgumentException();
		}
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public VisioShape yReverse(double maxY) {
		final double y2 = maxY - (y + height);
		return new VisioRectangle(id, x, y2, width, height);
	}

	private static double toInches(double val) {
		return val / 72.0;
	}

	public void print(OutputStream os) throws IOException {
		out(os, "<Shape ID='" + id + "' Type='Shape' LineStyle='3' FillStyle='3' TextStyle='3'>");
		out(os, "<XForm>");
		out(os, "<PinX>" + (x + width / 2) + "</PinX>");
		out(os, "<PinY>" + (y + height / 2) + "</PinY>");
		out(os, "<Width>" + width + "</Width>");
		out(os, "<Height>" + height + "</Height>");
		// out(os, "<LocPinX F='Width*0.5'>" + (x + width / 2) + "</LocPinX>");
		// out(os, "<LocPinY F='Height*0.5'>" + (y + height / 2) + "</LocPinY>");
		out(os, "<Angle>0</Angle>");
		out(os, "<FlipX>0</FlipX>");
		out(os, "<FlipY>0</FlipY>");
		out(os, "<ResizeMode>0</ResizeMode>");
		out(os, "</XForm>");
		out(os, "<Geom IX='0'>");
		out(os, "<NoFill>0</NoFill>");
		out(os, "<NoLine>0</NoLine>");
		out(os, "<NoShow>0</NoShow>");
		out(os, "<NoSnap>0</NoSnap>");
		out(os, "<MoveTo IX='1'>");
		out(os, "<X F='Width*0'>0</X>");
		out(os, "<Y F='Height*0'>0</Y>");
		out(os, "</MoveTo>");
		out(os, "<LineTo IX='2'>");
		out(os, "<X F='Width*1'>" + width + "</X>");
		out(os, "<Y F='Height*0'>0</Y>");
		out(os, "</LineTo>");
		out(os, "<LineTo IX='3'>");
		out(os, "<X F='Width*1'>" + width + "</X>");
		out(os, "<Y F='Height*1'>" + height + "</Y>");
		out(os, "</LineTo>");
		out(os, "<LineTo IX='4'>");
		out(os, "<X F='Width*0'>0</X>");
		out(os, "<Y F='Height*1'>" + height + "</Y>");
		out(os, "</LineTo>");
		out(os, "<LineTo IX='5'>");
		out(os, "<X F='Geometry1.X1'>0</X>");
		out(os, "<Y F='Geometry1.Y1'>0</Y>");
		out(os, "</LineTo>");
		out(os, "</Geom>");
		out(os, "</Shape>");
	}

	private void out(OutputStream os, String s) throws IOException {
		os.write(s.getBytes());
		os.write("\n".getBytes());
	}

}
