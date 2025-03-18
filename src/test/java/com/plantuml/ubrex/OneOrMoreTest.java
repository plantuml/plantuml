package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OneOrMoreTest {

	@Test
	public void testPatternTrieMatchingInvalidMatch() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("he〇+lo");
		assertEquals("", cut.match(TextNavigator.build("heo")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHelo() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("he〇+lo");
		assertEquals("helo", cut.match(TextNavigator.build("helo")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHello() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("he〇+lo");
		assertEquals("hello", cut.match(TextNavigator.build("hello")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHelllo() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("he〇+lo");
		assertEquals("helllo", cut.match(TextNavigator.build("helllo")).getAcceptedMatch());
	}
}
