package net.sourceforge.plantuml.style.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class LexiqueTest {

	@Test
	void testAddAndContains() {
		Lexique lex = new Lexique();
		assertFalse(lex.contains("foo"));

		lex.add("foo");
		assertTrue(lex.contains("foo"));
		assertEquals(BigInteger.ONE, lex.getBitmask("foo"));

		lex.add("bar");
		assertTrue(lex.contains("bar"));
		assertEquals(BigInteger.valueOf(2), lex.getBitmask("bar"));

		BigInteger before = lex.getBitmask("foo");
		lex.add("foo");
		assertEquals(before, lex.getBitmask("foo"));
	}

	@Test
	void testBitmaskUniqueness() {
		Lexique lex = new Lexique();
		lex.add("foo").add("bar").add("baz");

		assertNotEquals(lex.getBitmask("foo"), lex.getBitmask("bar"));
		assertNotEquals(lex.getBitmask("bar"), lex.getBitmask("baz"));
		assertNotEquals(lex.getBitmask("foo"), lex.getBitmask("baz"));
	}

	@Test
	void testImmutability() {
		Lexique lex1 = new Lexique();
		lex1.add("foo").add("bar");
		lex1.froze();

		assertFalse(lex1.isMutable());

		Lexique lex2 = lex1.add("baz");
		assertTrue(lex2.isMutable());
		assertTrue(lex2.contains("baz"));
		assertTrue(lex1.contains("foo"));
		assertFalse(lex1.contains("baz"));
	}

	@Test
	void testToString() {
		Lexique lex = new Lexique();
		lex.add("a").add("b");
		String s = lex.toString();
		assertTrue(s.contains("mutable=true"));
		assertTrue(s.contains("a"));
		assertTrue(s.contains("b"));
	}
}