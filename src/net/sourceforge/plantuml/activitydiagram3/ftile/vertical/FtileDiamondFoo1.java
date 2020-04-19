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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class FtileDiamondFoo1 extends AbstractFtile {

	private final HColor backColor;
	private final HColor borderColor;
	private final Swimlane swimlane;
	private final TextBlock label;
	private final TextBlock west;
	private final TextBlock east;
	private final TextBlock north;

	public FtileDiamondFoo1(ISkinParam skinParam, HColor backColor, HColor borderColor, Swimlane swimlane,
			TextBlock label) {
		this(skinParam, backColor, borderColor, swimlane, label, TextBlockUtils.empty(0, 0),
				TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0));
	}

	public FtileDiamondFoo1 withNorth(TextBlock north) {
		return new FtileDiamondFoo1(skinParam(), backColor, borderColor, swimlane, label, north, west, east);
	}

	public FtileDiamondFoo1 withWest(TextBlock west) {
		return new FtileDiamondFoo1(skinParam(), backColor, borderColor, swimlane, label, north, west, east);
	}

	public FtileDiamondFoo1 withEast(TextBlock east) {
		return new FtileDiamondFoo1(skinParam(), backColor, borderColor, swimlane, label, north, west, east);
	}

	private FtileDiamondFoo1(ISkinParam skinParam, HColor backColor, HColor borderColor, Swimlane swimlane,
			TextBlock label, TextBlock north, TextBlock west, TextBlock east) {
		super(skinParam);
		this.backColor = backColor;
		this.swimlane = swimlane;
		this.borderColor = borderColor;
		this.label = label;
		this.west = west;
		this.east = east;
		this.north = north;
	}

	public Set<Swimlane> getSwimlanes() {
		if (swimlane == null) {
			return Collections.emptySet();
		}
		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimLabel = label.calculateDimension(stringBounder);
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		ug = ug.apply(borderColor).apply(getThickness()).apply(backColor.bg());
		ug.draw(Diamond.asPolygonFoo1(skinParam().shadowing(null), dimTotal.getWidth(), dimTotal.getHeight()));

		north.drawU(ug.apply(new UTranslate(4 + dimTotal.getWidth() / 2, dimTotal.getHeight())));

		final double lx = (dimTotal.getWidth() - dimLabel.getWidth()) / 2;
		final double ly = (dimTotal.getHeight() - dimLabel.getHeight()) / 2;
		label.drawU(ug.apply(new UTranslate(lx, ly)));

		final Dimension2D dimWeat = west.calculateDimension(stringBounder);
		west.drawU(ug.apply(new UTranslate(-dimWeat.getWidth(), -dimWeat.getHeight() + Diamond.diamondHalfSize)));

		final Dimension2D dimEast = east.calculateDimension(stringBounder);
		east.drawU(ug.apply(new UTranslate(dimTotal.getWidth(), -dimEast.getHeight() + Diamond.diamondHalfSize)));

	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final Dimension2D dim = calculateDimensionInternal(stringBounder);
		return new FtileGeometry(dim, dim.getWidth() / 2, 0, dim.getHeight());
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dimLabel = label.calculateDimension(stringBounder);
		if (dimLabel.getWidth() == 0 || dimLabel.getHeight() == 0) {
			return new Dimension2DDouble(Diamond.diamondHalfSize * 2, Diamond.diamondHalfSize * 2);
		}
		Dimension2D result = dimLabel;
		result = Dimension2DDouble.delta(result, Diamond.diamondHalfSize * 2, Diamond.diamondHalfSize * 2);
		return result;
	}

}
