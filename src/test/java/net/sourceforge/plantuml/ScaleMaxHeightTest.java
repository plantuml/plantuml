package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ScaleMaxHeightTest {

	@Test
	void testScaleBelowOneIsKept() {
		final ScaleMaxHeight cut = new ScaleMaxHeight(50);
		final double scale = cut.getScale(999, 100);
		assertEquals(0.5, scale, .001);
	}

	@Test
	void testScaleAboveOneIsCappedToOne() {
		final ScaleMaxHeight cut = new ScaleMaxHeight(100);
		final double scale = cut.getScale(999, 50);
		assertEquals(1.0, scale, .001);
	}

	@Test
	void testNonPositiveScaleIsClampedToOne() {
		final ScaleMaxHeight cut = new ScaleMaxHeight(-10);
		final double scale = cut.getScale(999, 50);
		assertEquals(1.0, scale, .001);
	}

}
