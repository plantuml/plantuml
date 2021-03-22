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
 */
package net.sourceforge.plantuml.ugraphic.debug;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;

public class UGraphicDebug extends AbstractCommonUGraphic implements ClipContainer, UGraphic2 {

	private final List<String> output;

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicDebug(this, output);
	}

	private UGraphicDebug(UGraphicDebug other, List<String> output) {
		super(other);
		this.output = output;
	}

	public UGraphicDebug() {
		super(new ColorMapperIdentity());
		this.output = new ArrayList<String>();
	}

	public StringBounder getStringBounder() {
		return new StringBounderDebug();
	}

	public void draw(UShape shape) {
		if (shape instanceof ULine) {
			outLine((ULine) shape);
		} else if (shape instanceof URectangle) {
			outRectangle((URectangle) shape);
		} else if (shape instanceof UText) {
			outText((UText) shape);
		} else if (shape instanceof UPolygon) {
			outPolygon((UPolygon) shape);
		} else {
			throw new UnsupportedOperationException("UGraphicDebug " + shape.getClass().getSimpleName());
		}

	}

	private void outPolygon(UPolygon shape) {
		output.add("POLYGON:");
		output.add("  points:");
		for (Point2D pt : shape.getPoints()) {
			final double xp = getTranslateX() + pt.getX();
			final double yp = getTranslateY() + pt.getY();
			output.add("   - " + pointd(xp, yp));
		}
		output.add("  stroke: " + getParam().getStroke());
		output.add("  shadow: " + (int) shape.getDeltaShadow());
		output.add("  color: " + colorToString(getParam().getColor()));
		output.add("  backcolor: " + colorToString(getParam().getBackcolor()));
		output.add("");

	}

	private void outText(UText shape) {
		output.add("TEXT:");
		output.add("  text: " + shape.getText());
		output.add("  position: " + pointd(getTranslateX(), getTranslateY()));
		output.add("  orientation: " + shape.getOrientation());
		output.add("  font: " + shape.getFontConfiguration().toStringDebug());
		output.add("  color: " + colorToString(getParam().getColor()));
		output.add("");

	}

	private void outRectangle(URectangle shape) {
		output.add("RECTANGLE:");
		output.add("  pt1: " + pointd(getTranslateX(), getTranslateY()));
		output.add("  pt2: " + pointd(getTranslateX() + shape.getWidth(), getTranslateY() + shape.getHeight()));
		output.add("  xCorner: " + (int) shape.getRx());
		output.add("  yCorner: " + (int) shape.getRy());
		output.add("  stroke: " + getParam().getStroke());
		output.add("  shadow: " + (int) shape.getDeltaShadow());
		output.add("  color: " + colorToString(getParam().getColor()));
		output.add("  backcolor: " + colorToString(getParam().getBackcolor()));
		output.add("");

	}

	private void outLine(ULine shape) {
		output.add("LINE:");
		output.add("  pt1: " + pointd(getTranslateX(), getTranslateY()));
		output.add("  pt2: " + pointd(getTranslateX() + shape.getDX(), getTranslateY() + shape.getDY()));
		output.add("  stroke: " + getParam().getStroke());
		output.add("  shadow: " + (int) shape.getDeltaShadow());
		output.add("  color: " + colorToString(getParam().getColor()));
		output.add("");

	}

	private String pointd(double x, double y) {
		return String.format(Locale.US, "[ %.4f ; %.4f ]", x, y);
	}

	private String colorToString(HColor color) {
		if (color instanceof HColorSimple) {
			final HColorSimple simple = (HColorSimple) color;
			final Color internal = simple.getColor999();
			if (simple.isMonochrome()) {
				return "monochrome " + Integer.toHexString(internal.getRGB());
			}
			return Integer.toHexString(internal.getRGB());

		}
		return color.getClass().getSimpleName();
	}

	public void writeImageTOBEMOVED(OutputStream os, String metadata, int dpi) throws IOException {
		print(os, "DPI: " + dpi);
		print(os, "");

		for (String s : output) {
			print(os, s);
		}
		os.flush();
	}

	private void print(OutputStream os, String out) throws UnsupportedEncodingException, IOException {
		os.write(out.getBytes("UTF-8"));
		os.write("\n".getBytes("UTF-8"));
	}

}
