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
package net.sourceforge.plantuml.asciiverse;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.asciiart.TextStringBounder;
import net.sourceforge.plantuml.security.SecurityUtils;

public final class InfinitePlan {

	private final TextStringBounder stringBounder;
	private final List<InfiniteString> plan;
	private final int dx;
	private final int dy;

	public InfinitePlan(FileFormat fileFormat) {
		this(fileFormat, 0, 0, new ArrayList<>());
	}

	private InfinitePlan(FileFormat fileFormat, int dx, int dy, List<InfiniteString> plan) {
		this.stringBounder = new TextStringBounder(fileFormat);
		this.dx = dx;
		this.dy = dy;
		this.plan = plan;
	}

	public InfinitePlan move(int movex, int movey) {
		return new InfinitePlan(stringBounder.getFileFormat(), dx + movex, dy + movey, plan);
	}

	public void drawChar(char c) {
		ensureSize(dy);
		plan.get(dy).setCharAt(dx, c);
	}

	public void drawString(String s) {
		ensureSize(dy);
		plan.get(dy).setStringAt(dx, s);
	}

//	public void fillRect(char c, int x, int y, int width, int height) {
//		for (int i = 0; i < width; i++)
//			for (int j = 0; j < height; j++)
//				drawChar(c, x + i, y + j);
//	}

	private void ensureSize(int yAbsolute) {
		while (yAbsolute >= plan.size())
			plan.add(new InfiniteString());
	}

	public char getCharAt(int x, int y) {
		x += dx;
		y += dy;
		if (y < 0 || y >= plan.size())
			return ' ';

		return plan.get(y).getCharAt(x);
	}

	// Every line is rendered from the same startingPosition, found here as
	// the leftmost position any line actually used (InfiniteString.
	// getLeftmostPosition(), <= 0). Using each line's own extent instead
	// would misalign the output: a line that never drew left of 0 would start
	// one column further right than a neighboring line that did. If no line
	// ever went negative, this is 0, exactly the historical behavior.
	public void exportTxt(OutputStream os) {
		final PrintStream ps = SecurityUtils.createPrintStream(os);
		int startingPosition = 0;
		for (InfiniteString line : plan)
			startingPosition = Math.min(startingPosition, line.getLeftmostPosition());

		for (InfiniteString line : plan)
			ps.println(line.getString(startingPosition));

	}

	public TextStringBounder getStringBounder() {
		return stringBounder;
	}

	// Character set: centralizes the ASCII vs Unicode (box-drawing) choice for
	// every drawing method below, so that callers (Participant.asciiDraw,
	// CommunicationTile.asciiDraw, the lifeline-drawing pass...) never hardcode
	// a character themselves and stay correct regardless of FileFormat.
	// The Unicode code points below are copy-pasted from the legacy
	// asciiart.UmlCharAreaImpl (drawBoxSimpleUnicode) rather than reused from
	// it, since that package is slated for removal (see ASCIIVERSE.md).
	public boolean isUnicode() {
		return stringBounder.getFileFormat() == FileFormat.UTXT;
	}

	public char getHLineChar() {
		return isUnicode() ? '\u2500' : '-'; // ─ or -
	}

	public char getVLineChar() {
		return isUnicode() ? '\u2502' : '|'; // │ or |
	}

	// Straight, unconditional fills over an inclusive range — the ASCII
	// counterpart of a UGraphic hline/vline. Callers that need to preserve
	// existing content (e.g. not overwrite an arrowhead) should keep checking
	// getCharAt() themselves before calling drawChar() directly, as
	// PlayingSpaceWithParticipants's lifeline-fill pass currently does.
	public void drawHLine(int xFrom, int xTo, int y) {
		fillHLine(getHLineChar(), xFrom, xTo, y);
	}

	public void drawVLine(int yFrom, int yTo, int x) {
		fillVLine(getVLineChar(), yFrom, yTo, x);
	}

