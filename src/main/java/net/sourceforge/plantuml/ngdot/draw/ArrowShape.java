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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;

/**
 * A DOT arrow ({@code arrowhead}, {@code arrowtail}), following
 * {@code lib/common/arrows.c} of Graphviz 2.38.
 * <p>
 * An arrow name is a sequence of up to four shapes, the first one at the
 * node. Each shape is a base name ({@code normal inv vee crow tee box diamond
 * dot curve icurve none}) with optional modifiers: {@code o} (open, not
 * filled), {@code l} or {@code r} (only the left or right half). The
 * synonyms {@code empty}, {@code invempty}, {@code open}, {@code halfopen}
 * and {@code ediamond} are known. {@code none} alone means no arrow; inside a
 * sequence, it is a gap.
 */
public final class ArrowShape {

	/** Graphviz ARROW_LENGTH. */
	private static final double ARROW_LENGTH = 10;

	enum Type {
		NORMAL(1.0), CROW(1.0), TEE(0.5), BOX(1.0), DIAMOND(1.2), DOT(0.8), CURVE(1.0), GAP(0.5);

		private final double lenfact;

		private Type(double lenfact) {
			this.lenfact = lenfact;
		}
	}

	static final class Part {
		final Type type;
		final boolean inv;
		final boolean open;
		final boolean left;
		final boolean right;

		Part(Type type, boolean inv, boolean open, boolean left, boolean right) {
			this.type = type;
			this.inv = inv;
			this.open = open;
			this.left = left;
			this.right = right;
		}

		@Override
		public String toString() {
			return (open ? "o" : "") + (left ? "l" : "") + (right ? "r" : "") + (inv ? "inv-" : "") + type;
		}
	}

	public static final ArrowShape NONE = new ArrowShape(Collections.<Part>emptyList(), true);
	public static final ArrowShape NORMAL = new ArrowShape(
			Collections.singletonList(new Part(Type.NORMAL, false, false, false, false)), true);

	private final List<Part> parts;
	private final boolean known;

	private ArrowShape(List<Part> parts, boolean known) {
		this.parts = parts;
		this.known = known;
	}

	List<Part> getParts() {
		return parts;
	}

	/**
	 * {@code false} when the name was not understood: the arrow is then a
	 * {@code normal} one, as Graphviz does.
	 */
	public boolean isKnown() {
		return known;
	}

	public boolean isNone() {
		return parts.isEmpty();
	}

	private static final String[][] SYNONYMS = { { "invempty", "oinv" }, { "halfopen", "lvee" }, { "ediamond", "odiamond" },
			{ "empty", "onormal" }, { "open", "vee" } };

	private static final String[] NAMES = { "normal", "icurve", "curve", "crow", "tee", "box", "diamond", "dot",
			"none", "inv", "vee" };

	public static ArrowShape parse(String name) {
		if (name == null)
			return NORMAL;
		String s = name.trim().toLowerCase();
		if (s.length() == 0)
			return NORMAL;

		final List<Part> result = new ArrayList<Part>();
		while (s.length() > 0 && result.size() < 4) {
			boolean replaced = false;
			for (String[] synonym : SYNONYMS)
				if (s.startsWith(synonym[0])) {
					s = synonym[1] + s.substring(synonym[0].length());
					replaced = true;
					break;
				}
			if (replaced)
				continue;

			boolean open = false;
			boolean left = false;
			boolean right = false;
			String base = null;
			while (base == null && s.length() > 0) {
				for (String n : NAMES)
					if (s.startsWith(n)) {
						base = n;
						break;
					}
				if (base != null)
					break;
				final char c = s.charAt(0);
				if (c == 'o')
					open = true;
				else if (c == 'l')
					left = true;
				else if (c == 'r')
					right = true;
				else
					return new ArrowShape(NORMAL.parts, false);
				s = s.substring(1);
			}
			if (base == null)
				return new ArrowShape(NORMAL.parts, false);
			s = s.substring(base.length());
			result.add(createPart(base, open, left, right));
		}
		if (s.length() > 0)
			return new ArrowShape(NORMAL.parts, false);

		// "none" alone means no arrow at all
		if (result.size() == 1 && result.get(0).type == Type.GAP)
			return NONE;
		return new ArrowShape(result, true);
	}

	private static Part createPart(String base, boolean open, boolean left, boolean right) {
		if (base.equals("normal"))
			return new Part(Type.NORMAL, false, open, left, right);
		if (base.equals("inv"))
			return new Part(Type.NORMAL, true, open, left, right);
		if (base.equals("crow"))
			return new Part(Type.CROW, false, open, left, right);
		if (base.equals("vee"))
			return new Part(Type.CROW, true, open, left, right);
		if (base.equals("tee"))
			return new Part(Type.TEE, false, open, left, right);
		if (base.equals("box"))
			return new Part(Type.BOX, false, open, left, right);
		if (base.equals("diamond"))
			return new Part(Type.DIAMOND, false, open, left, right);
		if (base.equals("dot"))
			return new Part(Type.DOT, false, open, left, right);
		if (base.equals("curve"))
			return new Part(Type.CURVE, false, open, left, right);
		if (base.equals("icurve"))
			return new Part(Type.CURVE, true, open, left, right);
		return new Part(Type.GAP, false, open, left, right);
	}

	/**
	 * The distance between the tip of the arrow and the point where the edge
	 * curve stops.
	 */
	public double getLength(double arrowsize) {
		double lenfact = 0;
		for (Part part : parts)
			lenfact += part.type.lenfact;
		return ARROW_LENGTH * arrowsize * lenfact;
	}

