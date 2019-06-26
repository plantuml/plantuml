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
import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.AbstractComponentRoseArrow;
import net.sourceforge.plantuml.skin.rose.ComponentRoseArrow;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class MessageExoArrow extends Arrow {

	private final LivingParticipantBox p;
	private final MessageExoType type;
	private final boolean shortArrow;
	private final ArrowConfiguration arrowConfiguration;

	public MessageExoArrow(double startingY, Rose skin, ArrowComponent arrow, LivingParticipantBox p, MessageExoType type,
			Url url, boolean shortArrow, ArrowConfiguration arrowConfiguration) {
		super(startingY, skin, arrow, url);
		this.p = p;
		this.type = type;
		this.shortArrow = shortArrow;
		this.arrowConfiguration = arrowConfiguration;
	}

	double getActualWidth(StringBounder stringBounder, double maxX) {
		final double r = getRightEndInternal(stringBounder, maxX) - getLeftStartInternal(stringBounder);
		assert r > 0;
		return r;
	}

	private double getLeftStartInternal(StringBounder stringBounder) {
		if (type == MessageExoType.FROM_LEFT || type == MessageExoType.TO_LEFT) {
			if (shortArrow) {
				return p.getLiveThicknessAt(stringBounder, getArrowYStartLevel(stringBounder)).getSegment().getPos2()
						- getPreferredWidth(stringBounder);
			} else {
				if (arrowConfiguration.getDecoration1() == ArrowDecoration.CIRCLE && type == MessageExoType.FROM_LEFT) {
					return ComponentRoseArrow.diamCircle;
				}
				if (arrowConfiguration.getDecoration2() == ArrowDecoration.CIRCLE && type == MessageExoType.TO_LEFT) {
					return ComponentRoseArrow.diamCircle;
				}
				return 0;
			}
		}
		return p.getLiveThicknessAt(stringBounder, getArrowYStartLevel(stringBounder)).getSegment().getPos2();
	}

	private double getRightEndInternal(StringBounder stringBounder, double maxX) {
		if (type == MessageExoType.FROM_LEFT || type == MessageExoType.TO_LEFT) {
			return p.getLiveThicknessAt(stringBounder, getArrowYStartLevel(stringBounder)).getSegment().getPos1();
		}
		if (shortArrow) {
			return getLeftStartInternal(stringBounder) + getPreferredWidth(stringBounder);
		}
		double result = Math.max(maxX, getLeftStartInternal(stringBounder) + getPreferredWidth(stringBounder));
		if (arrowConfiguration.getDecoration2() == ArrowDecoration.CIRCLE && type == MessageExoType.TO_RIGHT) {
			result -= ComponentRoseArrow.diamCircle;
		}
		if (arrowConfiguration.getDecoration1() == ArrowDecoration.CIRCLE && type == MessageExoType.FROM_RIGHT) {
			result -= ComponentRoseArrow.diamCircle;
		}
		return result;
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
		double result = getArrowComponent().getPreferredWidth(stringBounder);
		if (arrowConfiguration.getDecoration2() == ArrowDecoration.CIRCLE && type == MessageExoType.TO_RIGHT) {
			result += ComponentRoseArrow.diamCircle;
		}
		if (arrowConfiguration.getDecoration1() == ArrowDecoration.CIRCLE && type == MessageExoType.FROM_RIGHT) {
			result += ComponentRoseArrow.diamCircle;
		}
		if (arrowConfiguration.getDecoration1() == ArrowDecoration.CIRCLE && type == MessageExoType.FROM_LEFT) {
			result += ComponentRoseArrow.diamCircle;
		}
		if (arrowConfiguration.getDecoration2() == ArrowDecoration.CIRCLE && type == MessageExoType.TO_LEFT) {
			result += ComponentRoseArrow.diamCircle;
		}

		return result;
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double x1 = getStartingX(stringBounder);
		final double x2 = maxX;
		ug = ug.apply(new UTranslate(x1, getStartingY()));
		startUrl(ug);
		getArrowComponent().drawU(ug, new Area(getActualDimension(stringBounder, x2)), context);
		endUrl(ug);
	}

	private Dimension2D getActualDimension(StringBounder stringBounder, double maxX) {
		return new Dimension2DDouble(getActualWidth(stringBounder, maxX), getArrowComponent().getPreferredHeight(
				stringBounder));
	}

	@Override
	public double getArrowYStartLevel(StringBounder stringBounder) {
		if (getArrowComponent() instanceof AbstractComponentRoseArrow) {
			final AbstractComponentRoseArrow arrowComponent = (AbstractComponentRoseArrow) getArrowComponent();
			final Dimension2D dim = new Dimension2DDouble(arrowComponent.getPreferredWidth(stringBounder),
					arrowComponent.getPreferredHeight(stringBounder));
			return getStartingY() + arrowComponent.getStartPoint(stringBounder, dim).getY();
		}
		return getStartingY();
	}

	@Override
	public double getArrowYEndLevel(StringBounder stringBounder) {
		if (getArrowComponent() instanceof AbstractComponentRoseArrow) {
			final AbstractComponentRoseArrow arrowComponent = (AbstractComponentRoseArrow) getArrowComponent();
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
