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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorate;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileMinWidthCentered extends FtileDecorate {

	private final double minWidth;
	private FtileGeometry calculateDimensionInternal;

	public FtileMinWidthCentered(Ftile tile, double minWidth) {
		super(tile);
		this.minWidth = minWidth;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final UTranslate change = getUTranslateInternal(stringBounder);
		super.drawU(ug.apply(change));
	}

	@Override
	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		if (calculateDimensionInternal == null) {
			calculateDimensionInternal = calculateDimensionSlow(stringBounder);
		}
		return calculateDimensionInternal;
	}

	private FtileGeometry calculateDimensionSlow(StringBounder stringBounder) {
		final FtileGeometry geo = super.calculateDimension(stringBounder);
		final double left = getPoint2(geo.getLeft(), stringBounder);
		if (geo.hasPointOut() == false) {
			return new FtileGeometry(getDimensionInternal(stringBounder), left, geo.getInY());
		}
		return new FtileGeometry(getDimensionInternal(stringBounder), left, geo.getInY(), geo.getOutY());
	}

	private Dimension2D getDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dim = getFtileDelegated().calculateDimension(stringBounder);
		if (dim.getWidth() < minWidth) {
			return new Dimension2DDouble(minWidth, dim.getHeight());
		}
		return dim;
	}

	private UTranslate getUTranslateInternal(final StringBounder stringBounder) {
		final Dimension2D dimTile = getFtileDelegated().calculateDimension(stringBounder);
		final Dimension2D dimTotal = getDimensionInternal(stringBounder);
		final UTranslate change = UTranslate.dx((dimTotal.getWidth() - dimTile.getWidth()) / 2);
		return change;
	}

	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == getFtileDelegated()) {
			return getUTranslateInternal(stringBounder);
		}
		return null;
	}

	private double getPoint2(double x, StringBounder stringBounder) {
		final Dimension2D dim = getFtileDelegated().calculateDimension(stringBounder);
		if (dim.getWidth() < minWidth) {
			final double diff = minWidth - dim.getWidth();
			return x + diff / 2;
		}
		return x;
	}

}
