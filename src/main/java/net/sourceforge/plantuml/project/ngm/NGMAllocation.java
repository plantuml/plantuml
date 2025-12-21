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

import java.time.LocalDateTime;

import net.sourceforge.plantuml.project.ngm.math.Fraction;
import net.sourceforge.plantuml.project.ngm.math.LoadIntegrator;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantWeekday;

/**
 * Represents the resource allocation applied to a task over time.
 *
 * <p>An allocation describes how much workforce capacity is assigned to a task
 * at any given instant. Unlike a simple constant fraction, an allocation can
 * vary over time, which is modeled using a {@link PiecewiseConstant} function.
 *
 * <p>The allocation value at any instant is expressed as a {@link Fraction},
 * representing the number of "full-time equivalent" (FTE) resources effectively
 * working at that moment.
 *
 * <h3>Examples of allocation patterns</h3>
 * <ul>
 *   <li><code>1</code> — One full-time person (100% capacity)</li>
 *   <li><code>2</code> — Two full-time persons working simultaneously</li>
 *   <li><code>1/2</code> — One person working at 50% capacity</li>
 *   <li><code>0</code> at certain times — No work during weekends, holidays,
 *       or outside working hours</li>
 * </ul>
 *
 * <h3>Time-varying allocation</h3>
 * <p>By wrapping a {@link PiecewiseConstant}, this class can represent complex
 * real-world scenarios such as:
 * <ul>
 *   <li>A person who only works Monday to Friday (weekday calendar)</li>
 *   <li>A person who works mornings only (08:00-12:00)</li>
 *   <li>A person who is unavailable during specific holidays</li>
 *   <li>A combination of the above (using {@code Combiner.product()})</li>
 * </ul>
 *
 * <p>The {@link LoadIntegrator} uses this allocation function to compute
 * how long a fixed-effort task will take, by integrating the allocation
 * over time until the required effort is consumed.
 *
 * @see PiecewiseConstant
 * @see net.sourceforge.plantuml.project.ngm.math.LoadIntegrator
 */
public class NGMAllocation {

	/**
	 * The time-varying allocation function.
	 * 
	 * <p>At any given {@link LocalDateTime}, this function returns a {@link Fraction}
	 * representing the effective FTE allocation at that instant.
	 * 
	 * <p>A value of {@code Fraction.ONE} means full-time work.
	 * A value of {@code Fraction.ZERO} means no work (e.g., weekend, holiday, night).
	 */
	private final PiecewiseConstant loadFunction;

	/**
	 * Private constructor. Use factory methods to create instances.
	 *
	 * @param loadFunction the piecewise constant function defining the allocation over time
	 */
	private NGMAllocation(PiecewiseConstant loadFunction) {
		this.loadFunction = loadFunction;
	}

	/**
	 * Creates an allocation from a time-varying load function.
	 *
	 * <p>This is the most flexible factory method, allowing any {@link PiecewiseConstant}
	 * to define the allocation pattern.
	 *
	 * @param loadFunction the piecewise constant function defining allocation over time
	 * @return a new NGMAllocation wrapping the given function
	 */
	public static NGMAllocation of(PiecewiseConstant loadFunction) {
		return new NGMAllocation(loadFunction);
	}

	/**
	 * Creates a constant full-time allocation (100% at all times).
	 *
	 * <p>This represents one person working full-time with no calendar constraints.
	 * Work can happen 24/7. Useful for simple calculations or as a baseline
	 * before applying calendar constraints via {@code Combiner.product()}.
	 *
	 * @return a new NGMAllocation representing constant 100% availability
	 */
	public static NGMAllocation fullTime() {
		// Full-time: 1/1 at all times, all days of the week
		return new NGMAllocation(PiecewiseConstantWeekday.of(Fraction.ONE));
	}

	/**
	 * Returns the underlying load function.
	 *
	 * <p>This function can be used with {@link net.sourceforge.plantuml.project.ngm.math.LoadIntegrator}
	 * to compute task durations based on effort and allocation.
	 *
	 * @return the piecewise constant function representing the allocation over time
	 */
	public PiecewiseConstant getLoadFunction() {
		return loadFunction;
	}

}
