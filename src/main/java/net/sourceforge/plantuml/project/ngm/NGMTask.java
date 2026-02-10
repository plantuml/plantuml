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
import java.time.LocalDateTime;

/**
 * Represents an abstract scheduled task in the New Gantt Model (NGM).
 *
 * <p>
 * This model is built around three independent notions:
 * </p>
 *
 * <ul>
 * <li><b>Total effort</b> — an {@link NGMTotalEffort} expressing the total
 * amount of work associated with the task, typically in person-time (for
 * example, person-seconds or person-hours).</li>
 *
 * <li><b>Allocation</b> — an {@link NGMAllocation} representing the effective
 * full-time-equivalent (FTE) assigned to the task (e.g., 1 = 100%, 1/2 = 50%,
 * 5/7 = weekdays only, 2 = two persons).</li>
 *
 * <li><b>Duration</b> — a {@link Duration} representing the calendar span
 * between the start and end instants.</li>
 * </ul>
 *
 * <p>
 * These three quantities must remain conceptually independent. Confusing them
 * leads to incorrect scheduling behaviour. The goal of NGM is to provide a
 * clean and unambiguous task model using {@code java.time} and explicit
 * resource-allocation logic.
 * </p>
 *
 *
 * <h3>Task behaviour</h3>
 *
 * <p>
 * At the scheduling level, a task can behave in one of two ways:
 * </p>
 *
 * <ul>
 * <li><b>Fixed-total-effort task</b>: The total effort is intrinsic and does
 * not change. The scheduled duration is computed from the total effort and the
 * allocation. Example: “this task requires 80 hours of work”.</li>
 *
 * <li><b>Fixed-duration task</b>: The calendar span is intrinsic and does not
 * change. The total effort becomes a derived quantity and depends on the
 * allocation. Example: “crossing the Atlantic takes 7 days regardless of crew
 * size”.</li>
 * </ul>
 *
 * <p>
 * This distinction is crucial: without it, the scheduler cannot make consistent
 * decisions about resource allocation, overlapping tasks, or long-running
 * schedules.
 * </p>
 *
 *
 * <h3>Why allocation is final</h3>
 *
 * <p>
 * The allocation represents the <em>structural capacity</em> assigned to the
 * task. It may influence start date, end date, duration, or total effort
 * (depending on the task type), but it is not modified by them.
 * </p>
 *
 * <p>
 * By contrast, the temporal attributes (<code>start</code>, <code>end</code>,
 * <code>duration</code>) as well as the total effort (for fixed-duration tasks)
 * may vary depending on scheduling decisions, calendars, dependencies, or
 * external constraints.
 * </p>
 *
 *
 * <h3>Factory methods</h3>
 *
 * <p>
 * The static factory methods
 * {@link #withFixedDuration} and
 * {@link #withFixedTotalEffort} create concrete
 * implementations representing these two behaviours.
 * </p>
 */
public abstract class NGMTask {

	protected final NGMAllocation allocation;

	/**
	 * Creates a new task with a fixed workload allocation.
	 *
	 */
	protected NGMTask(NGMAllocation allocation) {
		this.allocation = allocation;
	}

	/**
	 * Returns the current start instant of the task.
	 *
	 * <p>
	 * The start and end instants are not independent values in the NGM model. They
	 * form a coherent scheduled window derived from the task type (fixed-duration
	 * vs fixed-total-effort), the allocation, and the scheduling constraints.
	 * </p>
	 *
	 * @return the scheduled start instant
	 */
	public abstract LocalDateTime getStart();

	/**
	 * Sets (anchors) the start instant of the task.
	 *
	 * <p>
	 * This method is a scheduling input. Calling it triggers a recomputation of the
	 * task's scheduled window. As a consequence, after this call:
	 * </p>
	 *
	 * <ul>
	 * <li>{@link #getStart()} will reflect the newly anchored start, and</li>
	 * <li>{@link #getEnd()} will be recalculated to maintain a consistent task
	 * model.</li>
	 * </ul>
	 *
	 * <p>
	 * The exact recomputation rules depend on the concrete task type and may take
	 * into account the intrinsic duration or total effort, the constant allocation,
	 * and the scheduling calendar.
	 * </p>
	 *
	 * @param start the start instant to anchor for scheduling
	 */
	public abstract void setStart(LocalDateTime start);

	/**
	 * Returns the current end instant of the task.
	 *
	 * <p>
	 * The start and end instants are not independent values in the NGM model. They
	 * form a coherent scheduled window derived from the task type (fixed-duration
	 * vs fixed-total-effort), the allocation, and the scheduling constraints.
	 * </p>
	 *
	 * @return the scheduled end instant
	 */
	public abstract LocalDateTime getEnd();

	/**
	 * Sets (anchors) the end instant of the task.
	 *
	 * <p>
	 * This method is a scheduling input. Calling it triggers a recomputation of the
	 * task's scheduled window. As a consequence, after this call:
	 * </p>
	 *
	 * <ul>
	 * <li>{@link #getEnd()} will reflect the newly anchored end, and</li>
	 * <li>{@link #getStart()} will be recalculated to maintain a consistent task
	 * model.</li>
	 * </ul>
	 *
	 * <p>
	 * The exact recomputation rules depend on the concrete task type and may take
	 * into account the intrinsic duration or total effort, the constant allocation,
	 * and the scheduling calendar.
	 * </p>
	 *
	 * @param end the end instant to anchor for scheduling
	 */
	public abstract void setEnd(LocalDateTime end);

