/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.AbstractComponentRoseArrow;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class CommunicationTile extends AbstractTile implements TileWithUpdateStairs, TileWithCallbackY {

	private final LivingSpace livingSpace1;
	private final LivingSpace livingSpace2;
	private final Message message;
	private final Rose skin;
	private final ISkinParam skinParam;

	public Event getEvent() {
		return message;
	}

	@Override
	public String toString() {
		return super.toString() + " " + message;
	}

	public CommunicationTile(LivingSpace livingSpace1, LivingSpace livingSpace2, Message message, Rose skin,
			ISkinParam skinParam) {
		if (livingSpace1 == livingSpace2) {
			throw new IllegalArgumentException();
		}
		this.livingSpace1 = livingSpace1;
		this.livingSpace2 = livingSpace2;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;

		if (message.isCreate()) {
			livingSpace2.goCreate();
		}
		// for (LifeEvent lifeEvent : message.getLiveEvents()) {
		// System.err.println("lifeEvent = " + lifeEvent);
		// // livingSpace1.addLifeEvent(this, lifeEvent);
		// // livingSpace2.addLifeEvent(this, lifeEvent);
		// }
	}

	public boolean isReverse(StringBounder stringBounder) {
		final Real point1 = livingSpace1.getPosC(stringBounder);
		final Real point2 = livingSpace2.getPosC(stringBounder);
		if (point1.getCurrentValue() > point2.getCurrentValue()) {
			return true;
		}
		return false;
	}

	private boolean isCreate() {
		return message.isCreate();
	}

	private double getArrowThickness() {
		final UStroke result = skinParam.getThickness(LineParam.sequenceArrow, null);
		if (result == null) {
			return 1;
		}
		return result.getThickness();
	}

	private ArrowComponent getComponent(StringBounder stringBounder) {
		ArrowConfiguration arrowConfiguration = message.getArrowConfiguration();
		/*
		 * if (isSelf()) { arrowConfiguration = arrowConfiguration.self(); } else
		 */
		if (isReverse(stringBounder)) {
			arrowConfiguration = arrowConfiguration.reverse();
		}
		arrowConfiguration = arrowConfiguration.withThickness(getArrowThickness());

		final ArrowComponent comp = skin.createComponentArrow(message.getUsedStyles(), arrowConfiguration, skinParam,
				message.getLabelNumbered());
		return comp;
	}

	@Override
	public double getYPoint(StringBounder stringBounder) {
		return getComponent(stringBounder).getYPoint(stringBounder);
	}

	public static final double LIVE_DELTA_SIZE = 5;

	public void updateStairs(StringBounder stringBounder, double y) {
		final AbstractComponentRoseArrow comp = (AbstractComponentRoseArrow) getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final Point2D p2 = comp.getEndPoint(stringBounder, dim);
		// System.err.println("CommunicationTile::updateStairs y=" + y + " p1=" + p1 + " p2=" + p2 + " dim=" + dim);
		final double arrowY = comp.getStartPoint(stringBounder, dim).getY();

		livingSpace1.addStepForLivebox(getEvent(), y + arrowY);
		livingSpace2.addStepForLivebox(getEvent(), y + arrowY);

		// System.err.println("CommunicationTile::updateStairs msg=" + message + " y=" + y + " arrowY=" + arrowY);
	}

	public void drawU(UGraphic ug) {
		final String anchor1 = message.getPart1Anchor();
		final String anchor2 = message.getPart2Anchor();
		if (anchor1 != null || anchor2 != null) {
			return;
		}
		final StringBounder stringBounder = ug.getStringBounder();
		final ArrowComponent comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		double x1 = getPoint1(stringBounder).getCurrentValue();
		double x2 = getPoint2(stringBounder).getCurrentValue();

		final Area area;
		if (isReverse(stringBounder)) {
			final int level1 = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			final int level2 = livingSpace2.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			if (level1 > 0) {
				x1 -= LIVE_DELTA_SIZE;
			}
			x2 += LIVE_DELTA_SIZE * level2;
			area = new Area(x1 - x2, dim.getHeight());
			ug = ug.apply(UTranslate.dx(x2));
			if (isCreate()) {
				livingSpace2.drawHead(ug, (Context2D) ug, VerticalAlignment.TOP, HorizontalAlignment.RIGHT);
			}
		} else {
			final int level1 = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			int level2 = livingSpace2.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			if (level2 > 0) {
				level2 = level2 - 2;
			}
			x1 += LIVE_DELTA_SIZE * level1;
			x2 += LIVE_DELTA_SIZE * level2;
			area = new Area(x2 - x1, dim.getHeight());
			ug = ug.apply(UTranslate.dx(x1));
			if (isCreate()) {
				livingSpace2.drawHead(ug.apply(UTranslate.dx(area.getDimensionToUse().getWidth())), (Context2D) ug,
						VerticalAlignment.TOP, HorizontalAlignment.LEFT);
			}
		}
		comp.drawU(ug, area, (Context2D) ug);
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		double height = dim.getHeight();
		if (isCreate()) {
			height = Math.max(height, livingSpace2.getHeadPreferredDimension(stringBounder).getHeight());
		}
		return height;
	}

	public void addConstraints(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final double width = dim.getWidth();

		Real point1 = getPoint1(stringBounder);
		Real point2 = getPoint2(stringBounder);
		if (isReverse(stringBounder)) {
			final int level1 = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			final int level2 = livingSpace2.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			if (level1 > 0) {
				point1 = point1.addFixed(-LIVE_DELTA_SIZE);
			}
			point2 = point2.addFixed(level2 * LIVE_DELTA_SIZE);
			point1.ensureBiggerThan(point2.addFixed(width));
		} else {
			final int level2 = livingSpace2.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			if (level2 > 0) {
				point2 = point2.addFixed(-LIVE_DELTA_SIZE);
			}
			point2.ensureBiggerThan(point1.addFixed(width));
		}
	}

	private Real getPoint1(final StringBounder stringBounder) {
		return livingSpace1.getPosC(stringBounder);
	}

	private Real getPoint2(final StringBounder stringBounder) {
		if (message.isCreate()) {
			if (isReverse(stringBounder)) {
				return livingSpace2.getPosD(stringBounder);
			}
			return livingSpace2.getPosB();
		}
		return livingSpace2.getPosC(stringBounder);
	}

	public Real getMinX(StringBounder stringBounder) {
		if (isReverse(stringBounder)) {
			return getPoint2(stringBounder);
		}
		return getPoint1(stringBounder);
	}

	public Real getMaxX(StringBounder stringBounder) {
		if (isReverse(stringBounder)) {
			return getPoint1(stringBounder);
		}
		return getPoint2(stringBounder);
	}

	public void callbackY(double y) {
		if (message.isCreate()) {
			livingSpace2.goCreate(y);
		}
	}

}
