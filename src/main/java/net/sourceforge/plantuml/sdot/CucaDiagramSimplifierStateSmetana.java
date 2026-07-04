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
package net.sourceforge.plantuml.sdot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.svek.IEntityImage;

// Smetana counterpart of CucaDiagramSimplifierState.
//
// It turns composite state groups into pre-rendered leaves (via a nested Smetana
// layout), mirroring what the dot pipeline does. This makes the Smetana pipeline
// treat composite states as plain leaf nodes, consistent with dot.
//
// For now this is intentionally SELECTIVE: it does not touch concurrent states
// (GroupType.CONCURRENT_STATE) nor the STATE groups that contain concurrent
// regions. Concurrent support will be added in a later step; until then those
// diagrams keep their current (unchanged) behavior.
public final class CucaDiagramSimplifierStateSmetana {

	public void simplify(CucaDiagram diagram, StringBounder stringBounder) {
		boolean changed;
		do {
			changed = false;
			final Collection<Entity> groups = getOrdered(diagram.getRootGroup());
			for (Entity g : groups)
				if (shouldSimplify(g)) {
					final GroupMakerStateSmetana maker = new GroupMakerStateSmetana(diagram, g, stringBounder);
					final IEntityImage img = maker.getImage();
					g.overrideImage(img, LeafType.STATE);
					changed = true;
				}

		} while (changed);
	}

	private boolean shouldSimplify(Entity g) {
		if (g.isAutarkic() == false)
			return false;

		// Concurrency is not handled yet: leave concurrent regions and their
		// enclosing states untouched.
		if (g.getGroupType() == GroupType.CONCURRENT_STATE)
			return false;

		if (containsConcurrentRegion(g))
			return false;

		return true;
	}

	private boolean containsConcurrentRegion(Entity g) {
		for (Entity child : g.groups())
			if (child.getGroupType() == GroupType.CONCURRENT_STATE)
				return true;

		return false;
	}

	private Collection<Entity> getOrdered(Entity root) {
		final Collection<Entity> ordered = new LinkedHashSet<>();
		ordered.add(root);
		int size = 1;
		while (true) {
			size = ordered.size();
			addOneLevel(ordered);
			if (size == ordered.size())
				break;

		}
		final List<Entity> result = new ArrayList<>();
		for (Entity g : ordered)
			if (g.isRoot() == false)
				result.add(0, g);

		return result;
	}

	private void addOneLevel(Collection<Entity> currents) {
		for (Entity g : new ArrayList<>(currents))
			for (Entity child : reverse(g.groups()))
				currents.add(child);

	}

	private List<Entity> reverse(Collection<Entity> source) {
		final List<Entity> result = new ArrayList<>();
		for (Entity g : source)
			result.add(0, g);

		return result;
	}

}
