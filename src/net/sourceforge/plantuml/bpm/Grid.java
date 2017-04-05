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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;

public class Grid {

	private final Chain<Line> lines;
	private final Chain<Col> cols;
	private final Coord root;
	private final Map<Coord, Cell> cells = new HashMap<Coord, Cell>();

	public Grid() {
		this.root = new Coord(new Line(), new Col());
		this.lines = new ChainImpl<Line>(root.getLine());
		this.cols = new ChainImpl<Col>(root.getCol());
		this.cells.put(root, new Cell());
	}

	public Cell getCell(Coord coord) {
		return getCell(coord.getLine(), coord.getCol());
	}

	public Cell getCell(Line line, Col col) {
		if (lines.contains(line) == false) {
			throw new IllegalArgumentException();
		}
		if (cols.contains(col) == false) {
			throw new IllegalArgumentException();
		}
		final Coord coord = new Coord(line, col);
		Cell result = cells.get(coord);
		if (result == null) {
			result = new Cell();
			cells.put(coord, result);
		}
		return result;
	}

	// private Set<GridEdge> edgeWith(Cell someCell) {
	// // final Set<GridEdge> result = new HashSet<GridEdge>();
	// // for (GridEdge edge : edges) {
	// // if (edge.match(someCell)) {
	// // result.add(edge);
	// // }
	// // }
	// // return Collections.unmodifiableSet(result);
	// throw new UnsupportedOperationException();
	//
	// }
	//
	// private Collection<Cell> getCellsConnectedTo(Cell someCell) {
	// final Set<Cell> result = new HashSet<Cell>();
	// final Set<GridEdge> myEdges = edgeWith(someCell);
	// for (Cell cell : cells.values()) {
	// for (GridEdge edge : myEdges) {
	// assert edge.match(someCell);
	// if (edge.match(cell)) {
	// result.add(cell);
	// }
	// }
	// }
	// return Collections.unmodifiableSet(result);
	// }
	//
	// private SortedSet<Col> getColsConnectedTo(Cell someCell) {
	// final SortedSet<Col> result = new TreeSet<Col>(cols);
	// final Set<GridEdge> myEdges = edgeWith(someCell);
	// for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
	// for (GridEdge edge : myEdges) {
	// assert edge.match(someCell);
	// if (edge.match(ent.getValue())) {
	// result.add(ent.getKey().getCol());
	// }
	// }
	// }
	// return Collections.unmodifiableSortedSet(result);
	// }

	// public SortedSet<Col> colsConnectedTo(Line line) {
	// final SortedSet<Col> result = new TreeSet<Col>(cols);
	// for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
	// final Cell cell = ent.getValue();
	// if (cell == null || cell.getData() == null) {
	// continue;
	// }
	// if (ent.getKey().getLine() != line) {
	// continue;
	// }
	// result.addAll(getColsConnectedTo(ent.getValue()));
	//
	// }
	// return Collections.unmodifiableSortedSet(result);
	// }

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

	public final Chain<Line> lines() {
		return lines;
	}

	public final Chain<Col> cols() {
		return cols;
	}

