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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.bpm.ConnectorPuzzle.Where;

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

	private Grid(Grid other) {
		this.lines = ((ChainImpl<Line>) other.lines).cloneMe();
		this.cols = ((ChainImpl<Col>) other.cols).cloneMe();
		this.root = other.root;
		this.cells.putAll(other.cells);
	}

	public Grid cloneMe() {
		return new Grid(this);
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

	private Coord getCoord(Placeable placeable) {
		for (Map.Entry<Coord, Cell> ent : cells.entrySet()) {
			if (ent.getValue().getData() == placeable) {
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
			final int c = cols.indexOf(ent.getKey().getCol());
			if (c == -1) {
				throw new IllegalStateException("col=" + ent.getKey().getCol());
			}
			if (l == -1) {
				throw new IllegalStateException("line=" + ent.getKey().getLine());
			}
			result.setData(l, c, ent.getValue().getData());
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
		for (final Iterator<Map.Entry<Coord, Cell>> it = cells.entrySet().iterator(); it.hasNext();) {
			final Map.Entry<Coord, Cell> ent = it.next();
			if (ent.getKey().getLine() != line) {
				continue;
			}
			final Cell cell = ent.getValue();
			if (cell == null || cell.getData() == null) {
				it.remove();
			} else {
				throw new IllegalStateException();
			}
		}

		final boolean done = lines.remove(line);
		if (done == false) {
			throw new IllegalArgumentException();
		}
	}

	// public void addEdge(Collection<GridEdge> other) {
	// this.edges.addAll(other);
	// }

	// public void mergeLines(Line line1, Line line2) {
	// final Map<Coord, Cell> supp = new HashMap<Coord, Cell>();
	//
	// for (Iterator<Map.Entry<Coord, Cell>> it = cells.entrySet().iterator(); it.hasNext();) {
	// final Map.Entry<Coord, Cell> ent = it.next();
	// final Cell cell = ent.getValue();
	// if (cell == null || cell.getData() == null) {
	// continue;
	// }
	// if (ent.getKey().getLine() == source) {
	// supp.put(new Coord(dest, ent.getKey().getCol()), cell);
	// it.remove();
	// }
	// }
	// cells.putAll(supp);
	// removeLine(source);
	// }

	public void addConnections() {
		for (Map.Entry<Coord, Cell> ent : new HashMap<Coord, Cell>(cells).entrySet()) {
			final List<Placeable> dests2 = ent.getValue().getDestinations2();
			final Coord src = ent.getKey();
			for (int i = 0; i < dests2.size(); i++) {
				final Coord dest = getCoord(dests2.get(i));
				final boolean startHorizontal = i == 0;
				if (startHorizontal) {
					// System.err.println("DrawingHorizontal " + ent.getValue() + " --> " + dests.get(i) + " " + i);
					drawStartHorizontal(src, dest);
				} else {
					drawStartVertical(src, dest);
				}
			}
		}
	}

	private void drawStartVertical(final Coord src, final Coord dest) {
		if (src.getLine() == dest.getLine() && src.getCol() == dest.getCol()) {
			throw new IllegalStateException();
		}
		final BpmElement start = (BpmElement) getCell(src).getData();
		final int compare = lines.compare(src.getLine(), dest.getLine());
		if (compare == 0) {
			throw new IllegalStateException();
		}
		start.append(compare < 0 ? Where.SOUTH : Where.NORTH);

		for (Navigator<Line> itLine = Navigators.iterate(lines, src.getLine(), dest.getLine()); itLine.get() != dest
				.getLine();) {
			final Line cur = itLine.next();
			if (cur != dest.getLine()) {
				addPuzzle(cur, src.getCol(), "NS");
			}
		}
		for (Navigator<Col> itCol = Navigators.iterate(cols, src.getCol(), dest.getCol()); itCol.get() != dest.getCol();) {
			final Col cur = itCol.next();
			if (cur != dest.getCol()) {
				addPuzzle(dest.getLine(), cur, "EW");
			}
		}
		final BpmElement end = (BpmElement) getCell(dest).getData();
		if (src.getLine() == dest.getLine()) {
			end.append(compare < 0 ? Where.NORTH : Where.SOUTH);
		}
		if (src.getLine() != dest.getLine() && src.getCol() != dest.getCol()) {
			if (lines.compare(dest.getLine(), src.getLine()) > 0) {
				addPuzzle(dest.getLine(), src.getCol(), "N");
			} else {
				addPuzzle(dest.getLine(), src.getCol(), "S");
			}
			if (cols.compare(dest.getCol(), src.getCol()) > 0) {
				addPuzzle(dest.getLine(), src.getCol(), "E");
			} else {
				addPuzzle(dest.getLine(), src.getCol(), "W");
			}
			end.append(cols.compare(src.getCol(), dest.getCol()) > 0 ? Where.EAST : Where.WEST);
		}

	}

	private void drawStartHorizontal(final Coord src, final Coord dest) {
		if (src.getLine() == dest.getLine() && src.getCol() == dest.getCol()) {
			throw new IllegalStateException();
		}
		final BpmElement start = (BpmElement) getCell(src).getData();
		final int compare = cols.compare(src.getCol(), dest.getCol());
		if (compare == 0) {
			throw new IllegalStateException();
		}
		start.append(compare < 0 ? Where.EAST : Where.WEST);

		for (Navigator<Col> itCol = Navigators.iterate(cols, src.getCol(), dest.getCol()); itCol.get() != dest.getCol();) {
			final Col cur = itCol.next();
			if (cur != dest.getCol()) {
				addPuzzle(src.getLine(), cur, "EW");
			}
		}
		for (Navigator<Line> itLine = Navigators.iterate(lines, src.getLine(), dest.getLine()); itLine.get() != dest
				.getLine();) {
			final Line cur = itLine.next();
			if (cur != dest.getLine()) {
				addPuzzle(cur, dest.getCol(), "NS");
			}
		}
		final BpmElement end = (BpmElement) getCell(dest).getData();
		if (src.getLine() == dest.getLine()) {
			end.append(compare < 0 ? Where.WEST : Where.EAST);
		}
		if (src.getLine() != dest.getLine() && src.getCol() != dest.getCol()) {
			if (cols.compare(dest.getCol(), src.getCol()) > 0) {
				addPuzzle(src.getLine(), dest.getCol(), "W");
			} else {
				addPuzzle(src.getLine(), dest.getCol(), "E");
			}
			if (lines.compare(dest.getLine(), src.getLine()) > 0) {
				addPuzzle(src.getLine(), dest.getCol(), "S");
			} else {
				addPuzzle(src.getLine(), dest.getCol(), "N");
			}
			end.append(lines.compare(src.getLine(), dest.getLine()) > 0 ? Where.SOUTH : Where.NORTH);
		}
	}

	private void addPuzzle(Line line, Col col, String direction) {
		final Cell cell = getCell(line, col);
		ConnectorPuzzleEmpty connector = (ConnectorPuzzleEmpty) cell.getData();
		if (connector == null) {
			connector = new ConnectorPuzzleEmpty();
			cell.setData(connector);
		}
		connector.append(ConnectorPuzzleEmpty.get(direction));
	}

}
