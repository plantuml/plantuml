package com.plantuml.brackex.builder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.plantuml.brackex.BMatcher;
import com.plantuml.brackex.BracketedExpression;

class ColorParserTest {

	/*
	
	private static final String COLOR_REGEXP = "#\\w+[-\\\\|/]?\\w+";

	private static final String PART2 = "#(?:\\w+[-\\\\|/]?\\w+;)?(?:(?:text|back|header|line|line\\.dashed|line\\.dotted|line\\.bold|shadowing)(?::\\w+[-\\\\|/]?\\w+)?(?:;|(?![\\w;:.])))+";
	private static final String COLORS_REGEXP = "(?:" + PART2 + ")|(?:" + COLOR_REGEXP + ")";

	 */

	private BrackexPart cutPart1() {
		BrackexPart cut = BrackexConcat.build( //
				new BrackexLeaf("# 〇+〴w 〇?〘 「-\\|/」 〇+〴w 〙"), //
				BrackexLeaf.end() //
		);
		return cut;
	}

	private BrackexPart cutPart2() {
		BrackexPart cut = BrackexConcat.build( //
				new BrackexLeaf("# 〇?〘 〇+〴w 〇?〘 「-\\|/」 〇+〴w 〙 ;  〙"), //
				new BrackexOneOrMore(BrackexConcat.build( //
						new BrackexLeaf("【text┇back┇header┇line.dashed┇line.dotted┇line.bold┇line┇shadowing】"), //
						new BrackexLeaf("# 〇?〘 〇+〴w 〇?〘 「-\\|/」 〇+〴w 〙  〙"), //
						new BrackexLeaf("【 ; ┇ 〒(!)「〴w;:.」 】"))) //
		);
		return cut;
	}

	private BrackexPart cutPart() {
		BrackexPart cut = new BrackexOr(cutPart1(), cutPart2());
		return cut;
	}

	@Test
	void testCut() {
		BracketedExpression cut = cutPart();
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
		BracketedExpression cut = cutPart2();
		assertTrue(cut.match("#foo-bar;text#foo;").exactMatch());
		assertTrue(cut.match("#foo;text#foo;").exactMatch());
	}

	@Test
	void test201() {
		BracketedExpression cut = cutPart2();
		final BMatcher match = cut.match("#foo-bar;header#foo;");
		assertTrue(match.exactMatch());
		// assertEquals("[#foo-bar;header#foo;]", match.getCapture("COLOR").toString());
	}

	@Test
	void test202() {
		BracketedExpression cut = cutPart2();
		final BMatcher match = cut.match("#foo-bar;header#foo");
		assertTrue(match.exactMatch());
		// assertEquals("[#foo-bar;header#foo]", match.getCapture("COLOR").toString());
	}

	@Test
	void test203() {
		BracketedExpression cut = cutPart2();
		final BMatcher match = cut.match("#foo-bar;header#foo,");
		assertTrue(match.startMatch());
		assertFalse(match.exactMatch());
		// assertEquals("[#foo-bar;header#foo]", match.getCapture("COLOR").toString());
	}

	@Test
	void test100() {
		BracketedExpression cut = cutPart1();
		assertTrue(cut.match("#foo-bar").exactMatch());
		assertTrue(cut.match("#foo").exactMatch());
	}

}
