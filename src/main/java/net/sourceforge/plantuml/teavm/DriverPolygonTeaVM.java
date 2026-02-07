/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 */
package net.sourceforge.plantuml.teavm;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UPolygon;

public class DriverPolygonTeaVM implements UDriver<UPolygon, SvgGraphicsTeaVM> {

	private final ClipContainer clipContainer;

	public DriverPolygonTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(UPolygon polygon, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		DriverRectangleTeaVM.applyFillColor(svg, mapper, param);
		DriverRectangleTeaVM.applyStrokeColor(svg, mapper, param);
		svg.setStrokeWidth(param.getStroke().getThickness());

		// Convert polygon points to array
		final double[] points = new double[polygon.getPoints().size() * 2];
		int i = 0;
		for (XPoint2D pt : polygon.getPoints()) {
			points[i++] = x + pt.getX();
			points[i++] = y + pt.getY();
		}

		svg.drawPolygon(points);
	}
}
