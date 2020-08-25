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

import java.awt.geom.Dimension2D;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.MathUtils;

public class LinkedElement {

	private final TextBlock box;
	private final Network network;
	private final Network next;
	private final DiagElement element;
	private final Map<Network, TextBlock> conns;

	public LinkedElement(DiagElement element, TextBlock box, Network network, Network next,
			Map<Network, TextBlock> conns) {
		this.box = box;
		this.network = network;
		this.element = element;
		this.conns = conns;
		this.next = next;
	}

	public boolean isLinkedTo(Network some) {
		return conns.containsKey(some);
	}

	private final double marginAd = 10;
	private final double marginBox = 15;

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

	public void drawLinks(UGraphic ug, double width, double height, Map<Network, Double> pos) {
		final double ynet1 = pos.get(network);
		final double yMiddle = height / 2;
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimBox = box.calculateDimension(stringBounder);

		final double alpha = yMiddle - dimBox.getHeight() / 2;

		final HColor color = ColorParam.activityBorder.getDefaultValue();
		ug = ug.apply(color);

		final double xMiddle = width / 2;

		final TreeSet<Double> skip = new TreeSet<Double>(pos.values());
		new VerticalLine(ynet1 + GridTextBlockDecorated.NETWORK_THIN, ynet1 + alpha, skip)
				.drawU(ug.apply(UTranslate.dx(xMiddle)));
		drawCenter(ug, link1(), xMiddle, ynet1 + alpha / 2);

		final double seven = 7.0;
		double x = xMiddle - (conns.size() - 2) * seven / 2;
		boolean first = true;
		for (Entry<Network, TextBlock> ent : conns.entrySet()) {
			if (ent.getKey() == network) {
				continue;
			}
			final Double ynet2 = pos.get(ent.getKey());
			new VerticalLine(ynet1 + yMiddle + dimBox.getHeight() / 2, ynet2, skip).drawU(ug.apply(UTranslate.dx(x)));
			final double xtext;
			if (first && conns.size() > 2) {
				xtext = x - ent.getValue().calculateDimension(stringBounder).getWidth() / 2;
			} else {
				xtext = x;
			}
			drawCenter(ug, ent.getValue(), xtext, ynet2 - alpha / 2);
			x += seven;
			first = false;

		}

	}

	private TextBlock link1() {
		return conns.get(network);
	}

	private TextBlock link2() {
		if (next == null) {
			return null;
		}
		return conns.get(next);
	}

	private void drawCenter(UGraphic ug, TextBlock block, double x, double y) {
		final Dimension2D dim = block.calculateDimension(ug.getStringBounder());
		block.drawU(ug.apply(new UTranslate(x - dim.getWidth() / 2, y - dim.getHeight() / 2)));

	}

	public Dimension2D naturalDimension(StringBounder stringBounder) {
		final Dimension2D dim1 = link1().calculateDimension(stringBounder);
		final Dimension2D dimBox = box.calculateDimension(stringBounder);
		final Dimension2D dim2 = link2() == null ? new Dimension2DDouble(0, 0)
				: link2().calculateDimension(stringBounder);
		final double width = MathUtils.max(dim1.getWidth() + 2 * marginAd, dimBox.getWidth() + 2 * marginBox,
				dim2.getWidth() + 2 * marginAd);
		final double height = dim1.getHeight() + 2 * marginAd + dimBox.getHeight() + 2 * marginBox + dim2.getHeight()
				+ 2 * marginAd;
		return new Dimension2DDouble(width, height);
	}

	public final Network getNetwork() {
		return network;
	}

	public final DiagElement getElement() {
		return element;
	}

}
