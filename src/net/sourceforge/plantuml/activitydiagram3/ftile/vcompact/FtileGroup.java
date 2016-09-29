/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Dimension2D;
import java.util.Set;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UGraphicInterceptorUDrawable;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.svek.UGraphicForSnake;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class FtileGroup extends AbstractFtile {

	private final double diffYY2 = 20;
	private final Ftile inner;
	private final TextBlock name;
	private final TextBlock headerNote;
	// private final HtmlColor arrowColor;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;
	// private final HtmlColor titleColor;
	private final UStroke stroke;

	public FtileGroup(Ftile inner, Display title, Display displayNote, HtmlColor arrowColor, HtmlColor backColor,
			HtmlColor titleColor, ISkinParam skinParam, HtmlColor borderColor) {
		super(inner.skinParam());
		this.backColor = backColor == null ? HtmlColorUtils.WHITE : backColor;
		this.inner = FtileUtils.addHorizontalMargin(inner, 10);
		// this.arrowColor = arrowColor;
		// this.titleColor = titleColor;
		this.borderColor = backColor == null ? HtmlColorUtils.BLACK : borderColor;
		final UFont font = skinParam.getFont(null, false, FontParam.PARTITION);

		final HtmlColor fontColor = skinParam.getFontHtmlColor(null, FontParam.PARTITION);
		final FontConfiguration fc = new FontConfiguration(font, fontColor, skinParam.getHyperlinkColor(),
				skinParam.useUnderlineForHyperlink(), skinParam.getTabSize());
		if (title == null) {
			this.name = TextBlockUtils.empty(0, 0);
		} else {
			this.name = title.create(fc, HorizontalAlignment.LEFT, skinParam);
		}
		if (Display.isNull(displayNote)) {
			this.headerNote = TextBlockUtils.empty(0, 0);
		} else {
			this.headerNote = new FloatingNote(displayNote, skinParam);
		}

		final UStroke thickness = skinParam.getThickness(LineParam.partitionBorder, null);
		this.stroke = thickness == null ? new UStroke(2) : thickness;
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return inner.getInLinkRendering();
	}

	public Set<Swimlane> getSwimlanes() {
		return inner.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return inner.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return inner.getSwimlaneOut();
	}

	private double diffHeightTitle(StringBounder stringBounder) {
		final Dimension2D dimTitle = name.calculateDimension(stringBounder);
		return Math.max(25, dimTitle.getHeight() + 20);
	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		final double suppWidth = suppWidth(stringBounder);
		return new UTranslate(suppWidth / 2, diffHeightTitle(stringBounder) + headerNoteHeight(stringBounder));
	}

	private static MinMax getMinMax(TextBlock tb, StringBounder stringBounder) {
		final LimitFinder limitFinder = new LimitFinder(stringBounder, false);
		final UGraphicForSnake interceptor = new UGraphicForSnake(limitFinder);
		final UGraphicInterceptorUDrawable interceptor2 = new UGraphicInterceptorUDrawable(interceptor);

		tb.drawU(interceptor2);
		interceptor2.flushUg();
		return limitFinder.getMinMax();
	}

	public double suppWidth(StringBounder stringBounder) {
		final FtileGeometry orig = getInnerDimension(stringBounder);
		final Dimension2D dimTitle = name.calculateDimension(stringBounder);
		final Dimension2D dimHeaderNote = headerNote.calculateDimension(stringBounder);
		final double suppWidth = MathUtils
				.max(orig.getWidth(), dimTitle.getWidth() + 20, dimHeaderNote.getWidth() + 20) - orig.getWidth();
		return suppWidth;
	}

	private FtileGeometry getInnerDimension(StringBounder stringBounder) {
		final FtileGeometry orig = inner.calculateDimension(stringBounder);
		final MinMax minMax = getMinMax(inner, stringBounder);
		final double missingWidth = minMax.getMaxX() - orig.getWidth();
		if (missingWidth > 0) {
			return orig.addDim(missingWidth + 5, 0);
		}
		return orig;
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		final FtileGeometry orig = getInnerDimension(stringBounder);
		final double suppWidth = suppWidth(stringBounder);
		final double width = orig.getWidth() + suppWidth;
		final double height = orig.getHeight() + diffHeightTitle(stringBounder) + diffYY2
				+ headerNoteHeight(stringBounder);
		final double titleAndHeaderNoteHeight = diffHeightTitle(stringBounder) + headerNoteHeight(stringBounder);
		if (orig.hasPointOut()) {
			return new FtileGeometry(width, height, orig.getLeft() + suppWidth / 2, orig.getInY()
					+ titleAndHeaderNoteHeight, orig.getOutY() + titleAndHeaderNoteHeight);
		}
		return new FtileGeometry(width, height, orig.getLeft() + suppWidth / 2, orig.getInY()
				+ titleAndHeaderNoteHeight);
	}

	private double headerNoteHeight(StringBounder stringBounder) {
		return headerNote.calculateDimension(stringBounder).getHeight();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		final SymbolContext symbolContext = new SymbolContext(backColor, borderColor).withShadow(
				skinParam().shadowing()).withStroke(stroke);
		USymbol.FRAME.asBig(name, TextBlockUtils.empty(0, 0), dimTotal.getWidth(), dimTotal.getHeight(), symbolContext)
				.drawU(ug);

		final Dimension2D dimHeaderNote = headerNote.calculateDimension(stringBounder);
		headerNote.drawU(ug.apply(new UTranslate(dimTotal.getWidth() - dimHeaderNote.getWidth() - 10,
				diffHeightTitle(ug.getStringBounder()) - 10)));

		ug.apply(getTranslate(stringBounder)).draw(inner);

	}

}
