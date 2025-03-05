package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UpToTest {

	@Test
	public void test10() {
		BracketedExpression cut = BracketedExpression.build("a 〄>1");
		assertEquals("a000000001", cut.match(TextNavigator.build("a000000001")).getAcceptedMatch());
	}

	@Test
	public void test11() {
		BracketedExpression cut = BracketedExpression.build("a 〄>1");
		assertEquals("a000000001", cut.match(TextNavigator.build("a000000001")).getAcceptedMatch());
	}

	@Test
	public void test20() {
		BracketedExpression cut = BracketedExpression.build("a 〶$cell=〄>1");
		assertEquals("a000000001", cut.match(TextNavigator.build("a000000001")).getAcceptedMatch());
		assertEquals("[00000000]", cut.match(TextNavigator.build("a000000001")).getCapture("cell").toString());
	}

}
