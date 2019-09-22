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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.PaddingParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.ArrowBody;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.style.Style;

class Step1Message extends Step1Abstract {

	private final MessageArrow messageArrow;

	Step1Message(ParticipantRange range, StringBounder stringBounder, Message message, DrawableSet drawingSet,
			Frontier freeY) {
		super(range, stringBounder, message, drawingSet, freeY);

		final double x1 = getParticipantBox1().getCenterX(stringBounder);
		final double x2 = getParticipantBox2().getCenterX(stringBounder);

		this.setConfig(isSelfMessage() ? getSelfArrowType(message) : getArrowType(message, x1, x2));

		if (isSelfMessage()) {
			this.messageArrow = null;
		} else {
			final ArrowComponent comp = drawingSet.getSkin().createComponentArrow(message.getUsedStyles(), getConfig(),
					drawingSet.getSkinParam(), message.getLabelNumbered());
			final Component compAliveBox = drawingSet.getSkin().createComponent(
					new Style[] { ComponentType.ALIVE_BOX_OPEN_OPEN.getDefaultStyleDefinition().getMergedStyle(
							drawingSet.getSkinParam().getCurrentStyleBuilder()) }, ComponentType.ALIVE_BOX_OPEN_OPEN,
					null, drawingSet.getSkinParam(), null);

			this.messageArrow = new MessageArrow(freeY.getFreeY(range), drawingSet.getSkin(), comp,
					getLivingParticipantBox1(), getLivingParticipantBox2(), message.getUrl(), compAliveBox);
		}

		final List<Note> noteOnMessages = message.getNoteOnMessages();
		for (Note noteOnMessage : noteOnMessages) {
			final ISkinParam skinParam = noteOnMessage.getSkinParamBackcolored(drawingSet.getSkinParam());
			addNote(drawingSet.getSkin().createComponent(noteOnMessage.getUsedStyles(),
					noteOnMessage.getNoteStyle().getNoteComponentType(), null, skinParam, noteOnMessage.getStrings()));
		}

	}

	Frontier prepareMessage(ConstraintSet constraintSet, InGroupablesStack inGroupablesStack) {
		final Arrow graphic = createArrow();
		final double arrowYStartLevel = graphic.getArrowYStartLevel(getStringBounder());
		final double arrowYEndLevel = graphic.getArrowYEndLevel(getStringBounder());

		// final double delta1 = isSelfMessage() ? 4 : 0;
		final double delta1 = 0;
		getMessage().setPosYstartLevel(arrowYStartLevel + delta1);

		final double length;
		if (isSelfMessage()) {
			length = graphic.getArrowOnlyWidth(getStringBounder())
					+ getLivingParticipantBox1().getLiveThicknessAt(getStringBounder(), arrowYStartLevel).getSegment()
							.getLength();
		} else {
			length = graphic.getArrowOnlyWidth(getStringBounder())
					+ getLivingParticipantBox(NotePosition.LEFT).getLifeLine().getRightShift(arrowYStartLevel)
					+ getLivingParticipantBox(NotePosition.RIGHT).getLifeLine().getLeftShift(arrowYStartLevel);
		}

		incFreeY(graphic.getPreferredHeight(getStringBounder()));
		double marginActivateAndDeactive = 0;
		if (getMessage().isActivateAndDeactive()) {
			marginActivateAndDeactive = 30;
			incFreeY(marginActivateAndDeactive);
		}
		getDrawingSet().addEvent(getMessage(), graphic);

		if (isSelfMessage()) {
			constraintSet.getConstraintAfter(getParticipantBox1()).ensureValue(length);
		} else {
			constraintSet.getConstraint(getParticipantBox1(), getParticipantBox2()).ensureValue(length);
		}

		final double posYendLevel = arrowYEndLevel + marginActivateAndDeactive - delta1;
		getMessage().setPosYendLevel(posYendLevel);

		assert graphic instanceof InGroupable;
		if (graphic instanceof InGroupable) {
			inGroupablesStack.addElement((InGroupable) graphic);
			inGroupablesStack.addElement(getLivingParticipantBox1());
			if (isSelfMessage() == false) {
				inGroupablesStack.addElement(getLivingParticipantBox2());
			}
		}

		return getFreeY();
	}

	private boolean isSelfMessage() {
		return getParticipantBox1().equals(getParticipantBox2());
	}

	private ParticipantBox getParticipantBox1() {
		return getLivingParticipantBox1().getParticipantBox();
	}

	private ParticipantBox getParticipantBox2() {
		return getLivingParticipantBox2().getParticipantBox();
	}

	private LivingParticipantBox getLivingParticipantBox1() {
		return getDrawingSet().getLivingParticipantBox(((Message) getMessage()).getParticipant1());
	}

	private LivingParticipantBox getLivingParticipantBox2() {
		return getDrawingSet().getLivingParticipantBox(((Message) getMessage()).getParticipant2());
	}

	private LivingParticipantBox getLivingParticipantBox(NotePosition position) {
		if (isSelfMessage()) {
			throw new IllegalStateException();
		}
		return messageArrow.getParticipantAt(getStringBounder(), position);
	}

