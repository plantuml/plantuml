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
import java.util.List;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.timingdiagram.ChangeState;
import net.sourceforge.plantuml.timingdiagram.TimeConstraint;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingNote;
import net.sourceforge.plantuml.timingdiagram.TimingRuler;
import net.sourceforge.plantuml.utils.Position;

public abstract class AbstractFooRibbon extends AbstractFooPanel {

	private final List<ChangeState> changes = new ArrayList<>();

	private String initialState;
	private Colors initialColors;

	protected AbstractFooRibbon(TimingRuler ruler, ISkinParam skinParam, int suggestedHeight, Style style,
			List<TimingNote> notes, List<TimeConstraint> constraints) {
		super(ruler, skinParam, suggestedHeight == 0 ? 24 : suggestedHeight, style, notes, constraints);
	}

	public final IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		if (tick == null)
			return null;
		final double x = ruler.getPosInPixel(tick);
		final double y = getHeightForConstraints(stringBounder) + getHeightForNotes(stringBounder, Position.TOP)
				+ getHeightForTopComment(stringBounder) + getRibbonHeight() / 2;
		for (ChangeState change : changes)
			if (change.getWhen().compareTo(tick) == 0)
				return new IntricatedPoint(new XPoint2D(x, y), new XPoint2D(x, y));

