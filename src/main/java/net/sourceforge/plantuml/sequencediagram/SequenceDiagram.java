/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.abel.EntityPortion;
import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramFileMakerPuma2;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramTxtMaker;
import net.sourceforge.plantuml.sequencediagram.teoz.SequenceDiagramFileMakerTeoz;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.PragmaKey;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.xmi.SequenceDiagramXmiMaker;

public class SequenceDiagram extends UmlDiagram {

	private boolean hideUnlinkedData;

	public final boolean isHideUnlinkedData() {
		return hideUnlinkedData;
	}

	public final void setHideUnlinkedData(boolean hideUnlinkedData) {
		this.hideUnlinkedData = hideUnlinkedData;
	}

	private final List<Participant> participantsList = new ArrayList<>();

	private final List<Event> events = new ArrayList<>();

	private final Map<Participant, ParticipantEnglober> participantEnglobers2 = new HashMap<Participant, ParticipantEnglober>();

	private final Rose skin2 = new Rose();

	private final AtomicInteger cpt = new AtomicInteger(1);

	public AtomicInteger getCounter() {
		return cpt;
	}

	public SequenceDiagram(UmlSource source, Previous previous, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.SEQUENCE, previous, preprocessing);
	}

	@Deprecated
	public Participant getOrCreateParticipant(LineLocation location, String code) {
		return getOrCreateParticipant(location, code, Display.getWithNewlines(getPragma(), code));
	}

	public Participant getOrCreateParticipant(LineLocation location, String code, Display display) {
		Participant result = participantsget(code);
		if (result == null) {
			result = new Participant(location, ParticipantType.PARTICIPANT, code, display, hiddenPortions, 0,
					getSkinParam().getCurrentStyleBuilder(), createUid());
			addWithOrder(result);
			participantEnglobers2.put(result, participantEnglober);
		}
		return result;
	}

	private Participant participantsget(String code) {
		for (Participant p : participantsList)
			if (p.getCode().equals(code))
				return p;

		return null;
	}

	private EventWithDeactivate lastEventWithDeactivate;

	public EventWithDeactivate getLastEventWithDeactivate() {
		for (int i = events.size() - 1; i >= 0; i--)
			if (events.get(i) instanceof EventWithDeactivate)
				return (EventWithDeactivate) events.get(i);
		return null;
	}

	public EventWithNote getLastEventWithNote() {
		for (int i = events.size() - 1; i >= 0; i--)
			if (events.get(i) instanceof EventWithNote)
				return (EventWithNote) events.get(i);
		return null;
	}

	public Participant createNewParticipant(LineLocation location, ParticipantType type, String code, Display display,
			int order) {
		if (participantsget(code) != null)
			throw new IllegalArgumentException();

		if (Display.isNull(display)) {
			// display = Arrays.asList(code);
			display = Display.getWithNewlines(getPragma(), code);
		}

		final Participant result = new Participant(location, type, code, display, hiddenPortions, order,
				getSkinParam().getCurrentStyleBuilder(), createUid());
		addWithOrder(result);
		participantEnglobers2.put(result, participantEnglober);
		return result;
	}

	private String createUid() {
		return "part" + cpt.getAndAdd(1);
	}

	private void addWithOrder(final Participant result) {
		for (int i = 0; i < participantsList.size(); i++)
			if (result.getOrder() < participantsList.get(i).getOrder()) {
				participantsList.add(i, result);
				return;
			}

		participantsList.add(result);
	}

	public Collection<Participant> participants() {
		return Collections.unmodifiableCollection(participantsList);
	}

	public boolean participantsContainsKey(String code) {
		return participantsget(code) != null;
	}

	public CommandExecutionResult addMessage(AbstractMessage m) {
		if (m.isParallel())
			m.setParallelBrother(getLastAbstractMessage());

		lastEventWithDeactivate = m;
		lastDelay = null;
		events.add(m);
		if (pendingCreate != null) {
			if (m.compatibleForCreate(pendingCreate.getParticipant()) == false)
				return CommandExecutionResult.error("After create command, you have to send a message to \""
						+ pendingCreate.getParticipant() + "\"");

			m.addLifeEvent(pendingCreate);
			pendingCreate = null;
		}
		return CommandExecutionResult.ok();
	}

	private AbstractMessage getLastAbstractMessage() {
		for (int i = events.size() - 1; i >= 0; i--)
			if (events.get(i) instanceof AbstractMessage)
				return (AbstractMessage) events.get(i);

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
		if (ignoreNewpage)
			return;

		events.add(new Newpage(strings, getSkinParam().getCurrentStyleBuilder()));
		countNewpage++;
	}

	private boolean ignoreNewpage = false;
	private int countNewpage = 0;

	public void ignoreNewpage() {
		this.ignoreNewpage = true;
	}

	// private int autonewpage = -1;

