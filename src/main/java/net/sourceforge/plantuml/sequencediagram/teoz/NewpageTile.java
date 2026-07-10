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
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;

public class NewpageTile extends AbstractTile {

	// Small vertical space before and after the dashed separator
	private static final double MARGINY = 10;

	private final Newpage newpage;
	private final TileArguments tileArguments;
	private final YGauge yGauge;

	@Override
	public double getContactPointRelative() {
		return 0;
	}

	public NewpageTile(Newpage newpage, TileArguments tileArguments, YGauge currentY) {
		super(tileArguments.getStringBounder(), currentY);
		this.newpage = newpage;
		this.tileArguments = tileArguments;
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	private Component getComponent() {
		return tileArguments.getSkin().createComponentNewPage(newpage.getUsedStyles(), tileArguments.getSkinParam());
	}

	public void drawU(UGraphic ug) {
		// Like in Puma, the dashed separator is displayed at the bottom of the
		// page ending here and at the top of the page starting here: the pages
		// slightly overlap in PlayingSpaceWithParticipants
		if (((Context2D) ug).isBackground())
			return;

		final Component comp = getComponent();
		final Area area = Area.create(tileArguments.getBorder2() - tileArguments.getBorder1()
				- tileArguments.getXOrigin().getCurrentValue(), comp.getPreferredHeight(getStringBounder()));
		ug = ug.apply(new UTranslate(tileArguments.getBorder1(), MARGINY));
		comp.drawU(ug, area, (Context2D) ug);
	}

	public double getPreferredHeight() {
		return getComponent().getPreferredHeight(getStringBounder()) + 2 * MARGINY;
	}

	public void addConstraints() {
	}

	public Real getMinX() {
		return tileArguments.getXOrigin();
	}

	public Real getMaxX() {
		return tileArguments.getXOrigin();
	}

	public Event getEvent() {
		return newpage;
	}

}
