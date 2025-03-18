package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RepetitionTest {

	@Test
	public void test01() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("he〇{6}lo");
		assertEquals("hellllllo", cut.match(TextNavigator.build("hellllllo")).getAcceptedMatch());
	}

	@Test
	public void test02() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("he〇{6}lo");
		assertEquals("", cut.match(TextNavigator.build("helllllo")).getAcceptedMatch());
	}

}
