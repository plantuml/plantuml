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
package net.sourceforge.plantuml.klimt.creole;

import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.atom.AbstractAtom;
import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UHorizontalLine;
import net.sourceforge.plantuml.style.ISkinSimple;

public class CreoleHorizontalLine extends AbstractAtom implements Atom {

	private final FontConfiguration fontConfiguration;
	private final String line;
	private final char style;
	private final ISkinSimple skinParam;

	private final static double defaultThickness = 1;

	public static CreoleHorizontalLine create(FontConfiguration fontConfiguration, String line, char style,
			ISkinSimple skinParam) {
		return new CreoleHorizontalLine(fontConfiguration, line, style, skinParam);
	}

	private CreoleHorizontalLine(FontConfiguration fontConfiguration, String line, char style, ISkinSimple skinParam) {
		this.fontConfiguration = fontConfiguration;
		this.line = line;
		this.style = style;
		this.skinParam = skinParam;
	}

	private UHorizontalLine getHorizontalLine() {
		if (line.length() == 0) {
			return UHorizontalLine.infinite(defaultThickness, 0, 0, style);
		}
		final TextBlock tb = getTitle();
		return UHorizontalLine.infinite(defaultThickness, 0, 0, tb, style);
	}

	private TextBlock getTitle() {
		if (line.length() == 0) {
			return TextBlockUtils.empty(0, 0);
		}
		final SheetBuilder parser = skinParam.sheet(fontConfiguration, HorizontalAlignment.LEFT, CreoleMode.FULL);
		final Sheet sheet = parser.createSheet(Display.getWithNewlines(line));
		final TextBlock tb = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());
		return tb;
	}

	public void drawU(UGraphic ug) {
		// ug = ug.apply(UChangeColor.nnn(fontConfiguration.getColor()));
		final XDimension2D dim = calculateDimension(ug.getStringBounder());
		ug = ug.apply(UTranslate.dy(dim.getHeight() / 2));
		ug.draw(getHorizontalLine());
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		if (line.length() == 0) {
			return new XDimension2D(10, 10);
		}
		final TextBlock tb = getTitle();
		return tb.calculateDimension(stringBounder);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

}
