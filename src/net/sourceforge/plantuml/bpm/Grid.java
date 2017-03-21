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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ISkinParam;

public class Grid {

	private final Chain<Row> rows;
	private final Chain<Line> lines;
	private final Coord root;
	private final Map<Coord, Cell> cells = new HashMap<Coord, Cell>();

	public Grid() {
		this.root = new Coord(new Row(), new Line());
		this.rows = new ChainImpl<Row>(root.getRow());
		this.lines = new ChainImpl<Line>(root.getLine());
		this.cells.put(root, new Cell());
	}

	public Cell getCell(Coord coord) {
		return getCell(coord.getRow(), coord.getLine());
	}

	public Cell getCell(Row row, Line line) {
		if (rows.contains(row) == false) {
			throw new IllegalArgumentException();
		}
		if (lines.contains(line) == false) {
			throw new IllegalArgumentException();
		}
		final Coord coord = new Coord(row, line);
		Cell result = cells.get(coord);
		if (result == null) {
			result = new Cell();
			cells.put(coord, result);
		}
		return result;
	}

	public Coord getById(String id) {
		for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
			final Cell cell = ent.getValue();
			if (cell == null || cell.getData() == null) {
				continue;
			}
			if (id.equals(cell.getData().getId())) {
				return ent.getKey();
			}
		}
		return null;
	}

	public final Coord getRoot() {
		return root;
	}

	public final Chain<Row> rows() {
		return rows;
	}

	public final Chain<Line> lines() {
		return lines;
	}

	private final Coord getCoord(Cell cell) {
		for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
			if (ent.getValue() == cell) {
				return ent.getKey();
			}
		}
		throw new IllegalArgumentException();
	}

	public final Navigator<Row> rowsOf(Coord coord) {
		return rows.navigator(coord.getRow());
	}

	public final Navigator<Line> linesOf(Coord coord) {
		return lines.navigator(coord.getLine());
	}

	public final Navigator<Row> rowsOf(Cell cell) {
		return rowsOf(getCoord(cell));
	}

	public final Navigator<Line> linesOf(Cell cell) {
		return linesOf(getCoord(cell));
	}

	public final GridArray toArray(ISkinParam skinParam) {
		final List<Row> rows = this.rows.toList();
		final List<Line> lines = this.lines.toList();
		final GridArray result = new GridArray(skinParam, rows.size(), lines.size());
		for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
			final int r = rows.indexOf(ent.getKey().getRow());
			final int l = lines.indexOf(ent.getKey().getLine());
			if (r == -1 || l == -1) {
				throw new IllegalStateException();
			}
			result.setData(r, l, ent.getValue().getData());
		}
		return result;
	}

}
