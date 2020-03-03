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
package net.sourceforge.plantuml.project.lang;

import java.util.Arrays;
import java.util.Collection;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Task;

public class SubjectTask implements SubjectPattern {

	public Collection<VerbPattern> getVerbs() {
		return Arrays.<VerbPattern>asList(new VerbLasts(), new VerbTaskStarts(), new VerbTaskStartsAbsolute(),
				new VerbHappens(), new VerbEnds(), new VerbTaskEndsAbsolute(), new VerbIsColored(), new VerbIsDeleted(),
				new VerbIsForTask(), new VerbLinksTo());
	}

	public IRegex toRegex() {
		return new RegexConcat( //
				new RegexLeaf("THEN", "(then[%s]+)?"), //
				new RegexLeaf("SUBJECT", "\\[([^\\[\\]]+?)\\](?:[%s]+as[%s]+\\[([^\\[\\]]+?)\\])?"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("on"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("RESOURCE", "((?:\\{[^{}]+\\}[%s]*)+)") //
						)) //
		);
	}

	public Subject getSubject(GanttDiagram project, RegexResult arg) {
		final String s = arg.get("SUBJECT", 0);
		final String shortName = arg.get("SUBJECT", 1);
		final String then = arg.get("THEN", 0);
		final String resource = arg.get("RESOURCE", 0);
		final Task result = project.getOrCreateTask(s, shortName, then != null);
		if (result == null) {
			throw new IllegalStateException();
		}
		if (resource != null) {
			for (final StringTokenizer st = new StringTokenizer(resource, "{}"); st.hasMoreTokens();) {
				final String part = st.nextToken().trim();
				if (part.length() > 0) {
					project.affectResource(result, part);
				}
			}

		}
		return result;
	}
}
