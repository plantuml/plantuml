package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CharSetPatternTest {

	private static boolean match(ChallengeCharSet pattern, char ch) {
		return pattern.runChallenge(TextNavigator.build("" + ch), 0).getFullCaptureLength() == 1;
	}

	/**
	 * Tests that adding valid characters works and that the {@code contains} method
	 * returns the correct result.
	 */
	@Test
	public void testAddAndContains() {
		ChallengeCharSet pattern = new ChallengeCharSet();

		// Add some valid characters
		pattern.addChar('A');
		pattern.addChar('z');
		pattern.addChar(' '); // lower bound (code 32)
		pattern.addChar((char) 128); // upper bound

		// Verify that the added characters are present
		assertTrue(match(pattern, 'A'));
		assertTrue(match(pattern, 'z'));
		assertTrue(match(pattern, ' '));
		assertTrue(match(pattern, (char) 128));

		// Verify that other characters are not present
		assertFalse(match(pattern, 'B'));
		assertFalse(match(pattern, 'b'));
		assertFalse(match(pattern, '0'));
	}

	/**
	 * Tests that adding a character outside the valid range throws an
	 * IllegalArgumentException.
	 */
	@Test
	public void testInvalidAddChar() {
		ChallengeCharSet pattern = new ChallengeCharSet();

		// Character below 32
		assertThrows(IllegalArgumentException.class, () -> {
			pattern.addChar((char) 31);
		});

		// Character above 128
		assertThrows(IllegalArgumentException.class, () -> {
			pattern.addChar((char) 129);
		});
	}

	/**
	 * Tests that calling {@code contains} with a character outside the valid range
	 * throws an IllegalArgumentException.
	 */
	@Test
	public void testInvalidContains() {
		ChallengeCharSet pattern = new ChallengeCharSet();

		// Check with a character below 32
		assertFalse(match(pattern, (char) 31));

		// Check with a character above 128
		assertFalse(match(pattern, (char) 129));
	}

	/**
	 * Tests adding multiple characters (possibly duplicates) and verifies that each
	 * is present.
	 */
	@Test
	public void testMultipleAdditions() {
		ChallengeCharSet pattern = new ChallengeCharSet();

		List<Character> characters = Arrays.asList('A', 'B', 'C', '1', '2', '3', '#', '$');

		// Add each character (even if added multiple times)
		for (char ch : characters) {
			pattern.addChar(ch);
			pattern.addChar(ch); // Verify that duplicate additions do not cause issues
		}

		// Verify that all added characters are present
		for (char ch : characters) {
			assertTrue(match(pattern, ch));
		}
	}

	/**
	 * Tests building a pattern from a string with individual characters. Pattern:
	 * 「abc」 should contain 'a', 'b', and 'c'.
	 */
	@Test
	public void testBuildWithSingleCharacters() {
		ChallengeCharSet pattern = ChallengeCharSet.build("abc");
		assertTrue(match(pattern, 'a'));
		assertTrue(match(pattern, 'b'));
		assertTrue(match(pattern, 'c'));
		assertFalse(match(pattern, 'd'));
	}

	/**
	 * Tests building a pattern from a string specifying a range. Pattern: 「a〜c」
	 * should contain 'a', 'b', and 'c'.
	 */
	@Test
	public void testBuildWithRange() {
		ChallengeCharSet pattern = ChallengeCharSet.build("a〜c");
		assertTrue(match(pattern, 'a'));
		assertTrue(match(pattern, 'b'));
		assertTrue(match(pattern, 'c'));
		assertFalse(match(pattern, 'd'));
	}

	/**
	 * Tests building a pattern that mixes individual characters and ranges.
	 * Pattern: 「-a〜z0〜9」 should contain '-' plus all lowercase letters from 'a' to
	 * 'z' and all digits from '0' to '9'.
	 */
	@Test
	public void testBuildWithMultipleRangesAndCharacters() {
		ChallengeCharSet pattern = ChallengeCharSet.build("-a〜z0〜9");

		// Check for the individual character.
		assertTrue(match(pattern, '-'));

		// Check for the range of lowercase letters.
		for (char c = 'a'; c <= 'z'; c++) {
			assertTrue(match(pattern, c));
		}

		// Check for the range of digits.
		for (char c = '0'; c <= '9'; c++) {
			assertTrue(match(pattern, c));
		}

		// Verify a character that is not included.
		assertFalse(match(pattern, '.'));
	}

	/**
	 * Tests that a pattern with an incomplete range (missing the end character)
	 * throws an IllegalArgumentException.
	 */
	@Test
	public void testBuildWithIncompleteRange() {
		// Pattern: 「a〜」 is invalid because the range operator is not followed by a
		// character.
		assertThrows(IllegalArgumentException.class, () -> {
			ChallengeCharSet.build("a〜");
		});
	}

	/**
	 * Tests that a pattern with an invalid range order throws an
	 * IllegalArgumentException. For example, a range where the start character is
	 * greater than the end character.
	 */
	@Test
	public void testBuildWithInvalidRangeOrder() {
		// Pattern: 「z〜a」 is invalid because 'z' is greater than 'a'.
		assertThrows(IllegalArgumentException.class, () -> {
			ChallengeCharSet.build("z〜a");
		});
	}

	/**
	 * Tests building a pattern with empty inner content. Pattern: 「」 should result
	 * in an empty character set.
	 */
	@Test
	public void testBuildWithEmptyInnerContent() {
		assertThrows(IllegalArgumentException.class, () -> {
			ChallengeCharSet pattern = ChallengeCharSet.build("");
		});
	}

	/**
	 * Tests building a pattern with empty inner content. Pattern: 「」 should result
	 * in an empty character set.
	 */
	@Test
	public void test1() {
		assertThrows(IllegalArgumentException.class, () -> {
			ChallengeCharSet pattern = ChallengeCharSet.build("f〜");
		});
	}

	/**
	 * Case 1: Test addRange when the entire range is in mask1. For example, the
	 * range from 'A' (65) to 'Z' (90) lies in mask1.
	 */
	@Test
	public void testAddRangeEntirelyInMask1() {
		ChallengeCharSet pattern = new ChallengeCharSet();
		pattern.addRange('A', 'Z'); // 'A' = 65, 'Z' = 90, all in mask1

		for (char c = 'A'; c <= 'Z'; c++) {
			assertTrue(match(pattern, c));
		}
		// Verify that a character outside this range is not included.
		assertFalse(match(pattern, '5'));
	}

	/**
	 * Case 2: Test addRange when the entire range is in mask2. For example, the
	 * range from 'a' (97) to 'z' (122) lies in mask2.
	 */
	@Test
	public void testAddRangeEntirelyInMask2() {
		ChallengeCharSet pattern = new ChallengeCharSet();
		pattern.addRange('a', 'z'); // 'a' = 97, 'z' = 122, all in mask2

		for (char c = 'a'; c <= 'z'; c++) {
			assertTrue(match(pattern, c));
		}
		// Verify that a character outside this range is not included.
		assertFalse(match(pattern, '5'));
	}

	@Test
	public void testAddRangeSpanningBothMasks() {
		ChallengeCharSet pattern = new ChallengeCharSet();
		pattern.addRange('1', '8');

		for (char c = '1'; c <= '8'; c++) {
			assertTrue(match(pattern, c));
		}
		// Verify characters immediately outside the range are not included.
		// '@' (64) comes just before 'A', and '{' (123) comes just after 'z'.
		assertFalse(match(pattern, '0'));
		assertFalse(match(pattern, '9'));
	}

}
