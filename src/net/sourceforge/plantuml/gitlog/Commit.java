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
package net.sourceforge.plantuml.gitlog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commit {

	private final String name;
	private final String comment;
	private final CursorPosition position;

	public Commit(String name, CursorPosition position) {
		this.name = name;
		this.position = position;
		this.comment = position.getCommentInLine();
		if (position.matches("* ") == false && position.matches("*-") == false) {
			throw new IllegalArgumentException();
		}
	}

	public String getComment() {
		return comment;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final Commit other = (Commit) obj;
		return this.name.equals(other.name);
	}

	@Override
	public String toString() {
		return name;
	}

	public List<CursorPosition> getCandidatesForDown() {
		final List<CursorPosition> result = new ArrayList<>();
		if (position.move(0, 1).matches("*")) {
			result.add(position.move(0, 1));
		}
		CursorPosition current = position;
		addAbove(result, current);
		while (true) {
			current = current.move(1, 0);
			if (current.matches(".")) {
				addAbove(result, current);
			} else if (current.matches("-")) {
			} else {
				return Collections.unmodifiableList(result);
			}

		}
	}

	private static void addAbove(List<CursorPosition> result, CursorPosition here) {
		if (here.move(0, 1).matches("|")) {
			result.add(here.move(0, 1));
		}
		if (here.move(1, 1).matches("\\")) {
			result.add(here.move(1, 1));
		}
		if (here.move(-1, 1).matches("/")) {
			result.add(here.move(-1, 1));
		}
	}

	public List<Commit> getAncestors() {
		final List<Commit> result = new ArrayList<>();

		for (CursorPosition pos : getCandidatesForDown()) {
			final CursorPosition down = pos.getDownFromHere();
			result.add(down.getCommit());
		}

		return Collections.unmodifiableList(result);

	}

	public String getName() {
		return name;
	}

	public final CursorPosition getPosition() {
		return position;
	}

}
