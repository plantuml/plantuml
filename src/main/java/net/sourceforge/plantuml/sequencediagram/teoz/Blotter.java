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

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public class Blotter implements UDrawable {

	private final XDimension2D dim;
	private final HColor defaultBackcolor;
	private final double round;
	private HColor last;
	private final SortedMap<Double, HColor> changes = new TreeMap<>();

	public Blotter(XDimension2D dim, HColor defaultBackcolor, double round) {
		if (defaultBackcolor == null)
			defaultBackcolor = HColors.transparent();
		this.round = round;
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
		int i = 0;
		for (Entry<Double, HColor> ent : changes.entrySet()) {
			if (current.isTransparent() == false) {
				final UShape rect = getRectangleBackground(i, ent.getKey() - y);
				ug.apply(current).apply(current.bg()).apply(UTranslate.dy(y)).draw(rect);
			}
			y = ent.getKey();
			current = ent.getValue();
			i++;
		}
	}

	private UShape getRectangleBackground(int i, double height) {
		final double width = dim.getWidth();
		if (round == 0)
			return URectangle.build(width, height);

		if (changes.size() == 1)
			return URectangle.build(width, height).rounded(round);

		if (i == 0) {
			final UPath result = UPath.none();
			result.moveTo(round / 2, 0);
			result.lineTo(width - round / 2, 0);
			result.arcTo(round / 2, round / 2, 0, 0, 1, width, round / 2);
			result.lineTo(width, height);
			result.lineTo(0, height);
			result.lineTo(0, round / 2);
			result.arcTo(round / 2, round / 2, 0, 0, 1, round / 2, 0);
			result.closePath();
			return result;
		}
		if (i == changes.size() - 1) {
			final UPath result = UPath.none();
			result.moveTo(0, 0);
			result.lineTo(width, 0);
			result.lineTo(width, height - round / 2);
			result.arcTo(round / 2, round / 2, 0, 0, 1, width - round / 2, height);
			result.lineTo(round / 2, height);
			result.arcTo(round / 2, round / 2, 0, 0, 1, 0, height - round / 2);
			result.lineTo(0, 0);
			result.closePath();
			return result;

		}
		return URectangle.build(width, height);
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
