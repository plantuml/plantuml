package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ScaleWidthTest {

	@Test
	void testScale() {
		final ScaleWidth cut = new ScaleWidth(100);
		final double scale = cut.getScale(50, 999);
		assertEquals(2.0, scale, .001);
	}

	@Test
	void testScaleAboveFourIsClampedToFour() {
		final ScaleWidth cut = new ScaleWidth(1000);
		final double scale = cut.getScale(1, 999);
		assertEquals(4.0, scale, .001);
	}

	@Test
	void testNonPositiveScaleIsClampedToOne() {
		final ScaleWidth cut = new ScaleWidth(-10);
		final double scale = cut.getScale(1, 999);
		assertEquals(1.0, scale, .001);
	}

}
