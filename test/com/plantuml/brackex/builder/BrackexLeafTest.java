package com.plantuml.brackex.builder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.plantuml.brackex.BMatcher;
import com.plantuml.brackex.BracketedExpression;
import com.plantuml.brackex.TextNavigator;

class BrackexLeafTest {

	@Test
	void test1() {
		BrackexLeaf cut = new BrackexLeaf("else");
		BMatcher m = cut.match("else");

		assertTrue(m.startMatch());
		assertTrue(m.exactMatch());
	}

	@Test
	void test2() {
		BrackexLeaf cut = new BrackexLeaf("else");
		BMatcher m = cut.match("elsex");

		assertTrue(m.startMatch());
		assertFalse(m.exactMatch());
	}

	@Test
	void test3() {
		final TextNavigator string = TextNavigator.build("Xelse");

		BrackexLeaf cut = new BrackexLeaf("else");

		assertFalse(cut.match(string).startMatch());
		assertFalse(cut.match(string).exactMatch());

		assertTrue(cut.match(string, 1).startMatch());
		assertTrue(cut.match(string, 1).exactMatch());

	}

	@Test
	void test4() {
		final TextNavigator string = TextNavigator.build("Xelse");
		string.jump(1);
		BrackexLeaf cut = new BrackexLeaf("else");
		assertTrue(cut.match(string).startMatch());
		assertTrue(cut.match(string).exactMatch());

	}

	@Test
	void test5() {
		final TextNavigator string = TextNavigator.build("Xelse");
		BrackexLeaf cut = new BrackexLeaf("else");
		assertTrue(cut.match(string, 1).startMatch());

	}

	@Test
	void test6() {
		BrackexLeaf cut = new BrackexLeaf("else");
		assertTrue(cut.match(TextNavigator.build("Xelse"), 1).startMatch());
		assertFalse(cut.match(TextNavigator.build("XelseZ"), 1).exactMatch());

	}

	@Test
	void test7() {
		BracketedExpression cut = BrackexConcat.build( //
				new BrackexLeaf("else"), //
				BrackexLeaf.end() //
		);
		assertTrue(cut.match("else").exactMatch());
		assertFalse(cut.match("else ").exactMatch());

	}

}
