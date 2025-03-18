package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TwoGroupsTest {

	@Test
	public void testTwoGroupsNoGroupMatch() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("0〇*〘a〘〇+b〙c〙9");
		UMatcher matcher = cut.match(TextNavigator.build("09"));
		assertEquals("09", matcher.getAcceptedMatch());
	}

	@Test
	public void testTwoGroupsMinimalMatch() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("0〇*〘a〘〇+b〙c〙9");
		UMatcher matcher = cut.match(TextNavigator.build("0abbc9"));
		assertEquals("0abbc9", matcher.getAcceptedMatch());
	}

	@Test
	public void testTwoGroupsWithPrefixWildcards() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("0〇*〘a〘〇+b〙c〙9");
		UMatcher matcher = cut.match(TextNavigator.build("0XYZaabc9"));
		assertEquals("", matcher.getAcceptedMatch());
	}

	@Test
	public void testSeveral() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("0〇*〘a〘〇+b〙c〙9");
		UMatcher matcher = cut.match(TextNavigator.build("0abbcabbbbc9"));
		assertEquals("0abbcabbbbc9", matcher.getAcceptedMatch());
	}

}
