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

import net.sourceforge.plantuml.utils.StartUtils;

public enum DiagramType {
	// ::remove folder when __HAXE__
	UML, BPM, DITAA, DOT, PROJECT, JCCKIT, SALT, FLOW, CREOLE, MATH, LATEX, DEFINITION, GANTT, CHRONOLOGY, NW, MINDMAP,
	WBS, WIRE, JSON, GIT, BOARD, YAML, HCL, EBNF, REGEX, FILES, UNKNOWN;

	static public DiagramType getTypeFromArobaseStart(String s) {
		s = s.toLowerCase();
		// if (s.startsWith("@startuml2")) {
		// return UML2;
		// }
		if (StartUtils.startsWithSymbolAnd("startwire", s))
			return WIRE;

		if (StartUtils.startsWithSymbolAnd("startbpm", s))
			return BPM;

		if (StartUtils.startsWithSymbolAnd("startuml", s))
			return UML;

		if (StartUtils.startsWithSymbolAnd("startdot", s))
			return DOT;

		// ::comment when __CORE__ or __MIT__ or __EPL__ or __BSD__ or __ASL__ or
		// __LGPL__
		if (StartUtils.startsWithSymbolAnd("startjcckit", s))
			return JCCKIT;
		// ::done

		// ::comment when __CORE__ or __MIT__ or __EPL__ or __BSD__ or __ASL__
		if (StartUtils.startsWithSymbolAnd("startditaa", s))
			return DITAA;
		// ::done

		if (StartUtils.startsWithSymbolAnd("startproject", s))
			return PROJECT;

		if (StartUtils.startsWithSymbolAnd("startsalt", s))
			return SALT;

		if (StartUtils.startsWithSymbolAnd("startflow", s))
			return FLOW;

		if (StartUtils.startsWithSymbolAnd("startcreole", s))
			return CREOLE;

		if (StartUtils.startsWithSymbolAnd("startmath", s))
			return MATH;

		if (StartUtils.startsWithSymbolAnd("startlatex", s))
			return LATEX;

		if (StartUtils.startsWithSymbolAnd("startdef", s))
			return DEFINITION;

		if (StartUtils.startsWithSymbolAnd("startgantt", s))
			return GANTT;

		if (StartUtils.startsWithSymbolAnd("startnwdiag", s))
			return NW;

		if (StartUtils.startsWithSymbolAnd("startmindmap", s))
			return MINDMAP;

		if (StartUtils.startsWithSymbolAnd("startwbs", s))
			return WBS;

		if (StartUtils.startsWithSymbolAnd("startjson", s))
			return JSON;

		if (StartUtils.startsWithSymbolAnd("startgit", s))
			return GIT;

		if (StartUtils.startsWithSymbolAnd("startboard", s))
			return BOARD;

		if (StartUtils.startsWithSymbolAnd("startyaml", s))
			return YAML;

		if (StartUtils.startsWithSymbolAnd("starthcl", s))
			return HCL;

		if (StartUtils.startsWithSymbolAnd("startebnf", s))
			return EBNF;

		if (StartUtils.startsWithSymbolAnd("startregex", s))
			return REGEX;

		if (StartUtils.startsWithSymbolAnd("startfiles", s))
			return FILES;

		if (StartUtils.startsWithSymbolAnd("startchronology", s))
			return CHRONOLOGY;

		return UNKNOWN;
	}
}
