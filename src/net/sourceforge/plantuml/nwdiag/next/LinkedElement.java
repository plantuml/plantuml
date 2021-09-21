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

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.nwdiag.VerticalLine;
import net.sourceforge.plantuml.nwdiag.core.NServer;
import net.sourceforge.plantuml.nwdiag.core.Network;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class LinkedElement {

	public static final int MAGIC = 15;

	private final TextBlock box;
	private final Network network;
	private final NServer server;
	private final Map<Network, TextBlock> conns;
	private final List<Network> networks;
	private final double topMargin;

	public LinkedElement(double topMargin, NServer server, TextBlock box, Map<Network, TextBlock> conns,
			List<Network> networks) {
		this.topMargin = topMargin;
		this.networks = networks;
		this.box = box;
		this.network = server.getMainNetworkNext();
		this.server = server;
		this.conns = conns;
	}

	public boolean isLinkedTo(Network some) {
		return conns.containsKey(some);
	}

	private final double marginAd = 10;

	private int marginBoxW() {
		return 15;
	}

	private double marginBoxH() {
		return topMargin;
	}

	public MinMax getMinMax(StringBounder stringBounder, double width, double height) {
		final double xMiddle = width / 2;
		final double yMiddle = height / 2;
		final Dimension2D dimBox = box.calculateDimension(stringBounder);

		final double x1 = xMiddle - dimBox.getWidth() / 2;
		final double y1 = yMiddle - dimBox.getHeight() / 2;
		final double x2 = xMiddle + dimBox.getWidth() / 2;
		final double y2 = yMiddle + dimBox.getHeight() / 2;
		return MinMax.getEmpty(false).addPoint(x1 - 5, y1 - 5).addPoint(x2 + 5, y2 + 5);
	}

	public void drawMe(UGraphic ug, double width, double height) {
		final double xMiddle = width / 2;
		final double yMiddle = height / 2;
		drawCenter(ug, box, xMiddle, yMiddle);
	}

	public void drawLinks(UGraphic ug, double xstart, double width, double height) {

		ug = ug.apply(UTranslate.dx(xstart));

		final double ynet1 = network.getY();
		final double yMiddle = height / 2;
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimBox = box.calculateDimension(stringBounder);

		final double alpha = yMiddle - dimBox.getHeight() / 2;
		final double posLink1 = (yMiddle - dimBox.getHeight() / 2 - topMargin + MAGIC) / 2;

		final double xMiddle = width / 2;
		final double xLinkPos = width / 2;

		final TreeSet<Double> skip = new TreeSet<>();

		for (Network n : networks) {
			if (xstart + xMiddle > n.getXmin() && xstart + xMiddle < n.getXmax())
				skip.add(n.getY());
		}

		if (server.printFirstLink()) {
			if (network.isVisible()) {
				new VerticalLine(ynet1 + GridTextBlockDecorated.NETWORK_THIN, ynet1 + alpha, skip)
						.drawU(ug.apply(UTranslate.dx(xLinkPos + network.magicDelta())));
			} else {
				new VerticalLine(ynet1, ynet1 + alpha, Collections.<Double>emptySet())
						.drawU(ug.apply(UTranslate.dx(xLinkPos + network.magicDelta())));
			}
		}
		drawCenter(ug, link1(), xMiddle + network.magicDelta(), ynet1 + posLink1);

		final double seven = 9.0;
		double x = xLinkPos - (conns.size() - 2) * seven / 2;
		boolean first = true;
		for (Entry<Network, TextBlock> ent : conns.entrySet()) {
			if (ent.getKey() == network) {
				continue;
			}
			final double ynet2 = ent.getKey().getY();
			new VerticalLine(ynet1 + yMiddle + dimBox.getHeight() / 2, ynet2, skip)
					.drawU(ug.apply(UTranslate.dx(x - ent.getKey().magicDelta())));
			final double xtext;
			if (first && conns.size() > 2) {
				xtext = x - ent.getValue().calculateDimension(stringBounder).getWidth() / 2;
			} else {
				xtext = x;
			}
			drawCenter(ug, ent.getValue(), xtext - ent.getKey().magicDelta(), ynet2 - alpha / 2);
			x += seven;
			first = false;

		}

	}

	private TextBlock link1() {
		return conns.get(network);
	}

	private TextBlock link2() {
		final int i = networks.indexOf(network);
		if (i == networks.size() - 1) {
			return null;
		}
		return conns.get(networks.get(i + 1));
	}

	private void drawCenter(UGraphic ug, TextBlock block, double x, double y) {
		if (block == null) {
			return;
		}
		final Dimension2D dim = block.calculateDimension(ug.getStringBounder());
		block.drawU(ug.apply(new UTranslate(x - dim.getWidth() / 2, y - dim.getHeight() / 2)));

	}

	public Dimension2D naturalDimension(StringBounder stringBounder) {
		final Dimension2D dimLink1 = link1() == null ? new Dimension2DDouble(0, 0)
				: link1().calculateDimension(stringBounder);
		final Dimension2D dimBox = box.calculateDimension(stringBounder);
		final Dimension2D dimLink2 = link2() == null ? new Dimension2DDouble(0, 0)
				: link2().calculateDimension(stringBounder);
		final double width = MathUtils.max(dimLink1.getWidth() + 2 * marginAd, dimBox.getWidth() + 2 * marginBoxW(),
				dimLink2.getWidth() + 2 * marginAd);
		final double height = dimLink1.getHeight() + 2 * marginAd + 2 * marginBoxH() + dimBox.getHeight()
				+ dimLink2.getHeight() + 2 * marginAd;
		return new Dimension2DDouble(width, height);
	}

	public final Network getNetwork() {
		return network;
	}

	public final NServer getServer() {
		return server;
	}

}
