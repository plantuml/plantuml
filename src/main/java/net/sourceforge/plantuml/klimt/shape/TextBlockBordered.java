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
package net.sourceforge.plantuml.klimt.shape;

import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.SheetBlock2;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;

public class TextBlockBordered extends AbstractTextBlock implements TextBlock {
    // ::remove file when __HAXE__

	private final double cornersize;
	private final HColor backgroundColor;
	private final HColor borderColor;
	private final double top;
	private final double right;
	private final double bottom;
	private final double left;
	private final UStroke stroke;
	private final boolean withShadow;
	private final String id;
	private final TextBlock textBlock;

	TextBlockBordered(TextBlock textBlock, UStroke stroke, HColor borderColor, HColor backgroundColor,
			double cornersize, ClockwiseTopRightBottomLeft margins, String id) {
		this.top = margins.getTop();
		this.right = margins.getRight();
		this.bottom = margins.getBottom();
		this.left = margins.getLeft();
		this.cornersize = cornersize;
		this.textBlock = textBlock;
		this.withShadow = false;
		this.stroke = stroke;
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
		this.id = id;
	}

	private double getTextHeight(StringBounder stringBounder) {
		final XDimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getHeight() + top + bottom;
	}

	private double getPureTextWidth(StringBounder stringBounder) {
		final XDimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getWidth();
	}

	private double getTextWidth(StringBounder stringBounder) {
		return getPureTextWidth(stringBounder) + left + right;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final double height = getTextHeight(stringBounder);
		final double width = getTextWidth(stringBounder);
		return new XDimension2D(width + 1, height + 1);
	}

	private UGraphic applyStroke(UGraphic ug) {
		if (stroke == null)
			return ug;

		return ug.apply(stroke);
	}

	private boolean noBorder() {
		if (stroke == null)
			return false;

		return stroke.getThickness() == 0;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Shadowable polygon = getPolygonNormal(stringBounder);
		final UGraphic ugOriginal = ug;
		if (withShadow)
			polygon.setDeltaShadow(4);

		final HColor back;
		if (backgroundColor == null || backgroundColor.isTransparent()
				|| backgroundColor.equals(ug.getDefaultBackground()))
			back = HColors.none();
		else
			back = backgroundColor;

		HColor color = noBorder() ? back : borderColor;
		if (color == null)
			color = HColors.none();

		if (back.isTransparent() == false || color.isTransparent() == false) {
			ug = ug.apply(back.bg());
			ug = ug.apply(color);
			ug = applyStroke(ug);
			ug.draw(polygon);
		}
		TextBlock toDraw = textBlock;
		if (textBlock instanceof SheetBlock2)
			toDraw = ((SheetBlock2) textBlock).enlargeMe(left, right);

		toDraw.drawU(ugOriginal.apply(color).apply(new UTranslate(left, top)));
	}

	private Shadowable getPolygonNormal(final StringBounder stringBounder) {
		final double height = getTextHeight(stringBounder);
		final double width = getTextWidth(stringBounder);
		return URectangle.build(width, height).rounded(cornersize).withCommentAndCodeLine(id, null);
	}

}
