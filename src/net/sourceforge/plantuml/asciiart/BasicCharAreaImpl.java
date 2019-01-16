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
package net.sourceforge.plantuml.asciiart;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicCharAreaImpl implements BasicCharArea {

	private int charSize1 = 160;
	private int charSize2 = 160;

	private int width;
	private int height;

	private char chars[][];

	public BasicCharAreaImpl() {
		this.chars = new char[charSize1][charSize2];
		for (int i = 0; i < charSize1; i++) {
			for (int j = 0; j < charSize2; j++) {
				chars[i][j] = ' ';
			}
		}
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public void drawChar(char c, int x, int y) {
		ensurePossible(x, y);
		chars[x][y] = c;
		if (x >= width) {
			width = x + 1;
		}
		if (y >= height) {
			height = y + 1;
		}
	}

	private boolean isLong(char c) {
		final int wc = Wcwidth.of(c);
		if (wc == 1) {
			return false;
		}
		if (wc == 2) {
			return true;
		}
		return false;
//		throw new IllegalArgumentException("warning width=" + wc + " char=" + ((int) c));
	}

	private void ensurePossible(int x, int y) {
		int newCharSize1 = charSize1;
		int newCharSize2 = charSize2;
		while (x >= newCharSize1) {
			newCharSize1 *= 2;
		}
		while (y >= newCharSize2) {
			newCharSize2 *= 2;
		}
		if (newCharSize1 != charSize1 || newCharSize2 != charSize2) {
			final char newChars[][] = new char[newCharSize1][newCharSize2];
			for (int i = 0; i < newCharSize1; i++) {
				for (int j = 0; j < newCharSize2; j++) {
					char c = ' ';
					if (i < charSize1 && j < charSize2) {
						c = chars[i][j];
					}
					newChars[i][j] = c;
				}
			}
			this.chars = newChars;
			this.charSize1 = newCharSize1;
			this.charSize2 = newCharSize2;
		}

	}

	public void drawStringLR(String string, int x, int y) {
		int pos = x;
		for (int i = 0; i < string.length(); i++) {
			final char c = string.charAt(i);
			drawChar(c, pos, y);
			pos++;
			if (isLong(c)) {
				drawChar('\0', pos, y);
				pos++;
			}
		}
	}

	public void drawStringTB(String string, int x, int y) {
		for (int i = 0; i < string.length(); i++) {
			drawChar(string.charAt(i), x, y + i);
		}
	}

	public String getLine(int line) {
		final StringBuilder sb = new StringBuilder(charSize1);
		for (int x = 0; x < width; x++) {
			final char c = chars[x][line];
			if (c != '\0') {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public void print(PrintStream ps) {
		for (String s : getLines()) {
			ps.println(s);
		}
	}

	public List<String> getLines() {
		final List<String> result = new ArrayList<String>(height);
		for (int y = 0; y < height; y++) {
			result.add(getLine(y));
		}
		return Collections.unmodifiableList(result);
	}

	public void drawHLine(char c, int line, int col1, int col2) {
		for (int x = col1; x < col2; x++) {
			this.drawChar(c, x, line);
		}
	}

	public void drawHLine(char c, int line, int col1, int col2, char ifFound, char thenUse) {
		for (int x = col1; x < col2; x++) {
			ensurePossible(x, line);
			if (this.chars[x][line] == ifFound) {
				this.drawChar(thenUse, x, line);
			} else {
				this.drawChar(c, x, line);
			}
		}
	}

	public void drawVLine(char c, int col, int line1, int line2) {
		for (int y = line1; y < line2; y++) {
			this.drawChar(c, col, y);
		}
	}

	@Override
	public String toString() {
		return getLines().toString();
	}

	public void fillRect(char c, int x, int y, int width, int height) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				drawChar(c, x + i, y + j);
			}
		}
	}

}
