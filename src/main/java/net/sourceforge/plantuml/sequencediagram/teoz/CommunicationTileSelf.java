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

import java.util.Iterator;

import net.sourceforge.plantuml.asciiverse.ADimension2D;
import net.sourceforge.plantuml.asciiverse.AsciiBlock;
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.utils.Log;

public class CommunicationTileSelf extends AbstractCommunicationTile {

	private final LivingSpace livingSpace1;
	private final Message message;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final LivingSpaces livingSpaces;
	private final YGauge yGauge;

	public Event getEvent() {
		return message;
	}

	@Override
	public double getContactPointRelative() {
		return getComponent(getStringBounder()).getYPoint(getStringBounder());
	}

	public CommunicationTileSelf(StringBounder stringBounder, LivingSpace livingSpace1, Message message, Rose skin,
			ISkinParam skinParam, LivingSpaces livingSpaces, YGauge currentY) {
		super(stringBounder, skinParam, currentY);
		this.livingSpace1 = livingSpace1;
		this.livingSpaces = livingSpaces;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;
		if (message.isParallel())
			this.yGauge = YGauge.createParallel(currentY, getContactPointRelative(), getPreferredHeight());
		else
			this.yGauge = YGauge.createWithContact(currentY, getContactPointRelative(), getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	private ArrowComponent getComponent(StringBounder stringBounder) {
		ArrowConfiguration arrowConfiguration = message.getArrowConfiguration();
		arrowConfiguration = arrowConfiguration.self();
		final ArrowComponent comp = skin.createComponentArrow(message.getUsedStyles(), arrowConfiguration, skinParam,
				message.getLabelNumbered());
		return comp;
	}

	@Override
	final public void onGaugeResolved() {
		final ArrowComponent comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		final XPoint2D p1 = comp.getStartPoint(getStringBounder(), dim);
		final XPoint2D p2 = comp.getEndPoint(getStringBounder(), dim);
		final double y = getYGauge().getMin().getCurrentValue();

		if (message.isActivate())
			livingSpace1.addStepForLivebox(getEvent(), y + p2.getY());
		else if (message.isDeactivate())
			livingSpace1.addStepForLivebox(getEvent(), y + p1.getY());
		else if (message.isDestroy())
			livingSpace1.addStepForLivebox(getEvent(), y + p2.getY());

	}

	public void drawU(UGraphic ug) {
		// Self-translate prologue: absolute gauge position
		ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()));
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		double x1 = getMinX().getCurrentValue();
		final int levelIgnore = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_ACTIVATE);
		final int levelConsidere = livingSpace1.getLevelAt(this, EventsHistoryMode.CONSIDER_FUTURE_DEACTIVATE);
		Log.info(() -> "CommunicationTileSelf::drawU levelIgnore=" + levelIgnore + " levelConsidere=" + levelConsidere);
		if (!isReverseDefine()) {
			x1 += CommunicationTile.LIVE_DELTA_SIZE * levelIgnore;
			if (levelIgnore < levelConsidere) {
				x1 += CommunicationTile.LIVE_DELTA_SIZE * (levelConsidere - levelIgnore);
			}
		}

