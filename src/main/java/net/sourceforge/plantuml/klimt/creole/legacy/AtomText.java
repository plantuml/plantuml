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
package net.sourceforge.plantuml.klimt.creole.legacy;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Neutron;
import net.sourceforge.plantuml.klimt.creole.NeutronType;
import net.sourceforge.plantuml.klimt.creole.atom.AbstractAtom;
import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.CharHidder;
import net.sourceforge.plantuml.utils.Log;

public final class AtomText extends AbstractAtom implements Atom {

	protected interface DelayedDouble {
		public double getDouble(StringBounder stringBounder);
	}

	private final FontConfiguration fontConfiguration;
	private final String text;
	private final DelayedDouble marginLeft;
	private final DelayedDouble marginRight;
	private final Url url;
	private final boolean manageSpecialChars;

	@JawsStrange
	protected AtomText(String text, FontConfiguration style, Url url, DelayedDouble marginLeft,
			DelayedDouble marginRight, boolean manageSpecialChars) {
//		if (text.contains("" + BackSlash.hiddenNewLine()))
//			throw new IllegalArgumentException(text);

		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		String s = CharHidder.unhide(text);
		if (manageSpecialChars)
			s = manageSpecialChars(s);

		this.manageSpecialChars = manageSpecialChars;
		this.text = s;
		this.fontConfiguration = style;
		this.url = url;
	}

	private static String manageSpecialChars(String s) {
		final int len = s.length();
		StringBuilder sb = null; // allocated lazily on first replacement
		int i = 0;
		while (i < len) {
			final char c = s.charAt(i);
			int consumed = 0;
			String replacement = null;

			if (c == '&') {
				// Try to match &#[0-9]+;
				int j = i + 1;
				if (j < len && s.charAt(j) == '#') {
					j++;
					final int start = j;
					while (j < len) {
						final char cj = s.charAt(j);
						if (cj < '0' || cj > '9')
							break;
						j++;
					}
					if (j > start && j < len && s.charAt(j) == ';') {
						try {
							final int codePoint = Integer.parseInt(s.substring(start, j));
							replacement = new String(Character.toChars(codePoint));
							consumed = j + 1 - i;
						} catch (NumberFormatException e) {
							// overflow: leave as-is
						}
					}
				}
			} else if (c == '<') {
				// Try to match <U+[0-9a-fA-F]{4,5}>
				if (i + 6 < len && s.charAt(i + 1) == 'U' && s.charAt(i + 2) == '+') {
					int j = i + 3;
					final int start = j;
					while (j < len && j - start < 5 && isHexDigit(s.charAt(j)))
						j++;
					final int hexLen = j - start;
					if (hexLen >= 4 && j < len && s.charAt(j) == '>') {
						final int value = Integer.parseInt(s.substring(start, j), 16);
						replacement = new String(Character.toChars(value));
						consumed = j + 1 - i;
					}
				}
			} else if (c == '~') {
				// Try to match ~@start
				if (i + 7 <= len && s.charAt(i + 1) == '@' && s.charAt(i + 2) == 's' && s.charAt(i + 3) == 't'
						&& s.charAt(i + 4) == 'a' && s.charAt(i + 5) == 'r' && s.charAt(i + 6) == 't') {
					replacement = "@start";
					consumed = 7;
				}
			} else if (c == '\\') {
				// Try to match \t
				if (i + 1 < len && s.charAt(i + 1) == 't') {
					replacement = "\t";
					consumed = 2;
				}
			}

			if (replacement != null) {
				if (sb == null) {
					sb = new StringBuilder(len);
					sb.append(s, 0, i);
				}
				sb.append(replacement);
				i += consumed;
			} else {
				if (sb != null)
					sb.append(c);
				i++;
			}
		}
		return sb == null ? s : sb.toString();
	}

	private static boolean isHexDigit(char c) {
		return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
	}

	private AtomText withText(String text) {
		return new AtomText(text, fontConfiguration, url, marginLeft, marginRight, manageSpecialChars);
	}

	@JawsStrange
	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final XDimension2D rect = stringBounder.calculateDimension(fontConfiguration.getFont(), text);
		Log.debug(() -> "g2d=" + rect);
		Log.debug(() -> "Size for " + text + " is " + rect);
		double h = rect.getHeight();
		if (h < 10)
			h = 10;