	/**
	 * Returns the effective scheduled duration of the task.
	 *
	 * <p>
	 * This value is not always equal to <code>end - start</code>. In practice, the
	 * duration depends on:
	 * </p>
	 *
	 * <ul>
	 * <li>the start and end instants,</li>
	 * <li>the working calendar (non-working days, holidays, weekends),</li>
	 * <li>whether the task is fixed-load or fixed-duration.</li>
	 * </ul>
	 *
	 * <p>
	 * For a fixed-duration task, the duration is intrinsic and constant even if the
	 * start or end dates shift due to calendar constraints. For a fixed-load task,
	 * the duration must be computed from the intrinsic load, the assigned workload
	 * (FTE), and the working calendar.
	 * </p>
	 *
	 * <p>
	 * Because of these factors, the duration may represent the scheduled "active
	 * working time" rather than a simple chronological difference.
	 * </p>
	 *
	 * @return the computed scheduled duration of the task
	 */
	public abstract Duration getDuration();

	public abstract void setEffort(NGMTotalEffort effort);

	/**
	 * Returns the total effort associated with this task.
	 *
	 * <p>
	 * The term <b>total effort</b> refers to the overall amount of work required to
	 * complete the task, independent of the calendar span. It is typically
	 * expressed in person-time (for example, person-seconds or person-hours).
	 * </p>
	 *
	 * <p>
	 * Depending on the concrete task type, this value may be:
	 * </p>
	 * <ul>
	 * <li><b>intrinsic (fixed)</b> — the effort is defined directly by the user and
	 * does not change when dates or allocation change (e.g., “this task requires 80
	 * hours of work”).</li>
	 * <li><b>computed (derived)</b> — the effort is calculated from other defining
	 * properties such as the task’s duration, the effective allocation, and any
	 * scheduling constraints.</li>
	 * </ul>
	 *
	 * <p>
	 * In other words, this method provides a single, consistent access point to the
	 * task’s “work quantity”, whether that quantity is a primary input or a
	 * secondary result of the scheduling model.
	 * </p>
	 *
	 * @return the intrinsic or computed total effort of the task
	 */
	public abstract NGMTotalEffort getTotalEffort();

	/**
	 * Returns the constant allocation applied to the task.
	 *
	 * <p>
	 * The <b>allocation</b> represents the effective full-time-equivalent (FTE)
	 * assigned to the task. It describes the resource intensity available for
	 * executing the work, and therefore influences derived scheduling properties.
	 * </p>
	 *
	 * <p>
	 * Examples of typical meanings:
	 * </p>
	 * <ul>
	 * <li><code>1</code> — one full-time equivalent (100%).</li>
	 * <li><code>1/2</code> — half-time allocation (50%).</li>
	 * <li><code>2</code> — two full-time equivalents (two people at 100%).</li>
	 * <li><code>5/7</code> — an allocation constrained to weekdays only, if your
	 * model uses such fractions to represent availability patterns.</li>
	 * </ul>
	 *
	 * <p>
	 * This method returns the constant allocation associated with the task
	 * instance. Task types that support variable or time-sliced allocations may
	 * override or complement this behaviour with more advanced APIs as the model
	 * evolves.
	 * </p>
	 *
	 * @return the constant FTE allocation applied to the task
	 */
	public NGMAllocation getAllocation() {
		return allocation;
	}

	/**
	 * Creates a task whose duration is intrinsic (fixed) and does not depend on the
	 * assigned allocation.
	 *
	 * <p>
	 * In this type of task, the duration is the defining property: it remains
	 * constant regardless of how many resources are allocated.
	 * </p>
	 *
	 * <p>
	 * When a scheduling calendar is applied, the <em>start</em> and/or <em>end</em>
	 * instants may shift to satisfy availability constraints (for example, when
	 * certain days are closed or non-working). However, the overall scheduled
	 * duration represented by this task remains unchanged.
	 * </p>
	 *
	 * <p>
	 * The total effort is then derived from the allocation applied over this
	 * fixed-duration window.
	 * </p>
	 *
	 * <p>
	 * Example: a ship crossing the Atlantic takes a fixed number of days; assigning
	 * more or fewer crew members does not shorten or extend the trip, it only
	 * changes the total effort performed during that period.
	 * </p>
	 *
	 * @param allocation the constant full-time-equivalent allocation applied to the
	 *                   task
	 * @param duration   the intrinsic fixed calendar duration of the task
	 * @return a new fixed-duration task
	 */
	public static NGMTask withFixedDuration(NGMAllocation allocation, Duration duration) {
		return new NGMTaskFixedDuration(allocation, duration);
	}

	/**
	 * Creates a task whose total effort is intrinsic (fixed) and does not depend on
	 * the assigned allocation.
	 *
	 * <p>
	 * In this type of task, the total amount of work is the defining property:
	 * regardless of the calendar duration, the task requires a fixed quantity of
	 * person-time (for example, person-seconds or person-hours).
	 * </p>
	 *
	 * <p>
	 * The actual scheduled duration will be derived from the available allocation
	 * (FTE) and the scheduling constraints.
	 * </p>
	 *
	 * <p>
	 * Example: implementing a feature requires “80 hours of work”. Allocating
	 * additional resources reduces the duration, while reducing resources increases
	 * it. The intrinsic total effort itself does not change.
	 * </p>
	 *
	 * <p>
	 * This method will eventually return a concrete {@code NGMTask} implementation
	 * representing this behaviour. For now, it throws
	 * {@link UnsupportedOperationException} because the model is still under
	 * construction.
	 * </p>
	 *
	 * @param allocation  the constant full-time-equivalent allocation applied to
	 *                    the task
	 * @param totalEffort the intrinsic amount of work required for this task
	 * @return a new fixed-total-effort task (when implemented)
	 */
	public static NGMTask withFixedTotalEffort(NGMAllocation allocation, LocalDateTime start,
			NGMTotalEffort totalEffort) {
		return new NGMTaskFixedTotalEffort(allocation, start, totalEffort);
	}

}
