/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.baraye;

import java.util.ArrayList;
import java.util.List;

public class Quark {

	private final Plasma plasma;
	private /* final */ Quark parent;
	private final String name;
	private Object data;

	Quark(Plasma plasma, Quark parent, String name) {
		this.name = name;
		this.plasma = plasma;
		this.parent = parent;
	}

	public Quark getParent() {
		return parent;
	}

	@Override
	public String toString() {
		// return parts.toString() + "(parent=" + parent + ")";
		return getSignature().toString();
	}

	public List<String> getSignature() {
		final List<String> result = new ArrayList<>();
		if (parent != null)
			result.addAll(parent.getSignature());
		result.add(name);
		return result;
	}

	public List<String> parts() {
		return getSignature();
	}

	public boolean containsLarge(Quark other) {
		final List<String> signature = this.parts();
		final List<String> otherSignature = other.parts();
		return otherSignature.size() > signature.size()
				&& otherSignature.subList(0, signature.size()).equals(signature);
	}

//	@Override
//	public boolean equals(Object obj) {
//		final Quark other = (Quark) obj;
//		if (this.plasma != other.plasma)
//			throw new IllegalArgumentException();
//		return this.parts.equals(other.parts);
//	}
//
//	@Override
//	public int hashCode() {
//		return parts.hashCode();
//	}

	public String toString(String sep) {
		if (sep == null)
			sep = ".";

		final StringBuilder sb = new StringBuilder();
		for (String s : parts()) {
			if (sb.length() > 0)
				sb.append(sep);

			sb.append(s);
		}
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public String getQualifiedName() {
		if (plasma.hasSeparator())
			return toString(plasma.getSeparator());
		return name;
	}

	public boolean isRoot() {
		return parent == null;
		// throw new UnsupportedOperationException();
		// return parts.size() == 0;
	}

	public final Plasma getPlasma() {
		return plasma;
	}

	public final Object getData() {
		return data;
	}

	public final void setData(Object data) {
		this.data = data;
	}

	public Quark childIfExists(String name) {
		final List<String> sig = new ArrayList<>(getSignature());
		sig.add(name);
		return plasma.getIfExists(sig);
	}

	public Quark child(String full) {
		return plasma.parse(this, full);
	}

	public int countChildren() {
		return plasma.countChildren(this);
	}

	public List<Quark> getChildren() {
		return plasma.getChildren(this);
	}

	public String forXmi() {
		final StringBuilder sb = new StringBuilder();
		for (String s : parts()) {
			if (sb.length() > 0)
				sb.append(".");

			sb.append(s);
		}
		return sb.toString();
	}

	public String getPortMember() {
		final String last = getName();
		final int x = last.lastIndexOf("::");
		if (x == -1) {
			return null;
		}
		return last.substring(x + 2);
	}

	void setParent(Quark newFather) {
		this.parent = newFather;
	}

}
