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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.EnumSet;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.style.parser2.StyleAtom;
import net.sourceforge.plantuml.style.parser2.StyleQuery;
import net.sourceforge.plantuml.utils.BlocLines;

/**
 * {@link StyleLoader#parseStyleText} is the new {@code parser2}-backed replacement for
 * {@code new net.sourceforge.plantuml.style.parser.StyleParser(counter).parse(lines)} -- the
 * text-parsing entry point every {@code muteStyle}/{@code loadInternal} caller now goes through
 * (see {@code CommandStyleImport}, {@code CommandStyleSingleLineCSS},
 * {@code CommandStyleMultilinesCSS}, {@code StyleExtractor}, {@code SkinParam},
 * {@code StyleLoader#loadSkinSlow} itself). These tests pin down the specific behaviors found,
 * while wiring it in, to diverge from a naive reading of the new tokenizer/tree builder if not
 * handled explicitly -- each one first caught either by comparing directly against the legacy
 * parser's own output, or by a genuine Vega regression. The legacy {@code
 * net.sourceforge.plantuml.style.parser.StyleParser} tokenizer itself is gone now (its every
 * production caller long since migrated to {@link StyleLoader#parseStyleText}), so
 * {@link #producesTheSameStylesAsTheLegacyParserForARepresentativeSample()} no longer recomputes
 * one side of its comparison from it -- the expected values it pinned down are frozen in place
 * instead, exactly as they were last confirmed to match.
 */
class StyleLoaderParseStyleTextTest {

	private static Style theOneStyle(Collection<Style> styles) {
		assertEquals(1, styles.size(), () -> "expected exactly one Style, got " + styles);
		return styles.iterator().next();
	}

	/** Every {@link SName} tag {@code query} carries -- the {@code StyleQuery} counterpart of the
	 * legacy {@code StyleSignature.getKey().snames}. */
	private static EnumSet<SName> snamesOf(StyleQuery query) {
		final EnumSet<SName> result = EnumSet.noneOf(SName.class);
		for (StyleAtom atom : query.getAtoms())
			if (atom.isName())
				result.add(atom.getSName());
		return result;
	}

	@Test
	void aLightAndADarkDeclarationOfTheSameSelectorAreFoldedIntoOneStyle() throws Exception {
		final String text = "root { FontColor black }\n" //
				+ "@media (prefers-color-scheme:dark) {\n" //
				+ "  root { FontColor white }\n" //
				+ "}\n";

		final Style style = theOneStyle(
				StyleLoader.parseStyleText(BlocLines.getWithNewlines(text), new AutomaticCounterBasic()));

		assertEquals("black/white (1)", style.value(PName.FontColor).toString());
	}

	@Test
	void aStereotypeSelectorsPropertiesCarryTheirStereotypeCount() throws Exception {
		final String text = "class {\n  .myStereo {\n    FontColor red\n  }\n}\n";

		final Style style = theOneStyle(
				StyleLoader.parseStyleText(BlocLines.getWithNewlines(text), new AutomaticCounterBasic()));

		assertEquals("red/null (1,stereo=1)", style.value(PName.FontColor).toString());
	}

	@Test
	void anUnrecognizedSelectorWordIsFoldedInAsAStereotypeExactlyLikeTheLegacyParser() throws Exception {
		// Neither a known SName, a ".stereotype", nor a "depth(n)": the legacy Context#push
		// falls back to addStereotype(s) for this case (SName.retrieve(s) == null), and this
		// must too -- unlike StyleAtomTrie#compileNode's own (still-dead) handling of the same
		// case, which deliberately treats it as no constraint at all.
		final String text = "totoNotARealSName {\n  FontColor green\n}\n";

		final Style style = theOneStyle(
				StyleLoader.parseStyleText(BlocLines.getWithNewlines(text), new AutomaticCounterBasic()));

		assertEquals(EnumSet.noneOf(SName.class), snamesOf(style.getSignature()));
		assertEquals("green/null (1,stereo=1)", style.value(PName.FontColor).toString());
	}

	@Test
	void aDepthStarSelectorKeepsItsLevelAndStar() throws Exception {
		final String text = "wbsDiagram { node { depth(2)* {\n  FontColor blue\n} } }\n";

		final Style style = theOneStyle(
				StyleLoader.parseStyleText(BlocLines.getWithNewlines(text), new AutomaticCounterBasic()));

		assertEquals(2, style.getSignature().getLevelConstraint().getLevel());
		assertEquals(true, style.getSignature().getLevelConstraint().isStar());
	}

