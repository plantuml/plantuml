package com.plantuml.ubrex.builder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.plantuml.ubrex.TextNavigator;
import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.UnicodeBracketedExpression;

class UBrexLeafTest {

	@Test
	void test1() {
		UBrexLeaf cut = new UBrexLeaf("else");
		UMatcher m = cut.match("else");

		assertTrue(m.startMatch());
		assertTrue(m.exactMatch());
	}

	@Test
	void test2() {
		UBrexLeaf cut = new UBrexLeaf("else");
		UMatcher m = cut.match("elsex");

		assertTrue(m.startMatch());
		assertFalse(m.exactMatch());
	}

	@Test
	void test3() {
		final TextNavigator string = TextNavigator.build("Xelse");

		UBrexLeaf cut = new UBrexLeaf("else");

		assertFalse(cut.match(string).startMatch());
		assertFalse(cut.match(string).exactMatch());

		assertTrue(cut.match(string, 1).startMatch());
		assertTrue(cut.match(string, 1).exactMatch());

	}

	@Test
	void test4() {
		final TextNavigator string = TextNavigator.build("Xelse");
		string.jump(1);
		UBrexLeaf cut = new UBrexLeaf("else");
		assertTrue(cut.match(string).startMatch());
		assertTrue(cut.match(string).exactMatch());

	}

	@Test
	void test5() {
		final TextNavigator string = TextNavigator.build("Xelse");
		UBrexLeaf cut = new UBrexLeaf("else");
		assertTrue(cut.match(string, 1).startMatch());

	}

	@Test
	void test6() {
		UBrexLeaf cut = new UBrexLeaf("else");
		assertTrue(cut.match(TextNavigator.build("Xelse"), 1).startMatch());
		assertFalse(cut.match(TextNavigator.build("XelseZ"), 1).exactMatch());

	}

	@Test
	void test7() {
		UnicodeBracketedExpression cut = UBrexConcat.build( //
				new UBrexLeaf("else"), //
				UBrexLeaf.end() //
		);
		assertTrue(cut.match("else").exactMatch());
		assertFalse(cut.match("else ").exactMatch());

	}

}
