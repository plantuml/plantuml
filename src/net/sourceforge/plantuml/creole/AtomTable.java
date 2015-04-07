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
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class AtomTable implements Atom {

	class Line {
		private final List<Atom> cells = new ArrayList<Atom>();

		public void add(Atom cell) {
			cells.add(cell);
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
	private final HtmlColor lineColor;

	public AtomTable(HtmlColor lineColor) {
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
		for (Line line : lines) {
			for (Atom cell : line.cells) {
				final Position pos = positions.get(cell);
				cell.drawU(ug.apply(pos.getTranslate()));
			}
		}
		ug = ug.apply(new UChangeColor(lineColor));
		final ULine hline = new ULine(getEndingX(getNbCols() - 1), 0);
		for (int i = 0; i <= getNbLines(); i++) {
			ug.apply(new UTranslate(0, getStartingY(i))).draw(hline);
		}
		final ULine vline = new ULine(0, getEndingY(getNbLines() - 1));
		for (int i = 0; i <= getNbCols(); i++) {
			ug.apply(new UTranslate(getStartingX(i), 0)).draw(vline);
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
			final double width = getPosition(i, col).getWidth();
			result = Math.max(result, width);
		}
		return result;
	}

	private double getLineHeight(int line) {
		double result = 0;
		for (int i = 0; i < getNbCols(); i++) {
			final double height = getPosition(line, i).getHeight();
			result = Math.max(result, height);
		}
		return result;
	}

	private Position getPosition(int line, int col) {
		final Line l = lines.get(line);
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

	public void addCell(Atom cell) {
		lastLine().add(cell);
		positions.clear();
	}

	public void newLine() {
		lines.add(new Line());
	}

}
