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

public enum AsciiShape {

	// int STICKMAN_HEIGHT = 5;
	// int STICKMAN_UNICODE_HEIGHT = 6;

	STICKMAN(3, 5), STICKMAN_UNICODE(3, 6), BOUNDARY(8, 3), DATABASE(10, 6);

	private final int width;
	private final int height;

	private AsciiShape(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void draw(BasicCharArea area, int x, int y) {
		if (this == STICKMAN) {
			drawStickMan(area, x, y);
		} else if (this == STICKMAN_UNICODE) {
			drawStickManUnicode(area, x, y);
		} else if (this == BOUNDARY) {
			drawBoundary(area, x, y);
		} else if (this == DATABASE) {
			drawDatabase(area, x, y);
		}
	}

	private void drawDatabase(BasicCharArea area, int x, int y) {
		area.drawStringLR(" ,.-^^-._", x, y++);
		area.drawStringLR("|-.____.-|", x, y++);
		area.drawStringLR("|        |", x, y++);
		area.drawStringLR("|        |", x, y++);
		area.drawStringLR("|        |", x, y++);
		area.drawStringLR("'-.____.-'", x, y++);
	}

	private void drawDatabaseSmall(BasicCharArea area, int x, int y) {
		area.drawStringLR(" ,.-\"-._ ", x, y++);
		area.drawStringLR("|-.___.-|", x, y++);
		area.drawStringLR("|       |", x, y++);
		area.drawStringLR("|       |", x, y++);
		area.drawStringLR("|       |", x, y++);
		area.drawStringLR("'-.___.-'", x, y++);
	}

	private void drawBoundary(BasicCharArea area, int x, int y) {
		area.drawStringLR("|   ,-.", x, y++);
		area.drawStringLR("+--{   )", x, y++);
		area.drawStringLR("|   `-'", x, y++);
	}

	private void drawStickMan(BasicCharArea area, int x, int y) {
		area.drawStringLR(",-.", x, y++);
		area.drawStringLR("`-'", x, y++);
		area.drawStringLR("/|\\", x, y++);
		area.drawStringLR(" | ", x, y++);
		area.drawStringLR("/ \\", x, y++);
	}

	private void drawStickManUnicode(BasicCharArea area, int x, int y) {
		area.drawStringLR("\u250c\u2500\u2510", x, y++);
		area.drawStringLR("\u2551\"\u2502", x, y++);
		area.drawStringLR("\u2514\u252c\u2518", x, y++);
		area.drawStringLR("\u250c\u253c\u2510", x, y++);
		area.drawStringLR(" \u2502 ", x, y++);
		area.drawStringLR("\u250c\u2534\u2510", x, y++);
	}

	public final int getHeight() {
		return height;
	}

	public final int getWidth() {
		return width;
	}

}
