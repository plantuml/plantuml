package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LookAheadTest3 {

	@Test
	public void test1() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a【 ; ┇ 〒(!)「〴w;:.」 】");
		assertEquals("a;", cut.match("a;").getAcceptedMatch());
	}

	@Test
	public void test2() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a【 ; ┇ 〒(!)「〴w;:.」 】");
		assertEquals("a", cut.match("a,").getAcceptedMatch());
	}

	@Test
	public void test3() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a【 ; ┇ 〒(!)「〴w;:.」 】");
		assertEquals("", cut.match("a.").getAcceptedMatch());
	}

}
