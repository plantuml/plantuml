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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final List<Network> networks;

	public GridTextBlockDecorated(int lines, int cols, Collection<DiagGroup> groups, List<Network> networks) {
		super(lines, cols);
		this.groups = groups;
		this.networks = networks;
	}

	@Override
	protected void drawGrid(UGraphic ug) {
		for (DiagGroup group : groups) {
			drawGroups(ug, group);
		}
		final Map<Network, Double> pos = drawNetworkTube(ug);
		drawLinks(ug, pos);
	}

	private void drawLinks(UGraphic ug, Map<Network, Double> pos) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (int i = 0; i < data.length; i++) {
			final double lineHeight = lineHeight(stringBounder, i);
			double x = 0;
			for (int j = 0; j < data[i].length; j++) {
				final double colWidth = colWidth(stringBounder, j);
				if (data[i][j] != null) {
					data[i][j].drawLinks(ug.apply(UTranslate.dx(x)), colWidth, lineHeight, pos);
				}
				x += colWidth;
			}
		}

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
					final MinMax minMax = element.getMinMax(stringBounder, colWidth, lineHeight)
							.translate(new UTranslate(x, y));
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

	private boolean isThereALink(int j, Network network) {
		for (int i = 0; i < data.length; i++) {
			final LinkedElement element = data[i][j];
			if (element != null && element.isLinkedTo(network)) {
				return true;
			}
		}
		return false;
	}

	private Map<Network, Double> drawNetworkTube(final UGraphic ug) {
		final Map<Network, Double> pos = new HashMap<Network, Double>();
		final StringBounder stringBounder = ug.getStringBounder();
		double y = 0;
		for (int i = 0; i < data.length; i++) {
			final Network network = getNetwork(i);
			double x = 0;
			double xmin = -1;
			double xmax = 0;
			for (int j = 0; j < data[i].length; j++) {
				final boolean hline = isThereALink(j, network);
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
			if (network != null) {
				pos.put(network, y);
			}
			ug2.draw(rect);
			y += lineHeight(stringBounder, i);
		}
		return pos;
	}

	private Network getNetwork(int i) {
		return networks.get(i);
	}
}
