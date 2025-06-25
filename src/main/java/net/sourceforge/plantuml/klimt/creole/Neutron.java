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

	private static boolean isChinese(char ch) {
	    // CJK Unified Ideographs
	    if (ch >= 0x4E00 && ch <= 0x9FFF)
	        return true;
//	    // CJK Unified Ideographs Extension A
//	    if (ch >= 0x3400 && ch <= 0x4DBF)
//	        return true;
//	    // CJK Unified Ideographs Extension B–F
//	    if (ch >= 0x20000 && ch <= 0x2EBEF)
//	        return true;
//	    // CJK Compatibility Ideographs
//	    if (ch >= 0xF900 && ch <= 0xFAFF)
//	        return true;
//	    // CJK Symbols and Punctuation
//	    if (ch >= 0x3000 && ch <= 0x303F)
//	        return true;
	    return false;
	}


//	private static boolean isSentenceBoundaryUnused(char ch) {
//		return ch == '.' || ch == ',';
//
//	}
//
//	private static boolean isChineseSentenceBoundary(char ch) {
//		return ch == '\uFF01' // U+FF01 FULLWIDTH EXCLAMATION MARK (!)
////				|| ch == '\uFF08' // U+FF08 FULLWIDTH LEFT PARENTHESIS
////				|| ch == '\uFF09' // U+FF09 FULLWIDTH RIGHT PARENTHESIS
//				|| ch == '\uFF0C' // U+FF0C FULLWIDTH COMMA
//				|| ch == '\uFF1A' // U+FF1A FULLWIDTH COLON (:)
//				|| ch == '\uFF1B' // U+FF1B FULLWIDTH SEMICOLON (;)
//				|| ch == '\uFF1F' // U+FF1F FULLWIDTH QUESTION MARK (?)
//				|| ch == '\u3002'; // U+3002 IDEOGRAPHIC FULL STOP (.)
//	}

	public double getWidth(StringBounder stringBounder) {
		if (type == NeutronType.ZWSP_SEPARATOR)
			return 0;

		return asAtom.calculateDimension(stringBounder).getWidth();
	}

	public static NeutronType getNeutronTypeFromChar(char ch) {
		if (Character.isWhitespace(ch))
			return NeutronType.WHITESPACE;
		if (isChinese(ch))
			return NeutronType.CJK_IDEOGRAPH;
		return NeutronType.UNBREAKABLE;
	}

}
