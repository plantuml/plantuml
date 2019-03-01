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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.graphic.HtmlColorAndStyle;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class WormTexted implements Iterable<Point2D.Double> {

	private final Worm worm;
	private TextBlock textBlock;

	public WormTexted() {
		this(new Worm());
	}

	private WormTexted(Worm worm) {
		this.worm = worm;
	}

	public Iterator<Point2D.Double> iterator() {
		return worm.iterator();
	}

	public void addPoint(double x, double y) {
		worm.addPoint(x, y);
	}

	public void drawInternalOneColor(UPolygon startDecoration, UGraphic ug, HtmlColorAndStyle color, double stroke,
			Direction emphasizeDirection, UPolygon endDecoration) {
		worm.drawInternalOneColor(startDecoration, ug, color, stroke, emphasizeDirection, endDecoration);
	}

	public Worm getWorm() {
		return worm;
	}

	public Point2D get(int i) {
		return worm.get(i);
	}

	public int size() {
		return worm.size();
	}

	public WormTexted merge(WormTexted other, MergeStrategy merge) {
		final Worm result = worm.merge(other.worm, merge);
		return new WormTexted(result);
	}

	public void addAll(WormTexted other) {
		this.worm.addAll(other.worm);

	}

	public void setLabel(TextBlock label) {
		if (textBlock != null) {
			throw new IllegalStateException();
		}
		this.textBlock = label;
	}

	public boolean isEmptyText(StringBounder stringBounder) {
		return TextBlockUtils.isEmpty(textBlock, stringBounder);
	}

	private Point2D getTextBlockPosition(StringBounder stringBounder) {
		final Point2D pt1 = get(0);
		final Point2D pt2 = get(1);
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		// if (worm.getDirectionsCode().startsWith("LD")) {
		// final double y = pt1.getY() - dim.getHeight();
		// return new Point2D.Double(Math.max(pt1.getX(), pt2.getX()) - dim.getWidth(), y);
		// }
		final double y = (pt1.getY() + pt2.getY()) / 2 - dim.getHeight() / 2;
		return new Point2D.Double(Math.max(pt1.getX(), pt2.getX()) + 4, y);
	}

	public double getMaxX(StringBounder stringBounder) {
		double result = -Double.MAX_VALUE;
		for (Point2D pt : this) {
			result = Math.max(result, pt.getX());
		}
		if (textBlock != null) {
			final Point2D position = getTextBlockPosition(stringBounder);
			final Dimension2D dim = textBlock.calculateDimension(stringBounder);
			result = Math.max(result, position.getX() + dim.getWidth());
		}
		return result;
	}

	void drawInternalLabel(UGraphic ug) {
		if (textBlock != null) {
			final Point2D position = getTextBlockPosition(ug.getStringBounder());
			textBlock.drawU(ug.apply(new UTranslate(position)));
		}
	}

	public void copyLabels(WormTexted other) {
		this.textBlock = other.textBlock;
	}

}
