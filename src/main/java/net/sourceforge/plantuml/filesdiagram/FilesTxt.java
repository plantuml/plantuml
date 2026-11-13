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
 */
package net.sourceforge.plantuml.filesdiagram;

import java.util.ArrayList;
import java.util.List;

/**
 * Text rendering (-tutxt and -tatxt), in the style of the {@code tree} command:
 *
 * <pre>
 * src/
 * ├── main/
 * │   └── App.java  ## the entry point
 * └── test/
 * </pre>
 *
 * Folders get a trailing {@code /} and descriptions keep their {@code ##}, so
 * that the output can be pasted back into a {@code @startfiles} diagram.
 */
final class FilesTxt {

	private final String branch;
	private final String lastBranch;
	private final String bar;
	private final String blank = "    ";
	private final boolean unicode;

	FilesTxt(boolean unicode) {
		this.unicode = unicode;
		this.branch = unicode ? "├── " : "|-- ";
		this.lastBranch = unicode ? "└── " : "`-- ";
		this.bar = unicode ? "│   " : "|   ";
	}

	public List<String> getLines(FEntry root) {
		final List<String> result = new ArrayList<>();
		for (FEntry child : root)
			addEntry(result, child, "", "");
		return result;
	}

	private void addEntry(List<String> result, FEntry entry, String firstPrefix, String nextPrefix) {
		if (entry.isNote())
			addNote(result, entry.getNote(), firstPrefix);
		else
			result.add(firstPrefix + getText(entry));

		final FEntry last = entry.getLastEntryChild();
		boolean afterLast = false;
		for (FEntry child : entry) {
			if (child.isNote()) {
				addEntry(result, child, nextPrefix + (afterLast || last == null ? blank : bar), null);
				continue;
			}
			final boolean isLast = child == last;
			addEntry(result, child, nextPrefix + (isLast ? lastBranch : branch), nextPrefix + (isLast ? blank : bar));
			if (isLast)
				afterLast = true;
		}
	}

	private String getText(FEntry entry) {
		final StringBuilder sb = new StringBuilder(entry.getName());
		if (entry.isFolder())
			sb.append('/');
		if (entry.getDescription() != null && entry.getDescription().length() > 0)
			sb.append("  ## ").append(entry.getDescription());
		return sb.toString();
	}

	private void addNote(List<String> result, List<String> note, String prefix) {
		int width = 0;
		for (String s : note)
			width = Math.max(width, s.length());
		final String h = repeat(unicode ? '─' : '-', width + 2);
		final String v = unicode ? "│" : "|";
		result.add(prefix + (unicode ? "┌" : "+") + h + (unicode ? "┐" : "+"));
		for (String s : note)
			result.add(prefix + v + " " + s + repeat(' ', width - s.length()) + " " + v);
		result.add(prefix + (unicode ? "└" : "+") + h + (unicode ? "┘" : "+"));
	}

	private static String repeat(char c, int n) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
			sb.append(c);
		return sb.toString();
	}

}
