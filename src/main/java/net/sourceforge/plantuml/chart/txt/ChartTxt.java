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
 */
package net.sourceforge.plantuml.chart.txt;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.chart.CartesianChart;
import net.sourceforge.plantuml.chart.ChartDiagram;
import net.sourceforge.plantuml.chart.ChartSeries;
import net.sourceforge.plantuml.klimt.creole.Display;

/**
 * A chart drawn as text (ATXT, UTXT): its title, its legend, then the chart itself. Bars
 * and lines are drawn on axes (see {@link CartesianTxt}); a pie, and the charts that
 * cannot be drawn so, are written as tables of values (see {@link ValueTableTxt}).
 */
public final class ChartTxt {

	private ChartTxt() {
	}

	public static List<String> getLines(ChartDiagram diagram, boolean unicode) {
		final TxtCharset charset = new TxtCharset(unicode);
		final ValueTableTxt tables = new ValueTableTxt(charset);
		List<String> body = new ArrayList<>();
		String legend = null;

		final ChartDiagram.Kind kind = diagram.getKind();
		if (diagram.getSeries().isEmpty()) {
			// Nothing to draw
		} else if (kind == ChartDiagram.Kind.PIE) {
			body = tables.pie(diagram.resolvePie());
		} else if (kind == ChartDiagram.Kind.RADAR) {
			body = tables.radar(diagram.resolveRadar());
		} else {
			final CartesianChart chart = diagram.resolveCartesian();
			if (CartesianTxt.isSupported(chart)) {
				final CartesianTxt cartesian = new CartesianTxt(chart, charset);
				body = cartesian.getLines();
				if (chart.getSeries().size() > 1 || chart.getLegendPosition() != ChartDiagram.LegendPosition.NONE)
					legend = getLegend(chart, cartesian);
			} else {
				body = tables.cartesian(chart);
			}
		}

		int width = 0;
		for (String line : body)
			width = Math.max(width, line.length());

		final List<String> result = new ArrayList<>();
		final Display title = diagram.getTitle().getDisplay();
		if (Display.isNull(title) == false)
			for (CharSequence line : title)
				result.add(center(line.toString(), width));
		if (legend != null)
			result.add(center(legend, width));
		if (result.isEmpty() == false && body.isEmpty() == false)
			result.add("");
		result.addAll(body);
		return result;
	}

	// The symbol and the name of each series: "█ Sales  ─ Target"
	private static String getLegend(CartesianChart chart, CartesianTxt cartesian) {
		final StringBuilder sb = new StringBuilder();
		for (ChartSeries s : chart.getSeries()) {
			if (sb.length() > 0)
				sb.append("  ");
			sb.append(cartesian.getSymbol(s)).append(' ').append(s.getName());
		}
		return sb.toString();
	}

	private static String center(String s, int width) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < (width - s.length()) / 2; i++)
			sb.append(' ');
		return CharGrid.rtrim(sb.append(s).toString());
	}
}
