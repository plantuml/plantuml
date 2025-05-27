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
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Divider;
import net.sourceforge.plantuml.sequencediagram.Doll;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.GroupingLeaf;
import net.sourceforge.plantuml.sequencediagram.GroupingStart;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.HSpace;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Notes;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantEnglober;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.PaddingParam;
import net.sourceforge.plantuml.skin.SkinParamBackcolored;
import net.sourceforge.plantuml.skin.SkinParamBackcoloredReference;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

class DrawableSetInitializer {

	private ComponentType defaultLineType;
	private final DrawableSet drawableSet;
	private final boolean showTail;

	private double freeX = 0;
	private Frontier freeY2 = null;
	private Frontier lastFreeY2 = null;

	// private final double autonewpage;

	private ConstraintSet constraintSet;

	public DrawableSetInitializer(Rose skin, ISkinParam skinParam, boolean showTail, /*double autonewpage,*/
			AtomicInteger counter) {
		this.drawableSet = new DrawableSet(skin, skinParam, counter);
		this.showTail = showTail;
		// this.autonewpage = autonewpage;

	}

	private boolean useContinueLineBecauseOfDelay() {
		final String strategy = drawableSet.getSkinParam().getValue("lifelineStrategy");
		if ("nosolid".equalsIgnoreCase(strategy))
			return false;

		if ("solid".equalsIgnoreCase(strategy))
			return true;

		for (Event ev : drawableSet.getAllEvents())
			if (ev instanceof Delay)
				return true;

		return false;
	}

	private ParticipantRange getFullParticipantRange() {
		return new ParticipantRange(0, drawableSet.getAllParticipants().size());
	}

	private ParticipantRange getParticipantRange(Event ev) {
		return getFullParticipantRange();
	}

	// private int getParticipantRangeIndex(Participant participant) {
	// int r = 0;
	// for (Participant p : drawableSet.getAllParticipants()) {
	// r++;
	// if (p == participant) {
	// return r;
	// }
	// }
	// throw new IllegalArgumentException();
	// }

	public DrawableSet createDrawableSet(StringBounder stringBounder) {
		if (freeY2 != null)
			throw new IllegalStateException();

		this.defaultLineType = useContinueLineBecauseOfDelay() ? ComponentType.CONTINUE_LINE
				: ComponentType.PARTICIPANT_LINE;

		for (Participant p : drawableSet.getAllParticipants())
			prepareParticipant(stringBounder, p);

		this.freeY2 = new FrontierStackImpl(drawableSet.getHeadHeight(stringBounder),
				drawableSet.getAllParticipants().size());

		this.lastFreeY2 = this.freeY2;

		drawableSet.setTopStartingY(this.freeY2.getFreeY(getFullParticipantRange()));

		for (Participant p : drawableSet.getAllParticipants()) {
			final LivingParticipantBox living = drawableSet.getLivingParticipantBox(p);
			for (int i = 0; i < p.getInitialLife(); i++)
				living.getLifeLine().addSegmentVariation(LifeSegmentVariation.LARGER,
						freeY2.getFreeY(getFullParticipantRange()), p.getLiveSpecificBackColors(i));

		}

		final List<ParticipantBox> col = new ArrayList<>();
		for (LivingParticipantBox livingParticipantBox : drawableSet.getAllLivingParticipantBox())
			col.add(livingParticipantBox.getParticipantBox());

		constraintSet = new ConstraintSet(col, freeX);

		for (Event ev : new ArrayList<>(drawableSet.getAllEvents())) {
			final ParticipantRange range = getParticipantRange(ev);
			final double diffY = freeY2.getFreeY(range) - lastFreeY2.getFreeY(range);
			// final double diffY = freeY2.diff(lastFreeY2);

//			if (autonewpage > 0 && diffY > 0 && diffY + getTotalHeight(0, stringBounder) > autonewpage)
//				// We create a temporary Newpage object used internally by the
//				// getPreferredHeight() method,
//				// which is hardcoded in ComponentRoseNewpage. The title is irrelevant in this
//				// context.
//				prepareNewpageSpecial(stringBounder,
//						new Newpage(null, drawableSet.getSkinParam().getCurrentStyleBuilder()), ev, range);

			if (ev instanceof MessageExo)
				prepareMessageExo(stringBounder, (MessageExo) ev, range);
			else if (ev instanceof Message)
				prepareMessage(stringBounder, (Message) ev, range);
			else if (ev instanceof Note)
				prepareNote(stringBounder, (Note) ev, range);
			else if (ev instanceof Notes)
				prepareNotes(stringBounder, (Notes) ev, range);
			else if (ev instanceof LifeEvent)
				prepareLiveEvent(stringBounder, (LifeEvent) ev, range);
			else if (ev instanceof GroupingLeaf)
				prepareGroupingLeaf(stringBounder, (GroupingLeaf) ev, range);
			else if (ev instanceof GroupingStart)
				prepareGroupingStart(stringBounder, (GroupingStart) ev, range);
			else if (ev instanceof Newpage)
				prepareNewpage(stringBounder, (Newpage) ev, range);
			else if (ev instanceof Divider)
				prepareDivider(stringBounder, (Divider) ev, range);
			else if (ev instanceof HSpace)
				prepareHSpace(stringBounder, (HSpace) ev, range);
			else if (ev instanceof Delay)
				prepareDelay(stringBounder, (Delay) ev, col, range);
			else if (ev instanceof Reference)
				prepareReference(stringBounder, (Reference) ev, range);
			else
				throw new IllegalStateException();

		}

		takeParticipantEngloberPadding(stringBounder);
		constraintSet.takeConstraintIntoAccount(stringBounder);
		takeParticipantEngloberTitleWidth(stringBounder);

		prepareMissingSpace(stringBounder);

		drawableSet.setDimension(
				new XDimension2D(freeX, getTotalHeight(freeY2.getFreeY(getFullParticipantRange()), stringBounder)));
		return drawableSet;
	}

