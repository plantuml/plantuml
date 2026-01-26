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
 */
package net.sourceforge.plantuml.timingdiagram.graphic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.timingdiagram.TimeConstraint;
import net.sourceforge.plantuml.timingdiagram.TimeProjected;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingFormat;
import net.sourceforge.plantuml.timingdiagram.TimingNote;
import net.sourceforge.plantuml.timingdiagram.TimingRuler;
import net.sourceforge.plantuml.utils.Position;

public abstract class Panels implements TimeProjected {

	// ========== Common layout constants ==========
	protected static final double MARGIN_Y = 8;
	protected static final double MARGIN_X = 12;
	protected static final double BOTTOM_MARGIN = 10;
	protected static final double LEFT_PANEL_MIN_WIDTH = 5;

	protected final ISkinParam skinParam;
	protected final TimingRuler ruler;
	protected final int suggestedHeight;
	protected final Style style;

	private final List<TimeConstraint> constraints;
	private final List<TimingNote> notes;

	public Panels(TimingRuler ruler, ISkinParam skinParam, int suggestedHeight, Style style, List<TimingNote> notes,
			List<TimeConstraint> constraints) {
		this.ruler = ruler;
		this.skinParam = skinParam;
		this.suggestedHeight = suggestedHeight;
		this.style = style;
		this.notes = notes != null ? notes : new ArrayList<>();
		this.constraints = constraints != null ? constraints : new ArrayList<>();
	}

	// ========== Constraints management ==========

	public abstract void drawLeftPanel(UGraphic ug, double fullAvailableWidth);

	public abstract void drawRightPanel(UGraphic ug);

	public abstract double getFullHeight(StringBounder stringBounder);

	public abstract double getLeftPanelWidth(StringBounder stringBounder);

	protected final List<TimeConstraint> getConstraints() {
		return Collections.unmodifiableList(constraints);
	}

	protected double getConstraintDeltaY(TimeConstraint constraint) {
		return 0;
	}

	protected final void drawConstraints(UGraphic ug) {
		final List<TimeConstraint> allConstraints = getConstraints();
		for (int i = 0; i < allConstraints.size(); i++) {
			TimeConstraint constraint = allConstraints.get(i);
			boolean overlap = false;
			int delta = 0;
			for (int j = 0; j < i; j++) {
				if (constraint.getTick1().compareTo(allConstraints.get(j).getTick2()) < 0
				&& constraint.getTick2().compareTo(allConstraints.get(j).getTick1()) > 0) {
					overlap = true;
					delta = delta - 25; // à voir pour harmoniser avec getConstraintDeltaY
				}
			}
			if (overlap) {
				constraint.drawU(ug.apply(UTranslate.dy(delta)), ruler);
			}
			else {
				constraint.drawU(ug.apply(UTranslate.dy(getConstraintDeltaY(constraint))), ruler);	
			}
		}
	}

	protected double getHeightForConstraints(StringBounder stringBounder) {
		if (constraints.size() == 0)
			return 0;

		double result = 0;
		for (TimeConstraint constraint : constraints)
			result = Math.max(result, constraint.getConstraintHeight(stringBounder) - getConstraintDeltaY(constraint));

		return result;
	}

	protected final List<TimingNote> getNotes() {
		return Collections.unmodifiableList(notes);
	}

	protected final void drawNotes(UGraphic ug, Position position) {
		for (TimingNote note : notes)
			if (note.getPosition() == position) {
				final TimeTick when = note.getWhen();
				final double x = when == null ? 0 : ruler.getPosInPixel(when);
				note.drawU(ug.apply(UTranslate.dx(x)));
			}
	}

	protected final double getHeightForNotes(StringBounder stringBounder, Position position) {
		double height = 0;
		for (TimingNote note : notes)
			if (note.getPosition() == position)
				height = Math.max(height, note.getHeight(stringBounder));

		return height;
	}

	protected final Fashion getContext() {
		final HColor lineColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final HColor backgroundColor = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());

		return new Fashion(backgroundColor, lineColor).withStroke(getStroke());
	}

	protected final UStroke getStroke() {
		return style.getStroke();
	}

	protected final FontConfiguration getFontConfiguration() {
		return FontConfiguration.create(skinParam, style);
	}

	protected final TextBlock createTextBlock(String value) {
		final Display display = Display.getWithNewlines(skinParam.getPragma(), value);
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	final protected TextBlock getTextBlock(String value) {
		return createTextBlock(value);
	}

	final protected void drawHLineFromPoint(UGraphic ug, final XPoint2D start, final double length) {
		ug.apply(UTranslate.point(start)).draw(ULine.hline(length));
	}

	final protected void drawHorizontalBetweenTimes(UGraphic ug, double startTime, double endTime) {
		final double x1 = xOfTime(startTime);
		final double x2 = Math.min(ruler.getWidth(), xOfTime(endTime));

		final ULine hline = ULine.hline(x2 - x1);
		ug.apply(UTranslate.dx(x1)).draw(hline);
	}

	final protected double xOfTime(double time) {
		return ruler.getPosInPixel(new TimeTick(new BigDecimal(time), TimingFormat.DECIMAL));
	}

	final protected void drawVline(UGraphic ug, double x, double y, final ULine vline) {
		ug.apply(new UTranslate(x, y)).draw(vline);
	}

}
