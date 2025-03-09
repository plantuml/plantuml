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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;

public class DelayTile extends AbstractTile implements Tile {

	private final Delay delay;
	private final TileArguments tileArguments;
	// private Real first;
	// private Real last;
	private Real middle;
	private final YGauge yGauge;

	public Event getEvent() {
		return delay;
	}

	public DelayTile(Delay delay, TileArguments tileArguments, YGauge currentY) {
		super(tileArguments.getStringBounder(), currentY);
		this.delay = delay;
		this.tileArguments = tileArguments;
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	private void init(StringBounder stringBounder) {
		if (middle != null) {
			return;
		}
		final Real first = tileArguments.getFirstLivingSpace().getPosC(stringBounder);
		final Component comp = getComponent(stringBounder);
		final Real last = tileArguments.getLastLivingSpace().getPosC(stringBounder);
		this.middle = RealUtils.middle(first, last);

	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = tileArguments.getSkin().createComponent(delay.getUsedStyles(), ComponentType.DELAY_TEXT,
				null, tileArguments.getSkinParam(), delay.getText());
		return comp;
	}

	private double getPreferredWidth(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim.getWidth();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		init(stringBounder);
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		final Area area = Area.create(getPreferredWidth(stringBounder), dim.getHeight());
		final double ypos;
		if (YGauge.USE_ME)
			ypos = getYGauge().getMin().getCurrentValue();
		else
			ypos = getTimeHook().getValue();
		tileArguments.getLivingSpaces().delayOn(ypos, dim.getHeight());

		ug = ug.apply(UTranslate.dx(getMinX().getCurrentValue()));
		comp.drawU(ug, area, (Context2D) ug);
	}

	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return dim.getHeight();
	}

	public void addConstraints() {
	}

	public Real getMinX() {
		init(getStringBounder());
		return this.middle.addFixed(-getPreferredWidth(getStringBounder()) / 2);
	}

	public Real getMaxX() {
		init(getStringBounder());
		return this.middle.addFixed(getPreferredWidth(getStringBounder()) / 2);
	}

	// private double startingY;
	//
	// public void setStartingY(double startingY) {
	// this.startingY = startingY;
	// }
	//
	// public double getStartingY() {
	// return startingY;
	// }

}
