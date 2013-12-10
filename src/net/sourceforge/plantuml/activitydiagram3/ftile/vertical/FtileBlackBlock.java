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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class FtileBlackBlock extends AbstractFtile {

	private final double width;
	private final double height;
	private final HtmlColor colorBar;
	private final Swimlane swimlane;

	public FtileBlackBlock(boolean shadowing, double width, double height, HtmlColor colorBar, Swimlane swimlane) {
		super(shadowing);
		this.height = height;
		this.width = width;
		this.colorBar = colorBar;
		this.swimlane = swimlane;
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		return new Point2D.Double(width / 2, 0);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		return new Point2D.Double(width / 2, height);
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				final URectangle rect = new URectangle(width, height, 5, 5);
				if (shadowing()) {
					rect.setDeltaShadow(3);
				}
				ug.apply(new UChangeColor(colorBar)).apply(new UChangeBackColor(colorBar)).draw(rect);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}
		};
	}

	public boolean isKilled() {
		return false;
	}

	public Set<Swimlane> getSwimlanes() {
		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

}
