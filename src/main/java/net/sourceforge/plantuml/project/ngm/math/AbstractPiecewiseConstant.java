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
 * Base class for {@link PiecewiseConstant} implementations whose value is
 * constant over contiguous time segments.
 *
 * <p>
 * This class provides a implementation of
 * {@link #iterateSegmentsFrom(LocalDateTime)} that lazily iterates over
 * successive {@link Segment segments}, starting from the segment that contains
 * a given instant.
 * </p>
 *
 * <p>
 * The iterator returned by this method has the following properties:
 * </p>
 * <ul>
 * <li>The first segment returned is the segment that contains the given
 * {@code instant}, but it is trimmed so that it starts exactly at
 * {@code instant}.</li>
 * <li>Each subsequent segment starts exactly where the previous one ends
 * (end-exclusive), ensuring a continuous, gap-free iteration.</li>
 * <li>The iterator is conceptually unbounded: {@link Iterator#hasNext()} always
 * returns {@code true}.</li>
 * </ul>
 *
 * <p>
 * This method relies on the concrete implementation of
 * {@link #segmentAt(LocalDateTime)} to locate the segment covering a given
 * instant. It does not precompute or materialize all segments, making it
 * suitable for efficient forward traversal over long or infinite timelines.
 * </p>
 *
 * <p>
 * <strong>Important:</strong> Implementations of
 * {@link #segmentAt(LocalDateTime)} must guarantee that the returned segment:
 * </p>
 * <ul>
 * <li>contains the provided instant,</li>
 * <li>has a strictly increasing {@code endExclusive} instant,</li>
 * <li>is consistent with adjacent segments so that repeated calls with
 * successive instants form a coherent timeline.</li>
 * </ul>
 *
 * <p>
 * Violating these assumptions may result in infinite loops or incorrect segment
 * boundaries during iteration.
 * </p>
 *
 * @param instant the instant from which to start iterating; the first segment
 *                returned will begin exactly at this instant
 * @return an iterator over successive segments
 */
public abstract class AbstractPiecewiseConstant implements PiecewiseConstant {

	@Override
	public final Iterator<Segment> iterateSegmentsFrom(final LocalDateTime instant, final TimeDirection direction) {
		return new Iterator<Segment>() {

			// Current iteration position.
			// This represents the start instant of the next segment to be returned.
			private LocalDateTime current = instant;
			
			private int counter = 9999;

			@Override
			public Segment next() {
				// Check counter to detect infinite loops.
				if (counter <= 0)
					throw new IllegalStateException("Infinite loop detected in PiecewiseConstant iteration");
				counter--;
				
				// Retrieve the (possibly larger) segment that contains the original instant.
				// Implementations of segmentAt(...) are expected to return a segment
				// that fully covers the given instant.
				final Segment segment = segmentAt(current, direction);

				// Advance the iterator to the end of the returned segment.
				// The next call to next() will start from this instant.
				current = segment.endExclusive();

				return segment;
			}

			@Override
			public boolean hasNext() {
				// Piecewise-constant functions are considered unbounded in time.
				// Iteration therefore never terminates.
				return true;
			}
		};
	}

}