	public final Coord getCoord(Cell cell) {
		for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
			if (ent.getValue() == cell) {
				return ent.getKey();
			}
		}
		throw new IllegalArgumentException();
	}

	public final Navigator<Line> linesOf(Coord coord) {
		return lines.navigator(coord.getLine());
	}

	public final Navigator<Col> colsOf(Coord coord) {
		return cols.navigator(coord.getCol());
	}

	public final Navigator<Line> linesOf(Cell cell) {
		return linesOf(getCoord(cell));
	}

	public final Navigator<Col> colsOf(Cell cell) {
		return colsOf(getCoord(cell));
	}

	public final GridArray toArray(ISkinParam skinParam) {
		final List<Line> lines = this.lines.toList();
		final List<Col> cols = this.cols.toList();
		final GridArray result = new GridArray(skinParam, lines.size(), cols.size());
		for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
			final int l = lines.indexOf(ent.getKey().getLine());
			final int r = cols.indexOf(ent.getKey().getCol());
			if (r == -1 || l == -1) {
				throw new IllegalStateException();
			}
			result.setData(l, r, ent.getValue().getData());
		}
		return result;
	}

	// public boolean isRowEmpty(Col row) {
	// System.err.println("Testing Row " + row);
	// for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
	// final Cell cell = ent.getValue();
	// if (cell == null || cell.getData() == null) {
	// continue;
	// }
	// if (ent.getKey().getCol() == row) {
	// System.err.println("Not empty " + cell + " " + cell.getData());
	// return false;
	// }
	// }
	// System.err.println("EMPTY!!!");
	// return true;
	// }

	// public boolean isLineEmpty(Line line) {
	// for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
	// final Cell cell = ent.getValue();
	// if (cell == null || cell.getData() == null) {
	// continue;
	// }
	// if (ent.getKey().getLine() == line) {
	// return false;
	// }
	// }
	// return true;
	// }

	public Set<Col> usedColsOf(Line line) {
		final Set<Col> result = new HashSet<Col>();
		for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
			final Cell cell = ent.getValue();
			if (cell == null || cell.getData() == null) {
				continue;
			}
			if (ent.getKey().getLine() == line) {
				result.add(ent.getKey().getCol());
			}
		}
		return Collections.unmodifiableSet(result);
	}

	public void removeLine(Line line) {
		assert usedColsOf(line).isEmpty();
		final boolean done = lines.remove(line);
		if (done == false) {
			throw new IllegalArgumentException();
		}
	}

	// public void addEdge(Collection<GridEdge> other) {
	// this.edges.addAll(other);
	// }

	public void mergeLines(Line source, Line dest) {
		final Map<Coord, Cell> supp = new HashMap<Coord, Cell>();

		for (Iterator<Map.Entry<Coord, Cell>> it = cells.entrySet().iterator(); it.hasNext();) {
			final Map.Entry<Coord, Cell> ent = it.next();
			final Cell cell = ent.getValue();
			if (cell == null || cell.getData() == null) {
				continue;
			}
			if (ent.getKey().getLine() == source) {
				supp.put(new Coord(dest, ent.getKey().getCol()), cell);
				it.remove();
			}
		}
		cells.putAll(supp);
		removeLine(source);
	}

	public void addConnections() {
		for (Map.Entry<Coord, Cell> ent : new HashMap<Coord, Cell>(cells).entrySet()) {
			final List<Cell> dests = ent.getValue().getDestinations();
			final Coord src = ent.getKey();
			for (int i = 0; i < dests.size(); i++) {
				final Coord dest = getCoord(dests.get(i));
				final boolean startHorizontal = i == 0;
				if (startHorizontal) {
					System.err.println("DrawingHorizontal " + ent.getValue() + " --> " + dests.get(i) + " " + i);
					drawHorizontal(src, dest);
				} else {
					// drawVertical(src, dest);
				}
			}
		}
	}

	private void drawVertical(final Coord src, final Coord dest) {
		for (Navigator<Line> itLine = Navigators.iterate(lines, src.getLine(), dest.getLine()); itLine.get() != dest
				.getLine();) {
			final Line cur = itLine.next();
			if (cur != dest.getLine()) {
				Cell tmp = getCell(cur, src.getCol());
				addPuzzle(tmp, "NS");
			}
		}
		for (Navigator<Col> itCol = Navigators.iterate(cols, src.getCol(), dest.getCol()); itCol.get() != dest.getCol();) {
			final Col cur = itCol.next();
			if (cur != dest.getCol()) {
				Cell tmp = getCell(dest.getLine(), cur);
				addPuzzle(tmp, "EW");
			}
		}

	}

	private void drawHorizontal(final Coord src, final Coord dest) {
		for (Navigator<Col> itCol = Navigators.iterate(cols, src.getCol(), dest.getCol()); itCol.get() != dest.getCol();) {
			final Col cur = itCol.next();
			if (cur != dest.getCol()) {
				Cell tmp = getCell(src.getLine(), cur);
				addPuzzle(tmp, "EW");
			}
		}
		System.err.println("src=" + src + " " + getCell(src));
		System.err.println("dest=" + dest + " " + getCell(dest));
		for (Navigator<Line> itLine = Navigators.iterate(lines, src.getLine(), dest.getLine()); itLine.get() != dest
				.getLine();) {
			final Line cur = itLine.next();
			if (cur != dest.getLine()) {
				Cell tmp = getCell(cur, src.getCol());
				addPuzzle(tmp, "NS");
			}
		}
	}

	private void addPuzzle(Cell tmp, String direction) {
		ConnectorPuzzle after = ConnectorPuzzle.get(direction);
		final ConnectorPuzzle before = (ConnectorPuzzle) tmp.getData();
		if (before != null) {
			after = after.append(before);
		}
		tmp.setData(after);
	}

}
