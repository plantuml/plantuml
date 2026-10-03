package net.sourceforge.plantuml.tim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

class TrieImplTest {

	private static TrieImpl trie(String... words) {
		final TrieImpl result = new TrieImpl();
		for (String w : words)
			result.add(w);
		return result;
	}

	@Test
	void empty_trie_matches_nothing() {
		final TrieImpl trie = new TrieImpl();
		assertEquals("", trie.getLonguestMatchStartingIn("abc", 0));
		assertEquals("", trie.getLonguestMatchStartingIn("abc", 3));
		assertEquals("", trie.getLonguestMatchStartingIn("", 0));
	}

	@Test
	void exact_match_and_match_followed_by_other_chars() {
		final TrieImpl trie = trie("$foo");
		assertEquals("$foo", trie.getLonguestMatchStartingIn("$foo", 0));
		assertEquals("$foo", trie.getLonguestMatchStartingIn("$foo+1", 0));
		assertEquals("$foo", trie.getLonguestMatchStartingIn("x=$foo;", 2));
		assertEquals("", trie.getLonguestMatchStartingIn("x=$foo;", 1));
		assertEquals("", trie.getLonguestMatchStartingIn("$fo", 0));
		assertEquals("", trie.getLonguestMatchStartingIn("$bar", 0));
	}

	@Test
	void longest_word_wins() {
		final TrieImpl trie = trie("$a", "$ab", "$abc");
		assertEquals("$a", trie.getLonguestMatchStartingIn("$a+", 0));
		assertEquals("$ab", trie.getLonguestMatchStartingIn("$ab+", 0));
		assertEquals("$abc", trie.getLonguestMatchStartingIn("$abcd", 0));
	}

	@Test
	void walk_is_greedy_and_does_not_backtrack() {
		// Known behaviour, kept on purpose: callers reject a match followed by
		// a letter anyway, so backtracking to "$a" would not change anything.
		final TrieImpl trie = trie("$a", "$abc");
		assertEquals("", trie.getLonguestMatchStartingIn("$ab+", 0));
		assertEquals("", trie.getLonguestMatchStartingIn("$ab", 0));
	}

	@Test
	void function_names_are_stored_with_their_parenthesis() {
		final TrieImpl trie = trie("%strlen(", "%str(");
		assertEquals("%strlen(", trie.getLonguestMatchStartingIn("%strlen(\"x\")", 0));
		assertEquals("%str(", trie.getLonguestMatchStartingIn("%str(1)", 0));
		assertEquals("", trie.getLonguestMatchStartingIn("%strle(", 0));
	}

	@Test
	void empty_word_matches_empty_string_everywhere() {
		final TrieImpl trie = trie("");
		assertEquals("", trie.getLonguestMatchStartingIn("abc", 1));
		trie.add("b");
		assertEquals("b", trie.getLonguestMatchStartingIn("abc", 1));
	}

	@Test
	void add_rejects_nul_character() {
		assertThrows(IllegalArgumentException.class, () -> new TrieImpl().add("a\0b"));
	}

	@Test
	void remove_returns_whether_the_word_was_present() {
		final TrieImpl trie = trie("$ab", "$abc");
		assertFalse(trie.remove("$a"));
		assertFalse(trie.remove("$abcd"));
		assertFalse(trie.remove("$x"));
		assertTrue(trie.remove("$ab"));
		assertFalse(trie.remove("$ab"));
		assertEquals("", trie.getLonguestMatchStartingIn("$ab+", 0));
		assertEquals("$abc", trie.getLonguestMatchStartingIn("$abc+", 0));
	}

	@Test
	void remove_rejects_empty_word() {
		assertThrows(UnsupportedOperationException.class, () -> trie("a").remove(""));
	}

	@Test
	void removed_branch_does_not_hide_shorter_word() {
		final TrieImpl trie = trie("$a", "$ab");
		assertTrue(trie.remove("$ab"));
		assertEquals("$a", trie.getLonguestMatchStartingIn("$ab", 0));
		trie.add("$ab");
		assertEquals("$ab", trie.getLonguestMatchStartingIn("$ab", 0));
	}

