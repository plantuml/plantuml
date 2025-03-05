package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZeroOrMoreLazzyTest {

	// @Test
	public void testPatternTrieMatchingHeo() {
		BracketedExpression cut = BracketedExpression.build("he〇_*lo");
		assertEquals("heo", cut.match(TextNavigator.build("heo")).getAcceptedMatch());
	}

	// @Test
	public void testPatternTrieMatchingHelo() {
		BracketedExpression cut = BracketedExpression.build("he〇_*lo");
		assertEquals("helo", cut.match(TextNavigator.build("helo")).getAcceptedMatch());
	}

	// @Test
	public void testPatternTrieMatchingHello() {
		BracketedExpression cut = BracketedExpression.build("he〇_*lo");
		assertEquals("hello", cut.match(TextNavigator.build("hello")).getAcceptedMatch());
	}

	// @Test
	public void test4() {
		BracketedExpression cut = BracketedExpression.build("he〇_*〴w  l");
		assertEquals("hell", cut.match(TextNavigator.build("hello")).getAcceptedMatch());
	}

}
