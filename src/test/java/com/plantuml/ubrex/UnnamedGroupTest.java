package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UnnamedGroupTest {

	@Test
	public void testNamedGroupNoMatch() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a〘〇+b〙c");
		UMatcher matcher = cut.match(TextNavigator.build("heo"));
		assertEquals("", matcher.getAcceptedMatch());
		// assertEquals("???", matcher.getNamedCaptures().toString());
	}

	@Test
	public void testNamedGroupMatchSingleCharacter() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a〘〇+b〙c");
		UMatcher matcher = cut.match(TextNavigator.build("abc"));
		assertEquals("abc", matcher.getAcceptedMatch());
		// assertEquals("???", matcher.getNamedCaptures().toString());
	}

	@Test
	public void testNamedGroupMatchDoubleCharacters() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a〘〇+b〙c");
		UMatcher matcher = cut.match(TextNavigator.build("abbc"));
		assertEquals("abbc", matcher.getAcceptedMatch());
		// assertEquals("???", matcher.getNamedCaptures().toString());
	}

	@Test
	public void testNamedGroupMatchTripleCharacters() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a〘〇+b〙c");
		UMatcher matcher = cut.match(TextNavigator.build("abbbc"));
		assertEquals("abbbc", matcher.getAcceptedMatch());
		// assertEquals("???", matcher.getNamedCaptures().toString());
	}
}
