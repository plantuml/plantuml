/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml;

public class QString {

	private final String data;
	private final long mask;

	public QString(String data) {
		this.data = data;
		this.mask = getMask(data);
	}
	
	@Override
	public String toString() {
		return data;
	}

	public boolean containsQ(QString other) {
		if ((this.mask & other.mask) != other.mask) {
			return false;
		}
		return this.data.contains(other.data);
	}

	static long getMask(String s) {
		long result = 0;
		for (int i = 0; i < s.length(); i++) {
			result |= getMask(s.charAt(i));
		}
		return result;
	}

	static long getMask(char c) {
		if (c >= '0' && c <= '9') {
			final int n = c - '0';
			return 1L << n;
		}
		if (c >= 'a' && c <= 'z') {
			final int n = c - 'a' + 10;
			return 1L << n;
		}
		if (c >= 'A' && c <= 'Z') {
			final int n = c - 'A' + 10 + 26;
			return 1L << n;
		}
		if (c == '_') {
			return 1L << (10 + 26 + 26);
		}
		if (c == '(') {
			return 1L << 63;
		}
		return 0;
	}

}
