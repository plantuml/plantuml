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
 */
package net.sourceforge.plantuml.sequencediagram;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramFileMakerPuma;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramTxtMaker;
import net.sourceforge.plantuml.skin.ProtectedSkin;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.skin.SkinUtils;
import net.sourceforge.plantuml.skin.rose.Rose;

public class SequenceDiagram extends UmlDiagram {

	private final Map<String, Participant> participants = new LinkedHashMap<String, Participant>();

	private final List<Event> events = new ArrayList<Event>();

	private final Map<Participant, ParticipantEnglober> participantEnglobers2 = new HashMap<Participant, ParticipantEnglober>();

	private Skin skin = new ProtectedSkin(new Rose());

	@Deprecated
	public Participant getOrCreateParticipant(String code) {
		return getOrCreateParticipant(code, Display.getWithNewlines(code));
	}

	public Participant getOrCreateParticipant(String code, Display display) {
		Participant result = participants.get(code);
		if (result == null) {
			result = new Participant(ParticipantType.PARTICIPANT, code, display);
			participants.put(code, result);
			participantEnglobers2.put(result, participantEnglober);
		}
		return result;
	}

	private AbstractMessage lastMessage;

	public AbstractMessage getLastMessage() {
		return lastMessage;
	}

	public Participant createNewParticipant(ParticipantType type, String code, Display display) {
		if (participants.containsKey(code)) {
			throw new IllegalArgumentException();
		}
		if (display == null) {
			// display = Arrays.asList(code);
			display = Display.getWithNewlines(code);
		}
		final Participant result = new Participant(type, code, display);
		participants.put(code, result);
		participantEnglobers2.put(result, participantEnglober);
		return result;
	}

	public Map<String, Participant> participants() {
		return Collections.unmodifiableMap(participants);
	}

	public String addMessage(AbstractMessage m) {
		lastMessage = m;
		lastDelay = null;
		events.add(m);
		if (pendingCreate != null) {
			if (m.compatibleForCreate(pendingCreate.getParticipant()) == false) {
				return "After create command, you have to send a message to \"" + pendingCreate.getParticipant() + "\"";
			}
			m.addLifeEvent(pendingCreate);
			pendingCreate = null;
		}
		return null;
	}

	public void addNote(Note n, boolean tryMerge) {
		if (tryMerge && events.size() > 0) {
			final Event last = events.get(events.size() - 1);
			if (last instanceof Note) {
				final Notes notes = new Notes((Note) last, n);
				events.set(events.size() - 1, notes);
				return;
			}
			if (last instanceof Notes) {
				((Notes) last).add(n);
				return;
			}
		}
		events.add(n);
	}

	public void newpage(Display strings) {
		if (ignoreNewpage) {
			return;
		}
		events.add(new Newpage(strings));
	}

	private boolean ignoreNewpage = false;

	public void ignoreNewpage() {
		this.ignoreNewpage = true;
	}

	private int autonewpage = -1;

	public final int getAutonewpage() {
		return autonewpage;
	}

	public void setAutonewpage(int autonewpage) {
		this.autonewpage = autonewpage;
	}

	public void divider(Display strings) {
		events.add(new Divider(strings));
	}

	public void hspace() {
		events.add(new HSpace());
	}

	public void hspace(int pixel) {
		events.add(new HSpace(pixel));
	}

	private Delay lastDelay;

	public void delay(Display strings) {
		final Delay delay = new Delay(strings);
		events.add(delay);
		lastDelay = delay;
	}

	public List<Event> events() {
		return Collections.unmodifiableList(events);
	}

	private FileMaker getSequenceDiagramPngMaker(FileFormatOption fileFormatOption, List<BufferedImage> flashcodes) {

		final FileFormat fileFormat = fileFormatOption.getFileFormat();

		if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT) {
			return new SequenceDiagramTxtMaker(this, fileFormat);
		}

