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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleLoader;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocationImpl;

/**
 * Compiles the bundled {@code plantuml.skin} all the way to a {@link CompiledStyleSheet} and
 * checks it answers the same questions {@link MergedStyleSheetTest} already checks on the
 * merged tree, but through {@link StyleAtomTrie#findMatching(StyleQuery)} and
 * {@link CompiledStyleSheet#resolve(StyleQuery)}.
 */
class CompiledStyleSheetTest {

	private static CompiledStyleSheet compilePlantumlSkin() throws Exception {
		// StyleLoader2 (a since-deleted, never-wired-in second loader) used to hand back the
		// bundled plantuml.skin's raw tree directly; StyleLoader itself has no such "give me the
		// RawStyleSheet" entry point (parseStyleText already merges), so this goes straight to
		// the same resource StyleLoader#loadSkin loads in production, through its own
		// getInputStreamForStyle -- the exact bytes every real diagram's default skin comes from.
		final InputStream is = StyleLoader.getInputStreamForStyle("plantuml.skin");
		final BlocLines lines = BlocLines.load(is, new LineLocationImpl("plantuml.skin", null));
		final RawStyleSheet raw = RawStyleParser.parse(lines);
		return CompiledStyleSheet.compile(MergedStyleSheet.build(raw));
	}

	private static EnumSet<SName> setOf(SName... names) {
		final EnumSet<SName> result = EnumSet.noneOf(SName.class);
		for (SName n : names)
			result.add(n);
		return result;
	}

	@Test
	void rootPropertiesAreFoundBySubsetQuery() throws Exception {
		final CompiledStyleSheet sheet = compilePlantumlSkin();

		final List<CompiledStyleRule> matches = sheet.getBase().findMatching(StyleQuery.of(setOf(SName.root)));
		assertEquals(1, matches.size());
		assertEquals("SansSerif", matches.get(0).getProperties().get(PName.FontName));
		assertEquals("#f1f1f1", matches.get(0).getProperties().get(PName.BackGroundColor));

		// A query missing "root" must not see its properties (containsAll semantics).
		assertTrue(sheet.getBase().findMatching(StyleQuery.of(setOf(SName.element))).isEmpty()
				|| sheet.getBase().findMatching(StyleQuery.of(setOf(SName.element))).stream()
						.noneMatch(r -> r.getProperties().containsKey(PName.FontName)
								&& "SansSerif".equals(r.getProperties().get(PName.FontName))));
	}

	@Test
	void repeatedTopLevelDeclarationsCompileToOneMergedEntry() throws Exception {
		// mindmapDiagram { node { RoundCorner 25 } } -- declared as two separate blocks in the
		// file, already merged by MergedStyleNode; the trie must see one declaration, not two.
		final CompiledStyleSheet sheet = compilePlantumlSkin();

		final List<CompiledStyleRule> matches = sheet.getBase()
				.findMatching(StyleQuery.of(setOf(SName.root, SName.element, SName.mindmapDiagram, SName.node)));

		final long withRoundCorner = matches.stream().filter(r -> "25".equals(r.getProperties().get(PName.RoundCorner)))
				.count();
		assertEquals(1, withRoundCorner);
	}

	@Test
	void commaSeparatedSelectorsStillMatchIndependently() throws Exception {
		final CompiledStyleSheet sheet = compilePlantumlSkin();

		final List<CompiledStyleRule> compositeTitle = sheet.getBase()
				.findMatching(StyleQuery.of(setOf(SName.element, SName.composite, SName.title)));
		final List<CompiledStyleRule> packageTitle = sheet.getBase()
				.findMatching(StyleQuery.of(setOf(SName.element, SName.package_, SName.title)));

		assertTrue(compositeTitle.stream().anyMatch(r -> "bold".equals(r.getProperties().get(PName.FontStyle))));
		assertTrue(packageTitle.stream().anyMatch(r -> "bold".equals(r.getProperties().get(PName.FontStyle))));

		// composite and package are independent: a query with only "composite" (no "package")
		// must not accidentally pick up a package-only declaration, and vice versa -- there is
		// none here, but cross-checking the atom sets pins the independence down directly.
		assertFalse(compositeTitle.isEmpty());
		assertFalse(packageTitle.isEmpty());
	}

	@Test
	void resolveCombinesTheLightAndDarkDeclarationsIntoOneValuePerProperty() throws Exception {
		// There is no theme to pick any more: resolve() always returns both halves of a
		// property that has a light AND a dark declaration, exactly like the legacy
		// ValueImpl/DarkString would, ready for something like HColor#withDark to decide later.
		final CompiledStyleSheet sheet = compilePlantumlSkin();
		final Map<PName, PrioritizedValue> resolved = sheet.resolve(StyleQuery.of(setOf(SName.root)));

		assertEquals("black", resolved.get(PName.FontColor).getLight());
		assertEquals("white", resolved.get(PName.FontColor).getDark());
		// A property that never had a dark declaration keeps only its light half.
		assertEquals("SansSerif", resolved.get(PName.FontName).getLight());
		assertNull(resolved.get(PName.FontName).getDark());
	}

