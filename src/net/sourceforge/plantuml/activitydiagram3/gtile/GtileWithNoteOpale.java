/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.AlignmentParam;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Parser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.Styleable;
import net.sourceforge.plantuml.svek.image.Opale;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class GtileWithNoteOpale extends AbstractGtile implements Stencil, Styleable {

	private final Gtile tile;
	private final Opale opale;

	private final NotePosition notePosition;
	private final double suppSpace = 20;
	private final Swimlane swimlaneNote;

	private final UTranslate positionNote;
	private final UTranslate positionTile;

	private final Dimension2D dimNote;
	private final Dimension2D dimTile;

	public StyleSignature getDefaultStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.note);
	}

	public GtileWithNoteOpale(Gtile tile, PositionedNote note, ISkinParam skinParam, boolean withLink) {
		super(tile.getStringBounder(), tile.skinParam());
		this.swimlaneNote = note.getSwimlaneNote();
		if (note.getColors() != null) {
			skinParam = note.getColors().mute(skinParam);
		}
		this.tile = tile;
		this.notePosition = note.getNotePosition();
		if (note.getType() == NoteType.FLOATING_NOTE) {
			withLink = false;
		}

		final Rose rose = new Rose();

		final HColor noteBackgroundColor;
		final HColor borderColor;
		final FontConfiguration fc;

		final double shadowing;
		final LineBreakStrategy wrapWidth;
		if (UseStyle.useBetaStyle()) {
			final Style style = getDefaultStyleDefinition().getMergedStyle(skinParam.getCurrentStyleBuilder())
					.eventuallyOverride(note.getColors());
			noteBackgroundColor = style.value(PName.BackGroundColor).asColor(skinParam.getThemeStyle(),
					getIHtmlColorSet());
			borderColor = style.value(PName.LineColor).asColor(skinParam.getThemeStyle(), getIHtmlColorSet());
			fc = style.getFontConfiguration(skinParam.getThemeStyle(), getIHtmlColorSet());
			shadowing = style.value(PName.Shadowing).asDouble();
			wrapWidth = style.wrapWidth();
		} else {
			noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
			borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);
			fc = new FontConfiguration(skinParam, FontParam.NOTE, null);
			shadowing = skinParam.shadowing(null) ? 4 : 0;
			wrapWidth = skinParam.wrapWidth();
		}

		final HorizontalAlignment align = skinParam.getHorizontalAlignment(AlignmentParam.noteTextAlignment, null,
				false, null);
		final Sheet sheet = Parser.build(fc, align, skinParam, CreoleMode.FULL).createSheet(note.getDisplay());
		final TextBlock text = new SheetBlock2(new SheetBlock1(sheet, wrapWidth, skinParam.getPadding()), this,
				new UStroke(1));
		this.opale = new Opale(shadowing, borderColor, noteBackgroundColor, text, withLink);

		this.dimNote = opale.calculateDimension(stringBounder);
		this.dimTile = tile.calculateDimension(stringBounder);

		final Dimension2D dimTotal = calculateDimension(stringBounder);

		if (note.getNotePosition() == NotePosition.LEFT) {
			this.positionNote = new UTranslate(0, (dimTotal.getHeight() - dimNote.getHeight()) / 2);
			this.positionTile = new UTranslate(dimNote.getWidth() + suppSpace,
					(dimTotal.getHeight() - dimTile.getHeight()) / 2);
		} else {
			this.positionNote = new UTranslate(dimTile.getWidth() + suppSpace,
					(dimTotal.getHeight() - dimNote.getHeight()) / 2);
			this.positionTile = new UTranslate(0, (dimTotal.getHeight() - dimTile.getHeight()) / 2);
		}
	}

	@Override
	protected UTranslate getCoordImpl(String name) {
		return tile.getCoord(name).compose(positionTile);
	}

	@Override
	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final double height = Math.max(dimNote.getHeight(), dimTile.getHeight());
		return new Dimension2DDouble(dimTile.getWidth() + dimNote.getWidth() + suppSpace, height);
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		ug.apply(positionNote).draw(opale);
		ug.apply(positionTile).draw(tile);
	}

	@Override
	public double getStartingX(StringBounder stringBounder, double y) {
		return -opale.getMarginX1();
	}

	@Override
	public double getEndingX(StringBounder stringBounder, double y) {
		return opale.calculateDimension(stringBounder).getWidth() - opale.getMarginX1();
	}

}
