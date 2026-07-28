package net.sourceforge.plantuml.gantt.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FractionTest {

	@Test
	void wholeNumber() {
		Fraction f = Fraction.of(5);
		
		assertEquals(5, f.getNumerator());
		assertEquals(1, f.getDenominator());
	}
	
	@Test
	void denominatorShouldNotBeZero() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> {
			new Fraction(7, 0);
		});
	}
	
	@Test
	void normalizingNegativeDenominator() throws Exception {
		Fraction f = new Fraction(3, -4);
		
		assertEquals(-3, f.getNumerator());
		assertEquals(4, f.getDenominator());
	}
	
	@Test
	void reducingFraction() throws Exception {
		Fraction f = new Fraction(8, 12);
		
		assertEquals(2, f.getNumerator());
		assertEquals(3, f.getDenominator());
	}
	
	@Test
	void addingFractions() throws Exception {
		Fraction f1 = new Fraction(1, 3);
		Fraction f2 = new Fraction(1, 6);
		
		Fraction result = f1.add(f2);
		
		assertEquals(1, result.getNumerator());
		assertEquals(2, result.getDenominator());
	}
	
	@Test
	void subtractingFractions() throws Exception {
		Fraction f1 = new Fraction(1, 2);
		Fraction f2 = new Fraction(3, 4);
		
		Fraction result = f1.subtract(f2);
		
		assertEquals(-1, result.getNumerator());
		assertEquals(4, result.getDenominator());
	}
	
	@Test
	void multiplyingFractions() throws Exception {
		Fraction f1 = new Fraction(2, 3);
		Fraction f2 = new Fraction(-3, 4);
		
		Fraction result = f1.multiply(f2);
		
		assertEquals(-1, result.getNumerator());
		assertEquals(2, result.getDenominator());
	}
	
	@Test
	void reciprocalFraction() throws Exception {
		Fraction f = new Fraction(-3, 5);
		
		assertEquals(new Fraction(-5, 3), f.reciprocal());
	}
	
	@Test
	void divideFractions() throws Exception {
		Fraction f1 = new Fraction(2, 3);  
		Fraction f2 = new Fraction(4, 5);
		
		Fraction result = f1.divide(f2); // (2 / 3) / (4 / 5) = 10 / 12 = 5 / 6 
		
		assertEquals(new Fraction(5, 6), result);
	}
	
	@Test
	void negateFraction() throws Exception {
		Fraction f = new Fraction(3, 7);
		
		assertEquals(new Fraction(-3, 7), f.negate());
	}
	
	@Test
	void wholePart() throws Exception {
		assertEquals(3, Fraction.of(3).wholePart());
		assertEquals(2, new Fraction(7, 3).wholePart());
		assertEquals(-2, new Fraction(-7, 3).wholePart());
	}
	
	@Test
	void toStringRepresentation() throws Exception {
		Fraction f = new Fraction(3, 4);
		
		assertEquals("3/4", f.toString());
	}
	
	@Test
	void toStringRepresentationOfWholeNumbers() throws Exception {
		Fraction f = Fraction.of(5);
		
		assertEquals("5", f.toString());
	}
	
	@Test 
	void zeroNumerator() throws Exception {
		Fraction f = new Fraction(0, 5);
		
		assertEquals(0, f.getNumerator());
	}
	
	@Test
	void toStringRepresentationOfZero() throws Exception {
		Fraction f = new Fraction(0, 3);
		
		assertEquals("0", f.toString());
	}
	
	@Test
	void equalsAndHashCode() throws Exception {
		Fraction f1 = new Fraction(2, 4);
		Fraction f2 = new Fraction(1, 2);
		
		assertEquals(f2, f1);
		assertEquals(f2.hashCode(), f1.hashCode());
	}
	
	@Test
	void notEquals() throws Exception {
		Fraction f1 = new Fraction(1, 3);
		Fraction f2 = new Fraction(2, 3);
		
		assertNotEquals(f2, f1);
	}
	
	@Test
	void compareTo() throws Exception {
		Fraction f1 = new Fraction(1, 2);
		Fraction f2 = new Fraction(2, 3);
		Fraction f3 = new Fraction(1, 2);
		
		assertTrue(f1.compareTo(f2) < 0);
		assertTrue(f2.compareTo(f1) > 0);
		assertEquals(0, f1.compareTo(f3));
	}

}
