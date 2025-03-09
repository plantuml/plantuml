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

public class YamlBuilder {

	private final List<Monomorph> stack = new ArrayList<>();

	public YamlBuilder() {
		stack.add(new Monomorph());
	}

	public void increaseIndentation() {
		// stack.addLast(new Monomorph());

	}

	public void decreaseIndentation() {
		stack.remove(stack.size() - 1);
		if (getLast().getType() == MonomorphType.LIST)
			stack.remove(stack.size() - 1);
	}

	public Monomorph getResult() {
		return stack.get(0);
	}

	private Monomorph getLast() {
		return stack.get(stack.size() - 1);
	}

	public void onListItemPlainDash() {
		if (isArrayAlreadyThere())
			stack.remove(stack.size() - 1);
		final Monomorph newElement = new Monomorph();
		getLast().addInList(newElement);
		stack.add(newElement);
	}

	private boolean isArrayAlreadyThere() {
		if (stack.size() < 2)
			return false;
		final Monomorph potentialList = stack.get(stack.size() - 2);
		if (potentialList.getType() != MonomorphType.LIST)
			return false;
		return potentialList.getElementAt(potentialList.size() - 1) == stack.get(stack.size() - 1);
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
		stack.add(newElement);
	}

	@Override
	public String toString() {
		return stack.toString();
	}

	public void onListItemOnlyKey(String key) {
		if (isArrayAlreadyThere())
			stack.remove(stack.size() - 1);
		final Monomorph newElement = new Monomorph();
		getLast().addInList(newElement);
		stack.add(newElement);
		final Monomorph newElement2 = new Monomorph();
		getLast().putInMap(key, newElement2);
		stack.add(newElement2);

	}

	public void onListItemOnlyValue(String value) {
		if (isArrayAlreadyThere())
			stack.remove(stack.size() - 1);
		getLast().addInList(Monomorph.scalar(value));

	}

	public void onListItemKeyAndValue(String key, String value) {
		if (isArrayAlreadyThere())
			stack.remove(stack.size() - 1);
		final Monomorph newElement = new Monomorph();
		getLast().addInList(newElement);
		stack.add(newElement);
		getLast().putInMap(key, Monomorph.scalar(value));

	}

	public void onListItemKeyAndFlowSequence(String key, List<String> values) {
		if (isArrayAlreadyThere())
			stack.remove(stack.size() - 1);
		final Monomorph newElement = new Monomorph();
		getLast().addInList(newElement);
		stack.add(newElement);
		getLast().putInMap(key, Monomorph.list(values));
		
	}


}
