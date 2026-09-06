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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.style.AutomaticCounter;
import net.sourceforge.plantuml.style.AutomaticCounterBasic;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Specificity;
import net.sourceforge.plantuml.style.StyleLoader;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocationImpl;

/**
 * Checks that {@link MergedStyleSheet} actually does the three things {@link RawStyleSheet}
 * deliberately leaves undone: merging repeated declarations of the same selector, expanding
 * comma-separated selector lists, and folding an {@code @media (prefers-color-scheme:dark)}
 * declaration into the very same node as its light counterpart -- using the bundled
 * plantuml.skin, which exercises all three.
 *
 * Also checks {@link MergedStyleSheet#build} against the legacy loading path it mirrors,
 * {@code StyleBuilder#loadInternal}: a base style sheet may never declare a starred selector.
 * An overlay ({@code <style>} block) does not go through {@link MergedStyleSheet} at all -- see
 * {@code net.sourceforge.plantuml.style.StyleLoader#parseStyleText} -- but it reuses the very
 * same {@link MergedStyleSheet#mergeInto} merging logic {@link #build} is built on, which a few
 * tests below exercise directly against a bare {@link MergedStyleNode}, exactly the way
 * {@code parseStyleText} does.
 */
class MergedStyleSheetTest {

	private static MergedStyleSheet loadPlantumlSkin() throws Exception {
		// StyleLoader2 (a since-deleted, never-wired-in second loader) used to hand back the
		// bundled plantuml.skin's raw tree directly; StyleLoader itself has no such "give me the
		// RawStyleSheet" entry point (parseStyleText already merges), so this goes straight to
		// the same resource StyleLoader#loadSkin loads in production, through its own
		// getInputStreamForStyle -- the exact bytes every real diagram's default skin comes from.
		final InputStream is = StyleLoader.getInputStreamForStyle("plantuml.skin");
		final BlocLines lines = BlocLines.load(is, new LineLocationImpl("plantuml.skin", null));
		return MergedStyleSheet.build(RawStyleParser.parse(lines));
	}

	private static RawStyleSheet parse(String text) throws Exception {
		return RawStyleParser.parse(BlocLines.getWithNewlines(text));
	}

	@Test
	void rootPropertiesAreReadable() throws Exception {
		final MergedStyleSheet sheet = loadPlantumlSkin();
		final MergedStyleNode root = sheet.getBase().getNamedChildren().get(SName.root);

		assertNotNull(root);
		assertEquals("SansSerif", root.getProperty(PName.FontName).getValue());
		// Resolved from var(--common-background) at parse time, already substituted.
		assertEquals("#f1f1f1", root.getProperty(PName.BackGroundColor).getValue());
	}

	@Test
	void repeatedTopLevelDeclarationsAreMergedIntoOneNode() throws Exception {
		// plantuml.skin declares "mindmapDiagram {}" once, empty, and later
		// "mindmapDiagram { node {...} arrow {...} }": both must land on the same node.
		final MergedStyleSheet sheet = loadPlantumlSkin();
		final MergedStyleNode mindmap = sheet.getBase().getNamedChildren().get(SName.mindmapDiagram);

		assertNotNull(mindmap);
		assertNotNull(mindmap.getNamedChildren().get(SName.node));
		assertNotNull(mindmap.getNamedChildren().get(SName.arrow));
		assertEquals("25", mindmap.getNamedChildren().get(SName.node).getProperty(PName.RoundCorner).getValue());
	}

	@Test
	void commaSeparatedSelectorsAreExpandedToIndependentNodes() throws Exception {
		// "element { composite,package { title { FontStyle bold ... } } }": both
		// "composite" and "package" must end up with their own independent "title" child.
		final MergedStyleSheet sheet = loadPlantumlSkin();
		final MergedStyleNode element = sheet.getBase().getNamedChildren().get(SName.element);
		assertNotNull(element);

		final MergedStyleNode compositeTitle = element.getNamedChildren().get(SName.composite).getNamedChildren()
				.get(SName.title);
		final MergedStyleNode packageTitle = element.getNamedChildren().get(SName.package_).getNamedChildren()
				.get(SName.title);

		assertNotNull(compositeTitle);
		assertNotNull(packageTitle);
		assertEquals("bold", compositeTitle.getProperty(PName.FontStyle).getValue());
		assertEquals("bold", packageTitle.getProperty(PName.FontStyle).getValue());

		// They must be independent nodes, not the same one reached twice.
		compositeTitle.getProperties().put(PName.FontSize,
				PrioritizedValue.light("999", Specificity.atOrder(Integer.MAX_VALUE)));
		assertNull(packageTitle.getProperty(PName.FontSize));
	}

