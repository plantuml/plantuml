/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4041 $
 *
 */
package net.sourceforge.plantuml.dedication;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.SignatureUtils;

public class Dedications {

	private static final Map<String, Dedication> all = new HashMap<String, Dedication>();

	static {
		addNormal("Write your own dedication!");
		addCrypted("RyHcSMMTGTW-ZlDelq18AwlwfbZZdfo-Yo0ketavjyFxRAFoKx1mAI032reWO3p4Mog-AV6jFqjXfi8G6pKo7G00");
	}

	private static void addNormal(String sentence) {
		final String signature = SignatureUtils.getSignatureSha512(keepLetter(sentence));
		addCrypted(signature);
	}

	private static void addCrypted(String signature) {
		all.put(signature, new Dedication(signature));
	}

	private Dedications() {
	}

	public static Dedication get(String line) {
		final String signature = SignatureUtils.getSignatureSha512(keepLetter(line));
		return all.get(signature);
	}

	public static String keepLetter(String s) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (Character.isLetter(c)) {
				sb.append(c);
			}
		}
		return sb.toString().toUpperCase();
	}

}
