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
package net.sourceforge.plantuml.decoration.symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicStencil;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.CoordinateChange;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.style.SName;

class USymbolCloud extends USymbol {

	private final static boolean NEW = true;
	private final static boolean DEBUG = false;

	@Override
	public SName[] getSNames() {
		return new SName[] { SName.cloud };
	}

	private void drawCloud(UGraphic ug, double width, double height, double shadowing) {
		final UPath shape = getSpecificFrontierForCloud(width, height);
		shape.setDeltaShadow(shadowing);

		ug.apply(UTranslate.dy(0)).draw(shape);
	}

	private UPath getSpecificFrontierForCloudNew(double width, double height) {
		final Random rnd = new Random((long) width + 7919L * (long) height);
		final List<XPoint2D> points = new ArrayList<>();

		double bubbleSize = 11;
		if (Math.max(width, height) / bubbleSize > 16) {
			bubbleSize = Math.max(width, height) / 16;
		}

		final double margin1 = 8;

		final XPoint2D pointA = new XPoint2D(margin1, margin1);
		final XPoint2D pointB = new XPoint2D(width - margin1, margin1);
		final XPoint2D pointC = new XPoint2D(width - margin1, height - margin1);
		final XPoint2D pointD = new XPoint2D(margin1, height - margin1);

		if (width > 100 && height > 100) {
			complex(rnd, points, bubbleSize, pointA, pointB, pointC, pointD);
		} else {
			simple(rnd, points, bubbleSize, pointA, pointB, pointC, pointD);
		}

		points.add(points.get(0));

		final UPath result = UPath.none();
		result.moveTo(points.get(0));
		for (int i = 0; i < points.size() - 1; i++) {
			if (DEBUG) {
				result.lineTo(points.get(i + 1));
			} else {
				addCurve(rnd, result, points.get(i), points.get(i + 1));
			}
		}
		return result;

	}

	private void complex(final Random rnd, final List<XPoint2D> points, double bubbleSize, final XPoint2D pointA,
			final XPoint2D pointB, final XPoint2D pointC, final XPoint2D pointD) {
		final double margin2 = 7;
		specialLine(bubbleSize, rnd, points, mvX(pointA, margin2), mvX(pointB, -margin2));
		points.add(mvY(pointB, margin2));
		specialLine(bubbleSize, rnd, points, mvY(pointB, margin2), mvY(pointC, -margin2));
		points.add(mvX(pointC, -margin2));
		specialLine(bubbleSize, rnd, points, mvX(pointC, -margin2), mvX(pointD, margin2));
		points.add(mvY(pointD, -margin2));
		specialLine(bubbleSize, rnd, points, mvY(pointD, -margin2), mvY(pointA, margin2));
		points.add(mvX(pointA, margin2));
	}

	private void simple(final Random rnd, final List<XPoint2D> points, double bubbleSize, final XPoint2D pointA,
			final XPoint2D pointB, final XPoint2D pointC, final XPoint2D pointD) {
		specialLine(bubbleSize, rnd, points, pointA, pointB);
		specialLine(bubbleSize, rnd, points, pointB, pointC);
		specialLine(bubbleSize, rnd, points, pointC, pointD);
		specialLine(bubbleSize, rnd, points, pointD, pointA);
	}

	private static XPoint2D mvX(XPoint2D pt, double dx) {
		return new XPoint2D(pt.getX() + dx, pt.getY());
	}

	private static XPoint2D mvY(XPoint2D pt, double dy) {
		return new XPoint2D(pt.getX(), pt.getY() + dy);
	}

	private void specialLine(double bubbleSize, Random rnd, List<XPoint2D> points, XPoint2D p1, XPoint2D p2) {
		final CoordinateChange change = CoordinateChange.create(p1, p2);
		final double length = change.getLength();
		final XPoint2D middle = change.getTrueCoordinate(length / 2, -rnd(rnd, 1, 1 + Math.min(12, bubbleSize * 0.8)));
		// final XPoint2D middle = change.getTrueCoordinate(length / 2, -13);
		if (DEBUG) {
			points.add(middle);
			points.add(p2);
		} else {
			bubbleLine(rnd, points, p1, middle, bubbleSize);
			bubbleLine(rnd, points, middle, p2, bubbleSize);
		}
	}

	private void bubbleLine(Random rnd, List<XPoint2D> points, XPoint2D p1, XPoint2D p2, double bubbleSize) {
		final CoordinateChange change = CoordinateChange.create(p1, p2);
		final double length = change.getLength();
		int nb = (int) (length / bubbleSize);
		if (nb == 0) {
			bubbleSize = length / 2;
			nb = (int) (length / bubbleSize);
		}
		for (int i = 0; i < nb; i++) {
			points.add(rnd(rnd, change.getTrueCoordinate(i * length / nb, 0), bubbleSize * .2));
		}
	}

	private void addCurve(Random rnd, UPath path, XPoint2D p1, XPoint2D p2) {
		final CoordinateChange change = CoordinateChange.create(p1, p2);
		final double length = change.getLength();
		final double coef = rnd(rnd, .25, .35);
		final XPoint2D middle = change.getTrueCoordinate(length * coef, -length * rnd(rnd, .4, .55));
		final XPoint2D middle2 = change.getTrueCoordinate(length * (1 - coef), -length * rnd(rnd, .4, .55));
		path.cubicTo(middle, middle2, p2);

	}

	static private double rnd(Random rnd, double a, double b) {
		return rnd.nextDouble() * (b - a) + a;
	}

	static private XPoint2D rnd(Random rnd, XPoint2D pt, double v) {
		final double x = pt.getX() + v * rnd.nextDouble();
		final double y = pt.getY() + v * rnd.nextDouble();
		return new XPoint2D(x, y);
	}

	private UPath getSpecificFrontierForCloud(double width, double height) {
		if (NEW) {
			return getSpecificFrontierForCloudNew(width, height);
		}
		final UPath path = UPath.none();
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

	private Margin getMargin() {
		if (NEW)
			return new Margin(15, 15, 15, 15);
		return new Margin(10, 10, 10, 10);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final Fashion symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final XDimension2D dim = calculateDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, dim);
				ug = symbolContext.apply(ug);
				drawCloud(ug, dim.getWidth(), dim.getHeight(), symbolContext.getDeltaShadow());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
				tb.drawU(ug.apply(new UTranslate(margin.getX1(), margin.getY1())));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D dimLabel = label.calculateDimension(stringBounder);
				final XDimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(dimStereo.mergeTB(dimLabel));
			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final Fashion symbolContext,
			final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final XDimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawCloud(ug, dim.getWidth(), dim.getHeight(), symbolContext.getDeltaShadow());
				final XDimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
				final double posStereo = (width - dimStereo.getWidth()) / 2;
				stereotype.drawU(ug.apply(new UTranslate(posStereo, 13)));
				final XDimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug.apply(new UTranslate(posTitle, 13 + dimStereo.getHeight())));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(width, height);
			}
		};
	}

}