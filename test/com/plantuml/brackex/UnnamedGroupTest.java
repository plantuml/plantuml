package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UnnamedGroupTest {

	@Test
	public void testNamedGroupNoMatch() {
		BracketedExpression cut = BracketedExpression.build("a〘〇+b〙c");
		BMatcher matcher = cut.match(TextNavigator.build("heo"));
		assertEquals("", matcher.getAcceptedMatch());
		// assertEquals("???", matcher.getNamedCaptures().toString());
	}

	@Test
	public void testNamedGroupMatchSingleCharacter() {
		BracketedExpression cut = BracketedExpression.build("a〘〇+b〙c");
		BMatcher matcher = cut.match(TextNavigator.build("abc"));
		assertEquals("abc", matcher.getAcceptedMatch());
		// assertEquals("???", matcher.getNamedCaptures().toString());
	}

	@Test
	public void testNamedGroupMatchDoubleCharacters() {
		BracketedExpression cut = BracketedExpression.build("a〘〇+b〙c");
		BMatcher matcher = cut.match(TextNavigator.build("abbc"));
		assertEquals("abbc", matcher.getAcceptedMatch());
		// assertEquals("???", matcher.getNamedCaptures().toString());
	}

	@Test
	public void testNamedGroupMatchTripleCharacters() {
		BracketedExpression cut = BracketedExpression.build("a〘〇+b〙c");
		BMatcher matcher = cut.match(TextNavigator.build("abbbc"));
		assertEquals("abbbc", matcher.getAcceptedMatch());
		// assertEquals("???", matcher.getNamedCaptures().toString());
	}
}
