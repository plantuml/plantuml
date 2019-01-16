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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class FrontierStackImpl implements FrontierStack {

	class Stack {
		final private FrontierComplex current;
		final private FrontierComplex envelop;

		Stack(FrontierComplex current) {
			this(current, null);
		}

		private Stack(FrontierComplex current, FrontierComplex envelop) {
			this.current = current;
			this.envelop = envelop;
		}

		Stack addEnvelop(FrontierComplex env) {
			if (this.envelop == null) {
				return new Stack(this.current, env);
			}
			return new Stack(this.current, this.envelop.mergeMax(env));
		}
	}

	final private List<Stack> all;

	public FrontierStackImpl(double freeY, int rangeEnd) {
		final Stack s = new Stack(new FrontierComplex(freeY, rangeEnd));
		all = Collections.singletonList(s);
	}

	private FrontierStackImpl(List<Stack> all) {
		this.all = Collections.unmodifiableList(all);
	}

	private FrontierComplex getLast() {
		return all.get(all.size() - 1).current;
	}

	public double getFreeY(ParticipantRange range) {
		return getLast().getFreeY(range);
	}

	public FrontierStackImpl add(double delta, ParticipantRange range) {
		final List<Stack> result = new ArrayList<Stack>(all);
		final Stack s = new Stack(getLast().add(delta, range));
		result.set(result.size() - 1, s);
		return new FrontierStackImpl(result);
	}

	public FrontierStack openBar() {
		final List<Stack> result = new ArrayList<Stack>(all);
		final Stack s = new Stack(getLast().copy());
		result.add(s);
		return new FrontierStackImpl(result);
	}

	public FrontierStack restore() {
		final List<Stack> result = new ArrayList<Stack>(all);
		final Stack openedBar = result.get(result.size() - 2);
		final Stack lastStack = result.get(result.size() - 1);
		result.set(result.size() - 2, openedBar.addEnvelop(lastStack.current));
		result.remove(result.size() - 1);
		final Stack s = new Stack(openedBar.current.copy());
		result.add(s);
		return new FrontierStackImpl(result);
	}

	public FrontierStack closeBar() {
		final List<Stack> result = new ArrayList<Stack>(all);
		final Stack openedBar = result.get(result.size() - 2);
		final Stack lastStack = result.get(result.size() - 1);
		final Stack merge = openedBar.addEnvelop(lastStack.current);
		result.set(result.size() - 2, new Stack(merge.envelop));
		result.remove(result.size() - 1);
		return new FrontierStackImpl(result);
	}

	public FrontierStackImpl copy() {
		// return new FrontierStackImpl(all);
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "nb=" + all.size() + " " + getLast().toString();
	}

}
