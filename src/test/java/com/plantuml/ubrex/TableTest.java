package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TableTest {

	@Test
	public void test1() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("| 〇+〘 〇*〴s  〶$cell=〇*「〤|」  〇*〴s  | 〙");

		assertEquals("|a|", cut.match(TextNavigator.build("|a|")).getAcceptedMatch());
		assertEquals("[a]", cut.match(TextNavigator.build("|a|")).getCapture("cell").toString());
	}

}
