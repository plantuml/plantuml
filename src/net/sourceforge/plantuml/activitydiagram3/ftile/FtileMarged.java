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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileMarged extends AbstractFtile {

	private final Ftile tile;
	private final double margin1;
	private final double margin2;

	public FtileMarged(Ftile tile, double margin1, double margin2) {
		super(tile.skinParam());
		this.tile = tile;
		this.margin1 = margin1;
		this.margin2 = margin2;
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		return Collections.singleton(tile);
		// return tile.getMyChildren();
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return tile.getInLinkRendering();
	}

	@Override
	public LinkRendering getOutLinkRendering() {
		return tile.getOutLinkRendering();
	}

	public Set<Swimlane> getSwimlanes() {
		return tile.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return tile.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return tile.getSwimlaneOut();
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final FtileGeometry orig = tile.calculateDimension(stringBounder);
		return new FtileGeometry(orig.getWidth() + margin1 + margin2, orig.getHeight(), orig.getLeft() + margin1,
				orig.getInY(), orig.getOutY());
	}

	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile) {
			return getTranslate();
		}
		UTranslate result = tile.getTranslateFor(child, stringBounder);
		result = result.compose(getTranslate());
		return result;
	}

	private UTranslate getTranslate() {
		return UTranslate.dx(margin1);
	}

	public void drawU(UGraphic ug) {
		ug.apply(getTranslate()).draw(tile);
	}

}
