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
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileMarged extends AbstractFtile {

	private final Ftile tile;
	private final double marge;

	public FtileMarged(Ftile tile, double marge) {
		super(tile.shadowing());
		this.tile = tile;
		this.marge = marge;
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


	public Point2D getPointIn(StringBounder stringBounder) {
		final Point2D p = tile.getPointIn(stringBounder);
		return new UTranslate(marge, 0).getTranslated(p);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		final Point2D p = tile.getPointOut(stringBounder);
		return new UTranslate(marge, 0).getTranslated(p);
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				ug.apply(new UTranslate(marge, 0)).draw(tile);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return Dimension2DDouble.delta(tile.asTextBlock().calculateDimension(stringBounder), 2 * marge, 0);
			}
		};
	}

	public boolean isKilled() {
		return tile.isKilled();
	}

}
