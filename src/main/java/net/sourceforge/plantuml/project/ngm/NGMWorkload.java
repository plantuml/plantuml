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
 * Represents the effective workload applied to a task.
 *
 * <p>This value is expressed as a rational fraction, indicating
 * how many "full-time equivalent" resources are effectively working.
 *
 * <p>Examples:
 * <ul>
 *   <li><code>1</code> — 100% workload (one full-time person)</li>
 *   <li><code>2</code> — two full-time persons working simultaneously</li>
 *   <li><code>1/2</code> — one person working at 50% every day</li>
 *   <li><code>5/7</code> — one person working Monday to Friday
 *       (5 active days out of 7)</li>
 *   <li><code>5/14</code> — one person working at 50% on Monday to Friday
 *       (5 half-days over a 14-half-days reference period)</li>
 * </ul>
 *
 * <p>The field remains public intentionally, as this class is temporary
 * and will likely evolve toward a more structured representation.
 */
public class NGMWorkload {

    /**
     * Workload value expressed as a fraction.
     * A value of 1 represents 100% workload.
     */
    public Fraction value;
    
    // In the future this will become more complex. We will probably use
    // something like: public Function<Instant, Fraction>
    // because the workload may depend on the instant.
    // For now, we keep it simple and assume a constant workload.

}

