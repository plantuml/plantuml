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
 */
package net.sourceforge.plantuml.sequencediagram;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramFileMakerPuma2;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramTxtMaker;
import net.sourceforge.plantuml.sequencediagram.teoz.SequenceDiagramFileMakerTeoz;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class SequenceDiagram extends UmlDiagram {

	private final List<Participant> participantsList = new ArrayList<Participant>();

	private final List<Event> events = new ArrayList<Event>();

	private final Map<Participant, ParticipantEnglober> participantEnglobers2 = new HashMap<Participant, ParticipantEnglober>();

	private final Rose skin2 = new Rose();

	public SequenceDiagram(ISkinSimple skinParam) {
		super(skinParam);
	}

	@Deprecated
	public Participant getOrCreateParticipant(String code) {
		return getOrCreateParticipant(code, Display.getWithNewlines(code));
	}

	public Participant getOrCreateParticipant(String code, Display display) {
		Participant result = participantsget(code);
		if (result == null) {
			result = new Participant(ParticipantType.PARTICIPANT, code, display, hiddenPortions, 0,
					getSkinParam().getCurrentStyleBuilder());
			addWithOrder(result);
			participantEnglobers2.put(result, participantEnglober);
		}
		return result;
	}

	private Participant participantsget(String code) {
		for (Participant p : participantsList) {
			if (p.getCode().equals(code)) {
				return p;
			}
		}
		return null;
	}

	private EventWithDeactivate lastEventWithDeactivate;

	public EventWithDeactivate getLastEventWithDeactivate() {
		return lastEventWithDeactivate;
	}

	public Participant createNewParticipant(ParticipantType type, String code, Display display, int order) {
		if (participantsget(code) != null) {
			throw new IllegalArgumentException();
		}
		if (Display.isNull(display)) {
			// display = Arrays.asList(code);
			display = Display.getWithNewlines(code);
		}
		final Participant result = new Participant(type, code, display, hiddenPortions, order,
				getSkinParam().getCurrentStyleBuilder());
		addWithOrder(result);
		participantEnglobers2.put(result, participantEnglober);
		return result;
	}

	private void addWithOrder(final Participant result) {
		for (int i = 0; i < participantsList.size(); i++) {
			if (result.getOrder() < participantsList.get(i).getOrder()) {
				participantsList.add(i, result);
				return;
			}
		}
		participantsList.add(result);
	}

	public Collection<Participant> participants() {
		return Collections.unmodifiableCollection(participantsList);
	}

	public boolean participantsContainsKey(String code) {
		return participantsget(code) != null;
	}

	public String addMessage(AbstractMessage m) {
		lastEventWithDeactivate = m;
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
		// this.lastEventWithDeactivate = null;
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
		events.add(new Divider(strings, getSkinParam().getCurrentStyleBuilder()));
	}

	public void hspace() {
		events.add(new HSpace());
	}

	public void hspace(int pixel) {
		events.add(new HSpace(pixel));
	}

	private Delay lastDelay;

	public void delay(Display strings) {
		final Delay delay = new Delay(strings, getSkinParam().getCurrentStyleBuilder());
		events.add(delay);
		lastDelay = delay;
	}

	public List<Event> events() {
		return Collections.unmodifiableList(events);
	}

	private FileMaker getSequenceDiagramPngMaker(int index, FileFormatOption fileFormatOption) {

		final FileFormat fileFormat = fileFormatOption.getFileFormat();

		if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT) {
			return new SequenceDiagramTxtMaker(this, fileFormat);
		}

		if (modeTeoz()) {
			return new SequenceDiagramFileMakerTeoz(this, skin2, fileFormatOption, index);
		}

		return new SequenceDiagramFileMakerPuma2(this, skin2, fileFormatOption);
	}

	private boolean modeTeoz() {
		return OptionFlags.FORCE_TEOZ || getPragma().useTeozLayout();
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormat)
			throws IOException {
		final FileMaker sequenceDiagramPngMaker = getSequenceDiagramPngMaker(index, fileFormat);
		return sequenceDiagramPngMaker.createOne(os, index, fileFormat.isWithMetadata());
	}

	// support for CommandReturn
	private final Stack<AbstractMessage> activationState = new Stack<AbstractMessage>();

	public AbstractMessage getActivatingMessage() {
		if (activationState.empty()) {
			return null;
		}
		return activationState.peek();
	}

	private LifeEvent pendingCreate = null;

	public String activate(Participant p, LifeEventType lifeEventType, HColor backcolor) {
		return activate(p, lifeEventType, backcolor, null);
	}

	public String activate(Participant p, LifeEventType lifeEventType, HColor backcolor, HColor linecolor) {
		if (lastDelay != null) {
			return "You cannot Activate/Deactivate just after a ...";
		}
		final LifeEvent lifeEvent = new LifeEvent(p, lifeEventType, new SymbolContext(backcolor, linecolor));
		events.add(lifeEvent);
		if (lifeEventType == LifeEventType.CREATE) {
			pendingCreate = lifeEvent;
			return null;
		}
		if (lastEventWithDeactivate == null) {
			if (lifeEventType == LifeEventType.ACTIVATE) {
				p.incInitialLife(new SymbolContext(backcolor, linecolor));
				return null;
			}
			if (p.getInitialLife() == 0) {
				return "You cannot deactivate here";
			}
			return null;
		}
		if (lifeEventType == LifeEventType.ACTIVATE && lastEventWithDeactivate instanceof AbstractMessage) {
			activationState.push((AbstractMessage) lastEventWithDeactivate);
		} else if (lifeEventType == LifeEventType.DEACTIVATE && activationState.empty() == false) {
			activationState.pop();
		}
		final boolean ok = lastEventWithDeactivate.addLifeEvent(lifeEvent);
		if (lastEventWithDeactivate instanceof AbstractMessage) {
			lifeEvent.setMessage((AbstractMessage) lastEventWithDeactivate);
		}
		if (ok) {
			return null;
		}
		return "Activate/Deactivate already done on " + p.getCode();
	}

	private final List<GroupingStart> openGroupings = new ArrayList<GroupingStart>();

	public boolean grouping(String title, String comment, GroupingType type, HColor backColorGeneral,
			HColor backColorElement, boolean parallel) {
		if (type != GroupingType.START && openGroupings.size() == 0) {
			return false;
		}
		if (backColorGeneral == null) {
			backColorGeneral = getSkinParam().getHtmlColor(ColorParam.sequenceGroupBodyBackground, null, false);
		}

		final GroupingStart top = openGroupings.size() > 0 ? openGroupings.get(0) : null;

		final Grouping g = type == GroupingType.START
				? new GroupingStart(title, comment, backColorGeneral, backColorElement, top,
						getSkinParam().getCurrentStyleBuilder())
				: new GroupingLeaf(title, comment, type, backColorGeneral, backColorElement, top,
						getSkinParam().getCurrentStyleBuilder());
		events.add(g);

		if (type == GroupingType.START) {
			if (parallel) {
				((GroupingStart) g).goParallel();
			}
			openGroupings.add(0, (GroupingStart) g);
		} else if (type == GroupingType.END) {
			openGroupings.remove(0);
			lastEventWithDeactivate = (GroupingLeaf) g;
		}

		return true;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(" + participantsList.size() + " participants)");
	}

	private final AutoNumber autoNumber = new AutoNumber();

	public final void autonumberGo(DottedNumber startingNumber, int increment, DecimalFormat decimalFormat) {
		autoNumber.go(startingNumber, increment, decimalFormat);
	}

	public final void autonumberStop() {
		autoNumber.stop();
	}

	public final AutoNumber getAutoNumber() {
		return autoNumber;
	}

	// public final void autonumberResume(DecimalFormat decimalFormat) {
	// autoNumber.resume(decimalFormat);
	// }
	//
	// public final void autonumberResume(int increment, DecimalFormat
	// decimalFormat) {
	// autoNumber.resume(increment, decimalFormat);
	// }

	public String getNextMessageNumber() {
		return autoNumber.getNextMessageNumber();
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

	public void boxStart(Display comment, HColor color, Stereotype stereotype) {
		if (participantEnglober != null) {
			throw new IllegalStateException();
		}
		this.participantEnglober = new ParticipantEnglober(comment, color, stereotype);
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
		try {
			return getSequenceDiagramPngMaker(1, new FileFormatOption(FileFormat.PNG)).getNbPages();
		} catch (Throwable t) {
			t.printStackTrace();
			return 1;
		}
	}

	public void removeHiddenParticipants() {
		for (Participant p : new ArrayList<Participant>(participantsList)) {
			if (isAlone(p)) {
				remove(p);
			}
		}
	}

	private void remove(Participant p) {
		final boolean ok = participantsList.remove(p);
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
		final Participant p = participantsget(code);
		if (p == null) {
			throw new IllegalArgumentException(code);
		}
		final boolean ok = participantsList.remove(p);
		assert ok;
		addWithOrder(p);
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
		for (Participant p : participantsList) {
			if (p.getUrl() != null) {
				return true;
			}
		}
		for (Event ev : events) {
			if (ev.hasUrl()) {
				return true;
			}
		}
		if (getLegend().isNull() == false && getLegend().hasUrl()) {
			return true;
		}
		return false;
	}

	public void addReference(Reference ref) {
		events.add(ref);
	}

	@Override
	public boolean isOk() {
		if (participantsList.size() == 0) {
			return false;
		}
		return true;
	}

	public double getDpiFactor(FileFormatOption fileFormatOption, Dimension2D dim) {
		final double dpiFactor;
		final Scale scale = getScale();
		if (scale == null) {
			dpiFactor = getScaleCoef(fileFormatOption);
		} else {
			dpiFactor = scale.getScale(dim.getWidth(), dim.getHeight());
		}
		return dpiFactor;
	}

	@Override
	public String checkFinalError() {
		if (this.isHideUnlinkedData()) {
			this.removeHiddenParticipants();
		}
		return super.checkFinalError();
	}

	private final Set<EntityPortion> hiddenPortions = EnumSet.<EntityPortion>noneOf(EntityPortion.class);

	public void hideOrShow(Set<EntityPortion> portions, boolean show) {
		if (show) {
			hiddenPortions.removeAll(portions);
		} else {
			hiddenPortions.addAll(portions);
		}
	}

	public Display manageVariable(Display labels) {
		return labels.replace("%autonumber%", autoNumber.getCurrentMessageNumber(false));
	}

	private final List<LinkAnchor> linkAnchors = new ArrayList<LinkAnchor>();

	public CommandExecutionResult linkAnchor(String anchor1, String anchor2, String message) {
		this.linkAnchors.add(new LinkAnchor(anchor1, anchor2, message));
		return CommandExecutionResult.ok();
	}

	public List<LinkAnchor> getLinkAnchors() {
		return Collections.unmodifiableList(linkAnchors);
	}
}
