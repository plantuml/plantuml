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

/**
 * Represents an abstract scheduled task with intrinsic workload semantics
 * and temporal properties (start, end, duration) that may or may not be fixed.
 *
 * <p>The model distinguishes three concepts that must remain separate:</p>
 *
 * <ul>
 *   <li><b>Load</b> — an {@link NGMLoad} representing the total amount of work
 *       required for this task. When the load is fixed, the duration depends on
 *       the assigned workload and the available calendar window.</li>
 *
 *   <li><b>Workload</b> — an {@link NGMWorkload} expressing the effective
 *       full-time-equivalent (FTE) allocation applied to the task.
 *       For example: 1 (100%), 1/2 (50%), 5/7 (weekdays only), 2 (two persons).
 *       This defines how quickly the work progresses.</li>
 *
 *   <li><b>Duration</b> — a {@link Duration} representing the calendar span
 *       between the start and end instants. When the duration is fixed,
 *       the load becomes the variable quantity: adding more or fewer resources
 *       changes the total amount of work accumulated over that time window.</li>
 * </ul>
 *
 * <p><b>Two types of tasks exist in this model:</b></p>
 * <ul>
 *   <li><b>Fixed-load tasks</b>: the total amount of work is intrinsic.  
 *       The duration varies depending on the allocated workload.  
 *       Example: developing a feature requires “80 hours of work”.</li>
 *
 *   <li><b>Fixed-duration tasks</b>: the calendar span is intrinsic.  
 *       The load becomes the variable quantity.  
 *       Example: crossing the Atlantic takes a fixed amount of time;
 *       assigning different workloads does not change the duration,
 *       only the resulting amount of accumulated work.</li>
 * </ul>
 *
 * <p>This distinction is essential: a task cannot be correctly modeled unless
 * we know whether its duration or its load is the intrinsic property.
 * The previous Gantt implementation mixed these notions, which led to
 * inconsistencies (especially when handling months or resource changes).</p>
 *
 * <p><b>Why workload is final:</b></p>
 * <p>The workload allocation (FTE fraction) is considered a structural property
 * of the task. It may influence the schedule but is not modified by it.
 * By contrast, <code>start</code>, <code>end</code>, <code>duration</code>,
 * and <code>load</code> may vary depending on the concrete type of task
 * (fixed-load vs fixed-duration) and on external scheduling constraints.</p>
 */
public abstract class NGMTask {

    protected final NGMWorkload workload;

    /**
     * Creates a new task with a fixed workload allocation.
     *
     * @param workload  the constant full-time-equivalent allocation
     *                  applied to this task
     */
    protected NGMTask(NGMWorkload workload) {
        this.workload = workload;
    }

    /** Returns the start instant of the task. */
    public abstract Instant getStart();

    /** Sets the start instant of the task. */
    public abstract void setStart(Instant start);

    /** Returns the end instant of the task. */
    public abstract Instant getEnd();

    /** Sets the end instant of the task. */
    public abstract void setEnd(Instant end);

    /** Returns the scheduled duration of the task (end - start). */
    public abstract Duration getDuration();

    /** Sets the scheduled duration of the task. */
    public abstract void setDuration(Duration duration);

    /** Returns the intrinsic or computed load of the task. */
    public abstract NGMLoad getLoad();

    /** Updates the load, depending on whether this is a fixed-load or fixed-duration task. */
    public abstract void setLoad(NGMLoad load);

    /** Returns the constant FTE allocation applied to the task. */
    public NGMWorkload getWorkload() {
        return workload;
    }
}


