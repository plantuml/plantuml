package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OneOrMoreLazzyTest {

	@Test
	public void test10() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("a 〇l+〴w end  〒($)");
		assertEquals("abbbbend", cut.match(TextNavigator.build("abbbbend")).getAcceptedMatch());
	}

	@Test
	public void test20() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aa 〇l+〴w end  〒($)");
		assertEquals("aabbbbend", cut.match(TextNavigator.build("aabbbbend")).getAcceptedMatch());
	}

	@Test
	public void test30() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aa 〇l+〶$gr1=〴w 〶$gr2=end  〒($)");
		final UMatcher matcher = cut.match(TextNavigator.build("aabbbbend"));
		assertEquals("aabbbbend", matcher.getAcceptedMatch());
		assertEquals("[b, b, b, b]", matcher.getCapture("gr1").toString());
		assertEquals("[e]", matcher.getCapture("gr2").toString());

	}

	@Test
	public void test40() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aa 〶$gr1=〇l+〴w   〶$gr2=end  〒($)");
		final UMatcher matcher = cut.match(TextNavigator.build("aabbbbend"));
		assertEquals("aabbbbend", matcher.getAcceptedMatch());
		assertEquals("[bbbb]", matcher.getCapture("gr1").toString());
		assertEquals("[e]", matcher.getCapture("gr2").toString());
	}


	@Test
	public void test50() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aaa 〶$gr1=〇l+〴w   〶$gr2=end  〒($)");
		final UMatcher matcher = cut.match(TextNavigator.build("aaabbbbend"));
		assertEquals("aaabbbbend", matcher.getAcceptedMatch());
		assertEquals("[bbbb]", matcher.getCapture("gr1").toString());
		assertEquals("[e]", matcher.getCapture("gr2").toString());
	}

	@Test
	public void test60() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aaa 〶$gr1=〇l+〘〴w〴w〙   〶$gr2=end  〒($)");
		final UMatcher matcher = cut.match(TextNavigator.build("aaabbbbbbend"));
		assertEquals("aaabbbbbbend", matcher.getAcceptedMatch());
		assertEquals("[bbbbbb]", matcher.getCapture("gr1").toString());
		assertEquals("[e]", matcher.getCapture("gr2").toString());
	}

	@Test
	public void test61() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build(" 〶$gr1=〇l+〘〴w〴w〙   〶$gr2=end  〒($)");
		final UMatcher matcher = cut.match(TextNavigator.build("bbbbbbend"));
		assertEquals("bbbbbbend", matcher.getAcceptedMatch());
		assertEquals("[bbbbbb]", matcher.getCapture("gr1").toString());
		assertEquals("[e]", matcher.getCapture("gr2").toString());
	}

	@Test
	public void test70() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build(" 〶$gr1=〇l+〘〴w〴w〙  〶$gr2=end  〒($)");
		final UMatcher matcher = cut.match(TextNavigator.build("bbend"));
		assertEquals("bbend", matcher.getAcceptedMatch());
		assertEquals("[bb]", matcher.getCapture("gr1").toString());
		assertEquals("[e]", matcher.getCapture("gr2").toString());
	}

	@Test
	public void test71() {
		// UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aaaa 〶$gr1=〇l+〘〴w〴w〙   〶$gr2=end  〒($)");
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("aaaa 〶$gr1=〇l+〘〴w〴w〙   〶$gr2=end  〒($)");
		final UMatcher matcher = cut.match(TextNavigator.build("aaaabbend"));
		assertEquals("aaaabbend", matcher.getAcceptedMatch());
		assertEquals("[bb]", matcher.getCapture("gr1").toString());
		assertEquals("[e]", matcher.getCapture("gr2").toString());
	}

}
