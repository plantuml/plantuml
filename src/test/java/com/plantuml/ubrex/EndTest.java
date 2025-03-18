package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EndTest {

	@Test
	public void test1() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("ab");
		assertTrue(cut.match(TextNavigator.build("ab")).startMatch());
	}


	@Test
	public void test2() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("ab");
		assertTrue(cut.match(TextNavigator.build("abc")).startMatch());
	}


	@Test
	public void test3() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("ab 〒($)");
		assertTrue(cut.match(TextNavigator.build("ab")).startMatch());
	}


	@Test
	public void test4() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("ab 〒($)");
		assertFalse(cut.match(TextNavigator.build("abc")).startMatch());
	}


}
