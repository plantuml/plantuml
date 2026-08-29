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

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.parser2.StyleQuery;
import net.sourceforge.plantuml.utils.BlocLines;

/**
 * Pins today's behavior for an element carrying SEVERAL stereotype labels (e.g.
 * {@code <<foo>><<bar>>}), reached through {@link StyleSignature#withTOBECHANGED}: nothing
 * else pins it down, and it is the crux of whether a future {@code StyleQuery}-based rewrite of
 * {@code getStyleSignature()}-style call sites (see {@code EntityImageActivity} and its ~35
 * siblings) can be purely mechanical, or must first settle a real behavior question.
 *
 * {@code withTOBECHANGED} now folds every label into ONE signature requiring all of them at once,
 * instead of resolving each label as its own separate query and picking a winner (the old
 * {@code StyleSignatures} composite's job) -- exactly like a CSS selector requiring several
 * classes at once ({@code .foo.bar}) is one selector, not two. Two consequences fall out, neither
 * obvious from reading {@code withTOBECHANGED} alone:
 *
 * <ul>
 * <li>a conflict between two single-stereotype declarations that both match (an element tagged
 * {@code <<foo>><<bar>>} satisfies both a lone {@code .foo} and a lone {@code .bar} selector, each
 * requiring only one of its two stereotypes) is settled purely by which declaration comes LATER
 * in the {@code .skin} file -- {@link Specificity}'s source-order tiebreak -- never by the LABEL
 * ORDER on the element's own tag, since the query itself no longer has an order once its labels
 * are folded into one signature;
 * <li>a declaration that itself requires several stereotypes at once (written as nested selectors,
 * {@code .foo { .bar { ... } } }, since there is no single-token compound-stereotype selector
 * syntax) always wins over either single-stereotype declaration, whatever their relative file
 * order -- {@link Specificity}'s stereotype-count tier outranks the source-order tiebreak, exactly
 * like a CSS declaration naming two classes always outranks one naming only one of them.
 * </ul>
 */
class StyleSignatureStereotypeFanOutTest {

	private static StyleBuilder builderFrom(String skinText) throws Exception {
		final Collection<Style> styles = StyleLoader.parseStyleText(BlocLines.getWithNewlines(skinText),
				new AutomaticCounterBasic());
		return new StyleBuilder().muteStyle(styles);
	}

	@Test
	void aConflictBetweenTwoSingleStereotypeDeclarationsIsSettledByFileOrderNotByTheElementsOwnLabelOrder()
			throws Exception {
		final String skin = "" //
				+ ".foo {\n  BackGroundColor red\n}\n" //
				+ ".bar {\n  BackGroundColor blue\n}\n"; // declared later, so it wins any tie
		final StyleBuilder builder = builderFrom(skin);
		final StyleQuery base = StyleQuery.of(Arrays.asList(SName.root));

		final Style fooLabelFirst = builder.getMergedStyle(base.withTOBECHANGED(Stereotype.build("<<foo>><<bar>>")));
		assertEquals("blue", fooLabelFirst.value(PName.BackGroundColor).asString());

		final Style barLabelFirst = builder.getMergedStyle(base.withTOBECHANGED(Stereotype.build("<<bar>><<foo>>")));
		assertEquals("blue", barLabelFirst.value(PName.BackGroundColor).asString());
	}

	@Test
	void aDeclarationRequiringBothStereotypesAtOnceAlwaysWinsOverEitherSingleOne() throws Exception {
		final String skin = "" //
				+ ".foo {\n  BackGroundColor red\n}\n" //
				+ ".bar {\n  BackGroundColor blue\n}\n" //
				+ ".foo {\n  .bar {\n    BackGroundColor green\n  }\n}\n";
		final StyleBuilder builder = builderFrom(skin);
		final StyleQuery base = StyleQuery.of(Arrays.asList(SName.root));

		// "green" (the {foo, bar} declaration) always wins now, whatever the label order on the
		// element and whatever file order the three declarations were in.
		final Style result = builder.getMergedStyle(base.withTOBECHANGED(Stereotype.build("<<foo>><<bar>>")));
		assertEquals("green", result.value(PName.BackGroundColor).asString());

		final Style resultReversed = builder.getMergedStyle(base.withTOBECHANGED(Stereotype.build("<<bar>><<foo>>")));
		assertEquals("green", resultReversed.value(PName.BackGroundColor).asString());
	}

}
