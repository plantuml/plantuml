/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.bpm;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ConnectorPuzzle implements Placeable, TextBlock {

	public static enum Where {
		NORTH(1), EAST(2), SOUTH(4), WEST(8);

		private int coding;

		private Where(int coding) {
			this.coding = coding;
		}

		String toShortString() {
			return name().substring(0, 1);
		}
	}

	private static final ConnectorPuzzle all[] = new ConnectorPuzzle[16];
	private final int type;

	static {
		for (int i = 0; i < all.length; i++) {
			all[i] = new ConnectorPuzzle(i);
		}
	}

	public ConnectorPuzzle append(ConnectorPuzzle before) {
		return all[this.type | before.type];
	}

	public static ConnectorPuzzle get(String value) {
		int num = 0;
		for (Where w : Where.values()) {
			if (value.contains(w.toShortString())) {
				num += w.coding;
			}
		}
		return all[num];
	}

	public boolean have(Where where) {
		return (type & where.coding) != 0;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (Where w : Where.values()) {
			if (have(w)) {
				sb.append(w.toShortString());
			}
		}
		if (sb.length() == 0) {
			sb.append("NONE");
		}

		return sb.toString();
	}

	private ConnectorPuzzle(int type) {
		this.type = type;
	}

	public Dimension2D getDimension(StringBounder stringBounder, ISkinParam skinParam) {
		return new Dimension2DDouble(20, 20);
	}

	public TextBlock toTextBlock(ISkinParam skinParam) {
		return this;
	}

	public String getId() {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug) {
		// System.err.println("DRAWING " + toString());
		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLUE));
		for (Where w : Where.values()) {
			if (have(w)) {
				drawLine(ug, w);
			}
		}

	}

	private void drawLine(UGraphic ug, Where w) {
		if (w == Where.WEST) {
			ug.apply(new UTranslate(0, 10)).draw(new ULine(10, 0));
		}
		if (w == Where.EAST) {
			ug.apply(new UTranslate(10, 10)).draw(new ULine(10, 0));
		}
		if (w == Where.NORTH) {
			ug.apply(new UTranslate(10, 0)).draw(new ULine(0, 10));
		}
		if (w == Where.SOUTH) {
			ug.apply(new UTranslate(10, 10)).draw(new ULine(0, 10));
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(20, 20);
	}

	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return null;
	}

}
