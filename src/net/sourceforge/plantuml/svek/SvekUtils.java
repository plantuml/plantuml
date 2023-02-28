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
package net.sourceforge.plantuml.svek;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.utils.Log;

public class SvekUtils {

	// ::comment when __CORE__
	static public void traceString(final SFile f, String text) throws IOException {
		Log.info("Creating intermediate file " + f.getPrintablePath());
		try (PrintWriter pw = f.createPrintWriter()) {
			pw.print(text);
		}
	}
	// ::done

	static public double getValue(String svg, int starting, String varName) {
		final String varNameString = varName + "=\"";
		int p1 = svg.indexOf(varNameString, starting);
		if (p1 == -1) {
			throw new IllegalStateException();
		}
		p1 += varNameString.length();
		final int p2 = svg.indexOf('\"', p1);
		return Double.parseDouble(svg.substring(p1, p2));

	}

	public static XPoint2D getMinXY(List<XPoint2D> points) {
		double minx = points.get(0).x;
		double miny = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x < minx)
				minx = points.get(i).x;
			if (points.get(i).y < miny)
				miny = points.get(i).y;
		}
		return new XPoint2D(minx, miny);
	}

	public static XPoint2D getMaxXY(List<XPoint2D> points) {
		double maxx = points.get(0).x;
		double maxy = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x > maxx)
				maxx = points.get(i).x;
			if (points.get(i).y > maxy)
				maxy = points.get(i).y;
		}

		return new XPoint2D(maxx, maxy);
	}

	public static void println(StringBuilder sb) {
		sb.append('\n');
	}

	public static String pixelToInches(double pixel) {
		final double v = pixel / 72.0;
		return String.format(Locale.US, "%6.6f", v);
	}

}
