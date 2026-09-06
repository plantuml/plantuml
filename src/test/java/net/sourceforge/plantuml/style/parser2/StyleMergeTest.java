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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.style.MergeStrategy;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Specificity;

/**
 * Pins {@link StyleMerge} against the legacy mechanics it replaces: plain
 * {@code Style#mergeWith} ({@link Specificity}-wins, with its exact tie-break, and its light/dark
 * combining via {@code DarkString#mergeWith}), and
 * {@code StyleBuilder#getMergedStyleSpecial}'s ancestor-rank cascade, which is how a mindmap/wbs
 * ancestor cascade lets a closer ancestor's catch-all rule beat a farther one.
 */
class StyleMergeTest {

	private static CompiledStyleRule rule(LevelConstraint levelConstraint, PName name, PrioritizedValue value) {
		final Map<PName, PrioritizedValue> values = new EnumMap<PName, PrioritizedValue>(PName.class);
		values.put(name, value);
		return new CompiledStyleRule(levelConstraint, values);
	}

	@Test
	void higherPriorityWinsRegardlessOfArgumentOrder() {
		final Map<PName, PrioritizedValue> accumulated = new EnumMap<PName, PrioritizedValue>(PName.class);
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", Specificity.atOrder(10)));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor, PrioritizedValue.light("red", Specificity.atOrder(20)));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("red", merged.get(PName.FontColor).getValue());
		assertEquals(0, merged.get(PName.FontColor).getSpecificity().compareTo(Specificity.atOrder(20)));
	}

	@Test
	void lowerIncomingPriorityLosesToWhatIsAlreadyAccumulated() {
		final Map<PName, PrioritizedValue> accumulated = new EnumMap<PName, PrioritizedValue>(PName.class);
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", Specificity.atOrder(20)));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor, PrioritizedValue.light("red", Specificity.atOrder(10)));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("black", merged.get(PName.FontColor).getValue());
	}

	@Test
	void onAnExactPriorityTieTheAlreadyAccumulatedValueWins() {
		// Mirrors DarkString#mergeWith's isBigger(this, other): strictly greater wins, so an
		// exact tie falls through to "other", i.e. the value already accumulated -- not the
		// incoming one.
		final Map<PName, PrioritizedValue> accumulated = new EnumMap<PName, PrioritizedValue>(PName.class);
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", Specificity.atOrder(15)));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor, PrioritizedValue.light("red", Specificity.atOrder(15)));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("black", merged.get(PName.FontColor).getValue());
	}

	@Test
	void aLightOnlyAndADarkOnlyValueCombineInsteadOfOneWinning() {
		// The core of the light/dark fix: merging a light-only value with a dark-only one for
		// the very same property does not pick a winner, it folds them into one value carrying
		// both -- whichever order they are merged in.
		final PrioritizedValue light = PrioritizedValue.light("black", Specificity.atOrder(5));
		final PrioritizedValue dark = PrioritizedValue.dark("white", Specificity.atOrder(50));

		final PrioritizedValue lightThenDark = dark.mergeWith(light);
		assertEquals("black", lightThenDark.getLight());
		assertEquals("white", lightThenDark.getDark());

		final PrioritizedValue darkThenLight = light.mergeWith(dark);
		assertEquals("black", darkThenLight.getLight());
		assertEquals("white", darkThenLight.getDark());
	}

	@Test
	void twoLightValuesNeverCombineOnlyThePriorityWinnerSurvives() {
		final PrioritizedValue merged = PrioritizedValue.light("red", Specificity.atOrder(99))
				.mergeWith(PrioritizedValue.light("black", Specificity.atOrder(1)));
		assertEquals("red", merged.getLight());
		assertNull(merged.getDark());
	}

	@Test
	void keepExistingValueOfStereotypeProtectsABoostedValueEvenFromAHigherIncomingPriority() {
		final Map<PName, PrioritizedValue> accumulated = new EnumMap<PName, PrioritizedValue>(PName.class);
		accumulated.put(PName.FontColor,
				PrioritizedValue.light("black", Specificity.atOrder(500).withStereotypeCount(1)));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor,
				PrioritizedValue.light("red", Specificity.atOrder(999999).withStereotypeCount(1)));

