/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project2;

public class TaskImpl implements Task {

	private final String code;
	private TimeElement start;
	private TimeElement end;
	private int duration;
	private final TimeLine timeLine;

	public TaskImpl(TimeLine timeLine, String code) {
		this.code = code;
		this.timeLine = timeLine;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return code;
	}

	public long getLoad() {
		throw new UnsupportedOperationException();
	}

	public TimeElement getStart() {
		return start;
	}

	public TimeElement getEnd() {
		TimeElement result = start;
		for (int i = 1; i < duration; i++) {
			result = timeLine.next(result);
		}
		return result;
	}

	public TimeElement getCompleted() {
		return timeLine.next(getEnd());
	}

	public void setStart(TimeElement exp) {
		this.start = exp;
	}

	public void setDuration(int value) {
		this.duration = value;
	}

	public void setLoad(int value) {
	}

}