	@Test
	void aCommaSeparatedSelectorListSharesOnePriorityAcrossEveryAlternative() throws Exception {
		// Regression: an earlier version of MergedStyleNode#mergeRule drew one priority per
		// selector alternative (composite, then package), instead of one per property shared
		// across every alternative the way the legacy Context does (it builds a single Value
		// object and fans it out) -- silently giving "package"'s declaration a higher priority
		// than "composite"'s for the exact same line, which could tip a cascade against another
		// declaration close enough in priority. Confirmed against plantuml.skin's own real
		// "composite,package { title { FontStyle bold } } }" declaration (see
		// MergedStyleSheetTest#commaSeparatedSelectorsAreExpandedToIndependentNodes).
		final String text = "composite,package {\n  title {\n    FontStyle bold\n  }\n}\n";

		final AutomaticCounterBasic counter = new AutomaticCounterBasic();
		final Collection<Style> styles = StyleLoader.parseStyleText(BlocLines.getWithNewlines(text), counter);

		assertEquals(2, styles.size());
		for (Style style : styles)
			assertEquals("bold/null (1)", style.value(PName.FontStyle).toString());
	}

	@Test
	void aQuotedFontNameIsCarriedThroughWithoutItsQuoteCharacters() throws Exception {
		// Regression: RawStyleParser#readValue originally copied a value's characters verbatim,
		// quotes included -- fine for an unquoted value, but a font name with an embedded space
		// must be quoted ("Cascadia Code PL") the same way the legacy character-level tokenizer
		// always stripped a quoted STRING token's quotes before ever handing it to readValue.
		// Left unstripped, the literal quote characters made the font unrecognizable, silently
		// falling back to different metrics -- caught as a genuine Vega regression on
		// vega/xgantt/gantt6.puml, not by any narrower test.
		final String text = "task { FontName \"Cascadia Code PL\" }\n";

		final Style style = theOneStyle(
				StyleLoader.parseStyleText(BlocLines.getWithNewlines(text), new AutomaticCounterBasic()));

		assertEquals("Cascadia Code PL", style.value(PName.FontName).asString());
	}

	@Test
	void producesTheSameStylesAsTheLegacyParserForARepresentativeSample() throws Exception {
		// This used to run both parsers side by side (the legacy tokenizer and StyleLoader) and
		// compare their output style by style -- see this class's own javadoc. Now that every
		// production caller has migrated and the legacy net.sourceforge.plantuml.style.parser.
		// StyleParser tokenizer is gone, there is nothing left to recompute the "legacy" side
		// from: the expected toString() below is exactly what that comparison last confirmed,
		// frozen in place as a plain pinned/golden assertion instead of a live cross-check.
		// Re-pinned to Style#toString()'s new StyleQuery-backed shape (a single merged
		// SName+stereotype atom set followed by its LevelConstraint, instead of the legacy
		// StyleSignature's separate "[snames]  [stereotypes]" pair) once Style stopped carrying
		// a StyleSignature -- purely a debug-format change, not a resolution behavior change.
		final String[] samples = { //
				"root { FontColor black }\n@media (prefers-color-scheme:dark) {\n root { FontColor white }\n}\n", //
				"class {\n .myStereo {\n  FontColor red\n }\n}\n", //
				"wbsDiagram { node { depth(2)* {\n FontColor blue\n} } }\n", //
				"mindmapDiagram {}\nmindmapDiagram { node {\n RoundCorner 25\n} }\n", //
		};
		final String[] expected = { //
				"[root]  {FontColor=black/white (1)}", //
				"[class_, .mystereo]  {FontColor=red/null (1,stereo=1)}", //
				"[node, wbsDiagram] depth(2)* {FontColor=blue/null (1)}", //
				"[mindmapDiagram, node]  {RoundCorner=25/null (1)}", //
		};

		for (int i = 0; i < samples.length; i++) {
			final String text = samples[i];
			final Collection<Style> styles = StyleLoader.parseStyleText(BlocLines.getWithNewlines(text),
					new AutomaticCounterBasic());

			assertEquals(1, styles.size(), () -> "for text:\n" + text);
			assertEquals(expected[i], styles.iterator().next().toString(), () -> "for text:\n" + text);
		}
	}

	@Test
	void emptyTextProducesNoStyles() throws Exception {
		final Collection<Style> styles = StyleLoader.parseStyleText(BlocLines.getWithNewlines(""),
				new AutomaticCounterBasic());
		assertEquals(0, styles.size());
	}

	@Test
	void aStrayClosingBraceBackAtTheTopLevelIsSilentlyIgnoredExactlyLikeTheLegacyParser() throws Exception {
		// Regression (found from a real user diagram, codalo-68): the legacy character-level
		// tokenizer never raises an error for a "}" left over once every real block is already
		// closed -- its Context#pop() is only ever called guarded by Context#isEmpty(), so an
		// extra "}" back at the root context is a silent no-op (see StyleParser's
		// CLOSE_BRACKET handling and Context#isEmpty()/#pop()). A hand-written <style> block
		// with one closing brace too many, right before </style>, must keep parsing exactly as
		// it did before, not start throwing a StyleParsingException.
		final String text = "wbsDiagram {\n  BackGroundColor white\n}\n\n"
				+ ".americaStyle * {\n  FontColor red\n  BackGroundColor blue\n}\n\n}\n";

		final Collection<Style> styles = StyleLoader.parseStyleText(BlocLines.getWithNewlines(text),
				new AutomaticCounterBasic());

		assertEquals(2, styles.size());
	}

