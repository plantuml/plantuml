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
package net.sourceforge.plantuml.project.core;

import java.util.Objects;

public class TaskCode {

	private final String id;
	private final String display;

	public static TaskCode fromId(String id) {
		return new TaskCode(id, id);
	}

	public static TaskCode fromIdAndDisplay(String id, String display) {
		if (id == null)
			id = display;
		if (display == null)
			return fromId(id);
		return new TaskCode(id, display);
	}

	private TaskCode(String id, String display) {
		this.id = Objects.requireNonNull(id);
		this.display = Objects.requireNonNull(display);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object arg) {
		final TaskCode other = (TaskCode) arg;
		return this.id.equals(other.id);
	}

	public String getDisplay() {
		return display;
	}

	public String getId() {
		return id;
	}

}
