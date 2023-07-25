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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Sheet;
import net.sourceforge.plantuml.klimt.creole.SheetBlock1;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.AlignmentParam;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.image.Opale;

public class FtileIfWithDiamonds extends FtileIfNude {

	private static final double SUPP_WIDTH = 20;
	protected final Ftile diamond1;
	protected final Ftile diamond2;
	private TextBlock opaleLeft = TextBlockUtils.EMPTY_TEXT_BLOCK;
	private TextBlock opaleRight = TextBlockUtils.EMPTY_TEXT_BLOCK;

	public FtileIfWithDiamonds(Ftile diamond1, Ftile tile1, Ftile tile2, Ftile diamond2, Swimlane in,
			StringBounder stringBounder, Collection<PositionedNote> notes) {
		super(tile1, tile2, in);
		this.diamond1 = diamond1;
		this.diamond2 = diamond2;
		for (PositionedNote first : notes) {
			if (first.getNotePosition() == NotePosition.LEFT) {
				if (this.opaleLeft != TextBlockUtils.EMPTY_TEXT_BLOCK)
					continue;
				this.opaleLeft = createOpale(first, skinParam());
				final double pos1 = getTranslateDiamond1(stringBounder).getDx();
				final XDimension2D dimOpale = opaleLeft.calculateDimension(stringBounder);
				final double opaleWith = dimOpale.getWidth();
				if (opaleWith > pos1)
					xDeltaNote = opaleWith - pos1;

				yDeltaNote = Math.max(yDeltaNote, dimOpale.getHeight());
			} else if (first.getNotePosition() == NotePosition.RIGHT) {
				if (this.opaleRight != TextBlockUtils.EMPTY_TEXT_BLOCK)
					continue;
				this.opaleRight = createOpale(first, skinParam());
				final XDimension2D dimOpale = opaleRight.calculateDimension(stringBounder);
				final double pos1 = getTranslateDiamond1(stringBounder).getDx()
						+ diamond1.calculateDimension(stringBounder).getWidth() + dimOpale.getWidth();
				final double pos2 = calculateDimensionInternalSlow(stringBounder).getWidth();
				if (pos1 > pos2)
					suppWidthNode = pos1 - pos2;

				yDeltaNote = Math.max(yDeltaNote, dimOpale.getHeight());
			}
			clearCacheDimensionInternal();
		}

	}

	public static Opale createOpale(final PositionedNote first, ISkinParam skinParam) {
		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder())
				.eventuallyOverride(first.getColors());
		final HColor noteBackgroundColor = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
		final HColor borderColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final FontConfiguration fc = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		final double shadowing = style.value(PName.Shadowing).asDouble();
		final LineBreakStrategy wrapWidth = style.wrapWidth();
		final UStroke stroke = style.getStroke();

		final HorizontalAlignment align = skinParam.getHorizontalAlignment(AlignmentParam.noteTextAlignment, null,
				false, null);
		final Sheet sheet = skinParam.sheet(fc, align, CreoleMode.FULL).createSheet(first.getDisplay());
		final SheetBlock1 tmp1 = new SheetBlock1(sheet, wrapWidth, skinParam.getPadding());
		// final TextBlock text = new SheetBlock2(tmp1, this, stroke);
		return new Opale(shadowing, borderColor, noteBackgroundColor, tmp1, false, stroke);
	}

	private static StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.note);
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		return Arrays.asList(diamond1, diamond2, tile1, tile2);
	}

	protected int getYdelta1a(StringBounder stringBounder) {
		if (getSwimlanes().size() > 1)
			return 20;
		return 10;
	}

	protected int getYdelta1b(StringBounder stringBounder) {
		if (getSwimlanes().size() > 1)
			return 10;
		return hasTwoBranches(stringBounder) ? 6 : 0;
	}

	protected double getYdeltaForLabels(StringBounder stringBounder) {
		return 0;
	}

	@Override
	protected double widthInner(StringBounder stringBounder) {
		final FtileGeometry dim1 = diamond1.calculateDimension(stringBounder);
		return Math.max(super.widthInner(stringBounder), dim1.getWidth() + SUPP_WIDTH);
	}

	@Override
	protected FtileGeometry calculateDimensionInternalSlow(StringBounder stringBounder) {
		final FtileGeometry dim1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry dim2 = diamond2.calculateDimension(stringBounder);

		final FtileGeometry dimNude = super.calculateDimensionInternalSlow(stringBounder);

		final FtileGeometry all = dim1.appendBottom(dimNude).appendBottom(dim2);

		final double deltaHeight = getYdelta1a(stringBounder) + getYdelta1b(stringBounder)
				+ getYdeltaForLabels(stringBounder);

		final FtileGeometry result = all.addDim(0, deltaHeight).incInY(yDeltaNote);
		return result;

	}

