package com.plantuml.ubrex.builder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.UnicodeBracketedExpression;

class ColorParserTest {

	/*
	
	private static final String COLOR_REGEXP = "#\\w+[-\\\\|/]?\\w+";

	private static final String PART2 = "#(?:\\w+[-\\\\|/]?\\w+;)?(?:(?:text|back|header|line|line\\.dashed|line\\.dotted|line\\.bold|shadowing)(?::\\w+[-\\\\|/]?\\w+)?(?:;|(?![\\w;:.])))+";
	private static final String COLORS_REGEXP = "(?:" + PART2 + ")|(?:" + COLOR_REGEXP + ")";

	 */

	private UBrexPart cutPart1() {
		UBrexPart cut = UBrexConcat.build( //
				new UBrexLeaf("# 〇+〴w 〇?〘 「-\\|/」 〇+〴w 〙"), //
				UBrexLeaf.end() //
		);
		return cut;
	}

	private UBrexPart cutPart2() {
		UBrexPart cut = UBrexConcat.build( //
				new UBrexLeaf("# 〇?〘 〇+〴w 〇?〘 「-\\|/」 〇+〴w 〙 ;  〙"), //
				new UBrexOneOrMore(UBrexConcat.build( //
						new UBrexLeaf("【text┇back┇header┇line.dashed┇line.dotted┇line.bold┇line┇shadowing】"), //
						new UBrexLeaf("# 〇?〘 〇+〴w 〇?〘 「-\\|/」 〇+〴w 〙  〙"), //
						new UBrexLeaf("【 ; ┇ 〒(!)「〴w;:.」 】"))) //
		);
		return cut;
	}

	private UBrexPart cutPart() {
		UBrexPart cut = new UBrexOr(cutPart1(), cutPart2());
		return cut;
	}

	@Test
	void testCut() {
		UnicodeBracketedExpression cut = cutPart();
		// 200
		assertTrue(cut.match("#foo-bar;text#foo;").exactMatch());
		assertTrue(cut.match("#foo;text#foo;").exactMatch());

		// 201
		assertTrue(cut.match("#foo-bar;header#foo;").exactMatch());

		// 202
		assertTrue(cut.match("#foo-bar;header#foo").exactMatch());
		
		// 203
		assertTrue(cut.match("#foo-bar;header#foo,").startMatch());
		assertFalse(cut.match("#foo-bar;header#foo,").exactMatch());

		// 100
		assertTrue(cut.match("#foo-bar").exactMatch());
		assertTrue(cut.match("#foo").exactMatch());


	}

	@Test
	void test200() {
		UnicodeBracketedExpression cut = cutPart2();
		assertTrue(cut.match("#foo-bar;text#foo;").exactMatch());
		assertTrue(cut.match("#foo;text#foo;").exactMatch());
	}

	@Test
	void test201() {
		UnicodeBracketedExpression cut = cutPart2();
		final UMatcher match = cut.match("#foo-bar;header#foo;");
		assertTrue(match.exactMatch());
		// assertEquals("[#foo-bar;header#foo;]", match.getCapture("COLOR").toString());
	}

	@Test
	void test202() {
		UnicodeBracketedExpression cut = cutPart2();
		final UMatcher match = cut.match("#foo-bar;header#foo");
		assertTrue(match.exactMatch());
		// assertEquals("[#foo-bar;header#foo]", match.getCapture("COLOR").toString());
	}

	@Test
	void test203() {
		UnicodeBracketedExpression cut = cutPart2();
		final UMatcher match = cut.match("#foo-bar;header#foo,");
		assertTrue(match.startMatch());
		assertFalse(match.exactMatch());
		// assertEquals("[#foo-bar;header#foo]", match.getCapture("COLOR").toString());
	}

	@Test
	void test100() {
		UnicodeBracketedExpression cut = cutPart1();
		assertTrue(cut.match("#foo-bar").exactMatch());
		assertTrue(cut.match("#foo").exactMatch());
	}

}
