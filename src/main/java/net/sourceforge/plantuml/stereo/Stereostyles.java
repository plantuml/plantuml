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
package net.sourceforge.plantuml.stereo;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;

public class Stereostyles {
	// ::remove folder when __HAXE__

	public static final Stereostyles NONE = new Stereostyles();

	private final Set<String> names = new LinkedHashSet<>();

	private Stereostyles() {
	}

	public boolean isEmpty() {
		return names.isEmpty();
	}

	private static final Pattern2 p = Pattern2.cmpile("\\<{3}(.*?)\\>{3}");

	public static Stereostyles build(String label) {
		final Stereostyles result = new Stereostyles();
		final Matcher2 m = p.matcher(label);
		while (m.find())
			result.names.add(m.group(1));

		return result;
	}

	public Collection<String> getStyleNames() {
		return Collections.unmodifiableCollection(names);
	}

}
