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
package net.sourceforge.plantuml.ngdot.draw;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.ngdot.attr.DotNumbers;
import net.sourceforge.plantuml.ngdot.layout.LayoutInput.Outline;
import net.sourceforge.plantuml.ngdot.model.DotAttributes;

/**
 * The {@code shape} of a node: how it is sized around its label and how it
 * is drawn.
 * <p>
 * Smetana only knows boxes and ellipses: a {@link #DIAMOND} is laid out as an
 * ellipse of the same size, and the edges are clipped on the real outline by
 * PlantUML (see {@link #hasOwnContour()}).
 */
public enum NodeShape {

	BOX(Outline.BOX, false, 0), //
	ELLIPSE(Outline.ELLIPSE, false, 0), //
	CIRCLE(Outline.ELLIPSE, true, 0), //
	DOUBLECIRCLE(Outline.ELLIPSE, true, 0), //
	POINT(Outline.ELLIPSE, true, 0), //
	DIAMOND(Outline.ELLIPSE, false, 4), //
	PLAINTEXT(Outline.BOX, false, 0);

	private static final double SQRT2 = Math.sqrt(2);

	/** Graphviz GAP: the space between two peripheries. */
	public static final double GAP = 4;

	/** Graphviz DEF_POINT: the default size of a point, in inches. */
	private static final double DEF_POINT = 0.05;

	private final Outline outline;
	private final boolean regular;
	private final int sides;

	private NodeShape(Outline outline, boolean regular, int sides) {
		this.outline = outline;
		this.regular = regular;
		this.sides = sides;
	}

	/**
	 * The shape of a node. An unknown name gives {@code null}: the caller falls
	 * back to {@link #ELLIPSE}, the Graphviz default.
	 */
	public static NodeShape fromName(String name) {
		if (name == null)
			return ELLIPSE;
		final String s = name.trim().toLowerCase();
		if (s.equals("box") || s.equals("rect") || s.equals("rectangle") || s.equals("square"))
			return BOX;
		if (s.equals("ellipse") || s.equals("oval"))
			return ELLIPSE;
		if (s.equals("circle"))
			return CIRCLE;
		if (s.equals("doublecircle"))
			return DOUBLECIRCLE;
		if (s.equals("point"))
			return POINT;
		if (s.equals("diamond"))
			return DIAMOND;
		if (s.equals("plaintext") || s.equals("plain") || s.equals("none"))
			return PLAINTEXT;
		return null;
	}

	public Outline getOutline() {
		return outline;
	}

	/**
	 * {@code true} when Smetana does not know the real outline, so that the
	 * edges must be clipped by PlantUML with {@link #contains}.
	 */
	public boolean hasOwnContour() {
		return this == DIAMOND;
	}

	/**
	 * Tells whether a point, relative to the center of the node, is inside the
	 * outline of a node of the given (outer) size.
	 */
	public boolean contains(double dx, double dy, XDimension2D size) {
		final double a = size.getWidth() / 2;
		final double b = size.getHeight() / 2;
		if (a <= 0 || b <= 0)
			return false;
		if (this == DIAMOND)
			return Math.abs(dx) / a + Math.abs(dy) / b <= 1;
		if (outline == Outline.BOX)
			return Math.abs(dx) <= a && Math.abs(dy) <= b;
		return (dx * dx) / (a * a) + (dy * dy) / (b * b) <= 1;
	}

	public boolean hasLabel() {
		return this != POINT;
	}

	/**
	 * The number of outlines drawn by default: 2 for a double circle, 0 for
	 * plain text.
	 */
	public int defaultPeripheries() {
		if (this == DOUBLECIRCLE)
			return 2;
		if (this == PLAINTEXT)
			return 0;
		return 1;
	}

	/**
	 * The size of the inner outline of the node, following Graphviz
	 * {@code poly_init}: the label is padded by its margin, the shapes other
	 * than boxes grow so that the label fits inside them, then the result is at
	 * least {@code width x height} (0.75 x 0.5 inch by default), or exactly that
	 * with {@code fixedsize=true}. The extra peripheries are not included.
	 */
	public XDimension2D computeSize(XDimension2D label, DotAttributes attributes, boolean squareByName) {
		if (this == POINT)
			return pointSize(attributes);

		final double minWidth = DotNumbers.parse(attributes.getString("width"), 0.75) * 72;
		final double minHeight = DotNumbers.parse(attributes.getString("height"), 0.5) * 72;
		final String fixedsize = attributes.getString("fixedsize");
		final boolean square = regular || squareByName
				|| DotNumbers.parseBoolean(attributes.getString("regular"), false);

		if (fixedsize != null && DotNumbers.parseBoolean(fixedsize, false)) {
			if (square) {
				final double side = attributes.contains("width") ? minWidth
						: attributes.contains("height") ? minHeight : Math.min(minWidth, minHeight);
				return new XDimension2D(side, side);
			}
			return new XDimension2D(minWidth, minHeight);
		}

		double width = label.getWidth() + marginX(attributes);
		double height = label.getHeight() + marginY(attributes);

		if (outline == Outline.ELLIPSE) {
			final double temp = height * SQRT2;
			if (minHeight > temp && regular == false) {
				// There is height to spare: only stretch the width
				width *= Math.sqrt(1. / (1. - (height / minHeight) * (height / minHeight)));
			} else {
				width *= SQRT2;
				height = temp;
			}
			if (sides > 2) {
				final double cos = Math.cos(Math.PI / sides);
				width /= cos;
				height /= cos;
			}
		}

		if (square) {
			double min = Math.min(minWidth, minHeight);
			if (attributes.contains("width") && attributes.contains("height") == false)
				min = minWidth;
			else if (attributes.contains("height") && attributes.contains("width") == false)
				min = minHeight;
			final double side = Math.max(min, Math.max(width, height));
			return new XDimension2D(side, side);
		}
		return new XDimension2D(Math.max(minWidth, width), Math.max(minHeight, height));
	}

