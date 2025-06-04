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
package net.sourceforge.plantuml.asciiart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.txt.UGraphicTxt;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;

public class ComponentTextArrow extends AbstractComponentText implements ArrowComponent {

	private final ComponentType type;
	private final Display stringsToDisplay;
	private final FileFormat fileFormat;
	private final ArrowConfiguration config;
	private final int maxAsciiMessageLength;

	public ComponentTextArrow(ComponentType type, ArrowConfiguration config, Display stringsToDisplay,
			FileFormat fileFormat, int maxAsciiMessageLength) {
		this.fileFormat = fileFormat;
		this.maxAsciiMessageLength = maxAsciiMessageLength;
		this.type = type;
		this.config = config;
		this.stringsToDisplay = cleanAndManageBoldNumber(stringsToDisplay, fileFormat);
	}

	public static Display cleanAndManageBoldNumber(Display orig, FileFormat fileFormat) {
		if (orig.size() == 0 || orig.get(0) instanceof MessageNumber == false) {
			return orig;
		}
		Display result = Display.empty();
		for (int i = 0; i < orig.size(); i++) {
			CharSequence element = orig.get(i);
			if (i == 1) {
				element = removeTagAndManageBoldNumber(orig.get(0).toString(), fileFormat) + " " + element;
			}
			if (i != 0) {
				result = result.add(element);
			}
		}
		return result;
	}

	private static final Pattern pattern = Pattern.compile("\\<b\\>([0-9]+)\\</b\\>");

	private static String removeTagAndManageBoldNumber(String s, FileFormat fileFormat) {
		if (fileFormat == FileFormat.UTXT) {
			final Matcher matcher = pattern.matcher(s);
			final StringBuffer result = new StringBuffer(); // Can't be switched to StringBuilder in order to support
															// Java 8
			while (matcher.find()) {
				final String num = matcher.group(1);
				final String replace = StringUtils.toInternalBoldNumber(num);
				matcher.appendReplacement(result, Matcher.quoteReplacement(replace));
			}
			matcher.appendTail(result);
			s = result.toString();
		}
		return s.replaceAll("\\<[^<>]+\\>", "");
	}

	public void drawU(UGraphic ug, Area area, Context2D context) {
		if (config.isHidden()) {
			return;
		}
		final XDimension2D dimensionToUse = area.getDimensionToUse();
		final UmlCharArea charArea = ((UGraphicTxt) ug).getCharArea();
		final int width = (int) dimensionToUse.getWidth();
		final int height = (int) dimensionToUse.getHeight();
		final int textWidth = StringUtils.getWcWidth(stringsToDisplay);

		final int yarrow = height - 2;
		charArea.drawHLine(fileFormat == FileFormat.UTXT ? '\u2500' : '-', yarrow, 1, width);
		if (config.isDotted()) {
			for (int i = 1; i < width; i += 2) {
				charArea.drawChar(' ', i, yarrow);
			}
		}

		if (config.getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			charArea.drawChar('>', width - 1, yarrow);
		} else if (config.getArrowDirection() == ArrowDirection.RIGHT_TO_LEFT_REVERSE) {
			charArea.drawChar('<', 1, yarrow);
		} else if (config.getArrowDirection() == ArrowDirection.BOTH_DIRECTION) {
			charArea.drawChar('>', width - 1, yarrow);
			charArea.drawChar('<', 1, yarrow);
		} else {
			throw new UnsupportedOperationException();
		}
		// final int position = Math.max(0, (width - textWidth) / 2);

		if (fileFormat == FileFormat.UTXT) {
			charArea.drawStringsLRUnicode(stringsToDisplay.asList(), (width - textWidth) / 2, 0);
		} else {
			charArea.drawStringsLRSimple(stringsToDisplay.asList(), (width - textWidth) / 2, 0);
		}
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return StringUtils.getHeight(stringsToDisplay) + 2;
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		final int width = StringUtils.getWcWidth(stringsToDisplay) + 2;
		if (maxAsciiMessageLength > 0) {
			return Math.min(maxAsciiMessageLength, width);
		}
		return width;
	}

	public XPoint2D getStartPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		return new XPoint2D(0, 0);
	}

	public XPoint2D getEndPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		return new XPoint2D(0, 0);
	}

	public double getPaddingY() {
		throw new UnsupportedOperationException();
	}

	public double getYPoint(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

	public double getPosArrow(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

}
