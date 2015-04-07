/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3835 $
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
import net.sourceforge.plantuml.graphic.HtmlColorSet;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

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
		final Element elmt = new ElementText(Arrays.asList(s.trim()), font, spriteContainer);
		entries.add(new ElementTreeEntry(level, elmt));
	}

	public void addCellToEntry(String s) {
		final int size = entries.size();
		if (size > 0) {
			final Element elmt = new ElementText(Arrays.asList(s.trim()), font, spriteContainer);
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
		ug = ug.apply(new UChangeColor(HtmlColorSet.getInstance().getColorIfValid("#888888")));
		skeleton.draw(ug, 0, 0);
		if (strategy != TableStrategy.DRAW_NONE) {
			final Grid2 grid = new Grid2(rows, cols, strategy);
			grid.drawU(ug);
		}
	}

	static class Skeleton {

		private final List<Entry> entries = new ArrayList<Entry>();

		static class Entry {
			private final double xpos;
			private final double ypos;

			public Entry(double x, double y) {
				this.xpos = x;
				this.ypos = y;
			}

			public void drawRectangle(UGraphic ug) {
				ug.apply(new UTranslate(xpos, ypos)).draw(new URectangle(2, 2));
			}
		}

		public void add(double x, double y) {
			entries.add(new Entry(x, y));
		}

		public void draw(UGraphic ug, double x, double y) {
			for (int i = 0; i < entries.size(); i++) {
				final Entry en = entries.get(i);
				if (i + 1 < entries.size() && entries.get(i + 1).xpos > en.xpos) {
					en.drawRectangle(ug);
				}
				Entry parent = null;
				for (int j = 0; j < i; j++) {
					final Entry en0 = entries.get(j);
					if (en0.xpos < en.xpos) {
						parent = en0;
					}
				}
				if (parent != null) {
					drawChild(ug, parent, en);
				}
			}
		}

		private void drawChild(UGraphic ug, Entry parent, Entry child) {
			final double dy = child.ypos - parent.ypos - 2;
			ug.apply(new UTranslate(parent.xpos + 1, parent.ypos + 3)).draw(new ULine(0, dy));

			final double dx = child.xpos - parent.xpos - 2;
			ug.apply(new UTranslate(parent.xpos + 1, child.ypos + 1)).draw(new ULine(dx, 0));

		}

	}

}
