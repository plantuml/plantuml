package net.sourceforge.plantuml.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StringLocatedFirstTokenTest {

	private static String firstToken(String line) {
		return new StringLocated(line, null).getFirstToken();
	}

	@Test
	void theFirstTokenIsTheLeadingRunOfLettersLowerCased() {
		assertEquals("title", firstToken("Title foo"));
		assertEquals("footbox", firstToken("  footbox off"));
		assertEquals("note", firstToken("note2"));
	}

	@Test
	void aLineStartingWithAnythingElseHasItsFirstCharacterForToken() {
		assertEquals("=", firstToken("== divider =="));
		assertEquals("}", firstToken("}"));
		assertEquals("-", firstToken("  -> B"));
		assertEquals("@", firstToken("@0"));
		assertEquals("1", firstToken("12:00"));
		assertEquals("\u00e9", firstToken("\u00e9l\u00e9ment"));
		assertEquals("\u00c9", firstToken("\u00c9l\u00e9ment"));
	}

	@Test
	void aBlankLineHasAnEmptyToken() {
		assertEquals("", firstToken(""));
		assertEquals("", firstToken(" \t\u00A0"));
	}

	@Test
	void theNonBreakingSpaceIsSkippedLikeAnyOtherLeadingBlank() {
		assertEquals("footbox", firstToken(" footbox"));
		assertEquals("title", firstToken("\t  title foo"));
		// Inside the line it still ends the token, as any blank does.
		assertEquals("hide", firstToken("hide footbox"));
	}

	@Test
	void aLineHasTheSameFirstTokenTrimmedOrNot() {
		final StringLocated line = new StringLocated("  autonumber", null);
		assertEquals(line.getFirstToken(), line.getTrimmed().getFirstToken());
	}

}
