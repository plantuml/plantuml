/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.skin.ArrowConfiguration;

public abstract class AbstractMessage implements Event {

	final private Display label;
	final private ArrowConfiguration arrowConfiguration;
	final private List<LifeEvent> lifeEvents = new ArrayList<LifeEvent>();

	private Display notes;
	private NotePosition notePosition;
	private HtmlColor noteBackColor;
	private Url urlNote;
	private final Url url;
	private final String messageNumber;

	public AbstractMessage(Display label, ArrowConfiguration arrowConfiguration, String messageNumber) {
		this.url = label.initUrl();
		this.label = label.removeUrl(url);
		this.arrowConfiguration = arrowConfiguration;
		this.messageNumber = messageNumber;
	}

	final public Url getUrl() {
		if (url == null) {
			return urlNote;
		}
		return url;
	}

	public boolean hasUrl() {
		if (notes != null && notes.hasUrl()) {
			return true;
		}
		if (label != null && label.hasUrl()) {
			return true;
		}
		return getUrl() != null;
	}

	public final boolean addLifeEvent(LifeEvent lifeEvent) {
		final Set<Participant> noActivationAuthorized = new HashSet<Participant>();
		for (LifeEvent le : this.lifeEvents) {
			if (le.getType() == LifeEventType.DEACTIVATE || le.getType() == LifeEventType.DESTROY) {
				noActivationAuthorized.add(le.getParticipant());
			}
		}
		if (lifeEvent.getType() == LifeEventType.ACTIVATE
				&& noActivationAuthorized.contains(lifeEvent.getParticipant())) {
			return false;
		}
		// for (LifeEvent le : this.lifeEvents) {
		// if (le.getParticipant().equals(lifeEvent.getParticipant())) {
		// return false;
		// }
		// }
		this.lifeEvents.add(lifeEvent);
		return true;
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

	public final Display getLabel() {
		return label;
	}

	public final ArrowConfiguration getArrowConfiguration() {
		return arrowConfiguration;
	}

	public final Display getNote() {
		return notes == null ? notes : notes;
	}

	public final Url getUrlNote() {
		return urlNote;
	}

	public final void setNote(Display strings, NotePosition notePosition, String backcolor, Url url) {
		if (notePosition != NotePosition.LEFT && notePosition != NotePosition.RIGHT) {
			throw new IllegalArgumentException();
		}
		this.notes = strings;
		this.urlNote = url;
		this.notePosition = overideNotePosition(notePosition);
		this.noteBackColor = HtmlColorUtils.getColorIfValid(backcolor);
	}

	protected NotePosition overideNotePosition(NotePosition notePosition) {
		return notePosition;
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

	public boolean isActivate() {
		for (LifeEvent le : this.lifeEvents) {
			if (le.getType() == LifeEventType.ACTIVATE) {
				return true;
			}
		}
		return false;
	}

	public boolean isDeactivate() {
		for (LifeEvent le : this.lifeEvents) {
			if (le.getType() == LifeEventType.DEACTIVATE) {
				return true;
			}
		}
		return false;
	}

	public abstract boolean compatibleForCreate(Participant p);

}
