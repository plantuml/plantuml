/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.ArrowConfiguration;

public abstract class AbstractMessage implements Event {

	final private List<String> label;
//	final private boolean dotted;
//	final private boolean full;
	final private ArrowConfiguration arrowConfiguration;
	final private List<LifeEvent> lifeEvents = new ArrayList<LifeEvent>();

	private List<String> notes;
	private NotePosition notePosition;
	private HtmlColor noteBackColor;
	private final String messageNumber;

	public AbstractMessage(List<String> label, ArrowConfiguration arrowConfiguration, String messageNumber) {
		this.label = label;
		this.arrowConfiguration = arrowConfiguration;
		this.messageNumber = messageNumber;
	}

	public final void addLifeEvent(LifeEvent lifeEvent) {
		this.lifeEvents.add(lifeEvent);
	}

	public final boolean isCreate() {
		for (LifeEvent le : lifeEvents) {
			if (le.getType() == LifeEventType.CREATE) {
				return true;
			}
		}
		return false;
	}

	public final boolean isActivateAndDeactive() {
		if (lifeEvents.size() < 2) {
			return false;
		}
		return lifeEvents.get(0).getType() == LifeEventType.ACTIVATE
				&& (lifeEvents.get(1).getType() == LifeEventType.DEACTIVATE || lifeEvents.get(1).getType() == LifeEventType.DESTROY);
	}

	public final List<LifeEvent> getLiveEvents() {
		return Collections.unmodifiableList(lifeEvents);
	}

	public final List<String> getLabel() {
		return Collections.unmodifiableList(label);
	}

	public final ArrowConfiguration getArrowConfiguration() {
		return arrowConfiguration;
	}

	public final List<String> getNote() {
		return notes == null ? notes : Collections.unmodifiableList(notes);
	}

	public final void setNote(List<String> strings, NotePosition notePosition, String backcolor) {
		if (notePosition != NotePosition.LEFT && notePosition != NotePosition.RIGHT) {
			throw new IllegalArgumentException();
		}
		this.notes = strings;
		this.notePosition = notePosition;
		this.noteBackColor = HtmlColor.getColorIfValid(backcolor);
	}

	public final HtmlColor getSpecificBackColor() {
		return noteBackColor;
	}

	public final NotePosition getNotePosition() {
		return notePosition;
	}

	public final String getMessageNumber() {
		return messageNumber;
	}

}
