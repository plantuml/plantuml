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

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GtileAssembly extends GtileTopDown {

	private final TextBlock textBlock;

	public GtileAssembly(Gtile tile1, Gtile tile2, LinkRendering linkRendering) {
		super(tile1, tile2);
		this.textBlock = getTextBlock(linkRendering.getDisplay());
		// See FtileFactoryDelegatorAssembly
	}

	@Override
	protected UTranslate supplementaryMove() {
		final double height = 30 + textBlock.calculateDimension(stringBounder).getHeight();
		return new UTranslate(0, height);
	}

	final public StyleSignatureBasic getDefaultStyleDefinitionArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D raw = super.calculateDimension(stringBounder);
		final double textBlockWidth = textBlock.calculateDimension(stringBounder).getWidth();
		final double pos1 = tile1.getCoord(GPoint.SOUTH_HOOK).compose(getPos1()).getDx();
		return raw.atLeast(pos1 + textBlockWidth, 0);
	}

	protected final TextBlock getTextBlock(Display display) {
		// DUP3945
		if (Display.isNull(display))
			return TextBlockUtils.EMPTY_TEXT_BLOCK;

		final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
		final FontConfiguration fontConfiguration = style.getFontConfiguration(skinParam().getIHtmlColorSet());
		return display.create7(fontConfiguration, HorizontalAlignment.LEFT, skinParam(), CreoleMode.SIMPLE_LINE);
	}

	@Override
	public Collection<GConnection> getInnerConnections() {
		final GConnection arrow = new GConnectionVerticalDown(getPos1(), tile1.getGPoint(GPoint.SOUTH_HOOK), getPos2(),
				tile2.getGPoint(GPoint.NORTH_HOOK), textBlock);
		return Collections.singletonList(arrow);
	}

}