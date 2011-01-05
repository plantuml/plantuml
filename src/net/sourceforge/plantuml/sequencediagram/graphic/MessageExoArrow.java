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
 * Revision $Revision: 4274 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class MessageExoArrow extends Arrow {

	private final LivingParticipantBox p;
	private final MessageExoType type;

	public MessageExoArrow(double startingY, Skin skin, Component arrow, LivingParticipantBox p, MessageExoType type) {
		super(startingY, skin, arrow);

		this.p = p;
		this.type = type;
	}

	double getActualWidth(StringBounder stringBounder, double maxX) {
		final double r = getRightEndInternal(stringBounder, maxX) - getLeftStartInternal(stringBounder);
		assert r > 0;
		return r;
	}

	private double getLeftStartInternal(StringBounder stringBounder) {
		if (type == MessageExoType.FROM_LEFT || type == MessageExoType.TO_LEFT) {
			// return Math.max(0, p.getLiveThicknessAt(stringBounder,
			// getArrowYStartLevel(stringBounder)).getPos1()
			// - getPreferredWidth(stringBounder));
			return 0;
		}
		return p.getLiveThicknessAt(stringBounder, getArrowYStartLevel(stringBounder)).getPos2();
	}

	private double getRightEndInternal(StringBounder stringBounder, double maxX) {
		if (type == MessageExoType.FROM_LEFT || type == MessageExoType.TO_LEFT) {
			return p.getLiveThicknessAt(stringBounder, getArrowYStartLevel(stringBounder)).getPos1();
		}
		return Math.max(maxX, getLeftStartInternal(stringBounder) + getPreferredWidth(stringBounder));
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getArrowComponent().getPreferredHeight(stringBounder);
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		return getLeftStartInternal(stringBounder);
	}

	@Override
	public int getDirection(StringBounder stringBounder) {
		return type.getDirection();
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getArrowComponent().getPreferredWidth(stringBounder);
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug.translate(getStartingX(stringBounder), getStartingY());
		getArrowComponent().drawU(ug, getActualDimension(stringBounder, maxX), context);
	}

	private Dimension2D getActualDimension(StringBounder stringBounder, double maxX) {
		return new Dimension2DDouble(getActualWidth(stringBounder, maxX), getArrowComponent().getPreferredHeight(
				stringBounder));
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
		return getRightEndInternal(stringBounder, 0);
	}

	public double getMinX(StringBounder stringBounder) {
		return getLeftStartInternal(stringBounder);
	}

	public String toString(StringBounder stringBounder) {
		return getMinX(stringBounder) + "-" + getMaxX(stringBounder);
	}

	public final MessageExoType getType() {
		return type;
	}

	@Override
	public LivingParticipantBox getParticipantAt(StringBounder stringBounder, NotePosition position) {
		return p;
	}

	@Override
	public double getActualWidth(StringBounder stringBounder) {
		return getActualWidth(stringBounder, getMaxX());
	}
	
}
