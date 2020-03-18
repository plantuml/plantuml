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

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class ArrowAndParticipant extends Arrow implements InGroupable {

	private final Arrow arrow;
	private final ParticipantBox participantBox;
	private final double paddingParticipant;

	public ArrowAndParticipant(StringBounder stringBounder, Arrow arrow, ParticipantBox participantBox,
			double paddingParticipant) {
		super(arrow.getStartingY(), arrow.getSkin(), arrow.getArrowComponent(), arrow.getUrl());
		this.arrow = arrow;
		this.participantBox = participantBox;
		this.paddingParticipant = paddingParticipant;
		arrow.setPaddingArrowHead(participantBox.getPreferredWidth(stringBounder) / 2 - paddingParticipant);
	}

	@Override
	public void setMaxX(double m) {
		super.setMaxX(m);
		arrow.setMaxX(m);
	}

	@Override
	final public double getArrowOnlyWidth(StringBounder stringBounder) {
		return arrow.getPreferredWidth(stringBounder) + participantBox.getPreferredWidth(stringBounder) / 2;
	}

	@Override
	public double getArrowYEndLevel(StringBounder stringBounder) {
		return arrow.getArrowYEndLevel(stringBounder);
	}

	@Override
	public double getArrowYStartLevel(StringBounder stringBounder) {
		return arrow.getArrowYStartLevel(stringBounder);
	}

	@Override
	public int getDirection(StringBounder stringBounder) {
		return arrow.getDirection(stringBounder);
	}

	@Override
	public LivingParticipantBox getParticipantAt(StringBounder stringBounder, NotePosition position) {
		return arrow.getParticipantAt(stringBounder, position);
	}

	@Override
	protected void drawInternalU(final UGraphic ug, double maxX, Context2D context) {
		final double participantBoxStartingX = participantBox.getStartingX();
		final double arrowStartingX = arrow.getStartingX(ug.getStringBounder());

		if (arrowStartingX < participantBoxStartingX) {
			arrow.drawInternalU(ug, maxX, context);
		} else {
			final double boxWidth = participantBox.getPreferredWidth(ug.getStringBounder());
			arrow.drawInternalU(ug.apply(UTranslate.dx(boxWidth / 2 - paddingParticipant)), maxX, context);
		}

		final double arrowHeight = arrow.getPreferredHeight(ug.getStringBounder());
		final double boxHeight = participantBox.getHeadHeight(ug.getStringBounder());
		// final double diff = getDiff(ug);
		double diff = 0;
		if (arrowHeight > boxHeight) {
			diff = arrowHeight - boxHeight;
		}
		if (context.isBackground() == false) {
			participantBox
					.drawParticipantHead(ug.apply(new UTranslate(participantBoxStartingX, getStartingY() + diff)));
		}
	}

	private double getDiff(UGraphic ug) {
		final double y1 = arrow.getPreferredHeight(ug.getStringBounder());
		final double y2 = participantBox.getHeadHeight(ug.getStringBounder());
		final double diff = y1 - y2;
		return diff;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return Math.max(arrow.getPreferredHeight(stringBounder), participantBox.getHeadHeight(stringBounder));
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return arrow.getPreferredWidth(stringBounder) + participantBox.getPreferredWidth(stringBounder) / 2;
	}

	@Override
	public double getActualWidth(StringBounder stringBounder) {
		return arrow.getActualWidth(stringBounder) + participantBox.getPreferredWidth(stringBounder) / 2;
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		return arrow.getStartingX(stringBounder);
	}

	public double getMaxX(StringBounder stringBounder) {
		return arrow.getMaxX(stringBounder);
	}

	public double getMinX(StringBounder stringBounder) {
		return arrow.getMinX(stringBounder);
	}

	public String toString(StringBounder stringBounder) {
		return arrow.toString(stringBounder);
	}

}