	private void takeParticipantEngloberPadding(StringBounder stringBounder) {
		final double padding = drawableSet.getSkinParam().getPadding(PaddingParam.BOX);
		if (padding == 0)
			return;

		for (Doll pe : drawableSet.getExistingParticipantEnglober(stringBounder)) {
			final ParticipantBox first = drawableSet.getLivingParticipantBox(pe.getFirst2TOBEPRIVATE())
					.getParticipantBox();
			final ParticipantBox last = drawableSet.getLivingParticipantBox(pe.getLast2TOBEPRIVATE())
					.getParticipantBox();
			constraintSet.pushToLeftParticipantBox(padding, first, true);
			constraintSet.pushToLeftParticipantBox(padding, last, false);
		}
	}

	private void takeParticipantEngloberTitleWidth(StringBounder stringBounder) {
		for (Doll doll : drawableSet.getExistingParticipantEnglober(stringBounder)) {
			final double preferredWidth = drawableSet.getEngloberPreferedWidth(stringBounder, doll);
			final ParticipantBox first = drawableSet.getLivingParticipantBox(doll.getFirst2TOBEPRIVATE())
					.getParticipantBox();
			final ParticipantBox last = drawableSet.getLivingParticipantBox(doll.getLast2TOBEPRIVATE())
					.getParticipantBox();
			final double x1 = drawableSet.getX1(doll);
			final double x2 = drawableSet.getX2(stringBounder, doll);
			final double missing = preferredWidth - (x2 - x1);
			if (missing > 0) {
				constraintSet.pushToLeftParticipantBox(missing / 2, first, true);
				constraintSet.pushToLeftParticipantBox(missing / 2, last, false);
			}
		}
	}

	private double getTotalHeight(double y, StringBounder stringBounder) {
		final double signature = 0;
		return y + drawableSet.getTailHeight(stringBounder, showTail) + signature;
	}

	public double getYposition(StringBounder stringBounder, Newpage newpage) {
		final GraphicalNewpage graphicalNewpage = (GraphicalNewpage) drawableSet
				.getEvent(Objects.requireNonNull(newpage));
		return graphicalNewpage.getStartingY();
	}

