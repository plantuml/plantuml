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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidthCentered;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside2;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.svek.ConditionStyle;

class FtileSwitch extends AbstractFtile {
    // ::remove folder when __HAXE__

	private final double xSeparation = 20;

	private final List<Ftile> tiles;

	private final Rainbow arrowColor;

	private FtileSwitch(List<Double> inlabelSizes, List<Ftile> tiles, Rainbow arrowColor) {
		super(tiles.get(0).skinParam());
		this.tiles = new ArrayList<>(tiles);
		this.arrowColor = arrowColor;

	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<>();
		if (getSwimlaneIn() != null)
			result.add(getSwimlaneIn());

		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return tiles.get(0).getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	static Ftile create(Swimlane swimlane, HColor borderColor, HColor backColor, Rainbow arrowColor,
			FtileFactory ftileFactory, ConditionStyle conditionStyle, List<Branch> thens, FontConfiguration fcArrow,
			LinkRendering topInlinkRendering, LinkRendering afterEndwhile, FontConfiguration fcTest) {
		Objects.requireNonNull(afterEndwhile);
		final List<Ftile> tiles = new ArrayList<>();

		for (Branch branch : thens)
			tiles.add(new FtileMinWidthCentered(branch.getFtile(), 30));

		List<Double> inlabelSizes = new ArrayList<>();
		for (Branch branch : thens) {
			final TextBlock tb1 = branch.getDisplayPositive().create(fcArrow, HorizontalAlignment.LEFT,
					ftileFactory.skinParam());
			final TextBlock tbTest = branch.getLabelTest().create(fcTest,
					ftileFactory.skinParam().getDefaultTextAlignment(HorizontalAlignment.LEFT),
					ftileFactory.skinParam());
			final HColor diamondColor = branch.getColor() == null ? backColor : branch.getColor();

			FtileDiamondInside2 diamond = new FtileDiamondInside2(tbTest, branch.skinParam(), diamondColor, borderColor,
					swimlane);
			TextBlock tbInlabel = null;
			if (Display.isNull(branch.getInlabel())) {
				inlabelSizes.add(0.0);
			} else {
				tbInlabel = branch.getInlabel().create(fcArrow, HorizontalAlignment.LEFT, ftileFactory.skinParam());
				inlabelSizes.add(tbInlabel.calculateDimension(ftileFactory.getStringBounder()).getWidth());
				diamond = diamond.withWest(tbInlabel);
			}
			diamond = diamond.withNorth(tb1);
		}

		return new FtileSwitch(inlabelSizes, tiles, arrowColor);

	}

	@Override
	public Collection<Ftile> getMyChildren() {
		final List<Ftile> result = new ArrayList<>(tiles);
		return Collections.unmodifiableList(result);
	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (tiles.contains(child))
			return getTranslate1(child, stringBounder);

		throw new UnsupportedOperationException();
	}

	private UTranslate getTranslate1(Ftile tile, StringBounder stringBounder) {
		double x1 = 0;
		for (Ftile candidate : tiles) {
			final FtileGeometry dim1 = candidate.calculateDimension(stringBounder);
			if (candidate == tile)
				return new UTranslate(x1, 25);

			x1 += dim1.getWidth() + xSeparation;
		}
		throw new IllegalArgumentException();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (Ftile tile : tiles)
			ug.apply(getTranslate1(tile, stringBounder)).draw(tile);

	}

	private FtileGeometry calculateDimensionInternal(StringBounder stringBounder) {
		XDimension2D result = new XDimension2D(0, 0);
		for (Ftile couple : tiles)
			result = result.mergeLR(couple.calculateDimension(stringBounder));

		result = result.delta(xSeparation * (tiles.size() - 1), 100);

		return new FtileGeometry(result, result.getWidth() / 2, 0);
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final XDimension2D dimTotal = calculateDimensionInternal(stringBounder);

		final List<Ftile> all = new ArrayList<>(tiles);
		for (Ftile tmp : all)
			if (tmp.calculateDimension(stringBounder).hasPointOut())
				return new FtileGeometry(dimTotal, dimTotal.getWidth() / 2, 0, dimTotal.getHeight());

		return new FtileGeometry(dimTotal, dimTotal.getWidth() / 2, 0);

	}

}
