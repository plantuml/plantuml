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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileSwitchWithDiamonds extends FtileSwitchNude {

	private static final double SUPP15 = 15;

	static enum Mode {
		BIG_DIAMOND, SMALL_DIAMOND
	};

	protected final Ftile diamond1;
	protected final Ftile diamond2;
	protected final List<Branch> branches;
	private Mode mode;
	private final double w13;
	private final double w9;

	public FtileSwitchWithDiamonds(List<Ftile> tiles, List<Branch> branches, Swimlane in, Ftile diamond1,
			Ftile diamond2, StringBounder stringBounder) {
		super(tiles, in);
		this.branches = branches;
		this.diamond1 = diamond1;
		this.diamond2 = diamond2;

		w13 = diamond1.calculateDimension(stringBounder).getWidth()
				- tiles.get(0).calculateDimension(stringBounder).getRight()
				- tiles.get(tiles.size() - 1).calculateDimension(stringBounder).getLeft();
		w9 = getW9(stringBounder);
		if (w13 > w9) {
			mode = Mode.BIG_DIAMOND;
		} else {
			mode = Mode.SMALL_DIAMOND;
		}
	}

	private double getW9(StringBounder stringBounder) {
		double result = 0;
		for (int i = 1; i < tiles.size() - 1; i++) {
			result += tiles.get(i).calculateDimension(stringBounder).getWidth();
		}
		return result;
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		final Collection<Ftile> result = new ArrayList<Ftile>(super.getMyChildren());
		result.add(diamond1);
		result.add(diamond2);
		return Collections.unmodifiableCollection(result);
	}

	public double getYdelta1a(StringBounder stringBounder) {
		return 20;
	}

	public double getYdelta1b(StringBounder stringBounder) {
		return 10;
	}

	@Override
	protected FtileGeometry calculateDimensionInternalSlow(StringBounder stringBounder) {
		final FtileGeometry dim1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry dim2 = diamond2.calculateDimension(stringBounder);

		final double nudeHeight = super.calculateDimensionInternalSlow(stringBounder).getHeight();

		if (mode == Mode.BIG_DIAMOND) {
			final double height = dim1.getHeight() + nudeHeight + dim2.getHeight() + getYdelta1a(stringBounder)
					+ getYdelta1b(stringBounder);
			final FtileGeometry tile0 = tiles.get(0).calculateDimension(stringBounder);
			final double width = tile0.getWidth() + SUPP15 + w13 + SUPP15
					+ +tiles.get(tiles.size() - 1).calculateDimension(stringBounder).getWidth();
			return new FtileGeometry(width, height, tile0.getLeft() + SUPP15 + dim1.getLeft(), 0, height);

		} else {
			final FtileGeometry dimNude = super.calculateDimensionInternalSlow(stringBounder);
			final FtileGeometry all = dim1.appendBottom(dimNude).appendBottom(dim2);
			return all.addDim(0, getYdelta1a(stringBounder) + getYdelta1b(stringBounder));
		}

	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		if (mode == Mode.BIG_DIAMOND) {
			for (Ftile tile : tiles) {
				tile.drawU(ug.apply(getTranslateOf(tile, stringBounder)));
			}
		} else {
			super.drawU(ug.apply(getTranslateMain(stringBounder)));
		}
		ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);
	}

	protected UTranslate getTranslateOf(Ftile tile, StringBounder stringBounder) {
		final UTranslate main = getTranslateMain(stringBounder);
		if (mode == Mode.BIG_DIAMOND) {
			double dx = 0;
			final double suppx = (w13 - w9) / (tiles.size() - 1);
			for (int i = 0; i < tiles.size() - 1; i++) {
				if (tile == tiles.get(i)) {
					return main.compose(UTranslate.dx(dx));
				}
				dx += tiles.get(i).calculateDimension(stringBounder).getWidth() + suppx;

			}
			if (tile == tiles.get(tiles.size() - 1)) {
				final double dx9 = tiles.get(0).calculateDimension(stringBounder).getWidth() + w13 + SUPP15 + SUPP15;
				return main.compose(UTranslate.dx(dx9));
			}
			throw new IllegalArgumentException();

		}
		return getTranslateNude(tile, stringBounder).compose(main);
	}

	protected UTranslate getTranslateMain(StringBounder stringBounder) {
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double dy1 = dimDiamond1.getHeight() + getYdelta1a(stringBounder);
		return UTranslate.dy(dy1);
	}

	protected UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final double y1 = 0;
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft();
		return new UTranslate(x1, y1);
	}

	protected UTranslate getTranslateDiamond2(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final double y2 = dimTotal.getHeight() - dimDiamond2.getHeight();
		final double x2 = dimTotal.getLeft() - dimDiamond2.getWidth() / 2;
		return new UTranslate(x2, y2);
	}

	protected TextBlock getLabelPositive(Branch branch) {
		final FontConfiguration fcArrow = new FontConfiguration(skinParam(), FontParam.ARROW, null);
		return branch.getLabelPositive().create7(fcArrow, HorizontalAlignment.LEFT, skinParam(), CreoleMode.SIMPLE_LINE);
	}

}
