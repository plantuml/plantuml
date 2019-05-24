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
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.error.PSystemErrorUtils;

public abstract class PSystemAbstractFactory implements PSystemFactory {

	public static final String EMPTY_DESCRIPTION = "Empty description";
	private final DiagramType type;

	protected PSystemAbstractFactory(DiagramType type) {
		this.type = type;
	}

	final protected PSystemError buildEmptyError(UmlSource source, LineLocation lineLocation,
			List<StringLocated> trace) {
		final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, EMPTY_DESCRIPTION, /* 1, */lineLocation);
		// final AbstractPSystemError result = PSystemErrorUtils.buildV1(source, err, null);
		final PSystemError result = PSystemErrorUtils.buildV2(source, err, null, trace);
		result.setSource(source);
		return result;
	}

	final protected PSystemError buildExecutionError(UmlSource source, String stringError,
			LineLocation lineLocation, List<StringLocated> trace) {
		final ErrorUml err = new ErrorUml(ErrorUmlType.EXECUTION_ERROR, stringError, /* 1, */
		lineLocation);
		final PSystemError result = PSystemErrorUtils.buildV2(source, err, null, trace);
		result.setSource(source);
		return result;
	}

	final public DiagramType getDiagramType() {
		return type;
	}

}
