package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LookAheadTest3 {

	@Test
	public void test1() {
		BracketedExpression cut = BracketedExpression.build("a【 ; ┇ 〒(!)「〴w;:.」 】");
		assertEquals("a;", cut.match("a;").getAcceptedMatch());
	}

	@Test
	public void test2() {
		BracketedExpression cut = BracketedExpression.build("a【 ; ┇ 〒(!)「〴w;:.」 】");
		assertEquals("a", cut.match("a,").getAcceptedMatch());
	}

	@Test
	public void test3() {
		BracketedExpression cut = BracketedExpression.build("a【 ; ┇ 〒(!)「〴w;:.」 】");
		assertEquals("", cut.match("a.").getAcceptedMatch());
	}

}
