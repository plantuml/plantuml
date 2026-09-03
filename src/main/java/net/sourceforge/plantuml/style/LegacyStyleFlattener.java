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
package net.sourceforge.plantuml.style;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.style.parser2.MergedStyleNode;
import net.sourceforge.plantuml.style.parser2.PrioritizedValue;
import net.sourceforge.plantuml.style.parser2.RawSelector;

/**
 * Turns a {@link MergedStyleNode} tree (built by the new {@code parser2} text parser) into the
 * flat {@code Collection<Style>} shape the legacy {@code net.sourceforge.plantuml.style.parser}
 * package used to hand back -- so that everything downstream of parsing (loading a .skin file
 * via {@code StyleBuilder#loadInternal}, layering a {@code <style>} overlay via
 * {@code StyleBuilder#muteStyle}, both ultimately resolved through {@link StyleIndex}) keeps
 * working against legacy {@link Style}/{@link Value} objects exactly as before, unaware that the
 * text was parsed by a different engine.
 *
 * This walk mirrors {@code net.sourceforge.plantuml.style.parser2.StyleAtomTrie#compileNode}
 * closely -- same tree, same recursion shape -- but instead of inserting into a trie keyed by
 * {@code StyleAtom}, it accumulates a legacy {@link StyleSignature} along the path and
 * emits one {@link Style} per node that has properties, converting each
 * {@link PrioritizedValue} to a {@link Value} on the way. Two legacy behaviors that
 * {@code StyleAtomTrie#compileNode} does not need to reproduce (nothing downstream of it reads
 * signatures) matter here and are handled explicitly:
 * <ul>
 * <li>a stereotype-scoped node's properties carry their {@link Specificity} stereotype count
 * (how many stereotypes {@code pathSoFar} requires), exactly like the legacy
 * {@code net.sourceforge.plantuml.style.parser.Context#toStyles} used to apply via
 * {@code StyleLoader#addPriorityForStereotype} -- without it, a stereotype rule could lose to a
 * later plain one it must always outrank;</li>
 * <li>an unrecognized selector word (neither a known {@link SName}, a {@code .stereotype}, nor a
 * {@code depth(n)}) is folded in as a stereotype tag, exactly like the legacy
 * {@code Context#push}'s {@code SName.retrieve(s) == null -> addStereotype(s)} fallback --
 * unlike {@code StyleAtomTrie#compileNode}'s {@code RawSelector.Kind#UNKNOWN} handling, which
 * deliberately treats it as no constraint at all (harmless there since that trie's own resolver,
 * {@code CompiledStyleSheet}, is not on this path).</li>
 * </ul>
 */
final class LegacyStyleFlattener {

	private LegacyStyleFlattener() {
	}

	/** Every declaration in {@code root}, as flat legacy {@link Style} objects. */
	static List<Style> flatten(MergedStyleNode root) {
		final List<Style> result = new ArrayList<Style>();
		flattenNode(root, StyleSignature.empty(), result);
		return result;
	}

	private static void flattenNode(MergedStyleNode node, StyleSignature pathSoFar, List<Style> result) {
		if (node.getProperties().isEmpty() == false)
			result.add(new Style(pathSoFar, toValueMap(node.getProperties(), pathSoFar.getStereotypes().size())));

		// A selector declared once plain and once starred (e.g. ".europeStyle {...}" alongside
		// ".europeStyle * {...}") lives in two separate MergedStyleNode child slots, never one --
		// see MergedStyleNode's own javadoc for why merging them would leak a non-cascading
		// property onto descendants that must only inherit the cascading one. So each of the two
		// child kinds (named, other) is visited across both its plain and its starred map.
		flattenNamedChildren(node.getNamedChildren(), pathSoFar, result);
		flattenNamedChildren(node.getStarredNamedChildren(), pathSoFar, result);
		flattenOtherChildren(node, node.getOtherChildren(), pathSoFar, result);
		flattenOtherChildren(node, node.getStarredOtherChildren(), pathSoFar, result);
	}

	private static void flattenNamedChildren(Map<SName, MergedStyleNode> children, StyleSignature pathSoFar,
			List<Style> result) {
		for (Map.Entry<SName, MergedStyleNode> ent : children.entrySet()) {
			final MergedStyleNode child = ent.getValue();
			StyleSignature childSignature = pathSoFar.addSName(ent.getKey());
			if (child.isStar())
				childSignature = childSignature.addStar();
			flattenNode(child, childSignature, result);
		}
	}

	private static void flattenOtherChildren(MergedStyleNode node, Map<String, MergedStyleNode> children,
			StyleSignature pathSoFar, List<Style> result) {
		for (Map.Entry<String, MergedStyleNode> ent : children.entrySet()) {
			final String key = ent.getKey();
			final MergedStyleNode child = ent.getValue();
			final RawSelector.Kind kind = node.getOtherChildKind(key);

			StyleSignature childSignature;
			if (kind == RawSelector.Kind.DEPTH)
				childSignature = pathSoFar.addLevel(parseDepthKey(key));
			else
				// STEREOTYPE, and UNKNOWN too -- see this class's own documentation for why an
				// unrecognized selector word is folded in as a stereotype tag here, unlike
				// StyleAtomTrie#compileNode's UNKNOWN handling.
				childSignature = pathSoFar.addStereotype(key);

			if (child.isStar())
				childSignature = childSignature.addStar();
			flattenNode(child, childSignature, result);
		}
	}

	private static Map<PName, Value> toValueMap(Map<PName, PrioritizedValue> properties, int stereotypeCount) {
		final Map<PName, Value> result = new EnumMap<PName, Value>(PName.class);
		for (Map.Entry<PName, PrioritizedValue> ent : properties.entrySet()) {
			final PrioritizedValue pv = ent.getValue();
			final Specificity specificity = Specificity.atOrder(pv.getPriority()).withStereotypeCount(stereotypeCount);
			result.put(ent.getKey(), ValueImpl.of(pv.getLight(), pv.getDark(), specificity));
		}
		return result;
	}

	private static int parseDepthKey(String key) {
		return Integer.parseInt(key.substring("depth(".length(), key.length() - 1));
	}

}
