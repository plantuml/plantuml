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
 * Contribution:  Miguel Esteves
 *
 */
package net.sourceforge.plantuml.klimt.shape;

import net.sourceforge.plantuml.klimt.AbstractShadowable;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UShapeSized;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.compress.CompressionMode;
import net.sourceforge.plantuml.klimt.compress.UShapeIgnorableForCompression;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class URectangle extends AbstractShadowable implements UShapeSized, UShapeIgnorableForCompression {

	private final double width;
	private final double height;
	private final double rx;
	private final double ry;
	private final String comment;
	private final String codeLine;
	private final boolean ignoreForCompressionOnX;
	private final boolean ignoreForCompressionOnY;

	public URectangle withHeight(double newHeight) {
		final URectangle result = new URectangle(width, newHeight, rx, ry, comment, ignoreForCompressionOnX,
				ignoreForCompressionOnY, codeLine);
		result.setDeltaShadow(this.getDeltaShadow());
		return result;
	}

	public URectangle withWidth(double newWidth) {
		final URectangle result = new URectangle(newWidth, height, rx, ry, comment, ignoreForCompressionOnX,
				ignoreForCompressionOnY, codeLine);
		result.setDeltaShadow(this.getDeltaShadow());
		return result;
	}

	public URectangle withCommentAndCodeLine(String comment, String codeLine) {
		return new URectangle(width, height, rx, ry, comment, ignoreForCompressionOnX, ignoreForCompressionOnY,
				codeLine);
	}

	public URectangle rounded(double round) {
		return new URectangle(width, height, round, round, comment, ignoreForCompressionOnX, ignoreForCompressionOnY,
				codeLine);
	}

	public Shadowable diagonalCorner(double diagonalCorner) {
		if (ignoreForCompressionOnX || ignoreForCompressionOnY)
			throw new IllegalStateException();

		if (diagonalCorner == 0)
			return this;

		final UPath result = UPath.none();
		result.moveTo(diagonalCorner, 0);
		result.lineTo(width - diagonalCorner, 0);
		result.lineTo(width, diagonalCorner);
		result.lineTo(width, height - diagonalCorner);
		result.lineTo(width - diagonalCorner, height);
		result.lineTo(diagonalCorner, height);
		result.lineTo(0, height - diagonalCorner);
		result.lineTo(0, diagonalCorner);
		result.lineTo(diagonalCorner, 0);
		return result;
	}

	public final URectangle ignoreForCompressionOnX() {
		return new URectangle(width, height, rx, ry, comment, true, ignoreForCompressionOnY, codeLine);
	}

	public final URectangle ignoreForCompressionOnY() {
		return new URectangle(width, height, rx, ry, comment, ignoreForCompressionOnX, true, codeLine);
	}

	public static URectangle build(double width, double height) {
		return new URectangle(width, height, 0, 0, null, false, false, null);
	}

	public static URectangle build(XDimension2D dim) {
		return build(dim.getWidth(), dim.getHeight());
	}

	private URectangle(double width, double height, double rx, double ry, String comment,
			boolean ignoreForCompressionOnX, boolean ignoreForCompressionOnY, String codeLine) {
		if (height == 0)
			throw new IllegalArgumentException("height=" + height);

		if (width == 0)
			throw new IllegalArgumentException("width=" + width);

		this.ignoreForCompressionOnX = ignoreForCompressionOnX;
		this.ignoreForCompressionOnY = ignoreForCompressionOnY;
		this.comment = comment;
		this.codeLine = codeLine;
		this.width = width;
		this.height = height;
		this.rx = rx;
		this.ry = ry;
	}

	@Override
	public String toString() {
		return "width=" + width + " height=" + height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getRx() {
		return rx;
	}

	public double getRy() {
		return ry;
	}

	public URectangle clip(UClip clip) {
		return this;
	}

	public MinMax getMinMax() {
		return MinMax.fromMax(width, height);
	}

	public final String getComment() {
		return comment;
	}

	public String getCodeLine() {
		return codeLine;
	}

	public void drawWhenCompressed(UGraphic ug, CompressionMode mode) {
		if (mode == CompressionMode.ON_X) {
			ug.draw(new UEmpty(2, getHeight()));
			ug.apply(UTranslate.dx(getWidth() - 2)).draw(new UEmpty(2, getHeight()));
//			drawEmpty(x, y, new UEmpty(2, shape.getHeight()));
//			drawEmpty(x + shape.getWidth() - 2, y, new UEmpty(2, shape.getHeight()));
		}
		if (mode == CompressionMode.ON_Y) {
			ug.draw(new UEmpty(getWidth(), 2));
			ug.apply(UTranslate.dy(getHeight() - 2)).draw(new UEmpty(getWidth(), 2));
//			drawEmpty(x, y, new UEmpty(shape.getWidth(), 2));
//			drawEmpty(x, y + shape.getHeight() - 2, new UEmpty(shape.getWidth(), 2));
		}

	}

	public boolean isIgnoreForCompressionOn(CompressionMode mode) {
		if (mode == CompressionMode.ON_X)
			return ignoreForCompressionOnX;

		if (mode == CompressionMode.ON_Y)
			return ignoreForCompressionOnY;

		throw new IllegalArgumentException();
	}

}
