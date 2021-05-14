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
package net.sourceforge.plantuml.board;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BNode {

	private final String name;
	private final int stage;
	private int x = -1;
	private BNode parent;
	private final List<BNode> children = new ArrayList<>();

	public BNode(int stage, String name) {
		this.name = name;
		this.stage = stage;
	}

	public void addChild(BNode child) {
		if (child.stage <= this.stage) {
			throw new IllegalArgumentException();
		}
		this.children.add(child);
		if (child.parent != null) {
			throw new IllegalArgumentException();
		}
		child.parent = this;
	}

	public final String getName() {
		return name;
	}

	public final int getStage() {
		return stage;
	}

	public final BNode getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return name + "(" + stage + ") [" + x + "]";
	}

	public void computeX(AtomicInteger count) {
		this.x = count.intValue();
		for (int i = 0; i < children.size(); i++) {
			final BNode child = children.get(i);
			if (i > 0) {
				count.addAndGet(1);
			}
			child.computeX(count);
		}
	}

	public void initBarray(BArray array) {
		array.put(this);
		for (BNode child : children) {
			child.initBarray(array);
		}

	}

	public final int getX() {
		return x;
	}

}
