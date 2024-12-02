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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Set;

import net.sourceforge.plantuml.abel.SpecificBackcolorable;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.skin.Pragma;

public class Swimlane implements SpecificBackcolorable, Comparable<Swimlane> {

	private final String name;
	private final int order;
	private Display display;

	private UTranslate translate = UTranslate.none();
	private double actualWidth;

	public Swimlane(String name, int order, Pragma pragma) {
		this.name = name;
		this.display = Display.getWithNewlines(pragma, name);
		this.order = order;

	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display label) {
		this.display = label;
	}

	public final UTranslate getTranslate() {
		return translate;
	}

	public final void setTranslate(UTranslate translate) {
		this.translate = translate;
	}

	public final void setWidth(double actualWidth) {
		this.actualWidth = actualWidth;
	}

	public Colors getColors() {
		return colors;
	}

	public void setSpecificColorTOBEREMOVED(ColorType type, HColor color) {
		if (color != null) {
			this.colors = colors.add(type, color);
		}
	}

	private Colors colors = Colors.empty();

	public final double getActualWidth() {
		return actualWidth;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	private MinMax minMax;

	public void setMinMax(MinMax minMax) {
		this.minMax = minMax;

	}

	public MinMax getMinMax() {
		return minMax;
	}

	@Override
	public int compareTo(Swimlane other) {
		return Integer.compare(this.order, other.order);
	}

	public boolean isSmallerThanAllOthers(Set<Swimlane> others) {
		if (others.size() == 1 && others.contains(this))
			return false;
		for (Swimlane sw : others)
			if (sw.compareTo(this) < 0)
				return false;
		return true;
	}
}
