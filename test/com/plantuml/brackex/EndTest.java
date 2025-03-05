package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EndTest {

	@Test
	public void test1() {
		BracketedExpression cut = BracketedExpression.build("ab");
		assertTrue(cut.match(TextNavigator.build("ab")).startMatch());
	}


	@Test
	public void test2() {
		BracketedExpression cut = BracketedExpression.build("ab");
		assertTrue(cut.match(TextNavigator.build("abc")).startMatch());
	}


	@Test
	public void test3() {
		BracketedExpression cut = BracketedExpression.build("ab 〒($)");
		assertTrue(cut.match(TextNavigator.build("ab")).startMatch());
	}


	@Test
	public void test4() {
		BracketedExpression cut = BracketedExpression.build("ab 〒($)");
		assertFalse(cut.match(TextNavigator.build("abc")).startMatch());
	}


}