	private static XDimension2D pointSize(DotAttributes attributes) {
		final Double width = DotNumbers.parseFirst(attributes.getString("width"));
		final Double height = DotNumbers.parseFirst(attributes.getString("height"));
		double size = DEF_POINT;
		if (width != null && height != null)
			size = Math.min(width, height);
		else if (width != null)
			size = width;
		else if (height != null)
			size = height;
		size = Math.max(size, 0.0003) * 72;
		return new XDimension2D(size, size);
	}

	// Graphviz default margin: 0.11 x 0.055 inch on each side, i.e. 16 x 8 points
	// in total. A "margin" attribute gives the margin of each side, in inches.
	private static double marginX(DotAttributes attributes) {
		final String margin = attributes.getString("margin");
		if (margin == null)
			return 16;
		return 2 * 72 * DotNumbers.parse(margin, 0.11);
	}

	private static double marginY(DotAttributes attributes) {
		final String margin = attributes.getString("margin");
		if (margin == null)
			return 8;
		final int comma = margin.indexOf(',');
		if (comma == -1)
			return 2 * 72 * DotNumbers.parse(margin, 0.055);
		return 2 * 72 * DotNumbers.parse(margin.substring(comma + 1), 0.055);
	}

	/**
	 * The size of the node once its extra peripheries are added.
	 */
	public static XDimension2D withPeripheries(XDimension2D inner, int peripheries) {
		if (peripheries <= 1)
			return inner;
		final double more = 2 * GAP * (peripheries - 1);
		return new XDimension2D(inner.getWidth() + more, inner.getHeight() + more);
	}

	/**
	 * Draws the node outlines, the top-left corner of the node at the origin of
	 * {@code ug}, which carries the line color and the stroke. Only the inner
	 * outline is filled; with no periphery, only the fill is drawn.
	 *
	 * @param fill  the fill color, or {@code null} for none
	 * @param outer the size of the node, peripheries included
	 */
	public void draw(UGraphic ug, HColor fill, XDimension2D outer, int peripheries, boolean rounded,
			boolean diagonals) {
		final HColor background = fill == null ? HColors.none() : fill;
		if (peripheries <= 0) {
			if (fill != null)
				drawOne(ug.apply(HColors.none()).apply(background.bg()), outer, 0, rounded, false);
			return;
		}
		for (int i = peripheries - 1; i >= 0; i--) {
			final double offset = GAP * (peripheries - 1 - i);
			final XDimension2D size = new XDimension2D(outer.getWidth() - 2 * offset,
					outer.getHeight() - 2 * offset);
			final HColor bg = i == 0 ? background : HColors.none();
			drawOne(ug.apply(bg.bg()).apply(new UTranslate(offset, offset)), size, i, rounded, diagonals);
		}
	}

	private void drawOne(UGraphic ug, XDimension2D size, int index, boolean rounded, boolean diagonals) {
		final double w = size.getWidth();
		final double h = size.getHeight();
		switch (this) {
		case BOX:
		case PLAINTEXT:
			if (rounded)
				ug.draw(URectangle.build(size).rounded(Math.min(24, Math.min(w, h))));
			else
				ug.draw(URectangle.build(size));
			if (diagonals && index == 0)
				drawDiagonals(ug, w, h);
			break;
		case DIAMOND:
			final UPolygon polygon = new UPolygon();
			polygon.addPoint(w / 2, 0);
			polygon.addPoint(w, h / 2);
			polygon.addPoint(w / 2, h);
			polygon.addPoint(0, h / 2);
			ug.draw(polygon);
			break;
		default:
			ug.draw(UEllipse.build(w, h));
			break;
		}
	}

	/** Small lines cutting the corners, as Graphviz {@code diagonals}. */
	private static void drawDiagonals(UGraphic ug, double w, double h) {
		final double d = Math.min(12, Math.min(w, h) / 4);
		ug.apply(new UTranslate(0, d)).draw(new ULine(d, -d));
		ug.apply(new UTranslate(w - d, 0)).draw(new ULine(d, d));
		ug.apply(new UTranslate(w, h - d)).draw(new ULine(-d, d));
		ug.apply(new UTranslate(d, h)).draw(new ULine(-d, -d));
	}

}
