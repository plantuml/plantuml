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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.url.Url;

class MessageSelfArrow extends Arrow {

	private final LivingParticipantBox p1;
	private final double deltaX;
	private final double deltaY;
	private final boolean isReverse;
	private final int currentLevel;
	private final double halfLifeWidth;

	public MessageSelfArrow(double startingY, Rose skin, ArrowComponent arrow, LivingParticipantBox p1, double deltaY,
													Url url, double deltaX, boolean isReverse, int currentLevel, double halfLifeWidth) {
		super(startingY, skin, arrow, url);
		this.p1 = p1;
		this.deltaY = deltaY;
		this.deltaX = deltaX;
		this.isReverse = isReverse;
		this.currentLevel = currentLevel;
		this.halfLifeWidth = halfLifeWidth;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getArrowComponent().getPreferredHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		double length = p1.getLiveThicknessAt(stringBounder, getArrowYStartLevel(stringBounder)).getSegment().getLength();
		return getArrowComponent().getPreferredWidth(stringBounder) + length;
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		startGroup(ug);
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(new UTranslate(getStartingX(stringBounder), getStartingY() + deltaY));
		final Area area = new Area(
				new XDimension2D(getPreferredWidth(stringBounder), getPreferredHeight(stringBounder)));
		area.setDeltaX1(deltaY);
		area.setLevel(currentLevel);
		area.setLiveDeltaSize(halfLifeWidth);
		startUrl(ug);
		getArrowComponent().drawU(ug, area, context);
		endUrl(ug);
		endGroup(ug);
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		// if (OptionFlags.STRICT_SELFMESSAGE_POSITION) {
		// final double pos1 = p1.getLiveThicknessAt(stringBounder,
		// getArrowYEndLevel(stringBounder)).getSegment()
		// .getPos2();
		// return pos1;
		// }
		final double pos2 = p1.getLiveThicknessAt(stringBounder, getArrowYStartLevel(stringBounder)).getSegment()
				.getPos2();
		if (isReverse) {

			return pos2  - getPreferredWidth(stringBounder);
		} else
			return pos2 + deltaX;
	}

	@Override
	public int getDirection(StringBounder stringBounder) {
		return 1;
	}

	@Override
	public double getArrowYStartLevel(StringBounder stringBounder) {
		if (getArrowComponent() instanceof ArrowComponent) {
			final ArrowComponent arrowComponent = (ArrowComponent) getArrowComponent();
			final XDimension2D dim = new XDimension2D(arrowComponent.getPreferredWidth(stringBounder),
					arrowComponent.getPreferredHeight(stringBounder));
			return getStartingY() + arrowComponent.getStartPoint(stringBounder, dim).getY();
		}
		return getStartingY();
	}

	@Override
	public double getArrowYEndLevel(StringBounder stringBounder) {
		if (getArrowComponent() instanceof ArrowComponent) {
			final ArrowComponent arrowComponent = (ArrowComponent) getArrowComponent();
			final XDimension2D dim = new XDimension2D(arrowComponent.getPreferredWidth(stringBounder),
					arrowComponent.getPreferredHeight(stringBounder));
			return getStartingY() + arrowComponent.getEndPoint(stringBounder, dim).getY();
		}
		return getStartingY() + getArrowComponent().getPreferredHeight(stringBounder);
	}

	public double getMaxX(StringBounder stringBounder) {
		return getStartingX(stringBounder) + getPreferredWidth(stringBounder);
	}

	public double getMinX(StringBounder stringBounder) {
		return getStartingX(stringBounder);
	}

	public String toString(StringBounder stringBounder) {
		return super.toString();
	}

	@Override
	public LivingParticipantBox getParticipantAt(StringBounder stringBounder, NotePosition position) {
		return p1;
	}

	@Override
	protected String getParticipant1Code() {
		return p1.getParticipantCode();
	}

	@Override
	protected String getParticipant2Code() {
		return getParticipant1Code();
	}

	@Override
	public double getActualWidth(StringBounder stringBounder) {
		return getPreferredWidth(stringBounder);
	}

	public boolean isReverse() {
		return isReverse;
	}
}
