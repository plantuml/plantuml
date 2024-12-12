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
package net.sourceforge.plantuml.command;

import java.util.Map;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter2;

public abstract class PSystemBasicFactory<P extends AbstractPSystem> extends PSystemAbstractFactory {

	public PSystemBasicFactory(DiagramType diagramType) {
		super(diagramType);
	}

	public PSystemBasicFactory() {
		this(DiagramType.UML);
	}

	public abstract P executeLine(UmlSource source, P system, String line);

	public abstract P initDiagram(UmlSource source, String startLine);

	private boolean isEmptyLine(StringLocated result) {
		return result.getTrimmed().getString().length() == 0;
	}

	@Override
	final public Diagram createSystem(UmlSource source, Map<String, String> skinMap) {
		source = source.removeInitialSkinparam();
		final IteratorCounter2 it = source.iterator2();
		final StringLocated startLine = it.next();
		P system = initDiagram(source, startLine.getString());
		boolean first = true;
		while (it.hasNext()) {
			final StringLocated s = it.next();
			if (first && s != null && isEmptyLine(s))
				continue;

			first = false;
			if (StartUtils.isArobaseEndDiagram(s.getString())) {
				if (source.getTotalLineCount() == 2 && source.isStartDef() == false)
					return buildEmptyError(source, s.getLocation(), it.getTrace());

				return system;
			}
			system = executeLine(source, system, s.getString());
			if (system == null) {
				final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?", 0, s.getLocation(), getUmlDiagramType());
				// return PSystemErrorUtils.buildV1(source, err, null);
				return PSystemErrorUtils.buildV2(source, err, null, it.getTrace());
			}
		}
		return system;
	}

}
