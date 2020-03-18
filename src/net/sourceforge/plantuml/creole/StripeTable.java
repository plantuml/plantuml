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
package net.sourceforge.plantuml.creole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.creole.atom.AtomTable;
import net.sourceforge.plantuml.creole.atom.AtomWithMargin;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class StripeTable implements Stripe {

	static enum Mode {
		HEADER, NORMAL
	};

	final private FontConfiguration fontConfiguration;
	final private ISkinSimple skinParam;
	final private AtomTable table;
	final private Atom marged;
	final private StripeStyle stripeStyle = new StripeStyle(StripeStyleType.NORMAL, 0, '\0');

	public StripeTable(FontConfiguration fontConfiguration, ISkinSimple skinParam, String line) {
		this.skinParam = skinParam;
		this.fontConfiguration = fontConfiguration;
		HColor lineColor = getBackOrFrontColor(line, 1);
		if (lineColor == null) {
			lineColor = fontConfiguration.getColor();
		}
		this.table = new AtomTable(lineColor);
		this.marged = new AtomWithMargin(table, 2, 2);
		analyzeAndAddInternal(line);
	}

	public List<Atom> getAtoms() {
		return Collections.<Atom>singletonList(marged);
	}

	public Atom getHeader() {
		return null;
	}

	static Atom asAtom(List<StripeSimple> cells, double padding) {
		final Sheet sheet = new Sheet(HorizontalAlignment.LEFT);
		for (StripeSimple cell : cells) {
			sheet.add(cell);
		}
		return new SheetBlock1(sheet, LineBreakStrategy.NONE, padding);
	}

	private HColor getBackOrFrontColor(String line, int idx) {
		if (CreoleParser.doesStartByColor(line)) {
			final int idx1 = line.indexOf('#');
			final int idx2 = line.indexOf('>');
			if (idx2 == -1) {
				throw new IllegalStateException();
			}
			final String[] color = line.substring(idx1, idx2).split(",");
			if (idx < color.length) {
				return skinParam.getIHtmlColorSet().getColorIfValid(color[idx]);
			}
		}
		return null;
	}

	private String withouBackColor(String line) {
		final int idx2 = line.indexOf('>');
		if (idx2 == -1) {
			throw new IllegalStateException();
		}
		return line.substring(idx2 + 1);
	}

	private static final String hiddenBar = "\uE000";

	private void analyzeAndAddInternal(String line) {
		line = line.replace("\\|", hiddenBar);
		HColor lineBackColor = getBackOrFrontColor(line, 0);
		if (lineBackColor != null) {
			line = withouBackColor(line);
		}
		table.newLine(lineBackColor);
		for (final StringTokenizer st = new StringTokenizer(line, "|"); st.hasMoreTokens();) {
			Mode mode = Mode.NORMAL;
			String v = st.nextToken().replace(hiddenBar.charAt(0), '|');
			if (v.startsWith("=")) {
				v = v.substring(1);
				mode = Mode.HEADER;
			}
			HColor cellBackColor = getBackOrFrontColor(v, 0);
			if (cellBackColor != null) {
				v = withouBackColor(v);
			}
			final List<String> lines = getWithNewlinesInternal(v);
			final List<StripeSimple> cells = new ArrayList<StripeSimple>();
			for (String s : lines) {
				final StripeSimple cell = new StripeSimple(getFontConfiguration(mode), stripeStyle, new CreoleContext(),
						skinParam, CreoleMode.FULL);
				if (s.startsWith("<r>")) {
					cell.setCellAlignment(HorizontalAlignment.RIGHT);
					s = s.substring("<r>".length());
				}
				cell.analyzeAndAdd(s);
				cells.add(cell);
			}
			table.addCell(asAtom(cells, skinParam.getPadding()), cellBackColor);
		}
	}

	static List<String> getWithNewlinesInternal(String s) {
		final List<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n') {
					result.add(current.toString());
					current.setLength(0);
				} else if (c2 == '\\') {
					current.append(c2);
				} else {
					current.append(c);
					current.append(c2);
				}
			} else if (c == BackSlash.hiddenNewLine()) {
				result.add(current.toString());
				current.setLength(0);
			} else {
				current.append(c);
			}
		}
		result.add(current.toString());
		return result;
	}

	private FontConfiguration getFontConfiguration(Mode mode) {
		if (mode == Mode.NORMAL) {
			return fontConfiguration;
		}
		return fontConfiguration.bold();
	}

	public void analyzeAndAddLine(String line) {
		analyzeAndAddInternal(line);
	}

}
