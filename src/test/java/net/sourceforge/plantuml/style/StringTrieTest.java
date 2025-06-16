package net.sourceforge.plantuml.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class StringTrieTest {

    @Test
    void testPutAndGetSimple() {
        StringTrie<Integer> trie = new StringTrie<>();
        trie.put("foo", 123);
        assertEquals(123, trie.get("foo"));
        assertNull(trie.get("bar"));
    }

    @Test
    void testCaseInsensitivity() {
        StringTrie<String> trie = new StringTrie<>();
        trie.put("Hello", "world");
        assertEquals("world", trie.get("hello"));
        assertEquals("world", trie.get("HELLO"));
        assertEquals("world", trie.get("HeLlO"));
    }

    @Test
    void testOverwriteValue() {
        StringTrie<Integer> trie = new StringTrie<>();
        trie.put("key", 1);
        trie.put("key", 2);
        assertEquals(2, trie.get("KEY"));
    }

    @Test
    void testPrefixCollision() {
        StringTrie<Integer> trie = new StringTrie<>();
        trie.put("abc", 10);
        trie.put("abcd", 20);
        assertEquals(10, trie.get("ABC"));
        assertEquals(20, trie.get("ABCD"));
        assertNull(trie.get("ab"));
    }

    @Test
    void testNullKey() {
        StringTrie<Integer> trie = new StringTrie<>();
        assertThrows(IllegalArgumentException.class, () -> trie.put(null, 1));
        assertNull(trie.get(null));
    }

    @Test
    void testEmptyStringKey() {
        StringTrie<String> trie = new StringTrie<>();
        trie.put("", "empty");
        assertEquals("empty", trie.get(""));
    }
}
