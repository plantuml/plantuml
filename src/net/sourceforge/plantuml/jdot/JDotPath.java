/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.jdot;

import h.Agedge_s;
import h.Agedgeinfo_t;
import h.bezier;
import h.pointf;
import h.splines;
import h.textlabel_t;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import smetana.core.Macro;
import smetana.core.__ptr__;
import smetana.core.__struct__;

public class JDotPath implements UDrawable {

	private final Link link;
	private final Agedge_s edge;
	private final YMirror ymirror;
	private final CucaDiagram diagram;
	private final TextBlock label;
	private final Rose rose = new Rose();

	public JDotPath(Link link, Agedge_s edge, YMirror ymirror, CucaDiagram diagram, TextBlock label) {
		this.link = link;
		this.edge = edge;
		this.ymirror = ymirror;
		this.diagram = diagram;
		this.label = label;
	}

	private ColorParam getArrowColorParam() {
		if (diagram.getUmlDiagramType() == UmlDiagramType.CLASS) {
			return ColorParam.classArrow;
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			return ColorParam.objectArrow;
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.DESCRIPTION) {
			return ColorParam.usecaseArrow;
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return ColorParam.activityArrow;
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.STATE) {
			return ColorParam.stateArrow;
		}
		throw new IllegalStateException();
	}

	public void drawU(UGraphic ug) {

		HtmlColor color = rose.getHtmlColor(diagram.getSkinParam(), getArrowColorParam(), null);

		if (this.link.getColors() != null) {
			final HtmlColor newColor = this.link.getColors().getColor(ColorType.ARROW, ColorType.LINE);
			if (newColor != null) {
				color = newColor;
			}

		} else if (this.link.getSpecificColor() != null) {
			color = this.link.getSpecificColor();
		}

		DotPath dotPath = getDotPath(edge);
		if (ymirror != null) {
			dotPath = ymirror.getMirrored(dotPath);
		}

		ug.apply(new UChangeColor(color)).draw(dotPath);
		if (getLabelRectangleTranslate() != null) {
			label.drawU(ug.apply(getLabelRectangleTranslate()));
		}
		// printDebug(ug);

	}

	private void printDebug(UGraphic ug) {
		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLUE)).apply(new UChangeBackColor(HtmlColorUtils.BLUE));
		final splines splines = getSplines(edge);
		final bezier beziers = (bezier) splines.getPtr("list");
		for (int i = 0; i < beziers.getInt("size"); i++) {
			Point2D pt = getPoint(splines, i);
			if (ymirror != null) {
				pt = ymirror.getMirrored(pt);
			}
			ug.apply(new UTranslate(pt).compose(new UTranslate(-1, -1))).draw(new UEllipse(3, 3));
		}
		if (getLabelRectangleTranslate() != null && getLabelURectangle() != null) {
			ug = ug.apply(new UChangeColor(HtmlColorUtils.BLUE)).apply(new UChangeBackColor(null));
			ug.apply(getLabelRectangleTranslate()).draw(getLabelURectangle());
		}

	}

	private URectangle getLabelURectangle() {
		final Agedgeinfo_t data = (Agedgeinfo_t) Macro.AGDATA(edge).castTo(Agedgeinfo_t.class);
		textlabel_t label = (textlabel_t) data.getPtr("label");
		if (label == null) {
			return null;
		}
		final __struct__<pointf> dimen = label.getStruct("dimen");
		final __struct__<pointf> space = label.getStruct("space");
		final __struct__<pointf> pos = label.getStruct("pos");
		final double x = pos.getDouble("x");
		final double y = pos.getDouble("y");
		final double width = dimen.getDouble("x");
		final double height = dimen.getDouble("y");
		return new URectangle(width, height);
	}

	private UTranslate getLabelRectangleTranslate() {
		final Agedgeinfo_t data = (Agedgeinfo_t) Macro.AGDATA(edge).castTo(Agedgeinfo_t.class);
		textlabel_t label = (textlabel_t) data.getPtr("label");
		if (label == null) {
			return null;
		}
		final __struct__<pointf> dimen = label.getStruct("dimen");
		final __struct__<pointf> space = label.getStruct("space");
		final __struct__<pointf> pos = label.getStruct("pos");
		final double x = pos.getDouble("x");
		final double y = pos.getDouble("y");
		final double width = dimen.getDouble("x");
		final double height = dimen.getDouble("y");

		if (ymirror == null) {
			return new UTranslate(x - width / 2, y - height / 2);
		}
		return ymirror.getMirrored(new UTranslate(x - width / 2, y + height / 2));
	}

	public DotPath getDotPath(Agedge_s e) {
		final splines splines = getSplines(e);
		return getDotPath(splines);
	}

	private splines getSplines(Agedge_s e) {
		final Agedgeinfo_t data = (Agedgeinfo_t) Macro.AGDATA(e).castTo(Agedgeinfo_t.class);
		final splines splines = (splines) data.getPtr("spl");
		return splines;
	}

	private DotPath getDotPath(splines splines) {
		DotPath result = new DotPath();
		final bezier beziers = (bezier) splines.getPtr("list");
		final Point2D pt1 = getPoint(splines, 0);
		final Point2D pt2 = getPoint(splines, 1);
		final Point2D pt3 = getPoint(splines, 2);
		final Point2D pt4 = getPoint(splines, 3);
		result = result.addCurve(pt1, pt2, pt3, pt4);
		final int n = beziers.getInt("size");
		for (int i = 4; i < n; i += 3) {
			final Point2D ppt2 = getPoint(splines, i);
			final Point2D ppt3 = getPoint(splines, i + 1);
			final Point2D ppt4 = getPoint(splines, i + 2);
			result = result.addCurve(ppt2, ppt3, ppt4);
		}
		return result;
	}

	private Point2D getPoint(splines splines, int i) {
		final bezier beziers = (bezier) splines.getPtr("list");
		final __ptr__ pt = beziers.getPtr("list").plus(i).getPtr();
		return new Point2D.Double(pt.getDouble("x"), pt.getDouble("y"));
	}

}
