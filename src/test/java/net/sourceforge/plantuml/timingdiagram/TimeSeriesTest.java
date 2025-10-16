package net.sourceforge.plantuml.timingdiagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class TimeSeriesTest {

	private static TimeTick tt(double t) {
		return new TimeTick(BigDecimal.valueOf(t), null);
	}

	@Test
	void testPutAndGet() {
		TimeSeries ts = new TimeSeries();
		ts.put(tt(1.0), 42.0);
		ts.put(tt(2.0), -3.5);

		assertEquals(42.0, ts.get(tt(1.0)));
		assertEquals(-3.5, ts.get(tt(2.0)));
		assertNull(ts.get(tt(3.0)));
	}

	@Test
	void testMinMaxWithoutOverrides() {
		TimeSeries ts = new TimeSeries();

		// Empty case
		assertAlmostEquals(0.0, ts.getMin());
		assertAlmostEquals(10.0, ts.getMax());

		// All positive values -> min stays 0 (current logic)
		ts.put(tt(1), 2.0);
		ts.put(tt(2), 5.0);
		assertAlmostEquals(0.0, ts.getMin());
		assertAlmostEquals(5.0, ts.getMax());

		// Add negative values -> min reflects the smallest value
		ts.put(tt(3), -7.0);
		assertAlmostEquals(-7.0, ts.getMin());
		assertAlmostEquals(5.0, ts.getMax());

		// All negative values -> max would remain 0 then 10 by the rule max==0 ? 10
		TimeSeries tsNeg = new TimeSeries();
		tsNeg.put(tt(1), -1.0);
		tsNeg.put(tt(2), -3.0);
		assertAlmostEquals(-3.0, tsNeg.getMin()); // min starts at 0 and only decreases if value < 0
		assertAlmostEquals(10.0, tsNeg.getMax()); // max==0 => 10
	}

	@Test
	void testMinMaxWithOverrides() {
		TimeSeries ts = new TimeSeries();
		ts.put(tt(1), -2.0);
		ts.put(tt(2), 8.0);

		ts.setBounds("1.0", "9.5");
		assertEquals(1.0, ts.getMin(), 1e-12);
		assertEquals(9.5, ts.getMax(), 1e-12);

		// getDisplayValue must prefer exact labels over numeric formats
		assertEquals("1.0", ts.getDisplayValue(1.0));
		assertEquals("9.5", ts.getDisplayValue(9.5));
	}

	@Test
	void testDisplayValueFormatting() {
		TimeSeries ts = new TimeSeries();
		// No overrides here
		assertEquals("3.0", ts.getDisplayValue(3.0));
		assertEquals("3.5", ts.getDisplayValue(3.5));
		assertEquals("0.0", ts.getDisplayValue(0.0));
		assertEquals("-2.0", ts.getDisplayValue(-2.0));
	}

	@Test
	void testGetValueAtExactTick() {
		TimeSeries ts = new TimeSeries();
		ts.put(tt(0), 0.0);
		ts.put(tt(10), 10.0);

		assertAlmostEquals(0.0, ts.getValueAt(tt(0)));
		assertAlmostEquals(10.0, ts.getValueAt(tt(10)));
	}

	@Test
	void testGetValueAtInterpolation() {
		TimeSeries ts = new TimeSeries();
		ts.put(tt(0), 0.0);
		ts.put(tt(10), 10.0);

		// Middle: 5 -> 5
		assertAlmostEquals(5.0, ts.getValueAt(tt(5)));

		// Another ratio: at t=2, p = (2-0)/(10-0)=0.2 => 0 + (10-0)*0.2 = 2
		assertAlmostEquals(2.0, ts.getValueAt(tt(2)));
	}

	@Test
	void testGetValueAtBeforeFirst() {
		TimeSeries ts = new TimeSeries();
		ts.put(tt(10), 7.0);
		ts.put(tt(20), 12.0);

		// tick=5 < 10 -> loop returns v2 of the first point (7.0)
		assertAlmostEquals(7.0, ts.getValueAt(tt(5)));
	}

	@Test
	void testGetValueAtAfterLast() {
		TimeSeries ts = new TimeSeries();
		ts.put(tt(1), 2.0);
		ts.put(tt(3), 6.0);

		// tick=4 > 3 -> loop ends and returns the value of the last point (6.0)
		assertAlmostEquals(6.0, ts.getValueAt(tt(4)));
	}

	@Test
	void testGetValueAtEmptySeries() {
		TimeSeries ts = new TimeSeries();
		assertThrows(NullPointerException.class, () -> ts.getValueAt(tt(0)));
	}

	private static void assertAlmostEquals(double expected, double actual) {
		assertEquals(expected, actual, 1e-12, () -> "Expected " + expected + " but was " + actual);
	}

}
