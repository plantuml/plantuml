package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UpToTest2 {

	@Test
	public void test1() {
		BracketedExpression cut = BracketedExpression.build("〄+〴w->z");
		assertEquals("abcdz", cut.match(TextNavigator.build("abcdzedf")).getAcceptedMatch());
	}

	@Test
	public void test2() {
		BracketedExpression cut = BracketedExpression.build("〄+〴w -> z");
		assertEquals("abcdz", cut.match(TextNavigator.build("abcdzedf")).getAcceptedMatch());
	}

	@Test
	public void test3() {
		BracketedExpression cut = BracketedExpression.build("〄+〶$txt=〴w -> z");
		assertEquals("abcdz", cut.match(TextNavigator.build("abcdzedf")).getAcceptedMatch());
		assertEquals("[a, b, c, d]", cut.match(TextNavigator.build("abcdzedf")).getCapture("txt").toString());

	}

	@Test
	public void test4() {
		BracketedExpression cut = BracketedExpression.build("〶$txt=〄+〴w -> z");
		// assertEquals("abcdz", cut.getMatcher(TextNavigator.build("abcdzedf")).getAcceptedMatch());
		assertEquals("[abcd]", cut.match(TextNavigator.build("abcdzedf")).getCapture("txt").toString());

	}

	@Test
	public void test5() {
		BracketedExpression cut = BracketedExpression.build("Z〶$txt=〄+〴w -> Z");
		assertEquals("ZabcdZ", cut.match(TextNavigator.build("ZabcdZedf")).getAcceptedMatch());
		assertEquals("[abcd]", cut.match(TextNavigator.build("ZabcdZedf")).getCapture("txt").toString());

	}

	@Test
	public void test6() {
		BracketedExpression cut = BracketedExpression.build("〇+〘 Z〶$txt=〄+〴w -> Z 〇*〴s 〙");
		assertEquals("ZabcdZ", cut.match(TextNavigator.build("ZabcdZedf")).getAcceptedMatch());
		assertEquals("[abcd]", cut.match(TextNavigator.build("ZabcdZedf")).getCapture("txt").toString());
	}

	@Test
	public void test7() {
		BracketedExpression cut = BracketedExpression.build("〇+〘 Z〶$txt=〄+〴w -> Z 〇*〴s 〙");
		assertEquals("ZabcdZ ZedfZ", cut.match(TextNavigator.build("ZabcdZ ZedfZ")).getAcceptedMatch());
		assertEquals("[abcd, edf]", cut.match(TextNavigator.build("ZabcdZ ZedfZ")).getCapture("txt").toString());
	}

	@Test
	public void test8() {
		BracketedExpression cut = BracketedExpression.build("〇+〘 Z〄+〶$txt=〴w -> Z 〇*〴s 〙");
		assertEquals("ZabcdZ ZedfZ", cut.match(TextNavigator.build("ZabcdZ ZedfZ")).getAcceptedMatch());
		assertEquals("[a, b, c, d, e, d, f]", cut.match(TextNavigator.build("ZabcdZ ZedfZ")).getCapture("txt").toString());
	}

}
