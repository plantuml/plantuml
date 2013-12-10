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
 * Revision $Revision: 4768 $
 *
 */
package net.sourceforge.plantuml.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StartUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.version.IteratorCounter;
import net.sourceforge.plantuml.version.IteratorCounterImpl;

/**
 * Represents the textual source of some diagram.
 * The source should start with a <code>@startfoo</code> and end with <code>@endfoo</code>.
 * <p>
 * So the diagram does not have to be a UML one.
 * 
 * @author Arnaud Roques
 *
 */
final public class UmlSource {

	final private List<String> source;

	/**
	 * Build the source from a text.
	 * 
	 * @param source	the source of the diagram
	 * @param checkEndingBackslash	<code>true</code> if an ending backslash means that a line has
	 * to be collapsed with the following one.
	 */
	public UmlSource(List<? extends CharSequence> source, boolean checkEndingBackslash) {
		final List<String> tmp = new ArrayList<String>();
		// final DiagramType type =
		// DiagramType.getTypeFromArobaseStart(source.get(0).toString());
		if (checkEndingBackslash) {
			final StringBuilder pending = new StringBuilder();
			for (CharSequence cs : source) {
				final String s = cs.toString();
				if (s.endsWith("\\") && s.endsWith("\\\\") == false) {
					pending.append(s.substring(0, s.length() - 1));
				} else {
					pending.append(s);
					tmp.add(pending.toString());
					pending.setLength(0);
				}
			}
		} else {
			for (CharSequence s : source) {
				tmp.add(s.toString());
			}
		}
		this.source = Collections.unmodifiableList(tmp);
	}

	/**
	 * Retrieve the type of the diagram.
	 * This is based on the first line <code>@startfoo</code>.
	 * 
	 * @return the type of the diagram.
	 */
	public DiagramType getDiagramType() {
		return DiagramType.getTypeFromArobaseStart(source.get(0));
	}

	/**
	 * Allows to iterator over the source.
	 * 
	 * @return a iterator that allow counting line number.
	 */
	public IteratorCounter iterator() {
		return new IteratorCounterImpl(source.iterator());
	}

	/**
	 * Return the source as a single String with <code>\n</code> as line separator.
	 * 
	 * @return the whole diagram source
	 */
	public String getPlainString() {
		final StringBuilder sb = new StringBuilder();
		for (String s : source) {
			sb.append(s);
			sb.append('\r');
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * Return a specific line of the diagram description.
	 * 
	 * @param n		line number, starting at 0
	 * @return
	 */
	public String getLine(int n) {
		return source.get(n);
	}

	/**
	 * Return the number of line in the diagram.
	 * 
	 * @return
	 */
	public int getTotalLineCount() {
		return source.size();
	}

	/**
	 * Check if a source diagram description is empty.
	 * Does not take comment line into account.
	 * 
	 * @return <code>true<code> if the diagram does not contain information.
	 */
	public boolean isEmpty() {
		for (String s : source) {
			if (StartUtils.isArobaseStartDiagram(s)) {
				continue;
			}
			if (StartUtils.isArobaseEndDiagram(s)) {
				continue;
			}
			if (s.matches("\\s*'.*")) {
				continue;
			}
			if (s.trim().length() != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Retrieve the title, if defined in the diagram source.
	 * Never return <code>null</code>.
	 * @return
	 */
	public Display getTitle() {
		final Pattern p = Pattern.compile("(?i)^\\s*title\\s+(.+)$");
		for (String s : source) {
			final Matcher m = p.matcher(s);
			final boolean ok = m.matches();
			if (ok) {
				return Display.asList(m.group(1));
			}
		}
		return Display.emptyList();
	}

}
