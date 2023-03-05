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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Parser;
import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.creole.atom.AtomHorizontalTexts;
import net.sourceforge.plantuml.klimt.creole.atom.AtomImg;
import net.sourceforge.plantuml.klimt.creole.atom.AtomOpenIcon;
import net.sourceforge.plantuml.klimt.creole.atom.AtomSprite;
import net.sourceforge.plantuml.klimt.creole.atom.AtomVerticalTexts;
import net.sourceforge.plantuml.klimt.creole.command.Splitter;
import net.sourceforge.plantuml.klimt.creole.legacy.AtomText.DelayedDouble;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.ImgValign;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.openiconic.OpenIcon;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.url.Url;

public class AtomTextUtils {

	protected static DelayedDouble ZERO = new DelayedDouble() {
		public double getDouble(StringBounder stringBounder) {
			return 0;
		}
	};

	public static Atom createLegacy(String text, FontConfiguration fontConfiguration) {
		return new AtomText(text, fontConfiguration, null, ZERO, ZERO, true);
	}

	public static Atom create(String text, FontConfiguration fontConfiguration) {
		return new AtomText(text, fontConfiguration, null, ZERO, ZERO, false);
	}

	public static Atom createUrl(Url url, FontConfiguration fontConfiguration, ISkinSimple skinSimple) {
		fontConfiguration = fontConfiguration.hyperlink();
		final Display display = Display.getWithNewlines(url.getLabel());
		if (display.size() > 1) {
			final List<Atom> all = new ArrayList<>();
			for (CharSequence s : display.asList()) {
				all.add(createAtomText(s.toString(), url, fontConfiguration, skinSimple));
			}
			return new AtomVerticalTexts(all);

		}
		return createAtomText(url.getLabel(), url, fontConfiguration, skinSimple);
	}

	private static final Pattern p = Pattern.compile(Splitter.openiconPattern + "|" + Splitter.spritePattern2 + "|"
			+ Splitter.imgPatternNoSrcColon + "|" + Splitter.emojiPattern);

	private static Atom createAtomText(final String text, Url url, FontConfiguration fontConfiguration,
			ISkinSimple skinSimple) {
		final Matcher m = p.matcher(text);
		final List<Atom> result = new ArrayList<>();
		while (m.find()) {
			final StringBuffer sb = new StringBuffer();
			m.appendReplacement(sb, "");
			if (sb.length() > 0) {
				result.add(new AtomText(sb.toString(), fontConfiguration, url, ZERO, ZERO, true));
			}
			final String valOpenicon = m.group(1);
			final String valSprite = m.group(3);
			final String valImg = m.group(5);
			final String valEmoji = m.group(7);
			if (valEmoji != null)
				throw new UnsupportedOperationException();

			if (valOpenicon != null) {
				final OpenIcon openIcon = OpenIcon.retrieve(valOpenicon);
				if (openIcon != null) {
					final double scale = Parser.getScale(m.group(2), 1);
					result.add(new AtomOpenIcon(null, scale, openIcon, fontConfiguration, url));
				}
			} else if (valSprite != null) {
				final Sprite sprite = skinSimple.getSprite(valSprite);
				if (sprite != null) {
					final double scale = Parser.getScale(m.group(4), 1);
					result.add(new AtomSprite(null, scale, fontConfiguration, sprite, url));
				}
			} else if (valImg != null) {
				final double scale = Parser.getScale(m.group(6), 1);
				result.add(AtomImg.create(valImg, ImgValign.TOP, 0, scale, url));

			}
		}
		final StringBuffer sb = new StringBuffer();
		m.appendTail(sb);
		if (sb.length() > 0) {
			result.add(new AtomText(sb.toString(), fontConfiguration, url, ZERO, ZERO, true));
		}
		if (result.size() == 1) {
			return result.get(0);
		}
		return new AtomHorizontalTexts(result);
	}

	public static Atom createListNumber(final FontConfiguration fontConfiguration, final int order, int localNumber) {
		final DelayedDouble left = new DelayedDouble() {
			public double getDouble(StringBounder stringBounder) {
				final XDimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), "9. ");
				return dim.getWidth() * order;
			}
		};
		final DelayedDouble right = new DelayedDouble() {
			public double getDouble(StringBounder stringBounder) {
				final XDimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), ".");
				return dim.getWidth();
			}
		};
		return new AtomText("" + (localNumber + 1) + ".", fontConfiguration, null, left, right, true);
	}

}
