package com.plantuml.brackex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LookAheadTest2 {

	@Test
	public void test1a() {
		BracketedExpression cut = BracketedExpression.build("〇+〘「〤|」  〒(=)Z 〙");
		assertEquals("b", cut.match(TextNavigator.build("bZ")).getAcceptedMatch());
	}

	@Test
	public void test4a() {
		BracketedExpression cut = BracketedExpression.build("〇+〘「〤|」  〒(=)Z 〙");
		assertEquals("bZ", cut.match(TextNavigator.build("bZZ")).getAcceptedMatch());
	}

	@Test
	public void test4() {
		BracketedExpression cut = BracketedExpression.build("〇+〘「〤|」  〒(!)Z 〙");
		assertEquals("bb", cut.match(TextNavigator.build("bbbZ")).getAcceptedMatch());
	}

	@Test
	public void test5() {
		BracketedExpression cut = BracketedExpression.build("〇+〘「〤|」  〒(!)〘a | 〙 〙");
		assertEquals("bbbZ", cut.match(TextNavigator.build("bbbZ")).getAcceptedMatch());
	}

	@Test
	public void test6() {
		BracketedExpression cut = BracketedExpression.build("〇+〘「〤|」  〒(!)〘a | 〙 〙");
		assertEquals("bbbZ", cut.match(TextNavigator.build("bbbZ|")).getAcceptedMatch());
	}

	@Test
	public void test7() {
		BracketedExpression cut = BracketedExpression.build("〇+〘「〤|」  〒(!)〘a | 〙 〙");
		assertEquals("babbZ", cut.match(TextNavigator.build("babbZ|")).getAcceptedMatch());
	}

	@Test
	public void test8() {
		BracketedExpression cut = BracketedExpression.build("〇+〘「〤|」  〒(!)〘〇+a | 〙 〙");
		assertEquals("12", cut.match(TextNavigator.build("123a|")).getAcceptedMatch());
	}

}
