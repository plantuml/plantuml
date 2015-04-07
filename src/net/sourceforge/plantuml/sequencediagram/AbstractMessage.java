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
package net.sourceforge.plantuml.sequencediagram;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSet;
import net.sourceforge.plantuml.skin.ArrowConfiguration;

public abstract class AbstractMessage implements EventWithDeactivate {

	final private Display label;
	final private ArrowConfiguration arrowConfiguration;

	final private Set<LifeEventType> lifeEventsType = EnumSet.noneOf(LifeEventType.class);

	private Display notes;
	private NotePosition notePosition;
	private HtmlColor noteBackColor;
	private Url urlNote;
	private final Url url;
	private final String messageNumber;
	private boolean parallel = false;

	public AbstractMessage(Display label, ArrowConfiguration arrowConfiguration, String messageNumber) {
		this.url = label.initUrl();
		this.label = label.removeUrl(url);
		this.arrowConfiguration = arrowConfiguration;
		this.messageNumber = messageNumber;
	}

	public void goParallel() {
		this.parallel = true;
	}
	
	public boolean isParallel() {
		return parallel;
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

	private boolean firstIsActivate = false;
	private final Set<Participant> noActivationAuthorized2 = new HashSet<Participant>();

	public final boolean addLifeEvent(LifeEvent lifeEvent) {
		lifeEvent.setMessage(this);
		lifeEventsType.add(lifeEvent.getType());
		if (lifeEventsType.size() == 1 && isActivate()) {
			firstIsActivate = true;
		}

		if (lifeEvent.getType() == LifeEventType.ACTIVATE
				&& noActivationAuthorized2.contains(lifeEvent.getParticipant())) {
			return false;
		}

		if (lifeEvent.getType() == LifeEventType.DEACTIVATE || lifeEvent.getType() == LifeEventType.DESTROY) {
			noActivationAuthorized2.add(lifeEvent.getParticipant());
		}

		return true;
	}

	public final boolean isCreate() {
		return lifeEventsType.contains(LifeEventType.CREATE);
	}

	public boolean isActivate() {
		return lifeEventsType.contains(LifeEventType.ACTIVATE);
	}

	public boolean isDeactivate() {
		return lifeEventsType.contains(LifeEventType.DEACTIVATE);
	}

	private boolean isDeactivateOrDestroy() {
		return lifeEventsType.contains(LifeEventType.DEACTIVATE) || lifeEventsType.contains(LifeEventType.DESTROY);
	}

	public final boolean isActivateAndDeactive() {
		return firstIsActivate && isDeactivateOrDestroy();
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
		this.noteBackColor = HtmlColorSet.getInstance().getColorIfValid(backcolor);
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

	public abstract boolean compatibleForCreate(Participant p);

	public abstract boolean isSelfMessage();

	private double posYendLevel;
	private double posYstartLevel;

	public double getPosYstartLevel() {
		return posYstartLevel;
	}

	public void setPosYstartLevel(double posYstartLevel) {
		this.posYstartLevel = posYstartLevel;
	}

	public void setPosYendLevel(double posYendLevel) {
		this.posYendLevel = posYendLevel;
	}

	public double getPosYendLevel() {
		return posYendLevel;
	}
}
