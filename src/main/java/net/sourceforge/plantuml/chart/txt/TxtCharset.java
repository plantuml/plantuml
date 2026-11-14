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
 */
package net.sourceforge.plantuml.chart.txt;

/**
 * The characters of a chart drawn as text: Unicode (box drawing, blocks) for UTXT,
 * plain ASCII for ATXT. The renderers never write a drawing character themselves.
 */
final class TxtCharset {

	// Fill of the bars of the n-th bar series, and of the value tables
	private static final char[] UNICODE_FILLS = { '█', '▓', '▒', '░' }; // █ ▓ ▒ ░
	private static final char[] ASCII_FILLS = { '#', '%', '@', '*' };

	// Lines of the n-th line series: horizontal, vertical, then the corners joining
	// right-down, left-down, right-up and left-up
	private static final String[] UNICODE_LINES = { "─│╭╮╰╯", // ─│╭╮╰╯
			"═║╔╗╚╝", // ═║╔╗╚╝
			"━┃┏┓┗┛" }; // ━┃┏┓┗┛
	private static final String[] ASCII_LINES = { "-|++++", "=|++++", "~|++++" };

	private final boolean unicode;

	TxtCharset(boolean unicode) {
		this.unicode = unicode;
	}

	char dot() {
		return unicode ? '·' : '.'; // ·
	}

	char vAxis() {
		return unicode ? '│' : '|'; // │
	}

	char hAxis() {
		return unicode ? '─' : '-'; // ─
	}

	// A tick on a vertical axis, its label on the left
	char tickLeft() {
		return unicode ? '┤' : '+'; // ┤
	}

	// A tick on a horizontal axis, its label below
	char tickDown() {
		return unicode ? '┬' : '+'; // ┬
	}

	char origin() {
		return unicode ? '┼' : '+'; // ┼
	}

	char fill(int style) {
		final char[] fills = unicode ? UNICODE_FILLS : ASCII_FILLS;
		return fills[style % fills.length];
	}

	// Only the plain block has halves: a bar ends on a half cell
	boolean hasHalves(int style) {
		return unicode && style % UNICODE_FILLS.length == 0;
	}

	char lowerHalf() {
		return '▄'; // ▄
	}

	char upperHalf() {
		return '▀'; // ▀
	}

	char leftHalf() {
		return '▌'; // ▌
	}

	char rightHalf() {
		return '▐'; // ▐
	}

	char lineH(int style) {
		return line(style, 0);
	}

	char lineV(int style) {
		return line(style, 1);
	}

	char cornerRightDown(int style) {
		return line(style, 2);
	}

	char cornerLeftDown(int style) {
		return line(style, 3);
	}

	char cornerRightUp(int style) {
		return line(style, 4);
	}

	char cornerLeftUp(int style) {
		return line(style, 5);
	}

	private char line(int style, int index) {
		final String[] lines = unicode ? UNICODE_LINES : ASCII_LINES;
		return lines[style % lines.length].charAt(index);
	}
}
