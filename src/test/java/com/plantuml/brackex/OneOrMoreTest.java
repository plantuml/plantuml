package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OneOrMoreTest {

	@Test
	public void testPatternTrieMatchingInvalidMatch() {
		BracketedExpression cut = BracketedExpression.build("he〇+lo");
		assertEquals("", cut.match(TextNavigator.build("heo")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHelo() {
		BracketedExpression cut = BracketedExpression.build("he〇+lo");
		assertEquals("helo", cut.match(TextNavigator.build("helo")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHello() {
		BracketedExpression cut = BracketedExpression.build("he〇+lo");
		assertEquals("hello", cut.match(TextNavigator.build("hello")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHelllo() {
		BracketedExpression cut = BracketedExpression.build("he〇+lo");
		assertEquals("helllo", cut.match(TextNavigator.build("helllo")).getAcceptedMatch());
	}
}
