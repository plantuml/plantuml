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
 * Original Author:  Arnaud Roques, Mario Kušek
 * 
 *
 */
package net.sourceforge.plantuml.project.ngm.math;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A constant workload segment.
 *
 * <p>
 * Within {@code [startInclusive, endExclusive)}, the workload is assumed to be constant.
 * This mirrors how resource planning is often defined: a stable allocation over a
 * time range with changes occurring only at explicit boundaries.
 * </p>
 *
 * <p>
 * The value may be zero to explicitly model periods with no assigned workload.
 * </p>
 */
public final class Segment {
	/**
	 * The start of the segment, inclusive. Can not be null.
	 */
	private final LocalDateTime startInclusive;
	
	/**
	 * The end of the segment, exclusive. Can not be null. Can not be before {@link #startInclusive}.
	 */
	private final LocalDateTime endExclusive;
	
	/**
	 * The constant workload value over this segment.
	 */
	private final Fraction value;

	public Segment(LocalDateTime startInclusive, LocalDateTime endExclusive, Fraction value) {
		Objects.requireNonNull(startInclusive, "startInclusive must not be null");
		Objects.requireNonNull(endExclusive, "endExclusive must not be null");
		Objects.requireNonNull(value, "value must not be null");
		if (startInclusive != null && endExclusive != null && !startInclusive.isBefore(endExclusive))
			throw new IllegalArgumentException("startInclusive must be before endExclusive");

		this.startInclusive = startInclusive;
		this.endExclusive = endExclusive;
		this.value = value;
	}

	public LocalDateTime getStartInclusive() {
		return startInclusive;
	}

	public LocalDateTime getEndExclusive() {
		return endExclusive;
	}

	public Fraction getValue() {
		return value;
	}
	
	/**
	 * Splits this segment into two contiguous segments at the given instant.
	 *
	 * <p>
	 * The split instant {@code time} must lie within the bounds of this segment,
	 * that is:
	 * </p>
	 * <pre>
	 * startInclusive &lt;= time &lt; endExclusive
	 * </pre>
	 *
	 * <p>
	 * The returned array always contains exactly two segments:
	 * </p>
	 * <ul>
	 *   <li>the first segment spans from {@code startInclusive} (inclusive)
	 *       to {@code time} (exclusive),</li>
	 *   <li>the second segment spans from {@code time} (inclusive)
	 *       to {@code endExclusive} (exclusive).</li>
	 * </ul>
	 *
	 * <p>
	 * Both resulting segments carry the same {@link #getValue() value} as this
	 * segment. The two segments are guaranteed to be contiguous and non-overlapping.
	 * </p>
	 *
	 * <p>
	 * If {@code time} is equal to {@code startInclusive}, the first segment will
	 * be empty and the second segment will be identical to this segment.
	 * If {@code time} is equal to {@code endExclusive}, the second segment will
	 * be empty and the first segment will be identical to this segment.
	 * </p>
	 *
	 * @param time the instant at which to split this segment
	 * @return an array of exactly two segments resulting from the split,
	 *         in chronological order
	 * @throws IllegalArgumentException if {@code time} is {@code null} or lies
	 *         outside the bounds of this segment
	 */
	public Segment[] split(LocalDateTime time) {
		throw new UnsupportedOperationException("wip");
	}

	@Override
	public String toString() {
		return "Segment{" + "startInclusive=" + startInclusive + ", endExclusive=" + endExclusive + ", value="
				+ value + '}';
	}
}