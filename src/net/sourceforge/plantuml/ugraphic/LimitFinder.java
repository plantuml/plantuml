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
package net.sourceforge.plantuml.ugraphic;

import static net.sourceforge.plantuml.utils.ObjectUtils.instanceOfAny;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.activitydiagram3.ftile.CenteredText;
import net.sourceforge.plantuml.graphic.SpecialText;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class LimitFinder extends UGraphicNo {

	@Override
	public UGraphic apply(UChange change) {
		return new LimitFinder(this, change);
	}

	private final MinMaxMutable minmax;
	private final UClip clip;

	public LimitFinder(StringBounder stringBounder, boolean initToZero) {
		super(stringBounder);
		this.minmax = MinMaxMutable.getEmpty(initToZero);
		this.clip = null;
	}

	private LimitFinder(LimitFinder other, UChange change) {
		super(other, change);
		if (!instanceOfAny(change,
				UAntiAliasing.class,
				UBackground.class,
				UClip.class,
				HColor.class,
				UHidden.class,
				UStroke.class,
				UTranslate.class
		)) {
			throw new UnsupportedOperationException(change.getClass().toString());
		}
		this.clip = change instanceof UClip ? ((UClip) change).translate(getTranslate()) : other.clip;
		this.minmax = other.minmax;
	}

	private void addPoint(double x, double y) {
		if (clip == null || clip.isInside(x, y)) {
			minmax.addPoint(x, y);
		}
	}

	public void draw(UShape shape) {
		final double x = getTranslate().getDx();
		final double y = getTranslate().getDy();
		if (shape instanceof UText) {
			drawText(x, y, (UText) shape);
		} else if (shape instanceof ULine) {
			drawULine(x, y, (ULine) shape);
		} else if (shape instanceof UEllipse) {
			drawEllipse(x, y, (UEllipse) shape);
		} else if (shape instanceof UPolygon) {
			drawUPolygon(x, y, (UPolygon) shape);
		} else if (shape instanceof UPath) {
			drawUPath(x, y, (UPath) shape);
		} else if (shape instanceof URectangle) {
			drawRectangle(x, y, (URectangle) shape);
		} else if (shape instanceof DotPath) {
			drawDotPath(x, y, (DotPath) shape);
		} else if (shape instanceof UImage) {
			drawImage(x, y, (UImage) shape);
		} else if (shape instanceof UImageSvg) {
			drawImageSvg(x, y, (UImageSvg) shape);
		} else if (shape instanceof UComment) {
		} else if (shape instanceof UEmpty) {
			drawEmpty(x, y, (UEmpty) shape);
		} else if (shape instanceof TextBlock) {
			final TextBlock tb = (TextBlock) shape;
			tb.drawU(this);
		} else if (shape instanceof UCenteredCharacter) {
			// To be done
		} else if (shape instanceof CenteredText) {
			// Ignored
		} else if (shape instanceof SpecialText) {
			// Ignored
		} else if (shape instanceof UPixel) {
			addPoint(x, y);
		} else {
			throw new UnsupportedOperationException(shape.getClass().getName());
		}
	}

	private void drawEmpty(double x, double y, UEmpty shape) {
		addPoint(x, y);
		addPoint(x + shape.getWidth(), y + shape.getHeight());
	}

	private void drawUPath(double x, double y, UPath shape) {
		addPoint(x + shape.getMinX(), y + shape.getMinY());
		addPoint(x + shape.getMaxX(), y + shape.getMaxY());
	}

	private final static double HACK_X_FOR_POLYGON = 10;

	private void drawUPolygon(double x, double y, UPolygon shape) {
		if (shape.getPoints().size() == 0) {
			return;
		}
		addPoint(x + shape.getMinX() - HACK_X_FOR_POLYGON, y + shape.getMinY());
		addPoint(x + shape.getMaxX() + HACK_X_FOR_POLYGON, y + shape.getMaxY());
	}

	private void drawULine(double x, double y, ULine shape) {
		addPoint(x, y);
		addPoint(x + shape.getDX(), y + shape.getDY());
	}

	private void drawRectangle(double x, double y, URectangle shape) {
		addPoint(x - 1, y - 1);
		addPoint(x + shape.getWidth() - 1 + shape.getDeltaShadow() * 2,
				y + shape.getHeight() - 1 + shape.getDeltaShadow() * 2);
	}

	private void drawDotPath(double x, double y, DotPath shape) {
		final MinMax shapeMinMax = shape.getMinMax();
		addPoint(x + shapeMinMax.getMinX(), y + shapeMinMax.getMinY());
		addPoint(x + shapeMinMax.getMaxX(), y + shapeMinMax.getMaxY());
	}

	private void drawImage(double x, double y, UImage shape) {
		addPoint(x, y);
		addPoint(x + shape.getWidth() - 1, y + shape.getHeight() - 1);
	}

	private void drawImageSvg(double x, double y, UImageSvg shape) {
		addPoint(x, y);
		addPoint(x + shape.getWidth() - 1, y + shape.getHeight() - 1);
	}

	private void drawEllipse(double x, double y, UEllipse shape) {
		addPoint(x, y);
		addPoint(x + shape.getWidth() - 1 + shape.getDeltaShadow() * 2,
				y + shape.getHeight() - 1 + shape.getDeltaShadow() * 2);
	}

	private void drawText(double x, double y, UText text) {
		final Dimension2D dim = getStringBounder().calculateDimension(text.getFontConfiguration().getFont(), text.getText());
		y -= dim.getHeight() - 1.5;
		addPoint(x, y);
		addPoint(x, y + dim.getHeight());
		addPoint(x + dim.getWidth(), y);
		addPoint(x + dim.getWidth(), y + dim.getHeight());
	}

	public ColorMapper getColorMapper() {
		return new ColorMapperIdentity();
	}

	public double getMaxX() {
		return minmax.getMaxX();
	}

	public double getMaxY() {
		return minmax.getMaxY();
	}

	public double getMinX() {
		return minmax.getMinX();
	}

	public double getMinY() {
		return minmax.getMinY();
	}

	public MinMax getMinMax() {
		if (minmax.isInfinity()) {
			return MinMax.getEmpty(true);
		}
		return MinMax.fromMutable(minmax);
	}

}