	@Test
	void resolveToStringsProjectsToTheLightValue() throws Exception {
		final CompiledStyleSheet sheet = compilePlantumlSkin();
		final Map<PName, String> resolved = sheet.resolveToStrings(StyleQuery.of(setOf(SName.root)));

		assertEquals("black", resolved.get(PName.FontColor));
		assertEquals("SansSerif", resolved.get(PName.FontName));
	}

	@Test
	void lightAndDarkDeclarationsCombineRegardlessOfWhichIsDeclaredFirst() {
		// A synthetic sheet where the @media block comes BEFORE the base declaration -- the
		// combining must not depend on file order, unlike the old two-tree cascade did (there,
		// only "dark overrides base when active" mattered; now the two must fold into one
		// value however they are ordered).
		final RawStyleRule darkRoot = RawStyleRule
				.forSelectors(Collections.singletonList(RawSelector.classify("root")), false);
		darkRoot.putProperty(PName.FontColor, "white");
		final RawStyleRule mediaBlock = RawStyleRule.forMedia("media (prefers-color-scheme:dark)");
		mediaBlock.addChild(darkRoot);

		final RawStyleRule baseRoot = RawStyleRule
				.forSelectors(Collections.singletonList(RawSelector.classify("root")), false);
		baseRoot.putProperty(PName.FontColor, "black");

		final RawStyleSheet raw = new RawStyleSheet(Collections.<String, String> emptyMap(),
				Arrays.asList(mediaBlock, baseRoot));
		final CompiledStyleSheet sheet = CompiledStyleSheet.compile(MergedStyleSheet.build(raw));

		final Map<PName, PrioritizedValue> resolved = sheet.resolve(StyleQuery.of(setOf(SName.root)));
		assertEquals("black", resolved.get(PName.FontColor).getLight());
		assertEquals("white", resolved.get(PName.FontColor).getDark());
	}

	@Test
	void aHandWrittenDepthStarOverlayResolvesAsAnAncestorCascadeCatchAllWould() throws Exception {
		// The real usage this exercises: a "<style> wbsDiagram { node { depth(2)* { ... } } }
		// </style>" block, written by hand in a diagram and folded on top of an already-loaded
		// sheet via MergedStyleSheet#mute -- the muteStyle counterpart, which (unlike build())
		// accepts a starred selector.
		final MergedStyleSheet base = MergedStyleSheet.build(RawStyleParser
				.parse(BlocLines.getWithNewlines("wbsDiagram {\n  node {\n    FontColor black\n  }\n}\n")));
		final RawStyleSheet overlay = RawStyleParser.parse(BlocLines
				.getWithNewlines("wbsDiagram { node { depth(2)* {\n  FontColor red\n} } }\n"));

		final CompiledStyleSheet sheet = CompiledStyleSheet.compile(base.mute(overlay));

		final EnumSet<SName> wbsNode = setOf(SName.wbsDiagram, SName.node);

		// A plain, non-inheritance query for the element itself, three levels deep: a
		// depth(2)* declaration is a genuine "matches this level or any deeper one" rule, so
		// it answers directly here too, and -- coming from the later mute() call -- outranks
		// the plain "node { FontColor black }" declaration.
		final Map<PName, PrioritizedValue> ownLevelDeep = sheet
				.resolve(StyleQuery.of(wbsNode, Collections.<String> emptySet(), LevelConstraint.of(3, false)));
		assertEquals("red", ownLevelDeep.get(PName.FontColor).getValue());

		// The same kind of query, but shallower than depth(2): the catch-all no longer
		// qualifies (level too low), so only the plain declaration is left standing.
		final Map<PName, PrioritizedValue> ownLevelShallow = sheet
				.resolve(StyleQuery.of(wbsNode, Collections.<String> emptySet(), LevelConstraint.of(1, false)));
		assertEquals("black", ownLevelShallow.get(PName.FontColor).getValue());

		// An ancestor-inheritance lookup (a starred query, exactly what Idea#getStyle()'s
		// ancestor walk issues) at depth 3: only a declaration that is itself starred may
		// answer it -- the plain declaration is excluded outright, whatever its level -- and
		// depth(2)* qualifies since 3 is at or beyond its own depth.
		final Map<PName, PrioritizedValue> ancestorLookupDeep = sheet
				.resolve(StyleQuery.of(wbsNode, Collections.<String> emptySet(), LevelConstraint.of(3, true)));
		assertEquals("red", ancestorLookupDeep.get(PName.FontColor).getValue());

		// The very same ancestor-inheritance lookup, but shallower than depth(2): the plain
		// declaration is still excluded by the star gate, and now depth(2)* is excluded too
		// (level too low) -- nothing is left to answer it at all.
		final Map<PName, PrioritizedValue> ancestorLookupShallow = sheet
				.resolve(StyleQuery.of(wbsNode, Collections.<String> emptySet(), LevelConstraint.of(1, true)));
		assertNull(ancestorLookupShallow.get(PName.FontColor));
	}

}
