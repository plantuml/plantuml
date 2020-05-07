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
 */
package net.sourceforge.plantuml.bpm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class BpmDiagram extends UmlDiagram {

	private void cleanGrid(Grid grid) {
		while (true) {
			final boolean v1 = new CleanerEmptyLine().clean(grid);
			final boolean v2 = new CleanerInterleavingLines().clean(grid);
			final boolean v3 = new CleanerMoveBlock().clean(grid);
			if (v1 == false && v2 == false && v3 == false) {
				return;
			}
		}
	}

	private final BpmElement start = new BpmElement(null, BpmElementType.START);

	private List<BpmEvent> events = new ArrayList<BpmEvent>();
	private Deque<BpmBranch> branches = new ArrayDeque<BpmBranch>();

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Bpm Diagram)");
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.BPM;
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final double dpiFactor = 1;
		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(10);
			margin2 = SkinParam.zeroMargin(10);
		} else {
			margin1 = 10;
			margin2 = 10;
		}
		final ImageBuilder imageBuilder = ImageBuilder.buildD(getSkinParam(), ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2), getAnimation(), fileFormatOption.isWithMetadata() ? getMetadata() : null,
		getWarningOrError(), dpiFactor);
		imageBuilder.setUDrawable(getUDrawable());

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, seed(), os);
	}

	private UDrawable getUDrawable() {
		final Grid grid = createGrid();
		cleanGrid(grid);
		final GridArray gridArray = grid.toArray(SkinParam.create(getUmlDiagramType()));
		// gridArray.addEdges(edges);
		// System.err.println("gridArray=" + gridArray);
		return gridArray;
	}

	public CommandExecutionResult addEvent(BpmEvent event) {
		this.events.add(event);
		return CommandExecutionResult.ok();
	}

	private Coord current;
	private Cell last;

	private Grid createGrid() {
		final Grid grid = new Grid();
		this.current = grid.getRoot();
		// this.edges.clear();
		last = grid.getCell(current);
		grid.getCell(current).setData(start);

		for (BpmEvent event : events) {
			if (event instanceof BpmEventAdd) {
				final BpmEventAdd tmp = (BpmEventAdd) event;
				addInGrid(grid, tmp.getElement());
			} else if (event instanceof BpmEventResume) {
				final String idDestination = ((BpmEventResume) event).getId();
				current = grid.getById(idDestination);
				last = grid.getCell(current);
				if (last == null) {
					throw new IllegalStateException();
				}
				final Navigator<Line> nav = grid.linesOf(current);
				final Line newLine = new Line();
				nav.insertAfter(newLine);
				final Col row = current.getCol();
				current = new Coord(newLine, row);
			} else if (event instanceof BpmEventGoto) {
				final BpmEventGoto tmp = (BpmEventGoto) event;
				final String idDestination = tmp.getId();
				current = grid.getById(idDestination);
				final Cell src = last;
				last = grid.getCell(current);
				if (last == null) {
					throw new IllegalStateException();
				}
				final Navigator<Line> nav = grid.linesOf(current);
				final Line newLine = new Line();
				nav.insertAfter(newLine);
				final Col row = current.getCol();
				current = new Coord(newLine, row);
				src.addConnectionTo2(last.getData());
			} else {
				throw new IllegalStateException();
			}
		}
		grid.addConnections();
		// for (GridEdge edge : edges) {
		// System.err.println("EDGE=" + edge.getEdgeDirection());
		// edge.addLineIn(grid);
		// }
		// grid.addEdge(edges);
		return grid;
	}

	private void addInGrid(Grid grid, BpmElement element) {
		final Navigator<Col> nav = grid.colsOf(current);
		final Col newRow = new Col();
		nav.insertAfter(newRow);
		current = new Coord(current.getLine(), newRow);
		grid.getCell(current).setData(element);
		last.addConnectionTo2(grid.getCell(current).getData());
		last = grid.getCell(current);

	}

	public CommandExecutionResult newBranch() {
		final BpmBranch branch = new BpmBranch(events.size());
		branches.addLast(branch);
		return addEvent(new BpmEventAdd(branch.getEntryElement()));
	}

	public CommandExecutionResult elseBranch() {
		final BpmBranch branch = branches.getLast();
		final int counter = branch.incAndGetCounter();
		if (counter == 2) {
			addEvent(new BpmEventAdd(branch.getElseElement()));
			return addEvent(branch.getResumeEntryEvent());
		}
		addEvent(branch.getGoToEndEvent());
		return addEvent(branch.getResumeEntryEvent());
	}

	public CommandExecutionResult endBranch() {
		final BpmBranch branch = branches.removeLast();
		return addEvent(branch.getGoToEndEvent());
	}
}
