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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LifeEventTile extends AbstractTile implements TileWithUpdateStairs {

	private final LifeEvent lifeEvent;
	private final TileArguments tileArguments;
	private final LivingSpace livingSpace;
	private final Rose skin;
	private final ISkinParam skinParam;

	public void updateStairs(StringBounder stringBounder, double y) {
		// System.err.println("LifeEventTile::updateStairs " + lifeEvent + " " + livingSpace.getParticipant() + " y=" + y);
		livingSpace.addStepForLivebox(getEvent(), y);
	}

	public Event getEvent() {
		return lifeEvent;
	}
	
	@Override
	public double getYPoint(StringBounder stringBounder) {
		return 0;
	}

	public LifeEventTile(LifeEvent lifeEvent, TileArguments tileArguments, LivingSpace livingSpace, Rose skin,
			ISkinParam skinParam) {
		this.lifeEvent = lifeEvent;
		this.tileArguments = tileArguments;
		this.livingSpace = livingSpace;
		this.skin = skin;
		this.skinParam = skinParam;
	}

	public void drawU(UGraphic ug) {
		if (isDestroyWithoutMessage()) {
			final Component cross = skin.createComponent(null, ComponentType.DESTROY, null, skinParam, null);
			final Dimension2D dimCross = cross.getPreferredDimension(ug.getStringBounder());
			final double x = livingSpace.getPosC(ug.getStringBounder()).getCurrentValue();
			cross.drawU(ug.apply(UTranslate.dx(x - dimCross.getWidth() / 2)), null, (Context2D) ug);
		}
	}

	public boolean isDestroyWithoutMessage() {
		return lifeEvent.getMessage() == null && lifeEvent.getType() == LifeEventType.DESTROY;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
//		if (lifeEvent.isActivate()) {
//			return 20;
//		}
		if (isDestroyWithoutMessage()) {
			final Component cross = skin.createComponent(null, ComponentType.DESTROY, null, skinParam, null);
			final Dimension2D dimCross = cross.getPreferredDimension(stringBounder);
			return dimCross.getHeight();
		}
		return 0;
	}

	public void addConstraints(StringBounder stringBounder) {
	}

	public Real getMinX(StringBounder stringBounder) {
		// return tileArguments.getLivingSpace(lifeEvent.getParticipant()).getPosB();
		return livingSpace.getPosB();
	}

	public Real getMaxX(StringBounder stringBounder) {
		// final LivingSpace livingSpace2 = tileArguments.getLivingSpace(lifeEvent.getParticipant());
		return RealUtils.max(livingSpace.getPosD(stringBounder), livingSpace.getPosC2(stringBounder));
	}

}
