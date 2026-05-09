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
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

public class ComponentRoseParticipant extends AbstractTextualComponent {

	private final HColor back;
	private final HColor foregroundColor;
	private final double deltaShadow;
	private final double roundCorner;
	private final double diagonalCorner;
	private final UStroke stroke;
	private final double minWidth;
	private final boolean collections;
	private final ClockwiseTopRightBottomLeft padding;
	private final ClockwiseTopRightBottomLeft margin;

	public ComponentRoseParticipant(Style style, Style stereo, Display stringsToDisplay, ISkinParam skinParam,
			double minWidth, boolean collections, ClockwiseTopRightBottomLeft padding,
			ClockwiseTopRightBottomLeft margin) {
		super(style, stereo, LineBreakStrategy.NONE, style.getPadding(), skinParam, stringsToDisplay, false);

		this.roundCorner = getRoundCorner();
		this.diagonalCorner = getDiagonalCorner();
		final Fashion biColor = getSymbolContext();
		this.stroke = getStroke();

		this.padding = padding;
		this.margin = margin;
		this.minWidth = minWidth;
		this.collections = collections;
		this.back = biColor.getBackColor();
		this.deltaShadow = biColor.getDeltaShadow();
		this.foregroundColor = biColor.getForeColor();
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();

		// Margin: empty space outside the box
		ug = ug.apply(new UTranslate(margin.getLeft(), margin.getTop()));

		if (foregroundColor != null)
			ug = ug.apply(foregroundColor);
		if (back != null)
			ug = ug.apply(back.bg());
		ug = ug.apply(stroke);

		// Padding: space inside the box, between border and text
		final double boxWidth = getTextWidth(stringBounder);
		final double boxHeight = getTextHeight(stringBounder);

		final Shadowable rect = URectangle.build(boxWidth, boxHeight).rounded(roundCorner)
				.diagonalCorner(diagonalCorner);
		rect.setDeltaShadow(deltaShadow);
		if (collections) {
			ug.apply(UTranslate.dx(getDeltaCollection())).draw(rect);
			ug = ug.apply(UTranslate.dy(getDeltaCollection()));
		}
		ug.draw(rect);
		ug = ug.apply(UStroke.simple());

		// Text: centered inside the box, offset by padding
		final TextBlock textBlock = getTextBlock();
		// textBlock.drawU(ug.apply(new UTranslate(padding.getLeft() +
		// suppWidth(stringBounder) / 2, padding.getTop())));
		textBlock.drawU(ug.apply(padding.getTranslate()));
	}

	private double getDeltaCollection() {
		if (collections)
			return 4;

		return 0;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + margin.getTop() + margin.getBottom() + deltaShadow + 1
				+ getDeltaCollection();
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + margin.getLeft() + margin.getRight() + deltaShadow + getDeltaCollection();
	}

	@Override
	protected double getPureTextWidth(StringBounder stringBounder) {
		return Math.max(super.getPureTextWidth(stringBounder), minWidth);
	}

//	private final double suppWidth(StringBounder stringBounder) {
//		return getPureTextWidth(stringBounder) - super.getPureTextWidth(stringBounder);
//	}

}
