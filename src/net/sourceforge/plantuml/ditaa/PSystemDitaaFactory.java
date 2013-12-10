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
 */
package net.sourceforge.plantuml.ditaa;

import net.sourceforge.plantuml.command.PSystemBasicFactory;
import net.sourceforge.plantuml.core.DiagramType;

public class PSystemDitaaFactory extends PSystemBasicFactory<PSystemDitaa> {

	// private StringBuilder data;
	// // -E,--no-separation
	// private boolean performSeparationOfCommonEdges;
	//
	// // -S,--no-shadows
	// private boolean dropShadows;

	public PSystemDitaaFactory(DiagramType diagramType) {
		super(diagramType);
	}

	public PSystemDitaa init(String startLine) {
		boolean performSeparationOfCommonEdges = true;
		if (startLine != null && (startLine.contains("-E") || startLine.contains("--no-separation"))) {
			performSeparationOfCommonEdges = false;
		}
		boolean dropShadows = true;
		if (startLine != null && (startLine.contains("-S") || startLine.contains("--no-shadows"))) {
			dropShadows = false;
		}
		if (getDiagramType() == DiagramType.UML) {
			return null;
		} else if (getDiagramType() == DiagramType.DITAA) {
			return new PSystemDitaa("", performSeparationOfCommonEdges, dropShadows);
		} else {
			throw new IllegalStateException(getDiagramType().name());
		}
	}

	@Override
	public PSystemDitaa executeLine(PSystemDitaa system, String line) {
		if (system == null && (line.equals("ditaa") || line.startsWith("ditaa("))) {
			boolean performSeparationOfCommonEdges = true;
			if (line.contains("-E") || line.contains("--no-separation")) {
				performSeparationOfCommonEdges = false;
			}
			boolean dropShadows = true;
			if (line.contains("-S") || line.contains("--no-shadows")) {
				dropShadows = false;
			}
			return new PSystemDitaa("", performSeparationOfCommonEdges, dropShadows);
		}
		if (system == null) {
			return null;
		}
		return system.add(line);
	}

}
