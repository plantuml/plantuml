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
 * Revision $Revision: 4721 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class ArrowAndParticipant extends Arrow implements InGroupable {

	private final Arrow arrow;
	private final ParticipantBox participantBox;

	public ArrowAndParticipant(StringBounder stringBounder, Arrow arrow, ParticipantBox participantBox) {
		super(arrow.getStartingY(), arrow.getSkin(), arrow.getArrowComponent());
		this.arrow = arrow;
		this.participantBox = participantBox;
		//participantBox.setDeltaHead(arrow.getStartingY());
		arrow.setPaddingArrowHead(participantBox.getPreferredWidth(stringBounder) / 2);
		// participantBox.setTopStartingY(arrow.getStartingY());

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
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final double atX = ug.getTranslateX();
		final double atY = ug.getTranslateY();
		arrow.drawInternalU(ug, maxX, context);
		ug.setTranslate(atX, atY);
		//ug.translate(getStartingX(ug.getStringBounder()), getStartingY());
		ug.translate(participantBox.getStartingX(), getStartingY());
		participantBox.drawParticipantHead(ug);

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
