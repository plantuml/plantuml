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

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter2;

public abstract class PSystemSingleLineFactory extends PSystemAbstractFactory {

	protected abstract AbstractPSystem executeLine(String line);

	protected PSystemSingleLineFactory() {
		super(DiagramType.UML);
	}

	final public Diagram createSystem(UmlSource source) {

		if (source.getTotalLineCount() != 3) {
			return null;
		}
		final IteratorCounter2 it = source.iterator2();
		if (source.isEmpty()) {
			final LineLocation location = it.next().getLocation();
			return buildEmptyError(source, location, it.getTrace());
		}

		final StringLocated startLine = it.next();
		if (StartUtils.isArobaseStartDiagram(startLine.getString()) == false) {
			throw new UnsupportedOperationException();
		}

		if (it.hasNext() == false) {
			return buildEmptyError(source, startLine.getLocation(), it.getTrace());
		}
		final StringLocated s = it.next();
		if (StartUtils.isArobaseEndDiagram(s.getString())) {
			return buildEmptyError(source, s.getLocation(), it.getTrace());
		}
		final AbstractPSystem sys = executeLine(s.getString());
		if (sys == null) {
			final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?", s.getLocation());
			// return PSystemErrorUtils.buildV1(source, err, null);
			return PSystemErrorUtils.buildV2(source, err, null, it.getTrace());
		}
		sys.setSource(source);
		return sys;

	}

}
