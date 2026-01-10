package net.sourceforge.plantuml.project.ngm.math;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FractionTest {

	@Test
	void wholeNumber() {
		Fraction f = Fraction.of(5);
		
		assertThat(f.getNumerator()).isEqualTo(5);
		assertThat(f.getDenominator()).isEqualTo(1);
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
		
		assertThat(f.getNumerator()).isEqualTo(-3);
		assertThat(f.getDenominator()).isEqualTo(4);
	}
	
	@Test
	void reducingFraction() throws Exception {
		Fraction f = new Fraction(8, 12);
		
		assertThat(f.getNumerator()).isEqualTo(2);
		assertThat(f.getDenominator()).isEqualTo(3);
	}
	
	@Test
	void addingFractions() throws Exception {
		Fraction f1 = new Fraction(1, 3);
		Fraction f2 = new Fraction(1, 6);
		
		Fraction result = f1.add(f2);
		
		assertThat(result.getNumerator()).isEqualTo(1);
		assertThat(result.getDenominator()).isEqualTo(2);
	}
	
	@Test
	void subtractingFractions() throws Exception {
		Fraction f1 = new Fraction(1, 2);
		Fraction f2 = new Fraction(3, 4);
		
		Fraction result = f1.subtract(f2);
		
		assertThat(result.getNumerator()).isEqualTo(-1);
		assertThat(result.getDenominator()).isEqualTo(4);
	}
	
	@Test
	void multiplyingFractions() throws Exception {
		Fraction f1 = new Fraction(2, 3);
		Fraction f2 = new Fraction(-3, 4);
		
		Fraction result = f1.multiply(f2);
		
		assertThat(result.getNumerator()).isEqualTo(-1);
		assertThat(result.getDenominator()).isEqualTo(2);
	}
	
	@Test
	void reciprocalFraction() throws Exception {
		Fraction f = new Fraction(-3, 5);
		
		assertThat(f.reciprocal()).isEqualTo(new Fraction(-5, 3));
	}
	
	@Test
	void divideFractions() throws Exception {
		Fraction f1 = new Fraction(2, 3);  
		Fraction f2 = new Fraction(4, 5);
		
		Fraction result = f1.divide(f2); // (2 / 3) / (4 / 5) = 10 / 12 = 5 / 6 
		
		assertThat(result).isEqualTo(new Fraction(5, 6));
	}
	
	@Test
	void negateFraction() throws Exception {
		Fraction f = new Fraction(3, 7);
		
		assertThat(f.negate()).isEqualTo(new Fraction(-3, 7));
	}
	
	@Test
	void wholePart() throws Exception {
		assertThat(Fraction.of(3).wholePart()).isEqualTo(3);
		assertThat(new Fraction(7, 3).wholePart()).isEqualTo(2);
		assertThat(new Fraction(-7, 3).wholePart()).isEqualTo(-2);
	}
	
	@Test
	void toStringRepresentation() throws Exception {
		Fraction f = new Fraction(3, 4);
		
		assertThat(f.toString()).isEqualTo("3/4");
	}
	
	@Test
	void toStringRepresentationOfWholeNumbers() throws Exception {
		Fraction f = Fraction.of(5);
		
		assertThat(f.toString()).isEqualTo("5");
	}
	
	@Test 
	void zeroNumerator() throws Exception {
		Fraction f = new Fraction(0, 5);
		
		assertThat(f.getNumerator()).isEqualTo(0);
	}
	
	@Test
	void toStringRepresentationOfZero() throws Exception {
		Fraction f = new Fraction(0, 3);
		
		assertThat(f.toString()).isEqualTo("0");
	}
	
	@Test
	void equalsAndHashCode() throws Exception {
		Fraction f1 = new Fraction(2, 4);
		Fraction f2 = new Fraction(1, 2);
		
		assertThat(f1).isEqualTo(f2);
		assertThat(f1.hashCode()).isEqualTo(f2.hashCode());
	}
	
	@Test
	void notEquals() throws Exception {
		Fraction f1 = new Fraction(1, 3);
		Fraction f2 = new Fraction(2, 3);
		
		assertThat(f1).isNotEqualTo(f2);
	}
	
	@Test
	void compareTo() throws Exception {
		Fraction f1 = new Fraction(1, 2);
		Fraction f2 = new Fraction(2, 3);
		Fraction f3 = new Fraction(1, 2);
		
		assertThat(f1.compareTo(f2)).isLessThan(0);
		assertThat(f2.compareTo(f1)).isGreaterThan(0);
		assertThat(f1.compareTo(f3)).isEqualTo(0);
	}

}
