/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
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
 * Original Author:  kolulu23
 *
 * 
 */
package net.sourceforge.plantuml.packetdiag;

import java.util.Collections;

import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockMemoized;
import net.sourceforge.plantuml.nwdiag.VerticalLine;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

/**
 * Renders the bit-position scale for {@code packetdiag} diagrams.
 * <p>
 * A {@link PacketIndicator} represents a single tick on the scale (one bit position). Depending on its
 * configuration it can draw:
 * </p>
 * <ul>
 *   <li>a full-height or half-height vertical tick line,</li>
 *   <li>an optional numeric label centered above the tick.</li>
 * </ul>
 * <p>
 * Instances are typically created by {@link PacketDiagram} when building the scale for a given
 * {@code colWidth} and {@code scaleInterval}.
 * </p>
 */
public class PacketIndicator extends TextBlockMemoized {

	/**
	 * Height (in pixels) of a full tick mark.
	 */
	public static final double V_LINE_FULL = 32D;

	/**
	 * Height (in pixels) of a short tick mark (half of {@link #V_LINE_FULL}).
	 */
	public static final double V_LINE_SHORT = V_LINE_FULL / 2;

	private final boolean full;

	private final boolean numbered;

	private final String bitNumber;

	private final Style style;

	private final ISkinParam skinParam;

	/**
	 * Creates a scale indicator for a given bit position.
	 *
	 * @param full
	 *            {@code true} to draw a full-height tick mark, {@code false} to draw a short tick mark
	 * @param numbered
	 *            {@code true} to render the bit index label, {@code false} to draw only the tick
	 * @param bitNumber
	 *            the bit index to display when {@code numbered} is enabled
	 * @param style
	 *            the style used for font/stroke/color resolution
	 * @param skinParam
	 *            the skin parameters used for rendering (colors, fonts, etc.)
	 */
	public PacketIndicator(boolean full, boolean numbered, int bitNumber, Style style, ISkinParam skinParam) {
		this.full = full;
		this.numbered = numbered;
		this.style = style;
		this.bitNumber = bitNumber + "";
		this.skinParam = skinParam;
	}

	/**
	 * Draws this indicator (optional centered number + vertical tick) onto the provided graphics context.
	 * <p>
	 * The number text block is always measured to keep consistent padding/alignment between numbered and
	 * non-numbered indicators.
	 * </p>
	 *
	 * @param ug
	 *            the target graphics context
	 */
	@Override
	public void drawU(UGraphic ug) {
		TextBlock numberTb = numberTb();
		// number dimension is always calculated for consistent padding
		XDimension2D numberDim = numberTb.calculateDimension(ug.getStringBounder());

		double lineOffsetY = full ? 0 : V_LINE_SHORT;
		double lineLength = full ? V_LINE_FULL : V_LINE_SHORT;
		if (numbered) {
			double numberYOffset = full ? 0 : V_LINE_SHORT;
			numberTb.drawU(ug.apply(new UTranslate(-numberDim.getWidth() / 2, numberYOffset)));
		}
		TextBlock vLineTb = vLineTb(lineOffsetY, lineLength);
		vLineTb.drawU(ug.apply(UTranslate.dy(numberDim.getHeight())));
	}

	/**
	 * Computes the indicator dimensions (label + tick).
	 * <p>
	 * Height is the label height plus the tick mark height. Width is derived from the label (when present)
	 * and the stroke thickness used to draw the tick.
	 * </p>
	 *
	 * @param stringBounder
	 *            font metrics provider
	 * @return the bounding box of this indicator
	 */
	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final XDimension2D numberDim = numberTb().calculateDimension(stringBounder);
		double vLineHeight = full ? V_LINE_FULL : V_LINE_SHORT;
		return numberDim.mergeTB(new XDimension2D(0D, vLineHeight));
	}

	TextBlock numberTb() {
		final FontConfiguration fg = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		return createNumberTextBlock(fg, skinParam, bitNumber);
	}

	TextBlock vLineTb(double y, double height) {
		return createVLineTextBlock(style, skinParam, y, height);
	}

	/**
	 * Creates a {@link TextBlock} that draws the numeric label for a given indicator.
	 *
	 * @param fg
	 *            font configuration used to render the label
	 * @param skinParam
	 *            the skin parameters used for rendering
	 * @param s
	 *            the label content (typically the bit index)
	 * @return a text block that renders the label
	 */
	public static TextBlock createNumberTextBlock(FontConfiguration fg, ISkinParam skinParam, CharSequence... s) {
		return Display.create(s).create8(fg, HorizontalAlignment.LEFT, skinParam, CreoleMode.NO_CREOLE,
				LineBreakStrategy.NONE);
	}

	/**
	 * Creates a {@link TextBlock} that draws the vertical tick line of an indicator.
	 *
	 * @param style
	 *            the style used to resolve stroke and color
	 * @param skinParam
	 *            the skin parameters used for rendering
	 * @param y
	 *            vertical start offset (in pixels) for the tick line
	 * @param height
	 *            tick line height (in pixels)
	 * @return a text block that renders the vertical tick line
	 */
	public static TextBlock createVLineTextBlock(Style style, ISkinParam skinParam, double y, double height) {
		final HColor lineColor = style.getSymbolContext(skinParam.getIHtmlColorSet()).getForeColor();
		final UStroke stroke = style.getStroke();
		return new TextBlock() {
			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(stroke.getThickness(), height);
			}

			@Override
			public void drawU(UGraphic ug) {
				ug = ug.apply(lineColor).apply(stroke);
				VerticalLine vLine = new VerticalLine(y, y + height, Collections.emptySet());
				vLine.drawU(ug);
			}
		};
	}
}
