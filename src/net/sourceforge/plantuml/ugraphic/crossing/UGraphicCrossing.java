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
 * Original Author:  Adrian Vogt
 *
 */
package net.sourceforge.plantuml.ugraphic.crossing;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cute.Balloon;
import net.sourceforge.plantuml.cute.CrossingSegment;
import net.sourceforge.plantuml.geom.LineSegmentDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class UGraphicCrossing implements UGraphic {

	private final UGraphic ug;
	private final List<Pending> lines;
	private final UTranslate translate;
	
	static class Pending {
		final UGraphic ug;
		final LineSegmentDouble segment;
		final UTranslate translate;

		Pending(UGraphic ug, UTranslate translate, LineSegmentDouble segment) {
			this.ug = ug;
			this.segment = segment;
			this.translate = translate;
		}

		void drawNow(HColor color) {
			if (color == null) {
				segment.draw(ug);
			} else {
				segment.draw(ug.apply(color));
			}
		}

		List<Point2D> getCollisionsWith(List<Pending> others) {
			final List<Point2D> result = new ArrayList<Point2D>();
			for (Pending other : others) {
				if (isClose(segment.getP1(), other.segment.getP1()) || isClose(segment.getP1(), other.segment.getP2())
						|| isClose(segment.getP2(), other.segment.getP1())
						|| isClose(segment.getP2(), other.segment.getP2())) {
					continue;
				}
				final Point2D inter = segment.getSegIntersection(other.segment);
				if (inter != null) {
					result.add(inter);
				}
			}
			return result;
		}
	}

	public UGraphicCrossing(UGraphic ug) {
		this(ug, new UTranslate(), new ArrayList<Pending>());
	}

	private static boolean isClose(Point2D p1, Point2D p2) {
		return p1.distance(p2) < 0.1;
	}

	private UGraphicCrossing(UGraphic ug, UTranslate translate, List<Pending> lines) {
		this.ug = ug;
		this.translate = translate;
		this.lines = lines;
	}

	public StringBounder getStringBounder() {
		return ug.getStringBounder();
	}

	public UParam getParam() {
		return ug.getParam();
	}

	public void draw(UShape shape) {
		if (shape instanceof DotPath) {
			drawDotPath((DotPath) shape);
		} else {
			ug.draw(shape);
		}
	}

	private void drawDotPath(DotPath dotPath) {
		if (dotPath.isLine()) {
			for (LineSegmentDouble seg : dotPath.getLineSegments()) {
				lines.add(new Pending(ug.apply(translate.reverse()), translate, seg.translate(translate)));
			}
		} else {
			ug.draw(dotPath);
		}
	}

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new UGraphicCrossing(ug.apply(change), translate.compose((UTranslate) change), lines);
		} else {
			return new UGraphicCrossing(ug.apply(change), translate, lines);
		}
	}

	public ColorMapper getColorMapper() {
		return ug.getColorMapper();
	}

	public void startUrl(Url url) {
		ug.startUrl(url);
	}

	public void closeAction() {
		ug.closeAction();
	}

	public void flushUg() {
		final List<Pending> pendings = new ArrayList<Pending>();
		final List<Balloon> balloons = new ArrayList<Balloon>();
		for (Pending p : lines) {
			final List<Point2D> tmp = p.getCollisionsWith(lines);
			for (Point2D pt : tmp) {
				balloons.add(new Balloon(pt, 5));
			}
			// if (tmp.size() == 0) {
			// p.drawNow(null);
			// } else {
			// pendings.add(p);
			// }
		}
		for (Balloon b : balloons) {
			b.drawU(ug.apply(HColorUtils.GREEN.bg()).apply(HColorUtils.GREEN));
		}
		for (Pending p : lines) {
			for (Balloon b : balloons) {
				List<Point2D> pts = new CrossingSegment(b, p.segment).intersection();
				for (Point2D pt : pts) {
					final Balloon s2 = new Balloon(pt, 2);
					s2.drawU(ug.apply(HColorUtils.BLUE.bg()).apply(HColorUtils.BLUE));
				}
			}
		}
		ug.flushUg();
	}

	public boolean matchesProperty(String propertyName) {
		return ug.matchesProperty(propertyName);
	}

}
