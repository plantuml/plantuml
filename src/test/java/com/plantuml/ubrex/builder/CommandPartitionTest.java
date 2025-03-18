package com.plantuml.ubrex.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.UnicodeBracketedExpression;

class CommandPartitionTest {

	/*
	 * 
	 * private static IRegex getRegexConcat() { return
	 * RegexConcat.build(CommandPartition.class.getName(), RegexLeaf.start(), // new
	 * RegexLeaf("partition"), // RegexLeaf.spaceOneOrMore(), // new
	 * RegexLeaf("NAME", "([%g][^%g]+[%g]|\\S+)"), // RegexLeaf.spaceZeroOrMore(),
	 * // new RegexOr(// color().getRegex(), // new RegexLeaf("LEGACYCOLORIGNORED",
	 * "(#[0-9a-fA-F]{6}|#?\\w+)?")), // StereotypePattern.optional("STEREOTYPE"),
	 * // new RegexLeaf("\\{?"), // RegexLeaf.end()); }
	 * 
	 * new RegexOptional(new RegexLeaf(param, "(\\<\\<[-\\w]+\\>\\>)")), //
	 * 
	 */

	private UnicodeBracketedExpression cut() {
		UnicodeBracketedExpression cut = UBrexConcat.build( //
				new UBrexLeaf("partition"), //
				UBrexLeaf.spaceOneOrMore(), //
				new UBrexLeaf("【 〃 〶$NAME=〇+「〤〃」 〃 ┇ 〶$NAME=〇+〴S   】"), //
				UBrexLeaf.spaceZeroOrMore(), //
				new UBrexOptional(new UBrexNamed("STEREOTYPE", //
						new UBrexLeaf("<<  〄+〴. ->〘 >>〙"))), //
				UBrexLeaf.spaceZeroOrMore(), //
				new UBrexOptional(new UBrexLeaf("{")), //
				UBrexLeaf.end() //
		);
		return cut;
	}

	@Test
	void test1() {
		UnicodeBracketedExpression cut = cut();
		assertTrue(cut.match("partition \"foo\"").exactMatch());
		assertFalse(cut.match(" partition \"foo\"").exactMatch());
		assertFalse(cut.match("partition \"foo\"Z").exactMatch());

		final UMatcher match = cut.match("partition \"foo\"");
		assertTrue(match.exactMatch());
		assertEquals("[foo]", match.getCapture("NAME").toString());

	}

	@Test
	void test2() {
		UnicodeBracketedExpression cut = cut();
		final UMatcher match = cut.match("partition DUMMY");
		assertTrue(match.exactMatch());
		assertEquals("[DUMMY]", match.getCapture("NAME").toString());

	}

	@Test
	void test3() {
		UnicodeBracketedExpression cut = cut();
		final UMatcher match = cut.match("partition DUMMY <<my-stereo>>");
		assertTrue(match.exactMatch());
		assertEquals("[DUMMY]", match.getCapture("NAME").toString());
		assertEquals("[<<my-stereo>>]", match.getCapture("STEREOTYPE").toString());

	}

}
