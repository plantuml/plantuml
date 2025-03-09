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

import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.StyleBuilder;

public abstract class AbstractTask implements Task {

	private final TaskCode code;
	private final StyleBuilder styleBuilder;

	private Task row;
	private String displayString;
	private Stereotype stereotype;

	protected AbstractTask(StyleBuilder styleBuilder, TaskCode code) {
		this.styleBuilder = styleBuilder;
		this.code = code;
	}

	@Override
	final public void putInSameRowAs(Task row) {
		if (this != row)
			this.row = row;
	}

	@Override
	public final Task getRow() {
		return row;
	}

	@Override
	public final TaskCode getCode() {
		return code;
	}

	@Override
	public final StyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

	@Override
	public void setDisplay(String displayString) {
		this.displayString = displayString;
	}

	@Override
	public String getDisplayString() {
		return this.displayString;
	}
	
	@Override
	public Stereotype getStereotype() {
		return stereotype;
	}

	@Override
	public final void setStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
	}


}
