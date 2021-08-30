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
package net.sourceforge.plantuml.nwdiag.next;

import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.nwdiag.core.Network;
import net.sourceforge.plantuml.nwdiag.core.NwGroup;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GridTextBlockDecoratedNext extends GridTextBlockSimpleNext {

	public static final int NETWORK_THIN = 5;

	private final List<NwGroup> groups;
	private final List<Network> networks;

	public GridTextBlockDecoratedNext(int lines, int cols, List<NwGroup> groups, List<Network> networks,
			ISkinParam skinparam) {
		super(lines, cols, skinparam);
		this.groups = groups;
		this.networks = networks;
	}

	@Override
	protected void drawGrid(UGraphic ug) {
		for (NwGroup group : groups) {
			drawGroups(ug, group, skinparam);
		}
		drawNetworkTube(ug);
		drawLinks(ug);
	}

	private void drawLinks(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (int i = 0; i < data.getNbLines(); i++) {
			final double lineHeight = lineHeight(stringBounder, i);
			double x = 0;
			for (int j = 0; j < data.getNbCols(); j++) {
				final double colWidth = colWidth(stringBounder, j);
				if (data.get(i, j) != null) {
					data.get(i, j).drawLinks(ug, x, colWidth, lineHeight);
				}
				x += colWidth;
			}
		}

	}

	private void drawGroups(UGraphic ug, NwGroup group, ISkinParam skinParam) {
		final StringBounder stringBounder = ug.getStringBounder();

		MinMax size = null;
		double y = 0;
		for (int i = 0; i < data.getNbLines(); i++) {
			final double lineHeight = lineHeight(stringBounder, i);
			double x = 0;
			for (int j = 0; j < data.getNbCols(); j++) {
				final double colWidth = colWidth(stringBounder, j);
				final LinkedElementNext element = data.get(i, j);
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
			group.drawGroup(ug, size, skinParam);
		}

	}

	private boolean isThereALink(int j, Network network) {
		for (int i = 0; i < data.getNbLines(); i++) {
			final LinkedElementNext element = data.get(i, j);
			if (element != null && element.isLinkedTo(network)) {
				return true;
			}
		}
		return false;
	}

	private void drawNetworkTube(final UGraphic ug) {

		final StringBounder stringBounder = ug.getStringBounder();
		double y = 0;
		for (int i = 0; i < data.getNbLines(); i++) {
			final Network network = getNetwork(i);
			computeMixMax(data.getLine(i), stringBounder, network);

			final URectangle rect = new URectangle(network.getXmax() - network.getXmin(), NETWORK_THIN);
			rect.setDeltaShadow(1.0);
			UGraphic ug2 = ug.apply(new UTranslate(network.getXmin(), y));
			if (network != null && network.getColor() != null) {
				ug2 = ug2.apply(network.getColor().bg());
			}
			if (network != null) {
				network.setY(y);
			}
			if (network.isVisible()) {
				ug2.draw(rect);
			}
			y += lineHeight(stringBounder, i);
		}
	}

	private void computeMixMax(LinkedElementNext line[], StringBounder stringBounder, Network network) {
		double x = 0;
		double xmin = network.isFullWidth() ? 0 : -1;
		double xmax = 0;
		for (int j = 0; j < line.length; j++) {
			final boolean hline = isThereALink(j, network);
			if (hline && xmin < 0) {
				xmin = x;
			}
			x += colWidth(stringBounder, j);
			if (hline || network.isFullWidth()) {
				xmax = x;
			}
		}
		network.setMinMax(xmin, xmax);

	}

	private Network getNetwork(int i) {
		return networks.get(i);
	}

}
