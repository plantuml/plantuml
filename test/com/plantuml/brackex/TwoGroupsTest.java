package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TwoGroupsTest {

	@Test
	public void testTwoGroupsNoGroupMatch() {
		BracketedExpression cut = BracketedExpression.build("0〇*〘a〘〇+b〙c〙9");
		BMatcher matcher = cut.match(TextNavigator.build("09"));
		assertEquals("09", matcher.getAcceptedMatch());
	}

	@Test
	public void testTwoGroupsMinimalMatch() {
		BracketedExpression cut = BracketedExpression.build("0〇*〘a〘〇+b〙c〙9");
		BMatcher matcher = cut.match(TextNavigator.build("0abbc9"));
		assertEquals("0abbc9", matcher.getAcceptedMatch());
	}

	@Test
	public void testTwoGroupsWithPrefixWildcards() {
		BracketedExpression cut = BracketedExpression.build("0〇*〘a〘〇+b〙c〙9");
		BMatcher matcher = cut.match(TextNavigator.build("0XYZaabc9"));
		assertEquals("", matcher.getAcceptedMatch());
	}

	@Test
	public void testSeveral() {
		BracketedExpression cut = BracketedExpression.build("0〇*〘a〘〇+b〙c〙9");
		BMatcher matcher = cut.match(TextNavigator.build("0abbcabbbbc9"));
		assertEquals("0abbcabbbbc9", matcher.getAcceptedMatch());
	}

}
