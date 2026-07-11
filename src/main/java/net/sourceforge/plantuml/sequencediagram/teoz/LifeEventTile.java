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
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;

public class LifeEventTile extends AbstractTile {

	private final LifeEvent lifeEvent;
	private final TileArguments tileArguments;
	private final LivingSpace livingSpace;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final YGauge yGauge;

	@Override
	final protected void callbackY_internal(TimeHook y) {
		super.callbackY_internal(y);
		// System.err.println("LifeEventTile::updateStairs " + lifeEvent + " " +
		// livingSpace.getParticipant() + " y=" + y);
		livingSpace.addStepForLivebox(getEvent(), y.getValue());
		if (lifeEvent.getType() == LifeEventType.DESTROY)
			livingSpace.goDestroy(y.getValue());
	}

	public Event getEvent() {
		return lifeEvent;
	}

	@Override
	public double getContactPointRelative() {
		return 0;
	}

	public LifeEventTile(LifeEvent lifeEvent, TileArguments tileArguments, LivingSpace livingSpace, Rose skin,
			ISkinParam skinParam, YGauge currentY) {
		super(tileArguments.getStringBounder(), currentY);
		this.lifeEvent = lifeEvent;
		this.tileArguments = tileArguments;
		this.livingSpace = livingSpace;
		this.skin = skin;
		this.skinParam = skinParam;
		// The contact line is propagated through life events so that an
		// activate/deactivate between two parallel (&) messages does not break
		// the contact sharing (the legacy moveRecentParallelTilesToPending
		// skips through LifeEventTiles the same way)
		this.yGauge = YGauge.createPropagating(currentY, getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	private StyleSignature getStyleSignature() {
		return ComponentType.DESTROY.getStyleSignature();
	}

    private Style[] getUsedStyle() {
        final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		return new Style[] { style };
    }	

	public void drawU(UGraphic ug) {
		// Self-translate prologue: absolute gauge position
		ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()));
		if (isDestroyWithoutMessage()) {
			final Component cross = skin.createComponent(getUsedStyle(), ComponentType.DESTROY, null, skinParam,
					null);
			final XDimension2D dimCross = cross.getPreferredDimension(ug.getStringBounder());
			final double x = livingSpace.getPosC(ug.getStringBounder()).getCurrentValue();
			cross.drawU(ug.apply(UTranslate.dx(x - dimCross.getWidth() / 2)), null, (Context2D) ug);
		}
	}

	public boolean isDestroyWithoutMessage() {
		return lifeEvent.getMessage() == null && lifeEvent.getType() == LifeEventType.DESTROY;
	}

	public double getPreferredHeight() {
//		if (lifeEvent.isActivate()) {
//			return 20;
//		}
		if (isDestroyWithoutMessage()) {
			final Component cross = skin.createComponent(getUsedStyle(), ComponentType.DESTROY, null, skinParam, null);
			final XDimension2D dimCross = cross.getPreferredDimension(getStringBounder());
			return dimCross.getHeight();
		}
		return 0;
	}

	public void addConstraints() {
	}

	public Real getMinX() {
		final int levelAt = livingSpace.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_ACTIVATE);
		final double liveDeltaWidthAdjustment = levelAt > 0
				? CommunicationTile.LIVE_DELTA_SIZE
				: 0;
		return livingSpace.getPosC(getStringBounder()).addFixed(-liveDeltaWidthAdjustment);
	}

	public Real getMaxX() {
		final int levelAt = livingSpace.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_ACTIVATE);
		final double liveDeltaWidthAdjustment = levelAt > 0
				? levelAt * CommunicationTile.LIVE_DELTA_SIZE
				: 0;

		return livingSpace.getPosC(getStringBounder()).addFixed(liveDeltaWidthAdjustment);
	}

	// ---------------------------------------------------------------------
	// ASCII rendering (ASCIIVERSE.md §35).
	//
	// activate/deactivate are deliberately ignored for now (no livebox
	// indentation in ASCII yet, ASCIIVERSE.md §32/§9.5 territory): a
	// LifeEventTile that is not a destroy is a pure spacer, mirroring
	// EmptyTile (ASCIIVERSE.md §21) -- zero footprint, draws nothing.
	//
	// A destroy gets a single-row "X" marker, planted at the participant's own
	// absolute lifeline column (getAsciiPosC()) -- the plan's horizontal
	// translation is ignored, only its row matters, same convention as
	// CommunicationTile/CommunicationTileSelf reading their own home column
	// directly rather than depending on the caller's dx. The 'X' is drawn
	// before the orchestrator's lifeline-fill pass, which only ever writes
	// into still-empty cells (ASCIIVERSE.md §9.2), so it is never overwritten.
	// No Unicode variant: like arrowheads (§12), this character doesn't vary
	// between ATXT and UTXT.
	//
	// Deliberately NOT gated on isDestroyWithoutMessage(): that flag only
	// controls the rare pixel-only branch in drawU()/getPreferredHeight()
	// above. In the common case -- "destroy X" right after a message dealing
	// with X -- the LifeEvent gets attached to that message
	// (SequenceDiagram.activate(): lifeEvent.setMessage(lastMessage)), so
	// getMessage() != null and isDestroyWithoutMessage() is false; yet the
	// pixel cross still gets drawn there, just by a completely different,
	// tile-independent pass (LiveBoxesDrawer.drawDestroyIfNeeded(), driven by
	// the Step history LivingSpace.addStepForLivebox()/goDestroy() already
	// record above in callbackY_internal). ASCII has no such second pass (no
	// livebox rendering at all yet, §32), so the tile itself must draw the X
	// for every destroy, attached or not -- hence testing the LifeEventType
	// directly rather than reusing the pixel-specific gate.
	private boolean isDestroy() {
		return lifeEvent.getType() == LifeEventType.DESTROY;
	}

	@Override
	public ADimension2D asciiDimension() {
		if (isDestroy())
			return new ADimension2D(0, 1);

		return new ADimension2D(0, 0);
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		if (isDestroy() == false)
			return;

		final int x = (int) livingSpace.getAsciiPosC().getCurrentValue();
		plan.move(x, 0).drawString("X");
	}

	@Override
	public Real getAsciiMinX() {
		return livingSpace.getAsciiPosC();
	}

	@Override
	public Real getAsciiMaxX() {
		return livingSpace.getAsciiPosC();
	}

}
