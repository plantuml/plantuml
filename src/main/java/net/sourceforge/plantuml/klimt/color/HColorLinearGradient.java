/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.klimt.color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.plantuml.klimt.awt.XColor;

/**
 * Represents an SVG-like linear gradient with multiple stops, offsets, and
 * optional stop opacity. This class models the SVG design concepts used by
 * the SAX SVG sprite parser and is later translated into renderer-specific
 * gradient primitives (SVG defs or Java2D paints).
 *
 * <p>Key SVG concepts reflected here:
 * <ul>
 * 	<li>Gradient vector from (x1,y1) to (x2,y2)</li>
 * 	<li>Coordinate system selection (objectBoundingBox vs userSpaceOnUse)</li>
 * 	<li>Multiple color stops with offsets in the 0..1 range</li>
 * 	<li>Per-stop opacity that multiplies the base color alpha</li>
 * 	<li>Spread method for behavior beyond the gradient vector</li>
 * </ul>
 */
public class HColorLinearGradient extends HColor {

	/**
	 * Defines how the gradient behaves outside its vector range.
	 * Mirrors SVG's spreadMethod values.
	 */
	public enum SpreadMethod {
		/**
		 * Default SVG behavior: extend edge colors past the end stops.
		 */
		PAD,
		/**
		 * Reflect the gradient back and forth past the end stops.
		 */
		REFLECT,
		/**
		 * Repeat the gradient in the same direction past the end stops.
		 */
		REPEAT
	}

	/**
	 * A single gradient stop with offset, color, and opacity.
	 *
	 * <p>Offsets are normalized to the 0..1 range by the owning gradient.
	 * Opacity multiplies the base color alpha when rendered.</p>
	 */
	public static class Stop {
		/**
		 * Stop position along the gradient vector (0..1).
		 */
		private final double offset;
		/**
		 * Base color for the stop.
		 */
		private final HColor color;
		/**
		 * Opacity multiplier for this stop (0..1).
		 */
		private final double opacity;

		/**
		 * Creates a stop definition.
		 *
		 * @param offset stop offset in the 0..1 range (values are normalized by the gradient)
		 * @param color stop color
		 * @param opacity stop opacity multiplier (0..1)
		 */
		public Stop(double offset, HColor color, double opacity) {
			this.offset = offset;
			this.color = color;
			this.opacity = opacity;
		}

		/**
		 * Returns the stop offset along the gradient vector.
		 *
		 * @return offset in the 0..1 range
		 */
		public double getOffset() {
			return offset;
		}

		/**
		 * Returns the base color for this stop.
		 *
		 * @return stop color
		 */
		public HColor getColor() {
			return color;
		}

		/**
		 * Returns the stop opacity multiplier.
		 *
		 * @return opacity in the 0..1 range
		 */
		public double getOpacity() {
			return opacity;
		}
	}

	/**
	 * Gradient vector start X.
	 */
	private final double x1;
	/**
	 * Gradient vector start Y.
	 */
	private final double y1;
	/**
	 * Gradient vector end X.
	 */
	private final double x2;
	/**
	 * Gradient vector end Y.
	 */
	private final double y2;
	/**
	 * Whether coordinates are interpreted in user space (true) or object bounding box (false).
	 */
	private final boolean userSpaceOnUse;
	/**
	 * Spread method used outside the gradient vector.
	 */
	private final SpreadMethod spreadMethod;
	/**
	 * Normalized, ordered list of gradient stops.
	 */
	private final List<Stop> stops;