	@Test
	void aPlainAndAStarredDeclarationOfTheSameSelectorStayOnTwoIndependentNodes() throws Exception {
		// Regression, from a real user diagram: ".europeStyle * { node { FontColor red } }"
		// cascades FontColor to Europe and all its descendants, while the separate, non-starred
		// ".europeStyle { node { FontSize 20 } }" must apply FontSize to Europe alone. An earlier
		// version of MergedStyleNode keyed a child purely by selector name, so both declarations
		// landed on the very same node, and that node's star flag -- true, since at least one of
		// the two occurrences had a star -- made FontSize leak onto England, Germany and Spain
		// right along with FontColor. They must come back as two separate nodes instead, each
		// with only the property its own declaration set.
		// A starred selector may only appear in an inline <style> overlay, never in a base .skin
		// file (see buildRejectsATopLevelStarredSelector below) -- exactly the real shape of the
		// regression: a hand-written "<style>...</style>" block inside a .puml file, merged via
		// MergedStyleSheet#mergeInto exactly like StyleLoader#parseStyleText does, not #build().
		final MergedStyleNode root = MergedStyleNode.newTopLevelContainer();
		final AutomaticCounter counter = new AutomaticCounterBasic();
		MergedStyleSheet.mergeInto(root, parse("wbsDiagram {\n  FontColor black\n}\n"), counter);
		MergedStyleSheet.mergeInto(root, parse("wbsDiagram {\n" //
				+ "  .europeStyle * {\n    node {\n      FontColor red\n    }\n  }\n" //
				+ "  .europeStyle {\n    node {\n      FontSize 20\n    }\n  }\n" //
				+ "}\n"), counter);
		final MergedStyleNode wbsDiagram = root.getNamedChildren().get(SName.wbsDiagram);

		final MergedStyleNode plain = wbsDiagram.getOtherChildren().get("europestyle");
		final MergedStyleNode starred = wbsDiagram.getStarredOtherChildren().get("europestyle");

		assertNotNull(plain);
		assertNotNull(starred);
		assertFalse(plain.isStar());
		assertTrue(starred.isStar());

		assertNull(plain.getNamedChildren().get(SName.node).getProperty(PName.FontColor));
		assertEquals("20", plain.getNamedChildren().get(SName.node).getProperty(PName.FontSize).getValue());

		assertEquals("red", starred.getNamedChildren().get(SName.node).getProperty(PName.FontColor).getValue());
		assertNull(starred.getNamedChildren().get(SName.node).getProperty(PName.FontSize));
	}

	@Test
	void aDarkMediaDeclarationIsFoldedIntoTheSameNodeAsItsLightCounterpart() throws Exception {
		// "root { FontColor black }" and, later, "@media (dark) { root { FontColor white } }":
		// there is no second tree any more -- both land on the very same "root" node, combined
		// into one value carrying both, exactly like the legacy DarkString does.
		final MergedStyleSheet sheet = loadPlantumlSkin();
		final MergedStyleNode root = sheet.getBase().getNamedChildren().get(SName.root);
		final PrioritizedValue fontColor = root.getProperty(PName.FontColor);

		assertNotNull(fontColor);
		assertEquals("black", fontColor.getLight());
		assertEquals("white", fontColor.getDark());
	}

	@Test
	void buildRejectsATopLevelStarredSelector() throws Exception {
		// A real .skin file never needs a starred selector (none of the bundled ones declare
		// one): build() -- the counterpart of the legacy loadInternal -- rejects it outright,
		// exactly like loadInternal's own "if (signature.isStarred()) throw ...".
		final RawStyleSheet raw = parse("mindmapDiagram* {\n  FontColor red\n}\n");
		assertThrows(IllegalArgumentException.class, () -> MergedStyleSheet.build(raw));
	}

	@Test
	void buildRejectsAStarredSelectorNestedInsideAMediaBlock() throws Exception {
		// The guard walks the whole raw tree, @media content included, not just the top level.
		final RawStyleSheet raw = parse("@media (prefers-color-scheme:dark) {\n" //
				+ "  mindmapDiagram { node* {\n    FontColor white\n  } }\n" //
				+ "}\n");
		assertThrows(IllegalArgumentException.class, () -> MergedStyleSheet.build(raw));
	}

	@Test
	void mergeIntoAcceptsAStarredSelectorThatBuildWouldReject() throws Exception {
		// The real-world case: a hand-written "depth(n)*" ancestor-cascade catch-all, exactly
		// as it would come from a "<style> wbsDiagram { node { depth(2)* { ... } } } </style>"
		// block -- parseStyleText's mergeInto call must accept what build() refuses.
		final MergedStyleNode root = MergedStyleNode.newTopLevelContainer();
		final AutomaticCounter counter = new AutomaticCounterBasic();
		MergedStyleSheet.mergeInto(root, parse("wbsDiagram {\n  FontColor black\n}\n"), counter);
		MergedStyleSheet.mergeInto(root, parse("wbsDiagram { node { depth(2)* {\n  FontColor red\n} } }\n"), counter);

		final MergedStyleNode depthNode = root.getNamedChildren().get(SName.wbsDiagram).getNamedChildren()
				.get(SName.node).getStarredOtherChildren().get("depth(2)");
		assertNotNull(depthNode);
		assertTrue(depthNode.isStar());
		assertEquals("red", depthNode.getProperty(PName.FontColor).getValue());
	}

}
