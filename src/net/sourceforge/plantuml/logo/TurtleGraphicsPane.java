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
package net.sourceforge.plantuml.logo;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

class TurtleGraphicsPane {
	final private double width;
	final private double height;
	private double x;
	private double y;
	private double turtleDirection = 90;
	private boolean penIsDown = true;
	private boolean showTurtle = true;
	private HColor penColor = HColorUtils.BLACK;
	private List<Rectangle2D.Double> lines = new ArrayList<Rectangle2D.Double>();
	private List<HColor> colors = new ArrayList<HColor>();

	private String message;

	public TurtleGraphicsPane(int width, int height) {
		this.width = width;
		this.height = height;
		clearScreen();
	}

	public void clearScreen() {
		x = width / 2;
		y = -height / 2;
		turtleDirection = 90;
		lines.clear();
		colors.clear();
	}

	private double dtor(double degrees) {
		return degrees * Math.PI / 180.0;
	}

	private void drawTurtle(UGraphic ug) {
		if (showTurtle == false) {
			return;
		}
		final UPolygon poly = new UPolygon();
		double size = 2;
		double deltax = 4.5 * size;
		poly.addPoint(0 * size - deltax, 0);
		poly.addPoint(0 * size - deltax, -2 * size);
		poly.addPoint(1 * size - deltax, -2 * size);
		poly.addPoint(1 * size - deltax, -4 * size);
		poly.addPoint(2 * size - deltax, -4 * size);
		poly.addPoint(2 * size - deltax, -6 * size);
		poly.addPoint(3 * size - deltax, -6 * size);
		poly.addPoint(3 * size - deltax, -8 * size);
		poly.addPoint(4 * size - deltax, -8 * size);
		poly.addPoint(4 * size - deltax, -9 * size);
		poly.addPoint(5 * size - deltax, -9 * size);
		poly.addPoint(5 * size - deltax, -8 * size);
		poly.addPoint(6 * size - deltax, -8 * size);
		poly.addPoint(6 * size - deltax, -6 * size);
		poly.addPoint(7 * size - deltax, -6 * size);
		poly.addPoint(7 * size - deltax, -4 * size);
		poly.addPoint(8 * size - deltax, -4 * size);
		poly.addPoint(8 * size - deltax, -2 * size);
		poly.addPoint(9 * size - deltax, -2 * size);
		poly.addPoint(9 * size - deltax, 0);
		poly.addPoint(0 * size - deltax, 0);
		final double angle = -dtor(turtleDirection - 90);
		poly.rotate(angle);
		// ug.setAntiAliasing(false);
		final HColorSet htmlColorSet = HColorSet.instance();
		final HColor turtleColor1 = htmlColorSet.getColorIfValid("OliveDrab");
		final HColor turtleColor2 = htmlColorSet.getColorIfValid("MediumSpringGreen");

		ug.apply(turtleColor1).apply(turtleColor2.bg()).apply(new UTranslate(x, -y))
				.draw(poly);
		// ug.setAntiAliasing(true);
	}

	public void showTurtle() {
		showTurtle = true;
	}

	public void hideTurtle() {
		showTurtle = false;
	}

	public void setPenColor(HColor newPenColor) {
		penColor = newPenColor;
	}

	void addLine(double x1, double y1, double x2, double y2) {
		lines.add(new Rectangle2D.Double(x1, y1, x2, y2));
		colors.add(penColor);
	}

	public void forward(double distance) {
		double angle = dtor(turtleDirection);
		double newX = x + distance * Math.cos(angle);
		double newY = y + distance * Math.sin(angle);
		if (penIsDown) {
			addLine(x, y, newX, newY);
			x = newX;
			y = newY;
		} else {
			x = newX;
			y = newY;
		}
	}

	public void back(double distance) {
		forward(-distance);
	}

	public void left(double turnAngle) {
		turtleDirection += turnAngle;
		while (turtleDirection > 360) {
			turtleDirection -= 360;
		}
		while (turtleDirection < 0) {
			turtleDirection += 360;
		}
	}

	public void right(double turnAngle) {
		left(-turnAngle);
	}

	public void penUp() {
		penIsDown = false;
	}

	public void penDown() {
		penIsDown = true;
	}

	public void paint(UGraphic ug) {
		int n = lines.size();

		for (int i = 0; i < n; i++) {
			final HColor color = colors.get(i);
			final Rectangle2D.Double r = lines.get(i);
			final ULine line = new ULine(r.width - r.x, -r.height + r.y);
			ug.apply(color).apply(new UTranslate(r.x, -r.y)).draw(line);

		}
		drawTurtle(ug);
		if (message != null) {
			final FontConfiguration font = FontConfiguration.blackBlueTrue(new UFont("", Font.PLAIN, 14));
			final TextBlock text = Display.create(message).create(font, HorizontalAlignment.LEFT,
					new SpriteContainerEmpty());
			final Dimension2D dim = text.calculateDimension(ug.getStringBounder());
			final double textHeight = dim.getHeight();
			text.drawU(ug.apply(UTranslate.dy((height - textHeight))));
		}
	}

	public void message(String messageText) {
		this.message = messageText;
	}

}
