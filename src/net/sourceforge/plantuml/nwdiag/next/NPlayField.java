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
 */
package net.sourceforge.plantuml.nwdiag.next;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.nwdiag.core.NServer;
import net.sourceforge.plantuml.nwdiag.core.NwGroup;

public class NPlayField {

	private final List<NStage> stages = new ArrayList<>();

	private final List<NBox> boxes = new ArrayList<>();

	public NStage getStage(int num) {
		while (stages.size() <= num)
			stages.add(new NStage(stages.size()));

		return stages.get(num);
	}

	public NStage getLast() {
		if (stages.size() == 0)
			return null;

		return getStage(stages.size() - 1);
	}

	public NStage createNewStage() {
		return getStage(stages.size());
	}

	public void addInPlayfield(NBar bar) {
		if (bar.getParent() == null) {
			final NBox single = new NBox();
			single.add(bar);
			bar.setParent(single);
			boxes.add(bar.getParent());
		} else if (boxes.contains(bar.getParent()) == false) {
			boxes.add(bar.getParent());
		}
	}

	public Map<NBar, Integer> doLayout() {
		final NTetris<NBox> tetris = new NTetris<>();
		for (NBox box : boxes)
			tetris.add(box);

		final Map<NBar, Integer> result = new HashMap<>();

		final Map<NBox, Integer> pos = tetris.getPositions();
		for (Entry<NBox, Integer> ent : pos.entrySet()) {
			final NBox box = ent.getKey();
			final int boxPos = ent.getValue();

			final Map<NBar, Integer> bars = box.getPositions();
			for (Entry<NBar, Integer> bar : bars.entrySet())
				result.put(bar.getKey(), boxPos + bar.getValue());

		}
		return Collections.unmodifiableMap(result);
	}

	public void fixGroups(List<NwGroup> groups, Collection<NServer> servers) {
		for (NwGroup group : groups)
			for (NServer server : servers)
				if (group.contains(server))
					fixServerInGroup(server, group);
	}

	private void fixServerInGroup(NServer server, NwGroup group) {
		final NBox groupBox = group.getNboxInternal();
		if (server.getBar().getParent() == groupBox)
			return;
		boxes.remove(server.getBar().getParent());
		if (boxes.contains(groupBox) == false)
			boxes.add(groupBox);
		server.getBar().setParent(groupBox);
		groupBox.add(server.getBar());
	}

}
