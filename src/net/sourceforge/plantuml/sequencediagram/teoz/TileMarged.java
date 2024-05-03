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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.activitydiagram3.ftile.RectangleCoordinates;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;

public class TileMarged extends AbstractTile implements Tile {

	private final Tile tile;
	private RectangleCoordinates rectangleCoordinates = new RectangleCoordinates(0.0, 0.0, 0.0, 0.0);

	public TileMarged(Tile tile, double x1, double x2, double y1, double y2) {
		super(((AbstractTile) tile).getStringBounder());
		this.tile = tile;
		this.rectangleCoordinates.setX1(x1);
		this.rectangleCoordinates.setX2(x2);
		this.rectangleCoordinates.setY1(y1);
		this.rectangleCoordinates.setY2(y2);
	}

	public void drawU(UGraphic ug) {
		((UDrawable) tile).drawU(ug.apply(new UTranslate(rectangleCoordinates.getX1(), rectangleCoordinates.getY1())));

	}

	public double getPreferredHeight() {
		return tile.getPreferredHeight() + rectangleCoordinates.getY1() + rectangleCoordinates.getY2();
	}

	public void addConstraints() {
		tile.addConstraints();
	}

	public Real getMinX() {
		return tile.getMinX();
	}

	public Real getMaxX() {
		return tile.getMaxX().addFixed(rectangleCoordinates.getX1() + rectangleCoordinates.getX2());
	}

	public Event getEvent() {
		return tile.getEvent();
	}

}
