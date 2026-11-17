package net.sourceforge.plantuml.chart.command;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class ChartDataParserTest {

	@Test
	void emptyValuesRemainAnEmptyList() {
		assertTrue(ChartDataParser.parseValues(null).isEmpty());
		assertTrue(ChartDataParser.parseValues("  ").isEmpty());
	}

	@Test
	void parsesNumbersAndPreservesTrailingCommaBehavior() {
		assertEquals(Arrays.asList(1.0, -2.5, 300.0), ChartDataParser.parseValues(" 1, -2.5, 3e2,"));
	}

	@Test
	void invalidNumberReturnsNull() {
		assertNull(ChartDataParser.parseValues("1,wrong,3"));
		assertNull(ChartDataParser.parseValues("1,,3"));
	}

	@Test
	void coordinatePairsAppendToExistingLists() {
		final List<Double> x = new ArrayList<>(Arrays.asList(9.0));
		final List<Double> y = new ArrayList<>(Arrays.asList(8.0));
		assertTrue(ChartDataParser.parseCoordinatePairs("[(1, 2), (-3.5, 4e1)]", x, y));
		assertEquals(Arrays.asList(9.0, 1.0, -3.5), x);
		assertEquals(Arrays.asList(8.0, 2.0, 40.0), y);
	}

	@Test
	void invalidPairPreservesPreviouslyAppendedCoordinates() {
		final List<Double> x = new ArrayList<>();
		final List<Double> y = new ArrayList<>();
		assertFalse(ChartDataParser.parseCoordinatePairs("(1,2),(3,invalid)", x, y));
		assertEquals(Arrays.asList(1.0), x);
		assertEquals(Arrays.asList(2.0), y);
	}

	@Test
	void rejectsEmptyAndMalformedPairsWithoutAddingValues() {
		for (String input : new String[] { null, " ", "(1)", "(1,2,3)", "(x,2)" }) {
			final List<Double> x = new ArrayList<>();
			final List<Double> y = new ArrayList<>();
			assertFalse(ChartDataParser.parseCoordinatePairs(input, x, y));
			assertTrue(x.isEmpty());
			assertTrue(y.isEmpty());
		}
	}
}
