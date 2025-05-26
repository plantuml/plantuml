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
package net.sourceforge.plantuml.openiconic;

import java.util.ArrayList;
import java.util.List;

import com.plantuml.ubrex.TextNavigator;

public class StringDecipher {

	public static List<CharSequence> decipher(String path) {
		final TextNavigator input = TextNavigator.build(path);
		final List<CharSequence> numbers = new ArrayList<>();

		while (input.length() > 0) {
			// Step 1: Skip any separator characters (whitespace or commas)
			while (input.length() > 0 && (Character.isWhitespace(input.charAt(0)) || input.charAt(0) == ','))
				input.jump(1);

			if (input.length() == 0)
				break;

			// Step 2: If the next character is a command letter (e.g. M, L, C...), isolate
			// it
			if (Character.isLetter(input.charAt(0))) {
				numbers.add(input.subSequence(0, 1));
				input.jump(1);
				continue;
			}

			int i = 0;

			// Step 3: Handle optional sign for the mantissa
			char c = input.charAt(i);
			if (c == '+' || c == '-')
				i++;

			// Step 4: Read mantissa (digits with optional dot, only one allowed)
			boolean seenDot = false;
			boolean seenMantissaDigit = false;
			while (i < input.length()) {
				c = input.charAt(i);
				if (Character.isDigit(c)) {
					seenMantissaDigit = true;
					i++;
				} else if (c == '.' && !seenDot) {
					seenDot = true;
					i++;
				} else
					break;

			}

			// If no digit has been read, it's not a valid number
			if (!seenMantissaDigit)
				break;

			// Step 5: Optionally parse scientific notation (e.g. 1.2e-3)
			if (i < input.length() && (input.charAt(i) == 'e' || input.charAt(i) == 'E')) {
				final int expStart = i;
				i++; // consume 'e' or 'E'

				// Optional sign in exponent
				if (i < input.length() && (input.charAt(i) == '+' || input.charAt(i) == '-'))
					i++;

				boolean seenExpDigit = false;
				while (i < input.length() && Character.isDigit(input.charAt(i))) {
					seenExpDigit = true;
					i++;
				}

				// If exponent is not valid (no digit), backtrack
				if (!seenExpDigit)
					i = expStart;
			}

			// Step 6: Store the parsed numeric token
			numbers.add(input.subSequence(0, i));

			// Step 7: Move forward in the input
			input.jump(i);
		}

		return numbers;
	}

}
