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


/**
 * Represents the intrinsic amount of work required for a task,
 * expressed as a duration in seconds per person.
 *
 * <p>This value corresponds to the total workload assuming a
 * single full-time equivalent (1 FTE) resource works on the task.
 * For example:
 * <ul>
 *   <li>a value of <code>3600</code> means 1 hour of work for one person</li>
 *   <li>a value of <code>86400</code> means 1 day of work for one person</li>
 *   <li>a value of <code>2 * 86400</code> means 2 days of work for one person</li>
 * </ul>
 *
 * <p>This class intentionally stores only the raw workload. 
 * Higher-level concepts such as resource allocation (workload/fraction), 
 * effective duration, or calendar constraints are handled elsewhere.</p>
 */
public class NGMTotalEffort {

    /**
     * Workload expressed in seconds for a single person (1 FTE).
     */
    public long value;
}

