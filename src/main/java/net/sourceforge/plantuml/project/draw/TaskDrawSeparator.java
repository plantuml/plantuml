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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.project.LabelStrategy;
import net.sourceforge.plantuml.project.core.GArrowType;
import net.sourceforge.plantuml.project.core.GSide;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.url.Url;

public class TaskDrawSeparator implements TaskDraw {

	private final TimeScale timeScale;
	private Real y;
	private final Day min;
	private final Day max;
	private final String name;
	private final StyleBuilder styleBuilder;
	private final HColorSet colorSet;
	private final ISkinParam skinParam;

	public TaskDrawSeparator(String name, TimeScale timeScale, Real y, Day min, Day max, StyleBuilder styleBuilder,
			ISkinParam skinParam) {
		this.styleBuilder = styleBuilder;
		this.skinParam = skinParam;
		this.colorSet = skinParam.getIHtmlColorSet();
		this.name = name;
		this.y = y;
		this.timeScale = timeScale;
		this.min = min;
		this.max = max;
	}

	@Override
	public void drawTitle(UGraphic ug, LabelStrategy labelStrategy, double colTitles, double colBars) {
		final ClockwiseTopRightBottomLeft padding = getStyle().getPadding();
		final ClockwiseTopRightBottomLeft margin = getStyle().getMargin();
		final double dx = margin.getLeft() + padding.getLeft();
		final double dy = margin.getTop() + padding.getTop();
		final double x;
		if (labelStrategy.titleInFirstColumn())
			x = colTitles;
		else
			x = 0;

		getTitle().drawU(ug.apply(new UTranslate(x + dx, dy)));
	}

	@Override
	public double getTitleWidth(StringBounder stringBounder) {
		// Never used in first column
		return 0;
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.separator);
	}

	private Style getStyle() {
		return getStyleSignature().getMergedStyle(styleBuilder);
	}

	private TextBlock getTitle() {
		if (name == null)
			return TextBlockUtils.empty(0, 0);

		return Display.getWithNewlines(skinParam.getPragma(), this.name).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
	}

	private FontConfiguration getFontConfiguration() {
		return getStyle().getFontConfiguration(colorSet);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double widthTitle = getTitle().calculateDimension(stringBounder).getWidth();
		final double start = timeScale.getStartingPosition(min);
		// final double start2 = start1 + widthTitle;
		final double end = timeScale.getEndingPosition(max);

		final ClockwiseTopRightBottomLeft padding = getStyle().getPadding();
		final ClockwiseTopRightBottomLeft margin = getStyle().getMargin();
		ug = ug.apply(new UTranslate(0, margin.getTop()));

		final HColor backColor = getStyle().value(PName.BackGroundColor).asColor(colorSet);

		if (backColor.isTransparent() == false) {
			final double height = padding.getTop() + getTextHeight(stringBounder) + padding.getBottom();
			if (height > 0) {
				final URectangle rect = URectangle.build(end - start, height);
				ug.apply(backColor.bg()).draw(rect);
			}
		}

		final HColor lineColor = getStyle().value(PName.LineColor).asColor(colorSet);
		ug = ug.apply(lineColor);
		ug = ug.apply(UTranslate.dy(padding.getTop() + getTextHeight(stringBounder) / 2));

		if (widthTitle == 0) {
			final ULine line = ULine.hline(end - start);
			ug.draw(line);
		} else {
			if (padding.getLeft() > 1) {
				final ULine line1 = ULine.hline(padding.getLeft());
				ug.draw(line1);
			}
			final double x1 = padding.getLeft() + margin.getLeft() + widthTitle + margin.getRight();
			final double x2 = end - 1;
			final ULine line2 = ULine.hline(x2 - x1);
			ug.apply(UTranslate.dx(x1)).draw(line2);
		}
	}

	@Override
	public FingerPrint getFingerPrint(StringBounder stringBounder) {
		final double h = getFullHeightTask(stringBounder);
		final double end = timeScale.getEndingPosition(max);
		return new FingerPrint(0, getY(stringBounder).getCurrentValue(), end,
				getY(stringBounder).getCurrentValue() + h);
	}

	@Override
	public FingerPrint getFingerPrintNote(StringBounder stringBounder) {
		return null;
	}

	@Override
	public double getFullHeightTask(StringBounder stringBounder) {
		final ClockwiseTopRightBottomLeft padding = getStyle().getPadding();
		final ClockwiseTopRightBottomLeft margin = getStyle().getMargin();
		return margin.getTop() + padding.getTop() + getTextHeight(stringBounder) + padding.getBottom()
				+ margin.getBottom();
	}

	private double getTextHeight(StringBounder stringBounder) {
		return getTitle().calculateDimension(stringBounder).getHeight();
	}

	@Override
	public Real getY(StringBounder stringBounder) {
		return y;
	}

	@Override
	public TaskDraw getTrueRow() {
		return null;
	}

	@Override
	public void setColorsAndCompletion(CenterBorderColor colors, int completion, Url url, Display note,
			Stereotype noteStereotype) {
	}

	@Override
	public Task getTask() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getY(StringBounder stringBounder, GSide side) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getHeightMax(StringBounder stringBounder) {
		return getFullHeightTask(stringBounder);
	}

	@Override
	public double getX(StringBounder stringBounder, GSide side, GArrowType arrowType) {
		throw new UnsupportedOperationException();
	}

}
