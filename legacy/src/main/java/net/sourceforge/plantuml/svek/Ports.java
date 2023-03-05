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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import net.sourceforge.plantuml.utils.SignatureUtils;

public class Ports {

	private final Map<String, PortGeometry> ids = new HashMap<String, PortGeometry>();

	public static String encodePortNameToId(String portName) {
		return "p" + SignatureUtils.getMD5Hex(portName);
	}

	@Override
	public String toString() {
		return ids.toString();
	}

	public Ports translateY(double deltaY) {
		final Ports result = new Ports();
		for (Map.Entry<String, PortGeometry> ent : ids.entrySet())
			result.ids.put(ent.getKey(), ent.getValue().translateY(deltaY));

		return result;
	}

	public void add(String portName, int score, double position, double height) {
		final String id = encodePortNameToId(Objects.requireNonNull(portName));
		final PortGeometry already = ids.get(id);
		if (already == null || already.getScore() < score)
			ids.put(id, new PortGeometry(id, position, height, score));
	}

	public void addThis(Ports other) {
		for (Entry<String, PortGeometry> ent : other.ids.entrySet()) {
			final String key = ent.getKey();
			final PortGeometry already = ids.get(key);
			if (already == null || already.getScore() < ent.getValue().getScore())
				ids.put(key, ent.getValue());
		}
	}

	public Collection<PortGeometry> getAllPortGeometry() {
		final List<PortGeometry> result = new ArrayList<PortGeometry>(ids.values());
		Collections.sort(result);
		return Collections.unmodifiableCollection(result);
	}

}