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
package net.sourceforge.plantuml.klimt.drawing;

import java.util.HashMap;
import java.util.Map;

import net.atmp.SpecialText;
import net.sourceforge.plantuml.activitydiagram3.ftile.CenteredText;
import net.sourceforge.plantuml.klimt.CopyForegroundColorToBackgroundColor;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.legacy.AtomText;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.MinMaxMutable;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;
import net.sourceforge.plantuml.klimt.shape.UComment;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UEmpty;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPixel;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;

public abstract class AbstractUGraphic<O> extends AbstractCommonUGraphic {
	// ::remove file when __HAXE__

	private /* final */ O graphic;
	private /* final */ MinMaxMutable minmax;

	// It would be nice to do something like this but not sure how:
	// Map<Class<SHAPE>, UDriver<SHAPE, O>>
	// See
	// https://stackoverflow.com/questions/416540/java-map-with-values-limited-by-keys-type-parameter
	private final Map<Class<? extends UShape>, UDriver<?, O>> drivers = new HashMap<>();

	protected AbstractUGraphic(StringBounder stringBounder) {
		super(stringBounder);
	}

	public void copy(HColor defaultBackground, ColorMapper colorMapper, O graphic) {
		basicCopy(defaultBackground, colorMapper);
		this.graphic = graphic;
		this.minmax = MinMaxMutable.getEmpty(true);
	}

	protected void copy(AbstractUGraphic<O> other) {
		basicCopy(other);
		this.graphic = other.graphic;
		this.minmax = other.minmax;
	}

	protected final O getGraphicObject() {
		return graphic;
	}

	protected boolean manageHiddenAutomatically() {
		return true;
	}

	final protected <SHAPE extends UShape> void registerDriver(Class<SHAPE> cl, UDriver<SHAPE, O> driver) {
		this.drivers.put(cl, driver);
	}

	private static final UDriver<?, ?> NOOP_DRIVER = new UDriver<UShape, Object>() {
		@Override
		public void draw(UShape shape, double x, double y, ColorMapper mapper, UParam param, Object object) {
		}
	};

	@SuppressWarnings("unchecked")
	final protected <SHAPE extends UShape> void ignoreShape(Class<SHAPE> cl) {
		registerDriver(cl, (UDriver<SHAPE, O>) NOOP_DRIVER);
	}

	public final <SHAPE extends UShape> void draw(SHAPE shape) {
		if (shape instanceof SpecialText) {
			((SpecialText) shape).getTitle().drawU(this);
			return;
		}
		if (shape instanceof UEmpty)
			return;

		if (shape instanceof UComment) {
			drawComment((UComment) shape);
			return;
		}

		updateMinMax(shape);

		@SuppressWarnings("unchecked")
		final UDriver<SHAPE, O> driver = (UDriver<SHAPE, O>) drivers.get(shape.getClass());

		if (driver == null)
			throw new UnsupportedOperationException(shape.getClass().toString() + " " + this.getClass());

		if (getParam().isHidden() && manageHiddenAutomatically())
			return;

		beforeDraw();
		driver.draw(shape, getTranslateX(), getTranslateY(), getColorMapper(), getParam(), graphic);
		afterDraw();
	}

	private void updateMinMax(UShape shape) {
		if (matchesProperty("SPECIALTXT") && shape instanceof AtomText) {
			return;
		}

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
		} else if (shape instanceof CopyForegroundColorToBackgroundColor) {
			// Ignored
		} else if (shape instanceof UPixel) {
			addPoint(x, y);
		} else {
			throw new UnsupportedOperationException(shape.getClass().getName());
		}
	}

	private void addPoint(double x, double y) {
		if (getClip() == null || getClip().isInside(x, y))
			minmax.addPoint(x, y);

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
		if (shape.getPoints().size() == 0)
			return;

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
		final XDimension2D dim = getStringBounder().calculateDimension(text.getFontConfiguration().getFont(),
				text.getText());
		y -= dim.getHeight() - 1.5;
		addPoint(x, y);
		addPoint(x, y + dim.getHeight());
		addPoint(x + dim.getWidth(), y);
		addPoint(x + dim.getWidth(), y + dim.getHeight());
	}

	protected void drawComment(UComment shape) {
	}

	protected void beforeDraw() {
	}

	protected void afterDraw() {
	}

	final public double getMaxX() {
		return minmax.getMaxX();
	}

	final public double getMaxY() {
		return minmax.getMaxY();
	}

	final public void resetMax() {
		minmax.reset();
	}

}
