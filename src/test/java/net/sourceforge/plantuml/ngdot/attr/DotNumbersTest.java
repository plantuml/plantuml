package net.sourceforge.plantuml.ngdot.attr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DotNumbersTest {

	@Test
	void parseFirst() {
		assertEquals(1.2, DotNumbers.parseFirst("1.2 equally"), 1e-9);
		assertEquals(-3, DotNumbers.parseFirst(" -3"), 1e-9);
		assertEquals(0.5, DotNumbers.parseFirst(".5"), 1e-9);
		assertEquals(2, DotNumbers.parseFirst("2,3"), 1e-9);
		assertNull(DotNumbers.parseFirst("abc"));
		assertNull(DotNumbers.parseFirst("-"));
		assertNull(DotNumbers.parseFirst(null));
	}

	@Test
	void booleans() {
		assertFalse(DotNumbers.parseBoolean("false", true));
		assertFalse(DotNumbers.parseBoolean("No", true));
		assertFalse(DotNumbers.parseBoolean("0", true));
		assertTrue(DotNumbers.parseBoolean("TRUE", false));
		assertTrue(DotNumbers.parseBoolean("yes", false));
		assertTrue(DotNumbers.parseBoolean("2", false));
		assertTrue(DotNumbers.parseBoolean("maybe", true));
		assertTrue(DotNumbers.isFalse("false"));
		assertFalse(DotNumbers.isFalse("whatever"));
	}

}