	private void prepareMissingSpace(StringBounder stringBounder) {
		freeX = constraintSet.getMaxX();

		double missingSpace1 = 0;
		double missingSpace2 = 0;

		for (GraphicalElement ev : drawableSet.getAllGraphicalElements()) {
			if (ev instanceof GraphicalDelayText) {
				final double missing = ev.getPreferredWidth(stringBounder) - freeX;
				if (missing > 0) {
					missingSpace1 = Math.max(missingSpace1, missing / 2);
					missingSpace2 = Math.max(missingSpace2, missing / 2);
				}
				continue;
			}
			final double startX = ev.getStartingX(stringBounder);
			final double delta1 = -startX;
			if (delta1 > missingSpace1)
				missingSpace1 = delta1;

			if (ev instanceof Arrow) {
				final Arrow a = (Arrow) ev;
				a.setMaxX(freeX);
			}
			double width = ev.getPreferredWidth(stringBounder);
			if (ev instanceof Arrow) {
				final Arrow a = (Arrow) ev;
				if (width < a.getActualWidth(stringBounder))
					width = a.getActualWidth(stringBounder);

			}
			if (ev instanceof GroupingGraphicalElementHeader) {
				final GroupingGraphicalElementHeader gh = (GroupingGraphicalElementHeader) ev;
				if (width < gh.getActualWidth(stringBounder))
					width = gh.getActualWidth(stringBounder);

			}
			final double endX = startX + width;
			final double delta2 = endX - freeX;
			if (delta2 > missingSpace2)
				missingSpace2 = delta2;
		}

		if (missingSpace1 > 0)
			constraintSet.pushToLeft(missingSpace1);

		freeX = constraintSet.getMaxX() + missingSpace2;
	}

