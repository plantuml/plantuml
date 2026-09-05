package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ScaleWidthAndHeightTest {

	@Test
	void testScaleUsesTheSmallerOfBothRatios() {
		final ScaleWidthAndHeight cut = new ScaleWidthAndHeight(100, 200);
		final double scale = cut.getScale(50, 50);
		assertEquals(2.0, scale, .001);
	}

	@Test
	void testScaleAboveFourIsClampedToFour() {
		final ScaleWidthAndHeight cut = new ScaleWidthAndHeight(1000, 1000);
		final double scale = cut.getScale(10, 10);
		assertEquals(4.0, scale, .001);
	}

	@Test
	void testNonPositiveScaleIsClampedToOne() {
		final ScaleWidthAndHeight cut = new ScaleWidthAndHeight(-10, 100);
		final double scale = cut.getScale(10, 10);
		assertEquals(1.0, scale, .001);
	}

}
