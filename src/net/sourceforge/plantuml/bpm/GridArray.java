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
package net.sourceforge.plantuml.bpm;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class GridArray implements UDrawable {

	private final int lines;
	private final int cols;
	private final Placeable data[][];
	private final ISkinParam skinParam;

	// private final List<GridEdge> edges = new ArrayList<GridEdge>();

	public GridArray(ISkinParam skinParam, int lines, int cols) {
		this.skinParam = skinParam;
		this.lines = lines;
		this.cols = cols;
		this.data = new Placeable[lines][cols];
	}

	@Override
	public String toString() {
		return "" + lines + "x" + cols;
	}

	public void setData(int l, int c, Placeable element) {
		data[l][c] = element;
	}

	public Placeable getData(int l, int c) {
		return data[l][c];
	}

	public final int getRows() {
		return cols;
	}

	public final int getLines() {
		return lines;
	}

	private double getHeightOfLine(StringBounder stringBounder, int line) {
		double height = 0;
		for (int i = 0; i < cols; i++) {
			final Placeable cell = data[line][i];
			if (cell == null) {
				continue;
			}
			height = Math.max(height, cell.getDimension(stringBounder, skinParam).getHeight());
		}
		return height;
	}

	private double getWidthOfCol(StringBounder stringBounder, int col) {
		double width = 0;
		for (int i = 0; i < lines; i++) {
			final Placeable cell = data[i][col];
			if (cell == null) {
				continue;
			}
			width = Math.max(width, cell.getDimension(stringBounder, skinParam).getWidth());
		}
		return width;
	}

	private final double margin = 30;

	public void drawU(UGraphic ug) {
		// printMe();

		final StringBounder stringBounder = ug.getStringBounder();

		// for (GridEdge edge : edges) {
		// // System.err.println("Drawing " + edge);
		// final int from[] = getCoord(edge.getFrom());
		// final int to[] = getCoord(edge.getTo());
		// final Point2D pt1 = getCenterOf(stringBounder, from[0], from[1]);
		// final Point2D pt2 = getCenterOf(stringBounder, to[0], to[1]);
		// drawArrow(ug, pt1, pt2);
		// }

		double dy = 0;
		drawInternalGrid(ug);
		for (int l = 0; l < lines; l++) {
			double dx = 0;
			final double heightOfLine = getHeightOfLine(stringBounder, l);
			for (int r = 0; r < cols; r++) {
				final double widthOfCol = getWidthOfCol(stringBounder, r);
				final Placeable cell = data[l][r];
				if (cell != null) {
					final Dimension2D dim = cell.getDimension(stringBounder, skinParam);

					cell.toTextBlock(skinParam).drawU(
							ug.apply(new UTranslate(dx + (widthOfCol + margin - dim.getWidth()) / 2, dy
											+ (heightOfLine + margin - dim.getHeight()) / 2)));
				}
				dx += widthOfCol + margin;
			}
			dy += heightOfLine + margin;
		}

	}

	private void drawInternalGrid(UGraphic ug) {
		double heightMax = 0;
		for (int l = 0; l < lines; l++) {
			heightMax += getHeightOfLine(ug.getStringBounder(), l) + margin;
		}
		double widthMax = 0;
		for (int c = 0; c < cols; c++) {
			widthMax += getWidthOfCol(ug.getStringBounder(), c) + margin;
		}
		ug = ug.apply(HColorUtils.BLACK);
		double y = 0;
		for (int l = 0; l < lines; l++) {
			ug.apply(UTranslate.dy(y)).draw(ULine.hline(widthMax));
			y += getHeightOfLine(ug.getStringBounder(), l) + margin;
		}
		double x = 0;
		for (int c = 0; c < cols; c++) {
			ug.apply(UTranslate.dx(x)).draw(ULine.vline(heightMax));
			x += getWidthOfCol(ug.getStringBounder(), c) + margin;
		}

	}

	private void drawArrow(UGraphic ug, Point2D pt1, Point2D pt2) {
		ug = ug.apply(HColorUtils.BLUE);
		final ULine line = new ULine(pt2.getX() - pt1.getX(), pt2.getY() - pt1.getY());
		ug.apply(new UTranslate(pt1)).draw(line);
	}

	private Point2D getCenterOf(StringBounder stringBounder, int c, int l) {
		double x = getWidthOfCol(stringBounder, c) / 2 + margin / 2;
		for (int i = 0; i < c; i++) {
			final double widthOfCol = getWidthOfCol(stringBounder, i);
			x += widthOfCol + margin;
		}
		double y = getHeightOfLine(stringBounder, l) / 2 + margin / 2;
		for (int i = 0; i < l; i++) {
			final double heightOfLine = getHeightOfLine(stringBounder, i);
			y += heightOfLine + margin;
		}
		return new Point2D.Double(x, y);
	}

	private int[] getCoord(Cell someCell) {
		for (int l = 0; l < lines; l++) {
			for (int c = 0; c < cols; c++) {
				final Placeable cell = data[l][c];
				if (cell == someCell.getData()) {
					return new int[] { c, l };
				}
			}
		}
		throw new IllegalArgumentException();
	}

	private void printMe() {
		for (int l = 0; l < lines; l++) {
			for (int c = 0; c < cols; c++) {
				final Placeable cell = data[l][c];
				System.err.print(cell);
				System.err.print("  ;  ");
			}
			System.err.println();
		}
	}

	// void addEdgesInternal(List<GridEdge> edges) {
	// this.edges.addAll(edges);
	// }

}
