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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileBox extends AbstractFtile {

	private static final int MARGIN = 10;

	private final TextBlock tb;

	private final LinkRendering inRenreding;
	private final Swimlane swimlane;
	private final BoxStyle style;
	private final ISkinParam skinParam;

	final public LinkRendering getInLinkRendering() {
		return inRenreding;
	}

	public Set<Swimlane> getSwimlanes() {
		if (swimlane == null) {
			return Collections.emptySet();
		}
		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	class MyStencil implements Stencil {

		public double getStartingX(StringBounder stringBounder, double y) {
			return -MARGIN;
		}

		public double getEndingX(StringBounder stringBounder, double y) {
			final Dimension2D dim = calculateDimension(stringBounder);
			return dim.getWidth() - MARGIN;
		}

	}

	public FtileBox(boolean shadowing, Display label, UFont font, HtmlColor arrowColor, Swimlane swimlane,
			BoxStyle style, ISkinParam skinParam) {
		super(shadowing);
		this.style = style;
		this.skinParam = skinParam;
		this.swimlane = swimlane;
		this.inRenreding = new LinkRendering(arrowColor);
		final FontConfiguration fc = new FontConfiguration(skinParam, FontParam.ACTIVITY, null);
		final Sheet sheet = new CreoleParser(fc, HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL)
				.createSheet(label);
		this.tb = new SheetBlock2(new SheetBlock1(sheet, 0, skinParam.getPadding()), new MyStencil(), new UStroke(1));
		this.print = label.toString();
	}

	final private String print;

	@Override
	public String toString() {
		return print;
	}

	public void drawU(UGraphic ug) {
		final Dimension2D dimTotal = calculateDimension(ug.getStringBounder());
		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final UDrawable rect = style.getUDrawable(widthTotal, heightTotal, shadowing());

		final HtmlColor borderColor = SkinParamUtils.getColor(skinParam, ColorParam.activityBorder, null);
		final HtmlColor backColor = SkinParamUtils.getColor(skinParam, ColorParam.activityBackground, null);

		ug = ug.apply(new UChangeColor(borderColor)).apply(new UChangeBackColor(backColor)).apply(new UStroke(1.5));
		rect.drawU(ug);

		tb.drawU(ug.apply(new UTranslate(MARGIN, MARGIN)));
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = tb.calculateDimension(stringBounder);
		return new FtileGeometry(Dimension2DDouble.delta(dim, 2 * MARGIN, 2 * MARGIN), dim.getWidth() / 2 + MARGIN, 0,
				dim.getHeight() + 2 * MARGIN);
	}

}
