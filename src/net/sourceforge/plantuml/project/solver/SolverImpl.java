/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.project.solver;

import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.time.Day;

public class SolverImpl extends AbstractSolver implements Solver {

	private final LoadPlanable loadPlanable;

	public SolverImpl(LoadPlanable loadPlanable) {
		this.loadPlanable = loadPlanable;
	}

	@Override
	protected Day computeEnd() {
		Day current = (Day) values.get(TaskAttribute.START);
		int fullLoad = ((Load) values.get(TaskAttribute.LOAD)).getFullLoad();
		int cpt = 0;
		while (fullLoad > 0) {
			fullLoad -= loadPlanable.getLoadAt(current);
			current = current.increment();
			cpt++;
			if (cpt > 100000) {
				throw new IllegalStateException();
			}
		}
		return current.decrement();
	}

	@Override
	protected Day computeStart() {
		Day current = (Day) values.get(TaskAttribute.END);
		int fullLoad = ((Load) values.get(TaskAttribute.LOAD)).getFullLoad();
		int cpt = 0;
		while (fullLoad > 0) {
			fullLoad -= loadPlanable.getLoadAt(current);
			current = current.decrement();
			if (current.getMillis() <= 0) {
				return current;
			}
			cpt++;
			if (cpt > 100000) {
				throw new IllegalStateException();
			}
		}
		return current.increment();
	}

}
