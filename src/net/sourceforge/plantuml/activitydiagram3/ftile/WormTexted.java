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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Iterator;

import net.sourceforge.plantuml.decoration.HtmlColorAndStyle;
import net.sourceforge.plantuml.klimt.Arrows;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.utils.Direction;

public class WormTexted implements Iterable<XPoint2D> {
	// ::remove folder when __HAXE__

	private final Worm worm;
	private TextBlock textBlock;

	private WormTexted(Style style, Arrows arrows) {
		this(new Worm(style, arrows));
	}

	private WormTexted(Worm worm) {
		this.worm = worm;
	}

	public Iterator<XPoint2D> iterator() {
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

	public XPoint2D get(int i) {
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

	private XPoint2D getTextBlockPosition(StringBounder stringBounder) {
		final XPoint2D pt1 = get(0);
		final XPoint2D pt2 = get(1);
		final XDimension2D dim = textBlock.calculateDimension(stringBounder);
		// if (worm.getDirectionsCode().startsWith("LD")) {
		// final double y = pt1.getY() - dim.getHeight();
		// return new XPoint2D(Math.max(pt1.getX(), pt2.getX()) - dim.getWidth(), y);
		// }
		final double y = (pt1.getY() + pt2.getY()) / 2 - dim.getHeight() / 2;
		return new XPoint2D(Math.max(pt1.getX(), pt2.getX()) + 4, y);
	}

	public double getMaxX(StringBounder stringBounder) {
		double result = -Double.MAX_VALUE;
		for (XPoint2D pt : this) {
			result = Math.max(result, pt.getX());
		}
		if (textBlock != null) {
			final XPoint2D position = getTextBlockPosition(stringBounder);
			final XDimension2D dim = textBlock.calculateDimension(stringBounder);
			result = Math.max(result, position.getX() + dim.getWidth());
		}
		return result;
	}

	void drawInternalLabel(UGraphic ug) {
		if (textBlock != null) {
			final XPoint2D position = getTextBlockPosition(ug.getStringBounder());
			textBlock.drawU(ug.apply(UTranslate.point(position)));
		}
	}

	public void copyLabels(WormTexted other) {
		this.textBlock = other.textBlock;
	}

}
