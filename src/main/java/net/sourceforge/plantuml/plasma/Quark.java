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
package net.sourceforge.plantuml.plasma;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A named node in the entity graph.
 *
 * @see net.sourceforge.plantuml.plasma
 */
public class Quark<DATA> {

	private final Plasma<DATA> plasma;
	private final Quark<DATA> parent;
	private final String name;
	private DATA data;
	private final Map<String, Quark<DATA>> children = new LinkedHashMap<>();
	private final String qualifiedName;

	Quark(Plasma<DATA> plasma, Quark<DATA> parent, String name) {
		this.name = name;
		this.plasma = plasma;
		this.parent = parent;
		if (parent == null || parent.parent == null)
			this.qualifiedName = name;
		else
			this.qualifiedName = parent.qualifiedName + plasma.getSeparator() + name;
		this.plasma.register(this);
	}

	public Quark<DATA> getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return qualifiedName;
	}

	public String toStringPoint() {
		if (parent == null || parent.parent == null)
			return name;

		return parent.toStringPoint() + "." + name;
	}

	public String getName() {
		return name;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public final Plasma<DATA> getPlasma() {
		return plasma;
	}

	public final DATA getData() {
		return data;
	}

	public final void setData(DATA data) {
		if (this.data != null)
			throw new IllegalStateException();
		this.data = data;
	}

	public Quark<DATA> childIfExists(String name) {
		if (plasma.hasSeparator() && name.contains(plasma.getSeparator()))
			throw new IllegalArgumentException();
		return children.get(name);
	}

	public Quark<DATA> child(String full) {
		if (plasma.hasSeparator() == false)
			return getDirectChild(full);

		full = clean(full);
		final String separator = plasma.getSeparator();
		Quark<DATA> current = this;
		while (true) {
			int idx = full.indexOf(separator);
			if (idx == -1)
				return current.getDirectChild(full);

			final String first = full.substring(0, idx);
			current = current.getDirectChild(first);
			full = clean(full.substring(idx + separator.length()));
		}
	}

	private Quark<DATA> getDirectChild(String name) {
		Quark<DATA> result = children.get(name);
		if (result == null) {
			result = new Quark<DATA>(plasma, this, name);
			children.put(name, result);
		}
		return result;
	}

	private String clean(String full) {
		final String separator = plasma.getSeparator();
		while (full.startsWith(separator))
			full = full.substring(separator.length());
		while (full.endsWith(separator))
			full = full.substring(0, full.length() - separator.length());

		return full;
	}

	public Collection<Quark<DATA>> getChildren() {
		return Collections.unmodifiableCollection(children.values());
	}

	public int countChildren() {
		return children.size();
	}

}
