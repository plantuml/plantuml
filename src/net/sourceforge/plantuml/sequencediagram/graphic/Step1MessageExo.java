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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ComponentType;

class Step1MessageExo extends Step1Abstract {

	private final MessageExoArrow messageArrow;

	Step1MessageExo(StringBounder stringBounder, MessageExo message, DrawableSet drawingSet, double freeY) {
		super(stringBounder, message, drawingSet, freeY);

		setType(getArrowType(message));

		this.messageArrow = new MessageExoArrow(freeY, drawingSet.getSkin(), drawingSet.getSkin().createComponent(
				getType(), drawingSet.getSkinParam(), getLabelOfMessage(message)), getLivingParticipantBox(), message
				.getType());

		if (message.getNote() != null) {
			final ISkinParam skinParam = new SkinParamBackcolored(drawingSet.getSkinParam(), message
					.getSpecificBackColor());
			setNote(drawingSet.getSkin().createComponent(ComponentType.NOTE, drawingSet.getSkinParam(),
					message.getNote()));
			// throw new UnsupportedOperationException();
		}

	}

	double prepareMessage(ConstraintSet constraintSet, Collection<InGroupableList> groupingStructures) {
		final Arrow graphic = createArrow();
		final double arrowYStartLevel = graphic.getArrowYStartLevel(getStringBounder());
		final double arrowYEndLevel = graphic.getArrowYEndLevel(getStringBounder());

		for (LifeEvent lifeEvent : getMessage().getLiveEvents()) {
			beforeMessage(lifeEvent, arrowYStartLevel);
		}

		final double length = graphic.getArrowOnlyWidth(getStringBounder());
		incFreeY(graphic.getPreferredHeight(getStringBounder()));
		double marginActivateAndDeactive = 0;
		if (getMessage().isActivateAndDeactive()) {
			marginActivateAndDeactive = 30;
			incFreeY(marginActivateAndDeactive);
		}
		getDrawingSet().addEvent(getMessage(), graphic);

		final LivingParticipantBox livingParticipantBox = getLivingParticipantBox();
		if (messageArrow.getType().isRightBorder()) {
			constraintSet.getConstraint(livingParticipantBox.getParticipantBox(), constraintSet.getLastborder())
					.ensureValue(length);
		} else {
			constraintSet.getConstraint(constraintSet.getFirstBorder(), livingParticipantBox.getParticipantBox())
					.ensureValue(length);
		}

		for (LifeEvent lifeEvent : getMessage().getLiveEvents()) {
			afterMessage(getStringBounder(), lifeEvent, arrowYEndLevel + marginActivateAndDeactive);
		}

		if (groupingStructures != null && graphic instanceof InGroupable) {
			for (InGroupableList groupingStructure : groupingStructures) {
				groupingStructure.addInGroupable((InGroupable) graphic);
				groupingStructure.addInGroupable(livingParticipantBox);
				groupingStructure.addInGroupable(livingParticipantBox);
			}
		}

		return getFreeY();
	}

	private LivingParticipantBox getLivingParticipantBox() {
		return getDrawingSet().getLivingParticipantBox(((MessageExo) getMessage()).getParticipant());
	}

	private List<? extends CharSequence> getLabelOfMessage(MessageExo message) {
		if (message.getMessageNumber() == null) {
			return message.getLabel();
		}
		final List<CharSequence> result = new ArrayList<CharSequence>();
		result.add(new MessageNumber(message.getMessageNumber()));
		result.addAll(message.getLabel());
		return result;
	}

	private Arrow createArrow() {
		if (getMessage().getNote() == null) {
			return messageArrow;
		}
		final NoteBox toto = createNoteBox(getStringBounder(), messageArrow, getNote(), getMessage().getNotePosition(),
				getMessage().getUrlNote());
		return new ArrowAndNoteBox(getStringBounder(), messageArrow, toto);
	}

	private ComponentType getArrowType(MessageExo m) {
		ComponentType result = null;
		final MessageExoType type = m.getType();
		if (type.getDirection() == 1) {
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