	@Test
	void aStarredAndAPlainDeclarationOfTheSameStereotypeProduceTwoIndependentStyles() throws Exception {
		// Regression (found from a real user diagram, jujugo-23): ".europeStyle * { node {
		// FontColor red } }" must cascade FontColor to Europe and every descendant, while the
		// separate ".europeStyle { node { FontSize 20 } }" must apply FontSize to Europe alone.
		// An earlier version of MergedStyleNode folded both declarations onto the very same tree
		// node (keyed by selector name only), so the merged node's star-ness -- true, since one
		// of the two occurrences had a star -- leaked FontSize onto every descendant right along
		// with FontColor. They must come back as two separate Style entries instead: one
		// starred, carrying only FontColor, and one plain, carrying only FontSize.
		final String text = "wbsDiagram {\n" //
				+ "  .europeStyle * {\n    node {\n      FontColor red\n    }\n  }\n" //
				+ "  .europeStyle {\n    node {\n      FontSize 20\n    }\n  }\n" //
				+ "}\n";

		final Collection<Style> styles = StyleLoader.parseStyleText(BlocLines.getWithNewlines(text),
				new AutomaticCounterBasic());

		assertEquals(2, styles.size());
		Style starred = null;
		Style plain = null;
		for (Style style : styles)
			if (style.getSignature().getLevelConstraint().isStar())
				starred = style;
			else
				plain = style;

		assertNotNull(starred);
		assertNotNull(plain);
		// Style#value falls back to ValueNull.NULL for a property this Style never set, whose
		// asString() is "" -- there is no "the value itself is null" case to assert against.
		assertEquals("", starred.value(PName.FontSize).asString());
		assertEquals("red", starred.value(PName.FontColor).asString());
		assertEquals("", plain.value(PName.FontColor).asString());
		assertEquals("20", plain.value(PName.FontSize).asString());
	}

	@Test
	void aBlockLeftUnclosedAtEndOfTextIsSilentlyClosedExactlyLikeTheLegacyParser() throws Exception {
		// Regression (found from a real user diagram, jigocu-99): the legacy character-level
		// tokenizer never checked whether every selector block got its own closing brace --
		// parseNow simply stops at the last token, whatever Context was still open at that point
		// is silently abandoned, no error raised. A nested block that DID get its own "}" (here,
		// "arrow") was already linked to its parent the moment ITS closing brace was seen, so the
		// missing outer "}" costs nothing except its own now-implicit close. RawStyleParser must
		// match that tolerance instead of raising "N block(s) never closed with a '}'".
		final String text = "activityDiagram {\n  arrow  {\n    LineColor green\n    FontColor blue\n"
				+ "    LineThickness 2\n"; // "activityDiagram"'s own closing brace is missing.

		final Style style = theOneStyle(
				StyleLoader.parseStyleText(BlocLines.getWithNewlines(text), new AutomaticCounterBasic()));

		assertEquals("green", style.value(PName.LineColor).asString());
		assertEquals("blue", style.value(PName.FontColor).asString());
		assertEquals("2", style.value(PName.LineThickness).asString());
	}

	@Test
	void aStereotypeSelectorNameMayContainALiteralEmbeddedSpaceExactlyLikeTheLegacyParser() throws Exception {
		// Regression (found from a real user diagram, kucabo-74): the legacy character-level
		// tokenizer's own readString never stops at a space when the token being read starts
		// with '.' ("if (ch == ' ' && result.charAt(0) != '.') break;"), so ".static lib {"
		// has always been read as the single two-word stereotype selector "static lib", never
		// as ".static" followed by a stray "lib". RawStyleParser must match that tolerance
		// instead of misreading the first word alone as a selector/property head and then
		// choking on the second ("Property '.static' declared outside of any block"). Note
		// that a space is not equivalent to an underscore here: ".static lib" and
		// "static_lib" stay two distinct, non-matching stereotypes, exactly as before.
		final String text = ".static lib {\n  BackGroundColor DarkKhaki\n}\n.shared lib {\n  BackGroundColor Green\n}\n";

		final Collection<Style> styles = StyleLoader.parseStyleText(BlocLines.getWithNewlines(text),
				new AutomaticCounterBasic());

		assertEquals(2, styles.size());
		Style staticLib = null;
		Style sharedLib = null;
		for (Style style : styles) {
			final String stereotype = style.getSignature().getStereotypes().toString();
			if (stereotype.contains("static"))
				staticLib = style;
			else if (stereotype.contains("shared"))
				sharedLib = style;
		}

		assertNotNull(staticLib);
		assertNotNull(sharedLib);
		assertEquals("DarkKhaki", staticLib.value(PName.BackGroundColor).asString());
		assertEquals("Green", sharedLib.value(PName.BackGroundColor).asString());
	}

}
