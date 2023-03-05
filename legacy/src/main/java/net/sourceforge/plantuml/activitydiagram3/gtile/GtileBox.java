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

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Sheet;
import net.sourceforge.plantuml.klimt.creole.SheetBlock1;
import net.sourceforge.plantuml.klimt.creole.SheetBlock2;
import net.sourceforge.plantuml.klimt.creole.Stencil;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.skin.SkinParamColors;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GtileBox extends AbstractGtile {

	private final ClockwiseTopRightBottomLeft padding;
	private final ClockwiseTopRightBottomLeft margin;

	private final TextBlock tb;
	private double roundCorner = 25;
	private final double shadowing;
	private final HorizontalAlignment horizontalAlignment;
	private double minimumWidth = 0;

	private final LinkRendering inRendering;

	private final BoxStyle boxStyle;

	private final HColor borderColor;
	private final HColor backColor;
	private final Style style;

	static public StyleSignatureBasic getDefaultStyleDefinitionActivity() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	static public StyleSignatureBasic getDefaultStyleDefinitionArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	final public LinkRendering getInLinkRendering() {
		return inRendering;
	}

	class MyStencil implements Stencil {

		public double getStartingX(StringBounder stringBounder, double y) {
			return -padding.getLeft();
		}

		public double getEndingX(StringBounder stringBounder, double y) {
			final XDimension2D dim = calculateDimension(stringBounder);
			return dim.getWidth() - padding.getRight();
		}

	}

	public static GtileBox create(StringBounder stringBounder, ISkinParam skinParam, Display label, Swimlane swimlane,
			BoxStyle boxStyle, Stereotype stereotype) {
		final Style style = getDefaultStyleDefinitionActivity().withTOBECHANGED(stereotype)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final Style styleArrow = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam.getCurrentStyleBuilder());

		return new GtileBox(stringBounder, skinParam, label, swimlane, boxStyle, style, styleArrow);
	}

	private GtileBox(StringBounder stringBounder, ISkinParam skinParam, Display label, Swimlane swimlane,
			BoxStyle boxStyle, Style style, Style styleArrow) {
		super(stringBounder, skinParam, swimlane);
		Colors specBack = null;
		if (skinParam instanceof SkinParamColors)
			specBack = ((SkinParamColors) skinParam).getColors();

		style = style.eventuallyOverride(specBack);
		this.style = style;
		this.boxStyle = boxStyle;

		this.inRendering = LinkRendering.create(Rainbow.build(styleArrow, getIHtmlColorSet()));
		this.borderColor = style.value(PName.LineColor).asColor(getIHtmlColorSet());
		this.backColor = style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
		final FontConfiguration fc = style.getFontConfiguration(getIHtmlColorSet());
		this.horizontalAlignment = style.getHorizontalAlignment();
		this.padding = style.getPadding();
		this.margin = style.getMargin();
		this.roundCorner = style.value(PName.RoundCorner).asDouble();
		this.shadowing = style.value(PName.Shadowing).asDouble();
		final LineBreakStrategy wrapWidth = style.wrapWidth();
		this.minimumWidth = style.value(PName.MinimumWidth).asDouble();

		final Sheet sheet = skinParam.sheet(fc, skinParam.getDefaultTextAlignment(horizontalAlignment), CreoleMode.FULL)
				.createSheet(label);
		this.tb = new SheetBlock2(new SheetBlock1(sheet, wrapWidth, skinParam.getPadding()), new MyStencil(),
				UStroke.withThickness(1));
		this.print = label.toString();

	}

	final private String print;

	@Override
	public String toString() {
		return print;
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		final XDimension2D dimTotal = calculateDimension(ug.getStringBounder());
		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final UDrawable shape = boxStyle.getUDrawable(widthTotal, heightTotal, shadowing, roundCorner);

		final UStroke thickness = style.getStroke();

		if (borderColor == null)
			ug = ug.apply(HColors.none());
		else
			ug = ug.apply(borderColor);

		if (backColor == null)
			ug = ug.apply(HColors.none().bg());
		else
			ug = ug.apply(backColor.bg());

		ug = ug.apply(thickness);
		shape.drawU(ug);

		if (horizontalAlignment == HorizontalAlignment.LEFT) {
			tb.drawU(ug.apply(new UTranslate(padding.getLeft(), padding.getTop())));
		} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
			final XDimension2D dimTb = tb.calculateDimension(ug.getStringBounder());
			tb.drawU(ug.apply(
					new UTranslate(dimTotal.getWidth() - dimTb.getWidth() - padding.getRight(), padding.getBottom())));
		} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
			final XDimension2D dimTb = tb.calculateDimension(ug.getStringBounder());
			tb.drawU(ug.apply(new UTranslate((dimTotal.getWidth() - dimTb.getWidth()) / 2, padding.getBottom())));
		}
	}

	@Override
	public final XDimension2D calculateDimension(StringBounder stringBounder) {
		XDimension2D dimRaw = tb.calculateDimension(stringBounder);
		dimRaw = dimRaw.delta(padding.getLeft() + padding.getRight(), padding.getBottom() + padding.getTop());
		dimRaw = dimRaw.atLeast(minimumWidth, 0);
		return new FtileGeometry(dimRaw.getWidth() + boxStyle.getShield(), dimRaw.getHeight(), dimRaw.getWidth() / 2, 0,
				dimRaw.getHeight());
	}

}
