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
package net.sourceforge.plantuml.project.draw;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.project.ToTaskDraw;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.Direction;

public abstract class AbstractTaskDraw implements TaskDraw {

	private CenterBorderColor colors;

	private int completion = 100;
	protected Url url;
	protected Display note;
	protected final TimeScale timeScale;
	private Real y;
	protected final String prettyDisplay;
	protected final Day start;
	private final StyleBuilder styleBuilder;
	private final Task task;
	private final ToTaskDraw toTaskDraw;

	@Override
	final public String toString() {
		return super.toString() + " " + task;
	}

	final public void setColorsAndCompletion(CenterBorderColor colors, int completion, Url url, Display note) {
		this.colors = colors;
		this.completion = completion;
		this.url = url;
		this.note = note;
	}

	public AbstractTaskDraw(TimeScale timeScale, Real y, String prettyDisplay, Day start, Task task,
			ToTaskDraw toTaskDraw, StyleBuilder styleBuilder) {
		this.y = y;
		this.styleBuilder = styleBuilder;
		this.toTaskDraw = toTaskDraw;
		this.start = start;
		this.prettyDisplay = prettyDisplay;
		this.timeScale = timeScale;
		this.task = task;
	}

	abstract StyleSignature getStyleSignature();

	private StyleSignatureBasic getStyleSignatureUnstarted() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.task, SName.unstarted);
	}

	final protected HColor getLineColor() {
		final HColor unstarted = getStyleSignatureUnstarted().getMergedStyle(styleBuilder).value(PName.LineColor)
				.asColor(getColorSet());
		final HColor regular = getStyle().value(PName.LineColor).asColor(getColorSet());
		return HColors.unlinear(unstarted, regular, completion);
	}

	final protected HColor getBackgroundColor() {
		final HColor unstarted = getStyleSignatureUnstarted().getMergedStyle(styleBuilder).value(PName.BackGroundColor)
				.asColor(getColorSet());
		final HColor regular = getStyle().value(PName.BackGroundColor).asColor(getColorSet());
		return HColors.unlinear(unstarted, regular, completion);
	}

	final protected FontConfiguration getFontConfiguration() {
		return getStyle().getFontConfiguration(getColorSet());
	}

	final protected Style getStyle() {
		return getStyleSignature().getMergedStyle(styleBuilder);
	}

	final public double getTitleWidth(StringBounder stringBounder) {
		final Style style = getStyleSignature().getMergedStyle(getStyleBuilder());
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		return margin.getLeft() + getTitle().calculateDimension(stringBounder).getWidth() + margin.getRight();
	}

	protected abstract TextBlock getTitle();

	abstract protected double getShapeHeight(StringBounder stringBounder);

	final public double getFullHeightTask(StringBounder stringBounder) {
		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		return margin.getTop() + getShapeHeight(stringBounder) + margin.getBottom();
	}

	final public TaskDraw getTrueRow() {
		return toTaskDraw.getTaskDraw(task.getRow());
	}

	@Override
	final public Real getY(StringBounder stringBounder) {
		if (task.getRow() == null)
			return y;

		return getTrueRow().getY(stringBounder);
	}

	public final Task getTask() {
		return task;
	}

	@Override
	public final double getY(StringBounder stringBounder, Direction direction) {
		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		final ClockwiseTopRightBottomLeft padding = style.getPadding();

		final double y1 = margin.getTop() + getY(stringBounder).getCurrentValue();
		final double y2 = y1 + getShapeHeight(stringBounder);

		if (direction == Direction.UP)
			return y1;

		if (direction == Direction.DOWN)
			return y2;

		return (y1 + y2) / 2;

	}

	protected final StyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

	protected final HColorSet getColorSet() {
		return toTaskDraw.getIHtmlColorSet();
	}

	protected CenterBorderColor getColors() {
		return colors;
	}

	protected int getCompletion() {
		return completion;
	}

}
