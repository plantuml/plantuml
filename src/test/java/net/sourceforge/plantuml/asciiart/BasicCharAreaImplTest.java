package net.sourceforge.plantuml.asciiart;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * A text placed left of or above the origin of the area (as "colors" in text
 * output does) threw an ArrayIndexOutOfBoundsException: it is clipped.
 */
class BasicCharAreaImplTest {

	@Test
	void text_starting_left_of_the_area_is_clipped() {
		final BasicCharAreaImpl area = new BasicCharAreaImpl();
		area.drawStringLR("abcd", -2, 0);
		assertEquals("cd", area.getLine(0));
	}

	@Test
	void text_above_the_area_is_ignored() {
		final BasicCharAreaImpl area = new BasicCharAreaImpl();
		area.drawStringLR("abcd", 0, -1);
		area.drawStringLR("ef", 1, 0);
		assertEquals(" ef", area.getLine(0));
	}

}
