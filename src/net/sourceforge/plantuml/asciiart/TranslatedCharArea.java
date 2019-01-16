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
import java.util.Collection;
import java.util.List;

public class TranslatedCharArea implements UmlCharArea {

	private final int dx;
	private final int dy;
	private final UmlCharArea charArea;

	public TranslatedCharArea(UmlCharArea charArea, int dx, int dy) {
		this.charArea = charArea;
		this.dx = dx;
		this.dy = dy;
	}

	public void drawBoxSimple(int x, int y, int width, int height) {
		charArea.drawBoxSimple(x + dx, y + dy, width, height);

	}

	public void drawBoxSimpleUnicode(int x, int y, int width, int height) {
		charArea.drawBoxSimpleUnicode(x + dx, y + dy, width, height);
	}
	
	public void drawNoteSimple(int x, int y, int width, int height) {
		charArea.drawNoteSimple(x + dx, y + dy, width, height);
	}

	public void drawNoteSimpleUnicode(int x, int y, int width, int height) {
		charArea.drawNoteSimpleUnicode(x + dx, y + dy, width, height);
	}


	public void drawShape(AsciiShape shape, int x, int y) {
		charArea.drawShape(shape, x + dx, y + dy);
	}
	
	public void drawChar(char c, int x, int y) {
		charArea.drawChar(c, x + dx, y + dy);
	}

	public void drawHLine(char c, int line, int col1, int col2) {
		charArea.drawHLine(c, line + dy, col1 + dx, col2 + dx);
	}
	public void drawHLine(char c, int line, int col1, int col2, char ifFound, char thenUse) {
		charArea.drawHLine(c, line + dy, col1 + dx, col2 + dx, ifFound, thenUse);
	}

	public void drawStringLR(String string, int x, int y) {
		charArea.drawStringLR(string, x + dx, y + dy);
	}

	public void drawStringTB(String string, int x, int y) {
		charArea.drawStringTB(string, x + dx, y + dy);
	}

	public void drawVLine(char c, int col, int line1, int line2) {
		charArea.drawVLine(c, col + dx, line1 + dy, line2 + dy);
	}

	public int getHeight() {
		return charArea.getHeight();
	}

	public int getWidth() {
		return charArea.getWidth();
	}

	public String getLine(int line) {
		return charArea.getLine(line);
	}

	public List<String> getLines() {
		return charArea.getLines();
	}

	public void print(PrintStream ps) {
		charArea.print(ps);
	}

	public void drawStringsLR(Collection<? extends CharSequence> strings, int x, int y) {
		charArea.drawStringsLR(strings, x + dx, y + dy);
	}

	public void fillRect(char c, int x, int y, int width, int height) {
		charArea.fillRect(c, x + dx, y + dy, width, height);
	}



}