		final Area area = Area.create(dim.getWidth(), dim.getHeight());
		// if (message.isActivate()) {
		// area.setDeltaX1(CommunicationTile.LIVE_DELTA_SIZE);
		// } else if (message.isDeactivate()) {
		// // area.setDeltaX1(CommunicationTile.LIVE_DELTA_SIZE);
		// // x1 += CommunicationTile.LIVE_DELTA_SIZE * levelConsidere;
		// }
		area.setDeltaX1((levelIgnore - levelConsidere) * CommunicationTile.LIVE_DELTA_SIZE);
		area.setLevel(levelIgnore);
		area.setLiveDeltaSize(CommunicationTile.LIVE_DELTA_SIZE);
		ug = ug.apply(UTranslate.dx(x1));
		comp.drawU(ug, area, (Context2D) ug);
	}

	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return dim.getHeight();
	}

	public void addConstraints() {
		if (sequenceMessageSpan())
			return;

		if (isReverseDefine()) {
			final LivingSpace previous = getPrevious();
			if (previous != null) {
				livingSpace1.getPosC(getStringBounder())
						.ensureBiggerThan(previous.getPosC2(getStringBounder()).addFixed(getCompWidth()));
			}
		} else {
			final LivingSpace next = getNext();
			if (next != null) {
				next.getPosC(getStringBounder()).ensureBiggerThan(getMaxX());
			}
		}
	}

	private boolean isReverseDefine() {
		return message.getArrowConfiguration().isReverseDefine();
	}

	private LivingSpace getPrevious() {
		LivingSpace previous = null;
		for (Iterator<LivingSpace> it = livingSpaces.values().iterator(); it.hasNext();) {
			final LivingSpace current = it.next();
			if (current == livingSpace1) {
				return previous;
			}
			previous = current;
		}
		return null;
	}

	private LivingSpace getNext() {
		for (Iterator<LivingSpace> it = livingSpaces.values().iterator(); it.hasNext();) {
			final LivingSpace current = it.next();
			if (current == livingSpace1 && it.hasNext()) {
				return it.next();
			}
		}
		return null;
	}

	private Real getPoint1(final StringBounder stringBounder) {
		return livingSpace1.getPosC(stringBounder);
	}

	public Real getMinX() {
		if (isReverseDefine()) {
			double liveDeltaWidthAdjustment = livingSpace1.getLevelAt(this,
					EventsHistoryMode.IGNORE_FUTURE_ACTIVATE) > 0 ? CommunicationTile.LIVE_DELTA_SIZE : 0.0;
			return livingSpace1.getPosC(getStringBounder()).addFixed(-getCompWidth() - liveDeltaWidthAdjustment);
		}
		return getPoint1(getStringBounder());
	}

	public Real getMaxX() {
		if (isReverseDefine()) {
			return livingSpace1.getPosC2(getStringBounder());
		}
		return livingSpace1.getPosC2(getStringBounder()).addFixed(getCompWidth());
	}

	private double getCompWidth() {
		final Component comp = getComponent(getStringBounder());
		return comp.getPreferredDimension(getStringBounder()).getWidth();
	}

	// ---------------------------------------------------------------------
	// ASCII rendering.
	//
	// Idea copied from the legacy asciiart.ComponentTextSelfArrow (never its
	// code, which belongs to the doomed asciiart package): a small loop box
	// sits to the side of the lifeline --
	//   ,----.
	//   |    |    label
	//   <----'
	// -- with the label starting one gap column after the box's own far edge.
	// Reused wholesale from InfinitePlan: the box corners (getTopRightChar()/
	// getBottomRightChar()/... -- widened to public for exactly this caller,
	// see InfinitePlan), the line characters (getHLineChar()/getVLineChar()),
	// and the dotted-line helper (drawHLine(..., dotted)) for isDotted()
	// messages, exactly like CommunicationTile.asciiDraw() already does for a
	// normal message's arrow.
	// ---------------------------------------------------------------------

	// The loop box itself, excluding any label: 5 columns wide (the
	// near-lifeline dash/arrowhead column, two more dashes, then the far
	// corner/vertical-connector column), mirroring the legacy
	// "----." / "|" / "<---'" shape one-for-one.
	private static final int ASCII_SELF_BOX_WIDTH = 5;

	// One blank column between the box's own far edge and the label -- the
	// same kind of "runway" column CommunicationTile.asciiMessageWidth()
	// reserves next to each lifeline, here reserved once
	// rather than on both sides since the loop only has one open side.
	private static final int ASCII_SELF_LABEL_GAP = 1;

	// The message's label, unwrapped (no AsciiBlockMarginLR): unlike
	// CommunicationTile's centered label, this one is drawn flush after the
	// loop box, never on a lifeline column, so there is no margin-cell/pipe
	// collision to guard against.
	private AsciiBlock asciiLabel() {
		return message.getLabel().marginLR(1, 1);
	}

	// At least 1, exactly like CommunicationTile.asciiLabelRows(): a
	// label-less self-message still gets one row for its (empty) label, so
	// the loop keeps its historical 3-row minimum height.
	private int asciiLabelRows() {
		return Math.max(1, asciiLabel().asciiDimension().getHeight());
	}

	// How far the loop (box, plus label if any) reaches beyond the lifeline
	// column, in whichever direction it opens. An empty label reserves only
	// the box itself, the same "don't pad what has no content" rule
	// AsciiBlockMarginLR already applies.
	private int asciiSelfWidth() {
		final int labelWidth = asciiLabel().asciiDimension().getWidth();
		if (labelWidth == 0)
			return ASCII_SELF_BOX_WIDTH;

		return ASCII_SELF_BOX_WIDTH + ASCII_SELF_LABEL_GAP + labelWidth;
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		final int xa = (int) livingSpace1.getAsciiLifeColumn().getCurrentValue();
		final boolean dotted = message.getArrowConfiguration().isDotted();
		final int rows = asciiLabelRows();
		final int bottomRow = rows + 1;
		final AsciiBlock label = asciiLabel();
		final int labelWidth = label.asciiDimension().getWidth();

		if (isReverseDefine()) {
			// The loop opens to the LEFT of the lifeline: the box's far corner sits
			// at xa - ASCII_SELF_BOX_WIDTH, the arrowhead points back INTO the
			// lifeline (rightward, '>'), and the label -- if any -- grows leftward
			// from the box, ending one gap column before it.
			final int nearCol = xa - 1; // dash/arrowhead column closest to the lifeline
			final int farCol = xa - ASCII_SELF_BOX_WIDTH; // the box's own far corner column

			plan.drawHLine(farCol + 1, nearCol, 0, dotted);
			plan.move(farCol, 0).drawChar(plan.getTopLeftChar());

			for (int r = 1; r <= rows; r++)
				plan.move(farCol, r).drawChar(plan.getVLineChar());

			plan.drawHLine(farCol + 1, nearCol, bottomRow, dotted);
			plan.move(farCol, bottomRow).drawChar(plan.getBottomLeftChar());
			plan.move(nearCol, bottomRow).drawChar('>');

			if (labelWidth > 0)
				label.asciiDraw(plan.move(farCol - ASCII_SELF_LABEL_GAP - labelWidth, 1));
		} else {
			// The loop opens to the RIGHT of the lifeline (the common case): the
			// box's far corner sits at xa + ASCII_SELF_BOX_WIDTH, the arrowhead
			// points back into the lifeline (leftward, '<'), and the label -- if
			// any -- starts one gap column after the box.
			final int nearCol = xa + 1; // dash/arrowhead column closest to the lifeline
			final int farCol = xa + ASCII_SELF_BOX_WIDTH; // the box's own far corner column

			plan.drawHLine(nearCol, farCol - 1, 0, dotted);
			plan.move(farCol, 0).drawChar(plan.getTopRightChar());

			for (int r = 1; r <= rows; r++)
				plan.move(farCol, r).drawChar(plan.getVLineChar());

			plan.drawHLine(nearCol, farCol - 1, bottomRow, dotted);
			plan.move(farCol, bottomRow).drawChar(plan.getBottomRightChar());
			plan.move(nearCol, bottomRow).drawChar('<');

			if (labelWidth > 0)
				label.asciiDraw(plan.move(farCol + ASCII_SELF_LABEL_GAP, 1));
		}
	}

	// Total ASCII footprint: the loop's own width (box, plus label if any)
	// and label rows + 2 (top row, label rows, bottom row) -- the exact same
	// formula as CommunicationTile.asciiDimension(), generalized the same way
	// for a multi-line label.
	@Override
	public ADimension2D asciiDimension() {
		return new ADimension2D(asciiSelfWidth(), asciiLabelRows() + 2);
	}

	// ASCII counterpart of addConstraints() above: reserves room for the loop
	// on whichever side it opens, pushing the neighbouring participant (the
	// previous one if the loop opens left, the next one if it opens right)
	// far enough away that the loop never overlaps it. Reuses the existing
	// getPrevious()/getNext() helpers as-is -- they only walk the
	// LivingSpaces map, no pixel-specific state involved.
	@Override
	public void asciiAddConstraints() {
		final int width = asciiSelfWidth();
		if (isReverseDefine()) {
			final LivingSpace previous = getPrevious();
			if (previous != null)
				livingSpace1.getAsciiPosC().ensureBiggerThan(previous.getAsciiPosC().addFixed(width));
		} else {
			final LivingSpace next = getNext();
			if (next != null)
				next.getAsciiPosC().ensureBiggerThan(livingSpace1.getAsciiPosC().addFixed(width));
		}
	}

	// ASCII counterpart of getMinX()/getMaxX(): composable Real (not yet
	// resolved to columns), so an enclosing GroupingTile can fold this tile's
	// range into its own frame span before the ASCII RealLine compiles --
	// same contract as CommunicationTile.getAsciiMinX()/getAsciiMaxX().
	@Override
	public Real getAsciiMinX() {
		if (isReverseDefine())
			return livingSpace1.getAsciiPosC().addFixed(-asciiSelfWidth());

		return livingSpace1.getAsciiPosC();
	}

	@Override
	public Real getAsciiMaxX() {
		if (isReverseDefine())
			return livingSpace1.getAsciiPosC();

		return livingSpace1.getAsciiPosC().addFixed(asciiSelfWidth());
	}

}
