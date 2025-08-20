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

// To be merged with UmlDiagramType
public enum DiagramType {
	// ::remove folder when __HAXE__
	UML, BPM, DITAA, DOT, PROJECT, JCCKIT, SALT, FLOW, CREOLE, MATH, LATEX, DEFINITION, GANTT, CHRONOLOGY, NW, MINDMAP,
	WBS, WIRE, JSON, GIT, BOARD, YAML, HCL, EBNF, REGEX, FILES, CHEN_EER, UNKNOWN;

	static public DiagramType getTypeFromArobaseStart(String text) {
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);

			if (Character.isWhitespace(c))
				continue;

			if (c != '@' && c != '\\')
				return UNKNOWN;

			final int pos = i + 1;

			if (text.length() - pos < 5 || check("start", text, pos) == false)
				return UNKNOWN;

			final int p = pos + 5;
			if (p >= text.length())
				return UNKNOWN;

			return getType(text, p);
		}

		return UNKNOWN;
	}

	private static DiagramType getType(String text, final int p) {
		switch (Character.toLowerCase(text.charAt(p))) {

		case 'b':
			if (check("bpm", text, p))
				return BPM;
			if (check("board", text, p))
				return BOARD;
			return UNKNOWN;

		case 'c':
			if (check("creole", text, p))
				return CREOLE;
			if (check("chronology", text, p))
				return CHRONOLOGY;
			if (check("chen", text, p))
				return CHEN_EER;
			return UNKNOWN;

		case 'd':
			if (check("dot", text, p))
				return DOT;
			// ::comment when __CORE__ or __MIT__ or __EPL__ or __BSD__ or __ASL__
			if (check("ditaa", text, p))
				return DITAA;
			// ::done
			if (check("def", text, p))
				return DEFINITION;
			return UNKNOWN;

		case 'e':
			if (check("ebnf", text, p))
				return EBNF;
			return UNKNOWN;

		case 'f':
			if (check("flow", text, p))
				return FLOW;
			if (check("files", text, p))
				return FILES;
			return UNKNOWN;

		case 'g':
			if (check("gantt", text, p))
				return GANTT;
			if (check("git", text, p))
				return GIT;
			return UNKNOWN;

		case 'h':
			if (check("hcl", text, p))
				return HCL;
			return UNKNOWN;

		case 'j':
			// ::comment when __CORE__ or __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__
			if (check("jcckit", text, p))
				return JCCKIT;
			// ::done
			if (check("json", text, p))
				return JSON;
			return UNKNOWN;

		case 'l':
			if (check("latex", text, p))
				return LATEX;
			return UNKNOWN;

		case 'm':
			if (check("math", text, p))
				return MATH;
			if (check("mindmap", text, p))
				return MINDMAP;
			return UNKNOWN;

		case 'n':
			if (check("nwdiag", text, p))
				return NW;
			return UNKNOWN;

		case 'p':
			if (check("project", text, p))
				return PROJECT;
			return UNKNOWN;

		case 'r':
			if (check("regex", text, p))
				return REGEX;
			return UNKNOWN;

		case 's':
			if (check("salt", text, p))
				return SALT;
			return UNKNOWN;

		case 'u':
			if (check("uml", text, p))
				return UML;
			return UNKNOWN;

		case 'w':
			if (check("wire", text, p))
				return WIRE;
			if (check("wbs", text, p))
				return WBS;
			return UNKNOWN;

		case 'y':
			if (check("yaml", text, p))
				return YAML;
			return UNKNOWN;

		default:
			return UNKNOWN;
		}
	}

	static boolean check(String key, String text, int p) {
		return text.regionMatches(true, p, key, 0, key.length());
	}

}
