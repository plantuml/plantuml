/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.gitlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GNodeBuilder {

	private final List<GNode> all = new ArrayList<>();

	public GNodeBuilder(List<Commit> allCommits) {

		final Map<String, GNode> tmp = new LinkedHashMap<String, GNode>();
		for (Commit commit : allCommits) {
			final GNode node = new GNode();
			node.setComment(commit.getComment());
			node.addText(commit.getName());
			tmp.put(commit.getName(), node);
		}

		for (Commit commit : allCommits) {
			for (Commit parent : commit.getAncestors()) {
				GNode.link(tmp.get(commit.getName()), tmp.get(parent.getName()));
			}
		}

		this.all.addAll(tmp.values());

		merge();

	}

	private void merge() {
		while (true) {
			boolean changed = false;
			for (Iterator<GNode> it = all.iterator(); it.hasNext();) {
				final GNode node = it.next();
				if (node.canEatTheNextOne()) {
					final GNode removed = node.eatTheNextOne();
					all.remove(removed);
					changed = true;
					break;
				}
			}
			if (changed == false) {
				return;
			}
		}
	}

	public Collection<GNode> getAllNodes() {
		return Collections.unmodifiableCollection(all);
	}

}