	private void prepareNewpage(StringBounder stringBounder, Newpage newpage, ParticipantRange range) {
		final GraphicalNewpage graphicalNewpage = new GraphicalNewpage(freeY2.getFreeY(range),
				drawableSet.getSkin().createComponentNewPage(newpage.getUsedStyles(), drawableSet.getSkinParam()));
		this.lastFreeY2 = freeY2;
		freeY2 = freeY2.add(graphicalNewpage.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(newpage, graphicalNewpage);
	}

	private void prepareNewpageSpecial(StringBounder stringBounder, Newpage newpage, Event justBefore,
			ParticipantRange range) {
		final GraphicalNewpage graphicalNewpage = new GraphicalNewpage(freeY2.getFreeY(range),
				drawableSet.getSkin().createComponentNewPage(newpage.getUsedStyles(), drawableSet.getSkinParam()));
		this.lastFreeY2 = freeY2;
		freeY2 = freeY2.add(graphicalNewpage.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(newpage, graphicalNewpage, justBefore);
	}

	private void prepareDivider(StringBounder stringBounder, Divider divider, ParticipantRange range) {
		final GraphicalDivider graphicalDivider = new GraphicalDivider(freeY2.getFreeY(range),
				drawableSet.getSkin().createComponent(divider.getUsedStyles(), ComponentType.DIVIDER, null,
						drawableSet.getSkinParam(), divider.getText()));
		freeY2 = freeY2.add(graphicalDivider.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(divider, graphicalDivider);
	}

	private void prepareHSpace(StringBounder stringBounder, HSpace hspace, ParticipantRange range) {
		final GraphicalHSpace graphicalHSpace = new GraphicalHSpace(freeY2.getFreeY(range), hspace.getPixel());
		freeY2 = freeY2.add(graphicalHSpace.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(hspace, graphicalHSpace);
	}

	private void prepareDelay(StringBounder stringBounder, Delay delay, List<ParticipantBox> participants,
			ParticipantRange range) {
		final Component compText = drawableSet.getSkin().createComponent(delay.getUsedStyles(),
				ComponentType.DELAY_TEXT, null, drawableSet.getSkinParam(), delay.getText());
		final ParticipantBox first = participants.get(0);
		final ParticipantBox last = participants.get(participants.size() - 1);
		final GraphicalDelayText graphicalDivider = new GraphicalDelayText(freeY2.getFreeY(range), compText, first,
				last);
		for (ParticipantBox p : participants)
			p.addDelay(graphicalDivider);

		freeY2 = freeY2.add(graphicalDivider.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(delay, graphicalDivider);
	}

	final private InGroupablesStack inGroupableStack = new InGroupablesStack();

	private void prepareGroupingStart(StringBounder stringBounder, GroupingStart start, ParticipantRange range) {
		if (start.getType() != GroupingType.START)
			throw new IllegalStateException();

		final ISkinParam skinParam = new SkinParamBackcolored(drawableSet.getSkinParam(), start.getBackColorElement(),
				start.getBackColorGeneral());

		final Component comp = drawableSet.getSkin().createComponent(null, ComponentType.GROUPING_SPACE, null,
				skinParam, Display.create(start.getComment()));
		final double preferredHeight = comp.getPreferredHeight(stringBounder);
		freeY2 = freeY2.add(preferredHeight, range);

		final Display strings = start.getTitle().equals("group") ? Display.create(start.getComment())
				: Display.create(start.getTitle(), start.getComment());
		final Component header = drawableSet.getSkin().createComponent(start.getUsedStyles(),
				ComponentType.GROUPING_HEADER_LEGACY, null, skinParam, strings);
		final ParticipantBox veryfirst = drawableSet.getVeryfirst();
		final InGroupableList inGroupableList = new InGroupableList(veryfirst, start, freeY2.getFreeY(range));
		inGroupableStack.addList(inGroupableList);

		final GraphicalElement element = new GroupingGraphicalElementHeader(freeY2.getFreeY(range), header,
				inGroupableList, start.isParallel());
		inGroupableList.setMinWidth(element.getPreferredWidth(stringBounder));
		freeY2 = freeY2.add(element.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(start, element);
		if (start.isParallel())
			freeY2 = ((FrontierStack) freeY2).openBar();

	}

	private void prepareGroupingLeaf(StringBounder stringBounder, final GroupingLeaf m, ParticipantRange range) {
		final GraphicalElement element;
		final ISkinParam skinParam = new SkinParamBackcolored(drawableSet.getSkinParam(), null,
				m.getBackColorGeneral());
		if (m.getType() == GroupingType.ELSE) {
			if (m.isParallel())
				freeY2 = ((FrontierStack) freeY2).restore();

			final Component compElse = drawableSet.getSkin().createComponent(m.getUsedStyles(),
					ComponentType.GROUPING_ELSE_LEGACY, null, skinParam, Display.create(m.getComment()));
			final Lazy lazy = new Lazy() {
				public double getNow() {
					final GraphicalElement after = drawableSet.getEvent(m.getJustAfter());
					if (after == null)
						return 0;

					return after.getStartingY();
				}
			};
			element = new GroupingGraphicalElementElse(freeY2.getFreeY(range), compElse,
					inGroupableStack.getTopGroupingStructure(), m.isParallel(), lazy);
			final double preferredHeight = element.getPreferredHeight(stringBounder);
			freeY2 = freeY2.add(preferredHeight, range);
			// MODIF42
			inGroupableStack.addElement((GroupingGraphicalElementElse) element);
		} else if (m.getType() == GroupingType.END) {
			final List<Component> notes = new ArrayList<>();
			for (Note noteOnMessage : m.getNoteOnMessages()) {
				final ISkinParam sk = noteOnMessage.getSkinParamBackcolored(drawableSet.getSkinParam());
				final Component note = drawableSet.getSkin().createComponentNote(noteOnMessage.getUsedStyles(),
						noteOnMessage.getNoteStyle().getNoteComponentType(), sk, noteOnMessage.getDisplay(),
						noteOnMessage.getColors());
				notes.add(note);
			}
			if (m.isParallel())
				freeY2 = ((FrontierStack) freeY2).closeBar();

			final GroupingGraphicalElementHeader groupingHeaderStart = (GroupingGraphicalElementHeader) drawableSet
					.getEvent(m.getGroupingStart());
			if (groupingHeaderStart != null) {
				groupingHeaderStart.setEndY(freeY2.getFreeY(range));
				groupingHeaderStart.addNotes(stringBounder, notes);
			}
			element = new GroupingGraphicalElementTail(freeY2.getFreeY(range),
					inGroupableStack.getTopGroupingStructure(), m.isParallel());
			final Component comp = drawableSet.getSkin().createComponent(null, ComponentType.GROUPING_SPACE, null,
					skinParam, Display.create(m.getComment()));
			final double preferredHeight = comp.getPreferredHeight(stringBounder);
			freeY2 = freeY2.add(preferredHeight, range);
			inGroupableStack.pop();
		} else
			throw new IllegalStateException();

		drawableSet.addEvent(m, element);

	}

	private void prepareNote(StringBounder stringBounder, Note n, ParticipantRange range) {
		final NoteBox noteBox = createNoteBox(stringBounder, n, range);
		inGroupableStack.addElement(noteBox);

		drawableSet.addEvent(n, noteBox);
		freeY2 = freeY2.add(noteBox.getPreferredHeight(stringBounder), range);
	}

	private NoteBox createNoteBox(StringBounder stringBounder, Note n, ParticipantRange range) {
		LivingParticipantBox p1 = drawableSet.getLivingParticipantBox(n.getParticipant());
		LivingParticipantBox p2;
		if (n.getParticipant2() == null)
			p2 = null;
		else {
			p2 = drawableSet.getLivingParticipantBox(n.getParticipant2());
			if (p1.getParticipantBox().getCenterX(stringBounder) > p2.getParticipantBox().getCenterX(stringBounder)) {
				final LivingParticipantBox tmp = p1;
				p1 = p2;
				p2 = tmp;
			}
		}
		final ISkinParam skinParam = n.getSkinParamBackcolored(drawableSet.getSkinParam());
		final ComponentType type = n.getNoteStyle().getNoteComponentType();
		if (p1 == null && p2 == null)
			for (LivingParticipantBox p : drawableSet.getAllLivingParticipantBox()) {
				if (p1 == null)
					p1 = p;

				p2 = p;
			}

		final Component component = drawableSet.getSkin().createComponentNote(n.getUsedStyles(), type, skinParam,
				n.getDisplay(), n.getColors(), n.getPosition());
		final NoteBox noteBox = new NoteBox(freeY2.getFreeY(range), component, p1, p2, n.getPosition(), n.getUrl());
		return noteBox;
	}

	private void prepareNotes(StringBounder stringBounder, Notes notes, ParticipantRange range) {
		final NotesBoxes notesBoxes = new NotesBoxes(freeY2.getFreeY(range));
		for (Note n : notes) {
			final NoteBox noteBox = createNoteBox(stringBounder, n, range);
			final ParticipantBox p1 = drawableSet.getLivingParticipantBox(n.getParticipant()).getParticipantBox();
			final ParticipantBox p2 = n.getParticipant2() == null ? null
					: drawableSet.getLivingParticipantBox(n.getParticipant2()).getParticipantBox();
			notesBoxes.add(noteBox, p1, p2);
		}
		notesBoxes.ensureConstraints(stringBounder, constraintSet);
		inGroupableStack.addElement(notesBoxes);

		drawableSet.addEvent(notes, notesBoxes);
		freeY2 = freeY2.add(notesBoxes.getPreferredHeight(stringBounder), range);
	}

	private void prepareLiveEvent(StringBounder stringBounder, LifeEvent lifeEvent, ParticipantRange range) {
		final double y = freeY2.getFreeY(range);
		final AbstractMessage message = lifeEvent.getMessage();
		if (lifeEvent.getType() == LifeEventType.ACTIVATE) {
			double pos = 0;
			if (message != null) {
				int delta1 = 0;
				if (message.isCreate())
					delta1 += 10;
				else if (OptionFlags.STRICT_SELFMESSAGE_POSITION && message.isSelfMessage())
					delta1 += 8;

				pos = message.getPosYstartLevel() + delta1;
			}
			final LifeLine line1 = drawableSet.getLivingParticipantBox(lifeEvent.getParticipant()).getLifeLine();
			line1.addSegmentVariation(LifeSegmentVariation.LARGER, pos, lifeEvent.getSpecificColors());
		} else if (lifeEvent.getType() == LifeEventType.DESTROY || lifeEvent.getType() == LifeEventType.DEACTIVATE) {
			double delta = 0;
			if (OptionFlags.STRICT_SELFMESSAGE_POSITION && message != null && message.isSelfMessage())
				delta += 7;

			final Participant p = lifeEvent.getParticipant();
			final LifeLine line = drawableSet.getLivingParticipantBox(p).getLifeLine();
			double pos2 = y;
			if (message != null)
				pos2 = message.getPosYendLevel() - delta;

			line.addSegmentVariation(LifeSegmentVariation.SMALLER, pos2, lifeEvent.getSpecificColors());
		}

		if (lifeEvent.getType() == LifeEventType.DESTROY) {
			final Style[] style = lifeEvent.getUsedStyle();
			final Component comp = drawableSet.getSkin().createComponent(style, ComponentType.DESTROY, null,
					drawableSet.getSkinParam(), null);
			final double delta = comp.getPreferredHeight(stringBounder) / 2;
			final LivingParticipantBox livingParticipantBox = drawableSet
					.getLivingParticipantBox(lifeEvent.getParticipant());
			double pos2 = y;
			if (message == null) {
				pos2 = y;
				freeY2 = freeY2.add(comp.getPreferredHeight(stringBounder), range);
			} else
				pos2 = message.getPosYendLevel() - delta;

			final LifeDestroy destroy = new LifeDestroy(pos2, livingParticipantBox.getParticipantBox(), comp);
			drawableSet.addEvent(lifeEvent, destroy);
		} else
			drawableSet.addEvent(lifeEvent, new GraphicalElementLiveEvent(y));

	}

	private void prepareMessageExo(StringBounder stringBounder, MessageExo m, ParticipantRange range) {
		final Step1MessageExo step1Message = new Step1MessageExo(range, stringBounder, m, drawableSet, freeY2);
		freeY2 = step1Message.prepareMessage(constraintSet, inGroupableStack);
	}

	private void prepareMessage(StringBounder stringBounder, Message m, ParticipantRange range) {
		final Step1Message step1Message = new Step1Message(range, stringBounder, m, drawableSet, freeY2);
		freeY2 = step1Message.prepareMessage(constraintSet, inGroupableStack);
	}

	private void prepareReference(StringBounder stringBounder, Reference reference, ParticipantRange range) {
		final LivingParticipantBox p1 = drawableSet
				.getLivingParticipantBox(drawableSet.getFirst(reference.getParticipant()));
		final LivingParticipantBox p2 = drawableSet
				.getLivingParticipantBox(drawableSet.getLast(reference.getParticipant()));
		final ISkinParam skinParam = new SkinParamBackcoloredReference(drawableSet.getSkinParam(),
				reference.getBackColorElement(), reference.getBackColorGeneral());

		Display strings = Display.empty();
		strings = strings.add("ref");
		strings = strings.addAll(reference.getStrings());

		Component noteLeft = null;
		Component noteRight = null;
		for (Note noteOnMessage : reference.getNoteOnMessages()) {
			final ISkinParam skinParam2 = noteOnMessage.getSkinParamBackcolored(drawableSet.getSkinParam());
			final Component note = drawableSet.getSkin().createComponentNote(noteOnMessage.getUsedStyles(),
					noteOnMessage.getNoteStyle().getNoteComponentType(), skinParam2, noteOnMessage.getDisplay(),
					noteOnMessage.getColors());
			if (noteOnMessage.getPosition() == NotePosition.RIGHT)
				noteRight = note;
			else
				noteLeft = note;
		}

		final Component comp = drawableSet.getSkin().createComponent(reference.getUsedStyles(), ComponentType.REFERENCE,
				null, skinParam, strings);
		final GraphicalReference graphicalReference = new GraphicalReference(freeY2.getFreeY(range), comp, p1, p2,
				reference.getUrl(), noteLeft, noteRight);

		final ParticipantBox pbox1 = p1.getParticipantBox();
		final ParticipantBox pbox2 = p2.getParticipantBox();
		final double width = graphicalReference.getPreferredWidth(stringBounder)
				- pbox1.getPreferredWidth(stringBounder) / 2 - pbox2.getPreferredWidth(stringBounder) / 2;

		final Constraint constraint;
		if (p1 == p2)
			constraint = constraintSet.getConstraintAfter(pbox1);
		else
			constraint = constraintSet.getConstraint(pbox1, pbox2);

		constraint.ensureValue(width);

		inGroupableStack.addElement(graphicalReference);
		inGroupableStack.addElement(p1);
		if (p1 != p2)
			inGroupableStack.addElement(p2);

		freeY2 = freeY2.add(graphicalReference.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(reference, graphicalReference);
	}

	private void prepareParticipant(StringBounder stringBounder, Participant p) {
		final ComponentType headType;
		final ComponentType tailType;
		if (p.getType() == ParticipantType.PARTICIPANT) {
			headType = ComponentType.PARTICIPANT_HEAD;
			tailType = ComponentType.PARTICIPANT_TAIL;
		} else if (p.getType() == ParticipantType.ACTOR) {
			headType = ComponentType.ACTOR_HEAD;
			tailType = ComponentType.ACTOR_TAIL;
		} else if (p.getType() == ParticipantType.BOUNDARY) {
			headType = ComponentType.BOUNDARY_HEAD;
			tailType = ComponentType.BOUNDARY_TAIL;
		} else if (p.getType() == ParticipantType.CONTROL) {
			headType = ComponentType.CONTROL_HEAD;
			tailType = ComponentType.CONTROL_TAIL;
		} else if (p.getType() == ParticipantType.ENTITY) {
			headType = ComponentType.ENTITY_HEAD;
			tailType = ComponentType.ENTITY_TAIL;
		} else if (p.getType() == ParticipantType.QUEUE) {
			headType = ComponentType.QUEUE_HEAD;
			tailType = ComponentType.QUEUE_TAIL;
		} else if (p.getType() == ParticipantType.DATABASE) {
			headType = ComponentType.DATABASE_HEAD;
			tailType = ComponentType.DATABASE_TAIL;
		} else if (p.getType() == ParticipantType.COLLECTIONS) {
			headType = ComponentType.COLLECTIONS_HEAD;
			tailType = ComponentType.COLLECTIONS_TAIL;
		} else {
			throw new IllegalArgumentException();
		}

		final ISkinParam skinParam = drawableSet.getSkinParam();
		final Display participantDisplay = p.getDisplay(skinParam.forceSequenceParticipantUnderlined());
		final Component head = drawableSet.getSkin().createComponentParticipant(p, headType, null, skinParam,
				participantDisplay);
		final Component tail = drawableSet.getSkin().createComponentParticipant(p, tailType, null, skinParam,
				participantDisplay);
		final Style style = this.defaultLineType.getStyleSignature().withTOBECHANGED(p.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final Component line = drawableSet.getSkin().createComponent(new Style[] { style }, this.defaultLineType, null,
				drawableSet.getSkinParam(), participantDisplay);
		final Component delayLine = drawableSet.getSkin().createComponent(new Style[] { style },
				ComponentType.DELAY_LINE, null, drawableSet.getSkinParam(), participantDisplay);
		final ParticipantBox box = new ParticipantBox(skinParam.getPragma(), p.getLocation(), head, line, tail,
				delayLine, this.freeX, skinParam.maxAsciiMessageLength() > 0 ? 1 : 5, p);

		final Component comp = drawableSet.getSkin().createComponent(
				new Style[] { ComponentType.ALIVE_BOX_CLOSE_CLOSE.getStyleSignature()
						.getMergedStyle(drawableSet.getSkinParam().getCurrentStyleBuilder()) },
				ComponentType.ALIVE_BOX_CLOSE_CLOSE, null, drawableSet.getSkinParam(), participantDisplay);

		final LifeLine lifeLine = new LifeLine(box, comp.getPreferredWidth(stringBounder),
				drawableSet.getSkinParam().shadowing(p.getStereotype()), participantDisplay);
		drawableSet.setLivingParticipantBox(p, new LivingParticipantBox(box, lifeLine));

		this.freeX = box.getMaxX(stringBounder);
	}

	public void addParticipant(Participant p, ParticipantEnglober participantEnglober) {
		drawableSet.addParticipant(p, participantEnglober);
	}

	public void addEvent(Event event) {
		drawableSet.addEvent(event, null);
	}

}
