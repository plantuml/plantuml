/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable3;
import net.sourceforge.plantuml.posimo.BezierUtils;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.posimo.Positionable;
import net.sourceforge.plantuml.posimo.PositionableUtils;
import net.sourceforge.plantuml.svek.SvekUtils.PointListIterator;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class Line {

	private final String ltail;
	private final String lhead;
	private final Link link;

	private DotPath dotPath;

	private final String startUid;
	private final String endUid;

	private final TextBlock startTailText;
	private final TextBlock endHeadText;
	private final TextBlock noteLabelText;

	private final int lineColor;
	private final int noteLabelColor;
	private final int startTailColor;
	private final int endHeadColor;

	private Positionable startTailLabelXY;
	private Positionable endHeadLabelXY;
	private Positionable noteLabelXY;

	private UDrawable3 endHead;
	private UDrawable3 startTail;

	private final StringBounder stringBounder;

	public Line(String startUid, String endUid, Link link, ColorSequence colorSequence, String ltail, String lhead,
			ISkinParam skinParam, StringBounder stringBounder, FontConfiguration labelFont) {
		if (startUid == null || endUid == null || link == null) {
			throw new IllegalArgumentException();
		}
		this.stringBounder = stringBounder;
		this.link = link;
		this.startUid = startUid;
		this.endUid = endUid;
		this.ltail = ltail;
		this.lhead = lhead;

		this.lineColor = colorSequence.getValue();
		this.noteLabelColor = colorSequence.getValue();
		this.startTailColor = colorSequence.getValue();
		this.endHeadColor = colorSequence.getValue();

		final TextBlock labelOnly;
		if (link.getLabel() == null) {
			labelOnly = null;
		} else {
			labelOnly = TextBlockUtils.create(StringUtils.getWithNewlines(link.getLabel()), labelFont,
					HorizontalAlignement.CENTER);
		}

		final TextBlock noteOnly;
		if (link.getNote() == null) {
			noteOnly = null;
		} else {
			noteOnly = TextBlockUtils.fromIEntityImage(new EntityImageNoteLink(link.getNote(), skinParam));
		}

		if (labelOnly != null && noteOnly != null) {
			if (link.getNotePosition() == Position.LEFT) {
				noteLabelText = TextBlockUtils.mergeLR(noteOnly, labelOnly);
			} else if (link.getNotePosition() == Position.RIGHT) {
				noteLabelText = TextBlockUtils.mergeLR(labelOnly, noteOnly);
			} else if (link.getNotePosition() == Position.TOP) {
				noteLabelText = TextBlockUtils.mergeTB(noteOnly, labelOnly);
			} else {
				noteLabelText = TextBlockUtils.mergeTB(labelOnly, noteOnly);
			}
		} else if (labelOnly != null) {
			noteLabelText = labelOnly;
		} else if (noteOnly != null) {
			noteLabelText = noteOnly;
		} else {
			noteLabelText = null;
		}

		if (link.getQualifier1() == null) {
			startTailText = null;
		} else {
			startTailText = TextBlockUtils.create(StringUtils.getWithNewlines(link.getQualifier1()), labelFont,
					HorizontalAlignement.CENTER);
		}

		if (link.getQualifier2() == null) {
			endHeadText = null;
		} else {
			endHeadText = TextBlockUtils.create(StringUtils.getWithNewlines(link.getQualifier2()), labelFont,
					HorizontalAlignement.CENTER);
		}

	}

	public void appendLine(StringBuilder sb) {
		sb.append(startUid);
		sb.append("->");
		sb.append(endUid);
		sb.append("[");
		String decoration = link.getType().getSpecificDecorationSvek();
		if (decoration.endsWith(",") == false) {
			decoration += ",";
		}
		sb.append(decoration);

		if (link.getLength() != 1) {
			sb.append("minlen=" + (link.getLength() - 1));
			sb.append(",");
		}
		sb.append("color=\"" + StringUtils.getAsHtml(lineColor) + "\"");
		if (noteLabelText != null) {
			sb.append(",");
			sb.append("label=<");
			appendTable(sb, noteLabelText.calculateDimension(stringBounder), noteLabelColor);
			sb.append(">");
		}

		if (startTailText != null) {
			sb.append(",");
			sb.append("taillabel=<");
			appendTable(sb, startTailText.calculateDimension(stringBounder), startTailColor);
			sb.append(">");
		}
		if (endHeadText != null) {
			sb.append(",");
			sb.append("headlabel=<");
			appendTable(sb, endHeadText.calculateDimension(stringBounder), endHeadColor);
			sb.append(">");
		}

		if (ltail != null) {
			sb.append(",");
			sb.append("ltail=");
			sb.append(ltail);
		}
		if (lhead != null) {
			sb.append(",");
			sb.append("lhead=");
			sb.append(lhead);
		}
		if (link.isInvis()) {
			sb.append(",");
			sb.append("style=invis");
		}
		sb.append("];");
		SvekUtils.println(sb);
	}

	public String rankSame() {
		if (link.getLength() == 1) {
			return "{rank=same; " + getStartUid() + "; " + getEndUid() + "}";
		}
		return null;
	}

	public static void appendTable(StringBuilder sb, Dimension2D dim, int col) {
		final int w = (int) dim.getWidth();
		final int h = (int) dim.getHeight();
		appendTable(sb, w, h, col);
	}

	public static void appendTable(StringBuilder sb, int w, int h, int col) {
		sb.append("<TABLE ");
		sb.append("BGCOLOR=\"" + StringUtils.getAsHtml(col) + "\" ");
		sb.append("FIXEDSIZE=\"TRUE\" WIDTH=\"" + w + "\" HEIGHT=\"" + h + "\">");
		sb.append("<TR");
		sb.append(">");
		sb.append("<TD");
		// sb.append(" FIXEDSIZE=\"TRUE\" WIDTH=\"" + 0 + "\" HEIGHT=\"" + 0 +
		// "\"");
		sb.append(">");
		sb.append("</TD>");
		sb.append("</TR>");
		sb.append("</TABLE>");
	}

	public final String getStartUid() {
		if (startUid.endsWith(":h")) {
			return startUid.substring(0, startUid.length() - 2);
		}
		return startUid;
	}

	public final String getEndUid() {
		if (endUid.endsWith(":h")) {
			return endUid.substring(0, endUid.length() - 2);
		}
		return endUid;
	}

	public void solveLine(final String svg, final int fullHeight) {
		if (this.link.isInvis()) {
			return;
		}

		int idx = svg.indexOf(SvekUtils.getStrokeString(this.lineColor));
		if (idx == -1) {
			throw new IllegalStateException();
		}
		idx = svg.indexOf("d=\"", idx);
		if (idx == -1) {
			throw new IllegalStateException();
		}
		final int end = svg.indexOf("\"", idx + 3);
		final String path = svg.substring(idx + 3, end);
		dotPath = new DotPath(path, fullHeight);

		final PointListIterator pointListIterator = new PointListIterator(svg.substring(end), fullHeight);

		if (link.getType().getDecor2() == LinkDecor.PLUS) {
			final List<Point2D.Double> points = pointListIterator.next();
			final Point2D p0 = points.get(0);
			final Point2D p1 = points.get(1);
			final Point2D p2 = points.get(2);
			final Point2D pc = new Point2D.Double((p0.getX() + p2.getX()) / 2, (p0.getY() + p2.getY()) / 2);
			this.endHead = new Plus(p1, pc);
		} else if (link.getType().getDecor2() != LinkDecor.NONE) {
			final UShape sh = new UPolygon(pointListIterator.next());
			this.endHead = new UDrawable3() {
				public void drawU(UGraphic ug, double x, double y) {
					ug.draw(x, y, sh);
				}
			};
		}

		if (link.getType().getDecor1() == LinkDecor.PLUS) {
			final List<Point2D.Double> points = pointListIterator.next();
			final Point2D p0 = points.get(0);
			final Point2D p1 = points.get(1);
			final Point2D p2 = points.get(2);
			final Point2D pc = new Point2D.Double((p0.getX() + p2.getX()) / 2, (p0.getY() + p2.getY()) / 2);
			this.startTail = new Plus(p1, pc);
		} else if (link.getType().getDecor1() != LinkDecor.NONE) {
			final UShape sh = new UPolygon(pointListIterator.next());
			this.startTail = new UDrawable3() {
				public void drawU(UGraphic ug, double x, double y) {
					ug.draw(x, y, sh);
				}
			};
		}

		if (this.noteLabelText != null) {
			this.noteLabelXY = TextBlockUtils.asPositionable(noteLabelText, stringBounder, getXY(svg,
					this.noteLabelColor, fullHeight));
		}

		if (this.startTailText != null) {
			this.startTailLabelXY = TextBlockUtils.asPositionable(startTailText, stringBounder, getXY(svg,
					this.startTailColor, fullHeight));
		}

		if (this.endHeadText != null) {
			this.endHeadLabelXY = TextBlockUtils.asPositionable(endHeadText, stringBounder, getXY(svg,
					this.endHeadColor, fullHeight));
		}

	}

	private Point2D.Double getXY(String svg, int color, int height) {
		final int idx = svg.indexOf(SvekUtils.getStrokeString(color));
		if (idx == -1) {
			throw new IllegalStateException();
		}
		return SvekUtils.getMinXY(SvekUtils.extractPointsList(svg, idx, height));

	}

	public void drawU(UGraphic ug, double x, double y, HtmlColor color) {
		if (link.isInvis()) {
			return;
		}
		if (this.link.getSpecificColor() != null) {
			color = this.link.getSpecificColor();
		}
		ug.getParam().setColor(color);
		ug.getParam().setBackcolor(null);
		ug.getParam().setStroke(link.getType().getStroke());
		ug.draw(x, y, dotPath);
		ug.getParam().setStroke(new UStroke());

		if (this.startTail != null) {
			ug.getParam().setColor(color);
			if (this.link.getType().getDecor1().isFill()) {
				ug.getParam().setBackcolor(color);
			} else {
				ug.getParam().setBackcolor(null);
			}
			this.startTail.drawU(ug, x, y);
		}
		if (this.endHead != null) {
			ug.getParam().setColor(color);
			if (this.link.getType().getDecor2().isFill()) {
				ug.getParam().setBackcolor(color);
			} else {
				ug.getParam().setBackcolor(null);
			}
			this.endHead.drawU(ug, x, y);
		}
		if (this.noteLabelText != null) {
			this.noteLabelText.drawU(ug, x + this.noteLabelXY.getPosition().getX(), y
					+ this.noteLabelXY.getPosition().getY());
		}
		if (this.startTailText != null) {
			this.startTailText.drawU(ug, x + this.startTailLabelXY.getPosition().getX(), y
					+ this.startTailLabelXY.getPosition().getY());
		}
		if (this.endHeadText != null) {
			this.endHeadText.drawU(ug, x + this.endHeadLabelXY.getPosition().getX(), y
					+ this.endHeadLabelXY.getPosition().getY());
		}
	}

	public boolean isInverted() {
		return link.isInverted();
	}

	private double getDecorDzeta() {
		final int size1 = link.getType().getDecor1().getSize();
		final int size2 = link.getType().getDecor2().getSize();
		return size1 + size2;
	}

	public double getHorizontalDzeta(StringBounder stringBounder) {
		if (startUid.equals(endUid)) {
			return getDecorDzeta();
		}
		final ArithmeticStrategy strategy;
		if (isHorizontal()) {
			strategy = new ArithmeticStrategySum();
		} else {
			return 0;
		}
		if (noteLabelText != null) {
			strategy.eat(noteLabelText.calculateDimension(stringBounder).getWidth());
		}
		if (startTailText != null) {
			strategy.eat(startTailText.calculateDimension(stringBounder).getWidth());
		}
		if (endHeadText != null) {
			strategy.eat(endHeadText.calculateDimension(stringBounder).getWidth());
		}
		return strategy.getResult() + getDecorDzeta();
	}

	private boolean isHorizontal() {
		return link.getLength() == 1;
	}

	public double getVerticalDzeta(StringBounder stringBounder) {
		if (startUid.equals(endUid)) {
			return getDecorDzeta();
		}
		final ArithmeticStrategy strategy;
		if (isHorizontal()) {
			return 0;
		} else {
			strategy = new ArithmeticStrategySum();
		}
		if (noteLabelText != null) {
			strategy.eat(noteLabelText.calculateDimension(stringBounder).getHeight());
		}
		if (startTailText != null) {
			strategy.eat(startTailText.calculateDimension(stringBounder).getHeight());
		}
		if (endHeadText != null) {
			strategy.eat(endHeadText.calculateDimension(stringBounder).getHeight());
		}
		return strategy.getResult() + getDecorDzeta();
	}

	// private Positionable getStartTailPositionnable() {
	// if (startTailText == null) {
	// return null;
	// }
	// return new Positionable() {
	// public Point2D getPosition() {
	// return startTailLabelXY.;
	// }
	//
	// public Dimension2D getSize() {
	// return startTailText.calculateDimension(stringBounder);
	// }
	// };
	// }
	//
	// private Positionable getEndHeadPositionnable() {
	// if (endHeadText == null) {
	// return null;
	// }
	// return new Positionable() {
	// public Point2D getPosition() {
	// return endHeadLabelXY;
	// }
	//
	// public Dimension2D getSize() {
	// return endHeadText.calculateDimension(stringBounder);
	// }
	// };
	// }

	public void manageCollision(List<Shape> allShapes) {

		for (Shape sh : allShapes) {
			final Positionable cl = PositionableUtils.addMargin(sh, 8, 8);
			if (startTailText != null && PositionableUtils.intersect(cl, startTailLabelXY)) {
				startTailLabelXY = PositionableUtils.moveAwayFrom(cl, startTailLabelXY);
			}
			if (endHeadText != null && PositionableUtils.intersect(cl, endHeadLabelXY)) {
				endHeadLabelXY = PositionableUtils.moveAwayFrom(cl, endHeadLabelXY);
			}
		}

		// final Positionable start = getStartTailPositionnable();
		// if (start != null) {
		// for (Shape sh : allShapes) {
		// if (cut(start, sh)) {
		// avoid(startTailLabelXY, start, sh);
		// }
		// }
		// }
		//
		// final Positionable end = getEndHeadPositionnable();
		// if (end != null) {
		// for (Shape sh : allShapes) {
		// if (cut(end, sh)) {
		// avoid(endHeadLabelXY, end, sh);
		// }
		// }
		// }

	}

	private void avoid(Point2D.Double move, Positionable pos, Shape sh) {
		final Oscillator oscillator = new Oscillator();
		final Point2D.Double orig = new Point2D.Double(move.x, move.y);
		while (cut(pos, sh)) {
			final Point2D.Double m = oscillator.nextPosition();
			move.setLocation(orig.x + m.x, orig.y + m.y);
		}
	}

	private boolean cut(Positionable pos, Shape sh) {
		return BezierUtils.intersect(pos, sh) || tooClose(pos);
	}

	private boolean tooClose(Positionable pos) {
		final double dist = dotPath.getMinDist(BezierUtils.getCenter(pos));
		final Dimension2D dim = pos.getSize();
		System.err.println("dist=" + dist);
		return dist < (dim.getWidth() / 2 + 2) || dist < (dim.getHeight() / 2 + 2);
	}

	static class Plus implements UDrawable3 {

		private final AffineTransform at;
		private final AffineTransform at2;
		private int radius;
		private final Point2D center;
		private final Point2D p1;
		private final Point2D p2;
		private Point2D p3;
		private Point2D p4;

		public Plus(Point2D p1, Point2D p2) {
			this.center = new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
			at = AffineTransform.getTranslateInstance(-center.getX(), -center.getY());
			at2 = AffineTransform.getTranslateInstance(center.getX(), center.getY());
			radius = (int) (p1.distance(p2) / 2);
			if (radius % 2 == 0) {
				radius--;
			}
			this.p1 = putOnCircle(p1);
			this.p2 = putOnCircle(p2);

			this.p3 = at.transform(this.p1, null);
			this.p3 = new Point2D.Double(p3.getY(), -p3.getX());
			this.p3 = at2.transform(p3, null);

			this.p4 = at.transform(this.p2, null);
			this.p4 = new Point2D.Double(p4.getY(), -p4.getX());
			this.p4 = at2.transform(p4, null);
		}

		private Point2D putOnCircle(Point2D p) {
			p = at.transform(p, null);
			final double coef = p.distance(new Point2D.Double()) / radius;
			p = new Point2D.Double(p.getX() / coef, p.getY() / coef);
			return at2.transform(p, null);
		}

		public void drawU(UGraphic ug, double x, double y) {
			final UShape circle = new UEllipse(radius * 2, radius * 2);
			ug.draw(x + center.getX() - radius, y + center.getY() - radius, circle);
			drawLine(ug, x, y, p1, p2);
			drawLine(ug, x, y, p3, p4);
		}

		static private void drawLine(UGraphic ug, double x, double y, Point2D p1, Point2D p2) {
			final double dx = p2.getX() - p1.getX();
			final double dy = p2.getY() - p1.getY();
			ug.draw(x + p1.getX(), y + p1.getY(), new ULine(dx, dy));

		}

	}

}