	// Dashed variant, for dotted arrows (return messages, async replies...).
	// Idea copied from the legacy asciiart.ComponentTextArrow.drawU(), which
	// draws the full line then blanks every other cell when
	// ArrowConfiguration.isDotted(); reimplemented here as "only draw every
	// other cell" in the first place, same visual result, no code reused.
	public void drawHLine(int xFrom, int xTo, int y, boolean dotted) {
		if (dotted == false) {
			drawHLine(xFrom, xTo, y);
			return;
		}

		final char c = getHLineChar();
		int idx = 0;
		for (int i = xFrom; i <= xTo; i++, idx++)
			if (idx % 2 == 0)
				move(i, y).drawChar(c);
	}

	// Char-parameterized versions of the two above: used internally wherever
	// a border needs a character other than the plain box/lifeline one
	// (namely the note box's double lines in Unicode mode, see
	// drawNoteBoxUnicode()/ANote). Package-private rather than private so
	// that ANote (the AsciiBlock that owns the note box shape) can call them
	// directly instead of every InfinitePlan drawing primitive needing a
	// public wrapper.
	void fillHLine(char c, int xFrom, int xTo, int y) {
		for (int i = xFrom; i <= xTo; i++)
			move(i, y).drawChar(c);
	}

	void fillVLine(char c, int yFrom, int yTo, int x) {
		for (int j = yFrom; j <= yTo; j++)
			move(x, j).drawChar(c);
	}

	// Package-private (not private): AGroupFrame needs the Unicode-aware top
	// corners for its own custom-drawn top border (its bottom border and sides
	// use different characters entirely — tildes and '!' — so it cannot reuse
	// drawBox() wholesale, see ASCIIVERSE.md §28), same relaxation already made
	// for fillHLine/fillVLine when ANote needed them (§18).
	char getTopLeftChar() {
		return isUnicode() ? '\u250c' : ','; // ┌ or ,
	}

	char getTopRightChar() {
		return isUnicode() ? '\u2510' : '.'; // ┐ or .
	}

	private char getBottomLeftChar() {
		return isUnicode() ? '\u2514' : '`'; // └ or `
	}

	private char getBottomRightChar() {
		return isUnicode() ? '\u2518' : '\''; // ┘ or '
	}

	public void drawBox(int width, int height) {
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException();

		// top border
		drawChar(getTopLeftChar());
		if (width > 2)
			drawHLine(1, width - 2, 0);
		if (width > 1)
			move(width - 1, 0).drawChar(getTopRightChar());

		// sides
		if (height > 2) {
			drawVLine(1, height - 2, 0);
			if (width > 1)
				drawVLine(1, height - 2, width - 1);
		}

		// bottom border
		if (height > 1) {
			move(0, height - 1).drawChar(getBottomLeftChar());
			if (width > 2)
				drawHLine(1, width - 2, height - 1);
			if (width > 1)
				move(width - 1, height - 1).drawChar(getBottomRightChar());
		}
	}

	// Returns an AsciiBlock rather than drawing eagerly: the actual shape
	// (ASCII dog-ear vs. Unicode double-line box) lives in ANote, so that the
	// note box's drawing code sits next to the object that owns it instead of
	// as anonymous classes here. ANote.asciiDraw(plan) is called later,
	// against whatever InfinitePlan the caller has positioned at the block's
	// top-left corner (see AsciiBlock.asciiDraw's contract) — isUnicode isn't
	// even needed here: ANote reads it off that same plan when it draws,
	// rather than being told at construction time (see ANote.asciiDraw()).
	//
	// The box is sized around `text` rather than taking explicit width/height:
	// `text` is itself an AsciiBlock (e.g. a Display, which now prints its own
	// lines — see Display.asciiDraw()), and ANote derives its own width/height
	// from text.asciiDimension() internally — nothing to compute here.
	public AsciiBlock createNoteBox(AsciiBlock text) {
		return new ANote(text);
	}

}
