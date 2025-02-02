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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.SkinParamUtils;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GtileCircleSpot extends AbstractGtile {

	private static final int SIZE = 20;

	private final String spot;
	private final FontConfiguration fc;
	private final HColor backColor;
	private double shadowing;

	private StyleSignatureBasic getDefaultStyleDefinitionCircle() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.circle);
	}

	public GtileCircleSpot(StringBounder stringBounder, ISkinParam skinParam, HColor backColor, Swimlane swimlane,
			String spot, UFont font) {
		super(stringBounder, skinParam, swimlane);
		this.spot = spot;
		this.backColor = backColor;
		this.fc = FontConfiguration.create(skinParam, FontParam.ACTIVITY, null);
		final Style style = getDefaultStyleDefinitionCircle().getMergedStyle(skinParam().getCurrentStyleBuilder());
		this.shadowing = style.getShadowing();

	}

	@Override
	protected void drawUInternal(UGraphic ug) {

		final HColor borderColor = SkinParamUtils.getColor(skinParam(), null, ColorParam.activityBorder);
		final HColor backColor = this.backColor == null
				? SkinParamUtils.getColor(skinParam(), null, ColorParam.activityBackground)
				: this.backColor;

		final UEllipse circle = UEllipse.build(SIZE, SIZE);
		circle.setDeltaShadow(shadowing);
		ug.apply(borderColor).apply(backColor.bg()).apply(getThickness()).draw(circle);

		ug.apply(fc.getColor()).apply(new UTranslate(SIZE / 2, SIZE / 2))
				.draw(new UCenteredCharacter(spot.charAt(0), fc.getFont()));

	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(SIZE, SIZE);
	}

}
