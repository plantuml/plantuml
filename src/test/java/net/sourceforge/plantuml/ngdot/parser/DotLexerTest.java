package net.sourceforge.plantuml.ngdot.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.ngdot.parser.DotToken.IdKind;

class DotLexerTest {

	private static String types(String text) throws DotSyntaxException {
		final List<String> result = new ArrayList<String>();
		for (DotToken token : DotLexer.tokenize(text))
			result.add(token.getType() == DotTokenType.ID ? token.getIdKind() + ":" + token.getText()
					: token.getType().name());
		return String.join(" ", result);
	}

	@Test
	void keywordsAreCaseInsensitive() throws DotSyntaxException {
		assertEquals("STRICT DIGRAPH NODE EDGE SUBGRAPH GRAPH EOF", types("Strict DiGraph NODE edge SubGraph graph"));
	}

	@Test
	void punctuation() throws DotSyntaxException {
		assertEquals("LBRACE RBRACE LBRACKET RBRACKET EQUAL SEMICOLON COMMA COLON PLUS ARROW LINE EOF",
				types("{}[]=;,:+->--"));
	}

	@Test
	void identifiersAndNumbers() throws DotSyntaxException {
		assertEquals("PLAIN:_a1 PLAIN:été NUMBER:-1.5 NUMBER:.5 NUMBER:3. NUMBER:42 EOF",
				types("_a1 été -1.5 .5 3. 42"));
	}

	@Test
	void numberFollowedByLetterIsSplit() throws DotSyntaxException {
		assertEquals("NUMBER:2 PLAIN:a EOF", types("2a"));
	}

	@Test
	void edgeOperatorBeforeNegativeNumber() throws DotSyntaxException {
		assertEquals("PLAIN:a ARROW NUMBER:-1 EOF", types("a->-1"));
	}

	@Test
	void quotedStringKeepsLabelEscapes() throws DotSyntaxException {
		final List<DotToken> tokens = DotLexer.tokenize("\"a\\\"b\\nc\\\\\" \"x\\\ny\"");
		assertEquals(IdKind.QUOTED, tokens.get(0).getIdKind());
		assertEquals("a\"b\\nc\\\\", tokens.get(0).getText());
		assertEquals("xy", tokens.get(1).getText());
	}

	@Test
	void htmlStringWithNestedBrackets() throws DotSyntaxException {
		final List<DotToken> tokens = DotLexer.tokenize("<<b>bold</b> text>");
		assertEquals(IdKind.HTML, tokens.get(0).getIdKind());
		assertEquals("<b>bold</b> text", tokens.get(0).getText());
	}

	@Test
	void comments() throws DotSyntaxException {
		assertEquals("PLAIN:a PLAIN:b PLAIN:c EOF", types("a // one\n/* two\n */ b\n# three\n  # four\nc\n"));
	}

	@Test
	void positions() throws DotSyntaxException {
		final List<DotToken> tokens = DotLexer.tokenize("a\n  /* x\n */ b");
		assertEquals(0, tokens.get(0).getLine());
		assertEquals(2, tokens.get(1).getLine());
		assertEquals(4, tokens.get(1).getColumn());
	}

	@Test
	void errors() {
		assertEquals(1, assertThrows(DotSyntaxException.class, () -> DotLexer.tokenize("a\n\"abc")).getLine());
		assertThrows(DotSyntaxException.class, () -> DotLexer.tokenize("<a<b>"));
		assertThrows(DotSyntaxException.class, () -> DotLexer.tokenize("/* abc"));
		assertThrows(DotSyntaxException.class, () -> DotLexer.tokenize("a @ b"));
		// '#' starts a comment only at the beginning of a line
		assertThrows(DotSyntaxException.class, () -> DotLexer.tokenize("c # d"));
	}

}
