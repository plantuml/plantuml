package net.sourceforge.plantuml.klimt.drawing.font;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UnicodeBlockTest {

	@Test
	void test00() {
		UnicodeBlock block = new UnicodeBlock(UnicodeFontWidthSansSerif.SANS_SERIF[0]);
		assertEquals(4.4, block.getWidth('!'), 0.01);
		assertEquals(10.7, block.getWidth('A'), 0.01);
	}

	@Test
	void test25() {
		UnicodeBlock block = new UnicodeBlock(UnicodeFontWidthSansSerif.SANS_SERIF[0x25]);
		assertEquals(11.3, block.getWidth('\u2500'), 0.01);
		assertEquals(16.0, block.getWidth('\u2501'), 0.01);
		assertEquals(16.0, block.getWidth('\u250F'), 0.01);
		assertEquals(16.0, block.getWidth('\u257F'), 0.01);
		assertEquals(11.3, block.getWidth('\u2580'), 0.01);
	}

}
