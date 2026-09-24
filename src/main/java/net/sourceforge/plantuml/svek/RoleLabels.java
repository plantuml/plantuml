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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

/**
 * Role labels of an association (<code>A "1"/"-owner" --> B</code>), shared by
 * all the layout engines (Graphviz, Smetana and ELK).
 * <ul>
 * <li>{@link #create} builds the text block, turning a leading visibility
 * character into the same icon as the one used for class attributes.</li>
 * <li>{@link #getPosition} decides where the block is drawn: on the other side
 * of the line from the quantifier, and never on top of the line.</li>
 * </ul>
 */
public final class RoleLabels {

	private static final double GAP = 2;
	private static final double MIN_DISTANCE_FOR_DIRECTION = 15;
	private static final double SEARCH_STEP = 2;
	private static final double SEARCH_RANGE = 30;

	private RoleLabels() {
	}

	public static TextBlock create(String role, Pragma pragma, FontConfiguration font, ISkinParam skinParam) {
		// A role is a property of the class, so it gets the icons of the fields
		// (hollow), as the attributes do, and not the ones of the methods (filled).
		VisibilityModifier visibilityModifier = null;
		String text = role;
		if (skinParam.classAttributeIconSize() > 0) {
			visibilityModifier = VisibilityModifier.getVisibilityModifier(role, true);
			if (visibilityModifier != null)
				text = role.substring(1).trim();
		}

		TextBlock result = Display.getWithNewlines(pragma, text).create(font,
				skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER), skinParam);

