/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class CommunicationTile implements TileWithUpdateStairs {

	private final LivingSpace livingSpace1;
	private final LivingSpace livingSpace2;
	private final Message message;
	private final Skin skin;
	private final ISkinParam skinParam;

	public Event getEvent() {
		return message;
	}

	public CommunicationTile(LivingSpace livingSpace1, LivingSpace livingSpace2, Message message, Skin skin,
			ISkinParam skinParam) {
		if (livingSpace1 == livingSpace2) {
			throw new IllegalArgumentException();
		}
		this.livingSpace1 = livingSpace1;
		this.livingSpace2 = livingSpace2;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;
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

	private Component getComponent(StringBounder stringBounder) {
		ArrowConfiguration arrowConfiguration = message.getArrowConfiguration();
		/*
		 * if (isSelf()) { arrowConfiguration = arrowConfiguration.self(); } else
		 */
		if (isReverse(stringBounder)) {
			arrowConfiguration = arrowConfiguration.reverse();
		}
		final Component comp = skin.createComponent(ComponentType.ARROW, arrowConfiguration, skinParam,
				message.getLabel());
		return comp;
	}

	public static final double LIVE_DELTA_SIZE = 5;

	public void updateStairs(StringBounder stringBounder, double y) {
		final ArrowComponent comp = (ArrowComponent) getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final Point2D p2 = comp.getEndPoint(stringBounder, dim);
		// System.err.println("CommunicationTile::updateStairs y=" + y + " p1=" + p1 + " p2=" + p2 + " dim=" + dim);
		final double arrowY = comp.getStartPoint(stringBounder, dim).getY();

		livingSpace1.addStepForLivebox(getEvent(), y + arrowY);
		livingSpace2.addStepForLivebox(getEvent(), y + arrowY);

		// System.err.println("CommunicationTile::updateStairs msg=" + message + " y=" + y + " arrowY=" + arrowY);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		double x1 = getPoint1(stringBounder).getCurrentValue();
		double x2 = getPoint2(stringBounder).getCurrentValue();

		final int level1 = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
		final int level2 = livingSpace2.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
		// System.err.println("CommunicationTile::draw msg=" + message + " level1=" + level1 + " level2=" + level2);

		final Area area;
		if (isReverse(stringBounder)) {
			System.err.println("isreverse!");
			// x1 -= LIVE_DELTA_SIZE * level1;
			x2 += LIVE_DELTA_SIZE * level2;
			area = new Area(x1 - x2, dim.getHeight());
			ug = ug.apply(new UTranslate(x2, 0));
		} else {
			x1 += LIVE_DELTA_SIZE * level1;
			x2 -= LIVE_DELTA_SIZE * level2;
			area = new Area(x2 - x1, dim.getHeight());
			ug = ug.apply(new UTranslate(x1, 0));
		}
		comp.drawU(ug, area, (Context2D) ug);
		// ug.draw(new ULine(x2 - x1, 0));
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim.getHeight();
	}

	public void addConstraints(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final double width = dim.getWidth();

		// if (isSelf()) {
		// final LivingSpace next = livingSpace1.getNext();
		// if (next != null) {
		// next.getPosB().ensureBiggerThan(getMaxX(stringBounder));
		// }
		// } else {
		final Real point1 = getPoint1(stringBounder);
		final Real point2 = getPoint2(stringBounder);
		if (point1.getCurrentValue() < point2.getCurrentValue()) {
			point2.ensureBiggerThan(point1.addFixed(width));
		} else {
			point1.ensureBiggerThan(point2.addFixed(width));
			// }
		}
	}

	// private boolean isSelf() {
	// return livingSpace1 == livingSpace2;
	// }

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
		// if (isSelf()) {
		// final Component comp = getComponent(stringBounder);
		// final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final double width = dim.getWidth();
		// return livingSpace1.getPosC(stringBounder).addFixed(width);
		// }
		if (isReverse(stringBounder)) {
			return getPoint1(stringBounder);
		}
		return getPoint2(stringBounder);
	}

}
