/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.StringUtils;

public class Project {

	private final Map<String, FreeVariable> variables = new TreeMap<String, FreeVariable>();

	private final Map<String, Item> items = new TreeMap<String, Item>();

	private final Map<String, Ressource> ressources = new TreeMap<String, Ressource>();

	private final DayCloseOr dayClose = new DayCloseOr();

	public Project() {
	}

	public final Instant getStart() {
		Instant result = null;
		for (Item it : getValidItems()) {
			if (result == null || result.compareTo(it.getBegin()) > 0) {
				result = it.getBegin();
			}
		}
		return result;
	}

	public final Instant getEnd() {
		Instant result = null;
		for (Item it : getValidItems()) {
			if (result == null || result.compareTo(it.getCompleted()) < 0) {
				result = it.getCompleted();
			}
		}
		return result;
	}

	public FreeVariable createVariable(String name, NumericType type) {
		if (variables.containsKey(name)) {
			throw new IllegalArgumentException("Already exist: " + name);
		}
		final FreeVariable variable = new FreeVariable(name, type);
		variables.put(name, variable);
		return variable;
	}

	public Expression getExpression(String desc) {
		desc = StringUtils.trin(desc);
		final int plus = desc.indexOf('+');

		if (plus != -1) {
			final Expression exp1 = getExpression(desc.substring(0, plus));
			final Expression exp2 = getExpression(desc.substring(plus + 1));
			if (exp1.getNumericType() == NumericType.INSTANT
					&& (exp2.getNumericType() == NumericType.DURATION || exp2.getNumericType() == NumericType.NUMBER)) {
				return new FormalAdditionInstantDuration(exp1, exp2, new BasicInstantArithmetic(dayClose));
			}
			if (exp2.getNumericType() == NumericType.INSTANT
					&& (exp1.getNumericType() == NumericType.DURATION || exp1.getNumericType() == NumericType.NUMBER)) {
				return new FormalAdditionInstantDuration(exp2, exp1, new BasicInstantArithmetic(dayClose));
			}
			return new FormalAddition(exp1, exp2);
		}

		if (desc.matches("^\\d+$")) {
			return new Constant(new NumericNumber(Integer.parseInt(desc)));
		}
		if (desc.matches("^\\$\\w+$")) {
			final String varName = desc.substring(1);
			final FreeVariable v = variables.get(varName);
			if (v != null) {
				return v;
			}
			throw new IllegalArgumentException("No such variable: " + desc);
		}
		if (Day.isValidDesc(desc)) {
			final Day d = new Day(desc);
			return new Constant(new Instant(d));
		}
		if (desc.matches("^[\\w/]+\\$(begin|completed|work|load|duration)$")) {
			final int idx = desc.indexOf('$');
			final String varName = desc.substring(0, idx);
			final Item item = items.get(varName);
			if (item == null) {
				throw new IllegalArgumentException("No such variable: " + desc);
			}
			return new Constant(ItemCaract.valueOf(StringUtils.goUpperCase(desc.substring(idx + 1))).getData(item));
		}
		if (desc.startsWith("^")) {
			final Item item = items.get(desc.substring(1));
			if (item == null) {
				throw new IllegalArgumentException("No such variable: " + desc);
			}
			return new Constant(item.getBegin());
		}
		throw new IllegalArgumentException("cannot parse");
	}

	public boolean affectation(String destination, Expression expression) {
		if (destination.startsWith("^")) {
			return affectationJalon(destination, expression);
		}
		if (destination.startsWith("~")) {
			return affectationRessource(destination, expression);
		}
		final int idx = destination.indexOf('$');
		if (idx == -1) {
			return affectationVariable(destination, expression);
		}
		final String itemName = destination.substring(0, idx);
		final Item item = getItem(itemName);
		if (item instanceof IncompleteItem == false) {
			return false;
		}
		final IncompleteItem incompleteItem = (IncompleteItem) item;
		final String suf = destination.substring(idx + 1);
		if (suf.equalsIgnoreCase("begin")) {
			incompleteItem.setData(ItemCaract.BEGIN, expression.getValue());
		} else if (suf.equalsIgnoreCase("completed")) {
			incompleteItem.setData(ItemCaract.COMPLETED, expression.getValue());
		} else if (suf.equalsIgnoreCase("work")) {
			incompleteItem.setData(ItemCaract.WORK, expression.getValue());
		} else if (suf.equalsIgnoreCase("duration")) {
			if (expression.getNumericType() == NumericType.NUMBER) {
				expression = new Constant(new Duration((NumericNumber) expression.getValue()));
			}
			incompleteItem.setData(ItemCaract.DURATION, expression.getValue());
		} else if (suf.equalsIgnoreCase("LOAD")) {
			if (expression.getNumericType() == NumericType.NUMBER) {
				expression = new Constant(new Load((NumericNumber) expression.getValue()));
			}
			incompleteItem.setData(ItemCaract.LOAD, expression.getValue());
		} else {
			return false;
		}
		return true;
	}

	private boolean affectationRessource(String res, Expression expression) {
		res = res.substring(1);
		final int idx = res.indexOf('$');
		final String suf = res.substring(idx + 1);
		if (suf.equals("capacity")) {
			final Ressource ressource = getRessource(res.substring(0, idx));
			ressource.setCapacity(((NumericNumber) expression.getValue()).getIntValue());
			return true;
		}
		return false;
	}

	private Ressource getRessource(String code) {
		Ressource result = ressources.get(code);
		if (result == null) {
			result = new Ressource(code);
			ressources.put(code, result);
		}
		return result;
	}

	private boolean affectationJalon(String jalon, Expression expression) {
		final Jalon it = getItemJalon(jalon.substring(1));
		it.setInstant(expression.getValue());
		return true;
	}

	private Jalon getItemJalon(String jalon) {
		Jalon result = (Jalon) items.get(jalon);
		if (result == null) {
			result = new Jalon(jalon, null);
			items.put(jalon, result);

		}
		return result;
	}

	private Item getItem(String code) {
		Item result = items.get(code);
		if (result == null) {
			final int idx = code.indexOf('/');
			if (idx == -1) {
				result = new IncompleteItem(code, null, new BasicInstantArithmetic(dayClose));
			} else {
				final ParentItem parent = getItemParent(code.substring(0, idx));
				result = new IncompleteItem(code, parent, new BasicInstantArithmetic(dayClose));
				parent.addChild(result);
			}
			items.put(code, result);
		}
		return result;
	}

	private ParentItem getItemParent(String code) {
		Item result = items.get(code);
		if (result == null) {
			final int idx = code.indexOf('/');
			if (idx == -1) {
				result = new ParentItem(code, null);
				items.put(code, result);
			} else {
				throw new UnsupportedOperationException();
			}
		}
		return (ParentItem) result;
	}

	private boolean affectationVariable(String destination, Expression expression) {
		if (variables.containsKey(destination) == false) {
			return false;
		}
		variables.get(destination).setValue(expression);
		return true;
	}

	public List<Item> getValidItems() {
		final List<Item> result = new ArrayList<Item>();
		for (Item item : items.values()) {
			if (item.isValid()) {
				result.add(item);
			}
		}
		Collections.sort(result, new ItemComparator());
		return Collections.unmodifiableList(result);
	}

	public final DayClose getDayClose() {
		return dayClose;
	}

	public void closeWeekDay(WeekDay weekDay) {
		dayClose.add(new DayCloseWeekDay(weekDay));
	}

	// public Item getItem(String code) {
	// BasicItem result = items.get(code);
	// if (result == null) {
	// result = new BasicItem(code);
	// items.put(code, result);
	// }
	// return result;
	// }

}
