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
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class SheetBlock1 extends AbstractTextBlock implements TextBlock, Atom, Stencil {

	public List<Atom> splitInTwo(StringBounder stringBounder, double width) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	private final Sheet sheet;
	private List<Stripe> stripes;
	private Map<Stripe, Double> heights;
	private Map<Stripe, Double> widths;
	private Map<Atom, Position> positions;
	private MinMax minMax;
	private final LineBreakStrategy maxWidth;
	private final double padding;

	public SheetBlock1(Sheet sheet, LineBreakStrategy maxWidth, double padding) {
		this.sheet = sheet;
		this.maxWidth = maxWidth;
		this.padding = padding;
		if (maxWidth == null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		return sheet.toString();
	}

	public HorizontalAlignment getCellAlignment() {
		if (stripes.size() != 1) {
			return HorizontalAlignment.LEFT;
		}
		final Stripe simple = stripes.get(0);
		if (simple instanceof StripeSimple) {
			return ((StripeSimple) simple).getCellAlignment();
		}
		return HorizontalAlignment.LEFT;
	}

	private void initMap(StringBounder stringBounder) {
		if (positions != null) {
			return;
		}
		stripes = new ArrayList<Stripe>();
		for (Stripe stripe : sheet) {
			stripes.addAll(new Fission(stripe, maxWidth).getSplitted(stringBounder));
		}
		positions = new LinkedHashMap<Atom, Position>();
		widths = new LinkedHashMap<Stripe, Double>();
		heights = new LinkedHashMap<Stripe, Double>();
		minMax = MinMax.getEmpty(true);
		double y = 0;
		for (Stripe stripe : stripes) {
			if (stripe.getAtoms().size() == 0) {
				continue;
			}
			final Sea sea = new Sea(stringBounder);
			for (Atom atom : stripe.getAtoms()) {
				sea.add(atom);
			}
			sea.doAlign();
			sea.translateMinYto(y);
			sea.exportAllPositions(positions);
			final double width = sea.getWidth();
			widths.put(stripe, width);
			minMax = sea.update(minMax);
			final double height = sea.getHeight();
			heights.put(stripe, height);
			y += height;
		}
		final int coef;
		if (sheet.getHorizontalAlignment() == HorizontalAlignment.CENTER) {
			coef = 2;
		} else if (sheet.getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
			coef = 1;
		} else {
			coef = 0;
		}
		if (coef != 0) {
			double maxWidth = 0;
			for (Double v : widths.values()) {
				if (v > maxWidth) {
					maxWidth = v;
				}
			}
			for (Map.Entry<Stripe, Double> ent : widths.entrySet()) {
				final double diff = maxWidth - ent.getValue();
				if (diff > 0) {
					for (Atom atom : ent.getKey().getAtoms()) {
						final Position pos = positions.get(atom);
						positions.put(atom, pos.translateX(diff / coef));
					}
				}

			}

		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		initMap(stringBounder);
		return Dimension2DDouble.delta(minMax.getDimension(), 2 * padding);
	}

	@Override
	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return null;
	}

	public void drawU(UGraphic ug) {
		initMap(ug.getStringBounder());
		if (padding > 0) {
			ug = ug.apply(new UTranslate(padding, padding));
		}
		for (Stripe stripe : stripes) {
			for (Atom atom : stripe.getAtoms()) {
				final Position position = positions.get(atom);
				atom.drawU(position.translate(ug));
				// position.drawDebug(ug);
			}
		}
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return 0;
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return calculateDimension(stringBounder).getWidth();
	}

}
