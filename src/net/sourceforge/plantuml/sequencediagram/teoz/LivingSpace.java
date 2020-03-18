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
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantEnglober;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LivingSpace {

	private final Participant p;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final ComponentType headType;
	private final ComponentType tailType;
	private final boolean useContinueLineBecauseOfDelay;
	private final MutingLine mutingLine;
	private final Rose rose = new Rose();
	private final LiveBoxes liveBoxes;

	// private final LivingSpaceImpl previous;
	// private LivingSpace next;

	private final Real posB;
	private Real posC;
	private Real posD;

	private final EventsHistory eventsHistory;
	private boolean create = false;
	private double createY = 0;

	private final ParticipantEnglober englober;

	public int getLevelAt(Tile tile, EventsHistoryMode mode) {
		// assert mode == EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE;
		return eventsHistory.getLevelAt(tile.getEvent(), mode);
	}

	public void addStepForLivebox(Event event, double y) {
		eventsHistory.addStepForLivebox(event, y);
	}

	@Override
	public String toString() {
		return p.getCode() + " B=" + posB.getCurrentValue() + "/C=" + currentValue(posC) + "/D=" + currentValue(posD);
	}

	private static String currentValue(Real pos) {
		if (pos == null) {
			return null;
		}
		return "" + pos.getCurrentValue();
	}

	public LivingSpace(Participant p, ParticipantEnglober englober, Rose skin, ISkinParam skinParam, Real position,
			List<Event> events) {
		this.eventsHistory = new EventsHistory(p, events);
		this.p = p;
		this.skin = skin;
		this.skinParam = skinParam;
		this.englober = englober;
		this.posB = position;
		if (p.getType() == ParticipantType.PARTICIPANT) {
			headType = ComponentType.PARTICIPANT_HEAD;
			tailType = ComponentType.PARTICIPANT_TAIL;
		} else if (p.getType() == ParticipantType.ACTOR) {
			headType = ComponentType.ACTOR_HEAD;
			tailType = ComponentType.ACTOR_TAIL;
		} else if (p.getType() == ParticipantType.BOUNDARY) {
			headType = ComponentType.BOUNDARY_HEAD;
			tailType = ComponentType.BOUNDARY_TAIL;
		} else if (p.getType() == ParticipantType.CONTROL) {
			headType = ComponentType.CONTROL_HEAD;
			tailType = ComponentType.CONTROL_TAIL;
		} else if (p.getType() == ParticipantType.ENTITY) {
			headType = ComponentType.ENTITY_HEAD;
			tailType = ComponentType.ENTITY_TAIL;
		} else if (p.getType() == ParticipantType.QUEUE) {
			headType = ComponentType.QUEUE_HEAD;
			tailType = ComponentType.QUEUE_TAIL;
		} else if (p.getType() == ParticipantType.DATABASE) {
			headType = ComponentType.DATABASE_HEAD;
			tailType = ComponentType.DATABASE_TAIL;
		} else if (p.getType() == ParticipantType.COLLECTIONS) {
			headType = ComponentType.COLLECTIONS_HEAD;
			tailType = ComponentType.COLLECTIONS_TAIL;
		} else {
			throw new IllegalArgumentException();
		}
		// this.stairs2.addStep2(0, p.getInitialLife());
		// this.stairs2.addStep2(0, 0);
		this.useContinueLineBecauseOfDelay = useContinueLineBecauseOfDelay(events);
		this.mutingLine = new MutingLine(skin, skinParam, events);
		this.liveBoxes = new LiveBoxes(eventsHistory, skin, skinParam, p);
	}

	private boolean useContinueLineBecauseOfDelay(List<Event> events) {
		final String strategy = skinParam.getValue("lifelineStrategy");
		if ("nosolid".equalsIgnoreCase(strategy)) {
			return false;
		}
		for (Event ev : events) {
			if (ev instanceof Delay) {
				return true;
			}
		}
		return false;
	}

	public void drawLineAndLiveBoxes(UGraphic ug, double height, Context2D context) {

		mutingLine.drawLine(ug, context, createY, height);
		liveBoxes.drawBoxes(ug, context, createY, height);
	}

	// public void addDelayTile(DelayTile tile) {
	// System.err.println("addDelayTile " + this + " " + tile);
	// }

	public void drawHead(UGraphic ug, Context2D context, VerticalAlignment verticalAlignment,
			HorizontalAlignment horizontalAlignment) {
		if (create && verticalAlignment == VerticalAlignment.BOTTOM) {
			return;
		}
		final Component comp = rose.createComponent(p.getUsedStyles(), headType, null,
				p.getSkinParamBackcolored(skinParam), p.getDisplay(skinParam.forceSequenceParticipantUnderlined()));
		final Dimension2D dim = comp.getPreferredDimension(ug.getStringBounder());
		if (horizontalAlignment == HorizontalAlignment.RIGHT) {
			ug = ug.apply(UTranslate.dx(-dim.getWidth()));
		}
		if (verticalAlignment == VerticalAlignment.CENTER) {
			ug = ug.apply(UTranslate.dy(-dim.getHeight() / 2));
		}
		final Area area = new Area(dim);
		final Url url = getParticipant().getUrl();
		if (url != null) {
			ug.startUrl(url);
		}
		comp.drawU(ug, area, context);
		if (url != null) {
			ug.closeAction();
		}
	}

	public Dimension2D getHeadPreferredDimension(StringBounder stringBounder) {
		final Component comp = rose.createComponent(p.getUsedStyles(), headType, null, skinParam,
				p.getDisplay(skinParam.forceSequenceParticipantUnderlined()));
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim;
	}

	private double getPreferredWidth(StringBounder stringBounder) {
		return getHeadPreferredDimension(stringBounder).getWidth();
	}

	public Real getPosC(StringBounder stringBounder) {
		if (posC == null) {
			this.posC = posB.addFixed(this.getPreferredWidth(stringBounder) / 2);
		}
		return posC;
	}

	public Real getPosC2(StringBounder stringBounder) {
		final double delta = liveBoxes.getMaxPosition(stringBounder);
		return getPosC(stringBounder).addFixed(delta);
	}

	public Real getPosD(StringBounder stringBounder) {
		if (posD == null) {
			this.posD = posB.addFixed(this.getPreferredWidth(stringBounder));
		}
		// System.err.println("LivingSpace::getPosD "+posD.getCurrentValue());
		return posD;
	}

	public Real getPosB() {
		return posB;
	}

	public Participant getParticipant() {
		return p;
	}

	public void goCreate(double y) {
		this.createY = y;
		this.create = true;
	}

	public void goCreate() {
		this.create = true;
	}

	public void delayOn(double y, double height) {
		mutingLine.delayOn(y, height);
		liveBoxes.delayOn(y, height);
	}

	public ParticipantEnglober getEnglober() {
		return englober;
	}

}
