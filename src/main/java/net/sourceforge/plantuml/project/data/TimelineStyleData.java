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
package net.sourceforge.plantuml.project.data;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.project.GanttStyle;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;

/**
 * Value object containing timeline styling information.
 */
public class TimelineStyleData {

	private final GanttStyle ganttStyle;
	private final HColorSet colorSet;
	private final ISkinParam skinParam;

	public TimelineStyleData(ISkinParam skinParam, GanttStyle ganttStyle, HColorSet colorSet) {
		this.skinParam = skinParam;
		this.ganttStyle = ganttStyle;
		this.colorSet = colorSet;
	}

	public ISkinParam getSkinParam() {
		return skinParam;
	}

	public Pragma getPragma() {
		return skinParam.getPragma();
	}

	public double getFontSizeDay() {
		return getStyleDay().value(PName.FontSize).asDouble();
	}

	public double getFontSizeMonth() {
		return ganttStyle.getStyle(SName.timeline, SName.month).value(PName.FontSize).asDouble();
	}

	public double getFontSizeYear() {
		return ganttStyle.getStyle(SName.timeline, SName.year).value(PName.FontSize).asDouble();
	}

	public UFont getFont(SName param) {
		return ganttStyle.getStyle(SName.timeline, param).getUFont();
	}

	public HColor getClosedBackgroundColor() {
		return ganttStyle.getStyle(SName.closed).value(PName.BackGroundColor).asColor(colorSet);
	}

	public HColor getClosedFontColor() {
		return ganttStyle.getStyle(SName.closed).value(PName.FontColor).asColor(colorSet);
	}

	public HColor getOpenFontColor() {
		return ganttStyle.getStyle(SName.timeline).value(PName.FontColor).asColor(colorSet);
	}

	public HColor getLineColor() {
		return ganttStyle.getStyle(SName.timeline).value(PName.LineColor).asColor(colorSet);
	}

	public HColorSet getColorSet() {
		return colorSet;
	}

	public UGraphic applyVerticalSeparatorStyle(UGraphic ug) {
		final Style style = ganttStyle.getStyle(SName.verticalSeparator);
		final HColor color = style.value(PName.LineColor).asColor(colorSet);
		final UStroke stroke = style.getStroke();
		return ug.apply(color).apply(stroke);
	}

	public double getCellWidth() {
		final double w = getStyleDay().value(PName.FontSize).asDouble();
		return w * 1.6;
	}

	private Style getStyleDay() {
		return ganttStyle.getStyle(SName.timeline, SName.day);
	}

}
