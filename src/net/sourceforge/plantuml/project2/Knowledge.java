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
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project2;

import java.util.HashMap;
import java.util.Map;

public class Knowledge {

	private final TaskContainer taskContainer;
	private final TimeLine timeline;
	private final Map<String, Value> variables = new HashMap<String, Value>();

	public Knowledge(TaskContainer taskContainer, TimeLine timeline) {
		this.taskContainer = taskContainer;
		this.timeline = timeline;
	}

	public Value evaluate(String exp) {
		exp = exp.trim();
		int idx = exp.indexOf('$');
		if (idx != -1) {
			return evaluate(exp.substring(0, idx), exp.substring(idx + 1));
		}
		if (exp.matches("\\d+")) {
			return new ValueInt(Integer.parseInt(exp));
		}
		if (Day.isValidDesc(exp)) {
			final Day day = new Day(exp);
			return new ValueTime(day);
		}
		if (exp.startsWith("^")) {
			exp = exp.substring(1);
		}
		if (variables.containsKey(exp)) {
			return variables.get(exp);
		}
		idx = exp.indexOf("+");
		if (idx != -1) {
			return plus(exp.substring(0, idx), exp.substring(idx + 1));
		}
		throw new UnsupportedOperationException(exp);

	}

	private Value plus(String arg1, String arg2) {
		final Value v1 = evaluate(arg1);
		final Value v2 = evaluate(arg2);
		if (v1 instanceof ValueInt && v2 instanceof ValueInt) {
			return new ValueInt(((ValueInt) v1).getValue() + ((ValueInt) v2).getValue());
		}
		if (v1 instanceof ValueTime && v2 instanceof ValueInt) {
			final int nb = ((ValueInt) v2).getValue();
			TimeElement t = ((ValueTime) v1).getValue();
			if (nb > 0) {
				for (int i = 0; i < nb; i++) {
					t = timeline.next(t);
				}
			}
			if (nb < 0) {
				for (int i = 0; i < -nb; i++) {
					t = timeline.previous(t);
				}
			}
			return new ValueTime(t);
		}
		throw new UnsupportedOperationException();
	}

	private Value evaluate(String task, String attribute) {
		final Task t = taskContainer.getTask(task);
		final TaskAttribute att = TaskAttribute.fromString(attribute);
		if (att == TaskAttribute.COMPLETED) {
			return new ValueTime(t.getCompleted());
		}
		throw new UnsupportedOperationException();
	}

	public void set(String var, Value exp) {
		variables.put(var, exp);
	}

}