		final Map<PName, PrioritizedValue> kept = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.KEEP_EXISTING_VALUE_OF_STEREOTYPE);
		assertEquals("black", kept.get(PName.FontColor).getValue());

		// The same pair, with the plain strategy: both require a stereotype, so that tier ties
		// and the higher order wins as usual.
		final Map<PName, PrioritizedValue> overwritten = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("red", overwritten.get(PName.FontColor).getValue());
	}

	@Test
	void keepExistingValueOfStereotypeDoesNotProtectAnUnboostedValue() {
		// The accumulated value requires no stereotype at all: the strategy does not kick in,
		// ordinary specificity comparison applies and the higher incoming order wins.
		final Map<PName, PrioritizedValue> accumulated = new EnumMap<PName, PrioritizedValue>(PName.class);
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", Specificity.atOrder(1)));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor, PrioritizedValue.light("red", Specificity.atOrder(2)));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.KEEP_EXISTING_VALUE_OF_STEREOTYPE);
		assertEquals("red", merged.get(PName.FontColor).getValue());
	}

	@Test
	void mergeAllFoldsEveryMatchByPriorityNotByTrieOrder() {
		// Two matches disagreeing on FontColor: the *lower*-priority one comes LAST in the
		// list, which is exactly the case a plain Map#putAll loop over trie-visit order would
		// get wrong.
		final CompiledStyleRule earlyButHigherPriority = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.light("red", Specificity.atOrder(50)));
		final CompiledStyleRule laterButLowerPriority = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.light("blue", Specificity.atOrder(5)));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeAll(
				Arrays.asList(earlyButHigherPriority, laterButLowerPriority), MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("red", merged.get(PName.FontColor).getValue());
	}

	@Test
	void mergeAllCombinesALightMatchAndADarkMatchForTheSameProperty() {
		final CompiledStyleRule lightMatch = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.light("black", Specificity.atOrder(1)));
		final CompiledStyleRule darkMatch = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.dark("white", Specificity.atOrder(2)));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeAll(Arrays.asList(lightMatch, darkMatch),
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("black", merged.get(PName.FontColor).getLight());
		assertEquals("white", merged.get(PName.FontColor).getDark());
	}

	@Test
	void ancestorRankOnlyAppliesToStarredMatches() {
		// The element's own (non-starred) declaration, at a much higher file-order than the
		// catch-all -- with the old int-priority scheme this would have won outright. Once the
		// catch-all is promoted into the ancestor cascade, though, it always outranks a
		// non-cascaded declaration whatever the latter's own order (see Specificity's own
		// javadoc): the ancestor-cascade tier is strictly more significant than order.
		final CompiledStyleRule ownDeclaration = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.light("black", Specificity.atOrder(1000000)));
		final CompiledStyleRule catchAll = rule(LevelConstraint.of(2, true), PName.FontColor,
				PrioritizedValue.light("red", Specificity.atOrder(1)));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeAllWithAncestorRank(
				Arrays.asList(ownDeclaration, catchAll), -1, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		assertEquals("red", merged.get(PName.FontColor).getValue());
	}

	@Test
	void ancestorCascadeLetsACloserAncestorsCatchAllBeatAFartherOne() {
		// Reproduces Idea#getStyle(): one query per ancestor level, each resolved at its own
		// ancestor rank (0 = the element's own level, more negative = farther up), the partial
		// results then merged together with a plain, non-cascading mergeInto.
		final CompiledStyleRule parentCatchAll = rule(LevelConstraint.of(0, true), PName.FontColor,
				PrioritizedValue.light("blue", Specificity.atOrder(5)));
		final Map<PName, PrioritizedValue> parentResolved = StyleMerge.mergeAllWithAncestorRank(
				Arrays.asList(parentCatchAll), -1, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		// Declared much later (order 9), so it would win on file order alone -- but it is
		// farther away, so it must lose once both ancestor ranks are taken into account.
		final CompiledStyleRule grandParentCatchAll = rule(LevelConstraint.of(0, true), PName.FontColor,
				PrioritizedValue.light("green", Specificity.atOrder(9)));
		final Map<PName, PrioritizedValue> grandParentResolved = StyleMerge.mergeAllWithAncestorRank(
				Arrays.asList(grandParentCatchAll), -2, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		final Map<PName, PrioritizedValue> result = StyleMerge.mergeInto(parentResolved, grandParentResolved,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);

		assertEquals("blue", result.get(PName.FontColor).getValue());
	}

	@Test
	void mergeAllWithAncestorRankLeavesNonStarredMatchesUncascaded() {
		final CompiledStyleRule nonStarred = rule(LevelConstraint.of(3, false), PName.FontColor,
				PrioritizedValue.light("black", Specificity.atOrder(42)));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeAllWithAncestorRank(Arrays.asList(nonStarred),
				12345, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		// Untouched: still exactly Specificity.atOrder(42), not promoted into the ancestor
		// cascade and not given any stereotype count.
		assertEquals(0, merged.get(PName.FontColor).getSpecificity().compareTo(Specificity.atOrder(42)));
	}

	@Test
	void mergeIntoDoesNotMutateTheAccumulatedMapPassedIn() {
		final Map<PName, PrioritizedValue> accumulated = new LinkedHashMap<PName, PrioritizedValue>();
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", Specificity.atOrder(1)));
		final Map<PName, PrioritizedValue> incoming = new LinkedHashMap<PName, PrioritizedValue>();
		incoming.put(PName.FontColor, PrioritizedValue.light("red", Specificity.atOrder(99)));

		StyleMerge.mergeInto(accumulated, incoming, MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertFalse("red".equals(accumulated.get(PName.FontColor).getValue()));
	}

}
