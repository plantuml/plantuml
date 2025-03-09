package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ScaleHeightTest {

	@Test
	void testScale2() {
		final ScaleHeight cut = new ScaleHeight(100);
		final double scale = cut.getScale(50, 50);
		assertEquals(2.0, scale, .001);
	}

}
