/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockable;

public class LimitFinder implements UGraphic {

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new LimitFinder(stringBounder, minmax, translate.compose((UTranslate) change));
		} else if (change instanceof UStroke) {
			return new LimitFinder(this);
		} else if (change instanceof UChangeBackColor) {
			return new LimitFinder(this);
		} else if (change instanceof UChangeColor) {
			return new LimitFinder(this);
		}
		throw new UnsupportedOperationException();
	}

	private final StringBounder stringBounder;
	private final UTranslate translate;
	private final MinMaxMutable minmax;

	public LimitFinder(StringBounder stringBounder, boolean initToZero) {
		this(stringBounder, MinMaxMutable.getEmpty(initToZero), new UTranslate());
	}

	private LimitFinder(StringBounder stringBounder, MinMaxMutable minmax, UTranslate translate) {
		this.stringBounder = stringBounder;
		this.minmax = minmax;
		this.translate = translate;
	}

	private LimitFinder(LimitFinder other) {
		this(other.stringBounder, other.minmax, other.translate);
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public UParam getParam() {
		return new UParamNull();
	}

	public void draw(UShape shape) {
		final double x = translate.getDx();
		final double y = translate.getDy();
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
		} else if (shape instanceof UImage) {
			drawImage(x, y, (UImage) shape);
		} else if (shape instanceof UEmpty) {
			drawEmpty(x, y, (UEmpty) shape);
		} else if (shape instanceof TextBlockable) {
			final TextBlock tb = ((TextBlockable) shape).asTextBlock();
			tb.drawU(this);
		} else if (shape instanceof UCenteredCharacter) {
			// To be done
		} else {
			throw new UnsupportedOperationException(shape.getClass().getName());
		}
	}

	private void drawEmpty(double x, double y, UEmpty shape) {
		minmax.addPoint(x, y);
		minmax.addPoint(x + shape.getWidth(), y + shape.getHeight());
	}

	private void drawUPath(double x, double y, UPath shape) {
		minmax.addPoint(x + shape.getMinX(), y + shape.getMinY());
		minmax.addPoint(x + shape.getMaxX(), y + shape.getMaxY());
	}

	private void drawUPolygon(double x, double y, UPolygon shape) {
		minmax.addPoint(x + shape.getMinX(), y + shape.getMinY());
		minmax.addPoint(x + shape.getMaxX(), y + shape.getMaxY());
	}

	private void drawULine(double x, double y, ULine shape) {
		minmax.addPoint(x, y);
		minmax.addPoint(x + shape.getDX(), y + shape.getDY());
	}

	private void drawRectangle(double x, double y, URectangle shape) {
		minmax.addPoint(x, y);
		minmax.addPoint(x + shape.getWidth(), y + shape.getHeight());
	}

	private void drawImage(double x, double y, UImage shape) {
		minmax.addPoint(x, y);
		minmax.addPoint(x + shape.getWidth(), y + shape.getHeight());
	}

	private void drawEllipse(double x, double y, UEllipse shape) {
		minmax.addPoint(x, y);
		minmax.addPoint(x + shape.getWidth(), y + shape.getHeight());
	}

	private void drawText(double x, double y, UText text) {
		final Dimension2D dim = stringBounder.calculateDimension(text.getFontConfiguration().getFont(), text.getText());
		y -= dim.getHeight() - 1.5;
		minmax.addPoint(x, y);
		minmax.addPoint(x, y + dim.getHeight());
		minmax.addPoint(x + dim.getWidth(), y);
		minmax.addPoint(x + dim.getWidth(), y + dim.getHeight());
	}

	public ColorMapper getColorMapper() {
		return new ColorMapperIdentity();
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public void writeImage(OutputStream os, String metadata, int dpi) throws IOException {
		throw new UnsupportedOperationException();
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
		return MinMax.fromMutable(minmax);
	}

	public void flushUg() {
	}

}
