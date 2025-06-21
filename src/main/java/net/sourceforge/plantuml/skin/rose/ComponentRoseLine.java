/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.skin.rose;

import static net.sourceforge.plantuml.klimt.color.HColor.TransparentFillBehavior.WITH_FILL_OPACITY;

import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.AbstractComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class ComponentRoseLine extends AbstractComponent {

	private final HColor color;
	private final boolean continueLine;
	private final UStroke stroke;
	private final Display stringsToDisplay;

	public ComponentRoseLine(Style style, boolean continueLine, Display stringsToDisplay, ISkinParam skinParam) {
		super(style, skinParam);
		this.color = getColorLine();
		this.stroke = getStroke();
		this.continueLine = continueLine;
		// Ideally, stringsToDisplay should never be null. However, as a safeguard, 
		// we default to Display.NULL when it is null.
		this.stringsToDisplay = stringsToDisplay == null ? Display.NULL : stringsToDisplay;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final XDimension2D dimensionToUse = area.getDimensionToUse();
		ug = ug.apply(color);
		ug = ug.apply(stroke);
//		if (continueLine)
//			ug = ug.apply(UStroke.simple());

		ug.startGroup(UGroup.singletonMap(UGroupType.TITLE, stringsToDisplay.toTooltipText()));
		drawTitleHoverTargetRect(ug, dimensionToUse);

		final int x = (int) (dimensionToUse.getWidth() / 2);
		ug.apply(UTranslate.dx(x)).draw(ULine.vline(dimensionToUse.getHeight()));
		ug.closeGroup();
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return 20;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return 1;
	}

	private void drawTitleHoverTargetRect(UGraphic ug, XDimension2D dimensionToUse) {
		if (dimensionToUse.getHeight() > 0) {
			final double hoverTargetWidth = 8;
			ug = ug.apply(UStroke.withThickness(0));
			ug = ug.apply(HColors.transparent());
			ug = ug.apply(HColors.transparent(WITH_FILL_OPACITY).bg());
			ug = ug.apply(UTranslate.dx((dimensionToUse.getWidth() - hoverTargetWidth) / 2));
			ug.draw(URectangle.build(hoverTargetWidth, dimensionToUse.getHeight()));
		}
	}
}
