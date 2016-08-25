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
import java.util.Iterator;
import java.util.List;

public class ParentItem implements Item {

	private final String code;
	private final Item parent;

	private final List<Item> children = new ArrayList<Item>();

	public ParentItem(String code, Item parent) {
		this.code = code;
		this.parent = parent;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(code + " {");
		for (final Iterator<Item> it = children.iterator(); it.hasNext();) {
			final Item child = it.next();
			sb.append(child.getCode());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	public Instant getBegin() {
		Instant result = null;
		for (Item it : children) {
			if (result == null || result.compareTo(it.getBegin()) > 0) {
				result = it.getBegin();
			}
		}
		return result;
	}

	public Instant getCompleted() {
		Instant result = null;
		for (Item it : children) {
			if (result == null || result.compareTo(it.getCompleted()) < 0) {
				result = it.getCompleted();
			}
		}
		return result;
	}

	public Duration getDuration() {
		throw new UnsupportedOperationException();
	}

	public Load getLoad() {
		throw new UnsupportedOperationException();
	}

	public NumericNumber getWork() {
		throw new UnsupportedOperationException();
	}

	public boolean isLeaf() {
		return false;
	}

	public Item getParent() {
		return parent;
	}

	public List<Item> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public String getCode() {
		return code;
	}

	public void addChild(Item child) {
		this.children.add(child);
	}

	public boolean isValid() {
		if (children.size() == 0) {
			return false;
		}
		for (Item it : children) {
			if (it.isValid() == false) {
				return false;
			}
		}
		return true;
	}

}
