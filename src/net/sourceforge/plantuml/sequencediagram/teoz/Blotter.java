/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class Blotter implements UDrawable {

	private final XDimension2D dim;
	private final HColor defaultBackcolor;
	private HColor last;
	private final SortedMap<Double, HColor> changes = new TreeMap<>();

	public Blotter(XDimension2D dim, HColor defaultBackcolor) {
		if (defaultBackcolor == null)
			defaultBackcolor = HColors.transparent();
		this.dim = dim;
		this.defaultBackcolor = defaultBackcolor;
		this.last = defaultBackcolor;
	}

	@Override
	public String toString() {
		return "" + dim + " " + defaultBackcolor;
	}

	@Override
	public void drawU(UGraphic ug) {
		HColor current = defaultBackcolor;
		double y = 0;
		for (Entry<Double, HColor> ent : changes.entrySet()) {
			if (current.isTransparent() == false) {
				final URectangle rect = new URectangle(dim.getWidth(), ent.getKey() - y);
				ug.apply(current).apply(current.bg()).apply(UTranslate.dy(y)).draw(rect);
			}
			y = ent.getKey();
			current = ent.getValue();
		}
	}

	public void closeChanges() {
		changes.put(dim.getHeight(), defaultBackcolor);
	}

	public void addChange(double ypos, HColor color) {
		if (color == null)
			color = HColors.transparent();
		if (color.equals(last))
			return;
		changes.put(ypos, color);
		last = color;
	}

}
