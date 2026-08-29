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
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.style.MergeStrategy;
import net.sourceforge.plantuml.style.PName;

/**
 * Pins {@link StyleMerge} against the legacy priority mechanics it replaces: plain
 * {@code Style#mergeWith} (priority-wins, with its exact tie-break, and its light/dark
 * combining via {@code DarkString#mergeWith}), and
 * {@code StyleBuilder#getMergedStyleSpecial}'s delta-shift-for-starred-declarations, which is
 * how a mindmap/wbs ancestor cascade lets a closer ancestor's catch-all rule beat a farther
 * one, or an element's own explicit declaration beat both.
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
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", 10));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor, PrioritizedValue.light("red", 20));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("red", merged.get(PName.FontColor).getValue());
		assertEquals(20, merged.get(PName.FontColor).getPriority());
	}

	@Test
	void lowerIncomingPriorityLosesToWhatIsAlreadyAccumulated() {
		final Map<PName, PrioritizedValue> accumulated = new EnumMap<PName, PrioritizedValue>(PName.class);
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", 20));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor, PrioritizedValue.light("red", 10));

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
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", 15));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor, PrioritizedValue.light("red", 15));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("black", merged.get(PName.FontColor).getValue());
	}

	@Test
	void aLightOnlyAndADarkOnlyValueCombineInsteadOfOneWinning() {
		// The core of the light/dark fix: merging a light-only value with a dark-only one for
		// the very same property does not pick a winner, it folds them into one value carrying
		// both -- whichever order they are merged in.
		final PrioritizedValue light = PrioritizedValue.light("black", 5);
		final PrioritizedValue dark = PrioritizedValue.dark("white", 50);

		final PrioritizedValue lightThenDark = dark.mergeWith(light);
		assertEquals("black", lightThenDark.getLight());
		assertEquals("white", lightThenDark.getDark());

		final PrioritizedValue darkThenLight = light.mergeWith(dark);
		assertEquals("black", darkThenLight.getLight());
		assertEquals("white", darkThenLight.getDark());
	}

	@Test
	void twoLightValuesNeverCombineOnlyThePriorityWinnerSurvives() {
		final PrioritizedValue merged = PrioritizedValue.light("red", 99).mergeWith(PrioritizedValue.light("black", 1));
		assertEquals("red", merged.getLight());
		assertNull(merged.getDark());
	}

	@Test
	void keepExistingValueOfStereotypeProtectsABoostedValueEvenFromAHigherIncomingPriority() {
		final Map<PName, PrioritizedValue> accumulated = new EnumMap<PName, PrioritizedValue>(PName.class);
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", StyleMerge.DELTA_PRIORITY_FOR_STEREOTYPE + 500));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor,
				PrioritizedValue.light("red", StyleMerge.DELTA_PRIORITY_FOR_STEREOTYPE + 999999));

		final Map<PName, PrioritizedValue> kept = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.KEEP_EXISTING_VALUE_OF_STEREOTYPE);
		assertEquals("black", kept.get(PName.FontColor).getValue());

		// The same pair, with the plain strategy, lets the higher raw priority win as usual.
		final Map<PName, PrioritizedValue> overwritten = StyleMerge.mergeInto(accumulated, incoming,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("red", overwritten.get(PName.FontColor).getValue());
	}

	@Test
	void keepExistingValueOfStereotypeDoesNotProtectAnUnboostedValue() {
		// Below the threshold: the strategy does not kick in, ordinary priority comparison
		// applies and the higher incoming priority wins.
		final Map<PName, PrioritizedValue> accumulated = new EnumMap<PName, PrioritizedValue>(PName.class);
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", StyleMerge.DELTA_PRIORITY_FOR_STEREOTYPE - 1));
		final Map<PName, PrioritizedValue> incoming = new EnumMap<PName, PrioritizedValue>(PName.class);
		incoming.put(PName.FontColor, PrioritizedValue.light("red", StyleMerge.DELTA_PRIORITY_FOR_STEREOTYPE + 1));

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
				PrioritizedValue.light("red", 50));
		final CompiledStyleRule laterButLowerPriority = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.light("blue", 5));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeAll(
				Arrays.asList(earlyButHigherPriority, laterButLowerPriority), MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("red", merged.get(PName.FontColor).getValue());
	}

	@Test
	void mergeAllCombinesALightMatchAndADarkMatchForTheSameProperty() {
		final CompiledStyleRule lightMatch = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.light("black", 1));
		final CompiledStyleRule darkMatch = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.dark("white", 2));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeAll(Arrays.asList(lightMatch, darkMatch),
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("black", merged.get(PName.FontColor).getLight());
		assertEquals("white", merged.get(PName.FontColor).getDark());
	}

	@Test
	void shiftPriorityMovesEveryValueByDeltaButKeepsTheProperties() {
		final Map<PName, PrioritizedValue> values = new EnumMap<PName, PrioritizedValue>(PName.class);
		values.put(PName.FontColor, PrioritizedValue.light("red", 10));
		values.put(PName.BackGroundColor, PrioritizedValue.light("yellow", 20));

		final Map<PName, PrioritizedValue> shifted = StyleMerge.shiftPriority(values, -1000);
		assertEquals(-990, shifted.get(PName.FontColor).getPriority());
		assertEquals(-980, shifted.get(PName.BackGroundColor).getPriority());
		assertEquals("red", shifted.get(PName.FontColor).getValue());
	}

	@Test
	void deltaForStarredOnlyShiftsStarredMatches() {
		final CompiledStyleRule ownDeclaration = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.light("black", 100));
		final CompiledStyleRule catchAll = rule(LevelConstraint.of(2, true), PName.FontColor,
				PrioritizedValue.light("red", 100));

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeAllWithDeltaForStarred(
				Arrays.asList(ownDeclaration, catchAll), -50, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		// Same starting priority (100), but the starred one was shifted down to 50: the
		// non-starred, unshifted declaration wins.
		assertEquals("black", merged.get(PName.FontColor).getValue());
	}

	@Test
	void ancestorCascadeLetsACloserAncestorsCatchAllBeatAFartherOne() {
		// Reproduces Idea#getStyle(): one query per ancestor level, each folded with its own
		// (decreasing) delta, the partial results then merged together.
		final int stepByParent = 1000;

		// The element's own declaration (non-star), declared early in the file (priority 1).
		final CompiledStyleRule self = rule(LevelConstraint.none(), PName.FontColor, PrioritizedValue.light("black", 1));
		final Map<PName, PrioritizedValue> selfResolved = StyleMerge.mergeAllWithDeltaForStarred(
				Arrays.asList(self), stepByParent * 1, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		// The immediate parent's catch-all (depth(0)*), declared later (priority 5): closer,
		// so it gets the larger remaining delta.
		final CompiledStyleRule parent = rule(LevelConstraint.of(0, true), PName.FontColor,
				PrioritizedValue.light("blue", 5));
		final Map<PName, PrioritizedValue> parentResolved = StyleMerge.mergeAllWithDeltaForStarred(
				Arrays.asList(parent), stepByParent * 1, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		// The grandparent's catch-all, declared even later (priority 9), so it would win on raw
		// priority alone -- but it is farther away, so it must lose once both deltas are applied.
		final CompiledStyleRule grandParent = rule(LevelConstraint.of(0, true), PName.FontColor,
				PrioritizedValue.light("green", 9));
		final Map<PName, PrioritizedValue> grandParentResolved = StyleMerge.mergeAllWithDeltaForStarred(
				Arrays.asList(grandParent), stepByParent * 0, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		Map<PName, PrioritizedValue> result = selfResolved;
		result = StyleMerge.mergeInto(result, parentResolved, MergeStrategy.OVERWRITE_EXISTING_VALUE);
		result = StyleMerge.mergeInto(result, grandParentResolved, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		assertEquals("blue", result.get(PName.FontColor).getValue());
	}

	@Test
	void ancestorCascadeStillLetsTheElementsOwnDeclarationWinWhenItsPriorityIsHighEnough() {
		final int stepByParent = 1000;

		// The element's own declaration, declared very late in the file (priority 999).
		final CompiledStyleRule self = rule(LevelConstraint.none(), PName.FontColor,
				PrioritizedValue.light("black", 999));
		final Map<PName, PrioritizedValue> selfResolved = StyleMerge.mergeAllWithDeltaForStarred(
				Arrays.asList(self), stepByParent * 1, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		// The parent's catch-all, declared early (priority 1), shifted down further still.
		final CompiledStyleRule parent = rule(LevelConstraint.of(0, true), PName.FontColor,
				PrioritizedValue.light("blue", 1));
		final Map<PName, PrioritizedValue> parentResolved = StyleMerge.mergeAllWithDeltaForStarred(
				Arrays.asList(parent), 0, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		final Map<PName, PrioritizedValue> result = StyleMerge.mergeInto(selfResolved, parentResolved,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals("black", result.get(PName.FontColor).getValue());
	}

	@Test
	void mergeAllWithDeltaLeavesNonStarredMatchesAtTheirOwnPriority() {
		final CompiledStyleRule nonStarred = rule(LevelConstraint.of(3, false), PName.FontColor,
				PrioritizedValue.light("black", 42));
		final List<CompiledStyleRule> matches = Arrays.asList(nonStarred);

		final Map<PName, PrioritizedValue> merged = StyleMerge.mergeAllWithDeltaForStarred(matches, 12345,
				MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertEquals(42, merged.get(PName.FontColor).getPriority());
	}

	@Test
	void mergeIntoDoesNotMutateTheAccumulatedMapPassedIn() {
		final Map<PName, PrioritizedValue> accumulated = new LinkedHashMap<PName, PrioritizedValue>();
		accumulated.put(PName.FontColor, PrioritizedValue.light("black", 1));
		final Map<PName, PrioritizedValue> incoming = new LinkedHashMap<PName, PrioritizedValue>();
		incoming.put(PName.FontColor, PrioritizedValue.light("red", 99));

		StyleMerge.mergeInto(accumulated, incoming, MergeStrategy.OVERWRITE_EXISTING_VALUE);
		assertFalse("red".equals(accumulated.get(PName.FontColor).getValue()));
	}

}
