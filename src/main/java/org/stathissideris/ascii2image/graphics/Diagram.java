/*
 * DiTAA - Diagrams Through Ascii Art
 * 
 * Copyright (C) 2004 Efstathios Sideris
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *   
 */
package org.stathissideris.ascii2image.graphics;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

import org.stathissideris.ascii2image.core.ConversionOptions;
import org.stathissideris.ascii2image.core.Pair;
import org.stathissideris.ascii2image.core.ProcessingOptions;
import org.stathissideris.ascii2image.text.AbstractionGrid;
import org.stathissideris.ascii2image.text.CellSet;
import org.stathissideris.ascii2image.text.TextGrid;

/**
 * 
 * @author Efstathios Sideris
 */
public class Diagram {

	private static final boolean DEBUG = false;
	private static final boolean VERBOSE_DEBUG = false;

	private ArrayList shapes = new ArrayList();
	private ArrayList compositeShapes = new ArrayList();
	private ArrayList textObjects = new ArrayList();
	
	private int width, height;
	private int cellWidth, cellHeight;
	
	
	/**
	 * 
	 * <p>An outline of the inner workings of this very important (and monstrous)
	 * constructor is presented here. Boundary processing is the first step
	 * of the process:</p>
	 * 
	 * <ol>
	 *   <li>Copy the grid into a work grid and remove all type-on-line
	 *       and point markers from the work grid</li>
	 *   <li>Split grid into distinct shapes by plotting the grid
	 * 	     onto an AbstractionGrid and its getDistinctShapes() method.</li>
	 *   <li>Find all the possible boundary sets of each of the
	 *       distinct shapes. This can produce duplicate shapes (if the boundaries
	 *       are the same when filling from the inside and the outside).</li>
	 *   <li>Remove duplicate boundaries.</li>
	 *   <li>Remove obsolete boundaries. Obsolete boundaries are the ones that are
	 *       the sum of their parts when plotted as filled shapes. (see method
	 *       removeObsoleteShapes())</li>
	 *   <li>Seperate the found boundary sets to open, closed or mixed
	 *       (See CellSet class on how its done).</li>
	 *   <li>Are there any closed boundaries?
	 *        <ul>
	 *           <li>YES. Subtract all the closed boundaries from each of the
	 *           open ones. That should convert the mixed shapes into open.</li>
	 *           <li>NO. In this (harder) case, we use the method
	 *           breakTrulyMixedBoundaries() of CellSet to break boundaries
	 *           into open and closed shapes (would work in any case, but it's
	 *           probably slower than the other method). This method is based
	 *           on tracing from the lines' ends and splitting when we get to
	 *           an intersection.</li>
	 *        </ul>
	 *   </li>
	 *   <li>If we had to eliminate any mixed shapes, we seperate the found
	 *   boundary sets again to open, closed or mixed.</li>
	 * </ol>
	 * 
	 * <p>At this stage, the boundary processing is all complete and we
	 * proceed with using those boundaries to create the shapes:</p>
	 * 
	 * <ol>
	 *   <li>Create closed shapes.</li>
	 *   <li>Create open shapes. That's when the line end corrections are
	 *   also applied, concerning the positioning of the ends of lines
	 *   see methods connectEndsToAnchors() and moveEndsToCellEdges() of
	 *   DiagramShape.</li>
	 *   <li>Assign color codes to closed shapes.</li>
	 *   <li>Assing extended markup tags to closed shapes.</p>
	 *   <li>Create arrowheads.</p>
	 *   <li>Create point markers.</p>
	 * </ol>
	 * 
	 * <p>Finally, the text processing occurs: [pending]</p>
	 * 
	 * @param grid
	 * @param cellWidth
	 * @param cellHeight
	 */
	public Diagram(TextGrid grid, ConversionOptions options, ProcessingOptions processingOptions) {
		
		this.cellWidth = options.renderingOptions.getCellWidth();
		this.cellHeight = options.renderingOptions.getCellHeight();
		
		width = grid.getWidth() * cellWidth;
		height = grid.getHeight() * cellHeight;
		
		TextGrid workGrid = new TextGrid(grid);
		workGrid.replaceTypeOnLine();
		workGrid.replacePointMarkersOnLine();
		if(DEBUG) workGrid.printDebug();
		
		int width = grid.getWidth();
		int height = grid.getHeight();

	
		//split distinct shapes using AbstractionGrid 
		AbstractionGrid temp = new AbstractionGrid(workGrid, workGrid.getAllBoundaries());
		ArrayList boundarySetsStep1 = temp.getDistinctShapes();
		
		if(DEBUG){
			System.out.println("******* Distinct shapes found using AbstractionGrid *******");
			Iterator dit = boundarySetsStep1.iterator();
			while (dit.hasNext()) {
				CellSet set = (CellSet) dit.next();
				set.printAsGrid();
			}
			System.out.println("******* Same set of shapes after processing them by filling *******");
		}
		
		
		//Find all the boundaries by using the special version of the filling method
		//(fills in a different buffer than the buffer it reads from)
		ArrayList boundarySetsStep2 = new ArrayList();
		Iterator boundarySetIt = boundarySetsStep1.iterator();
		while (boundarySetIt.hasNext()) {
			CellSet set = (CellSet) boundarySetIt.next();
			
			//the fill buffer keeps track of which cells have been
			//filled already
			TextGrid fillBuffer = new TextGrid(width * 3, height * 3);
			
			for(int yi = 0; yi < height * 3; yi++){
				for(int xi = 0; xi < width * 3; xi++){
					if(fillBuffer.isBlank(xi, yi)){
						
						TextGrid copyGrid = new AbstractionGrid(workGrid, set).getCopyOfInternalBuffer();

						CellSet boundaries =
							copyGrid
							.findBoundariesExpandingFrom(copyGrid.new Cell(xi, yi));
						if(boundaries.size() == 0) continue; //i'm not sure why these occur
						boundarySetsStep2.add(boundaries.makeScaledOneThirdEquivalent());
					
						copyGrid = new AbstractionGrid(workGrid, set).getCopyOfInternalBuffer();
						CellSet filled =
							copyGrid
							.fillContinuousArea(copyGrid.new Cell(xi, yi), '*');
						fillBuffer.fillCellsWith(filled, '*');
						fillBuffer.fillCellsWith(boundaries, '-');
						
						if(DEBUG){
							//System.out.println("Fill buffer:");
							//fillBuffer.printDebug();
							boundaries.makeScaledOneThirdEquivalent().printAsGrid();
							System.out.println("-----------------------------------");
						}
						
					}
				}
			}
		}

		if (DEBUG)
			System.out.println("******* Removed duplicates *******");

		boundarySetsStep2 = CellSet.removeDuplicateSets(boundarySetsStep2);

		if(DEBUG){
			Iterator dit = boundarySetsStep2.iterator();
			while (dit.hasNext()) {
				CellSet set = (CellSet) dit.next();
				set.printAsGrid();
			}
		}

		int originalSize = boundarySetsStep2.size(); 
		boundarySetsStep2 = CellSet.removeDuplicateSets(boundarySetsStep2);
		if(DEBUG) {
			System.out.println(
				"******* Removed duplicates: there were "
				+originalSize
				+" shapes and now there are "
				+boundarySetsStep2.size());
		} 
		

		//split boundaries to open, closed and mixed
		
		if (DEBUG)
			System.out.println("******* First evaluation of openess *******");
		
		ArrayList open = new ArrayList();
		ArrayList closed = new ArrayList();
		ArrayList mixed = new ArrayList();
		
		Iterator sets = boundarySetsStep2.iterator();
		while(sets.hasNext()){
			CellSet set = (CellSet) sets.next();
			int type = set.getType(workGrid);
			if(type == CellSet.TYPE_CLOSED) closed.add(set);
			else if(type == CellSet.TYPE_OPEN) open.add(set);
			else if(type == CellSet.TYPE_MIXED) mixed.add(set);
			if(DEBUG){
				if(type == CellSet.TYPE_CLOSED) System.out.println("Closed boundaries:");
				else if(type == CellSet.TYPE_OPEN) System.out.println("Open boundaries:");
				else if(type == CellSet.TYPE_MIXED) System.out.println("Mixed boundaries:");
				set.printAsGrid();
			}
		}
		
		boolean hadToEliminateMixed = false;
		
		if(mixed.size() > 0 && closed.size() > 0) {
							// mixed shapes can be eliminated by
							// subtracting all the closed shapes from them 
			if (DEBUG)
				System.out.println("******* Eliminating mixed shapes (basic algorithm) *******");
		
			hadToEliminateMixed = true;
			
			//subtract from each of the mixed sets all the closed sets
			sets = mixed.iterator();
			while(sets.hasNext()){
				CellSet set = (CellSet) sets.next();
				Iterator closedSets = closed.iterator();
				while(closedSets.hasNext()){
					CellSet closedSet = (CellSet) closedSets.next();
					set.subtractSet(closedSet);
				}
				// this is necessary because some mixed sets produce
				// several distinct open sets after you subtract the
				// closed sets from them
				if(set.getType(workGrid) == CellSet.TYPE_OPEN) {
					boundarySetsStep2.remove(set);
					boundarySetsStep2.addAll(set.breakIntoDistinctBoundaries(workGrid));
				}
			}

		} else if(mixed.size() > 0 && closed.size() == 0) {
							// no closed shape exists, will have to
							// handle mixed shape on its own 
			// an example of this case is the following:
			// +-----+
			// |  A  |C                 B
			// +  ---+-------------------
			// |     |
			// +-----+

			hadToEliminateMixed = true;

			if (DEBUG)
				System.out.println("******* Eliminating mixed shapes (advanced algorithm for truly mixed shapes) *******");
				
			sets = mixed.iterator();
			while(sets.hasNext()){
				CellSet set = (CellSet) sets.next();
				boundarySetsStep2.remove(set);
				boundarySetsStep2.addAll(set.breakTrulyMixedBoundaries(workGrid));
			}

		} else {
			if (DEBUG)
				System.out.println("No mixed shapes found. Skipped mixed shape elimination step");
		}
		
		
		if(hadToEliminateMixed){
			if (DEBUG)
				System.out.println("******* Second evaluation of openess *******");
		
			//split boundaries again to open, closed and mixed
			open = new ArrayList();
			closed = new ArrayList();
			mixed = new ArrayList();
		
			sets = boundarySetsStep2.iterator();
			while(sets.hasNext()){
				CellSet set = (CellSet) sets.next();
				int type = set.getType(workGrid);
				if(type == CellSet.TYPE_CLOSED) closed.add(set);
				else if(type == CellSet.TYPE_OPEN) open.add(set);
				else if(type == CellSet.TYPE_MIXED) mixed.add(set);
				if(DEBUG){
					if(type == CellSet.TYPE_CLOSED) System.out.println("Closed boundaries:");
					else if(type == CellSet.TYPE_OPEN) System.out.println("Open boundaries:");
					else if(type == CellSet.TYPE_MIXED) System.out.println("Mixed boundaries:");
					set.printAsGrid();
				}
			}
		}

		boolean removedAnyObsolete = removeObsoleteShapes(workGrid, closed);
		
		boolean allCornersRound = false;
		if(processingOptions.areAllCornersRound()) allCornersRound = true;
		
		//make shapes from the boundary sets
		//make closed shapes
		ArrayList closedShapes = new ArrayList();
		sets = closed.iterator();
		while(sets.hasNext()){
			CellSet set = (CellSet) sets.next();
			DiagramComponent shape = DiagramComponent.createClosedFromBoundaryCells(workGrid, set, cellWidth, cellHeight, allCornersRound); 
			if(shape != null){
				if(shape instanceof DiagramShape){
					addToShapes((DiagramShape) shape);
					closedShapes.add(shape);
				} else if(shape instanceof CompositeDiagramShape)
					addToCompositeShapes((CompositeDiagramShape) shape);
			}
		}

		if(processingOptions.performSeparationOfCommonEdges())
			separateCommonEdges(closedShapes);

		//make open shapes
		sets = open.iterator();
		while(sets.hasNext()){
			CellSet set = (CellSet) sets.next();
			if(set.size() == 1){ //single cell "shape"
				TextGrid.Cell cell = (TextGrid.Cell) set.getFirst();
				if(!grid.cellContainsDashedLineChar(cell)) { 
					DiagramShape shape = DiagramShape.createSmallLine(workGrid, cell, cellWidth, cellHeight); 
					if(shape != null) {
						addToShapes(shape); 
						shape.connectEndsToAnchors(workGrid, this);
					}
				}
			} else { //normal shape
				DiagramComponent shape =
					CompositeDiagramShape
						.createOpenFromBoundaryCells(
								workGrid, set, cellWidth, cellHeight, allCornersRound);

				if(shape != null){
					if(shape instanceof CompositeDiagramShape){
						addToCompositeShapes((CompositeDiagramShape) shape);
						((CompositeDiagramShape) shape).connectEndsToAnchors(workGrid, this);
					} else if(shape instanceof DiagramShape) {
						addToShapes((DiagramShape) shape);
						((DiagramShape) shape).connectEndsToAnchors(workGrid, this);
						((DiagramShape) shape).moveEndsToCellEdges(grid, this);
					}
				}
					
			}
		}

		//assign color codes to shapes
		//TODO: text on line should not change its color
		//TODO: each color tag should be assigned to the smallest containing shape (like shape tags)
		
		Iterator cellColorPairs = grid.findColorCodes().iterator();
		while(cellColorPairs.hasNext()){
			TextGrid.CellColorPair pair =
				(TextGrid.CellColorPair) cellColorPairs.next();
			ShapePoint point =
				new ShapePoint(getCellMidX(pair.cell), getCellMidY(pair.cell));
			Iterator shapes = getShapes().iterator();
			while(shapes.hasNext()){
				DiagramShape shape = (DiagramShape) shapes.next();
				if(shape.contains(point)) shape.setFillColor(pair.color);  
			}
		}

		//assign markup to shapes
		Iterator cellTagPairs = grid.findMarkupTags().iterator();
		while(cellTagPairs.hasNext()){
			TextGrid.CellTagPair pair =
				(TextGrid.CellTagPair) cellTagPairs.next();
			ShapePoint point =
				new ShapePoint(getCellMidX(pair.cell), getCellMidY(pair.cell));

			//find the smallest shape that contains the tag
			DiagramShape containingShape = null;
			Iterator shapes = getShapes().iterator();
			while(shapes.hasNext()){
				DiagramShape shape = (DiagramShape) shapes.next();
				if(shape.contains(point)){
					if(containingShape == null){
						containingShape = shape;
					} else {
						if(shape.isSmallerThan(containingShape)){
							containingShape = shape;
						}
					}
				}
			}
			
			//this tag is not within a shape, skip
			if(containingShape == null) continue;
			
			//TODO: the code below could be a lot more concise
			if(pair.tag.equals("d")){
				CustomShapeDefinition def =
					processingOptions.getFromCustomShapes("d");
				if(def == null)
					containingShape.setType(DiagramShape.TYPE_DOCUMENT);
				else {
					containingShape.setType(DiagramShape.TYPE_CUSTOM);
					containingShape.setDefinition(def);
				}
			} else if(pair.tag.equals("s")){
				CustomShapeDefinition def =
					processingOptions.getFromCustomShapes("s");
				if(def == null)
					containingShape.setType(DiagramShape.TYPE_STORAGE);
				else {
					containingShape.setType(DiagramShape.TYPE_CUSTOM);
					containingShape.setDefinition(def);
				}
			} else if(pair.tag.equals("io")){
				CustomShapeDefinition def =
					processingOptions.getFromCustomShapes("io");
				if(def == null)
					containingShape.setType(DiagramShape.TYPE_IO);
				else {
					containingShape.setType(DiagramShape.TYPE_CUSTOM);
					containingShape.setDefinition(def);
				}
			} else if(pair.tag.equals("c")){
				CustomShapeDefinition def =
					processingOptions.getFromCustomShapes("c");
				if(def == null)
					containingShape.setType(DiagramShape.TYPE_DECISION);
				else {
					containingShape.setType(DiagramShape.TYPE_CUSTOM);
					containingShape.setDefinition(def);
				}
			} else if(pair.tag.equals("mo")){
				CustomShapeDefinition def =
					processingOptions.getFromCustomShapes("mo");
				if(def == null)
					containingShape.setType(DiagramShape.TYPE_MANUAL_OPERATION);
				else {
					containingShape.setType(DiagramShape.TYPE_CUSTOM);
					containingShape.setDefinition(def);
				}
			} else if(pair.tag.equals("tr")){
				CustomShapeDefinition def =
					processingOptions.getFromCustomShapes("tr");
				if(def == null)
					containingShape.setType(DiagramShape.TYPE_TRAPEZOID);
				else {
					containingShape.setType(DiagramShape.TYPE_CUSTOM);
					containingShape.setDefinition(def);
				}
			} else if(pair.tag.equals("o")){
				CustomShapeDefinition def =
					processingOptions.getFromCustomShapes("o");
				if(def == null)
					containingShape.setType(DiagramShape.TYPE_ELLIPSE);
				else {
					containingShape.setType(DiagramShape.TYPE_CUSTOM);
					containingShape.setDefinition(def);
				}
			} else {
				CustomShapeDefinition def =
					processingOptions.getFromCustomShapes(pair.tag);
				containingShape.setType(DiagramShape.TYPE_CUSTOM);
				containingShape.setDefinition(def);						
			}
		}
		
		//make arrowheads
		Iterator arrowheadCells = workGrid.findArrowheads().iterator();
		while(arrowheadCells.hasNext()){
			TextGrid.Cell cell = (TextGrid.Cell) arrowheadCells.next();
			DiagramShape arrowhead = DiagramShape.createArrowhead(workGrid, cell, cellWidth, cellHeight);
			if(arrowhead != null) addToShapes(arrowhead);
			else System.err.println("Could not create arrowhead shape. Unexpected error.");
		}
		
		//make point markers
		Iterator markersIt = grid.getPointMarkersOnLine().iterator();
		while (markersIt.hasNext()) {
			TextGrid.Cell cell = (TextGrid.Cell) markersIt.next();

			DiagramShape mark = new DiagramShape();
			mark.addToPoints(new ShapePoint(
					getCellMidX(cell),
					getCellMidY(cell)
				));
			mark.setType(DiagramShape.TYPE_POINT_MARKER);
			mark.setFillColor(Color.white);
			shapes.add(mark);
		}

		removeDuplicateShapes();
		
		if(DEBUG) System.out.println("Shape count: "+shapes.size());
		if(DEBUG) System.out.println("Composite shape count: "+compositeShapes.size());
		
		//copy again
		workGrid = new TextGrid(grid);
		workGrid.removeNonText();
		
		
		// ****** handle text *******
		//break up text into groups
		TextGrid textGroupGrid = new TextGrid(workGrid);
		CellSet gaps = textGroupGrid.getAllBlanksBetweenCharacters();
		//kludge
		textGroupGrid.fillCellsWith(gaps, '|');
		CellSet nonBlank = textGroupGrid.getAllNonBlank();
		ArrayList textGroups = nonBlank.breakIntoDistinctBoundaries();
		if(DEBUG) System.out.println(textGroups.size()+" text groups found");
		
		Font font = FontMeasurer.instance().getFontFor(cellHeight);
		
		Iterator textGroupIt = textGroups.iterator();
		while(textGroupIt.hasNext()){
			CellSet textGroupCellSet = (CellSet) textGroupIt.next();
			
			TextGrid isolationGrid = new TextGrid(width, height);
			workGrid.copyCellsTo(textGroupCellSet, isolationGrid);
			 
			ArrayList strings = isolationGrid.findStrings();
			Iterator it = strings.iterator();
			while(it.hasNext()){
				TextGrid.CellStringPair pair = (TextGrid.CellStringPair) it.next();
				TextGrid.Cell cell = pair.cell;
				String string = pair.string;
				if (DEBUG)
					System.out.println("Found string "+string);
				TextGrid.Cell lastCell = isolationGrid.new Cell(cell.x + string.length() - 1, cell.y);
			
				int minX = getCellMinX(cell);
				int y = getCellMaxY(cell);
				int maxX = getCellMaxX(lastCell);
			
				DiagramText textObject;
				if(FontMeasurer.instance().getWidthFor(string, font) > maxX - minX){ //does not fit horizontally
					Font lessWideFont = FontMeasurer.instance().getFontFor(maxX - minX, string);
					textObject = new DiagramText(minX, y, string, lessWideFont);
				} else textObject = new DiagramText(minX, y, string, font);
			
				textObject.centerVerticallyBetween(getCellMinY(cell), getCellMaxY(cell));
			
				//TODO: if the strings start with bullets they should be aligned to the left
			
				//position text correctly
				int otherStart = isolationGrid.otherStringsStartInTheSameColumn(cell);
				int otherEnd = isolationGrid.otherStringsEndInTheSameColumn(lastCell);
				if(0 == otherStart && 0 == otherEnd) {
					textObject.centerHorizontallyBetween(minX, maxX);
				} else if(otherEnd > 0 && otherStart == 0) {
					textObject.alignRightEdgeTo(maxX);
				} else if(otherEnd > 0 && otherStart > 0){
					if(otherEnd > otherStart){
						textObject.alignRightEdgeTo(maxX);
					} else if(otherEnd == otherStart){
						textObject.centerHorizontallyBetween(minX, maxX);
					}
				}
			
				addToTextObjects(textObject);
			}
		}
		
		if (DEBUG)
			System.out.println("Positioned text");
		
		//correct the color of the text objects according
		//to the underlying color
		Iterator shapes = this.getAllDiagramShapes().iterator();
		while(shapes.hasNext()){
			DiagramShape shape = (DiagramShape) shapes.next();
			Color fillColor = shape.getFillColor();
			if(shape.isClosed()
					&& shape.getType() != DiagramShape.TYPE_ARROWHEAD
					&& fillColor != null
					&& BitmapRenderer.isColorDark(fillColor)){
				Iterator textObjects = getTextObjects().iterator();
				while(textObjects.hasNext()){
					DiagramText textObject = (DiagramText) textObjects.next();
					if(shape.intersects(textObject.getBounds())){
						textObject.setColor(Color.white);
					}
				}
			}
		}

		//set outline to true for test within custom shapes
		shapes = this.getAllDiagramShapes().iterator();
		while(shapes.hasNext()){
			DiagramShape shape = (DiagramShape) shapes.next();
			if(shape.getType() == DiagramShape.TYPE_CUSTOM){
				Iterator textObjects = getTextObjects().iterator();
				while(textObjects.hasNext()){
					DiagramText textObject = (DiagramText) textObjects.next();
					textObject.setHasOutline(true);
					textObject.setColor(DiagramText.DEFAULT_COLOR);
				}
			}
		}
		
		if (DEBUG)
			System.out.println("Corrected color of text according to underlying color");

	}
	
