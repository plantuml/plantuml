package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LookAheadTest {

	@Test
	public void test1() {
		BracketedExpression cut = BracketedExpression.build("ab");
		assertEquals("ab", cut.match(TextNavigator.build("ab")).getAcceptedMatch());
	}

	@Test
	public void test2() {
		BracketedExpression cut = BracketedExpression.build("ab");
		assertEquals("ab", cut.match(TextNavigator.build("abc")).getAcceptedMatch());
	}

	@Test
	public void test3() {
		BracketedExpression cut = BracketedExpression.build("a 〒(=)b");
		assertEquals("a", cut.match(TextNavigator.build("abc")).getAcceptedMatch());
		assertEquals("a", cut.match(TextNavigator.build("ab")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("a")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("aa")).getAcceptedMatch());
	}

	@Test
	public void test4() {
		BracketedExpression cut = BracketedExpression.build("a 〒(!)b");
		assertEquals("", cut.match(TextNavigator.build("abc")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("ab")).getAcceptedMatch());
		assertEquals("a", cut.match(TextNavigator.build("a")).getAcceptedMatch());
		assertEquals("a", cut.match(TextNavigator.build("aa")).getAcceptedMatch());
	}

}
