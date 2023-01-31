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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.baraye.EntityImp;

public class HideOrShow2 {

	private final String what;
	private final boolean show;

	@Override
	public String toString() {
		return what + " (" + show + ")";
	}

	private boolean isApplyable(EntityImp leaf) {
		if (what.startsWith("$"))
			return isApplyableTag(leaf, what.substring(1));

		if (what.startsWith("<<") && what.endsWith(">>"))
			return isApplyableStereotype(leaf.getStereotype(), what.substring(2, what.length() - 2).trim());

		if (isAboutUnlinked())
			return isApplyableUnlinked(leaf);

		final String fullName = leaf.getCodeGetName();
		// System.err.println("fullName=" + fullName);
		return match(fullName, what);
	}

	private boolean isApplyable(Stereotype stereotype) {
		if (what.startsWith("<<") && what.endsWith(">>"))
			return isApplyableStereotype(stereotype, what.substring(2, what.length() - 2).trim());
		return false;
	}

	public boolean isAboutUnlinked() {
		return what.equalsIgnoreCase("@unlinked");
	}

	private boolean isApplyableUnlinked(EntityImp leaf) {
		if (leaf.isAloneAndUnlinked())
			return true;

		return false;
	}

	private boolean isApplyableStereotype(Stereotype stereotype, String pattern) {
		if (stereotype == null)
			return false;

		for (String label : stereotype.getMultipleLabels())
			if (match(label, pattern))
				return true;

		return false;
	}

	private boolean isApplyableTag(EntityImp leaf, String pattern) {
		for (Stereotag tag : leaf.stereotags())
			if (match(tag.getName(), pattern))
				return true;

		return false;
	}

	private boolean match(String s, String pattern) {
		if (pattern.contains("*")) {
			// System.err.println("f1=" + pattern);
			// System.err.println("f2=" + Pattern.quote(pattern));
			// System.err.println("f3=" + Matcher.quoteReplacement(pattern));
			String reg = "^" + pattern.replace("*", ".*") + "$";
			return s.matches(reg);

		}
		return s.equals(pattern);
	}

	public HideOrShow2(String what, boolean show) {
		this.what = what;
		this.show = show;
	}

	public boolean apply(boolean hidden, EntityImp leaf) {
		if (isApplyable(leaf))
			return !show;

		return hidden;
	}

	public boolean apply(boolean hidden, Stereotype stereotype) {
		if (isApplyable(stereotype))
			return !show;

		return hidden;
	}

}