	/**
	 * Draws the arrow.
	 *
	 * @param ug        carries the line color and the stroke
	 * @param fill      the color of the filled shapes
	 * @param tip       the point on the node
	 * @param base      the point where the curve stops
	 * @param arrowsize the {@code arrowsize} attribute
	 */
	public void draw(UGraphic ug, HColor fill, XPoint2D tip, XPoint2D base, double arrowsize) {
		double dx = base.getX() - tip.getX();
		double dy = base.getY() - tip.getY();
		final double norm = Math.sqrt(dx * dx + dy * dy);
		if (norm < 1e-9)
			return;
		dx /= norm;
		dy /= norm;

		XPoint2D p = tip;
		for (Part part : parts) {
			final double len = ARROW_LENGTH * arrowsize * part.type.lenfact;
			final XPoint2D u = new XPoint2D(dx * len, dy * len);
			drawPart(ug, fill, part, p, u);
			p = add(p, u, 1);
		}
	}

	private static XPoint2D add(XPoint2D p, XPoint2D u, double k) {
		return new XPoint2D(p.getX() + u.getX() * k, p.getY() + u.getY() * k);
	}

	private static XPoint2D perp(XPoint2D u, double k) {
		return new XPoint2D(-u.getY() * k, u.getX() * k);
	}

	private static void polygon(UGraphic ug, HColor fill, boolean filled, XPoint2D... points) {
		final UPolygon polygon = new UPolygon();
		for (XPoint2D pt : points)
			polygon.addPoint(pt);
		ug.apply(filled ? fill.bg() : HColors.none().bg()).draw(polygon);
	}

	private static void line(UGraphic ug, XPoint2D a, XPoint2D b) {
		ug.apply(new UTranslate(a.getX(), a.getY())).draw(new ULine(b.getX() - a.getX(), b.getY() - a.getY()));
	}

	private void drawPart(UGraphic ug, HColor fill, Part part, XPoint2D p, XPoint2D u) {
		final XPoint2D q = add(p, u, 1);
		final boolean filled = part.open == false;
		switch (part.type) {
		case NORMAL: {
			final XPoint2D v = perp(u, 0.35);
			final XPoint2D[] a;
			if (part.inv)
				a = new XPoint2D[] { p, add(p, v, -1), q, add(p, v, 1), p };
			else
				a = new XPoint2D[] { q, add(q, v, -1), p, add(q, v, 1), q };
			if (part.left)
				polygon(ug, fill, filled, a[0], a[1], a[2]);
			else if (part.right)
				polygon(ug, fill, filled, a[2], a[3], a[4]);
			else
				polygon(ug, fill, filled, a[1], a[2], a[3]);
			break;
		}
		case CROW: {
			final XPoint2D v = perp(u, 0.45);
			final XPoint2D m = add(p, u, 0.5);
			final XPoint2D[] a;
			if (part.inv)
				a = new XPoint2D[] { p, add(q, v, -1), m, q, q, q, m, add(q, v, 1), p };
			else
				a = new XPoint2D[] { q, add(p, v, -1), m, p, p, p, m, add(p, v, 1), q };
			if (part.left)
				polygon(ug, fill, filled, a[0], a[1], a[2], a[3], a[4], a[5]);
			else if (part.right)
				polygon(ug, fill, filled, a[3], a[4], a[5], a[6], a[7], a[8]);
			else
				polygon(ug, fill, filled, a);
			break;
		}
		case TEE: {
			final XPoint2D v = perp(u, 1);
			final XPoint2D m = add(p, u, 0.2);
			final XPoint2D n = add(p, u, 0.6);
			XPoint2D a0 = add(m, v, 1);
			XPoint2D a1 = add(m, v, -1);
			XPoint2D a2 = add(n, v, -1);
			XPoint2D a3 = add(n, v, 1);
			if (part.left) {
				a0 = m;
				a3 = n;
			} else if (part.right) {
				a1 = m;
				a2 = n;
			}
			polygon(ug, fill, true, a0, a1, a2, a3);
			line(ug, p, q);
			break;
		}
		case BOX: {
			final XPoint2D v = perp(u, 0.4);
			final XPoint2D m = add(p, u, 0.8);
			XPoint2D a0 = add(p, v, 1);
			XPoint2D a1 = add(p, v, -1);
			XPoint2D a2 = add(m, v, -1);
			XPoint2D a3 = add(m, v, 1);
			if (part.left) {
				a0 = p;
				a3 = m;
			} else if (part.right) {
				a1 = p;
				a2 = m;
			}
			polygon(ug, fill, filled, a0, a1, a2, a3);
			line(ug, m, q);
			break;
		}
		case DIAMOND: {
			final XPoint2D v = perp(u, 1.0 / 3);
			final XPoint2D r = add(p, u, 0.5);
			final XPoint2D[] a = { q, add(r, v, 1), p, add(r, v, -1), q };
			if (part.left)
				polygon(ug, fill, filled, a[2], a[3], a[4]);
			else if (part.right)
				polygon(ug, fill, filled, a[0], a[1], a[2]);
			else
				polygon(ug, fill, filled, a[0], a[1], a[2], a[3]);
			break;
		}
		case DOT: {
			final double r = Math.sqrt(u.getX() * u.getX() + u.getY() * u.getY()) / 2;
			final XPoint2D center = add(p, u, 0.5);
			ug.apply(filled ? fill.bg() : HColors.none().bg())
					.apply(new UTranslate(center.getX() - r, center.getY() - r)).draw(UEllipse.build(2 * r, 2 * r));
			break;
		}
		case CURVE:
			line(ug, p, q);
			break;
		case GAP:
			line(ug, p, q);
			break;
		}
	}

	@Override
	public String toString() {
		return parts.toString();
	}

}
