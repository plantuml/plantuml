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

import java.util.ArrayDeque;
import java.util.Deque;

import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;

public class YamlBuilder {

	private final Deque<JsonValue> stack = new ArrayDeque<>();
	private JsonValue current;
	private String lastEmpiledKey;

	public YamlBuilder() {
		current = new JsonObject();
		stack.addLast(current);
	}

	public void increaseIndentation() {
		stack.addLast(current);

	}

	public void indentationDecrease() {
		stack.removeLast();
	}

	public void onOnlyKey(String key) {
		lastEmpiledKey = key;
		current = new JsonObject();

		final JsonObject pending = getPending();
		pending.add(key, current);

	}

	public void onKeyAndValue(String key, String value) {
		final JsonObject pending = getPending();
		pending.add(key, value);
	}

	private JsonObject getPending() {
		final JsonObject pending;
		if (stack.getLast() instanceof JsonArray) {
			final JsonArray array = (JsonArray) stack.getLast();
			pending = array.get(array.size() - 1).asObject();
		} else {
			pending = (JsonObject) stack.getLast();
		}
		return pending;
	}

	public void onListItemKeyAndValue(String key, String value) {
		final JsonArray array = getCurrentArray();
		current = new JsonObject();
		array.add(current);
		((JsonObject) current).add(key, value);
	}

	public void onListItemOnlyKey(String key) {
		final JsonArray array = getCurrentArray();
		current = new JsonObject();
		final JsonObject firstTableElement = new JsonObject();
		array.add(firstTableElement);
		firstTableElement.add(key, current);
	}

	public void onListItemOnlyValue(String value) {
		final JsonArray array = getCurrentArray();
		array.add(value);

	}

	private JsonArray getCurrentArray() {
		final JsonArray array;
		if (stack.getLast() instanceof JsonObject && ((JsonObject) stack.getLast()).size() == 0) {
			array = new JsonArray();
			stack.removeLast();
			final JsonObject parent = (JsonObject) stack.getLast();
			parent.set(lastEmpiledKey, array);
			stack.addLast(array);
		} else if (stack.getLast() instanceof JsonArray) {
			array = (JsonArray) stack.getLast();
		} else
			throw new IllegalStateException("wip");
		return array;
	}

	public JsonObject getResult() {
		return (JsonObject) stack.getFirst();
	}

}
