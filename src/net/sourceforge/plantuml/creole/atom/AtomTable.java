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
package net.sourceforge.plantuml.creole.atom;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.creole.Position;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class AtomTable extends AbstractAtom implements Atom {

	class Line {
		private final List<Atom> cells = new ArrayList<Atom>();
		private final List<HColor> cellsBackColor = new ArrayList<HColor>();
		private final HColor lineBackColor;

		private Line(HColor lineBackColor) {
			this.lineBackColor = lineBackColor;
		}

		public void add(Atom cell, HColor cellBackColor) {
			cells.add(cell);
			cellsBackColor.add(cellBackColor);
		}

		public int size() {
			return cells.size();
		}

		@Override
		public String toString() {
			return super.toString() + " " + cells.size();
		}
	}

	private final List<Line> lines = new ArrayList<Line>();
	private final Map<Atom, Position> positions = new HashMap<Atom, Position>();
	private final HColor lineColor;

	public AtomTable(HColor lineColor) {
		this.lineColor = lineColor;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		initMap(stringBounder);
		final double width = getEndingX(getNbCols() - 1);
		final double height = getEndingY(getNbLines() - 1);
		return new Dimension2DDouble(width, height);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public void drawU(UGraphic ug) {
		initMap(ug.getStringBounder());
		for (int i = 0; i < getNbLines(); i++) {
			final Line line = lines.get(i);
			if (line.lineBackColor != null) {
				final double y1 = getStartingY(i);
				final double y2 = getStartingY(i + 1);
				final double x1 = getStartingX(0);
				final double x2 = getStartingX(getNbCols());
				ug.apply(new HColorNone()).apply(line.lineBackColor.bg())
						.apply(new UTranslate(x1, y1)).draw(new URectangle(x2 - x1, y2 - y1));
			}
			for (int j = 0; j < getNbCols(); j++) {
				if (j >= line.cells.size()) {
					continue;
				}
				final Atom cell = line.cells.get(j);
				HorizontalAlignment align = HorizontalAlignment.LEFT;
				if (cell instanceof SheetBlock1) {
					align = ((SheetBlock1) cell).getCellAlignment();
				}
				final HColor cellBackColor = line.cellsBackColor.get(j);
				final double x1 = getStartingX(j);
				final double x2 = getStartingX(j + 1);
				final double cellWidth = x2 - x1;
				if (cellBackColor != null) {
					final double y1 = getStartingY(i);
					final double y2 = getStartingY(i + 1);
					ug.apply(new HColorNone()).apply(cellBackColor.bg())
							.apply(new UTranslate(x1, y1)).draw(new URectangle(x2 - x1, y2 - y1));
				}
				final Position pos = positions.get(cell);
				final Dimension2D dimCell = cell.calculateDimension(ug.getStringBounder());
				final double dx;
				if (align == HorizontalAlignment.RIGHT) {
					dx = cellWidth - dimCell.getWidth();
				} else {
					dx = 0;
				}
				cell.drawU(ug.apply(pos.getTranslate().compose(UTranslate.dx(dx))));
			}
		}
		ug = ug.apply(lineColor);
		final ULine hline = ULine.hline(getEndingX(getNbCols() - 1));
		for (int i = 0; i <= getNbLines(); i++) {
			ug.apply(UTranslate.dy(getStartingY(i))).draw(hline);
		}
		final ULine vline = ULine.vline(getEndingY(getNbLines() - 1));
		for (int i = 0; i <= getNbCols(); i++) {
			ug.apply(UTranslate.dx(getStartingX(i))).draw(vline);
		}

	}

	private void initMap(StringBounder stringBounder) {
		if (positions.size() > 0) {
			return;
		}
		for (Line line : lines) {
			for (Atom cell : line.cells) {
				final Dimension2D dim = cell.calculateDimension(stringBounder);
				final Position pos = new Position(0, 0, dim);
				positions.put(cell, pos);
			}
		}
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(i).size(); j++) {
				final Atom cell = lines.get(i).cells.get(j);
				final Dimension2D dim = cell.calculateDimension(stringBounder);
				final double x = getStartingX(j);
				final double y = getStartingY(i);
				final Position pos = new Position(x, y, dim);
				positions.put(cell, pos);
			}
		}
	}

	private double getStartingX(int col) {
		double result = 0;
		for (int i = 0; i < col; i++) {
			result += getColWidth(i);
		}
		return result;
	}

	private double getEndingX(int col) {
		double result = 0;
		for (int i = 0; i <= col; i++) {
			result += getColWidth(i);
		}
		return result;
	}

	private double getStartingY(int line) {
		double result = 0;
		for (int i = 0; i < line; i++) {
			result += getLineHeight(i);
		}
		return result;
	}

	private double getEndingY(int line) {
		double result = 0;
		for (int i = 0; i <= line; i++) {
			result += getLineHeight(i);
		}
		return result;
	}

	private double getColWidth(int col) {
		double result = 0;
		for (int i = 0; i < getNbLines(); i++) {
			final Position position = getPosition(i, col);
			if (position == null) {
				continue;
			}
			final double width = position.getWidth();
			result = Math.max(result, width);
		}
		return result;
	}

	private double getLineHeight(int line) {
		double result = 0;
		for (int i = 0; i < getNbCols(); i++) {
			final Position position = getPosition(line, i);
			if (position == null) {
				continue;
			}
			final double height = position.getHeight();
			result = Math.max(result, height);
		}
		return result;
	}

	private Position getPosition(int line, int col) {
		if (line >= lines.size()) {
			return null;
		}
		final Line l = lines.get(line);
		if (col >= l.cells.size()) {
			return null;
		}
		final Atom atom = l.cells.get(col);
		return positions.get(atom);
	}

	private int getNbCols() {
		return lines.get(0).size();
	}

	private int getNbLines() {
		return lines.size();
	}

	private Line lastLine() {
		return lines.get(lines.size() - 1);
	}

	public void addCell(Atom cell, HColor cellBackColor) {
		lastLine().add(cell, cellBackColor);
		positions.clear();
	}

	public void newLine(HColor lineBackColor) {
		lines.add(new Line(lineBackColor));
	}
}
