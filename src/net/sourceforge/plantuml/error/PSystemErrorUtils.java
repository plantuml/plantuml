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
import java.util.List;

import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.text.StringLocated;

public class PSystemErrorUtils {

	public static PSystemError buildV2(UmlSource source, ErrorUml singleError, List<String> debugLines,
			List<StringLocated> list) {
//		if (source.isEmpty()) {
//			return new PSystemErrorEmpty(source, list, singleError);
//		}
		return new PSystemErrorV2(source, list, singleError);
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
