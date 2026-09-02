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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.BlocLines;

/**
 * Pins today's exact behavior for an element carrying SEVERAL stereotype labels (e.g.
 * {@code <<foo>><<bar>>}), reached through {@link StyleSignatureBasic#withTOBECHANGED}: nothing
 * else pinned it before, and it is the crux of whether a future {@code StyleQuery}-based rewrite
 * of {@code getStyleSignature()}-style call sites (see {@code EntityImageActivity} and its ~35
 * siblings) can be purely mechanical, or must first settle a real behavior question.
 *
 * {@link StyleSignatures#getMergedStyle} resolves each label as its OWN, separate query -- one
 * full trie lookup per label -- and folds the results with
 * {@link MergeStrategy#KEEP_EXISTING_VALUE_OF_STEREOTYPE} ({@link Style#mergeWith} refuses to
 * overwrite a property once some earlier match already set it at stereotype-boosted priority).
 * Two consequences fall out, neither obvious from reading {@code withTOBECHANGED} alone:
 *
 * <ul>
 * <li>a conflict between two single-stereotype declarations is settled by the LABEL ORDER on the
 * element's own tag ({@code <<foo>><<bar>>} vs {@code <<bar>><<foo>>}), not by which declaration
 * comes first in the {@code .skin} file;
 * <li>a declaration that itself requires several stereotypes at once (written as nested selectors,
 * {@code .foo { .bar { ... } } }, since there is no single-token compound-stereotype selector
 * syntax) can NEVER win here: neither the {@code {foo}} nor the {@code {bar}} per-label query
 * alone is a superset of {@code {foo, bar}}, so {@link StyleSignatureBasic#matchAll} always
 * rejects it, whichever label is queried first.
 * </ul>
 *
 * The already-tested, not-yet-wired {@code parser2} pipeline
 * ({@link net.sourceforge.plantuml.style.parser2.StyleMerge#mergeAll}) answers both differently:
 * one combined query carrying every stereotype atom at once, folded by declaration order/priority
 * -- so ties go to file order instead of label order, and the compound declaration above would
 * actually match. Whichever way that gets settled, these two tests make sure it is a deliberate
 * choice instead of an accidental side effect.
 */
class StyleSignatureStereotypeFanOutTest {

	private static StyleBuilder builderFrom(String skinText) throws Exception {
		final Collection<Style> styles = StyleLoader.parseStyleText(BlocLines.getWithNewlines(skinText),
				new AutomaticCounterBasic());
		return new StyleBuilder().muteStyle(styles);
	}

	@Test
	void aConflictBetweenTwoSingleStereotypeDeclarationsIsSettledByTheElementsOwnLabelOrder() throws Exception {
		final String skin = "" //
				+ ".foo {\n  BackGroundColor red\n}\n" //
				+ ".bar {\n  BackGroundColor blue\n}\n";
		final StyleBuilder builder = builderFrom(skin);
		final StyleSignature base = StyleSignatureBasic.of(SName.root);

		final Style fooLabelFirst = base.withTOBECHANGED(Stereotype.build("<<foo>><<bar>>")).getMergedStyle(builder);
		assertEquals("red", fooLabelFirst.value(PName.BackGroundColor).asString());

		final Style barLabelFirst = base.withTOBECHANGED(Stereotype.build("<<bar>><<foo>>")).getMergedStyle(builder);
		assertEquals("blue", barLabelFirst.value(PName.BackGroundColor).asString());
	}

	@Test
	void aDeclarationRequiringBothStereotypesAtOnceNeverWinsOverEitherSingleOne() throws Exception {
		final String skin = "" //
				+ ".foo {\n  BackGroundColor red\n}\n" //
				+ ".bar {\n  BackGroundColor blue\n}\n" //
				+ ".foo {\n  .bar {\n    BackGroundColor green\n  }\n}\n";
		final StyleBuilder builder = builderFrom(skin);
		final StyleSignature base = StyleSignatureBasic.of(SName.root);

		// "green" (the {foo, bar} declaration) can never win today, whatever the label order on
		// the element -- only whichever single-stereotype declaration matches its first label
		// does (see the sibling test for why that one is "red").
		final Style result = base.withTOBECHANGED(Stereotype.build("<<foo>><<bar>>")).getMergedStyle(builder);
		assertEquals("red", result.value(PName.BackGroundColor).asString());

		final Style resultReversed = base.withTOBECHANGED(Stereotype.build("<<bar>><<foo>>")).getMergedStyle(builder);
		assertEquals("blue", resultReversed.value(PName.BackGroundColor).asString());
	}

}