		return new IntricatedPoint(new XPoint2D(x, y - getRibbonHeight() / 2),
				new XPoint2D(x, y + getRibbonHeight() / 2));
	}

	public final void addChange(ChangeState change) {
		this.changes.add(change);
	}

	protected final double getPosInPixel(ChangeState change) {
		return ruler.getPosInPixel(change.getWhen());
	}

	@Override
	public final void drawLeftPanel(UGraphic ug, double fullAvailableWidth) {
	}

	@Override
	public final double getLeftPanelWidth(StringBounder stringBounder) {
		return getInitialWidth(stringBounder);
	}

	@Override
	public final void drawRightPanel(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final UGraphic ugRibbon = ug.apply(UTranslate.dy(getHeightForConstraints(stringBounder)
				+ getHeightForTopComment(stringBounder) + getHeightForNotes(stringBounder, Position.TOP)));

		drawBeforeZeroState(ugRibbon);
		drawBeforeZeroStateLabel(ugRibbon.apply(UTranslate.dy(getRibbonHeight() / 2)));
		drawStates(ugRibbon);
		drawStatesLabels(ugRibbon.apply(UTranslate.dy(getRibbonHeight() / 2)));

		drawConstraints(ug.apply(UTranslate.dy(getHeightForConstraints(stringBounder) / 2)));

		drawNotes(ug, Position.TOP);
		drawNotes(ug.apply(UTranslate.dy(getHeightForConstraints(stringBounder) + getRibbonHeight()
				+ getHeightForNotes(stringBounder, Position.TOP))), Position.BOTTOM);
	}

	private double getInitialWidth(final StringBounder stringBounder) {
		if (initialState == null)
			return 0;

		return createTextBlock(initialState).calculateDimension(stringBounder).getWidth() + 24;
	}

	protected abstract void drawHexa(UGraphic ug, double len, ChangeState change);

	protected abstract void drawPentaA(UGraphic ug, double len, ChangeState change);

	protected abstract void drawPentaB(UGraphic ug, double len, ChangeState change);

	protected final void drawFlat(UGraphic ug, double len, ChangeState change) {
		final ULine line = ULine.hline(len);
		change.getContext(skinParam, style).apply(ug).apply(UTranslate.dy(getRibbonHeight() / 2)).draw(line);
	}

	protected final double getRibbonHeight() {
		return suggestedHeight;
	}

	protected final double getMarginX() {
		return 12;
	}

	public final void setInitialState(String initialState, Colors initialColors) {
		this.initialState = initialState;
		this.initialColors = initialColors;
	}

	protected final String getInitialState() {
		return initialState;
	}

	protected final Colors getInitialColors() {
		return initialColors;
	}

	protected final List<ChangeState> getChanges() {
		return changes;
	}

	protected double getBottomMargin() {
		return 10;
	}

	private void drawBeforeZeroState(UGraphic ug) {
		if (initialState == null)
			return;
		final StringBounder stringBounder = ug.getStringBounder();
		if (changes.size() == 0) {
			drawSingle(ug.apply(UTranslate.dx(-getInitialWidth(stringBounder))),
					getInitialWidth(stringBounder) + ruler.getWidth());
		} else {
			final double a = getPosInPixel(changes.get(0));
			if (ChangeState.isFlat(initialState))
				drawFlat(ug.apply(UTranslate.dx(-getInitialWidth(stringBounder))), getInitialWidth(stringBounder) + a,
						changes.get(0));
			else
				drawPentaA(ug.apply(UTranslate.dx(-getInitialWidth(stringBounder))), getInitialWidth(stringBounder) + a,
						changes.get(0));
		}
	}

	private void drawBeforeZeroStateLabel(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		if (initialState != null && ChangeState.isFlat(initialState) == false) {
			final TextBlock initial = createTextBlock(initialState);
			final XDimension2D dimInital = initial.calculateDimension(stringBounder);
			initial.drawU(ug.apply(new UTranslate(-getMarginX() - dimInital.getWidth(), -dimInital.getHeight() / 2)));
		}
	}

	private void drawSingle(UGraphic ug, double len) {
		final HColor back = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
		final HColor line = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final Fashion context = new Fashion(back, back).withStroke(style.getStroke());
		ug = context.apply(ug);

		final ULine border = ULine.hline(len);
		final double height = getRibbonHeight();

		if (ChangeState.isFlat(initialState)) {
			ug = ug.apply(line);
			ug.apply(UTranslate.dy(height / 2)).draw(border);
			return;
		}

		final URectangle rect = URectangle.build(len, height);
		ug.draw(rect);

		ug = ug.apply(line);
		ug.draw(border);
		ug.apply(UTranslate.dy(height)).draw(border);
	}

	private void drawStates(UGraphic ug) {
		for (int i = 0; i < changes.size() - 1; i++) {
			final double a = getPosInPixel(changes.get(i));
			final double b = getPosInPixel(changes.get(i + 1));
			assert b > a;
			if (changes.get(i).isFlat())
				drawFlat(ug.apply(UTranslate.dx(a)), b - a, changes.get(i));
			else if (changes.get(i).isCompletelyHidden() == false)
				drawHexa(ug.apply(UTranslate.dx(a)), b - a, changes.get(i));
		}
		if (changes.size() >= 1) {
			final ChangeState last = changes.get(changes.size() - 1);
			final double a = getPosInPixel(last);
			if (last.isFlat())
				drawFlat(ug.apply(UTranslate.dx(a)), ruler.getWidth() - a, last);
			else if (last.isCompletelyHidden() == false)
				drawPentaB(ug.apply(UTranslate.dx(a)), ruler.getWidth() - a, last);
		}
	}

	private void drawStatesLabels(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (int i = 0; i < changes.size(); i++) {
			final ChangeState change = changes.get(i);
			final double x = ruler.getPosInPixel(change.getWhen());
			if (change.isBlank() == false && change.isCompletelyHidden() == false && change.isFlat() == false) {
				final TextBlock state = createTextBlock(change.getState());
				final XDimension2D dim = state.calculateDimension(stringBounder);
				final double xtext;
				if (i == changes.size() - 1) {
					xtext = x + getMarginX();
				} else {
					final double x2 = ruler.getPosInPixel(changes.get(i + 1).getWhen());
					xtext = (x + x2) / 2 - dim.getWidth() / 2;
				}
				state.drawU(ug.apply(new UTranslate(xtext, -dim.getHeight() / 2)));
			}
			final TextBlock commentTopBlock = getCommentTopBlock(change);
			final XDimension2D dimComment = commentTopBlock.calculateDimension(stringBounder);
			commentTopBlock
					.drawU(ug.apply(new UTranslate(x + getMarginX(), -getRibbonHeight() / 2 - dimComment.getHeight())));
		}
	}

	private TextBlock getCommentTopBlock(final ChangeState change) {
		if (change.getComment() == null)
			return TextBlockUtils.empty(0, 0);

		return createTextBlock(change.getComment());
	}

	protected double getHeightForTopComment(StringBounder stringBounder) {
		double result = 0;
		for (ChangeState change : changes)
			result = Math.max(result, getCommentTopBlock(change).calculateDimension(stringBounder).getHeight());

		return result;
	}

	protected final Fashion getContextWithInitialColors(ChangeState change) {
		Fashion context = change.getContext(skinParam, style);
		final HColor back = initialColors.getColor(ColorType.BACK);
		final HColor line = initialColors.getColor(ColorType.LINE);
		if (back != null)
			context = context.withBackColor(back);
		if (line != null)
			context = context.withForeColor(line);
		return context;
	}

}
