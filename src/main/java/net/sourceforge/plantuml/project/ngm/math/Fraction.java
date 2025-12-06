/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.project.ngm.math;

/**
 * Immutable rational number represented as a reduced numerator/denominator
 * pair.
 *
 * This implementation uses long integers for both numerator and denominator.
 * All fractions are always kept in reduced (canonical) form: - gcd(|num|,
 * |den|) == 1 - denominator is always positive
 *
 * The class provides the basic arithmetic operations (add, subtract, multiply,
 * divide), as well as negation, reciprocal, comparison, and conversion to
 * double.
 *
 * Note: This class does not protect against overflow in intermediate
 * multiplications. For safe arbitrary precision, BigInteger would be required.
 */
public final class Fraction implements Comparable<Fraction> {

	private final long num; // numerator
	private final long den; // denominator (always > 0)

	/**
	 * Creates a new fraction with the given numerator and denominator. The fraction
	 * is immediately reduced and normalized.
	 */
	public Fraction(long numerator, long denominator) {
		if (denominator == 0) {
			throw new IllegalArgumentException("Denominator cannot be zero.");
		}

		// Normalize sign: denominator positive
		if (denominator < 0) {
			numerator = -numerator;
			denominator = -denominator;
		}

		// Reduce to simplest form
		long g = gcd(Math.abs(numerator), denominator);
		this.num = numerator / g;
		this.den = denominator / g;
	}

	/**
	 * Creates a fraction representing an integer.
	 */
	public static Fraction of(long value) {
		return new Fraction(value, 1);
	}

	/** Returns the numerator. */
	public long getNumerator() {
		return num;
	}

	/** Returns the denominator. */
	public long getDenominator() {
		return den;
	}

	/** Adds another fraction to this one. */
	public Fraction add(Fraction other) {
		// a/b + c/d = (ad + bc) / bd
		long n = this.num * other.den + other.num * this.den;
		long d = this.den * other.den;
		return new Fraction(n, d);
	}

	/** Subtracts another fraction from this one. */
	public Fraction subtract(Fraction other) {
		// a/b - c/d = (ad - bc) / bd
		long n = this.num * other.den - other.num * this.den;
		long d = this.den * other.den;
		return new Fraction(n, d);
	}

	/** Multiplies this fraction by another fraction. */
	public Fraction multiply(Fraction other) {
		// (a/b) * (c/d) = (ac) / (bd)
		long n = this.num * other.num;
		long d = this.den * other.den;
		return new Fraction(n, d);
	}

	/** Returns a human-readable representation such as "3/4" or "5". */
	@Override
	public String toString() {
		if (den == 1) {
			return Long.toString(num);
		}
		return num + "/" + den;
	}

	/** Compares two fractions using long cross-multiplication. */
	@Override
	public int compareTo(Fraction other) {
		// Compare a/b and c/d by cross-products: ad ? bc
		long lhs = this.num * other.den;
		long rhs = other.num * this.den;
		return Long.compare(lhs, rhs);
	}

	/** Standard equality: same numerator and denominator after reduction. */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Fraction))
			return false;
		Fraction other = (Fraction) obj;
		return this.num == other.num && this.den == other.den;
	}

	@Override
	public int hashCode() {
		// Simple and stable hash since the class is immutable
		int result = Long.hashCode(num);
		result = 31 * result + Long.hashCode(den);
		return result;
	}

	/**
	 * Computes the greatest common divisor using the Euclidean algorithm.
	 */
	private static long gcd(long a, long b) {
		while (b != 0) {
			long t = b;
			b = a % b;
			a = t;
		}
		return a;
	}
}
