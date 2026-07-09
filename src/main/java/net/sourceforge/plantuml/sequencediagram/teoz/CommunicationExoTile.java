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
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.ComponentRoseArrow;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class CommunicationExoTile extends AbstractTile {

	private final LivingSpace livingSpace;
	private final MessageExo message;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final TileArguments tileArguments;
	private final YGauge yGauge;

	public Event getEvent() {
		return message;
	}

	public CommunicationExoTile(LivingSpace livingSpace, MessageExo message, Rose skin, ISkinParam skinParam,
			TileArguments tileArguments, YGauge currentY) {
		super(tileArguments.getStringBounder(), currentY);
		this.tileArguments = tileArguments;
		this.livingSpace = livingSpace;
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

	@Override
	public double getContactPointRelative() {
		return getComponent(getStringBounder()).getYPoint(getStringBounder());
	}

	private ArrowComponent getComponent(StringBounder stringBounder) {
		ArrowConfiguration arrowConfiguration = message.getArrowConfiguration();
		if (message.getType().getDirection() == -1)
			arrowConfiguration = arrowConfiguration.reverse();

		final ArrowComponent comp = skin.createComponentArrow(message.getUsedStyles(), arrowConfiguration, skinParam,
				message.getLabelNumbered());
		return comp;
	}

	public void drawU(UGraphic ug) {
		// Self-translate prologue: absolute gauge position
		ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()));
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		double x1 = getPoint1Value(stringBounder);
		double x2 = getPoint2Value(stringBounder);

		final double textDeltaX = isFromLeftBorderMessage()
																? getPoint1(stringBounder).getCurrentValue() - x1
																: isFromRightBorderMessage()
																		? getPoint2(stringBounder).getCurrentValue() - x2
																		: 0;

		final int level = livingSpace.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
		if (level > 0) {
			if (message.getType().isRightBorder())
				x1 += CommunicationTile.LIVE_DELTA_SIZE * level;
			else
				x2 += CommunicationTile.LIVE_DELTA_SIZE * (level - 2);
		}

		final ArrowConfiguration arrowConfiguration = message.getArrowConfiguration();
		final MessageExoType type = message.getType();
		if (arrowConfiguration.getDecoration1() == ArrowDecoration.CIRCLE && type == MessageExoType.FROM_LEFT)
			x1 += ComponentRoseArrow.diamCircle / 2 + 2;

		if (arrowConfiguration.getDecoration2() == ArrowDecoration.CIRCLE && type == MessageExoType.TO_LEFT)
			x1 += ComponentRoseArrow.diamCircle / 2 + 2;

		if (arrowConfiguration.getDecoration2() == ArrowDecoration.CIRCLE && type == MessageExoType.TO_RIGHT)
			x2 -= ComponentRoseArrow.diamCircle / 2 + 2;

		if (arrowConfiguration.getDecoration1() == ArrowDecoration.CIRCLE && type == MessageExoType.FROM_RIGHT)
			x2 -= ComponentRoseArrow.diamCircle / 2 + 2;

		final Area area = Area.create(x2 - x1, dim.getHeight());
		area.setTextDeltaX(textDeltaX);
		ug = ug.apply(UTranslate.dx(x1));
		comp.drawU(ug, area, (Context2D) ug);
	}

	private boolean isShortArrow() {
		return message.isShortArrow();
	}

	@Override
	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return dim.getHeight();
	}

	private double getPreferredWidth(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim.getWidth();
	}

	public void addConstraints() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		final double width = dim.getWidth();

		if (message.getType().isRightBorder()) {
		} else {
			livingSpace.getPosC(getStringBounder()).ensureBiggerThan(tileArguments.getXOrigin().addFixed(width));
		}

		// final Real point1 = getPoint1(stringBounder);
		// if (message.getType().isRightBorder()) {
		// final Real point2 = point1.addFixed(width);
		// } else {
		// final Real point2 = getPoint2(stringBounder);
		// if (point1.getCurrentValue() < point2.getCurrentValue()) {
		// point2.ensureBiggerThan(point1.addFixed(width));
		// } else {
		// point1.ensureBiggerThan(point2.addFixed(width));
		// }
		// }
	}

	@Override
	final protected void callbackY_internal(TimeHook y) {
		super.callbackY_internal(y);
		final ArrowComponent comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		final double arrowY = comp.getStartPoint(getStringBounder(), dim).getY();

		livingSpace.addStepForLivebox(getEvent(), y.getValue() + arrowY);

	}

	private Real getPoint1(final StringBounder stringBounder) {
		if (message.getType().isRightBorder())
			return livingSpace.getPosC(stringBounder);

		return livingSpace.getPosC(stringBounder).addFixed(-getPreferredWidth(stringBounder));
	}

	private Real getPoint2(final StringBounder stringBounder) {
		if (message.getType().isLeftBorder())
			return livingSpace.getPosC(stringBounder);

		return getPoint1(stringBounder).addFixed(getPreferredWidth(stringBounder));
	}

	private double getPoint1Value(final StringBounder stringBounder) {
		if (isFromLeftBorderMessage())
			return tileArguments.getBorder1();

		return getPoint1(stringBounder).getCurrentValue();
	}

	private double getPoint2Value(final StringBounder stringBounder) {
		if (isFromRightBorderMessage())
			return tileArguments.getBorder2();

		return getPoint2(stringBounder).getCurrentValue();
	}

	public Real getMinX() {
		return getPoint1(getStringBounder());
	}

	public Real getMaxX() {
		return getPoint2(getStringBounder());
	}

	final public double getMiddleX() {
		if (!isFromLeftBorderMessage())
			return super.getMiddleX();

		final double min = getPoint1Value(getStringBounder());

		final double max = isFromLeftBorderMessage()
												? min + getPreferredWidth(getStringBounder())
												: getPoint2Value(getStringBounder());

		return (min + max) / 2;
	}

	public boolean isFromRightBorderMessage() {
		return message.getType().isRightBorder() && !isShortArrow();
	}

	public boolean isFromLeftBorderMessage() {
		return message.getType().isLeftBorder() && !isShortArrow();
	}

	// ---------------------------------------------------------------------
	// ASCII rendering.
	//
	// An exo message draws an arrow between the participant's own lifeline
	// and either the diagram's outer edge (the common case: "A->?"/"?->E",
	// spanning past every OTHER participant to reach the border, exactly like
	// the pixel drawU() above uses tileArguments.getBorder1()/getBorder2())
	// or, for a short arrow (message.isShortArrow(), the compact "->o"/"o<-"
	// syntax), a small local stub next to the participant instead -- mirroring
	// the pixel isFromLeftBorderMessage()/isFromRightBorderMessage() split
	// above, which is exactly "not a short arrow".
	//
	// Idea reused from CommunicationTile.asciiDraw() (never its code, since
	// there is no shared superclass method to call into): the participant's
	// own lifeline column is left untouched by the tile -- filled in
	// afterwards by PlayingSpaceWithParticipants' lifeline-fill pass -- and
	// the arrowhead lands one column short of it, on whichever end is the
	// message's target. The diagram edge is not a lifeline column (nothing
	// else ever draws or fills it), so it needs no such reservation: the
	// arrow may be drawn flush onto it, including the arrowhead when the edge
	// is the target.
	// ---------------------------------------------------------------------

	// How far outside the outermost participant's own box the diagram edge
	// sits, for a non-short exo message -- the ASCII analogue of
	// GroupingTile.ASCII_FRAME_MARGIN, a fixed breathing-room constant rather
	// than anything derived from the label (the border position does not
	// depend on THIS message's label width; only a short arrow's local stub
	// does, see asciiShortWidth() below).
	private static final int ASCII_EXO_MARGIN = 2;

	// The message's label, padded with a 1-column margin on each side, exactly
	// like CommunicationTile.asciiLabel() -- same reason:
	// centering it flush against a lifeline/edge column would let a
	// lifeline-fill pass or the arrow's own dashes swallow the margin.
	private AsciiBlock asciiLabel() {
		return message.getLabel().marginLR(1, 1);
	}

	private int asciiLabelRows() {
		return Math.max(1, asciiLabel().asciiDimension().getHeight());
	}

	// A short arrow's local stub width: just enough to fit the padded label,
	// or a small fixed minimum if there is none -- there is no lifeline on the
	// far end to leave room for, unlike CommunicationTileSelf's loop, so no
	// extra "runway" column is needed here.
	private static final int ASCII_EXO_SHORT_MIN_WIDTH = 3;

	private int asciiShortWidth() {
		return Math.max(ASCII_EXO_SHORT_MIN_WIDTH, asciiLabel().asciiDimension().getWidth());
	}

	// The column the arrow reaches away from the participant: either the
	// diagram's outer edge (one fixed margin outside the first/last
	// participant's own box -- tileArguments.getFirstLivingSpace()/
	// getLastLivingSpace(), the same "outermost participant" accessors
	// PartitionTile's full-width frame already relies on) or, for a short
	// arrow, a small local offset from the message's own participant.
	// Composable Real, not yet a resolved column -- read back as an int only
	// at draw time / by getAsciiMinX()/getAsciiMaxX() callers, exactly like
	// every other ASCII position in this codebase.
	private Real asciiEdge() {
		final boolean leftBorder = message.getType().isLeftBorder();
		if (isShortArrow()) {
			final int width = asciiShortWidth();
			return leftBorder ? livingSpace.getAsciiPosC().addFixed(-width)
					: livingSpace.getAsciiPosC().addFixed(width);
		}

		if (leftBorder)
			return tileArguments.getFirstLivingSpace().getAsciiPosB().addFixed(-ASCII_EXO_MARGIN);

		return tileArguments.getLastLivingSpace().getAsciiPosD().addFixed(ASCII_EXO_MARGIN - 1);
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		final int p = (int) livingSpace.getAsciiLifeColumn().getCurrentValue();
		final int edge = (int) asciiEdge().getCurrentValue();
		final boolean dotted = message.getArrowConfiguration().isDotted();
		final boolean targetIsParticipant = message.getType() == MessageExoType.FROM_LEFT
				|| message.getType() == MessageExoType.FROM_RIGHT;
		final int rows = asciiLabelRows();
		final int arrowRow = rows;

		int dashFrom;
		int dashTo;
		final int arrowCol;
		final char arrowChar;
		if (edge < p) {
			if (targetIsParticipant) {
				dashFrom = edge;
				dashTo = p - 2;
				arrowCol = p - 1;
				arrowChar = '>';
			} else {
				dashFrom = edge + 1;
				dashTo = p - 1;
				arrowCol = edge;
				arrowChar = '<';
			}
		} else {
			if (targetIsParticipant) {
				dashFrom = p + 2;
				dashTo = edge;
				arrowCol = p + 1;
				arrowChar = '<';
			} else {
				dashFrom = p + 1;
				dashTo = edge - 1;
				arrowCol = edge;
				arrowChar = '>';
			}
		}
		if (dashFrom <= dashTo)
			plan.drawHLine(dashFrom, dashTo, arrowRow, dotted);
		plan.move(arrowCol, arrowRow).drawChar(arrowChar);

		final AsciiBlock label = asciiLabel();
		final int labelWidth = label.asciiDimension().getWidth();
		if (labelWidth > 0) {
			final int a = Math.min(edge, p);
			final int b = Math.max(edge, p);
			final int mid = (a + b) / 2;
			label.asciiDraw(plan.move(mid - labelWidth / 2, 0));
		}
	}

	// Same formula as CommunicationTile.asciiDimension(): label rows, plus the
	// arrow row, plus one trailing blank row.
	@Override
	public ADimension2D asciiDimension() {
		final int p = (int) livingSpace.getAsciiLifeColumn().getCurrentValue();
		final int edge = (int) asciiEdge().getCurrentValue();
		return new ADimension2D(Math.abs(edge - p), asciiLabelRows() + 2);
	}

	// ASCII counterpart of addConstraints() above -- but only for a short
	// arrow: a non-short exo message reaches all the way to the diagram's
	// outer edge, past every other participant, so it can never collide with
	// just a neighbour (the same reasoning that lets the pixel addConstraints()
	// above skip the right-border case entirely). A short arrow, by contrast,
	// is a small LOCAL stub, so it needs the same neighbour-reservation
	// CommunicationTileSelf.asciiAddConstraints() already does for its loop --
	// reused here via LivingSpaces.previous()/next() rather than duplicating
	// private helpers, since those are already public on LivingSpaces.
	@Override
	public void asciiAddConstraints() {
		if (!isShortArrow())
			return;

		final int width = asciiShortWidth();
		if (message.getType().isLeftBorder()) {
			final LivingSpace previous = tileArguments.getLivingSpaces().previous(livingSpace);
			if (previous != null)
				livingSpace.getAsciiPosC().ensureBiggerThan(previous.getAsciiPosC().addFixed(width));
		} else {
			final LivingSpace next = tileArguments.getLivingSpaces().next(livingSpace);
			if (next != null)
				next.getAsciiPosC().ensureBiggerThan(livingSpace.getAsciiPosC().addFixed(width));
		}
	}

	// ASCII counterpart of getMinX()/getMaxX(): composable Real spanning
	// whichever of {participant lifeline, edge} is smaller/bigger, so an
	// enclosing GroupingTile can fold this tile's range into its own frame
	// span before the ASCII RealLine compiles -- same contract as every other
	// migrated tile's getAsciiMinX()/getAsciiMaxX().
	@Override
	public Real getAsciiMinX() {
		return RealUtils.min(asciiEdge(), livingSpace.getAsciiLifeColumn());
	}

	@Override
	public Real getAsciiMaxX() {
		return RealUtils.max(asciiEdge(), livingSpace.getAsciiLifeColumn());
	}
}
