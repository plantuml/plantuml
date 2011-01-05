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
 */
package net.sourceforge.plantuml.sequencediagram;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramFileMaker;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramTxtMaker;
import net.sourceforge.plantuml.skin.ProtectedSkin;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.skin.SkinUtils;
import net.sourceforge.plantuml.skin.rose.Rose;

public class SequenceDiagram extends UmlDiagram {

	private final Map<String, Participant> participants = new LinkedHashMap<String, Participant>();

	private final List<Event> events = new ArrayList<Event>();

	private final List<ParticipantEnglober> participantEnglobers = new ArrayList<ParticipantEnglober>();

	private Skin skin = new ProtectedSkin(new Rose());

	@Deprecated
	public Participant getOrCreateParticipant(String code) {
		return getOrCreateParticipant(code, StringUtils.getWithNewlines(code));
	}

	public Participant getOrCreateParticipant(String code, List<String> display) {
		Participant result = participants.get(code);
		if (result == null) {
			result = new Participant(ParticipantType.PARTICIPANT, code, display);
			participants.put(code, result);
		}
		return result;
	}

	private AbstractMessage lastMessage;

	public AbstractMessage getLastMessage() {
		return lastMessage;
	}

	public Participant createNewParticipant(ParticipantType type, String code, List<String> display) {
		if (participants.containsKey(code)) {
			throw new IllegalArgumentException();
		}
		if (display == null) {
			display = Arrays.asList(code);
		}
		final Participant result = new Participant(type, code, display);
		participants.put(code, result);
		return result;
	}

	public Map<String, Participant> participants() {
		return Collections.unmodifiableMap(participants);
	}

	public void addMessage(AbstractMessage m) {
		lastMessage = m;
		events.add(m);
		if (pendingCreate != null) {
			m.addLifeEvent(pendingCreate);
			pendingCreate = null;
		}
	}

	public void addNote(Note n) {
		events.add(n);
	}

	public void newpage(List<String> strings) {
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

	public void divider(List<String> strings) {
		events.add(new Divider(strings));
	}

	public List<Event> events() {
		return Collections.unmodifiableList(events);
	}

	private FileMaker getSequenceDiagramPngMaker(FileFormatOption fileFormatOption) {

		final FileFormat fileFormat = fileFormatOption.getFileFormat();

		if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT) {
			return new SequenceDiagramTxtMaker(this, fileFormat);
		}

		return new SequenceDiagramFileMaker(this, skin, fileFormatOption);
	}

	public List<File> createFiles(File suggestedFile, FileFormatOption fileFormat) throws IOException {
		return getSequenceDiagramPngMaker(fileFormat).createMany(suggestedFile);
	}

	public void createFile(OutputStream os, int index, FileFormatOption fileFormat) throws IOException {
		getSequenceDiagramPngMaker(fileFormat).createOne(os, index);
	}

	private LifeEvent pendingCreate = null;

	public void activate(Participant p, LifeEventType lifeEventType, HtmlColor backcolor) {
		if (lifeEventType == LifeEventType.CREATE) {
			pendingCreate = new LifeEvent(p, lifeEventType, backcolor);
			return;
		}
		if (lastMessage == null) {
			if (lifeEventType == LifeEventType.ACTIVATE) {
				p.incInitialLife(backcolor);
			}
			return;
			// throw new
			// UnsupportedOperationException("Step1Message::beforeMessage");
		}
		lastMessage.addLifeEvent(new LifeEvent(p, lifeEventType, backcolor));
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

	public String getDescription() {
		return "(" + participants.size() + " participants)";
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
		return showFootbox;
	}

	private boolean showFootbox = true;

	public void setShowFootbox(boolean footbox) {
		this.showFootbox = footbox;

	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.SEQUENCE;
	}

	private Participant boxStart;
	private List<String> boxStartComment;
	private HtmlColor boxColor;
	private boolean boxPending = false;

	public void boxStart(List<String> comment, HtmlColor color) {
		if (boxPending) {
			throw new IllegalStateException();
		}
		this.boxStart = getLastParticipant();
		this.boxStartComment = comment;
		this.boxColor = color;
		this.boxPending = true;
	}

	public void endBox() {
		if (boxPending == false) {
			throw new IllegalStateException();
		}
		final Participant last = getLastParticipant();
		this.participantEnglobers.add(new ParticipantEnglober(next(boxStart), last, boxStartComment, boxColor));
		this.boxStart = null;
		this.boxStartComment = null;
		this.boxColor = null;
		this.boxPending = false;
	}

	public boolean isBoxPending() {
		return boxPending;
	}

	private Participant next(Participant p) {
		if (p == null) {
			return participants.values().iterator().next();
		}
		for (final Iterator<Participant> it = participants.values().iterator(); it.hasNext();) {
			final Participant current = it.next();
			if (current == p && it.hasNext()) {
				return it.next();
			}
		}
		throw new IllegalArgumentException("p=" + p.getCode());
	}

	private Participant getLastParticipant() {
		Participant result = null;
		for (Participant p : participants.values()) {
			result = p;
		}
		return result;
	}

	public final List<ParticipantEnglober> getParticipantEnglobers() {
		return Collections.unmodifiableList(participantEnglobers);
	}

	@Override
	public int getNbImages() {
		return getSequenceDiagramPngMaker(new FileFormatOption(FileFormat.PNG)).getNbPages();
	}

}
