package net.sourceforge.plantuml.regex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

class FirstTokensTest {

	private void assertTokens(String pattern, String... expected) {
		final Set<String> tokens = FirstTokens.from(pattern);
		assertEquals(new TreeSet<>(Arrays.asList(expected)).toString(), new TreeSet<>(tokens).toString(), pattern);
	}

	@Test
	void aWordIsItsOwnToken() {
		assertTokens("^title[%s]+(.*)$", "title");
		assertTokens("^autonumber[%s]*$", "autonumber");
		assertTokens("^sprite[%s]+\\$?([%pLN_]+)$", "sprite");
	}

	@Test
	void everyBranchOfAnAlternationCounts() {
		assertTokens("^(?:hide|show)[%s]+(.*)$", "hide", "show");
		assertTokens("^(skinparam|skinparamlocked)[%s]+(.*)$", "skinparam", "skinparamlocked");
		assertTokens("^(activate|deactivate|destroy|create)[%s]+(.*)$", "activate", "deactivate", "destroy", "create");
	}

	@Test
	void anElementThatMayBeEmptyDoesNotCloseTheToken() {
		// "[%s]*" matches nothing at all, so this pattern also accepts "endsplit", whose token is
		// the whole word. Reading "end" alone here would make the command unreachable for it.
		assertTokens("^end[%s]*split$", "end", "endsplit");
	}

	@Test
	void aLineStartingWithAnythingElseHasItsFirstCharacterForToken() {
		assertTokens("^!pragma[%s]+(.*)$", "!");
		assertTokens("^==[%s]*(.*)[%s]*==$", "=");
		assertTokens("^\\<style[%s]*\\>$", "<");
		assertTokens("^\\}$", "}");
		// Each digit is a token of its own.
		assertTokens("^(-?\\d{1,7})$", "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
		// A non-ASCII character too, as it is: the patterns do not fold its case.
		assertTokens("^(?:\\.{3}|\u2026)(.*)$", ".", "\u2026");
	}

	@Test
	void aClassIsReadAsTheCharactersItHolds() {
		assertTokens("^([-#+~])?[%s]*class[%s]+(.*)$", "-", "#", "+", "~", "class");
		assertTokens("^[+-]+(.*)$", "+", "-");
		assertTokens("^[0-3]$", "0", "1", "2", "3");
		assertTokens("^[%q](.*)$", "'", "\u2018", "\u2019");
	}

	@Test
	void theLettersOfAClassAreReadOneByOne() {
		assertTokens("^[%s]*[hx]-axis[%s]*$", "h", "x");
		assertTokens("^([vy]2?-axis)[%s]*$", "v", "y");
		// Case is folded: "[HX]" matches "h" as well.
		assertTokens("^[HX]-axis$", "h", "x");
		assertTokens("^[a-c]$", "a", "b", "c");
		assertTokens("^end[s-]?if$", "end", "endsif", "endif");
	}

	@Test
	void aLookaroundIsReadPastAsIfItWereNotThere() {
		assertTokens("^(&[%s]*)?(opt|alt|end)((?<!end)#\\w+)?(?:[%s]+(.*?))?$", "&", "opt", "alt", "end");
		assertTokens("^(?!x)title$", "title");
		assertTokens("^(?=title)title$", "title");
	}

	@Test
	void aBlankLineHasAnEmptyToken() {
		assertTokens("^[%s]*$", "");
		assertTokens("^[%s]*\\}?[%s]*$", "", "}");
	}

	@Test
	void aLeadingBlankIsSkippedLikeTheTokenReaderDoes() {
		// "%s" stands for "\s" plus U+00A0, and the token reader skips both, so a leading blank
		// adds no "" of its own: a line made of U+00A0 then "footbox" has "footbox" for a token.
		assertTokens("^(hide|show)?[%s]*footbox$", "footbox", "hide", "hidefootbox", "show", "showfootbox");
		assertTokens("^[%s]*title[%s]+(.*)$", "title");
		// Here "&" and "/" are not blanks: they are tokens of their own.
		assertTokens("^(&[%s]*)?(/)?[%s]*(note|hnote|rnote)[%s]+(.*)$", "&", "/", "note", "hnote", "rnote");
	}

	@Test
	void aClassThatMayBeABlankOrAPunctuationSignIsReadBothWays() {
		assertTokens("^[%s,]*title$", ",", "title");
	}

	@Test
	void whatCannotBeReadIsNotGuessed() {
		// "notefoo" matches this one too.
		assertNull(FirstTokens.from("^[%s]*note[%s]*(.*)$"));
		// "abab" matches this one.
		assertNull(FirstTokens.from("^(a|b)+$"));
		assertNull(FirstTokens.from("^[%s]*(.*)$"));
		// A class that may hold a letter, or that cannot be listed character by character.
		assertNull(FirstTokens.from("^[^x]+$"));
		assertNull(FirstTokens.from("^[%pLN_]+$"));
		assertNull(FirstTokens.from("^[\\w]+$"));
		assertNull(FirstTokens.from("^[!-~]$"));
		// Repeating a class of letters may make any word of them.
		assertNull(FirstTokens.from("^[hx]+$"));
		// A named group is not a lookaround.
		assertNull(FirstTokens.from("^(?<name>title)$"));
		// Commands are matched with find(), so an unanchored pattern says nothing about the start
		// of the line.
		assertNull(FirstTokens.from("title$"));
	}

}
