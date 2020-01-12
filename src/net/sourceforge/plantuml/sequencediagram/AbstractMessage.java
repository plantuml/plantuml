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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.WithStyle;

public abstract class AbstractMessage implements EventWithDeactivate, WithStyle {

	public Style[] getUsedStyles() {
		Style style = getDefaultStyleDefinition().getMergedStyle(styleBuilder);
		if (style != null && arrowConfiguration.getColor() != null) {
			style = style.eventuallyOverride(PName.LineColor, arrowConfiguration.getColor());
		}
		return new Style[] { style };
	}

	public StyleSignature getDefaultStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.sequenceDiagram, SName.arrow);
	}

	private final Display label;
	private final ArrowConfiguration arrowConfiguration;
	private final Set<LifeEventType> lifeEventsType = EnumSet.noneOf(LifeEventType.class);

	private Url url;
	private final String messageNumber;
	private boolean parallel = false;
	private final StyleBuilder styleBuilder;

	private List<Note> noteOnMessages = new ArrayList<Note>();

	public AbstractMessage(StyleBuilder styleBuilder, Display label, ArrowConfiguration arrowConfiguration,
			String messageNumber) {
		this.styleBuilder = styleBuilder;
		this.url = null;
		this.label = label;
		this.arrowConfiguration = arrowConfiguration;
		this.messageNumber = messageNumber;
	}

	public final void setUrl(Url url) {
		this.url = url;
	}

	public void goParallel() {
		this.parallel = true;
	}

	public boolean isParallel() {
		return parallel;
	}

	final public Url getUrl() {
		if (url == null) {
			for (Note n : noteOnMessages) {
				if (n.getUrl() != null) {
					return n.getUrl();
				}
			}
		}
		return url;
	}

	public boolean hasUrl() {
		for (Note n : noteOnMessages) {
			if (n.hasUrl()) {
				return true;
			}
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

	public final Display getLabelNumbered() {
		if (getMessageNumber() == null) {
			return getLabel();
		}
		Display result = Display.empty();
		result = result.add(new MessageNumber(getMessageNumber()));
		result = result.addAll(getLabel());
		return result;
	}

	public final ArrowConfiguration getArrowConfiguration() {
		return arrowConfiguration;
	}

	public final List<Note> getNoteOnMessages() {
		return noteOnMessages;
	}

	public final void setNote(Note note) {
		if (note.getPosition() != NotePosition.LEFT && note.getPosition() != NotePosition.RIGHT
				&& note.getPosition() != NotePosition.BOTTOM && note.getPosition() != NotePosition.TOP) {
			throw new IllegalArgumentException();
		}
		note = note.withPosition(overideNotePosition(note.getPosition()));
		this.noteOnMessages.add(note);
	}

	protected NotePosition overideNotePosition(NotePosition notePosition) {
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

	private String anchor;
	private String anchor1;
	private String anchor2;

	public void setAnchor(String anchor) {
		this.anchor = anchor;
		if (anchor != null && anchor.startsWith("{")) {
			throw new IllegalArgumentException(anchor);
		}
	}

	public void setPart1Anchor(String anchor) {
		this.anchor1 = anchor;
	}

	public void setPart2Anchor(String anchor) {
		this.anchor2 = anchor;
	}

	public String getAnchor() {
		return anchor;
	}

	public String getPart1Anchor() {
		return this.anchor1;
	}

	public String getPart2Anchor() {
		return this.anchor2;
	}

	public abstract Participant getParticipant1();

	public abstract Participant getParticipant2();
}