	@Test
	void node_with_many_children_including_non_ascii() {
		// More than 8 children forces the switch to the direct ASCII table
		final TrieImpl trie = new TrieImpl();
		final String firsts = "éabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$%中à";
		for (int i = 0; i < firsts.length(); i++)
			trie.add(firsts.charAt(i) + "x");

		for (int i = 0; i < firsts.length(); i++) {
			final String word = firsts.charAt(i) + "x";
			assertEquals(word, trie.getLonguestMatchStartingIn("-" + word + "-", 1));
			assertEquals("", trie.getLonguestMatchStartingIn(firsts.charAt(i) + "y", 0));
		}
		assertEquals("", trie.getLonguestMatchStartingIn("èx", 0));
		assertEquals("", trie.getLonguestMatchStartingIn("\u007fx", 0));
	}

	@Test
	void surrogate_pairs_are_plain_chars() {
		final String smile = "😀";
		final TrieImpl trie = trie("$" + smile, "$" + smile + smile);
		assertEquals("$" + smile, trie.getLonguestMatchStartingIn("$" + smile + "!", 0));
		assertEquals("$" + smile + smile, trie.getLonguestMatchStartingIn("$" + smile + smile, 0));
		assertEquals("", trie.getLonguestMatchStartingIn("$\ud83d", 0));
	}

	@Test
	void random_operations_agree_with_reference_model() {
		for (int seed = 0; seed < 500; seed++) {
			final Random rnd = new Random(seed);
			final String alphabet = seed % 2 == 0 ? "ab$(" : "abcdefghijxyz_$(%é";
			final TrieImpl trie = new TrieImpl();
			final Reference reference = new Reference();
			for (int op = 0; op < 200; op++) {
				final int kind = rnd.nextInt(10);
				if (kind < 3) {
					final String word = randomString(rnd, alphabet, 5);
					trie.add(word);
					reference.add(word);
				} else if (kind < 5) {
					final String word = randomString(rnd, alphabet, 5);
					if (word.isEmpty())
						continue;
					assertEquals(reference.remove(word), trie.remove(word), "seed " + seed + " remove " + word);
				} else {
					final String s = randomString(rnd, alphabet, 8);
					for (int pos = 0; pos <= s.length(); pos++)
						assertEquals(reference.getLonguestMatchStartingIn(s, pos),
								trie.getLonguestMatchStartingIn(s, pos), "seed " + seed + " '" + s + "' at " + pos);
				}
			}
		}
	}

	private static String randomString(Random rnd, String alphabet, int maxLength) {
		final int length = rnd.nextInt(maxLength + 1);
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
		return sb.toString();
	}

	/**
	 * Naive model of TrieImpl: nodes are all prefixes of every word ever added
	 * (remove() does not prune them), and the walk stops before a node that
	 * neither ends a word nor has any child.
	 */
	static class Reference {

		private final Set<String> nodes = new HashSet<>();
		private final Set<String> words = new HashSet<>();
		private final List<String> added = new ArrayList<>();

		void add(String word) {
			for (int i = 0; i <= word.length(); i++)
				nodes.add(word.substring(0, i));
			words.add(word);
			added.add(word);
		}

		boolean remove(String word) {
			return words.remove(word);
		}

		private boolean hasChild(String node) {
			for (String w : added)
				if (w.length() > node.length() && w.startsWith(node))
					return true;
			return false;
		}

		String getLonguestMatchStartingIn(String s, int pos) {
			String current = "";
			int i = pos;
			while (i < s.length()) {
				final String child = current + s.charAt(i);
				if (nodes.contains(child) == false || (words.contains(child) == false && hasChild(child) == false))
					break;
				current = child;
				i++;
			}
			return words.contains(current) ? current : "";
		}
	}

}
