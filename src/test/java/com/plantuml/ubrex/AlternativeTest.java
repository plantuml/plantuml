package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AlternativeTest {

	@Test
	public void test1() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("【a┇b┇c】");
		assertEquals("a", cut.match(TextNavigator.build("a")).getAcceptedMatch());
		assertEquals("b", cut.match(TextNavigator.build("b")).getAcceptedMatch());
		assertEquals("c", cut.match(TextNavigator.build("c")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("h")).getAcceptedMatch());
	}

	@Test
	public void test2() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("【ab┇cd】");
		assertEquals("ab", cut.match(TextNavigator.build("ab")).getAcceptedMatch());
		assertEquals("cd", cut.match(TextNavigator.build("cd")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("c")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("h")).getAcceptedMatch());
	}

	@Test
	public void test3() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("【〇+a┇〇+b┇〇?c】");
		assertEquals("", cut.match(TextNavigator.build("")).getAcceptedMatch());
		assertEquals("aaaa", cut.match(TextNavigator.build("aaaa")).getAcceptedMatch());
		assertEquals("bb", cut.match(TextNavigator.build("bb")).getAcceptedMatch());
		assertEquals("c", cut.match(TextNavigator.build("c")).getAcceptedMatch());
	}

	@Test
	public void test4() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("partition∙〇+〴S∙【 # 〇{6}「0〜9a〜fA〜F」┇ 〇?# 〇+〴w 】");
		assertEquals("partition to #012345", cut.match("partition to #012345").getAcceptedMatch());
		assertEquals("partition to gray", cut.match("partition to gray").getAcceptedMatch());
	}

	@Test
	public void test5() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("partition∙〇+〴S∙# 〇{6}「0〜9a〜fA〜F」");
		assertEquals("partition to #012345", cut.match("partition to #012345").getAcceptedMatch());
	}

	@Test
	public void test50() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("partition∙〇+〴S∙# 〇+「0〜9」");
		assertEquals("partition to #012345", cut.match("partition to #012345").getAcceptedMatch());
	}


}
