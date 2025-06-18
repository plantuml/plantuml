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

public class CommunicationTileSelf extends AbstractTile {

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
		super(stringBounder, currentY);
		this.livingSpace1 = livingSpace1;
		this.livingSpaces = livingSpaces;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
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
	final protected void callbackY_internal(TimeHook y) {
		super.callbackY_internal(y);
		final ArrowComponent comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		final XPoint2D p1 = comp.getStartPoint(getStringBounder(), dim);
		final XPoint2D p2 = comp.getEndPoint(getStringBounder(), dim);

		if (message.isActivate())
			livingSpace1.addStepForLivebox(getEvent(), y.getValue() + p2.getY());
		else if (message.isDeactivate())
			livingSpace1.addStepForLivebox(getEvent(), y.getValue() + p1.getY());
		else if (message.isDestroy())
			livingSpace1.addStepForLivebox(getEvent(), y.getValue() + p2.getY());

	}

	public void drawU(UGraphic ug) {
		if (YGauge.USE_ME)
			ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()));
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		double x1 = getMinX().getCurrentValue();
		final int levelIgnore = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_ACTIVATE);
		final int levelConsidere = livingSpace1.getLevelAt(this, EventsHistoryMode.CONSIDERE_FUTURE_DEACTIVATE);
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

}
