/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 10298 $
 *
 */
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.StartUtils;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.version.IteratorCounter;

public abstract class PSystemSingleLineFactory extends PSystemAbstractFactory {

	protected abstract AbstractPSystem executeLine(String line);

	protected PSystemSingleLineFactory() {
		super(DiagramType.UML);
	}

	final public Diagram createSystem(UmlSource source) {

		if (source.isEmpty()) {
			return buildEmptyError(source);
		}

		final IteratorCounter it = source.iterator();
		final String startLine = it.next();
		if (StartUtils.isArobaseStartDiagram(startLine) == false) {
			throw new UnsupportedOperationException();
		}

		if (it.hasNext() == false) {
			return buildEmptyError(source);
		}
		final String s = it.next();
		if (StartUtils.isArobaseEndDiagram(s)) {
			return buildEmptyError(source);
		}
		final AbstractPSystem sys = executeLine(s);
		if (sys == null) {
			return new PSystemError(source, new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?",
					it.currentNum() - 1));
		}
		sys.setSource(source);
		return sys;

	}

}
