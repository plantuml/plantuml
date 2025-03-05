package com.plantuml.brackex.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.plantuml.brackex.BMatcher;
import com.plantuml.brackex.BracketedExpression;

class CommandPartitionTest {

	/*
	
	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPartition.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("partition"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("NAME", "([%g][^%g]+[%g]|\\S+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr(//
						color().getRegex(), //
						new RegexLeaf("LEGACYCOLORIGNORED", "(#[0-9a-fA-F]{6}|#?\\w+)?")), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexLeaf("\\{?"), //
				RegexLeaf.end());
	}
	
					new RegexOptional(new RegexLeaf(param, "(\\<\\<[-\\w]+\\>\\>)")), //

	 */

	private BracketedExpression cut() {
		BracketedExpression cut = BrackexConcat.build( //
				new BrackexLeaf("partition"), //
				BrackexLeaf.spaceOneOrMore(), //
				new BrackexLeaf("【 〃 〶$NAME=〇+「〤〃」 〃 ┇ 〶$NAME=〇+〴S   】"), //
				BrackexLeaf.spaceZeroOrMore(), //
				// new BrackexOptional(new BrackexLeaf("〶$STEREOTYPE=〘<< 〇+「-〴w」 >>〙")), //
				// new BrackexOptional(new BrackexLeaf("〶$STEREOTYPE=〘<< 〇~>〘 >>〙>> 〙")), //
				// new BrackexOptional(new BrackexLeaf("〶$STEREOTYPE=〘<< 〇-+〴. ->〘 >>〙 〙")), //
				new BrackexOptional(new BrackexNamed("STEREOTYPE", //
						new BrackexLeaf("<<  〄+〴. ->〘 >>〙"))), //
				BrackexLeaf.spaceZeroOrMore(), //
				new BrackexOptional(new BrackexLeaf("{")), //
				BrackexLeaf.end() //
		);
		return cut;
	}

	@Test
	void test1() {
		BracketedExpression cut = cut();
		assertTrue(cut.match("partition \"foo\"").exactMatch());
		assertFalse(cut.match(" partition \"foo\"").exactMatch());
		assertFalse(cut.match("partition \"foo\"Z").exactMatch());

		final BMatcher match = cut.match("partition \"foo\"");
		assertTrue(match.exactMatch());
		assertEquals("[foo]", match.getCapture("NAME").toString());

	}

	@Test
	void test2() {
		BracketedExpression cut = cut();
		final BMatcher match = cut.match("partition DUMMY");
		assertTrue(match.exactMatch());
		assertEquals("[DUMMY]", match.getCapture("NAME").toString());

	}

	@Test
	void test3() {
		BracketedExpression cut = cut();
		final BMatcher match = cut.match("partition DUMMY <<my-stereo>>");
		assertTrue(match.exactMatch());
		assertEquals("[DUMMY]", match.getCapture("NAME").toString());
		assertEquals("[<<my-stereo>>]", match.getCapture("STEREOTYPE").toString());

	}

}
