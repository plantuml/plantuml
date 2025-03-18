package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OptionalTest {

	@Test
	public void testPatternTrieMatchingHeo() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("he〇?lo");
		assertEquals("heo", cut.match(TextNavigator.build("heo")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHelo() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("he〇?lo");
		assertEquals("helo", cut.match(TextNavigator.build("helo")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHello() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("he〇?lo");
		assertEquals("", cut.match(TextNavigator.build("hello")).getAcceptedMatch());
	}

}
