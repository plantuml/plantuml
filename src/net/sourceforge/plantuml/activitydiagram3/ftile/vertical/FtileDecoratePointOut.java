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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

public class FtileDecoratePointOut extends FtileDecorate {

	final private double dx;
	final private double dy;

	public FtileDecoratePointOut(final Ftile ftile, final double dx, double dy) {
		super(ftile);
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public Point2D getPointOut(StringBounder stringBounder) {
		final Point2D pt = super.getPointOut(stringBounder);
		return new Point2D.Double(pt.getX() + dx, pt.getY() + dy);
	}
	
	@Override
	public TextBlock asTextBlock() {
		final TextBlock result = super.asTextBlock();
		return TextBlockUtils.withMargin(result, 0, 0, dx, dy);
	}

}
