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
package net.sourceforge.plantuml.sudoku;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;

public class PSystemSudoku extends AbstractPSystem {

	final private ISudoku sudoku;

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final GraphicsSudoku sud = new GraphicsSudoku(sudoku);
		if (fileFormat.getFileFormat() == FileFormat.EPS) {
			return sud.writeImageEps(os);
		}
		if (fileFormat.getFileFormat() == FileFormat.SVG) {
			return sud.writeImageSvg(os);
		}
		if (fileFormat.getFileFormat() == FileFormat.LATEX
				|| fileFormat.getFileFormat() == FileFormat.LATEX_NO_PREAMBLE) {
			return sud.writeImageLatex(os, fileFormat.getFileFormat());
		}
		return sud.writeImagePng(os);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Sudoku)");
	}

	public PSystemSudoku(Long seed) {
		sudoku = new SudokuDLX(seed);
	}

}
