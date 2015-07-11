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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class FtileIfWithDiamonds extends FtileIfNude {

	private final double SUPP_WITH = 20;
	protected final Ftile diamond1;
	protected final Ftile diamond2;

	public FtileIfWithDiamonds(Ftile diamond1, Ftile tile1, Ftile tile2, Ftile diamond2, Swimlane in,
			StringBounder stringBounder) {
		super(tile1, tile2, in);
		this.diamond1 = diamond1;
		this.diamond2 = diamond2;
	}

	public int getYdelta1a(StringBounder stringBounder) {
		// if (getSwimlanes().size() > 1 && hasTwoBranches(stringBounder)) {
		if (getSwimlanes().size() > 1) {
			return 20;
		}
		return 10;
		// return hasTwoBranches(stringBounder) ? 6 : 6;
	}

	public int getYdelta1b(StringBounder stringBounder) {
		// if (getSwimlanes().size() > 1 && hasTwoBranches(stringBounder)) {
		if (getSwimlanes().size() > 1) {
			return 10;
		}
		return hasTwoBranches(stringBounder) ? 6 : 0;
	}

	@Override
	protected double withInner(StringBounder stringBounder) {
		final FtileGeometry dim1 = diamond1.calculateDimension(stringBounder);
		return Math.max(super.withInner(stringBounder), dim1.getWidth() + SUPP_WITH);
	}

	@Override
	protected Dimension2D calculateDimensionInternalSlow(StringBounder stringBounder) {
		final FtileGeometry dim1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry dim2 = diamond2.calculateDimension(stringBounder);

		final Dimension2D dimNude = super.calculateDimensionInternalSlow(stringBounder);

		final double width = MathUtils.max(dim1.getWidth(), dim2.getWidth(), dimNude.getWidth());
		final double height = dimNude.getHeight() + dim1.getHeight() + dim2.getHeight() + getYdelta1a(stringBounder)
				+ getYdelta1b(stringBounder);
		return new Dimension2DDouble(width, height);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		super.drawU(ug);
		ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);
	}

	@Override
	protected UTranslate getTranslate1(StringBounder stringBounder) {
		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		return super.getTranslate1(stringBounder).compose(
				new UTranslate(0, dimDiamond1.getHeight() + getYdelta1a(stringBounder)));
	}

	@Override
	protected UTranslate getTranslate2(StringBounder stringBounder) {
		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		return super.getTranslate2(stringBounder).compose(
				new UTranslate(0, dimDiamond1.getHeight() + getYdelta1a(stringBounder)));
	}

	protected UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final double y1 = 0;
		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double x1 = getLeft(stringBounder) - dimDiamond1.getWidth() / 2;
		return new UTranslate(x1, y1);
	}

	protected UTranslate getTranslateDiamond2(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final double y2 = dimTotal.getHeight() - dimDiamond2.getHeight();
		final double x2 = getLeft(stringBounder) - dimDiamond2.getWidth() / 2;
		return new UTranslate(x2, y2);
	}

}
