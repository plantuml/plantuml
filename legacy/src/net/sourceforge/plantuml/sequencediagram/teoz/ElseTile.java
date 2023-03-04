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
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.GroupingLeaf;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SkinParamBackcolored;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class ElseTile extends AbstractTile {

	private final Rose skin;
	private final ISkinParam skinParam;
	private final GroupingLeaf anElse;
	private final Tile parent;
	private final YGauge yGauge;

	public Event getEvent() {
		return anElse;
	}

	@Override
	public double getContactPointRelative() {
		return 0;
	}

	public ElseTile(GroupingLeaf anElse, Rose skin, ISkinParam skinParam, Tile parent, YGauge currentY) {
		super(((AbstractTile) parent).getStringBounder(), currentY);
		this.anElse = anElse;
		this.skin = skin;
		this.skinParam = skinParam;
		this.parent = parent;
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	public final HColor getBackColorGeneral() {
		return anElse.getBackColorGeneral();
	}

	public Component getComponent(StringBounder stringBounder) {
		// final Display display = Display.create(anElse.getTitle());
		final ISkinParam tmp = new SkinParamBackcolored(skinParam, anElse.getBackColorElement(),
				anElse.getBackColorGeneral());

		final Display display = Display.create(anElse.getComment());
		final Component comp = skin.createComponent(anElse.getUsedStyles(), ComponentType.GROUPING_ELSE_TEOZ, null, tmp,
				display);
		return comp;
	}

	public void drawU(UGraphic ug) {
		if (YGauge.USE_ME == false)
			return;

		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		final Real min = parent.getMinX().addFixed(GroupingTile.EXTERNAL_MARGINX1);
		final Real max = parent.getMaxX().addFixed(-GroupingTile.EXTERNAL_MARGINX2);
		final Context2D context = (Context2D) ug;
		double height = dim.getHeight();
		// if (context.isBackground() && parent instanceof GroupingTile) {
		// final double startingY = ((GroupingTile) parent).getStartY();
		// final double totalParentHeight = parent.getPreferredHeight(stringBounder);
		// height = totalParentHeight - (startingY - y);
		// }
		final Area area = Area.create(max.getCurrentValue() - min.getCurrentValue(), height);
		ug = ug.apply(new UTranslate(min.getCurrentValue(), 0));
		comp.drawU(ug, area, context);
	}

	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());

		double height = dim.getHeight();
//		if (anElse.getComment() != null)
//			height += 10;
//
//		return height + 20;
		return height;
	}

	public void addConstraints() {
		// final Component comp = getComponent(stringBounder);
		// final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final double width = dim.getWidth();
	}

	public Real getMinX() {
		return parent.getMinX();
	}

	public Real getMaxX() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return getMinX().addFixed(dim.getWidth());
	}

}
