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

import net.sourceforge.plantuml.annotation.Fast;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicStencil;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

/**
 * A single drawable field (block) in a {@code packetdiag} diagram.
 * <p>
 * A {@link PacketBlock} represents a rectangular region spanning a given number of bit columns
 * ({@code width}) and a given vertical size ({@code height}), with an optional label rendered inside.
 * The block can be marked as "open" on the left and/or right side to indicate that it is a continuation
 * of the same logical field split across multiple rows.
 * </p>
 * <p>
 * This is a low-level rendering model used by {@link PacketDiagram} when laying out {@code PacketItem}s
 * into a row grid. Actual pixel dimensions are obtained by multiplying bit units by the current scale
 * (bit width/height) and adding margins/padding during shape creation.
 * </p>
 */
public class PacketBlock {

	private static final UStroke openerStroke = new UStroke(5, 5, 1);

	/**
	 * Desired block width unit, this is not the final drawing dimension as margins,
	 * paddings and scaling are not added
	 */
	private final int width;

	/**
	 * Desired block height unit
	 */
	private int height;

	/**
	 * Full label text
	 */
	private final String label;

	private final ISkinParam skinParam;

	private boolean leftOpen = false;

	private boolean rightOpen = false;

	/**
	 * Creates a closed block with the given bit-size and label.
	 *
	 * @param width
	 *            block width in bit units (columns)
	 * @param height
	 *            block height in bit units (rows)
	 * @param label
	 *            label to display inside the block (may be {@code null} depending on caller conventions)
	 * @param skinParam
	 *            skin parameters used to resolve styles, fonts and colors
	 */
	public PacketBlock(int width, int height, String label, ISkinParam skinParam) {
		this.width = width;
		this.height = height;
		this.label = label;
		this.skinParam = skinParam;
	}

	/**
	 * Creates a block with optional open edges to represent a field continued across rows.
	 *
	 * @param width
	 *            block width in bit units (columns)
	 * @param height
	 *            block height in bit units (rows)
	 * @param label
	 *            label to display inside the block
	 * @param skinParam
	 *            skin parameters used to resolve styles, fonts and colors
	 * @param leftOpen
	 *            {@code true} if the left border is "open" (continuation from a previous row)
	 * @param rightOpen
	 *            {@code true} if the right border is "open" (continues on the next row)
	 */
	public PacketBlock(int width, int height, String label, ISkinParam skinParam, boolean leftOpen, boolean rightOpen) {
		this.width = width;
		this.height = height;
		this.label = label;
		this.skinParam = skinParam;
		this.leftOpen = leftOpen;
		this.rightOpen = rightOpen;
	}

	/**
	 * Marks whether the left edge should be rendered as open (continuation indicator).
	 *
	 * @param leftOpen {@code true} to open the left edge, {@code false} to close it
	 */
	public void openLeft(boolean leftOpen) {
		this.leftOpen = leftOpen;
	}

	/**
	 * Marks whether the right edge should be rendered as open (continuation indicator).
	 *
	 * @param rightOpen {@code true} to open the right edge, {@code false} to close it
	 */
	public void openRight(boolean rightOpen) {
		this.rightOpen = rightOpen;
	}

	/**
	 * Returns the block width in bit units.
	 *
	 * @return width in bit units (columns)
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the block height in bit units.
	 *
	 * @return height in bit units (rows)
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the block height in bit units.
	 *
	 * @param height height in bit units (rows)
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Returns the block width in pixels for a given horizontal bit scale.
	 *
	 * @param scale pixels per bit
	 * @return width in pixels
	 */
	public double getDrawWidth(double scale) {
		return width * scale;
	}

	/**
	 * Returns the block height in pixels for a given vertical bit scale.
	 *
	 * @param scale pixels per bit
	 * @return height in pixels
	 */
	public double getDrawHeight(double scale) {
		return height * scale;
	}

	/**
	 * Returns the label text displayed inside this block.
	 *
	 * @return the block label (as provided at construction time)
	 * (And may be empty depending on caller conventions)
	 */
	public String getLabel() {
		return label;
	}

	Style getStyle() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.packetdiagDiagram, SName.rectangle)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	Fashion getFashion() {
		return getStyle().getSymbolContext(skinParam.getIHtmlColorSet());
	}

	private TextBlock getLabelTextBlock(String label) {
		return Display.getWithNewlines(skinParam.getPragma(), label).create8(
				getStyle().getFontConfiguration(skinParam.getIHtmlColorSet()), HorizontalAlignment.CENTER, skinParam,
				CreoleMode.SIMPLE_LINE, LineBreakStrategy.NONE);
	}

	private TextBlock getLabelTextBlockAbbr(StringBounder stringBounder, double bitWidth) {
		UFont font = getStyle().getUFont();
		final String pad = "...";
		XDimension2D padDim = stringBounder.calculateDimension(font, pad);
		XDimension2D labelDim = stringBounder.calculateDimension(font, label);
		double reqWidth = getDrawWidth(bitWidth);
		if (labelDim.getWidth() < reqWidth) {
			return getLabelTextBlock(label);
		}
		// Shrink label char by char, this is technically wrong when there's multiple
		// Unicode code points
		String abbr = label;
		for (int i = label.length(); i > 0; i--) {
			if (labelDim.getWidth() + padDim.getWidth() < reqWidth) {
				break;
			}
			abbr = label.substring(0, i) + pad;
			labelDim = stringBounder.calculateDimension(font, abbr);
		}
		return getLabelTextBlock(abbr);
	}

	TextBlock getShapeTextBlock(StringBounder stringBounder, double bitWidth, double bitHeight) {
		final double vMargin = 10D;
		final double reqWidth = getDrawWidth(bitWidth);
		final Fashion fashion = getFashion();
		final TextBlock label = getLabelTextBlockAbbr(stringBounder, bitWidth);
		final double labelHeight = label.calculateDimension(stringBounder).getHeight();
		final double totalSpare = Math.max(0D, getDrawHeight(bitHeight) - labelHeight - vMargin - vMargin);
		final double topSpare = totalSpare / 2.0;
		final double bottomSpare = totalSpare - topSpare;
		final TextBlock topSpacer = TextBlockUtils.empty(reqWidth, topSpare);
		final TextBlock bottomSpacer = TextBlockUtils.empty(reqWidth, bottomSpare);

		return new TextBlock() {
			@Override
			@Fast
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final double w = reqWidth;
				final double h = topSpare + labelHeight + bottomSpare + vMargin + vMargin;
				return new XDimension2D(w, h);
			}

			@Override
			public void drawU(UGraphic ug) {
				final XDimension2D dim = calculateDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, dim);
				ug = fashion.apply(ug);
				final URectangle rect = URectangle.build(dim.getWidth(), dim.getHeight());
				final Shadowable shape = rect.rounded(fashion.getRoundCorner());
				shape.setDeltaShadow(fashion.getDeltaShadow());
				ug.draw(shape);

				final TextBlock tb = TextBlockUtils.mergeTB(topSpacer, label, HorizontalAlignment.CENTER);
				final TextBlock full = TextBlockUtils.mergeTB(tb, bottomSpacer, HorizontalAlignment.CENTER);
				full.drawU(ug.apply(new UTranslate(0D, vMargin)));
			}
		};
	}
}
