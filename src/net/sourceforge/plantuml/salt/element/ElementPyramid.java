/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.salt.Position;
import net.sourceforge.plantuml.salt.Positionner;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementPyramid implements Element {

	private final Element elements[][];
	private final int rows;
	private final int cols;
	private final TableStrategy tableStrategy;

	public ElementPyramid(Positionner positionner, TableStrategy tableStrategy) {
		this.rows = positionner.getNbRows();
		this.cols = positionner.getNbCols();
		this.tableStrategy = tableStrategy;
		if (rows == 0) {
			throw new IllegalArgumentException("rows=0");
		}
		if (cols == 0) {
			throw new IllegalArgumentException("cols=0");
		}
		this.elements = new Element[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				this.getElements()[r][c] = new ElementEmpty();
			}
		}

		for (Map.Entry<Element, Position> ent : positionner.getAll().entrySet()) {
			final int r = ent.getValue().getRow();
			final int c = ent.getValue().getCol();
			this.getElements()[r][c] = ent.getKey();
		}
	}

	private double getRowHeight(StringBounder stringBounder, int row) {
		double max = 0;
		for (int c = 0; c < cols; c++) {
			final Dimension2D dim = elements[row][c].getPreferredDimension(stringBounder, 0, 0);
			if (dim.getHeight() > max) {
				max = dim.getHeight();
			}
		}
		return max;
	}

	private double getColWidth(StringBounder stringBounder, int col) {
		double max = 0;
		for (int r = 0; r < rows; r++) {
			final Dimension2D dim = elements[r][col].getPreferredDimension(stringBounder, 0, 0);
			if (dim.getWidth() > max) {
				max = dim.getWidth();
			}
		}
		return max;
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		double width = 1;
		for (int c = 0; c < cols; c++) {
			width += getColWidth(stringBounder, c) + 3;
		}
		double height = 1;
		for (int r = 0; r < rows; r++) {
			height += getRowHeight(stringBounder, r) + 3;
		}
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug, final double x, final double y, int zIndex, Dimension2D dimToUse) {
		double ytmp = y + 2;
		final Dimension2D preferred = getPreferredDimension(ug.getStringBounder(), 0, 0);
		if (tableStrategy == TableStrategy.DRAW_OUTSIDE || tableStrategy == TableStrategy.DRAW_ALL) {
			ug.apply(new UTranslate(x, y)).draw(new URectangle(preferred.getWidth() - 1, preferred.getHeight() - 1));
		}
		for (int r = 0; r < rows; r++) {
			double xtmp = x + 2;
			final double rowHeight = getRowHeight(ug.getStringBounder(), r);
			for (int c = 0; c < cols; c++) {
				final double colWidth = getColWidth(ug.getStringBounder(), c);
				this.elements[r][c].drawU(ug, xtmp, ytmp, zIndex, new Dimension2DDouble(colWidth, rowHeight));
				if (tableStrategy == TableStrategy.DRAW_ALL || tableStrategy == TableStrategy.DRAW_VERTICAL) {
					ug.apply(new UTranslate(xtmp - 2, y)).draw(new ULine(0, preferred.getHeight() - 1));
				}
				xtmp += colWidth + 3;
			}
			if (tableStrategy == TableStrategy.DRAW_VERTICAL) {
				ug.apply(new UTranslate(xtmp - 2, y)).draw(new ULine(0, preferred.getHeight() - 1));
			}
			if (tableStrategy == TableStrategy.DRAW_ALL || tableStrategy == TableStrategy.DRAW_HORIZONTAL) {
				ug.apply(new UTranslate(x, ytmp - 2)).draw(new ULine(preferred.getWidth() - 1, 0));
			}
			ytmp += rowHeight + 3;
		}
		if (tableStrategy == TableStrategy.DRAW_HORIZONTAL) {
			ug.apply(new UTranslate(x, ytmp - 2)).draw(new ULine(preferred.getWidth() - 1, 0));
		}
	}

	protected final Element[][] getElements() {
		return elements;
	}

//	public final int getRows() {
//		return rows;
//	}
//
//	public final int getCols() {
//		return cols;
//	}

}
