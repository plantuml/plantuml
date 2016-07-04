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
import java.util.Collection;
import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.image.Opale;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class FtileWithNotes extends AbstractFtile /* implements Stencil */{

	private final Ftile tile;

	private TextBlock left;
	private TextBlock right;

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

	public FtileWithNotes(Ftile tile, Collection<PositionedNote> notes, ISkinParam skinParam) {
		super(tile.skinParam());
		// if (note.getColors() != null) {
		// skinParam = note.getColors().mute(skinParam);
		// }
		this.tile = tile;

		final Rose rose = new Rose();

		final HtmlColor noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);

		final FontConfiguration fc = new FontConfiguration(skinParam, FontParam.NOTE, null);

		for (PositionedNote note : notes) {
			final Sheet sheet = new CreoleParser(fc, HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL)
					.createSheet(note.getDisplay());
			final SheetBlock1 sheet1 = new SheetBlock1(sheet, 0, skinParam.getPadding());

			final TextBlock opale = TextBlockUtils.withMargin(new Opale(borderColor, noteBackgroundColor, sheet1,
					skinParam.shadowing(), false), 10, 10);
			if (note.getNotePosition() == NotePosition.LEFT) {
				if (left == null) {
					left = opale;
				} else {
					left = TextBlockUtils.mergeTB(left, opale, HorizontalAlignment.CENTER);
				}
			} else {
				if (right == null) {
					right = opale;
				} else {
					right = TextBlockUtils.mergeTB(right, opale, HorizontalAlignment.CENTER);
				}
			}
		}

		if (left == null) {
			left = TextBlockUtils.empty(0, 0);
		}
		if (right == null) {
			right = TextBlockUtils.empty(0, 0);
		}

	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final double xDelta = left.calculateDimension(stringBounder).getWidth();
		final double yDelta = (dimTotal.getHeight() - dimTile.getHeight()) / 2;
		return new UTranslate(xDelta, yDelta);
	}

	private UTranslate getTranslateForLeft(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimLeft = left.calculateDimension(stringBounder);
		final double xDelta = 0;
		final double yDelta = (dimTotal.getHeight() - dimLeft.getHeight()) / 2;
		return new UTranslate(xDelta, yDelta);
	}

	private UTranslate getTranslateForRight(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimRight = right.calculateDimension(stringBounder);
		final double xDelta = dimTotal.getWidth() - dimRight.getWidth();
		final double yDelta = (dimTotal.getHeight() - dimRight.getHeight()) / 2;
		return new UTranslate(xDelta, yDelta);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug.apply(getTranslateForLeft(stringBounder)).draw(left);
		ug.apply(getTranslateForRight(stringBounder)).draw(right);
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
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final Dimension2D dimLeft = left.calculateDimension(stringBounder);
		final Dimension2D dimRight = right.calculateDimension(stringBounder);
		final double height = MathUtils.max(dimLeft.getHeight(), dimRight.getHeight(), dimTile.getHeight());
		return new Dimension2DDouble(dimTile.getWidth() + dimLeft.getWidth() + dimRight.getWidth(), height);
	}

	// public double getStartingX(StringBounder stringBounder, double y) {
	// return -opale.getMarginX1();
	// }
	//
	// public double getEndingX(StringBounder stringBounder, double y) {
	// return opale.calculateDimension(stringBounder).getWidth() - opale.getMarginX1();
	// }

}
