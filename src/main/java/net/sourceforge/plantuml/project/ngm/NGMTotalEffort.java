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
package net.sourceforge.plantuml.project.ngm;

import net.sourceforge.plantuml.project.ngm.math.Fraction;

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
 * {@link net.sourceforge.plantuml.project.ngm.math.LoadIntegrator}.
 *
 * @see NGMAllocation
 * @see net.sourceforge.plantuml.project.ngm.math.LoadIntegrator
 */
public final class NGMTotalEffort {

	private static final long SECONDS_PER_MINUTE = 60;
	private static final long SECONDS_PER_HOUR = 3600;

	/**
	 * Workload expressed in seconds for a single person (1 FTE).
	 */
	private final long seconds;

	/**
	 * Private constructor. Use factory methods to create instances.
	 *
	 * @param seconds the total effort in seconds
	 */
	private NGMTotalEffort(long seconds) {
		if (seconds < 0) {
			throw new IllegalArgumentException("Effort cannot be negative: " + seconds);
		}
		this.seconds = seconds;
	}

	/**
	 * Creates an effort from a number of seconds.
	 *
	 * @param seconds the effort in seconds
	 * @return a new NGMTotalEffort instance
	 * @throws IllegalArgumentException if seconds is negative
	 */
	public static NGMTotalEffort ofSeconds(long seconds) {
		return new NGMTotalEffort(seconds);
	}

	/**
	 * Creates an effort from a number of minutes.
	 *
	 * @param minutes the effort in minutes
	 * @return a new NGMTotalEffort instance
	 * @throws IllegalArgumentException if minutes is negative
	 */
	public static NGMTotalEffort ofMinutes(long minutes) {
		return new NGMTotalEffort(minutes * SECONDS_PER_MINUTE);
	}

	/**
	 * Creates an effort from a number of hours.
	 *
	 * <p>
	 * This is the most common factory method for typical project tasks. For
	 * example, {@code ofHours(8)} represents one working day of effort.
	 *
	 * @param hours the effort in hours
	 * @return a new NGMTotalEffort instance
	 * @throws IllegalArgumentException if hours is negative
	 */
	public static NGMTotalEffort ofHours(long hours) {
		return new NGMTotalEffort(hours * SECONDS_PER_HOUR);
	}

	/**
	 * Creates an effort from a combination of hours and minutes.
	 *
	 * <p>
	 * Example: {@code ofHoursAndMinutes(2, 30)} represents 2 hours and 30 minutes.
	 *
	 * @param hours   the hours component
	 * @param minutes the minutes component (0-59 typically, but any value is
	 *                accepted)
	 * @return a new NGMTotalEffort instance
	 * @throws IllegalArgumentException if the total effort is negative
	 */
	public static NGMTotalEffort ofHoursAndMinutes(long hours, long minutes) {
		return new NGMTotalEffort(hours * SECONDS_PER_HOUR + minutes * SECONDS_PER_MINUTE);
	}

	/**
	 * Creates a zero effort (no work required).
	 *
	 * <p>
	 * Useful for milestones or placeholder tasks.
	 *
	 * @return a new NGMTotalEffort instance representing zero effort
	 */
	public static NGMTotalEffort zero() {
		return new NGMTotalEffort(0);
	}

	/**
	 * Returns the effort expressed in seconds.
	 *
	 * @return the total effort in seconds
	 */
	public long toSeconds() {
		return seconds;
	}

	/**
	 * Returns the effort expressed in minutes (truncated).
	 *
	 * @return the total effort in minutes
	 */
	public long toMinutes() {
		return seconds / SECONDS_PER_MINUTE;
	}

	/**
	 * Returns the effort expressed in hours (truncated).
	 *
	 * @return the total effort in hours
	 */
	public long toHours() {
		return seconds / SECONDS_PER_HOUR;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof NGMTotalEffort))
			return false;
		NGMTotalEffort other = (NGMTotalEffort) obj;
		return this.seconds == other.seconds;
	}

	@Override
	public String toString() {
		final long h = seconds / SECONDS_PER_HOUR;
		final long m = (seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE;
		final long s = seconds % SECONDS_PER_MINUTE;

		if (s == 0 && m == 0)
			return h + "h";
		else if (s == 0)
			return h + "h" + m + "min";
		else
			return h + "h" + m + "min" + s + "s";

	}

}
