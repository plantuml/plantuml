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
package net.sourceforge.plantuml.style.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleLoader;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.Value;

class Context {

	private final List<StyleSignatureBasic> signatures = new ArrayList<>();

	private final Map<PName, Value> map = new EnumMap<>(PName.class);
	private Context parent;

	private Context() {
	}

	public static Context empty() {
		final Context result = new Context();
		result.signatures.add(StyleSignatureBasic.empty());
		return result;
	}

	public Context push(String newString) {
		if (newString.startsWith(":"))
			newString = newString.substring(1);
		final Context result = new Context();
		result.parent = this;

		boolean star = false;

		if (newString.endsWith(StyleSignatureBasic.STAR)) {
			newString = newString.substring(0, newString.length() - 1).trim();
			star = true;
		}

		for (String s : newString.split(","))
			for (StyleSignatureBasic ssb : this.signatures) {
				if (s.startsWith(".")) {
					ssb = ssb.addStereotype(s);
				} else if (s.startsWith("depth(")) {
					ssb = ssb.addLevel(Integer.parseInt(s.replaceAll("\\D", "")));
				} else {
					final SName sname = SName.retrieve(s);
					if (sname == null)
						ssb = ssb.addStereotype(s);
					else
						ssb = ssb.addSName(sname);
				}

				if (star)
					ssb = ssb.addStar();

				result.signatures.add(ssb);
			}

		return result;
	}

	public Context pop() {
		if (isEmpty())
			throw new IllegalStateException();
		return this.parent;
	}

	@Override
	public String toString() {
		return signatures.toString();
	}

	public boolean isEmpty() {
		return signatures.get(0).isEmpty();
	}

	public Collection<StyleSignatureBasic> toSignatures() {
		return signatures;
	}

	public void putInContext(PName key, Value value) {
		map.put(key, value);
	}

	public Collection<Style> toStyles() {
		final Collection<Style> result = new ArrayList<>();
		final Collection<StyleSignatureBasic> signatures = toSignatures();
		for (StyleSignatureBasic signature : signatures) {
			Map<PName, Value> tmp = map;
			if (signature.isWithDot())
				tmp = StyleLoader.addPriorityForStereotype(tmp);
			if (tmp.size() > 0)
				result.add(new Style(signature, tmp));

		}
		return result;
	}

}