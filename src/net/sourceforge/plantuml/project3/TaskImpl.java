/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.project3;

public class TaskImpl implements Task {

	private final TaskCode code;
	private final Solver solver = new Solver();

	public TaskImpl(TaskCode code) {
		this.code = code;
		setStart(new InstantDay(0));
		setDuration(new DurationDay(1));
	}

	@Override
	public String toString() {
		return code.toString();
	}

	public String debug() {
		return "" + getStart() + " ---> " + getEnd() + "   [" + getDuration() + "]";
	}

	public TaskCode getCode() {
		return code;
	}

	public Instant getStart() {
		return (Instant) solver.getData(TaskAttribute.START);
	}

	public Instant getEnd() {
		return (Instant) solver.getData(TaskAttribute.END);
	}

	public Duration getDuration() {
		return (Duration) solver.getData(TaskAttribute.DURATION);
	}

	public void setStart(Instant start) {
		solver.setData(TaskAttribute.START, start);
	}

	public void setEnd(Instant end) {
		solver.setData(TaskAttribute.END, end);
	}

	public void setDuration(Duration duration) {
		solver.setData(TaskAttribute.DURATION, duration);
	}

	private TaskDraw taskDraw;
	private ComplementColors colors;

	public void setTaskDraw(TaskDraw taskDraw) {
		taskDraw.setColors(colors);
		this.taskDraw = taskDraw;
	}

	public TaskDraw getTaskDraw() {
		return taskDraw;
	}

	public void setColors(ComplementColors colors) {
		this.colors = colors;
	}

}
