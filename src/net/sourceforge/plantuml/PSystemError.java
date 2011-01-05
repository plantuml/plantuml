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
 * Revision $Revision: 5794 $
 */
package net.sourceforge.plantuml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import net.sourceforge.plantuml.graphic.GraphicStrings;

public class PSystemError extends AbstractPSystem {

	private final List<ErrorUml> errorsUml = new ArrayList<ErrorUml>();
	private final List<String> htmlStrings = new ArrayList<String>();
	private final List<String> plainStrings = new ArrayList<String>();
	private final int higherErrorPosition;
	private final Collection<String> errs;

	public PSystemError(UmlSource source, List<ErrorUml> errorUml) {
		this.errorsUml.addAll(errorUml);
		this.setSource(source);

		final Collection<ErrorUml> executions = getErrors(ErrorUmlType.EXECUTION_ERROR);
		if (executions.size() > 0) {
			higherErrorPosition = getHigherErrorPosition(ErrorUmlType.EXECUTION_ERROR);
			errs = getErrorsAt(higherErrorPosition, ErrorUmlType.EXECUTION_ERROR);
			appendSource(higherErrorPosition, errs);
		} else {
			higherErrorPosition = getHigherErrorPosition(ErrorUmlType.SYNTAX_ERROR);
			errs = getErrorsAt(higherErrorPosition, ErrorUmlType.SYNTAX_ERROR);
			if (errs.size() != 1) {
				throw new UnsupportedOperationException(errs.toString());
			}
			appendSource(higherErrorPosition, errs);
		}

	}

	public PSystemError(UmlSource source, ErrorUml... errorUml) {
		this(source, Arrays.asList(errorUml));
	}

	public List<File> createFiles(File suggestedFile, FileFormatOption fileFormat) throws IOException, InterruptedException {
		if (suggestedFile.exists() && suggestedFile.isDirectory()) {
			throw new IllegalArgumentException("File is a directory "+suggestedFile);
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(suggestedFile);
			getPngError().writeImage(os, getMetadata(), fileFormat);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		return Arrays.asList(suggestedFile);
	}

	public void createFile(OutputStream os, int index, FileFormatOption fileFormat) throws IOException {
		getPngError().writeImage(os, getMetadata(), fileFormat);
	}

	public GraphicStrings getPngError() throws IOException {
		return new GraphicStrings(htmlStrings);
	}

	private void appendSource(int position, Collection<String> errs) {
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
		for (String er : errs) {
			htmlStrings.add(" <font color=red>" + er);
			plainStrings.add(" " + er);
		}
	}

	private Collection<ErrorUml> getErrors(ErrorUmlType type) {
		final Collection<ErrorUml> result = new LinkedHashSet<ErrorUml>();
		for (ErrorUml error : errorsUml) {
			if (error.getType() == type) {
				result.add(error);
			}
		}
		return result;
	}

	private int getHigherErrorPosition(ErrorUmlType type) {
		int max = Integer.MIN_VALUE;
		for (ErrorUml error : getErrors(type)) {
			if (error.getPosition() > max) {
				max = error.getPosition();
			}
		}
		if (max == Integer.MIN_VALUE) {
			throw new IllegalStateException();
		}
		return max;
	}

	private Collection<String> getErrorsAt(int position, ErrorUmlType type) {
		final Collection<String> result = new TreeSet<String>();
		for (ErrorUml error : getErrors(type)) {
			if (error.getPosition() == position && StringUtils.isNotEmpty(error.getError())) {
				result.add(error.getError());
			}
		}
		return result;
	}

	public String getDescription() {
		return "(Error)";
	}

	public final List<ErrorUml> getErrorsUml() {
		return Collections.unmodifiableList(errorsUml);
	}

	public void print(PrintStream ps) {
		for (String s : plainStrings) {
			ps.println(StringUtils.showComparatorCharacters(s));
		}
	}

	public final int getHigherErrorPosition() {
		return higherErrorPosition;
	}

	public final Collection<String> getErrs() {
		return Collections.unmodifiableCollection(errs);
	}
}