//	public final int getAutonewpage() {
//		return autonewpage;
//	}

	public void setAutonewpage(int autonewpage) {
//		this.autonewpage = autonewpage;
	}

	public void divider(Display strings) {
		events.add(new Divider(strings, getSkinParam().getCurrentStyleBuilder()));
	}

	public void hspace() {
		events.add(new HSpace(25));
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

		// We reset the counter for messages
		this.cpt.set(1);

		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		// ::comment when __CORE__
		if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT)
			return new SequenceDiagramTxtMaker(this, fileFormat);

		if (fileFormat.name().startsWith("XMI"))
			return new SequenceDiagramXmiMaker(this, fileFormat);
		// ::done

		if (modeTeoz())
			return new SequenceDiagramFileMakerTeoz(this, skin2, fileFormatOption, index);

		return new SequenceDiagramFileMakerPuma2(this, skin2, fileFormatOption);
	}

	private boolean modeTeoz() {
		return GlobalConfig.FORCE_TEOZ || getPragma().isTrue(PragmaKey.TEOZ);
	}

	@Override
	public ImageBuilder createImageBuilder(FileFormatOption fileFormatOption) throws IOException {
		return super.createImageBuilder(fileFormatOption).annotations(false);
		// they are managed in the SequenceDiagramFileMaker* classes
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormat)
			throws IOException {
		final FileMaker sequenceDiagramPngMaker = getSequenceDiagramPngMaker(index, fileFormat);
		return sequenceDiagramPngMaker.createOne(os, index, fileFormat.isWithMetadata());
	}

	@Override
	final public void exportDiagramGraphic(UGraphic ug, FileFormatOption fileFormatOption) {
		final FileMaker sequenceDiagramPngMaker = getSequenceDiagramPngMaker(0, fileFormatOption);
		sequenceDiagramPngMaker.createOneGraphic(ug);
	}

	@Override
	final protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		throw new UnsupportedOperationException();
	}

	// support for CommandReturn
	private final Stack<AbstractMessage> activationState = new Stack<>();

	public AbstractMessage getActivatingMessage() {
		if (activationState.empty())
			return null;

		return activationState.peek();
	}

	private LifeEvent pendingCreate = null;

	public String activate(Participant p, LifeEventType lifeEventType, HColor backcolor) {
		return activate(p, lifeEventType, backcolor, null);
	}

	public String activate(Participant p, LifeEventType lifeEventType, HColor backcolor, HColor linecolor) {
		if (lastDelay != null)
			return "You cannot Activate/Deactivate just after a ...";

		final LifeEvent lifeEvent = new LifeEvent(p, lifeEventType, new Fashion(backcolor, linecolor),
				getSkinParam().getCurrentStyleBuilder());
		events.add(lifeEvent);
		if (lifeEventType == LifeEventType.CREATE) {
			pendingCreate = lifeEvent;
			return null;
		}
		if (lastEventWithDeactivate == null) {
			if (lifeEventType == LifeEventType.ACTIVATE) {
				p.incInitialLife(new Fashion(backcolor, linecolor));
				return null;
			}
			if (p.getInitialLife() == 0)
				return "You cannot deactivate here";

			return null;
		}
		if (lifeEventType == LifeEventType.ACTIVATE && lastEventWithDeactivate instanceof AbstractMessage)
			activationState.push((AbstractMessage) lastEventWithDeactivate);
		else if (lifeEventType == LifeEventType.DEACTIVATE && activationState.empty() == false)
			activationState.pop();

		final boolean ok = lastEventWithDeactivate.addLifeEvent(lifeEvent);
		if (lastEventWithDeactivate instanceof AbstractMessage) {
			final AbstractMessage lastMessage = (AbstractMessage) lastEventWithDeactivate;
			lifeEvent.setMessage(lastMessage);
		}
		if (ok)
			return null;

		return "Activate/Deactivate already done on " + p.getCode();
	}

	private final List<GroupingStart> openGroupings = new ArrayList<>();

	public boolean grouping(String title, String comment, GroupingType type, HColor backColorGeneral,
			HColor backColorElement, boolean parallel) {
		if (type != GroupingType.START && openGroupings.size() == 0)
			return false;

		if (backColorGeneral == null)
			backColorGeneral = getSkinParam().getHtmlColor(ColorParam.sequenceGroupBodyBackground, null, false);

		final GroupingStart top = openGroupings.size() > 0 ? openGroupings.get(0) : null;

		final Grouping g = type == GroupingType.START
				? new GroupingStart(title, comment, backColorGeneral, backColorElement, top,
						getSkinParam().getCurrentStyleBuilder())
				: new GroupingLeaf(title, comment, type, backColorGeneral, backColorElement, top,
						getSkinParam().getCurrentStyleBuilder());
		events.add(g);

		if (type == GroupingType.START) {
			if (parallel)
				((GroupingStart) g).goParallel();

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
		if (getSkinParam().strictUmlStyle())
			return false;

		final String footbox = getSkinParam().getValue("footbox");
		if (footbox == null)
			return showFootbox;

		if (footbox.equalsIgnoreCase("hide"))
			return false;

		return true;
	}

	private boolean showFootbox = true;

	public void setShowFootbox(boolean footbox) {
		this.showFootbox = footbox;
	}

	private ParticipantEnglober participantEnglober;

	public void boxStart(Display comment, HColor color, Stereotype stereotype) {
		if (participantEnglober == null)
			this.participantEnglober = ParticipantEnglober.build(comment, color, stereotype);
		else
			this.participantEnglober = participantEnglober.newChild(comment, color, stereotype);

	}

	public void endBox() {
		if (participantEnglober == null)
			throw new IllegalStateException();

		this.participantEnglober = participantEnglober.getParent();
	}

	public boolean isBoxPending() {
		return participantEnglober != null;
	}

	@Override
	public int getNbImages() {
		return countNewpage + 1;
//		// ::comment when __CORE__
//		try {
//			// The DEBUG StringBounder is ok just to compute the number of pages here.
//			return getSequenceDiagramPngMaker(1, new FileFormatOption(FileFormat.DEBUG)).getNbPages();
//		} catch (Throwable t) {
//			Logme.error(t);
//			// ::done
//		return 1;
//			// ::comment when __CORE__
//		}
//		// ::done
	}

	public void removeHiddenParticipants() {
		for (Participant p : new ArrayList<>(participantsList))
			if (isAlone(p))
				remove(p);
	}

	private void remove(Participant p) {
		final boolean ok = participantsList.remove(p);
		if (ok == false)
			throw new IllegalArgumentException();

		participantEnglobers2.remove(p);
	}

	private boolean isAlone(Participant p) {
		for (Event ev : events)
			if (ev.dealWith(p))
				return false;

		return true;
	}

	public void putParticipantInLast(String code) {
		final Participant p = Objects.requireNonNull(participantsget(code), code);
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
		for (Participant p : participantsList)
			if (p.getUrl() != null)
				return true;

		for (Event ev : events)
			if (ev.hasUrl())
				return true;

		if (getLegend().isNull() == false && getLegend().hasUrl())
			return true;

		return false;
	}

	public void addReference(Reference ref) {
		events.add(ref);
	}

	@Override
	public boolean isOk() {
		if (participantsList.size() == 0)
			return false;

		return true;
	}

	@Override
	public String checkFinalError() {
		if (this.isHideUnlinkedData())
			this.removeHiddenParticipants();

		return super.checkFinalError();
	}

	private final Set<EntityPortion> hiddenPortions = EnumSet.<EntityPortion>noneOf(EntityPortion.class);

	public void hideOrShow(Set<EntityPortion> portions, boolean show) {
		if (show)
			hiddenPortions.removeAll(portions);
		else
			hiddenPortions.addAll(portions);

	}

	public Display manageVariable(Display labels) {
		return labels.replace("%autonumber%", autoNumber.getCurrentMessageNumber(false));
	}

	private final List<LinkAnchor> linkAnchors = new ArrayList<>();

	public CommandExecutionResult linkAnchor(String anchor1, String anchor2, String message) {
		this.linkAnchors.add(new LinkAnchor(anchor1, anchor2, message));
		return CommandExecutionResult.ok();
	}

	public List<LinkAnchor> getLinkAnchors() {
		return Collections.unmodifiableList(linkAnchors);
	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return modeTeoz() // this is for backward compatibility
				? ClockwiseTopRightBottomLeft.same(5)
				: ClockwiseTopRightBottomLeft.topRightBottomLeft(5, 5, 5, 0);
	}
}
