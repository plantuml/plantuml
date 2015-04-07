/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.awt.font.LineMetrics;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.CharHidder;

public class AtomText implements Atom {

	interface DelayedDouble {
		public double getDouble(StringBounder stringBounder);
	}

	private static DelayedDouble ZERO = new DelayedDouble() {
		public double getDouble(StringBounder stringBounder) {
			return 0;
		}
	};

	private final FontConfiguration fontConfiguration;
	private final String text;
	private final DelayedDouble marginLeft;
	private final DelayedDouble marginRight;
	private final Url url;

	public static Atom create(String text, FontConfiguration fontConfiguration) {
		return new AtomText(text, fontConfiguration, null, ZERO, ZERO);
	}

	public static Atom createUrl(Url url, FontConfiguration fontConfiguration) {
		fontConfiguration = fontConfiguration.hyperlink();
		return new AtomText(url.getLabel(), fontConfiguration, url, ZERO, ZERO);
	}

	public static AtomText createHeading(String text, FontConfiguration fontConfiguration, int order) {
		if (order == 0) {
			fontConfiguration = fontConfiguration.bigger(4).bold();
		} else if (order == 1) {
			fontConfiguration = fontConfiguration.bigger(2).bold();
		} else if (order == 2) {
			fontConfiguration = fontConfiguration.bigger(1).bold();
		} else {
			fontConfiguration = fontConfiguration.italic();
		}
		return new AtomText(text, fontConfiguration, null, ZERO, ZERO);
	}

	public static Atom createListNumber(final FontConfiguration fontConfiguration, final int order, int localNumber) {
		final DelayedDouble left = new DelayedDouble() {
			public double getDouble(StringBounder stringBounder) {
				final Dimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), "9. ");
				return dim.getWidth() * order;
			}
		};
		final DelayedDouble right = new DelayedDouble() {
			public double getDouble(StringBounder stringBounder) {
				final Dimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), ".");
				return dim.getWidth();
			}
		};
		return new AtomText("" + (localNumber + 1) + ".", fontConfiguration, null, left, right);
	}

	@Override
	public String toString() {
		return text + " " + fontConfiguration;
	}

	private AtomText(String text, FontConfiguration style, Url url, DelayedDouble marginLeft, DelayedDouble marginRight) {
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		//this.text = StringUtils.showComparatorCharacters(StringUtils.manageBackslash(text));
		this.text = StringUtils.showComparatorCharacters(CharHidder.unhide(text));
		this.fontConfiguration = style;
		this.url = url;
	}

	public FontConfiguration getFontConfiguration() {
		return fontConfiguration;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D rect = stringBounder.calculateDimension(fontConfiguration.getFont(), text);
		Log.debug("g2d=" + rect);
		Log.debug("Size for " + text + " is " + rect);
		double h = rect.getHeight();
		if (h < 10) {
			h = 10;
		}
		final double width = text.indexOf('\t') == -1 ? rect.getWidth() : getWidth(stringBounder);
		final double left = marginLeft.getDouble(stringBounder);
		final double right = marginRight.getDouble(stringBounder);

		return new Dimension2DDouble(width + left + right, h);
	}

	private double getDescent() {
		final LineMetrics fm = TextBlockUtils.getLineMetrics(fontConfiguration.getFont(), text);
		final double descent = fm.getDescent();
		return descent;
	}

	public double getFontSize2D() {
		return fontConfiguration.getFont().getSize2D();
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return fontConfiguration.getSpace();
	}

	private double getTabSize(StringBounder stringBounder) {
		return stringBounder.calculateDimension(fontConfiguration.getFont(), "        ").getWidth();
	}

	public void drawU(UGraphic ug) {
		if (ug.isSpecialTxt()) {
			ug.draw(this);
			return;
		}
		if (url != null) {
			ug.startUrl(url);
		}
		ug = ug.apply(new UChangeColor(fontConfiguration.getColor()));
		if (marginLeft != ZERO) {
			ug = ug.apply(new UTranslate(marginLeft.getDouble(ug.getStringBounder()), 0));
		}

		final StringTokenizer tokenizer = new StringTokenizer(text, "\t", true);

		double x = 0;
		// final int ypos = fontConfiguration.getSpace();
		final Dimension2D rect = ug.getStringBounder().calculateDimension(fontConfiguration.getFont(), text);
		final double descent = getDescent();
		final double ypos = rect.getHeight() - descent;
		if (tokenizer.hasMoreTokens()) {
			final double tabSize = getTabSize(ug.getStringBounder());
			while (tokenizer.hasMoreTokens()) {
				final String s = tokenizer.nextToken();
				if (s.equals("\t")) {
					final double remainder = x % tabSize;
					x += tabSize - remainder;
				} else {
					final UText utext = new UText(s, fontConfiguration);
					final Dimension2D dim = ug.getStringBounder().calculateDimension(fontConfiguration.getFont(), s);
					ug.apply(new UTranslate(x, ypos)).draw(utext);
					x += dim.getWidth();
				}
			}
		}
		if (url != null) {
			ug.closeAction();
		}
	}

	private double getWidth(StringBounder stringBounder) {
		return getWidth(stringBounder, text);
	}

	private double getWidth(StringBounder stringBounder, String text) {
		final StringTokenizer tokenizer = new StringTokenizer(text, "\t", true);
		final double tabSize = getTabSize(stringBounder);
		double x = 0;
		while (tokenizer.hasMoreTokens()) {
			final String s = tokenizer.nextToken();
			if (s.equals("\t")) {
				final double remainder = x % tabSize;
				x += tabSize - remainder;
			} else {
				final Dimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), s);
				x += dim.getWidth();
			}
		}
		return x;
	}

	public List<AtomText> getSplitted(StringBounder stringBounder, double maxWidth) {
		final List<AtomText> result = new ArrayList<AtomText>();
		final StringTokenizer st = new StringTokenizer(text, " ", true);
		final StringBuilder currentLine = new StringBuilder();
		while (st.hasMoreTokens()) {
			final String token = st.nextToken();
			final double w = getWidth(stringBounder, currentLine + token);
			if (w > maxWidth) {
				result.add(new AtomText(currentLine.toString(), fontConfiguration, url, marginLeft, marginRight));
				currentLine.setLength(0);
				if (token.startsWith(" ") == false) {
					currentLine.append(token);
				}
			} else {
				currentLine.append(token);
			}
		}
		result.add(new AtomText(currentLine.toString(), fontConfiguration, url, marginLeft, marginRight));
		return Collections.unmodifiableList(result);

	}

	public final String getText() {
		return text;
	}
}
