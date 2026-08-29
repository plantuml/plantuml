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
package net.sourceforge.plantuml.style.parser2;

import java.util.EnumMap;
import java.util.Map;

import net.sourceforge.plantuml.style.PName;

/**
 * One declaration stored at a {@link StyleAtomTrie} node: the properties it sets, together
 * with its {@code depth(n)}/{@code *} side-condition. Its required {@link StyleAtom} set is
 * not repeated here -- it is exactly the trie path leading to the node this rule lives in.
 *
 * There can be more than one {@link CompiledStyleRule} at the very same trie node: two
 * declarations that share the same atom path but disagree on {@code depth(n)}, e.g.
 * {@code sequenceDiagram { depth(2) {...} depth(3) {...} } }, both reach the node for
 * {@code {sequenceDiagram}} but stay separate entries there.
 *
 * Each property carries the priority it was assigned in {@link MergedStyleNode}, so several
 * matching rules can be folded together correctly by {@link StyleMerge} instead of the earlier
 * "whichever the trie happened to visit last wins".
 */
public final class CompiledStyleRule {

	private final LevelConstraint levelConstraint;
	private final Map<PName, PrioritizedValue> values;

	CompiledStyleRule(LevelConstraint levelConstraint, Map<PName, PrioritizedValue> values) {
		this.levelConstraint = levelConstraint;
		this.values = values;
	}

	public LevelConstraint getLevelConstraint() {
		return levelConstraint;
	}

	/** This declaration's properties, each already carrying its priority and light/dark value. */
	public Map<PName, PrioritizedValue> getPrioritizedProperties() {
		return values;
	}

	/**
	 * A plain string view of the same properties -- see {@link PrioritizedValue#getValue()} --
	 * for callers that do not (yet) need priorities or the light/dark distinction.
	 */
	public Map<PName, String> getProperties() {
		final Map<PName, String> result = new EnumMap<PName, String>(PName.class);
		for (Map.Entry<PName, PrioritizedValue> ent : values.entrySet())
			result.put(ent.getKey(), ent.getValue().getValue());
		return result;
	}

	@Override
	public String toString() {
		return levelConstraint + " " + values;
	}

}
