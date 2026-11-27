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
package net.sourceforge.plantuml.ngdot.draw;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinSimple;

/**
 * A plain DOT label (not an HTML one), drawn line by line.
 * <p>
 * As in Graphviz ({@code make_simple_label}), {@code \n}, {@code \l} and
 * {@code \r} end a line which is centered, left or right justified; a real
 * newline counts as {@code \n}; {@code \\} is a backslash and any other
 * escaped character stands for itself. The object escapes ({@code \N},
 * {@code \G}...) are replaced beforehand, see {@link #substitute}. The text
 * is never read as Creole.
 */
public final class DotLabel implements TextBlock {

	enum Justification {
		LEFT, CENTER, RIGHT
	}

	private final List<TextBlock> lines = new ArrayList<TextBlock>();
	private final List<Justification> justifications = new ArrayList<Justification>();

	private DotLabel() {
	}

	public static DotLabel create(String text, FontConfiguration font, ISkinSimple skinParam) {
		final DotLabel result = new DotLabel();
		if (text == null || text.length() == 0)
			return result;

		final StringBuilder current = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);
			if (c == '\\' && i + 1 < text.length()) {
				final char next = text.charAt(++i);
				if (next == 'n')
					result.addLine(current, Justification.CENTER, font, skinParam);
				else if (next == 'l')
					result.addLine(current, Justification.LEFT, font, skinParam);
				else if (next == 'r')
					result.addLine(current, Justification.RIGHT, font, skinParam);
				else
					current.append(next);
			} else if (c == '\n') {
				result.addLine(current, Justification.CENTER, font, skinParam);
			} else if (c != '\r') {
				current.append(c);
			}
		}
		if (current.length() > 0)
			result.addLine(current, Justification.CENTER, font, skinParam);
		return result;
	}

	private void addLine(StringBuilder text, Justification justification, FontConfiguration font,
			ISkinSimple skinParam) {
		// An empty line still takes the height of a line
		final String s = text.length() == 0 ? " " : text.toString();
		lines.add(Display.create(s).create7(font, HorizontalAlignment.LEFT, skinParam, CreoleMode.NO_CREOLE));
		justifications.add(justification);
		text.setLength(0);
	}

	/**
	 * Replaces the object escapes of a label: {@code \N} (node name), {@code \G}
	 * (graph name), {@code \E} (edge name), {@code \T} and {@code \H} (tail and
	 * head names). {@code \\} is kept for {@link #create}.
	 */
	public static String substitute(String text, String nodeName, String graphName, String tailName,
			String headName, String edgeName) {
		if (text == null || text.indexOf('\\') == -1)
			return text;
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);
			if (c == '\\' && i + 1 < text.length()) {
				final char next = text.charAt(++i);
				if (next == 'N')
					appendNotNull(sb, nodeName);
				else if (next == 'G')
					appendNotNull(sb, graphName);
				else if (next == 'E')
					appendNotNull(sb, edgeName);
				else if (next == 'T')
					appendNotNull(sb, tailName);
				else if (next == 'H')
					appendNotNull(sb, headName);
				else
					sb.append(c).append(next);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static void appendNotNull(StringBuilder sb, String s) {
		if (s != null)
			sb.append(s);
	}

	public boolean isEmpty() {
		return lines.isEmpty();
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (TextBlock line : lines) {
			final XDimension2D dim = line.calculateDimension(stringBounder);
			width = Math.max(width, dim.getWidth());
			height += dim.getHeight();
		}
		return new XDimension2D(width, height);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double width = calculateDimension(stringBounder).getWidth();
		double y = 0;
		for (int i = 0; i < lines.size(); i++) {
			final TextBlock line = lines.get(i);
			final XDimension2D dim = line.calculateDimension(stringBounder);
			double x = 0;
			if (justifications.get(i) == Justification.CENTER)
				x = (width - dim.getWidth()) / 2;
			else if (justifications.get(i) == Justification.RIGHT)
				x = width - dim.getWidth();
			line.drawU(ug.apply(new UTranslate(x, y)));
			y += dim.getHeight();
		}
	}

}
