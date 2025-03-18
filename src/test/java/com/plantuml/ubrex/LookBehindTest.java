package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LookBehindTest {

	@Test
	public void test1() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("ab");
		assertTrue(cut.match(TextNavigator.build("ab")).startMatch());
	}

	@Test
	public void test2() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("abc");
		assertTrue(cut.match(TextNavigator.build("abc")).startMatch());
	}

	@Test
	public void test31() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〇*「〤_」  _");
		assertTrue(cut.match(TextNavigator.build("abcd_")).startMatch());
	}

	@Test
	public void test32() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〇*「〤_」  〒(<!)〴d  _");
		assertTrue(cut.match(TextNavigator.build("abcd_")).startMatch());
		assertTrue(cut.match(TextNavigator.build("_")).startMatch());
		assertFalse(cut.match(TextNavigator.build("0_")).startMatch());
	}

	@Test
	public void test4() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〇*「〤_」  〒(<=)〴d  _");
		assertFalse(cut.match(TextNavigator.build("abcd_")).startMatch());
		assertFalse(cut.match(TextNavigator.build("_")).startMatch());
		assertTrue(cut.match(TextNavigator.build("0_")).startMatch());
	}

	@Test
	public void test5() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〒(<!)「〴w」 _");
		assertTrue(cut.match(TextNavigator.build("abc._"), 4).startMatch());
		assertFalse(cut.match(TextNavigator.build("abcd_"), 4).startMatch());
		assertFalse(cut.match(TextNavigator.build("d_d"), 1).startMatch());
		assertTrue(cut.match(TextNavigator.build("_")).startMatch());
		assertFalse(cut.match(TextNavigator.build("._")).startMatch());
		assertTrue(cut.match(TextNavigator.build("._"), 1).startMatch());
		assertFalse(cut.match(TextNavigator.build("0_"), 1).startMatch());
	}

}
