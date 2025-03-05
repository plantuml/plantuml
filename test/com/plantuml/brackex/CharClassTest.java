package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CharClassTest {

	@Test
	public void test1() {
		BracketedExpression cut = BracketedExpression.build("〴w");
		assertEquals("h", cut.match(TextNavigator.build("h")).getAcceptedMatch());
	}

	@Test
	public void test2() {
		BracketedExpression cut = BracketedExpression.build("〇+〴w");
		assertEquals("aa", cut.match(TextNavigator.build("aaé")).getAcceptedMatch());
		assertEquals("h", cut.match(TextNavigator.build("h")).getAcceptedMatch());
		assertEquals("ab", cut.match(TextNavigator.build("ab")).getAcceptedMatch());
		assertEquals("abc", cut.match(TextNavigator.build("abc")).getAcceptedMatch());
	}

}
