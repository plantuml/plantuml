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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.LimitFinder;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicInterceptorUDrawable;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.skin.AlignmentParam;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.UGraphicForSnake;
import net.sourceforge.plantuml.utils.MathUtils;

public class FtileGroup extends AbstractFtile {

	private final double diffYY2 = 20;
	private final Ftile inner;
	private final TextBlock name;
	private final TextBlock headerNote;
	private final HColor borderColor;
	private final HColor backColor;
	private final double shadowing;
	private final UStroke stroke;
	private final USymbol type;
	private final double roundCorner;

	final public StyleSignatureBasic getStyleSignature() {
		return getStyleSignature(type);
	}

	final static public StyleSignatureBasic getStyleSignature(USymbol symbol) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, symbol.getSNames(),
				SName.composite);
	}

	public FtileGroup(Ftile inner, Display title, HColor backColor, ISkinParam skinParam, USymbol type, Style style) {
		super(inner.skinParam());
		this.type = type;
		this.inner = FtileUtils.addHorizontalMargin(inner, 10);

		final FontConfiguration fc = style.getFontConfiguration(getIHtmlColorSet());
		this.shadowing = style.getShadowing();
		this.backColor = backColor == null ? style.value(PName.BackGroundColor).asColor(getIHtmlColorSet()) : backColor;
		this.borderColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		this.roundCorner = style.value(PName.RoundCorner).asDouble();

		if (title == null)
			this.name = TextBlockUtils.empty(0, 0);
		else
			this.name = title.create(fc, HorizontalAlignment.LEFT, skinParam);

//		if (Display.isNull(displayNote))
		this.headerNote = TextBlockUtils.empty(0, 0);
//		else
//			this.headerNote = new FloatingNote(displayNote, skinParam);

		this.stroke = style.getStroke();
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		return inner.getMyChildren();
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
		final XDimension2D dimTitle = name.calculateDimension(stringBounder);
		return Math.max(25, dimTitle.getHeight() + 20);
	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		final double suppWidth = suppWidth(stringBounder);
		return new UTranslate(suppWidth / 2, diffHeightTitle(stringBounder) + headerNoteHeight(stringBounder));
	}

	private MinMax getInnerMinMax(StringBounder stringBounder) {
		final LimitFinder limitFinder = LimitFinder.create(stringBounder, false);
		final UGraphicForSnake interceptor = new UGraphicForSnake(limitFinder);
		final UGraphicInterceptorUDrawable interceptor2 = new UGraphicInterceptorUDrawable(interceptor);

		inner.drawU(interceptor2);
		interceptor2.flushUg();
		return limitFinder.getMinMax();
	}

	public double suppWidth(StringBounder stringBounder) {
		final FtileGeometry orig = getInnerDimension(stringBounder);
		final XDimension2D dimTitle = name.calculateDimension(stringBounder);
		final XDimension2D dimHeaderNote = headerNote.calculateDimension(stringBounder);
		final double suppWidth = MathUtils.max(orig.getWidth(), dimTitle.getWidth() + 20, dimHeaderNote.getWidth() + 20)
				- orig.getWidth();
		return suppWidth;
	}

	private FtileGeometry cachedInnerDimension;

	private FtileGeometry getInnerDimension(StringBounder stringBounder) {
		if (cachedInnerDimension == null)
			cachedInnerDimension = getInnerDimensionSlow(stringBounder);

		return cachedInnerDimension;

	}

	private FtileGeometry getInnerDimensionSlow(StringBounder stringBounder) {
		final FtileGeometry orig = inner.calculateDimension(stringBounder);
		final MinMax minMax = getInnerMinMax(stringBounder);
		final double missingWidth = minMax.getMaxX() - orig.getWidth();
		if (missingWidth > 0)
			return orig.addDim(missingWidth + 5, 0);

		return orig;
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final FtileGeometry orig = getInnerDimension(stringBounder);
		final double suppWidth = suppWidth(stringBounder);
		final double width = orig.getWidth() + suppWidth;
		final double height = orig.getHeight() + diffHeightTitle(stringBounder) + diffYY2
				+ headerNoteHeight(stringBounder);
		final double titleAndHeaderNoteHeight = diffHeightTitle(stringBounder) + headerNoteHeight(stringBounder);
		if (orig.hasPointOut())
			return new FtileGeometry(width, height, orig.getLeft() + suppWidth / 2,
					orig.getInY() + titleAndHeaderNoteHeight, orig.getOutY() + titleAndHeaderNoteHeight);

		return new FtileGeometry(width, height, orig.getLeft() + suppWidth / 2,
				orig.getInY() + titleAndHeaderNoteHeight);
	}

	private double headerNoteHeight(StringBounder stringBounder) {
		return headerNote.calculateDimension(stringBounder).getHeight();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);

		final Fashion symbolContext = new Fashion(backColor, borderColor).withShadow(shadowing).withStroke(stroke)
				.withCorner(roundCorner, 0);

		final HorizontalAlignment align = inner.skinParam().getHorizontalAlignment(AlignmentParam.packageTitleAlignment,
				null, false, null);
		type.asBig(name, align, TextBlockUtils.empty(0, 0), dimTotal.getWidth(), dimTotal.getHeight(), symbolContext,
				skinParam().getStereotypeAlignment()).drawU(ug);

		final XDimension2D dimHeaderNote = headerNote.calculateDimension(stringBounder);
		headerNote.drawU(ug.apply(new UTranslate(dimTotal.getWidth() - dimHeaderNote.getWidth() - 10,
				diffHeightTitle(ug.getStringBounder()) - 10)));

		ug.apply(getTranslate(stringBounder)).draw(inner);

	}

}
