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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Represents an abstract scheduled task in the New Gantt Model (NGM).
 *
 * <p>This model is built around three independent notions:</p>
 *
 * <ul>
 *   <li><b>Load</b> — an {@link NGMLoad} expressing the total amount of work
 *       (in seconds per person) associated with the task.</li>
 *
 *   <li><b>Workload</b> — an {@link NGMWorkload} representing the effective
 *       full-time-equivalent (FTE) allocation applied to the task 
 *       (e.g., 1 = 100%, 1/2 = 50%, 5/7 = weekdays only, 2 = two persons).</li>
 *
 *   <li><b>Duration</b> — a {@link Duration} representing the calendar span 
 *       between the start and end instants.</li>
 * </ul>
 *
 * <p>These three quantities must remain independent: confusing them leads to
 * incorrect scheduling behaviour. The goal of NGM is to redefine a clean,
 * unambiguous task model using {@code java.time} and explicit workload logic.</p>
 *
 *
 * <h3>Task behaviour</h3>
 *
 * <p>At the scheduling level, a task can behave in one of two ways:</p>
 *
 * <ul>
 *   <li><b>Fixed-load task</b>:  
 *       The amount of work is intrinsic and does not change.  
 *       Duration is computed from the load and the workload.  
 *       Example: “this task requires 80 hours of work”.</li>
 *
 *   <li><b>Fixed-duration task</b>:  
 *       The calendar span is intrinsic and does not change.  
 *       Load becomes a derived quantity and depends on the workload.  
 *       Example: “crossing the Atlantic takes 7 days regardless of crew size”.</li>
 * </ul>
 *
 * <p>This distinction is crucial: without it, the scheduler cannot make
 * consistent decisions about resource allocation, overlapping tasks, or
 * month-based durations.</p>
 *
 *
 * <h3>Why workload is final</h3>
 *
 * <p>The workload allocation represents the <em>structural capacity</em>
 * assigned to the task. It may influence start date, end date, duration,
 * or load (depending on the task type), but it is not modified by them.</p>
 *
 * <p>By contrast, the temporal attributes (<code>start</code>, <code>end</code>,
 * <code>duration</code>) as well as the load (for fixed-duration tasks) may vary
 * depending on scheduling decisions, calendars, dependencies, or external
 * constraints.</p>
 *
 *
 * <h3>Factory methods</h3>
 *
 * <p>The static factory methods {@link #withFixedDuration(NGMWorkload, Duration)}
 * and {@link #withFixedLoad(NGMWorkload, NGMLoad)} will eventually create concrete
 * implementations representing these two behaviours.</p>
 *
 * <p>For now, they throw {@link UnsupportedOperationException} because the model
 * is still under construction.</p>
 */

public abstract class NGMTask {

	protected final NGMWorkload workload;

	/**
	 * Creates a new task with a fixed workload allocation.
	 *
	 * @param workload the constant full-time-equivalent allocation applied to this
	 *                 task
	 */
	protected NGMTask(NGMWorkload workload) {
		this.workload = workload;
	}

	/** Returns the start instant of the task. */
	public abstract LocalDateTime getStart();

	/** Sets the start instant of the task. */
	public abstract void setStart(LocalDateTime start);

	/** Returns the end instant of the task. */
	public abstract LocalDateTime getEnd();

	/** Sets the end instant of the task. */
	public abstract void setEnd(LocalDateTime end);

    /**
     * Returns the effective scheduled duration of the task.
     *
     * <p>This value is not always equal to <code>end - start</code>.
     * In practice, the duration depends on:</p>
     *
     * <ul>
     *   <li>the start and end instants,</li>
     *   <li>the working calendar (non-working days, holidays, weekends),</li>
     *   <li>whether the task is fixed-load or fixed-duration.</li>
     * </ul>
     *
     * <p>For a fixed-duration task, the duration is intrinsic and constant even
     * if the start or end dates shift due to calendar constraints. For a
     * fixed-load task, the duration must be computed from the intrinsic load,
     * the assigned workload (FTE), and the working calendar.</p>
     *
     * <p>Because of these factors, the duration may represent the scheduled
     * "active working time" rather than a simple chronological difference.</p>
     *
     * @return the computed scheduled duration of the task
     */
    public abstract Duration getDuration();

	/** Returns the intrinsic or computed load of the task. */
	public abstract NGMLoad getLoad();

	/** Returns the constant FTE allocation applied to the task. */
	public NGMWorkload getWorkload() {
		return workload;
	}

    /**
     * Creates a task whose duration is intrinsic (fixed) and does not depend on
     * the assigned workload.
     *
    /**
     * <p>In this type of task, the duration is the defining property: it remains
     * constant regardless of how many resources are allocated. The start and end
     * instants may shift when the scheduling calendar is applied (for example,
     * when certain days are closed or non-working), but the duration itself
     * does not change.</p>
     *
     * <p>The total accumulated load is then derived from the workload applied
     * over this fixed-duration window.</p>
     *
     * <p>Example: a ship crossing the Atlantic takes a fixed number of days;
     * assigning more or fewer crew members does not shorten or extend the trip,
     * it only changes the amount of work performed during that period.</p>
     *
     * <p>This method will eventually return a concrete {@code NGMTask}
     * implementation representing this behaviour. For now, it throws
     * {@link UnsupportedOperationException} because the model is still evolving.</p>
     *
     * @param workload the constant full-time-equivalent allocation applied to the task
     * @param duration the fixed calendar duration of the task
     * @return a new fixed-duration task (when implemented)
     */
    public static NGMTask withFixedDuration(NGMWorkload workload, Duration duration) {
        return new NGMTaskFixedDuration(workload, duration);
    }

    /**
     * Creates a task whose load is intrinsic (fixed) and does not depend on
     * the assigned workload.
     *
     * <p>In this type of task, the total amount of work is the defining
     * property: regardless of the calendar duration, the task requires a fixed
     * number of seconds per person. The actual duration will be derived from
     * the available workload (FTE allocation) and the scheduling constraints.</p>
     *
     * <p>Example: implementing a feature requires “80 hours of work”.
     * Allocating additional resources reduces the duration, while reducing
     * resources increases it. The intrinsic load itself does not change.</p>
     *
     * <p>This method will eventually return a concrete {@code NGMTask}
     * implementation representing this behaviour. For now, it throws
     * {@link UnsupportedOperationException} because the model is still under construction.</p>
     *
     * @param workload the constant full-time-equivalent allocation applied to the task
     * @param load     the intrinsic amount of work required for this task
     * @return a new fixed-load task (when implemented)
     */
    public static NGMTask withFixedLoad(NGMWorkload workload, NGMLoad load) {
        throw new UnsupportedOperationException("Work In Progress");
    }

}
