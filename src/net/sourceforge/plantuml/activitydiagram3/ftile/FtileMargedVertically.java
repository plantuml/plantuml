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

import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorate;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileMargedVertically extends FtileDecorate {

	private final double margin1;
	private final double margin2;

	public FtileMargedVertically(Ftile tile, double margin1, double margin2) {
		super(tile);
		this.margin1 = margin1;
		this.margin2 = margin2;
	}

	public void drawU(UGraphic ug) {
		if (margin1 > 0) {
			ug = ug.apply(UTranslate.dy(margin1));
		}
		ug.draw(getFtileDelegated());
	}

	private FtileGeometry cached;

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		if (cached == null) {
			this.cached = calculateDimensionSlow(stringBounder);
		}
		return this.cached;
	}

	private FtileGeometry calculateDimensionSlow(StringBounder stringBounder) {
		final FtileGeometry orig = getFtileDelegated().calculateDimension(stringBounder);
		return new FtileGeometry(orig.getWidth(), orig.getHeight() + margin1 + margin2, orig.getLeft(), orig.getInY()
				+ margin1, orig.hasPointOut() ? orig.getOutY() + margin1 : orig.getOutY());
	}

}
