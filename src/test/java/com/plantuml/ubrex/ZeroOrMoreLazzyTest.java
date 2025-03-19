package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ZeroOrMoreLazzyTest {

	@Test
	public void test1() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a 〇l+〴w end  〒($)");
		assertEquals("abbbbend", cut.match(TextNavigator.build("abbbbend")).getAcceptedMatch());
	}


	@Test
	public void test2() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aa 〇l+〴w end  〒($)");
		assertEquals("aabbbbend", cut.match(TextNavigator.build("aabbbbend")).getAcceptedMatch());
	}


	@Test
	public void test3() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aa 〇l+〶$gr1=〴w 〶$gr2=end  〒($)");
		final UMatcher matcher = cut.match(TextNavigator.build("aabbbbend"));
		assertEquals("aabbbbend", matcher.getAcceptedMatch());
		assertEquals("[b, b, b, b]", matcher.getCapture("gr1").toString());
		assertEquals("[e]", matcher.getCapture("gr2").toString());

	}


	// @Test
	public void test4() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aa 〶$gr1=〇l+〴w   〶$gr2=end  〒($)");
		final UMatcher matcher = cut.match(TextNavigator.build("aabbbbend"));
		assertEquals("aabbbbend", matcher.getAcceptedMatch());
		assertEquals("[bbbb]", matcher.getCapture("gr1").toString());
		assertEquals("[e]", matcher.getCapture("gr2").toString());

	}


}
