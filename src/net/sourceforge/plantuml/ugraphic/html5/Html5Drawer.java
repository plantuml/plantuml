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
 */
package net.sourceforge.plantuml.ugraphic.html5;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.tikz.TikzGraphics;

public class Html5Drawer {

	private int maxX = 10;
	private int maxY = 10;
	private String strokeStyle = "black";
	private String fillStyle = "black";

	private List<String> data = new ArrayList<String>();

	final protected void ensureVisible(double x, double y) {
		if (x > maxX) {
			maxX = (int) (x + 1);
		}
		if (y > maxY) {
			maxY = (int) (y + 1);
		}
	}

	private static String format(double x) {
		return TikzGraphics.format(x);
	}

	public final void setStrokeColor(String stroke) {
		this.strokeStyle = stroke;
	}

	public final void setFillColor(String fill) {
		this.fillStyle = fill;
	}

	public String generateHtmlCode() {
		final StringBuilder sb = new StringBuilder();
		ap(sb, "<html>");
		ap(sb, "<canvas id=\"demo\" width=\"700\" height=\"350\">");
		ap(sb, "</canvas>");
		ap(sb, "</html>");
		ap(sb, "<script>");
		ap(sb, "window.addEventListener('load', function () {");
		ap(sb, "var elem = document.getElementById('demo');");
		ap(sb, "if (!elem || !elem.getContext) { return;}");
		ap(sb, "var ctx = elem.getContext('2d');");
		ap(sb, "if (!ctx) { return;}");
		// ap(sb, "ctx.fillStyle = 'green';");
		// ap(sb, "ctx.fillRect(30, 30, 55, 50);");
		for (String s : data) {
			ap(sb, s);
		}
		ap(sb, "}, false);");
		ap(sb, "</script>");
		ap(sb, "</html>");
		return sb.toString();
	}

	private void ap(StringBuilder sb, String s) {
		sb.append(s);
		sb.append('\n');
	}

	public void htmlRectangle(double x, double y, double width, double height, double rx, double ry) {
		ensureVisible(x, y);
		ensureVisible(x + width, y + height);
		// if (fillcolor != null) {
		// appendColor(fillcolor);
		// epsRectangleInternal(x, y, width, height, rx, ry, true);
		// append("closepath eofill", true);
		// }
		//
		// if (color != null) {
		// append(strokeWidth + " setlinewidth", true);
		// appendColor(color);
		// epsRectangleInternal(x, y, width, height, rx, ry, false);
		// append("closepath stroke", true);
		// }
		data.add("//RECT");
		data.add("ctx.strokeStyle='" + strokeStyle + "';");
		data.add("ctx.fillStyle='" + fillStyle + "';");
		data.add("ctx.rect(" + format(x) + "," + format(y) + "," + format(width) + "," + format(height) + ");");
		data.add("ctx.fill();");
		data.add("ctx.stroke();");
	}

	public void htmlLine(double x1, double y1, double x2, double y2, double deltaShadow) {
		ensureVisible(x1 + 2 * deltaShadow, y1 + 2 * deltaShadow);
		ensureVisible(x2 + 2 * deltaShadow, y2 + 2 * deltaShadow);
		data.add("ctx.strokeStyle='" + strokeStyle + "';");
		data.add("ctx.beginPath();");
		data.add("ctx.moveTo(" + format(x1) + "," + format(y1) + ");");
		data.add("ctx.lineTo(" + format(x2) + "," + format(y2) + ");");
		data.add("ctx.stroke();");
		data.add("ctx.closePath();");
	}

}
