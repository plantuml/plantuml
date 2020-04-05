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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorAndStyle;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.comp.PiecewiseAffineTransform;

public class Snake implements UShape {

	private final Worm worm = new Worm();
	private UPolygon startDecoration;
	private UPolygon endDecoration;
	private final Rainbow color;
	private TextBlock textBlock;
	private String textBlockPosition;
	private MergeStrategy mergeable = MergeStrategy.FULL;
	private Direction emphasizeDirection;
	private final HorizontalAlignment horizontalAlignment;

	public final void setIgnoreForCompression() {
		this.worm.setIgnoreForCompression();
	}

	public Snake transformX(PiecewiseAffineTransform compressionTransform) {
		final Snake result = new Snake(startDecoration, horizontalAlignment, color, endDecoration);
		result.textBlock = this.textBlock;
		result.mergeable = this.mergeable;
		result.emphasizeDirection = this.emphasizeDirection;
		for (Point2D.Double pt : worm) {
			final double x = compressionTransform.transform(pt.x);
			final double y = pt.y;
			result.addPoint(x, y);
		}
		return result;
	}

	public void removeEndDecoration() {
		this.endDecoration = null;
	}

	public Snake(HorizontalAlignment horizontalAlignment, Rainbow color, UPolygon endDecoration) {
		this(null, horizontalAlignment, color, endDecoration);
	}

	public Snake(UPolygon startDecoration, HorizontalAlignment horizontalAlignment, Rainbow color,
			UPolygon endDecoration) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		if (color.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.startDecoration = startDecoration;
		this.endDecoration = endDecoration;
		this.color = color;
		this.horizontalAlignment = horizontalAlignment;
	}

	public Snake(HorizontalAlignment horizontalAlignment, Rainbow color) {
		this(null, horizontalAlignment, color, null);
	}

	public void setLabel(TextBlock label, String position) {
		this.textBlock = label;
		this.textBlockPosition = position;
	}

	public void setLabel(TextBlock label) {
		this.textBlock = label;
	}

	public Snake move(double dx, double dy) {
		final Snake result = new Snake(startDecoration, horizontalAlignment, color, endDecoration);
		for (Point2D pt : worm) {
			result.addPoint(pt.getX() + dx, pt.getY() + dy);
		}
		result.textBlock = this.textBlock;
		result.mergeable = this.mergeable;
		result.emphasizeDirection = this.emphasizeDirection;
		return result;
	}

	public Snake translate(UTranslate translate) {
		return move(translate.getDx(), translate.getDy());
	}

	@Override
	public String toString() {
		return worm.toString();
	}

	public void addPoint(double x, double y) {
		worm.addPoint(x, y);
	}

	public void addPoint(Point2D p) {
		addPoint(p.getX(), p.getY());
	}

	public void drawInternal(UGraphic ug) {
		if (color.size() > 1) {
			drawRainbow(ug);
		} else {
			worm.drawInternalOneColor(startDecoration, ug, color.getColors().get(0), 1.5, emphasizeDirection,
					endDecoration);
			drawInternalLabel(ug);
		}

	}

	private void drawRainbow(UGraphic ug) {
		List<HtmlColorAndStyle> colors = color.getColors();
		final int colorArrowSeparationSpace = color.getColorArrowSeparationSpace();
		final double move = 2 + colorArrowSeparationSpace;
		final WormMutation mutation = WormMutation.create(worm, move);
		if (mutation.isDxNegative()) {
			colors = new ArrayList<HtmlColorAndStyle>(colors);
			Collections.reverse(colors);
		}
		final double globalMove = -1.0 * (colors.size() - 1) / 2.0;
		Worm current = worm.moveFirstPoint(mutation.getFirst().multiplyBy(globalMove));
		if (mutation.size() > 2) {
			current = current.moveLastPoint(mutation.getLast().multiplyBy(globalMove));
		}
		for (int i = 0; i < colors.size(); i++) {
			double stroke = 1.5;
			if (colorArrowSeparationSpace == 0) {
				stroke = i == colors.size() - 1 ? 2.0 : 3.0;
			}
			current.drawInternalOneColor(startDecoration, ug, colors.get(i), stroke, emphasizeDirection, endDecoration);
			current = mutation.mute(current);
		}
		final UTranslate textTranslate = mutation.getTextTranslate(colors.size());
		drawInternalLabel(ug.apply(textTranslate));
	}

	private void drawInternalLabel(UGraphic ug) {
		if (textBlock != null) {
			final Point2D position = getTextBlockPosition(ug.getStringBounder());
			textBlock.drawU(ug.apply(new UTranslate(position)));
		}
	}

