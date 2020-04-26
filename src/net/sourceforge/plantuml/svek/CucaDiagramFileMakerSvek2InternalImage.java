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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public final class CucaDiagramFileMakerSvek2InternalImage extends AbstractTextBlock implements IEntityImage {

	private final List<IEntityImage> inners;
	private final Separator separator;
	private final ISkinParam skinParam;
	private final Stereotype stereotype;

	static enum Separator {
		VERTICAL, HORIZONTAL;

		static Separator fromChar(char sep) {
			if (sep == '|') {
				return VERTICAL;
			}
			if (sep == '-') {
				return HORIZONTAL;
			}
			throw new IllegalArgumentException();
		}

		UTranslate move(Dimension2D dim) {
			if (this == VERTICAL) {
				return UTranslate.dx(dim.getWidth());
			}
			return UTranslate.dy(dim.getHeight());
		}

		Dimension2D add(Dimension2D orig, Dimension2D other) {
			if (this == VERTICAL) {
				return new Dimension2DDouble(orig.getWidth() + other.getWidth(), Math.max(orig.getHeight(),
						other.getHeight()));
			}
			return new Dimension2DDouble(Math.max(orig.getWidth(), other.getWidth()), orig.getHeight()
					+ other.getHeight());
		}

		void drawSeparator(UGraphic ug, Dimension2D dimTotal) {
			final double THICKNESS_BORDER = 1.5;
			final int DASH = 8;
			ug = ug.apply(new UStroke(DASH, 10, THICKNESS_BORDER));
			if (this == VERTICAL) {
				ug.draw(ULine.vline(dimTotal.getHeight() + DASH));
			} else {
				ug.draw(ULine.hline(dimTotal.getWidth() + DASH));
			}

		}
	}

	private HColor getColor(ColorParam colorParam, Stereotype stereotype) {
		return new Rose().getHtmlColor(skinParam, stereotype, colorParam);
	}

	public CucaDiagramFileMakerSvek2InternalImage(List<IEntityImage> inners, char concurrentSeparator,
			ISkinParam skinParam, Stereotype stereotype) {
		this.separator = Separator.fromChar(concurrentSeparator);
		this.skinParam = skinParam;
		this.stereotype = stereotype;
		this.inners = inners;
	}

	public void drawU(UGraphic ug) {
		final HColor dotColor = getColor(ColorParam.stateBorder, stereotype);
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		for (int i = 0; i < inners.size(); i++) {
			final IEntityImage inner = inners.get(i);
			inner.drawU(ug);
			final Dimension2D dim = inner.calculateDimension(stringBounder);
			ug = ug.apply(separator.move(dim));
			if (i < inners.size() - 1) {
				separator.drawSeparator(ug.apply(dotColor), dimTotal);
			}
		}

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D result = new Dimension2DDouble(0, 0);
		for (IEntityImage inner : inners) {
			final Dimension2D dim = inner.calculateDimension(stringBounder);
			result = separator.add(result, dim);
		}
		return result;
	}

	public HColor getBackcolor() {
		return skinParam.getBackgroundColor(false);
	}
	
	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}

	public boolean isHidden() {
		return false;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}
}
