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

import java.util.Objects;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicStencil;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.style.SName;

public class USymbolFolder extends USymbol {

	private final static int marginTitleX1 = 3;
	private final static int marginTitleX2 = 3;
	private final static int marginTitleX3 = 7;
	private final static int marginTitleY0 = 0;
	private final static int marginTitleY1 = 3;
	private final static int marginTitleY2 = 3;

	private final SName sname;
	private final boolean showTitle;

	public USymbolFolder(SName sname, boolean showTitle) {
		this.showTitle = showTitle;
		this.sname = sname;
	}

	@Override
	public String toString() {
		return super.toString() + " " + showTitle;
	}

	@Override
	public SName[] getSNames() {
		return new SName[] { sname };
	}

	private void drawFolder(UGraphic ug, double width, double height, XDimension2D dimName, double shadowing,
			double roundCorner) {

		final double wtitle = getWTitle(width, dimName);
		final double htitle = getHTitle(dimName);

		final Shadowable shape;
		if (roundCorner == 0) {
			final UPolygon poly = new UPolygon();
			poly.addPoint(0, 0);
			poly.addPoint(wtitle, 0);

			poly.addPoint(wtitle + marginTitleX3, htitle);
			poly.addPoint(width, htitle);
			poly.addPoint(width, height);
			poly.addPoint(0, height);
			poly.addPoint(0, 0);
			shape = poly;
		} else {
			final UPath path = UPath.none();
			path.moveTo(roundCorner / 2, 0);
			path.lineTo(wtitle - roundCorner / 2, 0);
			// path.lineTo(wtitle, roundCorner / 2);
			path.arcTo(new XPoint2D(wtitle, roundCorner / 2), roundCorner / 2 * 1.5, 0, 1);
			path.lineTo(wtitle + marginTitleX3, htitle);
			path.lineTo(width - roundCorner / 2, htitle);
			path.arcTo(new XPoint2D(width, htitle + roundCorner / 2), roundCorner / 2, 0, 1);
			path.lineTo(width, height - roundCorner / 2);
			path.arcTo(new XPoint2D(width - roundCorner / 2, height), roundCorner / 2, 0, 1);
			path.lineTo(roundCorner / 2, height);
			path.arcTo(new XPoint2D(0, height - roundCorner / 2), roundCorner / 2, 0, 1);
			path.lineTo(0, roundCorner / 2);
			path.arcTo(new XPoint2D(roundCorner / 2, 0), roundCorner / 2, 0, 1);
			path.closePath();
			shape = path;
		}
		shape.setDeltaShadow(shadowing);

		ug.draw(shape);
		ug.apply(UTranslate.dy(htitle)).draw(ULine.hline(wtitle + marginTitleX3));
	}

	private double getWTitle(double width, XDimension2D dimTitle) {
		final double wtitle;
		if (dimTitle.getWidth() == 0)
			wtitle = Math.max(30, width / 4);
		else
			wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
		return wtitle;
	}

	private double getHTitle(XDimension2D dimTitle) {
		final double htitle;
		if (dimTitle.getWidth() == 0)
			htitle = 10;
		else
			htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;

		return htitle;
	}

	private Margin getMargin() {
		return new Margin(10, 10 + 10, 10 + 3, 10);
	}

	@Override
	public TextBlock asSmall(final TextBlock title, final TextBlock label, final TextBlock stereotype,
			final Fashion symbolContext, final HorizontalAlignment stereoAlignment) {
		Objects.requireNonNull(title);
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final XDimension2D dim = calculateDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, dim);
				ug = symbolContext.apply(ug);
				final XDimension2D dimTitle = getDimTitle(ug.getStringBounder());
				drawFolder(ug, dim.getWidth(), dim.getHeight(), dimTitle, symbolContext.getDeltaShadow(),
						symbolContext.getRoundCorner());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
				if (showTitle)
					title.drawU(ug.apply(new UTranslate(4, 3)));

				tb.drawU(ug.apply(new UTranslate(margin.getX1(), margin.getY1() + dimTitle.getHeight())));
			}

			private XDimension2D getDimTitle(StringBounder stringBounder) {
				return showTitle ? title.calculateDimension(stringBounder) : new XDimension2D(40, 15);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D dimName = getDimTitle(stringBounder);
				final XDimension2D dimLabel = label.calculateDimension(stringBounder);
				final XDimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(dimName.mergeTB(dimStereo, dimLabel));
			}

			@Override
			public MagneticBorder getMagneticBorder() {
				return new MagneticBorder() {

					@Override
					public UTranslate getForceAt(StringBounder stringBounder, XPoint2D position) {
						final XDimension2D dim = calculateDimension(stringBounder);
						final XDimension2D dimTitle = getDimTitle(stringBounder);
						final double wtitle = getWTitle(dim.getWidth(), dimTitle);
						final double htitle = getHTitle(dimTitle);

						if (position.getX() >= wtitle && position.getY() >= 0 && position.getY() <= htitle)
							return new UTranslate(0, htitle);

						if (position.getY() <= 0 && position.getX() >= wtitle + marginTitleX3)
							return new UTranslate(0, htitle);

						if (position.getY() <= 0 && position.getX() >= wtitle - marginTitleX3) {
							final double delta = position.getX() - (wtitle - marginTitleX3);
							final double how = delta / (2 * marginTitleX3);
							return new UTranslate(0, htitle * how);
						}

						return UTranslate.none();
					}
				};

			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final Fashion symbolContext,
			final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final XDimension2D dim = calculateDimension(stringBounder);
				ug = symbolContext.apply(ug);
				final XDimension2D dimTitle = title.calculateDimension(stringBounder);
				drawFolder(ug, dim.getWidth(), dim.getHeight(), dimTitle, symbolContext.getDeltaShadow(),
						symbolContext.getRoundCorner());
				title.drawU(ug.apply(new UTranslate(4, 2)));
				final XDimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				final double posStereo = (width - dimStereo.getWidth()) / 2;

				stereotype.drawU(ug.apply(new UTranslate(4 + posStereo, 2 + getHTitle(dimTitle))));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(width, height);
			}

			@Override
			public MagneticBorder getMagneticBorder() {
				return new MagneticBorder() {

					@Override
					public UTranslate getForceAt(StringBounder stringBounder, XPoint2D position) {
						final XDimension2D dim = calculateDimension(stringBounder);
						final XDimension2D dimTitle = title.calculateDimension(stringBounder);
						final double wtitle = getWTitle(dim.getWidth(), dimTitle);
						final double htitle = getHTitle(dimTitle);

						if (position.getX() >= wtitle && position.getY() >= 0 && position.getY() <= htitle)
							return new UTranslate(0, htitle);

						if (position.getY() <= 0 && position.getX() >= wtitle + marginTitleX3)
							return new UTranslate(0, htitle);

						if (position.getY() <= 0 && position.getX() >= wtitle - marginTitleX3) {
							final double delta = position.getX() - (wtitle - marginTitleX3);
							final double how = delta / (2 * marginTitleX3);
							return new UTranslate(0, htitle * how);
						}
						return UTranslate.none();
					}
				};

			}

		};
	}

}
