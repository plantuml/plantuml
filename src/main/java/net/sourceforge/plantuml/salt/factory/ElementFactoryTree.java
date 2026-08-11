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
package net.sourceforge.plantuml.salt.factory;

import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.salt.DataSource;
import net.sourceforge.plantuml.salt.SaltDictionary;
import net.sourceforge.plantuml.salt.Terminated;
import net.sourceforge.plantuml.salt.Terminator;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.ElementTree;
import net.sourceforge.plantuml.salt.element.TableStrategy;

public class ElementFactoryTree extends AbstractElementFactoryComplex {

	public ElementFactoryTree(DataSource dataSource, SaltDictionary dictionary) {
		super(dataSource, dictionary);
	}

	public Terminated<Element> create() {
		if (ready() == false) {
			throw new IllegalStateException();
		}
		final String header = getDataSource().next().getElement();
		final String textT = getDataSource().next().getElement();
		TableStrategy strategy = TableStrategy.DRAW_NONE;
		if (textT.length() == 2) {
			strategy = TableStrategy.fromChar(textT.charAt(1));
		}

		final UFont font = UFontFactory.byDefault(12);
		final ElementTree result = new ElementTree(font, getDictionary(), strategy);

		boolean takeMe = true;
		// A row may contain its own nested "{...}" group (e.g. a set of
		// alternatives like "{ (X) public | () default }"). That group's
		// closing "}" must not be mistaken for the closing "}" of this tree
		// table itself, or everything after it in the source gets silently
		// dropped (see issue #2730: this is what made internal groups break
		// tree tables, and what made a root-level tree table lose its last
		// rows entirely once it no longer needed an external wrapper). Once
		// "other" cells are routed through getNextElement() below, a nested
		// group is consumed as a single call, so this is mostly a safety net
		// for the raw, single-token first cell.
		int depth = 0;
		while (depth > 0 || getDataSource().peek(0).getElement().equals("}") == false) {
			if (takeMe) {
				// The first cell of a row is the tree label: plain text,
				// with support for a leading "+" run indicating the nesting
				// level. It is read as a single raw token (not dispatched
				// through getNextElement()) precisely so that "+" prefix
				// stays available to ElementTree#addEntry.
				final Terminated<String> t = getDataSource().next();
				final String s = t.getElement();
				if (s.equals("}")) {
					depth--;
				} else if (s.startsWith("{")) {
					depth++;
				}
				result.addEntry(s);
				takeMe = t.getTerminator() == Terminator.NEWLINE;
			} else {
				// Every other cell is dispatched exactly like a regular
				// table cell (getNextElement() also tries Pyramid/Border/
				// Scroll, wired in as siblings by PSystemSalt), so a radio
				// button, a button, a text field or a nested "{...}" group
				// of alternatives renders as the real widget instead of its
				// raw source text (issue #2730, point 2).
				final Terminated<Element> next = getNextElement();
				result.addCellToEntry(next.getElement());
				takeMe = next.getTerminator() == Terminator.NEWLINE;
			}
		}
		final Terminated<String> next = getDataSource().next();
		return new Terminated<Element>(result, next.getTerminator());
	}

	public boolean ready() {
		final String text = getDataSource().peek(0).getElement();
		if (text.equals("{")) {
			final String text1 = getDataSource().peek(1).getElement();
			return isTreeMarker(text1);
		}
		return false;
	}

	// Whether text1 is a tree-table marker ("T" or "T" followed by a valid
	// TableStrategy character, e.g. "T#"). Shared with ElementFactoryPyramid
	// so both agree on what counts as a tree table header instead of relying
	// on two independently maintained checks (see issue #2730: Pyramid used
	// to only exclude the bare "T" case, so it would wrongly claim a top-level
	// "{T#..." block before ElementFactoryTree ever saw it).
	static boolean isTreeMarker(String text1) {
		if (text1.equals("T")) {
			return true;
		}
		if (text1.length() == 2 && text1.startsWith("T")) {
			final char c = text1.charAt(1);
			return TableStrategy.fromChar(c) != null;
		}
		return false;
	}
}
