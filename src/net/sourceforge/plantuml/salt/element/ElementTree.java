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
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class ElementTree extends AbstractElement {

	private final List<ElementTreeEntry> entries = new ArrayList<ElementTreeEntry>();
	private final UFont font;
	private final ISkinSimple spriteContainer;
	private final double margin = 10;
	private final TableStrategy strategy;

	public ElementTree(UFont font, ISkinSimple spriteContainer, TableStrategy strategy) {
		this.font = font;
		this.spriteContainer = spriteContainer;
		this.strategy = strategy;
	}

	public void addEntry(String s) {
		int level = 0;
		while (s.startsWith("+")) {
			level++;
			s = s.substring(1);
		}
		final Element elmt = new ElementText(Arrays.asList(StringUtils.trin(s)), font, spriteContainer);
		entries.add(new ElementTreeEntry(level, elmt));
	}

	public void addCellToEntry(String s) {
		final int size = entries.size();
		if (size > 0) {
			final Element elmt = new ElementText(Arrays.asList(StringUtils.trin(s)), font, spriteContainer);
			entries.get(size - 1).addCell(elmt);
		}
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		double w1 = 0;
		double h = 0;
		for (ElementTreeEntry entry : entries) {
			final Dimension2D dim1 = entry.getPreferredDimensionFirstCell(stringBounder);
			w1 = Math.max(w1, dim1.getWidth());
			h += dim1.getHeight();
		}
		double w2 = getWidthOther(stringBounder).getTotalWidthWithMargin(margin);
		if (w2 > 0) {
			w2 += margin;
		}
		return new Dimension2DDouble(w1 + w2 + 2, h);
	}

	private ListWidth getWidthOther(StringBounder stringBounder) {
		ListWidth merge = new ListWidth();
		for (ElementTreeEntry entry : entries) {
			final ListWidth dim2 = entry.getPreferredDimensionOtherCell(stringBounder);
			merge = merge.mergeMax(dim2);
		}
		return merge;
	}

	private double getWidth1(StringBounder stringBounder) {
		double w1 = 0;
		for (ElementTreeEntry entry : entries) {
			final Dimension2D dim1 = entry.getPreferredDimensionFirstCell(stringBounder);
			w1 = Math.max(w1, dim1.getWidth());
		}
		return w1;
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}

		final StringBounder stringBounder = ug.getStringBounder();
		final double w1 = getWidth1(stringBounder);
		final ListWidth otherWidth = getWidthOther(stringBounder);
		final Skeleton skeleton = new Skeleton();
		double yvar = 0;
		final List<Double> rows = new ArrayList<Double>();
		final List<Double> cols = new ArrayList<Double>();
		rows.add(yvar);
		double xvar = 0;
		cols.add(xvar);
		xvar += w1 + margin / 2;
		cols.add(xvar);
		for (final Iterator<Double> it = otherWidth.iterator(); it.hasNext();) {
			xvar += it.next() + margin;
			cols.add(xvar);
		}

		for (ElementTreeEntry entry : entries) {
			entry.drawFirstCell(ug, 0, yvar);
			entry.drawSecondCell(ug, w1 + margin, yvar, otherWidth, margin);
			final double h = entry.getPreferredDimensionFirstCell(stringBounder).getHeight();
			skeleton.add(entry.getXDelta() - 7, yvar + h / 2 - 1);
			yvar += h;
			rows.add(yvar);
		}
		ug = ug.apply(HColorSet.instance().getColorIfValid("#888888"));
		skeleton.draw(ug, 0, 0);
		if (strategy != TableStrategy.DRAW_NONE) {
			final Grid2 grid = new Grid2(rows, cols, strategy);
			grid.drawU(ug);
		}
	}

}
