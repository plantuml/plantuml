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

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileDecorateInLabel extends FtileDecorate {

	final private double xl;
	final private double yl;

	public FtileDecorateInLabel(Ftile ftile, Dimension2D dim) {
		this(ftile, dim.getWidth(), dim.getHeight());
	}

	private FtileDecorateInLabel(final Ftile ftile, double xl, double yl) {
		super(ftile);
		this.xl = xl;
		this.yl = yl;
	}

	@Override
	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		FtileGeometry result = super.calculateDimension(stringBounder);
		result = result.addTop(yl);
		final double missing = xl - result.getRight();
		if (missing > 0)
			result = result.incRight(missing);

		return result;
	}

	@Override
	public void drawU(UGraphic ug) {
		super.drawU(ug.apply(UTranslate.dy(yl)));
	}

}
