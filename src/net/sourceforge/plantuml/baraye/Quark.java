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

import net.sourceforge.plantuml.cucadiagram.Code;

public class Quark implements Code {

	private final Plasma plasma;
	private final List<String> parts;
	private Object data;

	Quark(Plasma plasma, List<String> parts) {
		this.plasma = plasma;
		this.parts = new ArrayList<String>(parts);
	}

	public Quark getParent() {
		if (parts.size() == 0)
			return null;
		return plasma.ensurePresent(parts.subList(0, parts.size() - 1));
	}

	@Override
	public String toString() {
		return parts.toString();
	}

	public List<String> getSignature() {
		return new ArrayList<>(parts);
	}

	public boolean containsLarge(Quark other) {
		return other.parts.size() > this.parts.size() && other.parts.subList(0, this.parts.size()).equals(this.parts);
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

	public boolean startsWith(Quark other) {
		if (other.parts.size() > this.parts.size())
			return false;

		for (int i = 0; i < other.parts.size(); i++)
			if (other.parts.get(i).equals(this.parts.get(i)) == false)
				return false;

		return true;
	}

	public String toString(String sep) {
		if (sep == null)
			sep = ".";

		final StringBuilder sb = new StringBuilder();
		for (String s : parts) {
			if (sb.length() > 0)
				sb.append(sep);

			sb.append(s);
		}
		return sb.toString();
	}

	public String getName() {
		if (parts.size() == 0)
			return "";

		return parts.get(parts.size() - 1);
	}

	public boolean isRoot() {
		return parts.size() == 0;
	}

//	public int size() {
//		return parts.size();
//	}

	public final Plasma getPlasma() {
		return plasma;
	}

	public final Object getData() {
		return data;
	}

	public final void setData(Object data) {
		this.data = data;
	}

	@Override
	public Code eventuallyRemoveStartingAndEndingDoubleQuote(String format) {
		throw new UnsupportedOperationException();
	}

}
