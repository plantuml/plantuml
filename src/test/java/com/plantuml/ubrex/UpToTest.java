package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UpToTest {

	@Test
	public void test10() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a 〄>1");
		assertEquals("a000000001", cut.match(TextNavigator.build("a000000001")).getAcceptedMatch());
	}

	@Test
	public void test11() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a 〄>1");
		assertEquals("a000000001", cut.match(TextNavigator.build("a000000001")).getAcceptedMatch());
	}

	@Test
	public void test20() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a 〶$cell=〄>1");
		assertEquals("a000000001", cut.match(TextNavigator.build("a000000001")).getAcceptedMatch());
		assertEquals("[00000000]", cut.match(TextNavigator.build("a000000001")).getCapture("cell").toString());
	}

}
