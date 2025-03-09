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
 * Contribution :  Hisashi Miyashita
 * 
 *
 */
package net.sourceforge.plantuml.abel;

import java.util.EnumSet;

import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.Rankdir;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.svek.ShapeType;

public enum EntityPosition {

	NORMAL, ENTRY_POINT, EXIT_POINT, INPUT_PIN, OUTPUT_PIN, EXPANSION_INPUT, EXPANSION_OUTPUT, PORTIN, PORTOUT;

	public static final double RADIUS = 6;

	public static EnumSet<EntityPosition> getInputs() {
		return EnumSet.of(ENTRY_POINT, INPUT_PIN, EXPANSION_INPUT, PORTIN);
	}

	public static EnumSet<EntityPosition> getOutputs() {
		return EnumSet.of(EXIT_POINT, OUTPUT_PIN, EXPANSION_OUTPUT, PORTOUT);
	}

	public static EnumSet<EntityPosition> getNormals() {
		return EnumSet.of(NORMAL);
	}

	public boolean isNormal() {
		return this == NORMAL;
	}

	public boolean isInput() {
		return getInputs().contains(this);
	}

	public boolean isOutput() {
		return getOutputs().contains(this);
	}

	public void drawSymbol(UGraphic ug, Rankdir rankdir) {
		if (this == NORMAL) {
			throw new IllegalStateException();
		} else if (this == ENTRY_POINT || this == EXIT_POINT) {
			final Shadowable circle = UEllipse.build(RADIUS * 2, RADIUS * 2);
			ug.draw(circle);
			if (this == EntityPosition.EXIT_POINT) {
				final double xc = 0 + RADIUS + .5;
				final double yc = 0 + RADIUS + .5;
				final double radius = RADIUS - .5;
				drawLine(ug, getPointOnCircle(xc, yc, Math.PI / 4, radius),
						getPointOnCircle(xc, yc, Math.PI + Math.PI / 4, radius));
				drawLine(ug, getPointOnCircle(xc, yc, -Math.PI / 4, radius),
						getPointOnCircle(xc, yc, Math.PI - Math.PI / 4, radius));
			}
		} else if (this == INPUT_PIN || this == OUTPUT_PIN /* || this == PORT */) {
			final Shadowable rectangle = URectangle.build(RADIUS * 2, RADIUS * 2);
			ug.draw(rectangle);
		} else if (this == EXPANSION_INPUT || this == EXPANSION_OUTPUT) {
			if (rankdir == Rankdir.TOP_TO_BOTTOM) {
				final Shadowable rectangle = URectangle.build(RADIUS * 2 * 4, RADIUS * 2);
				ug.draw(rectangle);
				final ULine vline = ULine.vline(RADIUS * 2);
				ug.apply(UTranslate.dx(RADIUS * 2)).draw(vline);
				ug.apply(UTranslate.dx(RADIUS * 2 * 2)).draw(vline);
				ug.apply(UTranslate.dx(RADIUS * 2 * 3)).draw(vline);
			} else {
				final Shadowable rectangle = URectangle.build(RADIUS * 2, RADIUS * 2 * 4);
				ug.apply(UTranslate.dy(0)).draw(rectangle);
				final ULine hline = ULine.hline(RADIUS * 2);
				ug.apply(UTranslate.dy(RADIUS * 2)).draw(hline);
				ug.apply(UTranslate.dy(RADIUS * 2 * 2)).draw(hline);
				ug.apply(UTranslate.dy(RADIUS * 2 * 3)).draw(hline);
			}
		}

	}

	public XDimension2D getDimension(Rankdir rankdir) {
		if (this == EXPANSION_INPUT || this == EXPANSION_OUTPUT) {
			if (rankdir == Rankdir.TOP_TO_BOTTOM)
				return new XDimension2D(EntityPosition.RADIUS * 2 * 4, EntityPosition.RADIUS * 2);

			return new XDimension2D(EntityPosition.RADIUS * 2, EntityPosition.RADIUS * 2 * 4);
		}
		return new XDimension2D(EntityPosition.RADIUS * 2, EntityPosition.RADIUS * 2);
	}

	private XPoint2D getPointOnCircle(double xc, double yc, double angle, double radius) {
		final double x = xc + radius * Math.cos(angle);
		final double y = yc + radius * Math.sin(angle);
		return new XPoint2D(x, y);
	}

	static private void drawLine(UGraphic ug, XPoint2D p1, XPoint2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.apply(new UTranslate(p1.getX(), p1.getY())).draw(new ULine(dx, dy));

	}

	public ShapeType getShapeType() {
		if (this == NORMAL)
			throw new IllegalStateException();

		if (this == ENTRY_POINT || this == EXIT_POINT)
			return ShapeType.RECTANGLE_PORT;

		return ShapeType.RECTANGLE;
	}

	public static EntityPosition fromStereotype(String label) {
		if ("<<port>>".equalsIgnoreCase(label))
			throw new UnsupportedOperationException();

		if ("<<entrypoint>>".equalsIgnoreCase(label))
			return ENTRY_POINT;

		if ("<<exitpoint>>".equalsIgnoreCase(label))
			return EXIT_POINT;

		if ("<<inputpin>>".equalsIgnoreCase(label))
			return INPUT_PIN;

		if ("<<outputpin>>".equalsIgnoreCase(label))
			return OUTPUT_PIN;

		if ("<<expansioninput>>".equalsIgnoreCase(label))
			return EXPANSION_INPUT;

		if ("<<expansionoutput>>".equalsIgnoreCase(label))
			return EXPANSION_OUTPUT;

		return EntityPosition.NORMAL;
	}

//	public static EnumSet<EntityPosition> getSame() {
//		return EnumSet.of(PORT);
//	}
//
	public boolean isPort() {
		return /* this == PORT || */ this == PORTIN || this == PORTOUT;
	}

	public boolean usePortP() {
		return isPort() || this == EXIT_POINT || this == ENTRY_POINT;
	}

}
