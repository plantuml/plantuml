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
package net.sourceforge.plantuml.nwdiag;

import java.util.Collection;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class GridTextBlockDecorated extends GridTextBlockSimple {

	public static final HColorSet colors = HColorSet.instance();

	public static final int NETWORK_THIN = 5;

	private final Collection<DiagGroup> groups;

	public GridTextBlockDecorated(int lines, int cols, Collection<DiagGroup> groups) {
		super(lines, cols);
		this.groups = groups;
	}

	@Override
	public void drawGrid(UGraphic ug) {
		for (DiagGroup group : groups) {
			drawGroups(ug, group);
		}
		drawNetworkTube(ug);
	}

	private void drawGroups(UGraphic ug, DiagGroup group) {
		final StringBounder stringBounder = ug.getStringBounder();

		MinMax size = null;
		double y = 0;
		for (int i = 0; i < data.length; i++) {
			final double lineHeight = lineHeight(stringBounder, i);
			double x = 0;
			for (int j = 0; j < data[i].length; j++) {
				final double colWidth = colWidth(stringBounder, j);
				final LinkedElement element = data[i][j];
				if (element != null && group.matches(element)) {
					final MinMax minMax = element.getMinMax(stringBounder, colWidth, lineHeight).translate(
							new UTranslate(x, y));
					size = size == null ? minMax : size.addMinMax(minMax);
				}
				x += colWidth;
			}
			y += lineHeight;
		}
		if (size != null) {
			HColor color = group.getColor();
			if (color == null) {
				color = colors.getColorIfValid("#AAA");
			}
			size.draw(ug, color);
		}

	}

	private void drawNetworkTube(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		double y = 0;
		for (int i = 0; i < data.length; i++) {
			final Network network = getNetwork(i);
			double x = 0;
			double xmin = -1;
			double xmax = 0;
			for (int j = 0; j < data[i].length; j++) {
				final boolean hline = isPresent(i, j) || isPresent(i - 1, j);
				if (hline && xmin < 0) {
					xmin = x;
				}
				x += colWidth(stringBounder, j);
				if (hline) {
					xmax = x;
				}
			}
			final URectangle rect = new URectangle(xmax - xmin, NETWORK_THIN);
			rect.setDeltaShadow(1.0);
			UGraphic ug2 = ug.apply(new UTranslate(xmin, y));
			if (network != null && network.getColor() != null) {
				ug2 = ug2.apply(network.getColor().bg());
			}
			ug2.draw(rect);
			y += lineHeight(stringBounder, i);
		}
	}

	private Network getNetwork(int i) {
		for (int j = 0; j < data[i].length; j++) {
			if (isPresent(i, j)) {
				return data[i][j].getNetwork();
			}
		}
		return null;
	}
}
