package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UrlUbrexTest {

	public static UnicodeBracketedExpression buildPart(String... parts) {
		final StringBuilder sb = new StringBuilder();
		for (String s : parts)
			sb.append(s);

		return UnicodeBracketedExpression.build(sb.toString());
	}

	
	@Test
	public void testTooltip() {
		String[] parts = { "〇?〘 〇*〴s { 〇*「〤{}」 } 〙" };
		UnicodeBracketedExpression sQuoted = buildPart(parts);
		assertEquals("{tooltip}", sQuoted.match(TextNavigator.build("{tooltip}")).getAcceptedMatch());

	}

	@Test
	public void test_S_QUOTED() {

		// UrlBuilder S_QUOTED
		String[] parts = { "[[ 〇*〴s", //
				"〃  〇+「〤〃」 〃", //
				"〇?〘 〇*〴s { 〇*「〤{}」 } 〙", //
				"〇?〘 〴s 「〤〴s{}[]」  〇*「〤[]」 〙", //
				"〇*〴s  ]]" };
		UnicodeBracketedExpression cut = buildPart(parts);

		assertEquals("[[\"hello1\"]]", cut.match(TextNavigator.build("[[\"hello1\"]]")).getAcceptedMatch());
		assertEquals("[[\"he llo2\"]]", cut.match(TextNavigator.build("[[\"he llo2\"]]")).getAcceptedMatch());
		assertEquals("[[\"he llo3\"]]", cut.match(TextNavigator.build("[[\"he llo3\"]]xx")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("x[[\"he llo4\"]]xxx")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("[x[\"he llo5\"]]xxx")).getAcceptedMatch());

		assertEquals("[[\"he llo6\"{tooltip}]]",
				cut.match(TextNavigator.build("[[\"he llo6\"{tooltip}]]")).getAcceptedMatch());

	}

	@Test
	public void test_S_ONLY_TOOLTIP() {

		// UrlBuilder S_ONLY_TOOLTIP
		String[] parts = { "[[ 〇*〴s", //
				"{  〇+「〤{}」 }", //
				"〇*〴s  ]]" };
		UnicodeBracketedExpression cut = buildPart(parts);

		assertEquals("[[ { hello1  }  ]]",
				cut.match(TextNavigator.build("[[ { hello1  }  ]]")).getAcceptedMatch());
		assertEquals("[[{hello1}]]", cut.match(TextNavigator.build("[[{hello1}]]xx")).getAcceptedMatch());

	}

	@Test
	public void test_S_ONLY_TOOLTIP_AND_LABEL() {

		// UrlBuilder S_ONLY_TOOLTIP_AND_LABEL
		String[] parts = { "[[ 〇*〴s", //
				"{  〇+「〤{}」 }", //
				"〇*〴s", //
				"「〤〴s[]{}」 〇*「〤[]」 ", //
				"〇*〴s  ]]" };
		UnicodeBracketedExpression cut = buildPart(parts);

		assertEquals("[[{hello1}toto]]", cut.match(TextNavigator.build("[[{hello1}toto]]")).getAcceptedMatch());
		assertEquals("[[{hello2}   toto{}  ]]",
				cut.match(TextNavigator.build("[[{hello2}   toto{}  ]]")).getAcceptedMatch());

	}

	@Test
	public void test_S_LINK_TOOLTIP_NOLABEL() {

		// UrlBuilder S_LINK_TOOLTIP_NOLABEL
		String[] parts = { "[[ 〇*〴s", //
				"〇+「〤〴s〴g{}[]」", //
				"〇*〴s", //
				"{  〇*「〤{}」  } ", //
				"〇*〴s  ]]" };
		UnicodeBracketedExpression cut = buildPart(parts);

		assertEquals("[[http://toto1 {foo}]]",
				cut.match(TextNavigator.build("[[http://toto1 {foo}]]")).getAcceptedMatch());

	}

	@Test
	public void test_S_LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL() {

		// UrlBuilder S_LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL
		String[] parts = { "[[ 〇*〴s", //
				"〇+「〤〴s〴g[]」", //
				"〇?〘  〇*〴s  {  〇*「〤{}」  } 〙", //
				"〇?〘  〴s   「〤〴s{}[]」  〇*「〤[]」   〙", //
				"〇*〴s  ]]" };
		UnicodeBracketedExpression cut = buildPart(parts);

		assertEquals("[[http://toto1]]", cut.match(TextNavigator.build("[[http://toto1]]")).getAcceptedMatch());
		assertEquals("[[http://toto2 {foo}]]",
				cut.match(TextNavigator.build("[[http://toto2 {foo}]]")).getAcceptedMatch());
		assertEquals("[[http://toto2 label]]",
				cut.match(TextNavigator.build("[[http://toto2 label]]")).getAcceptedMatch());
		assertEquals("[[http://toto2 my label]]",
				cut.match(TextNavigator.build("[[http://toto2 my label]]")).getAcceptedMatch());
		assertEquals("", cut.match(TextNavigator.build("[[http://toto2  my label]]")).getAcceptedMatch());
		assertEquals("[[http://toto2{tooltip} my label]]",
				cut.match(TextNavigator.build("[[http://toto2{tooltip} my label]]")).getAcceptedMatch());

	}

}
