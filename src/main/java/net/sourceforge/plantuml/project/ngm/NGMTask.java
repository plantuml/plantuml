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
 * Represents an abstract scheduled task with a fixed intrinsic workload
 * and dynamic temporal properties (start, end, duration).
 *
 * <p>The class distinguishes three key concepts:</p>
 *
 * <ul>
 *   <li><b>Load</b> — a {@link Duration} representing the amount of work 
 *       required to complete the task if performed at full efficiency 
 *       (100% workload, i.e., 1 FTE).  
 *       This is an intrinsic characteristic of the task: the volume of work
 *       to be done, independent of how many people are assigned.</li>
 *
 *   <li><b>Workload</b> — an {@link NGMWorkload} value expressing the effective 
 *       "full-time equivalent" allocation.  
 *       For example: 1 (100%), 1/2 (50%), 5/7 (weekdays only), 2 (two persons).  
 *       This determines how fast the task progresses relative to its load.</li>
 *
 *   <li><b>Duration</b> — a {@link Duration} representing the calendar span 
 *       between the start and end instants.  
 *       Unlike load and workload, the duration is not intrinsic: it is the 
 *       result of scheduling decisions and resource assignment. 
 *       Duration may change if start, end, or workload changes.</li>
 * </ul>
 *
 * <p><b>Why load and workload are final:</b></p>
 * <p>Both values are considered intrinsic properties of the task:</p>
 * <ul>
 *   <li><b>Load</b> defines how much total work must be executed.</li>
 *   <li><b>Workload</b> defines the constant execution capacity allocated to this task.</li>
 * </ul>
 *
 * <p>They do not change once the task exists. By contrast, the temporal
 * attributes—<code>start</code>, <code>end</code>, and <code>duration</code>—
 * may be updated as the task is rescheduled or as constraints evolve.</p>
 */
public abstract class NGMTask {

    protected final Duration load;
    protected final NGMWorkload workload;

    /**
     * Creates a new task with a fixed load and workload.
     *
     * @param load      the intrinsic amount of work required if executed at 100% capacity
     * @param workload  the constant full-time-equivalent allocation applied to this task
     */
    protected NGMTask(Duration load, NGMWorkload workload) {
        this.load = load;
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

    /** Returns the intrinsic work required by the task at 100% workload. */
    public Duration getLoad() {
        return load;
    }

    /** Returns the constant FTE allocation applied to the task. */
    public NGMWorkload getWorkload() {
        return workload;
    }
}

