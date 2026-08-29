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
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.style.AutomaticCounter;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;

/**
 * One node of the canonical, merged style tree: unlike {@link RawStyleRule}, there is at
 * most one node per (selector name, {@code *}-or-not) pair at a given level, whatever comma
 * lists or repeated declarations produced it in the source file.
 *
 * Building this tree from a {@link RawStyleSheet} does exactly two things the raw parse
 * tree deliberately left undone:
 * <ul>
 * <li>a comma-separated selector list, e.g. {@code composite,package { title {...} } },
 * is expanded so each alternative gets its own independent node (so a later declaration
 * of {@code package { title {...} } } elsewhere only ever affects {@code package}, never
 * {@code composite});</li>
 * <li>repeated declarations of the very same selector path <b>with the same {@code *}-ness</b>,
 * e.g. two separate top-level {@code mindmapDiagram { ... }} blocks, are folded into a single
 * node: properties are overwritten by whichever declaration comes last in the file (the same
 * last-one-wins rule the legacy {@code Style#mergeWith} already applies), and children
 * accumulate across every occurrence.</li>
 * </ul>
 *
 * A selector declared once plain and once starred, e.g. {@code .europeStyle { node { FontSize
 * 20 } } } alongside {@code .europeStyle * { node { FontColor red } } }, is deliberately
 * <b>not</b> folded together the way two identically-starred declarations are: they land in two
 * separate child slots (see {@link #getOrCreateChild}), because {@code *} changes what the
 * declaration actually means -- FontSize 20 must stay on the Europe node alone, while FontColor
 * red must cascade to every descendant too. The legacy character-level parser never had to get
 * this right on purpose: its {@code Context#push} always allocates a brand new {@code Context}
 * per occurrence, so a plain and a starred declaration of the same name were simply never the
 * same object to begin with. Collapsing them into one node here (as an earlier version of this
 * class did, keying a child purely by selector name) would silently let FontSize leak onto
 * England, Germany and Spain along with FontColor -- a real regression, not a hypothetical one.
 *
 * What this tree still does <b>not</b> do: turn a nested path such as
 * {@code sequenceDiagram > participant} into the flat, order-independent signature
 * (a set of {@link SName}) the legacy {@link net.sourceforge.plantuml.style.StyleSignatureBasic}
 * matches against, or resolve {@code depth(n)} into an actual matching rule. Those are later
 * steps; see {@link MergedStyleSheet}.
 *
 * A property set inside {@code @media (prefers-color-scheme:dark) { ... }} is not kept apart
 * from its plain (light) counterpart either: both land in the very same node, folded into one
 * {@link PrioritizedValue} carrying both -- see {@link #mergeRule}.
 */
public final class MergedStyleNode {

	/**
	 * Plain (non-cascading) and starred (cascading) declarations of the very same selector name
	 * are kept as two independent child slots, never merged into one -- see {@link #star} for why.
	 */
	private final Map<SName, MergedStyleNode> namedChildren = new EnumMap<SName, MergedStyleNode>(SName.class);
	private final Map<SName, MergedStyleNode> starredNamedChildren = new EnumMap<SName, MergedStyleNode>(
			SName.class);
	private final Map<String, MergedStyleNode> otherChildren = new LinkedHashMap<String, MergedStyleNode>();
	private final Map<String, MergedStyleNode> starredOtherChildren = new LinkedHashMap<String, MergedStyleNode>();
	private final Map<String, RawSelector.Kind> otherChildKinds = new LinkedHashMap<String, RawSelector.Kind>();
	private final Map<PName, PrioritizedValue> properties = new EnumMap<PName, PrioritizedValue>(PName.class);

	/**
	 * Whether the selector that led to this exact node carried a trailing {@code *} -- fixed at
	 * construction (see why in the class javadoc): a node reached without a star and a node
	 * reached with one are simply different nodes, even when they happen to sit at the same
	 * selector name inside the same parent (see {@link #getOrCreateChild}).
	 */
	private final boolean star;

	private MergedStyleNode(boolean star) {
		this.star = star;
	}

	public static MergedStyleNode newTopLevelContainer() {
		return new MergedStyleNode(false);
	}

