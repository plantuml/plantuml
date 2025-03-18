package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NamedGroup2Test {

	@Test
	public void testNamedGroup10() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〶$gr=〘 〇*〴s  〇+〴d 〙");
		UMatcher matcher = cut.match(TextNavigator.build("000"));
		assertTrue(matcher.startMatch());
		// assertEquals("{gr=000}", matcher.getNamedCaptures().toString());
		assertEquals("[000]", matcher.getCapture("gr").toString());
	}

	@Test
	public void testNamedGroup20() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〇+〘 〇*〴s  〶$num=〇+〴d 〙");
		UMatcher matcher = cut.match(TextNavigator.build("123 456"));
		assertTrue(matcher.startMatch());
		// assertEquals("{num=[123, 456]}", matcher.getNamedCaptures().toString());
		assertEquals("[123, 456]", matcher.getCapture("num").toString());
	}

	@Test
	public void testNamedGroup21() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〇+〘 〇*〴s  〶$num=〇+〴d 〙");
		UMatcher matcher = cut.match(TextNavigator.build("123 456 789"));
		assertTrue(matcher.startMatch());
		// assertEquals("{num=[123, 456, 789]}", matcher.getNamedCaptures().toString());
		assertEquals("[123, 456, 789]", matcher.getCapture("num").toString());
	}

	@Test
	public void testNamedGroup30() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〶$gr=〇+〘 〇*〴s  〶$num=〇+〴d 〙");
		UMatcher matcher = cut.match(TextNavigator.build("123 456"));
		assertTrue(matcher.startMatch());
		// assertEquals("{gr={num=[123, 456], =123 456}}", matcher.getNamedCaptures().toString());
		assertEquals("[123 456]", matcher.getCapture("gr").toString());
		assertEquals("[123, 456]", matcher.getCapture("gr/num").toString());
	}

	@Test
	public void testNamedGroup00() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〘 〇*〴s  〶$num=〇+〴d 〙");
		UMatcher matcher = cut.match(TextNavigator.build("123 456"));
		assertTrue(matcher.startMatch());
		// assertEquals("{num=123}", matcher.getNamedCaptures().toString());
		assertEquals("[123]", matcher.getCapture("num").toString());
	}

	@Test
	public void testNamedGroup01() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〇+〘 〇*〴s  〶$num=〇+〴d 〙");
		UMatcher matcher = cut.match(TextNavigator.build("123 456"));
		assertTrue(matcher.startMatch());
		// assertEquals("{num=[123, 456]}", matcher.getNamedCaptures().toString());
		assertEquals("[123, 456]", matcher.getCapture("num").toString());
	}

	@Test
	public void testNamedGroup02() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build(" 〶$gr=〘 〇*〴s  〶$num=〇+〴d 〙");
		UMatcher matcher = cut.match(TextNavigator.build("123 456"));
		assertTrue(matcher.startMatch());
		// assertEquals("{gr={num=123, =123}}", matcher.getNamedCaptures().toString());
		assertEquals("[123]", matcher.getCapture("gr").toString());
		assertEquals("[123]", matcher.getCapture("gr/num").toString());
	}

	@Test
	public void testNamedGroup31() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〶$gr=〇+〘 〇*〴s  〶$num=〇+〴d  〶$letter=〇*「a〜z」 〙");
		UMatcher matcher = cut.match(TextNavigator.build("123qsd 456 0ccc 0"));
		assertTrue(matcher.startMatch());
//		assertEquals("{gr={num=[123, 456, 0, 0], letter=[qsd, , ccc, ], =123qsd 456 0ccc 0}}",
//				matcher.getNamedCaptures().toString());
		assertEquals("[123qsd 456 0ccc 0]", matcher.getCapture("gr").toString());
		assertEquals("[123, 456, 0, 0]", matcher.getCapture("gr/num").toString());
		assertEquals("[qsd, , ccc, ]", matcher.getCapture("gr/letter").toString());
	}

	@Test
	public void testNamedGroup32() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("〶$gr=〇+〘 〇*「〴s_」  〶$num=〇+〴d  〶$letter=〇*「a〜z」 〙");
		UMatcher matcher = cut.match(TextNavigator.build("123qsd 456 0ccc 0"));
		assertTrue(matcher.startMatch());
//		assertEquals("{gr={num=[123, 456, 0, 0], letter=[qsd, , ccc, ], =123qsd 456 0ccc 0}}",
//				matcher.getNamedCaptures().toString());
		assertEquals("[123qsd 456 0ccc 0]", matcher.getCapture("gr").toString());
		assertEquals("[123, 456, 0, 0]", matcher.getCapture("gr/num").toString());
		assertEquals("[qsd, , ccc, ]", matcher.getCapture("gr/letter").toString());
	}

	@Test
	public void testNamedGroup33() {
		UnicodeBracketedExpression cut = UnicodeBracketedExpression
				.build("〇+〘 *  〶$gr=〇+〘 〇*「〴s_」  〶$num=〇+〴d  〶$letter=〇*「a〜z」 〙   〙");
		UMatcher matcher = cut.match(TextNavigator.build("*123qsd 456 1ccc 2*3aa"));
		assertTrue(matcher.startMatch());
//		assertEquals("{gr={num=[123, 456, 0, 0], letter=[qsd, , ccc, ], =123qsd 456 0ccc 0}}",
//				matcher.getNamedCaptures().toString());
		assertEquals("[123qsd 456 1ccc 2, 3aa]", matcher.getCapture("gr").toString());
		assertEquals("[123, 456, 1, 2, 3]", matcher.getCapture("gr/num").toString());
		assertEquals("[qsd, , ccc, , aa]", matcher.getCapture("gr/letter").toString());

	}

}
