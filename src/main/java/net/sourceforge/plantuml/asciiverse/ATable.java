/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.asciiverse;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class ATable implements AsciiBlock {

	private DataTable<Display> cells;
	private DataTable<CellSpan> mergedCells;
	private List<Integer> columnMinWidths;
	private List<Integer> rowMinHeights;
	private int leftPadding = 0;
	private int rightPadding = 0;

	private static class CellSpan {
		int startRow, startCol;
		int rowSpan, colSpan;

		CellSpan(int startRow, int startCol, int rowSpan, int colSpan) {
			this.startRow = startRow;
			this.startCol = startCol;
			this.rowSpan = rowSpan;
			this.colSpan = colSpan;
		}
	}

	public ATable(int minRows, int minCols) {

		this.cells = new DataTable<Display>(Display.NULL);
		this.columnMinWidths = new ArrayList<>();
		this.rowMinHeights = new ArrayList<>();
		this.mergedCells = new DataTable<CellSpan>(null);

		for (int j = 0; j < minCols; j++)
			this.columnMinWidths.add(1);

		for (int i = 0; i < minRows; i++)
			this.rowMinHeights.add(1);
	}

	public ATable withLeftPadding(int leftPadding) {
		this.leftPadding = Math.max(leftPadding, 0);
		return this;
	}

	public ATable withRightPadding(int rightPadding) {
		this.rightPadding = Math.max(rightPadding, 0);
		return this;
	}

	public ATable withColumnMinWidth(int col, int width) {
		columnMinWidths.set(col, Math.max(width, 1));
		return this;
	}

	public ATable withRowMinHeight(int row, int height) {
		rowMinHeights.set(row, Math.max(height, 1));
		return this;
	}

	public ATable addRow() {
		rowMinHeights.add(1);
		return this;
	}

	public ATable addColumn() {
		columnMinWidths.add(1);
		return this;
	}

	public void setCellContent(int row, int col, Display content) {
		cells.set(row, col, content);

		while (col >= columnMinWidths.size())
			columnMinWidths.add(1);

		while (row >= rowMinHeights.size())
			rowMinHeights.add(1);

		withColumnMinWidth(col, Math.max(columnMinWidths.get(col), content.contentWidth()));
		withRowMinHeight(row, Math.max(rowMinHeights.get(row), content.size()));
	}

	public Display getCellContent(int row, int col) {
		return cells.get(row, col);
	}

	public void mergeCells(int startRow, int startCol, int rowSpan, int colSpan) {
		final CellSpan span = new CellSpan(startRow, startCol, rowSpan, colSpan);

		for (int i = startRow; i < startRow + rowSpan; i++)
			for (int j = startCol; j < startCol + colSpan; j++)
				mergedCells.set(i, j, span);

	}

	private int columnRenderWidth(int col) {
		return columnMinWidths.get(col) + leftPadding + rightPadding;
	}

	// X offsets (relative to startX) of every vertical border: the left border,
	// each inter-column separator, and the right border.
	private int[] verticalBorderOffsets() {
		final int[] result = new int[columnMinWidths.size() + 1];
		int x = 0;
		result[0] = x;
		for (int col = 0; col < columnMinWidths.size(); col++) {
			x += columnRenderWidth(col) + 1;
			result[col + 1] = x;
		}
		return result;
	}

	// Draws a horizontal border at row currentY, spanning the whole table width,
	// using rounded corners and proper junctions where vertical borders cross it.
	// position: -1 = top edge, 0 = inner separator, +1 = bottom edge.
	private void drawHorizontalBorder(InfinitePlan plan, int startX, int currentY, int position) {
		final int[] borders = verticalBorderOffsets();
		final int right = borders[borders.length - 1];

		for (int x = 0; x <= right; x++)
			plan.drawChar('-', startX + x, currentY);

		for (int b = 0; b < borders.length; b++) {
			final boolean isLeft = b == 0;
			final boolean isRight = b == borders.length - 1;
			final char junction;
			if (position < 0)
				junction = isLeft ? ',' : isRight ? '.' : '+';
			else if (position > 0)
				junction = isLeft ? '`' : isRight ? '\'' : '+';
			else
				junction = '+';
			plan.drawChar(junction, startX + borders[b], currentY);
		}
	}

	public int getTotalWidth() {
		int totalWidth = 0;
		for (int col = 0; col < columnMinWidths.size(); col++)
			totalWidth += columnRenderWidth(col);

		totalWidth += columnMinWidths.size() + 1;
		return totalWidth;
	}

	public int getTotalHeight() {
		int totalHeight = 0;
		for (int height : rowMinHeights)
			totalHeight += height;

		return totalHeight;
	}

	@Override
	public XDimension2D asciiDimension() {
		final int width = getTotalWidth();
		final int height = getTotalHeight() + 2 + rowMinHeights.size() - 1;

		return new XDimension2D(width, height);
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		int currentY = 0;

		drawHorizontalBorder(plan, 0, currentY, -1);

		currentY++;

		for (int row = 0; row < rowMinHeights.size(); row++) {
			int currentX = 0;
			int rowHeight = rowMinHeights.get(row);

			for (int col = 0; col < columnMinWidths.size(); col++) {
				final CellSpan span = mergedCells.get(row, col);
				if (span != null && span.startRow == row && span.startCol == col) {

					final Display content = cells.get(row, col);
					int cellWidth = 0;
					for (int c = col; c < col + span.colSpan; c++)
						cellWidth += columnRenderWidth(c);

					cellWidth += span.colSpan - 1;

					plan.drawChar('|', currentX, currentY);
					currentX++;
					for (int i = 0; i < cellWidth; i++) {
						final char c = charAt(content, i - leftPadding);
						plan.drawChar(c, currentX + i, currentY);
					}
					currentX += cellWidth;

					col += span.colSpan - 1;
				} else if (span == null) {
					final Display content = cells.get(row, col);
					int cellWidth = columnRenderWidth(col);

					plan.drawChar('|', currentX, currentY);
					currentX++;
					for (int i = 0; i < cellWidth; i++) {
						final char c = charAt(content, i - leftPadding);
						plan.drawChar(c, currentX + i, currentY);
					}
					currentX += cellWidth;
				}
			}

			plan.drawChar('|', currentX, currentY);

			for (int h = 1; h < rowHeight; h++) {
				currentY++;
				currentX = 0;
				for (int col = 0; col < columnMinWidths.size(); col++) {
					final CellSpan span = mergedCells.get(row, col);
					if (span != null && span.startRow == row && span.startCol == col) {
						int cellWidth = 0;
						for (int c = col; c < col + span.colSpan; c++)
							cellWidth += columnRenderWidth(c);

						cellWidth += span.colSpan - 1;

						plan.drawChar('|', currentX, currentY);
						currentX++;
						for (int i = 0; i < cellWidth; i++)
							plan.drawChar(' ', currentX + i, currentY);

						currentX += cellWidth;
						col += span.colSpan - 1;
					} else if (span == null) {

						final int cellWidth = columnRenderWidth(col);
						plan.drawChar('|', currentX, currentY);
						currentX++;
						for (int i = 0; i < cellWidth; i++) {
							plan.drawChar(' ', currentX + i, currentY);
						}
						currentX += cellWidth;
					}
				}
				plan.drawChar('|', currentX, currentY);
			}
			currentY++;

			final int position = row == rowMinHeights.size() - 1 ? 1 : 0;
			drawHorizontalBorder(plan, 0, currentY, position);

			currentY++;
		}
	}

	private char charAt(Display content2, int i) {
		if (i < 0)
			return ' ';
		if (content2.size() == 0)
			return ' ';
		final CharSequence line0 = content2.get(0);
		if (i < line0.length())
			return line0.charAt(i);
		return ' ';
	}
}