	/**
	 * Returns a list of all DiagramShapes in the Diagram, including
	 * the ones within CompositeDiagramShapes
	 * 
	 * @return
	 */
	public ArrayList getAllDiagramShapes(){
		ArrayList shapes = new ArrayList();
		shapes.addAll(this.getShapes());
		
		Iterator shapesIt = this.getCompositeShapes().iterator();
		while(shapesIt.hasNext()){
			CompositeDiagramShape compShape = (CompositeDiagramShape) shapesIt.next();
			shapes.addAll(compShape.getShapes());
		}
		return shapes;		
	}
	
	/**
	 * Removes the sets from <code>sets</code>that are the sum of their parts
	 * when plotted as filled shapes.
	 * 
	 * @return true if it removed any obsolete.
	 * 
	 */
	private boolean removeObsoleteShapes(TextGrid grid, ArrayList<CellSet> sets){
		if (DEBUG)
			System.out.println("******* Removing obsolete shapes *******");
		
		boolean removedAny = false;
		
		ArrayList<CellSet> filledSets = new ArrayList<CellSet>();

		Iterator it;

		if(VERBOSE_DEBUG) {
			System.out.println("******* Sets before *******");
			it = sets.iterator();
			while(it.hasNext()){
				CellSet set = (CellSet) it.next();
				set.printAsGrid();
			}
		}

		//make filled versions of all the boundary sets
		it = sets.iterator();
		while(it.hasNext()){
			CellSet set = (CellSet) it.next();
			set = set.getFilledEquivalent(grid);
			if(set == null){
				return false;
			} else filledSets.add(set);
		}
		
		ArrayList<Integer> toBeRemovedIndices = new ArrayList<Integer>();
		it = filledSets.iterator();
		while(it.hasNext()){
			CellSet set = (CellSet) it.next();
			
			if(VERBOSE_DEBUG){
				System.out.println("*** Deciding if the following should be removed:");
				set.printAsGrid();
			}
			
			//find the other sets that have common cells with set
			ArrayList<CellSet> common = new ArrayList<CellSet>();
			common.add(set);
			Iterator it2 = filledSets.iterator();
			while(it2.hasNext()){
				CellSet set2 = (CellSet) it2.next();
				if(set != set2 && set.hasCommonCells(set2)){
					common.add(set2);
				}
			}
			//it only makes sense for more than 2 sets
			if(common.size() == 2) continue;
			
			//find largest set
			CellSet largest = set;
			it2 = common.iterator();
			while(it2.hasNext()){
				CellSet set2 = (CellSet) it2.next();
				if(set2.size() > largest.size()){
					largest = set2;
				}
			}
			
			if(VERBOSE_DEBUG){
				System.out.println("Largest:");
				largest.printAsGrid();
			}

			//see if largest is sum of others
			common.remove(largest);

			//make the sum set of the small sets on a grid
			TextGrid gridOfSmalls = new TextGrid(largest.getMaxX() + 2, largest.getMaxY() + 2);
			CellSet sumOfSmall = new CellSet();
			it2 = common.iterator();
			while(it2.hasNext()){
				CellSet set2 = (CellSet) it2.next();
				if(VERBOSE_DEBUG){
					System.out.println("One of smalls:");
					set2.printAsGrid();
				}
				gridOfSmalls.fillCellsWith(set2, '*');
			}
			if(VERBOSE_DEBUG){
				System.out.println("Sum of smalls:");
				gridOfSmalls.printDebug();
			}
			TextGrid gridLargest = new TextGrid(largest.getMaxX() + 2, largest.getMaxY() + 2);
			gridLargest.fillCellsWith(largest, '*');

			int index = filledSets.indexOf(largest);
			if(gridLargest.equals(gridOfSmalls)
					&& !toBeRemovedIndices.contains(new Integer(index))) {
				toBeRemovedIndices.add(new Integer(index));
				if (DEBUG){
					System.out.println("Decided to remove set:");
					largest.printAsGrid();
				}
			} else if (DEBUG){
				System.out.println("This set WILL NOT be removed:");
				largest.printAsGrid();
			}
			//if(gridLargest.equals(gridOfSmalls)) toBeRemovedIndices.add(new Integer(index));
		}
		
		ArrayList<CellSet> setsToBeRemoved = new ArrayList<CellSet>();
		it = toBeRemovedIndices.iterator();
		while(it.hasNext()){
			int i = ((Integer) it.next()).intValue();
			setsToBeRemoved.add(sets.get(i));
		}
	
		it = setsToBeRemoved.iterator();
		while(it.hasNext()){
			CellSet set = (CellSet) it.next();
			removedAny = true;
			sets.remove(set);
		}
	
		if(VERBOSE_DEBUG) {
			System.out.println("******* Sets after *******");
			it = sets.iterator();
			while(it.hasNext()){
				CellSet set = (CellSet) it.next();
				set.printAsGrid();
			}
		}
		
		return removedAny;
	}
	
