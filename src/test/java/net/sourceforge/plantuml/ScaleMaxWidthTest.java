package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ScaleMaxWidthTest {

	@Test
	void testScaleBelowOneIsKept() {
		final ScaleMaxWidth cut = new ScaleMaxWidth(30);
		final double scale = cut.getScale(60, 999);
		assertEquals(0.5, scale, .001);
	}

	@Test
	void testScaleAboveOneIsCappedToOne() {
		final ScaleMaxWidth cut = new ScaleMaxWidth(100);
		final double scale = cut.getScale(50, 999);
		assertEquals(1.0, scale, .001);
	}

	@Test
	void testNonPositiveScaleIsClampedToOne() {
		final ScaleMaxWidth cut = new ScaleMaxWidth(-10);
		final double scale = cut.getScale(50, 999);
		assertEquals(1.0, scale, .001);
	}

}
