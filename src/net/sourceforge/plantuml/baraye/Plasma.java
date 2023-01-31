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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Plasma {

	private String separator;
	private final Quark root;
	private final List<Quark> quarks = new ArrayList<>();

	public Plasma(String separator) {
		this.root = new Quark(this, null, "");
		this.separator = separator;
		this.quarks.add(root);
	}

	public Quark root() {
		return root;
	}

	public final String getSeparator() {
		return separator;
	}

	public final void setSeparator(String separator) {
		if (separator == null)
			separator = "\u0000";
		this.separator = separator;
	}

	public final boolean hasSeparator() {
		return this.separator != null && this.separator != "\u0000";
	}

	public Quark parse(Quark root, String full) {

		final List<String> result = root.getSignature();
		while (true) {
			int idx = full.indexOf(separator);
			if (idx == -1) {
				result.add(full);
				return ensurePresent(result);
			}
			if (idx > 0) {
				result.add(full.substring(0, idx));
				ensurePresent(new ArrayList<>(result));
			}

			full = full.substring(idx + separator.length());
		}
	}

	Quark ensurePresent(List<String> result) {
		Quark quark = getIfExists(result);
		if (quark == null) {
			if (result.size() == 0) {
				// quark = new Quark(this, null, result);
				throw new UnsupportedOperationException();
			} else {
				final Quark parent = ensurePresent(result.subList(0, result.size() - 1));
				quark = new Quark(this, parent, result.get(result.size() - 1));
			}
			// System.err.println("PUTTING " + quark);
			quarks.add(quark);
		}
		return quark;

	}

	public Collection<Quark> quarks() {
		return Collections.unmodifiableCollection(quarks);
	}

	public Quark getIfExistsFromName(String name) {
		Quark result = null;
		for (Quark quark : quarks)
			if (quark.getName().equals(name)) {
//				if (result != null)
//					throw new IllegalArgumentException("Duplicate name: " + name);
				result = quark;
				return result;
			}
		return result;
	}

	public int countByName(String name) {
		int count = 0;
		for (Quark quark : quarks)
			if (quark.getName().equals(name))
				count++;
		return count;
	}

	public Quark getIfExistsFromFullPath(String full) {
		for (Quark quark : quarks)
			if (quark.toString(separator).equals(full))
				return quark;
		return null;
	}

	public Quark getIfExists(List<String> signature) {
		for (Quark quark : quarks)
			if (quark.getSignature().equals(signature))
				return quark;
		return null;
	}

	public int countChildren(Quark parent) {
		int count = 0;
		for (Quark quark : quarks)
			if (quark.getParent() == parent)
				count++;
		return count;
	}

	public List<Quark> getChildren(Quark parent) {
		final List<Quark> result = new ArrayList<>();
		for (Quark quark : quarks)
			if (quark.getParent() == parent)
				result.add(quark);
		return Collections.unmodifiableList(result);
	}

	public void moveAllChildOfToAnewFather(Quark oldFather, Quark newFather) {
		for (Quark quark : quarks) {
			if (quark == newFather)
				continue;

			if (quark.getParent() == oldFather)
				quark.setParent(newFather);
		}

	}

}
