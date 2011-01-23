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
 * Revision $Revision: 6026 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.Collection;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ComponentType;

class Step1Message extends Step1Abstract {

	private final MessageArrow messageArrow;

	Step1Message(StringBounder stringBounder, Message message, DrawableSet drawingSet, double freeY) {
		super(stringBounder, message, drawingSet, freeY);

		final double x1 = getParticipantBox1().getCenterX(stringBounder);
		final double x2 = getParticipantBox2().getCenterX(stringBounder);

		this.setType(isSelfMessage() ? getSelfArrowType(message) : getArrowType(message, x1, x2));

		if (isSelfMessage()) {
			this.messageArrow = null;
		} else {
			this.messageArrow = new MessageArrow(freeY, drawingSet.getSkin(), drawingSet.getSkin().createComponent(
					getType(), drawingSet.getSkinParam(), getLabelOfMessage(message)), getLivingParticipantBox1(),
					getLivingParticipantBox2());
		}

		if (message.getNote() != null) {
			final ISkinParam skinParam = new SkinParamBackcolored(drawingSet.getSkinParam(), message
					.getSpecificBackColor());
			setNote(drawingSet.getSkin().createComponent(ComponentType.NOTE, drawingSet.getSkinParam(),
					message.getNote()));
		}

	}

	double prepareMessage(ConstraintSet constraintSet, Collection<InGroupableList> groupingStructures) {
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
					+ getLivingParticipantBox1().getLiveThicknessAt(getStringBounder(), arrowYStartLevel)
							.getSegment().getLength();
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

		if (groupingStructures != null && graphic instanceof InGroupable) {
			for (InGroupableList groupingStructure : groupingStructures) {
				groupingStructure.addInGroupable((InGroupable) graphic);
				groupingStructure.addInGroupable(getLivingParticipantBox1());
				if (isSelfMessage() == false) {
					groupingStructure.addInGroupable(getLivingParticipantBox2());
				}
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
		final MessageSelfArrow messageSelfArrow = new MessageSelfArrow(getFreeY(), getDrawingSet().getSkin(),
				getDrawingSet().getSkin().createComponent(getType(), getDrawingSet().getSkinParam(),
						getLabelOfMessage(getMessage())), getLivingParticipantBox1());
		if (getMessage().getNote() != null && isSelfMessage()) {
			final NoteBox noteBox = createNoteBox(getStringBounder(), messageSelfArrow, getNote(), getMessage()
					.getNotePosition());
			return new ArrowAndNoteBox(getStringBounder(), messageSelfArrow, noteBox);
		} else if (getMessage().getNote() != null) {
			final NoteBox noteBox = createNoteBox(getStringBounder(), messageArrow, getNote(), getMessage()
					.getNotePosition());
			return new ArrowAndNoteBox(getStringBounder(), messageArrow, noteBox);
		} else if (isSelfMessage()) {
			return messageSelfArrow;
		} else {
			return messageArrow;
		}
	}

	private Arrow createArrowCreate() {
		getLivingParticipantBox2().create(getFreeY());
		if (getMessage().getNote() != null) {
			final ArrowAndParticipant arrowAndParticipant = new ArrowAndParticipant(getStringBounder(), messageArrow,
					getParticipantBox2());
			final NoteBox noteBox = createNoteBox(getStringBounder(), arrowAndParticipant, getNote(), getMessage()
					.getNotePosition());
			if (getMessage().getNotePosition() == NotePosition.RIGHT) {
				noteBox.pushToRight(getParticipantBox2().getPreferredWidth(getStringBounder()) / 2);
			}
			return new ArrowAndNoteBox(getStringBounder(), arrowAndParticipant, noteBox);
		}
		return new ArrowAndParticipant(getStringBounder(), messageArrow, getParticipantBox2());
	}

	private ComponentType getSelfArrowType(Message m) {
		ComponentType result = m.getArrowConfiguration().isDotted() ? ComponentType.getArrow(ArrowDirection.SELF)
				.withDotted() : ComponentType.getArrow(ArrowDirection.SELF);
		if (m.getArrowConfiguration().isDotted()) {
			result = result.withDotted();
		}
		if (m.getArrowConfiguration().isASync()) {
			result = result.withAsync();
		}
		result = result.withPart(m.getArrowConfiguration().getPart());
		return result;
	}

	private ComponentType getArrowType(Message m, final double x1, final double x2) {
		ComponentType result = null;
		if (x2 > x1) {
			result = ComponentType.getArrow(ArrowDirection.LEFT_TO_RIGHT_NORMAL);
		} else {
			result = ComponentType.getArrow(ArrowDirection.RIGHT_TO_LEFT_REVERSE);
		}
		if (m.getArrowConfiguration().isDotted()) {
			result = result.withDotted();
		}
		if (m.getArrowConfiguration().isASync()) {
			result = result.withAsync();
		}
		result = result.withPart(m.getArrowConfiguration().getPart());
		return result;
	}

}
