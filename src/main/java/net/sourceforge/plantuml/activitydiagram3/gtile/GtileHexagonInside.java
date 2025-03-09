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

import net.sourceforge.plantuml.activitydiagram3.ftile.Hexagon;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GtileHexagonInside extends AbstractGtile {

	protected final HColor backColor;
	protected final HColor borderColor;

	protected final TextBlock label;
	protected final XDimension2D dimLabel;

	protected final double shadowing;

	final public StyleSignatureBasic getDefaultStyleDefinition() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	// FtileDiamondInside

	public GtileHexagonInsideLabelled withEastLabel(TextBlock eastLabel) {
		return new GtileHexagonInsideLabelled(this, TextBlockUtils.EMPTY_TEXT_BLOCK, eastLabel,
				TextBlockUtils.EMPTY_TEXT_BLOCK);
	}

	public GtileHexagonInsideLabelled withWestLabel(TextBlock westLabel) {
		return new GtileHexagonInsideLabelled(this, TextBlockUtils.EMPTY_TEXT_BLOCK, TextBlockUtils.EMPTY_TEXT_BLOCK,
				westLabel);
	}

	public AbstractGtileRoot withSouthLabel(TextBlock southLabel) {
		return new GtileHexagonInsideLabelled(this, southLabel, TextBlockUtils.EMPTY_TEXT_BLOCK,
				TextBlockUtils.EMPTY_TEXT_BLOCK);
	}

	public GtileHexagonInside(StringBounder stringBounder, TextBlock label, ISkinParam skinParam, HColor backColor,
			HColor borderColor, Swimlane swimlane) {
		super(stringBounder, skinParam, swimlane);

		final Style style = getDefaultStyleDefinition().getMergedStyle(skinParam.getCurrentStyleBuilder());
		this.borderColor = style.value(PName.LineColor).asColor(getIHtmlColorSet());
		this.backColor = style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
		this.shadowing = style.getShadowing();

		this.label = label;
		this.dimLabel = label.calculateDimension(stringBounder);

	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim;
		if (dimLabel.getWidth() == 0 || dimLabel.getHeight() == 0) {
			dim = new XDimension2D(Hexagon.hexagonHalfSize * 2, Hexagon.hexagonHalfSize * 2);
		} else {
			dim = dimLabel.atLeast(Hexagon.hexagonHalfSize * 2, Hexagon.hexagonHalfSize * 2)
					.delta(Hexagon.hexagonHalfSize * 2, 0);
		}
		return dim;
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);
		ug = ug.apply(borderColor).apply(getThickness()).apply(backColor.bg());
		ug.draw(Hexagon.asPolygon(shadowing, dimTotal.getWidth(), dimTotal.getHeight()));

		final double lx = (dimTotal.getWidth() - dimLabel.getWidth()) / 2;
		final double ly = (dimTotal.getHeight() - dimLabel.getHeight()) / 2;
		label.drawU(ug.apply(new UTranslate(lx, ly)));
	}

}
