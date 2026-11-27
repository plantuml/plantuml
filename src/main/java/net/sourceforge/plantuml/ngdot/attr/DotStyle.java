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
package net.sourceforge.plantuml.ngdot.attr;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.ngdot.model.DotAttributes;

/**
 * The {@code style} attribute ({@code "filled,dashed"}) together with
 * {@code penwidth}.
 * <p>
 * Supported: {@code filled}, {@code dashed}, {@code dotted}, {@code bold},
 * {@code solid}, {@code invis}, {@code rounded}, {@code diagonals} and the
 * deprecated {@code setlinewidth(n)}. Other styles are ignored.
 */
public final class DotStyle {

	/** Graphviz draws "bold" with a pen of width 2. */
	private static final double BOLD_WIDTH = 2;

	private final Set<String> styles = new HashSet<String>();
	private final Double penwidth;

	private DotStyle(String style, String penwidth) {
		Double width = DotNumbers.parseFirst(penwidth);
		if (style != null) {
			for (String part : style.split(",")) {
				final String s = part.trim().toLowerCase();
				if (s.startsWith("setlinewidth(") && s.endsWith(")")) {
					final Double w = DotNumbers.parseFirst(s.substring("setlinewidth(".length(), s.length() - 1));
					if (w != null && width == null)
						width = w;
				} else if (s.length() > 0) {
					styles.add(s);
				}
			}
		}
		this.penwidth = width;
	}

	public static DotStyle of(DotAttributes attributes) {
		return new DotStyle(attributes.getString("style"), attributes.getString("penwidth"));
	}

	public boolean has(String style) {
		return styles.contains(style);
	}

	public boolean isFilled() {
		return has("filled");
	}

	public boolean isInvisible() {
		return has("invis");
	}

	public boolean isRounded() {
		return has("rounded");
	}

	public boolean isDiagonals() {
		return has("diagonals");
	}

	/**
	 * The stroke to use, starting from the one of the theme: {@code penwidth}
	 * (or {@code bold}) sets the thickness, {@code dashed} and {@code dotted}
	 * set the dashes, as in the SVG output of Graphviz.
	 */
	public UStroke getStroke(UStroke themeStroke) {
		double thickness = themeStroke.getThickness();
		if (penwidth != null && penwidth >= 0)
			thickness = penwidth;
		else if (has("bold"))
			thickness = BOLD_WIDTH;

		if (has("dashed"))
			return new UStroke(5 * Math.max(1, thickness), 2 * Math.max(1, thickness), thickness);
		if (has("dotted"))
			return new UStroke(1 * Math.max(1, thickness), 5 * Math.max(1, thickness), thickness);
		if (has("solid"))
			return UStroke.withThickness(thickness);
		return new UStroke(themeStroke.getDashVisible(), themeStroke.getDashSpace(), thickness);
	}

}
