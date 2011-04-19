/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6453 $
 */
package net.sourceforge.plantuml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import net.sourceforge.plantuml.graphic.GraphicStrings;

public class PSystemError extends AbstractPSystem {

	private final List<String> htmlStrings = new ArrayList<String>();
	private final List<String> plainStrings = new ArrayList<String>();
	private final int higherErrorPosition;
	private final List<ErrorUml> printedErrors;

	public PSystemError(UmlSource source, List<ErrorUml> all) {
		this.setSource(source);

		final int higherErrorPositionExecution = getHigherErrorPosition(ErrorUmlType.EXECUTION_ERROR, all);
		final int higherErrorPositionSyntax = getHigherErrorPosition(ErrorUmlType.SYNTAX_ERROR, all);

		if (higherErrorPositionExecution == Integer.MIN_VALUE && higherErrorPositionSyntax == Integer.MIN_VALUE) {
			throw new IllegalStateException();
		}

		if (higherErrorPositionExecution >= higherErrorPositionSyntax) {
			higherErrorPosition = higherErrorPositionExecution;
			printedErrors = getErrorsAt(higherErrorPositionExecution, ErrorUmlType.EXECUTION_ERROR, all);
		} else {
			assert higherErrorPositionSyntax > higherErrorPositionExecution;
			higherErrorPosition = higherErrorPositionSyntax;
			printedErrors = getErrorsAt(higherErrorPositionSyntax, ErrorUmlType.SYNTAX_ERROR, all);
		}
		appendSource(higherErrorPosition);

	}

	public PSystemError(UmlSource source, ErrorUml singleError) {
		this(source, Collections.singletonList(singleError));
	}

//	public List<File> createFiles(File suggestedFile, FileFormatOption fileFormat) throws IOException,
//			InterruptedException {
//		if (suggestedFile.exists() && suggestedFile.isDirectory()) {
//			throw new IllegalArgumentException("File is a directory " + suggestedFile);
//		}
//		OutputStream os = null;
//		try {
//			os = new FileOutputStream(suggestedFile);
//			getPngError().writeImage(os, getMetadata(), fileFormat);
//		} finally {
//			if (os != null) {
//				os.close();
//			}
//		}
//		return Arrays.asList(suggestedFile);
//	}

	public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormat) throws IOException {
		getPngError().writeImage(os, getMetadata(), fileFormat);
	}

	public GraphicStrings getPngError() throws IOException {
		return new GraphicStrings(htmlStrings);
	}

	private void appendSource(int position) {
		final int limit = 4;
		int start;
		final int skip = position - limit + 1;
		if (skip <= 0) {
			start = 0;
		} else {
			if (skip == 1) {
				htmlStrings.add("... (skipping 1 line) ...");
				plainStrings.add("... (skipping 1 line) ...");
			} else {
				htmlStrings.add("... (skipping " + skip + " lines) ...");
				plainStrings.add("... (skipping " + skip + " lines) ...");
			}
			start = position - limit + 1;
		}
		for (int i = start; i < position; i++) {
			htmlStrings.add(StringUtils.hideComparatorCharacters(getSource().getLine(i)));
			plainStrings.add(getSource().getLine(i));
		}
		final String errorLine = getSource().getLine(position);
		htmlStrings.add("<w:red>" + StringUtils.hideComparatorCharacters(errorLine) + "</w>");
		plainStrings.add(StringUtils.hideComparatorCharacters(errorLine));
		final StringBuilder underscore = new StringBuilder();
		for (int i = 0; i < errorLine.length(); i++) {
			underscore.append("^");
		}
		plainStrings.add(underscore.toString());
		final Collection<String> textErrors = new LinkedHashSet<String>();
		for (ErrorUml er : printedErrors) {
			textErrors.add(er.getError());
		}
		for (String er : textErrors) {
			htmlStrings.add(" <color:red>" + er);
			plainStrings.add(" " + er);
		}
		boolean suggested = false;
		for (ErrorUml er : printedErrors) {
			if (er.hasSuggest()) {
				suggested = true;
			}
		}
		if (suggested) {
			htmlStrings.add(" <color:white><i>Did you mean:");
			for (ErrorUml er : printedErrors) {
				if (er.hasSuggest()) {
					htmlStrings.add("<color:white>"
							+ StringUtils.hideComparatorCharacters(er.getSuggest().getSuggestedLine()));
				}
			}
		}
	}

	private Collection<ErrorUml> getErrors(ErrorUmlType type, List<ErrorUml> all) {
		final Collection<ErrorUml> result = new LinkedHashSet<ErrorUml>();
		for (ErrorUml error : all) {
			if (error.getType() == type) {
				result.add(error);
			}
		}
		return result;
	}

	private int getHigherErrorPosition(ErrorUmlType type, List<ErrorUml> all) {
		int max = Integer.MIN_VALUE;
		for (ErrorUml error : getErrors(type, all)) {
			if (error.getPosition() > max) {
				max = error.getPosition();
			}
		}
		// if (max == Integer.MIN_VALUE) {
		// throw new IllegalStateException();
		// }
		return max;
	}

	private List<ErrorUml> getErrorsAt(int position, ErrorUmlType type, List<ErrorUml> all) {
		final List<ErrorUml> result = new ArrayList<ErrorUml>();
		for (ErrorUml error : getErrors(type, all)) {
			if (error.getPosition() == position && StringUtils.isNotEmpty(error.getError())) {
				result.add(error);
			}
		}
		return result;
	}

	public String getDescription() {
		return "(Error)";
	}

	public void print(PrintStream ps) {
		for (String s : plainStrings) {
			ps.println(StringUtils.showComparatorCharacters(s));
		}
	}

	public final int getHigherErrorPosition() {
		return higherErrorPosition;
	}

	public final Collection<ErrorUml> getErrorsUml() {
		return Collections.unmodifiableCollection(printedErrors);
	}
}
