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
 */
package net.sourceforge.plantuml.error;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.UgDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.directdot.PSystemDotFactory;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.text.StringLocated;

public class PSystemErrorUtils {

	public static UgDiagram checkBasicError(Collection<DiagramType> diagramTypes, List<StringLocated> source,
			PreprocessingArtifact preprocessing, final UmlSource umlSource) {

		if (diagramTypes.contains(DiagramType.UNKNOWN))
			return new PSystemUnsupported(umlSource, preprocessing);

		if (diagramTypes.contains(DiagramType.SEQUENCE) && source.size() > 1) {
			// Detect misuse of @startuml for DOT or DITAA diagrams and
			// return a helpful error
			final String secondLine = source.get(1).getString();
			if (!TeaVM.isTeaVM() && PSystemDotFactory.isGraphvizDotHeader(secondLine)) {
				final ErrorUml error = new ErrorUml(ErrorUmlType.EXECUTION_ERROR,
						"This looks like a DOT diagram. Please use @startdot instead of @startuml.", 100, source.get(1),
						DiagramType.SEQUENCE);

				return PSystemErrorUtils.buildV2(umlSource, error, Collections.<String>emptyList(), source,
						preprocessing);

			} else if (!TeaVM.isTeaVM() && secondLine.trim().equals("ditaa")) {
				final ErrorUml error = new ErrorUml(ErrorUmlType.EXECUTION_ERROR,
						"This looks like a DITAA diagram. Please use @startditaa instead of @startuml.", 100,
						source.get(1), DiagramType.SEQUENCE);

				return PSystemErrorUtils.buildV2(umlSource, error, Collections.<String>emptyList(), source,
						preprocessing);

			} else if (!TeaVM.isTeaVM() && secondLine.trim().equals("salt")) {
				final ErrorUml error = new ErrorUml(ErrorUmlType.EXECUTION_ERROR,
						"This looks like a salt diagram. Please use @startsalt instead of @startuml.", 100,
						source.get(1), DiagramType.SEQUENCE);

				return PSystemErrorUtils.buildV2(umlSource, error, Collections.<String>emptyList(), source,
						preprocessing);

			} else if (secondLine.trim().equals("nwdiag {")) {
				final ErrorUml error = new ErrorUml(ErrorUmlType.EXECUTION_ERROR,
						"This looks like a network diagram. Please use @startnwdiag instead of @startuml.", 100,
						source.get(1), DiagramType.SEQUENCE);

				return PSystemErrorUtils.buildV2(umlSource, error, Collections.<String>emptyList(), source,
						preprocessing);
			}
		}

		return null;
	}

	public static PSystemError buildV2(UmlSource source, ErrorUml singleError, List<String> debugLines,
			List<StringLocated> list, PreprocessingArtifact preprocessing) {
		return new PSystemErrorV2(source, list, singleError, preprocessing, null);
	}

	public static PSystemError buildV2(UmlSource source, ErrorUml singleError, List<String> debugLines,
			List<StringLocated> list, PreprocessingArtifact preprocessing, Throwable rootCause) {
		return new PSystemErrorV2(source, list, singleError, preprocessing, rootCause);
	}

	public static PSystemError merge(Collection<PSystemError> ps) {
		if (ps.size() == 0)
			throw new IllegalStateException();

		UmlSource source = null;
		final List<ErrorUml> errors = new ArrayList<>();
		// final List<String> debugs = new ArrayList<>();
		final List<PSystemErrorV2> errorsV2 = new ArrayList<>();
		for (PSystemError system : ps) {
			if (system == null)
				continue;

			if (system.getSource() != null && source == null)
				source = system.getSource();

			errors.addAll(system.getErrorsUml());
			if (system instanceof PSystemErrorV2)
				errorsV2.add((PSystemErrorV2) system);
		}
		if (source == null)
			throw new IllegalStateException();

		if (errorsV2.size() > 0)
			return mergeV2(errorsV2);

		throw new IllegalStateException();
	}

	private static PSystemErrorV2 mergeV2(List<PSystemErrorV2> errorsV2) {
		PSystemErrorV2 result = null;
		for (PSystemErrorV2 err : errorsV2)
			if (result == null || result.score() < err.score())
				result = err;

		return result;
	}

	public static boolean isDiagramError(Class<? extends Diagram> type) {
		return PSystemError.class.isAssignableFrom(type);
	}

}
