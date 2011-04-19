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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
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

	private final Map<Participant, ParticipantEnglober> participantEnglobers2 = new HashMap<Participant, ParticipantEnglober>();

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
			participantEnglobers2.put(result, participantEnglober);
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
		participantEnglobers2.put(result, participantEnglober);
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

	public void delay(List<String> strings) {
		events.add(new Delay(strings));
	}

	public List<Event> events() {
		return Collections.unmodifiableList(events);
	}

	private FileMaker getSequenceDiagramPngMaker(FileFormatOption fileFormatOption, List<BufferedImage> flashcodes) {

		final FileFormat fileFormat = fileFormatOption.getFileFormat();

		if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT) {
			return new SequenceDiagramTxtMaker(this, fileFormat);
		}

		return new SequenceDiagramFileMaker(this, skin, fileFormatOption, flashcodes);
	}

	// public List<File> exportDiagrams(File suggestedFile, FileFormatOption
	// fileFormat) throws IOException {
	// return
	// getSequenceDiagramPngMaker(fileFormat).createManyRRMV(suggestedFile);
	// }

	public List<File> exportDiagrams(File suggestedFile, FileFormatOption fileFormat) throws IOException {

		final List<File> result = new ArrayList<File>();
		final int nbImages = getNbImages();
		for (int i = 0; i < nbImages; i++) {

			final File f = SequenceDiagramFileMaker.computeFilename(suggestedFile, i, fileFormat.getFileFormat());
			Log.info("Creating file: " + f);
			final FileOutputStream fos = new FileOutputStream(f);
			final StringBuilder cmap = new StringBuilder();
			try {
				exportDiagram(fos, cmap, i, fileFormat);
			} finally {
				fos.close();
			}
			if (this.hasUrl() && cmap.length() > 0) {
				exportCmap(suggestedFile, cmap);
			}
			Log.info("File size : " + f.length());
			result.add(f);
		}
		return result;
	}

	@Override
	protected void exportDiagramInternal(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormat,
			List<BufferedImage> flashcodes) throws IOException {
		final FileMaker sequenceDiagramPngMaker = getSequenceDiagramPngMaker(fileFormat, flashcodes);
		sequenceDiagramPngMaker.createOne(os, index);
		if (this.hasUrl() && fileFormat.getFileFormat() == FileFormat.PNG) {
			sequenceDiagramPngMaker.appendCmap(cmap);
		}
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

	// private Participant boxStart;
	// private List<String> boxStartComment;
	// private HtmlColor boxColor;
	// private boolean boxPending = false;

	private ParticipantEnglober participantEnglober;

	public void boxStart(List<String> comment, HtmlColor color) {
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

	// private Participant next(Participant p) {
	// if (p == null) {
	// return participants.values().iterator().next();
	// }
	// for (final Iterator<Participant> it = participants.values().iterator();
	// it.hasNext();) {
	// final Participant current = it.next();
	// if (current == p && it.hasNext()) {
	// return it.next();
	// }
	// }
	// throw new IllegalArgumentException("p=" + p.getCode());
	// }
	//
	// private Participant getLastParticipant() {
	// Participant result = null;
	// for (Participant p : participants.values()) {
	// result = p;
	// }
	// return result;
	// }
	//
	// public final List<ParticipantEnglober> getParticipantEnglobers() {
	// return Collections.unmodifiableList(participantEnglobers);
	// }

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
			if (ev.getUrl() != null) {
				return true;
			}
		}
		return false;
	}

	public void addReference(Reference ref) {
		events.add(ref);
	}

}