		double width = (text.indexOf("\t") == -1 && text.indexOf(Jaws.BLOCK_E1_REAL_TABULATION) == -1)
				|| stringBounder.matchesProperty("TIKZ") ? rect.getWidth() : getWidth(stringBounder, text);
		final double left = marginLeft.getDouble(stringBounder);
		final double right = marginRight.getDouble(stringBounder);
		return new XDimension2D(width + left + right, h);
	}

	public double getFontHeight(StringBounder stringBounder) {
		final XDimension2D rect = stringBounder.calculateDimension(fontConfiguration.getFont(), text);
		final double descent = stringBounder.getDescent(fontConfiguration.getFont(), text);
		return rect.getHeight() - descent;
	}

	@JawsStrange
	public void drawU(UGraphic ug) {
		if (url != null)
			ug.startUrl(url);

		if (ug.matchesProperty("SPECIALTXT")) {
			ug.draw(this);
		} else {

			final FontConfiguration useFontConfiguration = fontConfiguration.adjustColorForBackground(ug);

			if (marginLeft != AtomTextUtils.ZERO)
				ug = ug.apply(UTranslate.dx(marginLeft.getDouble(ug.getStringBounder())));

			final StringTokenizer tokenizer = new StringTokenizer(text, "\t" + Jaws.BLOCK_E1_REAL_TABULATION, true);

			// final int ypos = fontConfiguration.getSpace();
			final XDimension2D rect = ug.getStringBounder().calculateDimension(fontConfiguration.getFont(), text);
			final double descent = getDescent(ug.getStringBounder());
			final double ypos = rect.getHeight() - descent;

			double x = 0;
			if (tokenizer.hasMoreTokens()) {
				final double tabSize = getTabSize(ug.getStringBounder());
				while (tokenizer.hasMoreTokens()) {
					final String s = tokenizer.nextToken();
					if (s.equals("\t") || s.equals("" + Jaws.BLOCK_E1_REAL_TABULATION)) {
						final double remainder = x % tabSize;
						x += tabSize - remainder;
					} else {
						final XDimension2D dim = ug.getStringBounder().calculateDimension(fontConfiguration.getFont(),
								s);
						final UText utext = UText.build(s, useFontConfiguration);
						ug.apply(new UTranslate(x, ypos)).draw(utext);
						x += dim.getWidth();
					}
				}
			}
		}
		if (url != null)
			ug.closeUrl();

	}

	@JawsStrange
	private double getWidth(StringBounder stringBounder, String text) {
		final StringTokenizer tokenizer = new StringTokenizer(text, "\t" + Jaws.BLOCK_E1_REAL_TABULATION, true);
		final double tabSize = getTabSize(stringBounder);
		double x = 0;
		while (tokenizer.hasMoreTokens()) {
			final String s = tokenizer.nextToken();
			if (s.equals("\t") || s.equals("" + Jaws.BLOCK_E1_REAL_TABULATION)) {
				final double remainder = x % tabSize;
				x += tabSize - remainder;
			} else {
				final XDimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), s);
				x += dim.getWidth();
			}
		}
		return x;
	}

	private String tabString() {
		final int nb = fontConfiguration.getTabSize();
		if (nb >= 1 && nb < 7)
			return "        ".substring(0, nb);

		return "        ";
	}

	private double getDescent(StringBounder stringBounder) {
		return stringBounder.getDescent(fontConfiguration.getFont(), text);
	}

	private double getTabSize(StringBounder stringBounder) {
		return stringBounder.calculateDimension(fontConfiguration.getFont(), tabString()).getWidth();
	}

	@Override
	public final List<Neutron> getNeutrons() {
		final List<Neutron> result = new ArrayList<>();
		final StringBuilder pending = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			final char ch = text.charAt(i);
			if (pending.length() == 0) {
				pending.append(ch);
				continue;
			}
			final NeutronType pendingType = Neutron.getNeutronTypeFromChar(pending.charAt(0));
			final NeutronType currentType = Neutron.getNeutronTypeFromChar(ch);
			if (pendingType != currentType || pendingType == NeutronType.CJK_IDEOGRAPH) {
				addPending(result, pending.toString());
				pending.setLength(0);
			}
			pending.append(ch);
			// if (Neutron.isSentenceBoundary(ch) || Neutron.isChineseSentenceBoundary(ch))
//			if (Neutron.isChineseSentenceBoundary(ch)) {
//				addPending(result, pending.toString());
//				pending.setLength(0);
//				result.add(Neutron.zwspSeparator());
//			}

		}
		if (pending.length() > 0)
			addPending(result, pending.toString());

		return result;
	}

	private void addPending(List<Neutron> result, String pending) {
		final Neutron tmp = Neutron.create(withText(pending));
		if (tmp.getType() == NeutronType.WHITESPACE || tmp.getType() == NeutronType.CJK_IDEOGRAPH)
			result.add(Neutron.zwspSeparator());
		result.add(tmp);
		if (tmp.getType() == NeutronType.WHITESPACE || tmp.getType() == NeutronType.CJK_IDEOGRAPH)
			result.add(Neutron.zwspSeparator());
	}

	public final String getText() {
		return text;
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return fontConfiguration.getSpace();
	}

	@Override
	public String toString() {
		return text + " " + fontConfiguration;
	}

	public FontConfiguration getFontConfiguration() {
		return fontConfiguration;
	}

}
