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
package net.sourceforge.plantuml.activitydiagram4.planner;

import net.sourceforge.plantuml.activitydiagram4.RealBand;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.creole.Display;

/**
 * A swimlane as a native layout citizen: a named, ordered {@link RealBand}
 * on the CROSS axis, plus its presentation attributes (title, colors).
 * <p>
 * Contrast with diagram3's Swimlane, which is a mutable bag filled during
 * post-processing (setTranslate, setWidth, setMinMax computed by LimitFinder
 * redraw passes). Here the band is declared upfront and its geometry emerges
 * from the solve; the lane is immutable after planning.
 * <p>
 * The lane order is static (declaration order), but it is only used for
 * band ordering constraints — never as a geometric proxy for routing
 * decisions, which are made post-solve on real coordinates.
 */
public class Lane implements Comparable<Lane> {

	private final String name;
	private final int order;
	private final Display display;
	private final Colors colors;
	private final RealBand band;

	public Lane(String name, int order, Display display, Colors colors, RealBand band) {
		this.name = name;
		this.order = order;
		this.display = display;
		this.colors = colors;
		this.band = band;
	}

	public final String getName() {
		return name;
	}

	public final int getOrder() {
		return order;
	}

	public final Display getDisplay() {
		return display;
	}

	public final Colors getColors() {
		return colors;
	}

	public final RealBand getBand() {
		return band;
	}

	@Override
	public int compareTo(Lane other) {
		return Integer.compare(this.order, other.order);
	}

	@Override
	public String toString() {
		return name;
	}

}
