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
 * Modified by : Arno Peterson
 * 
 * 
 */
package net.sourceforge.plantuml.svek;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ClusterDecoration {

	private final UStroke defaultStroke;// = new UStroke(2);
	final private USymbol symbol;
	final private TextBlock title;
	final private TextBlock stereo;

	final private double minX;
	final private double minY;
	final private double maxX;
	final private double maxY;

	public ClusterDecoration(PackageStyle style, USymbol symbol, TextBlock title, TextBlock stereo, double minX,
			double minY, double maxX, double maxY, UStroke stroke) {
		this.symbol = guess(symbol, style);
		this.stereo = stereo;
		this.title = title;
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		this.defaultStroke = stroke;
		// if (stateBack instanceof HtmlColorTransparent) {
		// throw new UnsupportedOperationException();
		// }
	}

	private static USymbol guess(USymbol symbol, PackageStyle style) {
		if (symbol != null) {
			return symbol;
		}
		return style.toUSymbol();
	}

	public final static int marginTitleX1 = 3;
	public final static int marginTitleX2 = 3;
	public final static int marginTitleX3 = 7;
	public final static int marginTitleY0 = 0;
	public final static int marginTitleY1 = 3;
	public final static int marginTitleY2 = 3;

	public void drawU(UGraphic ug, HtmlColor backColor, HtmlColor borderColor, boolean shadowing) {
		final SymbolContext biColor = new SymbolContext(backColor, borderColor);
		if (symbol == null) {
			throw new UnsupportedOperationException();
		}
		final SymbolContext symbolContext = biColor.withShadow(shadowing).withStroke(defaultStroke);
		symbol.asBig(title, stereo, maxX - minX, maxY - minY, symbolContext)
				.drawU(ug.apply(new UTranslate(minX, minY)));
		// return;
		// }
		// if (style == PackageStyle.NODE) {
		// drawWithTitleNode(ug, biColor, shadowing);
		// } else if (style == PackageStyle.CARD) {
		// drawWithTitleCard(ug, biColor, shadowing);
		// } else if (style == PackageStyle.DATABASE) {
		// drawWithTitleDatabase(ug, biColor, shadowing);
		// } else if (style == PackageStyle.CLOUD) {
		// drawWithTitleCloud(ug, biColor, shadowing);
		// } else if (style == PackageStyle.FRAME) {
		// drawWithTitleFrame(ug, biColor, shadowing);
		// } else if (style == PackageStyle.RECT) {
		// drawWithTitleRect(ug, biColor, shadowing);
		// } else {
		// throw new UnsupportedOperationException();
		// // drawWithTitleFolder(ug, biColor, shadowing);
		// }
	}

	// // Cloud
	// private void drawWithTitleCloud(UGraphic ug, SymbolContext biColor, boolean shadowing) {
	// final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
	// final double width = maxX - minX;
	// final double height = maxY - minY;
	// ug = biColor.applyColors(ug);
	// ug = ug.apply(defaultStroke);
	// PackageStyle.CLOUD.drawU(ug.apply(new UTranslate(minX, minY)), new Dimension2DDouble(width, height), dimTitle,
	// shadowing);
	// ug = ug.apply(new UStroke());
	// title.drawU(ug.apply(new UTranslate(minX + (width - dimTitle.getWidth()) / 2, minY + 10)));
	//
	// }
	//
	// // Database
	// private void drawWithTitleDatabase(UGraphic ug, SymbolContext biColor, boolean shadowing) {
	// final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
	// final double width = maxX - minX;
	// final double height = maxY - minY;
	// ug = ug.apply(defaultStroke);
	// ug = biColor.applyColors(ug);
	// PackageStyle.DATABASE.drawU(ug.apply(new UTranslate(minX, minY - 10)),
	// new Dimension2DDouble(width, height + 10), dimTitle, shadowing);
	// ug = ug.apply(new UStroke());
	// title.drawU(ug.apply(new UTranslate(minX + marginTitleX1, minY + 10)));
	//
	// }
	//
	// // Corner
	// private void drawWithTitleFrame(UGraphic ug, SymbolContext biColor, boolean shadowing) {
	// final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
	// final double width = maxX - minX;
	// final double height = maxY - minY;
	// ug = biColor.applyColors(ug);
	// ug = ug.apply(defaultStroke);
	// PackageStyle.FRAME.drawU(ug.apply(new UTranslate(minX, minY)), new Dimension2DDouble(width, height), dimTitle,
	// shadowing);
	// ug = ug.apply(new UStroke());
	// title.drawU(ug.apply(new UTranslate(minX + marginTitleX1, minY)));
	//
	// }
	//
	// // Card
	// private void drawWithTitleCard(UGraphic ug, SymbolContext biColor, boolean shadowing) {
	// final double width = maxX - minX;
	// final double height = maxY - minY;
	// final SymbolContext ctx = biColor.withStroke(defaultStroke).withShadow(shadowing);
	// USymbol.CARD.asBig(title, TextBlockUtils.empty(0, 0), width + 10, height, ctx).drawU(
	// ug.apply(new UTranslate(minX, minY)));
	// }
	//
	// // Node
	// private void drawWithTitleNode(UGraphic ug, SymbolContext biColor, boolean shadowing) {
	// final double width = maxX - minX;
	// final double height = maxY - minY;
	// final SymbolContext ctx = biColor.withStroke(defaultStroke).withShadow(shadowing);
	// USymbol.NODE.asBig(title, TextBlockUtils.empty(0, 0), width + 10, height, ctx).drawU(
	// ug.apply(new UTranslate(minX, minY)));
	// }
	//
	// // Folder
	// private UPolygon getSpecificFrontierForFolder(StringBounder stringBounder) {
	// final double width = maxX - minX;
	// final double height = maxY - minY;
	// final Dimension2D dimTitle = title.calculateDimension(stringBounder);
	// final double wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
	// final double htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;
	// final UPolygon shape = new UPolygon();
	// shape.addPoint(0, 0);
	// shape.addPoint(wtitle, 0);
	// shape.addPoint(wtitle + marginTitleX3, htitle);
	// shape.addPoint(width, htitle);
	// shape.addPoint(width, height);
	// shape.addPoint(0, height);
	// shape.addPoint(0, 0);
	// return shape;
	// }
	//
	// private void drawWithTitleFolder(UGraphic ug, SymbolContext biColor, boolean shadowing) {
	// final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
	// final double wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
	// final double htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;
	// final UPolygon shape = getSpecificFrontierForFolder(ug.getStringBounder());
	// if (shadowing) {
	// shape.setDeltaShadow(3.0);
	// }
	//
	// ug = biColor.applyColors(ug);
	// ug = ug.apply(defaultStroke);
	// ug.apply(new UTranslate(minX, minY)).draw(shape);
	// ug.apply(new UTranslate(minX, minY + htitle)).draw(new ULine(wtitle + marginTitleX3, 0));
	// ug = ug.apply(new UStroke());
	// title.drawU(ug.apply(new UTranslate(minX + marginTitleX1, minY + marginTitleY1)));
	// }
	//
	// // Rect
	// private void drawWithTitleRect(UGraphic ug, SymbolContext biColor, boolean shadowing) {
	// final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
	// final double width = maxX - minX;
	// final double height = maxY - minY;
	// final URectangle shape = new URectangle(width, height);
	// if (shadowing) {
	// shape.setDeltaShadow(3.0);
	// }
	//
	// ug = biColor.applyColors(ug);
	// ug = ug.apply(defaultStroke);
	//
	// ug.apply(new UTranslate(minX, minY)).draw(shape);
	// ug = ug.apply(new UStroke());
	// final double deltax = width - dimTitle.getWidth();
	// title.drawU(ug.apply(new UTranslate(minX + deltax / 2, minY + 5)));
	// }

}
