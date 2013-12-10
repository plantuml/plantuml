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
 * Revision $Revision: 11635 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;

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
			final Component comp = drawingSet.getSkin().createComponent(ComponentType.ARROW, getConfig(),
					drawingSet.getSkinParam(), getLabelOfMessage(message));
			final Component compAliveBox = drawingSet.getSkin().createComponent(ComponentType.ALIVE_BOX_OPEN_OPEN,
					null, drawingSet.getSkinParam(), null);

			this.messageArrow = new MessageArrow(freeY.getFreeY(range), drawingSet.getSkin(), comp,
					getLivingParticipantBox1(), getLivingParticipantBox2(), message.getUrl(), compAliveBox);
		}

		if (message.getNote() != null) {
			final ISkinParam skinParam = new SkinParamBackcolored(drawingSet.getSkinParam(),
					message.getSpecificBackColor());
			setNote(drawingSet.getSkin().createComponent(ComponentType.NOTE, null, skinParam, message.getNote()));
		}

	}

	Frontier prepareMessage(ConstraintSet constraintSet, InGroupablesStack inGroupablesStack) {
		final Arrow graphic = createArrow();
		final double arrowYStartLevel = graphic.getArrowYStartLevel(getStringBounder());
		final double arrowYEndLevel = graphic.getArrowYEndLevel(getStringBounder());

		// final double delta1 = isSelfMessage() ? 4 : 0;
		final double delta1 = 0;

		for (LifeEvent lifeEvent : getMessage().getLiveEvents()) {
			beforeMessage(lifeEvent, arrowYStartLevel + delta1);
		}

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

		for (LifeEvent lifeEvent : getMessage().getLiveEvents()) {
			afterMessage(getStringBounder(), lifeEvent, arrowYEndLevel + marginActivateAndDeactive - delta1);
		}

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
		if (getMessage().getNote() != null && isSelfMessage()) {
			final MessageSelfArrow messageSelfArrow = createMessageSelfArrow();
			final NoteBox noteBox = createNoteBox(getStringBounder(), messageSelfArrow, getNote(), getMessage()
					.getNotePosition(), getMessage().getUrlNote());
			return new ArrowAndNoteBox(getStringBounder(), messageSelfArrow, noteBox);
		} else if (getMessage().getNote() != null) {
			final NoteBox noteBox = createNoteBox(getStringBounder(), messageArrow, getNote(), getMessage()
					.getNotePosition(), getMessage().getUrlNote());
			return new ArrowAndNoteBox(getStringBounder(), messageArrow, noteBox);
		} else if (isSelfMessage()) {
			return createMessageSelfArrow();
		} else {
			return messageArrow;
		}
	}

	private MessageSelfArrow createMessageSelfArrow() {
		final double posY = getFreeY().getFreeY(getParticipantRange());
		double deltaY = 0;
		if (getMessage().isActivate()) {
			deltaY -= getHalfLifeWidth();
		}
		if (getMessage().isDeactivate()) {
			deltaY += getHalfLifeWidth();
		}

		return new MessageSelfArrow(posY, getDrawingSet().getSkin(), getDrawingSet().getSkin().createComponent(
				ComponentType.ARROW, getConfig(), getDrawingSet().getSkinParam(), getLabelOfMessage(getMessage())),
				getLivingParticipantBox1(), deltaY, getMessage().getUrl());
	}

	private double getHalfLifeWidth() {
		return getDrawingSet()
				.getSkin()
				.createComponent(ComponentType.ALIVE_BOX_OPEN_OPEN, null, getDrawingSet().getSkinParam(),
						Display.asList("")).getPreferredWidth(null) / 2;
	}

	private Arrow createArrowCreate() {
		if (messageArrow == null) {
			throw new IllegalStateException();
		}
		Arrow result = new ArrowAndParticipant(getStringBounder(), messageArrow, getParticipantBox2());
		if (getMessage().getNote() != null) {
			final NoteBox noteBox = createNoteBox(getStringBounder(), result, getNote(),
					getMessage().getNotePosition(), getMessage().getUrlNote());
			if (getMessage().getNotePosition() == NotePosition.RIGHT) {
				noteBox.pushToRight(getParticipantBox2().getPreferredWidth(getStringBounder()) / 2);
			}
			result = new ArrowAndNoteBox(getStringBounder(), result, noteBox);
		}
		getLivingParticipantBox2().create(
				getFreeY().getFreeY(getParticipantRange()) + result.getPreferredHeight(getStringBounder()) / 2);
		return result;
	}

	private ArrowConfiguration getSelfArrowType(Message m) {
		// return m.getArrowConfiguration().self();
		ArrowConfiguration result = ArrowConfiguration.withDirectionSelf();
		if (m.getArrowConfiguration().isDotted()) {
			result = result.withDotted();
		}
		if (m.getArrowConfiguration().isAsync()) {
			result = result.withHead(ArrowHead.ASYNC);
		}
		result = result.withPart(m.getArrowConfiguration().getPart());
		result = result.withColor(m.getArrowConfiguration().getColor());
		return result;
	}

	private ArrowConfiguration getArrowType(Message m, final double x1, final double x2) {
		if (x2 > x1) {
			return m.getArrowConfiguration();
		}
		return m.getArrowConfiguration().reverse();
	}

}
