/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorate;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileMinWidth extends FtileDecorate {

	private final double minWidth;

	public FtileMinWidth(Ftile tile, double minWidth) {
		super(tile);
		this.minWidth = minWidth;
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final UTranslate change = getUTranslateInternal(stringBounder);
				FtileMinWidth.super.asTextBlock().drawU(ug.apply(change));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return getDimensionInternal(stringBounder);
			}
		};
	}

	private UTranslate getUTranslateInternal(final StringBounder stringBounder) {
		final Dimension2D dimTile = getDimension(stringBounder);
		final Dimension2D dimTotal = getDimensionInternal(stringBounder);
		final UTranslate change = new UTranslate((dimTotal.getWidth() - dimTile.getWidth()) / 2, 0);
		return change;
	}

	private Dimension2D getDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dim = getDimension(stringBounder);
		if (dim.getWidth() < minWidth) {
			return new Dimension2DDouble(minWidth, dim.getHeight());
		}
		return dim;
	}

	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == getFtileDelegated()) {
			return getUTranslateInternal(stringBounder);
		}
		return null;
	}

	private Dimension2D getDimension(StringBounder stringBounder) {
		return super.asTextBlock().calculateDimension(stringBounder);
	}

	private Point2D getPoint(Point2D pt, StringBounder stringBounder) {
		final Dimension2D dim = getDimension(stringBounder);
		if (dim.getWidth() < minWidth) {
			final double diff = minWidth - dim.getWidth();
			return new Point2D.Double(pt.getX() + diff / 2, pt.getY());
		}
		return pt;
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		return getPoint(super.getPointIn(stringBounder), stringBounder);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		return getPoint(super.getPointOut(stringBounder), stringBounder);
	}

}