//	protected double getSuppWidthForNotes(StringBounder stringBounder) {
//		return opale.calculateDimension(stringBounder).getWidth();
//	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		if (TextBlockUtils.isEmpty(opaleLeft, stringBounder) == false) {
			final double xOpale = getTranslateDiamond1(stringBounder).getDx()
					- opaleLeft.calculateDimension(stringBounder).getWidth();
			opaleLeft.drawU(ug.apply(UTranslate.dx(xOpale)));
		}

		if (TextBlockUtils.isEmpty(opaleRight, stringBounder) == false) {
			final double xOpale = getTranslateDiamond1(stringBounder).getDx()
					+ diamond1.calculateDimension(stringBounder).getWidth();
			opaleRight.drawU(ug.apply(UTranslate.dx(xOpale)));
		}

		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		super.drawU(ug);
		ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);
	}

	@Override
	protected UTranslate getTranslateBranch1(StringBounder stringBounder) {
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		return super.getTranslateBranch1(stringBounder)
				.compose(UTranslate.dy(dimDiamond1.getHeight() + getYdelta1a(stringBounder)));
	}

	@Override
	protected UTranslate getTranslateBranch2(StringBounder stringBounder) {
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		return super.getTranslateBranch2(stringBounder)
				.compose(UTranslate.dy(dimDiamond1.getHeight() + getYdelta1a(stringBounder)));
	}

	protected UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final double y1 = yDeltaNote;
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft();
		return new UTranslate(x1, y1);
	}

	protected UTranslate getTranslateDiamond2(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final double x2 = dimTotal.getLeft() - dimDiamond2.getWidth() / 2;
		final double y2 = dimTotal.getHeight() - dimDiamond2.getHeight();
		return new UTranslate(x2, y2);
	}

	public double computeMarginNeedForBranchLabe1(StringBounder stringBounder, XDimension2D label1) {
		final double widthLabelBranch1 = label1.getWidth();
		final double dxDiamond = getTranslateDiamond1(stringBounder).getDx();
		final double diff = widthLabelBranch1 - dxDiamond;
		if (diff > 0)
			return diff;

		return 0;
	}

	public double computeMarginNeedForBranchLabe2(StringBounder stringBounder, XDimension2D label2) {
		final double widthLabelBranch2 = label2.getWidth();
		final double theoricalEndNeeded = getTranslateDiamond1(stringBounder).getDx()
				+ diamond1.calculateDimension(stringBounder).getWidth() + widthLabelBranch2;
		final double diff = theoricalEndNeeded - calculateDimension(stringBounder).getWidth();
		if (diff > 0)
			return diff;

		return 0;
	}

	public double computeVerticalMarginNeedForBranchs(StringBounder stringBounder, XDimension2D label1,
			XDimension2D label2) {
		final double heightLabels = Math.max(label1.getHeight(), label2.getHeight());
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double dyDiamond = dimDiamond1.getHeight();
		final double diff = heightLabels - dyDiamond;
		if (diff > 0)
			return diff;

		return 0;
	}

}
