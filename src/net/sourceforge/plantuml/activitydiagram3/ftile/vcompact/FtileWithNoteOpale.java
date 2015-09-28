/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
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
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.image.Opale;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileWithNoteOpale extends AbstractFtile implements Stencil {

	private final Ftile tile;
	private final Opale opale;

	// private final HtmlColor arrowColor;
	private final NotePosition notePosition;
	private final double suppSpace = 20;

	public Set<Swimlane> getSwimlanes() {
		return tile.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return tile.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return tile.getSwimlaneOut();
	}

	public FtileWithNoteOpale(Ftile tile, Display note, NotePosition notePosition, ISkinParam skinParam,
			boolean withLink) {
		super(tile.shadowing());
		this.tile = tile;
		this.notePosition = notePosition;
		// this.arrowColor = arrowColor;

		final Rose rose = new Rose();
		// final HtmlColor fontColor = rose.getFontColor(skinParam, FontParam.NOTE);
		// final UFont fontNote = skinParam.getFont(FontParam.NOTE, null, false);

		final HtmlColor noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);

		// final FontConfiguration fc = new FontConfiguration(fontNote, fontColor, skinParam.getHyperlinkColor(),
		// skinParam.useUnderlineForHyperlink());
		final FontConfiguration fc = new FontConfiguration(skinParam, FontParam.NOTE, null);

		final Sheet sheet = new CreoleParser(fc, HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL)
				.createSheet(note);
		final TextBlock text = new SheetBlock2(new SheetBlock1(sheet, 0, skinParam.getPadding()), this, new UStroke(1));
		opale = new Opale(borderColor, noteBackgroundColor, text, skinParam.shadowing(), withLink);

	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final double yForFtile = (dimTotal.getHeight() - dimTile.getHeight()) / 2;
		final double marge;
		if (notePosition == NotePosition.LEFT) {
			marge = dimNote.getWidth() + suppSpace;
		} else {
			marge = 0;
		}

		return new UTranslate(marge, yForFtile);

	}

	private UTranslate getTranslateForOpale(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);

		final double yForNote = (dimTotal.getHeight() - dimNote.getHeight()) / 2;

		if (notePosition == NotePosition.LEFT) {
			return new UTranslate(0, yForNote);
		}
		final double dx = dimTotal.getWidth() - dimNote.getWidth();
		return new UTranslate(dx, yForNote);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);

		if (notePosition == NotePosition.LEFT) {
			final Direction strategy = Direction.RIGHT;
			final Point2D pp1 = new Point2D.Double(dimNote.getWidth(), dimNote.getHeight() / 2);
			final Point2D pp2 = new Point2D.Double(dimNote.getWidth() + suppSpace, dimNote.getHeight() / 2);
			opale.setOpale(strategy, pp1, pp2);
		} else {
			final Direction strategy = Direction.LEFT;
			final Point2D pp1 = new Point2D.Double(0, dimNote.getHeight() / 2);
			final Point2D pp2 = new Point2D.Double(-suppSpace, dimNote.getHeight() / 2);
			opale.setOpale(strategy, pp1, pp2);
		}
		opale.drawU(ug.apply(getTranslateForOpale(ug)));
		ug.apply(getTranslate(stringBounder)).draw(tile);
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry orig = tile.calculateDimension(stringBounder);
		final UTranslate translate = getTranslate(stringBounder);
		if (orig.hasPointOut()) {
			return new FtileGeometry(dimTotal, orig.getLeft() + translate.getDx(), orig.getInY() + translate.getDy(),
					orig.getOutY() + translate.getDy());
		}
		return new FtileGeometry(dimTotal, orig.getLeft() + translate.getDx(), orig.getInY() + translate.getDy());
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final double height = Math.max(dimNote.getHeight(), dimTile.getHeight());
		return new Dimension2DDouble(dimTile.getWidth() + 1 * dimNote.getWidth() + suppSpace, height);
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return -opale.getMarginX1();
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return opale.calculateDimension(stringBounder).getWidth() - opale.getMarginX1();
	}

}
