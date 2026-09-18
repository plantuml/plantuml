package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ScaleMaxWidthAndHeightTest {

	@Test
	void testScaleBelowOneIsKept() {
		final ScaleMaxWidthAndHeight cut = new ScaleMaxWidthAndHeight(30, 40);
		final double scale = cut.getScale(60, 100);
		assertEquals(0.4, scale, .001);
	}

	@Test
	void testScaleAboveOneIsCappedToOne() {
		final ScaleMaxWidthAndHeight cut = new ScaleMaxWidthAndHeight(100, 200);
		final double scale = cut.getScale(50, 50);
		assertEquals(1.0, scale, .001);
	}

	@Test
	void testNonPositiveScaleIsClampedToOne() {
		final ScaleMaxWidthAndHeight cut = new ScaleMaxWidthAndHeight(-10, 40);
		final double scale = cut.getScale(60, 100);
		assertEquals(1.0, scale, .001);
	}

}
