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
 */
package net.sourceforge.plantuml.tim;

import java.util.Deque;
import java.util.LinkedList;

public abstract class ExecutionContexts {

	private final Deque<ExecutionContextIf> allIfs = new LinkedList<ExecutionContextIf>();
	private final Deque<ExecutionContextWhile> allWhiles = new LinkedList<ExecutionContextWhile>();
	private final Deque<ExecutionContextForeach> allForeachs = new LinkedList<ExecutionContextForeach>();

	public void addIf(ExecutionContextIf value) {
		allIfs.addLast(value);
	}

	public void addWhile(ExecutionContextWhile value) {
		allWhiles.addLast(value);
	}

	public void addForeach(ExecutionContextForeach value) {
		allForeachs.addLast(value);
	}

	public ExecutionContextIf peekIf() {
		return allIfs.peekLast();
	}

	public ExecutionContextWhile peekWhile() {
		return allWhiles.peekLast();
	}

	public ExecutionContextForeach peekForeach() {
		return allForeachs.peekLast();
	}

	public ExecutionContextIf pollIf() {
		return allIfs.pollLast();
	}

	public ExecutionContextWhile pollWhile() {
		return allWhiles.pollLast();
	}

	public ExecutionContextForeach pollForeach() {
		return allForeachs.pollLast();
	}

	public boolean areAllIfOk(TContext context, TMemory memory) throws EaterException {
		for (ExecutionContextIf conditionalContext : allIfs) {
			if (conditionalContext.conditionIsOkHere() == false) {
				return false;
			}
		}
		return true;
	}

}