		return new SequenceDiagramFileMakerPuma(this, skin, fileFormatOption, flashcodes);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormat,
			List<BufferedImage> flashcodes) throws IOException {
		final FileMaker sequenceDiagramPngMaker = getSequenceDiagramPngMaker(fileFormat, flashcodes);
		return sequenceDiagramPngMaker.createOne(os, index, fileFormat.isWithMetadata());
	}

	// support for CommandReturn
	private final Stack<Message> activationState = new Stack<Message>();

	public Message getActivatingMessage() {
		if (activationState.empty()) {
			return null;
		}
		return activationState.peek();
	}

	private LifeEvent pendingCreate = null;

	public String activate(Participant p, LifeEventType lifeEventType, HtmlColor backcolor) {
		if (lastDelay != null) {
			return "You cannot Activate/Deactivate just after a ...";
		}
		if (lifeEventType == LifeEventType.CREATE) {
			pendingCreate = new LifeEvent(p, lifeEventType, backcolor);
			return null;
		}
		if (lastMessage == null) {
			if (lifeEventType == LifeEventType.ACTIVATE) {
				p.incInitialLife(backcolor);
				return null;
			}
			return "Only activate command can occur before message are send";
		}
		if (lifeEventType == LifeEventType.ACTIVATE && lastMessage instanceof Message) {
			activationState.push((Message) lastMessage);
		} else if (lifeEventType == LifeEventType.DEACTIVATE && activationState.empty() == false) {
			activationState.pop();
		}
		final boolean ok = lastMessage.addLifeEvent(new LifeEvent(p, lifeEventType, backcolor));
		if (ok) {
			return null;
		}
		return "Activate/Deactivate already done on " + p.getCode();
	}

	private final List<GroupingStart> openGroupings = new ArrayList<GroupingStart>();

	public boolean grouping(String title, String comment, GroupingType type, HtmlColor backColorGeneral,
			HtmlColor backColorElement) {
		if (type != GroupingType.START && openGroupings.size() == 0) {
			return false;
		}

		final GroupingStart top = openGroupings.size() > 0 ? openGroupings.get(0) : null;

		final Grouping g = type == GroupingType.START ? new GroupingStart(title, comment, backColorGeneral,
				backColorElement, top)
				: new GroupingLeaf(title, comment, type, backColorGeneral, backColorElement, top);
		events.add(g);

		if (type == GroupingType.START) {
			openGroupings.add(0, (GroupingStart) g);
		} else if (type == GroupingType.END) {
			openGroupings.remove(0);
		}

		return true;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(" + participants.size() + " participants)", getClass());
	}

	public boolean changeSkin(String className) {
		final Skin s = SkinUtils.loadSkin(className);
		final Integer expected = new Integer(1);
		if (s != null && expected.equals(s.getProtocolVersion())) {
			this.skin = new ProtectedSkin(s);
			return true;
		}
		return false;
	}

	public Skin getSkin() {
		return skin;
	}

	private Integer messageNumber = null;
	private int incrementMessageNumber;

	private DecimalFormat decimalFormat;

	public final void goAutonumber(int startingNumber, int increment, DecimalFormat decimalFormat) {
		this.messageNumber = startingNumber;
		this.incrementMessageNumber = increment;
		this.decimalFormat = decimalFormat;
	}

	public String getNextMessageNumber() {
		if (messageNumber == null) {
			return null;
		}
		final Integer result = messageNumber;
		messageNumber += incrementMessageNumber;
		return decimalFormat.format(result);
	}

	public boolean isShowFootbox() {
		if (getSkinParam().strictUmlStyle()) {
			return false;
		}
		final String footbox = getSkinParam().getValue("footbox");
		if (footbox == null) {
			return showFootbox;
		}
		if (footbox.equalsIgnoreCase("hide")) {
			return false;
		}
		return true;
	}

	private boolean showFootbox = true;

	public void setShowFootbox(boolean footbox) {
		this.showFootbox = footbox;

	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.SEQUENCE;
	}

	private ParticipantEnglober participantEnglober;

	public void boxStart(Display comment, HtmlColor color) {
		if (participantEnglober != null) {
			throw new IllegalStateException();
		}
		this.participantEnglober = new ParticipantEnglober(comment, color);
	}

	public void endBox() {
		if (participantEnglober == null) {
			throw new IllegalStateException();
		}
		this.participantEnglober = null;
	}

	public boolean isBoxPending() {
		return participantEnglober != null;
	}


	@Override
	public int getNbImages() {
		return getSequenceDiagramPngMaker(new FileFormatOption(FileFormat.PNG), null).getNbPages();
	}

	public void removeHiddenParticipants() {
		for (Participant p : new ArrayList<Participant>(participants.values())) {
			if (isAlone(p)) {
				remove(p);
			}
		}
	}

	private void remove(Participant p) {
		final boolean ok = participants.values().remove(p);
		if (ok == false) {
			throw new IllegalArgumentException();
		}
		participantEnglobers2.remove(p);
	}

	private boolean isAlone(Participant p) {
		for (Event ev : events) {
			if (ev.dealWith(p)) {
				return false;
			}
		}
		return true;
	}

	public void putParticipantInLast(String code) {
		final Participant p = participants.get(code);
		if (p == null) {
			throw new IllegalArgumentException(code);
		}
		participants.remove(code);
		participants.put(code, p);
		participantEnglobers2.put(p, participantEnglober);
	}

	public ParticipantEnglober getEnglober(Participant p) {
		return participantEnglobers2.get(p);
	}

	private boolean autoactivate;

	public final void setAutoactivate(boolean autoactivate) {
		this.autoactivate = autoactivate;
	}

	public final boolean isAutoactivate() {
		return autoactivate;
	}

	public boolean hasUrl() {
		for (Participant p : participants.values()) {
			if (p.getUrl() != null) {
				return true;
			}
		}
		for (Event ev : events) {
			if (ev.hasUrl()) {
				return true;
			}
		}
		if (getLegend() != null && getLegend().hasUrl()) {
			return true;
		}
		return false;
	}

	public void addReference(Reference ref) {
		events.add(ref);
	}

	@Override
	public boolean isOk() {
		if (participants.size() == 0) {
			return false;
		}
		return true;
	}

}
