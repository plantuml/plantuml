/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.jungle;

import java.awt.geom.Dimension2D;
import java.util.Arrays;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class GTileNode extends AbstractTextBlock implements GTile {

	private final TextBlock tb;

	public GTileNode(GNode node) {
		final Display display = node.getDisplay();
		final SheetBlock1 sheetBlock1 = getTextBlock(display);

		final SymbolContext symbolContext = new SymbolContext(HtmlColorUtils.MY_YELLOW, HtmlColorUtils.BLACK);
		tb = USymbol.RECTANGLE.asSmall(null, sheetBlock1, TextBlockUtils.empty(0, 0), symbolContext);
	}

	public static SheetBlock1 getTextBlock(final Display display) {
		final Rose rose = new Rose();
		final SkinParam skinParam = new SkinParam();
		final HtmlColor fontColor = rose.getFontColor(skinParam, FontParam.NOTE);
		final UFont fontNote = skinParam.getFont(null, false, FontParam.NOTE);

		final FontConfiguration fc = new FontConfiguration(skinParam, FontParam.NOTE, null);

		final Sheet sheet9 = new CreoleParser(fc, HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL)
				.createSheet(display);
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet9, 0, 0);
		return sheetBlock1;
	}

	public void drawU(UGraphic ug) {
		tb.drawU(ug);
	}

	public GTileGeometry calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = tb.calculateDimension(stringBounder);
		return new GTileGeometry(dim, Arrays.asList(dim.getHeight() / 2));
	}

}