	private Arrow createArrow() {
		if (getMessage().isCreate()) {
			return createArrowCreate();
		}
		if (getMessage().getNoteOnMessages().size() > 0 && isSelfMessage()) {
			final MessageSelfArrow messageSelfArrow = createMessageSelfArrow();
			final List<NoteBox> noteBoxes = new ArrayList<NoteBox>();
			for (int i = 0; i < getNotes().size(); i++) {
				final Component note = getNotes().get(i);
				final Note noteOnMessage = getMessage().getNoteOnMessages().get(i);
				noteOnMessage.temporaryProtectedUntilTeozIsStandard();
				noteBoxes.add(createNoteBox(getStringBounder(), messageSelfArrow, note, noteOnMessage));
			}
			return new ArrowAndNoteBox(getStringBounder(), messageSelfArrow, noteBoxes);
		} else if (getMessage().getNoteOnMessages().size() > 0) {
			final List<NoteBox> noteBoxes = new ArrayList<NoteBox>();
			for (int i = 0; i < getNotes().size(); i++) {
				final Component note = getNotes().get(i);
				final Note noteOnMessage = getMessage().getNoteOnMessages().get(i);
				noteOnMessage.temporaryProtectedUntilTeozIsStandard();
				noteBoxes.add(createNoteBox(getStringBounder(), messageArrow, note, noteOnMessage));
			}
			return new ArrowAndNoteBox(getStringBounder(), messageArrow, noteBoxes);
		} else if (isSelfMessage()) {
			return createMessageSelfArrow();
		} else {
			return messageArrow;
		}
	}

	private MessageSelfArrow createMessageSelfArrow() {
		final double posY = getFreeY().getFreeY(getParticipantRange());
		double deltaY = 0;
		double deltaX = 0;
		if (getMessage().isActivate()) {
			deltaY -= getHalfLifeWidth();
			if (OptionFlags.STRICT_SELFMESSAGE_POSITION) {
				deltaX += 5;
			}
		}
		if (getMessage().isDeactivate()) {
			deltaY += getHalfLifeWidth();
		}

		final Style[] styles = getMessage().getUsedStyles();
		final ArrowComponent comp = getDrawingSet().getSkin().createComponentArrow(styles, getConfig(),
				getDrawingSet().getSkinParam(), getMessage().getLabelNumbered());
		return new MessageSelfArrow(posY, getDrawingSet().getSkin(), comp, getLivingParticipantBox1(), deltaY,
				getMessage().getUrl(), deltaX);
	}

	private double getHalfLifeWidth() {
		return getDrawingSet()
				.getSkin()
				.createComponent(
						new Style[] { ComponentType.ALIVE_BOX_OPEN_OPEN.getDefaultStyleDefinition().getMergedStyle(
								getDrawingSet().getSkinParam().getCurrentStyleBuilder()) },
						ComponentType.ALIVE_BOX_OPEN_OPEN, null, getDrawingSet().getSkinParam(), Display.create(""))
				.getPreferredWidth(null) / 2;
	}

	private Arrow createArrowCreate() {
		if (messageArrow == null) {
			throw new IllegalStateException();
		}
		Arrow result = new ArrowAndParticipant(getStringBounder(), messageArrow, getParticipantBox2(), getDrawingSet()
				.getSkinParam().getPadding(PaddingParam.PARTICIPANT));
		if (getMessage().getNoteOnMessages().size() > 0) {
			final List<NoteBox> noteBoxes = new ArrayList<NoteBox>();
			for (int i = 0; i < getNotes().size(); i++) {
				final Component note = getNotes().get(i);
				final Note noteOnMessage = getMessage().getNoteOnMessages().get(i);
				final NoteBox noteBox = createNoteBox(getStringBounder(), result, note, noteOnMessage);
				if (noteOnMessage.getPosition() == NotePosition.RIGHT) {
					noteBox.pushToRight(getParticipantBox2().getPreferredWidth(getStringBounder()) / 2);
				}
				noteBoxes.add(noteBox);
			}
			result = new ArrowAndNoteBox(getStringBounder(), result, noteBoxes);
		}
		getLivingParticipantBox2().create(
				getFreeY().getFreeY(getParticipantRange()) + result.getPreferredHeight(getStringBounder()) / 2);
		return result;
	}

	private ArrowConfiguration getSelfArrowType(Message m) {
		// return m.getArrowConfiguration().self();
		ArrowConfiguration result = ArrowConfiguration.withDirectionSelf();
		if (m.getArrowConfiguration().isDotted()) {
			result = result.withBody(ArrowBody.DOTTED);
		}
		if (m.getArrowConfiguration().isHidden()) {
			result = result.withBody(ArrowBody.HIDDEN);
		}
		if (m.getArrowConfiguration().isAsync()) {
			result = result.withHead(ArrowHead.ASYNC);
		}
		if (m.getArrowConfiguration().getDressing2().getHead() == ArrowHead.CROSSX) {
			result = result.withHead2(m.getArrowConfiguration().getDressing2().getHead());
			// System.err.println("WARNING : CROSSX");
			// Thread.dumpStack();
			// assert false;
		}
		result = result.withPart(m.getArrowConfiguration().getPart());
		result = result.withColor(m.getArrowConfiguration().getColor());
		result = result.withDecoration1(m.getArrowConfiguration().getDecoration1());
		result = result.withDecoration2(m.getArrowConfiguration().getDecoration2());

		return result;
	}

	private ArrowConfiguration getArrowType(Message m, final double x1, final double x2) {
		if (x2 > x1) {
			return m.getArrowConfiguration();
		}
		return m.getArrowConfiguration().reverse();
	}

}
