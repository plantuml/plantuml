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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 * Two ways to bring rules in, mirroring the legacy {@code StyleBuilder}'s two loading paths
 * exactly:
 * <ul>
 * <li>{@link #build(RawStyleSheet)} is the counterpart of {@code StyleBuilder#loadInternal},
 * used to load a whole base .skin file. Like {@code loadInternal}, it rejects a starred
 * selector outright: a real .skin file never needs one (none of the bundled skins declare
 * one), so seeing one there is almost certainly a mistake, not a deliberate ancestor-cascade
 * catch-all.</li>
 * <li>{@link #mute(RawStyleSheet)} is the counterpart of {@code StyleBuilder#muteStyle}, used
 * to fold a hand-written {@code <style>...</style>} block (or an imported style sheet, or a
 * single-line {@code <style>...</style>}) on top of an already-loaded sheet. Unlike
 * {@link #build}, it happily accepts a starred selector -- {@code depth(2)* { ... }} written
 * by hand is exactly the real-world case a mindmap/wbs ancestor cascade needs -- and, like
 * {@code muteStyle}, it never mutates the sheet it is called on: it returns a new one, built
 * from a copy of this sheet's tree, so the original stays reusable for the next diagram.</li>
 * </ul>
 */
public final class MergedStyleSheet {

	private final Map<String, String> variables;
	private final MergedStyleNode base;

	// The very same counter used to build (or last mute) this sheet, kept around so that a
	// later mute() continues numbering from where this sheet left off, instead of starting
	// over at 1 -- exactly like StyleBuilder#muteStyle carries its own counter field forward
	// (result.counter = this.counter) rather than each mute getting a fresh one. Without this,
	// an overlay's priorities could collide with (or even lose to) the base sheet's.
	private final AutomaticCounter counter;

	private MergedStyleSheet(Map<String, String> variables, MergedStyleNode base, AutomaticCounter counter) {
		this.variables = variables;
		this.base = base;
		this.counter = counter;
	}

	public static MergedStyleSheet build(RawStyleSheet raw) {
		rejectStarredRules(raw.getRules());

		final MergedStyleNode base = MergedStyleNode.newTopLevelContainer();

		// One counter for the whole sheet, @media content included -- exactly like the legacy
		// StyleBuilder is itself a single AutomaticCounter shared across a whole load, so
		// priorities stay comparable across every declaration in the file.
		final AutomaticCounter counter = new AutomaticCounterBasic();
		mergeInto(base, raw, counter);

		return new MergedStyleSheet(raw.getVariables(), base, counter);
	}

	/**
	 * Folds {@code overlay} on top of a copy of this sheet, continuing this sheet's own
	 * priority counter so every property {@code overlay} sets outranks whatever this sheet
	 * already had for it -- the same "last loaded wins" rule {@link MergedStyleNode#mergeRule}
	 * already applies within one sheet, just carried across two. Starred selectors are
	 * allowed here, unlike {@link #build(RawStyleSheet)}; see the class documentation.
	 *
	 * This sheet itself is left untouched: {@code this.getBase()} still resolves exactly as it
	 * did before the call, so it can be muted again independently for another diagram.
	 */
	public MergedStyleSheet mute(RawStyleSheet overlay) {
		final MergedStyleNode copy = base.copy();
		mergeInto(copy, overlay, counter);

		final Map<String, String> mergedVariables = new LinkedHashMap<String, String>(variables);
		mergedVariables.putAll(overlay.getVariables());

		return new MergedStyleSheet(mergedVariables, copy, counter);
	}

	/**
	 * Folds every rule in {@code raw} into {@code root}, {@code @media} content dispatched to its
	 * dark half exactly as {@link #build(RawStyleSheet)} and {@link #mute(RawStyleSheet)} do --
	 * exposed (rather than kept private to those two) so that a caller needing a one-shot tree
	 * that is immediately flattened back to legacy {@code net.sourceforge.plantuml.style.Style}
	 * objects (see {@code net.sourceforge.plantuml.style.StyleLoader#parseStyleText}) can reuse
	 * this exact merging logic against an external {@link AutomaticCounter} -- a plain
	 * {@link MergedStyleSheet} cannot be used there since it always manages its own counter.
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
						+ "only an inline <style> override merged with MergedStyleSheet#mute (the counterpart of "
						+ "StyleBuilder#muteStyle) may use one.");

			rejectStarredRules(rule.getChildren());
		}
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	/** The one merged tree for every declaration, light and dark alike. */
	public MergedStyleNode getBase() {
		return base;
	}

	@Override
	public String toString() {
		return "variables=" + variables + '\n' + base;
	}

}
