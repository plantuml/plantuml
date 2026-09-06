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

import java.util.List;

import net.sourceforge.plantuml.style.AutomaticCounter;
import net.sourceforge.plantuml.style.AutomaticCounterBasic;

/**
 * The canonical, merged reading of one .skin file: a single {@link MergedStyleNode} tree.
 *
 * There is deliberately no separate tree for {@code @media (prefers-color-scheme:dark)}
 * content: a dark declaration is merged into the very same node as its light counterpart,
 * the two folded into one {@link PrioritizedValue} per property (see
 * {@link MergedStyleNode#mergeRule}) -- exactly how the legacy loader does it too
 * ({@code net.sourceforge.plantuml.style.StyleBuilder#loadInternal} merging a dark-tagged
 * {@code Style} into the same signature's existing one via {@code Style#mergeWith}). Which
 * theme actually gets drawn is left to whatever consumes the resolved value (the legacy
 * pipeline picks it as late as {@code ValueImpl#asColor}/{@code HColor#withDark}), not decided
 * once and for all here.
 *
 * {@link #build(RawStyleSheet)} is the counterpart of {@code StyleBuilder#loadInternal}, used
 * to load a whole base .skin file straight into a queryable tree (see
 * {@code net.sourceforge.plantuml.style.StyleBuilder#forBaseStyleText} and
 * {@code net.sourceforge.plantuml.style.StyleIndex}). Like {@code loadInternal}, it rejects a
 * starred selector outright: a real .skin file never needs one (none of the bundled skins
 * declare one), so seeing one there is almost certainly a mistake, not a deliberate
 * ancestor-cascade catch-all.
 *
 * An overlay ({@code <style>...</style>}, an imported style sheet, a single-line
 * {@code <style>...</style>}) is not built into a second {@link MergedStyleSheet} on top of
 * this one: it stays on the legacy, flattened path instead (see
 * {@code net.sourceforge.plantuml.style.StyleLoader#parseStyleText} and
 * {@code net.sourceforge.plantuml.style.StyleIndex#withMuted}) -- an overlay is small and
 * never cached across diagrams the way a base skin is, and it can carry {@code Style}
 * capabilities (programmatic forced overrides, {@code ValueColor}) that this tree's
 * {@link PrioritizedValue} does not represent.
 */
public final class MergedStyleSheet {

	private final MergedStyleNode base;

	// The very same counter used to build this sheet, kept around so that a later overlay
	// parsed against it (see net.sourceforge.plantuml.style.StyleBuilder#forBaseStyleText)
	// keeps numbering from where this sheet left off, instead of starting over at 1 --
	// exactly like the legacy StyleBuilder#muteStyle carries its own counter field forward
	// (result.counter = this.counter). Without this, an overlay's priorities could collide
	// with (or even lose to) the base sheet's.
	private final AutomaticCounter counter;

	private MergedStyleSheet(MergedStyleNode base, AutomaticCounter counter) {
		this.base = base;
		this.counter = counter;
	}

	public static MergedStyleSheet build(RawStyleSheet raw) {
		return build(raw, new AutomaticCounterBasic());
	}

	/**
	 * Same as {@link #build(RawStyleSheet)}, but drawing declaration order (see
	 * {@code net.sourceforge.plantuml.style.Specificity#atOrder(int)}) from {@code counter}
	 * instead of a fresh one private to this call -- so a base sheet built this way and a later
	 * overlay parsed against the very same counter (e.g. {@code StyleBuilder} itself, an
	 * {@code AutomaticCounter}) stay numbered on one continuous scale. Used by
	 * {@code StyleBuilder#forBaseStyleText} so a base .skin file compiled straight into a
	 * {@code net.sourceforge.plantuml.style.StyleIndex} (no intermediate flattening) still leaves
	 * the builder's counter exactly where the old per-{@code Style} loading loop would have.
	 */
	public static MergedStyleSheet build(RawStyleSheet raw, AutomaticCounter counter) {
		rejectStarredRules(raw.getRules());

		final MergedStyleNode base = MergedStyleNode.newTopLevelContainer();
		mergeInto(base, raw, counter);

		return new MergedStyleSheet(base, counter);
	}

	/** No declaration at all -- mirroring {@code net.sourceforge.plantuml.style.StyleIndex#empty()}. */
	public static MergedStyleSheet empty() {
		return EMPTY;
	}

	private static final MergedStyleSheet EMPTY = new MergedStyleSheet(MergedStyleNode.newTopLevelContainer(),
			new AutomaticCounterBasic());

	/**
	 * Folds every rule in {@code raw} into {@code root}, {@code @media} content dispatched to its
	 * dark half exactly as {@link #build(RawStyleSheet)} does -- exposed (rather than kept private
	 * to it) so that a caller needing a one-shot tree that is immediately flattened back to legacy
	 * {@code net.sourceforge.plantuml.style.Style} objects (see
	 * {@code net.sourceforge.plantuml.style.StyleLoader#parseStyleText}) can reuse this exact
	 * merging logic against an external {@link AutomaticCounter} -- a plain {@link MergedStyleSheet}
	 * cannot be used there since it always manages its own counter.
	 */
	public static void mergeInto(MergedStyleNode root, RawStyleSheet raw, AutomaticCounter counter) {
		for (RawStyleRule rule : raw.getRules()) {
			if (rule.isMediaBlock() == false) {
				root.mergeRule(rule, counter, false);
				continue;
			}

			// Like the legacy parser, the actual @-rule condition text is not inspected: its
			// mere presence switches its content to the dark half of PrioritizedValue.
			for (RawStyleRule nested : rule.getChildren())
				root.mergeRule(nested, counter, true);
		}
	}

	/**
	 * Mirrors {@code StyleBuilder#loadInternal}'s {@code if (signature.isStarred()) throw ...}:
	 * a base style sheet declaring a starred selector -- at any depth, {@code @media} content
	 * included -- is rejected rather than silently accepted.
	 */
	private static void rejectStarredRules(List<RawStyleRule> rules) {
		for (RawStyleRule rule : rules) {
			if (rule.isMediaBlock() == false && rule.isStar())
				throw new IllegalArgumentException("A base style sheet cannot declare a starred selector ("
						+ rule.getSelectors() + "*): that mirrors the legacy StyleBuilder#loadInternal guard -- "
						+ "only an inline <style> override (parsed through parseStyleText, not this class) may use one.");

			rejectStarredRules(rule.getChildren());
		}
	}

	/** The one merged tree for every declaration, light and dark alike. */
	public MergedStyleNode getBase() {
		return base;
	}

	@Override
	public String toString() {
		return base.toString();
	}

}
