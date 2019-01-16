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

public class VisioText implements VisioShape {

	private final int id;
	private final String text;
	private final int fontSize;
	private final double x;
	private final double y;
	private final double width;
	private final double height;

	private final double coefFont = 150.0;

	public static VisioText createInches(int id, String text, int fontSize, double x, double y, double width,
			double height) {
		final double coef = 1.8;
		return new VisioText(id, text, fontSize, toInches(x), toInches(y + 2.5), toInches(width * coef),
				toInches(height * coef));
	}

	private static double toInches(double val) {
		return val / 72.0;
	}

	private VisioText(int id, String text, int fontSize, double x, double y, double width, double height) {
		this.id = id;
		this.text = text;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.fontSize = fontSize;
	}

	public void print(OutputStream os) throws IOException {
		out(os, "<Shape ID='" + id + "' Type='Shape' LineStyle='1' FillStyle='1' TextStyle='3'>");
		out(os, "<XForm>");
		out(os, "<PinX>" + x + "</PinX>");
		out(os, "<PinY>" + y + "</PinY>");
		out(os, "<Width>" + width + "</Width>");
		out(os, "<Height>" + height + "</Height>");
		// out(os, "<PinX>1.0625</PinX>");
		// out(os, "<PinY>1.9375</PinY>");
		out(os, "<LocPinX F='Width*0'>0</LocPinX>");
		out(os, "<LocPinY F='Height*0'>0</LocPinY>");
		out(os, "<Angle>0</Angle>");
		out(os, "<FlipX>0</FlipX>");
		out(os, "<FlipY>0</FlipY>");
		out(os, "<ResizeMode>0</ResizeMode>");
		out(os, "</XForm>");
		out(os, "<TextBlock>");
		out(os, "<VerticalAlign>0</VerticalAlign>");
		out(os, "</TextBlock>");
		out(os, "<Char IX='" + id + "'>");
		out(os, "<Font F='Inh'>0</Font>");
		out(os, "<Color F='Inh'>0</Color>");
		out(os, "<Style F='Inh'>0</Style>");
		out(os, "<Case F='Inh'>0</Case>");
		out(os, "<Pos F='Inh'>0</Pos>");
		out(os, "<FontScale F='Inh'>1</FontScale>");
		out(os, "<Locale F='Inh'>0</Locale>");
		out(os, "<Size Unit='PT'>" + fontSize / coefFont + "</Size>");
		out(os, "<DblUnderline F='Inh'>0</DblUnderline>");
		out(os, "<Overline F='Inh'>0</Overline>");
		out(os, "<Strikethru F='Inh'>0</Strikethru>");
		out(os, "<Perpendicular F='Inh'>0</Perpendicular>");
		out(os, "<Letterspace F='Inh'>0</Letterspace>");
		out(os, "<ColorTrans F='Inh'>0</ColorTrans>");
		out(os, "</Char>");

		out(os, "<Para IX='" + id + "'>");
		out(os, "<HorzAlign>0</HorzAlign>");
		out(os, "</Para>");

		out(os, "<Text><cp IX='" + id + "'/><pp IX='" + id + "'/>" + text + "</Text>");
		out(os, "</Shape>");

	}

	public VisioShape yReverse(double maxY) {
		final double y2 = maxY - y;
		return new VisioText(id, text, fontSize, x, y2, width, height);
	}

	private void out(OutputStream os, String s) throws IOException {
		os.write(s.getBytes());
		os.write("\n".getBytes());
	}

}
