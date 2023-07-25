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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class FtileIfNude extends FtileDimensionMemoize {

	protected final Ftile tile1;
	protected final Ftile tile2;
	private final Swimlane in;

	protected double xDeltaNote = 0;
	protected double yDeltaNote = 0;
	protected double suppWidthNode = 0;

	FtileIfNude(Ftile tile1, Ftile tile2, Swimlane in) {
		super(tile1.skinParam());
		this.tile1 = tile1;
		this.tile2 = tile2;
		this.in = in;
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		return Arrays.asList(tile1, tile2);
	}

	public boolean hasTwoBranches(StringBounder stringBounder) {
		return tile1.calculateDimension(stringBounder).hasPointOut()
				&& tile2.calculateDimension(stringBounder).hasPointOut();
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<>();
		if (getSwimlaneIn() != null)
			result.add(getSwimlaneIn());

		result.addAll(tile1.getSwimlanes());
		result.addAll(tile2.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return in;
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	protected UTranslate getTranslateBranch1(StringBounder stringBounder) {
		final double x1 = xDeltaNote;
		final double y1 = yDeltaNote;
		return new UTranslate(x1, y1);
	}

	protected UTranslate getTranslateBranch2(StringBounder stringBounder) {
		final XDimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final XDimension2D dim2 = tile2.calculateDimension(stringBounder);

		final double x2 = dimTotal.getWidth() - dim2.getWidth() - suppWidthNode;
		final double y2 = yDeltaNote;
		return new UTranslate(x2, y2);

	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile1)
			return getTranslateBranch1(stringBounder);

		if (child == tile2)
			return getTranslateBranch2(stringBounder);

		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		ug.apply(getTranslateBranch1(stringBounder)).draw(tile1);
		ug.apply(getTranslateBranch2(stringBounder)).draw(tile2);
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		if (tile1.calculateDimension(stringBounder).hasPointOut()
				|| tile2.calculateDimension(stringBounder).hasPointOut())
			return dimTotal;

		return dimTotal.withoutPointOut();
	}

	@Override
	protected FtileGeometry calculateDimensionInternalSlow(StringBounder stringBounder) {
		final FtileGeometry dim1 = tile1.calculateDimension(stringBounder);
		final FtileGeometry dim2 = tile2.calculateDimension(stringBounder);

		final double innerMargin = widthInner(stringBounder);
		final double width = xDeltaNote + dim1.getLeft() + innerMargin + (dim2.getWidth() - dim2.getLeft())
				+ suppWidthNode;

		final XDimension2D dim12 = dim1.mergeLR(dim2);

		final double height = yDeltaNote + dim12.getHeight();
		return new FtileGeometry(width, height, xDeltaNote + dim1.getLeft() + innerMargin / 2, yDeltaNote, height);
	}

	protected double widthInner(StringBounder stringBounder) {
		final FtileGeometry dim1 = tile1.calculateDimension(stringBounder);
		final FtileGeometry dim2 = tile2.calculateDimension(stringBounder);
		return (dim1.getWidth() - dim1.getLeft()) + dim2.getLeft();
	}

}
