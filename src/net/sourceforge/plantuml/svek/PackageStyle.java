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
 * Modified by : Arno Peterson
 *
 * 
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.EnumSet;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public enum PackageStyle {

	FOLDER, RECTANGLE, NODE, FRAME, CLOUD, DATABASE, AGENT, STORAGE, COMPONENT1, COMPONENT2, ARTIFACT, CARD;

	public static PackageStyle fromString(String value) {
		for (PackageStyle p : EnumSet.allOf(PackageStyle.class)) {
			if (p.toString().equalsIgnoreCase(value)) {
				return p;
			}
		}
		if ("rect".equalsIgnoreCase(value)) {
			return RECTANGLE;
		}
		return null;
	}

	public USymbol toUSymbol() {
		if (this == NODE) {
			return USymbol.NODE;
		}
		if (this == CARD) {
			return USymbol.CARD;
		}
		if (this == DATABASE) {
			return USymbol.DATABASE;
		}
		if (this == CLOUD) {
			return USymbol.CLOUD;
		}
		if (this == FRAME) {
			return USymbol.FRAME;
		}
		if (this == RECTANGLE) {
			return USymbol.RECTANGLE;
		}
		if (this == FOLDER) {
			return USymbol.FOLDER;
		}
		return null;
	}

	public void drawU(UGraphic ug, Dimension2D dim, Dimension2D titleDim, boolean shadowing) {
		if (titleDim == null) {
			titleDim = new Dimension2DDouble(0, 0);
		}
		final double width = dim.getWidth();
		final double height = dim.getHeight();
		if (this == DATABASE) {
			drawDatabase(ug, width, height, shadowing);
		} else if (this == FOLDER) {
			drawFolder(ug, width, height, shadowing);
		} else if (this == FRAME) {
			drawFrame(ug, width, height, titleDim, shadowing);
		} else if (this == CLOUD) {
			drawCloud(ug, width, height, shadowing);
		} else if (this == RECTANGLE) {
			drawRect(ug, width, height, shadowing);
		} else if (this == COMPONENT1) {
			drawComponent1(ug, width, height, shadowing);
		} else if (this == COMPONENT2) {
			drawComponent2(ug, width, height, shadowing);
		} else if (this == STORAGE) {
			drawStorage(ug, width, height, shadowing);
		} else if (this == AGENT) {
			drawRect(ug, width, height, shadowing);
		} else if (this == ARTIFACT) {
			drawArtifact(ug, width, height, shadowing);
		} else {
			// drawNode(ug, xTheoricalPosition, yTheoricalPosition, width, height, shadowing);
			throw new UnsupportedOperationException();
		}
	}

	private void drawArtifact(UGraphic ug, double width, double height, boolean shadowing) {

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, height);
		polygon.addPoint(width, height);
		final int cornersize = 10;
		polygon.addPoint(width, cornersize);
		polygon.addPoint(width - cornersize, 0);
		polygon.addPoint(0, 0);
		if (shadowing) {
			polygon.setDeltaShadow(3.0);
		}
		ug.draw(polygon);
		ug.apply(UTranslate.dx(width - cornersize)).draw(ULine.vline(cornersize));
		ug.apply(new UTranslate(width, cornersize)).draw(ULine.hline(-cornersize));
	}

	private void drawStorage(UGraphic ug, double width, double height, boolean shadowing) {
		final URectangle shape = new URectangle(width, height).rounded(70);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		ug.draw(shape);
	}

	private void drawComponent1(UGraphic ug, double widthTotal, double heightTotal, boolean shadowing) {

		final URectangle form = new URectangle(widthTotal, heightTotal);
		if (shadowing) {
			form.setDeltaShadow(4);
		}

		final UShape small = new URectangle(10, 5);

		ug.draw(form);

		// UML 1 Component Notation
		ug.apply(new UTranslate(-5, 5)).draw(small);
		ug.apply(new UTranslate(-5, heightTotal - 10)).draw(small);
	}

	private void drawComponent2(UGraphic ug, double widthTotal, double heightTotal, boolean shadowing) {

		final URectangle form = new URectangle(widthTotal, heightTotal);
		if (shadowing) {
			form.setDeltaShadow(4);
		}

		final UShape small = new URectangle(15, 10);
		final UShape tiny = new URectangle(4, 2);

		ug.draw(form);

		// UML 2 Component Notation
		ug.apply(new UTranslate(widthTotal - 20, 5)).draw(small);
		ug.apply(new UTranslate(widthTotal - 22, 7)).draw(tiny);
		ug.apply(new UTranslate(widthTotal - 22, 11)).draw(tiny);
	}

	private void drawRect(UGraphic ug, double width, double height, boolean shadowing) {
		final URectangle shape = new URectangle(width, height);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		ug.draw(shape);
	}

	private void drawCloud(UGraphic ug, double width, double height, boolean shadowing) {
		final UPath shape = getSpecificFrontierForCloud(width, height);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		ug.apply(new UTranslate(3, -3)).draw(shape);
	}

	private UPath getSpecificFrontierForCloud(double width, double height) {
		final UPath path = new UPath();
		path.moveTo(0, 10);
		double x = 0;
		for (int i = 0; i < width - 9; i += 10) {
			path.cubicTo(i, -3 + 10, 2 + i, -5 + 10, 5 + i, -5 + 10);
			path.cubicTo(8 + i, -5 + 10, 10 + i, -3 + 10, 10 + i, 10);
			x = i + 10;
		}
		double y = 0;
		for (int j = 10; j < height - 9; j += 10) {
			path.cubicTo(x + 3, j, x + 5, 2 + j, x + 5, 5 + j);
			path.cubicTo(x + 5, 8 + j, x + 3, 10 + j, x, 10 + j);
			y = j + 10;
		}
		for (int i = 0; i < width - 9; i += 10) {
			path.cubicTo(x - i, y + 3, x - 3 - i, y + 5, x - 5 - i, y + 5);
			path.cubicTo(x - 8 - i, y + 5, x - 10 - i, y + 3, x - 10 - i, y);
		}
		for (int j = 0; j < height - 9 - 10; j += 10) {
			path.cubicTo(-3, y - j, -5, y - 2 - j, -5, y - 5 - j);
			path.cubicTo(-5, y - 8 - j, -3, y - 10 - j, 0, y - 10 - j);
		}
		return path;
	}

	private void drawFrame(UGraphic ug, double width, double height, Dimension2D dimTitle, boolean shadowing) {
		final URectangle shape = new URectangle(width, height);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}

		ug.draw(shape);

		final double textWidth;
		final double textHeight;
		final int cornersize;
		if (dimTitle.getWidth() == 0) {
			textWidth = width / 3;
			textHeight = 12;
			cornersize = 7;
		} else {
			textWidth = dimTitle.getWidth() + 10;
			textHeight = dimTitle.getHeight() + 3;
			cornersize = 10;
		}

		final UPath polygon = new UPath();
		polygon.moveTo(textWidth, 1);

		polygon.lineTo(textWidth, textHeight - cornersize);
		polygon.lineTo(textWidth - cornersize, textHeight);

		polygon.lineTo(0, textHeight);
		ug.draw(polygon);

	}

	private void drawFolder(UGraphic ug, double width, double height, boolean shadowing) {
		final double wtitle = Math.max(30, width / 4);
		final UPolygon shape = new UPolygon();
		shape.addPoint(0, 0);
		shape.addPoint(wtitle, 0);
		final double htitle = 10;
		final double marginTitleX3 = 7;
		shape.addPoint(wtitle + marginTitleX3, htitle);
		shape.addPoint(width, htitle);
		shape.addPoint(width, height);
		shape.addPoint(0, height);
		shape.addPoint(0, 0);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		ug.draw(shape);
		ug.apply(UTranslate.dy(htitle)).draw(ULine.hline(wtitle + marginTitleX3));
	}

	private void drawDatabase(UGraphic ug, double width, double height, boolean shadowing) {
		final UPath shape = new UPath();
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		shape.moveTo(0, 10);
		shape.cubicTo(10, 0, width / 2 - 10, 0, width / 2, 0);
		shape.cubicTo(width / 2 + 10, 0, width - 10, 0, width, 10);
		shape.lineTo(width, height - 10);
		shape.cubicTo(width - 10, height, width / 2 - 10, height, width / 2, height);
		shape.cubicTo(width / 2 + 10, height, 10, height, 0, height - 10);
		shape.lineTo(0, 10);

		ug.draw(shape);

		final UPath closing = new UPath();
		closing.moveTo(0, 10);
		closing.cubicTo(10, 20, width / 2 - 10, 20, width / 2, 20);
		closing.cubicTo(width / 2 + 10, 20, width - 10, 20, width, 10);

		ug.draw(closing);

	}

	private void drawNode(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double width,
			double height, boolean shadowing) {
		final UPolygon shape = new UPolygon();
		shape.addPoint(0, 10);
		shape.addPoint(10, 0);
		shape.addPoint(width, 0);
		shape.addPoint(width, height - 10);
		shape.addPoint(width - 10, height);
		shape.addPoint(0, height);
		shape.addPoint(0, 10);
		if (shadowing) {
			shape.setDeltaShadow(2);
		}
		ug.apply(new UTranslate(xTheoricalPosition, yTheoricalPosition)).draw(shape);

		ug.apply(new UTranslate(xTheoricalPosition + width - 10, yTheoricalPosition + 10)).draw(new ULine(9, -9));
		final UPath path = new UPath();
		path.moveTo(0, 0);
		path.lineTo(width - 10, 0);
		path.lineTo(width - 10, height - 10);
		ug.apply(new UTranslate(xTheoricalPosition, yTheoricalPosition + 10)).draw(path);
	}

}