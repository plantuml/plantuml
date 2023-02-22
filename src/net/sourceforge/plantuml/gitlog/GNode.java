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
package net.sourceforge.plantuml.gitlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.klimt.creole.Display;

public class GNode {

	private final List<GNode> up = new ArrayList<>();
	private final List<GNode> down = new ArrayList<>();
	private final List<String> texts = new ArrayList<>();

	private String comment;

	public void addText(String text) {
		this.texts.add(text);
	}

	public boolean isTop() {
		return up.size() == 0;
	}

	public final String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public static void link(GNode n1, GNode n2) {
		n1.down.add(n2);
		n2.up.add(n1);
	}

	@Override
	public String toString() {
		return texts.toString();
	}

	public Display getDisplay() {
		return Display.create(texts);
	}

	public Collection<GNode> getDowns() {
		return Collections.unmodifiableCollection(down);
	}

	public boolean canEatTheNextOne() {
		if (up.size() != 1)
			return false;

		if (down.size() != 1)
			return false;

		final GNode next = down.get(0);
		if (next.up.size() != 1)
			return false;

		if (next.down.size() != 1)
			return false;

		return true;
	}

	public GNode eatTheNextOne() {
		if (canEatTheNextOne() == false)
			throw new IllegalStateException();

		final GNode removed = down.get(0);
		final GNode newNext = removed.down.get(0);
		this.texts.addAll(removed.texts);
		this.down.set(0, newNext);
		if (newNext.up.remove(removed) == false)
			throw new IllegalStateException();

		newNext.up.add(this);
		return removed;
	}

}
