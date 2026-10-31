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
package net.sourceforge.plantuml.yaml.parser;

import java.util.ArrayList;
import java.util.List;

// Builds a Monomorph tree from a sequence of YAML events.
//
// Two parallel lists work together to track the current parsing context.
//
// Example: while parsing the value of "b" in this YAML:
//   base:
//     a: hello
//     b:
//       - test
//
// the two lists look like this:
//
//   indents :        [0,    2,      4      ]
//   nodes   :  [root, base{}, b{},   items[]]
//
// - "indents" records the YAML indentation level at each nesting depth.
//   It grows when the parser encounters a deeper indent, and shrinks
//   when the indent decreases (returning to an outer scope).
//
// - "nodes" tracks which Monomorph is the "current container" at each
//   depth.  The top of the stack (getLast()) is always the node that
//   will receive the next key/value or list item.
//
// Important: "nodes" always has exactly one more entry than "indents",
// because nodes[0] is the root element (created in the constructor)
// which exists before any indentation level has been seen.
//
// adjustIndentation() keeps both lists in sync: on indent increase it
// pushes a new level; on indent decrease it pops both lists together.
// The on*() methods may also push into "nodes" (e.g. onOnlyKey pushes
// a new child map), and the corresponding pop happens later in
// adjustIndentation() when the indent level drops back.
public class YamlBuilder {

	private final List<Integer> indents = new ArrayList<>();
	private final List<Monomorph> nodes = new ArrayList<>();

	public YamlBuilder() {
		nodes.add(new Monomorph());
	}

	// Called by the parser before processing a line.
	// Adjusts the stack depth to match the current indentation level.
	public void adjustIndentation(int indent) {
		if (indents.isEmpty()) {
			indents.add(indent);
			return;
		}
		if (indent > indents.get(indents.size() - 1)) {
			indents.add(indent);
		} else {
			while (indents.size() > 0 && indent < indents.get(indents.size() - 1)) {
				indents.remove(indents.size() - 1);
				nodes.remove(nodes.size() - 1);
				if (getLast().getType() == MonomorphType.LIST)
					nodes.remove(nodes.size() - 1);
			}
		}
	}

	/** The indentation of the innermost open level, or -1 before the first line. */
	public int currentIndent() {
		return indents.isEmpty() ? -1 : indents.get(indents.size() - 1);
	}

	public Monomorph getResult() {
		return nodes.get(0);
	}

	private Monomorph getLast() {
		return nodes.get(nodes.size() - 1);
	}

	public void onListItemPlainDash() {
		if (isArrayAlreadyThere())
			nodes.remove(nodes.size() - 1);
		final Monomorph newElement = new Monomorph();
		getLast().addInList(newElement);
		nodes.add(newElement);
	}

	private boolean isArrayAlreadyThere() {
		if (nodes.size() < 2)
			return false;
		final Monomorph potentialList = nodes.get(nodes.size() - 2);
		if (potentialList.getType() != MonomorphType.LIST)
			return false;
		return potentialList.getElementAt(potentialList.size() - 1) == nodes.get(nodes.size() - 1);
	}

	public void onKeyAndValue(String key, String value) {
		getLast().putInMap(key, Monomorph.scalar(value));
	}

	public void onKeyAndFlowSequence(String key, List<String> list) {
		getLast().putInMap(key, Monomorph.list(list));
	}

	public void onOnlyKey(String key) {
		final Monomorph newElement = new Monomorph();
		getLast().putInMap(key, newElement);
		nodes.add(newElement);
	}

	@Override
	public String toString() {
		return nodes.toString();
	}

	public void onListItemOnlyKey(String key) {
		if (isArrayAlreadyThere())
			nodes.remove(nodes.size() - 1);
		final Monomorph newElement = new Monomorph();
		getLast().addInList(newElement);
		nodes.add(newElement);
		final Monomorph newElement2 = new Monomorph();
		getLast().putInMap(key, newElement2);
		nodes.add(newElement2);

	}

	public void onListItemOnlyValue(String value) {
		if (isArrayAlreadyThere())
			nodes.remove(nodes.size() - 1);
		getLast().addInList(Monomorph.scalar(value));

	}

	public void onListItemKeyAndValue(String key, String value) {
		if (isArrayAlreadyThere())
			nodes.remove(nodes.size() - 1);
		final Monomorph newElement = new Monomorph();
		getLast().addInList(newElement);
		nodes.add(newElement);
		getLast().putInMap(key, Monomorph.scalar(value));

	}

	public void onListItemKeyAndFlowSequence(String key, List<String> values) {
		if (isArrayAlreadyThere())
			nodes.remove(nodes.size() - 1);
		final Monomorph newElement = new Monomorph();
		getLast().addInList(newElement);
		nodes.add(newElement);
		getLast().putInMap(key, Monomorph.list(values));

	}


}