	public float getMinimumOfCellDimension(){
		return Math.min(getCellWidth(), getCellHeight());
	}
	
	private void separateCommonEdges(ArrayList shapes){

		float offset = getMinimumOfCellDimension() / 5;

		ArrayList<ShapeEdge> edges = new ArrayList<ShapeEdge>();

		//get all adges
		Iterator it = shapes.iterator();
		while (it.hasNext()) {
			DiagramShape shape = (DiagramShape) it.next();
			edges.addAll(shape.getEdges());
		}
		
		//group edges into pairs of touching edges
		ArrayList<Pair<ShapeEdge, ShapeEdge>> listOfPairs = new ArrayList<Pair<ShapeEdge, ShapeEdge>>();
		it = edges.iterator();
		
		//all-against-all touching test for the edges
		int startIndex = 1; //skip some to avoid duplicate comparisons and self-to-self comparisons
		
		while(it.hasNext()){
			ShapeEdge edge1 = (ShapeEdge) it.next();
			
			for(int k = startIndex; k < edges.size(); k++) {
				ShapeEdge edge2 =  edges.get(k);
				
				if(edge1.touchesWith(edge2)) {
					listOfPairs.add(new Pair<ShapeEdge, ShapeEdge>(edge1, edge2));
				}
			}
			startIndex++;
		}
		
		ArrayList<ShapeEdge> movedEdges = new ArrayList<ShapeEdge>();
		
		//move equivalent edges inwards
		it = listOfPairs.iterator();
		while(it.hasNext()){
			Pair<ShapeEdge, ShapeEdge> pair = (Pair<ShapeEdge, ShapeEdge>) it.next();
			if(!movedEdges.contains(pair.first)) {
				pair.first.moveInwardsBy(offset);
				movedEdges.add(pair.first);
			}
			if(!movedEdges.contains(pair.second)) {
				pair.second.moveInwardsBy(offset);
				movedEdges.add(pair.second);
			}
		}

	}
	
	
	//TODO: removes more than it should
	private void removeDuplicateShapes() {
		ArrayList originalShapes = new ArrayList();

		Iterator shapesIt = getShapesIterator();
		while(shapesIt.hasNext()){
			DiagramShape shape = (DiagramShape) shapesIt.next();
			boolean isOriginal = true;
			Iterator originals = originalShapes.iterator();
			while(originals.hasNext()){
				DiagramShape originalShape = (DiagramShape) originals.next();
				if(shape.equals(originalShape)){
					isOriginal = false;
				}
			}
			if(isOriginal) originalShapes.add(shape);
		}

		shapes.clear();
		shapes.addAll(originalShapes);
	}
	
