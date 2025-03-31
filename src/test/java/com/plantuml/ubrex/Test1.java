package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Test1 {

	@Test
	public void test1() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("if 〇*〴s 〴g  〶$IF1=〇*〴G 〴g 〇*〴s 〇?〘as 〇+〴s 〶$ASIF1=〇+「〴an_.」 〇+〴s 〙  〇?〘then〙");
		final UMatcher matcher = cut.match(TextNavigator.build("if \"\" then"));
		assertEquals("if \"\" then", matcher.getAcceptedMatch());
	}


}
