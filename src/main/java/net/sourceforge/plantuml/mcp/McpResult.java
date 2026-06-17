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
package net.sourceforge.plantuml.mcp;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.warning.Warning;

public class McpResult {

	private boolean ok;
	private String diagramType;
	private int lineCount;
	private final List<String> warnings = new ArrayList<>();
	private int errorLineNumber = -1;
	private String errorLine = "";
	private String errorMessage = "";
	private String errorContext = "";
	private String svg;

	private McpResult() {
	}

	public McpResult(final Diagram diagram) {
		this.lineCount = diagram.getSource().getTotalLineCount();
		if (diagram instanceof PSystemError) {
			this.ok = false;
			final PSystemError error = (PSystemError) diagram;
			final ErrorUml firstError = error.getFirstError();
			if (firstError != null) {
				// getPosition() is 0-based; the public contract exposes a 1-based line.
				this.errorLineNumber = firstError.getPosition() + 1;
				this.errorLine = firstError.getLine().getString() == null ? "" : firstError.getLine().getString();
				this.errorMessage = firstError.getError();
			}
		} else {
			this.ok = true;
			this.diagramType = diagram.getClass().getSimpleName();
		}

		if (diagram instanceof TitledDiagram) {
			final TitledDiagram titledDiagram = (TitledDiagram) diagram;
			for (Warning w : titledDiagram.getWarnings())
				this.warnings.add(w.asSingleLine());
		}
	}

	public McpResult(Diagram diagram, String svg) {
		this(diagram);
		this.svg = svg;
	}

	/**
	 * Builds a result for a diagram that parsed correctly but failed to render. The
	 * structured counters from {@code diagram} are kept, but the result is marked
	 * as not OK and carries the rendering error message.
	 *
	 * @param diagram the parsed (non-error) diagram
	 * @param message the rendering error message
	 * @return a not-OK result describing the rendering failure
	 */
	public static McpResult renderError(Diagram diagram, String message) {
		final McpResult result = new McpResult(diagram);
		result.ok = false;
		result.errorMessage = message;
		return result;
	}

	public static McpResult badInput() {
		final McpResult result = new McpResult();
		result.errorMessage = "Expected exactly one diagram in the source";
		return result;
	}

	public static McpResult noAtStart() {
		final McpResult result = new McpResult();
		result.errorMessage = "The input must start with a @start... directive (for example @startuml)";
		return result;
	}

	/**
	 * @return {@code true} if the source parsed without error
	 */
	public boolean isOk() {
		return ok;
	}

	/**
	 * @return the detected diagram type when OK (e.g. "SequenceDiagram"), or
	 *         {@code null} when not applicable
	 */
	public String getDiagramType() {
		return diagramType;
	}

	/**
	 * @return the number of source lines of the checked diagram
	 */
	public int getLineCount() {
		return lineCount;
	}

	/**
	 * @return the non-fatal warnings, never {@code null} (possibly empty)
	 */
	public List<String> getWarnings() {
		return warnings;
	}

	/**
	 * @return the 1-based line of the error, or -1 when {@link #isOk()} is
	 *         {@code true}
	 */
	public int getErrorLineNumber() {
		return errorLineNumber;
	}

	/**
	 * @return the error message, or {@code null} when {@link #isOk()} is
	 *         {@code true}
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorLine() {
		return errorLine;
	}

	/**
	 * @return the offending source line, or {@code null} when not available
	 */
	public String getErrorContext() {
		return errorContext;
	}

	public String getSvg() {
		return svg;
	}

}