		if (visibilityModifier != null) {
			final HColor fore = new Rose().getHtmlColor(skinParam, visibilityModifier.getForeground());
			TextBlock icon = visibilityModifier.getUBlock(skinParam.classAttributeIconSize(), fore, null, false);
			icon = TextBlockUtils.withMargin(icon, 0, 1, 2, 0);
			result = TextBlockUtils.mergeLR(icon, result, VerticalAlignment.CENTER);
		}
		return result;
	}

	/**
	 * Computes the top-left corner of the role label.
	 * 
	 * The role is preferably drawn on the other side of the line from the
	 * quantifier, at the same height (or at the same abscissa for a horizontal
	 * line). If this collides with one of the <code>obstacles</code> (typically the
	 * node the line is attached to: a line leaving a node diagonally can have its
	 * "other side" inside the node), the role is moved to the closest place where it has
	 * some room.
	 * 
	 * @param qDim          size of the quantifier, which is drawn at
	 *                      <code>quantifierPos</code> by the layout engine
	 * @param rDim          size of the role
	 * @param thisEndpoint  end of the line where the label is attached
	 * @param otherEndpoint the opposite end of the line
	 * @param pathSamples   points of the line, or <code>null</code> to use the
	 *                      straight segment between the two endpoints
	 * @param obstacles     areas to keep free, in the same coordinates as the
	 *                      other parameters (may be empty)
	 */
	public static XPoint2D getPosition(XDimension2D qDim, XDimension2D rDim, XPoint2D quantifierPos,
			XPoint2D thisEndpoint, XPoint2D otherEndpoint, Collection<XPoint2D> pathSamples,
			Collection<RectangleArea> obstacles) {
		final Collection<XPoint2D> samples = pathSamples != null ? pathSamples
				: sampleSegment(thisEndpoint, otherEndpoint);

		// Graphviz places the quantifier relatively to the direction of the line where
		// it leaves the node, not relatively to the direction from one end to the
		// other: a diagonal line can leave a node vertically and then bend.
		final XPoint2D towards = getPointAwayFrom(thisEndpoint, otherEndpoint, samples);
		final double dirX = towards.getX() - thisEndpoint.getX();
		final double dirY = towards.getY() - thisEndpoint.getY();

		if (Math.abs(dirX) + Math.abs(dirY) < 0.001)
			return new XPoint2D(quantifierPos.getX(), quantifierPos.getY() + qDim.getHeight());

		final XPoint2D preferred = getMirroredPosition(qDim, rDim, quantifierPos, thisEndpoint, dirX, dirY, samples);

		final List<RectangleArea> forbidden = new ArrayList<>(obstacles);
		forbidden.add(new RectangleArea(quantifierPos.getX(), quantifierPos.getY(),
				quantifierPos.getX() + qDim.getWidth(), quantifierPos.getY() + qDim.getHeight()));
		if (isFree(preferred, rDim, forbidden, null))
			return preferred;

		final XPoint2D alternative = searchFreePosition(qDim, rDim, quantifierPos, thisEndpoint, dirX, dirY, samples,
				forbidden);
		return alternative != null ? alternative : preferred;
	}

	private static XPoint2D getMirroredPosition(XDimension2D qDim, XDimension2D rDim, XPoint2D quantifierPos,
			XPoint2D thisEndpoint, double dirX, double dirY, Collection<XPoint2D> samples) {
		if (Math.abs(dirY) >= Math.abs(dirX)) {
			// Mostly vertical: mirror across the line X
			final double roleY = quantifierPos.getY();
			final double qCenterX = quantifierPos.getX() + qDim.getWidth() / 2;
			final double lineX = thisEndpoint.getX();
			double roleX;
			if (qCenterX < lineX) {
				roleX = lineX + GAP;
				// The line is not vertical: at the height of the label, it may be further
				// to the right than at its end.
				for (XPoint2D pt : samples)
					if (pt.getY() >= roleY - GAP && pt.getY() <= roleY + rDim.getHeight() + GAP)
						roleX = Math.max(roleX, pt.getX() + GAP);
			} else {
				roleX = lineX - rDim.getWidth() - GAP;
				for (XPoint2D pt : samples)
					if (pt.getY() >= roleY - GAP && pt.getY() <= roleY + rDim.getHeight() + GAP)
						roleX = Math.min(roleX, pt.getX() - rDim.getWidth() - GAP);
			}
			return new XPoint2D(roleX, roleY);
		}

		// Mostly horizontal: mirror across the line Y
		final double roleX = quantifierPos.getX();
		final double qCenterY = quantifierPos.getY() + qDim.getHeight() / 2;
		final double lineY = thisEndpoint.getY();
		double roleY;
		if (qCenterY < lineY) {
			roleY = lineY + GAP;
			for (XPoint2D pt : samples)
				if (pt.getX() >= roleX - GAP && pt.getX() <= roleX + rDim.getWidth() + GAP)
					roleY = Math.max(roleY, pt.getY() + GAP);
		} else {
			roleY = lineY - rDim.getHeight() - GAP;
			for (XPoint2D pt : samples)
				if (pt.getX() >= roleX - GAP && pt.getX() <= roleX + rDim.getWidth() + GAP)
					roleY = Math.min(roleY, pt.getY() - rDim.getHeight() - GAP);
		}
		return new XPoint2D(roleX, roleY);
	}

	/**
	 * Looks for the free place the closest to the mirror image of the quantifier
	 * across the line, in the neighbourhood of the end of the line.
	 */
	private static XPoint2D searchFreePosition(XDimension2D qDim, XDimension2D rDim, XPoint2D quantifierPos,
			XPoint2D endpoint, double dirX, double dirY, Collection<XPoint2D> samples,
			Collection<RectangleArea> forbidden) {
		final double length = Math.hypot(dirX, dirY);
		final double tx = dirX / length;
		final double ty = dirY / length;

		final double qx = quantifierPos.getX() + qDim.getWidth() / 2;
		final double qy = quantifierPos.getY() + qDim.getHeight() / 2;
		final double along = (qx - endpoint.getX()) * tx + (qy - endpoint.getY()) * ty;
		final double idealX = 2 * (endpoint.getX() + tx * along) - qx;
		final double idealY = 2 * (endpoint.getY() + ty * along) - qy;

		XPoint2D best = null;
		double bestDistance = Double.MAX_VALUE;
		for (double x = endpoint.getX() - rDim.getWidth() - SEARCH_RANGE; x <= endpoint.getX()
				+ SEARCH_RANGE; x += SEARCH_STEP)
			for (double y = endpoint.getY() - rDim.getHeight() - SEARCH_RANGE; y <= endpoint.getY()
					+ SEARCH_RANGE; y += SEARCH_STEP) {
				// Sliding along the line is preferred to moving away from it
				final double deltaX = x + rDim.getWidth() / 2 - idealX;
				final double deltaY = y + rDim.getHeight() / 2 - idealY;
				final double distance = Math.abs(deltaX * tx + deltaY * ty) + 2 * Math.abs(-deltaX * ty + deltaY * tx);
				if (distance >= bestDistance)
					continue;

				final XPoint2D candidate = new XPoint2D(x, y);
				if (isFree(candidate, rDim, forbidden, samples)) {
					best = candidate;
					bestDistance = distance;
				}
			}
		return best;
	}

	private static boolean isFree(XPoint2D pos, XDimension2D dim, Collection<RectangleArea> forbidden,
			Collection<XPoint2D> samples) {
		final double x1 = pos.getX();
		final double y1 = pos.getY();
		final double x2 = x1 + dim.getWidth();
		final double y2 = y1 + dim.getHeight();
		for (RectangleArea area : forbidden)
			if (x1 < area.getMaxX() && x2 > area.getMinX() && y1 < area.getMaxY() && y2 > area.getMinY())
				return false;

		if (samples != null)
			for (XPoint2D pt : samples)
				if (pt.getX() >= x1 - GAP && pt.getX() <= x2 + GAP && pt.getY() >= y1 - GAP
						&& pt.getY() <= y2 + GAP)
					return false;

		return true;
	}

	/**
	 * The point of the line the closest to the endpoint, among the ones that are
	 * far enough from it to give a meaningful direction.
	 */
	private static XPoint2D getPointAwayFrom(XPoint2D endpoint, XPoint2D otherEndpoint, Collection<XPoint2D> samples) {
		XPoint2D result = otherEndpoint;
		double best = Double.MAX_VALUE;
		for (XPoint2D pt : samples) {
			final double distance = pt.distance(endpoint);
			if (distance >= MIN_DISTANCE_FOR_DIRECTION && distance < best) {
				best = distance;
				result = pt;
			}
		}
		return result;
	}

	private static Collection<XPoint2D> sampleSegment(XPoint2D p1, XPoint2D p2) {
		final List<XPoint2D> result = new ArrayList<>();
		final int steps = Math.max(1, (int) Math.ceil(p1.distance(p2) / 2));
		for (int i = 0; i <= steps; i++) {
			final double t = i / (double) steps;
			result.add(new XPoint2D(p1.getX() + (p2.getX() - p1.getX()) * t, p1.getY() + (p2.getY() - p1.getY()) * t));
		}
		return result;
	}

}
