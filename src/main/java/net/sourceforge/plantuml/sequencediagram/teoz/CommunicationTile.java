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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.asciiverse.ADimension2D;
import net.sourceforge.plantuml.asciiverse.AsciiBlock;
import net.sourceforge.plantuml.asciiverse.AsciiBlockMarginLR;
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.skin.rose.AbstractComponentRoseArrow;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class CommunicationTile extends AbstractCommunicationTile {

	private final LivingSpace livingSpace1;
	private final LivingSpace livingSpace2;
	private final LivingSpaces livingSpaces;
	private final Message message;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final YGauge yGauge;

	public Event getEvent() {
		return message;
	}

	@Override
	public String toString() {
		return super.toString() + " " + message;
	}

	public CommunicationTile(StringBounder stringBounder, LivingSpaces livingSpaces, Message message, Rose skin,
			ISkinParam skinParam, YGauge currentY) {
		super(stringBounder, skinParam, currentY);
		this.livingSpace1 = livingSpaces.get(message.getParticipant1());
		this.livingSpace2 = livingSpaces.get(message.getParticipant2());
		this.livingSpaces = livingSpaces;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;

		if (message.isCreate())
			livingSpace2.goCreate();

		final ArrowComponent comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());

		if (message.isParallel())
			this.yGauge = YGauge.create(currentY.getMin(), dim.getHeight());
		else
			this.yGauge = YGauge.create(currentY.getMax(), dim.getHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	public boolean isReverse(StringBounder stringBounder) {
		final Real point1 = livingSpace1.getPosC(stringBounder);
		final Real point2 = livingSpace2.getPosC(stringBounder);
		if (point1.getCurrentValue() > point2.getCurrentValue())
			return true;

		return false;
	}

	private boolean isCreate() {
		return message.isCreate();
	}

	private double getArrowThickness() {
		final UStroke result = skinParam.getThickness(LineParam.sequenceArrow, null);
		if (result == null)
			return 1;

		return result.getThickness();
	}

	private ArrowComponent getComponent(StringBounder stringBounder) {
		ArrowConfiguration arrowConfiguration = message.getArrowConfiguration();
		if (isReverse(stringBounder))
			arrowConfiguration = arrowConfiguration.reverse();

		arrowConfiguration = arrowConfiguration.withThickness(getArrowThickness());

		return skin.createComponentArrow(message.getUsedStyles(), arrowConfiguration, skinParam,
				message.getLabelNumbered());
	}

	private ArrowComponent getComponentMulticast(StringBounder stringBounder, boolean reverse) {
		ArrowConfiguration arrowConfiguration = message.getArrowConfiguration();
		if (reverse)
			arrowConfiguration = arrowConfiguration.reverse();

		arrowConfiguration = arrowConfiguration.withThickness(getArrowThickness());

		return skin.createComponentArrow(message.getUsedStyles(), arrowConfiguration, skinParam, Display.NULL);
	}

	@Override
	public double getContactPointRelative() {
		return getComponent(getStringBounder()).getYPoint(getStringBounder());
	}

	public static final double LIVE_DELTA_SIZE = 5;

	@Override
	final protected void callbackY_internal(TimeHook y) {
		super.callbackY_internal(y);
		if (message.isCreate())
			livingSpace2.goCreate(y.getValue());

		final AbstractComponentRoseArrow comp = (AbstractComponentRoseArrow) getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());

		final double arrowY = comp.getStartPoint(getStringBounder(), dim).getY();

		livingSpace1.addStepForLivebox(getEvent(), y.getValue() + arrowY);
		livingSpace2.addStepForLivebox(getEvent(), y.getValue() + arrowY);
	}

	// ASCII rendering. The message is drawn relative to the plan (already
	// translated to this tile's row by the caller): row 0 is where the label
	// starts, the arrow row comes right after it (see asciiDimension() below
	// for how many rows the label actually takes). Columns come from the
	// ASCII layout pass (LivingSpace.getAsciiLifeColumn()), never from the
	// pixel Real solver.
	@Override
	public void asciiDraw(InfinitePlan plan) {
		final int xa = (int) livingSpace1.getAsciiLifeColumn().getCurrentValue();
		final int xb = (int) livingSpace2.getAsciiLifeColumn().getCurrentValue();

		// message.getLabel() is itself an AsciiBlock (Display, see
		// ASCIIVERSE.md §18), wrapped in a 1-column left/right margin
		// (AsciiBlockMarginLR) so the label reads " this is a test " instead of
		// sitting flush against the lifelines it's centered between. Centering
		// reads the padded width back from asciiDimension() instead of
		// flattening the label to a String first, and the actual draw is
		// delegated to the label's own asciiDraw() rather than a hand-rolled
		// drawString(). A multi-line label draws one row per line starting at
		// row 0 — the arrow row is pushed down to asciiLabelRows() accordingly
		// (see below), so it no longer collides with a label that has more
		// than one line.
		final AsciiBlock label = asciiLabel();
		final ADimension2D labelDim = label.asciiDimension();
		if (labelDim.getWidth() > 0) {
			final int mid = (xa + xb) / 2;
			label.asciiDraw(plan.move(mid - labelDim.getWidth() / 2, 0));
		}

		final int arrowRow = asciiLabelRows();
		final int from = Math.min(xa, xb);
		final int to = Math.max(xa, xb);
		// Fill only the inside of the span: the source keeps its lifeline '|',
		// the target receives the arrowhead. This way the arrow starts on the
		// source lifeline and ends on the target lifeline without overrunning.
		// Dotted arrows (return messages, async replies...) get a dashed line,
		// mirroring the pixel ArrowConfiguration.isDotted() behavior.
		plan.drawHLine(from + 1, to - 1, arrowRow, message.getArrowConfiguration().isDotted());

		// The arrowhead stays plain ASCII even in Unicode mode, matching the
		// legacy asciiart.ComponentTextArrow behavior (only the line character
		// switches to box-drawing Unicode, never the arrowhead itself).
		if (xb >= xa)
			plan.move(xb - 1, arrowRow).drawChar('>');
		else
			plan.move(xb + 1, arrowRow).drawChar('<');
	}

	// The message's label, padded with a 1-column margin on each side
	// (AsciiBlockMarginLR): a plain, reusable decorator rather than ad hoc
	// column arithmetic at each call site (asciiDraw(), asciiLabelRows(),
	// asciiMessageWidth() all read the same padded block). An empty label
	// stays empty — the decorator only pads a block that actually has
	// content (width > 0), so an unlabeled message reserves no extra space.
	private AsciiBlock asciiLabel() {
		return message.getLabel().marginLR(1, 1);
	}

	// How many rows the label needs, at least 1: even an empty label keeps a
	// blank row above the arrow, matching the vertical spacing a plain
	// unlabeled message has always had. A multi-line Display (ASCIIVERSE.md
	// §18) simply pushes this past 1, which is what moves the arrow row and
	// grows asciiDimension() below — no other change needed on either end.
	private int asciiLabelRows() {
		return Math.max(1, asciiLabel().asciiDimension().getHeight());
	}

	// Total ASCII footprint of this tile: label rows + the arrow row itself
	// + one blank trailing row (the historical fixed "3 rows" from
	// ASCIIVERSE.md §9.2/§15, generalized from a constant into
	// asciiLabelRows() + 2). PlayingSpaceWithParticipants.asciiDraw() reads
	// this back to know how far to advance its Y counter for this tile,
	// instead of a hardcoded per-tile increment — the same "read the size
	// back off asciiDimension(), don't hardcode it at the call site" rule as
	// InfinitePlan.createNoteBox() (ASCIIVERSE.md §18).
	@Override
	public ADimension2D asciiDimension() {
		return new ADimension2D(asciiMessageWidth(), asciiLabelRows() + 2);
	}

	// ASCII counterpart of addConstraints(): the two lifeline columns must be
	// far enough apart to fit the message. Two parts, not one: the padded
	// label's own width (asciiLabel(), margin included), plus a flat +2 —
	// one "runway" column immediately next to each lifeline that must stay
	// free of the label block entirely. Without that +2, the centered block
	// lands flush with the lifeline columns themselves, so its own margin
	// cell coincides with the pipe character and gets overwritten by it —
	// the margin becomes invisible even though it's still being reserved and
	// drawn (learned the hard way: removing this +2 as "redundant with the
	// margin" silently cancelled the margin's visual effect, since both the
	// centering and the reservation shrank by the same amount). The two are
	// independent concerns: this +2 is pipe/lifeline clearance, the margin
	// is text-from-block-edge spacing.
	private int asciiMessageWidth() {
		return asciiLabel().asciiDimension().getWidth() + 2;
	}

	@Override
	public void asciiAddConstraints() {
		final int width = asciiMessageWidth();
		final Real point1 = livingSpace1.getAsciiPosC();
		final Real point2 = livingSpace2.getAsciiPosC();
		if (point1.getCurrentValue() > point2.getCurrentValue())
			point1.ensureBiggerThan(point2.addFixed(width));
		else
			point2.ensureBiggerThan(point1.addFixed(width));
	}

	// ASCII counterpart of getMinX()/getMaxX(): the two lifeline positions
	// this message touches, as composable Real — not yet resolved to columns
	// — exactly like the pixel getMinX()/getMaxX() compose Real via
	// RealUtils.min/max below. This lets an enclosing GroupingTile fold a
	// child's range into its own min/max *before* the ASCII RealLine compiles
	// (ASCIIVERSE.md), the same way the pixel constructor composes min/max
	// from each child's getMinX()/getMaxX() right after construction.
	@Override
	public Real getAsciiMinX() {
		return RealUtils.min(livingSpace1.getAsciiLifeColumn(), livingSpace2.getAsciiLifeColumn());
	}

	@Override
	public Real getAsciiMaxX() {
		return RealUtils.max(livingSpace1.getAsciiLifeColumn(), livingSpace2.getAsciiLifeColumn());
	}

	public void drawU(UGraphic ug) {
		if (YGauge.USE_ME)
			ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()));
		final String anchor1 = message.getPart1Anchor();
		final String anchor2 = message.getPart2Anchor();
		if (anchor1 != null || anchor2 != null)
			return;

		final StringBounder stringBounder = ug.getStringBounder();
		final ArrowComponent comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		double x1 = getPoint1(stringBounder).getCurrentValue();
		double x2 = getPoint2(stringBounder).getCurrentValue();
		drawMulticast(ug.apply(UTranslate.dy(comp.getPosArrow(stringBounder))));

		final Area area;
		if (isReverse(stringBounder)) {
			final int level1 = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			final int level2 = livingSpace2.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			if (level1 == 1)
				x1 -= LIVE_DELTA_SIZE;
			else if (level1 > 2)
				x1 += LIVE_DELTA_SIZE * (level1 - 2);

			x2 += LIVE_DELTA_SIZE * level2;
			area = Area.create(x1 - x2, dim.getHeight());
			ug = ug.apply(UTranslate.dx(x2));
			if (isCreate())
				livingSpace2.drawHead(ug, (Context2D) ug, VerticalAlignment.TOP, HorizontalAlignment.RIGHT);

		} else {
			final int level1 = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			int level2 = livingSpace2.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			if (level2 > 0)
				level2 = level2 - 2;

			x1 += LIVE_DELTA_SIZE * level1;
			x2 += LIVE_DELTA_SIZE * level2;
			area = Area.create(x2 - x1, dim.getHeight());
			ug = ug.apply(UTranslate.dx(x1));
			if (isCreate())
				livingSpace2.drawHead(ug.apply(UTranslate.dx(area.getDimensionToUse().getWidth())), (Context2D) ug,
						VerticalAlignment.TOP, HorizontalAlignment.LEFT);

		}
		comp.drawU(ug, area, (Context2D) ug);
	}

	private void drawMulticast(final UGraphic ug) {
		if (message.getMulticast().size() == 0)
			return;

		final StringBounder stringBounder = ug.getStringBounder();

		final double x1 = getPoint1(stringBounder).getCurrentValue();
		double dy = 2;
		for (Participant participant : message.getMulticast()) {
			final double x2 = livingSpaces.get(participant).getPosC(stringBounder).getCurrentValue();
			final boolean reverse = x2 < x1;
			final ArrowComponent comp = getComponentMulticast(stringBounder, reverse);
			final XDimension2D dim = comp.getPreferredDimension(stringBounder);
			final Area area = Area.create(Math.abs(x2 - x1), dim.getHeight());
			final UGraphic ug2 = ug.apply(UTranslate.dx(Math.min(x1, x2))).apply(UTranslate.dy(dy));
			dy += 2;
			comp.drawU(ug2, area, (Context2D) ug2);
		}

	}

	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		double height = dim.getHeight();
		if (isCreate())
			height = Math.max(height, livingSpace2.getHeadPreferredDimension(getStringBounder()).getHeight());

		return height;
	}

	public void addConstraints() {
		if (sequenceMessageSpan())
			return;

		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		final double width = dim.getWidth();

		Real point1 = getPoint1(getStringBounder());
		Real point2 = getPoint2(getStringBounder());
		if (isReverse(getStringBounder())) {
			final int level1 = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			final int level2 = livingSpace2.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			if (level1 > 0)
				point1 = point1.addFixed(-LIVE_DELTA_SIZE);

			point2 = point2.addFixed(level2 * LIVE_DELTA_SIZE);
			point1.ensureBiggerThan(point2.addFixed(width));
		} else {
			final int level2 = livingSpace2.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			if (level2 > 0)
				point2 = point2.addFixed(-LIVE_DELTA_SIZE);

			point2.ensureBiggerThan(point1.addFixed(width));
		}
	}

	private Real getPoint1(final StringBounder stringBounder) {
		return livingSpace1.getPosC(stringBounder);
	}

	private Real getPoint2(final StringBounder stringBounder) {
		if (message.isCreate()) {
			if (isReverse(stringBounder))
				return livingSpace2.getPosD(stringBounder);

			return livingSpace2.getPosB(stringBounder);
		}
		return livingSpace2.getPosC(stringBounder);
	}

	public Real getMinX() {
		final StringBounder stringBounder = getStringBounder();
		if (isReverse(stringBounder))
			return getPoint2(stringBounder);

		return getPoint1(stringBounder);
	}

	public Real getMaxX() {
		final StringBounder stringBounder = getStringBounder();
		final double width = getComponent(stringBounder).getPreferredDimension(stringBounder).getWidth();
		final Real leftPoint = isReverse(stringBounder) ? getPoint2(stringBounder) : getPoint1(stringBounder);
		final Real rightPoint = isReverse(stringBounder) ? getPoint1(stringBounder) : getPoint2(stringBounder);

		return RealUtils.max(rightPoint, leftPoint.addFixed(width));
	}

}