	/**
	 * Merges one raw declaration -- and, recursively, everything nested inside it -- into
	 * this node, which stands for its parent selector (the top-level container for a
	 * top-level declaration).
	 *
	 * {@code counter} assigns each property value an increasing priority as it is met, exactly
	 * like the legacy {@code StyleBuilder} (itself an {@link AutomaticCounter}) does when
	 * loading a .skin file -- so it must be the very same counter for every {@code mergeRule}
	 * call across one sheet, {@code @media} content included, for priorities to be comparable
	 * across every declaration in the file.
	 *
	 * {@code dark} says whether {@code rule} sits inside an {@code @media} block: every
	 * property it sets is then wrapped as a {@link PrioritizedValue#dark(String, int) dark}
	 * value instead of a {@link PrioritizedValue#light(String, int) light} one, and
	 * {@link PrioritizedValue#mergeWith(PrioritizedValue)} takes care of folding it together
	 * with whatever light value the very same property already has at this node (in file order,
	 * either one may come first). This mirrors the legacy parser's own {@code @media} handling
	 * ({@code net.sourceforge.plantuml.style.parser.StyleParser}'s {@code scheme} field): it
	 * does not look at the actual media condition text at all, it only cares that some
	 * {@code @}-rule switched parsing into "dark" mode.
	 */
	public void mergeRule(RawStyleRule rule, AutomaticCounter counter, boolean dark) {
		mergeRule(rule, counter, dark, new IdentityHashMap<RawStyleRule, Map<PName, PrioritizedValue>>());
	}

	/**
	 * {@code ownValuesCache} draws each property of a given {@link RawStyleRule} node its
	 * priority exactly once, no matter how many times this method ends up visiting that very
	 * same node -- which happens whenever an ancestor selector is a comma list, e.g.
	 * {@code composite,package { title { FontStyle bold } } }: the "title" rule is reached once
	 * per alternative ({@code composite}'s child, then {@code package}'s child), and without this
	 * cache each visit would draw a fresh priority from {@code counter}, silently giving
	 * {@code package}'s title a higher (later) priority than {@code composite}'s for the exact
	 * same declaration. The legacy parser never had this problem: its {@code Context} builds one
	 * {@code Value} per property and fans out that very instance to every accumulated signature,
	 * rather than redrawing one per alternative. Scoped fresh to one top-level
	 * {@link #mergeRule(RawStyleRule, AutomaticCounter, boolean)} call -- a {@link RawStyleRule}
	 * is only ever revisited within the recursion of the one top-level rule it is nested under,
	 * never shared across top-level rules or files.
	 */
	private void mergeRule(RawStyleRule rule, AutomaticCounter counter, boolean dark,
			Map<RawStyleRule, Map<PName, PrioritizedValue>> ownValuesCache) {
		if (rule.isMediaBlock())
			throw new IllegalArgumentException("An @media block is not a selector; merge its children instead");

		Map<PName, PrioritizedValue> ownValues = ownValuesCache.get(rule);
		if (ownValues == null) {
			ownValues = new EnumMap<PName, PrioritizedValue>(PName.class);
			for (Map.Entry<PName, String> ent : rule.getProperties().entrySet()) {
				final int priority = counter.getNextInt();
				ownValues.put(ent.getKey(), dark ? PrioritizedValue.dark(ent.getValue(), priority)
						: PrioritizedValue.light(ent.getValue(), priority));
			}
			ownValuesCache.put(rule, ownValues);
		}

		for (RawSelector selector : rule.getSelectors()) {
			final MergedStyleNode child = getOrCreateChild(selector, rule.isStar());
			for (Map.Entry<PName, PrioritizedValue> ent : ownValues.entrySet()) {
				final PrioritizedValue existing = child.properties.get(ent.getKey());
				child.properties.put(ent.getKey(), ent.getValue().mergeWith(existing));
			}
			for (RawStyleRule nested : rule.getChildren())
				child.mergeRule(nested, counter, dark, ownValuesCache);
		}
	}

	/**
	 * {@code star} picks which of the two child slots for this selector to use -- see the class
	 * javadoc for why a plain and a starred declaration of the very same name must never share
	 * one. Repeated declarations that agree on {@code *}-ness still land in, and merge within,
	 * the very same slot, exactly as before.
	 */
	private MergedStyleNode getOrCreateChild(RawSelector selector, boolean star) {
		if (selector.getKind() == RawSelector.Kind.NAME) {
			final Map<SName, MergedStyleNode> children = star ? starredNamedChildren : namedChildren;
			MergedStyleNode child = children.get(selector.getSName());
			if (child == null) {
				child = new MergedStyleNode(star);
				children.put(selector.getSName(), child);
			}
			return child;
		}

		final String key = selector.canonicalOtherKey();
		final Map<String, MergedStyleNode> children = star ? starredOtherChildren : otherChildren;
		MergedStyleNode child = children.get(key);
		if (child == null) {
			child = new MergedStyleNode(star);
			children.put(key, child);
			otherChildKinds.put(key, selector.getKind());
		}
		return child;
	}

	/** The plain (non-starred) child at this name, if any; the starred one otherwise, if any. */
	public MergedStyleNode getChild(SName name) {
		final MergedStyleNode plain = namedChildren.get(name);
		return plain != null ? plain : starredNamedChildren.get(name);
	}

