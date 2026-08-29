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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.style.AutomaticCounterBasic;

import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;

/**
 * {@code plantuml.skin} never uses {@code depth(n)}, {@code *} or stereotype selectors (see
 * {@link CompiledStyleSheetTest} for the bundled-skin coverage), so this test builds small
 * synthetic {@link MergedStyleNode} trees by hand -- using the real {@link RawStyleRule} /
 * {@link MergedStyleNode#mergeRule} plumbing, not {@link StyleAtomTrie#insert} directly -- to
 * exercise the paths real .skin/style text can still reach: subset matching, comma-free
 * prefix sharing, stereotypes, and {@code depth(n)} / {@code *}.
 */
class StyleAtomTrieTest {

	private static RawStyleRule rule(String selector, boolean star) {
		return RawStyleRule.forSelectors(Collections.singletonList(RawSelector.classify(selector)), star);
	}

	@Test
	void aDeclarationMatchesOnlyWhenItsAtomsAreAllPresentInTheQuery() {
		// sequenceDiagram { participant { FontColor red } }
		final RawStyleRule participant = rule("participant", false);
		participant.putProperty(PName.FontColor, "red");
		final RawStyleRule sequenceDiagram = rule("sequenceDiagram", false);
		sequenceDiagram.addChild(participant);

		final MergedStyleNode base = MergedStyleNode.newTopLevelContainer();
		base.mergeRule(sequenceDiagram, new AutomaticCounterBasic(), false);
		final StyleAtomTrie trie = StyleAtomTrie.compile(base);

		final List<CompiledStyleRule> full = trie
				.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram, SName.participant)));
		assertEquals(1, full.size());
		assertEquals("red", full.get(0).getProperties().get(PName.FontColor));

		// Missing "participant": the participant-level declaration must not match.
		final List<CompiledStyleRule> partial = trie.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram)));
		assertTrue(partial.isEmpty());

		// Extra, unrelated atoms in the query do not prevent a match (subset test, not equality).
		final List<CompiledStyleRule> extra = trie
				.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram, SName.participant, SName.title)));
		assertEquals(1, extra.size());
	}

	@Test
	void exactDepthOnlyMatchesThatExactLevel() {
		// sequenceDiagram { depth(3) { FontColor blue } }
		final RawStyleRule depth3 = rule("depth(3)", false);
		depth3.putProperty(PName.FontColor, "blue");
		final RawStyleRule sequenceDiagram = rule("sequenceDiagram", false);
		sequenceDiagram.addChild(depth3);

		final MergedStyleNode base = MergedStyleNode.newTopLevelContainer();
		base.mergeRule(sequenceDiagram, new AutomaticCounterBasic(), false);
		final StyleAtomTrie trie = StyleAtomTrie.compile(base);

		assertEquals(1, trie
				.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram), Collections.<String> emptySet(),
						LevelConstraint.of(3, false)))
				.size());
		assertTrue(trie
				.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram), Collections.<String> emptySet(),
						LevelConstraint.of(4, false)))
				.isEmpty());
		// No level at all in the query: an exact-depth declaration cannot match.
		assertTrue(trie.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram))).isEmpty());
	}

	@Test
	void starredDepthMatchesItsLevelOrDeeper() {
		// sequenceDiagram { depth(2)* { FontColor red } }
		final RawStyleRule depth2Star = rule("depth(2)", true);
		depth2Star.putProperty(PName.FontColor, "red");
		final RawStyleRule sequenceDiagram = rule("sequenceDiagram", false);
		sequenceDiagram.addChild(depth2Star);

		final MergedStyleNode base = MergedStyleNode.newTopLevelContainer();
		base.mergeRule(sequenceDiagram, new AutomaticCounterBasic(), false);
		final StyleAtomTrie trie = StyleAtomTrie.compile(base);

		assertTrue(matchesAtLevel(trie, 1).isEmpty());
		assertEquals(1, matchesAtLevel(trie, 2).size());
		assertEquals(1, matchesAtLevel(trie, 5).size());
	}

	@Test
	void starredQueryOnlyReachesStarredDeclarations() {
		// sequenceDiagram { depth(2)* { FontColor red }  depth(2) { FontColor blue } }
		final RawStyleRule depth2Star = rule("depth(2)", true);
		depth2Star.putProperty(PName.FontColor, "red");
		final RawStyleRule sequenceDiagram = rule("sequenceDiagram", false);
		sequenceDiagram.addChild(depth2Star);

		final MergedStyleNode base = MergedStyleNode.newTopLevelContainer();
		base.mergeRule(sequenceDiagram, new AutomaticCounterBasic(), false);
		final StyleAtomTrie trie = StyleAtomTrie.compile(base);

		final StyleQuery ancestorLookupAtLevel5 = StyleQuery.of(setOf(SName.sequenceDiagram),
				Collections.<String> emptySet(), LevelConstraint.of(5, true));
		final List<CompiledStyleRule> matches = trie.findMatching(ancestorLookupAtLevel5);
		assertEquals(1, matches.size());
		assertEquals("red", matches.get(0).getProperties().get(PName.FontColor));
	}

	@Test
	void plainStarWithNoDepthMatchesAnyLevelIncludingStarredQueries() {
		// sequenceDiagram* { FontColor green }
		final RawStyleRule sequenceDiagramStar = rule("sequenceDiagram", true);
		sequenceDiagramStar.putProperty(PName.FontColor, "green");

		final MergedStyleNode base = MergedStyleNode.newTopLevelContainer();
		base.mergeRule(sequenceDiagramStar, new AutomaticCounterBasic(), false);
		final StyleAtomTrie trie = StyleAtomTrie.compile(base);

		assertEquals(1, trie.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram))).size());
		assertEquals(1,
				trie.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram), Collections.<String> emptySet(),
						LevelConstraint.of(9, true))).size());
	}

	@Test
	void stereotypeSelectorsRequireTheStereotypeAtomToBePresent() {
		// sequenceDiagram { .myStereotype { BackGroundColor yellow } }
		final RawStyleRule stereo = rule(".myStereotype", false);
		stereo.putProperty(PName.BackGroundColor, "yellow");
		final RawStyleRule sequenceDiagram = rule("sequenceDiagram", false);
		sequenceDiagram.addChild(stereo);

		final MergedStyleNode base = MergedStyleNode.newTopLevelContainer();
		base.mergeRule(sequenceDiagram, new AutomaticCounterBasic(), false);
		final StyleAtomTrie trie = StyleAtomTrie.compile(base);

		final List<CompiledStyleRule> withStereotype = trie.findMatching(
				StyleQuery.of(setOf(SName.sequenceDiagram), Collections.singleton("myStereotype")));
		assertEquals(1, withStereotype.size());

		final List<CompiledStyleRule> withoutStereotype = trie.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram)));
		assertTrue(withoutStereotype.isEmpty());
	}

	@Test
	void twoDeclarationsSharingTheirAtomPathBothLiveAtTheSameTrieNode() {
		// sequenceDiagram { depth(2) { FontColor red }  depth(3) { FontColor blue } }
		final RawStyleRule depth2 = rule("depth(2)", false);
		depth2.putProperty(PName.FontColor, "red");
		final RawStyleRule depth3 = rule("depth(3)", false);
		depth3.putProperty(PName.FontColor, "blue");
		final RawStyleRule sequenceDiagram = rule("sequenceDiagram", false);
		sequenceDiagram.addChild(depth2);
		sequenceDiagram.addChild(depth3);

		final MergedStyleNode base = MergedStyleNode.newTopLevelContainer();
		base.mergeRule(sequenceDiagram, new AutomaticCounterBasic(), false);
		final StyleAtomTrie trie = StyleAtomTrie.compile(base);

		assertEquals(1, matchesAtLevel(trie, 2).size());
		assertEquals(1, matchesAtLevel(trie, 3).size());
		assertTrue(matchesAtLevel(trie, 4).isEmpty());
	}

	private static List<CompiledStyleRule> matchesAtLevel(StyleAtomTrie trie, int level) {
		return trie.findMatching(StyleQuery.of(setOf(SName.sequenceDiagram), Collections.<String> emptySet(),
				LevelConstraint.of(level, false)));
	}

	private static java.util.EnumSet<SName> setOf(SName... names) {
		final java.util.EnumSet<SName> result = java.util.EnumSet.noneOf(SName.class);
		for (SName n : names)
			result.add(n);
		return result;
	}

}
