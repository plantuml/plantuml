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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class MessageSelfArrow extends Arrow {

	private final LivingParticipantBox p1;
	private final double deltaX;
	private final double deltaY;

	public MessageSelfArrow(double startingY, Rose skin, ArrowComponent arrow, LivingParticipantBox p1, double deltaY,
			Url url, double deltaX) {
		super(startingY, skin, arrow, url);
		this.p1 = p1;
		this.deltaY = deltaY;
		this.deltaX = deltaX;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getArrowComponent().getPreferredHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getArrowComponent().getPreferredWidth(stringBounder);
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(new UTranslate(getStartingX(stringBounder), getStartingY() + deltaY));
		final Area area = new Area(new Dimension2DDouble(getPreferredWidth(stringBounder),
				getPreferredHeight(stringBounder)));
		area.setDeltaX1(deltaY);
		startUrl(ug);
		getArrowComponent().drawU(ug, area, context);
		endUrl(ug);
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		// if (OptionFlags.STRICT_SELFMESSAGE_POSITION) {
		// final double pos1 = p1.getLiveThicknessAt(stringBounder, getArrowYEndLevel(stringBounder)).getSegment()
		// .getPos2();
		// return pos1;
		// }
		final double pos2 = p1.getLiveThicknessAt(stringBounder, getArrowYStartLevel(stringBounder)).getSegment()
				.getPos2();
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
			final Dimension2D dim = new Dimension2DDouble(arrowComponent.getPreferredWidth(stringBounder),
					arrowComponent.getPreferredHeight(stringBounder));
			return getStartingY() + arrowComponent.getStartPoint(stringBounder, dim).getY();
		}
		return getStartingY();
	}

	@Override
	public double getArrowYEndLevel(StringBounder stringBounder) {
		if (getArrowComponent() instanceof ArrowComponent) {
			final ArrowComponent arrowComponent = (ArrowComponent) getArrowComponent();
			final Dimension2D dim = new Dimension2DDouble(arrowComponent.getPreferredWidth(stringBounder),
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
	public double getActualWidth(StringBounder stringBounder) {
		return getPreferredWidth(stringBounder);
	}
}