	/** The plain (non-starred) child at this key, if any; the starred one otherwise, if any. */
	public MergedStyleNode getOtherChild(String canonicalKey) {
		final MergedStyleNode plain = otherChildren.get(canonicalKey);
		return plain != null ? plain : starredOtherChildren.get(canonicalKey);
	}

	/**
	 * Which {@link RawSelector.Kind} produced the "other" child stored under this canonical
	 * key -- {@code STEREOTYPE}, {@code DEPTH} or {@code UNKNOWN} -- so a later compilation
	 * step can tell a stereotype selector from an unrecognized one, which
	 * {@link RawSelector#canonicalOtherKey()} alone does not always disambiguate.
	 */
	public RawSelector.Kind getOtherChildKind(String canonicalKey) {
		return otherChildKinds.get(canonicalKey);
	}

	/** Children reached through a plain (non-starred) named selector. See {@link #getStarredNamedChildren()}. */
	public Map<SName, MergedStyleNode> getNamedChildren() {
		return namedChildren;
	}

	/**
	 * Children reached through a starred named selector, e.g. the {@code node} in
	 * {@code europeStyle * { node {...} } }. Kept apart from {@link #getNamedChildren()} rather
	 * than merged with it -- see the class javadoc -- so a caller building a child signature must
	 * visit both maps and {@code addStar()} unconditionally for entries found here.
	 */
	public Map<SName, MergedStyleNode> getStarredNamedChildren() {
		return starredNamedChildren;
	}

	/** Children reached through a plain (non-starred) stereotype/depth/unknown selector. See {@link #getStarredOtherChildren()}. */
	public Map<String, MergedStyleNode> getOtherChildren() {
		return otherChildren;
	}

	/** The starred counterpart of {@link #getOtherChildren()} -- see {@link #getStarredNamedChildren()}. */
	public Map<String, MergedStyleNode> getStarredOtherChildren() {
		return starredOtherChildren;
	}

	/**
	 * Properties declared anywhere for this exact selector, already merged -- light and dark
	 * declarations for the same property folded into one {@link PrioritizedValue}, per
	 * {@link #mergeRule}.
	 */
	public Map<PName, PrioritizedValue> getProperties() {
		return properties;
	}

	public PrioritizedValue getProperty(PName name) {
		return properties.get(name);
	}

	public boolean isStar() {
		return star;
	}

	/**
	 * A deep, fully independent copy of this node and everything under it -- so that
	 * {@link MergedStyleSheet#mute(RawStyleSheet)} can merge an overlay on top of a copy of
	 * the tree without disturbing the original, exactly the way the legacy
	 * {@code StyleBuilder#muteStyle} builds a brand new {@code StyleBuilder} (copying the
	 * storage first) rather than mutating the one it was called on. {@link PrioritizedValue}
	 * is itself immutable, so its instances are shared rather than copied.
	 */
	public MergedStyleNode copy() {
		final MergedStyleNode result = new MergedStyleNode(this.star);
		result.properties.putAll(this.properties);
		result.otherChildKinds.putAll(this.otherChildKinds);
		for (Map.Entry<SName, MergedStyleNode> ent : namedChildren.entrySet())
			result.namedChildren.put(ent.getKey(), ent.getValue().copy());
		for (Map.Entry<SName, MergedStyleNode> ent : starredNamedChildren.entrySet())
			result.starredNamedChildren.put(ent.getKey(), ent.getValue().copy());
		for (Map.Entry<String, MergedStyleNode> ent : otherChildren.entrySet())
			result.otherChildren.put(ent.getKey(), ent.getValue().copy());
		for (Map.Entry<String, MergedStyleNode> ent : starredOtherChildren.entrySet())
			result.starredOtherChildren.put(ent.getKey(), ent.getValue().copy());
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		print(sb, 0);
		return sb.toString();
	}

	private void print(StringBuilder sb, int indent) {
		for (Map.Entry<PName, PrioritizedValue> ent : properties.entrySet()) {
			indent(sb, indent);
			sb.append(ent.getKey()).append(" = ").append(ent.getValue()).append('\n');
		}
		printChildren(sb, indent, namedChildren);
		printChildren(sb, indent, starredNamedChildren);
		printChildren(sb, indent, otherChildren);
		printChildren(sb, indent, starredOtherChildren);
	}

	private static <K> void printChildren(StringBuilder sb, int indent, Map<K, MergedStyleNode> children) {
		for (Map.Entry<K, MergedStyleNode> ent : children.entrySet()) {
			indent(sb, indent);
			sb.append('[').append(ent.getKey()).append(ent.getValue().star ? " *" : "").append("] {\n");
			ent.getValue().print(sb, indent + 1);
			indent(sb, indent);
			sb.append("}\n");
		}
	}

	private static void indent(StringBuilder sb, int level) {
		for (int i = 0; i < level; i++)
			sb.append("  ");
	}

}
