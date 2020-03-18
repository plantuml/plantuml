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
package net.sourceforge.plantuml.project.draw;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.project.core.AbstractTask;
import net.sourceforge.plantuml.project.time.Wink;
import net.sourceforge.plantuml.project.timescale.TimeScale;

public abstract class AbstractTaskDraw implements TaskDraw {

	protected final TimeScale timeScale;
	protected final double y;
	protected final String prettyDisplay;
	protected final Wink start;

	protected final double margin = 2;

	public AbstractTaskDraw(TimeScale timeScale, double y, String prettyDisplay, Wink start) {
		this.y = y;
		this.start = start;
		this.prettyDisplay = prettyDisplay;
		this.timeScale = timeScale;
	}

	abstract protected FontConfiguration getFontConfiguration();

	final protected double getShapeHeight() {
		return getHeight() - 2 * margin;
	}

	final public double getHeight() {
		return AbstractTask.HEIGHT;
	}

	final public double getY() {
		return y;
	}

}
