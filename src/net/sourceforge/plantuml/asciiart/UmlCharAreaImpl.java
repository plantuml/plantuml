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

import java.util.Collection;

public class UmlCharAreaImpl extends BasicCharAreaImpl implements UmlCharArea {

	public void drawBoxSimple(int x, int y, int width, int height) {
		this.drawHLine('-', y, x + 1, x + width - 1);
		this.drawHLine('-', y + height - 1, x + 1, x + width - 1);

		this.drawVLine('|', x, y + 1, y + height - 1);
		this.drawVLine('|', x + width - 1, y + 1, y + height - 1);

		this.drawChar(',', x, y);
		this.drawChar('.', x + width - 1, y);
		this.drawChar('`', x, y + height - 1);
		this.drawChar('\'', x + width - 1, y + height - 1);
	}

	public void drawBoxSimpleUnicode(int x, int y, int width, int height) {
		this.drawHLine('\u2500', y, x + 1, x + width - 1);
		this.drawHLine('\u2500', y + height - 1, x + 1, x + width - 1);

		this.drawVLine('\u2502', x, y + 1, y + height - 1);
		this.drawVLine('\u2502', x + width - 1, y + 1, y + height - 1);

		this.drawChar('\u250c', x, y);
		this.drawChar('\u2510', x + width - 1, y);
		this.drawChar('\u2514', x, y + height - 1);
		this.drawChar('\u2518', x + width - 1, y + height - 1);
	}

	public void drawShape(AsciiShape shape, int x, int y) {
		shape.draw(this, x, y);
	}

	public void drawStringsLR(Collection<? extends CharSequence> strings, int x, int y) {
		int i = 0;
		if (x < 0) {
			x = 0;
		}
		for (CharSequence s : strings) {
			this.drawStringLR(s.toString(), x, y + i);
			i++;
		}

	}

	public void drawNoteSimple(int x, int y, int width, int height) {
		this.drawHLine('-', y, x + 1, x + width - 1);
		this.drawHLine('-', y + height - 1, x + 1, x + width - 1);

		this.drawVLine('|', x, y + 1, y + height - 1);
		this.drawVLine('|', x + width - 1, y + 1, y + height - 1);

		this.drawChar(',', x, y);

		this.drawStringLR("!. ", x + width - 3, y);
		this.drawStringLR("|_\\", x + width - 3, y + 1);

		this.drawChar('`', x, y + height - 1);
		this.drawChar('\'', x + width - 1, y + height - 1);
	}

	public void drawNoteSimpleUnicode(int x, int y, int width, int height) {
		this.drawChar('\u2591', x + width - 2, y + 1);

		this.drawHLine('\u2550', y, x + 1, x + width - 1, '\u2502', '\u2567');
		this.drawHLine('\u2550', y + height - 1, x + 1, x + width - 1, '\u2502', '\u2564');

		this.drawVLine('\u2551', x, y + 1, y + height - 1);
		this.drawVLine('\u2551', x + width - 1, y + 1, y + height - 1);

		this.drawChar('\u2554', x, y);
		this.drawChar('\u2557', x + width - 1, y);
		this.drawChar('\u255a', x, y + height - 1);
		this.drawChar('\u255d', x + width - 1, y + height - 1);
	}
}
