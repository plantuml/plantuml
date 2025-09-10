package net.sourceforge.plantuml.klimt.color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class ColorTrieNodeTest {

	@Test
	void testInvalidCharacterIgnoredOnPut() {
		ColorTrieNode root = ColorTrieNode.INSTANCE;

		assertNull(root.getColor("dark-blue"));
		assertNotNull(root.getColor("darkblue"));
		assertEquals(new Color(0x00008B), root.getColor("darkblue"));
	}

}
