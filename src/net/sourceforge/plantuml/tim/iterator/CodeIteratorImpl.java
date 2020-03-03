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
 */
package net.sourceforge.plantuml.tim.iterator;

import java.util.List;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;

public class CodeIteratorImpl implements CodeIterator {

	private final List<StringLocated> list;
	private int current = 0;
	private int countJump = 0;

	static class Position implements CodePosition {
		final int pos;

		Position(int pos) {
			this.pos = pos;
		}

//		@Override
//		public String toString() {
//			return "-->" + list.get(pos);
//		}
	}

	public CodeIteratorImpl(List<StringLocated> list) {
		this.list = list;
	}

	public StringLocated peek() {
		if (current == list.size()) {
			return null;
		}
		if (current > list.size()) {
			throw new IllegalStateException();
		}
		return list.get(current);
	}

	public void next() {
		if (current >= list.size()) {
			throw new IllegalStateException();
		}
		assert current < list.size();
		current++;
		assert current <= list.size();
	}

	public CodePosition getCodePosition() {
		return new Position(current);
	}

	public void jumpToCodePosition(CodePosition newPosition) throws EaterException {
		this.countJump++;
		if (this.countJump > 999) {
			throw EaterException.unlocated("Infinite loop?");
		}
		final Position pos = (Position) newPosition;
		this.current = pos.pos;

	}

}