	private void addToTextObjects(DiagramText shape){
		textObjects.add(shape);
	}

	private void addToCompositeShapes(CompositeDiagramShape shape){
		compositeShapes.add(shape);
	}

	
	private void addToShapes(DiagramShape shape){
		shapes.add(shape);
	}
	
	public Iterator getShapesIterator(){
		return shapes.iterator();
	}	

	/**
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return
	 */
	public int getCellWidth() {
		return cellWidth;
	}

	/**
	 * @return
	 */
	public int getCellHeight() {
		return cellHeight;
	}

	/**
	 * @return
	 */
	public ArrayList getCompositeShapes() {
		return compositeShapes;
	}

	/**
	 * @return
	 */
	public ArrayList getShapes() {
		return shapes;
	}
	
	public int getCellMinX(TextGrid.Cell cell){
		return getCellMinX(cell, cellWidth);
	}
	public static int getCellMinX(TextGrid.Cell cell, int cellXSize){
		return cell.x * cellXSize;
	}

	public int getCellMidX(TextGrid.Cell cell){
		return getCellMidX(cell, cellWidth);
	}
	public static int getCellMidX(TextGrid.Cell cell, int cellXSize){
		return cell.x * cellXSize + cellXSize / 2;
	}

	public int getCellMaxX(TextGrid.Cell cell){
		return getCellMaxX(cell, cellWidth);
	}
	public static int getCellMaxX(TextGrid.Cell cell, int cellXSize){
		return cell.x * cellXSize + cellXSize;
	}

	public int getCellMinY(TextGrid.Cell cell){
		return getCellMinY(cell, cellHeight);
	}
	public static int getCellMinY(TextGrid.Cell cell, int cellYSize){
		return cell.y * cellYSize;
	}

	public int getCellMidY(TextGrid.Cell cell){
		return getCellMidY(cell, cellHeight);
	}
	public static int getCellMidY(TextGrid.Cell cell, int cellYSize){
		return cell.y * cellYSize + cellYSize / 2;
	}

	public int getCellMaxY(TextGrid.Cell cell){
		return getCellMaxY(cell, cellHeight);
	}
	public static int getCellMaxY(TextGrid.Cell cell, int cellYSize){
		return cell.y * cellYSize + cellYSize;
	}

	public TextGrid.Cell getCellFor(ShapePoint point){
		if(point == null) throw new IllegalArgumentException("ShapePoint cannot be null");
		//TODO: the fake grid is a problem
		TextGrid g = new TextGrid();
		return g.new Cell((int) point.x / cellWidth,
							(int) point.y / cellHeight);
	}


	/**
	 * @return
	 */
	public ArrayList getTextObjects() {
		return textObjects;
	}

}
