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
package net.sourceforge.plantuml.style;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

class Context {

	private final List<String> data = new ArrayList<String>();

	public Context push(String newString) {
		final Context result = new Context();
		result.data.addAll(this.data);
		result.data.add(newString);
		return result;
	}

	public Context pop() {
		if (size() == 0)
			throw new IllegalStateException();
		final Context result = new Context();
		result.data.addAll(this.data.subList(0, this.data.size() - 1));
		return result;
	}

	@Override
	public String toString() {
		return data.toString();
	}

	public int size() {
		return data.size();
	}

	public Collection<StyleSignatureBasic> toSignatures() {
		List<StyleSignatureBasic> results = new ArrayList<>(Collections.singletonList(StyleSignatureBasic.empty()));
		boolean star = false;
		for (Iterator<String> it = data.iterator(); it.hasNext();) {
			String s = it.next();
			if (s.endsWith("*")) {
				star = true;
				s = s.substring(0, s.length() - 1);
			}
			final String[] names = s.split(",");
			final List<StyleSignatureBasic> tmp = new ArrayList<>();
			for (StyleSignatureBasic ss : results)
				for (String name : names)
					tmp.add(ss.add(name));
			results = tmp;
		}

		if (star)
			for (ListIterator<StyleSignatureBasic> it = results.listIterator(); it.hasNext();) {
				final StyleSignatureBasic tmp = it.next().addStar();
				it.set(tmp);
			}

		return Collections.unmodifiableCollection(results);
	}

}