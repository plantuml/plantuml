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
 */
package net.sourceforge.plantuml.timingdiagram;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

final class DeduceFormat {

	private final boolean forceSign;
	private final char groupingSep;
	private final char decimalSep;
	private final int minFractionDigits;
	private final int maxFractionDigits;

	private DeduceFormat(boolean forceSign, char groupingSep, char decimalSep, int minFractionDigits,
			int maxFractionDigits) {
		this.forceSign = forceSign;
		this.groupingSep = groupingSep;
		this.decimalSep = decimalSep;
		this.minFractionDigits = minFractionDigits;
		this.maxFractionDigits = maxFractionDigits;
	}

	public DeduceFormat mergeWith(DeduceFormat other) {
		if (other == null)
			return this;

		final boolean mergedForceSign = this.forceSign || other.forceSign;

		final char mergedDecimal = this.decimalSep != '\0' ? this.decimalSep : other.decimalSep;

		char mergedGrouping;
		if (this.groupingSep == '\0')
			mergedGrouping = other.groupingSep;
		else if (other.groupingSep == '\0')
			mergedGrouping = this.groupingSep;
		else if (this.groupingSep == other.groupingSep)
			mergedGrouping = this.groupingSep;
		else
			mergedGrouping = '\0';

		final int mergedMinFrac = Math.min(this.minFractionDigits, other.minFractionDigits);
		final int mergedMaxFrac = Math.max(this.maxFractionDigits, other.maxFractionDigits);

		if (mergedGrouping != '\0' && mergedDecimal != '\0' && mergedGrouping == mergedDecimal)
			mergedGrouping = '\0';

		return new DeduceFormat(mergedForceSign, mergedGrouping, mergedDecimal, mergedMinFrac, mergedMaxFrac);
	}

	public DecimalFormat getDecimalFormat() {
		final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);

		if (decimalSep != '\0')
			symbols.setDecimalSeparator(decimalSep);

		if (groupingSep != '\0')
			symbols.setGroupingSeparator(groupingSep);

		final StringBuilder pattern = new StringBuilder();

		if (forceSign)
			pattern.append("+");

		if (groupingSep != '\0')
			pattern.append("#,##0");
		else
			pattern.append("0");

		if (minFractionDigits > 0) {
			pattern.append(".");
			for (int i = 0; i < minFractionDigits; i++)
				pattern.append("0");

		}

		final DecimalFormat df = new DecimalFormat(pattern.toString(), symbols);
		df.setGroupingUsed(groupingSep != '\0');
		df.setMinimumFractionDigits(minFractionDigits);
		df.setMaximumFractionDigits(maxFractionDigits);

		return df;
	}

	static class CharCount {
		char ch;
		int count;

		public void init(char newOne) {
			ch = newOne;
			count = 1;
		}
	}

	public static DeduceFormat from(String number) {

		final CharCount nonDigit1 = new CharCount();
		final CharCount nonDigit2 = new CharCount();

		char sign = '\0';
		if (number.charAt(0) == '+' || number.charAt(0) == '-') {
			sign = number.charAt(0);
			number = number.substring(1);
		}

		int fractionDigits = 0;

		for (int i = 0; i < number.length(); i++) {
			final char ch = number.charAt(i);
			if (Character.isDigit(ch)) {
				if (nonDigit1.count > 0)
					fractionDigits++;
				continue;
			}

			if (nonDigit1.count == 0) {
				nonDigit1.init(ch);
				fractionDigits = 0;
			} else if (nonDigit1.ch == ch) {
				nonDigit1.count++;
			} else if (nonDigit2.count == 0) {
				nonDigit2.init(ch);
				fractionDigits = 0;
			} else if (nonDigit2.ch == ch) {
				nonDigit2.count++;
			} else
				return null;
		}

		if (nonDigit1.count > 0 && nonDigit2.count > 0)
			return new DeduceFormat(sign == '+', nonDigit1.ch, nonDigit2.ch, fractionDigits, fractionDigits);

		return new DeduceFormat(sign == '+', '\0', nonDigit1.ch, fractionDigits, fractionDigits);

	}

	@Override
	public String toString() {
		if (minFractionDigits == maxFractionDigits)
			return "DeduceFormat{" + "forceSign=" + forceSign + ", groupingSep="
					+ (groupingSep == '\0' ? "none" : "'" + groupingSep + "'") + ", decimalSep="
					+ (decimalSep == '\0' ? "none" : "'" + decimalSep + "'") + ", fractionDigits=" + minFractionDigits
					+ '}';

		return "DeduceFormat{" + "forceSign=" + forceSign + ", groupingSep="
				+ (groupingSep == '\0' ? "none" : "'" + groupingSep + "'") + ", decimalSep="
				+ (decimalSep == '\0' ? "none" : "'" + decimalSep + "'") + ", minFractionDigits=" + minFractionDigits
				+ ", maxFractionDigits=" + maxFractionDigits + '}';
	}

}
