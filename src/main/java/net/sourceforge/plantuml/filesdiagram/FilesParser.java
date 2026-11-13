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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Reads the body of a {@code @startfiles} diagram. Several syntaxes are
 * accepted, and can even be mixed:
 *
 * <pre>
 * /src/main/App.java          ' absolute path (the historical syntax)
 *
 * src/                        ' indentation
 *     main/
 *         App.java
 *
 * ├── src/                    ' "tree" output, light or heavy box drawing
 * │   └── App.java
 * ┣━━ src/
 * ┃   ┗━━ App.java
 *
 * |   readme.md               ' Windows "tree /f /a" output
 * +---bin
 * |       a.exe
 * \---etc
 * </pre>
 *
 * The depth of an entry is the column where its name starts, once tabs are
 * expanded and the tree characters removed: this single rule covers every
 * syntax above. Indentation does not need to be regular, an entry is the child
 * of the closest previous entry that starts on its left.
 */
final class FilesParser {

	private static final int TAB_SIZE = 4;

	private static final Pattern NOISE = Pattern.compile("(?i)^(\\.|[a-z]:\\.?|\\d+ director(y|ies)(, \\d+ files?)?"
			+ "|folder path listing.*|volume serial number.*|structure du dossier.*|le numéro de série.*"
			+ "|auflistung der ordnerpfade.*|volumeseriennummer.*|no subfolders exist.*"
			+ "|il n'existe aucun sous-dossier.*)$");

	private final FEntry root = FEntry.createRoot();
	private final FilesIcons icons = new FilesIcons();
	private final List<Level> stack = new ArrayList<>();
	private FEntry lastCreated;

	static class Level {
		private final int column;
		private final FEntry entry;

		Level(int column, FEntry entry) {
			this.column = column;
			this.entry = entry;
		}
	}

	public void parse(Iterator<String> it) {
		while (it.hasNext()) {
			final String line = normalize(it.next());
			final String trimmed = line.trim();
			if (trimmed.length() == 0 || trimmed.startsWith("@startfiles") || trimmed.startsWith("@endfiles"))
				continue;
			if (trimmed.startsWith("'"))
				continue;
			if (trimmed.startsWith("<note>")) {
				addNote(readNote(it));
				continue;
			}
			if (icons.declare(trimmed))
				continue;
			if (NOISE.matcher(trimmed).matches())
				continue;
			parseEntry(line);
		}
	}

	private void parseEntry(String line) {
		final TreePrefix prefix = TreePrefix.parse(line);
		final String text = line.substring(prefix.getColumn()).trim();
		if (text.length() == 0)
			// A line made only of tree characters, as "tree" prints between groups
			return;

		final boolean absolute = text.startsWith("/");
		final FLabel label = FLabel.parse(absolute ? text.substring(1) : text);
		String name = label.getName();
		if (name.length() == 0)
			return;
		if (prefix.isFolder() && name.endsWith("/") == false)
			name = name + "/";

		final int column = prefix.getColumn();
		while (stack.size() > 0 && stack.get(stack.size() - 1).column >= column)
			stack.remove(stack.size() - 1);

		final FEntry parent;
		if (absolute || stack.size() == 0)
			parent = root;
		else
			parent = stack.get(stack.size() - 1).entry;

		final FEntry entry = parent.addRawEntry(name);
		entry.applyLabel(label);
		stack.add(new Level(column, entry));
		lastCreated = entry;
	}

	private void addNote(List<String> note) {
		if (lastCreated == null)
			root.addNote(note);
		else if (lastCreated.isFolder())
			// A note after a folder line goes right under the folder
			lastCreated.addNote(note);
		else
			lastCreated.getParent().addNote(note);
	}

	private static List<String> readNote(Iterator<String> it) {
		final List<String> result = new ArrayList<String>();
		while (it.hasNext()) {
			final String line = it.next();
			if (line.trim().startsWith("</note>"))
				return result;
			result.add(line);
		}
		return result;
	}

	/**
	 * Expands tabs and turns non-breaking spaces (that GNU tree prints after
	 * {@code ├──}) into plain ones.
	 */
	static String normalize(String line) {
		if (line.indexOf('\t') == -1 && line.indexOf(' ') == -1)
			return line;
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < line.length(); i++) {
			final char c = line.charAt(i);
			if (c == '\t')
				do {
					sb.append(' ');
				} while (sb.length() % TAB_SIZE != 0);
			else if (c == ' ')
				sb.append(' ');
			else
				sb.append(c);
		}
		return sb.toString();
	}

	public FEntry getRoot() {
		return root;
	}

	public FilesIcons getIcons() {
		return icons;
	}

}
