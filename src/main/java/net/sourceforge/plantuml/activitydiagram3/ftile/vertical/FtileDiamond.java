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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Hexagon;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.style.ISkinParam;

public class FtileDiamond extends FtileDiamondWIP {

	public FtileDiamond(ISkinParam skinParam, HColor backColor, HColor borderColor, Swimlane swimlane) {
		this(skinParam, backColor, borderColor, swimlane, TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0),
				TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0));
	}

	public FtileDiamond withNorth(TextBlock north) {
		return new FtileDiamond(skinParam(), backColor, borderColor, swimlane, north, south, east, west);
	}

	public FtileDiamond withWest(TextBlock west1) {
		if (west1 == null)
			return this;

		return new FtileDiamond(skinParam(), backColor, borderColor, swimlane, north, south, east, west1);
	}

	public FtileDiamond withEast(TextBlock east1) {
		if (east1 == null)
			return this;

		return new FtileDiamond(skinParam(), backColor, borderColor, swimlane, north, south, east1, west);
	}

	public FtileDiamond withSouth(TextBlock south) {
		return new FtileDiamond(skinParam(), backColor, borderColor, swimlane, north, south, east, west);
	}

	private FtileDiamond(ISkinParam skinParam, HColor backColor, HColor borderColor, Swimlane swimlane, TextBlock north,
			TextBlock south, TextBlock east, TextBlock west) {
		super(null, skinParam, backColor, borderColor, swimlane, north, south, east, west);
	}

	public void drawU(UGraphic ug) {

		final double suppY1 = north.calculateDimension(ug.getStringBounder()).getHeight();
		ug = ug.apply(UTranslate.dy(suppY1));
		ug.apply(borderColor).apply(getThickness(getStyle())).apply(backColor.bg()).draw(Hexagon.asPolygon(shadowing));
		// final Dimension2D dimNorth = north.calculateDimension(ug.getStringBounder());
		north.drawU(ug.apply(new UTranslate(Hexagon.hexagonHalfSize * 1.5, -suppY1)));

		// final Dimension2D dimSouth = south.calculateDimension(ug.getStringBounder());
		south.drawU(ug.apply(new UTranslate(Hexagon.hexagonHalfSize * 1.5, 2 * Hexagon.hexagonHalfSize)));
		// south.drawU(ug.apply(new UTranslate(-(dimSouth.getWidth() - 2 *
		// Diamond.diamondHalfSize) / 2,
		// 2 * Diamond.diamondHalfSize)));

		final XDimension2D dimWeat1 = west.calculateDimension(ug.getStringBounder());
		west.drawU(ug.apply(new UTranslate(-dimWeat1.getWidth(), -dimWeat1.getHeight() + Hexagon.hexagonHalfSize)));

		final XDimension2D dimEast1 = east.calculateDimension(ug.getStringBounder());
		east.drawU(
				ug.apply(new UTranslate(Hexagon.hexagonHalfSize * 2, -dimEast1.getHeight() + Hexagon.hexagonHalfSize)));
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final double suppY1 = north.calculateDimension(stringBounder).getHeight();
		final XDimension2D dim = new XDimension2D(Hexagon.hexagonHalfSize * 2, Hexagon.hexagonHalfSize * 2 + suppY1);
		return new FtileGeometry(dim, dim.getWidth() / 2, suppY1, dim.getHeight());
	}

	public Ftile withWestAndEast(TextBlock tb1, TextBlock tb2) {
		return withWest(tb1).withEast(tb2);
	}

	public double getWestEastLabelHeight(StringBounder stringBounder) {
		final XDimension2D dimEast = east.calculateDimension(stringBounder);
		final XDimension2D dimWest = west.calculateDimension(stringBounder);
		return Math.max(dimEast.getHeight(), dimWest.getHeight());
	}

	public double getEastLabelWidth(StringBounder stringBounder) {
		final XDimension2D dimEast = east.calculateDimension(stringBounder);
		return dimEast.getWidth();
	}

	public double getSouthLabelHeight(StringBounder stringBounder) {
		final XDimension2D dimSouth = south.calculateDimension(stringBounder);
		return dimSouth.getHeight();
	}

}
