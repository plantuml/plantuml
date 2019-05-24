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
package net.sourceforge.plantuml.error;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;

public class PSystemErrorUtils {

	// public static AbstractPSystemError buildV1(UmlSource source, ErrorUml singleError, List<String> debugLines) {
	// return new PSystemErrorV1(source, singleError, debugLines);
	// }

	public static PSystemError buildV2(UmlSource source, ErrorUml singleError, List<String> debugLines,
			List<StringLocated> list) {
		// return new PSystemErrorV1(source, singleError, debugLines);
		return new PSystemErrorV2(source, list, singleError);
	}

	public static PSystemError merge(Collection<PSystemError> ps) {
		UmlSource source = null;
		final List<ErrorUml> errors = new ArrayList<ErrorUml>();
		// final List<String> debugs = new ArrayList<String>();
		final List<PSystemErrorV2> errorsV2 = new ArrayList<PSystemErrorV2>();
		for (PSystemError system : ps) {
			if (system == null) {
				continue;
			}
			if (system.getSource() != null && source == null) {
				source = system.getSource();
			}
			errors.addAll(system.getErrorsUml());
			// if (system instanceof PSystemErrorV1) {
			// debugs.addAll(((PSystemErrorV1) system).debugLines);
			// if (((PSystemErrorV1) system).debugLines.size() > 0) {
			// debugs.add("-");
			// }
			// }
			if (system instanceof PSystemErrorV2) {
				errorsV2.add((PSystemErrorV2) system);
			}
		}
		if (source == null) {
			throw new IllegalStateException();
		}
		if (errorsV2.size() > 0) {
			return mergeV2(errorsV2);
		}
		throw new IllegalStateException();
		// return new PSystemErrorV1(source, errors, debugs);
	}

	private static PSystemErrorV2 mergeV2(List<PSystemErrorV2> errorsV2) {
		PSystemErrorV2 result = null;
		for (PSystemErrorV2 err : errorsV2) {
			if (result == null || result.size() < err.size()) {
				result = err;
			}
		}
		return result;
	}

	public static boolean isDiagramError(Class<? extends Diagram> type) {
		return PSystemError.class.isAssignableFrom(type);
		// return type == PSystemErrorV1.class;
	}

}
