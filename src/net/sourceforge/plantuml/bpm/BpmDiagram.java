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
 */
package net.sourceforge.plantuml.bpm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class BpmDiagram extends UmlDiagram {

	private final BpmElement start = new BpmElement(null, BpmElementType.START);
	private BpmElement last = start;

	private List<BpmEvent> events = new ArrayList<BpmEvent>();

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
		final double margin = 10;
		final ImageBuilder imageBuilder = new ImageBuilder(getSkinParam(), dpiFactor,
				fileFormatOption.isWithMetadata() ? getMetadata() : null, getWarningOrError(), margin, margin,
				getAnimation());
		imageBuilder.setUDrawable(getUDrawable());

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, os);
	}

	private UDrawable getUDrawable() {
		final Grid grid = createGrid();
		final GridArray gridArray = grid.toArray(SkinParam.create(getUmlDiagramType()));
		gridArray.addEdges(edges);
		System.err.println("gridArray=" + gridArray);
		return gridArray;
	}

	public CommandExecutionResult addEvent(BpmEvent event) {
		this.events.add(event);
		return CommandExecutionResult.ok();
	}

	private Coord current;
	private final List<BpmEdge> edges = new ArrayList<BpmEdge>();

	private Grid createGrid() {
		final Grid grid = new Grid();
		this.current = grid.getRoot();
		this.edges.clear();
		grid.getCell(current).setData(start);

		for (BpmEvent event : events) {
			if (event instanceof BpmEventAdd) {
				addInGrid(grid, ((BpmEventAdd) event).getElement());
			} else if (event instanceof BpmEventResume) {
				final String idDestination = ((BpmEventResume) event).getId();
				current = grid.getById(idDestination);
				last = (BpmElement) grid.getCell(current).getData();
				if (last == null) {
					throw new IllegalStateException();
				}
				final Navigator<Line> nav = grid.linesOf(current);
				final Line newLine = new Line();
				nav.insertAfter(newLine);
				final Row row = current.getRow();
				current = new Coord(row, newLine);
			} else if (event instanceof BpmEventGoto) {
				final String idDestination = ((BpmEventGoto) event).getId();
				edges.add(new BpmEdge(last, (BpmElement) grid.getCell(grid.getById(idDestination)).getData()));
				current = grid.getById(idDestination);
				last = (BpmElement) grid.getCell(current).getData();
				if (last == null) {
					throw new IllegalStateException();
				}
				final Navigator<Line> nav = grid.linesOf(current);
				final Line newLine = new Line();
				nav.insertAfter(newLine);
				final Row row = current.getRow();
				current = new Coord(row, newLine);
			} else {
				throw new IllegalStateException();
			}
		}
		return grid;
	}

	private void addInGrid(Grid grid, BpmElement element) {
		final Navigator<Row> nav = grid.rowsOf(current);
		final Row newRow = new Row();
		nav.insertAfter(newRow);
		current = new Coord(newRow, current.getLine());
		grid.getCell(current).setData(element);
		edges.add(new BpmEdge(last, element));
		last = element;

	}
}
