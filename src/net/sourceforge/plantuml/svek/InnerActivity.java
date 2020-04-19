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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public final class InnerActivity extends AbstractTextBlock implements IEntityImage {

	private final IEntityImage im;
	private final HColor borderColor;
	private final double shadowing;
	private final HColor backColor;

	public InnerActivity(final IEntityImage im, HColor borderColor, HColor backColor, double shadowing) {
		this.im = im;
		this.backColor = backColor;
		this.borderColor = borderColor;
		this.shadowing = shadowing;
	}

	public final static double THICKNESS_BORDER = 1.5;

	public void drawU(UGraphic ug) {
		final Dimension2D total = calculateDimension(ug.getStringBounder());

		ug = ug.apply(backColor.bg()).apply(borderColor)
				.apply(new UStroke(THICKNESS_BORDER));
		final URectangle rect = new URectangle(total.getWidth(), total.getHeight()).rounded(IEntityImage.CORNER);
		rect.setDeltaShadow(shadowing);
		ug.draw(rect);
		ug = ug.apply(new UStroke());
		im.drawU(ug);
	}

	public HColor getBackcolor() {
		return im.getBackcolor();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D img = im.calculateDimension(stringBounder);
		return img;
	}

	public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public boolean isHidden() {
		return im.isHidden();
	}

	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}

}
