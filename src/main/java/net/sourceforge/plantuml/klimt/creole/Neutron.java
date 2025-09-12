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
package net.sourceforge.plantuml.klimt.creole;

import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.creole.legacy.AtomText;
import net.sourceforge.plantuml.klimt.font.StringBounder;

public class Neutron {

	private static final Neutron ZWSP = new Neutron(null, NeutronType.ZWSP_SEPARATOR, null);

	private final String data;
	private final NeutronType type;
	private final Atom asAtom;

	private Neutron(String data, NeutronType type, Atom asAtom) {
		this.data = data;
		this.type = type;
		this.asAtom = asAtom;
	}

	public static Neutron create(Atom atom) {
		if (atom instanceof AtomText) {
			final String text = ((AtomText) atom).getText();
			return new Neutron(text, getNeutronTypeFromChar(text.charAt(0)), atom);
		}
		return new Neutron(null, NeutronType.UNKNOWN, atom);
	}

	public static Neutron zwspSeparator() {
		return ZWSP;
	}

	@Override
	public String toString() {
		if (type == NeutronType.ZWSP_SEPARATOR)
			return "ZWSP";
		return type + "(" + data + ")";
	}

	public Atom asAtom() {
		return asAtom;
	}

	public NeutronType getType() {
		return type;
	}

	/**
	 * Returns true if the given character belongs to a script commonly used in
	 * Chinese or Japanese writing systems, such as Kanji, Hiragana, Katakana, or
	 * Japanese punctuation. This is used to determine valid line-break positions.
	 */
	private static boolean isCjkOrJapanese(char ch) {
		// CJK Unified Ideographs: Common Han/Kanji characters
		if (ch >= 0x4E00 && ch <= 0x9FFF)
			return true;

		// Hiragana: Japanese phonetic characters
		if (ch >= 0x3040 && ch <= 0x309F)
			return true;

		// Katakana: Japanese phonetic characters (often for foreign words)
		if (ch >= 0x30A0 && ch <= 0x30FF)
			return true;

		// CJK Symbols and Punctuation: Includes Japanese full-width punctuation
		if (ch >= 0x3000 && ch <= 0x303F)
			return true;

		// Halfwidth Katakana (commonly used in Japanese, lives in Halfwidth/Fullwidth
		// Forms)
		if (ch >= 0xFF65 && ch <= 0xFF9F)
			return true;

		return false;
	}

	public double getWidth(StringBounder stringBounder) {
		if (type == NeutronType.ZWSP_SEPARATOR)
			return 0;

		return asAtom.calculateDimension(stringBounder).getWidth();
	}

	public static NeutronType getNeutronTypeFromChar(char ch) {
		if (Character.isWhitespace(ch))
			return NeutronType.WHITESPACE;
		if (isCjkOrJapanese(ch))
			return NeutronType.CJK_IDEOGRAPH;
		return NeutronType.UNBREAKABLE;
	}

}
