package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SimpleTest {

	/**
	 * Tests the basic functionality of add() and getLonguestMatchStartingIn() using
	 * a single word.
	 */
	@Test
	public void testAddAndGetLongestMatch() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("hello");

		// Test exact match
		assertEquals("hello", cut.match(TextNavigator.build("hello")).getAcceptedMatch());

		// Test match with additional trailing characters
		assertEquals("hello", cut.match(TextNavigator.build("hello world")).getAcceptedMatch());

		// Test match starting at an offset
		assertEquals("hello", cut.match(TextNavigator.build("say hello"), 4).getAcceptedMatch());

		// Test no match when the prefix is not present
		assertEquals("", cut.match(TextNavigator.build("hi there")).getAcceptedMatch());
	}

//	/**
//	 * Tests that adding a string containing the null character ('\0') throws an
//	 * IllegalArgumentException.
//	 */
//	@Test
//	public void testAddThrowsExceptionForStringWithNullChar() {
//		BracketedExpression1 cut = new BracketedExpression1();
//		assertThrows(IllegalArgumentException.class, () -> {
//			cut.addPattern("he\0llo");
//		});
//	}
//
//	/**
//	 * Tests getLonguestMatchStartingIn() with multiple words added to the trie. In
//	 * this test the trie contains "he", "hello", and "helium".
//	 */
//	@Test
//	public void testLongestMatchWithMultipleWords() {
//		BracketedExpression1 cut = new BracketedExpression1();
//		cut.addPattern("he");
//		cut.addPattern("hello");
//		cut.addPattern("helium");
//
//		// For input "hello", the longest match should be "hello"
//		assertEquals("hello", cut.getMatcher(TextNavigator.build("hello", 0)).getAcceptedMatch());
//
//		// For input "helium", the longest match should be "helium"
//		assertEquals("helium", cut.getMatcher(TextNavigator.build("helium", 0)).getAcceptedMatch());
//
//		assertEquals("he", cut.getMatcher(TextNavigator.build("helix", 0)).getAcceptedMatch());
//	}
//
//	/**
//	 * Tests getLonguestMatchStartingIn() when matching occurs at different offsets
//	 * within the input string.
//	 */
//	@Test
//	public void testLongestMatchWithOffset() {
//		BracketedExpression1 cut = new BracketedExpression1();
//		cut.addPattern("hello");
//		cut.addPattern("world");
//		String text = "hello world";
//
//		// Starting at index 0, the longest match should be "hello"
//		assertEquals("hello", cut.getMatcher(TextNavigator.build(text, 0)).getAcceptedMatch());
//
//		// Starting at index 6, the longest match should be "world"
//		assertEquals("world", cut.getMatcher(TextNavigator.build(text, 6)).getAcceptedMatch());
//
//		// Starting at an index with a space character, no word should match.
//		assertEquals("", cut.getMatcher(TextNavigator.build(text, 5)).getAcceptedMatch());
//	}

	/**
	 * Tests the behavior of getLonguestMatchStartingIn() when the starting position
	 * is at the end of the input.
	 */
	@Test
	public void testLongestMatchAtEnd() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("end");
		String text = "the end";

		// Starting at index corresponding to "end"
		assertEquals("end", cut.match(TextNavigator.build(text), 4).getAcceptedMatch());

		// Starting at an index equal to the text length should return an empty string.
		assertEquals("", cut.match(TextNavigator.build(text), text.length()).getAcceptedMatch());
	}
}
