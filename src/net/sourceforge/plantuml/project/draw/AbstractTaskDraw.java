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

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.project.ToTaskDraw;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.time.Wink;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public abstract class AbstractTaskDraw implements TaskDraw {

	protected CenterBorderColor colors;
	protected int completion = 100;
	protected Url url;
	protected Display note;
	protected final TimeScale timeScale;
	protected final double y;
	protected final String prettyDisplay;
	protected final Wink start;
	protected final ISkinParam skinParam;
	private final Task task;
	private final ToTaskDraw toTaskDraw;

	protected final double margin = 2;

	final public void setColorsAndCompletion(CenterBorderColor colors, int completion, Url url, Display note) {
		this.colors = colors;
		this.completion = completion;
		this.url = url;
		this.note = note;
	}

	public AbstractTaskDraw(TimeScale timeScale, double y, String prettyDisplay, Wink start, ISkinParam skinParam,
			Task task, ToTaskDraw toTaskDraw) {
		this.y = y;
		this.toTaskDraw = toTaskDraw;
		this.start = start;
		this.prettyDisplay = prettyDisplay;
		this.timeScale = timeScale;
		this.skinParam = skinParam;
		this.task = task;
	}

	final protected HColor getLineColor() {
		return getStyle().value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
	}

	final protected HColor getBackgroundColor() {
		return getStyle().value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
	}

	final protected FontConfiguration getFontConfiguration() {
		return getStyle().getFontConfiguration(skinParam.getIHtmlColorSet());
	}

	abstract protected Style getStyle();

	final protected double getShapeHeight() {
		return getHeight() - 2 * margin;
	}

	final public double getHeight() {
		return getFontConfiguration().getFont().getSize2D() + 5;
	}

	final public double getY() {
		if (task.getRow() == null) {
			return y;
		}
		return toTaskDraw.getTaskDraw(task.getRow()).getY();
	}

	public final Task getTask() {
		return task;
	}

	public final double getY(Direction direction) {
		if (direction == Direction.UP) {
			return getY();
		}
		if (direction == Direction.DOWN) {
			return getY() + getHeight();
		}
		return getY() + getHeight() / 2;
	}

}
