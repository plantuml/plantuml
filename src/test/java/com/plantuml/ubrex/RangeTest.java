package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RangeTest {

	@Test
	public void test1() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("「〴w」");
		assertEquals("h", cut.match(TextNavigator.build("h")).getAcceptedMatch());
	}


	@Test
	public void test2() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("「〤〴d」");
		assertEquals("h", cut.match(TextNavigator.build("h")).getAcceptedMatch());
	}


	@Test
	public void test3() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("「〤h」");
		assertEquals("", cut.match(TextNavigator.build("h")).getAcceptedMatch());
	}


	@Test
	public void test4() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("「〤p」");
		assertEquals("h", cut.match(TextNavigator.build("h")).getAcceptedMatch());
	}



//	@Test
//	public void testPatternTrieMatching01() {
//		BracketedExpression1 cut = new BracketedExpression1();
//		cut.addPattern("he");
//		cut.addPattern("hello");
//		cut.addPattern("helium");
//		cut.addPattern("z「a〜c」「a〜e」ou");
//		assertEquals("zaaou", cut.getMatcher(TextNavigator.build("zaaou", 0)).getAcceptedMatch());
//		assertEquals("zaeou", cut.getMatcher(TextNavigator.build("zaeou", 0)).getAcceptedMatch());
//		assertEquals("", cut.getMatcher(TextNavigator.build("zaou", 0)).getAcceptedMatch());
//	}
//	
//	@Test
//	public void testPatternTrieMatching() {
//		BracketedExpression1 cut = new BracketedExpression1();
//		cut.addPattern("he");
//		cut.addPattern("hello");
//		cut.addPattern("helium");
//		cut.addPattern("「a〜c」ou");
//
//		// Test matching for patterns "he", "hello", and "helium"
//		// For input "hello", the longest complete match should be "hello"
//		assertEquals("hello", cut.getMatcher(TextNavigator.build("hello", 0)).getAcceptedMatch());
//
//		// For input "helium", the longest complete match should be "helium"
//		assertEquals("helium", cut.getMatcher(TextNavigator.build("helium", 0)).getAcceptedMatch());
//
//		// For input "hel", only the pattern "he" is complete, so the match should be
//		// "he"
//		assertEquals("he", cut.getMatcher(TextNavigator.build("hel", 0)).getAcceptedMatch());
//
//		// Test matching of the pattern with range "「a〜c」ou":
//		// This pattern should match any string starting with 'a', 'b' or 'c' followed
//		// by "ou".
//		// For input "aou", the match should be "aou".
//		assertEquals("aou", cut.getMatcher(TextNavigator.build("aou", 0)).getAcceptedMatch());
//
//		// For input "bou", the match should be "bou".
//		assertEquals("bou", cut.getMatcher(TextNavigator.build("bou", 0)).getAcceptedMatch());
//
//		// For input "cou", the match should be "cou".
//		assertEquals("cou", cut.getMatcher(TextNavigator.build("cou", 0)).getAcceptedMatch());
//
//		// For input "dou", no match should be found (since 'd' is not in the range
//		// a〜c).
//		assertEquals("", cut.getMatcher(TextNavigator.build("dou", 0)).getAcceptedMatch());
//
//		// Test matching with an offset:
//		// For the string "say hello", starting at index 4, the longest match should be
//		// "hello".
//		assertEquals("hello", cut.getMatcher(TextNavigator.build("say hello", 4)).getAcceptedMatch());
//	}

}
