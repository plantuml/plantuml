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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.Participant;

public class EventsHistory {

	private final Participant p;
	private final List<Event> events;
	private final Map<Event, Double> ys3 = new HashMap<Event, Double>();

	public EventsHistory(Participant p, List<Event> events) {
		this.p = p;
		this.events = events;
	}

	public void addStepForLivebox(Event event, double y) {
		ys3.put(event, y);
	}

	public Participant getParticipant() {
		return p;
	}

	public int getLevelAt(Event event, EventsHistoryMode mode) {
		final int result = getLevelAtInternal(event, mode);
		// System.err.println("EventsHistory::getLevelAt " + mode + " " + result + " " + event);
		return result;
	}

	private int getLevelAtInternal(Event event, EventsHistoryMode mode) {
		int level = 0; // p.getInitialLife();
		// System.err.println("--->EventsHistory for " + p + " " + event);
		for (Iterator<Event> it = events.iterator(); it.hasNext();) {
			final Event current = it.next();
			if (current instanceof LifeEvent) {
				final LifeEvent le = (LifeEvent) current;
				if (le.getParticipant() == p && le.isActivate()) {
					level++;
				}
				if (le.getParticipant() == p && le.isDeactivateOrDestroy()) {
					level--;
				}
			}
			if (event == current) {
				if (current instanceof AbstractMessage) {
					final Event next = nextButSkippingNotes(it);
					if (next instanceof LifeEvent) {
						final LifeEvent le = (LifeEvent) next;
						final AbstractMessage msg = (AbstractMessage) current;
						if (mode != EventsHistoryMode.IGNORE_FUTURE_ACTIVATE && le.isActivate() && msg.dealWith(p)
								&& le.getParticipant() == p) {
							level++;
						}
						if (mode == EventsHistoryMode.CONSIDERE_FUTURE_DEACTIVATE && le.isDeactivateOrDestroy()
								&& msg.dealWith(p) && le.getParticipant() == p) {
							level--;
						}
						// System.err.println("Warning, this is message " + current + " next=" + next);
					}

				}
				if (level < 0) {
					return 0;
				}
				// System.err.println("<-result1 is " + level);
				return level;
			}
		}
		throw new IllegalArgumentException();
		// return level;
	}

	private boolean isNextEventADestroy(Event event) {
		for (Iterator<Event> it = events.iterator(); it.hasNext();) {
			final Event current = it.next();
			if (event != current) {
				continue;
			}
			if (current instanceof Message) {
				final Event next = nextButSkippingNotes(it);
				if (next instanceof LifeEvent) {
					final LifeEvent le = (LifeEvent) next;
					return le.isDestroy(p);
				}
			}
			return false;
		}
		return false;
	}

	private SymbolContext getActivateColor(Event event) {
		if (event instanceof LifeEvent) {
			final LifeEvent le = (LifeEvent) event;
			if (le.isActivate()) {
				return le.getSpecificColors();
			}
		}
		for (Iterator<Event> it = events.iterator(); it.hasNext();) {
			final Event current = it.next();
			if (event != current) {
				continue;
			}
			if (current instanceof Message) {
				final Event next = nextButSkippingNotes(it);
				if (next instanceof LifeEvent) {
					final LifeEvent le = (LifeEvent) next;
					if (le.isActivate()) {
						return le.getSpecificColors();
					}
					return null;
				}
			}
			return null;
		}
		return null;
	}

	private Event nextButSkippingNotes(Iterator<Event> it) {
		while (true) {
			if (it.hasNext() == false) {
				return null;
			}
			final Event next = it.next();
			if (next instanceof Note) {
				continue;
			}
			// System.err.println("nextButSkippingNotes=" + next);
			return next;
		}
	}

	public Stairs2 getStairs(double createY, double totalHeight) {
		// System.err.println("EventsHistory::getStairs totalHeight=" + totalHeight);
		final Stairs2 result = new Stairs2();
		int value = 0;
		for (Event event : events) {
			final Double position = ys3.get(event);
			// System.err.println("EventsHistory::getStairs event=" + event + " position=" + position);
			if (position != null) {
				assert position <= totalHeight : "position=" + position + " totalHeight=" + totalHeight;
				value = getLevelAt(event, EventsHistoryMode.CONSIDERE_FUTURE_DEACTIVATE);
				final SymbolContext activateColor = getActivateColor(event);
				result.addStep(new StairsPosition(Math.max(createY, position), isNextEventADestroy(event)), value,
						activateColor);
			}
		}
		// System.err.println("EventsHistory::getStairs finishing totalHeight=" + totalHeight);
		result.addStep(new StairsPosition(totalHeight, false), value, null);
		// System.err.println("EventsHistory::getStairs " + p + " result=" + result);
		return result;
	}

	public int getMaxValue() {
		int max = 0;
		int level = 0;
		for (Event current : events) {
			if (current instanceof LifeEvent) {
				final LifeEvent le = (LifeEvent) current;
				if (le.getParticipant() == p && le.isActivate()) {
					level++;
				}
				if (level > max) {
					max = level;
				}
				if (le.getParticipant() == p && le.isDeactivateOrDestroy()) {
					level--;
				}
			}
		}
		return max;
	}

}
