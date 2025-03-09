package net.sourceforge.plantuml.yaml.parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IndentationStackTest {

	@Test
	void testSize() {
		IndentationStack cut = new IndentationStack();
		assertEquals(0, cut.size());

		assertFalse(cut.contains(13));
		cut.push(13);
		assertTrue(cut.contains(13));

		assertEquals(1, cut.size());
		assertEquals(13, cut.peek());
		assertEquals(1, cut.size());
	}

	@Test
	void testComplex() {
		IndentationStack cut = new IndentationStack();
		assertEquals(0, cut.size());
		assertFalse(cut.contains(13));
		cut.push(13);
		assertTrue(cut.contains(13));
		assertEquals(1, cut.size());
		assertEquals(13, cut.peek());
		assertEquals(1, cut.size());

		assertTrue(cut.contains(13));
		assertFalse(cut.contains(24));
		cut.push(24);
		assertTrue(cut.contains(13));
		assertTrue(cut.contains(24));
		
		assertEquals(2, cut.size());
		assertEquals(24, cut.peek());

		assertThrows(IllegalArgumentException.class, () -> {
			cut.push(24);
		});

	}

	@Test
	void testPop() {
		IndentationStack cut = new IndentationStack();
		assertEquals(0, cut.size());
		assertFalse(cut.contains(13));
		cut.push(13);
		assertTrue(cut.contains(13));
		assertEquals(1, cut.size());
		assertEquals(13, cut.peek());
		assertEquals(1, cut.size());

		assertEquals(13, cut.pop());
		assertEquals(0, cut.size());
		assertFalse(cut.contains(13));

	}


	@Test
	void testPop2() {
		IndentationStack cut = new IndentationStack();
		
		cut.push(5);
		
		assertEquals(1, cut.size());
		assertFalse(cut.contains(13));
		cut.push(13);
		assertTrue(cut.contains(13));
		assertEquals(2, cut.size());
		assertEquals(13, cut.peek());
		assertEquals(2, cut.size());

		assertEquals(13, cut.pop());
		assertEquals(1, cut.size());
		assertFalse(cut.contains(13));

	}

}
