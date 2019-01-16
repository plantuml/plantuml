/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import net.sourceforge.plantuml.StringUtils;

public class SvgCommandLetter implements SvgCommand {

	final private char letter;

	public SvgCommandLetter(String letter) {
		if (letter.matches("[a-zA-Z]") == false) {
			throw new IllegalArgumentException();
		}
		this.letter = letter.charAt(0);
	}

	@Override
	public String toString() {
		return super.toString() + " " + letter;
	}

	public String toSvg() {
		return "" + letter;
	}

	public int argumentNumber() {
		switch (StringUtils.goLowerCase(letter)) {
		case 'm':
		case 'M':
		case 'l':
			return 2;
		case 'z':
			return 0;
		case 'c':
			return 6;
		case 's':
			return 4;
		case 'a':
			return 7;
		}
		throw new UnsupportedOperationException("" + letter);
	}

//	public UGraphic drawMe(UGraphic ug, Iterator<SvgCommand> it) {
//		System.err.println("drawMe " + letter);
//		final List<SvgCommandNumber> numbers = new ArrayList<SvgCommandNumber>();
//		for (int i = 0; i < argumentNumber(); i++) {
//			numbers.add((SvgCommandNumber) it.next());
//		}
//		return drawMe(ug, numbers);
//	}
//
//	private UGraphic drawMe(UGraphic ug, List<SvgCommandNumber> numbers) {
//		switch (letter) {
//		case 'M':
//			final double x = numbers.get(0).getDouble();
//			final double y = numbers.get(1).getDouble();
//			return ug.apply(new UTranslate(x, y));
//		}
//		return ug;
//
//	}

	public boolean isUpperCase() {
		return Character.isUpperCase(letter);
	}

	public boolean is(char c) {
		return this.letter == c;
	}

	public char getLetter() {
		return letter;
	}
}
