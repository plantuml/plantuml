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

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.AbstractEvent;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Participant;

public class EmptyTile extends AbstractTile implements Tile {

	private final double height;
	private final Tile position;
	private final YGauge yGauge;

	public EmptyTile(double height, Tile position, YGauge currentY) {
		super(((AbstractTile) position).getStringBounder(), currentY);
		if (YGauge.USE_ME)
			throw new UnsupportedOperationException();
		this.height = height;
		this.position = position;
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	public void drawU(UGraphic ug) {
	}

	@Override
	public double getPreferredHeight() {
		return height;
	}

	public void addConstraints() {
	}

	public Real getMinX() {
		return position.getMinX();
	}

	public Real getMaxX() {
		return position.getMaxX();
	}

	public Event getEvent() {
		return new AbstractEvent() {
			public boolean dealWith(Participant someone) {
				return false;
			}
		};
	}

}
