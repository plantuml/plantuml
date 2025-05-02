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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

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
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	private StyleSignatureBasic getStyleSignature() {
		return ComponentType.DESTROY.getStyleSignature();
	}

    private Style[] getUsedStyle() {
        final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		return new Style[] { style };
    }	

	public void drawU(UGraphic ug) {
		if (YGauge.USE_ME)
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

}
