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
package net.sourceforge.plantuml.stats;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignment;

public class TextTable {

	private final static DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

	static interface TextLine {
		public int nbCols();

		public int getPreferredWidth(int col);

		public String getPrinted(int[] width);

	}

	static class Separator implements TextLine {

		public int nbCols() {
			return 0;
		}

		public int getPreferredWidth(int col) {
			return 0;
		}

		public String getPrinted(int[] width) {
			final StringBuilder sb = new StringBuilder();
			for (int w : width) {
				sb.append('+');
				for (int i = 0; i < w + 2; i++) {
					sb.append('-');
				}
			}
			sb.append('+');
			return sb.toString();
		}
	}

	static class DataLine implements TextLine {

		private final Object[] cells;

		private DataLine(Object[] cells) {
			this.cells = cells;
		}

		public int nbCols() {
			return cells.length;
		}

		public int getPreferredWidth(int col) {
			if (col < cells.length) {
				return formatMe(cells[col], 0).length();
			}
			return 0;
		}

		public String getPrinted(int[] width) {
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < width.length; i++) {
				sb.append('|');
				final Object s = cells[i];
				sb.append(' ');
				sb.append(formatMe(s, width[i]));
				sb.append(' ');
			}
			sb.append('|');
			return sb.toString();
		}

		private String formatMe(Object s, int width) {
			final StringBuilder sb = new StringBuilder();
			HorizontalAlignment align = HorizontalAlignment.CENTER;

			if (s instanceof Long) {
				final String num = String.format("%,d", s).replaceAll("\u00A0", " ");
				sb.append(num);
				align = HorizontalAlignment.RIGHT;
			} else if (s instanceof Date) {
				sb.append(formatter.format(s));
			} else if (s != null) {
				sb.append(s.toString());
			}
			while (sb.length() < width) {
				if (align == HorizontalAlignment.RIGHT) {
					sb.insert(0, " ");
				} else {
					sb.append(" ");
				}
				if (align == HorizontalAlignment.CENTER && sb.length() < width) {
					sb.insert(0, " ");
				}
			}
			return sb.toString();
		}
	}

	private final List<TextLine> lines = new ArrayList<TextLine>();

	public void addLine(Object... cells) {
		this.lines.add(new DataLine(cells));

	}

	public void addSeparator() {
		this.lines.add(new Separator());
	}

	private int[] getColsWidth() {
		final int[] result = new int[getNbCols()];
		for (int c = 0; c < result.length; c++) {
			for (TextLine line : lines) {
				result[c] = Math.max(result[c], line.getPreferredWidth(c));
			}
		}
		return result;
	}

	private int getNbCols() {
		int result = 0;
		for (TextLine line : lines) {
			result = Math.max(result, line.nbCols());
		}
		return result;
	}

	public void printMe(PrintStream ps) {
		final int width[] = getColsWidth();
		for (TextLine line : lines) {
			final String s = line.getPrinted(width);
			ps.println(s);
		}

	}

	public int getLines() {
		return lines.size();
	}

}
