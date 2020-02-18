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
package net.sourceforge.plantuml.core;

import net.sourceforge.plantuml.utils.StartUtils;

public enum DiagramType {
	UML, BPM, DITAA, DOT, PROJECT, JCCKIT, SALT, FLOW, CREOLE, JUNGLE, CUTE, MATH, LATEX, DEFINITION, GANTT, NW, MINDMAP, WBS, WIRE, UNKNOWN;

	static public DiagramType getTypeFromArobaseStart(String s) {
		s = s.toLowerCase();
		// if (s.startsWith("@startuml2")) {
		// return UML2;
		// }
		if (StartUtils.startsWithSymbolAnd("startwire", s)) {
			return WIRE;
		}
		if (StartUtils.startsWithSymbolAnd("startbpm", s)) {
			return BPM;
		}
		if (StartUtils.startsWithSymbolAnd("startuml", s)) {
			return UML;
		}
		if (StartUtils.startsWithSymbolAnd("startdot", s)) {
			return DOT;
		}
		if (StartUtils.startsWithSymbolAnd("startjcckit", s)) {
			return JCCKIT;
		}
		if (StartUtils.startsWithSymbolAnd("startditaa", s)) {
			return DITAA;
		}
		if (StartUtils.startsWithSymbolAnd("startproject", s)) {
			return PROJECT;
		}
		if (StartUtils.startsWithSymbolAnd("startsalt", s)) {
			return SALT;
		}
		if (StartUtils.startsWithSymbolAnd("startflow", s)) {
			return FLOW;
		}
		if (StartUtils.startsWithSymbolAnd("startcreole", s)) {
			return CREOLE;
		}
		if (StartUtils.startsWithSymbolAnd("starttree", s)) {
			return JUNGLE;
		}
		if (StartUtils.startsWithSymbolAnd("startcute", s)) {
			return CUTE;
		}
		if (StartUtils.startsWithSymbolAnd("startmath", s)) {
			return MATH;
		}
		if (StartUtils.startsWithSymbolAnd("startlatex", s)) {
			return LATEX;
		}
		if (StartUtils.startsWithSymbolAnd("startdef", s)) {
			return DEFINITION;
		}
		if (StartUtils.startsWithSymbolAnd("startgantt", s)) {
			return GANTT;
		}
		if (StartUtils.startsWithSymbolAnd("startnwdiag", s)) {
			return NW;
		}
		if (StartUtils.startsWithSymbolAnd("startmindmap", s)) {
			return MINDMAP;
		}
		if (StartUtils.startsWithSymbolAnd("startwbs", s)) {
			return WBS;
		}
		return UNKNOWN;
	}
}
