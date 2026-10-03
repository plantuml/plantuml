package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ScaleSimpleTest {

	@Test
	void testScaleIsIndependentOfWidthAndHeight() {
		final ScaleSimple cut = new ScaleSimple(2.5);
		assertEquals(2.5, cut.getScale(10, 10), .001);
		assertEquals(2.5, cut.getScale(9999, 1), .001);
	}

	@Test
	void testScaleAboveFourIsClampedToFour() {
		final ScaleSimple cut = new ScaleSimple(10);
		assertEquals(4.0, cut.getScale(50, 50), .001);
	}

	@Test
	void testNonPositiveScaleIsClampedToOne() {
		final ScaleSimple cut = new ScaleSimple(0);
		assertEquals(1.0, cut.getScale(50, 50), .001);
	}

	@Test
	void testNegativeScaleIsClampedToOne() {
		final ScaleSimple cut = new ScaleSimple(-3);
		assertEquals(1.0, cut.getScale(50, 50), .001);
	}

}
