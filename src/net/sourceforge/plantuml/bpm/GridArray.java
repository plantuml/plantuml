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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GridArray implements UDrawable {

	private final int rows;
	private final int lines;
	private final Placeable data[][];
	private final ISkinParam skinParam;
	private final List<BpmEdge> edges = new ArrayList<BpmEdge>();

	public GridArray(ISkinParam skinParam, int rows, int lines) {
		this.skinParam = skinParam;
		this.rows = rows;
		this.lines = lines;
		this.data = new Placeable[rows][lines];
	}

	@Override
	public String toString() {
		return "" + lines + "x" + rows;
	}

	public void setData(int r, int l, Placeable element) {
		data[r][l] = element;
	}

	public Placeable getData(int r, int l) {
		return data[r][l];
	}

	public final int getRows() {
		return rows;
	}

	public final int getLines() {
		return lines;
	}

	private double getHeightOfLine(StringBounder stringBounder, int line) {
		double height = 0;
		for (int i = 0; i < rows; i++) {
			final Placeable cell = data[i][line];
			if (cell == null) {
				continue;
			}
			height = Math.max(height, cell.getDimension(stringBounder, skinParam).getHeight());
		}
		return height;
	}

	private double getWidthOfRow(StringBounder stringBounder, int row) {
		double width = 0;
		for (int i = 0; i < lines; i++) {
			final Placeable cell = data[row][i];
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
		for (BpmEdge edge : edges) {
			// System.err.println("Drawing " + edge);
			final int from[] = getCoord(edge.getFrom());
			final int to[] = getCoord(edge.getTo());
			final Point2D pt1 = getCenterOf(stringBounder, from[0], from[1]);
			final Point2D pt2 = getCenterOf(stringBounder, to[0], to[1]);
			drawArrow(ug, pt1, pt2);
		}
		double dy = 0;
		for (int l = 0; l < lines; l++) {
			double dx = 0;
			final double heightOfLine = getHeightOfLine(stringBounder, l);
			for (int r = 0; r < rows; r++) {
				final double widthOfRow = getWidthOfRow(stringBounder, r);
				final Placeable cell = data[r][l];
				if (cell != null) {
					final Dimension2D dim = cell.getDimension(stringBounder, skinParam);

					cell.toTextBlock(skinParam).drawU(
							ug.apply(new UTranslate(dx + (widthOfRow - dim.getWidth()) / 2, dy
									+ (heightOfLine - dim.getHeight()) / 2)));
				}
				dx += widthOfRow + margin;
			}
			dy += heightOfLine + margin;
		}

	}

	private void drawArrow(UGraphic ug, Point2D pt1, Point2D pt2) {
		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLUE));
		final ULine line = new ULine(pt2.getX() - pt1.getX(), pt2.getY() - pt1.getY());
		ug.apply(new UTranslate(pt1)).draw(line);
	}

	private Point2D getCenterOf(StringBounder stringBounder, int r, int l) {
		double x = getWidthOfRow(stringBounder, r) / 2;
		for (int i = 0; i < r; i++) {
			final double widthOfRow = getWidthOfRow(stringBounder, i);
			x += widthOfRow + margin;
		}
		double y = getHeightOfLine(stringBounder, l) / 2;
		for (int i = 0; i < l; i++) {
			final double heightOfLine = getHeightOfLine(stringBounder, i);
			y += heightOfLine + margin;
		}
		return new Point2D.Double(x, y);
	}

	private int[] getCoord(Placeable element) {
		for (int l = 0; l < lines; l++) {
			for (int r = 0; r < rows; r++) {
				final Placeable cell = data[r][l];
				if (cell == element) {
					return new int[] { r, l };
				}
			}
		}
		throw new IllegalArgumentException();
	}

	private void printMe() {
		for (int l = 0; l < lines; l++) {
			for (int r = 0; r < rows; r++) {
				final Placeable cell = data[r][l];
				System.err.print(cell);
				System.err.print("  ;  ");
			}
			System.err.println();
		}
	}

	public void addEdges(List<BpmEdge> edges) {
		this.edges.addAll(edges);
	}

}
