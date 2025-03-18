package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LookAheadTest {

	@Test
	public void test1() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("ab");
		assertEquals("ab", cut.match(TextNavigator.build("ab")).getAcceptedMatch());
	}

	@Test
	public void test2() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("ab");
		assertEquals("ab", cut.match(TextNavigator.build("abc")).getAcceptedMatch());
	}

	@Test
	public void test3() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a 〒(=)b");
		assertEquals("a", cut.match(TextNavigator.build("abc")).getAcceptedMatch());
		assertEquals("a", cut.match(TextNavigator.build("ab")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("a")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("aa")).getAcceptedMatch());
	}

	@Test
	public void test4() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a 〒(!)b");
		assertEquals("", cut.match(TextNavigator.build("abc")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("ab")).getAcceptedMatch());
		assertEquals("a", cut.match(TextNavigator.build("a")).getAcceptedMatch());
		assertEquals("a", cut.match(TextNavigator.build("aa")).getAcceptedMatch());
	}

}