	/**
	 * Creates a linear gradient definition.
	 *
	 * @param x1 start X
	 * @param y1 start Y
	 * @param x2 end X
	 * @param y2 end Y
	 * @param userSpaceOnUse true for userSpaceOnUse, false for objectBoundingBox
	 * @param spreadMethod spread method (defaults to PAD if null)
	 * @param stops stop list (normalized, clamped, and ordered internally)
	 */
	public HColorLinearGradient(double x1, double y1, double x2, double y2, boolean userSpaceOnUse,
			SpreadMethod spreadMethod, List<Stop> stops) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.userSpaceOnUse = userSpaceOnUse;
		this.spreadMethod = spreadMethod == null ? SpreadMethod.PAD : spreadMethod;
		this.stops = normalizeStops(stops);
	}

	/**
	 * Normalize stop ordering and offsets.
	 *
	 * <p>Stops are sorted by offset, clamped to 0..1, and de-duplicated
	 * by nudging ties forward with a tiny epsilon. This mirrors SVG's
	 * requirement for non-decreasing stop offsets.</p>
	 *
	 * @param input raw stops
	 * @return immutable normalized list
	 */
	private List<Stop> normalizeStops(List<Stop> input) {
		final List<Stop> sorted = new ArrayList<Stop>(input);
		Collections.sort(sorted, new Comparator<Stop>() {
			public int compare(Stop o1, Stop o2) {
				return Double.compare(o1.getOffset(), o2.getOffset());
			}
		});

		final List<Stop> normalized = new ArrayList<Stop>(sorted.size());
		double last = -1.0;
		for (Stop stop : sorted) {
			double offset = stop.getOffset();
			if (offset < 0.0)
				offset = 0.0;
			if (offset > 1.0)
				offset = 1.0;
			if (offset <= last) {
				final double candidate = last + 0.000001;
				offset = candidate > 1.0 ? last : candidate;
			}
			last = offset;
			normalized.add(new Stop(offset, stop.getColor(), stop.getOpacity()));
		}
		return Collections.unmodifiableList(normalized);
	}

	/**
	 * Returns the gradient start X.
	 *
	 * @return start X
	 */
	public double getX1() {
		return x1;
	}

	/**
	 * Returns the gradient start Y.
	 *
	 * @return start Y
	 */
	public double getY1() {
		return y1;
	}

	/**
	 * Returns the gradient end X.
	 *
	 * @return end X
	 */
	public double getX2() {
		return x2;
	}

	/**
	 * Returns the gradient end Y.
	 *
	 * @return end Y
	 */
	public double getY2() {
		return y2;
	}

	/**
	 * Indicates whether coordinates are interpreted in user space.
	 *
	 * @return true for userSpaceOnUse, false for objectBoundingBox
	 */
	public boolean isUserSpaceOnUse() {
		return userSpaceOnUse;
	}

	/**
	 * Returns the configured spread method.
	 *
	 * @return spread method
	 */
	public SpreadMethod getSpreadMethod() {
		return spreadMethod;
	}

	/**
	 * Returns the normalized stop list.
	 *
	 * @return immutable list of stops
	 */
	public List<Stop> getStops() {
		return stops;
	}

	/**
	 * Returns the first stop color.
	 *
	 * <p>This is used as a fallback for APIs that only accept a single
	 * solid color.</p>
	 *
	 * @return color of the first stop
	 */
	public HColor getColor1() {
		return stops.get(0).getColor();
	}

	/**
	 * Converts to a single color by using the first stop color.
	 *
	 * @param mapper color mapper
	 * @return mapped color
	 */
	@Override
	public XColor toColor(ColorMapper mapper) {
		return getColor1().toColor(mapper);
	}

	/**
	 * Returns the opposite of the first stop color.
	 *
	 * @return opposite color of the first stop
	 */
	@Override
	public HColor opposite() {
		return getColor1().opposite();
	}

	/**
	 * Returns whether the first stop color is dark.
	 *
	 * @return true if the first stop color is dark
	 */
	@Override
	public boolean isDark() {
		return getColor1().isDark();
	}

	/**
	 * Returns whether the first stop color is transparent.
	 *
	 * @return true if the first stop color is transparent
	 */
	@Override
	public boolean isTransparent() {
		return getColor1().isTransparent();
	}
}
