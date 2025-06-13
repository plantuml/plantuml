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
package net.sourceforge.plantuml;

public final class StringBuilder2 {
	private final char[] buffer;
	private int length = 0;

	public StringBuilder2(int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("Capacity must be non-negative");

		this.buffer = new char[capacity];
	}

	public int capacity() {
		return buffer.length;
	}

	public int length() {
		return length;
	}

	public StringBuilder2 append(char c) {
		if (length == buffer.length)
			throw new IllegalStateException("Capacity exceeded");

		buffer[length++] = c;
		return this;
	}

	public StringBuilder2 append(CharSequence csq) {
		if (csq == null)
			throw new IllegalArgumentException();

		return append(csq, 0, csq.length());
	}

	public StringBuilder2 append(CharSequence csq, int start, int end) {
		if (csq == null)
			throw new IllegalArgumentException();
		final int len = end - start;
		if (length + len > buffer.length)
			throw new IllegalStateException("Capacity exceeded");
		for (int i = start; i < end; i++)
			buffer[length++] = csq.charAt(i);
		return this;
	}

	public StringBuilder2 append(String str) {
		if (str == null)
			throw new IllegalArgumentException();
		final int len = str.length();
		if (length + len > buffer.length)
			throw new IllegalStateException("Capacity exceeded");
		
		str.getChars(0, len, buffer, length);
		length += len;
		return this;
	}

	public StringBuilder2 append(int value) {
		return append(Integer.toString(value));
	}

	public StringBuilder2 clear() {
		length = 0;
		return this;
	}

	public char charAt(int index) {
		if (index < 0 || index >= length)
			throw new IndexOutOfBoundsException("Index: " + index);
		
		return buffer[index];
	}

	@Override
	public String toString() {
		return new String(buffer, 0, length);
	}
}
