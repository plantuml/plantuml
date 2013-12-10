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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ComponentType;

class Step1MessageExo extends Step1Abstract {

	private final MessageExoArrow messageArrow;

	Step1MessageExo(ParticipantRange range, StringBounder stringBounder, MessageExo message, DrawableSet drawingSet,
			Frontier freeY) {
		super(range, stringBounder, message, drawingSet, freeY);

		setConfig(getArrowType(message));

		this.messageArrow = new MessageExoArrow(freeY.getFreeY(range), drawingSet.getSkin(), drawingSet.getSkin()
				.createComponent(ComponentType.ARROW, getConfig(), drawingSet.getSkinParam(),
						getLabelOfMessage(message)), getLivingParticipantBox(), message.getType(), message.getUrl(),
				message.isShortArrow());

		if (message.getNote() != null) {
			final ISkinParam skinParam = new SkinParamBackcolored(drawingSet.getSkinParam(),
					message.getSpecificBackColor());
			setNote(drawingSet.getSkin().createComponent(ComponentType.NOTE, null, skinParam, message.getNote()));
			// throw new UnsupportedOperationException();
		}

	}

	Frontier prepareMessage(ConstraintSet constraintSet, InGroupablesStack inGroupablesStack) {
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

		assert graphic instanceof InGroupable;
		if (graphic instanceof InGroupable) {
			inGroupablesStack.addElement((InGroupable) graphic);
			inGroupablesStack.addElement(livingParticipantBox);
		}

		return getFreeY();
	}

	private LivingParticipantBox getLivingParticipantBox() {
		return getDrawingSet().getLivingParticipantBox(((MessageExo) getMessage()).getParticipant());
	}

	private Display getLabelOfMessage(MessageExo message) {
		if (message.getMessageNumber() == null) {
			return message.getLabel();
		}
		Display result = new Display();
		result = result.add(new MessageNumber(message.getMessageNumber()));
		result = result.addAll(message.getLabel());
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

	private ArrowConfiguration getArrowType(MessageExo m) {
		final MessageExoType type = m.getType();
		if (type.getDirection() == 1) {
			return m.getArrowConfiguration();
		}
		return m.getArrowConfiguration().reverse();
		// ArrowConfiguration result = null;
		// if (type.getDirection() == 1) {
		// result = ArrowConfiguration.withDirectionNormal();
		// } else {
		// result = ArrowConfiguration.withDirectionReverse();
		// }
		// if (m.getArrowConfiguration().isDotted()) {
		// result = result.withDotted();
		// }
		// if (m.getArrowConfiguration().isAsync()) {
		// result = result.withHead(ArrowHead.ASYNC);
		// }
		// result = result.withPart(m.getArrowConfiguration().getPart());
		// return result;
	}

}
