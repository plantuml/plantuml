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
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.timingdiagram.PlayerPanels;
import net.sourceforge.plantuml.timingdiagram.TimeConstraint;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingNote;
import net.sourceforge.plantuml.timingdiagram.TimingRuler;
import net.sourceforge.plantuml.utils.Position;

public abstract class AbstractFooPanel implements PlayerPanels {

	protected final ISkinParam skinParam;
	protected final TimingRuler ruler;
	protected final int suggestedHeight;
	protected final Style style;

	private final List<TimeConstraint> constraints;
	private final List<TimingNote> notes;

	public AbstractFooPanel(TimingRuler ruler, ISkinParam skinParam, int suggestedHeight, Style style,
			List<TimingNote> notes, List<TimeConstraint> constraints) {
		this.ruler = ruler;
		this.skinParam = skinParam;
		this.suggestedHeight = suggestedHeight;
		this.style = style;
		this.notes = notes != null ? notes : new ArrayList<>();
		this.constraints = constraints != null ? constraints : new ArrayList<>();
	}

	// ========== Constraints management ==========

	protected final List<TimeConstraint> getConstraints() {
		return Collections.unmodifiableList(constraints);
	}

	protected double getConstraintDeltaY(TimeConstraint constraint) {
		return 0;
	}

	protected final void drawConstraints(UGraphic ug) {
		for (TimeConstraint constraint : getConstraints())
			constraint.drawU(ug.apply(UTranslate.dy(getConstraintDeltaY(constraint))), ruler);

	}

	protected double getHeightForConstraints(StringBounder stringBounder) {
		if (constraints.size() == 0)
			return 0;

		double result = 0;
		for (TimeConstraint constraint : constraints)
			result = Math.max(result, constraint.getConstraintHeight(stringBounder));

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

	// Note: Subclasses can override getTextBlock() if they need different behavior
	protected TextBlock getTextBlock(String value) {
		return createTextBlock(value);
	}

}
