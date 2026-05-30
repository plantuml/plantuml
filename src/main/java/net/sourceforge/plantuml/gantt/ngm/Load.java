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
package net.sourceforge.plantuml.gantt.ngm;

import java.time.Duration;

import net.sourceforge.plantuml.gantt.PValue;
import net.sourceforge.plantuml.gantt.ngm.math.Fraction;

/**
 * Represents the intrinsic amount of work required for a task, expressed
 * internally as seconds of person-time.
 *
 * <p>
 * This value corresponds to the total workload assuming a single full-time
 * equivalent (1 FTE) resource works on the task. For example:
 * <ul>
 * <li>{@code ofHours(1)} means 1 hour of work for one person</li>
 * <li>{@code ofHours(8)} means 1 typical working day for one person</li>
 * <li>{@code ofHours(80)} means 10 working days for one person, or 5 days for
 * two people working in parallel</li>
 * </ul>
 *
 * <p>
 * This class is immutable and stores only the raw workload quantity.
 * Higher-level concepts such as resource allocation, effective duration, or
 * calendar constraints are handled by {@link NGMAllocation} and
 * {@link net.sourceforge.plantuml.gantt.ngm.math.LoadIntegrator}.
 *
 * @see NGMAllocation
 * @see net.sourceforge.plantuml.gantt.ngm.math.LoadIntegrator
 */
public final class Load implements PValue {

	/**
	 * Workload expressed in seconds for a single person (1 FTE).
	 */
	private final Duration duration;

	/**
	 * Private constructor. Use factory methods to create instances.
	 *
	 * @param duration the total effort in seconds
	 */
	private Load(Duration duration) {
		if (duration.isNegative())
			throw new IllegalArgumentException("Effort cannot be negative: " + duration);

		this.duration = duration;
	}

	public static Load of(Duration duration) {
		return new Load(duration);
	}

	/**
	 * Creates a zero effort (no work required).
	 *
	 * <p>
	 * Useful for milestones or placeholder tasks.
	 *
	 * @return a new NGMTotalEffort instance representing zero effort
	 */
	public static Load zero() {
		return new Load(Duration.ZERO);
	}

	public Fraction toSeconds() {
		return new Fraction(duration.toMillis() / 1000L, 1);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Load))
			return false;
		final Load other = (Load) obj;
		return this.duration.equals(other.duration);
	}

	@Override
	public String toString() {
		return duration.toString();
	}

}
