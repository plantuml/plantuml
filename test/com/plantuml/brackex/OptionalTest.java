package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OptionalTest {

	@Test
	public void testPatternTrieMatchingHeo() {
		BracketedExpression cut = BracketedExpression.build("he〇?lo");
		assertEquals("heo", cut.match(TextNavigator.build("heo")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHelo() {
		BracketedExpression cut = BracketedExpression.build("he〇?lo");
		assertEquals("helo", cut.match(TextNavigator.build("helo")).getAcceptedMatch());
	}

	@Test
	public void testPatternTrieMatchingHello() {
		BracketedExpression cut = BracketedExpression.build("he〇?lo");
		assertEquals("", cut.match(TextNavigator.build("hello")).getAcceptedMatch());
	}

}
