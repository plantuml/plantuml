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
package net.sourceforge.plantuml.hector2.continuity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Link;

public class SkeletonBuilder {

	private List<Skeleton> all = new ArrayList<Skeleton>();

	public void add(Link link) {
		addInternal(link);
		do {
			final boolean changed = merge();
			if (changed == false) {
				return;
			}
		} while (true);

	}

	private boolean merge() {
		for (int i = 0; i < all.size() - 1; i++) {
			for (int j = i + 1; j < all.size(); j++) {
				if (all.get(i).doesTouch(all.get(j))) {
					all.get(i).addAll(all.get(j));
					all.remove(j);
					return true;
				}
			}
		}
		return false;
	}

	private void addInternal(Link link) {
		for (Skeleton skeleton : all) {
			if (skeleton.doesTouch(link)) {
				skeleton.add(link);
				return;
			}
		}
		final Skeleton newSkeleton = new Skeleton();
		newSkeleton.add(link);
		all.add(newSkeleton);
	}

	public List<Skeleton> getSkeletons() {
		return Collections.unmodifiableList(all);
	}
}
