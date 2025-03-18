package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ReversedTest {

	public static TextNavigator from(String string, int position) {
		final TextNavigator origin = TextNavigator.build(string);
		return origin.reverse(position);
	}

	@Test
	public void test0() {
		TextNavigator cut = from("abcd", 0);
		assertEquals("", cut.toString());
		assertEquals(0, cut.length());
	}

	@Test
	public void test1() {
		TextNavigator cut = from("abcd", 1);
		assertEquals("a", cut.toString());
		assertEquals(1, cut.length());
		assertEquals('a', cut.charAt(0));
	}

	@Test
	public void test2() {
		TextNavigator cut = from("abcd", 2);
		assertEquals("ba", cut.toString());
		assertEquals(2, cut.length());
		assertEquals('b', cut.charAt(0));
		assertEquals('a', cut.charAt(1));
	}

	@Test
	public void test3() {
		TextNavigator cut = from("abcd", 3);
		assertEquals("cba", cut.toString());
		assertEquals(3, cut.length());
		assertEquals('c', cut.charAt(0));
		assertEquals('b', cut.charAt(1));
		assertEquals('a', cut.charAt(2));
	}

	@Test
	public void test4() {
		TextNavigator cut = from("abcd", 4);
		assertEquals("dcba", cut.toString());
		assertEquals(4, cut.length());
		assertEquals('d', cut.charAt(0));
		assertEquals('c', cut.charAt(1));
		assertEquals('b', cut.charAt(2));
		assertEquals('a', cut.charAt(3));
	}

	@Test
	public void testSubSequenceComplete() {
		TextNavigator seq = from("abcd", 4); // "dcba"
		assertEquals("dcba", seq.subSequence(0, seq.length()).toString());
	}

	@Test
	public void testSubSequenceComplete1() {
		TextNavigator seq = from("abcd", 3); // "dcba"
		assertEquals("cba", seq.subSequence(0, seq.length()).toString());
	}

	@Test
	public void testSubSequencePartialDebut() {
		TextNavigator seq = from("abcd", 4); // "dcba"
		assertEquals("dc", seq.subSequence(0, 2).toString());
	}

	@Test
	public void testSubSequencePartialMilieu() {
		TextNavigator seq = from("abcd", 4); // "dcba"
		assertEquals("cb", seq.subSequence(1, 3).toString());
	}

	@Test
	public void testSubSequenceSurSequenceVide() {
		TextNavigator seq = from("abcd", 0); // ""
		assertEquals("", seq.subSequence(0, 0).toString());
	}

	@Test
	public void testSubSequenceIndicesEgaux() {
		TextNavigator seq = from("abcd", 3); // "cba"
		assertEquals("", seq.subSequence(1, 1).toString());
	}

	@Test
	public void testSubSequenceIndicesInvalidesNegatif() {
		TextNavigator seq = from("abcd", 3); // "cba"
		assertThrows(IndexOutOfBoundsException.class, () -> {
			seq.subSequence(-1, 2);
		});
	}

	@Test
	public void testSubSequenceIndicesInvalidesTropGrand() {
		TextNavigator seq = from("abcd", 3); // "cba"
		assertThrows(IndexOutOfBoundsException.class, () -> {
			seq.subSequence(1, 4);
		});
	}

	@Test
	public void testSubSequenceIndicesIncohérents() {
		TextNavigator seq = from("abcd", 4); // "dcba"
		assertThrows(IndexOutOfBoundsException.class, () -> {
			seq.subSequence(3, 2);
		});
	}

}
