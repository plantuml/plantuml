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
package net.sourceforge.plantuml.core;

import java.util.Collection;
import java.util.EnumSet;

import net.sourceforge.plantuml.style.SName;

public enum DiagramType {

	SEQUENCE, STATE, CLASS, OBJECT, ACTIVITY, DESCRIPTION, COMPOSITE, TIMING, HELP, BPM, DITAA, DOT, JCCKIT, SALT, FLOW,
	CREOLE, MATH, LATEX, DEFINITION, GANTT, CHRONOLOGY, NWDIAG, MINDMAP, WBS, WIRE, JSON, GIT, BOARD, YAML, HCL, EBNF,
	REGEX, FILES, CHEN_EER, CHART, PACKET, SPRITES, UNKNOWN;

	public boolean isLegacyUML() {
		switch (this) {
		case SEQUENCE:
		case STATE:
		case CLASS:
		case OBJECT:
		case ACTIVITY:
		case DESCRIPTION:
		case COMPOSITE:
		case TIMING:
		case HELP:
		case SPRITES:
			return true;
		default:
			return false;
		}
	}

	static public Collection<DiagramType> getTypesFromArobaseStart(String text) {
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);

			if (Character.isWhitespace(c))
				continue;

			if (c != '@' && c != '\\')
				return EnumSet.of(UNKNOWN);

			final int pos = i + 1;

			if (text.length() - pos < 5 || check("start", text, pos) == false)
				return EnumSet.of(UNKNOWN);

			final int p = pos + 5;
			if (p >= text.length())
				return EnumSet.of(UNKNOWN);

			return getTypes(text, p);
		}

		return EnumSet.of(UNKNOWN);
	}

	private static Collection<DiagramType> getTypes(String text, final int p) {
		switch (Character.toLowerCase(text.charAt(p))) {

		case 'b':
			if (check("bpm", text, p))
				return EnumSet.of(BPM);
			if (check("board", text, p))
				return EnumSet.of(BOARD);
			return EnumSet.of(UNKNOWN);

		case 'c':
			if (check("chart", text, p))
				return EnumSet.of(CHART);
			if (check("creole", text, p))
				return EnumSet.of(CREOLE);
			if (check("chronology", text, p))
				return EnumSet.of(CHRONOLOGY);
			if (check("chen", text, p))
				return EnumSet.of(CHEN_EER);
			return EnumSet.of(UNKNOWN);

		case 'd':
			if (check("dot", text, p))
				return EnumSet.of(DOT);
			// ::comment when __MIT__ __EPL__ __BSD__ __ASL__
			if (check("ditaa", text, p))
				return EnumSet.of(DITAA);
			// ::done
			if (check("def", text, p))
				return EnumSet.of(DEFINITION);
			return EnumSet.of(UNKNOWN);

		case 'e':
			if (check("ebnf", text, p))
				return EnumSet.of(EBNF);
			return EnumSet.of(UNKNOWN);

		case 'f':
			if (check("flow", text, p))
				return EnumSet.of(FLOW);
			if (check("files", text, p))
				return EnumSet.of(FILES);
			return EnumSet.of(UNKNOWN);

		case 'g':
			if (check("gantt", text, p))
				return EnumSet.of(GANTT);
			if (check("git", text, p))
				return EnumSet.of(GIT);
			return EnumSet.of(UNKNOWN);

		case 'h':
			if (check("hcl", text, p))
				return EnumSet.of(HCL);
			return EnumSet.of(UNKNOWN);

		case 'j':
			// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__
			if (check("jcckit", text, p))
				return EnumSet.of(JCCKIT);
			// ::done
			if (check("json", text, p))
				return EnumSet.of(JSON);
			return EnumSet.of(UNKNOWN);

		case 'l':
			if (check("latex", text, p))
				return EnumSet.of(LATEX);
			return EnumSet.of(UNKNOWN);

		case 'm':
			if (check("math", text, p))
				return EnumSet.of(MATH);
			if (check("mindmap", text, p))
				return EnumSet.of(MINDMAP);
			return EnumSet.of(UNKNOWN);

		case 'n':
			if (check("nwdiag", text, p))
				return EnumSet.of(NWDIAG);
			return EnumSet.of(UNKNOWN);

		case 'p':
			if (check("project", text, p))
				return EnumSet.of(GANTT);
			if (check("packetdiag", text, p))
				return EnumSet.of(PACKET);
			return EnumSet.of(UNKNOWN);

		case 'r':
			if (check("regex", text, p))
				return EnumSet.of(REGEX);
			return EnumSet.of(UNKNOWN);

		case 's':
			if (check("salt", text, p))
				return EnumSet.of(SALT);
			if (check("sprites", text, p))
				return EnumSet.of(SPRITES);
			return EnumSet.of(UNKNOWN);

		case 'u':
			if (check("uml", text, p))
				return EnumSet.of(SEQUENCE, STATE, CLASS, OBJECT, ACTIVITY, DESCRIPTION, COMPOSITE, TIMING, HELP,
						SPRITES);
			return EnumSet.of(UNKNOWN);

		case 'w':
			if (check("wire", text, p))
				return EnumSet.of(WIRE);
			if (check("wbs", text, p))
				return EnumSet.of(WBS);
			return EnumSet.of(UNKNOWN);

		case 'y':
			if (check("yaml", text, p))
				return EnumSet.of(YAML);
			return EnumSet.of(UNKNOWN);

		default:
			return EnumSet.of(UNKNOWN);
		}
	}

	static boolean check(String key, String text, int p) {
		return text.regionMatches(true, p, key, 0, key.length());
	}

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

		if (this == CHART)
			return SName.chartDiagram;

		if (this == PACKET)
			return SName.packetdiagDiagram;

		return SName.activityDiagram;
	}

	public String humanReadableName() {
		if (this == DESCRIPTION)
			return "component";
		return name().toLowerCase();
	}

}