	public double getMaxX(StringBounder stringBounder) {
		double result = -Double.MAX_VALUE;
		for (Point2D pt : worm) {
			result = Math.max(result, pt.getX());
		}
		if (textBlock != null) {
			final Point2D position = getTextBlockPosition(stringBounder);
			final Dimension2D dim = textBlock.calculateDimension(stringBounder);
			result = Math.max(result, position.getX() + dim.getWidth());
		}
		return result;
	}

	private Point2D getTextBlockPosition(StringBounder stringBounder) {
		final Point2D pt1 = worm.get(0);
		final Point2D pt2 = worm.get(1);
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		double x = Math.max(pt1.getX(), pt2.getX()) + 4;
		final boolean zigzag = worm.getDirectionsCode().startsWith("DLD") || worm.getDirectionsCode().startsWith("DRD");
		double y = (pt1.getY() + pt2.getY()) / 2 - dim.getHeight() / 2;
		if ("bottom".equalsIgnoreCase(textBlockPosition)) {
			x = worm.getLast().getX();
		} else if (horizontalAlignment == HorizontalAlignment.CENTER && zigzag) {
			final Point2D pt3 = worm.get(2);
			x = (pt2.getX() + pt3.getX()) / 2 - dim.getWidth() / 2;
		} else if (horizontalAlignment == HorizontalAlignment.RIGHT && zigzag) {
			x = Math.max(pt1.getX(), pt2.getX()) - dim.getWidth() - 4;
		} else if (worm.getDirectionsCode().equals("RD")) {
			x = Math.max(pt1.getX(), pt2.getX());
			y = (pt1.getY() + worm.get(2).getY()) / 2 - dim.getHeight() / 2;
		} else if (worm.getDirectionsCode().equals("LD")) {
			x = Math.min(pt1.getX(), pt2.getX());
			y = (pt1.getY() + worm.get(2).getY()) / 2 - dim.getHeight() / 2;
		}
		return new Point2D.Double(x, y);
	}

	public List<Line2D> getHorizontalLines() {
		final List<Line2D> result = new ArrayList<Line2D>();
		for (int i = 0; i < worm.size() - 1; i++) {
			final Point2D pt1 = worm.get(i);
			final Point2D pt2 = worm.get(i + 1);
			if (pt1.getY() == pt2.getY()) {
				final Line2D line = new Line2D.Double(pt1, pt2);
				result.add(line);
			}
		}
		return result;

	}

	private Point2D getFirst() {
		return worm.get(0);
	}

	public Point2D getLast() {
		return worm.get(worm.size() - 1);
	}

	static boolean same(Point2D pt1, Point2D pt2) {
		return pt1.distance(pt2) < 0.001;
	}

	public Snake merge(Snake other, StringBounder stringBounder) {
		final MergeStrategy strategy = this.mergeable.max(other.mergeable);
		if (strategy == MergeStrategy.NONE) {
			return null;
		}
		final boolean emptyOther = TextBlockUtils.isEmpty(other.textBlock, stringBounder);
		// final boolean emptyThis = TextBlockUtils.isEmpty(this.textBlock, stringBounder);
		if (emptyOther == false /* || emptyThis == false */) {
			// System.err.println("merge other.textBlock="+other.textBlock+" "+other.textBlock.calculateDimension(TextBlockUtils.getDummyStringBounder()));
			return null;
		}
		if (same(this.getLast(), other.getFirst())) {
			final UPolygon oneOf = other.endDecoration == null ? endDecoration : other.endDecoration;
			if (this.startDecoration != null || other.startDecoration != null) {
				throw new UnsupportedOperationException("Not yet coded: to be done");
			}
			final Snake result = new Snake(null, horizontalAlignment, color, oneOf);
			// result.textBlock = oneOf(this.textBlock, other.textBlock, stringBounder);
			result.emphasizeDirection = emphasizeDirection == null ? other.emphasizeDirection : emphasizeDirection;
			result.worm.addAll(this.worm.merge(other.worm, strategy));
			result.mergeable = strategy;
			return result;
		}
		if (same(this.getFirst(), other.getLast())) {
			return other.merge(this, stringBounder);
		}
		return null;
	}

	public boolean touches(Snake other) {
		if (other.mergeable != MergeStrategy.FULL) {
			return false;
		}
		if (other.worm.isPureHorizontal()) {
			return false;
		}
		return same(this.getLast(), other.getFirst());
	}

	public void goUnmergeable(MergeStrategy strategy) {
		this.mergeable = strategy;
	}

	public void emphasizeDirection(Direction direction) {
		this.emphasizeDirection = direction;
	}

	public boolean doesHorizontalCross(MinMax minMax) {
		return worm.doesHorizontalCross(minMax);
	}

}
