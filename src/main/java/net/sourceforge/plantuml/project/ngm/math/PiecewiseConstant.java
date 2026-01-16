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
package net.sourceforge.plantuml.project.ngm.math;

import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * A piecewise constant function defined on a {@link LocalDateTime} axis.
 *
 * <p>
 * This abstraction is intended to model human workload allocation over time.
 * In many planning scenarios, a person's effective contribution is constant
 * over a given period (for example, a week, a sprint, or a contractual interval),
 * and may change only at discrete boundaries.
 * </p>
 *
 */
public interface PiecewiseConstant {

	Segment segmentAt(LocalDateTime instant, TimeDirection direction);

	/**
	 * Returns an iterator over segments in ascending chronological order, starting from
	 * the segment that contains the given instant.
	 *
	 * <p>
	 * <strong>Important:</strong> The first segment returned by this iterator is the segment
	 * that contains {@code instant}, but this segment does <strong>not necessarily start</strong>
	 * at {@code instant}. The segment may have started before {@code instant} and
	 * extends beyond it. Subsequent segments follow in chronological order.
	 * </p>
	 *
	 * <p>
	 * This method is intended for efficient forward traversal when computing or aggregating
	 * workload over time windows without materializing all segments.
	 * </p>
	 *
	 * <p>
	 * This iterator may represent a large or conceptually unbounded sequence,
	 * depending on the underlying workload rules.
	 * </p>
	 *
	 * @param instant the instant from which to begin iteration; the first segment
	 *                      returned will be the one containing this instant
	 * @return an iterator over segments containing and following the given instant
	 */
	Iterator<Segment> iterateSegmentsFrom(LocalDateTime instant, TimeDirection direction);
	
	

}
