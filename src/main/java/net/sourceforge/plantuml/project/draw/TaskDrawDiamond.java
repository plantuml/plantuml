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

import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.project.LabelStrategy;
import net.sourceforge.plantuml.project.ToTaskDraw;
import net.sourceforge.plantuml.project.core.GArrowType;
import net.sourceforge.plantuml.project.core.GSide;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class TaskDrawDiamond extends AbstractTaskDraw {

	public TaskDrawDiamond(TimeScale timeScale, Real y, String prettyDisplay, Day start, Task task,
			ToTaskDraw toTaskDraw, StyleBuilder styleBuilder, ISkinParam skinParam) {
		super(timeScale, y, prettyDisplay, start, task, toTaskDraw, styleBuilder, skinParam);
	}

	@Override
	StyleSignature getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.milestone);
	}

	private double getYNotePosition(StringBounder stringBounder) {
		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		return margin.getTop() + getShapeHeight(stringBounder) + margin.getBottom();
	}

	@Override
	public double getHeightMax(StringBounder stringBounder) {
		if (note == null)
			return getFullHeightTask(stringBounder);

		return getYNotePosition(stringBounder) + getOpaleNote().calculateDimension(stringBounder).getHeight();
	}

	@Override
	protected double getShapeHeight(StringBounder stringBounder) {
//		final String displayString = getTask().getDisplayString();
//		if (displayString == null)
//			return getDiamondHeight();

		final TextBlock title = getTitle();
		final XDimension2D titleDim = title.calculateDimension(stringBounder);
		return Math.max(titleDim.getHeight(), getDiamondHeight());
	}

	private double getDiamondHeight() {
		int result = (int) getFontConfiguration().getFont().getSize2D();
		if (result % 2 == 1)
			result--;
		return result;
	}

	@Override
	final public void drawTitle(UGraphic ug, LabelStrategy labelStrategy, double colTitles, double colBars) {

		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		final ClockwiseTopRightBottomLeft padding = style.getPadding();

		final TextBlock title = getTitle();

		ug = ug.apply(UTranslate.dy(margin.getTop()));

		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D titleDim = title.calculateDimension(stringBounder);
		final double h = (getShapeHeight(stringBounder) - titleDim.getHeight()) / 2;

		final double x;
		if (labelStrategy.titleInFirstColumn()) {
			if (labelStrategy.rightAligned())
				x = colTitles - titleDim.getWidth() - margin.getRight();
			else

				x = margin.getLeft();
		} else if (labelStrategy.titleInLastColumn()) {
			x = colBars + margin.getLeft();
		} else {
			final double x1 = timeScale.getStartingPosition(start);
			final double x2 = timeScale.getEndingPosition(start);
			final double width = getDiamondHeight();
			final double delta = x2 - x1 - width;
			x = x2 - delta / 2 + padding.getLeft();
		}
		title.drawU(ug.apply(new UTranslate(x, h)));
	}

	@Override
	protected TextBlock getTitle() {
		return Display.getWithNewlines(getPragma(), prettyDisplay).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
	}

	@Override
	public void drawU(UGraphic ug) {

		final double x1 = timeScale.getStartingPosition(start);
		drawNote(ug.apply(new UTranslate(x1, getYNotePosition(ug.getStringBounder()))));

		if (url != null)
			ug.startUrl(url);

		final String displayString = getTask().getDisplayString();

		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		ug = ug.apply(UTranslate.dy(margin.getTop()));

		ug = ug.apply(UTranslate.dx(x1));

		if (displayString == null) {
			final double x2 = timeScale.getEndingPosition(start);
			final double width = getDiamondHeight();
			final double delta = x2 - x1 - width;
			ug = ug.apply(UTranslate.dx(delta / 2));
			drawShape(applyColors(ug));
		} else {
			final TextBlock draw = Display.getWithNewlines(getPragma(), displayString).create(getFontConfiguration(),
					HorizontalAlignment.LEFT, new SpriteContainerEmpty());
			draw.drawU(ug);
		}
		if (url != null)
			ug.closeUrl();

	}

	private UGraphic applyColors(UGraphic ug) {
		final CenterBorderColor col = this.getColors();
		if (col != null && col.isOk())
			return col.apply(ug);

		return ug.apply(getLineColor()).apply(getBackgroundColor().bg());
	}

	private void drawShape(UGraphic ug) {
		ug.draw(getDiamond());
	}

	@Override
	public FingerPrint getFingerPrintNote(StringBounder stringBounder) {
		return null;
	}

	@Override
	public FingerPrint getFingerPrint(StringBounder stringBounder) {
		final double h = getFullHeightTask(stringBounder);
		final double startPos = timeScale.getStartingPosition(start);
		return new FingerPrint(startPos, getY(stringBounder).getCurrentValue(), startPos + h,
				getY(stringBounder).getCurrentValue() + h);
	}

	private UShape getDiamond() {
		final double h = getDiamondHeight();
		final UPolygon result = new UPolygon();
		result.addPoint(h / 2, 0);
		result.addPoint(h, h / 2);
		result.addPoint(h / 2, h);
		result.addPoint(0, h / 2);
		return result;
	}

	@Override
	public double getX(StringBounder stringBounder, GSide side, GArrowType arrowType) {
		double x;
		if (side == GSide.LEFT)
			x = timeScale.getStartingPosition(start);
		else if (side == GSide.RIGHT)
			x = timeScale.getEndingPosition(start);
		else
			x = (timeScale.getStartingPosition(start) + timeScale.getEndingPosition(start)) / 2;

		if (arrowType == GArrowType.OUTGOING) {
			final double width = getDiamondHeight();
			if (side == GSide.LEFT)
				x += width / 2;
			else if (side == GSide.RIGHT)
				x -= width / 2;
		}
		return x;
	}

	@Override
	public double getY(StringBounder stringBounder, GSide side) {
		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft margin = style.getMargin();

		final double y1 = margin.getTop() + getY(stringBounder).getCurrentValue();
		final double y2 = y1 + getShapeHeight(stringBounder);

		final String displayString = getTask().getDisplayString();

		if (displayString == null)
			return y1 + getDiamondHeight() / 2;

		return (y1 + y2) / 2;

	}

}
