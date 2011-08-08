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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.posimo.BezierUtils;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.posimo.Positionable;
import net.sourceforge.plantuml.svek.SvekUtils.PointListIterator;
import net.sourceforge.plantuml.ugraphic.UGraphic;
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

	private final TextBlock labelText;
	private final TextBlock startTailText;
	private final TextBlock endHeadText;
	private final IEntityImage note;

	private final int lineColor;
	private final int labelColor;
	private final int startTailColor;
	private final int endHeadColor;
	private final int noteColor;

	private Point2D.Double labelXY;
	private Point2D.Double startTailLabelXY;
	private Point2D.Double endHeadLabelXY;
	private Point2D.Double noteXY;

	private List<Point2D.Double> endHead;
	private List<Point2D.Double> startTail;

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
		this.labelColor = colorSequence.getValue();
		this.startTailColor = colorSequence.getValue();
		this.endHeadColor = colorSequence.getValue();
		this.noteColor = colorSequence.getValue();

		if (link.getLabel() == null) {
			labelText = null;
		} else {
			labelText = TextBlockUtils.create(StringUtils.getWithNewlines(link.getLabel()), labelFont,
					HorizontalAlignement.CENTER);
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

		if (link.getNote() == null) {
			note = null;
		} else {
			note = new EntityImageNoteLink(link.getNote(), skinParam);
		}

	}

	public void appendLine(StringBuilder sb) {
		sb.append(startUid);
		sb.append("->");
		sb.append(endUid);
		sb.append("[");
		String decoration = link.getType().getSpecificDecoration();
		if (decoration.endsWith(",") == false) {
			decoration += ",";
		}
		sb.append(decoration);

		if (link.getLength() != 1) {
			sb.append("minlen=" + (link.getLength() - 1));
			sb.append(",");
		}
		sb.append("color=\"" + StringUtils.getAsHtml(lineColor) + "\"");
		if (labelText != null) {
			sb.append(",");
			sb.append("label=<");
			appendTable(sb, labelText.calculateDimension(stringBounder), labelColor);
			sb.append(">");
		} else if (note != null) {
			sb.append(",");
			sb.append("label=<");
			appendTable(sb, note.getDimension(stringBounder), noteColor);
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
			return "{rank=same; " + startUid + "; " + endUid + "}";
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
		return startUid;
	}

	public final String getEndUid() {
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

		if (link.getType().getDecor2() != LinkDecor.NONE) {
			this.endHead = pointListIterator.next();
		}

		if (link.getType().getDecor1() != LinkDecor.NONE) {
			this.startTail = pointListIterator.next();
		}

		if (this.labelText != null) {
			this.labelXY = getXY(svg, this.labelColor, fullHeight);
		}

		if (this.startTailText != null) {
			this.startTailLabelXY = getXY(svg, this.startTailColor, fullHeight);
		}

		if (this.endHeadText != null) {
			this.endHeadLabelXY = getXY(svg, this.endHeadColor, fullHeight);
		}

		if (this.note != null) {
			this.noteXY = getXY(svg, this.noteColor, fullHeight);
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
			final UShape shape = new UPolygon(this.startTail);
			ug.getParam().setColor(color);
			if (this.link.getType().getDecor1().isFill()) {
				ug.getParam().setBackcolor(color);
			} else {
				ug.getParam().setBackcolor(null);
			}
			ug.draw(x, y, shape);
		}
		if (this.endHead != null) {
			final UShape shape = new UPolygon(this.endHead);
			ug.getParam().setColor(color);
			if (this.link.getType().getDecor2().isFill()) {
				ug.getParam().setBackcolor(color);
			} else {
				ug.getParam().setBackcolor(null);
			}
			ug.draw(x, y, shape);
		}
		if (this.labelText != null) {
			this.labelText.drawU(ug, x + this.labelXY.x, y + this.labelXY.y);
		}
		if (this.startTailText != null) {
			this.startTailText.drawU(ug, x + this.startTailLabelXY.x, y + this.startTailLabelXY.y);
		}
		if (this.endHeadText != null) {
			this.endHeadText.drawU(ug, x + this.endHeadLabelXY.x, y + this.endHeadLabelXY.y);
		}

		if (this.note != null) {
			this.note.drawU(ug, x + this.noteXY.x, y + this.noteXY.y);
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
		if (labelText != null) {
			strategy.eat(labelText.calculateDimension(stringBounder).getWidth());
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
		if (labelText != null) {
			strategy.eat(labelText.calculateDimension(stringBounder).getHeight());
		}
		if (startTailText != null) {
			strategy.eat(startTailText.calculateDimension(stringBounder).getHeight());
		}
		if (endHeadText != null) {
			strategy.eat(endHeadText.calculateDimension(stringBounder).getHeight());
		}
		return strategy.getResult() + getDecorDzeta();
	}

	private Positionable getStartTailPositionnable() {
		if (startTailText == null) {
			return null;
		}
		return new Positionable() {
			public Point2D getPosition() {
				return startTailLabelXY;
			}

			public Dimension2D getSize() {
				return startTailText.calculateDimension(stringBounder);
			}
		};
	}

	private Positionable getEndHeadPositionnable() {
		if (endHeadText == null) {
			return null;
		}
		return new Positionable() {
			public Point2D getPosition() {
				return endHeadLabelXY;
			}

			public Dimension2D getSize() {
				return endHeadText.calculateDimension(stringBounder);
			}
		};
	}

	public void manageCollision(List<Shape> allShapes) {
//		final Positionable start = getStartTailPositionnable();
//		if (start != null) {
//			for (Shape sh : allShapes) {
//				if (cut(start, sh)) {
//					avoid(startTailLabelXY, start, sh);
//				}
//			}
//		}
//
//		final Positionable end = getEndHeadPositionnable();
//		if (end != null) {
//			for (Shape sh : allShapes) {
//				if (cut(end, sh)) {
//					avoid(endHeadLabelXY, end, sh);
//				}
//			}
//		}

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

}
