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
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.style.SName;

public enum UmlDiagramType {
	SEQUENCE, STATE, CLASS, OBJECT, ACTIVITY, DESCRIPTION, COMPOSITE, FLOW, TIMING, BPM, NWDIAG, MINDMAP, WBS, WIRE,
	HELP, GANTT, SALT, JSON, GIT, BOARD, YAML, HCL, EBNF, REGEX, FILES, CHRONOLOGY, CHEN_EER, NASSI;

	public SName getStyleName() {
		if (this == SEQUENCE)
			return SName.sequenceDiagram;

		if (this == STATE)
			return SName.stateDiagram;

		if (this == CLASS)
			return SName.classDiagram;

		if (this == OBJECT)
			return SName.objectDiagram;

		if (this == ACTIVITY)
			return SName.activityDiagram;

		if (this == DESCRIPTION)
			return SName.componentDiagram;

		if (this == COMPOSITE)
			return SName.componentDiagram;

		if (this == MINDMAP)
			return SName.mindmapDiagram;

		if (this == WBS)
			return SName.wbsDiagram;

		if (this == GANTT)
			return SName.ganttDiagram;

		if (this == SALT)
			return SName.saltDiagram;

		if (this == YAML)
			return SName.yamlDiagram;

		if (this == HCL)
			return SName.yamlDiagram;

		if (this == JSON)
			return SName.jsonDiagram;

		if (this == TIMING)
			return SName.timingDiagram;

		if (this == NWDIAG)
			return SName.nwdiagDiagram;

		if (this == EBNF)
			return SName.ebnf;

		if (this == REGEX)
			return SName.regex;

		if (this == CHRONOLOGY)
			return SName.ganttDiagram;

		if (this == NASSI)
			return SName.nassiDiagram;

		return SName.activityDiagram;
	}
}
